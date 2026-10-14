package com.jahangirb.pmpprep;

import static org.junit.Assert.*;

import android.content.Intent;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 34)
public class AppTest {

    @Before
    public void load() {
        Data.loaded = false;
        Data.ensure(RuntimeEnvironment.getApplication());
        Progress.ensure(RuntimeEnvironment.getApplication());
        Progress.reset(RuntimeEnvironment.getApplication());
    }

    static List<View> all(View v, List<View> out) {
        out.add(v);
        if (v instanceof ViewGroup) for (int i = 0; i < ((ViewGroup) v).getChildCount(); i++) all(((ViewGroup) v).getChildAt(i), out);
        return out;
    }

    static List<String> texts(android.app.Activity a) {
        List<String> t = new ArrayList<String>();
        for (View v : all(a.getWindow().getDecorView(), new ArrayList<View>())) if (v instanceof TextView) t.add(((TextView) v).getText().toString());
        return t;
    }

    static Button button(android.app.Activity a, String prefix) {
        for (View v : all(a.getWindow().getDecorView(), new ArrayList<View>()))
            if (v instanceof Button && ((Button) v).getText().toString().startsWith(prefix) && v.getVisibility() == View.VISIBLE) return (Button) v;
        fail("no button " + prefix + " in " + texts(a));
        return null;
    }

    static boolean has(android.app.Activity a, String s) {
        for (String t : texts(a)) if (t.contains(s)) return true;
        return false;
    }

    @Test
    public void loadsEveryPack() {
        assertEquals(2930, Data.pool().size());
        assertEquals(26, Data.tasks.size());
        assertTrue(Data.glossary.length() > 300);
        for (Data.Pack p : Data.packs) assertEquals(p.name + " " + p.errors, 0, p.invalid);
    }

    @Test
    public void homeScreenShowsStats() {
        Data.loaded = true;
        MainActivity a = Robolectric.buildActivity(MainActivity.class).setup().get();
        assertTrue(texts(a).toString(), has(a, "2930 questions in your bank"));
        button(a, "Practice questions").performClick();
        Intent next = Shadows.shadowOf(a).getNextStartedActivity();
        assertEquals(PracticeSetupActivity.class.getName(), next.getComponent().getClassName());
    }

    @Test
    public void practiceSetupFilters() {
        PracticeSetupActivity a = Robolectric.buildActivity(PracticeSetupActivity.class).setup().get();
        assertTrue(texts(a).toString(), has(a, "2930 questions match"));
        assertEquals(40, PracticeSetupActivity.filter(Data.pool(), null, "people-3", "asset:people-3.json", null, null, "all").size());
        button(a, "Start").performClick();
        assertEquals(20, Session.current.ids.size());
    }

    /** Answers one question of every type correctly through the UI model, checks it, and sees the feedback. */
    @Test
    public void everyTypeRendersAndScores() throws Exception {
        String[] types = { "single", "multi", "matching", "pulldown", "hotspot", "graphic", "fillblank" };
        for (String type : types) {
            Data.Q q = null;
            for (Data.Q x : Data.pool()) if (x.type.equals(type)) { q = x; break; }
            if (q == null) { System.out.println("no item of type " + type); continue; }
            List<Data.Q> one = new ArrayList<Data.Q>();
            one.add(q);
            Session.current = Session.create(Session.PRACTICE, "Practice", one, 0);
            ActivityController<QuizActivity> c = Robolectric.buildActivity(QuizActivity.class).setup();
            QuizActivity a = c.get();
            assertTrue(type, has(a, q.stem));
            assertFalse(button(a, "Check answer").isEnabled());
            Session.current.answers.put(q.id, rightAnswer(q));
            // Re-render via a navigation no-op: recreate the activity on the same session.
            c.pause().stop().destroy();
            a = Robolectric.buildActivity(QuizActivity.class).setup().get();
            Button check = button(a, "Check answer");
            assertTrue(type + " should be answered", check.isEnabled());
            check.performClick();
            assertTrue(type + " " + texts(a), has(a, "Correct"));
            assertTrue(Progress.recs.get(q.id).lastRight);
            button(a, "See results").performClick();
        }
    }

    @Test
    public void wrongAnswersAreMarkedWrong() throws Exception {
        Data.Q q = null;
        for (Data.Q x : Data.pool()) if (x.type.equals("multi")) { q = x; break; }
        JSONArray right = (JSONArray) rightAnswer(q);
        JSONArray partial = new JSONArray().put(right.getString(0));
        Scoring.Result r = Scoring.score(q, partial);
        assertFalse(r.correct);
        assertTrue(r.credit > 0 && r.credit < 1);
        assertFalse(Scoring.score(q, new JSONArray()).correct);
    }

    @Test
    public void numericBlanksAcceptCommasAndUnits() throws Exception {
        JSONObject b = new JSONObject("{\"id\":\"x\",\"numeric\":true,\"expect\":720000,\"tol\":1000}");
        assertTrue(Scoring.blankIsRight(b, "£720,000"));
        assertTrue(Scoring.blankIsRight(b, "720500"));
        assertFalse(Scoring.blankIsRight(b, "730000"));
        JSONObject t = new JSONObject("{\"id\":\"y\",\"accept\":[\"schedule variance\"]}");
        assertTrue(Scoring.blankIsRight(t, "Schedule  Variance."));
    }

    @Test
    public void mockExamRunsAndSubmits() throws Exception {
        List<Data.Q> qs = MainActivity.buildMock(Data.pool(), 180);
        assertEquals(180, qs.size());
        int people = 0;
        for (Data.Q q : qs) if (q.domain.equals("people")) people++;
        assertEquals(59, people);
        Session s = Session.create(Session.EXAM, "Mock", qs, 230 * 60_000L);
        Session.current = s;
        for (int i = 0; i < 90; i++) s.answers.put(qs.get(i).id, rightAnswer(qs.get(i)));
        s.save(RuntimeEnvironment.getApplication());
        Session back = Session.loadSaved(RuntimeEnvironment.getApplication());
        assertEquals(90, back.answeredCount());
        Session.current = back;
        QuizActivity a = Robolectric.buildActivity(QuizActivity.class).setup().get();
        assertTrue(has(a, "Question 1 of 180"));
        button(a, "Next").performClick();
        assertTrue(has(a, "Question 2 of 180"));
        a.getClass().getDeclaredMethod("submit").setAccessible(true);
        java.lang.reflect.Method m = QuizActivity.class.getDeclaredMethod("submit");
        m.setAccessible(true);
        m.invoke(a);
        assertEquals(1, Progress.mocks.length());
        assertEquals(90, Progress.mocks.getJSONObject(0).getInt("correct"));
        assertFalse(Session.hasSaved(RuntimeEnvironment.getApplication()));
        ResultActivity r = Robolectric.buildActivity(ResultActivity.class).setup().get();
        assertTrue(texts(r).toString(), has(r, "50%"));
        button(r, "Review the 90").performClick();
        QuizActivity rev = Robolectric.buildActivity(QuizActivity.class).setup().get();
        assertTrue(has(rev, "Question 1 of 90"));
        assertTrue(has(rev, "Not answered"));
    }

    @Test
    public void importsAPackAndSkipsDuplicates() throws Exception {
        android.content.Context c = RuntimeEnvironment.getApplication();
        int before = Data.pool().size();
        JSONObject q = new JSONObject(Data.byId.get("pmp-p3-101").j.toString());
        q.put("id", "my-own-001");
        JSONObject dup = new JSONObject(Data.byId.get("pmp-p3-102").j.toString());
        JSONObject bad = new JSONObject("{\"id\":\"bad\",\"type\":\"single\",\"domain\":\"people\",\"stem\":\"x\",\"options\":[]}");
        JSONObject env = new JSONObject().put("type", "pmp-pack").put("schemaVersion", 2).put("name", "My bank")
            .put("questions", new JSONArray().put(q).put(dup).put(bad));
        Data.Pack p = Data.importPack(c, env.toString(), "mine.json");
        assertEquals(1, p.qs.size());
        assertEquals(1, p.skippedDuplicates);
        assertEquals(1, p.invalid);
        assertEquals(before + 1, Data.pool().size());
        // Survives a reload.
        Data.loaded = false;
        Data.ensure(c);
        assertNotNull(Data.byId.get("my-own-001"));
        Data.Pack again = null;
        for (Data.Pack x : Data.packs) if (!x.builtin) again = x;
        assertEquals("My bank", again.name);
        Data.setEnabled(c, again, false);
        assertEquals(before, Data.pool().size());
        Data.setEnabled(c, again, true);
        Data.deletePack(c, again);
        assertEquals(before, Data.pool().size());
        try {
            Data.importPack(c, "{\"type\":\"pmp-pack\",\"questions\":[]}", "empty.json");
            fail();
        } catch (Exception e) { assertTrue(e.getMessage().contains("No usable")); }
        PacksActivity a = Robolectric.buildActivity(PacksActivity.class).setup().get();
        assertTrue(has(a, "Add a question bank"));
    }

    @Test
    public void otherScreensOpen() {
        assertTrue(has(Robolectric.buildActivity(FlashcardsActivity.class).setup().get(), "Earned value"));
        GlossaryActivity g = Robolectric.buildActivity(GlossaryActivity.class).setup().get();
        EditText search = null;
        for (View v : all(g.getWindow().getDecorView(), new ArrayList<View>())) if (v instanceof EditText) search = (EditText) v;
        assertTrue(search.getHint().toString().contains("364 terms"));
        android.widget.ListView lv = null;
        for (View v : all(g.getWindow().getDecorView(), new ArrayList<View>())) if (v instanceof android.widget.ListView) lv = (android.widget.ListView) v;
        assertEquals(364, lv.getAdapter().getCount());
        search.setText("earned value");
        assertTrue(lv.getAdapter().getCount() > 0 && lv.getAdapter().getCount() < 40);
        assertTrue(has(Robolectric.buildActivity(FormulasActivity.class).setup().get(), "Planned value"));
        assertTrue(has(Robolectric.buildActivity(HistoryActivity.class).setup().get(), "No mock exams"));
    }

    static Object rightAnswer(Data.Q q) throws Exception {
        JSONObject j = q.j;
        switch (q.base) {
            case "single": {
                JSONArray o = j.getJSONArray("options");
                for (int i = 0; i < o.length(); i++) if (o.getJSONObject(i).optBoolean("correct")) return o.getJSONObject(i).getString("id");
                return null;
            }
            case "multi": case "hotspot": {
                JSONArray o = j.getJSONArray(q.base.equals("multi") ? "options" : "regions"), out = new JSONArray();
                for (int i = 0; i < o.length(); i++) if (o.getJSONObject(i).optBoolean("correct")) out.put(o.getJSONObject(i).getString("id"));
                return out;
            }
            case "matching": {
                JSONObject out = new JSONObject();
                JSONArray pr = j.getJSONArray("prompts");
                for (int i = 0; i < pr.length(); i++) out.put(pr.getJSONObject(i).getString("id"), Scoring.matchFor(j, pr.getJSONObject(i).getString("id")));
                return out;
            }
            default: {
                JSONObject out = new JSONObject();
                JSONArray b = j.getJSONArray("blanks");
                for (int i = 0; i < b.length(); i++) {
                    JSONObject bl = b.getJSONObject(i);
                    out.put(bl.getString("id"), bl.optBoolean("numeric") ? String.valueOf(bl.getDouble("expect")) : bl.getJSONArray("accept").getString(0));
                }
                return out;
            }
        }
    }
}
