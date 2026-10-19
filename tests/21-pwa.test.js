"use strict";
/* S12, the installable web app (§01.6b, §10.27, manifest.webmanifest, sw.js,
   icons/): the manifest is valid and linked when served, nothing is linked
   or registered from file://, and served over a local http server the
   service worker registers and the page opens again offline. */
var assert = require("assert");
var fs = require("fs");
var path = require("path");
var http = require("http");

var ROOT = path.join(__dirname, "..");
var TYPES = { ".html": "text/html; charset=utf-8", ".js": "text/javascript", ".json": "application/json",
              ".webmanifest": "application/manifest+json", ".png": "image/png" };

/* A plain static server over the repository, as GitHub Pages would serve it. */
function serve() {
  return new Promise(function (resolve) {
    var misses = [];
    var server = http.createServer(function (req, res) {
      var p = decodeURIComponent(req.url.split("?")[0]);
      if (p.slice(-1) === "/") p += "index.html";
      var file = path.join(ROOT, path.normalize(p));
      if (file.indexOf(ROOT) !== 0 || !fs.existsSync(file) || fs.statSync(file).isDirectory()) {
        misses.push(p); res.writeHead(404); res.end(); return;
      }
      res.writeHead(200, { "Content-Type": TYPES[path.extname(file)] || "application/octet-stream", "Cache-Control": "no-cache" });
      fs.createReadStream(file).pipe(res);
    });
    server.listen(0, "127.0.0.1", function () {
      resolve({ url: "http://127.0.0.1:" + server.address().port + "/", misses: misses, close: function () { server.close(); } });
    });
  });
}

module.exports = [
  {
    name: "manifest: valid JSON with name, icons, start_url and display; every icon exists at its size",
    fn: async function () {
      var m = JSON.parse(fs.readFileSync(path.join(ROOT, "manifest.webmanifest"), "utf8"));
      assert.ok(m.name && m.short_name, "names");
      assert.strictEqual(m.display, "standalone");
      assert.ok(m.start_url && !/^\//.test(m.start_url), "a relative start_url keeps a Pages subpath working");
      assert.ok(m.scope && !/^\//.test(m.scope));
      var sizes = m.icons.map(function (i) { return i.sizes + " " + (i.purpose || "any"); });
      ["192x192 any", "512x512 any", "512x512 maskable"].forEach(function (s) { assert.ok(sizes.indexOf(s) >= 0, s); });
      m.icons.forEach(function (i) {
        var buf = fs.readFileSync(path.join(ROOT, i.src));
        assert.strictEqual(buf.toString("ascii", 1, 4), "PNG", i.src);
        var w = buf.readUInt32BE(16), h = buf.readUInt32BE(20);
        assert.strictEqual(w + "x" + h, i.sizes, i.src);
      });
      var sw = fs.readFileSync(path.join(ROOT, "sw.js"), "utf8");
      assert.ok(/var VERSION = "pmp-[^"]+";/.test(sw), "the cache is named by a VERSION constant");
      m.icons.concat([{ src: "manifest.webmanifest" }, { src: "index.html" }]).forEach(function (i) {
        assert.ok(sw.indexOf('"' + i.src + '"') >= 0, "sw.js caches " + i.src);
      });
    }
  },
  {
    name: "file://: no manifest link, no service worker registration, and Settings says there is nothing to install",
    fn: async function (h, lib) {
      var ctx = await h.browser.newContext();
      await ctx.addInitScript(function () {
        window.__swCalls = 0;
        if (navigator.serviceWorker && navigator.serviceWorker.register) {
          var real = navigator.serviceWorker.register.bind(navigator.serviceWorker);
          navigator.serviceWorker.register = function () { window.__swCalls++; return real.apply(null, arguments); };
        }
      });
      var page = await ctx.newPage();
      var errors = [];
      page.on("pageerror", function (e) { errors.push(e.message); });
      page.on("console", function (m) { if (m.type() === "error") errors.push(m.text()); });
      await page.goto(lib.FILE + "#/settings");
      await page.waitForFunction(function () { return window.PMP && PMP.Store.state.sys.ready; });
      await page.waitForTimeout(300);
      var r = await page.evaluate(function () {
        return {
          links: document.querySelectorAll('link[rel="manifest"], link[rel="apple-touch-icon"]').length,
          calls: window.__swCalls, supported: PMP.Install.status.supported, install: !!PMP.INSTALL,
          card: document.getElementById("install-card").textContent, btn: !!document.getElementById("install-btn")
        };
      });
      assert.strictEqual(r.links, 0);
      assert.strictEqual(r.calls, 0);
      assert.strictEqual(r.supported, false);
      assert.strictEqual(r.install, false);
      assert.ok(/open from a file, so it already works offline/.test(r.card), r.card);
      assert.strictEqual(r.btn, false);
      assert.deepStrictEqual(errors, []);
      await ctx.close();
    }
  },
  {
    name: "http: the manifest is linked, the service worker registers and caches the shell, and the page loads offline on reload",
    fn: async function (h, lib) {
      var srv = await serve();
      var ctx = await h.browser.newContext({ serviceWorkers: "allow" });
      var page = await ctx.newPage();
      var errors = [];
      page.on("pageerror", function (e) { errors.push("pageerror: " + e.message); });
      try {
        await page.goto(srv.url + "index.html#/today");
        await page.waitForFunction(function () { return window.PMP && PMP.Store.state.sys.ready; });
        var linked = await page.evaluate(function () {
          var l = document.querySelector('link[rel="manifest"]');
          return l && fetch(l.href).then(function (r) { return r.json(); }).then(function (m) { return { href: l.getAttribute("href"), name: m.name, display: m.display }; });
        });
        assert.deepStrictEqual(linked, { href: "manifest.webmanifest", name: "PMP Prep Suite", display: "standalone" });
        /* Registered, active and controlling the page. */
        await page.waitForFunction(function () { return PMP.Install.status.registered; }, null, { timeout: 15000 });
        await page.evaluate(function () { return navigator.serviceWorker.ready; });
        await page.waitForFunction(function () { return !!navigator.serviceWorker.controller; }, null, { timeout: 15000 });
        var cached = await page.evaluate(function () {
          return caches.keys().then(function (keys) {
            return caches.open(keys[0]).then(function (c) { return c.keys(); }).then(function (reqs) {
              return { keys: keys, urls: reqs.map(function (r) { return new URL(r.url).pathname; }) };
            });
          });
        });
        assert.strictEqual(cached.keys.length, 1);
        assert.ok(/^pmp-/.test(cached.keys[0]));
        ["/index.html", "/manifest.webmanifest", "/icons/icon-192.png", "/icons/icon-512.png", "/packs/index.json"].forEach(function (u) {
          assert.ok(cached.urls.indexOf(u) >= 0, u + " in " + cached.urls.join(" "));
        });
        assert.ok(cached.urls.filter(function (u) { return /^\/packs\/.+\.json$/.test(u) && u !== "/packs/index.json"; }).length > 0, "pack shards cached");
        var scope = await page.evaluate(function () { return PMP.Install.status.scope; });
        assert.strictEqual(scope, srv.url);
        /* Offline: the reload is served from the cache and the app boots. */
        await ctx.setOffline(true);
        await page.reload();
        await page.waitForFunction(function () { return window.PMP && PMP.Store.state.sys.ready; }, null, { timeout: 15000 });
        await page.waitForTimeout(200);
        var off = await page.evaluate(function () {
          return { online: navigator.onLine, view: document.getElementById("view").textContent.length, title: document.title };
        });
        assert.strictEqual(off.online, false);
        assert.ok(off.view > 50, "the app rendered offline");
        assert.strictEqual(off.title.indexOf("PMP Prep Suite") >= 0 || off.title.length > 0, true);
        /* The core pool is all there offline, and the cached packs load too. */
        var pool = await page.evaluate(function () { return PMP.Content.all().length >= PMP.Packs.core && PMP.Packs.core === 360; });
        assert.ok(pool);
        await ctx.setOffline(false);
        assert.deepStrictEqual(errors, []);
      } finally {
        await ctx.close();
        srv.close();
      }
    }
  },
  {
    name: "http: the Install button appears only while the browser offers, and uses the offer once",
    fn: async function (h, lib) {
      var srv = await serve();
      var ctx = await h.browser.newContext({ serviceWorkers: "block" });
      var page = await ctx.newPage();
      try {
        await page.goto(srv.url + "index.html#/settings");
        await page.waitForFunction(function () { return window.PMP && PMP.Store.state.sys.ready; });
        await page.waitForTimeout(100);
        var before = await page.evaluate(function () { return { btn: !!document.getElementById("install-btn"), text: document.getElementById("install-card").textContent }; });
        assert.strictEqual(before.btn, false);
        assert.ok(/has not offered to install/.test(before.text), before.text);
        await page.evaluate(function () {
          var e = new Event("beforeinstallprompt", { cancelable: true });
          window.__prompted = 0;
          e.prompt = function () { window.__prompted++; return Promise.resolve(); };
          e.userChoice = Promise.resolve({ outcome: "accepted" });
          window.dispatchEvent(e);
          window.__prevented = e.defaultPrevented;
        });
        var offered = await page.evaluate(function () { return { btn: !!document.getElementById("install-btn"), prevented: window.__prevented }; });
        assert.deepStrictEqual(offered, { btn: true, prevented: true });
        await page.evaluate(function () { document.getElementById("install-btn").click(); });
        await page.waitForTimeout(100);
        var after = await page.evaluate(function () { return { n: window.__prompted, btn: !!document.getElementById("install-btn"), text: document.getElementById("install-card").textContent }; });
        assert.strictEqual(after.n, 1);
        assert.strictEqual(after.btn, false);
        assert.ok(/^Install.*Installed\./.test(after.text), after.text);
      } finally {
        await ctx.close();
        srv.close();
      }
    }
  }
];
