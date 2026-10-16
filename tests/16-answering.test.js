"use strict";
/* Answering tools in practice (§10.02, 10.04.4, §10.23), ported from the
   phones' QuestionView, PracticeScreen and PmpViewModel: three-level
   confidence that checks on one tap, strike-out, stem highlighting, the
   pacing bar, the freeze prompt, checkpoints and the reading drill. */
var assert = require("assert");
var H = require("./helpers");

/* A practice set of single-choice questions, on screen. */
async function drill(page, n, mode) {
  await page.evaluate(function (a) {
    var qs = PMP.QUESTIONS.filter(function (q) { return q.type === "single"; }).slice(0, a.n);
    PMP.Practice.startWith(qs, a.mode || "practice");
    PMP.Router.navigate("practice");
  }, { n: n, mode: mode });
  await page.waitForTimeout(120);
}
/* Picks the right or a wrong option on the question on screen. */
async function pick(page, right) {
  return page.evaluate(function (right) {
    var s = PMP.Practice.session, item = s.items[s.i];
    var o = item.p.options.filter(function (x) { return !!x.correct === right; })[0];
    document.querySelector('#drill-card .opt[data-opt="' + o.id + '"] input').click();
    return o.id;
  }, right);
}
async function conf(page, label) {
  await page.evaluate(function (label) {
    var b = Array.prototype.filter.call(document.querySelectorAll("#drill-card .q-conf button"), function (x) { return x.textContent === label; })[0];
    b.click();
  }, label);
  await page.waitForTimeout(60);
}
function lastAttempt(page) {
  return page.evaluate(function () { var a = PMP.Store.state.attempts; return a[a.length - 1]; });
}

module.exports = [
  {
    name: "confidence: Sure / Think so / Guess from the shared rules; one tap checks; the two callouts",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      await drill(page, 3);
      var r = await page.evaluate(function () {
        var bs = document.querySelectorAll("#drill-card .q-conf button");
        return { labels: Array.prototype.map.call(bs, function (b) { return b.textContent; }),
                 vals: Array.prototype.map.call(bs, function (b) { return b.getAttribute("data-conf"); }),
                 disabled: Array.prototype.every.call(bs, function (b) { return b.disabled; }),
                 check: !!Array.prototype.filter.call(document.querySelectorAll("#drill-card button"), function (b) { return !b.hidden && b.textContent === "Check answer"; }).length,
                 group: document.querySelector("#drill-card .q-conf").getAttribute("role") };
      });
      assert.deepStrictEqual(r.labels, ["Sure", "Think so", "Guess"]);
      assert.deepStrictEqual(r.vals, ["3", "2", "1"]);
      assert.ok(r.disabled, "waiting for an answer");
      assert.strictEqual(r.check, false, "no separate Check answer");
      assert.strictEqual(r.group, "group");

      await pick(page, false);
      await conf(page, "Sure");
      var a = await lastAttempt(page);
      assert.strictEqual(a.confidence, 3);
      assert.strictEqual(a.correct, false);
      var fb = await page.textContent("#drill-card");
      assert.ok(fb.indexOf("You were sure, and it was wrong.") >= 0);
      assert.strictEqual(await page.$$eval("#drill-card .q-conf", function (x) { return x.length; }), 0, "the row goes once checked");
      await page.click("#drill-card .btn--primary");                 /* Next */
      await page.waitForTimeout(60);

      await pick(page, true);
      await conf(page, "Guess");
      a = await lastAttempt(page);
      assert.deepStrictEqual([a.confidence, a.correct], [1, true]);
      fb = await page.textContent("#drill-card");
      assert.ok(fb.indexOf("Right, but a guess.") >= 0 && fb.indexOf("won't count toward mastery") >= 0);
      await page.click("#drill-card .btn--primary");
      await page.waitForTimeout(60);

      /* Enter, with an answer, goes to the buttons rather than choosing. */
      await pick(page, true);
      await page.focus("#drill-card");
      await page.keyboard.press("Enter");
      assert.strictEqual(await page.evaluate(function () { return document.activeElement.textContent; }), "Sure");
      await page.keyboard.press("Tab");
      await page.keyboard.press("Enter");                           /* Think so */
      await page.waitForTimeout(60);
      a = await lastAttempt(page);
      assert.strictEqual(a.confidence, 2);

      /* Tracking off: Check answer is back and nothing is asked. */
      await page.evaluate(function () { PMP.Store.commit(["settings"], function (s) { s.settings.confidenceTracking = false; }); });
      await drill(page, 1);
      await pick(page, true);
      await H.clickText(page, "#drill-card button", "Check answer");
      a = await lastAttempt(page);
      assert.strictEqual(a.confidence, null);
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "a lucky guess stays out of mastery; sure-but-wrong leads the journal; calibration is Sure against the rest",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      var r = await page.evaluate(function () {
        var lesson = PMP.LESSONS.filter(function (l) { return l.ecoTask && PMP.QUESTIONS.some(function (q) { return q.ecoTask === l.ecoTask; }); })[0];
        var qs = PMP.QUESTIONS.filter(function (q) { return q.ecoTask === lesson.ecoTask; });
        var now = Date.now(), rows = [];
        for (var i = 0; i < 12; i++) {
          var q = qs[i % qs.length];
          rows.push({ id: "g" + i, qid: q.id, ecoTask: q.ecoTask, domain: q.domain, type: "single", correct: true, credit: 1,
                      ms: 40000, at: new Date(now - 3600000 + i * 1000).toISOString(), confidence: i < 6 ? 1 : 2, mode: "practice", order: [] });
        }
        var other = PMP.QUESTIONS[5], other2 = PMP.QUESTIONS[6];
        rows.push({ id: "w-sure", qid: other.id, ecoTask: other.ecoTask, domain: other.domain, type: "single", correct: false, credit: 0,
                    ms: 40000, at: new Date(now - 7200000).toISOString(), confidence: 3, mode: "practice", order: [] });
        rows.push({ id: "w-new", qid: other2.id, ecoTask: other2.ecoTask, domain: other2.domain, type: "single", correct: false, credit: 0,
                    ms: 40000, at: new Date(now - 60000).toISOString(), confidence: 2, mode: "practice", order: [] });
        PMP.Store.commit(["attempts"], function (s) { s.attempts = rows; });
        var m = PMP.Learn.mastery(lesson);
        return { n: m.n, order: PMP.Journal.misses(PMP.Store.state).map(function (x) { return x.attempt.id; }) };
      });
      assert.strictEqual(r.n, 6, "six right guesses left out of twelve answers");
      assert.deepStrictEqual(r.order.slice(0, 2), ["w-sure", "w-new"], "the sure miss first, though older");
      await lib.go(page, "#/journal");
      assert.strictEqual(await page.$$eval("#view .jr-sure", function (x) { return x.length; }), 1);
      await lib.go(page, "#/analytics");
      var t = await page.textContent("#view");
      assert.ok(t.indexOf("Think so / Guess") >= 0, "the calibration rows");
      assert.ok(t.indexOf("Not sure") < 0);
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "strike-out: the cross button, right-click and Shift+letter; choosing restores; `el` recorded and shared",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      await drill(page, 2);
      var ids = await page.evaluate(function () {
        var s = PMP.Practice.session, item = s.items[s.i];
        return item.p.options.map(function (o) { return { id: o.id, correct: !!o.correct }; });
      });
      var wrong = ids.filter(function (o) { return !o.correct; });
      var right = ids.filter(function (o) { return o.correct; })[0];
      /* The cross button, on a wrong option. */
      await page.click('#drill-card .opt[data-opt="' + wrong[0].id + '"] .opt-x');
      var st = await page.evaluate(function (id) {
        var row = document.querySelector('#drill-card .opt[data-opt="' + id + '"]');
        return { struck: row.getAttribute("data-struck"), pressed: row.querySelector(".opt-x").getAttribute("aria-pressed"),
                 checked: row.querySelector("input").checked, opacity: getComputedStyle(row.querySelector(".txt")).opacity,
                 deco: getComputedStyle(row.querySelector(".txt > span")).textDecorationLine };
      }, wrong[0].id);
      assert.deepStrictEqual([st.struck, st.pressed, st.checked], ["true", "true", false], "struck, not chosen");
      assert.strictEqual(st.opacity, "0.5");
      assert.strictEqual(st.deco, "line-through");
      /* Right-click another. */
      await page.click('#drill-card .opt[data-opt="' + wrong[1].id + '"] .txt', { button: "right" });
      assert.strictEqual(await page.getAttribute('#drill-card .opt[data-opt="' + wrong[1].id + '"]', "data-struck"), "true");
      /* Shift+letter toggles the third, twice: on and back off. */
      var letter = await page.evaluate(function (id) {
        return document.querySelector('#drill-card .opt[data-opt="' + id + '"] .key').textContent;
      }, wrong[2].id);
      await page.focus("#drill-card");
      await page.keyboard.press("Shift+" + letter);
      assert.strictEqual(await page.getAttribute('#drill-card .opt[data-opt="' + wrong[2].id + '"]', "data-struck"), "true");
      await page.keyboard.press("Shift+" + letter);
      assert.strictEqual(await page.getAttribute('#drill-card .opt[data-opt="' + wrong[2].id + '"]', "data-struck"), null);
      /* Choosing a struck option brings it back. */
      await page.click('#drill-card .opt[data-opt="' + wrong[1].id + '"] .txt');
      assert.strictEqual(await page.getAttribute('#drill-card .opt[data-opt="' + wrong[1].id + '"]', "data-struck"), null);
      await page.click('#drill-card .opt[data-opt="' + right.id + '"] .txt');
      await conf(page, "Think so");
      var a = await lastAttempt(page);
      assert.strictEqual(a.el, 1, "one option still crossed out when checked");
      var doc = await page.evaluate(function () { var d = PMP.Sync.toDoc(PMP.Store.state); return d.attempts[d.attempts.length - 1]; });
      assert.strictEqual(doc.el, 1, "el goes out under the shared name");
      assert.ok(!doc.wx || !("el" in doc.wx));
      var back = await page.evaluate(function () {
        var d = PMP.Sync.toDoc(PMP.Store.state);
        var st2 = PMP.Sync.fromDoc(d, PMP.Store.state);
        var view = PMP.Store.defaults();
        Object.keys(st2).forEach(function (k) { view[k] = st2[k]; });
        return { el: st2.attempts[st2.attempts.length - 1].el, same: PMP.Sync.sameDoc(PMP.Sync.toDoc(view), d) };
      });
      assert.deepStrictEqual(back, { el: 1, same: true }, "and back, lossless");
      /* No cross buttons once checked. */
      assert.strictEqual(await page.$$eval("#drill-card .opt-x", function (x) { return x.length; }), 0);
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "the results page shows the elimination effect once each side has 10 answers",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      await page.evaluate(function () {
        var q = PMP.QUESTIONS.filter(function (x) { return x.type === "single"; })[0], now = Date.now(), rows = [];
        for (var i = 0; i < 24; i++) {
          rows.push({ id: "e" + i, qid: q.id, ecoTask: q.ecoTask, domain: q.domain, type: "single",
                      correct: i < 12 ? i % 4 !== 0 : i % 2 === 0, credit: 0, ms: 40000,
                      at: new Date(now - 86400000 + i * 1000).toISOString(), confidence: null, mode: "practice", order: [] });
          if (i < 12) rows[i].el = 2;
          rows[i].credit = rows[i].correct ? 1 : 0;
        }
        PMP.Store.commit(["attempts"], function (s) { s.attempts = rows; });
      });
      await drill(page, 1);
      await H.clickText(page, "#drill-card button", "Skip and finish");
      var t = await page.textContent("#elim-effect");
      assert.ok(/75% right when you did, 50% when you didn't/.test(t), t);
      assert.ok(/12 with an option crossed out, 12 without/.test(t), t);
      assert.ok(/Ruling out two options first is working for you/.test(t), t);
      /* Under 10 on a side: nothing. */
      await page.evaluate(function () { PMP.Store.commit(["attempts"], function (s) { s.attempts = s.attempts.slice(-9); }); });
      assert.strictEqual(await page.evaluate(function () { return PMP.Practice.eliminationEffect(PMP.Store.state); }), null);
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "stem highlighting: select words to mark them, select again to clear; whole words; never stored",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      await drill(page, 2);
      async function select(from, to) {
        await page.evaluate(function (a) {
          var p = document.querySelector("#drill-card .q-stem"), w = p.querySelectorAll(".hw");
          var r = document.createRange();
          r.setStart(w[a.from].firstChild, 0);
          /* Ends on the next word's first character, as a double-click with
             its trailing space can: that word is not touched. */
          r.setEnd(w[a.to + 1].firstChild, 0);
          var sel = window.getSelection();
          sel.removeAllRanges();
          sel.addRange(r);
          p.dispatchEvent(new MouseEvent("mouseup", { bubbles: true }));
        }, { from: from, to: to });
        await page.waitForTimeout(30);
      }
      function marked() { return page.$$eval("#drill-card .q-stem .hw", function (w) { return w.map(function (x, i) { return x.getAttribute("data-hl") === "true" ? i : -1; }).filter(function (i) { return i >= 0; }); }); }
      var before = await page.evaluate(function () { return JSON.stringify(PMP.Store.state); });
      await select(1, 2);
      assert.deepStrictEqual(await marked(), [1, 2]);
      assert.strictEqual(await page.evaluate(function () { return window.getSelection().isCollapsed; }), true, "the selection is cleared");
      await select(4, 4);
      assert.deepStrictEqual(await marked(), [1, 2, 4]);
      await select(1, 2);
      assert.deepStrictEqual(await marked(), [4], "selecting marked words clears them");
      var text = await page.evaluate(function () { var s = PMP.Practice.session; return [document.querySelector("#drill-card .q-stem").textContent, s.items[s.i].q.stem]; });
      assert.strictEqual(text[0], text[1], "the stem reads the same");
      /* They stay with the question in this drill, and only there. */
      await page.click("#drill-card button:has-text('Skip')");
      await page.waitForTimeout(60);
      assert.deepStrictEqual(await marked(), []);
      await page.click("#drill-card button:has-text('Back')");
      await page.waitForTimeout(60);
      assert.deepStrictEqual(await marked(), [4]);
      var after = await page.evaluate(function () { return JSON.stringify(PMP.Store.state); });
      assert.ok(after.indexOf("data-hl") < 0 && after.length === before.length, "nothing stored");
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "pacing bar against 76 s: green to 75%, amber to 100%, red past it",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      await drill(page, 1);
      async function at(sec) {
        await page.evaluate(function (ms) { var s = PMP.Practice.session; s.items[s.i].clock.elapsed = function () { return ms; }; }, sec * 1000);
        await page.waitForTimeout(320);
        return page.evaluate(function () {
          var p = document.querySelector(".drill-hd .pace");
          return { state: p.getAttribute("data-state"), w: parseFloat(p.querySelector(".bar > i").style.width),
                   over: !p.querySelector(".pace-over").hidden,
                   colour: getComputedStyle(p.querySelector(".bar > i")).backgroundColor };
        });
      }
      var a = await at(57), b = await at(58), c = await at(76), d = await at(77), e = await at(200);
      assert.deepStrictEqual([a.state, b.state, c.state, d.state], ["ok", "warn", "warn", "over"]);
      assert.ok(Math.abs(a.w - 57 / 76 / 1.5 * 100) < 0.01, a.w);
      assert.strictEqual(e.w, 100);
      assert.deepStrictEqual([a.over, c.over, d.over], [false, false, true]);
      assert.notStrictEqual(a.colour, b.colour);
      assert.notStrictEqual(b.colour, d.colour);
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "freeze prompt: max(150 s, 3 × median untimed answer); once per question; never in the diagnostic",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      var ms = await page.evaluate(function () {
        var q = PMP.QUESTIONS[0], now = Date.now();
        function rows(ms, mode, n) {
          var out = [];
          for (var i = 0; i < n; i++) out.push({ id: "f" + i + mode, qid: q.id, type: "single", correct: true, credit: 1, ms: ms,
            at: new Date(now - 100000 + i).toISOString(), mode: mode, order: [] });
          return out;
        }
        var st = PMP.Store.defaults();
        var none = PMP.Practice.freezeMs(st);
        st.attempts = rows(30000, "practice", 10);
        var fast = PMP.Practice.freezeMs(st);
        st.attempts = rows(70000, "practice", 10).concat(rows(5000, "timed", 30));
        var slow = PMP.Practice.freezeMs(st);
        return [none, fast, slow];
      });
      assert.deepStrictEqual(ms, [228000, 150000, 210000], "no history reads as 76 s; timed answers are left out");
      await drill(page, 2);
      async function stall() {
        await page.evaluate(function () { var s = PMP.Practice.session; s.items[s.i].sig.last = -1e9; });
        await page.waitForTimeout(320);
        return page.$$eval("#drill-notice .q-callout--calm", function (x) { return x.map(function (n) { return n.textContent; }); });
      }
      var shown = await stall();
      assert.strictEqual(shown.length, 1);
      assert.ok(/^Take a breath\./.test(shown[0]) && /rule out two options/.test(shown[0]), shown[0]);
      await H.clickText(page, "#drill-notice button", "OK");
      assert.deepStrictEqual(await stall(), [], "once per question");
      await page.ctx.close();

      page = await h.open("fresh");
      await drill(page, 2, "diagnostic");
      assert.deepStrictEqual(await stall(), [], "not in the diagnostic");
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "checkpoint cards at questions 60 and 120 in sets of 60 or more",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      var lengths = await page.evaluate(function () { return PMP.PRACTICE.LENGTHS.slice(); });
      assert.deepStrictEqual(lengths, [5, 10, 20, 40, 60, 100]);
      await page.evaluate(function () {
        PMP.Practice.startWith(PMP.QUESTIONS.slice(0, 121), "practice");
        PMP.Router.navigate("practice");
      });
      await page.waitForTimeout(150);
      async function at(i) {
        await page.click('.navgrid-c[aria-label^="Question ' + (i + 1) + ',"]');
        await page.waitForTimeout(40);
        return page.textContent("#drill-notice");
      }
      assert.strictEqual(await at(59), "");
      var t = await at(60);
      assert.ok(/^Checkpoint: 60 questions done/.test(t) && /exam pace is 76\. You're on pace\./.test(t), t);
      assert.strictEqual(await at(61), "");
      assert.ok(/Checkpoint: 120 questions done/.test(await at(120)));
      await page.evaluate(function () {
        PMP.Practice.startWith(PMP.QUESTIONS.slice(0, 59), "practice");
      });
      await page.waitForTimeout(100);
      await lib.go(page, "#/today");
      await lib.go(page, "#/practice");
      await page.evaluate(function () { document.querySelectorAll(".navgrid-c")[58].click(); });
      assert.strictEqual(await page.textContent("#drill-notice"), "", "not in a shorter set");
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "reading drill: offered as a mode; the stem alone until asked; recorded as practice with the stem's words",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      await lib.go(page, "#/practice");
      assert.ok(await page.isVisible("#mode-reading"), "a mode in the picker");
      await drill(page, 2, "reading");
      var r = await page.evaluate(function () {
        var c = document.getElementById("drill-card");
        return { opts: c.querySelectorAll(".opt").length, visible: Array.prototype.filter.call(c.querySelectorAll(".opt"), function (o) { return o.offsetParent; }).length,
                 ask: c.querySelector(".q-read").textContent, hint: c.querySelectorAll(".q-hints").length };
      });
      assert.ok(r.opts > 0 && r.visible === 0, "options hidden");
      assert.ok(/what is this question asking you to decide\?/.test(r.ask));
      assert.strictEqual(r.hint, 0, "no hints in a reading drill");
      await page.waitForTimeout(1100);
      await H.clickText(page, "#drill-card button", "I know what it's asking: show the options");
      assert.ok(await page.isVisible("#drill-card .opt"));
      await pick(page, true);
      await conf(page, "Sure");
      var a = await lastAttempt(page);
      var words = await page.evaluate(function (qid) { return PMP.signals.words(PMP.Content.byId(qid), true); }, a.qid);
      assert.strictEqual(a.mode, "reading");
      assert.strictEqual(a.w, words, "the stem's words only");
      assert.ok(a.ft >= 1000, "reading time ends at show the options: " + a.ft);
      var out = await page.evaluate(function () { var d = PMP.Sync.toDoc(PMP.Store.state); return d.attempts[d.attempts.length - 1]; });
      assert.deepStrictEqual([out.src, out.wx && out.wx.mode], ["practice", "reading"]);
      /* Enter also shows them. */
      await page.click("#drill-card .btn--primary");
      await page.waitForTimeout(60);
      await page.focus("#drill-card");
      await page.keyboard.press("Enter");
      assert.ok(await page.isVisible("#drill-card .opt"));
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "reading pace under 90% of the exam's needs adds one reading drill a week to Today",
    fn: async function (h, lib) {
      var page = await h.open("day3");
      var r = await page.evaluate(function () {
        var now = Date.now(), rows = [];
        PMP.QUESTIONS.slice(0, 40).forEach(function (q, i) {
          rows.push({ id: "rd" + i, qid: q.id, ecoTask: q.ecoTask, domain: q.domain, type: "single", correct: true, credit: 1, ms: 70000,
                      at: new Date(now - 3 * 86400000 + i * 60000).toISOString(), mode: "practice", order: [], ft: 60000, w: 100 });
        });
        PMP.Store.commit(["attempts"], function (s) { s.attempts = s.attempts.concat(rows); });
        var slow = PMP.Planner.readingSlow(PMP.Store.state);
        var step = PMP.Coach.session(PMP.Store.state).steps.filter(function (s) { return s.id === "coach:reading"; })[0];
        return { slow: slow, step: step && { label: step.label, params: step.params } };
      });
      assert.strictEqual(r.slow, true);
      assert.ok(r.step, "the step is in today's session");
      assert.deepStrictEqual(r.step.params, { mode: "reading", n: 10 });
      assert.strictEqual(r.step.label, "Reading drill, 10 questions");
      var line = await page.evaluate(function () { return PMP.Coach.line("step_reading", { n: 10 }); });
      assert.strictEqual(line, "Reading drill, 10 questions");
      /* A reading drill earlier this week: none today. */
      var after = await page.evaluate(function () {
        var q = PMP.QUESTIONS[0], wk = PMP.Metrics.weekStart(Date.now()), today = PMP.time.startOfDay(Date.now());
        if (today === wk) return "monday";
        PMP.Store.commit(["attempts"], function (s) {
          s.attempts.push({ id: "rd-x", qid: q.id, type: "single", correct: true, credit: 1, ms: 50000, at: new Date(wk + 3600000).toISOString(), mode: "reading", order: [] });
        });
        return PMP.Coach.session(PMP.Store.state).steps.some(function (s) { return s.id === "coach:reading"; });
      });
      assert.ok(after === "monday" || after === false, String(after));
      await lib.clean(page);
      await page.ctx.close();
    }
  }
];
