// Stand-in for the androidx.test class of the same name, which is only published
// on Google's Maven. Robolectric calls it during setup; these tests need no more.
package androidx.test.internal.runner.intent;
public class IntentMonitorImpl implements androidx.test.runner.intent.IntentMonitor {
    public void signalIntent(android.content.Intent i) {}
}
