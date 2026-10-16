"use strict";
/* S9: wellbeing (§10.20). The once-a-day check-in above Today's session
   and the breathing reset it offers after a low answer, the weekly worry
   check, the supportive card after three weeks of high worry, the lighter
   week, the "write it down" box before a mock (never saved) and the
   tension check-in after one. Check-ins are stored in the shared shape. */
var assert = require("assert");
var H = require("./helpers");

var DAY = 86400000;

async function primaries(page) {
  return page.evaluate(function () { return document.querySelectorAll("#view .btn--primary").length; });
}

module.exports = [
  {
    name: "Today asks how you feel once a day; a low answer offers the two-minute reset",
    fn: async function (h, lib) {
      var page = await h.open("day3");
      var shown = await page.evaluate(function () {
        var c = document.getElementById("wb-pre");
        return c && { q: c.querySelector("h2").textContent, faces: Array.prototype.map.call(c.querySelectorAll(".wb-face"), function (b) { return b.lastChild.textContent; }) };
      });
      assert.ok(shown, "check-in card on Today");
      assert.strictEqual(shown.q, "How do you feel about studying right now?");
      assert.deepStrictEqual(shown.faces, ["Very low", "Low", "OK", "Good", "Very good"]);
      assert.strictEqual(await primaries(page), 1, "Today keeps exactly one primary button");

      await H.clickText(page, "#wb-pre .wb-face", "🙁");
      var after = await page.evaluate(function () {
        return { rows: PMP.Store.state.checkins, offer: document.querySelector("#wb-pre h2").textContent };
      });
      assert.strictEqual(after.rows.length, 1);
      assert.deepStrictEqual(Object.keys(after.rows[0]).sort(), ["at", "items", "kind", "v"]);
      assert.deepStrictEqual([after.rows[0].kind, after.rows[0].v, after.rows[0].items], ["pre", 2, []]);
      assert.strictEqual(after.offer, "Want a two-minute reset first?");

      /* The reset, sped up: 4 s in and 6 s out, twelve times, at a hundredth. */
      await page.evaluate(function () { PMP.Wellbeing.BREATH.inMs = 40; PMP.Wellbeing.BREATH.outMs = 60; });
      await H.clickText(page, "#wb-pre button", "Breathe for 2 minutes");
      var s = await page.evaluate(function () {
        return { open: document.getElementById("sheet").open, title: document.getElementById("sheet-title").textContent,
                 phase: document.querySelector(".breath-p").textContent, n: document.querySelector(".breath-n").textContent,
                 action: document.querySelector("#sheet-foot .btn").textContent };
      });
      assert.ok(/^Breathe (in|out)$/.test(s.phase), s.phase);
      assert.ok(/^\d+ breaths? to go$/.test(s.n), s.n);
      delete s.phase; delete s.n;
      assert.deepStrictEqual(s, { open: true, title: "A two-minute reset", action: "I'm ready, start now" });
      await page.waitForFunction(function () { return !document.getElementById("sheet").open; }, null, { timeout: 5000 });

      await lib.go(page, "#/dashboard");
      await lib.go(page, "#/today");
      var again = await page.evaluate(function () { return !!document.getElementById("wb-pre"); });
      assert.strictEqual(again, false, "answered for today");
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "skipping holds for the day; the reset keeps still under reduced motion and ends on one press",
    fn: async function (h, lib) {
      var page = await h.open("day3");
      await H.clickText(page, "#wb-pre button", "Skip");
      await lib.go(page, "#/dashboard");
      await lib.go(page, "#/today");
      var r = await page.evaluate(function () { return { card: !!document.getElementById("wb-pre"), rows: PMP.Store.state.checkins.length }; });
      assert.deepStrictEqual(r, { card: false, rows: 0 }, "a skip writes nothing and is not asked again today");

      await page.emulateMedia({ reducedMotion: "reduce" });
      await page.evaluate(function () { PMP.Wellbeing.breathe(); });
      await page.waitForTimeout(200);
      var still = await page.evaluate(function () {
        var c = document.querySelector(".breath-c");
        return { still: c.getAttribute("data-still"), transform: c.style.transform };
      });
      assert.deepStrictEqual(still, { still: "true", transform: "" });
      await H.clickText(page, "#sheet-foot button", "I'm ready, start now");
      var open = await page.evaluate(function () { return document.getElementById("sheet").open; });
      assert.strictEqual(open, false);
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "the weekly check: five statements, Save only when all are answered, worry on Exam fitness",
    fn: async function (h, lib) {
      var page = await h.open("day3");
      var card = await page.evaluate(function () { return document.getElementById("wb-weekly-h") && document.getElementById("wb-weekly-h").textContent; });
      assert.strictEqual(card, "Weekly check · 30 seconds");
      await H.clickText(page, "#view button", "Take it");
      var st = await page.evaluate(function () {
        return { legends: Array.prototype.map.call(document.querySelectorAll("#sheet .wb-st legend"), function (l) { return l.textContent; }),
                 save: document.querySelector("#sheet-foot .btn--primary").disabled };
      });
      assert.strictEqual(st.legends.length, 5);
      assert.strictEqual(st.legends[4], "I feel able to stay focused for four hours.");
      assert.strictEqual(st.save, true);
      /* Agree, Somewhat agree, Neutral, Disagree … and Agree with the reversed fifth. */
      var picks = [5, 4, 3, 1];
      for (var i = 0; i < 4; i++) await page.click('label[for="wb-w' + i + "-" + (picks[i] - 1) + '"]');
      var still = await page.evaluate(function () { return document.querySelector("#sheet-foot .btn--primary").disabled; });
      assert.strictEqual(still, true, "four of five is not enough");
      await page.click('label[for="wb-w4-4"]');
      await H.clickText(page, "#sheet-foot button", "Save");
      var r = await page.evaluate(function () {
        var c = PMP.Store.state.checkins;
        return { row: c[c.length - 1], due: PMP.Wellbeing.weeklyDue(PMP.Store.state), worry: PMP.Fitness.report(PMP.Store.state).worry };
      });
      assert.deepStrictEqual([r.row.kind, r.row.v, r.row.items], ["weekly", 0, [5, 4, 3, 1, 5]]);
      assert.strictEqual(r.due, false);
      /* (5 + 4 + 3 + 1 + (6 − 5)) − 5 = 9, over 20: 45. */
      assert.strictEqual(r.worry, 45);
      await lib.go(page, "#/fitness");
      var t = await page.evaluate(function () { return { w: document.querySelector(".fit-worry").textContent, b: document.getElementById("fit-weekly").textContent }; });
      assert.deepStrictEqual(t, { w: "Exam worry: 45 / 100 (last weekly check)", b: "Take it again" });
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "three weeks of high worry brings the supportive card; falling momentum the lighter week",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      await page.evaluate(function (DAY) {
        var now = Date.now(), q = PMP.QUESTIONS[0];
        var atts = [];
        /* Busy three weeks ago (40 answers of a minute on three days), quiet this week (two). */
        [24, 20, 16].forEach(function (d) {
          for (var i = 0; i < 40; i++) atts.push({ id: "a" + d + "-" + i, qid: q.id, ecoTask: q.ecoTask, domain: q.domain, type: "single",
            correct: true, credit: 1, ms: 60000, at: new Date(now - d * DAY + i * 60000).toISOString(), mode: "practice", order: [] });
        });
        atts.push({ id: "a1", qid: q.id, ecoTask: q.ecoTask, domain: q.domain, type: "single", correct: true, credit: 1, ms: 60000,
          at: new Date(now - DAY).toISOString(), mode: "practice", order: [] });
        PMP.Store.commit(["attempts", "checkins", "coach"], function (s) {
          s.attempts = atts;
          s.checkins = [15, 8, 1].map(function (d) { return { at: now - d * DAY, kind: "weekly", v: 0, items: [5, 4, 5, 4, 2] }; });
          s.coach.profile = { at: new Date(now - 30 * DAY).toISOString(), experience: "some", agile: "some", when: "evening", why: "" };
        });
      }, DAY);
      var f = await page.evaluate(function () { var r = PMP.Fitness.report(PMP.Store.state); return { low: r.momentumLow, high: r.worryHighForWeeks, worry: r.worry }; });
      /* (5 + 4 + 5 + 4 + (6 − 2)) − 5 = 17, over 20: 85, three weeks running. */
      assert.deepStrictEqual(f, { low: true, high: true, worry: 85 });
      await lib.go(page, "#/dashboard");
      await lib.go(page, "#/today");
      var t = await page.evaluate(function () {
        return { light: document.getElementById("wb-light-h") && document.getElementById("wb-light-h").textContent,
                 worry: document.getElementById("wb-worry-h") && document.getElementById("wb-worry-h").textContent,
                 weekly: !!document.getElementById("wb-weekly-h") };
      });
      assert.strictEqual(t.light, "A lighter week");
      assert.strictEqual(t.worry, "Your weekly checks have shown a lot of worry for three weeks.");
      assert.strictEqual(t.weekly, false, "checked a day ago, so not due");
      assert.strictEqual(await primaries(page), 1);
      await H.clickText(page, ".wb-light button", "10 questions");
      var p = await page.evaluate(function () { return { route: PMP.Router.current.name, n: PMP.Practice.session.items.length }; });
      assert.deepStrictEqual(p, { route: "practice", n: 10 });
      await lib.go(page, "#/fitness");
      var support = await page.evaluate(function () { return !!document.querySelector("#view .wb-support"); });
      assert.ok(support);
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "write it down before a mock is never saved; after it, one tap for how tense it felt",
    fn: async function (h, lib) {
      var page = await h.open("day3", { hash: "#/exam" });
      await H.clickText(page, ".wb-write button", "Write it down");
      var secret = "worried about the EVM questions zq7";
      await page.fill("#wb-worries", secret);
      await page.evaluate(function () { return PMP.Store.flush(); });
      var leaked = await page.evaluate(function (t) {
        var all = JSON.stringify(PMP.Store.state);
        for (var i = 0; i < localStorage.length; i++) all += localStorage.getItem(localStorage.key(i));
        return all.indexOf(t) >= 0;
      }, secret);
      assert.strictEqual(leaked, false, "nothing written is kept");
      await H.clickText(page, ".wb-write button", "Done, clear it");
      var gone = await page.evaluate(function () { return !document.getElementById("wb-worries"); });
      assert.ok(gone);

      /* A sitting: answer the first question (wrong then right when it is a
         pick-one), then end and mark it. */
      await H.clickText(page, "#view button", "Start a sitting");
      await H.clickText(page, "#sheet-foot button", "Start the clock");
      await page.waitForTimeout(1100);
      var q = await page.evaluate(function () {
        var run = PMP.Store.state.examRun, q = PMP.Content.byId(run.qids[0]);
        return { id: q.id, type: q.type, right: q.type === "single" ? q.options.filter(function (o) { return o.correct; })[0].id : null,
                 wrong: q.type === "single" ? q.options.filter(function (o) { return !o.correct; })[0].id : null };
      });
      if (q.type === "single") {
        for (var id of [q.wrong, q.right]) {
          await page.evaluate(function (oid) { document.querySelector('#exam-card .opt[data-opt="' + oid + '"] input').click(); }, id);
          await page.waitForTimeout(60);
        }
      } else {
        await page.evaluate(function () {
          var c = document.getElementById("exam-card");
          var i = c.querySelector('input[type="radio"], input[type="checkbox"]');
          if (i) i.click();
          c.querySelectorAll("select").forEach(function (s) { if (s.options.length > 1) { s.selectedIndex = 1; s.dispatchEvent(new Event("change", { bubbles: true })); } });
          c.querySelectorAll('input[type="text"], input:not([type])').forEach(function (inp) { inp.value = "x"; inp.dispatchEvent(new Event("input", { bubbles: true })); });
          var hot = c.querySelector(".hot");
          if (hot) hot.dispatchEvent(new MouseEvent("click", { bubbles: true }));
        });
      }
      await H.clickText(page, "#view button", "End exam");
      await H.clickText(page, "#sheet-foot button", "End and mark it");
      var r = await page.evaluate(function (qid) {
        var st = PMP.Store.state;
        var a = st.attempts.filter(function (x) { return x.mode === "exam" && x.qid === qid; })[0];
        var row = PMP.Sync.toDoc(st).attempts.filter(function (x) { return x.q === qid && x.wx && x.wx.mode === "exam"; })[0];
        return { a: a && { ft: a.ft, w: a.w, wr: a.wr }, tm: row && row.tm, sit: st.sittings[st.sittings.length - 1],
                 card: document.querySelector('[data-checkin="post"] h2') && document.querySelector('[data-checkin="post"] h2').textContent };
      }, q.id);
      assert.ok(r.a && r.a.ft >= 1000, JSON.stringify(r.a));
      assert.ok(r.a.w > 0);
      if (q.type === "single") assert.strictEqual(r.a.wr, 1);
      assert.strictEqual(r.tm, true, "a mock answer is against the clock");
      assert.deepStrictEqual([r.sit.kind, r.sit.a, r.sit.f], ["mock", 1, true]);
      assert.strictEqual(r.card, "How tense did you feel during it?");
      var labels = await page.evaluate(function () { return Array.prototype.map.call(document.querySelectorAll('[data-checkin="post"] .wb-face'), function (b) { return b.lastChild.textContent; }); });
      assert.deepStrictEqual(labels, ["Very tense", "Tense", "So-so", "Calm", "Very calm"]);
      await H.clickText(page, '[data-checkin="post"] .wb-face', "🙂");
      var post = await page.evaluate(function () { var c = PMP.Store.state.checkins; return c[c.length - 1]; });
      assert.deepStrictEqual([post.kind, post.v], ["post", 4]);
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "discarding a started sitting logs it as left part-way",
    fn: async function (h, lib) {
      var page = await h.open("day3", { hash: "#/exam" });
      await H.clickText(page, "#view button", "Start a sitting");
      await H.clickText(page, "#sheet-foot button", "Start the clock");
      await page.evaluate(function () {
        var c = document.getElementById("exam-card");
        var i = c.querySelector('input[type="radio"], input[type="checkbox"]');
        if (i) i.click();
        c.querySelectorAll("select").forEach(function (s) { if (s.options.length > 1) { s.selectedIndex = 1; s.dispatchEvent(new Event("change", { bubbles: true })); } });
        c.querySelectorAll('input[type="text"], input:not([type])').forEach(function (inp) { inp.value = "x"; inp.dispatchEvent(new Event("input", { bubbles: true })); });
        var hot = c.querySelector(".hot");
        if (hot) hot.dispatchEvent(new MouseEvent("click", { bubbles: true }));
      });
      /* Close the page's hold on the sitting, as a closed tab would, and come back to it. */
      var saved = await page.evaluate(function () { return Object.keys(PMP.Store.state.examRun.answers).length; });
      assert.strictEqual(saved, 1);
      await page.evaluate(function () { return PMP.Store.flush(); });
      await page.reload();
      await page.waitForFunction(function () { return window.PMP && PMP.Store.state.sys.ready; });
      await lib.go(page, "#/exam");
      await page.waitForTimeout(100);
      await page.evaluate(function () { if (document.getElementById("sheet").open) PMP.UI.closeSheet(null); });
      await H.clickText(page, "#view button", "Discard it");
      await H.clickText(page, "#sheet-foot button", "Discard it");
      var s = await page.evaluate(function () { var x = PMP.Store.state.sittings; return x[x.length - 1]; });
      assert.deepStrictEqual([s.kind, s.a, s.f], ["mock", 1, false]);
      assert.ok(/^mock:\d+$/.test(s.k));
      await page.ctx.close();
    }
  }
];
