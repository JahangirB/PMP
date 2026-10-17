# Staged content: case studies and graphic items

Nothing here is live yet. These files are written to be spliced into `index.html` by
hand, and they were checked against that file's own rules by `validate.mjs`.

| File | What it holds | Where it goes |
|---|---|---|
| `cases.js` | `PMP.CASES = [ … ];` with 5 cases and 50 items | §05.3. Replaces the one line `PMP.CASES      = [];` |
| `graphic-questions.js` | `PMP.STAGED_GRAPHIC_QUESTIONS = [ … ];` with 12 items of type `graphic` | §05.2. The objects go at the end of `PMP.QUESTIONS` |
| `validate.mjs` | The checker (node, no dependencies) | Stays here |
| `build/*.json` | Plain JSON copies the validator writes | Stays here. The Android tests read it |

## 1. §05.3 case studies

1. Find the line `PMP.CASES      = [];`. It comes just after the comment that begins
   `/* 05.3  Case studies. Still empty by design`.
2. Replace that line with the whole of `cases.js`. Its header comment documents the
   shape, so it can stand in for the old comment.
3. `android/tools/extract-content.mjs` already exports `cases: P.CASES || []` into
   `extras.json`, so both phones pick the cases up on their next build.

Case shape:

```
{ id: "case-01", title, approach, setting, mix: { people, process, business },
  passage: [ "paragraph", … ],              // 300–600 words in all
  exhibits: [ { id, label: "Exhibit 1", title, cols: [], rows: [[]], note } ],
  questions: [ /* 8–12 ordinary §05.2 items, ids pmp-caseN-NNN */ ] }
```

## 2. §05.2 graphic items

1. Find the closing `];` of `PMP.QUESTIONS`. It is the line just before the
   `/* 05.3  Case studies` comment. The last item before it is `pmp-b8-026`.
2. Put a comma after that item's closing `}`, then paste the twelve objects from
   `graphic-questions.js`. That is everything between `PMP.STAGED_GRAPHIC_QUESTIONS = [`
   and the final `];`, without the wrapper line.
3. Update the §05.2 header comment:
   - The Type paragraph now reads 360 items, with 12 graphic. All seven type ids are
     now exercised.
   - Remove the "Still no item uses the graphic type" passage. §09.6 resolves the base
     type before its options check, so graphic items pass.
   - New mix: People 116 / Process 137 / Business 107 (32.2 / 38.1 / 29.7), and
     predictive 148 / adaptive 125 / hybrid 87 (41.1 / 34.7 / 24.2). All are inside
     `MIX_TOLERANCE`.
   - Update `AUTHORING.md` line 40, which says 348.
4. Ids are `pmp-<task>-061`. Nothing else uses 027–100 in any task.
5. Two of the items set `multi: true` (`pmp-pr8-061` has 2 correct, `pmp-b6-061` has 3),
   because `renderHotspot` reads `q.multi`.

## 3. Check

```
node content/staging/validate.mjs           # must end "clean"
node android/tools/extract-content.mjs . /tmp/assets-out
```

Once spliced, the validator's collision check will report every staged id as
colliding, because the ids now exist in `index.html`. That is expected: the check
only applies before the splice.

What the validator checks:

- **§09.6 structure:** ids, types, domains, ECO task for the domain, options, matching
  prompts, regions with a `viewBox`, numeric blanks with `expect` and `tol`.
- **§10.14 editorial floors:** stem 60 characters (30 for fill-in and hotspot),
  explanation 80, a rationale of 25 or more on every option, region and blank,
  3–10 options, single has exactly one correct and multi at least two, no repeated
  stem, no explanation that only repeats the answer, mindset, tags, difficulty and
  approach.
- **§10.07.9 abbreviation scan:** every abbreviation-shaped token is in §05.7.3.
- **Ids** are unique, and clear of `PMP.QUESTIONS`, the lesson checks and every file
  in `packs/`. It finds them by loading §01 and §05 through `vm`, the same way
  `extract-content.mjs` does.
- **Region geometry, by the rules `renderHotspot` draws to:** labels wrap to at most
  3 lines of 14 characters (` ` holds a phrase together); boxes stay inside the
  `viewBox`, are big enough for their text and do not overlap.
- **Case shape:** passage length; every "Exhibit n" mentioned exists; `mix` matches
  the items.

## 4. Web work still to do (not staged)

`layoutFor()` in §10.03.1 already computes `caseItems`, but nothing uses it yet.

- **Mock selection (§10.03.2).** Fill the first `L.caseItems` slots with whole cases,
  freshest first: cases with fewer items answered before come first, and each case is
  taken whole while it still fits. If no whole case fits, which happens in a 20-item
  sprint where the block is 4, take the opening items of one case. Keep each case's
  items in authored order and never shuffle them into the rest. Draw the rest from
  `PMP.QUESTIONS` as now, for `items - block.length`. The phones do exactly this
  (`CasePlan` on both). The break after item 40 then lands at the end of the block.
- **Mock and review rendering.** Above any case item, show a collapsible
  "Case: <title>" panel with the passage paragraphs and the exhibit tables, open by
  default and remembered while moving within the case. Show it in the post-sitting
  review too.
- **Practice.** Add a "Case studies" quick start that runs one whole case in order,
  picking the case with the fewest items answered before.
- **Lookups by id.** Anything that resolves a question id from `PMP.QUESTIONS` alone
  needs the case items as well: history, the mistake journal, notes, flags, search and
  restoring an unfinished sitting. Otherwise attempts at case items are orphaned.
- **Audit (§10.14).** Run `PMP.validate.pack` and the editorial pass over the
  flattened case items, plus a small case-shape check. `shippedRoots()` already
  includes `PMP.CASES`, so the abbreviation scan covers them. Report the case count
  rather than "empty by design".
- **About panel.** It already explains the 40-item block assumption. It could add how
  many cases are loaded.
