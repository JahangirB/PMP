import java.net.URI
import java.security.MessageDigest
import java.util.concurrent.Callable

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

// The questions, scenarios, lessons and reference content are not copied into
// this project. prepareContent rebuilds them on every build from the web app
// (../index.html) and the question packs (../packs/), so the APK always ships
// exactly what the repository holds. Needs Node.js on the PATH.
val repoRoot: File = rootProject.projectDir.parentFile
val generatedAssets = layout.buildDirectory.dir("generated/pmpAssets")

val prepareContent = tasks.register<Exec>("prepareContent") {
    description = "Extracts index.html content and copies packs/ into the app's assets."
    val script = rootProject.file("tools/extract-content.mjs")
    inputs.file(File(repoRoot, "index.html"))
    inputs.dir(File(repoRoot, "packs"))
    inputs.file(script)
    outputs.dir(generatedAssets)
    commandLine("node", script.absolutePath, repoRoot.absolutePath, generatedAssets.get().asFile.absolutePath)
}

// Listen mode's offline voice: the sherpa-onnx speech library and the Kokoro English voice
// (both Apache-2.0), downloaded from sherpa-onnx's GitHub releases at build time and packed
// into the APK, so nothing large lives in this repository and nothing is downloaded on the
// phone. Each file is pinned to an exact release and checked against its SHA-256.
val voiceCache: File = rootProject.file(".voice-cache")
val voiceAssets = layout.buildDirectory.dir("generated/voiceAssets")
val voiceFiles = mapOf(
    "sherpa-onnx-1.13.8.aar" to (
        "https://github.com/k2-fsa/sherpa-onnx/releases/download/v1.13.8/sherpa-onnx-1.13.8.aar" to
            "633c24321e06b1fe79feafa03ea16cbc0f8a286641e2da3559bac91bdb13bd96"),
    "kokoro-int8-en-v0_19.tar.bz2" to (
        "https://github.com/k2-fsa/sherpa-onnx/releases/download/tts-models/kokoro-int8-en-v0_19.tar.bz2" to
            "c9f0dd393615805b0bab050c340834d5e684e732aec91c0e860cd30e982c08bd"),
)
val sherpaAar = File(voiceCache, "sherpa-onnx-1.13.8.aar")
val voiceTar = File(voiceCache, "kokoro-int8-en-v0_19.tar.bz2")

fun sha256(f: File): String {
    val md = MessageDigest.getInstance("SHA-256")
    f.inputStream().use { input -> val buf = ByteArray(1 shl 16); while (true) { val n = input.read(buf); if (n < 0) break; md.update(buf, 0, n) } }
    return md.digest().joinToString("") { "%02x".format(it) }
}

val fetchVoice = tasks.register("fetchVoice") {
    description = "Downloads the pinned speech library and voice for Listen mode, and checks their SHA-256."
    outputs.files(voiceFiles.keys.map { File(voiceCache, it) })
    doLast {
        voiceCache.mkdirs()
        voiceFiles.forEach { (name, source) ->
            val (url, hash) = source
            val f = File(voiceCache, name)
            if (f.exists() && sha256(f) == hash) return@forEach
            logger.lifecycle("Downloading $url")
            val tmp = File(voiceCache, "$name.part")
            URI(url).toURL().openStream().use { input -> tmp.outputStream().use { input.copyTo(it) } }
            val got = sha256(tmp)
            if (got != hash) { tmp.delete(); throw GradleException("$name: SHA-256 is $got, expected $hash. The upstream file changed; check it before updating the pin.") }
            if (!tmp.renameTo(f)) { f.delete(); tmp.renameTo(f) }
        }
    }
}

val prepareVoice = tasks.register<Sync>("prepareVoice") {
    description = "Unpacks the voice into the app's assets: the model, its voices and tokens, and English-only pronunciation data."
    dependsOn(fetchVoice)
    from(Callable { tarTree(resources.bzip2(voiceTar)) }) {
        include("*/model.int8.onnx", "*/voices.bin", "*/tokens.txt", "*/LICENSE", "*/espeak-ng-data/**")
        // The voice is English: every other language's pronunciation dictionary is left out (19 MB → 2 MB).
        exclude { it.name.endsWith("_dict") && it.name != "en_dict" }
        eachFile { relativePath = RelativePath(true, "tts", *relativePath.segments.drop(1).toTypedArray()) }
        includeEmptyDirs = false
    }
    into(voiceAssets)
}

android {
    namespace = "com.jahangirb.pmpprep"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.jahangirb.pmpprep"
        minSdk = 24
        targetSdk = 35
        // CI passes its run number; earlier sideloaded builds used codes up to 76.
        val build = (System.getenv("VERSION_CODE") ?: "0").toIntOrNull() ?: 0
        versionCode = 100 + build
        versionName = "2.0.$build"
        // The speech library is native code; only 64-bit ARM, which every current phone uses.
        ndk { abiFilters += "arm64-v8a" }
    }

    signingConfigs {
        // A fixed key kept in the repository, so every build installs over the
        // last one without losing progress. Fine for a sideloaded study app;
        // use your own key before publishing to a store.
        create("sideload") {
            storeFile = rootProject.file("pmp-prep.keystore")
            storePassword = "pmpprep"
            keyAlias = "pmpprep"
            keyPassword = "pmpprep"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("sideload")
        }
        debug {
            signingConfig = signingConfigs.getByName("sideload")
        }
    }

    sourceSets["main"].assets.srcDir(generatedAssets)
    sourceSets["main"].assets.srcDir(voiceAssets)
    // Stored uncompressed: they barely compress, and the voice loads faster.
    androidResources { noCompress += listOf("onnx", "bin") }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
}

tasks.named("preBuild") { dependsOn(prepareContent, prepareVoice) }

// Unit tests read the generated assets straight from disk.
tasks.withType<Test>().configureEach {
    dependsOn(prepareContent)
    systemProperty("pmp.assets", generatedAssets.get().asFile.absolutePath)
}

dependencies {
    implementation(files(sherpaAar).builtBy(fetchVoice))
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.glance.appwidget)
    implementation(libs.androidx.glance.material3)
    debugImplementation(libs.androidx.compose.ui.tooling)

    testImplementation(libs.junit)
    testImplementation(libs.json)
}
