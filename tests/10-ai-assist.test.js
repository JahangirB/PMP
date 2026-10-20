"use strict";
/* The Gemini tab in the notes pane (§12.2.2): the person's own Google AI
   Studio key, chat in the pane, and the phones' automatic line-up of Flash
   models from 3.5 with fallback down it. There is no model picker any more:
   the old dropdown and its 3.0 rule are gone. Google's API is stubbed
   throughout; nothing reaches the real service. */
var assert = require("assert");

var H = { "access-control-allow-origin": "*" };
function M(name, display) { return { name: "models/" + name, displayName: display || name, supportedGenerationMethods: ["generateContent"] }; }
var MODELS = [
  M("gemini-2.5-flash"), M("gemini-3-flash"), M("gemini-3-pro"),
  M("gemini-3.5-flash", "Gemini 3.5 Flash"), M("gemini-3.5-flash-lite"),
  M("gemini-3.6-flash"), M("gemini-3.6-flash-preview"),
  M("gemini-4-flash"), M("gemini-4.1-flash"), M("gemini-4.2-flash-preview"), M("gemini-4.5-flash", "Gemini 4.5 Flash"),
  M("gemini-4.5-flash-lite"), M("gemini-4.5-flash-image"), M("gemini-4.5-flash-tts"),
  { name: "models/text-embedding-004", supportedGenerationMethods: ["embedContent"] }
];
function json(r, status, body) { return r.fulfill({ status: status, headers: H, contentType: "application/json", body: JSON.stringify(body) }); }
function reply(text) { return { candidates: [{ content: { role: "model", parts: [{ text: text }] } }] }; }

async function useKey(page, k, keep) {
  await page.click("#np-toggle");
  await page.click("#np-tab-ai");
  await page.fill("#np-key", k);
  if (keep === false) await page.uncheck("#np-key-keep");
  await page.click("#np-key-save");
  await page.waitForTimeout(300);
}
async function reload(page) {
  await page.waitForTimeout(700);
  await page.reload();
  await page.waitForFunction(function () { return window.PMP && PMP.Store.state.sys.ready; });
  await page.waitForTimeout(200);
}

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
      await reload(page);
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
    name: "line-up: Flash 3.5 and newer, no Lite, newest first, at most five, 3.5 kept; no picker; stored beside the key",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      await page.ctx.route(/generativelanguage\.googleapis\.com/, function (r) { json(r, 200, { models: MODELS }); });
      await useKey(page, "GOOD");
      var r = await page.evaluate(function () {
        var stored = JSON.parse(localStorage.getItem("pmp.gemini.lineup") || "[]");
        return {
          lineup: PMP.GeminiChat.lineup.map(function (m) { return m.n; }),
          stored: stored.map(function (m) { return m.n; }),
          at: Number(localStorage.getItem("pmp.gemini.lineupAt")),
          picker: document.querySelectorAll("#np-model, #np-ai select").length,
          shown: document.getElementById("np-lineup").textContent,
          title: document.getElementById("np-lineup").getAttribute("title"),
          inStore: JSON.stringify(PMP.Store.snapshot()).indexOf("gemini-4.5") >= 0,
          inDoc: JSON.stringify(PMP.Sync.toDoc(PMP.Store.state)).indexOf("gemini-4.5") >= 0,
          /* The rule itself, on a short list: everything eligible, in order. */
          small: PMP.GeminiChat.lineupOf([{ n: "models/gemini-3.5-flash", r: 350 }, { n: "models/gemini-4-flash-preview", r: 398 },
            { n: "models/gemini-4-flash", r: 400 }, { n: "models/gemini-3.5-flash-lite", r: 349 }]).map(function (m) { return m.n; })
        };
      });
      assert.deepStrictEqual(r.lineup, ["models/gemini-4.5-flash", "models/gemini-4.2-flash-preview", "models/gemini-4.1-flash",
        "models/gemini-4-flash", "models/gemini-3.5-flash"], "top four, and the 3.5 model kept in fifth place");
      assert.deepStrictEqual(r.stored, r.lineup, "the line-up is kept in localStorage");
      assert.ok(Date.now() - r.at < 60000, "with when it was fetched");
      assert.strictEqual(r.picker, 0, "no model dropdown");
      assert.ok(/Gemini 4\.5 Flash, taking turns with 4 other models/.test(r.shown), r.shown);
      assert.ok(/Gemini 3\.5 Flash/.test(r.title), r.title);
      assert.strictEqual(r.inStore, false, "never in the store");
      assert.strictEqual(r.inDoc, false, "never in the synced document");
      assert.deepStrictEqual(r.small, ["models/gemini-4-flash", "models/gemini-4-flash-preview", "models/gemini-3.5-flash"]);
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "a key with no Flash 3.5 or newer is refused, says so, and is not kept",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      await page.ctx.route(/generativelanguage\.googleapis\.com/, function (r) { json(r, 200, { models: MODELS.slice(0, 3) }); });
      await useKey(page, "OLD");
      assert.ok((await page.textContent("#toasts")).indexOf("3.5 Flash or newer") >= 0);
      assert.strictEqual(await page.isVisible("#np-msg"), false, "no chat opens");
      assert.strictEqual(await page.evaluate(function () { return localStorage.getItem("pmp.gemini.key"); }), null);
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "Gemini by API key: checked, snapshot and 30 turns with every message, chat slice, key never in a backup or a URL",
    fn: async function (h, lib) {
      var page = await h.open("day3");
      var calls = [];
      await page.ctx.route(/generativelanguage\.googleapis\.com/, function (r) {
        var q = r.request(), k = q.headers()["x-goog-api-key"];
        calls.push({ method: q.method(), url: q.url(), key: k, body: q.postData() ? JSON.parse(q.postData()) : null });
        if (k !== "GOOD") return json(r, 400, { error: { code: 400, message: "API key not valid.", details: [{ reason: "API_KEY_INVALID" }] } });
        if (q.method() === "GET") return json(r, 200, { models: [M("gemini-3.5-flash", "Gemini 3.5 Flash")] });
        return json(r, 200, reply("**CPI** is EV / AC.\n\n- Below 1: over budget\n- <img src=x onerror=alert(1)>"));
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
      await page.fill("#np-msg", "What is CPI?");
      await page.keyboard.press("Control+Enter");
      await page.waitForTimeout(300);
      await page.fill("#np-msg", "And SPI?");
      await page.click("#np-send");
      await page.waitForTimeout(300);
      var post = calls.filter(function (c) { return c.method === "POST"; });
      assert.strictEqual(post.length, 2);
      assert.ok(/models\/gemini-3\.5-flash:generateContent$/.test(post[1].url));
      assert.deepStrictEqual(post[1].body.contents.map(function (c) { return c.role; }), ["user", "model", "user"], "the conversation goes with each turn");
      var sys = post[1].body.systemInstruction.parts[0].text;
      assert.ok(/study coach inside a PMP exam preparation app/.test(sys), "the coach's system prompt");
      ["Learner progress snapshot", "Readiness: ", "Weakest ECO tasks", "Tagged mistake causes", "Pace: ", "Mock exams", "Exam date: ", "App coach's suggestions today"].forEach(function (k) {
        assert.ok(sys.indexOf(k) >= 0, "snapshot carries " + k);
      });
      assert.strictEqual(post[1].body.generationConfig.temperature, 0.6);
      assert.ok(calls.every(function (c) { return c.url.indexOf("GOOD") < 0; }), "the key travels in a header, never the URL");

      var reply1 = await page.$eval('#np-log .np-m[data-role="model"]', function (m) { return { html: m.innerHTML, imgs: m.querySelectorAll("img").length }; });
      assert.ok(reply1.html.indexOf("<p><b>CPI</b>") === 0 && reply1.html.indexOf("<li>") > 0, reply1.html);
      assert.strictEqual(reply1.imgs, 0, "a reply is text, never markup");

      var st = await page.evaluate(function () {
        return { chat: PMP.Store.state.chat, leak: JSON.stringify(PMP.Store.snapshot()).indexOf("GOOD") >= 0,
                 doc: PMP.Sync.toDoc(PMP.Store.state).chat, ai: "ai" in PMP.Store.state.notepad };
      });
      assert.deepStrictEqual(st.chat.map(function (c) { return c.r; }), ["user", "model", "user", "model"], "the shared chat slice");
      assert.ok(st.chat.every(function (c) { return typeof c.at === "number" && typeof c.t === "string"; }));
      assert.deepStrictEqual(st.doc, st.chat, "and it is the document's chat, as it is");
      assert.strictEqual(st.ai, false, "no conversation left in the notepad slice");
      assert.strictEqual(st.leak, false, "the key is not in a progress export");

      /* Thirty turns at most go with a message, from the sixty kept. */
      await page.evaluate(function () {
        var list = [];
        for (var i = 0; i < 70; i++) list.push({ r: i % 2 ? "model" : "user", t: "turn " + i, at: 1000 + i });
        PMP.Store.commit(["chat"], function (s) { s.chat = list.slice(-60); });
      });
      await page.fill("#np-msg", "One more");
      await page.click("#np-send");
      await page.waitForTimeout(300);
      post = calls.filter(function (c) { return c.method === "POST"; });
      assert.strictEqual(post[post.length - 1].body.contents.length, 30);
      assert.strictEqual(await page.evaluate(function () { return PMP.Store.state.chat.length; }), 60);

      await page.click("#np-new");
      await reload(page);
      assert.strictEqual(await page.isVisible("#np-msg"), true, "remembered key: straight back into the chat");
      assert.strictEqual(calls.filter(function (c) { return c.method === "GET"; }).length, 2, "a fresh line-up is not fetched again on load");

      await page.click("#np-key-forget");
      await page.click('#sheet-foot button:has-text("Forget key")');
      await page.waitForTimeout(100);
      assert.strictEqual(await page.isVisible("#np-key"), true);
      assert.deepStrictEqual(await page.evaluate(function () {
        return [localStorage.getItem("pmp.gemini.key"), localStorage.getItem("pmp.gemini.lineup")];
      }), [null, null]);
      /* Chrome logs the refused key's 400 itself; that one line is expected. */
      page.errors = page.errors.filter(function (e) { return e.indexOf("status of 400") < 0; });
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "turns and fallback: each message starts one model on, so 3.5 is used too; an error moves on to the next",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      var posts = [];
      await page.ctx.route(/generativelanguage\.googleapis\.com/, function (r) {
        var q = r.request();
        if (q.method() === "GET") return json(r, 200, { models: [M("gemini-4-flash"), M("gemini-3.5-flash")] });
        posts.push(q.url().replace(/^.*models\//, "").replace(/:generateContent$/, ""));
        if (/gemini-4-flash/.test(q.url()) && posts.length === 1) return json(r, 503, { error: { code: 503, message: "The model is overloaded." } });
        return json(r, 200, reply("Fine."));
      });
      await useKey(page, "GOOD");
      for (var t of ["one", "two", "three", "four"]) {
        await page.fill("#np-msg", t);
        await page.click("#np-send");
        await page.waitForTimeout(300);
      }
      assert.deepStrictEqual(posts, ["gemini-4-flash", "gemini-3.5-flash", "gemini-3.5-flash", "gemini-4-flash", "gemini-3.5-flash"],
        "the first message falls back to 3.5; after that the two take turns");
      assert.strictEqual(await page.$$eval('#np-log .np-m[data-role="model"]', function (m) { return m.length; }), 4);
      assert.ok(/gemini-3\.5-flash/.test(await page.textContent("#np-lineup")), "the one answering is shown");
      page.errors = page.errors.filter(function (e) { return e.indexOf("status of 503") < 0; });
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "every model failing shows Google's error and names a 429 limit; a refused key stops at once",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      var posts = 0, mode = "limit";
      await page.ctx.route(/generativelanguage\.googleapis\.com/, function (r) {
        var q = r.request();
        if (q.method() === "GET") return json(r, 200, { models: [M("gemini-4-flash"), M("gemini-3.6-flash"), M("gemini-3.5-flash")] });
        posts++;
        if (mode === "limit") return json(r, 429, { error: { code: 429, message: "Resource has been exhausted (e.g. check quota)." } });
        return json(r, 400, { error: { code: 400, message: "API key expired.", details: [{ reason: "API_KEY_INVALID" }] } });
      });
      await useKey(page, "GOOD");
      await page.fill("#np-msg", "Hello");
      await page.click("#np-send");
      await page.waitForTimeout(400);
      var err = await page.textContent('#np-log .np-m[data-role="error"]');
      assert.strictEqual(posts, 3, "all three tried");
      assert.ok(/reached its limit on every model/.test(err) && /quota or per-minute rate/.test(err), err);

      mode = "key";
      posts = 0;
      await page.fill("#np-msg", "Again");
      await page.click("#np-send");
      await page.waitForTimeout(400);
      assert.strictEqual(posts, 1, "a key problem is not tried on another model");
      var errs = await page.$$eval('#np-log .np-m[data-role="error"]', function (m) { return m.map(function (x) { return x.textContent; }); });
      assert.ok(/not valid/.test(errs[errs.length - 1]), errs.join(" | "));
      page.errors = page.errors.filter(function (e) { return !/status of (400|429)/.test(e); });
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "the line-up is refreshed when a week old",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      var gets = 0, list = [M("gemini-3.5-flash")];
      await page.ctx.route(/generativelanguage\.googleapis\.com/, function (r) {
        if (r.request().method() === "GET") { gets++; return json(r, 200, { models: list }); }
        return json(r, 200, reply("ok"));
      });
      await useKey(page, "GOOD");
      assert.strictEqual(gets, 1);
      list = [M("gemini-3.5-flash"), M("gemini-3.7-flash")];
      await page.evaluate(function () { localStorage.setItem("pmp.gemini.lineupAt", String(Date.now() - 8 * 86400000)); });
      await reload(page);
      assert.strictEqual(gets, 2, "a stale line-up is fetched again");
      assert.deepStrictEqual(await page.evaluate(function () { return PMP.GeminiChat.lineup.map(function (m) { return m.n; }); }),
        ["models/gemini-3.7-flash", "models/gemini-3.5-flash"], "a new model arrives with no update");
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "Gemini key not remembered: gone after a refresh",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      await page.ctx.route(/generativelanguage\.googleapis\.com/, function (r) { json(r, 200, { models: [M("gemini-3.5-flash")] }); });
      await useKey(page, "SESSION", false);
      assert.strictEqual(await page.isVisible("#np-msg"), true);
      assert.strictEqual(await page.evaluate(function () { return localStorage.getItem("pmp.gemini.key"); }), null);
      await reload(page);
      assert.strictEqual(await page.isVisible("#np-key"), true, "asks for the key again");
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "an older conversation in notepad.ai moves to the chat slice on load",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      await page.evaluate(function () {
        localStorage.setItem("pmp.v2.notepad", JSON.stringify({ v: 2, d: { open: false, text: "", html: "", tab: "notes",
          ai: { model: "models/gemini-3-flash", chat: [{ role: "user", text: "Hi", at: "2026-10-01T10:00:00.000Z" }, { role: "model", text: "Hello", at: "2026-10-01T10:00:01.000Z" }] } } }));
        localStorage.removeItem("pmp.v2.chat");
      });
      await reload(page);
      var r = await page.evaluate(function () { return { chat: PMP.Store.state.chat, ai: "ai" in PMP.Store.state.notepad }; });
      assert.deepStrictEqual(r.chat, [{ r: "user", t: "Hi", at: Date.parse("2026-10-01T10:00:00.000Z") }, { r: "model", t: "Hello", at: Date.parse("2026-10-01T10:00:01.000Z") }]);
      assert.strictEqual(r.ai, false);
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "chat: the privacy note closes for good; / opens the commands; your own commands are made, used, synced and deleted",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      var sent = [];
      await page.ctx.route(/generativelanguage\.googleapis\.com/, function (r) {
        var q = r.request();
        if (q.method() === "GET") return json(r, 200, { models: [M("gemini-3.5-flash")] });
        var b = JSON.parse(q.postData());
        sent.push(b.contents[b.contents.length - 1].parts[0].text);
        return json(r, 200, reply("ok"));
      });
      await useKey(page, "GOOD");
      assert.strictEqual(await page.isVisible("#np-snap"), true);
      assert.ok(/type \/ for commands/.test(await page.getAttribute("#np-msg", "placeholder")));
      assert.strictEqual(await page.isVisible("#np-cmds"), false, "no quick buttons and no menu at rest");
      await page.click("#np-snap-x");
      await reload(page);
      assert.strictEqual(await page.isVisible("#np-snap"), false, "closed for good");

      await page.focus("#np-msg");
      await page.keyboard.type("/");
      var opts = await page.$$eval("#np-cmds [role=option] code", function (c) { return c.map(function (x) { return x.textContent; }); });
      assert.deepStrictEqual(opts, ["/progress", "/improve", "/plan", "/weakest"]);
      await page.keyboard.type("pl");
      await page.keyboard.press("Enter");
      var v = await page.inputValue("#np-msg");
      assert.ok(/^I have about an hour today/.test(v), v);
      assert.strictEqual(await page.isVisible("#np-cmds"), false);
      await page.fill("#np-msg", "/weakest in agile");
      await page.keyboard.press("Control+Enter");
      await page.waitForTimeout(300);
      assert.ok(/^Teach me the ECO task[\s\S]*\n\nin agile$/.test(sent[0]), sent[0]);

      /* + New command, with a name already taken first. */
      await page.fill("#np-msg", "");
      await page.keyboard.type("/");
      await page.click("#np-cmds .np-cmd-new");
      await page.fill("#cmd-name", "plan");
      await page.fill("#cmd-text", "Quiz me on agile.");
      await page.click('#sheet-foot button:has-text("Save")');
      assert.ok(/already a command/.test(await page.textContent("#sheet-body")));
      await page.fill("#cmd-name", "/Agile");
      await page.click('#sheet-foot button:has-text("Save")');
      var st = await page.evaluate(function () { return { own: PMP.Store.state.shared.commands, doc: PMP.Sync.toDoc(PMP.Store.state).commands }; });
      assert.deepStrictEqual(st.own, [{ n: "agile", t: "Quiz me on agile." }]);
      assert.deepStrictEqual(st.doc, st.own, "it travels in the progress document");
      await page.fill("#np-msg", "");
      await page.focus("#np-msg");
      await page.keyboard.type("/ag");
      assert.deepStrictEqual(await page.$$eval("#np-cmds [role=option] code", function (c) { return c.map(function (x) { return x.textContent; }); }), ["/agile"]);
      await page.keyboard.press("Enter");
      assert.strictEqual(await page.inputValue("#np-msg"), "Quiz me on agile.");

      await page.fill("#np-msg", "");
      await page.keyboard.type("/agile");
      await page.click('#np-cmds button[aria-label="Edit /agile"]');
      await page.click('#sheet-foot button:has-text("Delete")');
      assert.strictEqual(await page.evaluate(function () { return "commands" in PMP.Store.state.shared; }), false);
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "a reply is written out word by word, the sent message stays at the top, the full text is saved at once; reduced motion shows it whole",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      var long = [];
      for (var i = 0; i < 40; i++) long.push("Point **" + i + "** about scope, with enough words to fill the line and push the log down.");
      await page.ctx.route(/generativelanguage\.googleapis\.com/, function (r) {
        if (r.request().method() === "GET") return json(r, 200, { models: [M("gemini-3.5-flash")] });
        return json(r, 200, reply(long.join("\n\n")));
      });
      await useKey(page, "GOOD");
      await page.evaluate(function () {
        var list = [];
        for (var i = 0; i < 20; i++) list.push({ r: i % 2 ? "model" : "user", t: "earlier turn " + i + " with a few words in it", at: 1000 + i });
        PMP.Store.commit(["chat"], function (s) { s.chat = list; });
      });
      await page.fill("#np-msg", "Tell me about scope");
      await page.click("#np-send");
      await page.waitForFunction(function () { return document.querySelector("#np-log .np-caret"); });
      var mid = await page.evaluate(function () {
        var log = document.getElementById("np-log"), ms = log.querySelectorAll(".np-m"), mine = ms[ms.length - 2], last = ms[ms.length - 1];
        return { shown: last.textContent.length, full: PMP.Store.state.chat[PMP.Store.state.chat.length - 1].t.length,
                 top: Math.round(mine.getBoundingClientRect().top - log.getBoundingClientRect().top), busy: last.getAttribute("aria-busy") };
      });
      assert.ok(mid.shown < mid.full / 2, "still typing: " + JSON.stringify(mid));
      assert.ok(mid.top >= 0 && mid.top < 40, "the sent message sits at the top: " + JSON.stringify(mid));
      assert.strictEqual(mid.busy, "true");
      await page.waitForFunction(function () { return !document.querySelector("#np-log .np-caret"); }, null, { timeout: 15000 });
      var end = await page.evaluate(function () {
        var log = document.getElementById("np-log"), ms = log.querySelectorAll(".np-m"), mine = ms[ms.length - 2];
        return { bold: ms[ms.length - 1].querySelectorAll("b").length, top: Math.round(mine.getBoundingClientRect().top - log.getBoundingClientRect().top) };
      });
      assert.deepStrictEqual(end, { bold: 40, top: mid.top }, "all of it, and no jump to the end");

      await page.emulateMedia({ reducedMotion: "reduce" });
      await page.fill("#np-msg", "Again");
      await page.click("#np-send");
      await page.waitForTimeout(400);
      var still = await page.evaluate(function () { var ms = document.querySelectorAll("#np-log .np-m"); return { caret: !!document.querySelector(".np-caret"), bold: ms[ms.length - 1].querySelectorAll("b").length }; });
      assert.deepStrictEqual(still, { caret: false, bold: 40 });
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "a failed reply leaves Try again under the message; it sends the same message again, once, and the answer arrives",
    fn: async function (h, lib) {
      var page = await h.open("fresh");
      var posts = 0, fail = true;
      await page.ctx.route(/generativelanguage\.googleapis\.com/, function (r) {
        if (r.request().method() === "GET") return json(r, 200, { models: [M("gemini-3.5-flash")] });
        posts++;
        if (fail) return json(r, 503, { error: { code: 503, message: "The model is overloaded. Please try again later." } });
        return json(r, 200, reply("Here is the answer."));
      });
      await useKey(page, "GOOD");
      await page.fill("#np-msg", "What is a WBS?");
      await page.click("#np-send");
      await page.waitForSelector("#np-retry");
      var f = await page.evaluate(function () {
        var b = document.getElementById("np-fail"), prev = b.previousElementSibling;
        return { text: b.textContent, after: prev && prev.textContent, chat: PMP.Store.state.chat.length };
      });
      assert.ok(/overloaded/.test(f.text) && /Try again/.test(f.text), f.text);
      assert.strictEqual(f.after, "What is a WBS?", "right under the message that failed");
      assert.strictEqual(f.chat, 1, "the message is kept");
      fail = false;
      await page.click("#np-retry");
      await page.waitForFunction(function () { return PMP.Store.state.chat.length === 2; });
      var after = await page.evaluate(function () { return { roles: PMP.Store.state.chat.map(function (c) { return c.r; }), retry: !!document.getElementById("np-retry") }; });
      assert.deepStrictEqual(after, { roles: ["user", "model"], retry: false }, "not sent twice into the log");
      page.errors = page.errors.filter(function (e) { return !/status of 503/.test(e); });
      await lib.clean(page);
      await page.ctx.close();
    }
  }
];
