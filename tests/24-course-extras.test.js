"use strict";
/* What the course material covered well, brought into the app: sample documents
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
    name: "plans match the phones: the newest shared diagnostic sets the domains; a finished lesson session counts its lesson",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      var r = await page.evaluate(function () {
        var P = PMP.Path, x = P.extras(), now = new Date(2027, 2, 1, 9).getTime();
        var st = { shared: { diagnostics: [
          { at: 1, n: 10, right: 5, domains: { people: [1, 4], process: [3, 4], business: [1, 2] } },
          { at: 2, n: 10, right: 6, domains: { people: [4, 4], process: [1, 4], business: [1, 2] } }] } };
        var f0 = x.lessons.filter(function (l) { return l.module === "foundations"; })[0].id;
        function keys(results) {
          return P.build({ x: x, attempts: [], lessonsDone: [], results: results, mocks: [],
            diagnostic: { people: 0.6, process: 0.5, business: 0.55 },
            settings: { examDate: now + 90 * 86400000, hoursPerWeek: 10, studyDays: [1, 2, 3, 4, 5, 6, 7] },
            now: now, mistakeTags: {}, leeches: {}, ladderNext: null, readingSlow: false }).sessions.map(function (s) { return s.key; });
        }
        var done = [{ key: "lesson:" + f0, kind: "FOUNDATION", taskId: null, domain: null, score: 1, minutes: 20, at: now - 86400000 }];
        return { latest: P.diagnosticOf(st),
                 before: keys([]).indexOf("lesson:" + f0) >= 0, after: keys(done).indexOf("lesson:" + f0) >= 0 };
      });
      assert.deepStrictEqual(r.latest, { people: 1, process: 0.25, business: 0.5 });
      assert.deepStrictEqual([r.before, r.after], [true, false]);
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
  },
  {
    name: "coach bubble: floats bottom right, opens the coach's messages with Gemini, Notes and Today; hides only in a running mock; drags",
    fn: async function (h, lib) {
      var page = await h.open("day3");
      await page.waitForTimeout(400);
      assert.strictEqual(await page.isVisible("#cb-btn"), true, "visible on Today");
      await page.click("#cb-btn");
      var p = await page.evaluate(function () {
        var panel = document.getElementById("cb-panel");
        return { open: !!panel, buttons: Array.prototype.map.call(panel.querySelectorAll(".cb-row:last-child button"), function (b) { return b.textContent; }),
                 label: document.getElementById("cb-btn").getAttribute("aria-label") };
      });
      assert.deepStrictEqual(p.buttons, ["Ask Gemini", "Notes", "Today"]);
      assert.ok(/^Coach/.test(p.label));
      await page.click('#cb-panel button:has-text("Ask Gemini")');
      await page.waitForTimeout(300);
      var g = await page.evaluate(function () {
        return { pane: PMP.Notepad.isOpen(), tab: document.getElementById("np-tab-ai").getAttribute("aria-selected"), hidden: document.getElementById("cb").hidden };
      });
      assert.deepStrictEqual(g, { pane: true, tab: "true", hidden: false }, "Gemini opens in the pane, and the bubble stays");
      var side = await page.evaluate(function () {
        return document.getElementById("cb-btn").getBoundingClientRect().right <= document.getElementById("np").getBoundingClientRect().left;
      });
      assert.ok(side, "beside the pane, not under it");
      await page.click("#np-toggle");
      await page.waitForTimeout(300);
      assert.strictEqual(await page.isVisible("#cb-btn"), true, "back when the pane closes");

      /* Dragged, it stays where it was put. */
      var box = await page.$eval("#cb-btn", function (b) { var r = b.getBoundingClientRect(); return { x: r.x + r.width / 2, y: r.y + r.height / 2 }; });
      await page.mouse.move(box.x, box.y);
      await page.mouse.down();
      await page.mouse.move(box.x - 200, box.y - 150, { steps: 6 });
      await page.mouse.up();
      var after = await page.evaluate(function () { return { pos: JSON.parse(localStorage.getItem("pmp.coachBubble")), panel: !!document.getElementById("cb-panel") }; });
      assert.ok(after.pos.x <= -190 && after.pos.y <= -140 && !after.panel, JSON.stringify(after));

      /* A question on screen: out of the way. */
      await lib.go(page, "#/practice");
      await page.click('#view button:has-text("Start drill")');
      /* Any question type (a diagram one has no .opt), so wait on the drill. */
      await page.waitForFunction(function () { return PMP.Practice.session && !PMP.Practice.session.finished && document.querySelector("#view .q-stem, #view .opt"); });
      await page.waitForTimeout(400);
      var q = await page.evaluate(function () { return { on: !!PMP.Practice.session, hidden: document.getElementById("cb").hidden }; });
      assert.ok(q.on && !q.hidden, "still there during practice: " + JSON.stringify(q));
      /* A mock exam in progress: the one place it steps away. */
      await page.click('#view button:has-text("End drill")').catch(function () {});
      await page.waitForTimeout(300);
      var endSheet = await page.$('#sheet-foot button:has-text("End")');
      if (endSheet) await endSheet.click();
      await lib.go(page, "#/exam");
      await page.evaluate(function () { PMP.Exam.start({ items: 20 }); });
      await page.waitForTimeout(300);
      var sheetBtn = await page.$('#sheet-foot button:has-text("Start")');
      if (sheetBtn) await sheetBtn.click();
      await page.waitForFunction(function () { return PMP.Exam.active; }, null, { timeout: 5000 });
      await page.waitForTimeout(400);
      assert.strictEqual(await page.evaluate(function () { return document.getElementById("cb").hidden; }), true, "hidden in a running mock");
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "flashcards: typing notes in the pane never reveals or grades a card; the shortcuts still work on the deck",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      await lib.go(page, "#/cards");
      await page.click('#view tr button');
      await page.waitForTimeout(200);
      await page.click("#np-toggle");
      await page.click("#np-tab-notes");
      await page.click("#np-text");
      await page.keyboard.type("Notes 1 2 3 4 ");
      await page.keyboard.press("Enter");
      var r = await page.evaluate(function () {
        return { revealed: PMP.Cards.session && PMP.Cards.session.revealed, done: PMP.Cards.session ? PMP.Cards.session.i : -1,
                 notes: document.getElementById("np-text").textContent };
      });
      assert.deepStrictEqual([r.revealed, r.done], [false, 0], "the card is untouched");
      assert.ok(r.notes.indexOf("Notes 1 2 3 4") >= 0, "the keys went to the notes");
      await page.click("#view-title");
      await page.keyboard.press(" ");
      assert.strictEqual(await page.evaluate(function () { return PMP.Cards.session.revealed; }), true, "space still reveals from the deck");
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "the wheel scrolls only the part under the pointer: the end of the notes, or a short note, never moves the page",
    fn: async function (h, lib) {
      var page = await h.open("day3");
      await lib.go(page, "#/library?tab=glossary");
      await page.click("#np-toggle");
      await page.click("#np-tab-notes");
      var lines = [];
      for (var i = 0; i < 80; i++) lines.push("<div>note line " + i + "</div>");
      await page.evaluate(function (html) { document.getElementById("np-text").innerHTML = html; }, lines.join(""));
      function pageTop() { return page.evaluate(function () { var v = document.scrollingElement; return v.scrollTop + window.scrollY; }); }
      var box = await page.$eval("#np-text", function (n) { var r = n.getBoundingClientRect(); return { x: r.x + r.width / 2, y: r.y + r.height / 2 }; });
      await page.mouse.move(box.x, box.y);
      var before = await pageTop();
      for (var k = 0; k < 12; k++) { await page.mouse.wheel(0, 600); await page.waitForTimeout(40); }
      var r = await page.evaluate(function () { var t = document.getElementById("np-text"); return t.scrollTop + t.clientHeight >= t.scrollHeight - 2; });
      assert.strictEqual(r, true, "the notes reached their end");
      assert.strictEqual(await pageTop(), before, "and the page behind did not move");

      await page.evaluate(function () { var t = document.getElementById("np-text"); t.innerHTML = "<div>short</div>"; });
      await page.mouse.wheel(0, 600);
      await page.waitForTimeout(100);
      assert.strictEqual(await pageTop(), before, "a short note does not pass the wheel on");

      /* Over the page itself, the page scrolls as ever. */
      var v = await page.$eval("#view", function (n) { var r = n.getBoundingClientRect(); return { x: r.x + 40, y: r.y + 200 }; });
      await page.mouse.move(v.x, v.y);
      await page.mouse.wheel(0, 600);
      await page.waitForTimeout(200);
      var moved = await page.evaluate(function () {
        var all = [document.scrollingElement].concat(Array.prototype.slice.call(document.querySelectorAll("main, #main, .main, #view, .content")));
        return all.some(function (n) { return n && n.scrollTop > 0; }) || window.scrollY > 0;
      });
      assert.strictEqual(moved, true);
      await lib.clean(page);
      await page.ctx.close();
    }
  }
];
