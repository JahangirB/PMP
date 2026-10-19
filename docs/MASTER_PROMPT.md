# Master authoring prompt — PMP question packs

For maintainers growing the official packs in `packs/`. Learners making their
own questions use `docs/question-generator-prompt.md` instead (Backup and packs
in the app links to it).

Reusable across all 26 ECO tasks. Replace `<task-id>` and `<label>`, adjust the
counts block for Business Environment tasks, paste the whole thing.

Attach **only** `AUTHORING.md` and the task's ledger slice, `ledger/<task-id>.md`.
Never attach the app — it costs context and the model does not need it. After
adding the batch, run `node tools/ledger.mjs` to regenerate the ledger.

---

## The prompt

```text
Attached: AUTHORING.md, ledger/<task-id>.md

Author 20 new PMP practice questions for ECO task <task-id> (<label>).

DELIVERABLE
- One downloadable file named exactly <task-id>-a.json (or -b for the second half).
- A valid pack per AUTHORING.md. No other files.
- IDs: pmp-<abbr>-NNN per AUTHORING.md §3.2 (e.g. pmp-pr6-141), continuing
  from the highest pack ID for this task in the ledger slice (packs start at 101).
- All wording original. Do not reproduce PMI or PMBOK text.

BATCH MIX — exact counts, not approximate
- Approach:   8 predictive, 7 adaptive/agile, 5 hybrid
- Difficulty: 5 at 1-2 combined, 10 at 3, 5 at 4-5 combined
              Use difficulty 1 and difficulty 5. Both are under-used.
- Type:       12 single, 3 multi, 2 matching, 1 pulldown, 1 fillblank, 1 hotspot
- Use "hotspot" for a chart of data and "graphic" for a diagram that is itself
  the subject (AUTHORING.md §4.5–4.6). Both carry graphic + regions, no options.

DIFFICULTY MEANING
- 1: one option is clearly right once the stem is read. No trickery.
- 3: two options are defensible; one is better on sequence, authority or timing.
- 5: all four are defensible to an experienced PM; the discriminator is subtle.
  Difficulty 5 never means obscure trivia, trick wording, or information the
  stem withheld.

STEMS
- 40-120 words, aim for 55-90. Scenario-based, requiring judgement.
- Concrete and specific: real settings, real numbers, named roles.
- Enough information to distinguish the intended answer without unstated
  assumptions about policy, authority, contract or lifecycle.
- No irrelevant detail added to reach a word count.

OPTIONS — ANSWER FAIRNESS
The correct answer must not be identifiable before the candidate analyses the
scenario. Three separate leaks to avoid.

1. POSITION
   Distribute authored correct IDs across all positions. No position may be
   correct in more than 35% of items in the batch.

2. LENGTH
   The correct option must not be systematically longest.
   If an option set looks unbalanced, RAISE THE DISTRACTORS to the correct
   option's register. Never shorten the correct option, and never trim a
   distractor to equalise a count. Shortening distractors creates the exact
   signal you are trying to remove.

3. SHAPE
   All options read as written by one author at one level of professional
   sophistication. The correct option must not be the only one carrying
   sequencing language, qualifiers, caveats or PMP-register vocabulary, and
   must not contain more reasoning steps or actions than the others.
   No absolutes (always, never) unless the concept requires them.
   Never use fixed:true on a correct option.

DISTRACTORS
- Each must be an action a competent project manager could genuinely consider.
- Wrong on timing, authority, sequence, scope, assumption or objective —
  never wrong on obviousness.
- No straw men, no unethical or careless options.
- Each carries its own rationale naming the specific candidate error it
  represents. Not merely "this is incorrect".

ANSWER ARCHETYPE
Tag each item with the reasoning pattern its correct answer rewards:
  make-visible | diagnose-first | route-to-owner | act-now |
  use-own-authority | accept-and-adapt | trade-off |
  challenge-the-premise | apply-the-rule

No archetype may exceed 25% of the batch. At least five of the nine must
appear. This stops the bank teaching a single house philosophy that a
candidate can learn as a shortcut — not every correct answer should be the
governance-purist answer.

EXPLANATION AND MINDSET
- The explanation states the transferable rule, so the learner can solve a
  different scenario testing the same concept. Not a restatement of why the
  keyed answer is right.
- Every item carries a non-empty mindset sentence: the disposition that
  outlives this particular scenario, in one line.

SCENARIO NOVELTY
- The ledger slice lists every scenario already in the bank for this task.
- Compare at the concept level, not the wording level. Reject any item
  reusing the same underlying situation, conflict, decision, numbers,
  stakeholder pattern or causal structure.
- Also compare the 20 drafts against each other.
- Do not reuse one scenario skeleton with different nouns.

TECHNICAL VALIDITY
- Follow AUTHORING.md exactly for schema, field names and type-specific
  structures. Invent no fields.
- Matching, pulldown, fillblank and hotspot items must satisfy their own
  schema requirements.
- Verify every correct answer is encoded correctly.

REPORT WITH THE OUTPUT
1. Correct-option ID distribution, as counts.
2. How often the correct option is the longest, as N/total and a percentage.
   Reference point is 25% for four options. If above 35%, fix before
   delivering — by raising distractors.
3. Mean correct-option word count vs mean distractor word count.
4. Archetype distribution.
5. Exact type, approach and difficulty counts.
6. Number of items rewritten before output, split by cause: scenario
   duplication, answer leakage, ambiguity, schema.
7. Nothing for the ledger: it is regenerated from the packs by tools/ledger.mjs.

If any reported figure breaches a threshold above, fix it and re-report
rather than delivering with a note.
```

---

## Business Environment tasks

Eight tasks at 35 items each. Run as 18 + 17, and scale:

- 18-item half: 7 predictive, 6 agile, 5 hybrid; 4 easy / 9 medium / 5 hard;
  11 single, 2 multi, 2 matching, 1 pulldown, 1 fillblank, 1 hotspot
- 17-item half: 7 predictive, 6 agile, 4 hybrid; 4 easy / 9 medium / 4 hard;
  10 single, 3 multi, 1 matching, 1 pulldown, 1 fillblank, 1 hotspot

---

## Why 20 and not 40

Every failure in the Process-6 rounds was an instruction-following failure
under a long prompt with many simultaneous constraints — distractors were
shortened when the instruction said to lengthen them, and two mechanical fixes
were skipped entirely. Halving the batch halves the number of constraints held
in tension at once, and a bad half costs one session to redo instead of two.

52 sessions instead of 26. The extra sessions are cheaper than the rework.

---

## Never use a model for mechanical fixes

Reordering options, reassigning IDs, retyping items, merging versions — these
are deterministic transformations. Three consecutive attempts to do them
through a prompt produced a regression that went unnoticed for two rounds.
Write a script, or ask for one, and verify the output with a diff.