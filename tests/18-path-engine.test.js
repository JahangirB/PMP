"use strict";
/* The guided path's engine (§10.24), run in page context on synthetic
   histories. Each case mirrors one in android/app/src/test/.../
   PlanEngineTest.kt, GapsTest.kt or StudyToolsTest.kt (missed-question
   review): same inputs, same outputs. Simulated learners study day by day,
   doing exactly what the plan says at a fixed level of skill. */
var assert = require("assert");

/* Installed into the page as window.__L: PlanEngineTest's Learner, line for
   line. The content is the app's own (§10.24 extras), which is what the
   phones' extras.json is copied from. */
function install() {
  var P = PMP.Path, x = P.extras(), DAY = 86400000;
  var weekdays = [2, 3, 4, 5, 6];
  /* People 5 of 8, Process 4 of 10, Business 4 of 7. */
  var diag = { people: 5 / 8, process: 4 / 10, business: 4 / 7 };
  /* A Monday at 9am, so weekday maths is predictable. */
  var start = new Date(2027, 2, 1, 9, 0, 0, 0).getTime();
  function sod(t) { return PMP.time.startOfDay(t); }
  function days(n) { return n * DAY; }
  function weekday(t) { return new Date(t).getDay() + 1; }
  function task(id) { return x.tasks.filter(function (t) { return t.id === id; })[0]; }

  function Learner(settings, skill) {
    this.settings = settings; this.skill = skill;
    this.attempts = []; this.lessons = {}; this.results = []; this.mocks = [];
    this.tags = {}; this.leeches = {}; this.ms = 60000; this.ladderNext = null; this.readingSlow = false;
  }
  Learner.prototype.plan = function (now) {
    return P.build({ x: x, attempts: this.attempts, lessonsDone: Object.keys(this.lessons), results: this.results,
      mocks: this.mocks, diagnostic: diag, settings: this.settings, now: now, mistakeTags: this.tags,
      leeches: this.leeches, ladderNext: this.ladderNext, readingSlow: this.readingSlow });
  };
  Learner.prototype.answer = function (id, n, score, at, source) {
    var t = task(id), right = Math.round(n * score);
    for (var i = 0; i < n; i++) {
      this.attempts.push({ qid: id + "-" + this.attempts.length, ecoTask: id, domain: t.domain, approach: null,
        credit: i < right ? 1 : 0, correct: i < right, ms: this.ms, at: at + i, source: source || "path",
        luckyGuess: false, hinted: false });
    }
  };
  Learner.prototype.doSession = function (s, at, score) {
    if (score === undefined) score = this.skill;
    var self = this, rr = 0;
    s.steps.forEach(function (st) {
      if (st.kind === "LESSON" && s.lessonId) self.lessons[s.lessonId] = true;
      else if (st.kind === "PRACTICE") {
        var pool = s.taskId ? [s.taskId]
          : s.domain ? x.tasks.filter(function (t) { return t.domain === s.domain; }).map(function (t) { return t.id; })
          : x.tasks.map(function (t) { return t.id; });
        var right = Math.round(st.count * score);
        for (var i = 0; i < st.count; i++) self.answer(pool[(rr++) % pool.length], 1, i < right ? 1 : 0, at + i);
      } else if (st.kind === "MOCK") self.mocks.push({ at: at, n: 180, right: Math.round(180 * score) });
    });
    this.results.push({ key: s.key, kind: s.kind, taskId: s.taskId, domain: s.domain, score: score, minutes: s.minutes, at: at });
  };
  /* Does today's planned sessions, as the Continue button would serve them. */
  Learner.prototype.studyDay = function (now, score) {
    var p = this.plan(now), today = sod(now), t = now, guard = 0;
    while (guard++ < 10) {
      var next = this.plan(t).sessions.filter(function (s) { return s.day === today; })[0];
      if (!next) break;
      this.doSession(next, t, score);
      t += 60000 * next.minutes;
    }
    return p;
  };
  function settings(exam, hours, d) { return { examDate: exam, hoursPerWeek: hours, studyDays: d || weekdays }; }
  window.__L = { P: P, x: x, DAY: DAY, start: start, days: days, sod: sod, weekday: weekday, weekdays: weekdays,
                 Learner: Learner, settings: settings, task: task };
}

async function engine(h) {
  var page = await h.open("fresh");
  await page.evaluate("(" + install.toString() + ")()");
  return page;
}

function test(name, body) {
  return {
    name: name,
    fn: async function (h, lib) {
      var page = await engine(h);
      var r = await page.evaluate(body);
      await page.ctx.close();
      if (r !== true) throw new Error(typeof r === "string" ? r : JSON.stringify(r));
    }
  };
}

module.exports = [
  test("a diligent learner covers everything, takes two mocks, passes each checkpoint and tapers", function () {
    var L = __L, exam = L.start + L.days(70), l = new L.Learner(L.settings(exam, 8), 0.9), t = L.start;
    while (t < exam - L.days(1)) {
      if (L.weekdays.indexOf(L.weekday(t)) >= 0) {
        var p = l.plan(t), today = L.sod(t);
        if (!p.sessions.every(function (s) { return s.day < L.sod(exam); })) return "planned on or after exam day";
        var byDay = {};
        p.sessions.filter(function (s) { return s.kind !== "MOCK"; }).forEach(function (s) { byDay[s.day] = (byDay[s.day] || 0) + s.minutes; });
        for (var d in byDay) if (byDay[d] > p.minutesPerDay + 60) return "day over budget: " + byDay[d] + " vs " + p.minutesPerDay;
        if (exam - t <= L.days(7)) {
          var todays = p.sessions.filter(function (s) { return s.day === today; });
          if (!todays.every(function (s) { return s.kind === "TAPER"; })) return "taper only: " + todays.map(function (s) { return s.key; });
        }
        l.studyDay(t);
      }
      t = L.P.nextDay(t);
    }
    if (Object.keys(l.lessons).length !== L.x.lessons.length) return "every lesson read: " + Object.keys(l.lessons).length;
    var thin = L.P.taskStates(L.x, l.attempts, t).filter(function (s) { return s.answers < 10; });
    if (thin.length) return "every task practised to the bar: " + thin.map(function (s) { return s.task.id; });
    if (l.mocks.length < 2) return "at least two full mocks: " + l.mocks.length;
    var failed = PMP.ECO.DOMAINS.filter(function (d) {
      return !l.results.some(function (r) { return r.kind === "CHECKPOINT" && r.domain === d && r.score >= 0.7; });
    });
    return failed.length ? "checkpoint not passed: " + failed : true;
  }),

  test("the weakest diagnostic domain comes first", function () {
    var L = __L, l = new L.Learner(L.settings(L.start + L.days(70), 8), 0.9);
    L.x.lessons.filter(function (x) { return x.module === "foundations"; }).forEach(function (x) { l.lessons[x.id] = true; });
    var first = l.plan(L.start).sessions.filter(function (s) { return s.kind === "BUILD"; })[0];
    /* Process was 40% in the diagnostic, the lowest. */
    return first.domain === "process" || first.domain;
  }),

  test("a week off brings a warm-up first, keeps everything before the exam, and is not offered again", function () {
    var L = __L, exam = L.start + L.days(60), l = new L.Learner(L.settings(exam, 8), 0.85), t = L.start;
    for (var i = 0; i < 10; i++) { if (L.weekdays.indexOf(L.weekday(t)) >= 0) l.studyDay(t); t = L.P.nextDay(t); }
    t += L.days(8);
    while (L.weekdays.indexOf(L.weekday(t)) < 0) t = L.P.nextDay(t);
    var p = l.plan(t);
    if (p.sessions[0].kind !== "REENTRY") return "first is " + p.sessions[0].kind;
    if (!/^reentry:\d+$/.test(p.sessions[0].key)) return "key " + p.sessions[0].key;
    if (!p.sessions.every(function (s) { return s.day < L.sod(exam); })) return "after the exam";
    if (!p.adjustments.some(function (a) { return /Re-entry/.test(a); })) return "no adjustment";
    l.studyDay(t);
    return !l.plan(L.P.nextDay(t)).sessions.some(function (s) { return s.kind === "REENTRY"; }) || "offered again";
  }),

  test("losing three weeks trims or flags the plan but keeps two mocks", function () {
    var L = __L, l = new L.Learner(L.settings(L.start + L.days(30), 5), 0.8);
    var p = l.plan(L.start + L.days(18));
    if (!(p.dropped.length || p.behind)) return "neither trimmed nor flagged";
    if (!(p.neededMinutes > 0)) return "no work";
    if (p.sessions.filter(function (s) { return s.kind === "MOCK"; }).length < 2) return "two mocks kept";
    if (p.behind && !p.adjustments.some(function (a) { return /hours of work/.test(a); })) return "behind without saying so";
    return true;
  }),

  test("a failed session is fixed first the next day, with easier questions, and goes once fixed", function () {
    var L = __L, l = new L.Learner(L.settings(L.start + L.days(60), 8), 0.9);
    L.x.lessons.filter(function (x) { return x.module === "foundations"; }).forEach(function (x) { l.lessons[x.id] = true; });
    var build = l.plan(L.start).sessions.filter(function (s) { return s.kind === "BUILD"; })[0];
    l.doSession(build, L.start, 0.4);
    var d1 = L.P.nextDay(L.start), next = l.plan(d1).sessions[0];
    if (next.kind !== "REMEDIATION" || next.taskId !== build.taskId) return "next is " + next.key;
    if (!next.steps.some(function (s) { return s.kind === "PRACTICE" && s.easy; })) return "no easy practice";
    if (next.key !== "remediation:" + build.taskId + ":" + L.start) return "key " + next.key;
    l.doSession(next, d1, 0.9);
    return !l.plan(L.P.nextDay(d1)).sessions.some(function (s) { return s.kind === "REMEDIATION" && s.taskId === build.taskId; }) || "still remediating";
  }),

  test("a middling session is not repeated the same day", function () {
    var L = __L, l = new L.Learner(L.settings(L.start + L.days(60), 8), 0.9), task = L.x.tasks[0].id;
    L.x.lessons.forEach(function (x) { l.lessons[x.id] = true; });
    l.answer(task, 10, 0.5, L.start - L.days(1));
    var drill = l.plan(L.start).sessions.filter(function (s) { return s.kind === "DRILL" && s.taskId === task; })[0];
    l.doSession(drill, L.start, 0.65);
    var again = l.plan(L.start + 3600000).sessions.filter(function (s) { return s.taskId === task && s.kind === "DRILL"; });
    return (again.length > 0 && again.every(function (s) { return s.day > L.sod(L.start); })) || "drilled again today";
  }),

  test("practice outside the path counts: a task mastered there needs no session", function () {
    var L = __L, l = new L.Learner(L.settings(L.start + L.days(60), 8), 0.9), t = L.task("business-5");
    l.lessons[L.x.lessons.filter(function (x) { return x.ecoTask === t.id; })[0].id] = true;
    if (!l.plan(L.start).sessions.some(function (s) { return s.taskId === t.id; })) return "nothing planned before";
    l.answer(t.id, 15, 0.9, L.start, "practice");
    var p = l.plan(L.start + 60000);
    if (p.sessions.some(function (s) { return s.taskId === t.id; })) return "still planned";
    if (p.tasks.filter(function (s) { return s.task.id === t.id; })[0].mastery !== "MASTERED") return "not mastered";
    return p.adjustments.some(function (a) { return a.indexOf(t.label) >= 0 && a.indexOf("own practice") >= 0; }) || "no adjustment";
  }),

  test("mastered tasks fade after the shared fadeDays and come back as a review", function () {
    var L = __L, l = new L.Learner(L.settings(L.start + L.days(90), 8), 0.9), t = L.task("people-2");
    l.lessons[L.x.lessons.filter(function (x) { return x.ecoTask === t.id; })[0].id] = true;
    l.answer(t.id, 15, 0.9, L.start);
    if (l.plan(L.start + L.days(1)).sessions.some(function (s) { return s.taskId === t.id; })) return "planned while mastered";
    return l.plan(L.start + L.days(20)).sessions.some(function (s) { return s.kind === "REVIEW" && s.key === "review:" + t.id; }) || "no review";
  }),

  test("a domain that slips after its checkpoint is re-checked", function () {
    var L = __L, l = new L.Learner(L.settings(L.start + L.days(60), 10), 0.8);
    l.results.push({ key: "checkpoint:people", kind: "CHECKPOINT", taskId: null, domain: "people", score: 0.8, minutes: 35, at: L.start });
    var people = L.x.tasks.filter(function (t) { return t.domain === "people"; }).map(function (t) { return t.id; });
    people.forEach(function (t, i) { l.answer(t, 5, 0.8, L.start + 3600000 + i * 1000); });
    if (l.plan(L.start + L.days(1)).sessions.some(function (s) { return s.title === "Re-check: People"; })) return "re-check while holding up";
    people.forEach(function (t, i) { l.answer(t, 4, 0.25, L.start + L.days(1) + i * 1000); });
    var r = l.plan(L.start + L.days(2)).sessions.filter(function (s) { return s.title === "Re-check: People"; })[0];
    return (r && r.key === "checkpoint:people:" + L.start) || "no re-check";
  }),

  test("tagged causes shape the task's drills: knowledge gap puts the lesson first, mindset adds scenarios", function () {
    var L = __L, t = L.x.tasks.filter(function (t) { return L.x.lessons.some(function (x) { return x.ecoTask === t.id; }); })[0];
    var l = new L.Learner(L.settings(L.start + L.days(60), 10), 0.5);
    l.lessons[L.x.lessons.filter(function (x) { return x.ecoTask === t.id; })[0].id] = true;
    l.answer(t.id, 12, 0.5, L.start);
    l.attempts.filter(function (a) { return !a.correct; }).slice(0, 3).forEach(function (a) { l.tags[a.qid] = "KNOWLEDGE_GAP"; });
    var drill = l.plan(L.start + L.days(1)).sessions.filter(function (s) { return s.key === "drill:" + t.id + ":0"; })[0];
    if (drill.steps[0].kind !== "LESSON") return "knowledge gap: " + drill.steps[0].kind;
    Object.keys(l.tags).forEach(function (q) { l.tags[q] = "WRONG_MINDSET"; });
    var drill2 = l.plan(L.start + L.days(1)).sessions.filter(function (s) { return s.key === "drill:" + t.id + ":0"; })[0];
    return drill2.steps.some(function (s) { return s.kind === "SCENARIOS" && s.count === 3; }) || "no scenarios";
  }),

  test("accurate but slow gets a paced drill", function () {
    var L = __L, t = L.x.tasks.filter(function (t) { return L.x.lessons.some(function (x) { return x.ecoTask === t.id; }); })[0];
    var l = new L.Learner(L.settings(L.start + L.days(60), 10), 1);
    l.lessons[L.x.lessons.filter(function (x) { return x.ecoTask === t.id; })[0].id] = true;
    l.answer(t.id, 15, 1, L.start);
    if (l.plan(L.start + L.days(1)).sessions.some(function (s) { return s.key === "pace:" + t.id; })) return "paced while quick";
    l.ms = 150000;
    l.answer(t.id, 15, 1, L.start + 1000);
    var s = l.plan(L.start + L.days(1)).sessions.filter(function (s) { return s.key === "pace:" + t.id; })[0];
    return (s && s.steps[0].timed) || "no paced drill";
  }),

  test("leech cards bring the lesson back once, until another card slips", function () {
    var L = __L, t = L.x.tasks.filter(function (t) { return L.x.lessons.some(function (x) { return x.ecoTask === t.id; }); })[0];
    var l = new L.Learner(L.settings(L.start + L.days(60), 10), 1);
    l.lessons[L.x.lessons.filter(function (x) { return x.ecoTask === t.id; })[0].id] = true;
    l.answer(t.id, 15, 1, L.start);
    l.leeches[t.id] = 2;
    var s = l.plan(L.start + L.days(1)).sessions.filter(function (s) { return s.key === "relearn:" + t.id + ":2"; })[0];
    if (!s || s.steps[0].kind !== "LESSON") return "no relearn";
    l.doSession(s, L.start + L.days(1));
    return !l.plan(L.start + L.days(2)).sessions.some(function (s) { return s.key.indexOf("relearn:" + t.id) === 0; }) || "relearn again";
  }),

  test("exam fitness adds one stamina sitting and one reading drill a week", function () {
    var L = __L, l = new L.Learner(L.settings(L.start + L.days(60), 10), 0.8);
    l.ladderNext = 45; l.readingSlow = true;
    var p = l.plan(L.start);
    var ladder = p.sessions.filter(function (s) { return s.key.indexOf("ladder:45:") === 0; })[0];
    if (!ladder || ladder.steps[0].count !== 45 || !ladder.steps[0].timed) return "ladder " + JSON.stringify(ladder);
    if (ladder.key !== "ladder:45:" + Math.floor(L.sod(L.start) / (7 * L.DAY))) return "ladder key " + ladder.key;
    var reading = p.sessions.filter(function (s) { return s.key.indexOf("reading:") === 0; })[0];
    if (!reading || !reading.steps[0].reading) return "reading";
    l.doSession(ladder, L.start);
    if (l.plan(L.start + L.days(2)).sessions.some(function (s) { return s.key.indexOf("ladder:") === 0; })) return "twice a week";
    return l.plan(L.start + L.days(7)).sessions.some(function (s) { return s.key.indexOf("ladder:") === 0; }) || "not back next week";
  }),

  /* GapsTest */
  test("the weakest approach is found only when clearly behind", function () {
    function answers(n, score, ap) {
      var out = [];
      for (var i = 0; i < n; i++) out.push({ qid: ap + i, approach: ap, credit: i < Math.round(n * score) ? 1 : 0, source: "practice" });
      return out;
    }
    var even = answers(20, 0.8, "predictive").concat(answers(20, 0.75, "agile"));
    if (PMP.Path.weakApproach(even) !== null) return "even read as weak";
    var agileWeak = answers(20, 0.9, "predictive").concat(answers(20, 0.4, "agile"));
    return PMP.Path.weakApproach(agileWeak) === "agile" || "missed agile";
  }),

  test("practice leans toward the weak approach without excluding the others", function () {
    var qs = PMP.QUESTIONS.filter(function (q) { return q.approach; }).slice(0, 60);
    var picked = PMP.Path.order(qs, function () { return null; }, "agile", 20, PMP.rng.seeded(1));
    var agile = picked.filter(function (q) { return q.approach === "agile"; }).length;
    var share = qs.filter(function (q) { return q.approach === "agile"; }).length / qs.length;
    if (!((agile >= Math.round(share * 20) && agile <= 10) || share > 0.5)) return "agile " + agile + " of 20, normal " + share;
    return picked.some(function (q) { return q.approach !== "agile"; }) || "only agile";
  }),

  test("unseen come before missed before right", function () {
    var qs = PMP.QUESTIONS.slice(0, 9);
    var last = {}; last[qs[0].id] = true; last[qs[1].id] = false; last[qs[2].id] = true;
    var picked = PMP.Path.order(qs, function (id) { return id in last ? last[id] : null; }, null, 9, PMP.rng.seeded(2));
    if (picked.slice(0, 6).some(function (q) { return q.id in last; })) return "seen before unseen";
    return picked[6].id === qs[1].id || "missed not seventh";
  }),

  /* StudyToolsTest: the missed-question ladder */
  test("a missed question comes back after 1 day, then 3, 7 and 21, then graduates; guesses and wrongs don't advance it", function () {
    var P = PMP.Path, D = 86400000, sod = PMP.time.startOfDay;
    var now = new Date(2027, 2, 10, 12, 0, 0, 0).getTime();
    function a(at, right, o) { return Object.assign({ qid: "q", at: at, correct: right, luckyGuess: false, hinted: false, source: "practice" }, o || {}); }
    var miss = a(now, false), s0 = P.reviewState([miss]);
    if (s0.step !== 0 || s0.due !== sod(now + D)) return "first " + JSON.stringify(s0);
    if (P.reviewState([miss, a(now + 3600000, true)]).step !== 0) return "same day moved it";
    var h = [miss], t = now, ladder = [[1, 3], [2, 7], [3, 21]];
    for (var i = 0; i < ladder.length; i++) {
      t = P.reviewState(h).due + 10 * 3600000;
      h = h.concat([a(t, true)]);
      var s = P.reviewState(h);
      if (s.step !== ladder[i][0] || s.due !== sod(sod(t) + ladder[i][1] * D + D / 2)) return "rung " + i + " " + JSON.stringify(s);
    }
    t = P.reviewState(h).due + 3600000;
    if (P.reviewState(h.concat([a(t, true)])) !== null) return "did not graduate";
    var due = P.reviewState([miss]).due;
    if (P.reviewState([miss, a(due + 1000, true, { luckyGuess: true })]).step !== 0) return "a guess advanced it";
    var again = P.reviewState([miss, a(due + 1000, true), a(due + 2 * D, false)]);
    if (again.step !== 0 || again.due !== sod(due + 2 * D + D)) return "wrong did not restart";
    return P.reviewState([Object.assign(a(now, true), { qid: "x" })]) === null || "never missed has a state";
  }),

  test("reviews due are most overdue first, five a day", function () {
    var P = PMP.Path, D = 86400000, now = new Date(2027, 2, 10, 12, 0, 0, 0).getTime();
    function a(q, at, right, src) { return { qid: q, at: at, correct: right, luckyGuess: false, hinted: false, source: src || "practice" }; }
    var atts = [];
    for (var i = 1; i <= 8; i++) atts.push(a("q" + i, now - (2 + i) * D, false));
    atts.push(a("fresh", now, false));
    var due = P.reviewsDue(atts, now);
    if (due.length !== 8 || due[0].qid !== "q8") return "due " + due.map(function (d) { return d.qid; });
    if (P.reviewAllowance(atts, now) !== 5) return "allowance";
    var reviewed = atts.concat([a("q1", now, true, "review"), a("q2", now, false, "review")]);
    return P.reviewAllowance(reviewed, now) === 3 || "allowance after two";
  }),

  test("spread puts the last mock as late as possible; the short version keeps the key under short:", function () {
    var P = PMP.Path;
    if (JSON.stringify(P.spread([10, 20, 30, 40, 50], 2, 0)) !== JSON.stringify([30, 50])) return "spread " + P.spread([10, 20, 30, 40, 50], 2, 0);
    if (JSON.stringify(P.spread([10], 3, 0)) !== JSON.stringify([10, 10, 10])) return "few free days";
    if (JSON.stringify(P.spread([], 2, 7)) !== JSON.stringify([7, 7])) return "no free days";
    var s = P.shortVersion({ key: "build:people-1", kind: "BUILD", title: "P1 · Shared purpose", why: "", minutes: 50,
      steps: [], taskId: "people-1", domain: "people", lessonId: "L2", day: 0, priority: 0, notBefore: 0 });
    if (s.key !== "short:build:people-1" || s.minutes !== 15 || s.kind !== "BUILD") return JSON.stringify(s);
    if (s.steps.map(function (x) { return x.kind + x.count; }).join() !== "CARDS0,PRACTICE10,DEBRIEF0") return "steps";
    return P.titleFor("short:drill:people-1:0") === "Short: Drill: Shared purpose" || P.titleFor("short:drill:people-1:0");
  })
];
