package com.jahangirb.pmpprep;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Score for a finished practice drill or mock exam, by domain and by task, with a way into review. */
public class ResultActivity extends BaseActivity {
    private Session s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        s = Session.current;
        if (s == null) { finish(); return; }
        setTitle(s.isExam() ? "Exam result" : "Practice result");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (s != null) build();
    }

    private void build() {
        Ui.Pal p = Ui.pal(this);
        LinearLayout col = Ui.page(this);

        List<String> scored = new ArrayList<String>();
        for (String id : s.ids) if (s.isExam() || s.checked.contains(id)) scored.add(id);
        final List<String> wrong = new ArrayList<String>();
        int correct = 0;
        int[][] dom = new int[3][2];
        final Map<String, int[]> byTask = new HashMap<String, int[]>();
        for (String id : scored) {
            Data.Q q = Data.byId.get(id);
            if (q == null) continue;
            Scoring.Result r = Scoring.score(q, s.answers.get(id));
            if (r.correct) correct++; else wrong.add(id);
            int d = MainActivity.domainIndex(q.domain);
            if (d >= 0) { dom[d][1]++; if (r.correct) dom[d][0]++; }
            if (!q.ecoTask.isEmpty()) {
                int[] t = byTask.get(q.ecoTask);
                if (t == null) { t = new int[2]; byTask.put(q.ecoTask, t); }
                t[1]++; if (r.correct) t[0]++;
            }
        }
        double score = scored.isEmpty() ? 0 : correct / (double) scored.size();

        LinearLayout head = Ui.card(this);
        head.setGravity(Gravity.CENTER_HORIZONTAL);
        TextView big = Ui.text(this, Ui.pct(score), 44, score >= 0.7 ? p.ok : score >= 0.6 ? p.warn : p.bad, true);
        big.setGravity(Gravity.CENTER);
        head.addView(big);
        TextView sub = Ui.muted(this, correct + " of " + scored.size() + " correct"
            + (s.isExam() ? " · " + Ui.clock(s.durationMs - Math.max(0, s.remainingMs)) + " used" : ""));
        sub.setGravity(Gravity.CENTER);
        head.addView(sub);
        if (s.isExam()) {
            TextView note = Ui.muted(this, score >= 0.7 ? "Above the 70% many candidates aim for before booking."
                : "Many candidates aim for 70% or more on mocks before booking. Review the questions you missed.");
            note.setGravity(Gravity.CENTER);
            note.setPadding(0, Ui.dp(this, 8), 0, 0);
            head.addView(note);
        }
        col.addView(head);

        LinearLayout dc = Ui.card(this);
        dc.addView(Ui.h2(this, "By domain"));
        for (int i = 0; i < 3; i++) {
            if (dom[i][1] == 0) continue;
            LinearLayout r = Ui.row(this);
            r.addView(Ui.text(this, Data.domainLabel(Data.DOMAINS[i]), 14, p.text, true), Ui.weight(1));
            r.addView(Ui.muted(this, Ui.pct(dom[i][0] / (double) dom[i][1]) + " · " + dom[i][0] + "/" + dom[i][1]));
            dc.addView(r);
            dc.addView(Ui.bar(this, dom[i][0] / (double) dom[i][1], Ui.domainColor(this, Data.DOMAINS[i])));
            dc.addView(Ui.gap(this, 8));
        }
        col.addView(dc);

        List<String> tasks = new ArrayList<String>(byTask.keySet());
        Collections.sort(tasks, new Comparator<String>() {
            @Override public int compare(String a, String b) {
                int[] x = byTask.get(a), y = byTask.get(b);
                return Double.compare(x[0] / (double) x[1], y[0] / (double) y[1]);
            }
        });
        if (tasks.size() > 1) {
            LinearLayout tc = Ui.card(this);
            tc.addView(Ui.h2(this, "Weakest tasks"));
            for (int i = 0; i < Math.min(5, tasks.size()); i++) {
                int[] v = byTask.get(tasks.get(i));
                Data.Task t = Data.taskById.get(tasks.get(i));
                LinearLayout r = Ui.row(this);
                r.addView(Ui.body(this, t == null ? tasks.get(i) : t.label), Ui.weight(1));
                r.addView(Ui.muted(this, v[0] + "/" + v[1]));
                tc.addView(r);
            }
            col.addView(tc);
        }

        col.addView(Ui.button(this, "Review all answers", true, new Runnable() {
            @Override public void run() { review(null); }
        }), Ui.margins(this, 0, 4, 0, 10));
        if (!wrong.isEmpty()) {
            col.addView(Ui.button(this, "Review the " + wrong.size() + " I got wrong", false, new Runnable() {
                @Override public void run() { review(wrong); }
            }), Ui.margins(this, 0, 0, 0, 10));
        }
        col.addView(Ui.button(this, "Home", false, new Runnable() {
            @Override public void run() { finish(); }
        }));
    }

    private void review(List<String> ids) {
        List<String> base = new ArrayList<String>();
        for (String id : s.ids) if (s.isExam() || s.checked.contains(id)) base.add(id);
        s.reviewIds = ids != null ? ids : base;
        if (s.reviewIds.isEmpty()) return;
        s.index = 0;
        startActivity(new Intent(this, QuizActivity.class));
    }
}
