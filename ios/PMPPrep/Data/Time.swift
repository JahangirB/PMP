import Foundation

/** Milliseconds since 1970, as the Android app and the backup file use. */
typealias Millis = Int64

enum Time {
    static let dayMs: Millis = 24 * 60 * 60 * 1000
    static func now() -> Millis { Millis(Date().timeIntervalSince1970 * 1000) }
    static func date(_ t: Millis) -> Date { Date(timeIntervalSince1970: Double(t) / 1000) }
    static func millis(_ d: Date) -> Millis { Millis(d.timeIntervalSince1970 * 1000) }
    /** Local midnight of the day holding [t]. */
    static func startOfDay(_ t: Millis) -> Millis { millis(Calendar.current.startOfDay(for: date(t))) }
    /** Calendar-correct across daylight-saving changes. */
    static func nextDay(_ d: Millis) -> Millis { startOfDay(d + dayMs + dayMs / 2) }
    static func prevDay(_ d: Millis) -> Millis { startOfDay(d - dayMs / 2) }
    /** java.util.Calendar day of week: Sunday 1 … Saturday 7. */
    static func weekday(_ t: Millis) -> Int { Calendar.current.component(.weekday, from: date(t)) }
    static func hour(_ t: Millis) -> Int { Calendar.current.component(.hour, from: date(t)) }
}

