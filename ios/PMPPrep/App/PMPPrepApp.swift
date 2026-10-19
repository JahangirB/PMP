import SwiftUI
import UserNotifications

@main
struct PMPPrepApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) private var delegate
    @StateObject private var model = AppModel()
    @Environment(\.scenePhase) private var phase

    var body: some Scene {
        WindowGroup {
            RootView()
                .environmentObject(model)
                .onAppear {
                    AppDelegate.model = model
                    model.applyLaunchArguments()
                }
                .onOpenURL { url in
                    // pmpprep://open/<tile>, from the home-screen widget.
                    model.pendingOpen = url.lastPathComponent
                    model.openPending()
                }
        }
        .onChange(of: phase) { _, p in
            model.appVisible = p == .active
            if p == .active {
                model.appOpened()
                model.openPending()
                model.autoSync()
            }
            if p == .background { model.syncOnBackground() }
        }
    }
}

/** Notification taps and the "Not today" action. */
final class AppDelegate: NSObject, UIApplicationDelegate, UNUserNotificationCenterDelegate {
    @MainActor static weak var model: AppModel?

    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]? = nil) -> Bool {
        UNUserNotificationCenter.current().delegate = self
        ReminderScheduler.registerCategories()
        return true
    }

    func userNotificationCenter(_ center: UNUserNotificationCenter, willPresent notification: UNNotification) async -> UNNotificationPresentationOptions {
        [.banner, .sound]
    }

    func userNotificationCenter(_ center: UNUserNotificationCenter, didReceive response: UNNotificationResponse) async {
        let info = response.notification.request.content.userInfo
        let id = (info["id"] as? NSNumber)?.int64Value ?? 0
        let open = info["open"] as? String
        await MainActor.run {
            guard let m = AppDelegate.model else { return }
            if response.actionIdentifier == ReminderScheduler.notTodayAction { m.notToday(id); return }
            m.reminderTapped(id)
            m.pendingOpen = open
            m.openPending()
        }
    }
}
