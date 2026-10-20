"use strict";
/* Phase 2: Today, intake, navigation, settings. */
var assert = require("assert");
var H = require("./helpers");
var FIXTURES = ["fresh", "day3", "planned_ontrack", "planned_behind", "gap10",
                "examweek", "examday", "after", "weekly_due", "misread_heavy"];

function ymd(offset) {
  var d = new Date(Date.now() + offset * 86400000);
  return d.getFullYear() + "-" + String(d.getMonth() + 1).padStart(2, "0") + "-" + String(d.getDate()).padStart(2, "0");
}

module.exports = [
  {
    name: "fresh opens on Today with the intake only; intake + diagnostic gives a plan-backed Today",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      var r = await page.evaluate(function () {
        return { route: PMP.Router.current.name, cards: document.querySelectorAll("#view .card").length,
                 steps: document.querySelectorAll(".td-step").length, msgs: document.querySelectorAll(".coach-msg").length,
                 h: document.querySelector("#view h2").textContent };
      });
      assert.deepStrictEqual(r, { route: "today", cards: 1, steps: 0, msgs: 0, h: "Set up your coach" });
      await H.clickText(page, "#view button", "Set up");
      await page.fill("#ik-date", ymd(60));
      await H.clickText(page, "#sheet-foot button", "Next");
      await H.clickText(page, "#sheet-foot button", "Next");
      await page.click('label[for="ik-agile-0"]');
      await H.clickText(page, "#sheet-foot button", "Next");
      await H.clickText(page, "#sheet-foot button", "Next");
      await H.clickText(page, "#sheet-foot button", "Diagnostic now");
      var d = await page.evaluate(function () {
        return { route: PMP.Router.current.name, mode: PMP.Practice.session && PMP.Practice.session.mode,
                 plan: PMP.Store.state.planner.days.length, agile: PMP.Store.state.coach.profile.agile,
                 date: PMP.Store.state.settings.examDate };
      });
      assert.strictEqual(d.route, "practice");
      assert.strictEqual(d.mode, "diagnostic");
      assert.ok(d.plan > 0);
      assert.strictEqual(d.agile, "new");
      assert.strictEqual(d.date, ymd(60));
      await H.finishDrill(page);
      /* The finished diagnostic is shared, so the phones plan from it too. */
      var row = await page.evaluate(function () {
        var rows = PMP.Store.state.shared.diagnostics || [], r = rows[rows.length - 1] || {};
        var n = 0;
        Object.keys(r.domains || {}).forEach(function (d) { n += r.domains[d][1]; });
        return { rows: rows.length, n: r.n, total: n, at: typeof r.at };
      });
      assert.ok(row.rows === 1 && row.n > 0 && row.total === row.n && row.at === "number", JSON.stringify(row));
      await lib.go(page, "#/today");
      /* With a date, Today is the guided path's home (§10.25): one card for
         the next planned session in place of the old step list. */
      var t = await page.evaluate(function () {
        var plan = PMP.Path.plan(PMP.Store.state);
        return { source: PMP.Coach.session(PMP.Store.state).source, main: !!document.querySelector(".pt-main"),
                 next: plan && plan.next && plan.next.title, title: document.querySelector("#pt-main-h").textContent,
                 diag: PMP.Planner.diagnostic(PMP.Store.state).complete };
      });
      assert.strictEqual(t.source, "plan");
      assert.ok(t.main && t.diag && t.next === t.title, JSON.stringify(t));
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "fresh, intake skipped: fallback session and a plan offer, no errors",
    fn: async function (h, lib) {
      for (var how = 0; how < 2; how++) {
        var page = await h.open("fresh");
        if (how === 0) await H.clickText(page, "#view button", "Skip for now");
        else { await H.clickText(page, "#view button", "Set up"); await page.keyboard.press("Escape"); await page.waitForTimeout(80); }
        var r = await page.evaluate(function () {
          return { stage: PMP.Coach.stage(PMP.Store.state), source: PMP.Coach.session(PMP.Store.state).source,
                   steps: document.querySelectorAll(".td-step").length,
                   msgs: Array.prototype.map.call(document.querySelectorAll(".coach-msg p"), function (p) { return p.textContent; }) };
        });
        assert.strictEqual(r.stage, "normal");
        assert.strictEqual(r.source, "fallback");
        assert.ok(r.steps > 0);
        assert.ok(r.msgs.some(function (m) { return /best guess, not a plan/.test(m); }), JSON.stringify(r.msgs));
        await lib.clean(page);
        await page.ctx.close();
      }
    }
  },
  {
    name: "planned_ontrack: Continue opens the session player on the path's next session; Close keeps the place",
    fn: async function (h, lib) {
      var page = await h.open("planned_ontrack");
      var next = await page.evaluate(function () { return PMP.Path.plan(PMP.Store.state).next.key; });
      await H.clickText(page, "#view .pt-main button", "Continue");
      var run = await page.evaluate(function () {
        return { route: PMP.Router.current.name, key: PMP.Session.run.s.key, h: document.querySelector("#view .ss-hd h2").textContent };
      });
      assert.strictEqual(run.route, "session");
      assert.strictEqual(run.key, next);
      await page.click('#view .ss-close');
      await page.waitForTimeout(80);
      var back = await page.evaluate(function () {
        return { route: PMP.Router.current.name, eyebrow: document.querySelector(".pt-main .eyebrow").textContent,
                 button: document.querySelector(".pt-main .btn--primary").textContent };
      });
      assert.deepStrictEqual(back, { route: "today", eyebrow: "In progress", button: "Resume" });
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "exactly one primary button on Today at every fixture",
    fn: async function (h, lib) {
      for (var i = 0; i < FIXTURES.length; i++) {
        var page = await h.open(FIXTURES[i]);
        var n = await page.evaluate(function () { return document.querySelectorAll("#view .btn--primary").length; });
        assert.strictEqual(n, 1, FIXTURES[i]);
        await lib.clean(page, FIXTURES[i]);
        await page.ctx.close();
      }
    }
  },
  {
    name: "mute hides messages and keeps the session",
    fn: async function (h, lib) {
      var page = await h.open("planned_behind");
      var a = await page.evaluate(function () { return document.querySelectorAll(".coach-msg").length; });
      assert.ok(a > 0);
      await lib.go(page, "#/settings");
      await page.selectOption("#set-coach-msgs", "off");
      await lib.go(page, "#/today");
      var b = await page.evaluate(function () { return { m: document.querySelectorAll(".coach-msg").length, s: document.querySelectorAll(".pt-main").length }; });
      assert.strictEqual(b.m, 0);
      assert.strictEqual(b.s, 1);
      await page.ctx.close();
    }
  },
  {
    name: "390px: no horizontal scroll, and the sticky button never covers the last step",
    fn: async function (h, lib) {
      for (var fx of ["day3", "planned_behind", "fresh"]) {
        var page = await h.open(fx, { viewport: { width: 390, height: 844 } });
        var r = await page.evaluate(function () {
          window.scrollTo(0, document.documentElement.scrollHeight);
          var steps = document.querySelectorAll(".td-step"), cta = document.querySelector(".td-cta");
          var last = steps.length ? steps[steps.length - 1].getBoundingClientRect() : null;
          return { over: document.documentElement.scrollWidth - window.innerWidth,
                   last: last && last.bottom, cta: cta && cta.getBoundingClientRect().top };
        });
        assert.ok(r.over <= 0, fx + " overflow " + r.over);
        if (r.cta !== null && r.last !== null) assert.ok(r.last <= r.cta + 0.5, fx + " " + JSON.stringify(r));
        await page.ctx.close();
      }
    }
  },
  {
    name: "keyboard: Tab to Continue and Enter starts the session; Escape closes the intake",
    fn: async function (h, lib) {
      var page = await h.open("planned_ontrack");
      var focusedView = await page.evaluate(function () { return document.activeElement === document.getElementById("view"); });
      assert.ok(focusedView, "focus is on the view after the route change");
      var found = false;
      for (var i = 0; i < 40; i++) {
        await page.keyboard.press("Tab");
        var t = await page.evaluate(function () { return document.activeElement.textContent.trim(); });
        if (t === "Continue") { found = true; break; }
      }
      assert.ok(found, "Continue is reachable by Tab");
      await page.keyboard.press("Enter");
      await page.waitForTimeout(100);
      var route = await page.evaluate(function () { return PMP.Router.current.name; });
      assert.notStrictEqual(route, "today");
      await page.ctx.close();

      var p2 = await h.open("fresh");
      await H.clickText(p2, "#view button", "Set up");
      var open = await p2.evaluate(function () { return document.getElementById("sheet").open; });
      assert.ok(open);
      await p2.keyboard.press("Escape");
      await p2.waitForTimeout(80);
      var closed = await p2.evaluate(function () { return !document.getElementById("sheet").open; });
      assert.ok(closed);
      await p2.ctx.close();
    }
  },
  {
    name: "#/dashboard deep links still work and show Progress",
    fn: async function (h, lib) {
      var page = await h.open("day3", { hash: "#/dashboard" });
      var r = await page.evaluate(function () {
        return { route: PMP.Router.current.name, title: document.getElementById("view-title").textContent,
                 nav: document.querySelector('.nav-i[data-route="dashboard"]').textContent.trim(),
                 first: document.querySelector(".nav-i").getAttribute("data-route") };
      });
      assert.deepStrictEqual(r, { route: "dashboard", title: "Progress", nav: "Progress", first: "today" });
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "redo intake from Settings, cancelled, writes nothing",
    fn: async function (h, lib) {
      var page = await h.open("day3", { hash: "#/settings" });
      var before = await page.evaluate(function () { return JSON.stringify(PMP.Store.state.coach); });
      await H.clickText(page, "#view button", "Redo intake");
      await H.clickText(page, "#sheet-foot button", "Cancel");
      var after = await page.evaluate(function () { return JSON.stringify(PMP.Store.state.coach); });
      assert.strictEqual(after, before);
      await lib.clean(page);
      await page.ctx.close();
    }
  }
];
