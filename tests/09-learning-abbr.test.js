"use strict";
/* §09.5a: abbreviations spelled out on first use in learning text (cards,
   lessons, the formula sheet, the library), and never in practice. */
var assert = require("assert");
var H = require("./helpers");

/* Shows one card through the real renderer: start its deck, swap the card
   in at the head of the queue, reveal. */
async function showCard(h, id) {
  var page = await h.open("fresh");
  var r = await page.evaluate(function (id) {
    var card = PMP.FLASHCARDS.filter(function (c) { return c.id === id; })[0];
    var label = PMP.DECKS.filter(function (d) { return d.id === card.deck; })[0].label;
    location.hash = "#/cards";
    return new Promise(function (res) { setTimeout(res, 120); }).then(function () {
      var btn = Array.prototype.filter.call(document.querySelectorAll("#view tr button"), function (b) {
        return b.closest("tr").textContent.indexOf(label) >= 0;
      })[0];
      btn.click();
      PMP.Cards.session.queue[PMP.Cards.session.i] = card;
      Array.prototype.filter.call(document.querySelectorAll("#view button"), function (b) {
        return b.textContent.indexOf("Show answer") === 0;
      })[0].click();
      var q = function (s) { var e = document.querySelector(s); return e ? e.textContent : null; };
      return { front: q(".cardface-front"), back: q(".cardface-back p"), note: q(".cardface-note p") };
    });
  }, id);
  await require("./lib").clean(page);
  await page.ctx.close();
  return r;
}

module.exports = [
  {
    name: "cards: first use spelled out once, formulas left whole with a key",
    fn: async function (h, lib) {
      var c = await showCard(h, "evm-09");
      assert.strictEqual(c.front, "Estimate at completion (EAC) when current cost performance is expected to continue");
      assert.ok(c.back.indexOf("EAC = BAC / CPI (BAC: budget at completion; CPI: cost performance index).") === 0, c.back);
      c = await showCard(h, "evm-03");
      assert.ok(c.back.indexOf("the work that earned value (EV) measures") > 0, c.back);
      assert.ok(c.note.indexOf("cause of a cost performance index (CPI) that") > 0, c.note);
      assert.ok(c.note.indexOf("AC must cover the same work as EV") === 0, "AC is defined on the front, EV on the back");
      c = await showCard(h, "evm-16");
      assert.strictEqual(c.front, "To-complete performance index (TCPI) to finish within a revised budget (estimate at completion, EAC)");
      /* RACI is what the card asks; spelling it out would give the answer away. */
      c = await showCard(h, "gloss-17");
      assert.strictEqual(c.front, "RACI chart");
    }
  },
  {
    name: "every learning string: nothing doubled, articles agree",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      var bad = await page.evaluate(function () {
        var A = PMP.Abbr, out = [];
        function scan(where, parts, ctx) {
          var u = A.unit(parts, ctx);
          parts.forEach(function (t) {
            if (!t) return;
            var x = u.x(t);
            PMP.ABBREVIATIONS.forEach(function (r) {
              var f = r.full.toLowerCase();
              if (x.toLowerCase().indexOf(f + " (" + f) >= 0) out.push(where + ": doubled " + r.abbr);
            });
            if (/\b[Aa]n [bcdfgjklmnpqrstvwxyz][a-z]+ [a-z ]+\([A-Z]/.test(x) && x !== t) out.push(where + ": " + x);
          });
        }
        PMP.FLASHCARDS.forEach(function (c) { scan(c.id, [c.front, c.back, c.note], { groups: A.groups([c.deck].concat(c.tags || [])) }); });
        PMP.LESSONS.forEach(function (l) { l.sections.forEach(function (s) { scan(l.id + s.id, [s.body], {}); }); });
        PMP.GLOSSARY.forEach(function (e) { scan(e.t, [e.d], { groups: [e.g], known: [e.a, e.t] }); });
        return out;
      });
      assert.deepStrictEqual(bad, []);
      await page.ctx.close();
    }
  },
  {
    name: "lessons, the sheet and the library spell out; ambiguous letters are left",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      await lib.go(page, "#/learn?lesson=B1");
      var recap = await page.evaluate(function () { return document.querySelectorAll(".ln-part")[8].textContent; });
      assert.ok(recap.indexOf("Know which type of project management office (PMO) you work with.") >= 0, recap);
      await lib.go(page, "#/formulas");
      var sheet = await page.textContent("#view");
      assert.ok(sheet.indexOf("Write earned value (EV) down first") >= 0);
      assert.ok(sheet.indexOf("CV = EV − AC") >= 0, "the expression itself is untouched");
      await lib.go(page, "#/library?tab=tables");
      var tables = await page.textContent("#view");
      assert.ok(tables.indexOf("Enterprise environmental factors (EEF), internal") >= 0);
      var r = await page.evaluate(function () {
        return PMP.Abbr.unit(["supporting FS, SS, FF and SF links."], { groups: ["schedule"] }).x("supporting FS, SS, FF and SF links.");
      });
      assert.strictEqual(r, "supporting finish-to-start (FS), start-to-start (SS), FF and start-to-finish (SF) links.");
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "practice, mock exam, scenarios, journal and the formula drill never call it",
    fn: async function (h, lib) {
      var page = await h.open("day3");
      await page.evaluate(function () {
        window.__abbr = 0;
        var f = PMP.Abbr.unit;
        PMP.Abbr.unit = function () { window.__abbr++; return f.apply(this, arguments); };
        PMP.Practice.startWith(PMP.QUESTIONS.filter(function (q) { return /\b(EV|CPI|WBS|PMO|RFP)\b/.test(q.stem); }).slice(0, 4), "practice");
        PMP.Router.navigate("practice");
      });
      await page.waitForTimeout(200);
      await H.finishDrill(page);
      for (var r of ["#/scenarios", "#/journal", "#/formulas?tab=drill"]) await lib.go(page, r);
      await lib.go(page, "#/exam");
      await H.clickText(page, "#view button", "Start a sitting");
      await H.clickText(page, "#sheet-foot button", "Start the clock");
      await page.waitForTimeout(200);
      assert.strictEqual(await page.evaluate(function () { return window.__abbr; }), 0);
      await page.ctx.close();
    }
  }
];
