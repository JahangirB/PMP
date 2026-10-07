"use strict";
/* The notes formatting ribbon (§12.2.1): browser commands for bold, italic,
   underline, line-through and lists; class-wrapped overline, text colour
   and highlight; plain-text paste; and the allowlist every stored note
   passes through on load. */
var assert = require("assert");

/* Puts "one two three" in the note and selects the word `word`. */
async function selectWord(page, word, text) {
  await page.evaluate(function (a) {
    var ed = document.getElementById("np-text");
    if (a.text !== null) { ed.innerHTML = ""; ed.appendChild(document.createTextNode(a.text)); ed.dispatchEvent(new Event("input")); }
    var walk = document.createTreeWalker(ed, NodeFilter.SHOW_TEXT), n;
    while ((n = walk.nextNode())) {
      var at = n.nodeValue.indexOf(a.word);
      if (at >= 0) {
        ed.focus();
        var r = document.createRange();
        r.setStart(n, at); r.setEnd(n, at + a.word.length);
        var sel = window.getSelection(); sel.removeAllRanges(); sel.addRange(r);
        return;
      }
    }
    throw new Error("no " + a.word);
  }, { word: word, text: text === undefined ? null : text });
  await page.waitForTimeout(30);   /* selectionchange is async */
}
async function html(page) { return page.evaluate(function () { return document.getElementById("np-text").innerHTML; }); }
async function open(h) {
  var page = await h.open("fresh");
  await page.click("#np-toggle");
  return page;
}

module.exports = [
  {
    name: "bold, italic, underline, line-through and overline on the selection",
    fn: async function (h, lib) {
      var page = await open(h);
      await selectWord(page, "two", "one two three");
      await page.click('#np-rib [data-cmd="bold"]');
      assert.strictEqual(await html(page), "one <b>two</b> three");
      assert.strictEqual(await page.getAttribute('#np-rib [data-cmd="bold"]', "aria-pressed"), "true", "the button shows it is on");
      for (var c of ["italic", "underline", "strikeThrough"]) await page.click('#np-rib [data-cmd="' + c + '"]');
      var x = await html(page);
      ["<b>", "<i>", "<u>"].forEach(function (t) { assert.ok(x.indexOf(t) >= 0, t + " in " + x); });
      assert.ok(/<(strike|s)>/.test(x), x);
      await selectWord(page, "three");
      await page.click('#np-rib [data-cmd="overline"]');
      assert.ok((await html(page)).indexOf('<span class="np-o">three</span>') >= 0);
      await selectWord(page, "three");
      await page.click('#np-rib [data-cmd="overline"]');
      assert.ok((await html(page)).indexOf("np-o") < 0, "pressed again, it comes off");
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "text colour and highlight cover exactly the selected characters",
    fn: async function (h, lib) {
      var page = await open(h);
      await selectWord(page, "PV", "EV and PV and AC");
      await page.click('#np-rib [data-menu="c"]');
      assert.strictEqual(await page.isVisible("#np-pop-c"), true);
      await page.click('#np-pop-c [data-c="red"]');
      assert.strictEqual(await page.isVisible("#np-pop-c"), false, "the palette closes after a pick");
      assert.strictEqual(await html(page), 'EV and <span class="np-c-red">PV</span> and AC');
      await selectWord(page, "AC");
      await page.click('#np-rib [data-menu="h"]');
      await page.click('#np-pop-h [data-h="yellow"]');
      assert.strictEqual(await html(page), 'EV and <span class="np-c-red">PV</span> and <mark class="np-h-yellow">AC</mark>');
      /* The colours are theme tokens: readable in both themes. */
      var colours = await page.evaluate(function () {
        var m = document.querySelector("#np-text mark"), sp = document.querySelector("#np-text .np-c-red");
        var a = [getComputedStyle(sp).color, getComputedStyle(m).backgroundColor];
        document.documentElement.setAttribute("data-theme", document.documentElement.getAttribute("data-theme") === "dark" ? "light" : "dark");
        return a.concat([getComputedStyle(sp).color, getComputedStyle(m).backgroundColor]);
      });
      assert.notStrictEqual(colours[0], colours[2]);
      assert.notStrictEqual(colours[1], colours[3]);
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "bulleted and numbered lists; clear formatting takes everything off",
    fn: async function (h, lib) {
      var page = await open(h);
      await selectWord(page, "item", "item");
      await page.click('#np-rib [data-cmd="insertUnorderedList"]');
      assert.ok(/<ul><li>item<\/li><\/ul>/.test(await html(page)), await html(page));
      await page.click('#np-rib [data-cmd="insertOrderedList"]');
      assert.ok(/<ol><li>item<\/li><\/ol>/.test(await html(page)), await html(page));
      var marker = await page.evaluate(function () { return getComputedStyle(document.querySelector("#np-text ol")).listStyleType; });
      assert.strictEqual(marker, "decimal");

      await selectWord(page, "two", "one two three");
      await page.click('#np-rib [data-cmd="bold"]');
      await page.click('#np-rib [data-menu="c"]');
      await page.click('#np-pop-c [data-c="blue"]');
      await page.click('#np-rib [data-menu="h"]');
      await page.click('#np-pop-h [data-h="pink"]');
      await selectWord(page, "two");
      await page.click('#np-rib [data-cmd="clear"]');
      assert.strictEqual((await html(page)).replace(/<\/?span>/g, ""), "one two three");
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "formatting is saved and comes back after a refresh",
    fn: async function (h, lib) {
      var page = await open(h);
      await selectWord(page, "CPI", "CPI below 1");
      await page.click('#np-rib [data-cmd="bold"]');
      await page.click('#np-rib [data-menu="h"]');
      await page.click('#np-pop-h [data-h="green"]');
      await page.waitForTimeout(700);
      await page.reload();
      await page.waitForFunction(function () { return window.PMP && PMP.Store.state.sys.ready; });
      assert.strictEqual(await html(page), '<b><mark class="np-h-green">CPI</mark></b> below 1');
      assert.strictEqual(await page.evaluate(function () { return PMP.Store.state.notepad.text; }), "CPI below 1");
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "paste brings text in, never markup",
    fn: async function (h, lib) {
      var page = await open(h);
      await selectWord(page, "X", "X");
      await page.evaluate(function () {
        var dt = new DataTransfer();
        dt.setData("text/html", '<b style="color:red">bold</b><img src=x onerror="window.__pwned=1">');
        dt.setData("text/plain", "bold");
        document.getElementById("np-text").dispatchEvent(new ClipboardEvent("paste", { clipboardData: dt, bubbles: true, cancelable: true }));
      });
      assert.strictEqual(await html(page), "bold");
      assert.strictEqual(await page.evaluate(function () { return window.__pwned; }), undefined);
      await lib.clean(page);
      await page.ctx.close();
    }
  },
  {
    name: "a stored note is cleaned on load; an old plain-text note keeps its lines",
    fn: async function (h, lib) {
      var page = await open(h);
      var r = await page.evaluate(function () {
        PMP.Store.commit(["notepad"], function (s) {
          s.notepad.html = '<p onclick="x()">Hi <img src=x onerror="window.__pwned=1"><script>window.__pwned=2<\/script>'
            + '<span class="np-c-red evil" style="position:fixed">red</span> <a href="javascript:alert(1)">link</a>'
            + '<mark class="np-h-yellow">mark</mark> <font color="red">font</font></p>';
        });
        var a = document.getElementById("np-text").innerHTML;
        PMP.Store.commit(["notepad"], function (s) { s.notepad.html = ""; s.notepad.text = "line one\nline <two>"; });
        return [a, document.getElementById("np-text").innerHTML];
      });
      assert.strictEqual(r[0], '<p>Hi <span class="np-c-red">red</span> link<mark class="np-h-yellow">mark</mark> font</p>');
      assert.strictEqual(r[1], "line one<br>line &lt;two&gt;");
      assert.strictEqual(await page.evaluate(function () { return window.__pwned; }), undefined);
      await lib.clean(page);
      await page.ctx.close();
    }
  }
];
