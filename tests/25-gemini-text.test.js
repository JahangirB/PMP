"use strict";
/* Gemini on the page's own text: select words anywhere to have them
   explained or to ask about them (§10.34), and the ✦ button in the notes
   that tidies, shortens or expands a part of them with Keep, Redo and Undo
   (§10.35). Google's API is stubbed throughout. */
var assert = require("assert");

var H = { "access-control-allow-origin": "*" };
function json(r, status, body) { return r.fulfill({ status: status, headers: H, contentType: "application/json", body: JSON.stringify(body) }); }
function reply(text) { return { candidates: [{ content: { role: "model", parts: [{ text: text }] } }] }; }
var MODELS = { models: [{ name: "models/gemini-3.5-flash", displayName: "Gemini 3.5 Flash", supportedGenerationMethods: ["generateContent"] }] };

/* Every POST is kept; `answer` decides the reply and may wait on `gate`. */
async function stub(page, answer) {
  var posts = [];
  await page.ctx.route(/generativelanguage\.googleapis\.com/, async function (r) {
    var q = r.request();
    if (q.method() === "GET") return json(r, 200, MODELS);
    var b = JSON.parse(q.postData());
    posts.push({ system: b.systemInstruction.parts[0].text, last: b.contents[b.contents.length - 1].parts[0].text });
    var t = await answer(posts.length);
    return json(r, 200, reply(t));
  });
  await page.click("#np-toggle");
  await page.click("#np-tab-ai");
  await page.fill("#np-key", "GOOD");
  await page.click("#np-key-save");
  await page.waitForTimeout(300);
  await page.click("#np-toggle");
  return posts;
}
/* Selects the words of the first element matching sel, as a mouse would. */
async function selectIn(page, sel) {
  await page.evaluate(function (s) {
    var n = document.querySelector(s), r = document.createRange();
    r.selectNodeContents(n);
    var g = window.getSelection();
    g.removeAllRanges();
    g.addRange(r);
  }, sel);
  await page.waitForTimeout(350);
}

module.exports = [
  {
    name: "select text: Explain this sends it at once with where it came from; Ask a question sends the question with it; never in a box",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      var posts = await stub(page, function () { return "It means checking the effect first."; });
      await lib.go(page, "#/learn?lesson=F1");
      await page.waitForSelector("#view p");
      await selectIn(page, "#view p");
      assert.strictEqual(await page.isVisible("#sa"), true, "the bar appears by the selection");
      var picked = await page.evaluate(function () { return String(window.getSelection()).replace(/\s+/g, " ").trim(); });
      await page.click("#sa-explain");
      await page.waitForTimeout(400);
      assert.strictEqual(posts.length, 1);
      assert.ok(/^Explain this clearly/.test(posts[0].last) && posts[0].last.indexOf(picked.slice(0, 40)) > 0, posts[0].last);
      assert.ok(/\(From “.+” in PMP Prep\.\)/.test(posts[0].last), "says where it came from");
      assert.ok(posts[0].last.indexOf("haven't answered") < 0, "no hold-back outside a question");
      var pane = await page.evaluate(function () { return { open: PMP.Notepad.isOpen(), tab: document.getElementById("np-tab-ai").getAttribute("aria-selected") }; });
      assert.deepStrictEqual(pane, { open: true, tab: "true" });
      assert.strictEqual(await page.isVisible("#sa"), false);

      await selectIn(page, "#view p");
      await page.click("#sa-ask");
      assert.strictEqual(await page.evaluate(function () { return document.activeElement.id; }), "sa-q");
      await page.keyboard.type("Why does this come first?");
      await page.keyboard.press("Enter");
      await page.waitForTimeout(400);
      assert.strictEqual(posts.length, 2);
      assert.ok(/^About this, from /.test(posts[1].last) && /Why does this come first\?$/.test(posts[1].last), posts[1].last);

      /* Text selected inside a box being typed in is the box's own business. */
      await page.focus("#np-msg");
      await page.keyboard.type("some words here");
      await page.keyboard.press("Control+A");
      await page.waitForTimeout(350);
      assert.strictEqual(await page.isVisible("#sa"), false);
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "select text in an unanswered practice question: Gemini is told not to give the answer; no bar in a running mock",
    fn: async function (h, lib) {
      var page = await h.open("day3");
      var posts = await stub(page, function () { return "ok"; });
      await lib.go(page, "#/practice");
      await page.click('#view button:has-text("Start drill")');
      await page.waitForSelector("#view .q-stem");
      await selectIn(page, "#view .q-stem");
      await page.click("#sa-explain");
      await page.waitForTimeout(400);
      assert.ok(posts[0].last.indexOf("don't tell me which option is right") > 0, posts[0].last);

      await page.click('#view button:has-text("End drill")').catch(function () {});
      await page.waitForTimeout(200);
      var endSheet = await page.$('#sheet-foot button:has-text("End")');
      if (endSheet) await endSheet.click();
      await lib.go(page, "#/exam");
      await page.evaluate(function () { PMP.Exam.start({ items: 20 }); });
      await page.waitForTimeout(300);
      var startBtn = await page.$('#sheet-foot button:has-text("Start")');
      if (startBtn) await startBtn.click();
      await page.waitForSelector("#view .q-stem");
      await selectIn(page, "#view .q-stem");
      assert.strictEqual(await page.isVisible("#sa"), false, "not during a mock");
      page.errors = [];
      await page.ctx.close();
    }
  },
  {
    name: "notes ✦: the selection locks while Gemini works, comes back highlighted; Redo asks for another; Undo restores; Keep saves",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      var release = null;
      var posts = await stub(page, function (n) {
        return n === 1 ? new Promise(function (res) { release = function () { res("## Earned value\n* **CPI** = EV / AC; below 1 is over budget."); }; })
                       : "- CPI = EV ÷ AC (version " + n + ")";
      });
      await page.click("#np-toggle");
      await page.click("#np-tab-notes");
      await page.evaluate(function () {
        var t = document.getElementById("np-text");
        t.innerHTML = "<div>evm cpi = ev/ac below 1 = over budget</div><div>Keep this line as it is.</div>";
        PMP.Notepad.save();
        var r = document.createRange();
        r.selectNodeContents(t.firstChild);
        t.focus();
        var g = window.getSelection(); g.removeAllRanges(); g.addRange(r);
      });
      await page.waitForTimeout(100);
      var before = await page.evaluate(function () { return document.getElementById("np-text").innerHTML; });
      await page.click("#np-ai-btn");
      assert.strictEqual(await page.isVisible("#np-pop-ai"), true);
      await page.click('#np-pop-ai [data-ai="tidy"]');
      await page.waitForTimeout(200);
      var busy = await page.evaluate(function () {
        var z = document.querySelector("#np-text .np-ai-zone");
        return { busy: z && z.classList.contains("np-ai-busy"), locked: z && z.getAttribute("contenteditable"), text: z && z.textContent,
                 rest: document.getElementById("np-text").lastChild.textContent, btn: document.getElementById("np-ai-btn").disabled,
                 strip: document.getElementById("np-ai-review").textContent };
      });
      assert.deepStrictEqual(busy, { busy: true, locked: "false", text: "evm cpi = ev/ac below 1 = over budget", rest: "Keep this line as it is.", btn: true,
        strip: "Tidy up: Gemini is working on the grey part…Cancel" });
      assert.ok(/^Tidy up these study notes/.test(posts[0].system) && posts[0].last === "My notes:\n\nevm cpi = ev/ac below 1 = over budget", JSON.stringify(posts[0]));
      release();
      await page.waitForSelector("#np-ai-keep");
      var done = await page.evaluate(function () {
        var z = document.querySelector("#np-text .np-ai-zone");
        return { done: z.classList.contains("np-ai-done"), html: z.innerHTML };
      });
      assert.strictEqual(done.done, true);
      assert.strictEqual(done.html, "<div><b>Earned value</b></div><ul><li><b>CPI</b> = EV / AC; below 1 is over budget.</li></ul>", "headings and * bullets folded into the notes' markup");

      await page.click("#np-ai-redo");
      await page.waitForSelector("#np-ai-keep");
      assert.ok(/noticeably different/.test(posts[1].last) && posts[1].last.indexOf("**Earned value**") > 0, posts[1].last);
      assert.ok(/version 2/.test(await page.textContent("#np-text .np-ai-zone")));

      await page.click("#np-ai-undo");
      assert.strictEqual(await page.evaluate(function () { return document.getElementById("np-text").innerHTML; }), before, "exactly as it was");
      assert.strictEqual(await page.isVisible("#np-ai-review"), false);

      /* Nothing selected: the whole page, then kept. */
      await page.evaluate(function () { window.getSelection().removeAllRanges(); });
      await page.click("#np-ai-btn");
      await page.click('#np-pop-ai [data-ai="short"]');
      await page.waitForSelector("#np-ai-keep");
      assert.ok(posts[2].last.indexOf("Keep this line as it is.") > 0, "the whole page went");
      await page.click("#np-ai-keep");
      var kept = await page.evaluate(function () { return { zone: !!document.querySelector(".np-ai-zone"), saved: PMP.Store.state.notepad.html }; });
      assert.strictEqual(kept.zone, false);
      assert.ok(/version 3/.test(kept.saved) && kept.saved.indexOf("np-ai") < 0, kept.saved);
      await lib.clean(page);
      await page.ctx.close();
    }
  }
];
