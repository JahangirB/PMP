/* ============================================================================
   PMP.PackLoader — external question pack loading for the hosted build
   ----------------------------------------------------------------------------
   WHAT THIS DOES
   Fetches packs/manifest.json, then each listed pack file, validates every
   pack through the existing PMP.validate.pack() (section 09.6), and appends
   good questions into PMP.Content.all. Practice and Mock already draw
   randomly from that pool, so no engine changes are needed.

   INTEGRATION (three steps)

   1. Serve this file alongside index.html and add, just before the closing
      </body> tag (after the main inline script):

          <script src="pack-loader.js"></script>

   2. Find where PMP.Content is created (search for "PMP.Content"). It holds
      the inline questions in something like:

          PMP.Content = (function () {
            var all = QUESTIONS.slice();   // <- whatever the inline array is
            return { all: all, ... };
          })();

      Add ONE method to whatever object it returns:

          addPack: function (qs) { for (var i = 0; i < qs.length; i++) all.push(qs[i]); }

      If PMP.Content.all is a plain array, you can skip step 2 entirely —
      the loader pushes directly into it.

   3. Put manifest.json and your pack files in a /packs folder next to
      index.html. Done.

   OFFLINE BEHAVIOUR
   First load caches each validated pack in localStorage. Later loads try the
   network first (fresh content wins), and fall back to the cache when
   offline — so the app degrades to "last known bank" instead of breaking.

   FAILURE BEHAVIOUR
   A pack that fails validation or fails to load is skipped; the inline bank
   always remains available. Every failure is recorded and shown via
   PMP.UI.toast and is queryable at PMP.PackLoader.report() — wire that into
   your Content audit panel when convenient.
   ========================================================================== */

PMP.PackLoader = (function () {

  var MANIFEST_URL = 'packs/manifest.json';
  var CACHE_PREFIX = 'pmp-pack-cache:';
  var failures = [];   // { where, msg } — same shape as your audit issues
  var loaded = [];     // { file, version, count }

  function err(where, msg) { failures.push({ where: where, msg: msg }); }

  /* Fetch JSON with a network-first / cache-fallback strategy. */
  function fetchJSON(url, cacheKey) {
    return fetch(url, { cache: 'no-cache' })
      .then(function (res) {
        if (!res.ok) throw new Error('HTTP ' + res.status);
        return res.json();
      })
      .then(function (json) {
        if (cacheKey) {
          try { localStorage.setItem(CACHE_PREFIX + cacheKey, JSON.stringify(json)); }
          catch (e) { /* quota — caching is best-effort */ }
        }
        return { json: json, fromCache: false };
      })
      .catch(function (netErr) {
        if (!cacheKey) throw netErr;
        var raw = null;
        try { raw = localStorage.getItem(CACHE_PREFIX + cacheKey); } catch (e) {}
        if (!raw) throw netErr;
        return { json: JSON.parse(raw), fromCache: true };
      });
  }

  /* Validate one pack object and return its usable questions.
     Questions whose id already exists in the bank are dropped (duplicate ids
     corrupt attempt history, since attempts are keyed by question id). */
  function usableQuestions(packObj, file) {
    var r = PMP.validate.pack(packObj);
    r.errors.forEach(function (e) { err(file + ' ' + e.where, e.msg); });
    if (!r.ok()) return [];
    var seen = {};
    PMP.Content.all.forEach(function (q) { seen[q.id] = true; });
    return packObj.questions.filter(function (q) {
      if (q.retired) return false;
      if (seen[q.id]) {
        err(file, 'Duplicate id "' + q.id + '" already in the bank — skipped.');
        return false;
      }
      seen[q.id] = true;
      return true;
    });
  }

  function addToBank(qs) {
    if (!qs.length) return;
    if (typeof PMP.Content.addPack === 'function') PMP.Content.addPack(qs);
    else if (Array.isArray(PMP.Content.all)) {
      for (var i = 0; i < qs.length; i++) PMP.Content.all.push(qs[i]);
    } else {
      err('pack-loader', 'PMP.Content has no addPack() and all is not an array. See integration step 2.');
    }
  }

  /* Entry point. Call once at startup, after PMP.Content exists.
     Returns a Promise so boot can wait for the bank before first render. */
  function load() {
    return fetchJSON(MANIFEST_URL, 'manifest')
      .then(function (m) {
        var packs = (m.json && m.json.packs) || [];
        return packs.reduce(function (chain, p) {
          return chain.then(function () {
            var url = 'packs/' + p.file + '?v=' + encodeURIComponent(p.version || '0');
            return fetchJSON(url, p.file).then(function (res) {
              var qs = usableQuestions(res.json, p.file);
              addToBank(qs);
              loaded.push({ file: p.file, version: p.version, count: qs.length, cached: res.fromCache });
            }).catch(function (e) {
              err(p.file, 'Could not load pack: ' + e.message);
            });
          });
        }, Promise.resolve());
      })
      .then(function () {
        var total = loaded.reduce(function (a, p) { return a + p.count; }, 0);
        if (PMP.UI && PMP.UI.toast) {
          if (total > 0) PMP.UI.toast('Question bank updated: ' + total + ' questions loaded from ' + loaded.length + ' pack(s).', 'ok');
          if (failures.length > 0) PMP.UI.toast(failures.length + ' pack issue(s) — see Content audit.', 'warn');
        }
      })
      .catch(function (e) {
        /* No manifest and no cache: the inline bank still works. */
        err('manifest', 'No pack manifest found (' + e.message + '). Using inline bank only.');
      });
  }

  return {
    load: load,
    report: function () { return { loaded: loaded.slice(), failures: failures.slice() }; }
  };

})();

/* Boot hook. If your bootstrap (section 12) has a startup sequence, call
   PMP.PackLoader.load() there BEFORE the first route renders, ideally:
       PMP.Store.hydrate().then(PMP.PackLoader.load).then(PMP.Router.start)
   Otherwise this fallback fires on DOMContentLoaded. */
if (document.readyState === 'loading') {
  document.addEventListener('DOMContentLoaded', function () { PMP.PackLoader.load(); });
} else {
  PMP.PackLoader.load();
}
