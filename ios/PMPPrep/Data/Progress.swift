import Foundation
import Combine

/** One answer, as it happened. Analytics, readiness and the mistake journal are all computed from these. */
struct Attempt: Equatable {
    var qid: String
    var ecoTask: String
    var domain: Domain
    var approach: Approach?
    var type: QType
    var credit: Double
    var correct: Bool
    var ms: Int64
    var at: Millis
    var source: String
    /** Time from the question appearing to the first touch of an answer (reading time); 0 if not captured. */
    var firstTapMs: Int64 = 0
    /** Longest stretch with no touch while the question was on screen; 0 if not captured. */
    var idleMs: Int64 = 0
    /** Answer changes before checking that went from right to wrong, and from wrong to right. */
    var rightToWrong: Int = 0
    var wrongToRight: Int = 0
    /** Words to read: the stem plus the options. */
    var words: Int = 0
    /** Answered against a clock: a mock, or timed practice. */
    var timed: Bool = false
    /** How sure you were before checking: SURE, THINK, GUESS, or 0 if not asked. */
    var confidence: Int = 0
    /** Options crossed out before checking. */
    var eliminated: Int = 0

    /** Right, but by your own account a guess: not evidence that you know it. */
    var luckyGuess: Bool { correct && confidence == Attempt.GUESS }
    /** Wrong while sure: the most dangerous kind of mistake on exam day. */
    var sureWrong: Bool { !correct && confidence == Attempt.SURE }

    static let GUESS = 1, THINK = 2, SURE = 3
}

/** A sitting started or ended: a practice set, a guided session or a mock. Used to tell finished from abandoned. */
struct SittingEvent: Equatable { var key: String; var kind: String; var at: Millis; var planned: Int; var answered: Int; var finished: Bool }

/** A one-tap check-in. kind is "pre" (before a session), "post" (after a mock) or "weekly"; values 1–5. */
struct CheckIn: Equatable { var at: Millis; var kind: String; var value: Int; var items: [Int] = [] }

enum RootCause: String, CaseIterable, Identifiable {
    case KNOWLEDGE_GAP, MISREAD_STEM, WRONG_MINDSET, CALCULATION, RUSHED
    var id: String { rawValue }
    var title: String {
        switch self {
        case .KNOWLEDGE_GAP: "Knowledge gap"; case .MISREAD_STEM: "Misread the stem"; case .WRONG_MINDSET: "Wrong mindset"
        case .CALCULATION: "Calculation slip"; case .RUSHED: "Rushed"
        }
    }
    var description: String {
        switch self {
        case .KNOWLEDGE_GAP: "Didn't know the process, term or formula"
        case .MISREAD_STEM: "Missed a word like FIRST, NEXT, NOT or BEST"
        case .WRONG_MINDSET: "Escalated, avoided or acted before understanding"
        case .CALCULATION: "Arithmetic or an inverted ratio"
        case .RUSHED: "Answered before reading every option"
        }
    }
}

struct MistakeTag: Equatable { var cause: RootCause; var note: String; var at: Millis }
struct ScenarioRun: Equatable { var id: String; var picked: String; var rank: Rank; var at: Millis }

struct MockResult: Equatable {
    var at: Millis
    var title: String
    var n: Int
    var right: Int
    var usedMs: Int64
    /** domain → (right, of) */
    var domains: [Domain: [Int]]
    var pct: Double { n == 0 ? 0 : Double(right) / Double(n) }
}

struct StudyPlan: Equatable {
    var examDate: Millis? = nil
    var hoursPerWeek: Int = 10
    /** Calendar weekday values (Sunday 1 … Saturday 7); Monday to Friday unless chosen otherwise. */
    var studyDays: Set<Int> = [2, 3, 4, 5, 6]
    /** Set when the first-run setup (date, hours, days, diagnostic) has been finished or skipped. */
    var onboarded: Bool = false
    /** One missed day a week doesn't break the streak. */
    var restDays: Bool = true
}

/** The cold 25-question level check the plan and the coach aim from. */
struct DiagnosticResult: Equatable {
    var at: Millis
    var n: Int
    var right: Int
    var domains: [Domain: [Int]]
    var pct: Double { n == 0 ? 0 : Double(right) / Double(n) }
    func domainPct(_ d: Domain) -> Double? { domains[d].flatMap { $0[1] == 0 ? nil : Double($0[0]) / Double($0[1]) } }
}

/** One turn of the conversation with the coach. role is "user" or "model". */
struct ChatTurn: Equatable { var role: String; var text: String; var at: Millis }

/**
 * Everything the person has done, in one JSON file in the app's private storage, in the
 * same format as the Android app's backups. Every change bumps revision (which the UI
 * observes) and is written in the background.
 */
final class ProgressStore: ObservableObject {
    static let TYPE = "pmp-android-progress"
    static let MY_DECK = "mine"
    private static let maxAttempts = 20000
    private static let maxChat = 60

    @Published private(set) var revision = 0

    private let file: URL
    private let io = DispatchQueue(label: "progress-save")

    var attempts: [Attempt] = []
    var flags: [String] = []
    var notes: [String: String] = [:]
    var mistakeTags: [String: MistakeTag] = [:]
    var cards: [String: CardState] = [:]
    var scenarioRuns: [ScenarioRun] = []
    var mocks: [MockResult] = []
    var lessonsDone: Set<String> = []
    private(set) var plan = StudyPlan()
    private(set) var notepad = ""
    var diagnostics: [DiagnosticResult] = []
    var chat: [ChatTurn] = []
    var coachSnoozed: [String: Millis] = [:]
    var sessionResults: [SessionResult] = []
    var sittings: [SittingEvent] = []
    var checkIns: [CheckIn] = []
    var weekly: [WeeklyReport] = []
    var myCards: [Flashcard] = []
    /** Latest attempt per question. */
    var last: [String: Attempt] = [:]

    init(dir: URL) {
        file = dir.appendingPathComponent("progress.json")
        try? FileManager.default.createDirectory(at: dir, withIntermediateDirectories: true)
        if let text = try? String(contentsOf: file, encoding: .utf8), let o = try? JObj.parse(text) { fromJson(o) }
    }

    var diagnostic: DiagnosticResult? { diagnostics.max { $0.at < $1.at } }

    func record(_ a: Attempt) {
        attempts.append(a)
        if attempts.count > Self.maxAttempts { attempts.removeFirst(attempts.count - Self.maxAttempts) }
        last[a.qid] = a
        changed()
    }

    @discardableResult
    func toggleFlag(_ id: String) -> Bool {
        let on: Bool
        if let i = flags.firstIndex(of: id) { flags.remove(at: i); on = false } else { flags.append(id); on = true }
        changed()
        return on
    }
    func setNote(_ id: String, _ text: String) {
        notes[id] = text.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty ? nil : text
        changed()
    }
    func tagMistake(_ id: String, _ cause: RootCause, _ note: String) { mistakeTags[id] = MistakeTag(cause: cause, note: note, at: Time.now()); changed() }
    func setCard(_ id: String, _ s: CardState) { cards[id] = s; changed() }
    func recordScenario(_ r: ScenarioRun) { scenarioRuns.append(r); changed() }
    func recordMock(_ m: MockResult) { mocks.append(m); changed() }
    func setLessonDone(_ id: String, _ done: Bool) { if done { lessonsDone.insert(id) } else { lessonsDone.remove(id) }; changed() }
    func setPlan(_ p: StudyPlan) { plan = p; changed() }
    func recordSession(_ r: SessionResult) { sessionResults.append(r); changed() }
    func recordSitting(_ e: SittingEvent) {
        sittings.append(e)
        if sittings.count > 2000 { sittings.removeFirst(sittings.count - 2000) }
        changed()
    }
    func recordCheckIn(_ c: CheckIn) { checkIns.append(c); changed() }

    /** Saves this week's report, replacing the week's earlier version. Doesn't bump the revision: it's derived. */
    func putWeekly(_ r: WeeklyReport) {
        if let i = weekly.firstIndex(where: { $0.weekStart == r.weekStart }) {
            if weekly[i] == r { return }
            weekly[i] = r
        } else { weekly.append(r) }
        weekly.sort { $0.weekStart < $1.weekStart }
        if weekly.count > 104 { weekly.removeFirst(weekly.count - 104) }
        save()
    }

    func addMyCard(_ c: Flashcard) { myCards.removeAll { $0.id == c.id }; myCards.append(c); changed() }
    func removeMyCard(_ id: String) { myCards.removeAll { $0.id == id }; cards[id] = nil; changed() }

    func setNotepad(_ text: String) { if text == notepad { return }; notepad = text; changed() }
    func recordDiagnostic(_ d: DiagnosticResult) { diagnostics.append(d); changed() }
    func addChat(_ t: ChatTurn) {
        chat.append(t)
        if chat.count > Self.maxChat { chat.removeFirst(chat.count - Self.maxChat) }
        changed()
    }
    func clearChat() { chat = []; changed() }
    func snooze(_ id: String, until: Millis) { coachSnoozed[id] = until; changed() }

    func reset() {
        attempts = []; flags = []; notes = [:]; mistakeTags = [:]; cards = [:]
        scenarioRuns = []; mocks = []; lessonsDone = []; last = [:]; plan = StudyPlan()
        notepad = ""; diagnostics = []; chat = []; coachSnoozed = [:]; sessionResults = []
        sittings = []; checkIns = []; weekly = []; myCards = []
        changed()
    }

    /** Restores a backup, from this app or the Android app. */
    func restore(_ text: String) throws {
        let o = try JObj.parse(text)
        if o.optString("type") != Self.TYPE { throw PackError("This is not a PMP Prep progress backup.") }
        fromJson(o)
        changed()
    }

    private func changed() {
        revision += 1
        save()
    }

    private func save() {
        let snapshot = JWrite.string(toJson())
        let target = file
        io.async {
            let tmp = target.deletingLastPathComponent().appendingPathComponent("progress.json.tmp")
            do {
                try snapshot.write(to: tmp, atomically: true, encoding: .utf8)
                _ = try? FileManager.default.removeItem(at: target)
                try FileManager.default.moveItem(at: tmp, to: target)
            } catch { /* the next change will try again */ }
        }
    }

    /* ── JSON: the Android app's format, field for field ─────────────────── */

    func toJson() -> [String: Any] {
        var o: [String: Any] = ["type": Self.TYPE, "version": 2]
        o["attempts"] = attempts.map { a -> [String: Any] in
            var j: [String: Any] = ["q": a.qid, "t": a.ecoTask, "d": a.domain.rawValue, "ap": a.approach?.rawValue ?? "",
                                    "ty": a.type.rawValue, "cr": a.credit, "ok": a.correct, "ms": a.ms, "at": a.at, "src": a.source]
            if a.firstTapMs > 0 { j["ft"] = a.firstTapMs }
            if a.idleMs > 0 { j["idle"] = a.idleMs }
            if a.rightToWrong > 0 { j["rw"] = a.rightToWrong }
            if a.wrongToRight > 0 { j["wr"] = a.wrongToRight }
            if a.words > 0 { j["w"] = a.words }
            if a.timed { j["tm"] = true }
            if a.confidence > 0 { j["cf"] = a.confidence }
            if a.eliminated > 0 { j["el"] = a.eliminated }
            return j
        }
        o["flags"] = flags
        o["notes"] = notes
        o["mistakes"] = mistakeTags.mapValues { ["c": $0.cause.rawValue, "n": $0.note, "at": $0.at] as [String: Any] }
        o["cards"] = cards.mapValues { [$0.ease, $0.interval, $0.reps, $0.lapses, $0.due, $0.last, $0.first] as [Any] }
        o["scenarios"] = scenarioRuns.map { ["id": $0.id, "p": $0.picked, "r": $0.rank.rawValue, "at": $0.at] as [String: Any] }
        o["mocks"] = mocks.map { m -> [String: Any] in
            ["at": m.at, "title": m.title, "n": m.n, "right": m.right, "usedMs": m.usedMs,
             "domains": Dictionary(uniqueKeysWithValues: m.domains.map { ($0.key.rawValue, $0.value) })]
        }
        o["lessons"] = Array(lessonsDone)
        o["plan"] = ["examDate": plan.examDate ?? 0, "hours": plan.hoursPerWeek, "days": Array(plan.studyDays),
                     "onboarded": plan.onboarded, "rest": plan.restDays] as [String: Any]
        o["sessions"] = sessionResults.map { r -> [String: Any] in
            ["k": r.key, "kind": r.kind.rawValue, "t": r.taskId ?? "", "d": r.domain?.rawValue ?? "", "s": r.score, "m": r.minutes, "at": r.at]
        }
        o["notepad"] = notepad
        o["diagnostics"] = diagnostics.map { d -> [String: Any] in
            ["at": d.at, "n": d.n, "right": d.right, "domains": Dictionary(uniqueKeysWithValues: d.domains.map { ($0.key.rawValue, $0.value) })]
        }
        o["chat"] = chat.map { ["r": $0.role, "t": $0.text, "at": $0.at] as [String: Any] }
        o["snoozed"] = coachSnoozed
        o["sittings"] = sittings.map { ["k": $0.key, "kind": $0.kind, "at": $0.at, "p": $0.planned, "a": $0.answered, "f": $0.finished] as [String: Any] }
        o["checkins"] = checkIns.map { ["at": $0.at, "kind": $0.kind, "v": $0.value, "items": $0.items] as [String: Any] }
        o["weekly"] = weekly.map { $0.toJson() }
        o["mycards"] = myCards.map { ["id": $0.id, "t": $0.ecoTask ?? "", "f": $0.front, "b": $0.back, "n": $0.note] as [String: Any] }
        return o
    }

    private func fromJson(_ o: JObj) {
        attempts = []; flags = []; notes = [:]; mistakeTags = [:]; cards = [:]
        scenarioRuns = []; mocks = []; lessonsDone = []; last = [:]
        diagnostics = []; chat = []; coachSnoozed = [:]; sessionResults = []
        sittings = []; checkIns = []; weekly = []; myCards = []
        notepad = o.optString("notepad")
        myCards = o.objects("mycards").map {
            Flashcard(id: $0.str("id"), deck: Self.MY_DECK, ecoTask: $0.str("t").isEmpty ? nil : $0.str("t"), front: $0.str("f"), back: $0.str("b"), note: $0.str("n"))
        }
        sittings = o.objects("sittings").map {
            SittingEvent(key: $0.str("k"), kind: $0.str("kind"), at: $0.optLong("at"), planned: $0.optInt("p"), answered: $0.optInt("a"), finished: $0.optBool("f"))
        }
        checkIns = o.objects("checkins").map { CheckIn(at: $0.optLong("at"), kind: $0.str("kind"), value: $0.optInt("v"), items: $0.ints("items") ?? []) }
        weekly = o.objects("weekly").map { WeeklyReport.fromJson($0) }
        sessionResults = o.objects("sessions").compactMap { r in
            guard let kind = SessionKind(rawValue: r.str("kind")) else { return nil }
            return SessionResult(key: r.str("k"), kind: kind, taskId: r.str("t").isEmpty ? nil : r.str("t"), domain: Domain.of(r.str("d")),
                                 score: r.optDouble("s"), minutes: r.optInt("m"), at: r.optLong("at"))
        }
        func domainPairs(_ j: JObj?) -> [Domain: [Int]] {
            var out: [Domain: [Int]] = [:]
            for d in Domain.allCases { if let a = j?.ints(d.rawValue), a.count >= 2 { out[d] = [a[0], a[1]] } }
            return out
        }
        diagnostics = o.objects("diagnostics").map {
            DiagnosticResult(at: $0.optLong("at"), n: $0.optInt("n"), right: $0.optInt("right"), domains: domainPairs($0.obj("domains")))
        }
        chat = o.objects("chat").map { ChatTurn(role: $0.str("r"), text: $0.str("t"), at: $0.optLong("at")) }
        if let sn = o.obj("snoozed") { for k in sn.keys { coachSnoozed[k] = sn.optLong(k) } }
        for a in o.objects("attempts") {
            let at = Attempt(
                qid: a.optString("q"), ecoTask: a.optString("t"), domain: Domain.of(a.optString("d")) ?? .process,
                approach: Approach.of(a.optString("ap")), type: QType.of(a.optString("ty")) ?? .single,
                credit: a.optDouble("cr"), correct: a.optBool("ok"), ms: a.optLong("ms"), at: a.optLong("at"), source: a.optString("src"),
                firstTapMs: a.optLong("ft"), idleMs: a.optLong("idle"), rightToWrong: a.optInt("rw"), wrongToRight: a.optInt("wr"),
                words: a.optInt("w"), timed: a.optBool("tm"), confidence: a.optInt("cf"), eliminated: a.optInt("el"))
            attempts.append(at)
            last[at.qid] = at
        }
        flags = o.strings("flags")
        if let n = o.obj("notes") { for k in n.keys { notes[k] = n.optString(k) } }
        if let m = o.obj("mistakes") {
            for k in m.keys {
                guard let v = m.obj(k) else { continue }
                mistakeTags[k] = MistakeTag(cause: RootCause(rawValue: v.str("c")) ?? .KNOWLEDGE_GAP, note: v.str("n"), at: v.optLong("at"))
            }
        }
        if let c = o.obj("cards") {
            for k in c.keys {
                guard let a = c.arr(k) else { continue }
                func n(_ i: Int) -> NSNumber? { i < a.count ? a[i] as? NSNumber : nil }
                cards[k] = CardState(ease: n(0)?.doubleValue ?? 2.5, interval: n(1)?.intValue ?? 0, reps: n(2)?.intValue ?? 0,
                                     lapses: n(3)?.intValue ?? 0, due: n(4)?.int64Value ?? 0, last: n(5)?.int64Value ?? 0, first: n(6)?.int64Value ?? 0)
            }
        }
        scenarioRuns = o.objects("scenarios").map { ScenarioRun(id: $0.str("id"), picked: $0.str("p"), rank: Rank.of($0.str("r")), at: $0.optLong("at")) }
        mocks = o.objects("mocks").map {
            MockResult(at: $0.optLong("at"), title: $0.str("title"), n: $0.optInt("n"), right: $0.optInt("right"), usedMs: $0.optLong("usedMs"),
                       domains: domainPairs($0.obj("domains")))
        }
        lessonsDone = Set(o.strings("lessons"))
        if let p = o.obj("plan") {
            let date = p.optLong("examDate")
            let days = Set(p.ints("days") ?? [])
            // Older backups have no onboarded flag: a date plus a diagnostic means setup was done.
            plan = StudyPlan(examDate: date > 0 ? date : nil, hoursPerWeek: p.optInt("hours", 10),
                             studyDays: days.isEmpty ? StudyPlan().studyDays : days,
                             onboarded: p.optBool("onboarded", date > 0 && !diagnostics.isEmpty), restDays: p.optBool("rest", true))
        }
    }
}
