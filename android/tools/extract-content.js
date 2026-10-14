// Regenerates android/content/*.json from ../index.html (the built-in 348
// questions, ECO tasks, flashcards, glossary and formulas).
// Usage: node android/tools/extract-content.js   (needs Playwright + Chromium)
const path = require('path'), fs = require('fs');
let pw; try { pw = require('playwright'); } catch (e) { pw = require('/opt/node22/lib/node_modules/playwright'); }
(async () => {
  const root = path.resolve(__dirname, '..', '..');
  const b = await pw.chromium.launch(); const p = await b.newPage();
  await p.goto('file://' + path.join(root, 'index.html'));
  await p.waitForFunction(() => window.PMP && PMP.QUESTIONS && PMP.FLASHCARDS);
  const d = await p.evaluate(() => ({
    core: { type: 'pmp-pack', schemaVersion: 2, ecoVersion: PMP.ECO.VERSION, name: 'Core pack', questions: PMP.QUESTIONS },
    extras: { tasks: PMP.ECO_TASKS, decks: PMP.DECKS, flashcards: PMP.FLASHCARDS, glossary: PMP.GLOSSARY, formulas: PMP.FORMULAS }
  }));
  const out = path.join(root, 'android', 'content');
  fs.writeFileSync(path.join(out, 'core.json'), JSON.stringify(d.core));
  fs.writeFileSync(path.join(out, 'extras.json'), JSON.stringify(d.extras));
  console.log('core questions:', d.core.questions.length, 'flashcards:', d.extras.flashcards.length);
  await b.close();
})();
