import Foundation
import SwiftUI
import Combine
import UIKit

enum Dest: String, CaseIterable {
    case DASHBOARD, PRACTICE, MOCK, SITUATIONAL, LEARN, FLASHCARDS, FORMULAS, REFERENCE, ANALYTICS, MISTAKES, PLANNER, DATA,
         COACH, MENU, GUIDE, FITNESS, REPORTS, SESSION, SETTINGS, BOOK, SEARCH
    var title: String {
        switch self {
        case .DASHBOARD: "PMP Prep"; case .PRACTICE: "Practice"; case .MOCK: "Mock Exam"; case .SITUATIONAL: "Situational Trainer"
        case .LEARN: "Learn"; case .FLASHCARDS: "Flashcards"; case .FORMULAS: "Formula Lab"; case .REFERENCE: "ECO & Reference"
        case .ANALYTICS: "Analytics"; case .MISTAKES: "Mistake Journal"; case .PLANNER: "Study Planner"; case .DATA: "Banks & Backup"
        case .COACH: "Coach and notes"; case .MENU: "Menu"; case .GUIDE: "How it works"; case .FITNESS: "Exam fitness"
        case .REPORTS: "Weekly reports"; case .SESSION: "Session"; case .SETTINGS: "Study settings"; case .BOOK: "Ready to book?"; case .SEARCH: "Search"
        }
    }
}

/** What to practise. nil means "any". */
struct PracticeFilter: Equatable {
    enum Which: String, CaseIterable {
        case ALL, NEW, MISSED, FLAGGED
        var title: String { switch self { case .ALL: "All questions"; case .NEW: "Not answered yet"; case .MISSED: "Wrong last time"; case .FLAGGED: "Flagged" } }
    }
    var domain: Domain? = nil
    var task: String? = nil
    var pack: String? = nil
    var type: QType? = nil
    var approach: Approach? = nil
    var which: Which = .ALL
    var count: Int = 20
}

/** Reading comfort: text size, line spacing and the reading guide, for questions and lessons. */
struct ReadingPrefs: Equatable {
    var scale: Double = 1
    var roomy = false
    var guide = false
    func size(_ base: CGFloat) -> CGFloat { base * scale }
    /** Extra space between lines. */
    func spacing(_ base: CGFloat) -> CGFloat { (roomy ? 0.45 : 0.25) * base * scale }
    static let sizes: [(Double, String)] = [(1, "Normal"), (1.15, "Large"), (1.3, "Larger")]
}

/** A session being played. Kept while the app runs, so closing it and coming back resumes at the same step. */
final class PathRun: ObservableObject {
    let session: Session
    let startedAt: Millis
    let mocksAtStart: Int
    @Published var step = 0
    /** The once-a-day "how do you feel" check-in has been answered or skipped. */
    @Published var checkedIn = false
    var practices: [QuizSession] = []
    @Published var current: QuizSession? = nil
    /** Missed questions due for review, after the warm-up cards. */
    @Published var review: QuizSession? = nil
    init(session: Session, startedAt: Millis, mocksAtStart: Int) { self.session = session; self.startedAt = startedAt; self.mocksAtStart = mocksAtStart }
}

/**
 * The app's state and actions, ported from the Android app's view model. Everything the screens
 * show is computed from the progress store and the content; nothing here is a second copy of it.
 */
@MainActor
final class AppModel: ObservableObject {
    let repo = ContentRepository()
    let store: ProgressStore
    @Published private(set) var ready = false
    @Published private(set) var loadError: String? = nil
    /** Bumped when packs change, so screens that list questions recompute. */
    @Published private(set) var contentVersion = 0
    /** Mirrors the store's revision so views observing the model redraw on every change. */
    @Published private(set) var revision = 0

    private(set) var readiness: Readiness!
    private(set) var srs: Srs!
    private var bag = Set<AnyCancellable>()
    private let defaults = UserDefaults.standard
    private static var docs: URL { FileManager.default.urls(for: .applicationSupportDirectory, in: .userDomainMask)[0] }

    init() {
        store = ProgressStore(dir: Self.docs)
        askConfidence = defaults.object(forKey: "confidence") as? Bool ?? true
        reading = ReadingPrefs(scale: defaults.object(forKey: "scale") as? Double ?? 1, roomy: defaults.bool(forKey: "roomy"), guide: defaults.bool(forKey: "guide"))
        geminiKey = KeyStore.read() ?? ""
        geminiModels = Self.savedLineup()
        store.$revision.receive(on: RunLoop.main).sink { [weak self] r in
            self?.revision = r
            self?.scheduleDerived()
        }.store(in: &bag)
        load()
    }

    private func load() {
        do {
            try repo.load()
            readiness = Readiness(r: repo.extras.readiness, exam: repo.extras.exam, tasks: repo.extras.tasks, domainWeights: repo.extras.domainWeights)
            srs = Srs(c: repo.extras.srs)
            restoreMock()
            ready = true
            scheduleDerived()
            checkModels()
        } catch {
            loadError = error.localizedDescription
        }
    }

    /** A second after progress settles: the weekly report, the widget and the reminders. */
    private var derivedTask: Task<Void, Never>? = nil
    private func scheduleDerived() {
        guard ready else { return }
        derivedTask?.cancel()
        derivedTask = Task { [weak self] in
            try? await Task.sleep(nanoseconds: 1_000_000_000)
            guard let self, !Task.isCancelled else { return }
            self.publishWeekly()
            self.publishWidget()
            self.publishReminders()
        }
    }

    /* ── study settings ── */

    /** Ask "how sure?" before checking a practice answer. */
    @Published private(set) var askConfidence: Bool
    @Published private(set) var reading: ReadingPrefs
    func changeAskConfidence(_ on: Bool) { askConfidence = on; defaults.set(on, forKey: "confidence") }
    func changeReading(_ r: ReadingPrefs) {
        reading = r
        defaults.set(r.scale, forKey: "scale"); defaults.set(r.roomy, forKey: "roomy"); defaults.set(r.guide, forKey: "guide")
    }
    func setRestDays(_ on: Bool) { var p = store.plan; p.restDays = on; store.setPlan(p) }

    /* ── navigation ── */

    @Published private(set) var screen: Dest = .DASHBOARD
    private var backStack: [Dest] = []

    func navigate(_ d: Dest) {
        if d == screen { return }
        backStack.append(screen)
        screen = d
    }

    /** Back to the path, forgetting the trail. */
    func home() { backStack = []; screen = .DASHBOARD }

    var canGoBack: Bool { screen != .DASHBOARD }

    /** Returns false when there is nowhere to go back to. */
    @discardableResult
    func back() -> Bool {
        // Inner screens first: a running drill, an open lesson, an open deck.
        switch screen {
        case .PRACTICE: if practice != nil { practice = nil; return true }
        case .LEARN: if openLesson != nil { openLesson = nil; return true }
        case .FLASHCARDS: if cardQueue != nil { cardQueue = nil; return true }
        case .MOCK: if mockReview != nil { mockReview = nil; return true }
        default: break
        }
        guard let last = backStack.popLast() else {
            if screen != .DASHBOARD { screen = .DASHBOARD; return true }
            return false
        }
        screen = last
        return true
    }

    /* ── practice ── */

    @Published var filter = PracticeFilter()
    /** The practice set on screen. Replacing or clearing one part-way records it as left (Exam fitness). */
    @Published var practice: QuizSession? = nil {
        didSet {
            if let old = oldValue, old !== practice, !old.finished, !old.reviewOnly, !old.diagnostic, old.checkedCount > 0 {
                store.recordSitting(SittingEvent(key: old.key, kind: "practice", at: Time.now(), planned: old.questions.count, answered: old.checkedCount, finished: false))
            }
        }
    }

    /** A practice set reached its end. */
    func finishQuiz(_ s: QuizSession) {
        s.finished = true
        if !s.reviewOnly && !s.diagnostic {
            store.recordSitting(SittingEvent(key: s.key, kind: "practice", at: Time.now(), planned: s.questions.count, answered: s.checkedCount, finished: true))
        }
        objectWillChange.send()
    }

    func pool() -> [Question] { repo.pool() }

    func matching(_ f: PracticeFilter) -> [Question] {
        pool().filter { q in
            (f.domain == nil || q.domain == f.domain) && (f.task == nil || q.ecoTask == f.task) && (f.pack == nil || q.packId == f.pack) &&
            (f.type == nil || q.base == f.type) && (f.approach == nil || q.approach == f.approach) && {
                switch f.which {
                case .ALL: return true
                case .NEW: return store.last[q.id] == nil
                case .MISSED: return store.last[q.id]?.correct == false
                case .FLAGGED: return store.flags.contains(q.id)
                }
            }()
        }
    }

    func startPractice(_ f: PracticeFilter? = nil, title: String = "Practice") {
        let qs = Array(matching(f ?? filter).shuffled().prefix((f ?? filter).count))
        if qs.isEmpty { return }
        practice = QuizSession(title: title, questions: qs)
        if screen != .PRACTICE { navigate(.PRACTICE) }
    }

    func startPracticeWith(_ qs: [Question], title: String) {
        if qs.isEmpty { return }
        practice = QuizSession(title: title, questions: qs.shuffled())
        if screen != .PRACTICE { navigate(.PRACTICE) }
    }

    func answer(_ s: QuizSession, _ q: Question, _ a: Answer) {
        if s.isChecked(q) { return }
        let now = Time.now()
        s.touch(now)
        if s.firstTapAt == 0 { s.firstTapAt = now; s.awayAtFirstTap = s.awayMs }
        if let up = changeDirection(q, s.answers[q.id], a) { if up { s.wrongToRight += 1 } else { s.rightToWrong += 1 } }
        s.answers[q.id] = a
        // Choosing a crossed-out option brings it back.
        if let st = s.struck[q.id], !st.isDisjoint(with: a.choices) { s.struck[q.id] = st.subtracting(a.choices) }
    }

    /** Crosses an option out, or restores it. Crossing out the chosen option unchooses it. */
    func toggleStrike(_ s: QuizSession, _ q: Question, _ optionId: String) {
        if s.isChecked(q) { return }
        s.touch(Time.now())
        let st = s.struck[q.id] ?? []
        s.struck[q.id] = st.contains(optionId) ? st.subtracting([optionId]) : st.union([optionId])
        if var a = s.answers[q.id], a.choices.contains(optionId), !st.contains(optionId) { a.choices.remove(optionId); s.answers[q.id] = a }
    }

    /** For pick-type questions: true if a change went wrong → right, false if right → wrong, nil otherwise. */
    private func changeDirection(_ q: Question, _ before: Answer?, _ after: Answer) -> Bool? {
        guard [.single, .multi, .hotspot].contains(q.base), let before, Scoring.isAnswered(q, before), Scoring.isAnswered(q, after) else { return nil }
        let was = Scoring.score(q, before).correct, now = Scoring.score(q, after).correct
        return was == now ? nil : now
    }

    /** Reading drill: you've said what the question is asking; the options appear. */
    func showOptions(_ s: QuizSession) {
        let now = Time.now()
        s.touch(now)
        if s.firstTapAt == 0 { s.firstTapAt = now; s.awayAtFirstTap = s.awayMs }
        s.optionsShown = true
    }

    /** confidence is Attempt.SURE, THINK or GUESS, or 0 when not asked. */
    func check(_ s: QuizSession, confidence: Int = 0) {
        let q = s.current
        if s.isChecked(q) || !Scoring.isAnswered(q, s.answers[q.id]) { return }
        let r = Scoring.score(q, s.answers[q.id])
        if confidence > 0 { s.confidence[q.id] = confidence }
        s.results[q.id] = r
        let now = Time.now()
        s.touch(now)
        s.totalRightToWrong += s.rightToWrong
        s.totalWrongToRight += s.wrongToRight
        store.record(attempt(q, r.credit, r.correct, now - s.shownAt - s.awayMs, s.source,
            firstTapMs: s.firstTapAt > 0 ? s.firstTapAt - s.shownAt - s.awayAtFirstTap : 0,
            idleMs: s.maxIdle, rightToWrong: s.rightToWrong, wrongToRight: s.wrongToRight,
            words: wordsOf(q, withOptions: !s.readingDrill), timed: s.timed,
            confidence: confidence, eliminated: s.struck[q.id]?.count ?? 0))
    }

    func moveTo(_ s: QuizSession, _ i: Int) {
        s.index = min(max(i, 0), s.questions.count - 1)
        s.shownAt = Time.now()
        s.resetTracking(s.shownAt)
    }

    private func attempt(_ q: Question, _ credit: Double, _ correct: Bool, _ ms: Int64, _ source: String,
                         firstTapMs: Int64 = 0, idleMs: Int64 = 0, rightToWrong: Int = 0, wrongToRight: Int = 0,
                         words: Int? = nil, timed: Bool = false, confidence: Int = 0, eliminated: Int = 0) -> Attempt {
        Attempt(qid: q.id, ecoTask: q.ecoTask, domain: q.domain, approach: q.approach, type: q.type, credit: credit, correct: correct,
                ms: max(0, ms), at: Time.now(), source: source, firstTapMs: max(0, firstTapMs), idleMs: max(0, idleMs),
                rightToWrong: rightToWrong, wrongToRight: wrongToRight, words: words ?? wordsOf(q), timed: timed,
                confidence: confidence, eliminated: eliminated)
    }

    /** Accuracy with and without crossing options out first; nil until both have 10. */
    func eliminationEffect() -> (Double, Double)? {
        let pick = store.attempts.filter { ($0.type.base == .single || $0.type.base == .multi) && $0.source != Readiness.SOURCE_LESSON }.suffix(400)
        let with = pick.filter { $0.eliminated > 0 }, without = pick.filter { $0.eliminated == 0 }
        if with.count < 10 || without.count < 10 { return nil }
        return (Double(with.filter(\.correct).count) / Double(with.count), Double(without.filter(\.correct).count) / Double(without.count))
    }

    /** Words to read: the stem, plus the options unless they're hidden (reading drill). */
    private func wordsOf(_ q: Question, withOptions: Bool = true) -> Int {
        func count(_ s: String) -> Int { s.split { !$0.isLetter && !$0.isNumber && $0 != "_" }.count }
        return count(q.stem) + (withOptions ? q.options.reduce(0) { $0 + count($1.text) } : 0)
    }

    /** No touch for this long on a practice question brings up the gentle "take a breath" prompt. */
    func freezePromptMs() -> Int64 {
        let times = store.attempts.suffix(100).filter { $0.ms > 500 && !$0.timed }.map(\.ms).sorted()
        let median = times.isEmpty ? 76_000 : times[times.count / 2]
        return max(150_000, 3 * median)
    }

    /* ── review of missed questions ── */

    private func dueReviews(_ now: Millis) -> [Question] {
        let enabled = Dictionary(pool().map { ($0.id, $0) }, uniquingKeysWith: { a, _ in a })
        return QuestionReview.due(store.attempts, now).compactMap { enabled[$0.qid] }
    }
    func reviewDueCount(_ now: Millis = Time.now()) -> Int { dueReviews(now).count }
    private func warmUpReviews(_ now: Millis) -> [Question] { Array(dueReviews(now).prefix(QuestionReview.allowance(store.attempts, now))) }

    func startReview() {
        let qs = dueReviews(Time.now())
        if qs.isEmpty { return }
        practice = QuizSession(title: "Missed questions, back for review", questions: qs, source: QuestionReview.SOURCE)
        if screen != .PRACTICE { navigate(.PRACTICE) }
    }

    /* ── mock exam ── */

    @Published var mock: MockSession? = nil
    @Published var lastMock: MockResult? = nil
    /** The finished sitting, kept for its review. */
    @Published var finishedMock: MockSession? = nil
    @Published var mockReview: QuizSession? = nil
    private var timer: Timer? = nil
    private var lastTick = Time.now()
    private var sinceSave: Int64 = 0
    private var mockFile: URL { Self.docs.appendingPathComponent("exam.json") }

    /** Draws a mock weighted to the domain split, unseen questions first. */
    func buildMock(_ n: Int) -> [Question] {
        let pool = pool()
        var out: [Question] = [], spare: [Question] = []
        for d in Domain.allCases {
            let l = pool.filter { $0.domain == d }.shuffled()
            let ordered = l.filter { store.last[$0.id] == nil } + l.filter { store.last[$0.id] != nil }
            let want = Int((Double(n) * (repo.extras.domainWeights[d] ?? 0)).rounded())
            out += ordered.prefix(want)
            spare += ordered.dropFirst(want)
        }
        out += spare.shuffled().prefix(max(0, n - out.count))
        return Array(out.prefix(n)).shuffled()
    }

    func startMock(_ n: Int) {
        let qs = buildMock(n)
        if qs.isEmpty { return }
        let layout = repo.extras.exam
        let full = n >= layout.totalItems
        let minutes = max(5, Int((Double(layout.minutes) * Double(n) / Double(layout.totalItems)).rounded()))
        mock = MockSession(title: full ? "Full mock exam" : "Mock exam · \(n) questions", questions: qs,
                           order: Dictionary(uniqueKeysWithValues: qs.map { ($0.id, shuffledOptions($0)) }),
                           durationMs: Int64(minutes) * 60_000,
                           breakAfter: full ? layout.breakAfter.filter { $0 >= 1 && $0 < qs.count } : [],
                           breakMs: Int64(layout.breakMinutes) * 60_000)
        lastMock = nil
        finishedMock = nil
        saveMock()
        startTimer()
    }

    private func startTimer() {
        timer?.invalidate()
        lastTick = Time.now()
        timer = Timer.scheduledTimer(withTimeInterval: 1, repeats: true) { [weak self] _ in
            Task { @MainActor in self?.tick() }
        }
    }

    private func tick() {
        guard let m = mock else { timer?.invalidate(); return }
        let now = Time.now()
        let dt = now - lastTick
        lastTick = now
        // The clock only runs while the exam is on screen; leaving it pauses the sitting.
        if screen != .MOCK || !appVisible { return }
        if m.onBreak {
            m.breakRemainingMs -= dt
            if m.breakRemainingMs <= 0 { endBreak() }
        } else {
            m.remainingMs -= dt
            m.spentMs[m.current.id, default: 0] += dt
            if m.remainingMs <= 0 { m.remainingMs = 0; submitMock(); return }
        }
        sinceSave += dt
        if sinceSave >= 15_000 { saveMock(); sinceSave = 0 }
    }

    private var awaySince: Millis = 0
    /** Set by the app. The exam clock stops while the app is in the background, and practice timings leave that time out. */
    var appVisible = true {
        didSet {
            let now = Time.now()
            if !appVisible && oldValue { awaySince = now; foregroundSince.map { noteForeground(from: $0, to: now) }; foregroundSince = nil }
            if appVisible && !oldValue {
                if awaySince > 0 {
                    let away = now - awaySince
                    for s in [practice, run?.current, run?.review].compactMap({ $0 }) { s.awayMs += away; s.awayTotal += away }
                    awaySince = 0
                }
                foregroundSince = now
                lastTick = now
                appOpened()
            }
        }
    }
    private var foregroundSince: Millis? = Time.now()

    func clearScorecard() { lastMock = nil; finishedMock = nil }

    func mockAnswer(_ q: Question, _ a: Answer) {
        guard let m = mock, let i = m.questions.firstIndex(of: q), m.inSection.contains(i) else { return }
        if m.firstTapMs[q.id] == nil { m.firstTapMs[q.id] = max(1, m.spentMs[q.id] ?? 0) }
        if let up = changeDirection(q, m.answers[q.id], a) {
            if up { m.wrongToRight[q.id, default: 0] += 1 } else { m.rightToWrong[q.id, default: 0] += 1 }
        }
        m.answers[q.id] = a
        if let st = m.struck[q.id], !st.isDisjoint(with: a.choices) { m.struck[q.id] = st.subtracting(a.choices) }
        saveMock()
    }

    func mockStrike(_ q: Question, _ optionId: String) {
        guard let m = mock, let i = m.questions.firstIndex(of: q), m.inSection.contains(i) else { return }
        let st = m.struck[q.id] ?? []
        m.struck[q.id] = st.contains(optionId) ? st.subtracting([optionId]) : st.union([optionId])
        if var a = m.answers[q.id], a.choices.contains(optionId), !st.contains(optionId) { a.choices.remove(optionId); m.answers[q.id] = a }
        saveMock()
    }

    func mockMarks(_ q: Question, _ words: Set<Int>) { mock?.marks[q.id] = words; saveMock() }

    func mockGo(_ i: Int) {
        guard let m = mock else { return }
        m.index = min(max(i, m.inSection.lowerBound), m.inSection.upperBound - 1)
    }

    func toggleMark(_ q: Question) { guard let m = mock else { return }; m.marked[q.id] = !(m.marked[q.id] ?? false) }

    /** Closes the current section. Offers the break; the earlier section can't be reopened after it. */
    func endSection(takeBreak: Bool) {
        guard let m = mock else { return }
        if m.section >= m.sectionCount - 1 { submitMock(); return }
        if takeBreak { m.onBreak = true; m.breakRemainingMs = m.breakMs } else { nextSection() }
        saveMock()
    }

    func endBreak() {
        guard let m = mock else { return }
        m.onBreak = false
        nextSection()
        saveMock()
    }

    private func nextSection() {
        guard let m = mock else { return }
        m.section = min(m.section + 1, m.sectionCount - 1)
        m.index = m.inSection.lowerBound
    }

    func submitMock() {
        guard let m = mock else { return }
        timer?.invalidate()
        m.finished = true
        let now = Time.now()
        var right = 0
        var dom: [Domain: [Int]] = [:]
        let perQ = m.questions.isEmpty ? 0 : (m.durationMs - m.remainingMs) / Int64(m.questions.count)
        for q in m.questions {
            let a = m.answers[q.id]
            let r = Scoring.score(q, a)
            if r.correct { right += 1 }
            var e = dom[q.domain] ?? [0, 0]
            e[1] += 1
            if r.correct { e[0] += 1 }
            dom[q.domain] = e
            // Only answered items feed history; a blank at the bell says nothing about the question.
            if Scoring.isAnswered(q, a) {
                store.record(attempt(q, r.credit, r.correct, m.spentMs[q.id] ?? perQ, Readiness.SOURCE_EXAM,
                                     firstTapMs: m.firstTapMs[q.id] ?? 0, rightToWrong: m.rightToWrong[q.id] ?? 0, wrongToRight: m.wrongToRight[q.id] ?? 0,
                                     timed: true, eliminated: m.struck[q.id]?.count ?? 0))
            }
        }
        let result = MockResult(at: now, title: m.title, n: m.questions.count, right: right, usedMs: m.durationMs - max(0, m.remainingMs), domains: dom)
        store.recordMock(result)
        store.recordSitting(SittingEvent(key: "mock:\(m.startedAt)", kind: "mock", at: now, planned: m.questions.count, answered: m.answeredCount, finished: true))
        lastMock = result
        finishedMock = m
        mock = nil
        try? FileManager.default.removeItem(at: mockFile)
    }

    func abandonMock() {
        if let m = mock, m.answeredCount > 0 {
            store.recordSitting(SittingEvent(key: "mock:\(m.startedAt)", kind: "mock", at: Time.now(), planned: m.questions.count, answered: m.answeredCount, finished: false))
        }
        timer?.invalidate()
        mock = nil
        try? FileManager.default.removeItem(at: mockFile)
    }

    func reviewMock(onlyWrong: Bool) {
        guard let m = finishedMock else { return }
        let qs = onlyWrong ? m.questions.filter { !Scoring.score($0, m.answers[$0.id]).correct } : m.questions
        if qs.isEmpty { return }
        mockReview = QuizSession(title: "Review", questions: qs, reviewOnly: true, givenAnswers: m.answers, givenOrder: m.order)
    }

    func saveMock() {
        guard let m = mock else { return }
        let json = JWrite.string(m.toJson())
        let file = mockFile
        DispatchQueue.global(qos: .utility).async { try? json.write(to: file, atomically: true, encoding: .utf8) }
    }

    private func restoreMock() {
        guard let text = try? String(contentsOf: mockFile, encoding: .utf8) else { return }
        guard let o = try? JObj.parse(text), let m = MockSession.fromJson(o, byId: repo.byId) else { try? FileManager.default.removeItem(at: mockFile); return }
        mock = m
        startTimer()
    }

    /* ── situational trainer ── */

    @Published var scenarioDomain: Domain? = nil
    @Published var scenarioIndex = 0
    @Published var scenarioPick: String? = nil
    func scenarios() -> [Scenario] { repo.extras.scenarios.filter { scenarioDomain == nil || $0.domain == scenarioDomain } }
    func submitScenario(_ s: Scenario) {
        guard let pick = scenarioPick, let opt = s.options.first(where: { $0.id == pick }) else { return }
        store.recordScenario(ScenarioRun(id: s.id, picked: pick, rank: opt.rank, at: Time.now()))
    }

    /* ── learn ── */

    @Published var openLesson: String? = nil
    func recordLessonCheck(_ q: Question, _ a: Answer) {
        let r = Scoring.score(q, a)
        // Lesson checks are learning, not evidence: they are kept out of readiness.
        store.record(attempt(q, r.credit, r.correct, 0, Readiness.SOURCE_LESSON))
    }

    /* ── flashcards ── */

    @Published var cardQueue: [Flashcard]? = nil
    @Published var cardIndex = 0
    @Published var cardFlipped = false
    @Published var newLeeches: [String] = []

    func cardState(_ id: String) -> CardState { store.cards[id] ?? srs.fresh() }

    /** The built-in decks, plus "My cards" once you've made one. */
    func decks() -> [Deck] {
        repo.extras.decks + (store.myCards.isEmpty ? [] : [Deck(id: ProgressStore.MY_DECK, label: "My cards", ecoTask: nil, summary: "Cards you made from your own mistakes.")])
    }
    func allCards() -> [Flashcard] { repo.extras.flashcards + store.myCards }

    /** A card drafted from a missed question: the situation in one line, then the right move and why. */
    func cardDraft(_ q: Question) -> (String, String) {
        let first = q.stem.components(separatedBy: CharacterSet(charactersIn: ".?!")).first.map { s -> String in
            let t = s.trimmingCharacters(in: .whitespacesAndNewlines)
            let end = q.stem.dropFirst(t.count).first.map(String.init) ?? ""
            return t + ([".", "?", "!"].contains(end) ? end : "")
        } ?? ""
        let front = first.count > 160 ? String(first.prefix(157)).trimmingCharacters(in: .whitespaces) + "…" : first
        let right: String
        switch q.base {
        case .single, .multi: right = q.options.filter(\.correct).map(\.text).joined(separator: "; ")
        case .hotspot: right = q.regions.filter(\.correct).map(\.label).joined(separator: "; ")
        case .matching: right = q.prompts.compactMap { p in q.options.first { $0.match == p.id }.map { "\(p.text) → \($0.text)" } }.joined(separator: "; ")
        case .fillblank: right = q.blanks.map { "\($0.label) \(Scoring.blankAnswer($0))" }.joined(separator: "; ")
        default: right = ""
        }
        let back = [right.isEmpty ? nil : "Right move: \(right).", q.explanation.isEmpty ? nil : q.explanation, q.mindset.isEmpty ? nil : q.mindset]
            .compactMap { $0 }.joined(separator: "\n\n")
        return (front, back)
    }

    func makeCard(_ q: Question, front: String, back: String) {
        let f = front.trimmingCharacters(in: .whitespacesAndNewlines), b = back.trimmingCharacters(in: .whitespacesAndNewlines)
        if f.isEmpty || b.isEmpty { return }
        store.addMyCard(Flashcard(id: "my:\(q.id)", deck: ProgressStore.MY_DECK, ecoTask: q.ecoTask, front: f, back: b, note: ""))
    }

    func deleteMyCard(_ id: String) {
        store.removeMyCard(id)
        cardQueue = cardQueue?.filter { $0.id != id }
        cardFlipped = false
    }

    struct DeckCounts { let total: Int; let due: Int; let new: Int; let leeches: Int }

    func deckCounts(_ deck: String?) -> DeckCounts {
        let now = Time.now()
        let cards = allCards().filter { deck == nil || $0.deck == deck }
        var due = 0, new = 0, leech = 0
        for c in cards {
            let s = store.cards[c.id]
            if s == nil || s!.isNew { new += 1 } else if s!.due <= now { due += 1 }
            if let s, srs.isLeech(s) { leech += 1 }
        }
        return DeckCounts(total: cards.count, due: due, new: new, leeches: leech)
    }

    private func newIntroducedToday() -> Int {
        let today = Time.startOfDay(Time.now())
        return store.cards.values.filter { $0.first >= today }.count
    }

    func startDeck(_ deck: String?) {
        let now = Time.now()
        let cards = allCards().filter { deck == nil || $0.deck == deck }
        let due = cards.filter { c in store.cards[c.id].map { !$0.isNew && $0.due <= now } ?? false }.sorted { (store.cards[$0.id]?.due ?? 0) < (store.cards[$1.id]?.due ?? 0) }
        let allowance = max(0, repo.extras.srs.newPerDay - newIntroducedToday())
        let fresh = cards.filter { c in store.cards[c.id]?.isNew ?? true }.prefix(allowance)
        cardQueue = due + fresh
        cardIndex = 0; cardFlipped = false; newLeeches = []
    }

    /** Practise a whole deck regardless of schedule; grades still update it. */
    func cramDeck(_ deck: String?) {
        cardQueue = allCards().filter { deck == nil || $0.deck == deck }.shuffled()
        cardIndex = 0; cardFlipped = false; newLeeches = []
    }

    func grade(_ g: Grade) {
        guard let q = cardQueue, cardIndex < q.count else { return }
        let card = q[cardIndex]
        let before = cardState(card.id)
        let after = srs.review(before, g, Time.now())
        store.setCard(card.id, after)
        if srs.isLeech(after) && !srs.isLeech(before) { newLeeches.append(card.front) }
        // A failed card comes back before the session ends.
        if g == .again { cardQueue = q + [card] }
        cardIndex += 1
        cardFlipped = false
    }

    /* ── guided path ── */

    /** Rebuilt from scratch on every call: the plan is a view of the current state, never a stored calendar. */
    func plan(_ now: Millis = Time.now()) -> Plan? {
        let p = store.plan
        guard let date = p.examDate, ready else { return nil }
        var leeches: [String: Int] = [:]
        for c in allCards() { if let t = c.ecoTask, let s = store.cards[c.id], srs.isLeech(s) { leeches[t, default: 0] += 1 } }
        let f = fitness(now)
        return PlanEngine.build(PlanInput(x: repo.extras, attempts: store.attempts, lessonsDone: store.lessonsDone, results: store.sessionResults,
            mocks: store.mocks, diagnostic: store.diagnostic, settings: PlanSettings(examDate: date, hoursPerWeek: p.hoursPerWeek, studyDays: p.studyDays),
            now: now, mistakeTags: store.mistakeTags.mapValues(\.cause), leeches: leeches,
            ladderNext: f.ladderNext, readingSlow: (f.wpm ?? 999) < ExamFitness.TARGET_WPM * 0.9))
    }

    /* ── exam fitness, booking and weekly reports ── */

    func fitness(_ now: Millis = Time.now()) -> FitnessReport {
        ExamFitness.report(FitnessInput(attempts: store.attempts, sittings: store.sittings, checkIns: store.checkIns, results: store.sessionResults,
                                        studyDays: store.plan.studyDays, hoursPerWeek: store.plan.hoursPerWeek, now: now))
    }
    func recordCheckIn(_ kind: String, _ value: Int, items: [Int] = []) { store.recordCheckIn(CheckIn(at: Time.now(), kind: kind, value: value, items: items)) }
    func booking(_ now: Millis = Time.now()) -> BookingReport {
        Booking.check(readiness: readiness.report(store, now).score, fitness: fitness(now).score, mocks: store.mocks,
                      fullItems: repo.extras.exam.totalItems, weekly: store.weekly, examDate: store.plan.examDate)
    }
    /** The weekly five-statement check is offered once a week, once you've started studying. */
    func weeklyCheckDue(_ now: Millis = Time.now()) -> Bool {
        !store.attempts.isEmpty && !store.checkIns.contains { $0.kind == "weekly" && now - $0.at < 6 * Time.dayMs }
    }

    private func publishWeekly() {
        let now = Time.now()
        let tasks = PlanEngine.taskStates(repo.extras, store.attempts, now)
        store.putWeekly(WeeklyReports.build(now: now, readiness: readiness.report(store, now), tasksTouched: tasks.filter { $0.answers > 0 }.count,
            mastered: tasks.filter { $0.mastery == .MASTERED }.count, attempts: store.attempts, results: store.sessionResults,
            mocks: store.mocks, fitness: fitness(now), tags: Array(store.mistakeTags.values)))
    }

    /* ── sessions on the path ── */

    @Published var run: PathRun? = nil

    func startSession(_ s: Session) {
        if run?.session.key != s.key {
            let r = PathRun(session: s, startedAt: Time.now(), mocksAtStart: store.mocks.count)
            let today = Time.startOfDay(Time.now())
            r.checkedIn = store.checkIns.contains { $0.kind == "pre" && Time.startOfDay($0.at) == today }
            run = r
        }
        navigate(.SESSION)
    }

    /** "Warm-up cards, a lesson and 15 questions" */
    private func describeSteps(_ s: Session) -> String {
        let parts: [String] = s.steps.compactMap {
            switch $0.kind {
            case .CARDS: "warm-up cards"; case .LESSON: "a lesson"; case .PRACTICE: "\($0.count) questions"
            case .SCENARIOS: "\($0.count) scenarios"; case .MOCK: "a full mock exam"; case .REFLECT: "a quick reflect"; case .DEBRIEF: nil
            }
        }
        let text = parts.count > 1 ? parts.dropLast().joined(separator: ", ") + " and " + parts.last! : (parts.first ?? "")
        return text.prefix(1).uppercased() + text.dropFirst()
    }

    /** A 15-minute version of a session for a short day: warm-up cards and 10 questions on the same ground. */
    func shortVersion(_ s: Session) -> Session {
        var c = s
        c.key = "short:" + s.key; c.title = "Short day: " + s.title; c.minutes = 15
        c.why = "A quick version so the day still counts."
        c.steps = [Step(kind: .CARDS), Step(kind: .PRACTICE, count: 10), Step(kind: .DEBRIEF)]
        return c
    }

    /** Questions for a practice step: the session's task or domain, least-recently-right first. */
    func questionsFor(_ s: Session, _ st: Step) -> [Question] {
        var qs = pool()
        if let t = s.taskId { qs = qs.filter { $0.ecoTask == t } }
        else if let d = s.domain { qs = qs.filter { $0.domain == d } }
        else if s.kind == .REENTRY {
            var recent: [String] = []
            for a in store.attempts.sorted(by: { $0.at > $1.at }) where !recent.contains(a.ecoTask) { recent.append(a.ecoTask); if recent.count == 3 { break } }
            if !recent.isEmpty { qs = qs.filter { recent.contains($0.ecoTask) } }
        }
        if st.easy { let e = qs.filter { $0.difficulty <= 3 }; if !e.isEmpty { qs = e } }
        return QuestionPicker.order(qs, lastCorrect: { self.store.last[$0]?.correct }, weak: QuestionPicker.weakApproach(store.attempts), count: st.count)
    }

    /** Called when a step is shown, so the step has what it needs. */
    func prepareStep(_ r: PathRun) {
        guard r.step < r.session.steps.count else { return }
        let st = r.session.steps[r.step]
        switch st.kind {
        case .CARDS:
            if r.review != nil { return }
            let now = Time.now()
            let q = repo.extras.flashcards.filter { c in store.cards[c.id].map { !$0.isNew && $0.due <= now } ?? false }
                .sorted { (store.cards[$0.id]?.due ?? 0) < (store.cards[$1.id]?.due ?? 0) }.prefix(15)
            cardQueue = Array(q); cardIndex = 0; cardFlipped = false; newLeeches = []
            if q.isEmpty { cardsDone(r) }
        case .PRACTICE:
            if r.current == nil {
                let qs = questionsFor(r.session, st)
                if qs.isEmpty { nextStep(r) }
                else { let s = QuizSession(title: r.session.title, questions: qs, timed: st.timed, readingDrill: st.reading); r.current = s; r.practices.append(s) }
            }
        default: break
        }
    }

    /** Warm-up cards are done: due reviews of missed questions follow, if any. */
    func cardsDone(_ r: PathRun) {
        cardQueue = nil
        let qs = warmUpReviews(Time.now())
        if qs.isEmpty { nextStep(r) }
        else { r.review = QuizSession(title: "Missed questions, back for review", questions: qs, source: QuestionReview.SOURCE) }
    }

    func nextStep(_ r: PathRun) {
        if r.step < r.session.steps.count {
            let st = r.session.steps[r.step]
            if st.kind == .LESSON, let l = r.session.lessonId { store.setLessonDone(l, true) }
            if st.kind == .CARDS { cardQueue = nil }
        }
        r.current = nil
        r.review = nil
        r.step = min(r.step + 1, r.session.steps.count)
    }

    /** Practice score for the session, or 1.0 when it was reading only. Mocks score as the sitting did. */
    func sessionScore(_ r: PathRun) -> Double {
        if r.session.steps.contains(where: { $0.kind == .MOCK }) { return store.mocks.dropFirst(r.mocksAtStart).last?.pct ?? 0 }
        let checked = r.practices.flatMap { p in p.questions.compactMap { p.results[$0.id] } }
        return checked.isEmpty ? 1 : checked.reduce(0) { $0 + $1.credit } / Double(checked.count)
    }

    private func pathQuestions(_ r: PathRun) -> Int { r.session.steps.filter { $0.kind == .PRACTICE }.reduce(0) { $0 + $1.count } }
    private func pathAnswered(_ r: PathRun) -> Int { r.practices.reduce(0) { $0 + $1.checkedCount } }

    func finishSession(_ r: PathRun) {
        store.recordSitting(SittingEvent(key: r.session.key, kind: "path", at: Time.now(), planned: pathQuestions(r), answered: pathAnswered(r), finished: true))
        let minutes = min(max(Int((Time.now() - r.startedAt) / 60_000), 5), max(r.session.minutes, 5))
        store.recordSession(SessionResult(key: r.session.key, kind: r.session.kind, taskId: r.session.taskId, domain: r.session.domain,
                                          score: sessionScore(r), minutes: minutes, at: Time.now()))
        run = nil
        cardQueue = nil
        home()
    }

    /** Leaves the player; the run is kept so Continue resumes it. */
    func closeSession() {
        if let r = run, pathAnswered(r) > 0 {
            store.recordSitting(SittingEvent(key: r.session.key, kind: "path", at: Time.now(), planned: pathQuestions(r), answered: pathAnswered(r), finished: false))
        }
        cardQueue = nil
        back()
    }

    func finishOnboarding() { var p = store.plan; p.onboarded = true; store.setPlan(p) }

    /* ── diagnostic ── */

    /** Twenty-five questions split 8 / 10 / 7 across the difficulty range, from the core pack. */
    func diagnosticSet() -> [Question] {
        let core = repo.packs.first { $0.id == "core" }?.questions ?? []
        let src = core.isEmpty ? pool() : core
        let want: [(Domain, Int)] = [(.people, 8), (.process, 10), (.business, 7)]
        var out: [Question] = []
        for (d, n) in want {
            var byDiff = Dictionary(grouping: src.filter { $0.domain == d }.shuffled(), by: \.difficulty)
            let levels = byDiff.keys.sorted()
            var i = 0
            while out.filter({ $0.domain == d }).count < n && byDiff.values.contains(where: { !$0.isEmpty }) {
                let lv = levels[i % levels.count]
                if var l = byDiff[lv], !l.isEmpty { out.append(l.removeFirst()); byDiff[lv] = l }
                i += 1
            }
        }
        return out.shuffled()
    }

    func startDiagnostic() {
        let qs = diagnosticSet()
        if qs.count < 5 { return }
        practice = QuizSession(title: "Diagnostic", questions: qs, diagnostic: true)
        if screen != .PRACTICE { navigate(.PRACTICE) }
    }

    func finishDiagnostic(_ s: QuizSession) {
        var dom: [Domain: [Int]] = [:]
        var right = 0
        for q in s.questions {
            let a = s.answers[q.id]
            let r = Scoring.score(q, a)
            s.results[q.id] = r
            if r.correct { right += 1 }
            var e = dom[q.domain] ?? [0, 0]; e[1] += 1; if r.correct { e[0] += 1 }; dom[q.domain] = e
            if Scoring.isAnswered(q, a) { store.record(attempt(q, r.credit, r.correct, 0, "diagnostic")) }
        }
        store.recordDiagnostic(DiagnosticResult(at: Time.now(), n: s.questions.count, right: right, domains: dom))
        s.index = 0
        s.finished = true
        objectWillChange.send()
    }

    /* ── coach ── */

    func coach() -> Coach { Coach(x: repo.extras, pool: pool(), store: store, readiness: readiness) }

    /** Where a coach line or step sends you. */
    func coachGo(_ action: CoachAction, _ arg: String?) {
        switch action {
        case .PLANNER: navigate(.PLANNER)
        case .DIAGNOSTIC: startDiagnostic()
        case .PRACTICE: startPractice(PracticeFilter(count: 10), title: "Mixed practice")
        case .TASK: if let arg { startPractice(PracticeFilter(task: arg, count: 10), title: "Practice: \(repo.task(arg)?.label ?? "")") } else { navigate(.PRACTICE) }
        case .MOCK: navigate(.MOCK)
        case .LEARN: openLesson = arg; navigate(.LEARN)
        case .CARDS: navigate(.FLASHCARDS)
        case .SCENARIOS: navigate(.SITUATIONAL)
        case .JOURNAL: navigate(.MISTAKES)
        case .FORMULAS: navigate(.FORMULAS)
        case .ANALYTICS: navigate(.ANALYTICS)
        }
    }

    func snoozeCoach(_ id: String, days: Int) { store.snooze(id, until: Time.now() + Millis(days) * Time.dayMs) }

    /* ── Gemini: the key lives in the Keychain, never in a backup ── */

    @Published private(set) var geminiKey: String
    /** The Flash models the coach uses, newest first, refreshed from Google once a week. */
    @Published private(set) var geminiModels: [Gemini.Model]
    /** The model that last answered in this app session: messages keep going to it until it fails. */
    private var sessionModel: String? = nil
    @Published private(set) var aiBusy = false
    @Published var aiError: String? = nil
    /** Text waiting in the coach's input, e.g. a question sent from practice. */
    @Published var coachDraft = ""
    @Published var coachTab = 0
    private static let lineupMaxAge: Int64 = 7 * 24 * 60 * 60 * 1000

    func useKey(_ key: String) {
        let k = key.trimmingCharacters(in: .whitespacesAndNewlines)
        if k.isEmpty || aiBusy { return }
        aiBusy = true
        aiError = nil
        Task {
            do {
                try await updateModels(k)
                geminiKey = k
                KeyStore.write(k)
            } catch { aiError = error.localizedDescription }
            aiBusy = false
        }
    }

    /** The weekly check: when the line-up is a week old (or missing), asks Google which models are current. */
    func checkModels() {
        if geminiKey.isEmpty || !lineupStale() { return }
        Task { try? await updateModels(geminiKey) }
    }

    private func lineupStale() -> Bool { geminiModels.isEmpty || Time.now() - Int64(defaults.double(forKey: "lineupAt")) > Self.lineupMaxAge }

    private func updateModels(_ key: String) async throws {
        let lineup = Gemini.lineup(try await Gemini.listModels(key))
        if lineup.isEmpty {
            throw Gemini.GeminiError(message: "This key works, but Google offers it no Gemini \(Gemini.BASE_VERSION) Flash or newer, and the coach doesn't use older models.")
        }
        geminiModels = lineup
        if !lineup.contains(where: { $0.name == sessionModel }) { sessionModel = nil }
        if let d = try? JSONEncoder().encode(lineup) { defaults.set(d, forKey: "lineup") }
        defaults.set(Double(Time.now()), forKey: "lineupAt")
    }

    private static func savedLineup() -> [Gemini.Model] {
        guard let d = UserDefaults.standard.data(forKey: "lineup") else { return [] }
        return (try? JSONDecoder().decode([Gemini.Model].self, from: d)) ?? []
    }

    /**
     * Sends one request down the line-up: from the model that answered last this session (or the newest),
     * each error moves it to the next model down; the first that answers is used from then on. A refused key
     * or no connection stops at once; when every model fails, the error says whether the key's limit is used up.
     */
    private func callGemini(_ system: String, _ turns: [ChatTurn]) async throws -> String {
        let key = geminiKey
        if lineupStale() { try? await updateModels(key) }
        let names = geminiModels.map(\.name)
        if names.isEmpty { throw Gemini.GeminiError(message: "Couldn't get the list of Gemini models from Google. Check your connection and try again.") }
        let start = sessionModel.flatMap { names.firstIndex(of: $0) } ?? 0
        var last: Gemini.GeminiError? = nil
        var limited = false
        for model in names[start...] {
            do {
                let reply = try await Gemini.generate(key, model, system, turns)
                sessionModel = model
                return reply
            } catch let e as Gemini.GeminiError {
                if !Gemini.tryNextModel(e) { throw e }
                last = e
                if e.status == 429 { limited = true }
            }
        }
        throw Gemini.GeminiError(message: limited ? "Your Gemini key has reached its limit on every model the coach uses. \(last?.message ?? "")"
                                                  : "None of the Gemini models answered. Google said: \(last?.message ?? "")", status: last?.status ?? 0)
    }

    func forgetKey() { geminiKey = ""; sessionModel = nil; KeyStore.delete() }

    /** Sends a message with a fresh snapshot of the person's progress. */
    func ask(_ text: String) {
        let t = text.trimmingCharacters(in: .whitespacesAndNewlines)
        if t.isEmpty || aiBusy || geminiKey.isEmpty { return }
        store.addChat(ChatTurn(role: "user", text: t, at: Time.now()))
        aiBusy = true
        aiError = nil
        let system = Coach.SYSTEM + "\n\nLearner progress snapshot, computed by the app just now:\n" + coach().snapshot()
        let turns = Array(store.chat.suffix(30))
        Task {
            do {
                let reply = try await callGemini(system, turns)
                store.addChat(ChatTurn(role: "model", text: reply, at: Time.now()))
            } catch { aiError = error.localizedDescription }
            aiBusy = false
        }
    }

    /** Explain-it-back: what Gemini made of your explanation. Not kept in the chat. */
    @Published private(set) var explainReply: String? = nil
    @Published private(set) var explainBusy = false
    @Published private(set) var explainError: String? = nil
    func clearExplain() { explainReply = nil; explainError = nil }

    func explainBack(topic: String, text: String) {
        let t = text.trimmingCharacters(in: .whitespacesAndNewlines)
        if t.isEmpty || explainBusy || geminiKey.isEmpty { return }
        explainBusy = true; explainError = nil; explainReply = nil
        let turns = [ChatTurn(role: "user", text: "What I was studying:\n\(topic)\n\nMy explanation in my own words:\n\(t)", at: Time.now())]
        Task {
            do { explainReply = try await callGemini(Coach.EXPLAIN_SYSTEM, turns) } catch { explainError = error.localizedDescription }
            explainBusy = false
        }
    }

    /** Opens the coach's chat with a question from practice ready to send. */
    func askAbout(_ q: Question, _ s: QuizSession) {
        let opts = s.order[q.id] ?? q.options
        let letters = Array("ABCDEFGH")
        var sb = "Help me with this question:\n\n\(q.stem)\n"
        for (i, o) in opts.enumerated() { sb += "\n\(letters[i % 8])) \(o.text)" }
        for p in q.prompts { sb += "\n• \(p.text)" }
        for r in q.regions { sb += "\n\(r.n)) \(r.label)" }
        for b in q.blanks { sb += "\n• \(b.label)" }
        if let a = s.answers[q.id], !a.choices.isEmpty, !opts.isEmpty {
            sb += "\n\nI chose " + a.choices.map { id in opts.firstIndex { $0.id == id }.map { String(letters[$0]) } ?? id }.joined(separator: ", ") + "."
        }
        sb += s.isChecked(q) ? " I've seen the answer; explain why it is right and the others are not." : "\n\nDon't give me the answer yet: give me a hint about how to think about it."
        coachDraft = sb
        coachTab = 1
        navigate(.COACH)
    }

    /* ── banks and backup ── */

    func setPackEnabled(_ p: QuestionPack, _ on: Bool) { repo.setEnabled(p, on); contentVersion += 1 }
    func deletePack(_ p: QuestionPack) { repo.deletePack(p); contentVersion += 1 }

    /** Imports each file and returns a report to show. */
    func importPacks(_ urls: [URL]) -> String {
        var out: [String] = []
        for u in urls {
            let access = u.startAccessingSecurityScopedResource()
            defer { if access { u.stopAccessingSecurityScopedResource() } }
            let name = u.lastPathComponent
            do {
                let text = try String(contentsOf: u, encoding: .utf8)
                let p = try repo.importPack(text, displayName: name)
                var line = "✓ \(p.name): \(p.questions.count) questions added"
                if p.skippedDuplicates > 0 { line += ", \(p.skippedDuplicates) already in the app" }
                if p.invalid > 0 { line += ", \(p.invalid) invalid skipped:\n   " + p.errors.joined(separator: "\n   ") }
                out.append(line)
            } catch { out.append("✗ \(name): \(error.localizedDescription)") }
        }
        contentVersion += 1
        return out.joined(separator: "\n\n")
    }

    /** The progress backup, in the same format as the Android app's. */
    func backupText() -> String { JWrite.string(store.toJson(), pretty: true) }

    func restoreProgress(_ url: URL) -> String {
        let access = url.startAccessingSecurityScopedResource()
        defer { if access { url.stopAccessingSecurityScopedResource() } }
        do {
            try store.restore(try String(contentsOf: url, encoding: .utf8))
            return "Progress restored."
        } catch { return "Could not restore: \(error.localizedDescription)" }
    }

    func resetProgress() {
        store.reset()
        abandonMock()
        lastMock = nil
        finishedMock = nil
    }

    /* ── home-screen widget ── */

    /** Set when a widget tile or a notification opened the app; acted on once content has loaded. */
    @Published var pendingOpen: String? = nil

    func openPending() {
        guard let target = pendingOpen, ready else { return }
        pendingOpen = nil
        if screen == .MOCK && mock != nil { return } // never pull you out of a running mock
        home()
        switch target {
        case "days": navigate(.PLANNER)
        case "today": if let s = run?.session ?? plan()?.next { startSession(s) }
        case "ready": navigate(.ANALYTICS)
        case "syllabus": navigate(.LEARN)
        case "fitness": navigate(.FITNESS)
        case "reports": navigate(.REPORTS)
        case "book": navigate(.BOOK)
        case "short": if let s = run?.session ?? plan()?.next { startSession(shortVersion(s)) }
        case "cards": navigate(.FLASHCARDS); startDeck(nil)
        default: break
        }
    }

    private func publishWidget() {
        let now = Time.now()
        let lessons = repo.extras.lessons
        let (state, at) = readyForWidget(now)
        let stats = WidgetStats(examDate: store.plan.examDate, day: Time.startOfDay(now),
                                todayMinutes: WidgetStats.minutesToday(store.sessionResults, store.attempts, now),
                                targetMinutes: plan(now)?.minutesPerDay ?? 0, readiness: readiness.report(store, now).score,
                                lessonsDone: lessons.filter { store.lessonsDone.contains($0.id) }.count, lessonsTotal: lessons.count,
                                fitness: fitness(now).score ?? -1, readyBy: at, readyState: state)
        if stats != WidgetStats.load() { stats.save(); WidgetBridge.reload() }
    }

    /** The widget's ready-by tile: now, a projected date, stalled, or unknown. */
    private func readyForWidget(_ now: Millis) -> (Int, Millis) {
        let b = booking(now)
        if b.criteria.prefix(2).allSatisfy(\.met) { return (WidgetStats.READY_NOW, now) }
        guard let p = b.projection else { return (WidgetStats.READY_UNKNOWN, 0) }
        if !p.stalled.isEmpty { return (WidgetStats.READY_STALLED, 0) }
        if let l = p.latest ?? p.earliest { return (WidgetStats.READY_PROJECTED, l) }
        return (WidgetStats.READY_UNKNOWN, 0)
    }

    /* ── smart reminders ── */

    @Published var reminders = ReminderState.load()

    func updateReminders(_ change: (inout ReminderState) -> Void) {
        change(&reminders)
        reminders.plans = [:]
        reminders.save()
        publishReminders()
    }

    private func publishReminders() {
        let now = Time.now()
        reminders.snapshot = reminderSnapshot(now)
        ReminderBrain.noteDelivered(&reminders, now)
        ReminderBrain.noteStudy(&reminders, store.sessionResults, store.attempts, now)
        let habit = ReminderBrain.studyHabit(store.attempts, store.sessionResults, now)
        reminders.studyHabit = habit.0; reminders.studyHabitDays = habit.1
        ReminderScheduler.schedule(&reminders, now)
        reminders.save()
    }

    private func appOpened() {
        ReminderBrain.noteOpened(&reminders, Time.now(), tappedId: nil)
        reminders.save()
        checkModels()
    }

    func reminderTapped(_ id: Millis) {
        ReminderBrain.noteOpened(&reminders, Time.now(), tappedId: id)
        reminders.save()
    }

    func notToday(_ id: Millis) {
        ReminderBrain.noteNotToday(&reminders, id, Time.now())
        publishReminders()
    }

    private func noteForeground(from: Millis, to: Millis) {
        ReminderBrain.noteForeground(&reminders, from: from, to: to)
        reminders.save()
    }

    private func reminderSnapshot(_ now: Millis) -> ReminderSnapshot {
        let p = plan(now)
        let next = run?.session ?? p?.next
        let today = Time.startOfDay(now)
        let weekAhead = p?.sessions.filter { $0.day > today && $0.day <= today + 7 * Time.dayMs } ?? []
        let focusDomain = Dictionary(grouping: weekAhead.compactMap(\.domain), by: { $0 }).max { $0.value.count < $1.value.count }?.key
        let focus = focusDomain.map { "\($0.title) domain" + (weekAhead.contains { $0.kind == .MOCK } ? " and a full mock" : "") }
        let st = readiness.streak(store, now)
        return ReminderSnapshot(day: today, nextTitle: next?.title, nextMinutes: next?.minutes ?? 0, nextWhat: next.map(describeSteps),
            cardsDue: deckCounts(nil).due, streak: st.current, studyDays: store.plan.studyDays,
            todayMinutes: WidgetStats.minutesToday(store.sessionResults, store.attempts, now), targetMinutes: p?.minutesPerDay ?? 0,
            lastStudyAt: max(store.attempts.map(\.at).max() ?? 0, store.sessionResults.map(\.at).max() ?? 0),
            finalWeek: next?.kind == .TAPER, readiness: readiness.report(store, now).score,
            mastered: p?.tasks.filter { $0.mastery == .MASTERED }.count ?? 0,
            weekMinutes: ReminderBrain.studyMinutes(store.sessionResults, store.attempts, now - 7 * Time.dayMs, now),
            nextWeekFocus: focus, restDayFree: st.restDayFree)
    }

    /* ── demo state for CI screenshots: -uiDemo 1 -uiScreen <name> ── */

    func applyLaunchArguments() {
        let d = UserDefaults.standard
        guard let screenName = d.string(forKey: "uiScreen") else { return }
        if d.bool(forKey: "uiDemo") && store.attempts.isEmpty { DemoData.seed(self) }
        switch screenName {
        case "practice": navigate(.PRACTICE)
        case "question": startPractice(PracticeFilter(count: 10), title: "Mixed practice")
        case "mock": navigate(.MOCK)
        case "flashcards": navigate(.FLASHCARDS)
        case "learn": navigate(.LEARN)
        case "analytics": navigate(.ANALYTICS)
        case "fitness": navigate(.FITNESS)
        case "reports": navigate(.REPORTS)
        case "book": navigate(.BOOK)
        case "coach": navigate(.COACH)
        case "menu": navigate(.MENU)
        case "settings": navigate(.SETTINGS)
        default: break
        }
    }
}
