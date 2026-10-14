# PMP Prep Suite

Offline PMP exam preparation: a starting-level diagnostic, practice questions
across all 26 ECO tasks, timed mock exams, a situational trainer, lessons,
spaced-repetition flashcards, a formula lab, a reference library, notes,
readiness analytics, and a coach that can talk it through with you on Gemini.

## Android app

[![Download APK](https://img.shields.io/badge/Download-Android%20APK-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://github.com/JahangirB/PMP/releases/latest/download/PMP-Prep.apk)
[![Android APK build](https://github.com/JahangirB/PMP/actions/workflows/android-apk.yml/badge.svg)](https://github.com/JahangirB/PMP/actions/workflows/android-apk.yml)

Press **Download APK** on your phone, open the file, and allow "Install
unknown apps" when Android asks. Android 7.0 or later. A newer APK installs
over the old one and keeps your progress.

The APK is built automatically by
[`.github/workflows/android-apk.yml`](.github/workflows/android-apk.yml)
whenever `index.html`, `packs/` or `android/` changes on `main`. The build
pulls every question bank in `packs/` and the content in `index.html` into
one APK, so adding a pack to `packs/` and pushing is all it takes to ship it.
You can also add your own `.json` banks inside the app (**⋮ → Question Banks &
Backup → Add question bank**), with no rebuild. See [`android/README.md`](android/README.md).

## Web app

Open `index.html` in a browser. It is the whole app in one file and works
offline. Served over http it also loads the extended packs in `packs/`.

## Writing questions

[`AUTHORING.md`](AUTHORING.md) describes the question pack format. The same
file works in the web app, the Android app and as a file in `packs/`.
