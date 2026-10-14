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

tasks.named("preBuild") { dependsOn(prepareContent) }

// Unit tests read the generated assets straight from disk.
tasks.withType<Test>().configureEach {
    dependsOn(prepareContent)
    systemProperty("pmp.assets", generatedAssets.get().asFile.absolutePath)
}

dependencies {
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
    debugImplementation(libs.androidx.compose.ui.tooling)

    testImplementation(libs.junit)
    testImplementation(libs.json)
}
