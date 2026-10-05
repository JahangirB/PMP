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
  await ctx.addInitScript(function (payload) {
    try {
      if (window.sessionStorage.getItem("pmp.test.seeded")) return;
      window.sessionStorage.setItem("pmp.test.seeded", "1");
      Object.keys(payload).forEach(function (k) {
        window.localStorage.setItem("pmp.v2." + k, JSON.stringify({ v: 2, d: payload[k] }));
      });
    } catch (e) { /* the test that needs storage will fail on its own */ }
  }, slices);
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

module.exports = { loadPlaywright: loadPlaywright, Harness: Harness, clean: clean, go: go, shot: shot, FILE: FILE };
