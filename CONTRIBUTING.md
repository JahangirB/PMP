# Contributing

Thank you for helping. There are three kinds of contribution, from easiest to
most involved. Please read the [code of conduct](CODE_OF_CONDUCT.md) first.

## 1. Report a wrong or unclear question

This is the most useful help of all. Open an issue with the **Question
problem** form. Give the question's ID if the app shows it, or its first few
words, and say why you think the answer or explanation is wrong. A reference
(the *PMBOK Guide*, the *Agile Practice Guide*, the Exam Content Outline)
makes it much quicker to fix.

All questions were written with AI and have not been reviewed by PMI, so
mistakes are expected.

## 2. Report a bug or suggest a feature

Use the **Bug report** or **Feature request** form. For a bug, say which app
(website, downloaded file, Android or iPhone), the device, and the steps that
cause it. Security problems go through [SECURITY.md](SECURITY.md), not an
issue.

## 3. Send a pull request

Open an issue first for anything bigger than a small fix, so we can agree on
it before you spend time on it.

### Questions and packs

- Follow [`AUTHORING.md`](AUTHORING.md): the pack format and the rules every
  question follows.
- Check [`ledger.md`](ledger.md) and the slices in [`ledger/`](ledger/) so you
  do not repeat a scenario that is already there.
- Write your own scenarios. Never copy real exam questions, prep-course
  questions or text from PMI publications.
- After changing any question, run `node tools/ledger.mjs` and commit the
  updated ledger.

### Code

- **Web:** the whole app is `index.html`. Keep to its style: plain ES5,
  modules as IIFEs on `PMP.*`, no build step and no new libraries.
- **Android:** Kotlin and Jetpack Compose in `android/`. See
  [`android/README.md`](android/README.md).
- **iPhone:** SwiftUI in `ios/`. See [`ios/README.md`](ios/README.md).
- A feature usually needs to land in all three apps. If it touches saved
  progress, backups or sync, follow
  [`docs/progress-format.md`](docs/progress-format.md) so the apps stay
  compatible.
- Keep changes small and focused, and add or update tests.

### Tests

```sh
node tests/run.js            # web (Playwright with Chromium)
cd android && ./gradlew testDebugUnitTest
```

The iPhone tests run in CI ([`.github/workflows/ios.yml`](.github/workflows/ios.yml)).

## Licence

By contributing, you agree that your contribution is licensed under the
[GNU GPL v3](LICENSE), like the rest of the project.
