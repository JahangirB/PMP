# PMP Prep for Android

`PMP-Prep.apk` is the whole app (`index.html`) plus every pack in `packs/`,
wrapped in an Android WebView. It runs fully offline; the network is only used
by the optional Gemini tab and links you tap.

## Install

1. Copy `PMP-Prep.apk` to the phone (or download it from GitHub on the phone).
2. Open it and allow "Install unknown apps" for the app you opened it with.
3. Android 7.0 or later.

## Adding question banks later

No rebuild needed. In the app open **Backup and packs → Add a question pack**,
choose the `.json` file (authored per `AUTHORING.md`), or paste its text.
Imported packs, progress and settings are stored inside the app and survive
updates of the APK.

**Export progress / Export question pack → Save file** opens Android's save
dialog so you can put the file in Downloads, Drive, etc.

## Rebuilding (after index.html or packs/ change)

```
apt-get install aapt dalvik-exchange zipalign apksigner   # plus a JDK
ANDROID_JAR=$ANDROID_HOME/platforms/android-34/android.jar android/build.sh
```

The version code is the git commit count, so a rebuild installs over the old
one. `pmp-prep.keystore` (password `pmpprep`) signs it; keep using the same
keystore, because Android refuses an update signed by a different key, and
uninstalling to work around that deletes the app's saved progress.
