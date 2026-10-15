import Foundation
import Security
import UserNotifications
import WidgetKit

/** The Gemini key, in the iOS Keychain: on this device only, never in a backup file. */
enum KeyStore {
    private static let service = "com.jahangirb.pmpprep.gemini"
    private static var query: [String: Any] { [kSecClass as String: kSecClassGenericPassword, kSecAttrService as String: service, kSecAttrAccount as String: "key"] }

    static func read() -> String? {
        var q = query
        q[kSecReturnData as String] = true
        q[kSecMatchLimit as String] = kSecMatchLimitOne
        var out: AnyObject?
        guard SecItemCopyMatching(q as CFDictionary, &out) == errSecSuccess, let d = out as? Data else { return nil }
        return String(data: d, encoding: .utf8)
    }

    static func write(_ key: String) {
        delete()
        var q = query
        q[kSecValueData as String] = Data(key.utf8)
        q[kSecAttrAccessible as String] = kSecAttrAccessibleAfterFirstUnlockThisDeviceOnly
        SecItemAdd(q as CFDictionary, nil)
    }

    static func delete() { SecItemDelete(query as CFDictionary) }
}

/** The cloud sync URL and code, in the Keychain like the Gemini key: on this device only, never in a backup file. */
enum SyncKeyStore {
    static let URL_ACCOUNT = "url"
    static let CODE_ACCOUNT = "code"
    /** A code made for a script not connected yet, kept so leaving the screen doesn't change it. */
    static let DRAFT_ACCOUNT = "draft"
    private static let service = "com.jahangirb.pmpprep.sync"

    private static func query(_ account: String) -> [String: Any] {
        [kSecClass as String: kSecClassGenericPassword, kSecAttrService as String: service, kSecAttrAccount as String: account]
    }

    static func read(_ account: String) -> String? {
        var q = query(account)
        q[kSecReturnData as String] = true
        q[kSecMatchLimit as String] = kSecMatchLimitOne
        var out: AnyObject?
        guard SecItemCopyMatching(q as CFDictionary, &out) == errSecSuccess, let d = out as? Data else { return nil }
        return String(data: d, encoding: .utf8)
    }

    static func write(_ account: String, _ value: String) {
        delete(account)
        var q = query(account)
        q[kSecValueData as String] = Data(value.utf8)
        q[kSecAttrAccessible as String] = kSecAttrAccessibleAfterFirstUnlockThisDeviceOnly
        SecItemAdd(q as CFDictionary, nil)
    }

    static func delete(_ account: String) { SecItemDelete(query(account) as CFDictionary) }
}

/** Asks the home-screen widget to redraw from the latest saved figures. */
enum WidgetBridge {
    static func reload() { WidgetCenter.shared.reloadAllTimelines() }
}

/**
 * Books the reminders as local notifications. iPhone apps can't run code when a notification is due,
 * so the next three days are planned ahead with their words written now, and re-planned whenever the
 * app opens or progress changes; a reminder whose reason has passed (today's plan done) is withdrawn then.
 */
enum ReminderScheduler {
    static let category = "study"
    static let notTodayAction = "notToday"

    static func registerCategories() {
        let notToday = UNNotificationAction(identifier: notTodayAction, title: "Not today", options: [])
        UNUserNotificationCenter.current().setNotificationCategories([UNNotificationCategory(identifier: category, actions: [notToday], intentIdentifiers: [])])
    }

    static func requestPermission(_ done: @escaping (Bool) -> Void) {
        UNUserNotificationCenter.current().requestAuthorization(options: [.alert, .sound, .badge]) { ok, _ in DispatchQueue.main.async { done(ok) } }
    }

    static func schedule(_ st: inout ReminderState, _ now: Millis) {
        let center = UNUserNotificationCenter.current()
        center.removeAllPendingNotificationRequests()
        guard st.settings.enabled else { return }
        let scores = Dictionary(uniqueKeysWithValues: ReminderBrain.scores(st, now).map { ($0.hour, $0) })
        var day = Time.startOfDay(now)
        for _ in 0..<3 {
            for p in ReminderBrain.plan(&st, day: day, now: now) where ReminderBrain.shouldSend(st, p, p.at) {
                let learned = (scores[p.hour]?.tried ?? 0) >= 3 && (scores[p.hour]?.mean ?? 0) >= ReminderBrain.GOOD_HOUR
                let m = ReminderText.compose(p.kind, st, p.at, learnedHour: learned)
                let content = UNMutableNotificationContent()
                content.title = m.title
                content.body = m.body
                content.sound = .default
                content.categoryIdentifier = category
                content.userInfo = ["id": NSNumber(value: p.at), "open": p.kind == .SUMMARY ? "reports" : (p.kind == .STREAK ? "cards" : "today")]
                let comps = Calendar.current.dateComponents([.year, .month, .day, .hour, .minute], from: Time.date(p.at))
                center.add(UNNotificationRequest(identifier: "pmp-\(p.at)-\(p.kind.rawValue)", content: content,
                                                 trigger: UNCalendarNotificationTrigger(dateMatching: comps, repeats: false)))
            }
            day = Time.nextDay(day)
        }
    }
}
