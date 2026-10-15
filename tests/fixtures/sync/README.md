# Merge fixture

`merge(base.json, local.json, remote.json)` must equal `merged.json`, compared after
normalising both sides:
- `savedAt` and `device` are ignored;
- a missing shared slice equals an empty one;
- logs are sorted by `at`, then by identity;
- sets and id-keyed lists (`flags`, `lessons`, `mycards`) are sorted ascending;
- object keys are sorted.

What it covers:
- attempts from both sides are united;
- flags: q2 is removed locally, q3 added locally and q4 added remotely;
- notes: q1 changed on both sides, so the later `savedAt` (local) wins; q2 deleted remotely is deleted;
- mistakes: q2 added remotely;
- cards: c1 changed on both sides, so the higher lastMs (remote) wins; c2 suspended remotely;
- plan changed only remotely;
- notepad changed only locally;
- `web` merges per key;
- an unknown top-level key survives the merge.
