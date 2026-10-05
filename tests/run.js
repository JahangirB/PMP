"use strict";
/* node tests/run.js [filter]
   Runs every *.test.js in this directory, in name order. Each file exports an
   array of { name, fn(h, lib) }. A test passes by returning; any throw fails
   it. Exit code is the number of failures (capped at 1). */
var fs = require("fs");
var path = require("path");
var lib = require("./lib");

(async function main() {
  var filter = process.argv[2] || "";
  var files = fs.readdirSync(__dirname).filter(function (f) { return /\.test\.js$/.test(f); }).sort();
  var pw = lib.loadPlaywright();
  var browser = await pw.chromium.launch();
  var h = new lib.Harness(browser);
  var pass = 0, fail = 0, failures = [];
  for (var i = 0; i < files.length; i++) {
    var tests = require(path.join(__dirname, files[i]));
    for (var j = 0; j < tests.length; j++) {
      var t = tests[j];
      var label = files[i].replace(/\.test\.js$/, "") + " › " + t.name;
      if (filter && label.indexOf(filter) < 0) continue;
      var t0 = Date.now();
      try {
        await t.fn(h, lib);
        pass++;
        console.log("  ✓ " + label + "  (" + (Date.now() - t0) + " ms)");
      } catch (e) {
        fail++;
        failures.push(label);
        console.log("  ✗ " + label + "\n      " + String(e && e.stack || e).split("\n").slice(0, 30).join("\n      "));
      }
    }
  }
  await browser.close();
  console.log("\n" + pass + " passed, " + fail + " failed");
  if (failures.length) console.log("Failed:\n  " + failures.join("\n  "));
  process.exit(fail ? 1 : 0);
})().catch(function (e) { console.error(e); process.exit(1); });
