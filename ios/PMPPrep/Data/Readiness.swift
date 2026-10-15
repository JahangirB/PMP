import Foundation

struct Component: Identifiable {
    let id: String
    let label: String
    let weight: Int
    let score: Double
    let ready: Bool
    let figure: String
    let detail: String
    var points: Double { score * Double(weight) }
}

struct ReadinessReport {
    let score: Int
    let components: [Component]
    let provisional: Bool
    let gates: [String]
    let nextAction: String
    let nextRoute: String
}

struct Streak {
    let current: Int
    let longest: Int
    let last14: [Bool]
    let activeInWindow: Int
    /** Today hasn't been studied yet but can be this week's rest day, so the streak isn't at risk. */
    var restDayFree: Bool = false
    /** Days kept by a rest day inside the current streak. */
    var restDays: Int = 0
}

struct GroupStat {
    let n: Int; let credit: Double; let right: Int; let meanSec: Double?
    var accuracy: Double { n == 0 ? 0 : credit / Double(n) }
}

/**
 * The web app's readiness model (§10.00), ported line for line: five components
 * (accuracy, coverage, mocks, pace, habit) weighted by §01.3. A component with no
 * evidence scores zero, and the total is provisional until enough has been answered
 * across enough tasks.
 */
struct Readiness {
    static let SOURCE_PRACTICE = "practice"
    static let SOURCE_EXAM = "exam"
    static let SOURCE_LESSON = "lesson"
    private static let FLOOR = 0.05

    let r: ReadinessConfig
    let exam: ExamLayout
    let tasks: [EcoTask]
    /** The exam's domain split: accuracy is each domain's accuracy weighted by it. */
    var domainWeights: [Domain: Double] = [:]

    func weightedAccuracy(_ attempts: [Attempt], _ now: Millis) -> (Double, Int, [Domain: Double]) {
        let (overall, n) = accuracy(attempts, now)
        if domainWeights.isEmpty { return (overall, n, [:]) }
        var byDomain: [Domain: Double] = [:]
        for d in Domain.allCases { byDomain[d] = accuracy(attempts, now) { $0.domain == d }.0 }
        let total = domainWeights.values.reduce(0, +)
        if total <= 0 { return (overall, n, byDomain) }
        return (Domain.allCases.reduce(0) { $0 + (domainWeights[$1] ?? 0) * byDomain[$1]! } / total, n, byDomain)
    }

    private func ratio(_ v: Double, _ lo: Double, _ hi: Double) -> Double { hi == lo ? 0 : min(max((v - lo) / (hi - lo), 0), 1) }

    func accuracy(_ attempts: [Attempt], _ now: Millis, _ filter: ((Attempt) -> Bool)? = nil) -> (Double, Int) {
        // Answers reached with hints say little about what you know: they stay out of accuracy and pace.
        var list = attempts.filter { $0.source != Self.SOURCE_LESSON && $0.hints == 0 }
        if let filter { list = list.filter(filter) }
        list = Array(list.suffix(r.accuracyWindow))
        var wSum = r.accuracyPriorN
        var cSum = r.accuracyPrior * r.accuracyPriorN
        for a in list {
            let ageDays = Double(now - a.at) / Double(Time.dayMs)
            let w = max(Self.FLOOR, pow(0.5, ageDays / r.accuracyHalflifeDays))
            wSum += w
            cSum += w * a.credit
        }
        return (cSum / wSum, list.count)
    }

    func group(_ attempts: [Attempt], _ key: (Attempt) -> String?) -> [String: GroupStat] {
        var m: [String: [Attempt]] = [:]
        for a in attempts where a.source != Self.SOURCE_LESSON {
            guard let k = key(a), !k.isEmpty else { continue }
            m[k, default: []].append(a)
        }
        return m.mapValues { l in
            let timed = l.filter { $0.ms > 500 }
            return GroupStat(n: l.count, credit: l.reduce(0) { $0 + $1.credit }, right: l.filter(\.correct).count,
                         meanSec: timed.isEmpty ? nil : Double(timed.reduce(0) { $0 + $1.ms } / Int64(timed.count)) / 1000)
        }
    }

    func medianPace(_ attempts: [Attempt]) -> (Double?, Int) {
        let secs = attempts.filter { $0.source != Self.SOURCE_LESSON && $0.hints == 0 }.suffix(r.accuracyWindow)
            .filter { $0.ms > 500 }.map { Double($0.ms) / 1000 }.sorted()
        if secs.isEmpty { return (nil, 0) }
        let mid = secs.count / 2
        return (secs.count % 2 == 1 ? secs[mid] : (secs[mid - 1] + secs[mid]) / 2, secs.count)
    }

    func streak(_ store: ProgressStore, _ now: Millis) -> Streak {
        var days = Set<Millis>()
        store.attempts.forEach { days.insert(Time.startOfDay($0.at)) }
        store.mocks.forEach { days.insert(Time.startOfDay($0.at)) }
        store.scenarioRuns.forEach { days.insert(Time.startOfDay($0.at)) }
        store.cards.values.forEach { if $0.last > 0 { days.insert(Time.startOfDay($0.last)) } }
        let today = Time.startOfDay(now)
        let kept = store.plan.restDays ? Readiness.withRestDays(days, today) : days
        var cursor = kept.contains(today) ? today : Time.prevDay(today)
        var cur = 0, rested = 0
        while kept.contains(cursor) { cur += 1; if !days.contains(cursor) { rested += 1 }; cursor = Time.prevDay(cursor) }
        var best = 0, run = 0
        var prev: Millis? = nil
        for d in kept.sorted() {
            run = prev != nil && Time.prevDay(d) == prev! ? run + 1 : 1
            best = max(best, run)
            prev = d
        }
        // The habit part of readiness counts real study days only.
        let window: [Bool] = (0..<r.habitWindowDays).reversed().map { i in
            var d = today
            for _ in 0..<i { d = Time.prevDay(d) }
            return days.contains(d)
        }
        let week = WeeklyReports.weekStart(today)
        let free = store.plan.restDays && !days.contains(today) && kept.contains(Time.prevDay(today)) &&
            !kept.contains { $0 >= week && $0 < today && !days.contains($0) }
        return Streak(current: cur, longest: best, last14: window, activeInWindow: window.filter { $0 }.count, restDayFree: free, restDays: rested)
    }

    /** Study days plus rest days: at most one missed day a week (Monday to Sunday) is kept, when it sits between two study days. */
    static func withRestDays(_ days: Set<Millis>, _ today: Millis) -> Set<Millis> {
        var out = days
        guard var d = days.min() else { return out }
        var used = Set<Millis>()
        while d < today {
            let next = Time.nextDay(d)
            if !days.contains(d) && out.contains(Time.prevDay(d)) && days.contains(next) {
                if used.insert(WeeklyReports.weekStart(d)).inserted { out.insert(d) }
            }
            d = next
        }
        return out
    }

    func coverage(_ attempts: [Attempt]) -> (Double, Int, [(EcoTask, Int)]) {
        let byTask = group(attempts) { $0.ecoTask }
        let target = r.coverageTargetPerTask
        var sum = 0.0, touched = 0
        var short: [(EcoTask, Int)] = []
        for t in tasks {
            let n = byTask[t.id]?.n ?? 0
            if n > 0 { touched += 1 }
            sum += Double(min(n, target)) / Double(target)
            if n < target { short.append((t, target - n)) }
        }
        short.sort { $0.1 > $1.1 }
        return (tasks.isEmpty ? 0 : sum / Double(tasks.count), touched, short)
    }

    func report(_ store: ProgressStore, _ now: Millis = Time.now()) -> ReadinessReport {
        let atts = store.attempts
        let (acc, accN, accByDomain) = weightedAccuracy(atts, now)
        let (cov, touched, short) = coverage(atts)

        let recent = Array(store.mocks.sorted { $0.at > $1.at }.prefix(r.mockWeights.count))
        var num = 0.0, den = 0.0
        for (i, m) in recent.enumerated() { num += r.mockWeights[i] * m.pct; den += r.mockWeights[i] }
        let mockRaw = den > 0 ? num / den : 0
        var stale = 1.0
        var staleDays = 0
        if let first = recent.first {
            staleDays = Int(floor(Double(now - first.at) / Double(Time.dayMs)))
            if Double(staleDays) > r.mockStaleAfterDays {
                stale = max(r.mockStaleFloor, pow(0.5, (Double(staleDays) - r.mockStaleAfterDays) / r.mockStaleHalflife))
            }
        }
        let mockScore = (den > 0 ? ratio(mockRaw, r.mockMin, r.mockMax) : 0) * stale

        let (median, paceN) = medianPace(atts)
        let paceReady = paceN >= r.paceMinSample
        let paceScore: Double
        if !paceReady || median == nil { paceScore = 0 }
        else if median! >= r.paceBand[0] && median! <= r.paceBand[1] { paceScore = 1 }
        else if median! < r.paceBand[0] { paceScore = ratio(median!, r.paceFastZero, r.paceBand[0]) }
        else { paceScore = ratio(r.paceSlowZero - median!, 0, r.paceSlowZero - r.paceBand[1]) }

        let st = streak(store, now)
        func pct(_ v: Double) -> String { "\(Int((v * 100).rounded()))%" }
        let accDetail: String = accN == 0 ? "No questions answered yet." :
            (accByDomain.isEmpty ? "" : "Each domain weighted as on the exam: " +
                Domain.allCases.map { "\($0.short) \(pct(accByDomain[$0]!)) × \(pct(domainWeights[$0] ?? 0))" }.joined(separator: ", ") +
                ". A domain you haven't practised counts as 50%. ") +
            "Recent answers count more (half-life \(Int(r.accuracyHalflifeDays)) days). Full marks at \(pct(r.accuracyMax)), none below \(pct(r.accuracyMin)). Across \(accN) answers."
        let comps = [
            Component(id: "accuracy", label: "Accuracy", weight: r.wAccuracy, score: ratio(acc, r.accuracyMin, r.accuracyMax), ready: accN > 0,
                      figure: accN > 0 ? pct(acc) : "—", detail: accDetail),
            Component(id: "coverage", label: "ECO coverage", weight: r.wCoverage, score: cov, ready: touched > 0, figure: "\(touched) / \(tasks.count)",
                      detail: "Counts up to \(r.coverageTargetPerTask) answers on each of the \(tasks.count) tasks, so breadth earns this, not volume."),
            Component(id: "mocks", label: "Mock exams", weight: r.wMocks, score: mockScore, ready: !recent.isEmpty,
                      figure: recent.isEmpty ? "none sat" : pct(mockRaw),
                      detail: recent.isEmpty ? "Scores zero until you sit one. A full sitting is the only evidence you can hold accuracy for \(exam.minutes) minutes."
                        : "Your last \(recent.count) sitting(s), the most recent counting most." + (stale < 1 ? " Discounted: the last one was \(staleDays) days ago." : "")),
            Component(id: "pace", label: "Pace", weight: r.wPace, score: paceScore, ready: paceReady,
                      figure: median.map { "\(Int($0.rounded()))s" } ?? "—",
                      detail: !paceReady ? "Needs \(r.paceMinSample) timed answers. You have \(paceN)."
                        : "Median time per question. Full marks between \(Int(r.paceBand[0])) and \(Int(r.paceBand[1])) seconds; too fast scores as badly as too slow."),
            Component(id: "habit", label: "Study habit", weight: r.wHabit, score: min(max(Double(st.activeInWindow) / Double(r.habitTargetDays), 0), 1),
                      ready: st.activeInWindow > 0, figure: "\(st.activeInWindow) / \(r.habitTargetDays)",
                      detail: "Days you studied in the last \(r.habitWindowDays), against a target of \(r.habitTargetDays)."),
        ]
        let total = comps.reduce(0) { $0 + $1.points }
        let n = atts.filter { $0.source != Self.SOURCE_LESSON }.count
        var gates: [String] = []
        if n < r.gateItems { gates.append("\(r.gateItems - n) more answers needed") }
        if touched < r.gateTasks { gates.append("\(r.gateTasks - touched) more ECO tasks to touch") }

        // The component with the most points still on the table decides the advice.
        let gap = comps.max { (1 - $0.score) * Double($0.weight) < (1 - $1.score) * Double($1.weight) }
        let action: String, route: String
        switch gap?.id {
        case "coverage": action = "Answer questions on tasks you haven't covered, starting with \(short.prefix(3).map(\.0.label).joined(separator: ", "))."; route = "practice"
        case "mocks": action = recent.isEmpty ? "Sit your first full mock exam: it is worth \(r.wMocks) of the 100 points on its own." : "Sit another full mock exam."; route = "mock"
        case "pace": action = "Run a timed drill and aim for \(Int(r.paceBand[0]))–\(Int(r.paceBand[1])) seconds a question."; route = "practice"
        case "habit": action = "Do something small today: a few flashcards or ten questions keep the habit going."; route = "flashcards"
        default: action = "Drill your weakest tasks and review every wrong answer in the mistake journal."; route = "practice"
        }
        return ReadinessReport(score: Int(total.rounded()), components: comps, provisional: !gates.isEmpty, gates: gates, nextAction: action, nextRoute: route)
    }
}
