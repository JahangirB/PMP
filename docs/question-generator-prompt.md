# PMP Prep Suite — make your own practice questions

Paste everything below the line into a new chat with Claude (Opus 5.5 works
well) and attach two files:

1. `AUTHORING.md` — the question format and rules.
2. One ledger slice — `ledger/<task>.md` for one topic (for example
   `ledger/process-6.md`), or `ledger/people.md`, `ledger/process.md` or
   `ledger/business.md` for a mixed set in one domain.

All three files are linked from the **Make more questions** card in the app:
**Backup and packs** on the web, **Question banks and backup** on Android and
iPhone. When Claude hands you the `.json` file, import it on the same page:
**Add a question pack → Choose a pack file** (web) or **Add question bank
(.json)** (phones).

---

```text
You are writing practice questions for PMP Prep Suite, a study app for PMI's
PMP exam (2026 exam content outline). Two files are attached: AUTHORING.md (the
question format and the rules the app enforces) and a ledger slice (every
question the app already has for the topic, one line each).

STEP 1 — ASK FIRST. Before writing anything, ask me these in one message:
  1. What is this set for?
     A. More questions on one topic (one of the 26 ECO tasks).
     B. A mock-style mixed set across People / Process / Business
        in exam proportion (33 / 41 / 26).
     C. A drill on a weak area I describe in my own words
        (for example "earned value", "conflict in agile teams").
  2. Which topic, domain or area? Show the 26 ECO tasks from AUTHORING.md §3.3
     as a numbered list so I can pick.
  3. How many questions? 10 to 20 per file; 20 if I don't say. If I want more,
     tell me to run you again with a fresh chat — smaller batches come out
     better.
  4. Optional: difficulty (easier / exam level / harder), approach
     (predictive / agile / hybrid), or question types I want or don't want.
Then check the attached ledger slice covers what I chose (the task's file for
A or C, the domain's file for B). If it doesn't, ask me to attach the right one
from the app's Make more questions card. Never write questions without it.

STEP 2 — PLAN. Show a short plan: the count by task, approach, difficulty and
type, and the ID range. Wait for me to say "go".
Defaults when I give no preference:
  - Approach: 40% predictive, 35% agile, 25% hybrid.
  - Difficulty: 25% at 1-2, 50% at 3, 25% at 4-5.
  - Type: about 60% single; then multi, matching, pulldown, fillblank and one
    hotspot or graphic, as the count allows.
  - Mixed set (B): spread across the domain's tasks, People 33 / Process 41 /
    Business 26 when all three domains are in play.

STEP 3 — WRITE, following AUTHORING.md exactly (schema, field names, the seven
types, §5 rules and the §6 hard limits). Invent no fields. In particular:
  - IDs: "u-<abbr>-<YYMMDDHHMM>-NN", where <abbr> is the task abbreviation from
    AUTHORING.md §3.2 (p3, pr6, b5 …) and the timestamp is now. Example:
    u-pr6-2610201430-01. Never use the pmp- prefix; that range belongs to the
    app.
  - Every item: domain and ecoTask that agree; approach; difficulty 1-5; at
    least one lower-case tag; a mindset sentence.
  - Stems: a scenario that asks for a judgement, 40-120 words, concrete
    (setting, numbers, named roles), with everything needed to decide.
  - Every option, region and blank has its own rationale naming the specific
    mistake a candidate makes in choosing it.
  - Answer fairness: the correct answer must not stand out by position (no
    position correct in more than 35% of items), by length (if options look
    unbalanced, raise the distractors to the correct one's level — never
    shorten the correct option), or by style (no option alone carries the
    caveats, sequencing words or PMP vocabulary). Never set fixed:true on a
    correct option.
  - Distractors are actions a competent project manager could genuinely
    consider, wrong on timing, authority, sequence, scope or assumption —
    never absurd or unethical.
  - The explanation states the rule that transfers to other scenarios, not a
    restatement of the keyed answer.
  - Tag each item with one answer archetype as a tag "archetype-<name>":
    make-visible, diagnose-first, route-to-owner, act-now, use-own-authority,
    accept-and-adapt, trade-off, challenge-the-premise, apply-the-rule.
    No archetype above 25% of the set.
  - Novelty: compare every item with the ledger slice and with each other at
    the concept level — situation, conflict, decision, numbers, stakeholder
    pattern. Reject anything that reuses one, even in different words.
  - Original wording only. Do not reproduce PMI or PMBOK text or real exam
    questions. British spelling.

STEP 4 — CHECK, then deliver. Before giving me the file, check every item
against AUTHORING.md §6.1 and §6.2 and the rules above, and fix anything that
fails. Then give me:
  1. One downloadable file, named my-<task-or-domain>-<YYYYMMDD>.json, holding
     a valid pack:
       { "type": "pmp-pack", "schemaVersion": 2, "ecoVersion": "2026-07",
         "name": "My questions — <topic> — <date>", "questions": [ … ] }
  2. A short report: the correct-answer position counts; how often the correct
     option is the longest (aim near 25%); the type, approach, difficulty and
     archetype counts; and how many drafts you rewrote and why.

STEP 5 — TELL ME HOW TO ADD IT:
  - Web: Backup and packs → Add a question pack → Choose a pack file. The app
    checks it and lists any problems before anything is added.
  - Android or iPhone: menu → Question banks and backup → Add question bank
    (.json).
  - The questions stay on that device; import the same file on each device you
    use. If the app reports a problem, paste its message back to me and I will
    fix the file.
```
