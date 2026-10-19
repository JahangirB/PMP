"use strict";
/* The path on Today and the session player (§10.25): one button for the
   next planned session, the 15-minute version, upcoming sessions by day,
   the behind-plan options, the plan's inputs on #/planner, and a session
   played step by step through the modules that own each step, ending in a
   shared `sessions` row in the phones' shape. */
var assert = require("assert");
var H = require("./helpers");

var DAY = 86400000;

async function primaries(page) {
  return page.evaluate(function () {
    return Array.prototype.map.call(document.querySelectorAll("#view .btn--primary"), function (b) { return b.textContent.trim(); });
  });
}
async function heading(page) {
  return page.evaluate(function () { var h = document.getElementById("ss-step-h"); return h && h.textContent; });
}

module.exports = [
  {
    name: "planned_ontrack: the path home has its status, one Continue, the short version, days ahead and the ahead sheet",
    fn: async function (h, lib) {
      var page = await h.open("planned_ontrack");
      var r = await page.evaluate(function () {
        var plan = PMP.Path.plan(PMP.Store.state);
        return {
          status: document.querySelector(".pt-line").textContent,
          fit: document.querySelector(".pt-fit").getAttribute("href"),
          title: document.getElementById("pt-main-h").textContent, next: plan.next.title,
          short: Array.prototype.some.call(document.querySelectorAll(".pt-main button"), function (b) { return /15-minute version/.test(b.textContent); }),
          days: Array.prototype.map.call(document.querySelectorAll(".pt-day"), function (d) { return d.textContent; }).slice(0, 2),
          rows: document.querySelectorAll(".pt-row").length, steps: document.querySelectorAll(".td-step").length
        };
      });
      assert.ok(/^Foundations · readiness \d+ · \d+-day streak$/.test(r.status), r.status);
      assert.strictEqual(r.fit, "#/fitness");
      assert.strictEqual(r.title, r.next);
      assert.ok(r.short, "the 15-minute version is offered");
      assert.deepStrictEqual(r.days, ["Today", "Tomorrow"]);
      assert.ok(r.rows > 4);
      assert.strictEqual(r.steps, 0, "the old step list is gone");
      assert.deepStrictEqual(await primaries(page), ["Continue"]);
      /* A session further down opens the ahead sheet; Start now plays it. */
      var key = await page.evaluate(function () {
        var p = PMP.Path.plan(PMP.Store.state);
        document.querySelectorAll(".pt-row")[3].click();
        return p.sessions[4].key;
      });
      await page.waitForTimeout(80);
      var sheet = await page.evaluate(function () { return { open: document.getElementById("sheet").open, text: document.getElementById("sheet-body").textContent }; });
      assert.ok(sheet.open && /ahead of your plan/.test(sheet.text), JSON.stringify(sheet));
      await H.clickText(page, "#sheet-foot button", "Start now");
      var run = await page.evaluate(function () { return { route: PMP.Router.current.name, key: PMP.Session.run.s.key }; });
      assert.deepStrictEqual(run, { route: "session", key: key });
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "behind the plan: +2 hours a week and Add a study day write the shared plan and re-plan",
    fn: async function (h, lib) {
      var page = await h.open("planned_ontrack");
      await page.evaluate(function () {
        PMP.Path.setPlan({ hours: 1, days: [2] });
        PMP.Router.navigate("planner"); PMP.Router.navigate("today");
      });
      await page.waitForTimeout(100);
      var before = await page.evaluate(function () {
        return { card: !!document.querySelector(".pt-behind"), behind: PMP.Path.plan(PMP.Store.state).behind,
                 cal: PMP.Store.state.planner.inputs };
      });
      assert.ok(before.card && before.behind, JSON.stringify(before));
      /* The calendar view follows the path's hours and days. */
      assert.deepStrictEqual([before.cal.weekdayMin, before.cal.weekendMin], [60, 0]);
      await H.clickText(page, ".pt-behind button", "+2 hours a week");
      await H.clickText(page, ".pt-behind button", "Add a study day");
      var after = await page.evaluate(function () {
        var p = PMP.Store.state.shared.plan;
        return { hours: p.hours, days: p.days, doc: PMP.Sync.toDoc(PMP.Store.state).plan, prim: document.querySelectorAll("#view .btn--primary").length };
      });
      assert.strictEqual(after.hours, 3);
      assert.deepStrictEqual(after.days, [1, 2]);
      assert.strictEqual(after.doc.hours, 3);
      assert.deepStrictEqual(after.doc.days, [1, 2]);
      assert.ok(after.doc.examDate > 0);
      assert.strictEqual(after.prim, 1);
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "#/planner: hours a week and study days are set there; without them they come from the calendar's minutes",
    fn: async function (h, lib) {
      var page = await h.open("planned_ontrack", { hash: "#/planner" });
      var derived = await page.evaluate(function () {
        var p = PMP.Path.planOf(PMP.Store.state);
        return { hours: p.hours, days: p.days, source: p.source, field: document.getElementById("pt-hours").value,
                 cal: document.querySelector("#view .view-hd .eyebrow").textContent };
      });
      assert.deepStrictEqual(derived, { hours: 10, days: [1, 2, 3, 4, 5, 6, 7], source: "planner", field: "10", cal: "Calendar view" });
      await page.fill("#pt-hours", "6");
      await page.click('label[for="pt-day-1"]');
      await page.click('label[for="pt-day-7"]');
      var per = await page.evaluate(function () { return document.getElementById("pt-per").textContent; });
      assert.strictEqual(per, "6 hours a week · about 72 minutes a study day");
      await H.clickText(page, "#pt-plan button", "Save");
      var r = await page.evaluate(function () {
        var st = PMP.Store.state, p = PMP.Path.planOf(st);
        return { hours: p.hours, days: p.days, source: p.source, per: PMP.Path.plan(st).minutesPerDay,
                 cal: [st.planner.inputs.weekdayMin, st.planner.inputs.weekendMin], fit: PMP.Fitness.report(st).signal("hours").detail };
      });
      assert.deepStrictEqual([r.hours, r.days, r.source, r.per], [6, [2, 3, 4, 5, 6], "plan", 72]);
      assert.deepStrictEqual(r.cal, [72, 0]);
      assert.ok(/planned 6 hours a week/.test(r.fit), r.fit);
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "a session played through: check-in, warm-up cards, questions, reflect, debrief; Finish writes the phones' row and syncs",
    fn: async function (h, lib) {
      var page = await h.open("planned_ontrack");
      await page.evaluate(function () {
        var now = Date.now(), DAY = 86400000, c = PMP.FLASHCARDS[0];
        PMP.Store.commit(["srs"], function (s) {
          s.srs = {}; s.srs[c.id] = { ease: 2.5, interval: 1, reps: 1, lapses: 0, due: now - DAY, suspended: false, leech: false,
                                    first: now - 2 * DAY, last: now - 2 * DAY, reviews: 1 };
        });
        window.__synced = [];
        var real = PMP.Sync.auto;
        PMP.Sync.auto = function (why) { window.__synced.push(why); return real.apply(this, arguments); };
        PMP.Session.start({ key: "drill:people-1:0", kind: "DRILL", title: "Drill: Shared purpose", why: "test", minutes: 25,
          steps: [{ kind: "CARDS", count: 0 }, { kind: "PRACTICE", count: 4 }, { kind: "REFLECT", count: 0 }, { kind: "DEBRIEF", count: 0 }],
          taskId: "people-1", domain: "people", lessonId: null, day: 0, priority: 0, notBefore: 0 });
      });
      await page.waitForTimeout(80);
      assert.strictEqual(await heading(page), "How do you feel about studying right now?");
      await H.clickText(page, "#view .wb-face", "🙁");
      assert.strictEqual(await heading(page), "Want a two-minute reset first?");
      await H.clickText(page, "#view .ss-step button", "Skip");
      assert.strictEqual(await heading(page), "Warm-up cards");
      assert.deepStrictEqual(await primaries(page), ["Review 1 card"]);
      await H.clickText(page, "#view .ss-next button", "Review 1 card");
      assert.strictEqual(await page.evaluate(function () { return PMP.Router.current.name; }), "cards");
      await H.clickText(page, "#view button", "Show answer");
      await page.click('#view .grade[data-g="good"]');
      await page.waitForTimeout(100);
      /* No missed questions are due back, so the cards hand straight on to the questions. */
      var at = await page.evaluate(function () { return { route: PMP.Router.current.name, step: PMP.Session.run.step }; });
      assert.deepStrictEqual(at, { route: "session", step: 1 });
      assert.strictEqual(await heading(page), "4 questions");
      await H.clickText(page, "#view .ss-next button", "Start the questions");
      var drill = await page.evaluate(function () {
        var s = PMP.Practice.session;
        return { route: PMP.Router.current.name, n: s.items.length, tasks: s.items.every(function (i) { return i.q.ecoTask === "people-1"; }) };
      });
      assert.deepStrictEqual(drill, { route: "practice", n: 4, tasks: true });
      /* Answer every one wrong where it can be, so reflect has misses to tag. */
      await page.evaluate(function () {
        PMP.Practice.session.items.forEach(function (it) {
          if (it.q.type === "single") it.q.__wrong = it.p.options.filter(function (o) { return !o.correct; })[0].id;
        });
      });
      for (var i = 0; i < 4; i++) {
        await page.evaluate(function () {
          var s = PMP.Practice.session, it = s.items[s.i], card = document.getElementById("drill-card");
          if (it.q.__wrong) {
            var row = card.querySelector('.opt[data-opt="' + it.q.__wrong + '"] input');
            if (row) row.click();
          }
        });
        await H.answerOne(page);
        await page.waitForTimeout(40);
      }
      await page.waitForTimeout(100);
      var refl = await page.evaluate(function () {
        var r = PMP.Session.run;
        return { route: PMP.Router.current.name, answers: r.answers.length, wrong: r.answers.filter(function (a) { return !a.ok; }).length,
                 rows: document.querySelectorAll(".ss-miss").length };
      });
      assert.strictEqual(refl.route, "session");
      assert.strictEqual(refl.answers, 4);
      if (refl.wrong) {
        assert.strictEqual(await heading(page), "Why did these go wrong?");
        assert.strictEqual(refl.rows, refl.wrong);
        await page.evaluate(function () { document.querySelector(".ss-miss .jr-cause").click(); });
        var tagged = await page.evaluate(function () {
          var a = PMP.Session.run.answers.filter(function (x) { return !x.ok; })[0];
          var j = PMP.Store.state.journal.filter(function (e) { return e.attemptId === a.a; })[0];
          return { cause: j && j.cause, pressed: document.querySelector(".ss-miss .jr-cause").getAttribute("aria-pressed") };
        });
        assert.deepStrictEqual(tagged, { cause: "KNOWLEDGE_GAP", pressed: "true" });
        await H.clickText(page, "#view .ss-next button", "Continue");
      }
      assert.strictEqual(await heading(page), "Session done");
      var deb = await page.evaluate(function () { return document.querySelector(".ss-step").textContent; });
      assert.ok(/of 4 right/.test(deb) && /Next on your path/.test(deb), deb);
      assert.deepStrictEqual(await primaries(page), ["Finish"]);
      await H.clickText(page, "#view .ss-next button", "Finish");
      var done = await page.evaluate(function () {
        var st = PMP.Store.state;
        return { route: PMP.Router.current.name, rows: st.sessions, sitting: st.sittings[st.sittings.length - 1],
                 run: PMP.Session.run, synced: window.__synced, stored: localStorage.getItem(PMP.Session.RUN_KEY),
                 doc: PMP.Sync.toDoc(st).sessions, checkins: st.checkins.map(function (c) { return [c.kind, c.v]; }),
                 doneList: Array.prototype.map.call(document.querySelectorAll(".pt-did .td-l"), function (x) { return x.textContent; }) };
      });
      assert.strictEqual(done.route, "today");
      assert.strictEqual(done.rows.length, 1);
      var row = done.rows[0];
      assert.deepStrictEqual(Object.keys(row).sort(), ["at", "d", "k", "kind", "m", "s", "t"]);
      assert.deepStrictEqual([row.k, row.kind, row.t, row.d, row.m], ["drill:people-1:0", "DRILL", "people-1", "people", 5]);
      assert.ok(typeof row.s === "number" && row.s >= 0 && row.s <= 1 && typeof row.at === "number");
      assert.deepStrictEqual([done.sitting.k, done.sitting.kind, done.sitting.p, done.sitting.a, done.sitting.f], ["drill:people-1:0", "path", 4, 4, true]);
      assert.deepStrictEqual(done.doc, done.rows, "the shared row is the web row, as it is");
      assert.strictEqual(done.run, null);
      assert.strictEqual(done.stored, null);
      assert.ok(done.synced.indexOf("session") >= 0, JSON.stringify(done.synced));
      assert.deepStrictEqual(done.checkins, [["pre", 2]]);
      assert.deepStrictEqual(done.doneList, ["Drill: Shared purpose"]);
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "a lesson step reads inline without the practice set, marks the lesson done, and the path moves on",
    fn: async function (h, lib) {
      var page = await h.open("planned_ontrack");
      await H.clickText(page, "#view .pt-main button", "Continue");
      await H.clickText(page, "#view .ss-step button", "Skip");
      var r = await page.evaluate(function () {
        return { title: document.getElementById("ln-title").textContent, parts: document.querySelectorAll(".ss-lesson .ln-part").length,
                 set: !!document.getElementById("ln-part-10"), strip: document.querySelectorAll(".coach-strip").length,
                 focus: document.activeElement && document.activeElement.id };
      });
      assert.strictEqual(r.title, "How the exam works and how to study with this suite");
      assert.strictEqual(r.parts, 9);
      assert.strictEqual(r.set, false);
      assert.strictEqual(r.strip, 0);
      assert.strictEqual(r.focus, "ln-title", "focus moves to the step");
      await H.clickText(page, "#view .ss-next button", "I've read it");
      await H.clickText(page, "#view .ss-next button", "Finish");
      var after = await page.evaluate(function () {
        var st = PMP.Store.state;
        return { done: !!st.lessons.F1.completedAt, shared: PMP.Sync.toDoc(st).lessons, next: PMP.Path.plan(st).next.key,
                 skipped: !PMP.Wellbeing.preDue(st), pre: !!document.getElementById("wb-pre") };
      });
      assert.deepStrictEqual(after, { done: true, shared: ["F1"], next: "lesson:F2", skipped: true, pre: false });
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "a session done on a phone is done here: its key, kind and lesson come in through the shared document",
    fn: async function (h, lib) {
      var page = await h.open("planned_ontrack");
      var r = await page.evaluate(function () {
        var st = PMP.Store.state, before = PMP.Path.plan(st);
        var doc = PMP.Sync.toDoc(st), now = Date.now();
        doc.lessons = ["F1"];
        doc.sessions = [{ k: "lesson:F1", kind: "FOUNDATION", t: "", d: "", s: 1, m: 30, at: now - 3600000 },
                        { k: "checkpoint:people", kind: "CHECKPOINT", t: "", d: "people", s: 0.8, m: 35, at: now - 1800000 }];
        var view = PMP.Store.defaults(), next = PMP.Sync.fromDoc(doc, st);
        Object.keys(next).forEach(function (k) { view[k] = next[k]; });
        var after = PMP.Path.plan(view);
        var keys = after.sessions.map(function (s) { return s.key; });
        return { before: before.next.key, after: after.next.key,
                 f1: keys.indexOf("lesson:F1"), people: keys.indexOf("checkpoint:people"), process: keys.indexOf("checkpoint:process") >= 0,
                 back: PMP.Sync.canon(PMP.Sync.toDoc(view).sessions) === PMP.Sync.canon(doc.sessions),
                 title: PMP.Path.titleFor("lesson:F1") };
      });
      assert.strictEqual(r.before, "lesson:F1");
      assert.strictEqual(r.after, "lesson:F2");
      assert.strictEqual(r.f1, -1);
      assert.strictEqual(r.people, -1, "a passed checkpoint is not planned again");
      assert.ok(r.process);
      assert.ok(r.back, "the phone's rows go back out unchanged");
      assert.strictEqual(r.title, "How the exam works and how to study with this suite");
      await page.ctx.close();
    }
  },
  {
    name: "Close keeps the place across a reload; a mock step offers the sitting; #/session without a run says so",
    fn: async function (h, lib) {
      var page = await h.open("planned_ontrack", { hash: "#/session" });
      var empty = await page.evaluate(function () { return document.querySelector("#view .empty h3").textContent; });
      assert.strictEqual(empty, "No session in progress");
      await page.evaluate(function () {
        PMP.Session.start({ key: "mock:1", kind: "MOCK", title: "Full mock exam 1", why: "", minutes: 270,
          steps: [{ kind: "MOCK", count: 0 }, { kind: "DEBRIEF", count: 0 }], taskId: null, domain: null, lessonId: null, day: 0, priority: 3, notBefore: 0 });
      });
      await page.waitForTimeout(80);
      await H.clickText(page, "#view .ss-step button", "Skip");
      assert.strictEqual(await heading(page), "Full mock exam");
      assert.deepStrictEqual(await primaries(page), ["Start the mock"]);
      await page.click("#view .ss-close");
      await page.reload();
      await page.waitForFunction(function () { return window.PMP && PMP.Store.state.sys.ready; });
      await page.waitForTimeout(80);
      var r = await page.evaluate(function () {
        return { eyebrow: document.querySelector(".pt-main .eyebrow").textContent, title: document.getElementById("pt-main-h").textContent,
                 prim: Array.prototype.map.call(document.querySelectorAll("#view .btn--primary"), function (b) { return b.textContent; }) };
      });
      assert.deepStrictEqual(r, { eyebrow: "In progress", title: "Full mock exam 1", prim: ["Resume"] });
      await H.clickText(page, "#view .pt-main button", "Resume");
      assert.strictEqual(await heading(page), "Full mock exam");
      await H.clickText(page, "#view .ss-next button", "Start the mock");
      var sheet = await page.evaluate(function () { return { route: PMP.Router.current.name, t: document.getElementById("sheet-title").textContent }; });
      assert.deepStrictEqual(sheet, { route: "exam", t: "Start the sitting?" });
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "390px: the path home and the player fit with no horizontal scroll",
    fn: async function (h, lib) {
      var page = await h.open("planned_ontrack", { viewport: { width: 390, height: 844 } });
      var a = await page.evaluate(function () { return document.documentElement.scrollWidth - window.innerWidth; });
      assert.ok(a <= 0, "today overflow " + a);
      await H.clickText(page, "#view .pt-main button", "Continue");
      await H.clickText(page, "#view .ss-step button", "Skip");
      var b = await page.evaluate(function () { return document.documentElement.scrollWidth - window.innerWidth; });
      assert.ok(b <= 0, "session overflow " + b);
      await lib.clean(page);
      await page.ctx.close();
    }
  }
];
