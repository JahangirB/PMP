"use strict";
/* In-page drivers shared by the UI tests. */

/* Answers the question on screen through the controls a person would use,
   whatever its type, then presses the primary button until the drill moves
   on. Returns false once the drill has finished. */
async function answerOne(page) {
  return page.evaluate(function () {
    var s = PMP.Practice.session;
    if (!s || s.finished) return false;
    var card = document.getElementById("drill-card");
    var radio = card.querySelector('input[type="radio"]:not(:disabled)');
    var boxes = card.querySelectorAll('input[type="checkbox"]:not(:disabled)');
    if (radio) radio.click();
    else if (boxes.length) boxes[0].click();
    card.querySelectorAll("select").forEach(function (sel) {
      if (sel.options.length > 1) { sel.selectedIndex = 1; sel.dispatchEvent(new Event("change", { bubbles: true })); }
    });
    card.querySelectorAll('input[type="text"], input:not([type])').forEach(function (inp) {
      inp.value = "x"; inp.dispatchEvent(new Event("input", { bubbles: true }));
    });
    var hot = card.querySelector(".hot");
    if (hot) hot.dispatchEvent(new MouseEvent("click", { bubbles: true }));
    var item = s.items[s.i];
    var primary = card.querySelector(".btn--primary");
    if (primary && !primary.disabled) primary.click();          /* Check answer */
    primary = card.querySelector(".btn--primary");
    if (primary && !primary.disabled && item.committed) primary.click();   /* Next */
    return !PMP.Practice.session.finished;
  });
}

async function finishDrill(page, max) {
  for (var i = 0; i < (max || 60); i++) {
    var more = await answerOne(page);
    await page.waitForTimeout(20);
    if (!more) return;
  }
  throw new Error("drill did not finish");
}

async function clickText(page, selector, text) {
  await page.evaluate(function (a) {
    var b = Array.prototype.filter.call(document.querySelectorAll(a.sel), function (x) { return x.textContent.trim().indexOf(a.text) === 0; })[0];
    if (!b) throw new Error("Nothing matching " + a.sel + " “" + a.text + "”");
    b.click();
  }, { sel: selector, text: text });
  await page.waitForTimeout(80);
}

module.exports = { answerOne: answerOne, finishDrill: finishDrill, clickText: clickText };
