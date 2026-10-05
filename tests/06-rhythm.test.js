"use strict";
/* Phase 4: the weekly review, re-entry, exam week, exam day, after the exam,
   and the calendar export. */
var assert = require("assert");
var fs = require("fs");
var H = require("./helpers");

/* A small RFC 5545 reader: unfold, then collect VEVENT properties. */
function parseIcs(text) {
  var lines = text.replace(/\r\n[ \t]/g, "").split(/\r\n/).filter(Boolean);
  var events = [], cur = null, cal = 0;
  lines.forEach(function (l) {
    if (l === "BEGIN:VCALENDAR") cal++;
    else if (l === "BEGIN:VEVENT") cur = {};
    else if (l === "END:VEVENT") { events.push(cur); cur = null; }
    else if (cur) { var i = l.indexOf(":"); cur[l.slice(0, i)] = l.slice(i + 1); }
  });
  if (cal !== 1 || !/^BEGIN:VCALENDAR\r\nVERSION:2\.0\r\n/.test(text) || !/END:VCALENDAR\r\n$/.test(text)) throw new Error("not a calendar");
  return events;
}

module.exports = [
  {
    name: "weekly_due: the review shows, saving writes one snapshot and hides it for seven days",
    fn: async function (h, lib) {
      var page = await h.open("weekly_due");
      var has = await page.evaluate(function () { return !!document.querySelector(".td-weekly"); });
      assert.ok(has, "review card shown");
      await page.click('label[for="ik-focus-3"]');
      await page.fill("#td-note", "Work ran late on Wednesday.");
      await H.clickText(page, ".td-weekly button", "Save the review");
      var r = await page.evaluate(function () {
        var c = PMP.Store.state.coach, DAY = 86400000, real = Date.now, out = {};
        out.snapshots = c.weekly.length; out.snap = c.weekly[0]; out.focus = c.focus;
        out.card = !!document.querySelector(".td-weekly");
        out.stage = PMP.Coach.stage(PMP.Store.state);
        Date.now = function () { return real() + 6 * DAY; };
        out.in6 = PMP.Coach.stage(PMP.Store.state);
        /* Studying on day six keeps day seven from being a comeback,
           which would otherwise rank above the review. */
        PMP.Store.commit(["attempts"], function (s) {
          var a = Object.assign({}, s.attempts[s.attempts.length - 1]);
          a.id = "att-day6"; a.at = new Date(real() + 6 * DAY).toISOString();
          s.attempts.push(a);
        });
        Date.now = function () { return real() + 7 * DAY + 3600000; };
        out.in7 = PMP.Coach.stage(PMP.Store.state);
        Date.now = real;
        return out;
      });
      assert.strictEqual(r.snapshots, 1);
      assert.deepStrictEqual(Object.keys(r.snap).sort(), ["adherence", "at", "daysActive", "focus", "note", "readiness"]);
      assert.strictEqual(r.snap.note, "Work ran late on Wednesday.");
      assert.ok(r.focus && r.focus.kind === "pace", JSON.stringify(r.focus));
      assert.strictEqual(r.card, false);
      assert.strictEqual(r.stage, "normal");
      assert.notStrictEqual(r.in6, "weekly");
      assert.strictEqual(r.in7, "weekly");
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "gap10: welcome back, the short session and the rebuild offer; nothing about a lost streak",
    fn: async function (h, lib) {
      var page = await h.open("gap10");
      var r = await page.evaluate(function () {
        var ses = PMP.Coach.session(PMP.Store.state);
        return { text: document.getElementById("view").textContent, steps: ses.steps.map(function (s) { return s.kind; }),
                 source: ses.source, msgs: Array.prototype.map.call(document.querySelectorAll(".coach-msg"), function (m) { return m.textContent; }) };
      });
      assert.strictEqual(r.source, "reentry");
      assert.deepStrictEqual(r.steps, ["cards", "practice"]);
      assert.ok(r.msgs.some(function (m) { return /^Welcome back/.test(m); }), r.msgs.join(" | "));
      assert.ok(r.msgs.some(function (m) { return /Rebuild the plan/.test(m); }), r.msgs.join(" | "));
      assert.ok(!/streak lost|broke|lost your streak/i.test(r.text), "guilt copy found");
      var src = fs.readFileSync(require("path").join(__dirname, "..", "index.html"), "utf8");
      var copy = src.slice(src.indexOf("/* ── 05.11  COACH MESSAGES"), src.indexOf("/* ═══ §06  STORAGE"));
      assert.ok(!/streak lost|broke|!\s*"/i.test(copy.replace(/\/\*[\s\S]*?\*\//g, "")), "no guilt or exclamation in the coach copy");
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "examweek has no lesson steps; examday has no study steps; after asks how it went",
    fn: async function (h, lib) {
      var p1 = await h.open("examweek");
      var w = await p1.evaluate(function () { return PMP.Coach.session(PMP.Store.state).steps.map(function (s) { return s.kind; }); });
      assert.ok(w.length > 0 && w.indexOf("lesson") < 0, JSON.stringify(w));
      await lib.clean(p1);
      await p1.ctx.close();

      var p2 = await h.open("examday");
      var d = await p2.evaluate(function () {
        return { steps: document.querySelectorAll(".td-step").length, items: document.querySelectorAll("#view .ln-bullets li").length,
                 session: PMP.Coach.session(PMP.Store.state).steps.length, msgs: document.querySelectorAll(".coach-msg").length };
      });
      assert.deepStrictEqual(d, { steps: 0, items: 6, session: 0, msgs: 0 });
      await lib.clean(p2);
      await p2.ctx.close();

      var p3 = await h.open("after");
      var q = await p3.evaluate(function () { return document.querySelector("#view .card h2").textContent; });
      assert.strictEqual(q, "How did the exam go?");
      await H.clickText(p3, "#view .card button", "Not this time");
      var a = await p3.evaluate(function () {
        return { open: document.getElementById("sheet").open, date: document.getElementById("ik-date").value,
                 setting: PMP.Store.state.settings.examDate, outcome: PMP.Store.state.coach.outcome.result };
      });
      assert.deepStrictEqual(a, { open: true, date: "", setting: null, outcome: "not" });
      await lib.clean(p3);
      await p3.ctx.close();
    }
  },
  {
    name: "after: Not sat yet takes a new date and rebuilds; Passed offers a backup",
    fn: async function (h, lib) {
      var page = await h.open("after");
      await H.clickText(page, "#view .card button", "Not sat yet");
      var d = new Date(Date.now() + 20 * 86400000);
      var v = d.getFullYear() + "-" + String(d.getMonth() + 1).padStart(2, "0") + "-" + String(d.getDate()).padStart(2, "0");
      await page.fill("#td-newdate", v);
      await H.clickText(page, "#view .card button", "Save the new date");
      var r = await page.evaluate(function () { return { stage: PMP.Coach.stage(PMP.Store.state), date: PMP.Store.state.settings.examDate, outcome: PMP.Store.state.coach.outcome }; });
      assert.strictEqual(r.date, v);
      assert.strictEqual(r.outcome, null);
      assert.ok(r.stage !== "after", r.stage);
      await lib.clean(page);
      await page.ctx.close();

      var p2 = await h.open("after");
      await H.clickText(p2, "#view .card button", "Passed");
      var t = await p2.evaluate(function () { return { text: document.getElementById("view").textContent, primary: document.querySelectorAll("#view .btn--primary").length }; });
      assert.ok(/Well done/.test(t.text) && /Export a backup/.test(t.text));
      assert.strictEqual(t.primary, 1);
      await p2.ctx.close();
    }
  },
  {
    name: "calendar export: one event per remaining study day, at the profile's time",
    fn: async function (h, lib) {
      var page = await h.open("planned_ontrack", { hash: "#/settings" });
      var expect = await page.evaluate(function () {
        var today = PMP.time.startOfDay(Date.now());
        return PMP.Store.state.planner.days.filter(function (d) { return d.d >= today && !d.rest && d.tasks.length; })
          .map(function (d) { var x = new Date(d.d); return x.getFullYear() + String(x.getMonth() + 1).padStart(2, "0") + String(x.getDate()).padStart(2, "0"); });
      });
      var dl = page.waitForEvent("download");
      await H.clickText(page, "#view button", "Export study times to calendar");
      var file = await dl;
      assert.ok(/\.ics$/.test(file.suggestedFilename()));
      var text = fs.readFileSync(await file.path(), "utf8");
      var ev = parseIcs(text);
      assert.strictEqual(ev.length, expect.length);
      assert.deepStrictEqual(ev.map(function (e) { return e.DTSTART.slice(0, 8); }), expect);
      ev.forEach(function (e) {
        assert.strictEqual(e.DTSTART.slice(8), "T190000", "evening is 19:00");
        assert.ok(/^PT\d+M$/.test(e.DURATION) && e.SUMMARY && e.UID);
      });
      text.split("\r\n").forEach(function (l) { assert.ok(l.length <= 75, "folded: " + l); });
      /* A morning person gets 07:00. */
      var morning = await page.evaluate(function () {
        PMP.Store.commit(["coach"], function (s) { s.coach.profile.when = "morning"; });
        return PMP.Coach.calendar(PMP.Store.state).text;
      });
      parseIcs(morning).forEach(function (e) { assert.strictEqual(e.DTSTART.slice(8), "T070000"); });
      await lib.clean(page);
      await page.ctx.close();
    }
  }
];
