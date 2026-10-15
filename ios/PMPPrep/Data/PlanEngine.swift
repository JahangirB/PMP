import Foundation

/*
 * The guided path. Nothing here is a stored calendar: every build starts from
 * what the person still needs (lessons not done, tasks not mastered, mocks not
 * sat) and the study time left before the exam, so skipped days are absorbed
 * rather than piling up, and results change what comes next.
 */

enum Mastery { case NOT_STARTED, LEARNING, SHAKY, MASTERED, FADING }

struct TaskState {
    let task: EcoTask; let answers: Int; let recentAccuracy: Double; let lastAt: Millis; let mastery: Mastery
    /** Median seconds per question over the same recent answers; nil without timed answers. */
    var medianSec: Double? = nil
}

enum SessionKind: String { case REENTRY, REMEDIATION, FOUNDATION, BUILD, DRILL, REVIEW, CHECKPOINT, SKILLS, MIXED, MOCK, TAPER }

enum StepKind { case CARDS, LESSON, PRACTICE, REFLECT, SCENARIOS, MOCK, DEBRIEF }

/** count: questions or scenarios; for PRACTICE, easy = drawn from difficulty 1–3 only. */
struct Step: Equatable {
    var kind: StepKind; var count: Int = 0; var timed: Bool = false; var easy: Bool = false
    /** Reading drill: each stem is shown alone first, and you say what it's asking before the options appear. */
    var reading: Bool = false
}

struct Session: Equatable {
    /** Stable for the same piece of work across rebuilds, e.g. "build:people-3". */
    var key: String
    var kind: SessionKind
    var title: String
    var why: String
    var minutes: Int
    var steps: [Step]
    var taskId: String? = nil
    var domain: Domain? = nil
    var lessonId: String? = nil
    /** Start of the study day this is planned for. */
    var day: Millis = 0
    var priority: Double = 0
    var notBefore: Millis = 0
}

struct SessionResult: Equatable { var key: String; var kind: SessionKind; var taskId: String?; var domain: Domain?; var score: Double; var minutes: Int; var at: Millis }

struct PlanSettings { let examDate: Millis; let hoursPerWeek: Int; let studyDays: Set<Int> }

struct Plan {
    let sessions: [Session]
    let phase: String
    let daysLeft: Int
    let minutesPerDay: Int
    let neededMinutes: Int
    let availableMinutes: Int
    /** True when the work left does not fit the time left even after trimming. */
    let behind: Bool
    let dropped: [String]
    let adjustments: [String]
    let tasks: [TaskState]
    var next: Session? { sessions.first }
}

struct PlanInput {
    let x: Extras
    let attempts: [Attempt]
    let lessonsDone: Set<String>
    let results: [SessionResult]
    let mocks: [MockResult]
    let diagnostic: DiagnosticResult?
    let settings: PlanSettings
    let now: Millis
    /** The cause you tagged on each missed question (question id → cause). */
    var mistakeTags: [String: RootCause] = [:]
    /** ECO task id → how many of its flashcards are leeches. */
    var leeches: [String: Int] = [:]
    /** Exam fitness: the next rung of the stamina ladder, or nil when done. */
    var ladderNext: Int? = nil
    /** Exam fitness: reading pace is below what the exam needs. */
    var readingSlow: Bool = false
}

private func pct(_ v: Double) -> String { "\(Int((v * 100).rounded()))%" }

enum PlanEngine {
    static let DAY = Time.dayMs
    static let MASTERY_ANSWERS = 10
    static let MASTERY_ACCURACY = 0.75
    static let FADE_DAYS: Int64 = 14
    static let PASS = 0.70
    static let REENTRY_DAYS = 5
    static let TAPER_DAYS: Int64 = 7
    static let SLOW_SEC = 110.0
    static let RECHECK_BELOW = 0.65
    static let CAUSE_MIN = 2
    private static let RECENT = 15

    static func startOfDay(_ t: Millis) -> Millis { Time.startOfDay(t) }
    static func nextDay(_ d: Millis) -> Millis { Time.nextDay(d) }

    static func taskStates(_ x: Extras, _ attempts: [Attempt], _ now: Millis) -> [TaskState] {
        let byTask = Dictionary(grouping: attempts.filter { $0.source != Readiness.SOURCE_LESSON }, by: \.ecoTask)
        return x.tasks.map { t in
            // A right answer you called a guess isn't evidence you know it.
            let list = (byTask[t.id] ?? []).filter { !$0.luckyGuess }.sorted { $0.at < $1.at }
            let recent = Array(list.suffix(RECENT))
            let acc = recent.isEmpty ? 0 : recent.reduce(0) { $0 + $1.credit } / Double(recent.count)
            let last = list.last?.at ?? 0
            let m: Mastery
            if list.isEmpty { m = .NOT_STARTED }
            else if list.count < MASTERY_ANSWERS { m = .LEARNING }
            else if acc < MASTERY_ACCURACY { m = .SHAKY }
            else if now - last > FADE_DAYS * DAY { m = .FADING }
            else { m = .MASTERED }
            let secs = recent.filter { $0.ms > 500 }.map { Double($0.ms) / 1000 }.sorted()
            return TaskState(task: t, answers: list.count, recentAccuracy: acc, lastAt: last, mastery: m, medianSec: secs.isEmpty ? nil : secs[secs.count / 2])
        }
    }

    static func build(_ inp: PlanInput) -> Plan {
        let x = inp.x
        let now = inp.now
        let today = startOfDay(now)
        let exam = startOfDay(inp.settings.examDate)
        let studyDays = inp.settings.studyDays.isEmpty ? Set(1...7) : inp.settings.studyDays
        let perDay = max(15, inp.settings.hoursPerWeek * 60 / studyDays.count)
        let tasks = taskStates(x, inp.attempts, now)
        var adjustments: [String] = []

        /* ── how weak each domain is: practice once there is enough, else the diagnostic ── */
        var domainAcc: [Domain: Double] = [:]
        for d in Domain.allCases {
            let a = inp.attempts.filter { $0.domain == d && $0.source != Readiness.SOURCE_LESSON }
            domainAcc[d] = a.count >= 30 ? a.reduce(0) { $0 + $1.credit } / Double(a.count) : inp.diagnostic?.domainPct(d) ?? 0.6
        }
        func taskPriority(_ s: TaskState) -> Double {
            let acc = s.answers >= 5 ? s.recentAccuracy : domainAcc[s.task.domain] ?? 0.6
            return (x.domainWeights[s.task.domain] ?? 0.3) * (1 - min(max(acc, 0), 0.95)) * 10
        }

        var items: [Session] = []
        var lessonsByTask: [String: Lesson] = [:]
        for l in x.lessons where !l.ecoTask.trimmingCharacters(in: .whitespaces).isEmpty { lessonsByTask[l.ecoTask] = l }
        var lastResult: [String: SessionResult] = [:]
        for r in inp.results { if let t = r.taskId, (lastResult[t]?.at ?? Int64.min) <= r.at { lastResult[t] = r } }
        let worked = Set(tasks.filter { s in inp.results.contains { $0.taskId == s.task.id && startOfDay($0.at) == today } }.map(\.task.id))

        /* ── 1. coming back after a gap ── */
        let lastActivity = [inp.attempts.map(\.at).max(), inp.results.map(\.at).max(), inp.mocks.map(\.at).max()].compactMap { $0 }.max()
        let gapDays = lastActivity.map { Int((today - startOfDay($0)) / DAY) } ?? 0
        if let la = lastActivity, gapDays >= REENTRY_DAYS {
            items.append(Session(key: "reentry:\(startOfDay(la))", kind: .REENTRY, title: "Welcome back: warm-up",
                why: "Your last study was \(gapDays) days ago. Due cards and 10 questions on what you learned last, before anything new.",
                minutes: 25, steps: [Step(kind: .CARDS), Step(kind: .PRACTICE, count: 10), Step(kind: .DEBRIEF)], priority: 1000))
            adjustments.append("Re-entry warm-up added: your last study was \(gapDays) days ago.")
        }

        /* ── causes you tagged on misses, per task: the most common one shapes that task's sessions ── */
        var taskOfQuestion: [String: String] = [:]
        for a in inp.attempts { taskOfQuestion[a.qid] = a.ecoTask }
        var causesByTask: [String: [RootCause]] = [:]
        for (qid, cause) in inp.mistakeTags { if let t = taskOfQuestion[qid] { causesByTask[t, default: []].append(cause) } }
        var leadCause: [String: RootCause] = [:]
        for (task, causes) in causesByTask {
            let counts = Dictionary(grouping: causes, by: { $0 }).mapValues(\.count)
            if let top = counts.max(by: { $0.value < $1.value }), top.value >= CAUSE_MIN { leadCause[task] = top.key }
        }
        /** Adds what the cause calls for: the lesson for a knowledge gap, scenarios for mindset, a pointer otherwise. */
        func shaped(_ base: Session, _ lesson: Lesson?) -> Session {
            guard let tid = base.taskId, let cause = leadCause[tid] else { return base }
            let label = base.title.range(of: ": ").map { String(base.title[$0.upperBound...]) } ?? base.title
            var s = base
            switch cause {
            case .KNOWLEDGE_GAP:
                guard let lesson, !base.steps.contains(where: { $0.kind == .LESSON }) else { return base }
                s.steps = [Step(kind: .LESSON)] + base.steps
                s.lessonId = lesson.id
                s.minutes = base.minutes + lesson.minutes
                s.why = base.why + " Most misses you tagged here were knowledge gaps, so the lesson comes first."
            case .WRONG_MINDSET:
                s.steps = base.steps.flatMap { $0.kind == .REFLECT ? [Step(kind: .SCENARIOS, count: 3), $0] : [$0] }
                s.minutes = base.minutes + 8
                s.why = base.why + " Most misses you tagged on \(label) were mindset, so three situational scenarios are added."
            case .MISREAD_STEM: s.why = base.why + " Most misses here were misread stems: read the last sentence first and look for FIRST, NEXT, NOT, BEST."
            case .RUSHED: s.why = base.why + " Most misses here were rushed: read every option before choosing; the pace bar shows the time you have."
            case .CALCULATION: s.why = base.why + " Most misses here were calculation slips: write the formula before the numbers. The Formula lab has the sheet."
            }
            return s
        }

        /* ── 2. per task: remediation, build, drills, reviews ── */
        for s in tasks {
            let t = s.task
            let res = lastResult[t.id]
            let pri = taskPriority(s)
            let lesson = lessonsByTask[t.id]
            let tomorrowIfWorked: Millis = worked.contains(t.id) ? nextDay(today) : 0
            if let res, res.score < 0.5, now - res.at < 3 * DAY, res.kind != .REMEDIATION {
                var steps: [Step] = lesson != nil ? [Step(kind: .LESSON)] : []
                steps += [Step(kind: .PRACTICE, count: 10, easy: true), Step(kind: .REFLECT), Step(kind: .DEBRIEF)]
                items.append(shaped(Session(key: "remediation:\(t.id):\(res.at)", kind: .REMEDIATION, title: "Fix: \(t.label)",
                    why: "Your last session on \(t.label) scored \(pct(res.score)). Re-read the lesson, then easier questions to rebuild it.",
                    minutes: 30, steps: steps, taskId: t.id, domain: t.domain, lessonId: lesson?.id, priority: 900), lesson))
                adjustments.append("Fix-up session for \(t.label): your last session there scored \(pct(res.score)).")
                continue
            }
            if let lesson, !inp.lessonsDone.contains(lesson.id) {
                if s.mastery == .MASTERED { adjustments.append("\(t.label): already mastered through your own practice, so only the lesson stays (as reading).") }
                let why = s.mastery == .MASTERED ? "You already answer \(t.label) well; this is the lesson for completeness."
                    : "Learn it, check it, then practise it. " + (inp.diagnostic?.domainPct(t.domain).map { "\(t.domain.title) scored \(pct($0)) in your diagnostic." } ?? "")
                items.append(Session(key: "build:\(t.id)", kind: .BUILD, title: "\(t.code) · \(t.label)", why: why,
                    minutes: lesson.minutes + (s.mastery == .MASTERED ? 0 : 20),
                    steps: s.mastery == .MASTERED ? [Step(kind: .LESSON), Step(kind: .DEBRIEF)]
                        : [Step(kind: .CARDS), Step(kind: .LESSON), Step(kind: .PRACTICE, count: 10), Step(kind: .REFLECT), Step(kind: .DEBRIEF)],
                    taskId: t.id, domain: t.domain, lessonId: lesson.id, priority: pri + 5))
                continue
            }
            switch s.mastery {
            case .NOT_STARTED, .LEARNING, .SHAKY:
                let drills = s.mastery == .SHAKY && s.recentAccuracy < 0.5 ? 2 : 1
                for i in 0..<drills {
                    items.append(shaped(Session(key: "drill:\(t.id):\(i)", kind: .DRILL, title: "Drill: \(t.label)",
                        why: s.answers == 0 ? "No answers on \(t.label) yet." : "\(t.label) is at \(pct(s.recentAccuracy)) over your last \(min(s.answers, RECENT)) answers; the bar is \(pct(MASTERY_ACCURACY)).",
                        minutes: 25, steps: [Step(kind: .PRACTICE, count: 15), Step(kind: .REFLECT), Step(kind: .DEBRIEF)],
                        taskId: t.id, domain: t.domain, priority: pri - Double(i),
                        notBefore: max(tomorrowIfWorked, i > 0 ? nextDay(nextDay(today)) : 0)), i == 0 ? lesson : nil))
                }
                if let res, res.score >= 0.5, res.score <= 0.799, now - res.at < 3 * DAY {
                    adjustments.append("Review of \(t.label) spaced 2 days out: your last session there scored \(pct(res.score)).")
                }
            case .FADING:
                let d = (now - s.lastAt) / DAY
                items.append(Session(key: "review:\(t.id)", kind: .REVIEW, title: "Review: \(t.label)",
                    why: "You had \(t.label) mastered, but it's been \(d) days. A short refresher keeps it.",
                    minutes: 15, steps: [Step(kind: .PRACTICE, count: 10), Step(kind: .DEBRIEF)], taskId: t.id, domain: t.domain, priority: pri, notBefore: tomorrowIfWorked))
                adjustments.append("\(t.label) is fading (\(d) days since practice): review added.")
            case .MASTERED:
                if !inp.results.contains(where: { $0.taskId == t.id }) { adjustments.append("\(t.label): mastered through your own practice, so no drill is planned.") }
            }
            // Accurate but slow: mastery shouldn't hide a pace problem the exam will punish.
            if s.mastery == .MASTERED, let median = s.medianSec, median > SLOW_SEC {
                items.append(Session(key: "pace:\(t.id)", kind: .DRILL, title: "Paced drill: \(t.label)",
                    why: "You answer \(t.label) well (\(pct(s.recentAccuracy))) but slowly: a median of \(Int(median)) s against about 80 s on the day. 15 questions with the pace bar in view.",
                    minutes: 20, steps: [Step(kind: .PRACTICE, count: 15, timed: true), Step(kind: .DEBRIEF)], taskId: t.id, domain: t.domain, priority: pri, notBefore: tomorrowIfWorked))
                adjustments.append("\(t.label): accurate but slow (median \(Int(median)) s), so a paced drill was added.")
            }
            // Flashcards that keep slipping point back to the lesson behind them.
            let leech = inp.leeches[t.id] ?? 0
            let relearnKey = "relearn:\(t.id):\(leech)"
            if leech > 0, let lesson, inp.lessonsDone.contains(lesson.id), !inp.results.contains(where: { $0.key == relearnKey }) {
                items.append(Session(key: relearnKey, kind: .REVIEW, title: "Relearn: \(t.label)",
                    why: "\(leech == 1 ? "A flashcard" : "\(leech) flashcards") on \(t.label) keep slipping. Re-read the lesson, then your due cards.",
                    minutes: lesson.minutes + 5, steps: [Step(kind: .LESSON), Step(kind: .CARDS), Step(kind: .DEBRIEF)], taskId: t.id, domain: t.domain, lessonId: lesson.id, priority: pri))
                adjustments.append("\(t.label): flashcards keep slipping, so its lesson comes back as a review.")
            }
            // A middling session result books a spaced review even after mastery is reached.
            if s.mastery == .MASTERED, let res, res.score >= 0.5, res.score <= 0.799, res.kind != .REVIEW {
                items.append(Session(key: "review:\(t.id)", kind: .REVIEW, title: "Review: \(t.label)",
                    why: "Your last session on \(t.label) scored \(pct(res.score)). A spaced review makes it stick.",
                    minutes: 15, steps: [Step(kind: .PRACTICE, count: 10), Step(kind: .DEBRIEF)], taskId: t.id, domain: t.domain, priority: pri, notBefore: res.at + 2 * DAY))
            }
        }

        /* ── 3. foundations, checkpoints, exam skills, mocks ── */
        for (i, l) in x.lessons.filter({ $0.module == "foundations" && !inp.lessonsDone.contains($0.id) }).enumerated() {
            items.append(Session(key: "lesson:\(l.id)", kind: .FOUNDATION, title: l.title, why: "Foundations come first: how the exam works and the mindset it rewards.",
                minutes: l.minutes, steps: [Step(kind: .LESSON), Step(kind: .DEBRIEF)], lessonId: l.id, priority: i == 0 ? 800 : 60 - Double(i)))
        }
        for d in Domain.allCases {
            let lastPass = inp.results.filter { $0.kind == .CHECKPOINT && $0.domain == d && $0.score >= PASS }.max { $0.at < $1.at }
            if let lastPass {
                // Passed once, but has the domain slipped since? Your answers there since the pass decide.
                let since = Array(inp.attempts.filter { $0.domain == d && $0.source != Readiness.SOURCE_LESSON && $0.at > lastPass.at }.suffix(30))
                let acc = since.isEmpty ? 1 : since.reduce(0) { $0 + $1.credit } / Double(since.count)
                let finalWeek = inp.settings.examDate - now <= TAPER_DAYS * DAY
                if since.count >= 20 && acc < RECHECK_BELOW && !finalWeek {
                    items.append(Session(key: "checkpoint:\(d.rawValue):\(lastPass.at)", kind: .CHECKPOINT, title: "Re-check: \(d.title)",
                        why: "Your \(d.title) answers since you passed its checkpoint average \(pct(acc)), well under the \(pct(PASS)) pass mark. 20 mixed questions show whether it has slipped.",
                        minutes: 35, steps: [Step(kind: .PRACTICE, count: 20), Step(kind: .REFLECT), Step(kind: .DEBRIEF)], domain: d, priority: 4))
                    adjustments.append("\(d.title) has slipped to \(pct(acc)) since its checkpoint, so it will be re-checked.")
                }
                continue
            }
            let failed = inp.results.filter { $0.kind == .CHECKPOINT && $0.domain == d }.max { $0.at < $1.at }
            items.append(Session(key: "checkpoint:\(d.rawValue)", kind: .CHECKPOINT, title: "Checkpoint: \(d.title)",
                why: failed.map { "Last checkpoint scored \(pct($0.score)); \(pct(PASS)) moves you on. Retest after the reviews." } ?? "20 mixed \(d.title) questions. \(pct(PASS)) or more moves you on.",
                minutes: 35, steps: [Step(kind: .PRACTICE, count: 20), Step(kind: .REFLECT), Step(kind: .DEBRIEF)], domain: d, priority: 4,
                notBefore: failed.map { $0.at + DAY } ?? 0))
        }
        /* ── exam fitness: one stamina sitting and, if reading is slow, one reading drill a week ── */
        let week = today / (7 * DAY)
        func doneThisWeek(_ prefix: String) -> Bool { inp.results.contains { $0.key.hasPrefix(prefix) && now - $0.at < 6 * DAY } }
        if let n = inp.ladderNext, n < x.exam.totalItems, !doneThisWeek("ladder:") {
            items.append(Session(key: "ladder:\(n):\(week)", kind: .MIXED, title: "Stamina sitting: \(n) questions",
                why: "The next rung of your stamina ladder: \(n) mixed questions at exam pace, in one go. You move up when your accuracy holds in the last quarter.",
                minutes: n * 76 / 60 + 5, steps: [Step(kind: .PRACTICE, count: n, timed: true), Step(kind: .DEBRIEF)], priority: 3.5))
        }
        if inp.readingSlow && !doneThisWeek("reading:") {
            items.append(Session(key: "reading:\(week)", kind: .SKILLS, title: "Reading drill",
                why: "Each question shows its scenario first. Say to yourself what it's asking, then reveal the options. It trains reading speed and calm on long questions.",
                minutes: 15, steps: [Step(kind: .PRACTICE, count: 10, reading: true), Step(kind: .DEBRIEF)], priority: 3))
        }
        for l in x.lessons where l.module == "skills" && !inp.lessonsDone.contains(l.id) {
            items.append(Session(key: "lesson:\(l.id)", kind: .SKILLS, title: l.title, why: "Exam technique, once the content is in place.",
                minutes: l.minutes, steps: [Step(kind: .LESSON), Step(kind: .DEBRIEF)], lessonId: l.id, priority: 2))
        }

        /* ── 4. capacity ── */
        let taperStart = max(today, startOfDay(exam - TAPER_DAYS * DAY))
        var days: [Millis] = []
        var d = today
        while d < exam { if studyDays.contains(Time.weekday(d)) { days.append(d) }; d = nextDay(d) }
        let doneToday = inp.results.filter { startOfDay($0.at) == today }.reduce(0) { $0 + $1.minutes }
        func cap(_ day: Millis) -> Int { day == today ? max(0, perDay - doneToday) : perDay }
        let buildDays = days.filter { $0 < taperStart }
        let available = buildDays.reduce(0) { $0 + cap($1) }

        let weeksLeft = Int((exam - today) / (7 * DAY))
        let recentMocks = inp.mocks.filter { now - $0.at < 45 * DAY }.count
        let mockTarget = weeksLeft >= 4 ? 3 : 2
        var mocksNeeded = max(0, mockTarget - recentMocks)
        let mockMinimum = max(0, 2 - recentMocks)

        var needed = items.reduce(0) { $0 + $1.minutes } + mocksNeeded * perDay
        var dropped: [String] = []
        if needed > available {
            // Trim the least valuable work first: second drills, then reviews, extra foundations, skills, extra mocks.
            let order = items.filter { [.DRILL, .REVIEW, .FOUNDATION, .SKILLS].contains($0.kind) && $0.priority < 500 }
                .sorted { (dropRank($0.kind, $0.key), $0.priority) < (dropRank($1.kind, $1.key), $1.priority) }
            for s in order {
                if needed <= available { break }
                if let i = items.firstIndex(of: s) { items.remove(at: i) }
                needed -= s.minutes; dropped.append(s.title)
            }
            while needed > available && mocksNeeded > mockMinimum { mocksNeeded -= 1; needed -= perDay; dropped.append("A full mock exam") }
            if !dropped.isEmpty { adjustments.append("Time is short, so \(dropped.count) lower-value sessions were trimmed.") }
        }
        let behind = needed > available

        /* ── 5. order: urgent, foundations, weakest domains first, checkpoints closing each domain ── */
        let domainOrder = Domain.allCases.sorted { (domainAcc[$0] ?? 0.6) < (domainAcc[$1] ?? 0.6) }
        func di(_ dm: Domain?) -> Double { Double(dm.flatMap { domainOrder.firstIndex(of: $0) } ?? -1) }
        func orderKey(_ s: Session) -> Double {
            switch s.kind {
            case .REENTRY, .REMEDIATION: return -1000 - s.priority
            case .FOUNDATION: return s.priority >= 800 ? -900 : -500 - s.priority
            case .BUILD, .DRILL, .REVIEW: return di(s.domain) * 100 - s.priority - (s.kind == .BUILD ? 50 : 0)
            case .CHECKPOINT: return di(s.domain) * 100 + 90
            case .SKILLS: return 400 - s.priority
            case .MIXED: return s.key.hasPrefix("ladder:") ? 300 : 500
            default: return 500
            }
        }
        var queue = items.enumerated().sorted { (orderKey($0.element), $0.offset) < (orderKey($1.element), $1.offset) }.map(\.element)

        /* ── 6. lay the queue onto study days, then mocks and mixed practice in what's left ── */
        var planned: [Session] = []
        var used: [Millis: Int] = [:]
        for day in buildDays {
            var i = 0
            while i < queue.count && (used[day] ?? 0) < cap(day) {
                var s = queue[i]
                if s.notBefore > day { i += 1; continue }
                s.day = day
                planned.append(s); used[day, default: 0] += s.minutes
                queue.remove(at: i)
            }
        }
        // Anything that did not fit still shows, on the last build day, so nothing silently vanishes.
        let lastBuild = buildDays.last ?? today
        for var s in queue { s.day = max(lastBuild, min(s.notBefore, lastBuild)); planned.append(s) }

        let free = buildDays.filter { (used[$0] ?? 0) == 0 && cap($0) > 0 }
        let mockDays = mocksNeeded == 0 ? [] : spread(free, mocksNeeded, lastBuild)
        for (i, day) in mockDays.enumerated() {
            planned.append(Session(key: "mock:\(recentMocks + i + 1)", kind: .MOCK, title: "Full mock exam \(recentMocks + i + 1)",
                why: "The only evidence you can hold accuracy for \(x.exam.minutes) minutes. Set aside the whole sitting.",
                minutes: x.exam.minutes + 30, steps: [Step(kind: .MOCK), Step(kind: .DEBRIEF)], day: day, priority: 3))
        }
        for day in free where !mockDays.contains(day) {
            planned.append(Session(key: "mixed:\(day)", kind: .MIXED, title: "Mixed timed practice",
                why: "Every domain mixed, against the clock, the way the exam asks.",
                minutes: min(perDay, 45), steps: [Step(kind: .CARDS), Step(kind: .PRACTICE, count: 20, timed: true), Step(kind: .SCENARIOS, count: 3), Step(kind: .DEBRIEF)], day: day))
        }
        for day in days where day >= taperStart && day < startOfDay(exam - DAY) {
            planned.append(Session(key: "taper:\(day)", kind: .TAPER, title: "Light review",
                why: "Final week: nothing new. Cards, a few mixed questions, then rest.",
                minutes: min(perDay, 30), steps: [Step(kind: .CARDS), Step(kind: .PRACTICE, count: 10), Step(kind: .DEBRIEF)], day: day))
        }

        let sessions = planned.enumerated().sorted { (a, b) in
            a.element.day != b.element.day ? a.element.day < b.element.day
                : (orderKey(a.element), a.offset) < (orderKey(b.element), b.offset)
        }.map(\.element)
        let phase: String
        switch sessions.first?.kind {
        case nil: phase = "Exam day"
        case .FOUNDATION?: phase = "Foundations"
        case .SKILLS?, .MOCK?, .MIXED?: phase = "Exam readiness"
        case .TAPER?: phase = "Final week"
        default: phase = "Build"
        }
        if behind { adjustments.append("Behind: about \(needed / 60) hours of work for \(available / 60) hours left. Add hours, a study day, or move the date.") }
        return Plan(sessions: sessions, phase: phase, daysLeft: Int((exam - today) / DAY), minutesPerDay: perDay, neededMinutes: needed,
                    availableMinutes: available, behind: behind, dropped: dropped, adjustments: adjustments, tasks: tasks)
    }

    private static func dropRank(_ k: SessionKind, _ key: String) -> Int {
        if k == .DRILL && key.hasSuffix(":1") { return 0 }
        switch k { case .REVIEW: return 1; case .FOUNDATION: return 2; case .SKILLS: return 3; default: return 4 }
    }

    /** n days spread over the free days, the last as late as possible. */
    private static func spread(_ free: [Millis], _ n: Int, _ fallback: Millis) -> [Millis] {
        if free.isEmpty { return Array(repeating: fallback, count: n) }
        if free.count <= n { return free + Array(repeating: free.last!, count: n - free.count) }
        var out: [Millis] = []
        for i in 1...n {
            let d = free[Int((Double(free.count - 1) * Double(i) / Double(n)).rounded())]
            if !out.contains(d) { out.append(d) }
        }
        return out
    }
}
