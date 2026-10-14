package com.jahangirb.pmpprep;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Choose what to practise: domain, task, bank, type, approach, which questions and how many. */
public class PracticeSetupActivity extends BaseActivity {
    public static final String FILTER_ALL = "all", FILTER_NEW = "new", FILTER_MISSED = "missed", FILTER_FLAGGED = "flagged";

    private Spinner domain, task, pack, type, approach, which, count;
    private final List<String> taskIds = new ArrayList<String>();
    private final List<String> packIds = new ArrayList<String>();
    private TextView matchInfo;
    private Button start;

    private static final String[] TYPE_KEYS = { null, "single", "multi", "matching", "hotspot", "fillblank" };
    private static final String[] TYPE_LABELS = { "Any type", "Multiple choice", "Multiple response", "Matching / drop-down", "Hotspot", "Fill in the blank" };
    private static final String[] APPROACH_KEYS = { null, "predictive", "agile", "hybrid" };
    private static final String[] APPROACH_LABELS = { "Any approach", "Predictive", "Agile", "Hybrid" };
    private static final String[] WHICH_KEYS = { FILTER_ALL, FILTER_NEW, FILTER_MISSED, FILTER_FLAGGED };
    private static final String[] WHICH_LABELS = { "All questions", "Only ones I haven't answered", "Only ones I got wrong last time", "Only flagged" };
    private static final int[] COUNTS = { 10, 20, 40, 60, 100, Integer.MAX_VALUE };
    private static final String[] COUNT_LABELS = { "10 questions", "20 questions", "40 questions", "60 questions", "100 questions", "All matching" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Practice");
        LinearLayout col = Ui.page(this);
        col.addView(Ui.muted(this, "You see whether you were right, and why, after each answer."));
        col.addView(Ui.gap(this, 8));

        domain = spinner(col, "Domain", new String[] { "All domains", "People", "Process", "Business Environment" });
        task = spinner(col, "ECO task", new String[] { "All tasks" });
        List<String> packLabels = new ArrayList<String>();
        packLabels.add("All enabled banks");
        packIds.add(null);
        for (Data.Pack p : Data.packs) {
            if (!Data.isEnabled(p) || p.qs.isEmpty()) continue;
            packLabels.add(p.name + " (" + p.qs.size() + ")");
            packIds.add(p.id);
        }
        pack = spinner(col, "Question bank", packLabels.toArray(new String[0]));
        type = spinner(col, "Question type", TYPE_LABELS);
        approach = spinner(col, "Approach", APPROACH_LABELS);
        which = spinner(col, "Which questions", WHICH_LABELS);
        count = spinner(col, "How many", COUNT_LABELS);
        count.setSelection(1);

        matchInfo = Ui.muted(this, "");
        col.addView(matchInfo, Ui.margins(this, 0, 6, 0, 12));
        start = Ui.button(this, "Start", true, new Runnable() {
            @Override public void run() { start(); }
        });
        col.addView(start);

        domain.setOnItemSelectedListener(new Sel() {
            @Override void changed() { fillTasks(); update(); }
        });
        Sel up = new Sel() { @Override void changed() { update(); } };
        task.setOnItemSelectedListener(up);
        pack.setOnItemSelectedListener(up);
        type.setOnItemSelectedListener(up);
        approach.setOnItemSelectedListener(up);
        which.setOnItemSelectedListener(up);
        count.setOnItemSelectedListener(up);
        fillTasks();
        update();
    }

    private Spinner spinner(LinearLayout col, String label, String[] items) {
        col.addView(Ui.text(this, label, 13, Ui.pal(this).muted, true), Ui.margins(this, 0, 8, 0, 2));
        Spinner s = new Spinner(this);
        s.setAdapter(adapter(items));
        s.setBackground(Ui.round(this, Ui.pal(this).surface, Ui.pal(this).line, 10));
        s.setPadding(Ui.dp(this, 4), Ui.dp(this, 6), Ui.dp(this, 4), Ui.dp(this, 6));
        col.addView(s, Ui.fill());
        return s;
    }

    private ArrayAdapter<String> adapter(String[] items) {
        ArrayAdapter<String> a = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return a;
    }

    private String selDomain() {
        int i = domain.getSelectedItemPosition();
        return i <= 0 ? null : Data.DOMAINS[i - 1];
    }

    private void fillTasks() {
        String d = selDomain();
        taskIds.clear();
        List<String> labels = new ArrayList<String>();
        labels.add("All tasks");
        taskIds.add(null);
        for (Data.Task t : Data.tasks) {
            if (d != null && !d.equals(t.domain)) continue;
            labels.add(Data.domainLabel(t.domain).split(" ")[0] + " " + t.n + " — " + t.label);
            taskIds.add(t.id);
        }
        task.setAdapter(adapter(labels.toArray(new String[0])));
    }

    private List<Data.Q> current() {
        int ti = task.getSelectedItemPosition(), pi = pack.getSelectedItemPosition();
        return filter(Data.pool(), selDomain(),
            ti < 0 || ti >= taskIds.size() ? null : taskIds.get(ti),
            pi < 0 || pi >= packIds.size() ? null : packIds.get(pi),
            TYPE_KEYS[Math.max(0, type.getSelectedItemPosition())],
            APPROACH_KEYS[Math.max(0, approach.getSelectedItemPosition())],
            WHICH_KEYS[Math.max(0, which.getSelectedItemPosition())]);
    }

    private void update() {
        int n = current().size();
        int c = COUNTS[Math.max(0, count.getSelectedItemPosition())];
        matchInfo.setText(n == 0 ? "No questions match these choices." : n + " questions match · you'll get " + Math.min(n, c));
        start.setEnabled(n > 0);
        start.setAlpha(n > 0 ? 1f : 0.5f);
    }

    private void start() {
        List<Data.Q> qs = current();
        Collections.shuffle(qs);
        int c = COUNTS[Math.max(0, count.getSelectedItemPosition())];
        if (qs.size() > c) qs = new ArrayList<Data.Q>(qs.subList(0, c));
        Session.current = Session.create(Session.PRACTICE, "Practice", qs, 0);
        startActivity(new Intent(this, QuizActivity.class));
    }

    static List<Data.Q> filter(List<Data.Q> pool, String domain, String task, String packId, String type,
                               String approach, String which) {
        List<Data.Q> out = new ArrayList<Data.Q>();
        for (Data.Q q : pool) {
            if (domain != null && !domain.equals(q.domain)) continue;
            if (task != null && !task.equals(q.ecoTask)) continue;
            if (packId != null && !packId.equals(q.packId)) continue;
            if (type != null && !type.equals(q.base)) continue;
            if (approach != null && !approach.equals(q.approach)) continue;
            Progress.Rec r = Progress.recs.get(q.id);
            if (FILTER_NEW.equals(which) && r != null) continue;
            if (FILTER_MISSED.equals(which) && (r == null || r.lastRight)) continue;
            if (FILTER_FLAGGED.equals(which) && !Progress.flags.contains(q.id)) continue;
            out.add(q);
        }
        return out;
    }

    private abstract static class Sel implements AdapterView.OnItemSelectedListener {
        abstract void changed();
        @Override public void onItemSelected(AdapterView<?> p, View v, int pos, long id) { changed(); }
        @Override public void onNothingSelected(AdapterView<?> p) {}
    }
}
