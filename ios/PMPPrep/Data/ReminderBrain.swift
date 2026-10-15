import Foundation

/*
 * Smart study reminders: learns which hours get you studying, from when you study in the app,
 * how long it stays open at each hour, and whether each reminder was opened and followed by study.
 * Every waking hour carries a score; each day a few hours are picked by Thompson sampling (mostly
 * the best, sometimes a less-tried one), within a daily cap. Pure logic; ReminderScheduler books
 * the notifications. iPhone has no equivalent of Android's Usage access, so phone activity isn't used.
 */

enum ReminderKind: String, Codable { case STUDY, STREAK, SUMMARY }
enum HourRule: String, Codable { case AUTO, PINNED, BLOCKED }

/** Parts of the day offered at setup ("When do you usually study?"). */
enum StudyWindow: String, Codable, CaseIterable, Identifiable {
    case EARLY, MORNING, LUNCH, AFTERNOON, EVENING, LATE
    var id: String { rawValue }
    var label: String {
        switch self { case .EARLY: "Early morning"; case .MORNING: "Morning"; case .LUNCH: "Lunch"; case .AFTERNOON: "Afternoon"; case .EVENING: "Evening"; case .LATE: "Late evening" }
    }
    var hours: ClosedRange<Int> {
        switch self { case .EARLY: 5...7; case .MORNING: 8...11; case .LUNCH: 12...13; case .AFTERNOON: 14...16; case .EVENING: 17...20; case .LATE: 21...23 }
    }
}

/** One reminder sent. id is the time it was due. */
struct ReminderLog: Codable, Equatable {
    var id: Millis; var hour: Int; var kind: ReminderKind
    var openedAt: Millis? = nil; var tapped: Bool = false; var studiedMinutes: Int = 0; var notToday: Bool = false
}

struct ReminderSettings: Codable, Equatable {
    var enabled = true
    /** Waking hours: reminders only between wake + 1 and sleep − 1. */
    var wake = 7
    var sleep = 23
    var windows: Set<StudyWindow> = []
    var rules: [Int: HourRule] = [:]
    var pausedUntil: Millis = 0
    /** Set once the setup question has been answered or skipped. */
    var asked = false
}

/** What reminders need to know about your study, saved by the app so they can be written ahead of time. */
struct ReminderSnapshot: Codable, Equatable {
    var day: Millis
    var nextTitle: String?
    var nextMinutes: Int
    var nextWhat: String?
    var cardsDue: Int
    var streak: Int
    var studyDays: Set<Int>
    var todayMinutes: Int
    var targetMinutes: Int
    var lastStudyAt: Millis
    var finalWeek: Bool
    var readiness: Int
    var mastered: Int
    var weekMinutes: Int
    var nextWeekFocus: String?
    /** Today can be this week's rest day: no streak-saver. */
    var restDayFree = false
}

/** A reminder planned for a time. */
struct PlannedReminder: Codable, Equatable { var at: Millis; var hour: Int; var kind: ReminderKind }

struct ReminderState: Codable {
    var settings = ReminderSettings()
    var logs: [ReminderLog] = []
    /** Minutes the app has been open at each hour of the day. */
    var appMinutes = [Double](repeating: 0, count: 24)
    /** Share of your active study days on which you studied at each hour, and how many such days. */
    var studyHabit = [Double](repeating: 0, count: 24)
    var studyHabitDays = 0
    var snapshot: ReminderSnapshot? = nil
    var lastSummaryReadiness = -1
    var lastSummaryMastered = -1
    var backoffUntil: Millis = 0
    var notTodayDay: Millis = 0
    /** Plans already chosen per day, so reopening the app doesn't reshuffle them. */
    var plans: [Millis: [PlannedReminder]] = [:]

    static let key = "reminderState"
    static func load() -> ReminderState {
        guard let d = UserDefaults.standard.data(forKey: key), let s = try? JSONDecoder().decode(ReminderState.self, from: d) else { return ReminderState() }
        return s
    }
    func save() { if let d = try? JSONEncoder().encode(self) { UserDefaults.standard.set(d, forKey: Self.key) } }
}

/** How well an hour works, as a Beta distribution over "a reminder then gets you studying". */
struct HourScore {
    let hour: Int; let prior: Double; let successes: Double; let failures: Double; let tried: Int; let rule: HourRule
    var alpha: Double { 1 + ReminderBrain.PRIOR_STRENGTH * prior + successes }
    var beta: Double { 1 + ReminderBrain.PRIOR_STRENGTH * (1 - prior) + failures }
    var mean: Double { alpha / (alpha + beta) }
}

enum ReminderBrain {
    static let HOUR: Millis = 3_600_000
    static let MINUTE: Millis = 60_000
    static let RESPONSE_WINDOW = 60 * MINUTE
    static let PRIOR_STRENGTH = 4.0
    static let HALF_LIFE_DAYS = 30.0
    static let LEARNING_REMINDERS = 20
    static let GOOD_HOUR = 0.4
    static let EXPLORE_RATE = 0.25
    static let SUMMARY_HOUR = 18
    static let SUMMARY_MINUTE = 30

    static func wakingHours(_ s: ReminderSettings) -> [Int] { s.sleep - 1 < s.wake + 1 ? [] : Array((s.wake + 1)...(s.sleep - 1)).filter { (0...23).contains($0) } }

    /** The starting guess for an hour: your answer at setup first, then your own study times as they build up. */
    static func prior(_ st: ReminderState, _ h: Int) -> Double {
        var sum = 0.0, weight = 0.0
        func add(_ v: Double, _ w: Double) { sum += v * w; weight += w }
        if !st.settings.windows.isEmpty { add(st.settings.windows.contains { $0.hours.contains(h) } ? 0.7 : 0.2, 1) }
        if st.studyHabitDays >= 3 { add(0.1 + 0.8 * min(max(st.studyHabit[h], 0), 1), min(3, Double(st.studyHabitDays) / 5)) }
        let maxApp = st.appMinutes.max() ?? 0
        if maxApp >= 30 { add(0.1 + 0.8 * st.appMinutes[h] / maxApp, 0.5) }
        return weight == 0 ? 0.3 : sum / weight
    }

    /** How a past reminder turned out: 1 studied, 0.6 opened only, 0 ignored; nil while still pending. */
    static func outcome(_ l: ReminderLog, _ now: Millis) -> Double? {
        if l.studiedMinutes >= 3 { return 1 }
        if l.notToday { return 0 }
        if l.openedAt != nil { return 0.6 }
        if now - l.id > RESPONSE_WINDOW { return 0 }
        return nil
    }

    static func scores(_ st: ReminderState, _ now: Millis) -> [HourScore] {
        (0..<24).map { h in
            var s = 0.0, f = 0.0, n = 0
            for l in st.logs where l.hour == h && l.kind != .SUMMARY {
                guard let v = outcome(l, now) else { continue }
                let age = Double(now - l.id) / Double(24 * HOUR)
                // "Not today" says more about the day than the hour, so it counts half.
                let w = pow(0.5, age / HALF_LIFE_DAYS) * (l.notToday ? 0.5 : 1)
                s += v * w; f += (1 - v) * w; n += 1
            }
            return HourScore(hour: h, prior: prior(st, h), successes: s, failures: f, tried: n, rule: st.settings.rules[h] ?? .AUTO)
        }
    }

    /** Daily cap: 3 while learning, then 2; 1 for three days after four ignored reminders in a row. */
    static func cap(_ st: inout ReminderState, _ now: Millis) -> Int {
        let resolved = st.logs.filter { $0.kind != .SUMMARY && outcome($0, now) != nil }
        let last4 = resolved.suffix(4)
        if last4.count == 4 && last4.allSatisfy({ $0.openedAt == nil && !$0.notToday && $0.studiedMinutes == 0 }) && st.backoffUntil < last4.last!.id {
            st.backoffUntil = last4.last!.id + 3 * 24 * HOUR
        }
        if now < st.backoffUntil { return 1 }
        return resolved.count < LEARNING_REMINDERS ? 3 : 2
    }

    /** Minutes to send before the hour: median of the last five opening delays, less ten, up to 30. */
    static func offsetMinutes(_ st: ReminderState, _ h: Int) -> Int {
        let delays = st.logs.filter { $0.hour == h && $0.openedAt != nil && $0.kind == .STUDY }.suffix(5).map { Int(($0.openedAt! - $0.id) / MINUTE) }.sorted()
        if delays.count < 3 { return 0 }
        return min(max(delays[delays.count / 2] - 10, 0), 30)
    }

    /** The reminders for one day, chosen once and kept. */
    static func plan(_ st: inout ReminderState, day: Millis, now: Millis) -> [PlannedReminder] {
        if let p = st.plans[day] { return p.filter { $0.at > now } }
        let p = choose(&st, day, now)
        st.plans[day] = p
        st.plans = st.plans.filter { $0.key >= Time.startOfDay(now) }
        return p
    }

    private static func choose(_ st: inout ReminderState, _ day: Millis, _ now: Millis) -> [PlannedReminder] {
        let s = st.settings
        if !s.enabled || now < s.pausedUntil { return [] }
        var out: [PlannedReminder] = []
        let snap = st.snapshot
        let studyDay = snap == nil || snap!.studyDays.isEmpty || snap!.studyDays.contains(Time.weekday(day))
        let hours = wakingHours(s)
        func at(_ h: Int, _ minute: Int = 0) -> Millis { day + Millis(h) * HOUR + Millis(minute) * MINUTE }
        if studyDay {
            let sentOnDay = st.logs.filter { $0.kind != .SUMMARY && $0.id >= day && $0.id < day + 24 * HOUR }.count
            let cap = max(0, self.cap(&st, now) - sentOnDay)
            let learning = st.logs.filter { $0.kind != .SUMMARY && outcome($0, now) != nil }.count < LEARNING_REMINDERS
            let sc = Dictionary(uniqueKeysWithValues: scores(st, now).map { ($0.hour, $0) })
            let isToday = day == Time.startOfDay(now)
            let streakHour: Int? = { let h = s.sleep - 2
                return hours.contains(h) && (snap?.streak ?? 0) >= 1 && !(isToday && snap?.restDayFree == true) && cap >= 2 && now < at(h) ? h : nil }()
            let studySlots = cap - (streakHour != nil ? 1 : 0)
            let open = hours.filter { sc[$0]!.rule != .BLOCKED && $0 != streakHour }
            var chosen: [Int] = []
            for h in open where sc[h]!.rule == .PINNED && chosen.count < studySlots { chosen.append(h) }
            let draws = open.filter { sc[$0]!.rule == .AUTO }.map { ($0, sampleBeta(sc[$0]!.alpha, sc[$0]!.beta)) }.sorted { $0.1 > $1.1 }
            // While learning, or while no hour has proven itself, every slot is a free draw, to explore.
            let proven = !learning && draws.contains { sc[$0.0]!.mean >= GOOD_HOUR }
            var explore = proven && Double.random(in: 0..<1) < EXPLORE_RATE
            for (h, _) in draws {
                guard chosen.count < studySlots, !chosen.contains(where: { abs($0 - h) < 2 }) else { continue }
                if !proven || sc[h]!.mean >= GOOD_HOUR { chosen.append(h) }
                else if explore { chosen.append(h); explore = false }
            }
            for h in chosen { out.append(PlannedReminder(at: at(h) - Millis(offsetMinutes(st, h)) * MINUTE, hour: h, kind: .STUDY)) }
            if let streakHour { out.append(PlannedReminder(at: at(streakHour), hour: streakHour, kind: .STREAK)) }
        }
        if Time.weekday(day) == 1 && ((s.wake + 1)...max(s.wake + 1, s.sleep - 1)).contains(SUMMARY_HOUR) {
            out.append(PlannedReminder(at: at(SUMMARY_HOUR, SUMMARY_MINUTE), hour: SUMMARY_HOUR, kind: .SUMMARY))
        }
        return out.filter { $0.at > now }.sorted { $0.at < $1.at }
    }

    /**
     * Whether a planned reminder should still go out, given today's study so far: no study reminder once
     * today's plan is done or after "Not today", no streak-saver once you've studied.
     */
    static func shouldSend(_ st: ReminderState, _ p: PlannedReminder, _ now: Millis) -> Bool {
        let s = st.settings
        if !s.enabled || p.at < s.pausedUntil { return false }
        if p.kind == .SUMMARY { return true }
        let day = Time.startOfDay(p.at)
        if st.notTodayDay == day { return false }
        guard let snap = st.snapshot else { return true }
        let todayMinutes = snap.day == day ? snap.todayMinutes : 0
        switch p.kind {
        case .STREAK: return todayMinutes == 0 && !(snap.day == day && snap.restDayFree)
        default: return snap.targetMinutes <= 0 || todayMinutes < snap.targetMinutes
        }
    }

    /* ── recording outcomes ── */

    /** The app came to the front: from a reminder's tap (tappedId), or on its own soon after one. */
    static func noteOpened(_ st: inout ReminderState, _ now: Millis, tappedId: Millis?) {
        let i: Int? = tappedId != nil ? st.logs.lastIndex { $0.id == tappedId }
            : st.logs.lastIndex { $0.openedAt == nil && !$0.notToday && now - $0.id >= 0 && now - $0.id <= RESPONSE_WINDOW }
        if let i, st.logs[i].openedAt == nil { st.logs[i].openedAt = now; st.logs[i].tapped = tappedId != nil }
    }

    static func noteNotToday(_ st: inout ReminderState, _ id: Millis, _ now: Millis) {
        if let i = st.logs.lastIndex(where: { $0.id == id }) { st.logs[i].notToday = true }
        st.notTodayDay = Time.startOfDay(now)
    }

    /** Reminders whose time has passed are logged as sent (iOS delivers them without the app running). */
    static func noteDelivered(_ st: inout ReminderState, _ now: Millis) {
        for (_, list) in st.plans {
            for p in list where p.at <= now && shouldSend(st, p, p.at) && !st.logs.contains(where: { $0.id == p.at && $0.kind == p.kind }) {
                st.logs.append(ReminderLog(id: p.at, hour: p.hour, kind: p.kind))
                if p.kind == .SUMMARY, let n = st.snapshot { st.lastSummaryReadiness = n.readiness; st.lastSummaryMastered = n.mastered }
            }
        }
        st.logs.sort { $0.id < $1.id }
        if st.logs.count > 400 { st.logs.removeFirst(st.logs.count - 400) }
    }

    /** Minutes the app spent in front between from and to, spread over the hours they fell in. */
    static func noteForeground(_ st: inout ReminderState, from: Millis, to: Millis) {
        var t = from
        while t < to {
            let h = Time.hour(t)
            let hourStart = Time.startOfDay(t) + Millis(h) * HOUR
            let end = min(to, hourStart + HOUR)
            st.appMinutes[h] += Double(end - t) / Double(MINUTE)
            t = end
        }
    }

    /** After opening from a reminder, how much did you study? Counted for the two hours that follow. */
    static func noteStudy(_ st: inout ReminderState, _ results: [SessionResult], _ attempts: [Attempt], _ now: Millis) {
        for i in st.logs.indices {
            guard let opened = st.logs[i].openedAt, now - opened <= 3 * HOUR else { continue }
            st.logs[i].studiedMinutes = studyMinutes(results, attempts, st.logs[i].id, opened + 2 * HOUR)
        }
    }

    static func studyMinutes(_ results: [SessionResult], _ attempts: [Attempt], _ from: Millis, _ to: Millis) -> Int {
        let sessions = results.filter { $0.at >= from && $0.at <= to }.reduce(0) { $0 + $1.minutes }
        let answering = Int((Double(attempts.filter { $0.at >= from && $0.at <= to }.reduce(0) { $0 + $1.ms }) / Double(MINUTE)).rounded())
        return max(sessions, answering)
    }

    /** On what share of your study days you studied at each hour, over the last eight weeks. */
    static func studyHabit(_ attempts: [Attempt], _ results: [SessionResult], _ now: Millis) -> ([Double], Int) {
        let since = now - 56 * 24 * HOUR
        var byDay: [Millis: Set<Int>] = [:]
        func add(_ t: Millis) { if t >= since { byDay[Time.startOfDay(t), default: []].insert(Time.hour(t)) } }
        attempts.forEach { add($0.at) }
        results.forEach { add($0.at - Millis($0.minutes) * MINUTE) }
        var share = [Double](repeating: 0, count: 24)
        if byDay.isEmpty { return (share, 0) }
        for hs in byDay.values { for h in hs { share[h] += 1 } }
        for h in 0..<24 { share[h] /= Double(byDay.count) }
        return (share, byDay.count)
    }

    /** Thompson sampling needs a draw from Beta(a, b): two gamma draws (Marsaglia–Tsang). */
    static func sampleBeta(_ a: Double, _ b: Double) -> Double {
        let x = sampleGamma(a), y = sampleGamma(b)
        return x / (x + y)
    }

    private static func sampleGamma(_ shape: Double) -> Double {
        if shape < 1 { return sampleGamma(shape + 1) * pow(Double.random(in: 0..<1), 1 / shape) }
        let d = shape - 1.0 / 3, c = 1 / sqrt(9 * d)
        while true {
            var x = 0.0, v = 0.0
            repeat { x = gaussian(); v = 1 + c * x } while v <= 0
            v = v * v * v
            let u = Double.random(in: 0..<1)
            if u < 1 - 0.0331 * x * x * x * x || log(u) < 0.5 * x * x + d * (1 - v + log(v)) { return d * v }
        }
    }

    private static func gaussian() -> Double {
        let u1 = max(Double.random(in: 0..<1), 1e-12), u2 = Double.random(in: 0..<1)
        return sqrt(-2 * log(u1)) * cos(2 * .pi * u2)
    }
}

/** The words of each reminder. */
enum ReminderText {
    struct Message { let title: String; let body: String }

    static func compose(_ kind: ReminderKind, _ st: ReminderState, _ at: Millis, learnedHour: Bool) -> Message {
        let n = st.snapshot
        switch kind {
        case .STUDY:
            let away = n.map { $0.lastStudyAt > 0 ? Int((at - $0.lastStudyAt) / (24 * ReminderBrain.HOUR)) : 0 } ?? 0
            guard let n, let title = n.nextTitle else { return Message(title: "Time for today's study", body: "Open your path to see today's session.") }
            if away >= 3 { return Message(title: "Welcome back: a 15-minute warm-up", body: "\(away) days off is fine. Let's pick up with what you studied last.") }
            if n.finalWeek { return Message(title: "Final week: \(title) · \(n.nextMinutes) min", body: "Light review only. Rest matters as much as revision now.") }
            let body = [n.nextWhat, learnedHour ? "You usually study around now." : nil].compactMap { $0 }.joined(separator: ". ")
            return Message(title: "Next up: \(title) · \(n.nextMinutes) min", body: body.isEmpty ? "Your next step on the path." : body)
        case .STREAK:
            let streak = n?.streak ?? 0, cards = n?.cardsDue ?? 0
            return Message(title: "Keep your \(streak)-day streak", body: cards > 0 ? "\(cards) flashcards are due. About 5 minutes." : "Ten quick questions keep it going. About 5 minutes.")
        case .SUMMARY:
            let m = n?.weekMinutes ?? 0
            let time = m >= 60 ? "\(m / 60) h \(m % 60) m" : "\(m) min"
            let ready = n?.readiness ?? 0
            let change = st.lastSummaryReadiness >= 0 ? "readiness \(st.lastSummaryReadiness) → \(ready)" : "readiness \(ready)"
            let mastered: Int? = n != nil && st.lastSummaryMastered >= 0 ? max(0, n!.mastered - st.lastSummaryMastered) : nil
            let parts = [mastered.map { "\($0) \($0 == 1 ? "task" : "tasks") mastered" }, n?.nextWeekFocus.map { "Next week: \($0)" }].compactMap { $0 }
            return Message(title: "Your week: \(time) studied, \(change)", body: parts.isEmpty ? "Open the app to see how the week went." : parts.joined(separator: ". ") + ".")
        }
    }
}
