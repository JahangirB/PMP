import Foundation

/*
 * The figures behind the analytics charts (web §11): rolling accuracy, the ECO task heat map,
 * the pace histogram, the calibration grid, the mistake-cause distribution and the formula
 * drill log by group. Lesson answers and answers reached with hints are left out, as they are
 * from accuracy and pace everywhere else.
 */

struct TrendPoint: Equatable { let i: Int; let v: Double; let full: Bool }

struct HeatCell: Equatable {
    let task: EcoTask
    let n: Int
    let accuracy: Double
    /** Under MIN_SAMPLE answers: shown grey with its count, never as a rate. */
    var thin: Bool { n < AnalyticsData.MIN_SAMPLE }
}

struct PaceBin: Equatable { let lo: Int; let hi: Int?; let n: Int }

/** Sure against Think so / Guess, each split into right and wrong. */
struct Calibration: Equatable {
    var sureRight: Int = 0
    var sureWrong: Int = 0
    var unsureRight: Int = 0
    var unsureWrong: Int = 0
    var sure: Int { sureRight + sureWrong }
    var unsure: Int { unsureRight + unsureWrong }
    var total: Int { sure + unsure }
}

struct DrillGroupStat: Equatable { let group: String; let n: Int; let right: Int; let last: Millis }

enum AnalyticsData {
    static let WINDOW = 50
    static let START = 10
    static let MIN_SAMPLE = 5
    static let BIN = 15
    static let BINS = 13

    /** The answers that count as evidence of what you know, oldest first. */
    static func evidence(_ attempts: [Attempt]) -> [Attempt] {
        attempts.filter { $0.source != Readiness.SOURCE_LESSON && $0.hints == 0 }.sorted { $0.at < $1.at }
    }

    /** Rolling mean credit over the last 50 answers, from the 10th answer on; full once 50 exist. */
    static func trend(_ attempts: [Attempt]) -> [TrendPoint] {
        let list: [Attempt] = evidence(attempts)
        if list.count < START { return [] }
        var out: [TrendPoint] = []
        var sum: Double = 0
        for i in 0..<list.count {
            sum += list[i].credit
            if i >= WINDOW { sum -= list[i - WINDOW].credit }
            if i + 1 < START { continue }
            let w: Int = min(i + 1, WINDOW)
            out.append(TrendPoint(i: i + 1, v: sum / Double(w), full: w >= WINDOW))
        }
        return out
    }

    static func heat(_ attempts: [Attempt], tasks: [EcoTask]) -> [HeatCell] {
        var n: [String: Int] = [:]
        var credit: [String: Double] = [:]
        for a in evidence(attempts) {
            n[a.ecoTask, default: 0] += 1
            credit[a.ecoTask, default: 0] += a.credit
        }
        return tasks.map { (t: EcoTask) -> HeatCell in
            let k: Int = n[t.id] ?? 0
            let acc: Double = k == 0 ? 0 : (credit[t.id] ?? 0) / Double(k)
            return HeatCell(task: t, n: k, accuracy: acc)
        }
    }

    /** Seconds per answer for timed answers (over half a second). */
    static func paceSamples(_ attempts: [Attempt]) -> [Double] {
        evidence(attempts).filter { $0.ms > 500 }.map { Double($0.ms) / 1000 }
    }

    /** Fifteen-second bins, 0–15 … 165–180, then 180+. */
    static func paceBins(_ samples: [Double]) -> [PaceBin] {
        var counts: [Int] = Array(repeating: 0, count: BINS)
        for v in samples {
            let k: Int = min(BINS - 1, max(0, Int((v / Double(BIN)).rounded(.down))))
            counts[k] += 1
        }
        var out: [PaceBin] = []
        for k in 0..<BINS {
            let hi: Int? = k == BINS - 1 ? nil : (k + 1) * BIN
            out.append(PaceBin(lo: k * BIN, hi: hi, n: counts[k]))
        }
        return out
    }

    static func median(_ v: [Double]) -> Double? {
        if v.isEmpty { return nil }
        let s: [Double] = v.sorted()
        let mid: Int = s.count / 2
        return s.count % 2 == 1 ? s[mid] : (s[mid - 1] + s[mid]) / 2
    }

    static func calibration(_ attempts: [Attempt]) -> Calibration {
        var c: Calibration = Calibration()
        for a in evidence(attempts) where a.confidence > 0 {
            if a.confidence >= Attempt.SURE {
                if a.correct { c.sureRight += 1 } else { c.sureWrong += 1 }
            } else {
                if a.correct { c.unsureRight += 1 } else { c.unsureWrong += 1 }
            }
        }
        return c
    }

    /** Tagged misses per cause, in the shared rules' order. */
    static func causes(_ tags: [MistakeTag]) -> [(RootCause, Int)] {
        var counts: [RootCause: Int] = [:]
        for t in tags { counts[t.cause, default: 0] += 1 }
        return RootCause.allCases.map { (c: RootCause) -> (RootCause, Int) in (c, counts[c] ?? 0) }
    }

    /** The formula lab's drill log by generator group, in first-seen order. */
    static func drillGroups(_ rows: [DrillRun]) -> [DrillGroupStat] {
        var order: [String] = []
        var n: [String: Int] = [:]
        var right: [String: Int] = [:]
        var last: [String: Millis] = [:]
        for r in rows {
            let k: String = DrillGenerators.groupOf(r.generator) ?? "other"
            if n[k] == nil { order.append(k) }
            n[k, default: 0] += 1
            if r.ok { right[k, default: 0] += 1 }
            last[k] = max(last[k] ?? 0, r.at)
        }
        return order.map { (k: String) -> DrillGroupStat in DrillGroupStat(group: k, n: n[k] ?? 0, right: right[k] ?? 0, last: last[k] ?? 0) }
    }
}
