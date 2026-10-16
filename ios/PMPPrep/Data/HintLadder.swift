import Foundation

/**
 * The coach's hint ladder (web §10.16.10). Three rungs, none naming the answer: the lens of the
 * question's delivery approach (with the lesson's own lens for its ECO task), the question's
 * mindset sentence, then one wrong option the person has not chosen, struck out with its
 * rationale; on a type with no options to strike, a trap from the lesson instead.
 * Offered in practice only: never in a mock, the diagnostic, a timed drill or a reading drill.
 * An answer given after a hint is recorded with h = rungs used and stays out of accuracy and pace.
 */
enum HintLadder {
    static let RUNGS = 3
    static let NOTE = "Hints keep the thinking yours. An answer given after one is kept out of your accuracy and pace."

    struct Hint: Equatable {
        let rung: Int
        let text: String
        let detail: String?
        /** The option id to cross out, on rung 3 for single and multiple response. */
        let strike: String?
    }

    /** The lesson that teaches an ECO task: domain modules only, as on the web. */
    static func lessonFor(_ ecoTask: String, lessons: [Lesson], modules: [LessonModule]) -> Lesson? {
        if ecoTask.isEmpty { return nil }
        let domainModules: Set<String> = Set(modules.filter { $0.domain != nil }.map { $0.id })
        return lessons.first { (l: Lesson) -> Bool in l.ecoTask == ecoTask && domainModules.contains(l.module) }
    }

    /** options is the order shown; chosen the options picked so far. */
    static func hint(_ q: Question, rung: Int, lesson: Lesson?, options: [Option], chosen: Set<String>) -> Hint {
        if rung <= 1 {
            if let ap = q.approach {
                var lens: String? = nil
                if let l = lesson {
                    for pair in l.lenses where pair.0 == ap.rawValue { lens = pair.1; break }
                }
                let detail: String? = (lens ?? "").isEmpty ? nil : lens
                return Hint(rung: 1, text: "Read the stem through the \(ap.rawValue) lens. What would that kind of team usually do first?",
                            detail: detail, strike: nil)
            }
            return Hint(rung: 1, text: "Read the stem for the role you hold and the stage the work is at. What comes first from there?",
                        detail: nil, strike: nil)
        }
        let mindset: String? = q.mindset.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty ? nil : q.mindset
        if rung == 2 { return Hint(rung: 2, text: "The principle this turns on:", detail: mindset, strike: nil) }
        if q.base == .single || q.base == .multi {
            if let o = options.first(where: { (x: Option) -> Bool in !x.correct && !chosen.contains(x.id) }) {
                let why: String? = o.rationale.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty ? nil : o.rationale
                return Hint(rung: 3, text: "One option you can rule out, and why:", detail: why, strike: o.id)
            }
        }
        if let trap = lesson?.traps.first, !trap.isEmpty {
            return Hint(rung: 3, text: "A trap the lesson on this task warns about:", detail: trap, strike: nil)
        }
        return Hint(rung: 3, text: "The principle this turns on:", detail: mindset, strike: nil)
    }
}

/**
 * The adaptive drill (web §10.02.2): each candidate weighted by how weak its ECO task looks,
 * (1 − acc)^1.5 + 0.05, where acc carries a prior of 0.5 over 4 answers so a task with two
 * answers is not settled evidence; unseen questions ×1.4, ones right last time ×0.5.
 * Lesson answers and hinted answers stay out of the task accuracy.
 */
enum AdaptivePicker {
    static let PRIOR = 0.5
    static let PRIOR_N = 4.0
    static let EXPONENT = 1.5
    static let FLOOR = 0.05
    static let UNSEEN_BOOST = 1.4
    static let RIGHT_DAMP = 0.5

    struct TaskStat { var n: Int = 0; var credit: Double = 0 }

    static func taskIndex(_ attempts: [Attempt]) -> [String: TaskStat] {
        var m: [String: TaskStat] = [:]
        for a in attempts where !a.ecoTask.isEmpty && a.source != Readiness.SOURCE_LESSON && a.hints == 0 {
            var e: TaskStat = m[a.ecoTask] ?? TaskStat()
            e.n += 1
            e.credit += a.credit
            m[a.ecoTask] = e
        }
        return m
    }

    /** lastCorrect is how the question went last time, or nil if never answered. */
    static func weight(_ task: TaskStat?, lastCorrect: Bool?) -> Double {
        let t: TaskStat = task ?? TaskStat()
        let acc: Double = (t.credit + PRIOR * PRIOR_N) / (Double(t.n) + PRIOR_N)
        var w: Double = pow(max(0, 1 - acc), EXPONENT) + FLOOR
        if lastCorrect == nil { w *= UNSEEN_BOOST } else if lastCorrect == true { w *= RIGHT_DAMP }
        return w
    }

    /** Weighted sample without replacement. A zero weight is never drawn while a positive one remains. */
    static func sample<T>(_ items: [T], _ weights: [Double], _ k: Int, _ rand: () -> Double) -> [T] {
        var pool: [(T, Double)] = []
        for (i, it) in items.enumerated() { pool.append((it, max(0, i < weights.count ? weights[i] : 0))) }
        var out: [T] = []
        let want: Int = min(k, pool.count)
        while out.count < want && !pool.isEmpty {
            let total: Double = pool.reduce(0) { $0 + $1.1 }
            var hit: Int = 0
            if total <= 0 {
                hit = min(pool.count - 1, Int(rand() * Double(pool.count)))
            } else {
                let t: Double = rand() * total
                var acc: Double = 0
                hit = pool.count - 1
                for j in 0..<pool.count {
                    acc += pool[j].1
                    if acc >= t { hit = j; break }
                }
            }
            out.append(pool[hit].0)
            pool.remove(at: hit)
        }
        return out
    }

    static func pick(_ qs: [Question], attempts: [Attempt], lastCorrect: (String) -> Bool?, count: Int,
                     rand: () -> Double = { Double.random(in: 0..<1) }) -> [Question] {
        if qs.count <= count { return qs.shuffled() }
        let tasks: [String: TaskStat] = taskIndex(attempts)
        let weights: [Double] = qs.map { (q: Question) -> Double in weight(tasks[q.ecoTask], lastCorrect: lastCorrect(q.id)) }
        return sample(qs, weights, count, rand)
    }
}

/** Pretest items in a mock (web §10.03): 10 of a full 180, the same share of smaller sittings. */
enum MockPlan {
    static let FULL_PRETEST = 10

    /** Scored items: round(scoredFull × n ÷ total), half up, at least 1; the rest are pretest. */
    static func pretestCount(_ n: Int, totalItems: Int) -> Int {
        if n <= 1 || totalItems <= 0 { return 0 }
        if n >= totalItems { return FULL_PRETEST }
        let scoredFull: Double = Double(totalItems - FULL_PRETEST)
        let scored: Int = max(1, Int((scoredFull * Double(n) / Double(totalItems) + 0.5).rounded(.down)))
        return max(0, n - scored)
    }
}
