/* PMP Prep Suite — service worker (index.html §10.27).

   Registered only when the app is served over http(s), as on GitHub Pages;
   opening index.html from disk never touches this file. It keeps the hosted
   copy working offline and nothing more: no push, no background sync, no
   analytics. Every path is relative to this file, so the app works from a
   project subpath (user.github.io/repo/) as well as from a root.

   · index.html, network first. Online, every visit gets the current file,
     so an update arrives on the next visit with no version bump at all; the
     copy in the cache is only what opens when the network is gone.
   · packs/ (index.json and the shards it lists), cache first. They are
     large and change rarely, so they come from the cache once fetched.
   · The manifest and icons, cache first.

   VERSION names the cache. Bump it whenever packs/ or the icons change, and
   on any change to index.html you want to be sure reaches people who only
   ever open the app offline: a changed sw.js is what makes the browser
   install the new worker, which fills a fresh cache and drops the old one.
   The date and a counter is enough, e.g. "pmp-2026-10-19.2". */
"use strict";
var VERSION = "pmp-2026-10-19.1";

var SHELL = [
  "./",
  "index.html",
  "manifest.webmanifest",
  "icons/icon-192.png",
  "icons/icon-512.png",
  "icons/icon-maskable-512.png",
  "icons/apple-touch-icon.png"
];
var PACK_INDEX = "packs/index.json";

/* The shell must all arrive or the install fails and is retried on the next
   visit. The packs are optional: a shard that is not written yet answers
   404 (index.html §05.9), and that must not cost the person the offline
   shell, so each is cached on its own and a failure is ignored. */
self.addEventListener("install", function (event) {
  event.waitUntil(caches.open(VERSION).then(function (cache) {
    return cache.addAll(SHELL).then(function () {
      return fetch(PACK_INDEX).then(function (res) {
        if (!res.ok) return null;
        return cache.put(PACK_INDEX, res.clone()).then(function () { return res.json(); });
      }).then(function (manifest) {
        var shards = (manifest && manifest.shards) || [];
        return Promise.all(shards.map(function (s) {
          var url = "packs/" + String(s && s.file || "");
          return fetch(url).then(function (r) { return r.ok ? cache.put(url, r) : null; }).catch(function () { return null; });
        }));
      }).catch(function () { return null; });
    });
  }).then(function () { return self.skipWaiting(); }));
});

self.addEventListener("activate", function (event) {
  event.waitUntil(caches.keys().then(function (keys) {
    return Promise.all(keys.filter(function (k) { return k.indexOf("pmp-") === 0 && k !== VERSION; })
      .map(function (k) { return caches.delete(k); }));
  }).then(function () { return self.clients.claim(); }));
});

function isPage(req, url) {
  return req.mode === "navigate" || /\/(index\.html)?$/.test(url.pathname);
}

function networkFirst(req) {
  return caches.open(VERSION).then(function (cache) {
    return fetch(req).then(function (res) {
      if (res && res.ok) cache.put("index.html", res.clone());
      return res;
    }).catch(function () {
      return cache.match("index.html").then(function (hit) { return hit || cache.match("./"); })
        .then(function (hit) { return hit || Response.error(); });
    });
  });
}

function cacheFirst(req) {
  return caches.open(VERSION).then(function (cache) {
    return cache.match(req, { ignoreSearch: true }).then(function (hit) {
      if (hit) return hit;
      return fetch(req).then(function (res) {
        if (res && res.ok) cache.put(req, res.clone());
        return res;
      });
    });
  });
}

self.addEventListener("fetch", function (event) {
  var req = event.request;
  if (req.method !== "GET") return;
  var url = new URL(req.url);
  /* Same origin only: cloud sync talks to script.google.com and the AI tab
     to Google's API, and neither belongs in a cache. */
  if (url.origin !== self.location.origin) return;
  event.respondWith(isPage(req, url) ? networkFirst(req) : cacheFirst(req));
});
