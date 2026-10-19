#!/usr/bin/env node
// Builds the question ledger: one row per question the app ships, grouped by
// ECO task, with a one-line gist of the situation it puts the candidate in.
// Authors (people or a model) read it before writing new questions so no
// scenario is repeated. See AUTHORING.md §5 rule 6.
//
//   node tools/ledger.mjs            (from the repo root)
//
// Reads index.html (§01 config and §05 data: the core pack and the case
// studies) and every pack listed in packs/index.json, then writes:
//   ledger.md                 every question, all 26 tasks
//   ledger/<domain>.md        people, process, business
//   ledger/<task>.md          one file per ECO task, for uploading to a model
// No dependencies beyond Node itself.
import fs from 'node:fs';
import path from 'node:path';
import vm from 'node:vm';

const root = process.cwd();
const html = fs.readFileSync(path.join(root, 'index.html'), 'utf8');
const scripts = [...html.matchAll(/<script>([\s\S]*?)<\/script>/g)].map(m => m[1]);
const main = scripts[1];
const data = main.slice(main.indexOf('/* ═══ §05'), main.indexOf('/* ═══ §06'));
const sb = {
  console,
  localStorage: { getItem: () => null, setItem() {} },
  document: { documentElement: { setAttribute() {}, style: {} } },
  matchMedia: () => ({ matches: false }),
};
sb.window = sb;
vm.createContext(sb);
vm.runInContext(scripts[0], sb);
vm.runInContext(data, sb);
const P = sb.PMP;

// The same gist rule the pack tools used: drop the question sentence, keep
// the situation verbatim to about 130 characters. Matching and drop-down
// stems are a standing instruction, so their prompts carry the situation.
function gist(q) {
  if (q.type === 'matching' || q.type === 'pulldown') {
    return ('Prompts: ' + (q.prompts || []).map(p => p.text.slice(0, 40).trimEnd() + '…').join(' / ')).replace(/\|/g, '/');
  }
  const sents = String(q.stem).split(/(?<=[.!?])\s+/);
  const keep = sents.filter(x => !x.trimEnd().endsWith('?'));
  let s = (keep.length ? keep : sents).join(' ');
  if (s.length > 130) s = s.slice(0, 130).replace(/\s+\S*$/, '') + '…';
  return s.replace(/\|/g, '/');
}

const rows = [];   // { q, source }
P.QUESTIONS.forEach(q => rows.push({ q, source: 'core' }));
(P.CASES || []).forEach(c => (c.questions || []).forEach(q => rows.push({ q, source: c.id })));
const index = JSON.parse(fs.readFileSync(path.join(root, 'packs', 'index.json'), 'utf8'));
for (const sh of index.shards) {
  const f = path.join(root, 'packs', sh.file);
  if (!fs.existsSync(f)) continue;
  const pack = JSON.parse(fs.readFileSync(f, 'utf8'));
  pack.questions.forEach(q => rows.push({ q, source: sh.file.replace(/\.json$/, '') }));
}

const ids = new Set();
for (const r of rows) {
  if (ids.has(r.q.id)) throw new Error('duplicate id ' + r.q.id);
  ids.add(r.q.id);
}

const byTask = new Map(P.ECO_TASKS.map(t => [t.id, []]));
for (const r of rows) {
  if (!byTask.has(r.q.ecoTask)) throw new Error(r.q.id + ': unknown ecoTask ' + r.q.ecoTask);
  byTask.get(r.q.ecoTask).push(r);
}

const sourceName = s => s === 'core' ? 'core' : s.startsWith('case-') ? s : s;
function taskSection(t) {
  const list = byTask.get(t.id);
  const out = [`### ${t.id} · ${t.label} — ${list.length} items`, '', `*${t.summary}*`, '',
    '|id|source|type|appr.|d|scenario|', '|-|-|-|-|-|-|'];
  for (const { q, source } of list) {
    out.push(`|\`${q.id}\`|${sourceName(source)}|${q.type}|${q.approach}|${q.difficulty}|${gist(q)}|`);
  }
  return out.join('\n');
}

const HEAD = `**What this is for.** No two questions in the app may repeat a scenario. The
app's audit only catches identical stems, so two questions can share a
situation, a lesson and a correct answer in different words and both pass.
This ledger is the check that closes that gap: before writing a question for a
task, read that task's rows and confirm your situation is not already there.
The obvious scenario for every task is long gone; go looking for the
second-order one.

**How the gists are made.** Mechanically, by \`tools/ledger.mjs\`: the question
sentence is dropped and the situation is kept verbatim up to about 130
characters. Matching and drop-down rows list their prompts instead. The full
item is findable by its id. **Source** says where it lives: \`core\` is built
into the app, \`case-NN\` is a case study, anything else is a file in \`packs/\`.

Generated — do not edit by hand. Run \`node tools/ledger.mjs\` after changing
any question.`;

function totals(list) {
  const count = (k) => {
    const m = {};
    list.forEach(({ q }) => { m[q[k]] = (m[q[k]] || 0) + 1; });
    return Object.keys(m).sort().map(x => `|${x}|${m[x]}|`).join('\n');
  };
  return `|Type|Count|\n|-|-:|\n${count('type')}\n\n|Approach|Count|\n|-|-:|\n${count('approach')}`;
}

const domains = P.ECO.DOMAINS;
const label = d => P.ECO.DOMAIN_LABELS[d];
fs.mkdirSync(path.join(root, 'ledger'), { recursive: true });

// Full ledger.
{
  const dom = domains.map(d => {
    const n = rows.filter(r => r.q.domain === d).length;
    return `|${label(d)}|${n}|${(100 * n / rows.length).toFixed(1)}%|`;
  }).join('\n');
  const body = domains.map(d => `## ${label(d)}\n\n` +
    P.ECO_TASKS.filter(t => t.domain === d).map(taskSection).join('\n\n')).join('\n\n');
  fs.writeFileSync(path.join(root, 'ledger.md'), `# Question ledger

Every question the app ships — the core pack and case studies in \`index.html\`
and every file in \`packs/\` — grouped by ECO task. Per-task and per-domain
slices of this file are in \`ledger/\`; upload the slice you need to a model
rather than this whole file.

${HEAD}

---

## Totals

|Slice|Count|Share|
|-|-:|-:|
|**Items**|**${rows.length}**||
${dom}

${totals(rows)}

---

${body}

---

${rows.length} items listed.
`);
}

// Domain and task slices.
for (const d of domains) {
  const tasks = P.ECO_TASKS.filter(t => t.domain === d);
  const n = tasks.reduce((a, t) => a + byTask.get(t.id).length, 0);
  fs.writeFileSync(path.join(root, 'ledger', d + '.md'),
    `# Question ledger — ${label(d)}\n\n${n} questions across the ${tasks.length} ${label(d)} tasks.\n\n${HEAD}\n\n---\n\n` +
    tasks.map(taskSection).join('\n\n') + '\n');
}
for (const t of P.ECO_TASKS) {
  fs.writeFileSync(path.join(root, 'ledger', t.id + '.md'),
    `# Question ledger — ${t.id} · ${t.label}\n\n${HEAD}\n\n---\n\n${taskSection(t)}\n`);
}

console.log(`ledger: ${rows.length} questions; ` +
  domains.map(d => `${d} ${rows.filter(r => r.q.domain === d).length}`).join(', '));
