"use strict";
/* Phase 0 baseline: every route renders for every fixture with no console
   errors and nothing reported through PMP.report. Screenshots at desktop and
   phone width are written to tests/screenshots/<dir>/ for review. */
var FIXTURES = ["fresh", "day3", "planned_ontrack", "planned_behind", "gap10",
                "examweek", "examday", "after", "weekly_due", "misread_heavy"];
var SHOT_FIXTURES = ["fresh", "day3"];
var SHOT_DIR = process.env.SHOT_DIR || "current";

module.exports = FIXTURES.map(function (fx) {
  return {
    name: "every route renders: " + fx,
    fn: async function (h, lib) {
      var page = await h.open(fx);
      var routes = await page.evaluate(function () { return PMP.Router.names; });
      for (var i = 0; i < routes.length; i++) {
        await lib.go(page, "#/" + routes[i]);
        var title = await page.evaluate(function () { return document.getElementById("view").textContent.length; });
        if (!title) throw new Error(routes[i] + " rendered nothing");
        await lib.clean(page, fx + " #/" + routes[i]);
        if (SHOT_FIXTURES.indexOf(fx) >= 0) await lib.shot(page, SHOT_DIR, fx + "-" + routes[i] + "-1400");
      }
      if (SHOT_FIXTURES.indexOf(fx) >= 0) {
        await page.setViewportSize({ width: 390, height: 844 });
        for (var k = 0; k < routes.length; k++) {
          await lib.go(page, "#/" + routes[k]);
          await lib.clean(page, fx + " #/" + routes[k] + " @390");
          var overflow = await page.evaluate(function () { return document.documentElement.scrollWidth > window.innerWidth + 1; });
          if (overflow) console.log("      note: horizontal overflow at 390px on #/" + routes[k]);
          await lib.shot(page, SHOT_DIR, fx + "-" + routes[k] + "-390");
        }
      }
      await page.ctx.close();
    }
  };
});
