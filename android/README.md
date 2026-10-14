# PMP Prep for Android

A native Android app (Kotlin, Jetpack Compose, Material 3) built from this
repository's content. Nothing is copied into this folder: every build runs
`tools/extract-content.mjs`, which reads `../index.html` and `../packs/` and
writes the app's assets, so the app and the web app always carry the same
questions, scenarios, lessons, flashcards, glossary, formulas and reference.

## What's in it

| Screen | What it does |
|---|---|
| Your path (home) | First run asks for the exam date, weekly hours, study days and the diagnostic. From then on the app leads: one **Continue** button opens the next session (cards, lesson, practice, reflect, debrief), with a 15-minute version for short days, what's coming up, and why the plan changed. `data/PlanEngine.kt` rebuilds the plan from your progress every time, so missed days, weak scores and practice done elsewhere all re-shape it (see below) |
| ☰ Menu | Everything beside the path: extra drills, mistakes, flagged, situational trainer, formula lab, mock, diagnostic, lessons, cards, reference, coach, analytics, notes, plan settings, banks |
| Coach | In the menu and on the path. **Today** works offline: how you're doing, what's going well, what to improve, the coach's messages and today's session, each with a Go button. **Ask Gemini** is a chat with Google's Gemini Flash on your own API key: every message carries a snapshot of your progress, so it can review how you're doing, say what to improve and answer PMP questions. Practice questions have an "Ask coach" button |
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
| Plan and schedule | Diagnostic, exam date, weekly hours and study days; the path re-plans as soon as any of them changes |
| Notes | One running page with a formatting ribbon (bold, italic, underline, strike, highlight, lists) and a read view, plus every note you wrote on a question. "Ask coach" sends the page to Gemini for a check |
| Banks & backup | Import `.json` banks, switch banks on or off, delete imported ones, content audit, backup / restore / reset |

All seven question types render natively: multiple choice, multiple response,
matching, drop-down, hotspot and graphic (drawn on a canvas), and fill in the
blank. Scoring follows the web app exactly.

## Design decisions

The layout follows published mobile-usability research; when changing it, keep to these.

- **One path, one button.** The home screen is the guided path with a single large *Continue*. Extra work
  is one tap away under ☰ in the top bar, as a labelled, grouped list. There is no bottom bar and no
  floating bubble: a learner who is led doesn't need five tabs, and nothing covers the content.
- **Sessions are full screen** with a step counter and progress bar; ✕ leaves and *Continue* resumes
  where you were. In drills and the mock exam the advancing action (Check, Next, Finish) is bottom-right.
- **Destructive actions apart.** *Delete this bank* sits on its own line, not beside the on/off switch,
  and *Reset all progress* is in a separate Danger zone; both confirm first.
- **Touch targets** are at least 48 dp (Material; WCAG 2.2 asks for 24 px minimum), whole cards and
  tiles are tappable, and menu rows show a live count.
- **Contrast measured, not assumed.** Status, domain and accent colours are tuned per theme so text
  meets WCAG AA (4.5:1) in both: the earlier single values measured 2.2–4.1:1 on white. The measured
  pairs are listed in `ui/theme/Color.kt`. No text is smaller than 11 sp.
- **Motion** uses Compose's animations, which follow Android's *Remove animations* setting.

## How the path adapts

The plan is never stored as a calendar; it is recomputed from what you have done each time you open
the app, so it cannot fall out of date. Each ECO task is *not started*, *learning* (fewer than 10
answers), *shaky* (under 75% over its last 15), *mastered*, or *fading* (mastered but untouched for 14
days). From that:

- **Days missed** just mean less time left: the remaining work is spread over the study days that are
  left. After 5 or more days away the next session is a short warm-up on what you studied last.
- **A session under 50%** is followed by a remediation session on that task next time.
- **Behind schedule**: lower-value work is dropped first (second drills, reviews, extra foundations and exam-skill lessons, a third mock), never the
  checkpoints or the last two mocks; if it still doesn't fit, the path says so and offers more hours,
  another study day or a later date.
- **Practice outside the path counts**: a drill from the menu moves a task's status like any session.
- **Checkpoints** close each domain (pass at 70%), then mocks (three, or two with under four weeks
  left), and the last seven days taper to light review.

`PlanEngineTest` drives simulated learners through these cases (diligent, a week off, three weeks lost,
a failed session, practice off the path, fading mastery).

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
