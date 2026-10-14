package com.jahangirb.pmpprep;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.Normalizer;
import java.util.HashSet;
import java.util.Set;

/**
 * Same rules as the web app (§10.04.2): credit 0–1, correct only at full
 * credit. Wrong picks on a multi-response item cancel right ones.
 */
public final class Scoring {
    private Scoring() {}

    public static final class Result {
        public double credit;
        public boolean correct;
    }

    private static Result of(double credit) {
        Result r = new Result();
        r.credit = Math.max(0, Math.min(1, credit));
        r.correct = r.credit >= 0.9999;
        return r;
    }

    public static Result score(Data.Q q, Object value) {
        JSONObject j = q.j;
        if (q.base.equals("single")) {
            String right = null;
            JSONArray o = j.optJSONArray("options");
            for (int i = 0; i < o.length(); i++) if (o.optJSONObject(i).optBoolean("correct")) right = o.optJSONObject(i).optString("id");
            return of(value instanceof String && value.equals(right) ? 1 : 0);
        }
        if (q.base.equals("multi") || q.base.equals("hotspot")) {
            JSONArray pool = j.optJSONArray(q.base.equals("multi") ? "options" : "regions");
            Set<String> want = new HashSet<String>();
            for (int i = 0; i < pool.length(); i++) if (pool.optJSONObject(i).optBoolean("correct")) want.add(pool.optJSONObject(i).optString("id"));
            JSONArray picked = value instanceof JSONArray ? (JSONArray) value : new JSONArray();
            int hits = 0, wrong = 0;
            for (int i = 0; i < picked.length(); i++) { if (want.contains(picked.optString(i))) hits++; else wrong++; }
            if (want.isEmpty()) return of(0);
            if (hits == want.size() && wrong == 0) return of(1);
            return of((hits - wrong) / (double) want.size());
        }
        if (q.base.equals("matching")) {
            JSONObject v = value instanceof JSONObject ? (JSONObject) value : new JSONObject();
            JSONArray prompts = j.optJSONArray("prompts");
            int hit = 0;
            for (int i = 0; i < prompts.length(); i++) {
                String pid = prompts.optJSONObject(i).optString("id");
                String want = matchFor(j, pid);
                if (want != null && want.equals(v.optString(pid, null))) hit++;
            }
            return of(prompts.length() == 0 ? 0 : hit / (double) prompts.length());
        }
        if (q.base.equals("fillblank")) {
            JSONObject v = value instanceof JSONObject ? (JSONObject) value : new JSONObject();
            JSONArray blanks = j.optJSONArray("blanks");
            int hit = 0;
            for (int i = 0; i < blanks.length(); i++) {
                JSONObject b = blanks.optJSONObject(i);
                if (blankIsRight(b, v.optString(b.optString("id"), ""))) hit++;
            }
            return of(blanks.length() == 0 ? 0 : hit / (double) blanks.length());
        }
        return of(0);
    }

    public static boolean isAnswered(Data.Q q, Object value) {
        if (value == null) return false;
        if (q.base.equals("single")) return value instanceof String && !((String) value).isEmpty();
        if (q.base.equals("multi") || q.base.equals("hotspot")) return value instanceof JSONArray && ((JSONArray) value).length() > 0;
        if (!(value instanceof JSONObject)) return false;
        JSONObject v = (JSONObject) value;
        JSONArray list = q.j.optJSONArray(q.base.equals("matching") ? "prompts" : "blanks");
        for (int i = 0; i < list.length(); i++) {
            if (v.optString(list.optJSONObject(i).optString("id"), "").trim().isEmpty()) return false;
        }
        return true;
    }

    /** The option id that answers a matching prompt. */
    public static String matchFor(JSONObject j, String promptId) {
        JSONArray o = j.optJSONArray("options");
        for (int i = 0; i < o.length(); i++) {
            JSONObject op = o.optJSONObject(i);
            if (promptId.equals(op.optString("match", null))) return op.optString("id");
        }
        return null;
    }

    public static boolean blankIsRight(JSONObject b, String raw) {
        if (raw == null || raw.trim().isEmpty()) return false;
        if (b.optBoolean("numeric")) {
            double v = parseNumber(raw);
            if (Double.isNaN(v)) return false;
            return Math.abs(v - b.optDouble("expect")) <= b.optDouble("tol", 0) + 1e-9;
        }
        String got = normalise(raw);
        JSONArray acc = b.optJSONArray("accept");
        if (acc == null) return false;
        for (int i = 0; i < acc.length(); i++) if (normalise(acc.optString(i)).equals(got)) return true;
        return false;
    }

    static double parseNumber(String s) {
        String t = s.trim();
        if (t.isEmpty()) return Double.NaN;
        if (t.startsWith("(") && t.endsWith(")")) t = "-" + t.substring(1, t.length() - 1);
        t = t.replaceAll("[,\\s]", "").replaceAll("[$£€%]", "");
        if (!t.matches("^[-+]?\\d*\\.?\\d+([eE][-+]?\\d+)?$")) return Double.NaN;
        try { return Double.parseDouble(t); } catch (NumberFormatException e) { return Double.NaN; }
    }

    static String normalise(String s) {
        String t = Normalizer.normalize(s == null ? "" : s.toLowerCase(), Normalizer.Form.NFKD)
            .replaceAll("[\\u0300-\\u036f]", "");
        return t.replaceAll("[^a-z0-9 ]+", " ").replaceAll("\\s+", " ").trim();
    }

    /** A readable statement of a blank's answer, for the reveal. */
    public static String blankAnswer(JSONObject b) {
        if (b.optBoolean("numeric")) {
            double e = b.optDouble("expect");
            String n = e == Math.rint(e) && Math.abs(e) < 1e15 ? String.format("%,d", (long) e) : String.valueOf(e);
            String unit = b.optString("unit", "");
            String s = unit.matches("[$£€]") ? unit + n : n + (unit.isEmpty() ? "" : " " + unit);
            double tol = b.optDouble("tol", 0);
            if (tol > 0) s += " (±" + (tol == Math.rint(tol) ? String.format("%,d", (long) tol) : String.valueOf(tol)) + ")";
            return s;
        }
        JSONArray acc = b.optJSONArray("accept");
        return acc == null || acc.length() == 0 ? "" : acc.optString(0);
    }
}
