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
    name: "content: F0 opens the path, the walk-throughs pair each step with a check, the confused pairs ship",
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
          first: PMP.LESSONS.slice().sort(function (a, b) { return a.order - b.order; })[0].id,
          walks: walks,
          deck: PMP.FLASHCARDS.filter(function (c) { return c.deck === "confused"; }).length,
          tables: PMP.REFERENCE.tables.filter(function (t) { return /^t-confused-/.test(t.id); }).length,
          theory: ["t-mastery", "t-five-cs", "t-ethics", "t-culture", "t-quality-stats"].filter(function (id) {
            return PMP.REFERENCE.tables.some(function (t) { return t.id === id; });
          }).length
        };
      });
      assert.strictEqual(r.first, "F0");
      r.walks.forEach(function (w) {
        var m = /(\w+):(\d+)\/(\d+)/.exec(w);
        assert.ok(+m[2] >= 5 && m[2] === m[3], w);
      });
      assert.ok(r.deck >= 30, "confused deck");
      assert.strictEqual(r.tables, 3);
      assert.strictEqual(r.theory, 5);
    }
  }
];
