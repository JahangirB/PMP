"use strict";
/* Study tools on an answer (§10.02.7a) and their shared slices (§10.18):
   Make a card into the "My cards" deck (§10.05), Explain it back and Ask
   about this question through Gemini (§12.2.2), "Ask Gemini for a full
   review" on Today and Progress, and `mycards` and `chat` as real web
   slices that go out and come back as they are. Google is stubbed. */
var assert = require("assert");
var H = require("./helpers");

var HDR = { "access-control-allow-origin": "*" };
function json(r, status, body) { return r.fulfill({ status: status, headers: HDR, contentType: "application/json", body: JSON.stringify(body) }); }
function stubGemini(page, onPost) {
  return page.ctx.route(/generativelanguage\.googleapis\.com/, function (r) {
    var q = r.request();
    if (q.method() === "GET") return json(r, 200, { models: [{ name: "models/gemini-3.5-flash", displayName: "Gemini 3.5 Flash", supportedGenerationMethods: ["generateContent"] }] });
    var body = JSON.parse(q.postData());
    var text = onPost ? onPost(body) : "OK.";
    return json(r, 200, { candidates: [{ content: { role: "model", parts: [{ text: text }] } }] });
  });
}
async function withKey(page) {
  await page.click("#np-toggle");
  await page.click("#np-tab-ai");
  await page.fill("#np-key", "GOOD");
  await page.click("#np-key-save");
  await page.waitForTimeout(300);
  await page.click("#np-toggle");
}
/* One wrong answer, checked, on a single-choice question with a two-sentence stem. */
async function missOne(page) {
  var qid = await page.evaluate(function () {
    var q = PMP.QUESTIONS.filter(function (x) { return x.type === "single" && /[.?!]\s+\S/.test(x.stem) && x.explanation && x.mindset; })[0];
    PMP.Practice.startWith([q, PMP.QUESTIONS[1]], "practice");
    PMP.Router.navigate("practice");
    return q.id;
  });
  await page.waitForTimeout(120);
  await page.evaluate(function () {
    var s = PMP.Practice.session, item = s.items[s.i];
    var o = item.p.options.filter(function (x) { return !x.correct; })[0];
    document.querySelector('#drill-card .opt[data-opt="' + o.id + '"] input').click();
    Array.prototype.filter.call(document.querySelectorAll("#drill-card .q-conf button"), function (b) { return b.textContent === "Think so"; })[0].click();
  });
  await page.waitForTimeout(80);
  return qid;
}

module.exports = [
  {
    name: "Make a card: drafted from the miss, editable, saved to the shared mycards slice; a My cards deck; delete",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      var qid = await missOne(page);
      await H.clickText(page, "#drill-card .q-study button", "Make a card");
      var draft = await page.evaluate(function (qid) {
        var q = PMP.Content.byId(qid);
        return { f: document.getElementById("mc-front").value, b: document.getElementById("mc-back").value, q: q,
                 expect: PMP.Practice.cardDraft(q) };
      }, qid);
      var first = /^[\s\S]*?[.?!](?=\s|$)/.exec(draft.q.stem)[0].trim();
      assert.strictEqual(draft.f, first.length > 160 ? first.slice(0, 157).trim() + "…" : first, "front: the first sentence");
      assert.ok(draft.f.length <= 160);
      var right = draft.q.options.filter(function (o) { return o.correct; })[0].text;
      assert.strictEqual(draft.b, "Right move: " + right + ".\n\n" + draft.q.explanation + "\n\n" + draft.q.mindset);
      await page.fill("#mc-front", "My own front");
      await H.clickText(page, "#sheet-foot button", "Save to My cards");
      var st = await page.evaluate(function () { return PMP.Store.state.mycards; });
      assert.deepStrictEqual(st, [{ id: "my:" + qid, t: draft.q.ecoTask, f: "My own front", b: draft.b, n: "" }]);
      assert.ok(await page.isVisible('#drill-card .q-study button:has-text("Edit my card")'));

      await lib.go(page, "#/cards");
      var deck = await page.evaluate(function () {
        var row = Array.prototype.filter.call(document.querySelectorAll(".deck-tbl tbody tr"), function (tr) { return tr.textContent.indexOf("My cards") >= 0; })[0];
        return { row: !!row, list: document.querySelectorAll("#my-cards li").length, counts: PMP.Cards.counts(PMP.Store.state, "mine"),
                 all: PMP.Cards.counts(PMP.Store.state, null).total - PMP.FLASHCARDS.length };
      });
      assert.ok(deck.row, "a My cards deck");
      assert.strictEqual(deck.list, 1);
      assert.strictEqual(deck.counts.total, 1);
      assert.strictEqual(deck.all, 1, "and in the all-decks totals");
      /* Studying it schedules it like any card. */
      await page.evaluate(function () {
        var row = Array.prototype.filter.call(document.querySelectorAll(".deck-tbl tbody tr"), function (tr) { return tr.textContent.indexOf("My cards") >= 0; })[0];
        row.querySelector("button").click();
      });
      await page.waitForTimeout(80);
      assert.ok((await page.textContent(".cardface-front")).indexOf("My own front") >= 0);
      await page.keyboard.press("Space");
      await page.keyboard.press("3");
      await page.waitForTimeout(80);
      assert.ok(await page.evaluate(function (id) { return !!PMP.Store.state.srs[id]; }, "my:" + qid));
      await H.clickText(page, "#view button", "Back to decks");
      /* Delete from the list: the card and its schedule go. */
      await H.clickText(page, "#my-cards button", "Delete");
      await H.clickText(page, "#sheet-foot button", "Delete card");
      var gone = await page.evaluate(function (id) { return { mine: PMP.Store.state.mycards.length, srs: !!PMP.Store.state.srs[id], row: !!document.getElementById("my-cards") }; }, "my:" + qid);
      assert.deepStrictEqual(gone, { mine: 0, srs: false, row: false });
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "mycards and chat are web slices, lossless both ways; a phone's copy comes in, shows, and goes back out",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      var r = await page.evaluate(function () {
        var q = PMP.QUESTIONS[0], now = Date.now();
        var phone = {
          type: "pmp-progress", version: 3, savedAt: now, device: { app: "android", id: "p1" },
          attempts: [{ q: q.id, t: q.ecoTask, d: q.domain, ap: q.approach, ty: "single", cr: 1, ok: true, ms: 50000, at: now - 5000, src: "practice", cf: 1, el: 2 }],
          mycards: [{ id: "my:" + q.id, t: q.ecoTask, f: "Phone front", b: "Phone back", n: "" }, { id: "my:zzz", t: "", f: "Second", b: "Back", n: "note" }],
          chat: [{ r: "user", t: "How am I doing?", at: now - 3000 }, { r: "model", t: "Well.", at: now - 2000 }],
          sittings: [], checkins: [], weekly: [], sessions: [], diagnostics: [], snoozed: {}, drills: []
        };
        var st = PMP.Sync.fromDoc(phone, PMP.Store.state);
        var view = PMP.Store.defaults();
        Object.keys(st).forEach(function (k) { view[k] = st[k]; });
        var back = PMP.Sync.toDoc(view);
        return {
          slices: { mycards: st.mycards, chat: st.chat, shared: Object.keys(st.shared).filter(function (k) { return k === "mycards" || k === "chat"; }) },
          el: st.attempts[0].el, conf: st.attempts[0].confidence,
          diff: ["attempts", "mycards", "chat"].filter(function (k) { return PMP.Sync.canon(back[k]) !== PMP.Sync.canon(phone[k]); })
        };
      });
      assert.strictEqual(r.slices.mycards.length, 2);
      assert.strictEqual(r.slices.chat.length, 2);
      assert.deepStrictEqual(r.slices.shared, [], "not left in the passthrough");
      assert.deepStrictEqual([r.el, r.conf], [2, 1]);
      assert.deepStrictEqual(r.diff, [], "changed on the way back out: " + r.diff.join(", "));

      /* Through the real import: the deck and the conversation show. */
      await page.evaluate(function () {
        var q = PMP.QUESTIONS[0], now = Date.now();
        PMP.Sync.importReplace({ type: "pmp-progress", version: 3, savedAt: now, device: { app: "android", id: "p1" },
          mycards: [{ id: "my:" + q.id, t: q.ecoTask, f: "Phone front", b: "Phone back", n: "" }],
          chat: [{ r: "user", t: "How am I doing?", at: now - 3000 }, { r: "model", t: "**Well.**", at: now - 2000 }] });
      });
      await lib.go(page, "#/cards");
      assert.ok((await page.textContent("#my-cards")).indexOf("Phone front") >= 0);
      await page.click("#np-toggle");
      await page.click("#np-tab-ai");
      assert.strictEqual(await page.isVisible("#np-log"), false, "no key: the key form, not the log");
      var log = await page.evaluate(function () { return PMP.Sync.chatOf(PMP.Store.state).map(function (c) { return c.r; }); });
      assert.deepStrictEqual(log, ["user", "model"]);
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "a phone's cards still waiting in `shared` from an older build are adopted on load",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      await page.evaluate(function () {
        localStorage.setItem("pmp.v2.shared", JSON.stringify({ v: 2, d: { mycards: [{ id: "my:a", t: "", f: "F", b: "B", n: "" }], chat: [{ r: "user", t: "old", at: 5 }] } }));
        localStorage.removeItem("pmp.v2.mycards");
      });
      await page.reload();
      await page.waitForFunction(function () { return window.PMP && PMP.Store.state.sys.ready; });
      var r = await page.evaluate(function () {
        var st = PMP.Store.state, d = PMP.Sync.toDoc(st);
        return { mine: st.mycards.map(function (c) { return c.id; }), shared: Object.keys(st.shared).sort(), chat: d.chat.length, docCards: d.mycards.length };
      });
      assert.deepStrictEqual(r, { mine: ["my:a"], shared: ["chat"], chat: 1, docCards: 1 });
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "Explain it back: without a key, how to add one; with one, the tutor prompt and feedback, not kept in the chat",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      await missOne(page);
      await H.clickText(page, "#drill-card .q-study button", "Explain it back");
      var t = await page.textContent("#sheet-body");
      assert.ok(/Add the key in the Gemini tab/.test(t), t);
      await H.clickText(page, "#sheet-foot button", "Open the Gemini tab");
      assert.ok(await page.isVisible("#np-key"), "the Gemini tab, on the key");
      await page.click("#np-toggle");
      await page.ctx.close();

      page = await h.open("fresh");
      var bodies = [];
      await stubGemini(page, function (b) { bodies.push(b); return "You got the **sequence** right.\n\nThe key idea: assess first."; });
      await withKey(page);
      await missOne(page);
      await H.clickText(page, "#drill-card .q-study button", "Explain it back");
      assert.ok(/Why is the right answer right, and why was your choice wrong\?/.test(await page.textContent("#sheet-body")));
      await page.fill("#xb-text", "Because you assess before acting.");
      await page.click("#xb-go");
      await page.waitForTimeout(300);
      var out = await page.textContent("#xb-out");
      assert.ok(/You got the sequence right/.test(out) && /AI feedback can be wrong/.test(out), out);
      assert.strictEqual(bodies.length, 1);
      assert.ok(/under 150 words/.test(bodies[0].systemInstruction.parts[0].text), "the phones' EXPLAIN_SYSTEM");
      var turn = bodies[0].contents[0].parts[0].text;
      assert.ok(/^What I was studying:\n/.test(turn) && /The right answer: /.test(turn) && /My explanation in my own words:\nBecause you assess before acting\.$/.test(turn), turn);
      assert.strictEqual(await page.evaluate(function () { return PMP.Store.state.chat.length; }), 0, "not kept in the chat");
      await H.clickText(page, "#sheet-foot button", "Close");
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "Ask about this question, from practice feedback and from mock review: the Gemini tab with a draft, nothing sent",
    fn: async function (h, lib) {
      var page = await h.open("day3");
      var posts = 0;
      await stubGemini(page, function () { posts++; return "OK."; });
      await withKey(page);
      var qid = await missOne(page);
      await H.clickText(page, "#drill-card .q-study button", "Ask about this question");
      var r = await page.evaluate(function (qid) {
        var s = PMP.Practice.session, item = s.items[0], q = PMP.Content.byId(qid);
        var mine = item.p.options.map(function (o, i) { return o.id === item.value ? "ABCDEFGHIJ".charAt(i) + ") " + o.text : null; }).filter(Boolean)[0];
        var right = item.p.options.map(function (o, i) { return o.correct ? "ABCDEFGHIJ".charAt(i) + ") " + o.text : null; }).filter(Boolean)[0];
        return { draft: document.getElementById("np-msg").value, focus: document.activeElement.id, tab: PMP.Store.state.notepad.tab,
                 open: document.documentElement.getAttribute("data-notepad"), stem: q.stem, mine: mine, right: right,
                 opts: item.p.options.map(function (o, i) { return "ABCDEFGHIJ".charAt(i) + ") " + o.text; }) };
      }, qid);
      assert.strictEqual(r.open, "open");
      assert.strictEqual(r.tab, "ai");
      assert.strictEqual(r.focus, "np-msg");
      assert.ok(r.draft.indexOf("Help me with this question:\n\n" + r.stem) === 0, r.draft);
      r.opts.forEach(function (o) { assert.ok(r.draft.indexOf(o) >= 0, o); });
      assert.ok(r.draft.indexOf("I chose " + r.mine + ".") >= 0, r.draft);
      assert.ok(r.draft.indexOf("The correct answer is " + r.right + ".") >= 0, r.draft);
      assert.strictEqual(posts, 0, "nothing sent until Send");

      /* A sitting, ended at once with one answer: its review has the button. */
      await page.click("#np-toggle");
      await page.evaluate(function () { PMP.Practice.cleanup(); });
      await lib.go(page, "#/exam");
      await H.clickText(page, "#view button", "Start a sitting");
      await H.clickText(page, "#sheet-foot button", "Start the clock");
      await page.evaluate(function () {
        var c = document.getElementById("exam-card");
        var i = c.querySelector('input[type="radio"], input[type="checkbox"]');
        if (i) i.click();
        c.querySelectorAll("select").forEach(function (x) { if (x.options.length > 1) { x.selectedIndex = 1; x.dispatchEvent(new Event("change", { bubbles: true })); } });
        c.querySelectorAll('input[type="text"], input:not([type])').forEach(function (x) { x.value = "x"; x.dispatchEvent(new Event("input", { bubbles: true })); });
        var hot = c.querySelector(".hot");
        if (hot) hot.dispatchEvent(new MouseEvent("click", { bubbles: true }));
      });
      await H.clickText(page, "#view button", "End exam");
      await H.clickText(page, "#sheet-foot button", "End and mark it");
      await page.waitForTimeout(200);
      await page.evaluate(function () { if (document.getElementById("sheet").open) PMP.UI.closeSheet(null); });
      await page.evaluate(function () {
        var d = Array.prototype.filter.call(document.querySelectorAll("details.review"), function (x) { return !x.open; })[0] || document.querySelector("details.review");
        d.open = true;
      });
      await page.waitForTimeout(80);
      await H.clickText(page, "details.review[open] .q-study button", "Ask about this question");
      var d2 = await page.evaluate(function () { return document.getElementById("np-msg").value; });
      assert.ok(/^Help me with this question:/.test(d2) && /The correct answer is /.test(d2) && /explain why it is right/.test(d2), d2);
      assert.strictEqual(posts, 0);
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "Ask Gemini for a full review, on Today and Progress: sent at once with a key, a draft without one",
    fn: async function (h, lib) {
      var page = await h.open("day3");
      await lib.go(page, "#/dashboard");
      await H.clickText(page, "#view button", "Ask Gemini for a full review");
      assert.ok(await page.isVisible("#np-key"), "no key: the key form");
      await page.ctx.close();

      page = await h.open("day3");
      var bodies = [];
      await stubGemini(page, function (b) { bodies.push(b); return "Here is your review."; });
      await withKey(page);
      await lib.go(page, "#/today");
      await H.clickText(page, "#view .td-foot button", "Ask Gemini for a full review");
      await page.waitForTimeout(300);
      assert.strictEqual(bodies.length, 1);
      var last = bodies[0].contents[bodies[0].contents.length - 1].parts[0].text;
      assert.ok(/^How am I doing overall\?/.test(last), last);
      assert.ok(/Readiness: \d+\/100/.test(bodies[0].systemInstruction.parts[0].text));
      assert.ok((await page.textContent("#np-log")).indexOf("Here is your review.") >= 0);
      await lib.clean(page);
      await page.ctx.close();
    }
  }
];
