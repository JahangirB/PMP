# Shared progress format, rules and cloud sync

All three apps (web `index.html`, Android, iOS) read and write one progress format.
That lets a backup or a cloud sync file move between them. This page is the contract.
When an app and this page disagree, the app is wrong.

## 1. Shared rules (`PMP.RULES`)

`index.html` §01 defines `PMP.RULES`. `android/tools/extract-content.mjs` copies it
into `extras.json` as `rules`, and Android and iOS read every value below from there
instead of hard-coding it.

| Key | Value | Used for |
| --- | --- | --- |
| `mockBands` | `{ above: 0.80, target: 0.65, below: 0.50 }` | Mock score bands: ≥above "Above Target", ≥target "Target", ≥below "Below Target", else "Needs Improvement". Also per domain. |
| `scenarioCredit` | `{ best: 1, acceptable: 0.5, premature: 0.25, wrong: 0 }` | Scenario trainer credit per rank. |
| `mastery` | `{ minAnswers: 10, window: 15, accuracy: 0.75, fadeDays: 14 }` | A task is mastered with at least `minAnswers` answers (lesson answers and lucky guesses excluded) and `accuracy` over the last `window`. It fades after `fadeDays` without an answer. The web's lesson "mastered" badge uses the lesson's task with this rule. |
| `causes` | 6 entries (below) | Mistake root causes, in this order. |
| `reentryDays` | `5` | Days away before the welcome-back / re-entry stage. |
| `confidence` | `[{v:3,id:"sure",label:"Sure"},{v:2,id:"think",label:"Think so"},{v:1,id:"guess",label:"Guess"}]` | Confidence asked before checking. `0` = not asked. |

Causes: each has `id`, `short`, `label` (first person, used on the tag button) and `hint`.

| id | short |
| --- | --- |
| `KNOWLEDGE_GAP` | Knowledge gap |
| `MISREAD_STEM` | Misread the question |
| `WRONG_MINDSET` | Wrong mindset |
| `TWO_PLAUSIBLE` | Two plausible answers |
| `CALCULATION` | Calculation slip |
| `RUSHED` | Rushed |

Old web ids map as `knowledge→KNOWLEDGE_GAP`, `misread→MISREAD_STEM`, `rushed→RUSHED`,
`mindset→WRONG_MINDSET` and `tossup→TWO_PLAUSIBLE`. Old web confidence maps `sure→3`
and `unsure`/"Not sure"→`2`.

## 2. Progress document (version 3)

```jsonc
{
  "type": "pmp-progress",
  "version": 3,
  "savedAt": 1760000000000,          // ms epoch, when this copy was written
  "device": { "app": "web" | "android" | "ios", "id": "random-uuid" },

  // ── shared slices: every app reads and writes these ──
  "attempts":   [ { "q": "qid", "t": "1.1", "d": "people", "ap": "agile", "ty": "single",
                    "cr": 0.5, "ok": false, "ms": 61000, "at": 1760000000000, "src": "practice",
                    // optional: ft idle rw wr w tm cf el (Android meanings), df (difficulty 1–5),
                    // h (hint rungs used; such rows stay out of accuracy and pace), id (web row id) } ],
  "flags":      [ "qid", ... ],
  "notes":      { "qid": "text" },
  "mistakes":   { "qid": { "c": "KNOWLEDGE_GAP", "n": "note", "at": ms } },
  "cards":      { "cardId": [ease, intervalDays, reps, lapses, dueMs, lastMs, firstMs, suspended0or1] },
  "scenarios":  [ { "id": "sid", "p": "picked option id", "r": "best|acceptable|premature|wrong", "at": ms } ],
  "mocks":      [ { "at": ms, "title": "Full exam", "n": 180, "right": 120, "usedMs": 0,
                    "domains": { "people": [right, n] } } ],
  "lessons":    [ "lessonId done", ... ],
  "plan":       { "examDate": ms|0, "hours": 10, "days": [1..7] /* Sunday=1 … Saturday=7 */, "onboarded": true, "rest": true,
                  "order": "course" /* optional: new lessons in the classroom order; absent = by exam domain */ },
  "sessions":   [ { "k": "key", "kind": "BUILD", "t": "task", "d": "domain", "s": 0.8, "m": 30, "at": ms } ],
  "notepad":    "light markup: **bold** *italic* __underline__ ~~strike~~ ==highlight== , - bullets, 1. numbers",
  "diagnostics":[ { "at": ms, "n": 25, "right": 15, "domains": { "people": [r, n] } } ],
  "chat":       [ { "r": "user|model", "t": "text", "at": ms } ],
  "snoozed":    { "messageId": untilMs },
  "sittings":   [ { "k": "key", "kind": "practice", "at": ms, "p": 20, "a": 20, "f": true } ],
  "checkins":   [ { "at": ms, "kind": "pre|post|weekly", "v": 3, "items": [..] } ],
  "weekly":     [ /* WeeklyReport.toJson(), identity = its week start */ ],
  "mycards":    [ { "id": "my:qid", "t": "task", "f": "front", "b": "back", "n": "note" } ],
  "drills":     [ { "at": ms, "g": "evm-variance", "ok": true } ],   // formula-lab drill problems, see below
  "commands":   [ { "n": "agile", "t": "the message it sends" } ],  // optional: the Gemini chat's own "/" commands

  // ── app-only blocks: other apps keep them byte-for-byte and never edit them ──
  "web":    { "settings": {...}, "planner": {...}, "coach": {...}, "issues": [...],
              "exams": [...], "lessons": {...}, "queue": [...], "notepadHtml": "…" },
  "native": { }
}
```

`commands` holds the person's own chat commands, in the order made: `n` is the name typed after `/`
(1–24 of a–z, 0–9 and `-`, never one of the built-in `progress`, `improve`, `plan`, `weakest`), `t` the
message it sends. Typing `/name`, alone or followed by more words, sends `t` (and the words after a blank
line). The slice is absent when there are none.

`drills` holds one row per formula-lab drill problem marked or shown (web §10.06.10a). `g` is the
generator id, one of `evm-variance`, `evm-forecast`, `tcpi`, `pert`, `pert-path`, `channels`, `emv`,
`decision-tree`, `pta`, `fee`, `float`, `npv`, `payback`, `irr`, `bcr`, `littles-law`, `velocity`,
`lead-time`; an id is never reused for a different kind of problem, and an app keeps rows with ids it
doesn't know. `ok` is true only for a right answer ("close" and "shown" are false). Drills never count
toward accuracy, pace or mastery, but a day with a drill on it is a study day (habit and streak).

Rules every app follows:
- **Times** are ms since the epoch (numbers), never ISO strings, in the shared slices.
- **Unknown top-level keys**, and the `web` and `native` blocks, are kept as they are and
  written back unchanged. An app may drop unknown fields inside a record it parses.
- **Reading.** An app accepts version 3 `pmp-progress`. Android/iOS also accept their own
  version 2 (`pmp-android-progress`). The web also accepts its own legacy
  `pmp-progress` v2 (`schemaVersion: 2`) and Android v2. Writing is always version 3.
- **Web mapping:**
  - Web attempts become shared attempts. `mode` diagnostic becomes `src` diagnostic; every
    other mode, including a lesson's practice set, becomes `practice` (the web mode rides in a
    per-row `wx`). `src: "lesson"` is reserved for the phones' lesson quick checks, which stay
    out of accuracy, pace and mastery. A timed drill and a mock answer get `tm: true`; `hinted` becomes `h`.
    The answer signals `ft`, `idle`, `w`, `rw`, `wr` are kept on web attempts under the same names
    (measured while the question is on screen and the tab visible) and written only when positive.
    So are `cf` (Sure 3, Think so 2, Guess 1, from `confidence`) and `el` (options crossed out before
    checking). A web reading drill is `src: "practice"` with `wx.mode: "reading"`, and its `w` counts
    the stem's words only, as on the phones.
  - `mycards` and `chat` are web slices in exactly the shared row shape and are copied both ways
    unchanged. A card made on the web has the id `"my:" + question id`, as on Android; the chat is
    capped at 60 turns. Rows an older web build left in its passthrough are folded in.
  - `srs` becomes `cards`.
  - `journal` (latest per qid) becomes `mistakes`.
  - `scenarioRuns` become `scenarios`.
  - Finished `exams` produce `mocks` summaries; the full records stay in `web.exams`.
  - Completed lessons go to `lessons`; the details stay in `web.lessons`.
  - A finished diagnostic also adds a `diagnostics` row (`at`, `n`, `right`, `domains` as `[right, n]`),
    as the phones do.
- **Planning from the document (every app):** the plan's domain order comes from the newest
  `diagnostics` row that has `domains` (the web falls back to its own attempts only when there is
  none). A finished session `lesson:<id>`, or `build:<task>` for that task's lesson, counts the
  lesson as read even when `lessons` lacks it, so a lesson is never offered twice.
  - `settings.examDate` becomes `plan.examDate`; `settings.restDays` becomes `plan.rest` (written only
    when it differs from the plan; absent means true).
  - `sittings`, `checkins`, `weekly` and `sessions` are web slices in exactly the shared row shape and are
    copied both ways unchanged (the web writes practice, mock and path sittings, check-ins, weekly reports
    and guided sessions; a session's `k` is the plan engine's key, e.g. `build:people-3`, `lesson:F1`,
    `mixed:<local midnight ms>`, `short:` + key, the same on every app). The web's hours and days for the
    path are `plan.hours` and `plan.days`; with no plan yet they are derived from its calendar planner's
    minutes. A missed question back for review is `src: "review"` (counted against five a day). Reading comfort (text size, line spacing, guide) is device-only.
  - The rich notes page is converted to `notepad` markup (colours are lost); the HTML stays in `web.notepadHtml`.
    When the shared `notepad` has changed and `notepadHtml` hasn't, the web rebuilds its HTML from the markup.
- **The Gemini key, its model line-up and device-only settings never go in the document.**

## 3. Merge (identical in every app)

The merge is three-way: `base` (the document as it was after this device's last sync),
`local` and `remote`. With no base yet, `base` is an empty document. Equality means
canonical JSON (sorted keys) is equal.

| Kind | Slices | Rule |
| --- | --- | --- |
| log | attempts (q+at), scenarios (id+at), mocks (at+title), sessions (k+at), diagnostics (at), chat (at+r), sittings (k+at), checkins (kind+at), weekly (week start), drills (at+g) | Union by identity; nothing is ever deleted. When both sides hold the same identity, keep the local record. Sort by `at`. Cap attempts at the newest 20,000, sittings at 2,000, drills at 2,000, chat at 60 and weekly at 104. |
| set | flags, lessons, opens | Per element: kept if kept on both sides; removed if removed on either side relative to base; added if added on either side. |
| map | notes, mistakes, cards, snoozed, mycards (by id), support, web (per key), native (per key) | Per key, three-way: if only one side changed the key relative to base (including deleting it), take that side. If both changed it: cards keep the higher `lastMs`, mistakes the higher `at`, support keeps the higher value, everything else the side with the later `savedAt`. |
| value | plan, notepad, commands, unknown top-level keys | Three-way as a whole: take the side that changed; if both did, the later `savedAt` wins. |

Sets and id-keyed lists are written sorted ascending, and logs sorted by `at`, so the same
inputs give byte-identical output on every platform.

After a merge, `savedAt = now`, `device` = this device, and the result becomes both the
local progress and the new `base`.

`tests/fixtures/sync/` holds `base.json`, `local.json`, `remote.json` and `merged.json`.
Each app has a test that merges the first three and compares against `merged.json`,
ignoring `savedAt` and `device`.

## 4. Support reminder

Two shared slices drive the "please support" message, so the count is the same on
every device once they sync:

```jsonc
"opens":   [ "2026-10-14", "2026-10-15", … ],   // local dates the app was opened, YYYY-MM-DD, newest 60 kept
"support": { "pressedAt": ms, "shownAt": ms, "anchor": "2026-10-16" }   // all optional
```

- **Recording opens:** on start and whenever the app comes back to the foreground,
  add today's local date to `opens`. Keep the newest 60 dates.
- **Streak:** the number of consecutive dates ending today in `opens`, counting
  only dates on or after `support.anchor` (if set).
- **When to show:** at most once a day, never during a mock exam or a guided
  session. In order:
  1. If `pressedAt` is set and less than 30 days have passed since it, show nothing.
  2. If `pressedAt` is set, 30 days have passed, and `shownAt` is earlier than
     `pressedAt + 30 days`, show the **"already supported"** message.
  3. Otherwise, if the streak is 7 or more, show the **first** message (the
     "already supported" message instead if `pressedAt` was ever set).
- **After it shows:** set `shownAt = now` and `anchor` = tomorrow's date, so the
  7-day count starts again.
- **Support button (menu):** opens the app's own support message first: a short
  pop-up on the web, a "Support the app" page on the phones. Their Ko-fi button,
  like the reminder's, opens `https://ko-fi.com/T4R728ULEV` and sets
  `pressedAt = now`. Opening the message alone records nothing. The app can't
  see payments, so pressing is all it knows.
- **The menu button's wording** (separate from the reminder's). Title "Support
  PMP Prep"; body "PMP Prep is built by one person in his own time, and stays free
  for everyone: no ads, no account, no tracking. A coffee on Ko-fi pays for new
  questions, testing on real phones and the website."; Ko-fi button "Buy a coffee
  on Ko-fi". The phones' page adds what support pays for and free ways to help:
  share the app, report a wrong question, star it on GitHub.
- **Wording.** First message: title "Enjoying PMP Prep?", body "You've studied 7 days
  in a row. If the app is helping you, please support it. It's free, has no ads,
  and your support keeps it going." The "already supported" message has body "If
  the app is helping you, please support it. It's free, has no ads, and your
  support keeps it going. If you have already supported, please ignore this
  message." Buttons: "Not now" and "Support the app".

## 5. Cloud sync through Google Apps Script

The user deploys `sync/pmp-sync.gs` (the same text is embedded in every app's Cloud sync
screen) as a web app in their own Google account. The script keeps one file,
`PMP Prep/pmp-sync.json`, in their Drive.

**Setup, as the in-app guide shows it:**
1. The app makes a random 32-character sync code and shows the script with the code already filled in.
2. Open https://script.google.com, choose **New project**, replace everything in `Code.gs` with the copied script and save it.
3. Choose **Deploy → New deployment → Select type: Web app**. Set **Execute as: Me** and **Who has access: Anyone**, then **Deploy**.
4. Authorise it. Choose your Google account. On "Google hasn't verified this app", choose **Advanced → Go to (project) (unsafe)** (it's your own script), then **Allow**.
5. Copy the **Web app URL** (`https://script.google.com/macros/s/…/exec`) and paste it into the app. Press **Test connection**.
6. On every other device, open Cloud sync, choose **I already have a script**, and paste the same URL and sync code.

"Anyone" only means the URL answers without a Google sign-in. Every request must carry
the sync code, and the script refuses anything without it. Whoever has both the URL and
the code can read and replace the progress file, so keep them private. To revoke access,
change the code in the script and deploy a new version.

**Protocol.** Every call is an HTTPS POST to the `/exec` URL with body
`Content-Type: text/plain;charset=utf-8` (text/plain avoids a CORS preflight in the web
app). Clients follow the 302 redirect Google sends, as a GET.

| Request body (JSON) | Response (JSON) |
| --- | --- |
| `{ "code": "...", "op": "ping" }` | `{ "ok": true, "rev": 7, "savedAt": ms }` |
| `{ "code": "...", "op": "get" }` | `{ "ok": true, "rev": 7, "doc": {…} \| null }` |
| `{ "code": "...", "op": "put", "baseRev": 7, "doc": {…} }` | `{ "ok": true, "rev": 8 }` or `{ "ok": false, "conflict": true, "rev": 9, "doc": {…} }` |
| wrong code | `{ "ok": false, "error": "bad-code" }` |

**Client algorithm.**
1. `get`, then merge `base`, `local` and `remote`.
2. If the merged document differs from the remote one, `put` with `baseRev`.
3. On a conflict, merge again with the returned doc and `put` again, at most 3 times.
4. Save the merged document as local progress and as base.

Sync runs:
- when the app opens or comes to the foreground;
- when it goes to the background or the tab is hidden;
- after a finished session, practice set, mock or card session;
- when the user presses **Sync now**.

It never runs during a mock. Errors are shown on the Cloud sync screen with the time of
the last successful sync, and are never shown as pop-ups. Sync is off until set up.
