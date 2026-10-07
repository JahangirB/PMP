"use strict";
/* The AI tab in the notes pane (§12.2.2): ChatGPT, Gemini, Claude or a
   custom link, opened in a window of its own beside the app. Outbound
   requests are stubbed; what is checked is the address each press opens. */
var assert = require("assert");

/* Records every window the page opens and stubs every http(s) request, so
   the test never reaches the real services and can see that nothing is
   requested before a press. */
async function watch(page) {
  var w = { opened: [], requests: [] };
  await page.ctx.route(/^https?:/, function (r) {
    w.requests.push(r.request().url());
    r.fulfill({ status: 200, contentType: "text/html", body: "stub" });
  });
  page.ctx.on("page", function (p) { w.opened.push(p); });
  return w;
}
async function lastUrl(w) {
  await new Promise(function (r) { setTimeout(r, 250); });
  return w.opened.length ? w.opened[w.opened.length - 1].url() : null;
}

module.exports = [
  {
    name: "two tabs; the chosen one survives a refresh; arrows move between them",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      await page.click("#np-toggle");
      assert.strictEqual(await page.isVisible("#np-text"), true);
      assert.strictEqual(await page.isVisible("#np-q"), false);
      await page.click("#np-tab-ai");
      assert.strictEqual(await page.isVisible("#np-q"), true);
      assert.strictEqual(await page.getAttribute("#np-tab-ai", "aria-selected"), "true");
      await page.waitForTimeout(700);
      await page.reload();
      await page.waitForFunction(function () { return window.PMP && PMP.Store.state.sys.ready; });
      assert.strictEqual(await page.isVisible("#np-q"), true, "AI tab is still the open one");
      await page.focus("#np-tab-ai");
      await page.keyboard.press("ArrowLeft");
      assert.strictEqual(await page.isVisible("#np-text"), true);
      assert.strictEqual(await page.evaluate(function () { return document.activeElement.id; }), "np-tab-notes");
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "nothing is opened or requested until a button is pressed",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      var w = await watch(page);
      await page.click("#np-toggle");
      await page.click("#np-tab-ai");
      await page.fill("#np-q", "What is a CCB?");
      for (var p of ["gemini", "claude", "custom", "chatgpt"]) await page.click('.np-prov-b[data-p="' + p + '"]');
      await page.waitForTimeout(300);
      assert.deepStrictEqual(w.requests, []);
      assert.strictEqual(w.opened.length, 0);
      await page.ctx.close();
    }
  },
  {
    name: "Ask prefills ChatGPT and Claude, copies for Gemini; Open starts a blank chat",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      var w = await watch(page);
      await page.click("#np-toggle");
      await page.click("#np-tab-ai");
      var q = "Explain CPI & SPI";

      await page.fill("#np-q", q);
      await page.click("#np-ask");
      assert.strictEqual(await lastUrl(w), "https://chatgpt.com/?q=Explain%20CPI%20%26%20SPI");
      assert.strictEqual(await page.inputValue("#np-q"), "", "a sent question is cleared");

      await page.click('.np-prov-b[data-p="claude"]');
      assert.strictEqual(await page.textContent("#np-ask"), "Ask Claude");
      await page.fill("#np-q", q);
      await page.click("#np-ask");
      assert.strictEqual(await lastUrl(w), "https://claude.ai/new?q=Explain%20CPI%20%26%20SPI");

      await page.click('.np-prov-b[data-p="gemini"]');
      await page.fill("#np-q", q);
      await page.click("#np-ask");
      assert.strictEqual(await lastUrl(w), "https://gemini.google.com/app");
      assert.strictEqual(await page.inputValue("#np-q"), q, "kept, because it still has to be pasted");
      assert.ok((await page.textContent("#toasts")).indexOf("Question copied") >= 0);

      await page.click("#np-open");
      assert.strictEqual(await lastUrl(w), "https://gemini.google.com/app");
      /* One named window, reused: five presses across three services. */
      assert.strictEqual(w.opened.length, 1);
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "a custom link: refused unless http(s), {q} prefills, kept after a refresh",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      var w = await watch(page);
      await page.click("#np-toggle");
      await page.click("#np-tab-ai");
      await page.click('.np-prov-b[data-p="custom"]');
      assert.strictEqual(await page.isDisabled("#np-ask"), true, "nothing to ask until a link is saved");
      await page.fill("#np-c-url", "javascript:alert(1)");
      await page.click("#np-c-save");
      assert.strictEqual(await page.evaluate(function () { return PMP.Store.state.notepad.ai.customUrl; }), "");
      await page.fill("#np-c-name", "Perplexity");
      await page.fill("#np-c-url", "https://www.perplexity.ai/search?q={q}");
      await page.click("#np-c-save");
      assert.strictEqual(await page.textContent('.np-prov-b[data-p="custom"]'), "Perplexity");
      await page.fill("#np-q", "RACI vs RAM");
      await page.click("#np-ask");
      assert.strictEqual(await lastUrl(w), "https://www.perplexity.ai/search?q=RACI%20vs%20RAM");
      /* A link of the person's own gets no handle back on this page. */
      assert.strictEqual(await w.opened[w.opened.length - 1].evaluate(function () { return window.opener; }), null);
      await page.waitForTimeout(700);
      await page.reload();
      await page.waitForFunction(function () { return window.PMP && PMP.Store.state.sys.ready; });
      assert.strictEqual(await page.textContent("#np-ask"), "Ask Perplexity");
      assert.strictEqual(await page.inputValue("#np-c-url"), "https://www.perplexity.ai/search?q={q}");
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "an unsent question survives a refresh; an older notes slice still loads",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      await page.click("#np-toggle");
      await page.click("#np-tab-ai");
      await page.type("#np-q", "half-typed");
      await page.waitForTimeout(900);
      await page.reload();
      await page.waitForFunction(function () { return window.PMP && PMP.Store.state.sys.ready; });
      assert.strictEqual(await page.inputValue("#np-q"), "half-typed");
      /* A slice saved before the AI tab existed has no tab and no ai. */
      var r = await page.evaluate(function () {
        PMP.Store.commit(["notepad"], function (s) { s.notepad = { open: true, text: "old", updatedAt: null }; });
        return { ask: document.getElementById("np-ask").textContent, notes: !document.getElementById("np-notes").hidden };
      });
      assert.deepStrictEqual(r, { ask: "Ask ChatGPT", notes: true });
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "Gemini by API key: checked, chat in the pane, key never in a backup or a URL",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      var calls = [];
      await page.ctx.route(/generativelanguage\.googleapis\.com/, function (r) {
        var q = r.request(), k = q.headers()["x-goog-api-key"], H = { "access-control-allow-origin": "*" };
        calls.push({ method: q.method(), url: q.url(), key: k, body: q.postData() ? JSON.parse(q.postData()) : null });
        if (k !== "GOOD") return r.fulfill({ status: 400, headers: H, contentType: "application/json",
          body: JSON.stringify({ error: { code: 400, message: "API key not valid.", details: [{ reason: "API_KEY_INVALID" }] } }) });
        if (q.method() === "GET") return r.fulfill({ status: 200, headers: H, contentType: "application/json", body: JSON.stringify({ models: [
          { name: "models/gemini-2.5-pro", displayName: "Gemini 2.5 Pro", supportedGenerationMethods: ["generateContent"] },
          { name: "models/gemini-2.5-flash", displayName: "Gemini 2.5 Flash", supportedGenerationMethods: ["generateContent"] },
          { name: "models/text-embedding-004", supportedGenerationMethods: ["embedContent"] }] }) });
        return r.fulfill({ status: 200, headers: H, contentType: "application/json", body: JSON.stringify({
          candidates: [{ content: { role: "model", parts: [{ text: "**CPI** is EV / AC.\n\n- Below 1: over budget\n- <img src=x onerror=alert(1)>" }] } }] }) });
      });
      await page.click("#np-toggle");
      await page.click("#np-tab-ai");
      await page.click('.np-prov-b[data-p="gemini"]');
      await page.click('#np-gmode button[data-m="key"]');

      await page.fill("#np-key", "BAD");
      await page.click("#np-key-save");
      await page.waitForTimeout(300);
      assert.ok((await page.textContent("#toasts")).indexOf("not valid") >= 0);
      assert.strictEqual(await page.isVisible("#np-msg"), false, "a refused key opens no chat");

      await page.fill("#np-key", "GOOD");
      await page.click("#np-key-save");
      await page.waitForTimeout(300);
      assert.deepStrictEqual(await page.$$eval("#np-model option", function (o) { return o.map(function (x) { return x.value; }); }),
        ["models/gemini-2.5-pro", "models/gemini-2.5-flash"], "chat models only");
      assert.strictEqual(await page.inputValue("#np-model"), "models/gemini-2.5-flash");

      await page.fill("#np-msg", "What is CPI?");
      await page.keyboard.press("Control+Enter");
      await page.waitForTimeout(300);
      await page.fill("#np-msg", "And SPI?");
      await page.click("#np-send");
      await page.waitForTimeout(300);
      var post = calls.filter(function (c) { return c.method === "POST"; });
      assert.strictEqual(post.length, 2);
      assert.ok(/models\/gemini-2\.5-flash:generateContent$/.test(post[1].url));
      assert.deepStrictEqual(post[1].body.contents.map(function (c) { return c.role; }), ["user", "model", "user"], "the conversation goes with each turn");
      assert.ok(calls.every(function (c) { return c.url.indexOf("GOOD") < 0; }), "the key travels in a header, never the URL");

      var reply = await page.$eval('#np-log .np-m[data-role="model"]', function (m) { return { html: m.innerHTML, imgs: m.querySelectorAll("img").length }; });
      assert.ok(reply.html.indexOf("<p><b>CPI</b>") === 0 && reply.html.indexOf("<li>") > 0, reply.html);
      assert.strictEqual(reply.imgs, 0, "a reply is text, never markup");

      var leak = await page.evaluate(function () { return JSON.stringify(PMP.Store.snapshot()).indexOf("GOOD") >= 0; });
      assert.strictEqual(leak, false, "the key is not in a progress export");

      await page.waitForTimeout(700);
      await page.reload();
      await page.waitForFunction(function () { return window.PMP && PMP.Store.state.sys.ready; });
      await page.waitForTimeout(200);
      assert.strictEqual(await page.isVisible("#np-msg"), true, "remembered key: straight back into the chat");
      assert.strictEqual(await page.$$eval("#np-log .np-m", function (m) { return m.length; }), 4);

      await page.click("#np-key-forget");
      await page.click('#sheet-foot button:has-text("Forget key")');
      await page.waitForTimeout(100);
      assert.strictEqual(await page.isVisible("#np-key"), true);
      assert.strictEqual(await page.evaluate(function () { return localStorage.getItem("pmp.gemini.key"); }), null);
      /* Chrome logs the refused key's 400 itself; that one line is expected. */
      page.errors = page.errors.filter(function (e) { return e.indexOf("status of 400") < 0; });
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "Gemini key not remembered: gone after a refresh",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      await page.ctx.route(/generativelanguage\.googleapis\.com/, function (r) {
        r.fulfill({ status: 200, headers: { "access-control-allow-origin": "*" }, contentType: "application/json",
          body: JSON.stringify({ models: [{ name: "models/gemini-2.5-flash", supportedGenerationMethods: ["generateContent"] }] }) });
      });
      await page.click("#np-toggle");
      await page.click("#np-tab-ai");
      await page.click('.np-prov-b[data-p="gemini"]');
      await page.click('#np-gmode button[data-m="key"]');
      await page.fill("#np-key", "SESSION");
      await page.uncheck("#np-key-keep");
      await page.click("#np-key-save");
      await page.waitForTimeout(300);
      assert.strictEqual(await page.isVisible("#np-msg"), true);
      assert.strictEqual(await page.evaluate(function () { return localStorage.getItem("pmp.gemini.key"); }), null);
      await page.waitForTimeout(700);
      await page.reload();
      await page.waitForFunction(function () { return window.PMP && PMP.Store.state.sys.ready; });
      assert.strictEqual(await page.isVisible("#np-key"), true, "asks for the key again");
      await lib.clean(page);
      await page.ctx.close();
    }
  }
];
