"use strict";
/* Case studies on the web (§05.3, §05.8, §10.26): the mock's opening block
   filled with whole cases at full, half and sprint length, the "Case:
   <title>" panel in the sitting, its review and practice, the Case studies
   quick start, every id lookup resolving a case item, the audit's case
   card, and a case item's attempt surviving a round trip through sync. */
var assert = require("assert");
var H = require("./helpers");

/* What a paper of n items looks like, against the cases it came from. */
async function paperShape(page, n) {
  return page.evaluate(function (n) {
    var run = PMP.Exam.paper(n), L = run.layout;
    var caseOf = function (id) { var c = PMP.Content.caseFor(id); return c ? c.id : null; };
    var block = run.qids.slice(0, L.caseItems);
    var rest = run.qids.slice(L.caseItems);
    var groups = [];
    block.forEach(function (id) {
      var c = caseOf(id);
      if (!groups.length || groups[groups.length - 1].c !== c) groups.push({ c: c, ids: [] });
      groups[groups.length - 1].ids.push(id);
    });
    return {
      items: L.items, caseItems: L.caseItems, break1: L.break1After, len: run.qids.length,
      distinct: Object.keys(run.qids.reduce(function (m, id) { m[id] = 1; return m; }, {})).length,
      groups: groups.map(function (g) {
        var c = PMP.CASES.filter(function (x) { return x.id === g.c; })[0];
        return { c: g.c, ids: g.ids, authored: c ? c.questions.slice(0, g.ids.length).map(function (q) { return q.id; }) : null, whole: c ? c.questions.length : 0 };
      }),
      restCases: rest.filter(caseOf).length,
      allResolve: run.qids.every(function (id) { return !!PMP.Content.byId(id); })
    };
  }, n);
}

module.exports = [
  {
    name: "the phones' extractor still runs over §01 and §05 and carries the cases in extras.json",
    fn: async function () {
      var cp = require("child_process"), fs = require("fs"), os = require("os"), path = require("path");
      var out = fs.mkdtempSync(path.join(os.tmpdir(), "pmp-extract-"));
      try {
        cp.execFileSync(process.execPath, [path.join(__dirname, "..", "android", "tools", "extract-content.mjs"), path.join(__dirname, ".."), out], { stdio: "pipe" });
        var extras = JSON.parse(fs.readFileSync(path.join(out, "content", "extras.json"), "utf8"));
        var core = JSON.parse(fs.readFileSync(path.join(out, "content", "core.json"), "utf8"));
        assert.strictEqual(extras.cases.length, 5);
        assert.strictEqual(extras.cases.reduce(function (n, c) { return n + c.questions.length; }, 0), 50);
        assert.strictEqual(core.questions.length, 360);
        assert.strictEqual(core.questions.filter(function (q) { return q.type === "graphic"; }).length, 12);
      } finally { fs.rmSync(out, { recursive: true, force: true }); }
    }
  },
  {
    name: "content: 360 core items with 12 graphic, 5 cases of 50 items registered by id but not in the pool",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      var r = await page.evaluate(function () {
        var items = PMP.Content.caseItems();
        return {
          core: PMP.Packs.core, graphic: PMP.QUESTIONS.filter(function (q) { return q.type === "graphic"; }).length,
          cases: PMP.Content.cases().length, items: items.length,
          resolve: items.every(function (q) { return PMP.Content.byId(q.id) === q && PMP.Content.has(q.id); }),
          inPool: PMP.Content.all().filter(function (q) { return /^pmp-case/.test(q.id); }).length,
          caseFor: PMP.Content.caseFor("pmp-case2-003") && PMP.Content.caseFor("pmp-case2-003").id,
          notCase: PMP.Content.caseFor(PMP.QUESTIONS[0].id),
          dupe: PMP.Content.add([JSON.parse(JSON.stringify(items[0]))]).duplicates.length
        };
      });
      assert.deepStrictEqual(r, { core: 360, graphic: 12, cases: 5, items: 50, resolve: true, inPool: 0, caseFor: "case-02", notCase: null, dupe: 1 });
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "placement: full, half and sprint papers open with whole cases in authored order, the rest drawn as before",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      /* Full: a 40-item block is four whole cases of ten, and the first
         break lands at its end. */
      var full = await paperShape(page, 180);
      assert.strictEqual(full.items, 180); assert.strictEqual(full.len, 180); assert.strictEqual(full.distinct, 180);
      assert.strictEqual(full.caseItems, 40); assert.strictEqual(full.break1, 40);
      assert.strictEqual(full.groups.length, 4);
      full.groups.forEach(function (g) { assert.ok(g.c, "every block item is a case item"); assert.deepStrictEqual(g.ids, g.authored); assert.strictEqual(g.ids.length, g.whole); });
      assert.strictEqual(full.restCases, 0, "no case item is drawn into the rest");
      assert.ok(full.allResolve);
      /* Half: 20 places, two whole cases. */
      var half = await paperShape(page, 90);
      assert.strictEqual(half.caseItems, 20); assert.strictEqual(half.break1, 20); assert.strictEqual(half.len, 90);
      assert.strictEqual(half.groups.length, 2);
      half.groups.forEach(function (g) { assert.deepStrictEqual(g.ids, g.authored); assert.strictEqual(g.ids.length, 10); });
      assert.strictEqual(half.restCases, 0); assert.strictEqual(half.distinct, 90);
      /* Sprint: a block of 4 fits no whole case, so it is the opening four
         items of one case. */
      var sprint = await paperShape(page, 20);
      assert.strictEqual(sprint.caseItems, 4); assert.strictEqual(sprint.len, 20);
      assert.strictEqual(sprint.groups.length, 1);
      assert.deepStrictEqual(sprint.groups[0].ids, sprint.groups[0].authored);
      assert.strictEqual(sprint.groups[0].ids.length, 4);
      assert.strictEqual(sprint.restCases, 0);
      /* Freshest first: with three cases answered, a half paper takes the
         two that were not. */
      var picked = await page.evaluate(function () {
        PMP.Store.commit(["attempts"], function (s) {
          ["case-01", "case-02", "case-03"].forEach(function (cid) {
            var c = PMP.CASES.filter(function (x) { return x.id === cid; })[0];
            s.attempts.push({ id: PMP.rng.id("att"), qid: c.questions[0].id, ecoTask: c.questions[0].ecoTask, domain: c.questions[0].domain,
              correct: true, credit: 1, ms: 30000, at: new Date().toISOString(), mode: "practice" });
          });
        });
        var out = [];
        for (var k = 0; k < 5; k++) {
          var run = PMP.Exam.paper(90), seen = {};
          run.qids.slice(0, 20).forEach(function (id) { seen[PMP.Content.caseFor(id).id] = 1; });
          out.push(Object.keys(seen).sort().join(","));
        }
        return out;
      });
      picked.forEach(function (p) { assert.strictEqual(p, "case-04,case-05"); });
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "mock: the case panel sits above case items with passage, exhibit tables and position, remembers folding, and shows in the review",
    fn: async function (h, lib) {
      var page = await h.open("fresh", { hash: "#/exam" });
      var row = await page.evaluate(function () { var d = document.querySelector("#view dd.ex-case"); return d && d.textContent; });
      assert.ok(/^The first 40 questions, from whole cases/.test(row), row);
      await H.clickText(page, "#view button", "Start a sitting");
      var sheet = await page.evaluate(function () { return document.getElementById("sheet-body").textContent; });
      assert.ok(/opens with 40 case-study questions/.test(sheet), sheet);
      await H.clickText(page, "#sheet-foot button", "Start the clock");
      await page.waitForTimeout(150);
      var first = await page.evaluate(function () {
        var p = document.querySelector("#exam-card .case-panel");
        var run = PMP.Store.state.examRun;
        var c = PMP.Content.caseFor(run.qids[0]);
        return {
          open: p && p.open, summary: p && p.querySelector("summary .cp-t").textContent,
          pos: p && p.querySelector(".cp-pos").textContent, title: c.title,
          paras: p.querySelectorAll(".cp-bd > p").length, passage: c.passage.length,
          tables: p.querySelectorAll("table").length, exhibits: c.exhibits.length,
          captions: Array.prototype.map.call(p.querySelectorAll("table caption"), function (x) { return x.textContent; }),
          want: c.exhibits.map(function (x) { return x.label + ": " + x.title; }),
          colHeads: p.querySelectorAll("table thead th[scope=col]").length,
          rowHeads: p.querySelectorAll("table tbody th[scope=row]").length,
          above: p.compareDocumentPosition(document.querySelector("#exam-card .q-stem")) & Node.DOCUMENT_POSITION_FOLLOWING,
          region: p.querySelector(".cp-bd").getAttribute("role")
        };
      });
      assert.strictEqual(first.open, true);
      assert.strictEqual(first.summary, "Case: " + first.title);
      assert.strictEqual(first.pos, "Question 1 of 10 in this case");
      assert.strictEqual(first.paras, first.passage);
      assert.strictEqual(first.tables, first.exhibits);
      assert.deepStrictEqual(first.captions, first.want);
      assert.ok(first.colHeads > 0 && first.rowHeads > 0);
      assert.ok(first.above, "the panel is above the stem");
      assert.strictEqual(first.region, "region");
      /* Fold it, move on: the next item of the same case shows it folded. */
      await page.evaluate(function () { document.querySelector("#exam-card .case-panel summary").click(); });
      await page.waitForTimeout(60);
      await H.clickText(page, "#exam-card button", "Next");
      var second = await page.evaluate(function () {
        var p = document.querySelector("#exam-card .case-panel");
        return { open: p.open, pos: p.querySelector(".cp-pos").textContent };
      });
      assert.deepStrictEqual(second, { open: false, pos: "Question 2 of 10 in this case" });
      await page.evaluate(function () { document.querySelector("#exam-card .case-panel summary").click(); });
      await page.waitForTimeout(60);
      await H.clickText(page, "#exam-card button", "Back");
      assert.strictEqual(await page.evaluate(function () { return document.querySelector("#exam-card .case-panel").open; }), true);
      /* End it and open the review: a case item's row carries the panel. */
      await H.clickText(page, "#view button", "End exam");
      await H.clickText(page, "#sheet-foot button", "End and mark it");
      await page.waitForTimeout(200);
      var rev = await page.evaluate(function () {
        var dets = document.querySelectorAll("#view details.review");
        var d = dets[0];
        if (!d.open) { d.open = true; }
        return new Promise(function (res) {
          setTimeout(function () {
            var last = dets[dets.length - 1];
            last.open = true;
            setTimeout(function () {
              res({ count: dets.length, panel: !!d.querySelector(".case-panel"), title: d.querySelector(".case-panel .cp-t").textContent,
                    lastPanel: !!last.querySelector(".case-panel") });
            }, 60);
          }, 60);
        });
      });
      assert.strictEqual(rev.count, 180);
      assert.ok(rev.panel && /^Case: /.test(rev.title), JSON.stringify(rev));
      assert.strictEqual(rev.lastPanel, false, "an ordinary item has no case panel");
      /* The sitting's case attempts resolve in history like any other. */
      var hist = await page.evaluate(function () {
        var atts = PMP.Store.state.attempts.filter(function (a) { return /^pmp-case/.test(a.qid); });
        return { n: atts.length, resolve: atts.every(function (a) { return !!PMP.Content.byId(a.qid); }) };
      });
      assert.ok(hist.resolve);
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "mock: an unfinished sitting with a case block restores with its panel after a reload",
    fn: async function (h, lib) {
      var page = await h.open("fresh", { hash: "#/exam" });
      await H.clickText(page, "#view button", "Start a sitting");
      await H.clickText(page, "#sheet-foot button", "Start the clock");
      await page.waitForTimeout(150);
      var qid = await page.evaluate(function () { PMP.Store.flush(); return PMP.Store.state.examRun.qids[0]; });
      await page.reload();
      await page.waitForFunction(function () { return window.PMP && PMP.Store.state.sys.ready; });
      await page.waitForTimeout(150);
      await page.evaluate(function () { if (PMP.Router.current.name !== "exam") location.hash = "#/exam"; });
      await page.waitForTimeout(100);
      await page.evaluate(function () {
        var b = Array.prototype.filter.call(document.querySelectorAll("#view button, #sheet-foot button"), function (x) { return /^Resume/.test(x.textContent.trim()); })[0];
        if (!b) throw new Error("no resume button");
        b.click();
      });
      await page.waitForTimeout(150);
      var r = await page.evaluate(function () {
        var p = document.querySelector("#exam-card .case-panel");
        return { qid: PMP.Store.state.examRun.qids[0], panel: !!p, title: p && p.querySelector(".cp-t").textContent };
      });
      assert.strictEqual(r.qid, qid);
      assert.ok(r.panel);
      assert.strictEqual(r.title, "Case: " + await page.evaluate(function (id) { return PMP.Content.caseFor(id).title; }, qid));
      await page.ctx.close();
    }
  },
  {
    name: "practice: the Case studies quick start runs one whole case in order with its panel, least-seen first",
    fn: async function (h, lib) {
      var page = await h.open("fresh", { hash: "#/practice" });
      var card = await page.evaluate(function () {
        var c = document.getElementById("case-quick");
        return c && { h: c.querySelector("h2").textContent, text: c.textContent };
      });
      assert.ok(card && card.h === "Case studies", JSON.stringify(card));
      assert.ok(/5 cases loaded/.test(card.text), card.text);
      await H.clickText(page, "#case-quick button", "Start a case study");
      await page.waitForTimeout(100);
      var s = await page.evaluate(function () {
        var sess = PMP.Practice.session, c = PMP.Content.caseFor(sess.items[0].q.id);
        var p = document.querySelector("#drill-card .case-panel");
        return {
          ids: sess.items.map(function (i) { return i.q.id; }), authored: c.questions.map(function (q) { return q.id; }),
          title: document.querySelector(".drill-title").textContent, want: "Case: " + c.title,
          panel: !!p, pos: p && p.querySelector(".cp-pos").textContent, caseId: c.id
        };
      });
      assert.deepStrictEqual(s.ids, s.authored);
      assert.strictEqual(s.title, s.want);
      assert.ok(s.panel);
      assert.strictEqual(s.pos, "Question 1 of 10 in this case");
      await H.finishDrill(page, 40);
      var after = await page.evaluate(function (cid) {
        var atts = PMP.Store.state.attempts.filter(function (a) { return PMP.Content.caseFor(a.qid) && PMP.Content.caseFor(a.qid).id === cid; });
        var next = PMP.Cases.next(PMP.Store.state);
        return { n: atts.length, modes: atts.map(function (a) { return a.mode; }).filter(function (m, i, a) { return a.indexOf(m) === i; }), next: next.id };
      }, s.caseId);
      assert.strictEqual(after.n, 10);
      assert.deepStrictEqual(after.modes, ["practice"]);
      assert.notStrictEqual(after.next, s.caseId, "the case just done is no longer the freshest");
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "lookups: journal, flags, notes and search resolve case items; search opens a whole case",
    fn: async function (h, lib) {
      var page = await h.open("fresh", { hash: "#/practice" });
      var c = await page.evaluate(function () {
        var c = PMP.CASES[2], q = c.questions[0];
        PMP.Store.commit(["attempts", "flags", "notes"], function (s) {
          s.attempts.push({ id: "att-case-x", qid: q.id, ecoTask: q.ecoTask, domain: q.domain, approach: q.approach, type: "single",
            correct: false, credit: 0, ms: 40000, at: new Date().toISOString(), mode: "practice" });
          s.flags[q.id] = true;
          s.notes[q.id] = "Case note about the steering group";
        });
        return { id: c.id, title: c.title, qid: q.id, stem: q.stem, phrase: c.passage[0].split(" ").slice(3, 8).join(" ") };
      });
      await lib.go(page, "#/journal");
      var journal = await page.evaluate(function (stem) { return document.getElementById("view").textContent.indexOf(stem.slice(0, 50)) >= 0; }, c.stem);
      assert.ok(journal, "the journal shows the missed case item");
      var hits = await page.evaluate(function (c) {
        return {
          passage: PMP.Search.run(c.phrase).filter(function (x) { return x.kind === "Case study"; }).map(function (x) { return x.title; }),
          note: PMP.Search.run("steering group").filter(function (x) { return x.kind === "Your note on a question"; }).length,
          stem: PMP.Search.run(c.stem.slice(0, 40)).filter(function (x) { return x.kind === "Question" && x.question.id === c.qid; }).length
        };
      }, c);
      assert.deepStrictEqual(hits, { passage: ["Case: " + c.title], note: 1, stem: 1 });
      /* The search result opens the whole case, flag and all. */
      await lib.go(page, "#/search");
      await page.evaluate(function (c) {
        var input = document.querySelector("#view input");
        input.value = c.phrase; input.dispatchEvent(new Event("input", { bubbles: true }));
      }, c);
      await page.waitForTimeout(400);
      await H.clickText(page, "#view .sr-go", "Case: " + c.title);
      await page.waitForTimeout(100);
      var run = await page.evaluate(function () {
        var s = PMP.Practice.session;
        return { first: s.items[0].q.id, flag: Array.prototype.filter.call(document.querySelectorAll("#drill-card .q-tools button"), function (b) { return /^Flag/.test(b.textContent.trim()); })[0].getAttribute("aria-pressed"), cell: document.querySelector(".navgrid-c").getAttribute("data-st") };
      });
      assert.deepStrictEqual(run, { first: c.qid, flag: "true", cell: "flagged" });
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "sync: a case item's attempt round-trips through the shared document",
    fn: async function (h, lib) {
      var page = await h.open("fresh", { hash: "#/practice" });
      await page.evaluate(function () { PMP.Practice.startCase(PMP.CASES[0]); });
      await page.waitForTimeout(80);
      await H.answerOne(page);
      await page.waitForTimeout(60);
      var r = await page.evaluate(function () {
        var st = PMP.Store.state;
        var a = st.attempts.filter(function (x) { return /^pmp-case1-/.test(x.qid); })[0];
        var doc = JSON.parse(JSON.stringify(PMP.Sync.toDoc(st)));
        var row = doc.attempts.filter(function (x) { return x.q === a.qid; })[0];
        var back = PMP.Sync.fromDoc(doc, PMP.Store.state);
        var b = back.attempts.filter(function (x) { return x.qid === a.qid; })[0];
        return {
          row: !!row, q: row && row.q, d: row && row.d, t: row && row.t,
          back: !!b, same: b && b.qid === a.qid && b.domain === a.domain && b.ecoTask === a.ecoTask && b.correct === a.correct && b.mode === a.mode,
          resolves: !!PMP.Content.byId(b.qid), sameDoc: PMP.Sync.sameDoc(PMP.Sync.toDoc(Object.assign({}, st, { attempts: back.attempts })), doc)
        };
      });
      assert.ok(r.row && r.back && r.same && r.resolves, JSON.stringify(r));
      assert.ok(/^pmp-case1-/.test(r.q));
      assert.ok(r.sameDoc, "the document reads back unchanged");
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "audit: the case card reports 5 cases and 50 items, every rule passing; About says the block is filled",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      var r = await page.evaluate(function () {
        var cr = PMP.Audit.auditCases(PMP.CASES), core = PMP.Audit.audit(PMP.Content.all());
        return { pass: cr.pass, fails: cr.failures.slice(0, 3), cases: cr.counts.cases, items: cr.counts.items, checks: cr.checks.length, core: core.pass, coreN: core.counts.total, lib: PMP.Library.check().pass };
      });
      assert.deepStrictEqual(r.fails, []);
      assert.ok(r.pass && r.core && r.lib, JSON.stringify(r));
      assert.strictEqual(r.cases, 5); assert.strictEqual(r.items, 50); assert.strictEqual(r.coreN, 360);
      assert.ok(r.checks >= 10);
      /* A broken case fails loudly, by case id. */
      var broken = await page.evaluate(function () {
        var c = JSON.parse(JSON.stringify(PMP.CASES[0]));
        c.passage = ["Too short. See Exhibit 9."];
        c.questions[0].explanation = "Short.";
        return PMP.Audit.auditCases([c]).failures.map(function (f) { return f.where; });
      });
      assert.ok(broken.indexOf("case-01.passage") >= 0, broken.join());
      assert.ok(broken.indexOf("case-01.exhibits") >= 0, broken.join());
      assert.ok(broken.indexOf("case-01.questions[0].explanation") >= 0, broken.join());
      await lib.go(page, "#/audit");
      var card = await page.evaluate(function () { return document.querySelector("#audit-cases h2").textContent; });
      assert.strictEqual(card, "5 cases, 50 items, every rule passes");
      await lib.go(page, "#/settings");
      var about = await page.evaluate(function () { return document.getElementById("view").textContent; });
      assert.ok(/PMI does not publish the size of the case-study block/.test(about));
      assert.ok(/fills that block with whole case studies/.test(about) && /5 cases and 50 case questions are loaded/.test(about), about.slice(0, 200));
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "390px: the case panel and its exhibit tables fit with no horizontal page scroll",
    fn: async function (h, lib) {
      var page = await h.open("fresh", { hash: "#/practice", viewport: { width: 390, height: 844 } });
      for (var k = 0; k < 5; k++) {
        await page.evaluate(function (k) { PMP.Practice.startCase(PMP.CASES[k]); }, k);
        await page.waitForTimeout(80);
        var r = await page.evaluate(function () {
          return { over: document.documentElement.scrollWidth > window.innerWidth + 1, panel: !!document.querySelector("#drill-card .case-panel") };
        });
        assert.deepStrictEqual(r, { over: false, panel: true }, "case " + (k + 1));
      }
      await lib.clean(page);
      await page.ctx.close();
    }
  }
];
