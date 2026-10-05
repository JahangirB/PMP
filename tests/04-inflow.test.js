"use strict";
/* Phase 3: in-flow coaching — hint ladder, the hinted exclusion, the
   wrong-answer lesson link, the strip and the debrief. */
var assert = require("assert");
var H = require("./helpers");

/* Starts a drill of the given mode over the given question ids straight
   through the engine, the way Learn and Planner do. */
async function startDrill(page, mode, n, filter) {
  await page.evaluate(function (a) {
    var qs = PMP.QUESTIONS.filter(a.filter ? new Function("q", "return " + a.filter) : function () { return true; }).slice(0, a.n);
    PMP.Practice.startWith(qs, a.mode);
    PMP.Router.navigate("practice");
  }, { mode: mode, n: n, filter: filter || null });
  await page.waitForTimeout(80);
}

async function hintButton(page) {
  return page.evaluate(function () {
    var b = Array.prototype.filter.call(document.querySelectorAll("#drill-card .q-hints .btn"), function (x) { return !x.hidden; })[0];
    return !!b;
  });
}

module.exports = [
  {
    name: "Hint is offered in practice and adaptive only",
    fn: async function (h, lib) {
      var page = await h.open("day3");
      var got = {};
      for (var mode of ["practice", "adaptive", "timed", "diagnostic", "lesson"]) {
        await startDrill(page, mode, 3);
        got[mode] = await hintButton(page);
      }
      assert.deepStrictEqual(got, { practice: true, adaptive: true, timed: false, diagnostic: false, lesson: false });
      /* Lesson quick checks render in the reader, not the engine. */
      await lib.go(page, "#/learn?lesson=P4");
      var ln = await page.evaluate(function () { return document.querySelectorAll(".ln-check .q-hints").length; });
      assert.strictEqual(ln, 0);
      /* Mock exam and scenario ranking have no hint control at all. */
      await lib.go(page, "#/scenarios");
      var sc = await page.evaluate(function () { return document.querySelectorAll(".q-hints").length; });
      assert.strictEqual(sc, 0);
      var src = require("fs").readFileSync(require("path").join(__dirname, "..", "index.html"), "utf8");
      var exam = src.slice(src.indexOf("/* ── 10.03  MOCK EXAM"), src.indexOf("/* ── 10.08  SCENARIO"));
      assert.ok(exam.length > 1000, "found the mock exam section");
      assert.ok(!/Coach\.hint|hintTools|CoachUI/.test(exam), "mock exam has no hint ladder or coach");
      /* Turning hints off in Settings removes the button. */
      await page.evaluate(function () { PMP.Coach.setPref("hints", false); });
      await startDrill(page, "practice", 3);
      assert.strictEqual(await hintButton(page), false);
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "no rung ever reveals the answer; rung 3 strikes an incorrect, unselected option",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      var r = await page.evaluate(function () {
        var bad = [], n = 0;
        PMP.QUESTIONS.forEach(function (q) {
          var p = PMP.QRender.present(q, { shuffle: true });
          if (p.type !== "single" && p.type !== "multi") return;
          var correct = p.options.filter(function (o) { return o.correct; }).map(function (o) { return o.id; });
          /* With nothing chosen, and with a wrong option chosen. */
          var wrong = p.options.filter(function (o) { return !o.correct; }).map(function (o) { return o.id; });
          [null, p.type === "multi" ? [wrong[0]] : wrong[0]].forEach(function (chosen) {
            for (var rung = 1; rung <= 3; rung++) {
              var hh = PMP.Coach.hint(q, rung, p, chosen);
              n++;
              if (hh.strike) {
                if (correct.indexOf(hh.strike) >= 0) bad.push(q.id + " struck the correct option");
                var sel = Array.isArray(chosen) ? chosen : chosen ? [chosen] : [];
                if (sel.indexOf(hh.strike) >= 0) bad.push(q.id + " struck the chosen option");
              }
              if (rung === 3 && !hh.strike) bad.push(q.id + " rung 3 struck nothing");
              /* No rung text quotes a correct option. */
              p.options.filter(function (o) { return o.correct; }).forEach(function (o) {
                if (o.text && o.text.length > 12 && ((hh.text || "") + (hh.detail || "")).indexOf(o.text) >= 0) bad.push(q.id + " quotes the answer");
              });
            }
          });
        });
        return { bad: bad, n: n };
      });
      assert.deepStrictEqual(r.bad, []);
      assert.ok(r.n > 300, "checked " + r.n);
      await page.ctx.close();
    }
  },
  {
    name: "rungs show in the drill, the struck row is marked, and nothing is marked correct before commit",
    fn: async function (h, lib) {
      var page = await h.open("day3");
      await startDrill(page, "practice", 2, "q.type === 'single'");
      for (var i = 0; i < 3; i++) await H.clickText(page, "#drill-card .q-hints .btn", i ? "Another hint" : "Hint");
      var r = await page.evaluate(function () {
        var item = PMP.Practice.session.items[PMP.Practice.session.i];
        var struck = document.querySelector('#drill-card .opt[data-struck="true"]');
        var id = struck && struck.getAttribute("data-opt");
        var o = item.p.options.filter(function (x) { return x.id === id; })[0];
        return { hints: item.hints, rungs: document.querySelectorAll("#drill-card .q-hint").length,
                 struckCorrect: o ? o.correct : null, marked: document.querySelectorAll("#drill-card [data-mark]").length,
                 disabled: document.querySelector("#drill-card .q-hints .btn").disabled };
      });
      assert.deepStrictEqual(r, { hints: 3, rungs: 3, struckCorrect: false, marked: 0, disabled: true });
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "a hinted attempt is recorded with hinted >= 1; accuracy and pace ignore it, coverage counts it",
    fn: async function (h, lib) {
      var page = await h.open("day3");
      var before = await page.evaluate(function () {
        var s = PMP.Store.state;
        return { acc: PMP.Metrics.accuracy(s), pace: PMP.Metrics.pace(s).n, cov: PMP.Metrics.coverage(s), n: s.attempts.length };
      });
      /* A question on a task day3 has never touched, so coverage must move. */
      var task = await page.evaluate(function () {
        var by = PMP.Metrics.byTask(PMP.Store.state);
        return PMP.ECO_TASKS.filter(function (t) { return !by[t.id]; })[0].id;
      });
      await startDrill(page, "practice", 1, "q.ecoTask === '" + task + "' && q.type === 'single'");
      await H.clickText(page, "#drill-card .q-hints .btn", "Hint");
      await page.waitForTimeout(600);
      await H.answerOne(page);
      var after = await page.evaluate(function () {
        var s = PMP.Store.state;
        return { acc: PMP.Metrics.accuracy(s), pace: PMP.Metrics.pace(s).n, cov: PMP.Metrics.coverage(s), n: s.attempts.length,
                 last: s.attempts[s.attempts.length - 1] };
      });
      assert.strictEqual(after.n, before.n + 1);
      assert.ok(after.last.hinted >= 1, JSON.stringify(after.last));
      assert.strictEqual(after.acc.n, before.acc.n);
      assert.ok(Math.abs(after.acc.raw - before.acc.raw) < 1e-12);
      assert.strictEqual(after.pace, before.pace);
      assert.strictEqual(after.cov.touched, before.cov.touched + 1);
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "a wrong answer links back to the lesson's concept sections",
    fn: async function (h, lib) {
      var page = await h.open("day3");
      await startDrill(page, "practice", 1, "q.type === 'single' && PMP.Learn.forTask(q.ecoTask)");
      await page.evaluate(function () {
        var item = PMP.Practice.session.items[0];
        var wrong = item.p.options.filter(function (o) { return !o.correct; })[0];
        var row = Array.prototype.filter.call(document.querySelectorAll("#drill-card .opt"), function (r) { return r.getAttribute("data-opt") === wrong.id; })[0];
        row.querySelector("input").click();
        document.querySelector("#drill-card .btn--primary").click();
      });
      var link = await page.evaluate(function () {
        var a = document.querySelector("#drill-card .ln-links a");
        return a && { href: a.getAttribute("href"), text: a.textContent };
      });
      assert.ok(link && /part=3/.test(link.href) && /covers this\. Re-read the concept/.test(link.text), JSON.stringify(link));
      await page.ctx.close();
    }
  },
  {
    name: "the strip appears only after a start from Today, and its Next opens the next undone step",
    fn: async function (h, lib) {
      var page = await h.open("planned_ontrack");
      /* Not from Today: no strip on the summary. */
      await startDrill(page, "practice", 2);
      await H.finishDrill(page);
      var none = await page.evaluate(function () { return document.querySelectorAll(".coach-strip").length; });
      assert.strictEqual(none, 0);
      /* From Today: start the practice step and finish it. */
      await lib.go(page, "#/today");
      await page.evaluate(function () {
        var s = PMP.Coach.session(PMP.Store.state).steps.filter(function (x) { return x.kind === "practice"; })[0];
        PMP.CoachUI.startStep(s);
      });
      await page.waitForTimeout(100);
      await H.finishDrill(page);
      var r = await page.evaluate(function () {
        var strip = document.querySelector(".coach-strip");
        var lis = strip ? strip.querySelectorAll(".coach-debrief li") : [];
        return { has: !!strip, lines: lis.length, text: strip && strip.textContent,
                 next: PMP.Coach.session(PMP.Store.state).next };
      });
      assert.ok(r.has, "strip shown");
      assert.strictEqual(r.lines, 3);
      assert.ok(r.next, "a next step exists");
      await H.clickText(page, ".coach-strip button", "Next:");
      var where = await page.evaluate(function () { return { route: PMP.Router.current.name, params: PMP.Router.current.params }; });
      assert.strictEqual(where.route, r.next.route, JSON.stringify(where));
      if (r.next.params && r.next.params.lesson) assert.strictEqual(where.params.lesson, r.next.params.lesson);
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "the debrief introduces no new number",
    fn: async function (h, lib) {
      var page = await h.open("day3");
      await page.evaluate(function () {
        PMP.CoachUI.startStep({ id: "t", route: "practice", params: { n: 8 } });
      });
      await page.waitForTimeout(100);
      await H.finishDrill(page);
      var r = await page.evaluate(function () {
        var strip = document.querySelector(".coach-debrief");
        var lines = Array.prototype.map.call(strip.querySelectorAll("li"), function (li) { return li.textContent; });
        /* Every figure the summary itself prints, plus the target and the
           median §10.00 reports. */
        var summary = document.getElementById("view").textContent.replace(strip.textContent, "");
        var allowed = (summary.match(/\d+/g) || []).concat([String(PMP.PRACTICE.PACE_TARGET_SEC)]);
        var nums = [];
        lines.slice(0, 2).forEach(function (l) { (l.match(/\d+/g) || []).forEach(function (x) { nums.push(x); }); });
        return { lines: lines, stray: nums.filter(function (x) { return allowed.indexOf(x) < 0; }) };
      });
      assert.deepStrictEqual(r.stray, [], JSON.stringify(r.lines));
      await page.ctx.close();
    }
  },
  {
    name: "strip on cards, scenarios and the lesson reader only from Today; muted hides it",
    fn: async function (h, lib) {
      var page = await h.open("planned_ontrack");
      await lib.go(page, "#/learn?lesson=F1");
      assert.strictEqual(await page.evaluate(function () { return document.querySelectorAll(".coach-strip").length; }), 0);
      await lib.go(page, "#/today");
      await page.evaluate(function () {
        var s = PMP.Coach.session(PMP.Store.state).steps.filter(function (x) { return x.kind === "lesson"; })[0];
        PMP.CoachUI.startStep(s);
      });
      await page.waitForTimeout(100);
      var why = await page.evaluate(function () { var s = document.querySelector(".coach-strip"); return s && s.textContent; });
      assert.ok(why && /today's plan/.test(why), why);
      await page.evaluate(function () { PMP.Coach.setPref("muted", true); });
      await lib.go(page, "#/learn?lesson=P4");
      await lib.go(page, "#/learn?lesson=F1");
      assert.strictEqual(await page.evaluate(function () { return document.querySelectorAll(".coach-strip").length; }), 0);
      await lib.clean(page);
      await page.ctx.close();
    }
  }
];
