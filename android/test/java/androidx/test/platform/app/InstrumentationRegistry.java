// Stand-in for the androidx.test class of the same name, which is only published
// on Google's Maven. Robolectric calls it during setup; these tests need no more.
package androidx.test.platform.app;
public final class InstrumentationRegistry {
    private static android.app.Instrumentation inst;
    private static android.os.Bundle args;
    public static void registerInstance(android.app.Instrumentation i, android.os.Bundle b) { inst = i; args = b; }
    public static android.app.Instrumentation getInstrumentation() { return inst; }
    public static android.os.Bundle getArguments() { return args; }
}
