"use strict";
/* Test harness for index.html. Opens the file from file://, seeds state
   through localStorage before the app's own scripts run, and fails on any
   uncaught page error or on anything the app routes to PMP.report.

   Playwright is resolved from the project first and the global install
   second. Chromium is pre-installed; nothing here downloads a browser. */
var path = require("path");
var fs = require("fs");
var childProcess = require("child_process");

function loadPlaywright() {
  try { return require("playwright"); } catch (e) { /* fall through */ }
  var root = childProcess.execSync("npm root -g").toString().trim();
  return require(path.join(root, "playwright"));
}

var FILE = "file://" + path.resolve(__dirname, "..", "index.html");
var SHOTS = path.resolve(__dirname, "screenshots");

function Harness(browser) {
  this.browser = browser;
  this.seeds = null;
}

/* One scratch page builds every fixture, because the builders need the
   app's own content (question ids, ECO tasks) and its planner. */
Harness.prototype.buildFixtures = async function () {
  if (this.seeds) return this.seeds;
  var ctx = await this.browser.newContext();
  var page = await ctx.newPage();
  await page.goto(FILE);
  await page.waitForFunction(function () { return window.PMP && PMP.Store.state.sys.ready; });
  var src = fs.readFileSync(path.join(__dirname, "fixtures.js"), "utf8");
  this.seeds = await page.evaluate(src);
  await ctx.close();
  return this.seeds;
};

/* Opens a fresh context with the named fixture written to localStorage
   before load, in the { v, d } wrapper §07.4's hydrate reads. */
Harness.prototype.open = async function (fixture, opts) {
  opts = opts || {};
  var seeds = await this.buildFixtures();
  var slices = fixture ? seeds[fixture] : {};
  if (!slices) throw new Error("No fixture called " + fixture);
  var ctx = await this.browser.newContext({ viewport: opts.viewport || { width: 1400, height: 900 } });
  await ctx.addInitScript(function (arg) {
    try {
      if (window.sessionStorage.getItem("pmp.test.seeded")) return;
      window.sessionStorage.setItem("pmp.test.seeded", "1");
      Object.keys(arg.slices).forEach(function (k) {
        window.localStorage.setItem("pmp.v2." + k, JSON.stringify({ v: 2, d: arg.slices[k] }));
      });
      /* Before you start (§10.38) is taken as read unless a test asks for it. */
      if (!arg.welcome) window.localStorage.setItem("pmp.welcome", "1");
    } catch (e) { /* the test that needs storage will fail on its own */ }
  }, { slices: slices, welcome: !!opts.welcome });
  var page = await ctx.newPage();
  var errors = [];
  page.on("pageerror", function (e) { errors.push("pageerror: " + e.message); });
  page.on("console", function (m) { if (m.type() === "error") errors.push("console: " + m.text()); });
  page.errors = errors;
  await page.goto((opts.file || FILE) + (opts.hash || ""));
  await page.waitForFunction(function () { return window.PMP && PMP.Store.state.sys.ready; });
  await page.waitForTimeout(50);
  page.ctx = ctx;
  return page;
};

/* Throws if the page has logged an error or the app has reported one. */
async function clean(page, where) {
  var last = await page.evaluate(function () { return PMP.Store.state.ui.lastError; });
  var errs = page.errors.slice();
  if (last) errs.push("PMP.report: " + last.where + ": " + last.message);
  if (errs.length) throw new Error((where ? where + ": " : "") + errs.join(" | "));
}

async function go(page, hash) {
  await page.evaluate(function (h) { window.location.hash = h; }, hash);
  await page.waitForTimeout(60);
}

async function shot(page, dir, name) {
  var d = path.join(SHOTS, dir);
  fs.mkdirSync(d, { recursive: true });
  await page.screenshot({ path: path.join(d, name + ".png") });
}

/* Runs in the page (page.evaluate(lib.structure, selector)). One line per
   rendered element or text run under the selector, indented by depth, in
   document order: tag, classes, ARIA and data attributes, link targets,
   inline style and text. Used where a test holds a surface to an earlier
   commit's content and structure; those tests compared pixels until the
   Sky Clay restyle changed every pixel on purpose. Elements not rendered are
   skipped, as a screenshot skips them, and so are elements scrolled out of
   a scrolling box (a virtual list's off-screen rows); text clipped to one line with an
   ellipsis is compared by its first 40 characters. data-dom (domain chip colours) and
   data-enter (the entry animation flag) are restyle-only and ignored. */
function structure(sel) {
  var IGNORE = { "data-dom": 1, "data-enter": 1 };
  var out = [];
  function walk(n, depth) {
    var pad = new Array(depth + 1).join(" ");
    if (n.nodeType === 3) {
      var t = n.nodeValue.replace(/\s+/g, " ").trim();
      /* A one-line run cut off with an ellipsis shows only its start, and
         how much of it shows depends on the face; only the first 40
         characters are held to the reference. */
      var ps = getComputedStyle(n.parentNode);
      if (ps.textOverflow === "ellipsis" && ps.whiteSpace === "nowrap" && t.length > 40) t = t.slice(0, 40) + "\u2026";
      if (t) out.push(pad + JSON.stringify(t));
      return;
    }
    if (n.nodeType !== 1) return;
    /* Not rendered: [hidden], display: none, or inside a closed <details>. */
    if (n.hidden || (n.getClientRects().length === 0 && getComputedStyle(n).display !== "contents")) return;
    /* Scrolled out of a scrolling box (the glossary's virtual list): not
       on screen, so not in a screenshot either. */
    var r = n.getBoundingClientRect();
    for (var up = n.parentElement; up && up !== document.body; up = up.parentElement) {
      var ov = getComputedStyle(up).overflowY;
      if (ov === "auto" || ov === "scroll" || ov === "hidden") {
        var u = up.getBoundingClientRect();
        if (r.bottom <= u.top || r.top >= u.bottom) return;
      }
    }
    var attrs = [];
    for (var i = 0; i < n.attributes.length; i++) {
      var a = n.attributes[i];
      if (IGNORE[a.name]) continue;
      if (a.name === "class" || a.name === "id" || a.name === "role" || a.name === "href" || a.name === "type" ||
          a.name === "style" || a.name === "for" || a.name === "name" || a.name === "value" || a.name === "disabled" ||
          a.name === "tabindex" || a.name.indexOf("aria-") === 0 || a.name.indexOf("data-") === 0) {
        attrs.push(a.name + "=" + JSON.stringify(a.value));
      }
    }
    attrs.sort();
    out.push(pad + "<" + n.tagName.toLowerCase() + (attrs.length ? " " + attrs.join(" ") : "") + ">");
    /* A closed select shows only its chosen option, so only that is held to
       the reference, as on screen; the lists themselves grew with content
       (the graphic questions' topics) on purpose. */
    if (n.tagName === "SELECT") { var o = n.options[n.selectedIndex]; out.push(pad + " " + JSON.stringify(o ? o.text : "")); return; }
    for (var c = n.firstChild; c; c = c.nextSibling) walk(c, depth + 1);
  }
  walk(document.querySelector(sel), 0);
  return out;
}

module.exports = { loadPlaywright: loadPlaywright, Harness: Harness, clean: clean, go: go, shot: shot, structure: structure, FILE: FILE };
