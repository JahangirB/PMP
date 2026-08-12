# Authoring extended question packs

Everything needed to write a batch of PMP Prep Suite questions that the app will
accept. This file is self-sufficient by design: you should not need to read
`index.html` to author against it.

- [1. What you are producing](#1-what-you-are-producing)
- [2. The file envelope](#2-the-file-envelope)
- [3. The item schema](#3-the-item-schema)
- [4. The seven types, with a worked example each](#4-the-seven-types-with-a-worked-example-each)
- [5. Authoring rules](#5-authoring-rules)
- [6. Hard limits the app enforces](#6-hard-limits-the-app-enforces)
- [7. Adding a batch](#7-adding-a-batch)
- [8. Checklist](#8-checklist)

---

## 1. What you are producing

One JSON file per ECO task, named after the task, holding that task's batch of
items. The manifest at `packs/index.json` already lists all 26 batches, so you
never edit it — you drop your file into `packs/` and it is picked up on the next
load.

| Batch | Files | Items each | Total |
|---|---|---:|---:|
| `people-1.json` … `people-8.json` | 8 | 40 | 320 |
| `process-1.json` … `process-10.json` | 10 | 40 | 400 |
| `business-1.json` … `business-8.json` | 8 | 35 | 280 |
| | **26** | | **1000** |

Every item in `people-3.json` is tagged `ecoTask: "people-3"`. A batch is
single-task by construction; that is what makes 26 independent files possible.

Those counts are not arbitrary. The 300-item core pack embedded in the app sits
at People 37.3% / Process 44.0% / Business 18.7% against a published weighting of
33 / 41 / 26 — Business is 7.3 points light and cannot be corrected inside 300
items. The batch sizes above are chosen to fix that: the full 1300-item pool
lands at **33.2 / 40.9 / 25.8**. If you change a batch size, you move the pool
off the published weighting, and a domain more than 8 points out is a *failure*
in the content audit, not a warning.

**A batch is also a valid import file.** The same file can be pasted or
uploaded through *Backup and packs* inside the app, which works offline. That is
the fallback for anyone who cannot host the directory.

---

## 2. The file envelope

```json
{
  "type": "pmp-pack",
  "schemaVersion": 2,
  "ecoVersion": "2026-07",
  "name": "People 3 — Leading the team",
  "questions": [ /* … items … */ ]
}
```

| Field | Required | Notes |
|---|---|---|
| `type` | yes | Must be exactly `"pmp-pack"`. |
| `schemaVersion` | yes | Number. Currently `2`. A file with a higher number is refused with a message telling the reader to update the app. |
| `ecoVersion` | recommended | `"2026-07"`. A mismatch is a warning, not a refusal, but items tagged to a retired task table will not count toward coverage. |
| `name` | no | Shown in the import summary. |
| `questions` | yes | Array of items. |

If `questions` is missing or is not an array, the whole file is refused.

---

## 3. The item schema

### 3.1 Fields on every item

| Field | Type | Required | Notes |
|---|---|---|---|
| `id` | string | **yes** | Unique across the entire pool, not just this file. See the id convention below. |
| `type` | string | **yes** | One of the seven in §4. |
| `domain` | string | **yes** | `people`, `process` or `business`. Must agree with the domain of `ecoTask`. |
| `ecoTask` | string | **yes** | The task id, e.g. `people-3`. Must be one of the 26 in §3.3. |
| `approach` | string | **yes** | `predictive`, `agile` or `hybrid`. |
| `difficulty` | number | **yes** | Integer 1–5. 1–2 easy, 3 medium, 4–5 hard. |
| `tags` | string[] | **yes** | Lower-case, hyphenated. Drives the tag filter. An item with no tags cannot be found by it. |
| `stem` | string | **yes** | The situation and the question. |
| `explanation` | string | **yes** | The rule that transfers. |
| `mindset` | string | **yes in practice** | One sentence naming the disposition the item turns on. Formally a warning rather than a refusal, for one historical reason given in §6.3 — write it anyway. |

Then, depending on type: `options`, `prompts`, `regions`, `blanks`, `graphic`.

### 3.2 The id convention

`pmp-` + a task abbreviation + `-` + a three-digit sequence:

```
people-1  -> pmp-p1-###          process-6  -> pmp-pr6-###
people-8  -> pmp-p8-###          process-10 -> pmp-pr10-###
business-5 -> pmp-b5-###
```

The core pack uses `001`–`014` in each task. **Start extended batches at `101`**
(`pmp-p3-101`, `pmp-p3-102`, …). That leaves the core pack's range alone and
makes an item's origin readable from its id.

An id that already exists in the pool is **skipped, not overwritten** — the app
will never let a pack quietly replace a question someone has answer history
against. A duplicate id therefore silently costs you the item. Sequence
carefully.

### 3.3 The 26 ECO tasks

| id | Label | | id | Label |
|---|---|---|---|---|
| `people-1` | Shared purpose | | `process-7` | Quality |
| `people-2` | Conflict | | `process-8` | Schedule |
| `people-3` | Leading the team | | `process-9` | Status and artifacts |
| `people-4` | Stakeholder engagement | | `process-10` | Closure |
| `people-5` | Expectation alignment | | `business-1` | Governance |
| `people-6` | Customer expectations | | `business-2` | Compliance |
| `people-7` | Knowledge transfer | | `business-3` | Change control |
| `people-8` | Communication | | `business-4` | Impediments and issues |
| `process-1` | Integrated planning | | `business-5` | Risk |
| `process-2` | Scope | | `business-6` | Continuous improvement |
| `process-3` | Value delivery | | `business-7` | Organisational change |
| `process-4` | Resources | | `business-8` | External environment |
| `process-5` | Procurement | | | |
| `process-6` | Project finance | | | |

### 3.4 The option object

Used by `single`, `multi`, `matching` and `pulldown`.

| Field | Type | Notes |
|---|---|---|
| `id` | string | Unique within the item. |
| `text` | string | The option as displayed. No two options may say the same thing. |
| `correct` | boolean | Required on every option, `false` included. |
| `rationale` | string | **Required on every option, right and wrong.** Minimum 25 characters. |
| `match` | string \| null | `matching` / `pulldown` only: the `prompts[].id` this option answers, or `null` for a deliberate unused distractor. |
| `fixed` | boolean | Optional. Keeps the option in its authored position when the others are shuffled — for an "all of the above" style tail that reads wrongly anywhere but last. |

Option order is shuffled on screen unless the reader turns shuffling off, so
never write an option that refers to another by position.

---

## 4. The seven types, with a worked example each

Two of the seven are presentation variants that resolve to another type's
shape: **`pulldown` is a `matching` item** and **`graphic` is a `hotspot`
item**. They differ in how they render, not in what they carry.

### 4.1 `single` — multiple choice

Exactly one option marked `correct: true`. 3–10 options.

```json
{
  "id": "pmp-p3-101",
  "type": "single",
  "domain": "people",
  "ecoTask": "people-3",
  "approach": "hybrid",
  "difficulty": 3,
  "tags": ["leadership", "delegation", "handover"],
  "stem": "A team lead who has run your integration workstream for a year is going on three months' parental leave in a fortnight. She is the only person who has ever spoken to the two legacy system owners, and she has offered to stay reachable by phone. What should the project manager do?",
  "options": [
    { "id": "a", "text": "Accept the offer to stay reachable and keep the workstream running as it is.", "correct": false,
      "rationale": "It converts a foreseeable absence into a dependency on somebody's goodwill during their leave. It also guarantees the handover never happens, because there is never a moment when it has to." },
    { "id": "b", "text": "Have her work alongside a named successor for the fortnight, starting with the two system-owner relationships.", "correct": true,
      "rationale": "Two weeks of overlap is the asset here and it expires. Spending it on the relationships rather than the documentation is right because the relationships are the part that cannot be written down and handed over later." },
    { "id": "c", "text": "Ask her to write a detailed handover document covering the workstream and its contacts.", "correct": false,
      "rationale": "A document captures what she knows she knows. It will not carry the standing she has with two system owners who have never met her replacement, which is the thing actually at risk." },
    { "id": "d", "text": "Split her responsibilities across the three remaining senior engineers for the duration.", "correct": false,
      "rationale": "Splitting a role with external relationships in it leaves the system owners with three new contacts and no single one accountable. Shared ownership of a relationship usually means nobody owns it." }
  ],
  "explanation": "A planned absence is a transfer problem, and the scarce resource is overlap time with the person leaving. Spend it on what cannot be documented — relationships, judgement, the reasons behind past decisions — and let the written handover carry the rest. The test for any handover plan is whether it still works on a day the departing person does not answer the phone.",
  "mindset": "Treat a known departure as a transfer to be completed, not a gap to be covered."
}
```

### 4.2 `multi` — multiple response

At least two options marked `correct: true`. Say how many in the stem
("Which two actions…"), because partial credit is awarded and the reader needs
to know what they are aiming at.

```json
{
  "id": "pmp-p4-101",
  "type": "multi",
  "domain": "people",
  "ecoTask": "people-4",
  "approach": "predictive",
  "difficulty": 3,
  "tags": ["stakeholders", "analysis", "regulator"],
  "stem": "Eight weeks before submission, you learn that the regulator reviewing your product has changed its lead assessor. The new assessor has published criticism of the approach your submission is built on. Which two actions should the project manager take?",
  "options": [
    { "id": "a", "text": "Request an early technical meeting with the new assessor before the submission is finalised.", "correct": true,
      "rationale": "A published position is not yet a position on your submission. The meeting converts a guess about how they will read it into something you can plan against, while there is still time to act." },
    { "id": "b", "text": "Re-run the stakeholder analysis for the regulator, treating the change of assessor as a change of stakeholder.", "correct": true,
      "rationale": "Influence and attitude are attributes of a person, not of an institution. A new assessor with a published view is a materially different stakeholder and the register should say so." },
    { "id": "c", "text": "Rewrite the submission to follow the approach the new assessor has argued for.", "correct": false,
      "rationale": "It abandons a defensible technical position on the strength of a general publication, before anyone has said it is a problem. If the original approach is sound, the work is to make that case." },
    { "id": "d", "text": "Escalate to the sponsor to ask whether the organisation can request a different assessor.", "correct": false,
      "rationale": "Challenging a regulator's staffing spends significant credibility on something you have no standing to decide, and it signals that you expect to fail the review on its merits." },
    { "id": "e", "text": "Continue as planned and address any criticism in the response to findings.", "correct": false,
      "rationale": "It leaves a known, specific risk unmanaged until the point at which it is most expensive to fix. Waiting for findings turns eight weeks of options into none." }
  ],
  "explanation": "A change of individual inside a stakeholder organisation is a change of stakeholder, and it re-opens the analysis rather than merely updating a name. The response is to gather information directly and re-plan the engagement, not to capitulate to a position that has not yet been taken and not to hope it does not apply. Engagement strategies are built on people, and people are replaceable in a way that the institution is not.",
  "mindset": "A stakeholder is a person, so a change of person is a change of stakeholder."
}
```

### 4.3 `matching` — pair each prompt with an option

`prompts[]` is the left-hand side, in authored order (never shuffled — prompts
often read as a sequence). Every prompt needs **exactly one** option whose
`match` equals its `id`. Options with `match: null` are deliberate unused
distractors and still need a rationale saying why they are not used.

```json
{
  "id": "pmp-pr5-101",
  "type": "matching",
  "domain": "process",
  "ecoTask": "process-5",
  "approach": "predictive",
  "difficulty": 3,
  "tags": ["procurement", "contract-types", "risk"],
  "stem": "Match each procurement situation to the contract type that allocates risk appropriately. One choice is not used.",
  "prompts": [
    { "id": "s1", "text": "A well-specified building fit-out where the scope is fully known and unlikely to move." },
    { "id": "s2", "text": "A research programme where the work needed cannot be described until the first phase reports." },
    { "id": "s3", "text": "A five-year support contract where the buyer wants the supplier rewarded for reducing running costs." }
  ],
  "options": [
    { "id": "ffp", "text": "Firm fixed price", "correct": true, "match": "s1",
      "rationale": "A fully defined scope is the condition under which fixed price works: the seller can price the risk because the risk is knowable, and the buyer gets cost certainty for it." },
    { "id": "cpff", "text": "Cost plus fixed fee", "correct": true, "match": "s2",
      "rationale": "When the work cannot be specified, no seller can price it honestly. Reimbursing cost with a fixed fee keeps the buyer carrying the uncertainty they actually own, and stops the seller pricing in a contingency nobody can size." },
    { "id": "cpif", "text": "Cost plus incentive fee", "correct": true, "match": "s3",
      "rationale": "An incentive fee ties the seller's return to a measurable outcome the buyer wants. Sharing the saving is what makes cost reduction worth the seller's effort over five years." },
    { "id": "tm", "text": "Time and materials", "correct": false, "match": null,
      "rationale": "Not used. Time and materials suits small, urgent, short-duration work such as staff augmentation, where the alternative is spending longer specifying the job than doing it. None of the three above is that." }
  ],
  "explanation": "Contract type is a decision about who carries which uncertainty, and it follows how well the scope can be defined. A definable scope lets the seller take the cost risk; an undefinable one means the buyer keeps it, whatever the contract says on paper; and where the buyer wants behaviour rather than delivery, the fee structure is the lever. Choosing fixed price for undefined work does not transfer the risk — it transfers it back with a dispute attached.",
  "mindset": "Pick the contract for how well the work can be described, not for how much certainty you would like."
}
```

### 4.4 `pulldown` — drop-down in the stem

Structurally a `matching` item. The stem is a standing instruction and each
prompt reads as a sentence with a blank to complete.

```json
{
  "id": "pmp-b3-101",
  "type": "pulldown",
  "domain": "business",
  "ecoTask": "business-3",
  "approach": "hybrid",
  "difficulty": 3,
  "tags": ["change-control", "baseline", "authority"],
  "stem": "Complete each sentence with the response that fits. One choice is not used.",
  "prompts": [
    { "id": "c1", "text": "A stakeholder asks for a change that is inside the current sprint's committed scope and would displace agreed work. The team should" },
    { "id": "c2", "text": "A defect is found in delivered functionality that never met the agreed acceptance criteria. This should be handled by" }
  ],
  "options": [
    { "id": "refuse", "text": "decline it for this sprint and take it to the backlog for prioritisation", "correct": true, "match": "c1",
      "rationale": "Sprint scope is a commitment the team made in exchange for a stable period to work in. Protecting it is what makes the estimate mean anything, and the backlog is where the request gets weighed against everything else." },
    { "id": "defect", "text": "fixing it as defect remediation, with no change request", "correct": true, "match": "c2",
      "rationale": "Work that never met the agreed criteria was never delivered, so putting it right is completing the original scope. Raising a change request for it would let the baseline absorb a failure as if it were a new requirement." },
    { "id": "ccb", "text": "raising a change request for the change control board", "correct": false, "match": null,
      "rationale": "Not used. A change request is for a change to an agreed baseline. Neither situation here is one: the first is a request that has not yet been prioritised, and the second is unfinished work." }
  ],
  "explanation": "Change control applies to changes to what was agreed, and the recurring error is applying it to things that are not changes. Work that never met its acceptance criteria is incomplete rather than changed; a request that has not been accepted into scope is a candidate rather than a change. Routing either through a board makes the baseline a record of what was asked for rather than what was agreed.",
  "mindset": "Ask first whether the baseline is actually being changed, because most things routed through change control are not."
}
```

### 4.5 `hotspot` — select a region of a diagram

Carries `graphic` and `regions`, and **no `options`**. Regions are positions in
a picture and are never shuffled. Every region needs a rationale, including the
wrong ones. Coordinates are in the `viewBox` space.

```json
{
  "id": "pmp-pr9-101",
  "type": "hotspot",
  "domain": "process",
  "ecoTask": "process-9",
  "approach": "predictive",
  "difficulty": 3,
  "tags": ["evm", "s-curve", "variance"],
  "stem": "The chart plots planned value, earned value and actual cost against time at the current status date. Select the gap that represents the schedule variance.",
  "graphic": {
    "viewBox": "0 0 640 320",
    "title": "Three cost curves against time with the status date marked",
    "caption": "Select a gap. Use Tab or the arrow keys to move between them, then Enter or Space."
  },
  "regions": [
    { "id": "g1", "n": 1, "label": "Between earned value and planned value", "x": 300, "y": 90, "w": 90, "h": 70, "correct": true,
      "rationale": "Schedule variance is earned value minus planned value: the gap between what was scheduled to be earned by now and what actually has been. Measured in currency, but it is a statement about time." },
    { "id": "g2", "n": 2, "label": "Between earned value and actual cost", "x": 300, "y": 170, "w": 90, "h": 70, "correct": false,
      "rationale": "This gap is the cost variance — value earned against money spent to earn it. It is the most common substitution on this chart, because both variances are read off the same vertical line." },
    { "id": "g3", "n": 3, "label": "Between planned value and actual cost", "x": 300, "y": 90, "w": 90, "h": 150, "correct": false,
      "rationale": "The distance between what was planned to be spent and what was spent is not a variance in earned value analysis at all. It compares two figures that say nothing about how much work is finished." },
    { "id": "g4", "n": 4, "label": "Between the status date and the planned finish", "x": 470, "y": 40, "w": 120, "h": 250, "correct": false,
      "rationale": "This is remaining duration, not variance. It describes how much calendar is left rather than how far ahead or behind the work is against the plan." }
  ],
  "explanation": "Both variances are read from the same status line, and which one you have depends on which pair of curves you measure between. Earned against planned answers whether the work is on schedule; earned against actual answers whether it is on budget. Naming the curves before reading the gap is what stops the two being swapped, and swapping them reverses the diagnosis.",
  "mindset": "Name which two curves a gap sits between before saying what it means."
}
```

### 4.6 `graphic` — hotspot over a supplied diagram

Structurally identical to `hotspot`: `graphic` plus `regions`, no `options`.
Use it where the diagram is the subject rather than a chart of data.

> **Note.** No item in the core pack uses this type, and not by choice. The
> validator's missing-options check tested the raw type rather than the resolved
> base type, so a `graphic` item — which correctly carries regions instead of
> options — was refused for a missing options array. That bug is fixed. This is
> the one type with no existing item to duplicate.

```json
{
  "id": "pmp-b1-101",
  "type": "graphic",
  "domain": "business",
  "ecoTask": "business-1",
  "approach": "hybrid",
  "difficulty": 4,
  "tags": ["governance", "escalation", "thresholds"],
  "stem": "The diagram shows your programme's escalation ladder with the decision threshold at each level. A change adding £180,000 and four weeks has just been requested. Select the level at which this decision should be taken.",
  "graphic": {
    "viewBox": "0 0 600 400",
    "title": "Four-level escalation ladder with authority thresholds",
    "caption": "Select a level. Use Tab or the arrow keys to move between levels, then Enter or Space."
  },
  "regions": [
    { "id": "pm", "n": 1, "label": "Project manager — up to £25,000 and 1 week", "x": 40, "y": 300, "w": 520, "h": 70, "correct": false,
      "rationale": "The request exceeds both delegated limits several times over. Deciding it here would be acting outside the authority the governance structure grants, whatever the merits of the change." },
    { "id": "steer", "n": 2, "label": "Steering group — up to £100,000 and 2 weeks", "x": 40, "y": 210, "w": 520, "h": 70, "correct": false,
      "rationale": "Closer, but still short on both dimensions. A threshold breached on either measure escalates, and this one is breached on both." },
    { "id": "board", "n": 3, "label": "Programme board — up to £250,000 and 6 weeks", "x": 40, "y": 120, "w": 520, "h": 70, "correct": true,
      "rationale": "The first level whose authority covers both the cost and the schedule impact. Escalating to exactly the level that can decide is the point of a threshold ladder." },
    { "id": "exec", "n": 4, "label": "Executive committee — unlimited", "x": 40, "y": 30, "w": 520, "h": 70, "correct": false,
      "rationale": "Unlimited authority makes this a valid decision-maker but the wrong one. Escalating past the level that can decide wastes executive attention and teaches the board its thresholds are optional." }
  ],
  "explanation": "An escalation ladder is read by finding the lowest level whose authority covers every dimension of the impact, and a breach on any one dimension pushes the decision up. Stopping short means deciding without authority; going past means spending scarce attention and undermining the level you skipped. Thresholds only govern anything if they are applied in both directions.",
  "mindset": "Escalate to the lowest level that can actually decide — no lower, and no higher."
}
```

### 4.7 `fillblank` — fill in the blank

Carries `blanks[]` and **no `options`**. A blank is either numeric or text.

| Field | Required | Notes |
|---|---|---|
| `id` | yes | Unique within the item. |
| `label` | yes | Names the quantity and its unit. This is the whole instruction the reader gets. |
| `numeric` | for numeric | `true`. |
| `expect` | numeric only | The number. |
| `tol` | numeric only | Tolerance, **required even when zero**. |
| `unit` | no | Displayed beside the input. |
| `accept` | text only | Non-empty array of accepted strings, matched case- and punctuation-insensitively. List the plausible phrasings. |
| `rationale` | yes | Works the answer through. Minimum 25 characters. |

```json
{
  "id": "pmp-pr6-101",
  "type": "fillblank",
  "domain": "process",
  "ecoTask": "process-6",
  "approach": "predictive",
  "difficulty": 3,
  "tags": ["evm", "forecasting", "eac"],
  "stem": "A project has a budget at completion of £600,000. At the status date the earned value is £150,000, the planned value is £200,000 and the actual cost is £180,000. Work down the cost performance index, then the estimate at completion on the assumption that current cost performance continues.",
  "blanks": [
    { "id": "cpi", "label": "Cost performance index, to two decimal places", "numeric": true, "expect": 0.83, "tol": 0.01,
      "rationale": "CPI is earned value divided by actual cost: 150,000 ÷ 180,000 = 0.833. Below 1 means every pound spent has bought less than a pound of planned work." },
    { "id": "eac", "label": "Estimate at completion, in £", "numeric": true, "expect": 720000, "tol": 1000, "unit": "£",
      "rationale": "With current performance assumed to continue, EAC is BAC ÷ CPI: 600,000 ÷ 0.833 = £720,000. The £120,000 overrun is what today's efficiency projects onto the remaining work." },
    { "id": "which", "label": "Which variance is £50,000 unfavourable here — cost or schedule", "accept": ["schedule", "schedule variance", "sv", "the schedule variance"],
      "rationale": "Earned value minus planned value is 150,000 − 200,000 = −£50,000, a schedule variance. The cost variance is 150,000 − 180,000 = −£30,000, so the two are different figures and only one of them is £50,000." }
  ],
  "explanation": "The forecast that assumes current performance continues divides the remaining budget by the efficiency achieved so far, which is why CPI has to be established before EAC can be. The choice of formula is a judgement about whether what caused the overrun is still present: a one-off cause argues for BAC minus current variance instead, and stating which assumption you are making is part of giving the number. A forecast without its assumption attached is not a forecast.",
  "mindset": "Say which assumption a forecast rests on, because the formula is a claim about the future, not a calculation about the past."
}
```

---

## 5. Authoring rules

These are the editorial rules the pack is held to. The first five are checked by
the audit panel; the last is not checkable and matters most.

**1. A stem poses a situation and asks for a judgement.** Not a definition
lookup. Give enough detail that the reader has to weigh something: who is
involved, what has already been tried, what the constraint is. Definition-recall
is acceptable only where the type demands it.

**2. Every option carries its own rationale, the wrong ones included.** A
distractor without a reason attached teaches nothing, and on this exam the
distractors are where the learning is.

**3. Every distractor rationale names the specific error a candidate makes in
choosing it.** Not "this is not the best answer" but *why someone plausible
picks it* — which is what makes a wrong answer diagnosable rather than merely
wrong. Write distractors that a competent candidate could choose; an obviously
absurd option wastes one of your four slots.

**4. The explanation states the rule that transfers, not the answer that was
correct this time.** If it can be read without knowing which option was chosen
and still teaches something, it is doing its job. An explanation that merely
restates the winning option is caught by the audit and fails the build.

**5. Each item carries a one-sentence mindset principle** — the disposition the
item turns on, stated so it outlives the particular scenario.

**6. No item may duplicate the situation of another.** This is the rule the app
cannot enforce. Duplicate detection keys on the normalised stem, so it catches
copy-and-paste and nothing else: two items can share a scenario, a lesson and a
correct answer in different words and both pass. **Read `ledger.md` for your task
before writing.** At 14 items per task the obvious scenario is long gone — in the
core pack, 38 of 50 drafted items in one batch were rewritten because they
restated something already there. Assume the obvious situation is taken and go
looking for the second-order one.

### Mix within a batch

A batch of 40 should land near:

| Slice | Target |
|---|---|
| Approach | ~40% predictive, ~35% agile, ~25% hybrid |
| Difficulty | ~25% easy (1–2), ~50% medium (3), ~25% hard (4–5) |
| Type | Majority `single`; 4–6 `multi`; 2–3 each of `matching`, `pulldown`, `fillblank`; 1–2 `hotspot` or `graphic` |

Approach drift is reported as a gap, not a failure — PMI publishes only the
40/60 predictive-to-other split, and the 35/25 sub-split is this build's
assumption. Domain drift **is** a failure, which is why batch sizes are fixed.

### Voice

British spelling. All wording original — no PMI text is reproduced anywhere in
this project and none of these are real exam questions. Use `£`, `€` or plain
numbers rather than always defaulting to one currency. Vary the industry: the
core pack ranges across construction, claims processing, benefits, grocery,
research consortia and plant automation, and reusing one sector repeatedly makes
scenarios blur together even when they are technically distinct.

---

## 6. Hard limits the app enforces

### 6.1 Refusals — a file with any of these is rejected entirely

The whole shard is skipped and reported in the content audit with the item index
and field path. Other shards still load.

- Missing or duplicate `id`; unknown `type`, `domain` or `ecoTask`
- `stem` under 10 characters, or missing `explanation`
- Missing `options` on a type that needs them (`single`, `multi`, `matching`, `pulldown`)
- No option marked `correct`, or any option with no `rationale`
- `matching`/`pulldown` with no `prompts`, or a prompt without exactly one option matched to it
- `hotspot`/`graphic` with no `regions`, no correct region, or no `graphic.viewBox`
- `fillblank` with no `blanks`, a blank with no `id`, a numeric blank with no numeric `expect`, or a text blank with no `accept` list

### 6.2 Audit failures — loads, but breaks the build's self-check

**These are stricter than the refusal list and you must author to them.** A
shard can pass validation, load, and still turn the content audit red. Treat
this list as the real bar.

| Rule | Limit |
|---|---|
| Stem length | ≥ 60 chars (≥ 30 for `fillblank`, `hotspot`, `graphic`) |
| Explanation length | ≥ 80 chars |
| Rationale length | ≥ 25 chars — on every option, region and blank |
| Option count | 3–10 |
| Two options saying the same thing | not allowed |
| `single` | exactly one correct option |
| `multi` | at least two correct options |
| Explanation restating the correct option | not allowed |
| `difficulty` | integer 1–5 |
| `approach` | `predictive`, `agile` or `hybrid` |
| Duplicate stem | not allowed (prompts count toward the key on `matching`/`pulldown`) |
| `blanks[].label` | required |
| `blanks[].tol` | required on numeric blanks, even when zero |
| `blanks[].rationale`, `regions[].rationale` | required, ≥ 25 chars |
| `placeholder: true` | must not appear in a shipped pack |
| Domain share | within 8 points of 33 / 41 / 26 |

Missing `tags` is a gap rather than a failure, but the item becomes invisible to
the tag filter. Always tag.

### 6.3 Why `mindset` is only a warning

The 60 items of the core pack's phase-one seed predate the mindset rule and do
not carry the field. Raising it as a refusal would fail the shipped 300-item
pack against its own self-check and make the app refuse its own exported pack on
re-import. It is therefore a **warning**: indexed, listed in the audit's gaps
table, and not blocking. **Write it anyway.** Every extended item is expected to
have one, and once the seed is backfilled this becomes a hard error.

---

## 7. Adding a batch

1. Author `packs/<ecoTask>.json` against this document — e.g. `packs/people-3.json`.
2. Drop it into the `packs/` directory beside `index.html`.
3. Reload. It is picked up automatically. **`packs/index.json` already lists all
   26 batches and never needs editing.**
4. Open **Content audit** in the app and check the *Content sources* card: your
   batch should read *Loaded* with its item count. If it reads *Refused*, the
   indexed problems are listed underneath.

Until a file is present its manifest entry reads **Pending**, which is the
normal state and not an error.

### If you cannot host

The loader only runs when the app is *served* over http — a `file://` document
has no origin to fetch from, so opening `index.html` from disk gives you the
300-item core pack and nothing else, silently and by design. Either:

- serve the directory (`python3 -m http.server`, GitHub Pages, any static host —
  all paths resolve relative to the document, so a project subpath such as
  `user.github.io/repo/` works), or
- import each shard by hand through **Backup and packs**, which takes a file or
  a pasted string and works offline. Manually imported packs are persisted to
  the device; loader-fetched ones are not.

### Validating before you publish

The app is the validator. Open it, go to **Backup and packs**, paste the shard
in, and read the summary. A refused pack lists every problem with its item index
and field path. Nothing is added until you confirm, so this is a safe dry run —
and it is exactly the check the loader will perform.

---

## 8. Checklist

Per item:

- [ ] `id` unique, follows the convention, sequence starts at 101
- [ ] `ecoTask` matches the file; `domain` agrees with the task
- [ ] `approach` set; `difficulty` 1–5; at least one tag
- [ ] Stem poses a situation and asks for a judgement (≥ 60 chars)
- [ ] Every option, region and blank has its own rationale (≥ 25 chars)
- [ ] Each distractor rationale names the error someone makes in choosing it
- [ ] Explanation gives the transferable rule, not a restatement of the answer (≥ 80 chars)
- [ ] `mindset` written
- [ ] Type-specific shape complete — options / prompts+match / regions+viewBox / blanks+tol
- [ ] Scenario checked against `ledger.md` for this task

Per batch:

- [ ] Correct item count (40, or 35 for Business)
- [ ] Every item tagged to the one task
- [ ] Approach, difficulty and type mixes roughly on target
- [ ] Envelope has `type`, `schemaVersion: 2`, `ecoVersion: "2026-07"`
- [ ] Dry-run imported through *Backup and packs* with no problems reported
