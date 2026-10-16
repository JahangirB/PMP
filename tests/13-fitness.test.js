"use strict";
/* S4: exam fitness, ready to book and weekly reports (§10.19, §10.21), and
   the answer signals they are built from (§09.9). The formulas are the
   Android app's (data/ExamFitness.kt, Booking.kt, WeeklyReport.kt); the
   synthetic histories below are built so each figure can be worked out by
   hand from those formulas. */
var assert = require("assert");
var H = require("./helpers");

var DAY = 86400000;

/* In-page: a synthetic attempt `daysAgo` days back at minute `i`. */
var MAKE = function () {
  window.__mk = function (daysAgo, i, extra) {
    var q = PMP.QUESTIONS[i % PMP.QUESTIONS.length];
    var base = PMP.time.startOfDay(Date.now()) - daysAgo * 86400000 + 9 * 3600000;
    var a = { id: "att-t-" + daysAgo + "-" + i, qid: q.id, ecoTask: q.ecoTask, domain: q.domain, approach: q.approach,
              type: "single", difficulty: q.difficulty, correct: true, credit: 1, ms: 60000,
              at: new Date(base + i * 61000).toISOString(), confidence: null, mode: "practice", order: [] };
    Object.keys(extra || {}).forEach(function (k) { a[k] = extra[k]; });
    return a;
  };
};

module.exports = [
  {
    name: "practice answers carry reading time, idle, words and answer changes; the set is a sitting",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      var picked = await page.evaluate(function () {
        var qs = PMP.QUESTIONS.filter(function (q) { return q.type === "single"; }).slice(0, 2);
        PMP.Practice.startWith(qs, "practice");
        PMP.Router.navigate("practice");
        return qs.map(function (q) {
          return { right: q.options.filter(function (o) { return o.correct; })[0].id,
                   wrong: q.options.filter(function (o) { return !o.correct; })[0].id,
                   words: PMP.signals.words(q) };
        });
      });
      await page.waitForTimeout(150);
      /* First question: wrong, then right (one wrong→right), then check. */
      async function pick(id) {
        await page.evaluate(function (oid) {
          var row = document.querySelector('#drill-card .opt[data-opt="' + oid + '"]');
          row.querySelector("input").click();
        }, id);
        await page.waitForTimeout(80);
      }
      await page.waitForTimeout(1200);
      await pick(picked[0].wrong);
      await pick(picked[0].right);
      await page.click("#drill-card .btn--primary");      /* Check answer */
      await page.waitForTimeout(50);
      await H.finishDrill(page);
      var r = await page.evaluate(function () {
        var st = PMP.Store.state;
        return { a: st.attempts.map(function (x) { return { ft: x.ft, idle: x.idle, w: x.w, rw: x.rw, wr: x.wr }; }),
                 sit: st.sittings[st.sittings.length - 1],
                 doc: PMP.Sync.toDoc(st).attempts.map(function (x) { return { ft: x.ft, idle: x.idle, w: x.w, wr: x.wr }; }) };
      });
      assert.strictEqual(r.a.length, 2);
      assert.ok(r.a[0].ft >= 1000, "reading time recorded: " + JSON.stringify(r.a[0]));
      assert.ok(r.a[0].idle >= r.a[0].ft - 5, "the first stretch before any touch is idle too");
      assert.strictEqual(r.a[0].w, picked[0].words);
      assert.strictEqual(r.a[0].wr, 1);
      assert.strictEqual(r.a[0].rw, undefined, "only positive counts are written");
      assert.deepStrictEqual({ kind: r.sit.kind, p: r.sit.p, a: r.sit.a, f: r.sit.f }, { kind: "practice", p: 2, a: 2, f: true });
      assert.ok(/^practice:\d+$/.test(r.sit.k) && typeof r.sit.at === "number");
      assert.strictEqual(r.doc[0].ft, r.a[0].ft, "ft goes out under the shared name");
      assert.strictEqual(r.doc[0].wr, 1);
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "a set ended early with questions unanswered is logged as left; replacing it too",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      await page.evaluate(function () {
        PMP.Practice.startWith(PMP.QUESTIONS.slice(0, 5), "practice");
        PMP.Router.navigate("practice");
      });
      await page.waitForTimeout(100);
      await H.answerOne(page);
      await page.evaluate(function () {
        /* Starting another set part-way through leaves this one. */
        PMP.Practice.startWith(PMP.QUESTIONS.slice(5, 8), "practice");
      });
      await page.waitForTimeout(100);
      await H.answerOne(page);
      await page.keyboard.press("Escape");
      await H.clickText(page, "#sheet-foot button", "End drill");
      var s = await page.evaluate(function () { return PMP.Store.state.sittings.map(function (e) { return [e.kind, e.p, e.a, e.f]; }); });
      assert.deepStrictEqual(s, [["practice", 5, 1, false], ["practice", 3, 1, false]]);
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "the fitness report follows the ported formulas on a synthetic history",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      await page.evaluate(MAKE);
      var r = await page.evaluate(function () {
        var atts = [];
        /* Sitting A, 3 days ago: 60 answers in one run, first quarter right,
           last quarter wrong: fade 1.0, a sitting of 40+. Reading 150 words
           in 45 s (200 wpm) on every answer. */
        for (var i = 0; i < 60; i++) atts.push(__mk(3, i, { correct: i < 45, credit: i < 45 ? 1 : 0, ft: 45000, w: 150 }));
        /* Sitting B, 1 day ago: 45 answers, all right. */
        for (var j = 0; j < 45; j++) atts.push(__mk(1, j));
        var st = PMP.Store.defaults();
        st.attempts = atts;
        st.checkins = [0, 1, 2].map(function (k) { return { at: Date.now() - (14 - 7 * k) * 86400000, kind: "weekly", v: 0, items: [5, 5, 5, 5, 1] }; });
        var f = PMP.Fitness.report(st);
        function sig(id) { var s = f.signal(id); return [s.score, s.figure]; }
        return {
          sittings: PMP.Fitness.sittings(PMP.Fitness.input(st).attempts).map(function (s) { return s.length; }),
          longest: sig("longest"), fade: sig("fade"), reading: sig("reading"), rush: sig("rush"), finish: sig("finish"),
          gate: f.part("stamina").gate, stamina: f.part("stamina").score,
          rung: f.ladderRung, next: f.ladderNext, wpm: f.wpm,
          worry: f.worry, high: f.worryHighForWeeks, total: f.score, provisional: f.provisional,
          finding: f.finding, nextStep: f.next
        };
      });
      assert.deepStrictEqual(r.sittings, [60, 45]);
      assert.deepStrictEqual(r.longest, [60 / 180, "60 questions"]);
      assert.deepStrictEqual(r.fade, [0, "−100 pts"], "fade 1.0 is 20+ points down: zero");
      assert.deepStrictEqual(r.reading, [1, "200 wpm"]);
      assert.strictEqual(r.wpm, 200);
      assert.deepStrictEqual(r.rush, [null, "not yet"], "too little evidence shows as not yet");
      assert.deepStrictEqual(r.finish, [null, "not yet"]);
      assert.strictEqual(r.gate, null, "two sittings of 40+ lift the gate");
      /* longest 1/3 × 35 + fade 0 × 30 over 65 of 100 weight (drift needs 5-a-quarter timing variety). */
      assert.ok(Math.abs(r.stamina - (35 / 180 * 60 / 65)) < 1e-9, String(r.stamina));
      assert.strictEqual(r.rung, 45, "the 60-question sitting faded, so the ladder stops at 45");
      assert.strictEqual(r.next, 70);
      assert.strictEqual(r.worry, 100);
      assert.strictEqual(r.high, true);
      assert.strictEqual(r.provisional, true);
      assert.strictEqual(r.finding, "Your accuracy fades in the last part of long sittings.");
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "least squares with a ±1 SE range, stalled scores, and a warning after the exam date",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      var r = await page.evaluate(function () {
        var W = 7 * 86400000, D = 86400000, x0 = PMP.Metrics.weekStart(Date.now()) - 3 * W;
        function pts(ys) { return ys.map(function (y, i) { return [x0 + i * W, y]; }); }
        var straight = PMP.Fitness.project([{ label: "Readiness", target: 75, pts: pts([40, 45, 50, 55]) }], null);
        var noisy = pts([40, 47, 49, 56]);
        var fit = PMP.Fitness.fit(noisy);
        var ranged = PMP.Fitness.project([{ label: "Readiness", target: 75, pts: noisy }], x0 + 4 * W);
        var rough = PMP.Fitness.project([{ label: "Readiness", target: 75, pts: pts([50, 60]) }], null);
        var stalled = PMP.Fitness.project([{ label: "Readiness", target: 75, pts: pts([50, 50, 49]) },
                                           { label: "Exam fitness", target: 70, pts: pts([40, 30]) }], null);
        return { x0: x0, W: W, D: D, straight: straight, fit: { slope: fit.slope, se: fit.se, b: fit.intercept }, ranged: ranged, rough: rough, stalled: stalled };
      });
      var last = r.x0 + 3 * r.W;
      assert.deepStrictEqual(r.straight, { earliest: last + 28 * r.D, latest: last + 28 * r.D, rough: false, stalled: [], afterExam: false });
      /* The same fit by hand: x in weeks, y readiness. */
      var xs = [0, 1, 2, 3].map(function (i) { return (r.x0 + i * r.W) / r.W; }), ys = [40, 47, 49, 56];
      var mx = xs.reduce(function (a, b) { return a + b; }) / 4, my = ys.reduce(function (a, b) { return a + b; }) / 4;
      var sxx = 0, sxy = 0;
      xs.forEach(function (x, i) { sxx += (x - mx) * (x - mx); sxy += (x - mx) * (ys[i] - my); });
      var slope = sxy / sxx, b = my - slope * mx, ss = 0;
      xs.forEach(function (x, i) { var e = ys[i] - (b + slope * x); ss += e * e; });
      var se = Math.sqrt(ss / 2 / sxx);
      assert.ok(Math.abs(r.fit.slope - slope) < 1e-9 && Math.abs(r.fit.se - se) < 1e-9);
      var at = function (s) { return last + Math.ceil(Math.max(0, (75 - (b + slope * xs[3])) / s) * 7 - 1e-6) * r.D; };
      assert.strictEqual(r.ranged.earliest, at(slope + se));
      assert.strictEqual(r.ranged.latest, at(slope - se));
      assert.strictEqual(r.ranged.afterExam, true, "the latest date is past an exam a week out");
      assert.strictEqual(r.rough.rough, true);
      assert.strictEqual(r.rough.earliest, null);
      /* Two weeks, 50 then 60: ten a week, 15 to go from the second, so
         1.5 weeks, rounded up to whole days: 11. */
      assert.strictEqual(r.rough.latest, r.x0 + r.W + 11 * r.D);
      assert.deepStrictEqual(r.stalled.stalled, ["Readiness", "Exam fitness"]);
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "#/book: three criteria, a projected range from weekly reports, and the late-exam warning",
    fn: async function (h, lib) {
      var page = await h.open("day3");
      await page.evaluate(function () {
        var W = 7 * 86400000, cur = PMP.Metrics.weekStart(Date.now());
        PMP.Store.commit(["weekly", "settings"], function (s) {
          s.weekly = [4, 3, 2, 1].map(function (k, i) { return { w: cur - k * W, u: cur - k * W + W - 1, r: 5 + i * 9, acc: 50, tt: 10, ma: 0, min: 60, d: 3, n: 30, mk: 0, lg: 20 }; });
          var d = new Date(Date.now() + 3 * 86400000);
          s.settings.examDate = d.getFullYear() + "-" + PMP.fmt.pad(d.getMonth() + 1) + "-" + PMP.fmt.pad(d.getDate());
        });
      });
      await lib.go(page, "#/book");
      var r = await page.evaluate(function () {
        var b = PMP.Fitness.booking(PMP.Store.state);
        return {
          crit: Array.prototype.map.call(document.querySelectorAll(".bk-crit li"), function (li) {
            return [li.querySelector(".bk-l").textContent, li.querySelector(".bk-v").textContent, li.getAttribute("data-met")];
          }),
          verdict: document.querySelector("#bk-verdict h2").textContent,
          proj: document.querySelector(".bk-proj-t").textContent,
          late: !!document.querySelector(".bk-late"),
          stalled: b.projection && b.projection.stalled, ready: b.ready, mocks: b.mocksNeeded,
          rail: document.querySelector('.nav-i[data-route="book"]').getAttribute("aria-current")
        };
      });
      assert.strictEqual(r.crit.length, 3);
      assert.strictEqual(r.crit[0][0], "Readiness: not yet met");
      assert.ok(/^\d+ \/ 75\+$/.test(r.crit[0][1]), r.crit[0][1]);
      assert.deepStrictEqual(r.crit[1], ["Exam fitness: not yet met", "not yet / 70+", "false"]);
      assert.deepStrictEqual(r.crit[2], ["Full mocks at Target or above: not yet met", "0 / 2", "false"]);
      assert.strictEqual(r.late, false, "a stalled score has no date to compare with the exam");
      assert.strictEqual(r.verdict, "Not yet.");
      assert.strictEqual(r.ready, false);
      assert.strictEqual(r.mocks, 2);
      /* Fitness has no weekly figures, so it cannot be projected. */
      assert.deepStrictEqual(r.stalled, ["Exam fitness"]);
      assert.ok(/^Exam fitness isn't rising yet/.test(r.proj), r.proj);
      assert.strictEqual(r.rail, "page");
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "weekly reports: Monday weeks, the current week kept up to date as you study, and compared",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      await page.evaluate(function () {
        PMP.Practice.startWith(PMP.QUESTIONS.slice(0, 3), "practice");
        PMP.Router.navigate("practice");
      });
      await page.waitForTimeout(100);
      await H.finishDrill(page);
      await page.waitForFunction(function () { return PMP.Store.state.weekly.length === 1; }, null, { timeout: 5000 });
      var r = await page.evaluate(function () {
        var w = PMP.Store.state.weekly[0], d = new Date(w.w);
        var prev = Object.assign({}, w, { w: w.w - 7 * 86400000, n: w.n + 5, fr: 10, cz: { RUSHED: 2 } });
        var cur = Object.assign({}, w, { fr: 4 });
        var cmp = PMP.Fitness.compare(cur, prev);
        function find(t, l) { return cmp.filter(function (s) { return s.title === t; })[0].rows.filter(function (x) { return x.label === l; })[0]; }
        return { w: w, day: d.getDay(), h: d.getHours() + d.getMinutes(), keys: Object.keys(w).sort(),
                 freezes: find("Exam fitness", "Freezes"), answers: find("Effort", "Questions answered"),
                 rushed: find("Mistake causes", "Rushed"), sections: cmp.map(function (s) { return s.title; }) };
      });
      assert.strictEqual(r.day, 1, "weeks start on Monday");
      assert.strictEqual(r.h, 0, "at midnight");
      assert.strictEqual(r.w.n, 3);
      ["w", "u", "r", "acc", "tt", "ma", "min", "d", "n", "mk", "lg"].forEach(function (k) { assert.ok(r.keys.indexOf(k) >= 0, k); });
      assert.deepStrictEqual([r.freezes.delta, r.freezes.direction], [-6, 1], "fewer freezes is better");
      assert.deepStrictEqual([r.answers.delta, r.answers.direction], [-5, -1]);
      assert.deepStrictEqual([r.rushed.value, r.rushed.previous, r.rushed.direction], [0, 2, 1]);
      /* Checking an answer asks how sure (§10.02), so a week with answers in
         it has the Confidence rows the phones show. */
      assert.deepStrictEqual(r.sections, ["Knowledge", "Exam fitness", "Effort", "Confidence", "Mistake causes"]);

      /* Three weeks on file: the trend, and a choice of comparison. */
      await page.evaluate(function () {
        var w = PMP.Store.state.weekly[0];
        PMP.Store.commit(["weekly"], function (s) {
          s.weekly = [2, 1].map(function (k) { return Object.assign({}, w, { w: w.w - k * 7 * 86400000, r: 10 * k, min: 30 * k }); }).concat(s.weekly);
        });
      });
      await lib.go(page, "#/reports");
      var v = await page.evaluate(function () {
        return {
          eyebrow: document.querySelector("#wk-cur .eyebrow").textContent,
          sparks: Array.prototype.map.call(document.querySelectorAll(".wk-spark svg"), function (s) { return s.getAttribute("aria-label"); }),
          seg: Array.prototype.map.call(document.querySelectorAll("#wk-cur .seg button"), function (b) { return b.textContent + ":" + b.getAttribute("aria-pressed"); }),
          earlier: document.querySelectorAll("details.wk-week").length
        };
      });
      assert.strictEqual(v.eyebrow, "This week so far");
      assert.strictEqual(v.sparks.length, 2, "readiness and minutes; fitness has no figures yet: " + JSON.stringify(v.sparks));
      assert.ok(/^Readiness by week: 20, 10, \d+$/.test(v.sparks[0]), v.sparks[0]);
      assert.deepStrictEqual(v.seg, ["vs last week:true", "vs your first week:false"]);
      assert.strictEqual(v.earlier, 2);
      await H.clickText(page, "#wk-cur .seg button", "vs your first week");
      var pressed = await page.evaluate(function () { return document.querySelector("#wk-cur .seg button[aria-pressed=true]").textContent; });
      assert.strictEqual(pressed, "vs your first week");
      await page.click("details.wk-week summary");
      /* The comparison is filled on the details' toggle event, which fires after the click. */
      await page.waitForFunction(function () { return document.querySelectorAll("details.wk-week .wk-tbl").length > 0; }, null, { timeout: 3000 });
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "#/fitness draws every part and signal with its figure, the ladder and the feeling card",
    fn: async function (h, lib) {
      var page = await h.open("examweek", { hash: "#/fitness" });
      var r = await page.evaluate(function () {
        return {
          parts: Array.prototype.map.call(document.querySelectorAll(".fit-part"), function (p) { return p.getAttribute("data-part"); }),
          signals: document.querySelectorAll(".fit-part [data-signal]").length,
          notYet: Array.prototype.filter.call(document.querySelectorAll(".fit-part [data-signal] .rd-p"), function (n) { return n.textContent === "not yet"; }).length,
          ladder: document.querySelectorAll(".fit-ladder li").length,
          ladderLabel: document.querySelector(".fit-ladder").getAttribute("aria-label"),
          weekly: document.getElementById("fit-weekly").textContent,
          links: Array.prototype.map.call(document.querySelectorAll('#view a[href="#/reports"], #view a[href="#/book"]'), function (a) { return a.getAttribute("href"); })
        };
      });
      assert.deepStrictEqual(r.parts, ["stamina", "composure", "consistency"]);
      assert.strictEqual(r.signals, 14);
      assert.ok(r.notYet >= 4, "fixtures without signals show not yet: " + r.notYet);
      assert.strictEqual(r.ladder, 6);
      assert.ok(/^Stamina ladder: reached \d+ questions/.test(r.ladderLabel));
      assert.strictEqual(r.weekly, "Take this week's check");
      assert.deepStrictEqual(r.links.sort(), ["#/book", "#/reports"]);
      await page.setViewportSize({ width: 390, height: 844 });
      for (var route of ["#/fitness", "#/book", "#/reports"]) {
        await lib.go(page, route);
        var over = await page.evaluate(function () { return document.documentElement.scrollWidth - window.innerWidth; });
        assert.ok(over <= 0, route + " overflows by " + over);
      }
      await lib.clean(page);
      await page.ctx.close();
    }
  }
];
