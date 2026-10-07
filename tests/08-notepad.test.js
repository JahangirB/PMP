"use strict";
/* The notes pane (§12.2.1): a top-bar toggle on every route, a right-hand
   pane that stays until the toggle is pressed again, and text and open state
   that survive a refresh. */
var assert = require("assert");

async function paneState(page) {
  return page.evaluate(function () {
    var pane = document.getElementById("np");
    var r = pane.getBoundingClientRect();
    return {
      attr: document.documentElement.getAttribute("data-notepad"),
      visible: getComputedStyle(pane).display !== "none" && r.width > 0,
      left: r.left, right: r.right,
      expanded: document.getElementById("np-toggle").getAttribute("aria-expanded"),
      text: document.getElementById("np-text").innerText,
      stored: PMP.Store.state.notepad
    };
  });
}

module.exports = [
  {
    name: "toggle is on every route and starts closed",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      var routes = ["today", "learn", "practice", "scenarios", "cards", "exam", "dashboard", "planner", "journal", "analytics", "formulas", "library", "settings", "data"];
      for (var r of routes) {
        await lib.go(page, "#/" + r);
        var ok = await page.evaluate(function () {
          var b = document.getElementById("np-toggle").getBoundingClientRect();
          return b.width > 0 && b.right > window.innerWidth - 120;
        });
        assert.ok(ok, "toggle not visible at the right on #/" + r);
      }
      var s = await paneState(page);
      assert.strictEqual(s.visible, false);
      assert.strictEqual(s.expanded, "false");
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "opens beside the view, stays open across routes, closes only on the toggle",
    fn: async function (h, lib) {
      var page = await h.open("day3");
      await page.click("#np-toggle");
      var s = await paneState(page);
      assert.strictEqual(s.visible, true);
      assert.strictEqual(s.expanded, "true");
      assert.ok(Math.abs(s.right - 1400) < 2, "pane is on the right edge");
      var mainRight = await page.evaluate(function () { return document.querySelector(".main").getBoundingClientRect().right; });
      assert.ok(mainRight <= s.left + 1, "pane sits beside the content, not over it");
      assert.strictEqual(await page.evaluate(function () { return document.activeElement.id; }), "np-text");
      for (var r of ["practice", "formulas", "library", "today"]) {
        await lib.go(page, "#/" + r);
        assert.strictEqual((await paneState(page)).visible, true, "closed on #/" + r);
      }
      await page.click("#np-toggle");
      s = await paneState(page);
      assert.strictEqual(s.visible, false);
      await page.click("#np-toggle");
      await page.click("#np-close");
      assert.strictEqual((await paneState(page)).visible, false);
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "text and open state survive a refresh",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      await page.click("#np-toggle");
      await page.type("#np-text", "EV = EMV\nCPI < 1 over budget");
      await page.waitForTimeout(700);
      await page.reload();
      await page.waitForFunction(function () { return window.PMP && PMP.Store.state.sys.ready; });
      /* open on first paint, before hydrate has run */
      var s = await paneState(page);
      assert.strictEqual(s.visible, true);
      assert.strictEqual(s.text, "EV = EMV\nCPI < 1 over budget");
      assert.strictEqual(s.stored.open, true);
      var stat = await page.textContent("#np-stat");
      assert.ok(/8 words · saved/.test(stat), stat);

      await page.click("#np-toggle");
      await page.waitForTimeout(700);
      await page.reload();
      await page.waitForFunction(function () { return window.PMP && PMP.Store.state.sys.ready; });
      s = await paneState(page);
      assert.strictEqual(s.visible, false);
      /* A hidden element has no layout, so its innerText loses line breaks;
         the saved plain-text mirror is what to check while closed. */
      assert.strictEqual(s.stored.text, "EV = EMV\nCPI < 1 over budget");
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "clear asks first; notes travel in a progress export",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      await page.click("#np-toggle");
      await page.type("#np-text", "keep me");
      var exported = await page.evaluate(function () { return PMP.Store.snapshot().notepad; });
      assert.strictEqual(exported.text, "keep me");
      await page.click("#np-clear");
      await page.waitForSelector("#sheet[open]");
      await page.click('#sheet-foot button:has-text("Cancel")');
      assert.strictEqual((await paneState(page)).text, "keep me");
      await page.click("#np-clear");
      await page.waitForSelector("#sheet[open]");
      await page.click('#sheet-foot button:has-text("Clear notes")');
      await page.waitForTimeout(100);
      var s = await paneState(page);
      assert.strictEqual(s.text, "");
      assert.strictEqual(s.stored.text, "");
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "phone width: overlay with no page-level horizontal scroll",
    fn: async function (h, lib) {
      for (var w of [390, 320]) {
        var page = await h.open("fresh", { viewport: { width: w, height: 800 } });
        await page.click("#np-toggle");
        var s = await paneState(page);
        assert.strictEqual(s.visible, true);
        assert.ok(s.right <= w + 1, "pane overflows at " + w);
        var sw = await page.evaluate(function () { return document.documentElement.scrollWidth; });
        assert.ok(sw <= w, "horizontal scroll at " + w + ": " + sw);
        await page.click("#np-close");
        assert.strictEqual((await paneState(page)).visible, false);
        await lib.shot(page, "notepad", "closed-" + w);
        await lib.clean(page);
        await page.ctx.close();
      }
    }
  },
  {
    name: "pane stays put while the page scrolls, like the rail",
    fn: async function (h, lib) {
      for (var vp of [{ width: 1400, height: 900 }, { width: 390, height: 800 }]) {
        var page = await h.open("day3", { viewport: vp, hash: "#/formulas" });
        await page.click("#np-toggle");
        var before = await page.evaluate(function () { return document.getElementById("np").getBoundingClientRect().top; });
        await page.evaluate(function () { window.scrollTo(0, 2500); });
        await page.waitForTimeout(100);
        var after = await page.evaluate(function () {
          var r = document.getElementById("np").getBoundingClientRect();
          return { y: window.scrollY, top: r.top, bottom: r.bottom, h: window.innerHeight };
        });
        assert.ok(after.y > 1000, "page did not scroll at " + vp.width);
        assert.strictEqual(after.top, before, "pane moved with the page at " + vp.width);
        assert.ok(Math.abs(after.bottom - after.h) < 2, "pane does not fill the viewport height at " + vp.width);
        await lib.clean(page);
        await page.ctx.close();
      }
    }
  }
];
