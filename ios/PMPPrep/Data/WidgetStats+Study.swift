import Foundation

extension WidgetStats {
    /** Minutes studied today: guided sessions or time answering, whichever is more. */
    static func minutesToday(_ results: [SessionResult], _ attempts: [Attempt], _ now: Millis) -> Int {
        let today = Time.startOfDay(now)
        let sessions = results.filter { Time.startOfDay($0.at) == today }.reduce(0) { $0 + $1.minutes }
        let answering = Int((Double(attempts.filter { Time.startOfDay($0.at) == today }.reduce(0) { $0 + $1.ms }) / 60_000).rounded())
        return max(sessions, answering)
    }

}
