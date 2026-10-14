package com.jahangirb.pmpprep;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * All content: the built-in core pack, the packs shipped in assets/packs, and
 * packs the user imports (kept as files in filesDir/packs). An id that is
 * already loaded is skipped, never overwritten, so answer history always
 * points at the question it was recorded against.
 */
public final class Data {
    private Data() {}

    public static final String[] DOMAINS = { "people", "process", "business" };
    public static final double[] DOMAIN_WEIGHTS = { 0.33, 0.41, 0.26 };
    public static final Set<String> TYPES = new HashSet<String>(Arrays.asList(
        "single", "multi", "matching", "pulldown", "hotspot", "graphic", "fillblank"));

    public static String domainLabel(String d) {
        if ("people".equals(d)) return "People";
        if ("process".equals(d)) return "Process";
        if ("business".equals(d)) return "Business Environment";
        return d == null ? "Other" : d;
    }

    public static String typeLabel(String t) {
        if ("single".equals(t)) return "Multiple choice";
        if ("multi".equals(t)) return "Multiple response";
        if ("matching".equals(t)) return "Matching";
        if ("pulldown".equals(t)) return "Drop-down";
        if ("hotspot".equals(t) || "graphic".equals(t)) return "Hotspot";
        if ("fillblank".equals(t)) return "Fill in the blank";
        return t;
    }

    public static final class Task {
        public String id, domain, label, summary;
        public int n;
    }

    public static final class Q {
        public JSONObject j;
        public String id, type, base, domain, ecoTask, approach, stem, explanation, mindset, packId;
        public int difficulty;
        public List<String> tags = new ArrayList<String>();
    }

    public static final class Pack {
        public String id, name, file;
        public boolean builtin;
        public final List<Q> qs = new ArrayList<Q>();
        public int skippedDuplicates, invalid;
        public final List<String> errors = new ArrayList<String>();
    }

    public static volatile boolean loaded;
    public static final List<Task> tasks = new ArrayList<Task>();
    public static final Map<String, Task> taskById = new HashMap<String, Task>();
    public static final List<Pack> packs = new ArrayList<Pack>();
    public static final Map<String, Q> byId = new LinkedHashMap<String, Q>();
    public static JSONArray decks = new JSONArray(), flashcards = new JSONArray(),
        glossary = new JSONArray(), formulas = new JSONArray();

    private static Set<String> disabled = new HashSet<String>();

    /* ── loading ─────────────────────────────────────────────────────────── */

    public static synchronized void ensure(Context c) {
        if (loaded) return;
        try {
            load(c.getApplicationContext());
        } catch (Exception e) {
            throw new RuntimeException("Could not load content: " + e.getMessage(), e);
        }
    }

    private static void load(Context c) throws Exception {
        tasks.clear(); taskById.clear(); packs.clear(); byId.clear();

        JSONObject extras = new JSONObject(readAsset(c, "content/extras.json"));
        JSONArray t = extras.getJSONArray("tasks");
        for (int i = 0; i < t.length(); i++) {
            JSONObject o = t.getJSONObject(i);
            Task k = new Task();
            k.id = o.getString("id"); k.domain = o.getString("domain");
            k.label = o.getString("label"); k.summary = o.optString("summary"); k.n = o.optInt("n");
            tasks.add(k); taskById.put(k.id, k);
        }
        decks = extras.getJSONArray("decks");
        flashcards = extras.getJSONArray("flashcards");
        glossary = extras.getJSONArray("glossary");
        formulas = extras.getJSONArray("formulas");

        addPack(parsePack(readAsset(c, "content/core.json"), "core", "Core pack (built in)", null, true));

        String[] files = c.getAssets().list("packs");
        List<String> names = new ArrayList<String>();
        if (files != null) for (String f : files) if (f.endsWith(".json") && !f.equals("index.json")) names.add(f);
        Collections.sort(names, NATURAL);
        for (String f : names) {
            String text = readAsset(c, "packs/" + f);
            addPack(parsePack(text, "asset:" + f, null, f, true));
        }

        File dir = userDir(c);
        File[] user = dir.listFiles();
        if (user != null) {
            Arrays.sort(user);
            for (File f : user) {
                if (!f.getName().endsWith(".json")) continue;
                try {
                    addPack(parsePack(readFile(f), "user:" + f.getName(), null, f.getName(), false));
                } catch (Exception e) {
                    // A damaged imported file must not stop the app from opening.
                }
            }
        }

        SharedPreferences p = c.getSharedPreferences("packs", Context.MODE_PRIVATE);
        disabled = new HashSet<String>(p.getStringSet("disabled", new HashSet<String>()));
        loaded = true;
    }

    private static void addPack(Pack pk) {
        for (int i = pk.qs.size() - 1; i >= 0; i--) {
            Q q = pk.qs.get(i);
            if (byId.containsKey(q.id)) { pk.qs.remove(i); pk.skippedDuplicates++; }
        }
        for (Q q : pk.qs) byId.put(q.id, q);
        packs.add(pk);
    }

    /* ── pool ────────────────────────────────────────────────────────────── */

    public static boolean isEnabled(Pack p) { return !disabled.contains(p.id); }

    public static void setEnabled(Context c, Pack p, boolean on) {
        if (on) disabled.remove(p.id); else disabled.add(p.id);
        c.getSharedPreferences("packs", Context.MODE_PRIVATE).edit()
            .putStringSet("disabled", new HashSet<String>(disabled)).apply();
    }

    public static List<Q> pool() {
        List<Q> out = new ArrayList<Q>();
        for (Pack p : packs) if (isEnabled(p)) out.addAll(p.qs);
        return out;
    }

    public static Pack packById(String id) {
        for (Pack p : packs) if (p.id.equals(id)) return p;
        return null;
    }

    /* ── parsing and validation ──────────────────────────────────────────── */

    public static Pack parsePack(String text, String id, String fallbackName, String file, boolean builtin)
            throws JSONException {
        String trimmed = text.trim();
        if (trimmed.startsWith("﻿")) trimmed = trimmed.substring(1);
        JSONArray arr;
        String name = fallbackName;
        if (trimmed.startsWith("[")) {
            arr = new JSONArray(trimmed);
        } else {
            JSONObject env = new JSONObject(trimmed);
            String type = env.optString("type", "pmp-pack");
            if (!"pmp-pack".equals(type)) throw new JSONException("This is not a question pack (type is \"" + type + "\").");
            if (env.optInt("schemaVersion", 2) > 2) throw new JSONException("This pack needs a newer version of the app.");
            arr = env.optJSONArray("questions");
            if (arr == null) throw new JSONException("The file has no \"questions\" array.");
            if (env.has("name")) name = env.getString("name");
        }
        Pack pk = new Pack();
        pk.id = id; pk.file = file; pk.builtin = builtin;
        pk.name = name != null ? name : (file != null ? file.replace(".json", "") : id);
        Set<String> seen = new HashSet<String>();
        for (int i = 0; i < arr.length(); i++) {
            JSONObject o = arr.optJSONObject(i);
            String err = o == null ? "not an object" : validate(o);
            if (err == null && !seen.add(o.optString("id"))) err = "id repeated inside this file";
            if (err != null) {
                pk.invalid++;
                if (pk.errors.size() < 20) {
                    String qid = o == null ? "" : o.optString("id", "");
                    pk.errors.add("#" + (i + 1) + (qid.isEmpty() ? "" : " (" + qid + ")") + ": " + err);
                }
                continue;
            }
            pk.qs.add(toQ(o, id));
        }
        return pk;
    }

    private static Q toQ(JSONObject o, String packId) {
        Q q = new Q();
        q.j = o;
        q.id = o.optString("id");
        q.type = o.optString("type");
        q.base = baseType(q.type);
        q.domain = o.optString("domain");
        q.ecoTask = o.optString("ecoTask", "");
        q.approach = o.optString("approach", "");
        q.stem = o.optString("stem");
        q.explanation = o.optString("explanation", "");
        q.mindset = o.optString("mindset", "");
        q.difficulty = o.optInt("difficulty", 3);
        q.packId = packId;
        JSONArray tg = o.optJSONArray("tags");
        if (tg != null) for (int i = 0; i < tg.length(); i++) q.tags.add(tg.optString(i));
        return q;
    }

    public static String baseType(String t) {
        if ("pulldown".equals(t)) return "matching";
        if ("graphic".equals(t)) return "hotspot";
        return t;
    }

    /** Returns null when the item can be shown and scored, else the reason. */
    static String validate(JSONObject o) {
        if (o.optString("id").trim().isEmpty()) return "missing id";
        String type = o.optString("type");
        if (!TYPES.contains(type)) return "unknown type \"" + type + "\"";
        String d = o.optString("domain");
        if (!Arrays.asList(DOMAINS).contains(d)) return "domain must be people, process or business";
        if (o.optString("stem").trim().isEmpty()) return "missing stem";
        String base = baseType(type);
        if (base.equals("single") || base.equals("multi") || base.equals("matching")) {
            JSONArray opts = o.optJSONArray("options");
            if (opts == null || opts.length() < 2) return "needs at least two options";
            int correct = 0;
            for (int i = 0; i < opts.length(); i++) {
                JSONObject op = opts.optJSONObject(i);
                if (op == null || op.optString("id").isEmpty() || op.optString("text").isEmpty()) return "option " + (i + 1) + " needs an id and text";
                if (op.optBoolean("correct")) correct++;
            }
            if (base.equals("single") && correct != 1) return "single needs exactly one correct option";
            if (base.equals("multi") && correct < 1) return "multi needs correct options";
            if (base.equals("matching")) {
                JSONArray pr = o.optJSONArray("prompts");
                if (pr == null || pr.length() == 0) return "matching needs prompts";
                for (int i = 0; i < pr.length(); i++) {
                    String pid = pr.optJSONObject(i) == null ? "" : pr.optJSONObject(i).optString("id");
                    boolean hit = false;
                    for (int k = 0; k < opts.length(); k++) if (pid.equals(opts.optJSONObject(k).optString("match"))) hit = true;
                    if (pid.isEmpty() || !hit) return "prompt " + (i + 1) + " has no matching option";
                }
            }
        } else if (base.equals("hotspot")) {
            JSONArray r = o.optJSONArray("regions");
            if (r == null || r.length() == 0) return "hotspot needs regions";
            int correct = 0;
            for (int i = 0; i < r.length(); i++) {
                JSONObject g = r.optJSONObject(i);
                if (g == null || !g.has("x") || !g.has("y") || !g.has("w") || !g.has("h")) return "region " + (i + 1) + " needs x, y, w and h";
                if (g.optBoolean("correct")) correct++;
            }
            if (correct == 0) return "hotspot needs a correct region";
        } else if (base.equals("fillblank")) {
            JSONArray b = o.optJSONArray("blanks");
            if (b == null || b.length() == 0) return "fillblank needs blanks";
            for (int i = 0; i < b.length(); i++) {
                JSONObject bl = b.optJSONObject(i);
                if (bl == null || bl.optString("id").isEmpty()) return "blank " + (i + 1) + " needs an id";
                if (bl.optBoolean("numeric") ? !bl.has("expect") : (bl.optJSONArray("accept") == null || bl.optJSONArray("accept").length() == 0))
                    return "blank " + (i + 1) + " needs expect (numeric) or accept";
            }
        }
        return null;
    }

    /* ── importing ───────────────────────────────────────────────────────── */

    /** Validates, stores and loads an imported pack. Returns the loaded pack. */
    public static synchronized Pack importPack(Context c, String text, String displayName) throws Exception {
        String base = displayName == null ? "pack" : displayName.replaceAll("\\.json$", "");
        base = base.replaceAll("[^A-Za-z0-9._-]+", "-");
        if (base.isEmpty()) base = "pack";
        String fname = base + "-" + System.currentTimeMillis() + ".json";
        Pack pk = parsePack(text, "user:" + fname, base, fname, false);
        if (pk.qs.isEmpty()) {
            throw new Exception("No usable questions in this file."
                + (pk.errors.isEmpty() ? "" : "\n\n" + join(pk.errors, "\n")));
        }
        int before = pk.qs.size();
        addPack(pk);
        if (pk.qs.isEmpty()) {
            packs.remove(pk);
            throw new Exception("All " + before + " questions in this file are already loaded.");
        }
        FileOutputStream out = new FileOutputStream(new File(userDir(c), fname));
        try { out.write(text.getBytes(StandardCharsets.UTF_8)); } finally { out.close(); }
        return pk;
    }

    public static synchronized void deletePack(Context c, Pack p) {
        if (p.builtin) return;
        new File(userDir(c), p.file).delete();
        packs.remove(p);
        for (Q q : p.qs) byId.remove(q.id);
    }

    private static File userDir(Context c) {
        File d = new File(c.getFilesDir(), "packs");
        if (!d.exists()) d.mkdirs();
        return d;
    }

    /* ── io helpers ──────────────────────────────────────────────────────── */

    static String readAsset(Context c, String path) throws IOException {
        InputStream in = c.getAssets().open(path);
        try { return readAll(in); } finally { in.close(); }
    }

    static String readFile(File f) throws IOException {
        InputStream in = new FileInputStream(f);
        try { return readAll(in); } finally { in.close(); }
    }

    static String readAll(InputStream in) throws IOException {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        byte[] buf = new byte[65536];
        int n;
        while ((n = in.read(buf)) > 0) bo.write(buf, 0, n);
        return new String(bo.toByteArray(), StandardCharsets.UTF_8);
    }

    static String join(List<String> xs, String sep) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < xs.size(); i++) { if (i > 0) sb.append(sep); sb.append(xs.get(i)); }
        return sb.toString();
    }

    /** "people-2" before "people-10". */
    static final java.util.Comparator<String> NATURAL = new java.util.Comparator<String>() {
        @Override public int compare(String a, String b) {
            int i = 0, j = 0;
            while (i < a.length() && j < b.length()) {
                char x = a.charAt(i), y = b.charAt(j);
                if (Character.isDigit(x) && Character.isDigit(y)) {
                    int si = i, sj = j;
                    while (i < a.length() && Character.isDigit(a.charAt(i))) i++;
                    while (j < b.length() && Character.isDigit(b.charAt(j))) j++;
                    long nx = Long.parseLong(a.substring(si, Math.min(i, si + 15)));
                    long ny = Long.parseLong(b.substring(sj, Math.min(j, sj + 15)));
                    if (nx != ny) return nx < ny ? -1 : 1;
                } else {
                    if (x != y) return x - y;
                    i++; j++;
                }
            }
            return (a.length() - i) - (b.length() - j);
        }
    };
}
