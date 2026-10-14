package com.jahangirb.pmpprep;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Answer history, flags, mock results and flashcard state, kept in one JSON
 * file in the app's private storage. Written atomically after every change.
 */
public final class Progress {
    private Progress() {}

    /** Per question: attempts, correct attempts, whether the last one was right, when. */
    public static final class Rec {
        public int attempts, correct;
        public boolean lastRight;
        public long last;
    }

    public static final Map<String, Rec> recs = new HashMap<String, Rec>();
    public static final Set<String> flags = new HashSet<String>();
    public static final Set<String> knownCards = new HashSet<String>();
    public static JSONArray mocks = new JSONArray();
    private static boolean loaded;

    public static synchronized void ensure(Context c) {
        if (loaded) return;
        loaded = true;
        File f = file(c);
        if (!f.exists()) return;
        try {
            fromJson(new JSONObject(Data.readFile(f)));
        } catch (Exception e) {
            // Unreadable file: start clean rather than refuse to open.
        }
    }

    private static void fromJson(JSONObject o) {
        recs.clear(); flags.clear(); knownCards.clear();
        JSONObject q = o.optJSONObject("q");
        if (q != null) {
            Iterator<String> it = q.keys();
            while (it.hasNext()) {
                String id = it.next();
                JSONArray a = q.optJSONArray(id);
                if (a == null) continue;
                Rec r = new Rec();
                r.attempts = a.optInt(0); r.correct = a.optInt(1); r.lastRight = a.optInt(2) == 1; r.last = a.optLong(3);
                recs.put(id, r);
            }
        }
        JSONArray fl = o.optJSONArray("flags");
        if (fl != null) for (int i = 0; i < fl.length(); i++) flags.add(fl.optString(i));
        JSONArray kc = o.optJSONArray("cards");
        if (kc != null) for (int i = 0; i < kc.length(); i++) knownCards.add(kc.optString(i));
        mocks = o.optJSONArray("mocks");
        if (mocks == null) mocks = new JSONArray();
    }

    public static synchronized JSONObject toJson() {
        try {
            JSONObject o = new JSONObject();
            o.put("type", "pmp-native-progress");
            o.put("version", 1);
            JSONObject q = new JSONObject();
            for (Map.Entry<String, Rec> e : recs.entrySet()) {
                Rec r = e.getValue();
                q.put(e.getKey(), new JSONArray().put(r.attempts).put(r.correct).put(r.lastRight ? 1 : 0).put(r.last));
            }
            o.put("q", q);
            o.put("flags", new JSONArray(flags));
            o.put("cards", new JSONArray(knownCards));
            o.put("mocks", mocks);
            return o;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static synchronized void save(Context c) {
        try {
            File f = file(c);
            File tmp = new File(f.getPath() + ".tmp");
            FileOutputStream out = new FileOutputStream(tmp);
            try { out.write(toJson().toString().getBytes(StandardCharsets.UTF_8)); } finally { out.close(); }
            tmp.renameTo(f);
        } catch (Exception e) {
            // Nothing sensible to do mid-drill; the next save will try again.
        }
    }

    public static void record(String id, boolean right) {
        Rec r = recs.get(id);
        if (r == null) { r = new Rec(); recs.put(id, r); }
        r.attempts++;
        if (right) r.correct++;
        r.lastRight = right;
        r.last = System.currentTimeMillis();
    }

    public static boolean toggleFlag(String id) {
        if (flags.remove(id)) return false;
        flags.add(id);
        return true;
    }

    public static void restore(Context c, String text) throws Exception {
        JSONObject o = new JSONObject(text);
        if (!"pmp-native-progress".equals(o.optString("type"))) throw new Exception("This is not a PMP Prep progress backup.");
        synchronized (Progress.class) { fromJson(o); }
        save(c);
    }

    public static void reset(Context c) {
        synchronized (Progress.class) {
            recs.clear(); flags.clear(); knownCards.clear(); mocks = new JSONArray();
        }
        save(c);
    }

    private static File file(Context c) { return new File(c.getFilesDir(), "progress.json"); }
}
