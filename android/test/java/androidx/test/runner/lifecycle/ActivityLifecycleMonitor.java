// Stand-in for the androidx.test class of the same name, which is only published
// on Google's Maven. Robolectric calls it during setup; these tests need no more.
package androidx.test.runner.lifecycle;
public interface ActivityLifecycleMonitor {
    void addLifecycleCallback(ActivityLifecycleCallback c);
    void removeLifecycleCallback(ActivityLifecycleCallback c);
}
