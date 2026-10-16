"use strict";
/* S6 search (§10.22) and S7 reading comfort and rest days (§10.23,
   §10.00.7). */
var assert = require("assert");
var H = require("./helpers");

var DAY = 86400000;

/* In-page: attempts on the given days ago, one each, mid-morning. */
var SEED_DAYS = function (days) {
  var q = PMP.QUESTIONS[0], today = PMP.time.startOfDay(Date.now());
  var atts = days.map(function (d, i) {
    return { id: "rd-" + i, qid: q.id, ecoTask: q.ecoTask, domain: q.domain, type: "single", correct: true, credit: 1,
             ms: 60000, at: new Date(today - d * 86400000 + 10 * 3600000).toISOString(), mode: "practice", order: [] };
  });
  PMP.Store.commit(["attempts"], function (s) { s.attempts = atts; });
};

async function results(page, q) {
  await page.fill("#search-q", q);
  await page.waitForTimeout(350);
  return page.evaluate(function () {
    return {
      status: document.getElementById("search-n").textContent,
      hits: Array.prototype.map.call(document.querySelectorAll("#search-results .sr-hit"), function (li) {
        return { kind: li.getAttribute("data-kind"), title: li.querySelector(".sr-t").textContent };
      })
    };
  });
}

module.exports = [
  {
    name: "/ opens search from anywhere outside a box; every word must match; at most 60",
    fn: async function (h, lib) {
      var page = await h.open("day3");
      await page.keyboard.press("/");
      await page.waitForTimeout(150);
      var r = await page.evaluate(function () { return { route: PMP.Router.current.name, focus: document.activeElement.id }; });
      assert.deepStrictEqual(r, { route: "search", focus: "search-q" });
      var ev = await results(page, "earned value");
      var kinds = ev.hits.map(function (x) { return x.kind; });
      ["Lesson", "Glossary", "Formula", "Question"].forEach(function (k) { assert.ok(kinds.indexOf(k) >= 0, k + " in " + kinds.join(",")); });
      /* In the order lessons, glossary, abbreviations, formulas, notes, questions. */
      var order = ["Lesson", "Glossary", "Abbreviation", "Formula", "Your notes", "Your note on a question", "Question"];
      for (var i = 1; i < kinds.length; i++) assert.ok(order.indexOf(kinds[i]) >= order.indexOf(kinds[i - 1]), kinds.join(","));
      var none = await results(page, "earned qqzzxx");
      assert.deepStrictEqual([none.status, none.hits.length], ["Nothing found.", 0]);
      var many = await results(page, "the");
      assert.deepStrictEqual([many.status, many.hits.length], ["First 60 results.", 60]);
      var one = await results(page, "e");
      assert.strictEqual(one.status, "Type at least two letters.");
      /* Typing / inside a box types it. */
      await page.fill("#search-q", "");
      await page.keyboard.type("a/b");
      var v = await page.evaluate(function () { return document.getElementById("search-q").value; });
      assert.strictEqual(v, "a/b");
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "notes are searched; a question opens as a practice set of one; a term opens in place",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      var qid = await page.evaluate(function () {
        var q = PMP.QUESTIONS[5];
        PMP.Store.commit(["notes", "notepad"], function (s) {
          s.notes = { [q.id]: "xyzzy mnemonic\nsecond line" };
          s.notepad = Object.assign({}, s.notepad, { text: "plugh is the float rule", html: "plugh is the float rule" });
        });
        return q.id;
      });
      await lib.go(page, "#/search");
      var a = await results(page, "xyzzy");
      assert.deepStrictEqual(a.hits, [{ kind: "Your note on a question", title: "xyzzy mnemonic" }]);
      var b = await results(page, "plugh float");
      assert.deepStrictEqual(b.hits, [{ kind: "Your notes", title: "Notes page" }]);
      await page.click("#search-results .sr-go");
      var open = await page.evaluate(function () { return PMP.Notepad.isOpen(); });
      assert.ok(open, "the notes pane opens");

      var c = await results(page, "acceptance criteria");
      var g = c.hits.map(function (x) { return x.kind; }).indexOf("Glossary");
      assert.ok(g >= 0);
      await page.evaluate(function (i) { document.querySelectorAll("#search-results .sr-hit")[i].querySelector("summary").click(); }, g);
      var shown = await page.evaluate(function (i) {
        var li = document.querySelectorAll("#search-results .sr-hit")[i];
        return { open: li.querySelector("details").open, text: li.querySelector(".sr-full").textContent };
      }, g);
      assert.ok(shown.open && /deliverable/.test(shown.text), JSON.stringify(shown));

      await results(page, "xyzzy");
      await page.click("#search-results .sr-go");
      await page.waitForTimeout(100);
      var p = await page.evaluate(function () { return { route: PMP.Router.current.name, ids: PMP.Practice.session.items.map(function (i) { return i.q.id; }) }; });
      assert.deepStrictEqual(p, { route: "practice", ids: [qid] });
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "reading comfort: text size and roomier lines scale stems, options and lessons; device-only",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      await page.evaluate(function () {
        PMP.Practice.startWith(PMP.QUESTIONS.filter(function (q) { return q.type === "single"; }).slice(0, 1), "practice");
        PMP.Router.navigate("practice");
      });
      await page.waitForTimeout(100);
      var base = await page.evaluate(function () {
        var s = getComputedStyle(document.querySelector("#drill-card .q-stem")), o = getComputedStyle(document.querySelector("#drill-card .opt .txt"));
        return { stem: parseFloat(s.fontSize), lh: parseFloat(s.lineHeight), opt: parseFloat(o.fontSize), read: document.documentElement.getAttribute("data-read") };
      });
      assert.strictEqual(base.read, null, "nothing applies at the defaults");
      await lib.go(page, "#/settings");
      await page.click('label[for="set-read-size-2"]');
      await page.selectOption("#set-read-roomy", "on");
      await lib.go(page, "#/practice");
      var big = await page.evaluate(function () {
        var s = getComputedStyle(document.querySelector("#drill-card .q-stem")), o = getComputedStyle(document.querySelector("#drill-card .opt .txt"));
        return { stem: parseFloat(s.fontSize), lh: parseFloat(s.lineHeight), opt: parseFloat(o.fontSize), reading: PMP.Store.state.settings.reading,
                 web: PMP.Sync.toDoc(PMP.Store.state).web.settings };
      });
      assert.ok(Math.abs(big.stem - base.stem * 1.3) < 0.05, JSON.stringify([base, big]));
      assert.ok(Math.abs(big.opt - base.opt * 1.3) < 0.05);
      assert.ok(Math.abs(big.lh - base.lh * 1.3 * 1.18) < 0.2, "roomier lines: " + big.lh);
      assert.deepStrictEqual(big.reading, { scale: 1.3, roomy: true, guide: false });
      assert.ok(!("reading" in big.web) && !("restDays" in big.web), "reading comfort stays on this device");
      await page.reload();
      await page.waitForFunction(function () { return window.PMP && PMP.Store.state.sys.ready; });
      var kept = await page.evaluate(function () { return [document.documentElement.getAttribute("data-read"), document.documentElement.style.getPropertyValue("--rd-scale")]; });
      assert.deepStrictEqual(kept, ["on", "1.3"]);
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "the reading guide: a band on one line of the stem that moves on click and with the arrow keys",
    fn: async function (h, lib) {
      var page = await h.open("fresh", { viewport: { width: 700, height: 900 } });
      await lib.go(page, "#/settings");
      await page.selectOption("#set-read-guide", "on");
      var preview = await page.evaluate(function () { return !!document.querySelector("#set-study .rd-preview .q-stem[data-guide=true] .rg-band"); });
      assert.ok(preview, "the preview shows the guide");
      await page.evaluate(function () {
        var long = PMP.QUESTIONS.filter(function (q) { return q.type === "single" && q.stem.length > 300; })[0];
        PMP.Practice.startWith([long], "practice");
        PMP.Router.navigate("practice");
      });
      await page.waitForTimeout(100);
      var box = await page.evaluate(function () {
        var p = document.querySelector("#drill-card .q-stem"), r = p.getBoundingClientRect();
        return { guide: p.getAttribute("data-guide"), tab: p.getAttribute("tabindex"), top: r.top, left: r.left, h: r.height,
                 lh: parseFloat(getComputedStyle(p).lineHeight), bandH: document.querySelector("#drill-card .rg-band").getBoundingClientRect().height };
      });
      assert.deepStrictEqual([box.guide, box.tab], ["true", "0"]);
      assert.ok(box.h / box.lh >= 3, "a stem of several lines");
      assert.ok(Math.abs(box.bandH - box.lh) < 1, "the band is one line tall");
      await page.mouse.click(box.left + 40, box.top + box.lh * 2.5);
      var line = await page.evaluate(function () { return document.querySelector("#drill-card .q-stem").getAttribute("data-line"); });
      assert.strictEqual(line, "2");
      await page.focus("#drill-card .q-stem");
      await page.keyboard.press("ArrowDown");
      await page.keyboard.press("ArrowUp");
      await page.keyboard.press("ArrowUp");
      var after = await page.evaluate(function () {
        var p = document.querySelector("#drill-card .q-stem"), band = p.querySelector(".rg-band");
        return { line: p.getAttribute("data-line"), off: band.getBoundingClientRect().top - p.getBoundingClientRect().top, lh: parseFloat(getComputedStyle(p).lineHeight) };
      });
      assert.strictEqual(after.line, "1");
      assert.ok(Math.abs(after.off - after.lh) < 1, "the band sits on the second line");
      for (var i = 0; i < 6; i++) await page.keyboard.press("ArrowUp");
      var top = await page.evaluate(function () { return document.querySelector("#drill-card .q-stem").getAttribute("data-line"); });
      assert.strictEqual(top, "0", "it stops at the first line");
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "rest days: one missed day a week between study days keeps the streak; off, it does not",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      await page.evaluate(SEED_DAYS, [6, 5, 4, 3, 1, 0]);
      var on = await page.evaluate(function () { var s = PMP.Metrics.streak(PMP.Store.state); return { cur: s.current, rest: s.restDays, active: s.activeInWindow, rests: s.days.filter(function (d) { return d.rest; }).length }; });
      assert.deepStrictEqual(on, { cur: 7, rest: 1, active: 6, rests: 1 }, "the habit window still counts real days");
      await lib.go(page, "#/dashboard");
      var dots = await page.evaluate(function () { return document.querySelectorAll('.dots i[data-rest="true"]').length; });
      assert.strictEqual(dots, 1);
      await lib.go(page, "#/settings");
      await page.selectOption("#set-rest", "off");
      var off = await page.evaluate(function () {
        var st = PMP.Store.state;
        return { cur: PMP.Metrics.streak(st).current, plan: PMP.Sync.toDoc(st).plan };
      });
      assert.strictEqual(off.cur, 2);
      assert.strictEqual(off.plan.rest, false, "plan.rest carries the setting");
      /* Two missed days in a row are a gap, not a rest day. */
      await page.selectOption("#set-rest", "on");
      await page.evaluate(SEED_DAYS, [6, 5, 4, 1, 0]);
      var gap = await page.evaluate(function () { return PMP.Metrics.streak(PMP.Store.state).current; });
      assert.strictEqual(gap, 2);
      await lib.clean(page);
      await page.ctx.close();
    }
  }
];
