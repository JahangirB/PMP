"use strict";
/* The Theory reading room (§10.37, §05.12): the parts and chapters, the
   reader, read marks and place kept on this device, and search. The data
   is generated from theory/*.txt by theory/build.mjs. */
var assert = require("assert");
var path = require("path");
var childProcess = require("child_process");

module.exports = [
  {
    name: "index.html carries the chapters exactly as theory/build.mjs makes them",
    fn: async function () {
      var out = childProcess.execSync("node " + path.join(__dirname, "..", "theory", "build.mjs") + " --check").toString();
      assert.ok(/up to date/.test(out), out);
    }
  },
  {
    name: "contents: six parts, every planned chapter listed, the written ones linked with their reading time",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      await lib.go(page, "#/theory");
      var r = await page.evaluate(function () {
        return { parts: document.querySelectorAll("#view .th-part").length, items: document.querySelectorAll("#view .th-li").length,
                 links: Array.prototype.map.call(document.querySelectorAll("#view a.th-li-t"), function (a) { return a.getAttribute("href"); }),
                 todo: document.querySelectorAll("#view .th-li--todo").length, nav: !!document.querySelector('.nav-i[data-route="theory"]'),
                 labs: [!!document.querySelector('.nav-i[data-route="theory"] .labs'), !!document.querySelector("#view .view-hd .labs")],
                 wide: document.documentElement.scrollWidth > document.documentElement.clientWidth };
      });
      var planned = await page.evaluate(function () { return PMP.THEORY.parts.reduce(function (n, p) { return n + p.planned.length; }, 0); });
      var written = await page.evaluate(function () { return PMP.THEORY.chapters.length; });
      assert.strictEqual(r.parts, 6);
      assert.strictEqual(r.items, planned);
      assert.strictEqual(r.links.length, written);
      assert.strictEqual(r.todo, planned - written);
      assert.ok(r.links[0] === "#/theory?ch=t1" && r.nav && !r.wide, JSON.stringify(r));
      assert.deepStrictEqual(r.labs, [true, true], "marked Labs in the menu and on the page");
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "a chapter reads in full: sections, figures, boxes, abbreviations spelled out once, key ideas mark it read, next chapter",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      await lib.go(page, "#/theory?ch=t2");
      var r = await page.evaluate(function () {
        var c = PMP.THEORY.chapters.filter(function (x) { return x.id === "t2"; })[0];
        var text = document.getElementById("th-art").textContent;
        return { secs: document.querySelectorAll("#th-art .th-sec[id^='t2-s']").length, want: c.sections.length,
                 toc: document.querySelectorAll(".th-toc-b").length, figs: document.querySelectorAll("#th-art figure svg").length,
                 boxes: document.querySelectorAll("#th-art .th-box").length,
                 pmiFull: text.indexOf("Project Management Institute (PMI)"), pmiTwice: text.split("Project Management Institute (PMI)").length - 1,
                 stars: /\*\*/.test(text), eyebrow: document.querySelector("#th-art .eyebrow").textContent,
                 next: (document.querySelector(".th-foot a.btn--primary") || {}).getAttribute("href"), read: PMP.Theory.isRead("t2") };
      });
      assert.strictEqual(r.secs, r.want);
      assert.strictEqual(r.toc, r.want + 1, "every section plus Key ideas");
      assert.ok(r.figs >= 1 && r.boxes >= 2, JSON.stringify(r));
      assert.ok(r.pmiFull > 0 && r.pmiTwice === 1, "PMI spelled out on first use only");
      assert.strictEqual(r.stars, false, "markup rendered, not shown");
      assert.strictEqual(r.eyebrow, "Part I · Chapter 2 · " + (await page.evaluate(function () { return PMP.Theory.chapter("t2").minutes; })) + " min read");
      assert.strictEqual(r.next, "#/theory?ch=t3");
      assert.strictEqual(r.read, false);
      await page.evaluate(function () { document.getElementById("t2-key").scrollIntoView(); });
      await page.waitForTimeout(400);
      assert.strictEqual(await page.evaluate(function () { return PMP.Theory.isRead("t2"); }), true, "reaching the key ideas marks it read");
      assert.strictEqual(await page.textContent("#th-read"), "Read ✓");
      await page.click("#th-read");
      assert.strictEqual(await page.evaluate(function () { return PMP.Theory.isRead("t2"); }), false, "and the button takes it back");

      /* The place is kept: Continue on the contents goes back to it. */
      await page.evaluate(function () { document.getElementById("t2-s4").scrollIntoView(); });
      await page.waitForTimeout(300);
      await lib.go(page, "#/theory");
      var cont = await page.$eval(".th-intro a.btn--primary", function (a) { return a.getAttribute("href"); });
      assert.ok(/^#\/theory\?ch=t2&sec=t2-s[3-5]$/.test(cont), cont);
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "search finds a theory section and opens the chapter at it; a phone gets one column",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      await lib.go(page, "#/search");
      await page.fill("#search-q", "sunk cost fallacy");
      await page.waitForTimeout(400);
      var hit = await page.evaluate(function () {
        var li = Array.prototype.filter.call(document.querySelectorAll("#search-results .sr-hit"), function (x) { return x.getAttribute("data-kind") === "Theory"; })[0];
        return li ? li.querySelector("a").getAttribute("href") : null;
      });
      assert.strictEqual(hit, "#/theory?ch=t4&sec=t4-s2");
      await lib.go(page, hit);
      await page.waitForTimeout(300);
      var top = await page.evaluate(function () { return Math.round(document.getElementById("t4-s2").getBoundingClientRect().top); });
      assert.ok(top >= 0 && top < 200, "scrolled to the section: " + top);
      await page.setViewportSize({ width: 390, height: 800 });
      var one = await page.evaluate(function () {
        return { cols: getComputedStyle(document.querySelector(".th")).gridTemplateColumns.split(" ").length,
                 wide: document.documentElement.scrollWidth > document.documentElement.clientWidth };
      });
      assert.deepStrictEqual(one, { cols: 1, wide: false });
      await lib.clean(page);
      await page.ctx.close();
    }
  }
];
