"use strict";
/* Before you start (§10.38, §05.13): six screens, one at a time, shown once
   to a new starter before the setup, with the menu hidden; reopened from
   Settings and About. */
var assert = require("assert");

async function next(page) { await page.click("#wl-next"); await page.waitForTimeout(40); }

module.exports = [
  {
    name: "a new starter reads six screens with Next and Back, then the setup opens; it is not shown again",
    fn: async function (h, lib) {
      var page = await h.open("fresh", { welcome: true });
      await page.waitForTimeout(100);
      var r = await page.evaluate(function () {
        return { route: PMP.Router.current.name, rail: getComputedStyle(document.getElementById("rail")).display,
                 warn: document.querySelector(".wl-warn strong").textContent, count: document.getElementById("wl-count").textContent,
                 back: document.getElementById("wl-back").style.visibility, items: document.querySelectorAll(".wl-ahead li").length };
      });
      assert.deepStrictEqual(r, { route: "welcome", rail: "none", warn: "Please read this carefully before you start",
                                  count: "1 of 6", back: "hidden", items: 5 });
      await next(page);
      var two = await page.evaluate(function () {
        return { h: document.getElementById("wl-h").textContent, main: !!document.querySelector(".wl-card--main"),
                 focus: document.activeElement.id, count: document.getElementById("wl-count").textContent };
      });
      assert.deepStrictEqual(two, { h: "Start with a fixed exam date", main: true, focus: "wl-h", count: "2 of 6" });
      await page.click("#wl-back");
      assert.strictEqual(await page.textContent("#wl-count"), "1 of 6");
      for (var i = 0; i < 5; i++) await next(page);
      var last = await page.evaluate(function () {
        return { count: document.getElementById("wl-count").textContent, btn: document.getElementById("wl-next").textContent,
                 again: document.querySelector(".wl-again").textContent,
                 wide: document.documentElement.scrollWidth > document.documentElement.clientWidth };
      });
      assert.strictEqual(last.count, "6 of 6");
      assert.strictEqual(last.btn, "I've read this · Set up my plan");
      assert.ok(/Settings and About/.test(last.again) && !last.wide, JSON.stringify(last));
      await next(page);
      await page.waitForTimeout(200);
      var after = await page.evaluate(function () {
        return { route: PMP.Router.current.name, rail: getComputedStyle(document.getElementById("rail")).display !== "none",
                 sheet: /Set up your coach/.test(document.getElementById("sheet").textContent), read: PMP.Welcome.isRead() };
      });
      assert.deepStrictEqual(after, { route: "today", rail: true, sheet: true, read: true });
      await page.keyboard.press("Escape");
      await page.reload();
      await page.waitForFunction(function () { return window.PMP && PMP.Store.state.sys.ready; });
      await page.waitForTimeout(150);
      assert.strictEqual(await page.evaluate(function () { return PMP.Router.current.name; }), "today", "read once per device");
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "someone already studying with a date is not sent there; Settings reopens it and it ends on Settings",
    fn: async function (h, lib) {
      var page = await h.open("planned_ontrack", { welcome: true });
      await page.waitForTimeout(100);
      assert.strictEqual(await page.evaluate(function () { return PMP.Router.current.name; }), "today");
      await lib.go(page, "#/settings");
      await page.click('#view a[href="#/welcome"]');
      await page.waitForTimeout(100);
      for (var i = 0; i < 5; i++) await next(page);
      assert.strictEqual(await page.textContent("#wl-next"), "I've read this");
      await page.setViewportSize({ width: 390, height: 760 });
      var phone = await page.evaluate(function () {
        var ft = document.querySelector(".wl-ft").getBoundingClientRect();
        return { wide: document.documentElement.scrollWidth > document.documentElement.clientWidth, foot: ft.bottom <= window.innerHeight + 1 };
      });
      assert.deepStrictEqual(phone, { wide: false, foot: true });
      await next(page);
      await page.waitForTimeout(100);
      assert.strictEqual(await page.evaluate(function () { return PMP.Router.current.name + ":" + document.documentElement.hasAttribute("data-welcome"); }), "settings:false");
      await lib.clean(page);
      await page.ctx.close();
    }
  }
];
