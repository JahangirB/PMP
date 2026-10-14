// Stand-in for the androidx.test class of the same name, which is only published
// on Google's Maven. Robolectric calls it during setup; these tests need no more.
package androidx.test.internal.runner.lifecycle;
import androidx.test.runner.lifecycle.*;
public class ApplicationLifecycleMonitorImpl implements ApplicationLifecycleMonitor {
    public void signalLifecycleChange(android.app.Application a, ApplicationStage s) {}
}
