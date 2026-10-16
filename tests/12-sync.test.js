"use strict";
/* The shared progress format (docs/progress-format.md): PMP.RULES, version 3
   backups, the three-way merge against tests/fixtures/sync, and cloud sync
   against a fake Apps Script endpoint routed in place of script.google.com.
   Also the S3 fixes that came with it: ICS escaping, the three-way theme
   toggle and the formula drill log. */
var assert = require("assert");
var fs = require("fs");
var path = require("path");

var ROOT = path.join(__dirname, "..");
var FIX = path.join(__dirname, "fixtures", "sync");
var EXEC = "https://script.google.com/macros/s/TEST-DEPLOYMENT/exec";

async function clickSheet(page, label) {
  await page.evaluate(function (l) {
    var b = Array.prototype.filter.call(document.querySelectorAll("#sheet-foot button"), function (x) { return x.textContent.trim() === l; })[0];
    if (!b) throw new Error("No sheet button " + l + " in: " + document.getElementById("sheet-foot").textContent);
    b.click();
  }, label);
  await page.waitForTimeout(60);
}

/* ── Normalisation from tests/fixtures/sync/README.md ─────────────────── */
var LOG_ID = {
  attempts: function (r) { return r.q + "|" + r.at; }, scenarios: function (r) { return r.id + "|" + r.at; },
  mocks: function (r) { return r.at + "|" + r.title; }, sessions: function (r) { return r.k + "|" + r.at; },
  diagnostics: function (r) { return String(r.at); }, chat: function (r) { return r.at + "|" + r.r; },
  sittings: function (r) { return r.k + "|" + r.at; }, checkins: function (r) { return r.kind + "|" + r.at; },
  weekly: function (r) { return String(r.w); }, drills: function (r) { return r.at + "|" + r.g; }
};
function normalise(doc) {
  var d = JSON.parse(JSON.stringify(doc));
  delete d.savedAt; delete d.device;
  Object.keys(LOG_ID).forEach(function (k) {
    var at = k === "weekly" ? function (r) { return r.w; } : function (r) { return r.at; };
    d[k] = (d[k] || []).slice().sort(function (a, b) {
      return (at(a) - at(b)) || (LOG_ID[k](a) < LOG_ID[k](b) ? -1 : LOG_ID[k](a) > LOG_ID[k](b) ? 1 : 0);
    });
  });
  ["flags", "lessons"].forEach(function (k) { d[k] = (d[k] || []).slice().sort(); });
  d.mycards = (d.mycards || []).slice().sort(function (a, b) { return a.id < b.id ? -1 : 1; });
  ["notes", "mistakes", "cards", "snoozed", "web", "native"].forEach(function (k) { d[k] = d[k] || {}; });
  if (d.notepad === undefined) d.notepad = "";
  return d;
}

/* ── The fake Apps Script. The same protocol as sync/pmp-sync.gs, in memory,
   answering POST /exec directly. (Google answers with a 302 that fetch
   follows; Playwright does not route the follow-up request of a fulfilled
   redirect, so the hop itself cannot be faked here.) ─────────────────── */
function fakeScript(code) {
  var st = { code: code, rev: 0, savedAt: 0, doc: null, ops: [], conflictNext: null };
  function answer(body) {
    var req;
    try { req = JSON.parse(body); } catch (e) { return { ok: false, error: "bad-request" }; }
    st.ops.push(req.op);
    if (!req || req.code !== st.code) return { ok: false, error: "bad-code" };
    if (req.op === "ping") return { ok: true, rev: st.rev, savedAt: st.savedAt };
    if (req.op === "get") return { ok: true, rev: st.rev, doc: st.doc };
    if (req.op === "put") {
      if (st.conflictNext) {               /* another device saves first */
        st.doc = st.conflictNext(st.doc);
        st.rev++;
        st.conflictNext = null;
      }
      if (Number(req.baseRev) !== st.rev) return { ok: false, conflict: true, rev: st.rev, doc: st.doc };
      if (!req.doc || req.doc.type !== "pmp-progress") return { ok: false, error: "bad-doc" };
      st.doc = req.doc;
      st.rev++;
      st.savedAt = Date.now();
      return { ok: true, rev: st.rev };
    }
    return { ok: false, error: "bad-op" };
  }
  var CORS = { "access-control-allow-origin": "*", "access-control-allow-headers": "*", "access-control-allow-methods": "GET, POST" };
  st.install = async function (ctx) {
    await ctx.route(EXEC, async function (route) {
      var req = route.request();
      if (req.method() === "OPTIONS") return route.fulfill({ status: 204, headers: CORS });
      var out = answer(req.postData() || "");
      await route.fulfill({ status: 200, headers: Object.assign({ "content-type": "application/json" }, CORS), body: JSON.stringify(out) });
    });
  };
  return st;
}

async function waitSynced(page) {
  await page.waitForFunction(function () { return PMP.Sync.status.state === "ok" || PMP.Sync.status.state === "error"; }, null, { timeout: 10000 });
}

/* Sets up sync through the page's own guide: the generated code, the script
   with it filled in, the URL field and Test connection. */
async function setUpNew(page) {
  await page.evaluate(function () { location.hash = "#/sync"; });
  await page.waitForSelector("#sync-code");
  var code = await page.textContent("#sync-code");
  await page.fill("#sync-url", EXEC);
  await page.click("#sync-test");
  await page.waitForSelector("#sync-status");
  await waitSynced(page);
  return code;
}

async function setUpExisting(page, code) {
  await page.evaluate(function () { location.hash = "#/sync"; });
  await page.waitForSelector(".seg button");
  await page.click('.seg button:has-text("I already have a script")');
  await page.waitForSelector("#sync-code-in");
  await page.fill("#sync-url", EXEC);
  await page.fill("#sync-code-in", code);
  await page.click("#sync-test");
}

module.exports = [
  {
    name: "PMP.RULES matches the contract and the web reads it",
    fn: async function (h, lib) {
      var page = await h.open("misread_heavy");
      var r = await page.evaluate(function () {
        return {
          rules: JSON.parse(JSON.stringify(PMP.RULES)), frozen: Object.isFrozen(PMP.RULES) && Object.isFrozen(PMP.RULES.causes[0]),
          bands: PMP.Exam.BANDS.map(function (b) { return b.min; }),
          credit: PMP.Scenarios.CREDIT, causes: PMP.Journal.CAUSES.map(function (c) { return c.id; }),
          reentry: PMP.Coach.REENTRY_GAP_DAYS,
          journal: PMP.Store.state.journal.map(function (j) { return j.cause; })
        };
      });
      assert.deepStrictEqual(r.rules.mockBands, { above: 0.8, target: 0.65, below: 0.5 });
      assert.deepStrictEqual(r.rules.scenarioCredit, { best: 1, acceptable: 0.5, premature: 0.25, wrong: 0 });
      assert.deepStrictEqual(r.rules.mastery, { minAnswers: 10, window: 15, accuracy: 0.75, fadeDays: 14 });
      assert.strictEqual(r.rules.reentryDays, 5);
      assert.deepStrictEqual(r.rules.confidence.map(function (c) { return c.v; }), [3, 2, 1]);
      assert.ok(r.frozen, "frozen all the way down");
      var ids = ["KNOWLEDGE_GAP", "MISREAD_STEM", "WRONG_MINDSET", "TWO_PLAUSIBLE", "CALCULATION", "RUSHED"];
      assert.deepStrictEqual(r.rules.causes.map(function (c) { return c.id; }), ids);
      r.rules.causes.forEach(function (c) { assert.ok(c.short && c.label && c.hint, c.id); });
      assert.deepStrictEqual(r.bands, [0.8, 0.65, 0.5, 0]);
      assert.strictEqual(r.credit.acceptable, 0.5);
      assert.deepStrictEqual(r.causes, ids);
      assert.strictEqual(r.reentry, 5);
      /* The fixture stores the old ids; they are read as the shared ones. */
      assert.ok(r.journal.length === 12 && r.journal.every(function (c) { return ids.indexOf(c) >= 0; }), r.journal.join());
      await lib.go(page, "#/journal");
      var buttons = await page.$$eval(".jr-causes", function (n) { return n[0] ? n[0].children.length : 0; });
      assert.strictEqual(buttons, 6);
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "old confidence and scenario credit are upgraded on load",
    fn: async function (h, lib) {
      var page = await h.open("day3");
      var r = await page.evaluate(function () {
        var N = PMP.Store.normalise;
        return {
          a: N("attempts", [{ confidence: "sure" }, { confidence: "unsure" }, { confidence: null }, { confidence: 3 }]).map(function (a) { return a.confidence; }),
          s: N("scenarioRuns", [{ rank: "acceptable", credit: 0.6 }])[0].credit
        };
      });
      assert.deepStrictEqual(r.a, [3, 2, null, 3]);
      assert.strictEqual(r.s, 0.5);
      await page.ctx.close();
    }
  },
  {
    name: "merge(base, local, remote) equals tests/fixtures/sync/merged.json",
    fn: async function (h, lib) {
      var load = function (n) { return JSON.parse(fs.readFileSync(path.join(FIX, n + ".json"), "utf8")); };
      var page = await h.open("fresh");
      var got = await page.evaluate(function (f) { return PMP.Sync.merge(f.base, f.local, f.remote); },
        { base: load("base"), local: load("local"), remote: load("remote") });
      assert.deepStrictEqual(normalise(got), normalise(load("merged")));
      /* And merging is stable: the same inputs give byte-identical output. */
      var twice = await page.evaluate(function (f) {
        return JSON.stringify(PMP.Sync.merge(f.base, f.local, f.remote, { now: 1, device: { app: "web", id: "x" } })) ===
               JSON.stringify(PMP.Sync.merge(f.base, f.local, f.remote, { now: 1, device: { app: "web", id: "x" } }));
      }, { base: load("base"), local: load("local"), remote: load("remote") });
      assert.ok(twice);
      await page.ctx.close();
    }
  },
  {
    name: "the embedded sync script is sync/pmp-sync.gs, byte for byte",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      var embedded = await page.evaluate(function () { return PMP.Sync.scriptText(); });
      assert.strictEqual(embedded, fs.readFileSync(path.join(ROOT, "sync", "pmp-sync.gs"), "utf8"));
      var filled = await page.evaluate(function () { return PMP.Sync.scriptText("ABCDEFGHIJKLMNOPQRSTUVWXYZ012345"); });
      assert.ok(filled.indexOf("const SYNC_CODE = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ012345';") > 0);
      assert.ok(filled.indexOf("__SYNC_CODE__") < 0);
      await page.ctx.close();
    }
  },
  {
    name: "export is pmp-progress v3, and a web backup round-trips losslessly",
    fn: async function (h, lib) {
      var src = await h.open("misread_heavy");
      var out = await src.evaluate(function () {
        var S = PMP.Store;
        S.commit(["flags", "notes", "lessons", "drills", "notepad", "settings"], function (s) {
          s.flags = { "q-x": true };
          s.notes = Object.assign({}, s.notes, { "q-y": "a note" });
          s.lessons = { P4: { opened: ["s1"], checks: { c1: 1 }, completedAt: "2026-09-01T10:00:00.000Z", practice: { at: "2026-09-01T10:00:00.000Z", credit: 0.8, n: 10 } } };
          s.drills = [{ at: 1760000000000, g: "npv", ok: true }];
          s.notepad = Object.assign({}, s.notepad, { html: "<b>EV</b> is <i>earned</i><ul><li>CPI</li></ul>", text: "EV is earned\nCPI" });
          s.settings = Object.assign({}, s.settings, { examDate: "2026-12-01" });
        });
        var a0 = S.state.attempts[0];
        /* A lesson practice-set answer: real practice, so it travels as src
           "practice" with the lesson kept in wx. */
        S.commit(["attempts"], function (s) {
          s.attempts = s.attempts.concat([Object.assign({}, a0, { id: "att-lesson-1", at: "2026-09-03T09:00:00.000Z",
            mode: "lesson", lessonId: "P4", correct: true, credit: 1 })]);
        });
        S.commit(["srs", "scenarioRuns"], function (s) {
          s.srs = { "fc-1": { ease: 2.36, interval: 6, reps: 2, lapses: 1, due: 1760000000000, suspended: false, leech: false, first: 1759000000000, last: 1759500000000, reviews: 4 } };
          s.scenarioRuns = [{ id: "scnrun-1", sid: PMP.SCENARIOS[0].id, domain: "people", ecoTask: "people-1", approach: "agile", difficulty: 3,
                             chosen: "b", rank: "acceptable", credit: 0.5, ms: 41000, at: "2026-09-02T09:00:00.000Z", order: [1, 0, 3, 2] }];
        });
        var doc = PMP.Sync.toDoc(S.state);
        var pick = function (st) {
          var o = {};
          ["attempts", "journal", "srs", "flags", "notes", "scenarioRuns", "lessons", "drills", "coach", "planner", "exams", "issues", "queue"].forEach(function (k) { o[k] = st[k]; });
          o.html = st.notepad.html;
          o.examDate = st.settings.examDate;
          o.dailyTarget = st.settings.dailyTarget;
          o.attempts = o.attempts.slice().sort(function (x, y) { return x.at < y.at ? -1 : x.at > y.at ? 1 : x.id < y.id ? -1 : 1; });
          return JSON.parse(JSON.stringify(o));
        };
        return { doc: doc, before: pick(S.state), a0: a0 };
      });
      await src.ctx.close();
      var doc = out.doc;
      assert.strictEqual(doc.type, "pmp-progress");
      assert.strictEqual(doc.version, 3);
      assert.strictEqual(doc.device.app, "web");
      assert.strictEqual(typeof doc.savedAt, "number");
      assert.ok(doc.attempts.every(function (r) { return typeof r.at === "number" && r.q && r.src; }));
      assert.ok(Object.keys(doc.mistakes).length > 0 && Object.keys(doc.mistakes).every(function (q) { return /^[A-Z_]+$/.test(doc.mistakes[q].c); }));
      assert.deepStrictEqual(doc.cards["fc-1"], [2.36, 6, 2, 1, 1760000000000, 1759500000000, 1759000000000, 0]);
      assert.deepStrictEqual(doc.flags, ["q-x"]);
      assert.deepStrictEqual(doc.lessons, ["P4"]);
      var lr = doc.attempts.filter(function (r) { return r.id === "att-lesson-1"; })[0];
      assert.strictEqual(lr.src, "practice");
      assert.strictEqual(lr.wx.mode, "lesson");
      assert.strictEqual(lr.wx.lessonId, "P4");
      assert.deepStrictEqual(doc.drills, [{ at: 1760000000000, g: "npv", ok: true }]);
      assert.strictEqual(doc.scenarios[0].r, "acceptable");
      assert.strictEqual(doc.notepad, "**EV** is *earned*\n- CPI");
      assert.strictEqual(doc.web.notepadHtml, "<b>EV</b> is <i>earned</i><ul><li>CPI</li></ul>");
      assert.ok(doc.plan && doc.plan.examDate > 0);
      assert.ok(!("theme" in doc.web.settings), "theme is device-only");
      ["sessions", "sittings", "checkins", "weekly", "mycards", "chat", "diagnostics"].forEach(function (k) { assert.ok(Array.isArray(doc[k]), k); });

      var dst = await h.open("fresh");
      var back = await dst.evaluate(function (d) {
        PMP.Data.importProgress(JSON.stringify(d));
        return null;
      }, doc);
      await clickSheet(dst, "Replace everything");
      await clickSheet(dst, "Replace it all");
      var after = await dst.evaluate(function (d) {
        var st = PMP.Store.state, o = {};
        ["attempts", "journal", "srs", "flags", "notes", "scenarioRuns", "lessons", "drills", "coach", "planner", "exams", "issues", "queue"].forEach(function (k) { o[k] = st[k]; });
        o.html = st.notepad.html;
        o.examDate = st.settings.examDate;
        o.dailyTarget = st.settings.dailyTarget;
        /* Order is not part of the record: the store sorts by time on read. */
        o.attempts = o.attempts.slice().sort(function (x, y) { return x.at < y.at ? -1 : x.at > y.at ? 1 : x.id < y.id ? -1 : 1; });
        return { st: JSON.parse(JSON.stringify(o)), same: PMP.Sync.sameDoc(PMP.Sync.toDoc(st), d) };
      }, doc);
      assert.deepStrictEqual(after.st, out.before);
      assert.ok(after.same, "the second device writes the same document back");
      await lib.clean(dst);
      await dst.ctx.close();
    }
  },
  {
    name: "an Android v2 backup imports, shows in the web's views, and goes back out unchanged",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      var r = await page.evaluate(function () {
        var q = PMP.QUESTIONS[0], q2 = PMP.QUESTIONS[1], now = Date.now(), DAY = 86400000;
        var android = {
          type: "pmp-android-progress", version: 2,
          attempts: [
            { q: q.id, t: q.ecoTask, d: q.domain, ap: q.approach, ty: "single", cr: 0, ok: false, ms: 50000, at: now - DAY, src: "practice", ft: 4000, cf: 3 },
            { q: q2.id, t: q2.ecoTask, d: q2.domain, ap: q2.approach, ty: "single", cr: 1, ok: true, ms: 70000, at: now - DAY + 1000, src: "exam", tm: true },
            { q: q2.id, t: q2.ecoTask, d: q2.domain, ap: q2.approach, ty: "single", cr: 1, ok: true, ms: 0, at: now - DAY + 2000, src: "lesson" }
          ],
          flags: [q.id], notes: { [q.id]: "from the phone" },
          mistakes: { [q.id]: { c: "CALCULATION", n: "inverted CPI", at: now - DAY + 5000 } },
          cards: { [PMP.FLASHCARDS[0].id]: [2.5, 1, 1, 0, now + DAY, now - DAY, now - DAY, 0] },
          scenarios: [{ id: PMP.SCENARIOS[0].id, p: "a", r: "best", at: now - DAY + 9000 }],
          mocks: [{ at: now - 2 * DAY, title: "Full exam", n: 170, right: 120, usedMs: 9000000, domains: { people: [40, 56] } }],
          lessons: ["F1"],
          plan: { examDate: new Date(2027, 0, 15, 9, 0, 0).getTime(), hours: 12, days: [2, 3, 4, 5, 6], onboarded: true, rest: true },
          sessions: [{ k: "s1", kind: "BUILD", t: q.ecoTask, d: q.domain, s: 0.8, m: 30, at: now - DAY }],
          notepad: "**EVM** notes\n- CPI = EV / AC",
          diagnostics: [], chat: [{ r: "user", t: "hi", at: now - DAY }], snoozed: { m1: now + DAY },
          sittings: [{ k: "x", kind: "practice", at: now - DAY, p: 10, a: 10, f: true }],
          checkins: [], weekly: [{ w: now - 7 * DAY, u: now, r: 40 }],
          mycards: [{ id: "my-1", t: "", f: "front", b: "back", n: "" }],
          drills: [{ at: now - DAY + 7000, g: "cpi", ok: false }]
        };
        PMP.Data.importProgress(JSON.stringify(android));
        window.__android = android;
        return null;
      });
      await clickSheet(page, "Replace everything");
      await clickSheet(page, "Replace it all");
      var got = await page.evaluate(function () {
        var st = PMP.Store.state, a = window.__android;
        var doc = PMP.Sync.toDoc(st);
        return {
          attempts: PMP.Metrics.attempts(st).length,
          accuracyN: PMP.Metrics.accuracy(st).n,
          modes: st.attempts.map(function (x) { return x.mode; }),
          mocks: PMP.Metrics.mocks(st).map(function (m) { return [m.of, m.right, !!m.foreign]; }),
          journal: st.journal.map(function (j) { return [j.qid === a.attempts[0].q, j.cause, j.note, !!j.attemptId]; }),
          srs: Object.keys(st.srs).length,
          flags: Object.keys(st.flags),
          exam: st.settings.examDate,
          lesson: !!(st.lessons.F1 && st.lessons.F1.completedAt),
          html: st.notepad.html,
          drillDay: PMP.Metrics.dayIndex(st)[PMP.time.startOfDay(a.drills[0].at)].drills,
          out: doc, inp: a,
          same: ["attempts", "sessions", "mocks", "plan", "notepad", "chat", "snoozed", "sittings", "weekly", "mycards",
                 "cards", "flags", "notes", "mistakes", "scenarios", "lessons", "drills"].filter(function (k) {
            return PMP.Sync.canon(doc[k]) !== PMP.Sync.canon(a[k]);
          })
        };
      });
      assert.strictEqual(got.attempts, 3);
      assert.strictEqual(got.accuracyN, 2, "the phone's lesson quick check stays out of accuracy");
      assert.deepStrictEqual(got.modes, ["practice", "exam", "lesson"]);
      assert.deepStrictEqual(got.mocks, [[170, 120, true]]);
      assert.deepStrictEqual(got.journal, [[true, "CALCULATION", "inverted CPI", true]]);
      assert.strictEqual(got.srs, 1);
      assert.strictEqual(got.exam, "2027-01-15");
      assert.ok(got.lesson);
      assert.ok(/<b>EVM<\/b>/.test(got.html) && /<li>CPI = EV \/ AC<\/li>/.test(got.html), got.html);
      assert.strictEqual(got.drillDay, 1);
      assert.deepStrictEqual(got.same, [], "slices changed on the way back out: " + got.same.join(", "));
      await lib.go(page, "#/journal");
      await lib.go(page, "#/analytics");
      await lib.go(page, "#/dashboard");
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "notes pane html converts to the shared light markup and back",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      var r = await page.evaluate(function () {
        var S = PMP.Sync;
        var html = "Top<div><b>bold</b> <i>it</i> <u>un</u> <s>st</s> <mark class=\"np-h-green\">hi</mark> <span class=\"np-c-red\">red</span> <span class=\"np-o\">over</span></div><div><br></div><ol><li>one</li><li>two</li></ol>";
        var m = S.htmlToMarkup(html);
        return { m: m, back: S.htmlToMarkup(S.markupToHtml(m)) };
      });
      assert.strictEqual(r.m, "Top\n**bold** *it* __un__ ~~st~~ ==hi== red over\n\n1. one\n2. two");
      assert.strictEqual(r.back, r.m);
      await page.ctx.close();
    }
  },
  {
    name: "cloud sync: guide, ping, first sync uploads, a second device merges",
    fn: async function (h, lib) {
      var fake = null;
      var a = await h.open("misread_heavy");
      var off = await a.evaluate(function () { return PMP.Sync.config(); });
      assert.strictEqual(off, null, "off until set up");
      await lib.go(a, "#/sync");
      var guide = await a.evaluate(function () {
        return {
          code: document.getElementById("sync-code").textContent,
          script: document.getElementById("sync-script").value,
          link: !!document.querySelector('a[href="https://script.google.com"]'),
          steps: document.querySelectorAll(".sync-steps > li").length,
          text: document.getElementById("view").textContent
        };
      });
      assert.ok(/^[A-Za-z0-9]{32}$/.test(guide.code), guide.code);
      assert.ok(guide.script.indexOf("const SYNC_CODE = '" + guide.code + "';") > 0);
      assert.ok(guide.link);
      assert.strictEqual(guide.steps, 6);
      ["Execute as: Me", "Who has access: Anyone", "Test connection", "I already have a script", "Advanced"].forEach(function (t) {
        assert.ok(guide.text.indexOf(t) >= 0, t);
      });
      fake = fakeScript(guide.code);
      await fake.install(a.ctx);
      var code = await setUpNew(a);
      assert.strictEqual(code, guide.code);
      assert.deepStrictEqual(fake.ops.slice(0, 3), ["ping", "get", "put"]);
      assert.ok(fake.doc && fake.doc.type === "pmp-progress" && fake.doc.version === 3);
      var nA = await a.evaluate(function () { return PMP.Store.state.attempts.length; });
      assert.strictEqual(fake.doc.attempts.length, nA, "first sync uploads every answer");
      var status = await a.textContent("#sync-status");
      assert.ok(/Last synced/.test(status), status);
      /* Never in a backup. */
      var leak = await a.evaluate(function (c) { return JSON.stringify(PMP.Sync.toDoc(PMP.Store.state)).indexOf(c) >= 0 || JSON.stringify(PMP.Store.snapshot()).indexOf(c) >= 0; }, code);
      assert.ok(!leak, "the code is never in progress");

      var b = await h.open("day3");
      await fake.install(b.ctx);
      await setUpExisting(b, code);
      await b.waitForSelector("#sync-status");
      await waitSynced(b);
      var merged = await b.evaluate(function () { return { n: PMP.Store.state.attempts.length, j: PMP.Store.state.journal.length }; });
      var nB0 = 40;
      assert.strictEqual(merged.n, nA + nB0, "both devices' answers");
      assert.strictEqual(merged.j, 12, "the journal came across");
      assert.strictEqual(fake.doc.attempts.length, nA + nB0);

      /* A change on B reaches A on its next sync. */
      await b.evaluate(function () {
        PMP.Store.commit(["flags"], function (s) { s.flags = Object.assign({}, s.flags, { "flag-from-b": true }); });
        return PMP.Sync.run("now");
      });
      await a.click("#sync-now");
      await waitSynced(a);
      await a.waitForFunction(function () { return !!PMP.Store.state.flags["flag-from-b"]; }, null, { timeout: 5000 });
      var nA2 = await a.evaluate(function () { return PMP.Store.state.attempts.length; });
      assert.strictEqual(nA2, nA + nB0);
      await lib.clean(a);
      await lib.clean(b);
      await a.ctx.close();
      await b.ctx.close();
    }
  },
  {
    name: "cloud sync: a conflict is merged again and retried",
    fn: async function (h, lib) {
      var a = await h.open("day3");
      await lib.go(a, "#/sync");
      var code = await a.textContent("#sync-code");
      var fake = fakeScript(code);
      await fake.install(a.ctx);
      await setUpNew(a);
      fake.ops = [];
      fake.conflictNext = function (doc) {
        var d = JSON.parse(JSON.stringify(doc));
        d.notes = Object.assign({}, d.notes, { "q-elsewhere": "saved by another device" });
        d.savedAt = Date.now();
        return d;
      };
      var res = await a.evaluate(function () {
        PMP.Store.commit(["notes"], function (s) { s.notes = Object.assign({}, s.notes, { "q-here": "mine" }); });
        return PMP.Sync.run("now");
      });
      assert.ok(res.ok, JSON.stringify(res));
      assert.strictEqual(res.retries, 1);
      assert.deepStrictEqual(fake.ops, ["get", "put", "put"]);
      assert.strictEqual(fake.doc.notes["q-here"], "mine");
      assert.strictEqual(fake.doc.notes["q-elsewhere"], "saved by another device");
      var local = await a.evaluate(function () { return PMP.Store.state.notes["q-elsewhere"]; });
      assert.strictEqual(local, "saved by another device");
      /* With nothing new on either side, a sync only reads. */
      fake.ops = [];
      var again = await a.evaluate(function () { return PMP.Sync.run("now"); });
      assert.ok(again.ok && !again.uploaded, JSON.stringify(again));
      assert.deepStrictEqual(fake.ops, ["get"]);
      /* Nothing during a mock. */
      var skipped = await a.evaluate(function () {
        PMP.Store.commit(["ui"], function (s) { s.ui.examActive = true; }, { transient: true });
        return PMP.Sync.run("now").then(function (r) {
          PMP.Store.commit(["ui"], function (s) { s.ui.examActive = false; }, { transient: true });
          return r;
        });
      });
      assert.strictEqual(skipped.skipped, "exam");
      await lib.clean(a);
      await a.ctx.close();
    }
  },
  {
    name: "cloud sync: a wrong code is shown inline, and Disconnect forgets the script",
    fn: async function (h, lib) {
      var a = await h.open("fresh");
      var fake = fakeScript("RIGHTCODE0123456789RIGHTCODE0123");
      await fake.install(a.ctx);
      await setUpExisting(a, "WRONGCODE0123456789WRONGCODE0123");
      await a.waitForFunction(function () { var m = document.getElementById("sync-test-msg"); return m && /refused the sync code/.test(m.textContent); }, null, { timeout: 5000 });
      assert.strictEqual(await a.evaluate(function () { return PMP.Sync.config(); }), null, "not saved");
      var toasts = await a.evaluate(function () { return document.getElementById("toasts").textContent; });
      assert.ok(!/refused/.test(toasts), "never a pop-up");

      await a.fill("#sync-code-in", "RIGHTCODE0123456789RIGHTCODE0123");
      await a.click("#sync-test");
      await a.waitForSelector("#sync-status");
      await waitSynced(a);
      /* The code is changed in the script: the next sync fails, on the page. */
      fake.code = "CHANGED00000000000000000000000000";
      await a.click("#sync-now");
      await a.waitForFunction(function () { return PMP.Sync.status.state === "error"; });
      var line = await a.textContent("#sync-status");
      assert.ok(/refused the sync code/.test(line) && /Last synced/.test(line), line);

      await a.click("#sync-off");
      await clickSheet(a, "Disconnect");
      var after = await a.evaluate(function () {
        return { cfg: PMP.Sync.config(), url: localStorage.getItem("pmp.sync.url"), code: localStorage.getItem("pmp.sync.code") };
      });
      assert.deepStrictEqual(after, { cfg: null, url: null, code: null });
      await lib.clean(a);
      await a.ctx.close();
    }
  },
  {
    name: "B1: calendar text is escaped per RFC 5545",
    fn: async function (h, lib) {
      var page = await h.open("planned_ontrack");
      var r = await page.evaluate(function () {
        return { esc: PMP.Coach.icsText("a\\b;c,d\ne\r\nf"), cal: PMP.Coach.calendar(PMP.Store.state).text };
      });
      assert.strictEqual(r.esc, "a\\\\b\\;c\\,d\\ne\\nf");
      r.cal.split("\r\n").filter(function (l) { return /^(SUMMARY|DESCRIPTION):/.test(l); }).forEach(function (l) {
        var v = l.replace(/^[A-Z]+:/, "");
        assert.ok(!/(^|[^\\]);|(^|[^\\]),/.test(v), "unescaped separator in " + l);
      });
      await page.ctx.close();
    }
  },
  {
    name: "B2: the theme button cycles device, light and dark",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      var seen = [];
      for (var i = 0; i < 4; i++) {
        seen.push(await page.evaluate(function () {
          return PMP.Store.state.settings.theme + ":" + document.getElementById("theme-label").textContent;
        }));
        await page.click("#theme-btn");
      }
      assert.deepStrictEqual(seen, ["system:System", "light:Light", "dark:Dark", "system:System"]);
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "B3: formula drills are kept, count as a study day and show in analytics",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      await lib.go(page, "#/formulas?tab=drill");
      await page.click('button:has-text("Show the working")');
      var r = await page.evaluate(function () {
        var d = PMP.Store.state.drills;
        return { n: d.length, keys: Object.keys(d[0]).sort(), ok: d[0].ok, g: d[0].g,
                 today: PMP.Metrics.streak(PMP.Store.state).today.drills };
      });
      assert.strictEqual(r.n, 1);
      assert.deepStrictEqual(r.keys, ["at", "g", "ok"]);
      assert.strictEqual(r.ok, false);
      assert.ok(r.g && r.g !== "unknown", r.g);
      assert.strictEqual(r.today, 1);
      await lib.go(page, "#/analytics");
      var text = await page.textContent("#view");
      assert.ok(/Formula drills/.test(text), "analytics lists the drill log");
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "lesson Mastered follows the shared task rule",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      var r = await page.evaluate(function () {
        var lesson = PMP.LESSONS.filter(function (l) { return l.ecoTask; })[0];
        var qs = PMP.QUESTIONS.filter(function (q) { return q.ecoTask === lesson.ecoTask; });
        var now = Date.now();
        function rows(n, right, ageDays) {
          var out = [];
          for (var i = 0; i < n; i++) {
            var q = qs[i % qs.length];
            out.push({ id: "m" + ageDays + "-" + i, qid: q.id, ecoTask: q.ecoTask, domain: q.domain, approach: q.approach, type: "single",
                       correct: i < right, credit: i < right ? 1 : 0, ms: 60000, at: new Date(now - ageDays * 86400000 + i * 1000).toISOString(),
                       confidence: null, mode: "practice", order: [] });
          }
          return out;
        }
        function status(att) {
          PMP.Store.commit(["attempts", "lessons"], function (s) { s.attempts = att; s.lessons = {}; s.lessons[lesson.id] = { opened: [], checks: {} }; });
          return PMP.Learn.status(lesson);
        }
        return [status(rows(9, 9, 1)), status(rows(12, 10, 1)), status(rows(12, 8, 1)), status(rows(12, 12, 20))];
      });
      assert.strictEqual(r[1], "mastered");
      assert.notStrictEqual(r[0], "mastered", "under minAnswers");
      assert.notStrictEqual(r[2], "mastered", "under 75% across the window");
      assert.notStrictEqual(r[3], "mastered", "faded after 14 days");
      await page.ctx.close();
    }
  },
  {
    name: "S4/S7/S9 slices: sittings, check-ins, weekly, sessions, answer signals and plan.rest travel both ways",
    fn: async function (h, lib) {
      var src = await h.open("misread_heavy");
      var out = await src.evaluate(function () {
        var S = PMP.Store, now = Date.now(), DAY = 86400000;
        var a0 = S.state.attempts[0];
        S.commit(["attempts", "sittings", "checkins", "weekly", "sessions", "settings"], function (s) {
          s.attempts = s.attempts.concat([
            Object.assign({}, a0, { id: "att-sig-1", at: new Date(now - DAY).toISOString(), ft: 21000, idle: 30000, w: 140, rw: 1, wr: 2 }),
            Object.assign({}, a0, { id: "att-sig-2", at: new Date(now - DAY + 5000).toISOString(), mode: "exam", examId: "sit-x", order: null, ft: 9000, w: 90 })
          ]);
          s.sittings = [{ k: "practice:1", kind: "practice", at: now - DAY, p: 20, a: 12, f: false }];
          s.checkins = [{ at: now - DAY, kind: "pre", v: 2, items: [] }, { at: now - 2 * DAY, kind: "weekly", v: 0, items: [4, 4, 3, 2, 3] }];
          s.weekly = [{ w: PMP.Metrics.weekStart(now) - 7 * DAY, u: now - 3 * DAY, r: 40, acc: 60, tt: 12, ma: 1, min: 90, d: 3, n: 50, mk: 0, lg: 30, cz: { RUSHED: 1 } }];
          s.sessions = [{ k: "s1", kind: "BUILD", t: "people-1", d: "people", s: 0.8, m: 30, at: now - 3 * DAY }];
          s.settings = Object.assign({}, s.settings, { restDays: false, reading: { scale: 1.3, roomy: true, guide: true } });
        });
        var doc = PMP.Sync.toDoc(S.state);
        var pick = function (st) { return JSON.parse(JSON.stringify({ sittings: st.sittings, checkins: st.checkins, weekly: st.weekly, sessions: st.sessions, rest: st.settings.restDays })); };
        return { doc: doc, before: pick(S.state) };
      });
      await src.ctx.close();
      var doc = out.doc;
      assert.deepStrictEqual(doc.sittings, out.before.sittings);
      assert.deepStrictEqual(doc.checkins.map(function (c) { return c.kind; }), ["weekly", "pre"], "sorted by time");
      assert.deepStrictEqual(doc.weekly, out.before.weekly);
      assert.deepStrictEqual(doc.sessions, out.before.sessions);
      ["sittings", "checkins", "weekly", "sessions"].forEach(function (k) { assert.ok(!(k in (doc.web || {})), k + " is not duplicated in web"); });
      var sig = doc.attempts.filter(function (r) { return r.id === "att-sig-1"; })[0];
      assert.deepStrictEqual([sig.ft, sig.idle, sig.w, sig.rw, sig.wr, sig.wx && sig.wx.ft], [21000, 30000, 140, 1, 2, undefined]);
      var ex = doc.attempts.filter(function (r) { return r.id === "att-sig-2"; })[0];
      assert.deepStrictEqual([ex.tm, ex.ft, ex.w, ex.src], [true, 9000, 90, "practice"]);
      assert.strictEqual(doc.plan.rest, false);
      assert.ok(!("reading" in doc.web.settings) && !("restDays" in doc.web.settings));

      var dst = await h.open("fresh");
      await dst.evaluate(function (d) { PMP.Data.importProgress(JSON.stringify(d)); }, doc);
      await clickSheet(dst, "Replace everything");
      await clickSheet(dst, "Replace it all");
      var after = await dst.evaluate(function (d) {
        var st = PMP.Store.state;
        return { st: JSON.parse(JSON.stringify({ sittings: st.sittings, checkins: st.checkins.slice().sort(function (a, b) { return b.at - a.at; }), weekly: st.weekly, sessions: st.sessions, rest: st.settings.restDays })),
                 reading: st.settings.reading, shared: Object.keys(st.shared).filter(function (k) { return ["sittings", "checkins", "weekly", "sessions"].indexOf(k) >= 0; }),
                 sig: st.attempts.filter(function (a) { return a.id === "att-sig-1"; })[0],
                 worry: PMP.Fitness.report(st).worry, same: PMP.Sync.sameDoc(PMP.Sync.toDoc(st), d) };
      }, doc);
      out.before.checkins.sort(function (a, b) { return b.at - a.at; });
      assert.deepStrictEqual(after.st, out.before);
      assert.deepStrictEqual(after.shared, [], "kept in their own slices, not in shared");
      assert.deepStrictEqual(after.reading, { scale: 1, roomy: false, guide: false }, "reading comfort is this device's own");
      assert.deepStrictEqual([after.sig.ft, after.sig.idle, after.sig.w, after.sig.rw, after.sig.wr], [21000, 30000, 140, 1, 2]);
      /* (4 + 4 + 3 + 2 + (6 − 3)) − 5 = 11, over 20: 55. */
      assert.strictEqual(after.worry, 55);
      assert.ok(after.same, "the second device writes the same document back");
      await lib.clean(dst);
      await dst.ctx.close();
    }
  },
  {
    name: "a phone's signals, sittings, check-ins and weekly reports are used here and go back out unchanged",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      var r = await page.evaluate(function () {
        var q = PMP.QUESTIONS[0], now = Date.now(), DAY = 86400000;
        var phone = {
          type: "pmp-progress", version: 3, savedAt: now, device: { app: "android", id: "p1" },
          attempts: [
            { q: q.id, t: q.ecoTask, d: q.domain, ap: q.approach, ty: "single", cr: 1, ok: true, ms: 50000, at: now - DAY, src: "practice", ft: 30000, idle: 31000, w: 100, rw: 1, el: 2 },
            { q: q.id, t: q.ecoTask, d: q.domain, ap: q.approach, ty: "single", cr: 0, ok: false, ms: 70000, at: now - DAY + 1000, src: "exam", tm: true, ft: 8000, w: 120, wr: 1 },
            { q: q.id, t: q.ecoTask, d: q.domain, ap: q.approach, ty: "single", cr: 1, ok: true, ms: 0, at: now - DAY + 2000, src: "exam" }
          ],
          plan: { examDate: 0, hours: 12, days: [2, 3, 4, 5, 6], onboarded: true, rest: false },
          sittings: [{ k: "mock:1", kind: "mock", at: now - DAY, p: 180, a: 170, f: true }],
          checkins: [{ at: now - 3 * DAY, kind: "weekly", v: 0, items: [5, 5, 5, 5, 1] }, { at: now - DAY, kind: "post", v: 2, items: [] }],
          weekly: [{ w: PMP.Metrics.weekStart(now) - 14 * DAY, u: now - 8 * DAY, r: 30, acc: 50, tt: 5, ma: 0, min: 40, d: 2, n: 20, mk: 0, f: 41, lg: 15 }],
          sessions: [{ k: "s9", kind: "BUILD", t: "", d: "", s: 0.5, m: 25, at: now - 2 * DAY }],
          mycards: [], chat: [], diagnostics: [], snoozed: {}
        };
        var st = PMP.Sync.fromDoc(phone, PMP.Store.state);
        var view = PMP.Store.defaults();
        Object.keys(st).forEach(function (k) { view[k] = st[k]; });
        var f = PMP.Fitness.report(view);
        var back = PMP.Sync.toDoc(view);
        return {
          rest: st.settings.restDays, worry: f.worry, hours: f.signal("hours").detail,
          ft: st.attempts[0].ft, mode: st.attempts.map(function (a) { return a.mode; }),
          weeks: PMP.Fitness.weeks(view).length,
          diff: ["attempts", "plan", "sittings", "checkins", "weekly", "sessions"].filter(function (k) { return PMP.Sync.canon(back[k]) !== PMP.Sync.canon(phone[k]); })
        };
      });
      assert.strictEqual(r.rest, false, "plan.rest becomes the rest-day setting");
      assert.strictEqual(r.worry, 100);
      assert.ok(/planned 12 hours a week/.test(r.hours), r.hours);
      assert.strictEqual(r.ft, 30000);
      assert.deepStrictEqual(r.mode, ["practice", "exam", "exam"]);
      assert.strictEqual(r.weeks, 2, "the phone's week and this week, live");
      assert.deepStrictEqual(r.diff, [], "changed on the way back out: " + r.diff.join(", "));
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "rows a phone left in `shared` before these slices existed are adopted on load",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      await page.evaluate(function () {
        localStorage.setItem("pmp.v2.shared", JSON.stringify({ v: 2, d: { chat: [], sittings: [{ k: "x", kind: "practice", at: 5, p: 10, a: 10, f: true }],
          checkins: [{ at: 7, kind: "pre", v: 4, items: [] }] } }));
        localStorage.removeItem("pmp.v2.sittings");
        localStorage.removeItem("pmp.v2.checkins");
      });
      await page.reload();
      await page.waitForFunction(function () { return window.PMP && PMP.Store.state.sys.ready; });
      var r = await page.evaluate(function () {
        var st = PMP.Store.state;
        return { sit: st.sittings.length, chk: st.checkins.length, shared: Object.keys(st.shared).sort(), doc: PMP.Sync.toDoc(st).sittings.length };
      });
      assert.deepStrictEqual(r, { sit: 1, chk: 1, shared: ["chat"], doc: 1 });
      await lib.clean(page);
      await page.ctx.close();
    }
  }
];
