#!/usr/bin/env bash
# Builds android/PMP-Prep.apk, the native app, without Gradle. The questions
# come from ../packs/*.json plus content/core.json and content/extras.json
# (the built-in pack, flashcards, glossary and formulas, extracted from
# ../index.html by tools/extract-content.js).
#
# Needs: a JDK, aapt, dalvik-exchange (dx), zipalign, apksigner
#   (Debian/Ubuntu: apt-get install aapt dalvik-exchange zipalign apksigner)
# and an Android platform jar with resources.arsc, API 34:
#   ANDROID_JAR=/path/to/android.jar ./build.sh
# (Android SDK: $ANDROID_HOME/platforms/android-34/android.jar)
set -euo pipefail
cd "$(dirname "$0")"

ANDROID_JAR="${ANDROID_JAR:-${ANDROID_HOME:-/opt/android-sdk}/platforms/android-34/android.jar}"
[ -f "$ANDROID_JAR" ] || { echo "android.jar not found: set ANDROID_JAR" >&2; exit 1; }
DX="$(command -v dalvik-exchange || command -v dx)"

VERSION_CODE="${VERSION_CODE:-$(git rev-list --count HEAD 2>/dev/null || echo 1)}"
VERSION_NAME="${VERSION_NAME:-1.0.$VERSION_CODE}"
KEYSTORE="${KEYSTORE:-pmp-prep.keystore}"
KS_PASS="${KS_PASS:-pmpprep}"

rm -rf build
mkdir -p build/gen build/classes build/assets
cp -r content build/assets/content
cp -r ../packs build/assets/packs

aapt package -f -m \
  -J build/gen -M AndroidManifest.xml -S res -A build/assets -I "$ANDROID_JAR" \
  --min-sdk-version 24 --target-sdk-version 34 \
  --version-code "$VERSION_CODE" --version-name "$VERSION_NAME" \
  -F build/unsigned.apk

javac --release 8 -nowarn -classpath "$ANDROID_JAR" -d build/classes \
  $(find build/gen src -name '*.java')

"$DX" --dex --min-sdk-version=24 --output=build/classes.dex build/classes
(cd build && zip -q unsigned.apk classes.dex)

zipalign -f -p 4 build/unsigned.apk build/aligned.apk

if [ ! -f "$KEYSTORE" ]; then
  keytool -genkeypair -keystore "$KEYSTORE" -storepass "$KS_PASS" -keypass "$KS_PASS" \
    -alias pmpprep -keyalg RSA -keysize 2048 -validity 10000 -dname "CN=PMP Prep"
fi
apksigner sign --ks "$KEYSTORE" --ks-pass "pass:$KS_PASS" --ks-key-alias pmpprep \
  --out PMP-Prep.apk build/aligned.apk
apksigner verify PMP-Prep.apk
rm -f PMP-Prep.apk.idsig
echo "Built $(pwd)/PMP-Prep.apk (version $VERSION_NAME, code $VERSION_CODE)"
