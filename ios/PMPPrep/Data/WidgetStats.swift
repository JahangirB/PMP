import Foundation

/**
 * What the home-screen widget shows, saved by the app (in the shared App Group) whenever
 * progress changes. Days left and today's progress are worked out when the widget draws,
 * so they roll over at midnight without the app running.
 */
struct WidgetStats: Codable, Equatable {
    var examDate: Millis?
    /** The study day todayMinutes belongs to (start of day, local time). */
    var day: Millis
    var todayMinutes: Int
    var targetMinutes: Int
    var readiness: Int
    var lessonsDone: Int
    var lessonsTotal: Int
    /** Exam fitness 0–100, or −1 until there's enough evidence. */
    var fitness: Int = -1
    /** When your pace says you'll be ready to sit it, and how sure that is: one of the READY_ values. */
    var readyBy: Millis = 0
    var readyState: Int = WidgetStats.READY_UNKNOWN

    static let READY_UNKNOWN = 0, READY_NOW = 1, READY_PROJECTED = 2, READY_STALLED = 3

    func daysLeft(_ now: Millis) -> Int? { examDate.map { max(0, Int((Time.startOfDay($0) - Time.startOfDay(now)) / Time.dayMs)) } }

    /** Share of today's plan done, 0..1; zero once the day has turned. */
    func today(_ now: Millis) -> Double {
        Time.startOfDay(now) != day || targetMinutes <= 0 ? 0 : min(max(Double(todayMinutes) / Double(targetMinutes), 0), 1)
    }

    /** Plenty of time is green, turning red as the exam gets close: two months or more fully green, the last week fully red. */
    func daysLevel(_ now: Millis) -> Double? { daysLeft(now).map { min(max(Double($0 - 7) / 53, 0), 1) } }

    /** True when the projected ready date is on or before the exam (or you're ready now), false when after; nil when unknown. */
    func onTrack() -> Bool? {
        switch readyState {
        case Self.READY_NOW: return true
        case Self.READY_STALLED: return false
        case Self.READY_PROJECTED: return examDate.map { Time.startOfDay(readyBy) <= Time.startOfDay($0) }
        default: return nil
        }
    }

    func syllabus() -> Double { lessonsTotal == 0 ? 0 : min(max(Double(lessonsDone) / Double(lessonsTotal), 0), 1) }

    /** Red at 0, amber at ½, green at 1, blended in between. RGB 0–255. */
    static func heat(_ level: Double) -> (Double, Double, Double) {
        let red = (214.0, 69.0, 69.0), amber = (227.0, 162.0, 26.0), green = (46.0, 158.0, 91.0)
        let t = min(max(level, 0), 1)
        return t < 0.5 ? mix(red, amber, t * 2) : mix(amber, green, (t - 0.5) * 2)
    }

    /** A tile's background: the heat colour softened toward white in light mode, toward near-black in dark mode. */
    static func tint(_ level: Double, dark: Bool) -> (Double, Double, Double) {
        dark ? mix((31, 31, 35), heat(level), 0.42) : mix((255, 255, 255), heat(level), 0.34)
    }

    private static func mix(_ a: (Double, Double, Double), _ b: (Double, Double, Double), _ t: Double) -> (Double, Double, Double) {
        ((a.0 + (b.0 - a.0) * t).rounded(), (a.1 + (b.1 - a.1) * t).rounded(), (a.2 + (b.2 - a.2) * t).rounded())
    }

    static let appGroup = "group.com.jahangirb.pmpprep"
    static let key = "widgetStats"

    func save() {
        guard let d = try? JSONEncoder().encode(self) else { return }
        UserDefaults(suiteName: Self.appGroup)?.set(d, forKey: Self.key)
    }

    static func load() -> WidgetStats? {
        guard let d = UserDefaults(suiteName: appGroup)?.data(forKey: key) else { return nil }
        return try? JSONDecoder().decode(WidgetStats.self, from: d)
    }
}
