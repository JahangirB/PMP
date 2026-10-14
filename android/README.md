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
| How it works | In the ☰ menu under *Start here*: a user guide covering what you get, how a study day runs, the readiness score with your own figures, how each exam task is judged, how weak spots are found and worked on, the other checks, reminders, and your data |
| ☰ Menu | Everything beside the path: extra drills, mistakes, flagged, situational trainer, formula lab, mock, diagnostic, lessons, cards, reference, coach, analytics, notes, plan settings, banks |
| Coach bubble | A draggable bubble on every screen, hidden while you pick answers. Three tabs: **Today** works offline: how you're doing, what's going well, what to improve, the coach's messages and today's session, each with a Go button. **Ask Gemini** is a chat with Google's Gemini Flash on your own API key: every message carries a snapshot of your progress, so it can review how you're doing, say what to improve and answer PMP questions. **Notes** is your notes page (below). Practice questions have an "Ask coach" button |
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
| Notes (bubble tab) | One running page with a formatting ribbon (bold, italic, underline, strike, highlight, lists) and a read view, plus every note you wrote on a question. "Ask coach" sends the page to Gemini for a check |
| Banks & backup | Import `.json` banks, switch banks on or off, delete imported ones, content audit, backup / restore / reset |

All seven question types render natively: multiple choice, multiple response,
matching, drop-down, hotspot and graphic (drawn on a canvas), and fill in the
blank. Scoring follows the web app exactly.

## Design decisions

The layout follows published mobile-usability research; when changing it, keep to these.

- **One path, one button.** The home screen is the guided path with a single large *Continue*. Extra work
  is one tap away under ☰ in the top bar, as a labelled, grouped list. There is no bottom bar.
- **The coach is a bubble** you can drag anywhere. It opens Coach, Ask Gemini and Notes. It hides
  wherever there are answers to pick (drills, the guided session's practice and mock steps, a running
  mock), so it never covers an option.
- **Sky Clay look** (`ui/theme/Clay.kt`, `Color.kt`): pastel sky-blue cards on a sky-to-lilac
  gradient, deep sky blue for anything you can press, 32 dp corners, a 2 dp light rim and a soft
  blue-tinted shadow. Light and dark follow the system. Type is Nunito throughout, bundled in
  `res/font` (SIL Open Font License, `app/NUNITO_OFL.txt`); timers, formulas and code stay monospaced.
- **Motion is calm**: no springs, no overshoot. Cards glide in from 36 dp left while fading up
  (800 ms, easing to a stop), 90 ms apart. Pressed cards lift 3 dp and glow under the finger. Buttons
  lift 2 dp and glow, with Material's ink ripple on tap. There's no idle pulsing. All of it is
  switched off when Android's *Remove animations* is on.
- **No glass blur.** Compose's blur only blurs a view's own content, not what's behind it, so a frosted
  panel would need a custom backdrop. The 8 px glass blur from the spec is left out rather than faked.
- **Sessions are full screen** with a step counter and progress bar; ✕ leaves and *Continue* resumes
  where you were. In drills and the mock exam the advancing action (Check, Next, Finish) is bottom-right.
- **Destructive actions apart.** *Delete this bank* sits on its own line, not beside the on/off switch,
  and *Reset all progress* is in a separate Danger zone; both confirm first.
- **Touch targets** are at least 48 dp (Material; WCAG 2.2 asks for 24 px minimum), whole cards and
  tiles are tappable, and menu rows show a live count.
- **Contrast measured, not assumed.** Status, domain and accent colours are tuned per theme so text
  meets WCAG AA (4.5:1) in both: the earlier single values measured 2.2–4.1:1 on white. The measured
  pairs are listed in `ui/theme/Color.kt`. No text is smaller than 11 sp.

## Home-screen widget

A 5×1 widget (`widget/PmpWidget.kt`, built with Glance) shows four clay tiles:

| Tile | Shows | Tap opens |
|---|---|---|
| Fitness | Exam fitness out of 100 | Exam fitness |
| Days left | Days to your exam date | Plan and schedule |
| Today | Minutes studied against today's plan, as a percentage with a bar | Today's session, or resumes the one in progress |
| Ready | The readiness score out of 100 | Analytics |
| Syllabus | Lessons completed out of all lessons, as a percentage with a bar | Lessons |

The app saves the figures whenever your progress changes; the widget redraws then and just after
midnight, when the day count and Today roll over. On Android 12 and newer, placing the widget asks
for **app colours** or **system colours** (taken from your wallpaper); long-press it and tap its
settings button to change. Older Android uses the app colours, with square corners.

## Smart reminders

Reminders learn which hours get you studying (`data/Reminders.kt`, tested in `RemindersTest`;
the Android side is `reminders/Reminders.kt`). Everything stays on the phone.

- **Starting guess.** At setup you say when you usually study and when you wake and sleep. If you
  switch on Usage access (the most Android allows: only you can turn it on, in Settings), the app
  also reads when your screen was on or unlocked over the last two weeks, never which apps.
- **Learning.** Each hour has a score for "a reminder here gets me studying". Each day the app
  picks a few hours by Thompson sampling: up to 3 a day while it learns (the first 20 reminders),
  then 2. After that, reminders go to the hours that have proven themselves, plus a try of another
  hour about one day in four. Older results fade (half weight after 30 days), so a changed routine
  is picked up; the tests show the switch from evenings to mornings being followed.
- **What counts.** Opening a reminder, or opening the app on your own within an hour of one, and
  then studying. *Not today* counts against the day more than the hour. How long the app is open at
  each hour, and when you study, feed the starting guess too.
- **Timing.** If you usually open an hour's reminder late, it goes out earlier: the median delay of
  the last five, minus 10 minutes, up to 30 minutes.
- **Manners.** Nothing on rest days, once today's plan is done, or after *Not today*. Four ignored
  in a row drop it to one a day for three days. One evening streak-saver (two hours before sleep)
  when you haven't studied yet. A summary on Sunday at 18:30.
- **Control.** *Plan and schedule → Smart reminders* shows each hour's score; tap an hour to pin
  it (always) or block it (never). Pause for a day or three, or switch reminders off.
- Alarms are inexact (a few minutes either way) so they need no special permission, survive
  battery saving, and are set again after a restart.

## Assessment

Readiness (`data/Readiness.kt`) has five parts: accuracy 35, mock exams 25, coverage 20, pace 10,
habit 10. Accuracy is worked out per domain and weighted like the exam (People 33%, Process 41%,
Business 26%), so a weak domain can't hide behind a strong one. The plan (`data/PlanEngine.kt`)
also:

- re-checks a domain whose answers fall under 65% after its checkpoint was passed (never in the final week);
- shapes a task's sessions by the cause you tag most on its misses: the lesson first for knowledge
  gaps, situational scenarios for mindset, a reading or calculation tip otherwise;
- adds a paced drill for a mastered task answered slower than 110 s a question;
- brings a task's lesson back when its flashcards become leeches.

Practice questions lean toward your weakest approach (predictive, agile or hybrid) when it is
clearly behind (`data/QuestionPicker.kt`). `PlanEngineTest` and `GapsTest` cover each of these.

## Exam fitness and weekly reports

Exam fitness (`data/ExamFitness.kt`, tested in `ExamFitnessTest`) asks whether you can *sit* the
exam, not whether you know it. It is behaviour-based and never a diagnosis.

- **Stamina 40%**: longest sitting (180 = full), accuracy fade and pace drift between the first
  and last quarter of long sittings, and long sittings finished rather than abandoned. Scored once
  there are two sittings of 40+ questions.
- **Composure 35%**: reading pace (words before the first tap; about 200 a minute leaves ~30 s of a
  ~76 s budget to decide on a typical 150-word question), freezes (no touch for over 3× your usual
  time and 2½ minutes), rushing after a slow question, accuracy against the clock versus relaxed,
  and right-to-wrong answer changes.
- **Consistency 25%**: regular days, hours kept, momentum (this week against the three before),
  sessions finished, bouncing back after breaks.
- Training follows from it: a weekly stamina-ladder sitting (20 → 45 → 70 → 100 → 140 → 180,
  moving up when the last quarter holds), reading drills (scenario first, options after), a
  gentle prompt after a long freeze in practice, pacing checkpoints at questions 60 and 120,
  a first-instinct debrief, a write-it-down option before mocks, and lighter weeks when momentum
  falls.
- Optional check-ins: one tap before the day's first session and after a mock, and a weekly
  five-statement check. Three weeks of high worry bring a gentle suggestion to talk to someone.
- The widget's fifth tile shows the score.

Weekly reports (`data/WeeklyReport.kt`) keep one report per Monday-to-Sunday week, compare every
figure with the week before or the first week (▲ better, ▼ worse), and draw trend lines. The
Sunday reminder opens them.

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
