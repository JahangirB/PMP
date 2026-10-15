import Foundation

/** One condition for booking, and whether it is met. */
struct BookingCriterion: Identifiable { var id: String { label }; let label: String; let target: String; let now: String; let met: Bool }

/** When the trend says the unmet scores will reach their targets. */
struct BookingProjection { let earliest: Millis?; let latest: Millis?; let rough: Bool; let stalled: [String]; let afterExam: Bool }

struct BookingReport {
    let criteria: [BookingCriterion]
    let projection: BookingProjection?
    let mocksNeeded: Int
    var ready: Bool { criteria.allSatisfy(\.met) }
}

/** "Am I ready to book?" The thresholds are the app's judgement, not PMI's. */
enum Booking {
    static let READINESS = 75
    static let FITNESS = 70
    static let MOCKS = 2
    static let MOCK_PASS = 0.65
    private static let WEEK = 7 * Time.dayMs

    static func check(readiness: Int, fitness: Int?, mocks: [MockResult], fullItems: Int, weekly: [WeeklyReport], examDate: Millis?) -> BookingReport {
        let good = mocks.filter { $0.n >= fullItems && $0.pct >= MOCK_PASS }.count
        let criteria = [
            BookingCriterion(label: "Readiness", target: "\(READINESS)+", now: "\(readiness)", met: readiness >= READINESS),
            BookingCriterion(label: "Exam fitness", target: "\(FITNESS)+", now: fitness.map(String.init) ?? "not yet", met: (fitness ?? 0) >= FITNESS),
            BookingCriterion(label: "Full mocks at Target or above", target: "\(MOCKS)", now: "\(good)", met: good >= MOCKS),
        ]
        let weeks = weekly.sorted { $0.weekStart < $1.weekStart }
        var series: [(String, Int, [(Millis, Int)])] = []
        if readiness < READINESS { series.append(("Readiness", READINESS, weeks.map { ($0.weekStart, $0.readiness) })) }
        if (fitness ?? 0) < FITNESS { series.append(("Exam fitness", FITNESS, weeks.compactMap { w in w.fitness.map { (w.weekStart, $0) } })) }
        let projection = series.isEmpty || weeks.count < 2 ? nil : project(series, examDate)
        return BookingReport(criteria: criteria, projection: projection, mocksNeeded: max(0, MOCKS - good))
    }

    private static func project(_ series: [(String, Int, [(Millis, Int)])], _ examDate: Millis?) -> BookingProjection {
        var stalled: [String] = []
        var earliest: Millis = 0, latest: Millis = 0
        var open = false
        let range = series.allSatisfy { $0.2.count >= 4 }
        for (label, target, pts) in series {
            guard let f = fit(pts), f.slope > 0 else { stalled.append(label); continue }
            let lastX = pts.last!.0
            func at(_ slope: Double) -> Millis {
                let weeks = max(0, (Double(target) - f.value(lastX)) / slope)
                return lastX + Millis(ceil(weeks * 7 - 1e-6)) * Time.dayMs
            }
            if range {
                earliest = max(earliest, at(f.slope + f.se))
                if f.slope - f.se <= 0 { open = true } else { latest = max(latest, at(f.slope - f.se)) }
            } else {
                latest = max(latest, at(f.slope))
            }
        }
        if !stalled.isEmpty { return BookingProjection(earliest: nil, latest: nil, rough: !range, stalled: stalled, afterExam: false) }
        let late: Millis? = open ? nil : latest
        let after = examDate != nil && (late == nil || late! > examDate!)
        return BookingProjection(earliest: range ? earliest : nil, latest: late, rough: !range, stalled: stalled, afterExam: after)
    }

    private struct Fit {
        let slope: Double, intercept: Double, se: Double
        func value(_ x: Millis) -> Double { intercept + slope * (Double(x) / Double(WEEK)) }
    }

    /** Least squares in points per week. */
    private static func fit(_ pts: [(Millis, Int)]) -> Fit? {
        if pts.count < 2 { return nil }
        let xs = pts.map { Double($0.0) / Double(WEEK) }, ys = pts.map { Double($0.1) }
        let mx = xs.reduce(0, +) / Double(xs.count), my = ys.reduce(0, +) / Double(ys.count)
        let sxx = xs.reduce(0) { $0 + ($1 - mx) * ($1 - mx) }
        if sxx == 0 { return nil }
        let slope = xs.indices.reduce(0) { $0 + (xs[$1] - mx) * (ys[$1] - my) } / sxx
        let intercept = my - slope * mx
        let se = pts.count < 3 ? 0 : sqrt(xs.indices.reduce(0) { let r = ys[$1] - (intercept + slope * xs[$1]); return $0 + r * r } / Double(pts.count - 2) / sxx)
        return Fit(slope: slope, intercept: intercept, se: se)
    }
}
