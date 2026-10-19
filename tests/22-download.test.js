"use strict";
/* The Download menu and pages (§10.28), the app icon in the rail and the
   browser tab, and the privacy copy on Backup and packs. */
var assert = require("assert");

module.exports = [
  {
    name: "Download sits after Notes; each option opens its page with a file link and steps",
    fn: async function (h) {
      var page = await h.open("fresh");
      var order = await page.evaluate(function () {
        var tb = document.querySelector(".topbar");
        return Array.prototype.map.call(tb.children, function (n) { return n.id; }).filter(Boolean);
      });
      assert.ok(order.indexOf("dl-menu") === order.indexOf("np-toggle") + 1, "Download follows Notes: " + order);

      await page.click("#dl-menu > summary");
      var links = await page.$$eval(".dl-list a", function (as) { return as.map(function (a) { return a.textContent; }); });
      assert.deepStrictEqual(links, ["Web app", "Android (APK)", "iPhone (IPA)"]);

      await page.click(".dl-list a[href='#/download?p=android']");
      await page.waitForTimeout(150);
      var a = await page.evaluate(function () {
        return {
          open: document.getElementById("dl-menu").open,
          title: document.querySelector("#view h2").textContent,
          href: document.querySelector("#view a.btn--primary").getAttribute("href"),
          steps: document.querySelectorAll("#view .dl-steps li").length
        };
      });
      assert.deepStrictEqual(a, { open: false, title: "Android app",
        href: "https://github.com/JahangirB/PMP/releases/latest/download/PMP-Prep.apk", steps: 4 });

      await page.evaluate(function () { PMP.Router.navigate("download", { p: "ios" }); });
      await page.waitForTimeout(150);
      var i = await page.evaluate(function () {
        return {
          title: document.querySelector("#view h2").textContent,
          href: document.querySelector("#view a.btn--primary").getAttribute("href"),
          warning: /never been tested on a real iPhone/.test(document.getElementById("view").textContent),
          linkedin: !!document.querySelector("#view a[href*='linkedin.com/in/jahangir-babayev']")
        };
      });
      assert.deepStrictEqual(i, { title: "iPhone app", href: "https://github.com/JahangirB/PMP/releases/download/ios/PMP-Prep.ipa",
        warning: true, linkedin: true });

      await page.evaluate(function () { PMP.Router.navigate("download", { p: "web" }); });
      await page.waitForTimeout(150);
      var w = await page.evaluate(function () {
        var v = document.getElementById("view");
        return { site: !!v.querySelector("a[href='https://pmp-sandy.vercel.app/']"),
                 onDisk: /already using a downloaded copy/.test(v.textContent) };
      });
      assert.deepStrictEqual(w, { site: true, onDisk: true }, "opened from disk, no file to download again");
    }
  },
  {
    name: "Escape and a click elsewhere close the menu",
    fn: async function (h) {
      var page = await h.open("fresh");
      await page.click("#dl-menu > summary");
      await page.keyboard.press("Escape");
      assert.strictEqual(await page.evaluate(function () { return document.getElementById("dl-menu").open; }), false);
      await page.click("#dl-menu > summary");
      await page.click("#view-title");
      assert.strictEqual(await page.evaluate(function () { return document.getElementById("dl-menu").open; }), false);
    }
  },
  {
    name: "the app icon is the rail logo and the browser tab icon",
    fn: async function (h) {
      var page = await h.open("fresh");
      var r = await page.evaluate(function () {
        var link = document.querySelector("link[rel='icon']");
        var img = document.querySelector(".brand img.mark");
        return { link: !!link && /^data:image\/png;base64,/.test(link.href), same: !!img && img.src === link.href,
                 loaded: !!img && img.naturalWidth === 64 };
      });
      assert.deepStrictEqual(r, { link: true, same: true, loaded: true });
    }
  },
  {
    name: "the privacy copy names each way of opening the app and what Gemini sends",
    fn: async function (h) {
      var page = await h.open("fresh");
      await page.evaluate(function () { PMP.Router.navigate("data"); });
      await page.waitForTimeout(150);
      var t = await page.evaluate(function () { return document.querySelector("#view .view-hd").textContent; });
      assert.ok(/no network requests at all/.test(t), "downloaded copy");
      assert.ok(/pmp-sandy\.vercel\.app/.test(t) && /IP address/.test(t), "website host");
      assert.ok(/summary of your study progress/.test(t) && /does not include your notes/.test(t), "Gemini snapshot");
      assert.ok(!/nothing else goes with it/.test(t), "the old claim is gone");
    }
  }
];
