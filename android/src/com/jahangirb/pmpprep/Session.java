package com.jahangirb.pmpprep;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * One run of questions: a practice drill (feedback after each answer) or a
 * mock exam (timed, scored at the end). A mock in progress is written to disk
 * so it survives the app being closed.
 */
public final class Session {
    public static final String PRACTICE = "practice", EXAM = "exam";

    public static Session current;

    public String mode = PRACTICE, title = "Practice";
    public final List<String> ids = new ArrayList<String>();
    public int index;
    /** Answers by question id: String (single), JSONArray (multi/hotspot), JSONObject (matching/fillblank). */
    public final Map<String, Object> answers = new HashMap<String, Object>();
    /** Displayed option order per question, fixed for the whole session. */
    public final Map<String, List<String>> order = new HashMap<String, List<String>>();
    /** Practice: questions already checked. */
    public final Set<String> checked = new HashSet<String>();
    public final Set<String> marked = new HashSet<String>();
    public long durationMs, remainingMs, startedAt;
    public boolean finished;
    /** Review after finishing: show only these ids (null = all). */
    public List<String> reviewIds;

    public boolean isExam() { return EXAM.equals(mode); }

    public List<String> visibleIds() { return reviewIds != null ? reviewIds : ids; }

    public Data.Q q(int i) { return Data.byId.get(visibleIds().get(i)); }

    public static Session create(String mode, String title, List<Data.Q> qs, long durationMs) {
        Session s = new Session();
        s.mode = mode; s.title = title;
        s.durationMs = durationMs; s.remainingMs = durationMs;
        s.startedAt = System.currentTimeMillis();
        Random rnd = new Random();
        for (Data.Q q : qs) {
            s.ids.add(q.id);
            JSONArray opts = q.j.optJSONArray("options");
            if (opts != null) {
                List<String> free = new ArrayList<String>();
                for (int i = 0; i < opts.length(); i++) if (!opts.optJSONObject(i).optBoolean("fixed")) free.add(opts.optJSONObject(i).optString("id"));
                Collections.shuffle(free, rnd);
                List<String> out = new ArrayList<String>();
                int k = 0;
                for (int i = 0; i < opts.length(); i++) {
                    JSONObject o = opts.optJSONObject(i);
                    out.add(o.optBoolean("fixed") ? o.optString("id") : free.get(k++));
                }
                s.order.put(q.id, out);
            }
        }
        return s;
    }

    /** Options of a question in the session's display order. */
    public List<JSONObject> options(Data.Q q) {
        JSONArray opts = q.j.optJSONArray("options");
        List<JSONObject> out = new ArrayList<JSONObject>();
        if (opts == null) return out;
        Map<String, JSONObject> m = new HashMap<String, JSONObject>();
        for (int i = 0; i < opts.length(); i++) m.put(opts.optJSONObject(i).optString("id"), opts.optJSONObject(i));
        List<String> ord = order.get(q.id);
        if (ord == null) { for (int i = 0; i < opts.length(); i++) out.add(opts.optJSONObject(i)); return out; }
        for (String id : ord) if (m.containsKey(id)) out.add(m.get(id));
        return out;
    }

    public int answeredCount() {
        int n = 0;
        for (String id : ids) { Data.Q q = Data.byId.get(id); if (q != null && Scoring.isAnswered(q, answers.get(id))) n++; }
        return n;
    }

    /* ── persistence (mock exams only) ───────────────────────────────────── */

    private static File file(Context c) { return new File(c.getFilesDir(), "exam.json"); }

    public void save(Context c) {
        if (!isExam() || finished) return;
        try {
            JSONObject o = new JSONObject();
            o.put("mode", mode); o.put("title", title); o.put("index", index);
            o.put("ids", new JSONArray(ids));
            JSONObject a = new JSONObject();
            for (Map.Entry<String, Object> e : answers.entrySet()) a.put(e.getKey(), e.getValue());
            o.put("answers", a);
            JSONObject ord = new JSONObject();
            for (Map.Entry<String, List<String>> e : order.entrySet()) ord.put(e.getKey(), new JSONArray(e.getValue()));
            o.put("order", ord);
            o.put("marked", new JSONArray(marked));
            o.put("duration", durationMs); o.put("remaining", remainingMs); o.put("started", startedAt);
            FileOutputStream out = new FileOutputStream(file(c));
            try { out.write(o.toString().getBytes(StandardCharsets.UTF_8)); } finally { out.close(); }
        } catch (Exception e) {
            // Best effort.
        }
    }

    public static boolean hasSaved(Context c) { return file(c).exists(); }

    public static void clearSaved(Context c) { file(c).delete(); }

    public static Session loadSaved(Context c) {
        try {
            JSONObject o = new JSONObject(Data.readFile(file(c)));
            Session s = new Session();
            s.mode = o.getString("mode"); s.title = o.optString("title", "Mock exam"); s.index = o.optInt("index");
            JSONArray ids = o.getJSONArray("ids");
            for (int i = 0; i < ids.length(); i++) if (Data.byId.containsKey(ids.getString(i))) s.ids.add(ids.getString(i));
            JSONObject a = o.getJSONObject("answers");
            JSONArray names = a.names();
            if (names != null) for (int i = 0; i < names.length(); i++) s.answers.put(names.getString(i), a.get(names.getString(i)));
            JSONObject ord = o.getJSONObject("order");
            names = ord.names();
            if (names != null) for (int i = 0; i < names.length(); i++) {
                JSONArray arr = ord.getJSONArray(names.getString(i));
                List<String> l = new ArrayList<String>();
                for (int k = 0; k < arr.length(); k++) l.add(arr.getString(k));
                s.order.put(names.getString(i), l);
            }
            JSONArray m = o.optJSONArray("marked");
            if (m != null) for (int i = 0; i < m.length(); i++) s.marked.add(m.getString(i));
            s.durationMs = o.getLong("duration"); s.remainingMs = o.getLong("remaining"); s.startedAt = o.optLong("started");
            if (s.ids.isEmpty()) return null;
            s.index = Math.min(s.index, s.ids.size() - 1);
            return s;
        } catch (Exception e) {
            return null;
        }
    }
}
