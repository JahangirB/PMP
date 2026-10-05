"use strict";
/* Phase 0b: the four small UI fixes. */
var assert = require("assert");

module.exports = [
  {
    name: "no eyebrow on any route shows a section code",
    fn: async function (h, lib) {
      var page = await h.open("day3");
      var routes = await page.evaluate(function () { return PMP.Router.names; });
      for (var i = 0; i < routes.length; i++) {
        await lib.go(page, "#/" + routes[i]);
        var bad = await page.evaluate(function () {
          return Array.prototype.map.call(document.querySelectorAll(".eyebrow"), function (e) { return e.textContent; })
            .filter(function (t) { return t.indexOf("§") >= 0; });
        });
        assert.deepStrictEqual(bad, [], "#/" + routes[i]);
      }
      await page.ctx.close();
    }
  },
  {
    name: "practice mode title and description sit on separate lines",
    fn: async function (h, lib) {
      var page = await h.open("fresh", { hash: "#/practice" });
      var r = await page.evaluate(function () {
        var o = document.querySelector(".mode-opt");
        var t = o.querySelector(".t").getBoundingClientRect(), d = o.querySelector(".d").getBoundingClientRect();
        return { tBottom: t.bottom, dTop: d.top };
      });
      assert.ok(r.dTop >= r.tBottom, JSON.stringify(r));
      await page.ctx.close();
    }
  },
  {
    name: "route change focuses the view without a ring; controls keep theirs",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      /* Keyboard first, so :focus-visible heuristics are in keyboard mode. */
      await page.keyboard.press("Tab");
      await lib.go(page, "#/practice");
      var v = await page.evaluate(function () {
        var view = document.getElementById("view");
        return { focused: document.activeElement === view, shadow: getComputedStyle(view).boxShadow };
      });
      assert.ok(v.focused, "view should hold focus after a route change");
      assert.strictEqual(v.shadow, "none");
      await page.keyboard.press("Tab");
      var c = await page.evaluate(function () {
        var a = document.activeElement;
        return { tag: a.tagName, shadow: getComputedStyle(a).boxShadow };
      });
      assert.notStrictEqual(c.shadow, "none", "a tabbed-to control shows its ring: " + JSON.stringify(c));
      await page.ctx.close();
    }
  },
  {
    name: "nav is regrouped and the audit is reached from Settings",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      var r = await page.evaluate(function () {
        return {
          groups: Array.prototype.map.call(document.querySelectorAll(".nav-grp > .eyebrow"), function (e) { return e.textContent; }),
          audit: !!document.querySelector('.nav-i[data-route="audit"]')
        };
      });
      assert.deepStrictEqual(r.groups, ["Learn", "Progress", "Reference"]);
      assert.strictEqual(r.audit, false);
      await lib.go(page, "#/settings");
      var link = await page.evaluate(function () { return !!document.querySelector('#view a[href="#/audit"]'); });
      assert.ok(link, "Settings links to the content audit");
      await page.ctx.close();
    }
  }
];
