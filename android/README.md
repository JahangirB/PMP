# PMP Prep for Android

A native Android app (Kotlin, Jetpack Compose, Material 3) built from this
repository's content. Nothing is copied into this folder: every build runs
`tools/extract-content.mjs`, which reads `../index.html` and `../packs/` and
writes the app's assets, so the app and the web app always carry the same
questions, scenarios, lessons, flashcards, glossary, formulas and reference.

## What's in it

| Screen | What it does |
|---|---|
| Coach (floating bubble) | On every screen except a running mock or the diagnostic. **Today** works offline: how you're doing, what's going well, what to improve, the coach's messages and today's session, each with a Go button. **Ask Gemini** is a chat with Google's Gemini Flash on your own API key: every message carries a snapshot of your progress, so it can review how you're doing, say what to improve and answer PMP questions. Practice questions have an "Ask coach" button |
| Home | Setup (exam date + diagnostic), readiness score (the web app's five-part model), streak, cards due, mistakes, domain accuracy |
| Diagnostic | 25 questions (8 People / 10 Process / 7 Business) across the difficulty range, taken cold with no feedback until the end; the planner and the coach aim from it |
| Practice | Filter by domain, ECO task, bank, type, approach and new / missed / flagged; check each answer and see why every option is right or wrong; pace bar; flags and notes |
| Mock exam | 180 / 90 / 45 / 20 questions, weighted by domain; timed, three sections with optional breaks, mark for review, question grid; saved if you leave; scorecard by domain; review |
| Learn | All lessons by module: sections, approach lenses, worked example, traps, quick checks, recap |
| Flashcards | SM-2 spaced repetition with the web app's settings, daily new-card cap, leech detection |
| Situational trainer | Dilemmas ranked Best / Acceptable / Premature / Wrong, with the reason for each |
| Formula lab | EVM, PERT, PTA, EMV, channels, finance and agile calculators with the working shown; formula sheet; calculation drills |
| Reference | ECO tasks with your accuracy, glossary, abbreviations, principles, performance domains, processes with ITTOs, agile, comparison tables |
| Analytics | How the readiness score is built, study days, mock trend, accuracy by task, approach and type, pace |
| Mistake journal | Every question wrong last time; tag the root cause; retry |
| Study planner | Your diagnostic, exam date and weekly hours turned into a week-by-week plan weighted to your weak domains |
| Notes | One running page with a formatting ribbon (bold, italic, underline, strike, highlight, lists) and a read view, plus every note you wrote on a question. "Ask coach" sends the page to Gemini for a check |
| Banks & backup | Import `.json` banks, switch banks on or off, delete imported ones, content audit, backup / restore / reset |

All seven question types render natively: multiple choice, multiple response,
matching, drop-down, hotspot and graphic (drawn on a canvas), and fill in the
blank. Scoring follows the web app exactly.

## The Gemini key

Create a free key at [aistudio.google.com/apikey](https://aistudio.google.com/apikey) and paste it into
**Coach → Ask Gemini**. The app checks it by listing the Flash models it may use and picks the newest
stable one (you can change it). The key stays in the app's private storage: it is excluded from
Android backups and from the progress backup file, and it is sent only to Google, in a request header.
Only the chat uses the network; everything else works offline.

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
