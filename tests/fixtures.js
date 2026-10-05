/* Fixture builders. Evaluated inside a booted copy of index.html (see
   lib.js), so they can read the real question bank and call the real
   planner. Every date is relative to Date.now(), so a fixture means the same
   thing whatever day the suite runs. Returns { fixtureName: { slice: data } }. */
(function () {
  "use strict";
  var DAY = 86400000;
  var now = Date.now();
  var sod = PMP.time.startOfDay;
  var today = sod(now);
  var Q = PMP.QUESTIONS;

  function iso(ms) { return new Date(ms).toISOString(); }
  /* A yyyy-mm-dd date in local time, the shape the date inputs write. */
  function ymd(offsetDays) {
    var d = new Date(today + offsetDays * DAY + 12 * 3600000);
    return d.getFullYear() + "-" + PMP.fmt.pad(d.getMonth() + 1) + "-" + PMP.fmt.pad(d.getDate());
  }
  /* Mid-morning on a day relative to today, plus a few minutes per item so
     rows on one day keep their order. */
  function at(dayOffset, i) { return today + dayOffset * DAY + 10 * 3600000 + (i || 0) * 90000; }

  var seq = 0;
  function attempt(q, ms, correct, extra) {
    seq++;
    var a = {
      id: "att-fx-" + seq, qid: q.id, ecoTask: q.ecoTask || null, domain: q.domain || null,
      approach: q.approach || null, type: PMP.QRender.base(q.type), difficulty: q.difficulty || null,
      correct: !!correct, credit: correct ? 1 : 0, ms: 60000, at: iso(ms), confidence: null,
      mode: "practice", order: (q.options || []).map(function (o, i) { return i; })
    };
    if (extra) Object.keys(extra).forEach(function (k) { a[k] = extra[k]; });
    return a;
  }
  /* n attempts spread over the given day offsets, cycling through the bank
     with a stride so tasks vary, right roughly `rate` of the time. */
  function attempts(n, days, rate, start) {
    var out = [];
    for (var i = 0; i < n; i++) {
      var q = Q[((start || 0) + i * 7) % Q.length];
      var d = days[i % days.length];
      out.push(attempt(q, at(d, i), (i % 10) < Math.round((rate || 0.6) * 10)));
    }
    out.sort(function (a, b) { return Date.parse(a.at) - Date.parse(b.at); });
    return out;
  }
  function range(from, to) { var o = []; for (var d = from; d <= to; d++) o.push(d); return o; }

  function coach(profileDaysAgo, extra) {
    var c = {
      profile: { at: iso(today - profileDaysAgo * DAY + 9 * 3600000), experience: "some", agile: "some", when: "evening", why: "" },
      shown: {}, dismissStreak: {}, lastWeekly: null, weekly: [], focus: null, muted: false, hints: true
    };
    if (extra) Object.keys(extra).forEach(function (k) { c[k] = extra[k]; });
    return c;
  }
  function settings(examOffset) {
    var s = PMP.Store.defaults().settings;
    s.examDate = examOffset === null ? null : ymd(examOffset);
    return s;
  }

  /* Builds a plan as if the planner had been run `daysAgo` days ago, by
     shifting the clock the planner reads for the length of the call. */
  function plan(slices, daysAgo, inputs) {
    var state = PMP.Store.defaults();
    Object.keys(slices).forEach(function (k) { state[k] = slices[k]; });
    var realNow = Date.now;
    Date.now = function () { return realNow() - daysAgo * DAY; };
    var p;
    try { p = PMP.Planner.build(inputs, state); } finally { Date.now = realNow; }
    if (p.error) throw new Error("fixture plan: " + p.error);
    return {
      generatedAt: iso(now - daysAgo * DAY), inputs: p.inputs, diagnostic: p.diagnostic,
      examAt: p.examAt, days: p.days, coverageWanted: p.coverageWanted, coverageLeft: p.coverageLeft,
      weeks: [], done: {}
    };
  }
  function inputs(examOffset) { return { examDate: ymd(examOffset), weekdayMin: 60, weekendMin: 150 }; }

  var F = {};

  F.fresh = {};

  F.day3 = {
    attempts: attempts(40, [-2, -1, 0], 0.6),
    coach: coach(3)
  };

  F.planned_ontrack = { settings: settings(60), coach: coach(1, { lastWeekly: iso(now - DAY) }) };
  F.planned_ontrack.planner = plan(F.planned_ontrack, 0, inputs(60));

  F.planned_behind = {
    attempts: attempts(5, [-1], 0.6, 3),
    settings: settings(50),
    coach: coach(4)
  };
  F.planned_behind.planner = plan({ attempts: [] }, 4, inputs(50));

  F.gap10 = {
    attempts: attempts(30, range(-14, -10), 0.6),
    settings: settings(40),
    coach: coach(20, { lastWeekly: iso(now - 12 * DAY) })
  };
  F.gap10.planner = plan({ attempts: [] }, 15, inputs(40));

  F.examweek = {
    attempts: attempts(120, range(-30, -1), 0.65),
    settings: settings(4),
    coach: coach(30, { lastWeekly: iso(now - 2 * DAY) })
  };
  F.examweek.planner = plan({ attempts: [] }, 30, inputs(4));

  F.examday = {
    attempts: attempts(150, range(-40, -1), 0.7),
    settings: settings(0),
    coach: coach(40, { lastWeekly: iso(now - 3 * DAY) })
  };

  F.after = {
    attempts: attempts(150, range(-50, -4), 0.7),
    settings: settings(-3),
    coach: coach(60, { lastWeekly: iso(now - 5 * DAY) })
  };

  F.weekly_due = {
    attempts: attempts(60, [-7, -5, -4, -2, -1, 0], 0.62),
    settings: settings(45),
    coach: coach(10, { lastWeekly: iso(now - 8 * DAY) })
  };

  /* 12 tagged misses, half of them misread: the journal's reading floor is 8
     tagged and its lead threshold 30%, so this clears both. */
  F.misread_heavy = (function () {
    var att = attempts(30, [-1, 0], 0.5, 11);
    var wrong = att.filter(function (a) { return !a.correct; }).slice(0, 12);
    var causes = ["misread", "misread", "misread", "misread", "misread", "misread",
                  "knowledge", "knowledge", "knowledge", "rushed", "rushed", "rushed"];
    var journal = wrong.map(function (a, i) {
      return { id: "jr-fx-" + i, attemptId: a.id, qid: a.qid, ecoTask: a.ecoTask, domain: a.domain,
               cause: causes[i], at: iso(now - DAY), note: "" };
    });
    return { attempts: att, journal: journal, settings: settings(70), coach: coach(5, { lastWeekly: iso(now - DAY) }) };
  })();

  return JSON.parse(JSON.stringify(F));
})();
