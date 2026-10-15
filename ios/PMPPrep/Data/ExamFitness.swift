import Foundation

/*
 * Exam fitness: are you ready to sit the exam, not just to know it?
 *   Stamina (40%)      can you hold accuracy and pace to question 180?
 *   Composure (35%)    can you read and decide steadily under the clock?
 *   Consistency (25%)  will you keep showing up until exam day?
 * Built from behaviour in the app, plus optional check-ins. It describes what happened;
 * it never diagnoses.
 */

/** One measured behaviour. score 0..1, or nil while there isn't enough evidence. */
struct FitnessSignal: Identifiable { let id: String; let label: String; let weight: Int; let score: Double?; let figure: String; let detail: String }

struct FitnessPart: Identifiable {
    let id: String; let label: String; let weight: Int; let signals: [FitnessSignal]
    /** Why the part isn't scored yet, beyond missing signals; nil when it may be. */
    var gate: String? = nil
    /** Weighted over the signals that have evidence, once they carry at least half the weight. */
    var score: Double? {
        if gate != nil { return nil }
        let ready = signals.filter { $0.score != nil }
        let w = ready.reduce(0) { $0 + $1.weight }
        if w == 0 || w * 2 < signals.reduce(0, { $0 + $1.weight }) { return nil }
        return ready.reduce(0) { $0 + $1.score! * Double($1.weight) } / Double(w)
    }
}

struct FitnessReport {
    let parts: [FitnessPart]
    let score: Int?
    let provisional: Bool
    let finding: String?
    let next: String?
    let ladderRung: Int
    let ladderNext: Int?
    let wpm: Double?
    let longest: Int
    let freezeRate: Double?
    let worry: Int?
    let worryHighForWeeks: Bool
    let link: String?
    let momentumLow: Bool
    func part(_ id: String) -> FitnessPart { parts.first { $0.id == id }! }
    func signal(_ id: String) -> FitnessSignal { parts.flatMap(\.signals).first { $0.id == id }! }
}

struct FitnessInput {
    let attempts: [Attempt]
    let sittings: [SittingEvent]
    let checkIns: [CheckIn]
    let results: [SessionResult]
    let studyDays: Set<Int>
    let hoursPerWeek: Int
    let now: Millis
}

enum ExamFitness {
    static let DAY: Millis = 86_400_000
    static let MINUTE: Millis = 60_000
    static let SITTING_GAP = 10 * MINUTE
    static let LADDER = [20, 45, 70, 100, 140, 180]
    static let TARGET_WPM = 200.0
    static let LOW_WPM = 100.0
    static let WEEKLY_STATEMENTS = [
        "When I see a long question, my mind goes blank.",
        "I worry about running out of time.",
        "Thinking about the exam makes it hard to start studying.",
        "I change answers I was sure about.",
        "I feel able to stay focused for four hours.",
    ]

    private static func ratio(_ v: Double, _ lo: Double, _ hi: Double) -> Double { hi == lo ? 0 : min(max((v - lo) / (hi - lo), 0), 1) }
    private static func pct(_ v: Double) -> String { "\(Int((v * 100).rounded()))%" }
    private static func median(_ xs: [Double]) -> Double? {
        if xs.isEmpty { return nil }
        let s = xs.sorted(); let m = s.count / 2
        return s.count % 2 == 1 ? s[m] : (s[m - 1] + s[m]) / 2
    }
    private static func mean(_ xs: [Double]) -> Double { xs.isEmpty ? 0 : xs.reduce(0, +) / Double(xs.count) }

    /** Runs of answers with no long gap, in the order they were given. Mock answers are their own sitting. */
    static func sittings(_ attempts: [Attempt]) -> [[Attempt]] {
        var out: [[Attempt]] = []
        var prev: Attempt? = nil
        for a in attempts where a.source != Readiness.SOURCE_LESSON {
            let split = prev == nil || a.at - prev!.at > SITTING_GAP || (a.source == Readiness.SOURCE_EXAM) != (prev!.source == Readiness.SOURCE_EXAM)
            if split { out.append([]) }
            out[out.count - 1].append(a)
            prev = a
        }
        return out
    }

    /** Accuracy drop from the first quarter of a sitting to the last. */
    static func fade(_ s: [Attempt]) -> Double {
        let q = s.count / 4
        if q == 0 { return 0 }
        return mean(s.prefix(q).map(\.credit)) - mean(s.suffix(q).map(\.credit))
    }

    /** Last quarter's median time over the first quarter's, minus one; nil when times weren't measured per question. */
    static func drift(_ s: [Attempt]) -> Double? {
        let q = s.count / 4
        if q < 5 || Set(s.map(\.ms)).count < 3 { return nil }
        guard let a = median(s.prefix(q).map { Double($0.ms) }), let b = median(s.suffix(q).map { Double($0.ms) }), a > 0 else { return nil }
        return b / a - 1
    }

    /** Sittings closed part-way and not finished. */
    private static func abandoned(_ events: [SittingEvent], since: Millis) -> (Int, Int) {
        let recent = events.filter { $0.at >= since }
        let finishedKeys = Set(recent.filter(\.finished).map(\.key))
        let left = Set(recent.filter { !$0.finished && $0.answered > 0 }.map(\.key)).filter { !finishedKeys.contains($0) }
        return (finishedKeys.count, left.count)
    }

    private static func minutes(_ inp: FitnessInput, _ from: Millis, _ to: Millis) -> Int {
        let s = inp.results.filter { $0.at >= from && $0.at < to }.reduce(0) { $0 + $1.minutes }
        let a = Int((Double(inp.attempts.filter { $0.at >= from && $0.at < to }.reduce(0) { $0 + $1.ms }) / Double(MINUTE)).rounded())
        return max(s, a)
    }

    static func report(_ inp: FitnessInput) -> FitnessReport {
        let now = inp.now
        let atts = inp.attempts.filter { $0.source != Readiness.SOURCE_LESSON }
        let sits = sittings(inp.attempts)
        let firstActivity = [atts.map(\.at).min(), inp.results.map(\.at).min()].compactMap { $0 }.min()
        let ageDays = firstActivity.map { Int((now - $0) / DAY) } ?? 0

        /* ── stamina ── */
        let recentSits = sits.filter { $0.last!.at >= now - 30 * DAY }
        let longest = recentSits.map(\.count).max() ?? 0
        let long = Array(sits.filter { $0.count >= 60 && $0.last!.at >= now - 60 * DAY }.suffix(3))
        let fades = long.map { fade($0) }
        let drifts = long.compactMap { drift($0) }
        let (finLong, leftLong) = abandoned(inp.sittings.filter { $0.planned >= 40 }, since: now - 60 * DAY)
        let sits40 = sits.filter { $0.count >= 40 }.count
        let avgFade = fades.isEmpty ? nil : mean(fades)
        let avgDrift = drifts.isEmpty ? nil : mean(drifts)
        let stamina = FitnessPart(id: "stamina", label: "Stamina", weight: 40, signals: [
            FitnessSignal(id: "longest", label: "Longest sitting", weight: 35, score: recentSits.isEmpty ? nil : min(1, Double(longest) / 180),
                figure: "\(longest) questions", detail: "Your longest run without a break over 10 minutes, in the last 30 days. Full marks at 180, the whole exam."),
            FitnessSignal(id: "fade", label: "Accuracy fade", weight: 30, score: avgFade.map { 1 - ratio($0, 0.05, 0.20) },
                figure: avgFade.map { "\($0 >= 0 ? "−" : "+")\(Int((abs($0) * 100).rounded())) pts" } ?? "not yet",
                detail: avgFade == nil ? "Needs a sitting of 60+ questions." : "Last quarter against first quarter of your last \(fades.count) long sitting(s). Up to 5 points down is full marks; 20 or more is zero."),
            FitnessSignal(id: "drift", label: "Pace drift", weight: 20, score: avgDrift.map { r in
                    r > 0.10 ? 1 - ratio(r, 0.10, 0.40) : (r < -0.25 ? 1 - ratio(-r, 0.25, 0.50) : 1) },
                figure: avgDrift.map { "\($0 >= 0 ? "+" : "−")\(Int((abs($0) * 100).rounded()))%" } ?? "not yet",
                detail: avgDrift == nil ? "Needs a long sitting with each question timed." : "Time per question in the last quarter against the first. Up to 10% slower is full marks; much faster late on counts too, as it usually means guessing to finish."),
            FitnessSignal(id: "finish", label: "Finishing long sittings", weight: 15,
                score: finLong + leftLong >= 2 ? Double(finLong) / Double(finLong + leftLong) : nil,
                figure: finLong + leftLong == 0 ? "not yet" : "\(finLong) of \(finLong + leftLong)",
                detail: "Mocks and sittings of 40+ questions finished rather than left part-way, last 60 days."),
        ], gate: sits40 >= 2 ? nil : "Scored after two sittings of 40+ questions (you have \(sits40)).")

        /* ── composure ── */
        let recentAtts = Array(atts.suffix(200))
        let reading = recentAtts.filter { $0.firstTapMs > 1000 && $0.words > 0 }
        let wpm = reading.count >= 30 ? median(reading.map { Double($0.words) / (Double($0.firstTapMs) / 60_000) }) : nil
        let withIdle = recentAtts.filter { $0.idleMs > 0 }
        let medianMs = median(recentAtts.filter { $0.ms > 500 && !$0.timed }.map { Double($0.ms) }) ?? 76_000
        let freezeAt = max(150_000, 3 * medianMs)
        let freezeRate = withIdle.count >= 30 ? Double(withIdle.filter { Double($0.idleMs) >= freezeAt }.count) / Double(withIdle.count) : nil
        var longStemFreeze = false
        if withIdle.count >= 30 {
            let mw = median(withIdle.map { Double($0.words) }) ?? 0
            let longQ = withIdle.filter { Double($0.words) > mw }, shortQ = withIdle.filter { Double($0.words) <= mw }
            longStemFreeze = !longQ.isEmpty && !shortQ.isEmpty &&
                Double(longQ.filter { Double($0.idleMs) >= freezeAt }.count) / Double(longQ.count) > 2 * Double(shortQ.filter { Double($0.idleMs) >= freezeAt }.count) / Double(shortQ.count)
        }
        var slow = 0, panic = 0
        for s in sits.filter({ !$0.isEmpty && $0.first!.source != Readiness.SOURCE_EXAM }).suffix(20) {
            guard let m = median(s.filter { $0.ms > 500 }.map { Double($0.ms) }) else { continue }
            let avg = mean(s.map(\.credit))
            if s.count > 3 {
                for i in 0..<(s.count - 3) where Double(s[i].ms) > 2 * m {
                    slow += 1
                    let following = s[(i + 1)...(i + 3)]
                    if following.allSatisfy({ Double($0.ms) < 0.5 * m }) && mean(following.map(\.credit)) < avg { panic += 1 }
                }
            }
        }
        let panicRate = slow >= 10 ? Double(panic) / Double(slow) : nil
        let timedA = Array(atts.filter(\.timed).suffix(200))
        let untimedA = Array(atts.filter { !$0.timed }.suffix(200))
        let gap = timedA.count >= 20 && untimedA.count >= 20 ? mean(untimedA.map(\.credit)) - mean(timedA.map(\.credit)) : nil
        let changes = atts.suffix(300)
        let rw = changes.reduce(0) { $0 + $1.rightToWrong }, wr = changes.reduce(0) { $0 + $1.wrongToRight }
        let composure = FitnessPart(id: "composure", label: "Composure", weight: 35, signals: [
            FitnessSignal(id: "reading", label: "Reading pace", weight: 30, score: wpm.map { ratio($0, LOW_WPM, TARGET_WPM) },
                figure: wpm.map { "\(Int($0.rounded())) wpm" } ?? "not yet",
                detail: wpm == nil ? "Needs 30 answered questions with reading time recorded."
                    : "Words read per minute before your first tap. About \(Int(TARGET_WPM)) reads a typical 150-word question in 45 seconds, leaving 30 to decide."),
            FitnessSignal(id: "freeze", label: "Freezes", weight: 20, score: freezeRate.map { 1 - ratio($0, 0.02, 0.15) },
                figure: freezeRate.map { pct($0) } ?? "not yet",
                detail: freezeRate == nil ? "Needs 30 questions answered in practice."
                    : "Questions where nothing happened for over \(Int((freezeAt / 60_000).rounded())) minutes" + (longStemFreeze ? ". They happen mostly on long questions." : ".")),
            FitnessSignal(id: "rush", label: "Rushing after a slow one", weight: 20, score: panicRate.map { 1 - ratio($0, 0.10, 0.50) },
                figure: panicRate.map { pct($0) } ?? "not yet",
                detail: panicRate == nil ? "Needs 10 slow questions in practice." : "After a question that took twice your usual time, how often the next three were rushed and went worse."),
            FitnessSignal(id: "pressure", label: "Pressure gap", weight: 15, score: gap.map { 1 - ratio($0, 0.05, 0.20) },
                figure: gap.map { "\($0 >= 0 ? "−" : "+")\(Int((abs($0) * 100).rounded())) pts" } ?? "not yet",
                detail: gap == nil ? "Needs 20 timed and 20 untimed answers." : "Accuracy against the clock compared with relaxed practice. Up to 5 points lower is full marks."),
            FitnessSignal(id: "doubt", label: "Second-guessing", weight: 15, score: rw + wr >= 10 ? ratio(Double(wr) / Double(rw + wr), 0.35, 0.60) : nil,
                figure: rw + wr == 0 ? "not yet" : "\(rw) right→wrong, \(wr) wrong→right",
                detail: "Answers changed before checking. More right-to-wrong than wrong-to-right means doubt is costing marks."),
        ])

        /* ── consistency ── */
        var days = Set<Millis>()
        inp.attempts.forEach { days.insert(Time.startOfDay($0.at)) }
        inp.results.forEach { days.insert(Time.startOfDay($0.at)) }
        let today = Time.startOfDay(now)
        let sd = inp.studyDays.isEmpty ? Set(1...7) : inp.studyDays
        let planned28 = (0..<28).filter { sd.contains(Time.weekday(today - Millis($0) * DAY)) }.count
        let studied28 = days.filter { $0 > today - 28 * DAY }.count
        let mins14 = minutes(inp, now - 14 * DAY, now + 1)
        let target14 = inp.hoursPerWeek * 2 * 60
        let thisWeek = minutes(inp, now - 7 * DAY, now + 1)
        let prevAvg = Double(minutes(inp, now - 28 * DAY, now - 7 * DAY)) / 3
        let momentum: Double? = ageDays < 14 ? nil : (prevAvg <= 0 ? (thisWeek > 0 ? 1 : 0) : ratio(Double(thisWeek) / prevAvg, 0.5, 1))
        let (finAll, leftAll) = abandoned(inp.sittings, since: now - 28 * DAY)
        let sortedDays = days.filter { $0 > today - 60 * DAY }.sorted()
        let gapsOff = zip(sortedDays, sortedDays.dropFirst()).map { Int(($1 - $0) / DAY - 1) }.filter { $0 >= 3 }
        let gapMean = mean(gapsOff.map(Double.init))
        let consistency = FitnessPart(id: "consistency", label: "Consistency", weight: 25, signals: [
            FitnessSignal(id: "regular", label: "Regular days", weight: 25, score: ageDays >= 7 ? min(1, Double(studied28) / Double(max(planned28, 1))) : nil,
                figure: "\(studied28) of \(planned28)", detail: "Days you studied in the last 28, against your planned study days."),
            FitnessSignal(id: "hours", label: "Hours kept", weight: 25, score: ageDays >= 7 && target14 > 0 ? min(1, Double(mins14) / Double(target14)) : nil,
                figure: "\(mins14 / 60) h \(mins14 % 60) m", detail: "Study time in the last 14 days against your planned \(inp.hoursPerWeek) hours a week."),
            FitnessSignal(id: "momentum", label: "Momentum", weight: 20, score: momentum,
                figure: ageDays < 14 ? "not yet" : "\(thisWeek) min vs \(Int(prevAvg.rounded())) avg",
                detail: "This week's minutes against your average for the three weeks before. A falling trend is the earliest sign motivation is slipping."),
            FitnessSignal(id: "sessions", label: "Finishing sessions", weight: 15, score: finAll + leftAll >= 3 ? Double(finAll) / Double(finAll + leftAll) : nil,
                figure: finAll + leftAll == 0 ? "not yet" : "\(finAll) of \(finAll + leftAll)", detail: "Sessions and practice sets finished rather than left part-way, last 28 days."),
            FitnessSignal(id: "bounce", label: "Bouncing back", weight: 15, score: ageDays >= 21 ? (gapsOff.isEmpty ? 1 : 1 - ratio(gapMean, 3, 10)) : nil,
                figure: gapsOff.isEmpty ? "no long gaps" : "\(gapsOff.count) gap(s), avg \(Int(gapMean.rounded())) days",
                detail: "Breaks of 3+ days in the last 60 days, and how long they lasted."),
        ])

        /* ── overall ── */
        let parts = [stamina, composure, consistency]
        let scored = parts.filter { $0.score != nil }
        let total = scored.isEmpty ? nil : Int((scored.reduce(0) { $0 + $1.score! * Double($1.weight) } / Double(scored.reduce(0) { $0 + $1.weight }) * 100).rounded())

        // The biggest shortfall among signals with evidence decides the finding and the next step.
        let worst = parts.filter { $0.gate == nil }.flatMap { p in p.signals.filter { $0.score != nil }.map { (p, $0) } }
            .max { (1 - $0.1.score!) * Double($0.1.weight * $0.0.weight) < (1 - $1.1.score!) * Double($1.1.weight * $1.0.weight) }
            .flatMap { $0.1.score! < 0.9 ? $0.1 : nil }
        let rung = LADDER.filter { r in sits.contains { s in s.count >= r && (s.count < 60 || fade(s) < 0.10) } }.max() ?? 0
        let nextRung = LADDER.first { $0 > rung }
        let (finding, next) = advice(worst, wpm, longest, longStemFreeze, nextRung)

        /* ── check-ins ── */
        let weekly = inp.checkIns.filter { $0.kind == "weekly" && $0.items.count == WEEKLY_STATEMENTS.count }.sorted { $0.at < $1.at }
        func worryOf(_ c: CheckIn) -> Int {
            let adjusted = c.items.enumerated().map { $0.offset == WEEKLY_STATEMENTS.count - 1 ? 6 - $0.element : $0.element }
            return Int((Double(adjusted.reduce(0, +) - adjusted.count) * 100 / Double(adjusted.count * 4)).rounded())
        }
        let worry = weekly.last.map { worryOf($0) }
        let worryHigh = weekly.count >= 3 && weekly.suffix(3).allSatisfy { worryOf($0) >= 75 }
        return FitnessReport(parts: parts, score: total, provisional: scored.count < parts.count, finding: finding, next: next,
                             ladderRung: rung, ladderNext: nextRung, wpm: wpm, longest: longest, freezeRate: freezeRate,
                             worry: worry, worryHighForWeeks: worryHigh, link: feelingLink(inp), momentumLow: momentum != nil && momentum! < 0.5)
    }

    private static func advice(_ worst: FitnessSignal?, _ wpm: Double?, _ longest: Int, _ longStem: Bool, _ nextRung: Int?) -> (String?, String?) {
        switch worst?.id {
        case nil: return (nil, nextRung.map { "A \($0)-question timed sitting: the next rung of your stamina ladder." })
        case "longest": return ("Your longest sitting is \(longest) questions; the exam is 180.", "A \(nextRung ?? 45)-question timed sitting, the next rung of your stamina ladder.")
        case "fade": return ("Your accuracy fades in the last part of long sittings.", "Keep to your ladder rung until the fade is under 10 points, and use the break points to stand, drink water and look away.")
        case "drift": return ("You slow down, or rush, late in long sittings.", "Watch the pacing checkpoints at questions 60 and 120 in your next long sitting.")
        case "finish": return ("Some long sittings were left part-way.", "Pick a sitting length you can finish; the ladder builds up from there.")
        case "reading": return ("You read about \(Int((wpm ?? 0).rounded())) words a minute; the exam needs about \(Int(TARGET_WPM)).", "Reading drills: name what each question asks before you see the options, and read the last sentence first.")
        case "freeze": return ("Some questions stop you for minutes" + (longStem ? ", mostly long ones." : "."), "When stuck: breathe out slowly, read the last sentence, rule out two options, then move on.")
        case "rush": return ("After a slow question you tend to rush the next few.", "One slow question is normal. Take the next one at your usual pace; the clock has room for it.")
        case "pressure": return ("You score noticeably lower against the clock.", "More timed practice, starting with generous time and tightening week by week.")
        case "doubt": return ("Changing answers is costing you marks.", "Trust your first answer unless you find a specific reason; the debrief shows which changes cost marks.")
        case "regular": return ("You're studying on fewer days than planned.", "Minimum days: even 10 cards or 10 questions keep the rhythm.")
        case "hours": return ("You're short of your planned hours.", "Use the 15-minute version on busy days rather than skipping.")
        case "momentum": return ("Your study time has dropped compared with recent weeks.", "A lighter week of minimum days rebuilds the habit without pressure.")
        case "sessions": return ("Some sessions are left unfinished.", "Choose the 15-minute version when time is short, so the session gets finished.")
        case "bounce": return ("Breaks of several days are taking a while to recover from.", "After a break, start with the warm-up, not catch-up.")
        default: return (nil, nil)
        }
    }

    /** One link between how you felt and how you read, if the check-ins show a clear one. */
    private static func feelingLink(_ inp: FitnessInput) -> String? {
        let pre = inp.checkIns.filter { $0.kind == "pre" }
        func wpmAfter(_ cs: [CheckIn]) -> (Double?, Int) {
            let xs = cs.flatMap { c in inp.attempts.filter { $0.at >= c.at && $0.at <= c.at + 3 * 3_600_000 && $0.firstTapMs > 1000 && $0.words > 0 } }
                .map { Double($0.words) / (Double($0.firstTapMs) / 60_000) }
            return (median(xs), xs.count)
        }
        let (low, nl) = wpmAfter(pre.filter { $0.value <= 2 })
        let (high, nh) = wpmAfter(pre.filter { $0.value >= 4 })
        guard let low, let high, nl >= 10, nh >= 10 else { return nil }
        let diff = 1 - low / high
        return diff >= 0.10 ? "On days you felt low before studying, you read \(Int((diff * 100).rounded()))% slower than on good days." : nil
    }
}
