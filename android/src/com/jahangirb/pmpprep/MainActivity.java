package com.jahangirb.pmpprep;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/** Home: progress at a glance and the way into every part of the app. */
public class MainActivity extends Activity {
    private boolean ready;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Data.loaded) { ready = true; return; }
        LinearLayout box = Ui.column(this);
        box.setGravity(Gravity.CENTER);
        box.setBackgroundColor(Ui.pal(this).bg);
        box.addView(new ProgressBar(this));
        TextView t = Ui.muted(this, "Loading question banks…");
        t.setGravity(Gravity.CENTER);
        box.addView(t);
        setContentView(box);
        final Context app = getApplicationContext();
        new Thread(new Runnable() {
            @Override public void run() {
                String err = null;
                try { Data.ensure(app); Progress.ensure(app); } catch (Exception e) { err = e.getMessage(); }
                final String fe = err;
                runOnUiThread(new Runnable() {
                    @Override public void run() {
                        if (isFinishing()) return;
                        if (fe != null) {
                            new AlertDialog.Builder(MainActivity.this).setTitle("Could not load").setMessage(fe)
                                .setPositiveButton("Close", null).show();
                            return;
                        }
                        ready = true;
                        build();
                    }
                });
            }
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ready) build();
    }

    private void build() {
        Progress.ensure(this);
        Ui.Pal p = Ui.pal(this);
        LinearLayout col = Ui.page(this);
        List<Data.Q> pool = Data.pool();

        int answered = 0, right = 0, missed = 0, flagged = 0;
        int[] dTotal = new int[3], dAns = new int[3], dRight = new int[3];
        for (Data.Q q : pool) {
            int d = domainIndex(q.domain);
            if (d >= 0) dTotal[d]++;
            Progress.Rec r = Progress.recs.get(q.id);
            if (Progress.flags.contains(q.id)) flagged++;
            if (r == null) continue;
            answered++;
            if (d >= 0) dAns[d]++;
            if (r.lastRight) { right++; if (d >= 0) dRight[d]++; } else missed++;
        }

        col.addView(Ui.h1(this, "Your progress"));
        col.addView(Ui.muted(this, pool.size() + " questions in your bank · " + answered + " answered"));
        col.addView(Ui.gap(this, 14));

        if (Session.hasSaved(this)) {
            final Session s = Session.loadSaved(this);
            if (s != null) {
                LinearLayout c = Ui.card(this);
                c.setBackground(Ui.round(this, p.warnBg, p.warn, 14));
                c.addView(Ui.h2(this, "Mock exam in progress"));
                c.addView(Ui.muted(this, s.answeredCount() + " of " + s.ids.size() + " answered · " + Ui.clock(s.remainingMs) + " left"));
                LinearLayout r = Ui.row(this);
                r.setPadding(0, Ui.dp(this, 10), 0, 0);
                r.addView(Ui.button(this, "Resume", true, new Runnable() {
                    @Override public void run() { Session.current = s; startActivity(new Intent(MainActivity.this, QuizActivity.class)); }
                }), Ui.weight(1));
                r.addView(Ui.gap(this, 0), new LinearLayout.LayoutParams(Ui.dp(this, 10), 1));
                r.addView(Ui.button(this, "Discard", false, new Runnable() {
                    @Override public void run() {
                        new AlertDialog.Builder(MainActivity.this).setMessage("Discard the mock exam in progress?")
                            .setPositiveButton("Discard", new DialogInterface.OnClickListener() {
                                @Override public void onClick(DialogInterface d, int w) { Session.clearSaved(MainActivity.this); build(); }
                            }).setNegativeButton("Keep", null).show();
                    }
                }), Ui.weight(1));
                c.addView(r);
                col.addView(c);
            }
        }

        LinearLayout stats = Ui.card(this);
        LinearLayout sr = Ui.row(this);
        sr.addView(stat(answered == 0 ? "–" : Ui.pct(right / (double) answered), "accuracy"), Ui.weight(1));
        sr.addView(stat(String.valueOf(answered), "answered"), Ui.weight(1));
        String lastMock = "–";
        if (Progress.mocks.length() > 0) {
            JSONObject m = Progress.mocks.optJSONObject(Progress.mocks.length() - 1);
            lastMock = Ui.pct(m.optDouble("score"));
        }
        sr.addView(stat(lastMock, "last mock"), Ui.weight(1));
        stats.addView(sr);
        stats.addView(Ui.gap(this, 10));
        for (int i = 0; i < 3; i++) {
            String d = Data.DOMAINS[i];
            LinearLayout dr = Ui.row(this);
            dr.addView(Ui.text(this, Data.domainLabel(d), 14, p.text, true), Ui.weight(1));
            dr.addView(Ui.muted(this, dAns[i] == 0 ? dAns[i] + "/" + dTotal[i] : Ui.pct(dRight[i] / (double) dAns[i]) + " · " + dAns[i] + "/" + dTotal[i]));
            stats.addView(dr);
            stats.addView(Ui.bar(this, dAns[i] == 0 ? 0 : dRight[i] / (double) dAns[i], Ui.domainColor(this, d)));
            stats.addView(Ui.gap(this, 8));
        }
        col.addView(stats);

        col.addView(Ui.button(this, "Practice questions", true, new Runnable() {
            @Override public void run() { startActivity(new Intent(MainActivity.this, PracticeSetupActivity.class)); }
        }), Ui.margins(this, 0, 0, 0, 10));
        col.addView(Ui.button(this, "Mock exam", true, new Runnable() {
            @Override public void run() { mockDialog(); }
        }), Ui.margins(this, 0, 0, 0, 10));

        LinearLayout r1 = Ui.row(this);
        final int fMissed = missed, fFlagged = flagged;
        r1.addView(Ui.button(this, "Mistakes (" + missed + ")", false, new Runnable() {
            @Override public void run() { quick(PracticeSetupActivity.FILTER_MISSED, fMissed, "Review mistakes"); }
        }), Ui.weight(1));
        r1.addView(Ui.gap(this, 0), new LinearLayout.LayoutParams(Ui.dp(this, 10), 1));
        r1.addView(Ui.button(this, "Flagged (" + flagged + ")", false, new Runnable() {
            @Override public void run() { quick(PracticeSetupActivity.FILTER_FLAGGED, fFlagged, "Flagged questions"); }
        }), Ui.weight(1));
        col.addView(r1, Ui.margins(this, 0, 0, 0, 10));

        LinearLayout r2 = Ui.row(this);
        r2.addView(nav("Flashcards", FlashcardsActivity.class), Ui.weight(1));
        r2.addView(Ui.gap(this, 0), new LinearLayout.LayoutParams(Ui.dp(this, 10), 1));
        r2.addView(nav("Glossary", GlossaryActivity.class), Ui.weight(1));
        r2.addView(Ui.gap(this, 0), new LinearLayout.LayoutParams(Ui.dp(this, 10), 1));
        r2.addView(nav("Formulas", FormulasActivity.class), Ui.weight(1));
        col.addView(r2, Ui.margins(this, 0, 0, 0, 10));

        col.addView(nav("Question banks and backup", PacksActivity.class), Ui.margins(this, 0, 0, 0, 10));
        col.addView(nav("Mock exam history", HistoryActivity.class), Ui.margins(this, 0, 0, 0, 10));
    }

    private android.widget.Button nav(String label, final Class<?> cls) {
        return Ui.button(this, label, false, new Runnable() {
            @Override public void run() { startActivity(new Intent(MainActivity.this, cls)); }
        });
    }

    private LinearLayout stat(String big, String small) {
        LinearLayout c = Ui.column(this);
        c.setGravity(Gravity.CENTER_HORIZONTAL);
        TextView b = Ui.text(this, big, 24, Ui.pal(this).text, true);
        b.setGravity(Gravity.CENTER);
        c.addView(b);
        TextView s = Ui.muted(this, small);
        s.setGravity(Gravity.CENTER);
        c.addView(s);
        return c;
    }

    private void quick(String filter, int count, String title) {
        if (count == 0) {
            new AlertDialog.Builder(this).setMessage(PracticeSetupActivity.FILTER_MISSED.equals(filter)
                ? "No mistakes to review yet. Questions you get wrong show up here until you answer them correctly."
                : "No flagged questions. Tap the flag on any question to save it here.")
                .setPositiveButton("OK", null).show();
            return;
        }
        List<Data.Q> qs = PracticeSetupActivity.filter(Data.pool(), null, null, null, null, null, filter);
        Collections.shuffle(qs);
        if (qs.size() > 40) qs = new ArrayList<Data.Q>(qs.subList(0, 40));
        Session.current = Session.create(Session.PRACTICE, title, qs, 0);
        startActivity(new Intent(this, QuizActivity.class));
    }

    private void mockDialog() {
        final String[] labels = {
            "Full exam — 180 questions, 230 minutes",
            "Half exam — 90 questions, 115 minutes",
            "Short — 45 questions, 58 minutes",
            "Sprint — 20 questions, 26 minutes"
        };
        final int[] counts = { 180, 90, 45, 20 };
        final int[] minutes = { 230, 115, 58, 26 };
        new AlertDialog.Builder(this).setTitle("Mock exam")
            .setItems(labels, new DialogInterface.OnClickListener() {
                @Override public void onClick(DialogInterface d, int which) {
                    startMock(counts[which], minutes[which]);
                }
            }).setNegativeButton("Cancel", null).show();
    }

    private void startMock(int n, int minutes) {
        List<Data.Q> qs = buildMock(Data.pool(), n);
        if (qs.isEmpty()) return;
        Session.clearSaved(this);
        Session s = Session.create(Session.EXAM, "Mock exam · " + qs.size() + " questions", qs, minutes * 60_000L);
        Session.current = s;
        s.save(this);
        startActivity(new Intent(this, QuizActivity.class));
    }

    /** Draws a mock weighted to the published domain split, favouring questions not seen yet. */
    static List<Data.Q> buildMock(List<Data.Q> pool, int n) {
        Random rnd = new Random();
        List<List<Data.Q>> byDomain = new ArrayList<List<Data.Q>>();
        for (int i = 0; i < 3; i++) byDomain.add(new ArrayList<Data.Q>());
        for (Data.Q q : pool) { int d = domainIndex(q.domain); if (d >= 0) byDomain.get(d).add(q); }
        List<Data.Q> out = new ArrayList<Data.Q>();
        List<Data.Q> spare = new ArrayList<Data.Q>();
        for (int i = 0; i < 3; i++) {
            List<Data.Q> l = byDomain.get(i);
            Collections.shuffle(l, rnd);
            // Unseen first, then the rest, keeping the shuffle within each group.
            List<Data.Q> ordered = new ArrayList<Data.Q>();
            for (Data.Q q : l) if (!Progress.recs.containsKey(q.id)) ordered.add(q);
            for (Data.Q q : l) if (Progress.recs.containsKey(q.id)) ordered.add(q);
            int want = (int) Math.round(n * Data.DOMAIN_WEIGHTS[i]);
            int take = Math.min(want, ordered.size());
            out.addAll(ordered.subList(0, take));
            spare.addAll(ordered.subList(take, ordered.size()));
        }
        Collections.shuffle(spare, rnd);
        for (Data.Q q : spare) { if (out.size() >= n) break; out.add(q); }
        while (out.size() > n) out.remove(out.size() - 1);
        Collections.shuffle(out, rnd);
        return out;
    }

    static int domainIndex(String d) {
        for (int i = 0; i < 3; i++) if (Data.DOMAINS[i].equals(d)) return i;
        return -1;
    }
}
