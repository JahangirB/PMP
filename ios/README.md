# PMP Prep for iPhone

A native SwiftUI port of the Android app (iOS 17+), with a home-screen widget.
It was converted from the Android code by AI and has not been tested on a real
iPhone. Install steps for users are in the [main README](../README.md#iphone-app).

## Layout

- `PMPPrep/Data`: models, content parser, scoring, SRS, plan, readiness,
  coach (Gemini), reminders. A line-by-line port of `android/.../data`.
- `PMPPrep/App`: `AppModel` (the view model), sessions, platform glue
  (Keychain, widget reload, notifications).
- `PMPPrep/UI`: theme, shared components and `Screens/`.
- `PMPPrepWidget`: the WidgetKit extension. It reads `WidgetStats` from the
  App Group `group.com.jahangirb.pmpprep`.
- `PMPPrepTests`: unit tests for the data layer.

Progress backups use the same JSON format as Android, so a backup moves between
the two apps.

## Building

There is no Xcode project in git. It is generated from `project.yml`:

```sh
node android/tools/extract-content.mjs . ios/Resources   # lessons, questions, packs
cd ios && xcodegen && open PMPPrep.xcodeproj
```

CI (`.github/workflows/ios.yml`) runs the tests on a simulator, takes light
and dark screenshots of each screen (`-uiScreen <name> -uiDemo 1`), and
builds an unsigned IPA, then signs it ad hoc so the widget's App Group
entitlement survives. SideStore/AltStore re-sign it with the user's Apple ID.
