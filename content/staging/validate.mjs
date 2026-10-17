#!/usr/bin/env node
// Checks the staged content in this folder against the rules index.html
// enforces on PMP.QUESTIONS, before anyone splices it in:
//   §09.6 PMP.validate.pack   structure: ids, types, domains, ECO tasks, options,
//                             prompts, regions + graphic.viewBox, blanks
//   §10.14 PMP.Audit          editorial floors: stem, explanation, rationale on
//                             every option/region/blank, option counts and
//                             distinctness, single/multi correct counts,
//                             numeric tolerance, explanation not an echo
//   §10.07.9 abbreviations    every abbreviation-shaped token is in §05.7.3
// plus staging-only checks: ids unique across staged files and clear of every
// id in PMP.QUESTIONS and packs/, the case shape, and region geometry that the
// hotspot renderer (§10.04 renderHotspot) can actually draw.
//
//   node content/staging/validate.mjs [repo-root]      (no dependencies)
//
// Also writes build/cases.json and build/graphic-questions.json, plain JSON
// copies for the native unit tests and for anyone splicing by hand.
import fs from 'node:fs';
import path from 'node:path';
import vm from 'node:vm';
import { fileURLToPath } from 'node:url';

const here = path.dirname(fileURLToPath(import.meta.url));
const root = path.resolve(process.argv[2] || path.join(here, '..', '..'));

/* ── load §01 and §05 of index.html the way android/tools/extract-content.mjs does ── */
const html = fs.readFileSync(path.join(root, 'index.html'), 'utf8');
const scripts = [...html.matchAll(/<script>([\s\S]*?)<\/script>/g)].map(m => m[1]);
const main = scripts[1];
const data = main.slice(main.indexOf('/* ═══ §05'), main.indexOf('/* ═══ §06'));
const sandbox = {
  console,
  localStorage: { getItem: () => null, setItem() {} },
  document: { documentElement: { setAttribute() {}, style: {} } },
  matchMedia: () => ({ matches: false }),
};
sandbox.window = sandbox;
vm.createContext(sandbox);
vm.runInContext(scripts[0], sandbox, { filename: 'index.html#config' });
vm.runInContext(data, sandbox, { filename: 'index.html#data' });
const P = sandbox.PMP;

/* ── load the staged files into the same sandbox ── */
vm.runInContext(fs.readFileSync(path.join(here, 'cases.js'), 'utf8'), sandbox, { filename: 'cases.js' });
vm.runInContext(fs.readFileSync(path.join(here, 'graphic-questions.js'), 'utf8'), sandbox, { filename: 'graphic-questions.js' });
const plain = v => JSON.parse(JSON.stringify(v));
const CASES = plain(P.CASES);
const GRAPHIC = plain(P.STAGED_GRAPHIC_QUESTIONS);

const errors = [];
const warns = [];
const err = (where, msg) => errors.push(where + ': ' + msg);
const warn = (where, msg) => warns.push(where + ': ' + msg);

/* ── existing ids: core questions, lesson checks and every pack ── */
const existing = new Set(P.QUESTIONS.map(q => q.id));
for (const f of fs.readdirSync(path.join(root, 'packs'))) {
  if (!f.endsWith('.json')) continue;
  const p = JSON.parse(fs.readFileSync(path.join(root, 'packs', f), 'utf8'));
  (p.questions || []).forEach(q => existing.add(q.id));
}
(P.LESSONS || []).forEach(l => (l.checks || []).forEach(c => c.id && existing.add(c.id)));

/* ── the rules ── */
const TYPES = ['single', 'multi', 'matching', 'pulldown', 'hotspot', 'graphic', 'fillblank'];
const ALIAS = { pulldown: 'matching', graphic: 'hotspot' };
const base = t => ALIAS[t] || t;
const RULE = { MIN_STEM: 60, MIN_STEM_RECALL: 30, MIN_EXPLANATION: 80, MIN_RATIONALE: 25, MIN_OPTIONS: 3, MAX_OPTIONS: 10 };
const norm = s => String(s || '').toLowerCase().replace(/[^a-z0-9]+/g, ' ').trim();
const taskById = Object.fromEntries(P.ECO_TASKS.map(t => [t.id, t]));
const stems = new Map(P.QUESTIONS.map((q, i) => [norm(q.stem), 'core ' + q.id]));

// §10.04 wrapLabel, character for character.
function wrapLabel(text, max) {
  const words = String(text).split(' '), lines = [];
  let line = '';
  words.forEach(w => {
    if (!line) line = w;
    else if ((line + ' ' + w).length <= max) line += ' ' + w;
    else { lines.push(line); line = w; }
  });
  if (line) lines.push(line);
  return lines;
}

function parseViewBox(vb) {
  const n = String(vb || '').trim().split(/[\s,]+/).map(Number);
  return n.length === 4 && n.every(Number.isFinite) ? n : null;
}

const seenIds = new Map();
function checkQuestion(q, at) {
  if (!q || typeof q !== 'object') return err(at, 'not an object');
  // §09.6 structure
  if (!q.id) err(at, 'missing id');
  else {
    if (seenIds.has(q.id)) err(at, `duplicate id ${q.id} (also ${seenIds.get(q.id)})`);
    seenIds.set(q.id, at);
    if (existing.has(q.id)) err(at, `id ${q.id} collides with an existing question`);
    if (!/^pmp-[a-z]+\d+-\d{3}$/.test(q.id)) err(at, `id ${q.id} does not follow pmp-<code><n>-<nnn>`);
  }
  if (!TYPES.includes(q.type)) err(at, `unknown type ${q.type}`);
  if (!P.ECO.DOMAINS.includes(q.domain)) err(at, `unknown domain ${q.domain}`);
  const task = taskById[q.ecoTask];
  if (!task) err(at, `unknown ecoTask ${q.ecoTask}`);
  else if (task.domain !== q.domain) err(at, `ecoTask ${q.ecoTask} is a ${task.domain} task, item says ${q.domain}`);
  if (!P.ECO.APPROACH_LABELS[q.approach]) err(at, `approach must be predictive, agile or hybrid; found ${q.approach}`);
  if (!(q.difficulty >= 1 && q.difficulty <= 5)) err(at, `difficulty must be 1-5; found ${q.difficulty}`);
  if (!Array.isArray(q.tags) || !q.tags.length) err(at, 'no tags');
  else if (!q.tags.some(t => t.startsWith('archetype-'))) warn(at, 'no archetype-* tag');
  if (!q.mindset || !String(q.mindset).trim()) err(at, 'no mindset');
  const t = base(q.type);
  const minStem = (t === 'fillblank' || t === 'hotspot') ? RULE.MIN_STEM_RECALL : RULE.MIN_STEM;
  if (String(q.stem || '').trim().length < minStem) err(at, `stem shorter than ${minStem}`);
  let key = norm(q.stem);
  if (t === 'matching') key += ' § ' + (q.prompts || []).map(p => norm(p.text)).join(' | ');
  if (stems.has(key)) err(at, `duplicate stem (also ${stems.get(key)})`);
  stems.set(key, q.id);
  if (String(q.explanation || '').trim().length < RULE.MIN_EXPLANATION) err(at, `explanation shorter than ${RULE.MIN_EXPLANATION}`);

  if (Array.isArray(q.options) && q.options.length) {
    if (q.options.length < RULE.MIN_OPTIONS || q.options.length > RULE.MAX_OPTIONS) err(at, `${q.options.length} options`);
    const texts = new Set(), oids = new Set();
    q.options.forEach((o, j) => {
      const oat = `${at}.options[${j}]`;
      if (!o.id) err(oat, 'missing id');
      else if (oids.has(o.id)) err(oat, `duplicate option id ${o.id}`);
      oids.add(o.id);
      if (!o.text) err(oat, 'missing text');
      else { if (texts.has(norm(o.text))) err(oat, 'two options say the same thing'); texts.add(norm(o.text)); }
      if (String(o.rationale || '').trim().length < RULE.MIN_RATIONALE) err(oat, 'every option needs its own rationale (25+ characters)');
    });
    const corr = q.options.filter(o => o.correct);
    if (!corr.length) err(at, 'no option is marked correct');
    if (t === 'single' && corr.length !== 1) err(at, `single needs exactly one correct, found ${corr.length}`);
    if (t === 'multi') {
      if (corr.length < 2) err(at, `multi needs at least two correct, found ${corr.length}`);
      const words = { 2: 'two', 3: 'three', 4: 'four' };
      if (!new RegExp('select ' + (words[corr.length] || corr.length), 'i').test(q.stem)) warn(at, `multi stem does not say "Select ${words[corr.length]}"`);
    }
    if (t === 'single' && corr[0] && q.explanation) {
      const en = norm(q.explanation), cn = norm(corr[0].text);
      if (cn && en.includes(cn) && en.length < cn.length * 1.6) err(at, 'explanation is a restatement of the correct option');
    }
  } else if (t !== 'fillblank' && t !== 'hotspot') err(at, 'missing options');

  if (t === 'matching') {
    if (!Array.isArray(q.prompts) || !q.prompts.length) err(at, 'matching/pulldown needs prompts');
    (q.prompts || []).forEach((pr, j) => {
      const hits = (q.options || []).filter(o => String(o.match) === String(pr.id));
      if (hits.length !== 1) err(`${at}.prompts[${j}]`, `needs exactly one option matched, found ${hits.length}`);
    });
    (q.options || []).forEach((o, j) => {
      if (o.match != null && !(q.prompts || []).some(p => p.id === o.match)) err(`${at}.options[${j}]`, `matches unknown prompt ${o.match}`);
      if (!!o.correct !== (o.match != null)) err(`${at}.options[${j}]`, 'correct should be true exactly when the option is matched');
    });
  }

  if (t === 'hotspot') {
    const g = q.graphic;
    const vb = g && parseViewBox(g.viewBox);
    if (!g || !g.viewBox) err(at, 'hotspot/graphic needs graphic.viewBox');
    else if (!vb) err(at, `graphic.viewBox "${g.viewBox}" is not four numbers`);
    if (g && !g.title) err(at, 'graphic needs a title (it is the diagram\'s accessible name)');
    if (g && !g.caption) warn(at, 'graphic has no caption');
    if (!Array.isArray(q.regions) || !q.regions.length) err(at, 'hotspot/graphic needs regions');
    const regions = q.regions || [];
    const corr = regions.filter(r => r.correct);
    if (!corr.length) err(at, 'no region is marked correct');
    if (corr.length > 1 && q.multi !== true) err(at, 'more than one correct region needs multi: true (the web renderer reads q.multi)');
    if (corr.length === 1 && q.multi === true) err(at, 'multi: true with a single correct region');
    const rids = new Set(), ns = new Set();
    regions.forEach((r, j) => {
      const rat = `${at}.regions[${j}]`;
      if (!r.id) err(rat, 'missing id'); else if (rids.has(r.id)) err(rat, `duplicate region id ${r.id}`);
      rids.add(r.id);
      if (!Number.isInteger(r.n)) err(rat, 'n must be an integer');
      else if (ns.has(r.n)) err(rat, `duplicate n ${r.n}`);
      ns.add(r.n);
      if (!r.label) err(rat, 'missing label');
      for (const k of ['x', 'y', 'w', 'h']) if (typeof r[k] !== 'number') err(rat, `${k} must be a number`);
      if (String(r.rationale || '').trim().length < RULE.MIN_RATIONALE) err(rat, 'every region needs its own rationale (25+ characters)');
      if (vb && typeof r.x === 'number') {
        if (r.x < vb[0] || r.y < vb[1] || r.x + r.w > vb[0] + vb[2] || r.y + r.h > vb[1] + vb[3]) err(rat, 'region lies outside the viewBox');
      }
      // What renderHotspot can draw: three lines of at most 14 characters, 15px apart, 12px type.
      const lines = wrapLabel(r.label || '', 14);
      if (lines.length > 3) err(rat, `label wraps to ${lines.length} lines; the renderer keeps 3: ${JSON.stringify(lines)}`);
      const longest = Math.max(...lines.map(l => l.length));
      if (longest > 14) err(rat, `label line "${lines.find(l => l.length === longest)}" is longer than 14 characters`);
      if (typeof r.w === 'number' && r.w < longest * 6.6 + 8) err(rat, `box ${r.w} wide is too narrow for "${lines.find(l => l.length === longest)}"`);
      if (typeof r.h === 'number' && r.h < Math.min(3, lines.length) * 15 + 18) err(rat, `box ${r.h} high is too short for ${lines.length} lines`);
    });
    for (let a = 0; a < regions.length; a++) for (let b = a + 1; b < regions.length; b++) {
      const A = regions[a], B = regions[b];
      if (A.x < B.x + B.w && B.x < A.x + A.w && A.y < B.y + B.h && B.y < A.y + A.h) err(at, `regions ${A.id} and ${B.id} overlap`);
    }
  }

  if (t === 'fillblank') {
    if (!Array.isArray(q.blanks) || !q.blanks.length) err(at, 'fillblank needs blanks');
    (q.blanks || []).forEach((b, j) => {
      const bat = `${at}.blanks[${j}]`;
      if (b.id == null) err(bat, 'missing id');
      if (!b.label) err(bat, 'missing label');
      if (b.numeric) {
        if (typeof b.expect !== 'number') err(bat, 'numeric blank needs a numeric expect');
        if (typeof b.tol !== 'number') err(bat, 'numeric blank needs a tolerance, even if zero');
        else if (b.tol < 0) err(bat, 'negative tolerance');
      } else if (!Array.isArray(b.accept) || !b.accept.length) err(bat, 'text blank needs accept[]');
      if (String(b.rationale || '').trim().length < RULE.MIN_RATIONALE) err(bat, 'every blank needs its own rationale');
    });
  }
}

/* ── abbreviation scan, as §10.07.9 abTokens/abWalk ── */
const AB_RUN = /[A-Z][A-Za-z0-9&-]*/g, AB_END = /[A-Z0-9]/, AB_WORD = /\w/, AB_GLUE = /[\w-]/;
const AB_SKIP = { id: 1, ecoTask: 1, tags: 1, prereqs: 1, terms: 1, domain: 1, approach: 1, type: 1, deck: 1, module: 1,
  option: 1, rank: 1, match: 1, formulaGroup: 1, viewBox: 1, practice: 1 };
function abTokens(s, into) {
  let m;
  AB_RUN.lastIndex = 0;
  while ((m = AB_RUN.exec(s))) {
    const run = m[0], start = m.index;
    if (start && AB_GLUE.test(s.charAt(start - 1))) continue;
    let k;
    for (k = run.length; k >= 2; k--) {
      if (!AB_END.test(run.charAt(k - 1))) continue;
      const nx = s.charAt(start + k);
      if (!AB_WORD.test(nx)) break;
      if (nx === 's' && k === run.length - 1 && !AB_WORD.test(s.charAt(start + k + 1))) break;
    }
    if (k >= 2 && k <= 8) into[run.slice(0, k)] = 1;
  }
}
function abWalk(o, into) {
  if (typeof o === 'string') return abTokens(o, into);
  if (!o || typeof o !== 'object') return;
  if (Array.isArray(o)) return o.forEach(x => abWalk(x, into));
  for (const k of Object.keys(o)) if (!AB_SKIP[k]) abWalk(o[k], into);
}

/* ── the cases ── */
const mixAll = { people: 0, process: 0, business: 0 };
const typeAll = {}, apprAll = {};
const caseIds = new Set();
CASES.forEach((c, i) => {
  const at = `CASES[${i}] ${c.id}`;
  if (!/^case-\d{2}$/.test(c.id || '')) err(at, 'id must be case-NN');
  if (caseIds.has(c.id)) err(at, 'duplicate case id');
  caseIds.add(c.id);
  if (!c.title) err(at, 'missing title');
  if (!c.setting) err(at, 'missing setting');
  if (!P.ECO.APPROACH_LABELS[c.approach]) err(at, `unknown approach ${c.approach}`);
  if (!Array.isArray(c.passage) || !c.passage.length || c.passage.some(p => typeof p !== 'string' || !p.trim())) err(at, 'passage must be a non-empty array of paragraphs');
  const words = (c.passage || []).join(' ').split(/\s+/).filter(Boolean).length;
  if (words < 300 || words > 600) err(at, `passage is ${words} words; 300–600 expected`);
  const labels = new Set();
  (c.exhibits || []).forEach((x, j) => {
    const xat = `${at}.exhibits[${j}]`;
    if (!x.id || !x.label || !x.title) err(xat, 'exhibit needs id, label and title');
    labels.add(x.label);
    if (!Array.isArray(x.cols) || !x.cols.length) err(xat, 'exhibit needs cols');
    (x.rows || []).forEach((r, k) => { if (!Array.isArray(r) || r.length !== (x.cols || []).length) err(`${xat}.rows[${k}]`, 'row length differs from cols'); });
  });
  // Every "Exhibit n" mentioned must exist in this case.
  const text = JSON.stringify(c);
  for (const m of text.matchAll(/Exhibit \d+/g)) if (!labels.has(m[0])) err(at, `mentions ${m[0]}, which this case does not carry`);
  const qs = c.questions || [];
  if (qs.length < 8 || qs.length > 12) err(at, `${qs.length} questions; 8–12 expected`);
  const mix = { people: 0, process: 0, business: 0 };
  qs.forEach((q, j) => {
    checkQuestion(q, `${at}.questions[${j}] ${q.id}`);
    mix[q.domain] = (mix[q.domain] || 0) + 1;
    mixAll[q.domain]++;
    typeAll[q.type] = (typeAll[q.type] || 0) + 1;
    apprAll[q.approach] = (apprAll[q.approach] || 0) + 1;
    if (base(q.type) === 'hotspot') warn(at, `${q.id}: a hotspot inside a case is allowed but untested on the phones' case panel`);
  });
  if (!c.mix || ['people', 'process', 'business'].some(d => (c.mix[d] || 0) !== mix[d])) err(at, `mix ${JSON.stringify(c.mix)} does not match the questions ${JSON.stringify(mix)}`);
});

/* ── the graphic items ── */
const gMix = { people: 0, process: 0, business: 0 }, gAppr = {};
GRAPHIC.forEach((q, i) => {
  const at = `GRAPHIC[${i}] ${q.id}`;
  if (q.type !== 'graphic') err(at, `type is ${q.type}, expected graphic`);
  checkQuestion(q, at);
  gMix[q.domain]++;
  gAppr[q.approach] = (gAppr[q.approach] || 0) + 1;
});

/* ── abbreviations ── */
const found = {};
abWalk([CASES, GRAPHIC], found);
const known = new Set(P.ABBREVIATIONS.map(a => a.abbr));
const missing = Object.keys(found).filter(t => !known.has(t)).sort();
if (missing.length) err('abbreviations', 'not in §05.7.3 PMP.ABBREVIATIONS: ' + missing.join(', '));

/* ── report ── */
const n = CASES.reduce((s, c) => s + (c.questions || []).length, 0);
const pct = (a, b) => (100 * a / b).toFixed(1);
console.log(`cases: ${CASES.length} cases, ${n} items`);
console.log(`  domain  people ${mixAll.people} (${pct(mixAll.people, n)}%)  process ${mixAll.process} (${pct(mixAll.process, n)}%)  business ${mixAll.business} (${pct(mixAll.business, n)}%)`);
console.log(`  type    ${Object.entries(typeAll).map(([k, v]) => k + ' ' + v).join(', ')}`);
console.log(`  approach ${Object.entries(apprAll).map(([k, v]) => k + ' ' + v).join(', ')}`);
CASES.forEach(c => console.log(`  ${c.id}  ${c.approach.padEnd(10)} ${String((c.questions || []).length).padStart(2)} items  ${(c.passage || []).join(' ').split(/\s+/).length} words  ${c.title}`));
console.log(`graphic: ${GRAPHIC.length} items  people ${gMix.people} / process ${gMix.process} / business ${gMix.business}; ${Object.entries(gAppr).map(([k, v]) => k + ' ' + v).join(', ')}`);
console.log(`checked against ${existing.size} existing ids and ${P.ABBREVIATIONS.length} abbreviations`);
warns.forEach(w => console.log('warning  ' + w));
if (errors.length) {
  errors.forEach(e => console.log('ERROR    ' + e));
  console.log(`${errors.length} error(s)`);
  process.exit(1);
}
fs.mkdirSync(path.join(here, 'build'), { recursive: true });
fs.writeFileSync(path.join(here, 'build', 'cases.json'), JSON.stringify(CASES, null, 1));
fs.writeFileSync(path.join(here, 'build', 'graphic-questions.json'), JSON.stringify(GRAPHIC, null, 1));
console.log('clean — wrote build/cases.json and build/graphic-questions.json');
