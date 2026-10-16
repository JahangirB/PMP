"use strict";
/* Routes the coach does not touch render pixel-for-pixel as they did before
   the coach landed. The reference is index.html at the Phase 0b commit
   (UI fixes in, no coach), written to a temporary file; only #view is
   compared, because the rail gained Today and Progress on purpose. On
   #/library only the tab panel is compared: the tab strip gained
   Abbreviations on purpose, and 07-abbreviations checks the strip itself.
   The sticky top bar is masked in both: it overlaps a full-length element
   screenshot, and it gained the Notes toggle on purpose (08-notepad). */
var assert = require("assert");
var fs = require("fs");
var os = require("os");
var path = require("path");
var cp = require("child_process");

var REF_COMMIT = "3f6ff75";
/* #/formulas and #/cards are learning surfaces that now spell abbreviations
   out on first use (§09.5a), on purpose; 09-learning-abbr covers them. The
   practice surfaces stay here precisely because they must not change.
   #/data gained a paragraph on the optional AI tab (§12.2.2), on purpose.
   #/journal gained a sixth cause (Calculation slip) and #/analytics reads
   its heat-map bands from the shared rules (§01.8), both on purpose with the
   shared progress format; their own tests cover them (12-sync).
   #/exam gained the optional "write it down" card before a sitting (§10.20)
   on purpose; it is taken out of the new page before the comparison, so
   everything else on the page is still held to the reference, and
   13-wellbeing covers the card itself.
   #/practice gained the Reading drill mode and three lines of keyboard help
   for the answering tools (§10.02, S5) on purpose; both are taken out before
   the comparison, and 16-answering covers them. */
var ROUTES = ["library", "exam", "practice", "scenarios"];
var ADDED = { exam: "#view .wb-write", practice: '#view .mode-opt[for="mode-reading"], #view .keylist li[data-k]' };

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
          if (ADDED[r]) await b.evaluate(function (sel) { document.querySelectorAll(sel).forEach(function (n) { n.remove(); }); }, ADDED[r]);
          var sel = r === "library" ? '#view [role="tabpanel"]' : "#view";
          var pa = await a.locator(sel).screenshot({ mask: [a.locator(".topbar"), b.locator(".topbar")] });
          var pb = await b.locator(sel).screenshot({ mask: [a.locator(".topbar"), b.locator(".topbar")] });
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
