"use strict";
/* What the lecture series taught well, brought into the app: sample documents
   (L2), commonly confused pairs (L3), worked formula examples (L4), the extra
   theory (L6), the certification lesson F0 (L7) and the walk-throughs (L8). */
var assert = require("assert");

module.exports = [
  {
    name: "Documents tab: every sample folds open, a link opens one, and its links resolve",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      await lib.go(page, "#/library?tab=documents");
      var r = await page.evaluate(function () {
        var boxes = document.querySelectorAll("#view details.lib-doc");
        var bad = [];
        Array.prototype.forEach.call(document.querySelectorAll("#view .lib-doc-see a"), function (a) {
          if (!/^#\/(learn\?lesson=[A-Z]\d+|library\?tab=(process|glossary)&sel=.+)$/.test(a.getAttribute("href"))) bad.push(a.getAttribute("href"));
        });
        return { n: boxes.length, data: PMP.DOCUMENTS.documents.length, open: document.querySelectorAll("#view details.lib-doc[open]").length, bad: bad };
      });
      assert.strictEqual(r.n, r.data);
      assert.ok(r.n >= 20, "about twenty samples");
      assert.strictEqual(r.open, 0, "all folded on arrival");
      assert.deepStrictEqual(r.bad, []);

      await lib.go(page, "#/library?tab=documents&sel=doc-charter");
      var opened = await page.evaluate(function () {
        var d = document.querySelector("#view details.lib-doc[open]");
        return d ? { id: d.id, rows: d.querySelectorAll("tbody tr").length } : null;
      });
      assert.ok(opened && opened.id === "doc-charter" && opened.rows > 3, JSON.stringify(opened));
    }
  },
  {
    name: "every formula on the sheet has a folded worked example with steps",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      await lib.go(page, "#/formulas?tab=sheet");
      var r = await page.evaluate(function () {
        var items = PMP.FORMULAS.reduce(function (a, g) { return a + g.items.length; }, 0);
        var ex = document.querySelectorAll("#view .fx-ex");
        var empty = Array.prototype.filter.call(ex, function (d) { return !d.querySelector(".fx-steps li"); }).length;
        return { items: items, ex: ex.length, empty: empty };
      });
      assert.strictEqual(r.ex, r.items);
      assert.strictEqual(r.empty, 0);
    }
  },
  {
    name: "content: F0 follows F1, the walk-throughs pair each step with a check, the confused pairs ship",
    fn: async function (h) {
      var page = await h.open("fresh");
      var r = await page.evaluate(function () {
        var byId = {};
        PMP.LESSONS.forEach(function (l) { byId[l.id] = l; });
        var walks = ["P3", "P4", "P6", "B3", "P7"].map(function (id) {
          var l = byId[id];
          var steps = l.sections.filter(function (s) { return /^w\d+$/.test(s.id); }).length;
          var checks = l.checks.filter(function (c) { return c.id.indexOf(id + "-w") === 0; }).length;
          return id + ":" + steps + "/" + checks;
        });
        return {
          first: PMP.LESSONS.slice().sort(function (a, b) { return a.order - b.order; }).slice(0, 2).map(function (l) { return l.id; }).join(),
          walks: walks,
          deck: PMP.FLASHCARDS.filter(function (c) { return c.deck === "confused"; }).length,
          tables: PMP.REFERENCE.tables.filter(function (t) { return /^t-confused-/.test(t.id); }).length,
          theory: ["t-mastery", "t-five-cs", "t-ethics", "t-culture", "t-quality-stats"].filter(function (id) {
            return PMP.REFERENCE.tables.some(function (t) { return t.id === id; });
          }).length
        };
      });
      assert.strictEqual(r.first, "F1,F0", "F0 follows the study-skills lesson");
      r.walks.forEach(function (w) {
        var m = /(\w+):(\d+)\/(\d+)/.exec(w);
        assert.ok(+m[2] >= 5 && m[2] === m[3], w);
      });
      assert.ok(r.deck >= 30, "confused deck");
      assert.strictEqual(r.tables, 3);
      assert.strictEqual(r.theory, 5);
    }
  },
  {
    name: "study order: by knowledge area, new lessons come in classroom order; the setting is saved in the shared plan",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      var r = await page.evaluate(function () {
        var P = PMP.Path, x = P.extras(), now = new Date(2027, 2, 1, 9).getTime();
        function builds(order) {
          var plan = P.build({ x: x, attempts: [], lessonsDone: [], results: [], mocks: [],
            diagnostic: { people: 0.6, process: 0.5, business: 0.55 },
            settings: { examDate: now + 90 * 86400000, hoursPerWeek: 10, studyDays: [1, 2, 3, 4, 5, 6, 7], order: order },
            now: now, mistakeTags: {}, leeches: {}, ladderNext: null, readingSlow: false });
          return plan.sessions.filter(function (s) { return s.kind === "BUILD"; }).map(function (s) { return s.lessonId; });
        }
        var course = builds("course"), domain = builds("domain");
        var idx = course.map(function (id) { return x.courseOrder.indexOf(id); });
        return { n: course.length, sorted: idx.every(function (v, i) { return i === 0 || v > idx[i - 1]; }), differs: course.join() !== domain.join() };
      });
      assert.ok(r.n >= 10 && r.sorted && r.differs, JSON.stringify(r));

      await lib.go(page, "#/planner");
      await page.selectOption("#pt-order", "course");
      await page.fill("#pt-date", "2027-06-01");
      await page.click("#pt-plan .filter-bar .btn");
      var saved = await page.evaluate(function () { return [PMP.Store.state.shared.plan.order, PMP.Path.planOf(PMP.Store.state).order]; });
      assert.deepStrictEqual(saved, ["course", "course"]);
    }
  },
  {
    name: "Area tours: ten areas, each with its flow figure and its processes in order",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      await lib.go(page, "#/library?tab=tours");
      var r = await page.evaluate(function () {
        var secs = document.querySelectorAll("#view [id^='tour-']");
        return { n: secs.length, figs: document.querySelectorAll("#view [id^='tour-'] figure svg").length,
                 procs: document.querySelectorAll("#view .lib-tour li").length,
                 wide: document.documentElement.scrollWidth > document.documentElement.clientWidth };
      });
      assert.deepStrictEqual(r, { n: 10, figs: 10, procs: 49, wide: false });
    }
  },
  {
    name: "lesson figures: drawn in the section they illustrate, with an accessible name, no page scroll at 390px",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      var n = await page.evaluate(function () {
        return PMP.LESSONS.reduce(function (a, l) { return a + l.sections.filter(function (s) { return s.figure; }).length; }, 0);
      });
      assert.ok(n >= 40, n + " figures");
      await page.setViewportSize({ width: 390, height: 800 });
      await lib.go(page, "#/learn?lesson=P3");
      var r = await page.evaluate(function () {
        document.querySelectorAll("#view details.ln-sec").forEach(function (d) { d.open = true; });
        var svgs = document.querySelectorAll("#view figure.ln-fig svg");
        return { svgs: svgs.length, named: Array.prototype.every.call(svgs, function (s) { return (s.getAttribute("aria-label") || "").length > 20; }),
                 shapes: svgs.length ? svgs[0].querySelectorAll("rect, polyline, text, circle").length : 0,
                 wide: document.documentElement.scrollWidth > document.documentElement.clientWidth };
      });
      assert.ok(r.svgs >= 5 && r.named && r.shapes > 3 && !r.wide, JSON.stringify(r));
    }
  },
  {
    name: "Export lives in Cloud sync, not at the foot of the menu, and still saves the progress file",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      assert.strictEqual(await page.$("#rail #export-btn"), null);
      await lib.go(page, "#/sync");
      var called = await page.evaluate(function () {
        var n = 0, real = PMP.Exporter.exportProgress;
        PMP.Exporter.exportProgress = function () { n++; };
        document.getElementById("export-btn").click();
        PMP.Exporter.exportProgress = real;
        return n;
      });
      assert.strictEqual(called, 1);
    }
  },
  {
    name: "progress tiles: the widget's six figures in the top bar from 900px, tinted, linked; hidden on a phone",
    fn: async function (h, lib) {
      var page = await h.open("day3");
      await page.evaluate(function () {
        var d = new Date(Date.now() + 45 * 86400000), z = function (n) { return (n < 10 ? "0" : "") + n; };
        PMP.Store.commit(["settings"], function (s) { s.settings.examDate = d.getFullYear() + "-" + z(d.getMonth() + 1) + "-" + z(d.getDate()); });
      });
      await page.waitForTimeout(700);
      var r = await page.evaluate(function () {
        var tiles = Array.prototype.slice.call(document.querySelectorAll("#top-stats .ts-tile"));
        var bar = document.querySelector(".topbar").getBoundingClientRect();
        return {
          labels: tiles.map(function (t) { return t.querySelector(".ts-l").textContent; }),
          links: tiles.map(function (t) { return t.getAttribute("href"); }),
          tinted: tiles.filter(function (t) { return t.hasAttribute("data-heat"); }).length,
          oneRow: tiles.every(function (t) { return t.getBoundingClientRect().top < bar.bottom; }),
          stats: PMP.TopStats.stats()
        };
      });
      assert.deepStrictEqual(r.labels, ["days left", "today", "ready", "syllabus", "fitness", "ready by"]);
      assert.deepStrictEqual(r.links, ["#/planner", "#/today", "#/analytics", "#/learn", "#/fitness", "#/book"]);
      assert.ok(r.tinted >= 3 && r.oneRow, JSON.stringify(r));
      assert.strictEqual(typeof r.stats.readiness, "number");
      if (process.env.PMP_SHOTS) await page.screenshot({ path: process.env.PMP_SHOTS + "/top-1400.png", clip: { x: 0, y: 0, width: 1400, height: 70 } });
      await page.click("#np-toggle");
      await page.waitForTimeout(300);
      var narrow = await page.evaluate(function () {
        var bar = document.querySelector(".topbar");
        return { wide: document.documentElement.scrollWidth > window.innerWidth, h: bar.getBoundingClientRect().height };
      });
      assert.ok(!narrow.wide && narrow.h < 80, "with the notes pane open, tiles drop out rather than crowd: " + JSON.stringify(narrow));
      if (process.env.PMP_SHOTS) await page.screenshot({ path: process.env.PMP_SHOTS + "/top-notes.png", clip: { x: 0, y: 0, width: 1400, height: 70 } });
      await page.setViewportSize({ width: 390, height: 800 });
      await page.waitForTimeout(200);
      assert.strictEqual(await page.isVisible("#top-stats"), false, "not on a phone-width screen");
      await lib.clean(page);
      await page.ctx.close();
    }
  }
];
