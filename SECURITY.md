# Security policy

## Supported versions

Only the latest release is supported: the website
(https://pmp-sandy.vercel.app/), the newest Android APK and the newest iPhone
IPA. Please check a problem still happens there before you report it.

## What the app stores and sends

The app has no server and no accounts. It is useful to know what there is to
attack:

- **Progress, notes and settings** stay on your device (browser storage, or the
  phone app's own storage).
- **Gemini** (optional) sends a summary of your study progress and your
  message to Google, using the API key you entered. The key is kept on your
  device and is never put in backups or cloud sync.
- **Cloud sync** (optional) talks only to a Google Apps Script that you deploy
  in your own Google account ([`sync/pmp-sync.gs`](sync/pmp-sync.gs)).
- **Imported packs and backups** are files you choose to load.

Problems worth reporting include: a way for a question pack, backup or synced
file to run code or break the app, the Gemini key leaking anywhere, the sync
script exposing data to someone other than its owner, or the app making a
network request it should not.

## Reporting a vulnerability

Please **do not open a public issue**. Use GitHub's private reporting instead:
go to the **Security** tab of this repository and press **Report a
vulnerability**.

Say what you found, the steps to reproduce it, which app (web, Android or
iPhone) and version, and what you think the impact is.

This is a one-person project, so replies may take a few days. You will hear
back whether the report is accepted, and you will be credited in the fix
unless you would rather not be.
