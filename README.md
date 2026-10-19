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

## iPhone app

[![Download IPA](https://img.shields.io/badge/Download-iPhone%20IPA-000000?style=for-the-badge&logo=apple&logoColor=white)](https://github.com/JahangirB/PMP/releases/download/ios/PMP-Prep.ipa)
[![iOS build](https://github.com/JahangirB/PMP/actions/workflows/ios.yml/badge.svg)](https://github.com/JahangirB/PMP/actions/workflows/ios.yml)

> **Please read first.** The iPhone app was converted from the Android version
> by AI and has **never been tested on a real iPhone**. It builds and passes its
> automated tests on the iOS simulator, but some things may not work. If you hit
> a problem, please reach me on LinkedIn: **[Jahangir Babayev](https://www.linkedin.com/in/jahangir-babayev-a432a5145)**. Include your
> iPhone model, iOS version and what you were doing.

The app isn't on the App Store, so you install it yourself ("sideloading") with
a free Apple ID. You need iOS 17 or later and a computer (Windows, Mac or
Linux) for the first setup only.

**Install with SideStore (recommended, refreshes on the phone):**

1. Follow the SideStore install guide at <https://docs.sidestore.io> to put
   SideStore on your iPhone. You sign in with your own Apple ID; a free one is
   fine.
2. On the iPhone turn on **Settings → Privacy & Security → Developer Mode** and
   restart when asked.
3. In SideStore open **Sources → +** and add
   `https://github.com/JahangirB/PMP/releases/download/ios/source.json`.
   PMP Prep then appears in SideStore and installs with one tap, and updates show
   up there too.
   Or download the **IPA** above, then in SideStore go to **My Apps → +** and pick
   the file.
4. If iOS says "Untrusted Developer", go to **Settings → General → VPN & Device
   Management**, tap your Apple ID and choose **Trust**.

**Other ways:** [AltStore](https://altstore.io) (add the same source URL) or
[Sideloadly](https://sideloadly.io) (drag the IPA in from a computer) both work
with a free Apple ID.

**Limits of a free Apple ID:** the app must be refreshed every **7 days**.
SideStore and AltStore do this for you; Sideloadly means reinstalling from the
computer. Your progress stays in place. A free ID can have at most 3 sideloaded
apps at a time. Back up your progress now and then from **⋮ → Question Banks &
Backup**. The backup file works on both Android and iPhone.

The IPA is built by [`.github/workflows/ios.yml`](.github/workflows/ios.yml) on
a macOS runner whenever `ios/`, `packs/` or `index.html` changes on `main`. It
uses the same question banks as the Android app. See [`ios/README.md`](ios/README.md).

## Web app

Open `index.html` in a browser. It is the whole app in one file and works
offline. Served over http it also loads the extended packs in `packs/`.

The mock exam opens with a case-study block filled with whole case studies
(passage, exhibits and their questions), and Practice has a **Case studies**
quick start that runs one case at a time.

**Hosting and installing.** To host it with GitHub Pages: in the repository's
**Settings → Pages**, choose **Deploy from a branch**, branch `main`, folder
`/ (root)`, and save. The app is then at `https://<user>.github.io/<repo>/`.
Served like that it is also an installable app: `manifest.webmanifest`,
`icons/` and the service worker `sw.js` sit beside `index.html`, and once it
has been opened online it opens offline too. Install it from the browser's
install icon or from the **Install** button in Settings (Chrome, Edge,
Android); in Safari use Share → Add to Home Screen (iPhone, iPad) or
File → Add to Dock (Mac). `index.html` is fetched network-first, so updates
arrive on the next online visit; the question packs are cached, so when
`packs/` changes, bump `VERSION` at the top of `sw.js`. Opened from disk
(`file://`), nothing is registered and the file works exactly as before.

Backups (Backup and packs → Export progress) use the shared progress format,
version 3, described in [docs/progress-format.md](docs/progress-format.md), so
a backup from the web, Android or iPhone app can be restored on any of them.
The web app also still reads its own older backups.

**Cloud sync** keeps the web, Android and iPhone apps in step through one file
in your own Google Drive. It is off until you set it up: open Cloud sync (in
the menu, or from Settings), copy the script it shows (`sync/pmp-sync.gs`, with
a random sync code already filled in), deploy it at
[script.google.com](https://script.google.com) as a web app, and paste its URL
back. Other devices choose "I already have a script" and use the same URL and
code. The address and code stay on each device and never go into a backup.

Tests: `node tests/run.js [filter]` (Playwright, Chromium).

## Writing questions

[`AUTHORING.md`](AUTHORING.md) describes the question pack format. The same
file works in the web app, the Android app and as a file in `packs/`.
