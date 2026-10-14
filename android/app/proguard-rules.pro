# Content is parsed with org.json (part of the platform) into plain Kotlin
# classes, so nothing is reached by reflection and no keep rules are needed.

# The speech library's native code reads its Kotlin config classes by field name over JNI.
-keep class com.k2fsa.sherpa.onnx.** { *; }
# Listen mode's callback: the library calls invoke(float[]) on it by name over JNI.
-keep class com.jahangirb.pmpprep.listen.Listen$Sink { *; }
