# PMP Prep for Android

`PMP-Prep.apk` is a native Android app (Java, Android SDK only, no WebView).
It ships with every question in `packs/` plus the 348 built-in questions,
flashcards, glossary and formula sheet from `index.html`: 2,930 questions in all.

## What it does

- **Practice**: filter by domain, ECO task, question bank, question type and
  approach, and by new, missed-last-time or flagged questions. After each
  answer you see whether you were right, why each option is right or wrong,
  the explanation and the mindset.
- **Mock exams**: 180, 90, 45 or 20 questions, weighted 33/41/26 across
  People, Process and Business, favouring questions you haven't seen. Timed,
  with mark-for-review and a question grid. A mock in progress is saved if you
  leave the app. Afterwards you get a score by domain, your weakest tasks and a
  review of every answer.
- **All seven question types**: multiple choice, multiple response, matching,
  drop-down, hotspot (the diagram is drawn natively), graphic and fill in the
  blank. Scoring follows the web app's rules.
- **Flashcards, Glossary, Formulas**, and a mock exam history.
- Light and dark mode follow the phone.

## Install

Copy `PMP-Prep.apk` to the phone, open it, and allow "Install unknown apps"
when asked. Needs Android 7.0 or later. It installs over the earlier
WebView build (same package and signing key).

## Adding question banks later

No rebuild needed. **Question banks and backup → Add a question bank (.json)**
and pick one or more pack files (the format in `AUTHORING.md`; a bare JSON
array of questions also works). You can also open or share a `.json` file into
the app from a file manager. Questions whose id is already loaded are skipped,
and invalid ones are listed with the reason. Each bank can be switched off or
deleted. Imported banks, progress and flags are stored inside the app.

The same screen saves a progress backup and restores it.

## Rebuilding

```
apt-get install aapt dalvik-exchange zipalign apksigner   # plus a JDK
ANDROID_JAR=$ANDROID_HOME/platforms/android-34/android.jar android/build.sh
```

After changing `index.html`'s built-in questions, flashcards, glossary or
formulas, run `node android/tools/extract-content.js` first (needs Playwright)
to refresh `android/content/`. Files in `packs/` are picked up by the build
directly.

Tests (Robolectric, run on the JVM without a device; build the APK first):

```
cd android/test && ANDROID_JAR=... mvn -q test
```

`ScreenshotTest` writes PNGs of the main screens to `android/test/target/screens`.

The version code is the git commit count, so a rebuild installs over the old
one. `pmp-prep.keystore` (password `pmpprep`) signs it; keep using the same
keystore, because Android refuses an update signed by a different key, and
uninstalling to get round that deletes the app's saved progress.
