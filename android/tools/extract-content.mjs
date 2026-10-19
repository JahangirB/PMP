#!/usr/bin/env node
// Builds the Android app's content assets from the web app, so the APK always
// carries exactly what index.html and packs/ hold.
//
//   node android/tools/extract-content.mjs <repo-root> <assets-out-dir>
//
// It evaluates §01 (config) and §05 (content) of index.html in a sandbox --
// both are plain data -- and writes:
//   content/core.json    the built-in question pack (pmp-pack envelope)
//   content/extras.json  exam layout, ECO tasks, scenarios, lessons, flashcards,
//                        glossary, abbreviations, formulas, the reference library,
//                        the shared rules (PMP.RULES) and the cloud sync script
//   packs/*.json         every pack file from packs/, copied as is
// No dependencies beyond Node itself.
import fs from 'node:fs';
import path from 'node:path';
import vm from 'node:vm';

const [root, out] = process.argv.slice(2);
if (!root || !out) {
  console.error('usage: extract-content.mjs <repo-root> <assets-out-dir>');
  process.exit(2);
}
const html = fs.readFileSync(path.join(root, 'index.html'), 'utf8');

// §01 is the first <script> block; §05 runs from its banner to §06's.
const scripts = [...html.matchAll(/<script>([\s\S]*?)<\/script>/g)].map(m => m[1]);
if (scripts.length < 2) throw new Error('index.html: expected at least two <script> blocks');
const config = scripts[0];
const main = scripts[1];
const start = main.indexOf('/* ═══ §05');
const end = main.indexOf('/* ═══ §06');
if (start < 0 || end < 0) throw new Error('index.html: could not find the §05 / §06 section banners');
const data = main.slice(start, end);

const sandbox = {
  console,
  localStorage: { getItem: () => null, setItem() {} },
  document: { documentElement: { setAttribute() {}, style: {} } },
  matchMedia: () => ({ matches: false }),
};
sandbox.window = sandbox;
vm.createContext(sandbox);
vm.runInContext(config, sandbox, { filename: 'index.html#config' });
vm.runInContext(data, sandbox, { filename: 'index.html#data' });
const P = sandbox.PMP;

const need = ['ECO', 'EXAM', 'SRS', 'READINESS', 'PRACTICE', 'ECO_TASKS', 'QUESTIONS', 'SCENARIOS', 'DECKS',
  'FLASHCARDS', 'FORMULAS', 'REFERENCE', 'GLOSSARY', 'ABBREVIATIONS', 'MODULES', 'LESSONS', 'RULES'];
for (const k of need) if (P[k] == null) throw new Error('index.html: PMP.' + k + ' is missing');

// JSON.stringify evaluates getters, so PMP.EXAM's derived break points come through.
const plain = v => JSON.parse(JSON.stringify(v));

const core = {
  type: 'pmp-pack', schemaVersion: 2, ecoVersion: P.ECO.VERSION,
  name: 'Core pack', questions: plain(P.QUESTIONS),
};
const extras = plain({
  eco: P.ECO, exam: P.EXAM, srs: P.SRS, readiness: P.READINESS, practice: P.PRACTICE,
  tasks: P.ECO_TASKS, scenarios: P.SCENARIOS, decks: P.DECKS, flashcards: P.FLASHCARDS,
  formulas: P.FORMULAS, reference: P.REFERENCE, glossary: P.GLOSSARY,
  abbreviations: P.ABBREVIATIONS, modules: P.MODULES, lessons: P.LESSONS, rules: P.RULES,
  cases: P.CASES || [],
  // Sample documents for the Reference library (L2); absent in older builds.
  documents: P.DOCUMENTS || null,
  // The knowledge-area tours and the classroom lesson order (L5).
  areaTours: P.AREA_TOURS || [],
  courseOrder: P.COURSE_ORDER || [],
  // The Google Apps Script the Cloud sync screen shows (docs/progress-format.md §5).
  syncScript: fs.readFileSync(path.join(root, 'sync', 'pmp-sync.gs'), 'utf8'),
  // The Make more questions prompt: the text block of docs/question-generator-prompt.md.
  generatorPrompt: /```text\n([\s\S]*?)```/.exec(fs.readFileSync(path.join(root, 'docs', 'question-generator-prompt.md'), 'utf8'))[1],
});

fs.rmSync(out, { recursive: true, force: true });
fs.mkdirSync(path.join(out, 'content'), { recursive: true });
fs.mkdirSync(path.join(out, 'packs'), { recursive: true });
fs.writeFileSync(path.join(out, 'content', 'core.json'), JSON.stringify(core));
fs.writeFileSync(path.join(out, 'content', 'extras.json'), JSON.stringify(extras));

let packCount = 0, packQuestions = 0;
const packDir = path.join(root, 'packs');
for (const f of fs.readdirSync(packDir).sort()) {
  if (!f.endsWith('.json') || f === 'index.json') continue;
  const text = fs.readFileSync(path.join(packDir, f), 'utf8');
  const pack = JSON.parse(text);  // fail the build on a malformed pack, not the app
  packQuestions += (pack.questions || []).length;
  fs.writeFileSync(path.join(out, 'packs', f), text);
  packCount++;
}

console.log(`content: ${core.questions.length} core questions, ${packCount} packs (${packQuestions} questions), ` +
  `${extras.scenarios.length} scenarios, ${extras.lessons.length} lessons, ${extras.flashcards.length} flashcards, ` +
  `${extras.glossary.length} glossary terms`);
