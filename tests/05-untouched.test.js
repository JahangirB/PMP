"use strict";
/* Routes the coach does not touch render pixel-for-pixel as they did before
   the coach landed. The reference is index.html at the Phase 0b commit
   (UI fixes in, no coach), written to a temporary file; only #view is
   compared, because the rail gained Today and Progress on purpose. */
var assert = require("assert");
var fs = require("fs");
var os = require("os");
var path = require("path");
var cp = require("child_process");

var REF_COMMIT = "3f6ff75";
var ROUTES = ["formulas", "library", "analytics", "journal", "exam", "data", "practice", "scenarios", "cards"];

module.exports = [
  {
    name: "routes the coach does not touch are unchanged",
    fn: async function (h, lib) {
      var ref = path.join(os.tmpdir(), "pmp-ref-" + REF_COMMIT + ".html");
      fs.writeFileSync(ref, cp.execSync("git show " + REF_COMMIT + ":index.html", { cwd: path.join(__dirname, ".."), maxBuffer: 64 << 20 }));
      var diff = [];
      for (var fx of ["fresh", "day3", "misread_heavy"]) {
        var a = await h.open(fx, { file: "file://" + ref });
        var b = await h.open(fx);
        for (var r of ROUTES) {
          await lib.go(a, "#/" + r);
          await lib.go(b, "#/" + r);
          var pa = await a.locator("#view").screenshot();
          var pb = await b.locator("#view").screenshot();
          if (!pa.equals(pb)) {
            diff.push(fx + " #/" + r);
            fs.mkdirSync(path.join(__dirname, "screenshots", "untouched"), { recursive: true });
            fs.writeFileSync(path.join(__dirname, "screenshots", "untouched", fx + "-" + r + "-before.png"), pa);
            fs.writeFileSync(path.join(__dirname, "screenshots", "untouched", fx + "-" + r + "-after.png"), pb);
          }
        }
        await a.ctx.close();
        await b.ctx.close();
      }
      assert.deepStrictEqual(diff, []);
    }
  }
];
