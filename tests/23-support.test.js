"use strict";
/* The Support button, the collapsible side menu (M2) and the weekly support
   reminder (§10.30, docs/progress-format.md §4): the pure rule over
   synthetic histories, the shared slices through §10.18 both ways and in the
   merge, the sheet, and the rail at desktop and phone widths. */
var assert = require("assert");

var KOFI = "https://ko-fi.com/T4R728ULEV";

/* Runs PMP.Support.due over a history of open dates given as day offsets
   from a fixed local noon (0 = today), so every case reads as a calendar. */
async function rule(page, cases) {
  return page.evaluate(function (cases) {
    var D = 86400000, now = new Date(2026, 9, 19, 12, 0, 0).getTime();
    function day(n) { return PMP.Support.ymd(new Date(2026, 9, 19 + n, 12).getTime()); }
    return cases.map(function (c) {
      var sup = {};
      if (c.pressed != null) sup.pressedAt = now - c.pressed * D;
      if (c.shown != null) sup.shownAt = now - c.shown * D;
      if (c.anchor != null) sup.anchor = day(c.anchor);
      return PMP.Support.due({ opens: c.opens.map(day), support: sup, ui: c.ui || {} }, now);
    });
  }, cases);
}
function range(a, b) { var out = []; for (var i = a; i <= b; i++) out.push(i); return out; }

module.exports = [
  {
    name: "due(): 7 days in a row, restarts after showing, a gap resets, at most once a day",
    fn: async function (h) {
      var page = await h.open("fresh");
      var r = await rule(page, [
        { opens: [] },                                         /* nothing yet */
        { opens: range(-5, 0) },                               /* 6 days: not yet */
        { opens: range(-6, 0) },                               /* day 7 */
        { opens: range(-6, -1) },                              /* not opened today */
        { opens: range(-9, 0), shown: 3, anchor: -2 },         /* shown 3 days ago: count restarted, 3 so far */
        { opens: range(-9, 0), shown: 7, anchor: -6 },         /* 7 days since the anchor */
        { opens: [-9, -8, -7, -5, -4, -3, -2, -1, 0] },        /* gap at -6: 6 */
        { opens: [-9, -8, -7, -6, -5, -4, -3, -2, 0] },        /* gap yesterday: 1 */
        { opens: range(-6, 0), shown: 0 },                     /* already shown today */
        { opens: range(-20, 0), shown: 0, anchor: -20 }        /* likewise, whatever the streak */
      ]);
      assert.deepStrictEqual(r, [null, null, "first", null, null, "first", null, null, null, null]);
      await page.ctx.close();
    }
  },
  {
    name: "due(): pressed is silent for 30 days, then the 'already' message once; never during a mock or session",
    fn: async function (h) {
      var page = await h.open("fresh");
      var r = await rule(page, [
        { opens: range(-20, 0), pressed: 10 },                         /* pressed 10 days ago */
        { opens: range(-40, 0), pressed: 29 },                         /* still inside 30 days */
        { opens: [0], pressed: 31 },                                   /* 30 days on: already, streak or not */
        { opens: [0], pressed: 31, shown: 40 },                        /* shown before the press: still owed */
        { opens: [0], pressed: 40, shown: 5, anchor: -4 },             /* owed one shown: silent again */
        { opens: range(-6, 0), pressed: 60, shown: 20, anchor: -19 },  /* then the weekly rule, as "already" */
        { opens: range(-6, 0), ui: { examActive: true } },
        { opens: range(-6, 0), ui: { route: "exam" } },
        { opens: range(-6, 0), ui: { route: "session" } },
        { opens: range(-6, 0), ui: { route: "today" } }
      ]);
      assert.deepStrictEqual(r, [null, null, "already", "already", null, "already", null, null, null, "first"]);
      await page.ctx.close();
    }
  },
  {
    name: "opens and support are shared slices: toDoc/fromDoc lossless, merged as a set and a higher-wins map",
    fn: async function (h) {
      var page = await h.open("fresh");
      var r = await page.evaluate(function () {
        var S = PMP.Sync;
        var st = PMP.Store.defaults();
        st.opens = ["2026-10-02", "2026-10-01"];
        st.support = { pressedAt: 5, shownAt: 7, anchor: "2026-10-03" };
        /* A phone's copy an older build kept in the passthrough is folded in. */
        st.shared = { opens: ["2026-09-30"], support: { pressedAt: 1 } };
        var doc = S.toDoc(st, { savedAt: 1, device: { app: "web", id: "t" } });
        var back = S.fromDoc(doc, st);
        var again = S.toDoc(Object.assign(PMP.Store.defaults(), back), { savedAt: 1, device: { app: "web", id: "t" } });
        var many = []; for (var i = 1; i <= 70; i++) many.push("2026-08-" + (i < 10 ? "0" : "") + i);
        function d(o, s) { return Object.assign({ type: "pmp-progress", version: 3, savedAt: s || 1, device: { app: "x", id: "" } }, o); }
        var base = d({ opens: ["2026-10-01", "2026-10-02"], support: { pressedAt: 10, shownAt: 20, anchor: "2026-10-05" } });
        var local = d({ opens: ["2026-10-02", "2026-10-03"], support: { pressedAt: 30, shownAt: 15, anchor: "2026-10-09" } }, 9);
        var remote = d({ opens: ["2026-10-01", "2026-10-02", "2026-10-04"], support: { pressedAt: 25, shownAt: 40, anchor: "2026-10-12" } }, 1);
        var m = S.merge(base, local, remote);
        var one = S.merge(base, d({ opens: base.opens, support: base.support }, 9), d({ opens: base.opens, support: { pressedAt: 5, shownAt: 20, anchor: "2026-10-05" } }, 1));
        return {
          opens: doc.opens, support: doc.support, sharedKept: "opens" in back.shared || "support" in back.shared,
          backOpens: back.opens, backSupport: back.support, same: S.sameDoc(doc, again),
          capped: S.opensOf(many).length, first: S.opensOf(many)[0],
          mOpens: m.opens, mSupport: m.support, oneSide: one.support
        };
      });
      assert.deepStrictEqual(r.opens, ["2026-09-30", "2026-10-01", "2026-10-02"]);
      assert.deepStrictEqual(r.support, { pressedAt: 5, shownAt: 7, anchor: "2026-10-03" });
      assert.strictEqual(r.sharedKept, false, "real slices, not the passthrough");
      assert.deepStrictEqual(r.backOpens, r.opens);
      assert.deepStrictEqual(r.backSupport, r.support);
      assert.ok(r.same, "round trip is lossless");
      assert.strictEqual(r.capped, 60);
      assert.strictEqual(r.first, "2026-08-11", "the newest 60 are kept");
      /* 10-01 removed locally, 10-03 added locally, 10-04 added remotely. */
      assert.deepStrictEqual(r.mOpens, ["2026-10-02", "2026-10-03", "2026-10-04"]);
      /* Both changed every key: the higher value, whichever side saved last. */
      assert.deepStrictEqual(r.mSupport, { anchor: "2026-10-12", pressedAt: 30, shownAt: 40 });
      /* Only one side changed: that side, even when it is lower. */
      assert.deepStrictEqual(r.oneSide, { anchor: "2026-10-05", pressedAt: 5, shownAt: 20 });
      await page.ctx.close();
    }
  },
  {
    name: "cross-device opens count toward the streak once merged",
    fn: async function (h) {
      var page = await h.open("fresh");
      var r = await page.evaluate(function () {
        var now = new Date(2026, 9, 19, 12).getTime();
        function day(n) { return PMP.Support.ymd(new Date(2026, 9, 19 + n, 12).getTime()); }
        function d(o, s) { return Object.assign({ type: "pmp-progress", version: 3, savedAt: s, device: { app: "x", id: "" } }, o); }
        var web = d({ opens: [day(-6), day(-5), day(-4), day(0)] }, 2);
        var phone = d({ opens: [day(-3), day(-2), day(-1)] }, 1);
        var alone = PMP.Support.due({ opens: web.opens, support: {}, ui: {} }, now);
        var m = PMP.Sync.merge(null, web, phone);
        var st = PMP.Sync.fromDoc(m, PMP.Store.state);
        return { alone: alone, merged: PMP.Support.due({ opens: st.opens, support: st.support, ui: {} }, now) };
      });
      assert.deepStrictEqual(r, { alone: null, merged: "first" });
      await page.ctx.close();
    }
  },
  {
    name: "boot records today; the message shows in the sheet with the exact wording and records the press",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      await page.waitForTimeout(1300);
      var boot = await page.evaluate(function () {
        return { opens: PMP.Store.state.opens.slice(), today: PMP.Support.ymd(Date.now()), open: document.getElementById("sheet").open };
      });
      assert.deepStrictEqual(boot.opens, [boot.today]);
      assert.strictEqual(boot.open, false, "one day is not a streak");
      var r = await page.evaluate(function () {
        var days = [];
        for (var i = 6; i >= 0; i--) { var t = new Date(); t.setDate(t.getDate() - i); days.push(PMP.Support.ymd(t.getTime())); }
        PMP.Store.commit(["opens"], function (s) { s.opens = days; });
        var kind = PMP.Support.check();
        var foot = document.getElementById("sheet-foot");
        var link = foot.querySelector("a");
        var sup = PMP.Store.state.support;
        var tomorrow = new Date(); tomorrow.setDate(tomorrow.getDate() + 1);
        var out = {
          kind: kind, title: document.getElementById("sheet-title").textContent,
          body: document.getElementById("sheet-body").textContent,
          labels: Array.prototype.map.call(foot.children, function (b) { return b.textContent.trim(); }),
          href: link.getAttribute("href"), target: link.getAttribute("target"), rel: link.getAttribute("rel"),
          warm: link.classList.contains("btn--warm"),
          anchorOk: sup.anchor === PMP.Support.ymd(tomorrow.getTime()), shown: Date.now() - sup.shownAt < 5000,
          again: PMP.Support.due(PMP.Store.state, Date.now())
        };
        link.addEventListener("click", function (e) { e.preventDefault(); });   /* no new tab in the test */
        link.click();
        return out;
      });
      assert.strictEqual(r.kind, "first");
      assert.strictEqual(r.title, "Enjoying PMP Prep?");
      assert.strictEqual(r.body, "You've studied 7 days in a row. If the app is helping you, please support it. It's free, has no ads, and your support keeps it going.");
      assert.deepStrictEqual(r.labels, ["Not now", "Support the app"]);
      assert.deepStrictEqual([r.href, r.target, r.rel, r.warm], [KOFI, "_blank", "noopener", true]);
      assert.ok(r.anchorOk && r.shown, "showing sets shownAt and anchor = tomorrow");
      assert.strictEqual(r.again, null, "once a day");
      await page.waitForTimeout(50);
      var after = await page.evaluate(function () {
        return { open: document.getElementById("sheet").open, pressed: Date.now() - (PMP.Store.state.support.pressedAt || 0) < 5000 };
      });
      assert.deepStrictEqual(after, { open: false, pressed: true });
      /* The "already" wording. */
      var body = await page.evaluate(function () {
        PMP.Support.show("already");
        var t = document.getElementById("sheet-body").textContent;
        PMP.UI.closeSheet(null);
        return t;
      });
      assert.strictEqual(body, "If the app is helping you, please support it. It's free, has no ads, and your support keeps it going. If you have already supported, please ignore this message.");
      /* Never during a mock. */
      var mock = await page.evaluate(function () {
        PMP.Store.commit(["support", "ui"], function (s) { s.support = {}; s.ui.examActive = true; }, { transient: true });
        var k = PMP.Support.check();
        PMP.Store.commit(["ui"], function (s) { s.ui.examActive = false; }, { transient: true });
        return { k: k, open: document.getElementById("sheet").open };
      });
      assert.deepStrictEqual(mock, { k: null, open: false });
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "the Support pill tops the rail: a Ko-fi link, warm colours in both themes, records the press",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      var r = await page.evaluate(function () {
        var a = document.querySelector("#nav .nav-grp").firstElementChild;
        function look() {
          var cs = getComputedStyle(a);
          return [cs.backgroundColor, cs.borderTopColor, cs.color, cs.borderTopWidth];
        }
        document.documentElement.setAttribute("data-theme", "light");
        var light = look();
        document.documentElement.setAttribute("data-theme", "dark");
        var dark = look();
        a.addEventListener("click", function (e) { e.preventDefault(); });
        a.click();
        return {
          id: a.id, href: a.getAttribute("href"), target: a.target, rel: a.rel, text: a.textContent.trim(),
          heart: !!a.querySelector("use[href='#i-heart']"), sprite: !!document.getElementById("i-heart"),
          scripts: Array.prototype.filter.call(document.scripts, function (s) { return /ko-fi/.test(s.src); }).length,
          light: light, dark: dark, pressed: Date.now() - (PMP.Store.state.support.pressedAt || 0) < 5000
        };
      });
      assert.deepStrictEqual([r.id, r.href, r.target, r.rel, r.text], ["nav-support", KOFI, "_blank", "noopener", "Support the app"]);
      assert.ok(r.heart && r.sprite);
      assert.strictEqual(r.scripts, 0, "no Ko-fi script");
      assert.deepStrictEqual(r.light, ["rgb(251, 232, 214)", "rgb(160, 74, 8)", "rgb(110, 50, 5)", "1.5px"]);
      assert.deepStrictEqual(r.dark, ["rgb(58, 44, 14)", "rgb(251, 191, 36)", "rgb(252, 213, 138)", "1.5px"]);
      assert.ok(r.pressed);
      /* A visible focus ring from the keyboard. */
      await page.keyboard.press("Tab");
      await page.focus("#nav-support");
      var ring = await page.evaluate(function () {
        var cs = getComputedStyle(document.getElementById("nav-support"));
        return cs.boxShadow !== "none" || cs.outlineStyle !== "none";
      });
      assert.ok(ring, "focus ring");
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "the rail collapses to icons at 1366px, is remembered on this device only, and works with the notes pane",
    fn: async function (h, lib) {
      var page = await h.open("fresh", { viewport: { width: 1366, height: 800 } });
      var before = await page.evaluate(function () {
        var rail = document.getElementById("rail"), t = document.getElementById("rail-toggle");
        return { w: rail.getBoundingClientRect().width, radius: getComputedStyle(rail).borderTopRightRadius,
                 shown: getComputedStyle(t).display !== "none", exp: t.getAttribute("aria-expanded"),
                 ctl: t.getAttribute("aria-controls"), label: t.getAttribute("aria-label"),
                 beforeTitle: t.nextElementSibling.id, main: document.querySelector(".main").getBoundingClientRect().width };
      });
      assert.deepStrictEqual([before.w, before.radius, before.shown, before.exp, before.ctl, before.label, before.beforeTitle],
        [240, "0px", true, "true", "rail", "Collapse menu", "view-title"]);
      await page.focus("#rail-toggle");
      await page.keyboard.press("Enter");
      await page.waitForTimeout(80);
      var c = await page.evaluate(function () {
        var rail = document.getElementById("rail"), t = document.getElementById("rail-toggle");
        var today = document.querySelector('.nav-i[data-route="today"]'), lbl = today.querySelector(".nav-t");
        var icon = today.querySelector(".i").getBoundingClientRect(), r = today.getBoundingClientRect();
        return {
          w: rail.getBoundingClientRect().width, exp: t.getAttribute("aria-expanded"), label: t.getAttribute("aria-label"),
          stored: localStorage.getItem("pmp.railCollapsed"), title: today.getAttribute("title"),
          inDom: lbl.textContent, clipped: lbl.getBoundingClientRect().width <= 1,
          centred: Math.abs((icon.left + icon.width / 2) - (r.left + r.width / 2)) < 2,
          brandHidden: document.querySelector(".brand-txt").getBoundingClientRect().width <= 1,
          eyebrowHidden: document.querySelector(".nav-grp > .eyebrow").getBoundingClientRect().width <= 1,
          supTitle: document.getElementById("nav-support").getAttribute("title"),
          supW: document.getElementById("nav-support").getBoundingClientRect().width,
          exportTitle: document.getElementById("export-btn").getAttribute("title"),
          main: document.querySelector(".main").getBoundingClientRect().width,
          overflow: document.documentElement.scrollWidth > window.innerWidth,
          inDoc: JSON.stringify(PMP.Sync.toDoc(PMP.Store.state)).indexOf("railCollapsed") >= 0
        };
      });
      assert.strictEqual(c.w, 64);
      assert.deepStrictEqual([c.exp, c.label, c.stored], ["false", "Expand menu", "1"]);
      assert.deepStrictEqual([c.title, c.inDom, c.clipped, c.centred], ["Today", "Today", true, true]);
      assert.ok(c.brandHidden && c.eyebrowHidden);
      assert.strictEqual(c.supTitle, "Support the app");
      assert.ok(c.supW <= 60, "the pill is just its heart");
      assert.strictEqual(c.exportTitle, "Export");
      assert.ok(c.main >= before.main + 170, "main takes the freed width");
      assert.ok(!c.overflow, "no horizontal overflow");
      assert.ok(!c.inDoc, "device-only");
      /* The notes pane alongside a collapsed rail. */
      await page.click("#np-toggle");
      await page.waitForTimeout(120);
      var np = await page.evaluate(function () {
        return { open: getComputedStyle(document.getElementById("np")).display !== "none",
                 main: document.querySelector(".main").getBoundingClientRect().width,
                 overflow: document.documentElement.scrollWidth > window.innerWidth };
      });
      assert.ok(np.open && np.main > 300 && !np.overflow, JSON.stringify(np));
      await page.click("#np-toggle");
      /* Remembered across a reload, from the first paint. */
      await page.reload();
      await page.waitForFunction(function () { return window.PMP && PMP.Store.state.sys.ready; });
      var re = await page.evaluate(function () {
        return { attr: document.documentElement.getAttribute("data-rail"), w: document.getElementById("rail").getBoundingClientRect().width,
                 exp: document.getElementById("rail-toggle").getAttribute("aria-expanded") };
      });
      assert.deepStrictEqual(re, { attr: "collapsed", w: 64, exp: "false" });
      await page.click("#rail-toggle");
      var back = await page.evaluate(function () {
        return { w: document.getElementById("rail").getBoundingClientRect().width, title: document.querySelector(".nav-i").getAttribute("title"),
                 stored: localStorage.getItem("pmp.railCollapsed") };
      });
      assert.deepStrictEqual(back, { w: 240, title: null, stored: "" });
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "at 390px the toggle is hidden, the drawer works as before, and nothing overflows",
    fn: async function (h, lib) {
      var page = await h.open("fresh", { viewport: { width: 390, height: 844 } });
      await page.evaluate(function () { localStorage.setItem("pmp.railCollapsed", "1"); });
      await page.reload();
      await page.waitForFunction(function () { return window.PMP && PMP.Store.state.sys.ready; });
      var r = await page.evaluate(function () {
        return { toggle: getComputedStyle(document.getElementById("rail-toggle")).display,
                 burger: getComputedStyle(document.getElementById("nav-toggle")).display,
                 overflow: document.documentElement.scrollWidth > window.innerWidth };
      });
      assert.deepStrictEqual(r, { toggle: "none", burger: "grid", overflow: false });
      await page.click("#nav-toggle");
      await page.waitForTimeout(260);
      var d = await page.evaluate(function () {
        var rail = document.getElementById("rail");
        return { open: rail.getAttribute("data-open"), w: Math.round(rail.getBoundingClientRect().width),
                 label: document.querySelector('.nav-i[data-route="today"] .nav-t').getBoundingClientRect().width > 1,
                 overflow: document.documentElement.scrollWidth > window.innerWidth };
      });
      assert.deepStrictEqual(d, { open: "true", w: 240, label: true, overflow: false });
      await lib.clean(page);
      await page.ctx.close();
    }
  }
];
