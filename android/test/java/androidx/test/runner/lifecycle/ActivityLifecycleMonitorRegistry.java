// Stand-in for the androidx.test class of the same name, which is only published
// on Google's Maven. Robolectric calls it during setup; these tests need no more.
package androidx.test.runner.lifecycle;
public final class ActivityLifecycleMonitorRegistry {
    private static ActivityLifecycleMonitor m;
    public static void registerInstance(ActivityLifecycleMonitor x) { m = x; }
    public static ActivityLifecycleMonitor getInstance() { return m; }
}
