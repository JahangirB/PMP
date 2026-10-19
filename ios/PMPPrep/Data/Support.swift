import Foundation

/** The shared `support` slice (docs/progress-format.md §4); 0 and "" mean not set. */
struct SupportState: Equatable {
    var pressedAt: Millis = 0
    var shownAt: Millis = 0
    var anchor: String = ""
}

/** Which support message is due. */
enum SupportDue: Equatable {
    case nothing, first, already
}

/**
 * The "please support" reminder (docs/progress-format.md §4), as pure functions over the shared
 * `opens` and `support` slices, so every device counts the same once they sync. Mirrors Android's Support.kt.
 */
enum Support {
    static let url: String = "https://ko-fi.com/T4R728ULEV"
    static let maxOpens: Int = 60
    static let streakDays: Int = 7
    static let quietMs: Millis = 30 * 24 * 60 * 60 * 1000

    static let title: String = "Enjoying PMP Prep?"
    static let firstBody: String = "You've studied 7 days in a row. If the app is helping you, please support it. It's free, has no ads, and your support keeps it going."
    static let alreadyBody: String = "If the app is helping you, please support it. It's free, has no ads, and your support keeps it going. If you have already supported, please ignore this message."

    private static func formatter(_ zone: TimeZone) -> DateFormatter {
        let f: DateFormatter = DateFormatter()
        f.calendar = Calendar(identifier: .gregorian)
        f.locale = Locale(identifier: "en_US_POSIX")
        f.timeZone = zone
        f.dateFormat = "yyyy-MM-dd"
        return f
    }

    /** The local date of t, YYYY-MM-DD. */
    static func day(_ t: Millis, zone: TimeZone = TimeZone.current) -> String {
        let f: DateFormatter = formatter(zone)
        return f.string(from: Time.date(t))
    }

    /** date (YYYY-MM-DD) moved by days: calendar arithmetic in UTC, so no clock change gets in the way. */
    static func addDays(_ date: String, _ days: Int) -> String {
        let utc: TimeZone = TimeZone(identifier: "UTC") ?? TimeZone.current
        let f: DateFormatter = formatter(utc)
        guard let start: Date = f.date(from: date) else { return date }
        var cal: Calendar = Calendar(identifier: .gregorian)
        cal.timeZone = utc
        guard let moved: Date = cal.date(byAdding: .day, value: days, to: start) else { return date }
        return f.string(from: moved)
    }

    /** opens with today added, sorted, the newest maxOpens kept. */
    static func recordOpen(_ opens: [String], _ today: String) -> [String] {
        var all: Set<String> = Set(opens)
        all.insert(today)
        let sorted: [String] = all.sorted()
        return Array(sorted.suffix(maxOpens))
    }

    /** Consecutive dates ending today in opens, counting only dates on or after the anchor. */
    static func streak(_ opens: [String], anchor: String, today: String) -> Int {
        let set: Set<String> = Set(opens)
        var n: Int = 0
        var d: String = today
        while set.contains(d) && (anchor.isEmpty || d >= anchor) {
            n += 1
            d = addDays(d, -1)
        }
        return n
    }

    /** Which message to show now, if any. busy: a mock exam or a guided session is under way. */
    static func due(_ opens: [String], _ s: SupportState, today: String, now: Millis, busy: Bool,
                    zone: TimeZone = TimeZone.current) -> SupportDue {
        if busy { return .nothing }
        if s.shownAt > 0 && day(s.shownAt, zone: zone) == today { return .nothing }
        let pressed: Bool = s.pressedAt > 0
        if pressed && now - s.pressedAt < quietMs { return .nothing }
        if pressed && s.shownAt < s.pressedAt + quietMs { return .already }
        let run: Int = streak(opens, anchor: s.anchor, today: today)
        if run >= streakDays { return pressed ? .already : .first }
        return .nothing
    }

    /** After the message shows: once today, and the 7-day count starts again tomorrow. */
    static func markShown(_ s: SupportState, now: Millis, today: String) -> SupportState {
        var out: SupportState = s
        out.shownAt = now
        out.anchor = addDays(today, 1)
        return out
    }

    /** The support button was pressed, in the message or in the menu. */
    static func markPressed(_ s: SupportState, now: Millis) -> SupportState {
        var out: SupportState = s
        out.pressedAt = now
        return out
    }
}
