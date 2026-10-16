import Foundation
import Combine

/** Option order is shuffled once per session; options marked fixed keep their place. */
func shuffledOptions(_ q: Question) -> [Option] {
    var free = q.options.filter { !$0.fixed }.shuffled().makeIterator()
    return q.options.map { $0.fixed ? $0 : free.next()! }
}

/** A practice drill: answer, check, see why, move on. Also used to review a finished mock. */
final class QuizSession: ObservableObject, Identifiable {
    let id = UUID()
    let title: String
    let questions: [Question]
    let reviewOnly: Bool
    /** The cold level check: no feedback until the end, scored as one set. */
    let diagnostic: Bool
    /** Against the clock: answers count as timed for the pressure gap. */
    let timed: Bool
    /** Reading drill: the stem shows alone until you say you know what it's asking. */
    let readingDrill: Bool
    /** Where the answers are recorded as coming from. */
    let source: String
    let startedAt = Time.now()
    let key: String
    let order: [String: [Option]]

    @Published var index = 0
    @Published var answers: [String: Answer] = [:]
    @Published var results: [String: ScoreResult] = [:]
    @Published var shownAt = Time.now()
    @Published var finished = false
    /** Options crossed out, highlighted words of the stem, and how sure you were, per question. */
    @Published var struck: [String: Set<String>] = [:]
    @Published var marks: [String: Set<Int>] = [:]
    @Published var confidence: [String: Int] = [:]
    @Published var optionsShown: Bool
    @Published var totalRightToWrong = 0
    @Published var totalWrongToRight = 0
    /** Hint rungs used per question, and the hints shown so far (the ladder, web §10.16.10). */
    @Published var hints: [String: Int] = [:]
    @Published var hintsShown: [String: [HintLadder.Hint]] = [:]

    /* How the current question is being handled, for Exam fitness. Times exclude time spent outside the app. */
    var firstTapAt: Millis = 0
    var awayAtFirstTap: Millis = 0
    var lastTouchAt = Time.now()
    var awayAtLastTouch: Millis = 0
    var maxIdle: Millis = 0
    var rightToWrong = 0
    var wrongToRight = 0
    var awayMs: Millis = 0
    var awayTotal: Millis = 0

    init(title: String, questions: [Question], reviewOnly: Bool = false, diagnostic: Bool = false,
         givenAnswers: [String: Answer] = [:], givenOrder: [String: [Option]]? = nil,
         timed: Bool = false, readingDrill: Bool = false, source: String = Readiness.SOURCE_PRACTICE) {
        self.title = title; self.questions = questions; self.reviewOnly = reviewOnly; self.diagnostic = diagnostic
        self.timed = timed; self.readingDrill = readingDrill; self.source = source
        key = "practice:\(startedAt)"
        order = givenOrder ?? Dictionary(uniqueKeysWithValues: questions.map { ($0.id, shuffledOptions($0)) })
        _answers = Published(initialValue: givenAnswers)
        _optionsShown = Published(initialValue: !readingDrill)
        var r: [String: ScoreResult] = [:]
        if reviewOnly { for q in questions { r[q.id] = Scoring.score(q, givenAnswers[q.id]) } }
        _results = Published(initialValue: r)
    }

    /** A touch: closes the idle stretch that ran since the last one. */
    func touch(_ now: Millis) {
        maxIdle = max(maxIdle, now - lastTouchAt - (awayMs - awayAtLastTouch))
        lastTouchAt = now
        awayAtLastTouch = awayMs
    }

    /** A new question is on screen. */
    func resetTracking(_ now: Millis) {
        firstTapAt = 0; awayAtFirstTap = 0; lastTouchAt = now; awayAtLastTouch = 0
        maxIdle = 0; rightToWrong = 0; wrongToRight = 0; awayMs = 0
        optionsShown = !readingDrill
    }

    var current: Question { questions[min(max(index, 0), questions.count - 1)] }
    /** Hints are for practice only: never a mock review, a lesson set, the diagnostic, a timed drill or a reading drill. */
    var hintable: Bool { !reviewOnly && !diagnostic && !timed && !readingDrill && source != Readiness.SOURCE_LESSON }
    func isChecked(_ q: Question) -> Bool { results[q.id] != nil }
    var checkedCount: Int { results.count }
    var correctCount: Int { results.values.filter(\.correct).count }
}

/** A timed mock exam. After each break the earlier section is closed, as in the real exam. */
final class MockSession: ObservableObject {
    let title: String
    let questions: [Question]
    let order: [String: [Option]]
    let durationMs: Int64
    /** Question counts at which a section ends and an optional break is offered. */
    let breakAfter: [Int]
    let breakMs: Int64

    @Published var index = 0
    @Published var section = 0
    @Published var answers: [String: Answer] = [:]
    @Published var marked: [String: Bool] = [:]
    @Published var struck: [String: Set<String>] = [:]
    @Published var marks: [String: Set<Int>] = [:]
    @Published var remainingMs: Int64
    @Published var onBreak = false
    @Published var breakRemainingMs: Int64 = 0
    @Published var finished = false
    var startedAt = Time.now()
    /** Unscored pretest items, as on the real exam; revealed only on the results. */
    var pretest: Set<String> = []
    /** Per question: time on screen, time to first answer, and answer changes (for Exam fitness). */
    var spentMs: [String: Int64] = [:]
    var firstTapMs: [String: Int64] = [:]
    var rightToWrong: [String: Int] = [:]
    var wrongToRight: [String: Int] = [:]

    init(title: String, questions: [Question], order: [String: [Option]], durationMs: Int64, breakAfter: [Int], breakMs: Int64) {
        self.title = title; self.questions = questions; self.order = order; self.durationMs = durationMs
        self.breakAfter = breakAfter; self.breakMs = breakMs; _remainingMs = Published(initialValue: durationMs)
    }

    var sectionCount: Int { breakAfter.count + 1 }
    func sectionStart(_ s: Int) -> Int { s == 0 ? 0 : breakAfter[s - 1] }
    func sectionEnd(_ s: Int) -> Int { s >= breakAfter.count ? questions.count : breakAfter[s] }
    var inSection: Range<Int> { sectionStart(section)..<sectionEnd(section) }
    var current: Question { questions[min(max(index, 0), questions.count - 1)] }
    var answeredCount: Int { questions.filter { Scoring.isAnswered($0, answers[$0.id]) }.count }
    func isPretest(_ q: Question) -> Bool { pretest.contains(q.id) }

    static func answerJson(_ a: Answer) -> [String: Any] { ["c": Array(a.choices), "m": a.map] }
    static func answerFrom(_ o: JObj?) -> Answer {
        guard let o else { return Answer() }
        var m: [String: String] = [:]
        if let mj = o.obj("m") { for k in mj.keys { m[k] = mj.optString(k) } }
        return Answer(choices: Set(o.strings("c")), map: m)
    }

    func toJson() -> [String: Any] {
        [
            "title": title, "ids": questions.map(\.id),
            "order": order.mapValues { $0.map(\.id) },
            "answers": answers.mapValues { Self.answerJson($0) },
            "marked": marked.filter(\.value).map(\.key),
            "struck": struck.mapValues { Array($0) }, "marks": marks.mapValues { Array($0) },
            "duration": durationMs, "remaining": remainingMs, "breakAfter": breakAfter, "breakMs": breakMs,
            "index": index, "section": section, "onBreak": onBreak, "breakRemaining": breakRemainingMs, "startedAt": startedAt,
            "spent": spentMs, "firstTap": firstTapMs, "rw": rightToWrong, "wr": wrongToRight,
            "pretest": Array(pretest).sorted(),
        ]
    }

    static func fromJson(_ o: JObj, byId: [String: Question]) -> MockSession? {
        let qs = o.strings("ids").compactMap { byId[$0] }
        if qs.isEmpty { return nil }
        let ord = o.obj("order")
        var order: [String: [Option]] = [:]
        for q in qs {
            let ids = ord?.strings(q.id) ?? []
            let byOpt = Dictionary(uniqueKeysWithValues: q.options.map { ($0.id, $0) })
            let list = ids.compactMap { byOpt[$0] }
            order[q.id] = list.count == q.options.count ? list : q.options
        }
        let s = MockSession(title: o.optString("title", "Mock exam"), questions: qs, order: order, durationMs: o.optLong("duration"),
                            breakAfter: (o.ints("breakAfter") ?? []).filter { $0 >= 1 && $0 < qs.count }, breakMs: o.optLong("breakMs"))
        if let a = o.obj("answers") { for k in a.keys { s.answers[k] = answerFrom(a.obj(k)) } }
        for k in o.strings("marked") { s.marked[k] = true }
        if let j = o.obj("struck") { for k in j.keys { s.struck[k] = Set(j.strings(k)) } }
        if let j = o.obj("marks") { for k in j.keys { s.marks[k] = Set(j.ints(k) ?? []) } }
        s.remainingMs = o.optLong("remaining")
        s.section = min(max(o.optInt("section"), 0), s.sectionCount - 1)
        s.index = min(max(o.optInt("index"), s.inSection.lowerBound), s.inSection.upperBound - 1)
        s.onBreak = o.optBool("onBreak")
        s.breakRemainingMs = o.optLong("breakRemaining")
        s.startedAt = o.optLong("startedAt", Time.now())
        if let j = o.obj("spent") { for k in j.keys { s.spentMs[k] = j.optLong(k) } }
        if let j = o.obj("firstTap") { for k in j.keys { s.firstTapMs[k] = j.optLong(k) } }
        if let j = o.obj("rw") { for k in j.keys { s.rightToWrong[k] = j.optInt(k) } }
        if let j = o.obj("wr") { for k in j.keys { s.wrongToRight[k] = j.optInt(k) } }
        s.pretest = Set(o.strings("pretest"))
        return s
    }
}
