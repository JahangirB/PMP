package com.jahangirb.pmpprep;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Shows one question at a time from Session.current, in one of three states:
 * practice (check each answer), exam (answer, move on, submit at the end) and
 * review (a finished session, everything revealed).
 */
public class QuizActivity extends BaseActivity {
    private Session s;
    private Ui.Pal p;
    private LinearLayout content;
    private ScrollView scroll;
    private TextView counter, timer;
    private Button flagBtn, prevBtn, midBtn, mainBtn;
    private View progressHost;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private long lastTick;
    private View feedbackAnchor;

    private static final String LETTERS = "ABCDEFGHIJKLMNOP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        s = Session.current;
        if (s == null || s.visibleIds().isEmpty()) { finish(); return; }
        p = Ui.pal(this);
        setTitle(s.finished ? "Review" : s.title);

        LinearLayout root = Ui.column(this);
        root.setBackgroundColor(p.bg);

        LinearLayout top = Ui.row(this);
        top.setBackgroundColor(p.surface);
        top.setPadding(Ui.dp(this, 16), Ui.dp(this, 8), Ui.dp(this, 8), Ui.dp(this, 8));
        counter = Ui.text(this, "", 15, p.text, true);
        top.addView(counter, Ui.weight(1));
        timer = Ui.text(this, "", 15, p.text, true);
        timer.setPadding(0, 0, Ui.dp(this, 12), 0);
        top.addView(timer);
        flagBtn = Ui.button(this, "Flag", false, new Runnable() {
            @Override public void run() { toggleFlag(); }
        });
        flagBtn.setMinHeight(Ui.dp(this, 38));
        flagBtn.setMinimumHeight(Ui.dp(this, 38));
        top.addView(flagBtn);
        root.addView(top);
        progressHost = new View(this);
        root.addView(progressHost, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Ui.dp(this, 4)));

        scroll = new ScrollView(this);
        content = Ui.column(this);
        int pad = Ui.dp(this, 16);
        content.setPadding(pad, pad, pad, Ui.dp(this, 24));
        scroll.addView(content);
        root.addView(scroll, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));

        LinearLayout bottom = Ui.row(this);
        bottom.setBackgroundColor(p.surface);
        bottom.setPadding(Ui.dp(this, 10), Ui.dp(this, 8), Ui.dp(this, 10), Ui.dp(this, 8));
        prevBtn = Ui.button(this, "Back", false, new Runnable() { @Override public void run() { go(-1); } });
        midBtn = Ui.button(this, "", false, new Runnable() { @Override public void run() { middle(); } });
        mainBtn = Ui.button(this, "", true, new Runnable() { @Override public void run() { primary(); } });
        bottom.addView(prevBtn, Ui.weight(1));
        bottom.addView(spacer());
        bottom.addView(midBtn, Ui.weight(1));
        bottom.addView(spacer());
        bottom.addView(mainBtn, Ui.weight(1.4f));
        root.addView(bottom);
        setContentView(root);
        render();
    }

    private View spacer() {
        View v = new View(this);
        v.setLayoutParams(new LinearLayout.LayoutParams(Ui.dp(this, 8), 1));
        return v;
    }

    /* ── timer ───────────────────────────────────────────────────────────── */

    private final Runnable tick = new Runnable() {
        @Override public void run() {
            if (s == null || !s.isExam() || s.finished) return;
            long now = System.currentTimeMillis();
            s.remainingMs -= now - lastTick;
            lastTick = now;
            if (s.remainingMs <= 0) {
                s.remainingMs = 0;
                Toast.makeText(QuizActivity.this, "Time is up. Your exam has been submitted.", Toast.LENGTH_LONG).show();
                submit();
                return;
            }
            timer.setText(Ui.clock(s.remainingMs));
            timer.setTextColor(s.remainingMs < 5 * 60_000L ? p.bad : p.text);
            handler.postDelayed(this, 1000 - (now % 1000));
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (s != null && s.isExam() && !s.finished) {
            lastTick = System.currentTimeMillis();
            handler.post(tick);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(tick);
        if (s != null && s.isExam() && !s.finished) {
            long now = System.currentTimeMillis();
            s.remainingMs -= Math.max(0, now - lastTick);
            lastTick = now;
            s.save(this);
        }
    }

    /* ── navigation ──────────────────────────────────────────────────────── */

    private boolean locked(Data.Q q) {
        return s.finished || (!s.isExam() && s.checked.contains(q.id));
    }

    private void go(int step) {
        int n = s.visibleIds().size();
        int to = s.index + step;
        if (to < 0 || to >= n) return;
        s.index = to;
        render();
        scroll.scrollTo(0, 0);
    }

    private void middle() {
        if (s.finished) return;
        if (s.isExam()) grid();
        else go(1); // practice: skip
    }

    private void primary() {
        Data.Q q = s.q(s.index);
        int n = s.visibleIds().size();
        boolean last = s.index == n - 1;
        if (s.finished) {
            if (last) finish(); else go(1);
            return;
        }
        if (s.isExam()) {
            if (last) grid(); else go(1);
            return;
        }
        if (!s.checked.contains(q.id)) {
            if (!Scoring.isAnswered(q, s.answers.get(q.id))) return;
            Scoring.Result r = Scoring.score(q, s.answers.get(q.id));
            s.checked.add(q.id);
            Progress.record(q.id, r.correct);
            Progress.save(this);
            render();
            if (feedbackAnchor != null) {
                final View a = feedbackAnchor;
                scroll.post(new Runnable() { @Override public void run() { scroll.smoothScrollTo(0, Math.max(0, a.getTop() - Ui.dp(QuizActivity.this, 120))); } });
            }
            return;
        }
        if (last) finishPractice(); else go(1);
    }

    private void finishPractice() {
        s.finished = true;
        s.index = 0;
        startActivity(new Intent(this, ResultActivity.class));
        finish();
    }

    private void submit() {
        handler.removeCallbacks(tick);
        int correct = 0;
        int[][] dom = new int[3][2];
        for (String id : s.ids) {
            Data.Q q = Data.byId.get(id);
            if (q == null) continue;
            Object a = s.answers.get(id);
            Scoring.Result r = Scoring.score(q, a);
            if (r.correct) correct++;
            int d = MainActivity.domainIndex(q.domain);
            if (d >= 0) { dom[d][1]++; if (r.correct) dom[d][0]++; }
            if (Scoring.isAnswered(q, a)) Progress.record(id, r.correct);
        }
        try {
            JSONObject m = new JSONObject();
            m.put("t", System.currentTimeMillis());
            m.put("title", s.title);
            m.put("n", s.ids.size());
            m.put("correct", correct);
            m.put("score", s.ids.isEmpty() ? 0 : correct / (double) s.ids.size());
            m.put("usedMs", s.durationMs - Math.max(0, s.remainingMs));
            JSONObject d = new JSONObject();
            for (int i = 0; i < 3; i++) d.put(Data.DOMAINS[i], new JSONArray().put(dom[i][0]).put(dom[i][1]));
            m.put("domains", d);
            Progress.mocks.put(m);
        } catch (JSONException e) { /* not reachable with these values */ }
        Progress.save(this);
        s.finished = true;
        s.index = 0;
        Session.clearSaved(this);
        startActivity(new Intent(this, ResultActivity.class));
        finish();
    }

    private void confirmSubmit() {
        int unanswered = s.ids.size() - s.answeredCount();
        new AlertDialog.Builder(this).setTitle("Submit exam?")
            .setMessage(unanswered > 0 ? unanswered + " questions are unanswered and will be scored as wrong." : "All questions are answered.")
            .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                @Override public void onClick(DialogInterface d, int w) { submit(); }
            }).setNegativeButton("Keep going", null).show();
    }

    private void grid() {
        final AlertDialog[] dlg = new AlertDialog[1];
        GridView g = new GridView(this);
        g.setNumColumns(6);
        g.setVerticalSpacing(Ui.dp(this, 6));
        g.setHorizontalSpacing(Ui.dp(this, 6));
        g.setPadding(Ui.dp(this, 12), Ui.dp(this, 12), Ui.dp(this, 12), Ui.dp(this, 12));
        g.setAdapter(new BaseAdapter() {
            @Override public int getCount() { return s.ids.size(); }
            @Override public Object getItem(int i) { return s.ids.get(i); }
            @Override public long getItemId(int i) { return i; }
            @Override public View getView(final int i, View cv, ViewGroup parent) {
                TextView t = new TextView(QuizActivity.this);
                String id = s.ids.get(i);
                boolean ans = Scoring.isAnswered(Data.byId.get(id), s.answers.get(id));
                boolean mk = s.marked.contains(id);
                t.setText(String.valueOf(i + 1) + (mk ? "•" : ""));
                t.setGravity(Gravity.CENTER);
                t.setTextColor(ans ? p.onPrimary : p.text);
                t.setTypeface(i == s.index ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
                t.setBackground(Ui.round(QuizActivity.this, ans ? p.primary : p.surface2, mk ? p.warn : (i == s.index ? p.text : 0), 8));
                t.setMinHeight(Ui.dp(QuizActivity.this, 40));
                Ui.onClick(t, new Runnable() {
                    @Override public void run() { s.index = i; render(); scroll.scrollTo(0, 0); dlg[0].dismiss(); }
                });
                return t;
            }
        });
        int un = s.ids.size() - s.answeredCount();
        dlg[0] = new AlertDialog.Builder(this)
            .setTitle(s.answeredCount() + " answered · " + un + " left · " + s.marked.size() + " marked")
            .setView(g)
            .setPositiveButton("Submit exam", new DialogInterface.OnClickListener() {
                @Override public void onClick(DialogInterface d, int w) { confirmSubmit(); }
            })
            .setNegativeButton("Close", null).show();
    }

    private void toggleFlag() {
        Data.Q q = s.q(s.index);
        if (s.isExam() && !s.finished) {
            if (!s.marked.remove(q.id)) s.marked.add(q.id);
        } else {
            boolean on = Progress.toggleFlag(q.id);
            Progress.save(this);
            Toast.makeText(this, on ? "Flagged — find it under Flagged on the home screen" : "Flag removed", Toast.LENGTH_SHORT).show();
        }
        updateBars();
    }

    @Override
    public void onBackPressed() {
        if (s == null || s.finished) { super.onBackPressed(); return; }
        if (s.isExam()) {
            new AlertDialog.Builder(this).setTitle("Leave the exam?")
                .setMessage("Your answers and remaining time are saved. Resume from the home screen.")
                .setPositiveButton("Leave", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface d, int w) { QuizActivity.super.onBackPressed(); }
                }).setNegativeButton("Stay", null).show();
            return;
        }
        if (s.checked.isEmpty()) { super.onBackPressed(); return; }
        new AlertDialog.Builder(this).setTitle("End practice?")
            .setPositiveButton("See results", new DialogInterface.OnClickListener() {
                @Override public void onClick(DialogInterface d, int w) { finishPractice(); }
            })
            .setNeutralButton("Leave", new DialogInterface.OnClickListener() {
                @Override public void onClick(DialogInterface d, int w) { QuizActivity.super.onBackPressed(); }
            })
            .setNegativeButton("Stay", null).show();
    }

    /* ── rendering ───────────────────────────────────────────────────────── */

    private void updateBars() {
        Data.Q q = s.q(s.index);
        int n = s.visibleIds().size();
        counter.setText("Question " + (s.index + 1) + " of " + n);
        timer.setVisibility(s.isExam() && !s.finished ? View.VISIBLE : View.GONE);
        if (s.isExam() && !s.finished) timer.setText(Ui.clock(s.remainingMs));
        boolean flagged = s.isExam() && !s.finished ? s.marked.contains(q.id) : Progress.flags.contains(q.id);
        flagBtn.setText(flagged ? (s.isExam() && !s.finished ? "Marked" : "Flagged") : (s.isExam() && !s.finished ? "Mark" : "Flag"));
        flagBtn.setTextColor(flagged ? p.warn : p.text);

        prevBtn.setEnabled(s.index > 0);
        prevBtn.setAlpha(s.index > 0 ? 1f : 0.4f);
        boolean last = s.index == n - 1;
        if (s.finished) {
            midBtn.setVisibility(View.GONE);
            mainBtn.setText(last ? "Done" : "Next");
            mainBtn.setEnabled(true);
        } else if (s.isExam()) {
            midBtn.setVisibility(View.VISIBLE);
            midBtn.setText("All (" + s.answeredCount() + "/" + s.ids.size() + ")");
            mainBtn.setText(last ? "Finish" : "Next");
            mainBtn.setEnabled(true);
        } else {
            boolean checked = s.checked.contains(q.id);
            midBtn.setVisibility(checked || last ? View.INVISIBLE : View.VISIBLE);
            midBtn.setText("Skip");
            if (checked) {
                mainBtn.setText(last ? "See results" : "Next");
                mainBtn.setEnabled(true);
            } else {
                mainBtn.setText("Check answer");
                mainBtn.setEnabled(Scoring.isAnswered(q, s.answers.get(q.id)));
            }
        }
        mainBtn.setAlpha(mainBtn.isEnabled() ? 1f : 0.5f);
        double frac = (s.index + 1) / (double) n;
        progressHost.setBackground(new android.graphics.drawable.ClipDrawable(
            new android.graphics.drawable.ColorDrawable(p.primary), Gravity.START, android.graphics.drawable.ClipDrawable.HORIZONTAL));
        progressHost.getBackground().setLevel((int) (frac * 10000));
    }

    private void render() {
        content.removeAllViews();
        feedbackAnchor = null;
        final Data.Q q = s.q(s.index);
        if (q == null) { finish(); return; }
        boolean lock = locked(q);

        LinearLayout chips = Ui.row(this);
        chips.addView(Ui.chip(this, Data.domainLabel(q.domain).split(" ")[0], Ui.domainColor(this, q.domain)));
        Data.Task t = Data.taskById.get(q.ecoTask);
        if (t != null) chips.addView(Ui.chip(this, t.label, p.muted));
        chips.addView(Ui.chip(this, Data.typeLabel(q.type), p.muted));
        android.widget.HorizontalScrollView hs = new android.widget.HorizontalScrollView(this);
        hs.setHorizontalScrollBarEnabled(false);
        hs.addView(chips);
        content.addView(hs);
        content.addView(Ui.gap(this, 12));

        TextView stem = Ui.text(this, q.stem, 17, p.text, false);
        stem.setTextIsSelectable(true);
        content.addView(stem);
        content.addView(Ui.gap(this, 12));

        if (q.base.equals("single") || q.base.equals("multi")) renderChoices(q, lock);
        else if (q.base.equals("matching")) renderMatching(q, lock);
        else if (q.base.equals("hotspot")) renderHotspot(q, lock);
        else if (q.base.equals("fillblank")) renderBlanks(q, lock);

        if (lock) renderFeedback(q);
        updateBars();
    }

    private void changed() { updateBars(); }

    /* single and multi */
    private void renderChoices(final Data.Q q, final boolean lock) {
        final boolean multi = q.base.equals("multi");
        int nCorrect = 0;
        JSONArray all = q.j.optJSONArray("options");
        for (int i = 0; i < all.length(); i++) if (all.optJSONObject(i).optBoolean("correct")) nCorrect++;
        if (multi) content.addView(hint("Select " + (nCorrect == 2 ? "two" : nCorrect == 3 ? "three" : nCorrect == 4 ? "four" : String.valueOf(nCorrect)) + "."));
        final Set<String> chosen = chosenSet(q);
        List<JSONObject> opts = s.options(q);
        for (int i = 0; i < opts.size(); i++) {
            final JSONObject o = opts.get(i);
            final String id = o.optString("id");
            boolean sel = chosen.contains(id), correct = o.optBoolean("correct");
            LinearLayout box = Ui.column(this);
            LinearLayout row = Ui.row(this);
            row.setGravity(Gravity.TOP);
            TextView badge = Ui.text(this, String.valueOf(LETTERS.charAt(i % LETTERS.length())), 14, sel ? p.onPrimary : p.text, true);
            badge.setGravity(Gravity.CENTER);
            badge.setBackground(Ui.round(this, sel ? p.primary : p.surface2, 0, multi ? 6 : 14));
            row.addView(badge, new LinearLayout.LayoutParams(Ui.dp(this, 28), Ui.dp(this, 28)));
            TextView tx = Ui.body(this, o.optString("text"));
            tx.setPadding(Ui.dp(this, 12), Ui.dp(this, 3), 0, 0);
            row.addView(tx, Ui.weight(1));
            box.addView(row);

            int fill = p.surface, stroke = sel ? p.primary : p.line;
            if (lock) {
                String lead = null;
                int col = p.muted;
                if (correct && sel) { fill = p.okBg; stroke = p.ok; lead = "✓ Correct. "; col = p.ok; }
                else if (correct) { fill = p.okBg; stroke = p.ok; lead = multi ? "✓ Correct — you missed this one. " : "✓ This was the answer. "; col = p.ok; }
                else if (sel) { fill = p.badBg; stroke = p.bad; lead = "✗ Your choice. "; col = p.bad; }
                TextView why = rationale(lead, o.optString("rationale"), col);
                box.addView(why);
            }
            box.setBackground(Ui.round(this, fill, stroke, 12));
            box.setPadding(Ui.dp(this, 12), Ui.dp(this, 12), Ui.dp(this, 12), Ui.dp(this, 12));
            if (!lock) {
                Ui.onClick(box, new Runnable() {
                    @Override public void run() {
                        if (multi) {
                            Set<String> c = chosenSet(q);
                            if (!c.remove(id)) c.add(id);
                            s.answers.put(q.id, new JSONArray(c));
                        } else {
                            s.answers.put(q.id, id);
                        }
                        int y = scroll.getScrollY();
                        render();
                        scroll.scrollTo(0, y);
                    }
                });
            }
            content.addView(box, Ui.margins(this, 0, 0, 0, 10));
        }
    }

    private Set<String> chosenSet(Data.Q q) {
        Set<String> out = new HashSet<String>();
        Object a = s.answers.get(q.id);
        if (a instanceof String) out.add((String) a);
        if (a instanceof JSONArray) for (int i = 0; i < ((JSONArray) a).length(); i++) out.add(((JSONArray) a).optString(i));
        return out;
    }

    private JSONObject mapAnswer(Data.Q q) {
        Object a = s.answers.get(q.id);
        if (a instanceof JSONObject) return (JSONObject) a;
        JSONObject o = new JSONObject();
        s.answers.put(q.id, o);
        return o;
    }

    /* matching and pulldown */
    private void renderMatching(final Data.Q q, boolean lock) {
        content.addView(hint(q.type.equals("pulldown") ? "Complete each sentence." : "Choose a match for each item."));
        final JSONObject ans = mapAnswer(q);
        final List<JSONObject> opts = s.options(q);
        JSONArray prompts = q.j.optJSONArray("prompts");
        for (int i = 0; i < prompts.length(); i++) {
            final JSONObject pr = prompts.optJSONObject(i);
            final String pid = pr.optString("id");
            LinearLayout box = Ui.column(this);
            box.addView(Ui.body(this, (i + 1) + ". " + pr.optString("text")));
            String chosenId = ans.optString(pid, null);
            JSONObject chosen = find(opts, chosenId);
            if (!lock) {
                final TextView pick = Ui.text(this, chosen == null ? "Choose…  ▾" : chosen.optString("text") + "  ▾", 15,
                    chosen == null ? p.primary : p.text, chosen == null);
                pick.setBackground(Ui.round(this, p.surface2, chosen == null ? p.primary : p.line, 10));
                pick.setPadding(Ui.dp(this, 12), Ui.dp(this, 10), Ui.dp(this, 12), Ui.dp(this, 10));
                Ui.onClick(pick, new Runnable() {
                    @Override public void run() {
                        chooser(opts, new Picked() {
                            @Override public void picked(String optId) {
                                try { ans.put(pid, optId); } catch (JSONException e) { return; }
                                int y = scroll.getScrollY();
                                render();
                                scroll.scrollTo(0, y);
                            }
                        });
                    }
                });
                box.addView(pick, Ui.margins(this, 0, 8, 0, 0));
                box.setBackground(Ui.round(this, p.surface, p.line, 12));
            } else {
                String want = Scoring.matchFor(q.j, pid);
                boolean ok = want != null && want.equals(chosenId);
                JSONObject right = find(opts, want);
                TextView yours = Ui.text(this, (ok ? "✓ " : "✗ ") + "Your answer: " + (chosen == null ? "none" : chosen.optString("text")), 15, ok ? p.ok : p.bad, true);
                box.addView(yours, Ui.margins(this, 0, 8, 0, 0));
                if (!ok && right != null) box.addView(Ui.text(this, "Correct: " + right.optString("text"), 15, p.ok, true), Ui.margins(this, 0, 4, 0, 0));
                if (right != null) box.addView(rationale(null, right.optString("rationale"), p.muted));
                box.setBackground(Ui.round(this, ok ? p.okBg : p.badBg, ok ? p.ok : p.bad, 12));
            }
            box.setPadding(Ui.dp(this, 12), Ui.dp(this, 12), Ui.dp(this, 12), Ui.dp(this, 12));
            content.addView(box, Ui.margins(this, 0, 0, 0, 10));
        }
        if (lock) {
            for (JSONObject o : opts) {
                if (!o.isNull("match") && o.has("match")) continue;
                LinearLayout box = Ui.card(this);
                box.addView(Ui.text(this, "Not used: " + o.optString("text"), 15, p.text, true));
                box.addView(rationale(null, o.optString("rationale"), p.muted));
                content.addView(box);
            }
        }
    }

    private interface Picked { void picked(String optId); }

    private void chooser(final List<JSONObject> opts, final Picked cb) {
        final AlertDialog[] d = new AlertDialog[1];
        LinearLayout col = Ui.column(this);
        col.setPadding(Ui.dp(this, 12), Ui.dp(this, 8), Ui.dp(this, 12), Ui.dp(this, 8));
        for (final JSONObject o : opts) {
            TextView t = Ui.body(this, o.optString("text"));
            t.setPadding(Ui.dp(this, 12), Ui.dp(this, 12), Ui.dp(this, 12), Ui.dp(this, 12));
            t.setBackground(Ui.round(this, p.surface2, 0, 10));
            Ui.onClick(t, new Runnable() {
                @Override public void run() { d[0].dismiss(); cb.picked(o.optString("id")); }
            });
            col.addView(t, Ui.margins(this, 0, 0, 0, 8));
        }
        ScrollView sv = new ScrollView(this);
        sv.addView(col);
        d[0] = new AlertDialog.Builder(this).setTitle("Choose").setView(sv).setNegativeButton("Cancel", null).show();
    }

    private static JSONObject find(List<JSONObject> opts, String id) {
        if (id == null) return null;
        for (JSONObject o : opts) if (id.equals(o.optString("id"))) return o;
        return null;
    }

    /* hotspot and graphic */
    private void renderHotspot(final Data.Q q, final boolean lock) {
        final JSONArray regs = q.j.optJSONArray("regions");
        int nCorrect = 0;
        for (int i = 0; i < regs.length(); i++) if (regs.optJSONObject(i).optBoolean("correct")) nCorrect++;
        final boolean multi = nCorrect > 1 || q.j.optBoolean("multi");
        JSONObject g = q.j.optJSONObject("graphic");
        if (g != null && !g.optString("title").isEmpty()) content.addView(hint(g.optString("title")));
        content.addView(hint(multi ? "Tap the " + nCorrect + " correct areas, in the diagram or the list." : "Tap one area, in the diagram or the list."));

        final Set<String> chosen = chosenSet(q);
        HotspotView hv = new HotspotView(this, g, regs);
        hv.setState(chosen, lock);
        if (!lock) hv.setListener(new HotspotView.OnRegion() {
            @Override public void tapped(String id) { toggleRegion(q, id, multi); }
        });
        LinearLayout frame = Ui.column(this);
        frame.setBackground(Ui.round(this, p.surface, p.line, 12));
        frame.setPadding(Ui.dp(this, 6), Ui.dp(this, 6), Ui.dp(this, 6), Ui.dp(this, 6));
        frame.addView(hv, Ui.fill());
        content.addView(frame, Ui.margins(this, 0, 4, 0, 12));

        for (int i = 0; i < regs.length(); i++) {
            JSONObject r = regs.optJSONObject(i);
            final String id = r.optString("id");
            boolean sel = chosen.contains(id), correct = r.optBoolean("correct");
            LinearLayout box = Ui.column(this);
            box.addView(Ui.body(this, r.optInt("n", i + 1) + ". " + r.optString("label")));
            int fill = p.surface, stroke = sel ? p.primary : p.line;
            if (lock) {
                String lead = null; int col = p.muted;
                if (correct && sel) { fill = p.okBg; stroke = p.ok; lead = "✓ Correct. "; col = p.ok; }
                else if (correct) { fill = p.okBg; stroke = p.ok; lead = "✓ This was the answer. "; col = p.ok; }
                else if (sel) { fill = p.badBg; stroke = p.bad; lead = "✗ Your choice. "; col = p.bad; }
                box.addView(rationale(lead, r.optString("rationale"), col));
            } else {
                Ui.onClick(box, new Runnable() { @Override public void run() { toggleRegion(q, id, multi); } });
            }
            box.setBackground(Ui.round(this, fill, stroke, 12));
            box.setPadding(Ui.dp(this, 12), Ui.dp(this, 10), Ui.dp(this, 12), Ui.dp(this, 10));
            content.addView(box, Ui.margins(this, 0, 0, 0, 8));
        }
    }

    private void toggleRegion(Data.Q q, String id, boolean multi) {
        if (multi) {
            Set<String> c = chosenSet(q);
            if (!c.remove(id)) c.add(id);
            s.answers.put(q.id, new JSONArray(c));
        } else {
            s.answers.put(q.id, new JSONArray().put(id));
        }
        int y = scroll.getScrollY();
        render();
        scroll.scrollTo(0, y);
    }

    /* fill in the blank */
    private void renderBlanks(final Data.Q q, boolean lock) {
        final JSONObject ans = mapAnswer(q);
        JSONArray blanks = q.j.optJSONArray("blanks");
        for (int i = 0; i < blanks.length(); i++) {
            final JSONObject b = blanks.optJSONObject(i);
            final String bid = b.optString("id");
            LinearLayout box = Ui.column(this);
            box.addView(Ui.text(this, b.optString("label"), 15, p.text, true));
            LinearLayout row = Ui.row(this);
            String unit = b.optString("unit", "");
            boolean prefix = unit.matches("[$£€]");
            if (prefix) row.addView(Ui.text(this, unit + " ", 17, p.muted, true));
            EditText et = new EditText(this);
            et.setText(ans.optString(bid, ""));
            et.setTextColor(p.text);
            et.setInputType(b.optBoolean("numeric")
                ? InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED
                : InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
            et.setEnabled(!lock);
            et.addTextChangedListener(new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence c, int a, int b2, int d) {}
                @Override public void onTextChanged(CharSequence c, int a, int b2, int d) {}
                @Override public void afterTextChanged(Editable e) {
                    try { ans.put(bid, e.toString()); } catch (JSONException x) { return; }
                    changed();
                }
            });
            row.addView(et, Ui.weight(1));
            if (!prefix && !unit.isEmpty()) row.addView(Ui.text(this, " " + unit, 17, p.muted, true));
            box.addView(row);
            int fill = p.surface, stroke = p.line;
            if (lock) {
                boolean ok = Scoring.blankIsRight(b, ans.optString(bid, ""));
                fill = ok ? p.okBg : p.badBg; stroke = ok ? p.ok : p.bad;
                box.addView(Ui.text(this, (ok ? "✓ Correct. " : "✗ Answer: ") + (ok ? "" : Scoring.blankAnswer(b)), 15, ok ? p.ok : p.bad, true));
                box.addView(rationale(null, b.optString("rationale"), p.muted));
            }
            box.setBackground(Ui.round(this, fill, stroke, 12));
            box.setPadding(Ui.dp(this, 12), Ui.dp(this, 12), Ui.dp(this, 12), Ui.dp(this, 12));
            content.addView(box, Ui.margins(this, 0, 0, 0, 10));
        }
    }

    private void renderFeedback(Data.Q q) {
        Object a = s.answers.get(q.id);
        Scoring.Result r = Scoring.score(q, a);
        boolean answered = Scoring.isAnswered(q, a);
        LinearLayout banner = Ui.card(this);
        int col = r.correct ? p.ok : (r.credit > 0 ? p.warn : p.bad);
        banner.setBackground(Ui.round(this, r.correct ? p.okBg : (r.credit > 0 ? p.warnBg : p.badBg), col, 14));
        banner.addView(Ui.text(this, !answered ? "Not answered" : r.correct ? "Correct" : r.credit > 0 ? "Partly right (" + Ui.pct(r.credit) + ")" : "Not quite", 18, col, true));
        content.addView(banner);
        feedbackAnchor = banner;

        if (!q.explanation.isEmpty()) {
            LinearLayout ex = Ui.card(this);
            ex.addView(Ui.h2(this, "Why"));
            TextView t = Ui.body(this, q.explanation);
            t.setTextIsSelectable(true);
            ex.addView(t);
            if (!q.mindset.isEmpty()) {
                TextView m = Ui.text(this, "Mindset: " + q.mindset, 15, p.primary, false);
                m.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
                m.setPadding(0, Ui.dp(this, 10), 0, 0);
                ex.addView(m);
            }
            content.addView(ex);
        }
        Progress.Rec rec = Progress.recs.get(q.id);
        if (rec != null) content.addView(Ui.muted(this, "You've answered this " + rec.attempts + (rec.attempts == 1 ? " time" : " times") + ", " + rec.correct + " correct. · " + q.id));
    }

    private TextView hint(String s2) {
        TextView t = Ui.text(this, s2, 14, p.muted, true);
        t.setPadding(0, 0, 0, Ui.dp(this, 8));
        return t;
    }

    private TextView rationale(String lead, String text, int color) {
        android.text.SpannableStringBuilder sb = new android.text.SpannableStringBuilder();
        if (lead != null) {
            sb.append(lead);
            sb.setSpan(new android.text.style.ForegroundColorSpan(color), 0, lead.length(), 0);
            sb.setSpan(new android.text.style.StyleSpan(Typeface.BOLD), 0, lead.length(), 0);
        }
        sb.append(text);
        TextView t = Ui.text(this, sb, 14, p.muted, false);
        t.setPadding(0, Ui.dp(this, 8), 0, 0);
        return t;
    }
}
