// Stand-in for the androidx.test class of the same name, which is only published
// on Google's Maven. Robolectric calls it during setup; these tests need no more.
package androidx.test.internal.runner.lifecycle;
import androidx.test.runner.lifecycle.*;
public class ActivityLifecycleMonitorImpl implements ActivityLifecycleMonitor {
    private final java.util.List<ActivityLifecycleCallback> cbs = new java.util.ArrayList<ActivityLifecycleCallback>();
    public void addLifecycleCallback(ActivityLifecycleCallback c) { cbs.add(c); }
    public void removeLifecycleCallback(ActivityLifecycleCallback c) { cbs.remove(c); }
    public void signalLifecycleChange(Stage s, android.app.Activity a) {
        for (ActivityLifecycleCallback c : new java.util.ArrayList<ActivityLifecycleCallback>(cbs)) c.onActivityLifecycleChanged(a, s);
    }
}
