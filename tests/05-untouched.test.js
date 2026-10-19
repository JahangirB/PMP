"use strict";
/* Routes the coach does not touch render the same content and structure as
   they did before the coach landed. The reference is index.html at the
   Phase 0b commit (UI fixes in, no coach), written to a temporary file; only
   #view is compared, because the rail gained Today and Progress on purpose.
   On #/library only the tab panel is compared: the tab strip gained
   Abbreviations on purpose, and 07-abbreviations checks the strip itself.

   This was a pixel comparison until the Sky Clay restyle (§02, §03.29),
   which changed every pixel on purpose: new palette, Nunito, clay radii and
   shadows. A pixel reference taken from the restyled file would only check
   the restyle against itself, so the test now compares what the restyle
   must not change: every rendered element in order, with its tag, classes,
   ARIA and data attributes, link targets, inline style and text. Elements
   that are not rendered (display: none, [hidden]) are skipped, as the pixel
   comparison skipped them. Two attributes are ignored, both added by the
   restyle: data-dom on domain chips, so they take the phones' domain
   colours, and data-enter, which §08.3 sets on the view for a moment after
   a route change to run the staggered card entry. */
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
var ADDED = { exam: "#view .wb-write, #view .ex-case", practice: '#view .mode-opt[for="mode-reading"], #view .keylist li[data-k], #case-quick' };
/* The case studies (§10.26) added a row to #/exam's sitting card and the
   "Case studies" quick start to #/practice, on purpose; 20-cases covers
   both. The 12 graphic items took the core pack from 348 to 360, so the
   pool figures on #/practice read 360; they are put back to the
   reference's figure before the comparison. */
var CORE_NOW = "360", CORE_REF = "348";

module.exports = [
  {
    name: "routes the coach does not touch are unchanged in content and structure",
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
          await b.evaluate(function (x) {
            document.querySelectorAll("#view .pool-n, #view .hint").forEach(function (n) {
              var w = document.createTreeWalker(n, NodeFilter.SHOW_TEXT), t;
              while ((t = w.nextNode())) t.nodeValue = t.nodeValue.split(x[0]).join(x[1]);
            });
          }, [CORE_NOW, CORE_REF]);
          var sel = r === "library" ? '#view [role="tabpanel"]' : "#view";
          var pa = await a.evaluate(lib.structure, sel);
          var pb = await b.evaluate(lib.structure, sel);
          if (pa.join("\n") !== pb.join("\n")) {
            diff.push(fx + " #/" + r);
            var at = 0;
            while (at < pa.length && pa[at] === pb[at]) at++;
            console.log("      first difference on " + fx + " #/" + r + " at line " + at + ":\n        before: " + pa[at] + "\n        after:  " + pb[at]);
            fs.mkdirSync(path.join(__dirname, "screenshots", "untouched"), { recursive: true });
            fs.writeFileSync(path.join(__dirname, "screenshots", "untouched", fx + "-" + r + "-before.txt"), pa.join("\n"));
            fs.writeFileSync(path.join(__dirname, "screenshots", "untouched", fx + "-" + r + "-after.txt"), pb.join("\n"));
          }
        }
        await a.ctx.close();
        await b.ctx.close();
      }
      assert.deepStrictEqual(diff, []);
    }
  }
];
