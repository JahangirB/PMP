"use strict";
/* The Abbreviations tab (§05.7.3, §10.07.10) and its self-check (§10.07.9).
   The two negative tests run the check against scratch copies of index.html
   with one deliberate fault each, written to the system temp directory. */
var assert = require("assert");
var fs = require("fs");
var os = require("os");
var path = require("path");
var cp = require("child_process");

var SRC = path.join(__dirname, "..", "index.html");
/* The last commit before the Abbreviations tab, for the unchanged-tabs check. */
var PRE_ABBR = "195fb6b";

function scratch(name, edit) {
  var html = fs.readFileSync(SRC, "utf8");
  var out = edit(html);
  assert.notStrictEqual(out, html, name + ": the edit changed nothing");
  var file = path.join(os.tmpdir(), "pmp-abbr-" + name + ".html");
  fs.writeFileSync(file, out);
  return "file://" + file;
}

async function checkOf(h, file) {
  var page = await h.open("fresh", { file: file });
  var r = await page.evaluate(function () { return PMP.Library.check(); });
  await page.ctx.close();
  return r;
}

async function rows(page) {
  return page.evaluate(function () {
    return Array.prototype.map.call(document.querySelectorAll(".lib-ab tbody tr:not(.lib-ab-g)"), function (tr) {
      return { abbr: tr.querySelector("th").textContent, full: tr.querySelector(".lib-ab-f").textContent };
    });
  });
}

async function search(page, q) {
  await page.fill("#lib-ab-q", q);
  await page.waitForTimeout(30);
}

module.exports = [
  {
    name: "self-check passes at boot and carries the five assertions",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      var r = await page.evaluate(function () { return PMP.Library.check(); });
      assert.deepStrictEqual(r.problems, []);
      await lib.clean(page, "boot");
      await page.ctx.close();
      var src = fs.readFileSync(SRC, "utf8");
      var body = src.slice(src.indexOf("function checkAbbreviations(bad)"), src.indexOf("function check() {"));
      ["/* 1. Shape, and 2. links resolve. */", "/* 3. Letters with more than one meaning",
       "/* 4. Glossary agreement", "/* 5. Coverage."].forEach(function (m) {
        assert.ok(body.indexOf(m) >= 0, "missing assertion: " + m);
      });
      assert.ok(src.indexOf("checkAbbreviations(bad);") > src.indexOf("function check() {"), "check() calls the abbreviation assertions");
    }
  },
  {
    name: "an unlisted abbreviation in a question stem fails the check by name",
    fn: async function (h) {
      var file = scratch("moscow", function (html) {
        var at = html.indexOf("PMP.QUESTIONS = [");
        var stem = html.indexOf('stem: "', at) + 'stem: "'.length;
        return html.slice(0, stem) + "Using MoSCoW, " + html.slice(stem);
      });
      var r = await checkOf(h, file);
      assert.strictEqual(r.pass, false);
      assert.ok(r.problems.some(function (p) { return /missing from .*MoSCoW/.test(p); }), r.problems.join(" | "));
    }
  },
  {
    name: "a clash row without its note fails the clash rule",
    fn: async function (h) {
      var file = scratch("pvnote", function (html) {
        return html.replace(/,\r?\n    note: "Also present value in the formula lab's finance group\." \}/, " }");
      });
      var r = await checkOf(h, file);
      assert.strictEqual(r.pass, false);
      assert.ok(r.problems.some(function (p) { return /^Abbreviation PV has 2 meanings and the row for Planned value has no note/.test(p); }), r.problems.join(" | "));
    }
  },
  {
    name: "the tab renders, eight tabs in the strip, callout lists the clashes",
    fn: async function (h, lib) {
      var page = await h.open("fresh", { hash: "#/library?tab=abbr" });
      await lib.clean(page, "#/library?tab=abbr");
      var r = await page.evaluate(function () {
        return {
          tabs: Array.prototype.map.call(document.querySelectorAll(".fx-tab"), function (t) { return t.textContent; }),
          selected: document.querySelector('.fx-tab[aria-selected="true"]').textContent,
          head: document.querySelector(".lib-sec h3").textContent,
          rows: document.querySelectorAll(".lib-ab tbody tr:not(.lib-ab-g)").length,
          caption: !!document.querySelector(".lib-ab caption"),
          cols: Array.prototype.map.call(document.querySelectorAll(".lib-ab thead th"), function (t) { return t.getAttribute("scope"); }),
          clash: Array.prototype.map.call(document.querySelectorAll(".lib-ab-clash li .lib-ab-k"), function (e) { return e.textContent; })
        };
      });
      assert.deepStrictEqual(r.tabs, ["Process view", "Area tours", "Principles", "Agile", "Tables", "Documents", "Glossary", "Abbreviations"]);
      assert.strictEqual(r.selected, "Abbreviations");
      assert.strictEqual(r.head, "Abbreviations");
      assert.strictEqual(r.rows, await page.evaluate(function () { return PMP.ABBREVIATIONS.length; }));
      assert.ok(r.caption);
      assert.deepStrictEqual(r.cols, ["col", "col", "col"]);
      ["PV", "CP", "TF", "FF"].forEach(function (k) { assert.ok(r.clash.indexOf(k) >= 0, k + " in " + r.clash); });
      await page.ctx.close();
    }
  },
  {
    name: "search ranks exact letters, then prefix, then expansion",
    fn: async function (h, lib) {
      var page = await h.open("fresh", { hash: "#/library?tab=abbr" });
      await search(page, "pv");
      assert.deepStrictEqual(await rows(page), [{ abbr: "PV", full: "Planned value" }, { abbr: "PV", full: "Present value" }]);
      var clash = await page.evaluate(function () { return !!document.querySelector(".lib-ab-clash"); });
      assert.ok(clash, "callout stays while the search hits a clash");
      await search(page, "ceil");
      assert.deepStrictEqual(await rows(page), [{ abbr: "CP", full: "Ceiling price" }]);
      await search(page, "t&m");
      assert.deepStrictEqual(await rows(page), [{ abbr: "T&M", full: "Time and materials" }]);
      await search(page, "kpi");
      clash = await page.evaluate(function () { return !!document.querySelector(".lib-ab-clash"); });
      assert.ok(!clash, "callout hides when the search hits no clash");
      /* q on entry, like the glossary. */
      await lib.go(page, "#/formulas");
      await lib.go(page, "#/library?tab=abbr&q=evm");
      var first = (await rows(page))[0];
      assert.deepStrictEqual(first, { abbr: "EVM", full: "Earned value management" });
      await lib.clean(page, "search");
      await page.ctx.close();
    }
  },
  {
    name: "every Definition link opens the glossary with that term selected",
    fn: async function (h, lib) {
      var page = await h.open("fresh", { hash: "#/library?tab=glossary" });
      /* A leftover glossary search must not hide the target. */
      await page.fill("#lib-q", "zzz");
      await page.waitForTimeout(150);
      await lib.go(page, "#/library?tab=abbr");
      var links = await page.evaluate(function () {
        return Array.prototype.map.call(document.querySelectorAll(".lib-ab-l a"), function (a) { return a.getAttribute("href"); });
      });
      var want = await page.evaluate(function () {
        return PMP.ABBREVIATIONS.filter(function (r) { return r.see; }).length;
      });
      assert.strictEqual(links.length, want);
      var bad = [];
      for (var i = 0; i < links.length; i++) {
        await lib.go(page, links[i]);
        var got = await page.evaluate(function () {
          var p = document.querySelector('.lib-row[aria-pressed="true"] .lib-row-t');
          return { h: (document.querySelector(".lib-def-h h3") || {}).textContent, row: p ? p.firstChild.textContent : null };
        });
        var term = decodeURIComponent(links[i].split("sel=")[1]);
        if (got.h !== term || got.row !== term) bad.push(term + " → " + JSON.stringify(got));
        await lib.go(page, "#/library?tab=abbr");
      }
      assert.deepStrictEqual(bad, []);
      await lib.clean(page, "definition links");
      await page.ctx.close();
    }
  },
  {
    name: "no page-level horizontal scroll at 390 and 320; the table scrolls in its box",
    fn: async function (h, lib) {
      for (var w of [390, 320]) {
        var page = await h.open("fresh", { hash: "#/library?tab=abbr", viewport: { width: w, height: 800 } });
        var r = await page.evaluate(function () {
          var tw = document.querySelector(".lib-ab").parentNode;
          return {
            page: document.documentElement.scrollWidth - window.innerWidth,
            wrapScrolls: tw.scrollWidth > tw.clientWidth,
            overflowX: getComputedStyle(tw).overflowX
          };
        });
        assert.ok(r.page <= 1, w + "px page overflow " + r.page);
        assert.strictEqual(r.overflowX, "auto");
        if (w === 320) assert.ok(r.wrapScrolls, "at 320px the table scrolls inside its wrapper");
        await lib.shot(page, "abbr", "abbr-" + w);
        await page.ctx.close();
      }
    }
  },
  {
    name: "keyboard reaches the tab, search, chips and links with a visible ring",
    fn: async function (h, lib) {
      var page = await h.open("fresh", { hash: "#/library?tab=abbr" });
      await page.focus('.fx-tab[aria-selected="true"]');
      await page.keyboard.press("Shift+Tab");
      await page.keyboard.press("Tab");
      var seen = {}, ringless = [];
      for (var i = 0; i < 40; i++) {
        var a = await page.evaluate(function () {
          var e = document.activeElement;
          var k = e.classList.contains("fx-tab") ? "tab" : e.id === "lib-ab-q" ? "search"
            : e.classList.contains("lib-fchip") ? "chip" : e.closest && e.closest(".lib-ab-l") ? "link" : "other";
          return { k: k, ring: getComputedStyle(e).boxShadow !== "none" || getComputedStyle(e).outlineStyle !== "none" };
        });
        seen[a.k] = true;
        if (a.k !== "other" && !a.ring) ringless.push(a.k);
        if (seen.link) break;
        await page.keyboard.press("Tab");
      }
      assert.ok(seen.tab && seen.search && seen.chip && seen.link, JSON.stringify(seen));
      assert.deepStrictEqual(ringless, []);
      /* Chips operate from the keyboard. */
      await page.focus('.lib-fchip[data-g="notation"]');
      await page.keyboard.press("Enter");
      var groups = await page.evaluate(function () {
        return Array.prototype.map.call(document.querySelectorAll(".lib-ab-g th"), function (e) { return e.textContent; });
      });
      assert.deepStrictEqual(groups, ["Formula symbols"]);
      await page.ctx.close();
    }
  },
  {
    name: "print shows the whole table and no navigation",
    fn: async function (h, lib) {
      var page = await h.open("fresh", { hash: "#/library?tab=abbr" });
      await search(page, "pv");
      await page.evaluate(function () { window.dispatchEvent(new Event("beforeprint")); });
      await page.emulateMedia({ media: "print" });
      var r = await page.evaluate(function () {
        function shown(sel) { var e = document.querySelector(sel); return !!e && getComputedStyle(e).display !== "none"; }
        return {
          rows: document.querySelectorAll(".lib-ab tbody tr:not(.lib-ab-g)").length,
          rail: shown(".rail"), tabs: shown(".fx-tabs"), tools: shown(".lib-tools"), chips: shown(".lib-chips"), links: shown(".lib-ab-l")
        };
      });
      var total = await page.evaluate(function () { return PMP.ABBREVIATIONS.length; });
      assert.deepStrictEqual(r, { rows: total, rail: false, tabs: false, tools: false, chips: false, links: false });
      await lib.shot(page, "abbr", "abbr-print");
      await page.emulateMedia({ media: "screen" });
      await page.evaluate(function () { window.dispatchEvent(new Event("afterprint")); });
      assert.strictEqual((await rows(page)).length, 2, "the search is back after printing");
      await page.ctx.close();
    }
  },
  {
    name: "the other tabs render as they did before this change",
    fn: async function (h, lib) {
      var ref = path.join(os.tmpdir(), "pmp-ref-" + PRE_ABBR + ".html");
      fs.writeFileSync(ref, cp.execSync("git show " + PRE_ABBR + ":index.html", { cwd: path.join(__dirname, ".."), maxBuffer: 64 << 20 }));
      var a = await h.open("fresh", { file: "file://" + ref });
      var b = await h.open("fresh");
      var diff = [];
      /* Agile and Tables now spell abbreviations out on first use (§09.5a),
         on purpose; 09-learning-abbr checks that text instead. The glossary
         gained the lecture-theory terms (L6), also on purpose. */
      for (var t of ["process", "principles"]) {
        await lib.go(a, "#/library?tab=" + t);
        await lib.go(b, "#/library?tab=" + t);
        /* Content and structure, not pixels: the Sky Clay restyle changed
           every pixel on purpose (see lib.structure). */
        var pa = await a.evaluate(lib.structure, '#view [role="tabpanel"]');
        var pb = await b.evaluate(lib.structure, '#view [role="tabpanel"]');
        if (pa.join("\n") !== pb.join("\n")) diff.push(t);
      }
      await a.ctx.close();
      await b.ctx.close();
      assert.deepStrictEqual(diff, []);
    }
  },
  {
    name: "content audit reports pack abbreviations as information",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      var r = await page.evaluate(function () {
        var q = JSON.parse(JSON.stringify(PMP.QUESTIONS[0]));
        q.id = "abbr-test-1";
        q.stem = "The PMO asks for a MoSCoW pass and an OKR review. " + q.stem;
        PMP.Content.add([q]);
        PMP.Router.navigate("audit");
        return null;
      });
      await page.waitForTimeout(100);
      var text = await page.evaluate(function () {
        var c = Array.prototype.filter.call(document.querySelectorAll("#view .card"), function (e) {
          var b = e.querySelector(".eyebrow"); return b && b.textContent === "Abbreviations";
        })[0];
        return c ? c.textContent : null;
      });
      assert.strictEqual(text, "Abbreviations2 abbreviations in imported or extended packs are not in the list: MoSCoW, OKR.");
      var check = await page.evaluate(function () { return PMP.Library.check().pass; });
      assert.ok(check, "an imported pack never fails the boot check");
      await page.ctx.close();
    }
  }
];
