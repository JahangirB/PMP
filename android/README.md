# PMP Prep for Android

A native Android app (Kotlin, Jetpack Compose, Material 3) built from this
repository's content. Nothing is copied into this folder: every build runs
`tools/extract-content.mjs`, which reads `../index.html` and `../packs/` and
writes the app's assets, so the app and the web app always carry the same
questions, scenarios, lessons, flashcards, glossary, formulas and reference.

## What's in it

| Screen | What it does |
|---|---|
| Home | Readiness score (the web app's five-part model), streak, cards due, mistakes, domain accuracy |
| Practice | Filter by domain, ECO task, bank, type, approach and new / missed / flagged; check each answer and see why every option is right or wrong; pace bar; flags and notes |
| Mock exam | 180 / 90 / 45 / 20 questions, weighted by domain; timed, three sections with optional breaks, mark for review, question grid; saved if you leave; scorecard by domain; review |
| Learn | All lessons by module: sections, approach lenses, worked example, traps, quick checks, recap |
| Flashcards | SM-2 spaced repetition with the web app's settings, daily new-card cap, leech detection |
| Situational trainer | Dilemmas ranked Best / Acceptable / Premature / Wrong, with the reason for each |
| Formula lab | EVM, PERT, PTA, EMV, channels, finance and agile calculators with the working shown; formula sheet; calculation drills |
| Reference | ECO tasks with your accuracy, glossary, abbreviations, principles, performance domains, processes with ITTOs, agile, comparison tables |
| Analytics | How the readiness score is built, study days, mock trend, accuracy by task, approach and type, pace |
| Mistake journal | Every question wrong last time; tag the root cause; retry |
| Study planner | Exam date and weekly hours turned into a week-by-week plan weighted to your weak domains |
| Banks & backup | Import `.json` banks, switch banks on or off, delete imported ones, content audit, backup / restore / reset |

All seven question types render natively: multiple choice, multiple response,
matching, drop-down, hotspot and graphic (drawn on a canvas), and fill in the
blank. Scoring follows the web app exactly.

## Building

The [Android APK workflow](../.github/workflows/android-apk.yml) builds and
publishes it; that is the normal route. To build locally you need JDK 17,
Node.js and the Android SDK (or open this folder in Android Studio):

```
cd android
./gradlew assembleRelease        # app/build/outputs/apk/release/app-release.apk
./gradlew testDebugUnitTest      # parses every pack and checks scoring, SM-2 and readiness
```

`pmp-prep.keystore` (password `pmpprep`) signs every build so each one
installs over the last. It is committed on purpose for a sideloaded study app;
use your own key before publishing to a store.
