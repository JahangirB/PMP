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
  }
];
