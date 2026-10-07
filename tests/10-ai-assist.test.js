"use strict";
/* The Gemini tab in the notes pane (§12.2.2): the person's own Google AI
   Studio key, chat in the pane, Flash models from 3.0. Google's API is
   stubbed throughout; nothing reaches the real service. */
var assert = require("assert");

var MODELS = [
  { name: "models/gemini-2.5-flash", displayName: "Gemini 2.5 Flash", supportedGenerationMethods: ["generateContent"] },
  { name: "models/gemini-3-pro", displayName: "Gemini 3 Pro", supportedGenerationMethods: ["generateContent"] },
  { name: "models/gemini-3-flash", displayName: "Gemini 3 Flash", supportedGenerationMethods: ["generateContent"] },
  { name: "models/gemini-3-flash-lite", displayName: "Gemini 3 Flash-Lite", supportedGenerationMethods: ["generateContent"] },
  { name: "models/gemini-3.1-flash-preview", displayName: "Gemini 3.1 Flash Preview", supportedGenerationMethods: ["generateContent"] },
  { name: "models/gemini-3-flash-image", displayName: "Gemini 3 Flash Image", supportedGenerationMethods: ["generateContent"] },
  { name: "models/text-embedding-004", supportedGenerationMethods: ["embedContent"] }
];

module.exports = [
  {
    name: "two tabs; the chosen one survives a refresh; arrows move between them",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      await page.click("#np-toggle");
      assert.strictEqual(await page.isVisible("#np-text"), true);
      assert.strictEqual(await page.isVisible("#np-key"), false);
      await page.click("#np-tab-ai");
      assert.strictEqual(await page.isVisible("#np-key"), true);
      assert.strictEqual(await page.getAttribute("#np-tab-ai", "aria-selected"), "true");
      await page.waitForTimeout(700);
      await page.reload();
      await page.waitForFunction(function () { return window.PMP && PMP.Store.state.sys.ready; });
      assert.strictEqual(await page.isVisible("#np-key"), true, "Gemini tab is still the open one");
      await page.focus("#np-tab-ai");
      await page.keyboard.press("ArrowLeft");
      assert.strictEqual(await page.isVisible("#np-text"), true);
      assert.strictEqual(await page.evaluate(function () { return document.activeElement.id; }), "np-tab-notes");
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "Gemini only: no other services; nothing is requested until a key is given",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      var requests = [];
      await page.ctx.route(/^https?:/, function (r) { requests.push(r.request().url()); r.abort(); });
      await page.click("#np-toggle");
      await page.click("#np-tab-ai");
      var text = await page.textContent("#np-ai");
      assert.ok(!/ChatGPT|Claude|Other/.test(text), text);
      await page.waitForTimeout(300);
      assert.deepStrictEqual(requests, []);
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "model list: Flash only, version 3.0 and later; defaults to the newest stable",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      await page.ctx.route(/generativelanguage\.googleapis\.com/, function (r) {
        r.fulfill({ status: 200, headers: { "access-control-allow-origin": "*" }, contentType: "application/json", body: JSON.stringify({ models: MODELS }) });
      });
      await page.click("#np-toggle");
      await page.click("#np-tab-ai");
      await page.fill("#np-key", "GOOD");
      await page.click("#np-key-save");
      await page.waitForTimeout(300);
      assert.deepStrictEqual(await page.$$eval("#np-model option", function (o) { return o.map(function (x) { return x.value; }); }),
        ["models/gemini-3.1-flash-preview", "models/gemini-3-flash", "models/gemini-3-flash-lite"]);
      assert.strictEqual(await page.inputValue("#np-model"), "models/gemini-3-flash", "defaults to the newest stable");
      await page.selectOption("#np-model", "models/gemini-3.1-flash-preview");
      await page.waitForTimeout(700);
      await page.reload();
      await page.waitForFunction(function () { return window.PMP && PMP.Store.state.sys.ready; });
      await page.waitForTimeout(200);
      assert.strictEqual(await page.inputValue("#np-model"), "models/gemini-3.1-flash-preview", "the choice is kept");
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "a key with no Flash 3 model says so and cannot send",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      await page.ctx.route(/generativelanguage\.googleapis\.com/, function (r) {
        r.fulfill({ status: 200, headers: { "access-control-allow-origin": "*" }, contentType: "application/json", body: JSON.stringify({ models: MODELS.slice(0, 2) }) });
      });
      await page.click("#np-toggle");
      await page.click("#np-tab-ai");
      await page.fill("#np-key", "OLD");
      await page.click("#np-key-save");
      await page.waitForTimeout(300);
      assert.ok((await page.textContent("#np-log")).indexOf("version 3.0") >= 0);
      assert.strictEqual(await page.isDisabled("#np-send"), true);
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
          { name: "models/gemini-3-flash", displayName: "Gemini 3 Flash", supportedGenerationMethods: ["generateContent"] }] }) });
        return r.fulfill({ status: 200, headers: H, contentType: "application/json", body: JSON.stringify({
          candidates: [{ content: { role: "model", parts: [{ text: "**CPI** is EV / AC.\n\n- Below 1: over budget\n- <img src=x onerror=alert(1)>" }] } }] }) });
      });
      await page.click("#np-toggle");
      await page.click("#np-tab-ai");

      await page.fill("#np-key", "BAD");
      await page.click("#np-key-save");
      await page.waitForTimeout(300);
      assert.ok((await page.textContent("#toasts")).indexOf("not valid") >= 0);
      assert.strictEqual(await page.isVisible("#np-msg"), false, "a refused key opens no chat");

      await page.fill("#np-key", "GOOD");
      await page.click("#np-key-save");
      await page.waitForTimeout(300);
      assert.strictEqual(await page.inputValue("#np-model"), "models/gemini-3-flash");

      await page.fill("#np-msg", "What is CPI?");
      await page.keyboard.press("Control+Enter");
      await page.waitForTimeout(300);
      await page.fill("#np-msg", "And SPI?");
      await page.click("#np-send");
      await page.waitForTimeout(300);
      var post = calls.filter(function (c) { return c.method === "POST"; });
      assert.strictEqual(post.length, 2);
      assert.ok(/models\/gemini-3-flash:generateContent$/.test(post[1].url));
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
          body: JSON.stringify({ models: [{ name: "models/gemini-3-flash", supportedGenerationMethods: ["generateContent"] }] }) });
      });
      await page.click("#np-toggle");
      await page.click("#np-tab-ai");
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
