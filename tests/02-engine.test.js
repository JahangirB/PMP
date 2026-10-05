"use strict";
/* Phase 1: the coach engine (§05.11, §10.16), with no UI. */
var assert = require("assert");
var fs = require("fs");
var path = require("path");

var EXPECTED = {
  fresh: "intake", day3: "normal", planned_ontrack: "normal", planned_behind: "normal",
  gap10: "reentry", examweek: "examweek", examday: "examday", after: "after",
  weekly_due: "weekly", misread_heavy: "normal"
};

async function clickSheet(page, label) {
  await page.evaluate(function (l) {
    var b = Array.prototype.filter.call(document.querySelectorAll("#sheet-foot button"), function (x) { return x.textContent.trim() === l; })[0];
    if (!b) throw new Error("No sheet button " + l + " in: " + document.getElementById("sheet-foot").textContent);
    b.click();
  }, label);
  await page.waitForTimeout(60);
}

module.exports = [
  {
    name: "stage() for each of the ten fixtures",
    fn: async function (h, lib) {
      var got = {};
      for (var fx in EXPECTED) {
        var page = await h.open(fx);
        got[fx] = await page.evaluate(function () { return PMP.Coach.stage(PMP.Store.state); });
        await lib.clean(page, fx);
        await page.ctx.close();
      }
      assert.deepStrictEqual(got, EXPECTED);
    }
  },
  {
    name: "planned_ontrack: session is exactly today's chips, in order, done as assess says",
    fn: async function (h, lib) {
      var page = await h.open("planned_ontrack");
      var r = await page.evaluate(function () {
        var s = PMP.Store.state;
        var a = PMP.Planner.assess(s.planner, s);
        var day = s.planner.days.filter(function (d) { return d.today; })[0];
        var ses = PMP.Coach.session(s);
        return {
          source: ses.source,
          chips: day.tasks.map(function (t) { return { id: "plan:" + t.id, k: t.k, done: t.done }; }),
          steps: ses.steps.map(function (x) { return { id: x.id, k: x.kind, done: x.done }; })
        };
      });
      assert.strictEqual(r.source, "plan");
      var rank = { cards: 0, lesson: 1, journal: 4 };
      var expect = r.chips.slice().sort(function (a, b) { return (rank[a.k] === undefined ? 2 : rank[a.k]) - (rank[b.k] === undefined ? 2 : rank[b.k]); });
      assert.deepStrictEqual(r.steps, expect);
      assert.ok(r.steps.length >= 2);
      await page.ctx.close();
    }
  },
  {
    name: "day3: no plan, fallback session, no mock step",
    fn: async function (h, lib) {
      var page = await h.open("day3");
      var r = await page.evaluate(function () {
        var ses = PMP.Coach.session(PMP.Store.state);
        return { source: ses.source, steps: ses.steps.map(function (x) { return x.kind + "|" + x.label; }), extra: ses.extra && ses.extra.label };
      });
      assert.strictEqual(r.source, "fallback");
      assert.ok(r.steps.length >= 2, JSON.stringify(r));
      r.steps.forEach(function (s) { assert.ok(!/mock/i.test(s) && s.indexOf("mock|") !== 0, s); });
      await page.ctx.close();
    }
  },
  {
    name: "journal triage joins only at five or more untagged, and always last",
    fn: async function (h, lib) {
      var cases = { day3: true, misread_heavy: false, planned_ontrack: false };
      for (var fx in cases) {
        var page = await h.open(fx);
        var r = await page.evaluate(function () {
          var s = PMP.Store.state;
          var untagged = PMP.Journal.misses(s).filter(function (m) { return !m.entry; }).length;
          var steps = PMP.Coach.session(s).steps.map(function (x) { return x.kind; });
          return { untagged: untagged, steps: steps };
        });
        var has = r.steps.indexOf("triage") >= 0;
        assert.strictEqual(has, cases[fx], fx + " " + JSON.stringify(r));
        assert.strictEqual(has, r.untagged >= 5, fx);
        if (has) assert.strictEqual(r.steps[r.steps.length - 1], "triage", fx + " triage last");
        await page.ctx.close();
      }
    }
  },
  {
    name: "messages(): max, cooldown after showing, backoff after three dismissals",
    fn: async function (h, lib) {
      var page = await h.open("planned_behind");
      var r = await page.evaluate(function () {
        var DAY = 86400000, out = {};
        var all = PMP.Coach.messages(PMP.Store.state, "today", 99);
        out.all = all.map(function (m) { return m.id; });
        out.two = PMP.Coach.messages(PMP.Store.state, "today", 2).length;
        out.one = PMP.Coach.messages(PMP.Store.state, "strip", 1).length;
        var id = "behind_plan";
        var m = PMP.COACH_MESSAGES.filter(function (x) { return x.id === id; })[0];
        PMP.Coach.markShown(id);
        out.afterShown = PMP.Coach.messages(PMP.Store.state, "today", 99).some(function (x) { return x.id === id; });
        /* Just past one cooldown: back with a clean record, still away after three dismissals. */
        function age(streak) {
          PMP.Store.commit(["coach"], function (s) {
            s.coach.shown[id] = new Date(Date.now() - m.cooldownDays * DAY - 3600000).toISOString();
            s.coach.dismissStreak[id] = streak;
          });
          return PMP.Coach.messages(PMP.Store.state, "today", 99).some(function (x) { return x.id === id; });
        }
        out.pastCooldown = age(0);
        out.pastCooldownAfter3 = age(3);
        PMP.Coach.dismiss(id); PMP.Coach.dismiss(id);
        out.streak = PMP.Store.state.coach.dismissStreak[id];
        PMP.Coach.setPref("muted", true);
        out.muted = PMP.Coach.messages(PMP.Store.state, "today", 99).length;
        return out;
      });
      assert.ok(r.all.indexOf("behind_plan") >= 0, JSON.stringify(r.all));
      assert.ok(r.all.length > 2, "fixture should offer more than two messages: " + r.all);
      assert.strictEqual(r.two, 2);
      assert.strictEqual(r.one, 1);
      assert.strictEqual(r.afterShown, false);
      assert.strictEqual(r.pastCooldown, true);
      assert.strictEqual(r.pastCooldownAfter3, false);
      assert.strictEqual(r.streak, 5);
      assert.strictEqual(r.muted, 0);
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "every template renders clean on every fixture, and the audit block passes",
    fn: async function (h, lib) {
      var problems = [];
      for (var fx in EXPECTED) {
        var page = await h.open(fx);
        var p = await page.evaluate(function () {
          var bad = [];
          var c = PMP.Coach.context(PMP.Store.state);
          PMP.COACH_MESSAGES.forEach(function (m) {
            var on = false;
            try { on = m.when(c); } catch (e) { bad.push(m.id + " when threw " + e.message); return; }
            if (!on) return;
            var t = m.text(c);
            if (/undefined|NaN|[{}]/.test(t) || t.length > 160) bad.push(m.id + ": " + t);
          });
          PMP.Coach.messages(PMP.Store.state, "today", 99);
          var ses = PMP.Coach.session(PMP.Store.state);
          ses.steps.concat(ses.extra ? [ses.extra] : []).forEach(function (s) {
            [s.label, s.why].forEach(function (t) { if (typeof t !== "string" || /undefined|NaN/.test(t)) bad.push("step " + s.id + ": " + t); });
          });
          return bad;
        });
        p.forEach(function (x) { problems.push(fx + " " + x); });
        await lib.clean(page, fx);
        await page.ctx.close();
      }
      assert.deepStrictEqual(problems, []);
      var page2 = await h.open("fresh", { hash: "#/audit" });
      var a = await page2.evaluate(function () {
        var r = PMP.Audit.auditCoach();
        var text = document.getElementById("view").textContent;
        return { pass: r.pass, failures: r.failures, shown: text.indexOf("Every coach sentence passes") >= 0, n: r.counts.messages };
      });
      assert.deepStrictEqual(a.failures, []);
      assert.ok(a.pass && a.shown && a.n >= 19, JSON.stringify(a));
      await page2.ctx.close();
    }
  },
  {
    name: "§10.16 reads only the allowed modules and writes only the coach slice",
    fn: async function () {
      var src = fs.readFileSync(path.join(__dirname, "..", "index.html"), "utf8");
      var start = src.indexOf("/* ── 10.16  COACH");
      var end = src.indexOf("/* ═══ §11  CHARTS");
      var body = src.slice(start, end);
      assert.ok(start > 0 && end > start);
      assert.ok(body.indexOf("PMP.Storage") < 0, "touches PMP.Storage");
      assert.ok(body.indexOf("localStorage") < 0, "touches localStorage");
      var used = {};
      body.replace(/PMP\.([A-Za-z_]+)/g, function (_, k) { used[k] = true; });
      var allowed = ["Coach", "Metrics", "Planner", "Learn", "Journal", "Cards", "Store", "time", "fmt",
                     "COACH_LINES", "COACH_MESSAGES", "ECO_TASK_BY_ID", "ECO_TASKS", "READINESS", "EXAM"];
      Object.keys(used).forEach(function (k) { assert.ok(allowed.indexOf(k) >= 0, "uses PMP." + k); });
      var commits = body.match(/PMP\.Store\.commit\(\[[^\]]*\]/g) || [];
      assert.deepStrictEqual(commits, ['PMP.Store.commit(["coach"]']);
      /* No accuracy or coverage arithmetic of its own. */
      assert.ok(!/\.credit\s*\/|creditOf/.test(body), "computes credit-based figures");
    }
  },
  {
    name: "progress export and import round-trips coach; a v2 file without coach still imports",
    fn: async function (h, lib) {
      var src = await h.open("weekly_due");
      var snap = await src.evaluate(function () {
        PMP.Coach.setPref("hints", false);
        PMP.Coach.markShown("provisional");
        return PMP.Store.snapshot();
      });
      await src.ctx.close();
      assert.ok(snap.coach && snap.coach.profile, "snapshot carries coach");

      var dst = await h.open("fresh");
      await dst.evaluate(function (t) { PMP.Data.importProgress(t); }, JSON.stringify(snap));
      await clickSheet(dst, "Replace everything");
      await clickSheet(dst, "Replace it all");
      var got = await dst.evaluate(function () { return PMP.Store.state.coach; });
      assert.deepStrictEqual(got, snap.coach);
      await lib.clean(dst);
      await dst.ctx.close();

      var old = JSON.parse(JSON.stringify(snap));
      delete old.coach;
      var d2 = await h.open("fresh");
      await d2.evaluate(function (t) { PMP.Data.importProgress(t); }, JSON.stringify(old));
      await clickSheet(d2, "Replace everything");
      await clickSheet(d2, "Replace it all");
      var r = await d2.evaluate(function () {
        return { coach: PMP.Store.state.coach, attempts: PMP.Store.state.attempts.length, stage: PMP.Coach.stage(PMP.Store.state) };
      });
      assert.ok(r.attempts > 0);
      assert.strictEqual(r.coach.profile, null);
      assert.strictEqual(r.stage, "intake");
      await lib.clean(d2);
      await d2.ctx.close();
    }
  }
];
