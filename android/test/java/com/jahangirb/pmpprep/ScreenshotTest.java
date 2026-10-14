package com.jahangirb.pmpprep;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;
import android.widget.ScrollView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.GraphicsMode;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/** Renders key screens to PNG (target/screens) for a visual check. Not an assertion test. */
@RunWith(RobolectricTestRunner.class)
@Config(sdk = 34, qualifiers = "w411dp-h891dp-xxhdpi")
@GraphicsMode(GraphicsMode.Mode.NATIVE)
public class ScreenshotTest {

    static void shot(Activity a, String name) throws Exception {
        View root = a.getWindow().getDecorView();
        int w = root.getWidth() > 0 ? root.getWidth() : 1233;
        // Measure full content height so long screens are captured whole.
        View content = a.findViewById(android.R.id.content);
        View inner = content;
        if (content instanceof android.view.ViewGroup && ((android.view.ViewGroup) content).getChildCount() > 0) inner = ((android.view.ViewGroup) content).getChildAt(0);
        int h = root.getHeight();
        if (inner instanceof ScrollView) {
            View child = ((ScrollView) inner).getChildAt(0);
            child.measure(View.MeasureSpec.makeMeasureSpec(w, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            h = Math.max(h, child.getMeasuredHeight() + 300);
        }
        h = Math.min(h, 6000);
        root.measure(View.MeasureSpec.makeMeasureSpec(w, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(h, View.MeasureSpec.EXACTLY));
        root.layout(0, 0, w, h);
        Bitmap b = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        root.draw(new Canvas(b));
        File dir = new File("target/screens");
        dir.mkdirs();
        FileOutputStream out = new FileOutputStream(new File(dir, name + ".png"));
        b.compress(Bitmap.CompressFormat.PNG, 100, out);
        out.close();
    }

    Data.Q first(String type) {
        for (Data.Q q : Data.pool()) if (q.type.equals(type)) return q;
        return null;
    }

    void quiz(String type, boolean answer, String name) throws Exception {
        Data.Q q = first(type);
        List<Data.Q> l = new ArrayList<Data.Q>();
        l.add(q);
        l.add(first("single"));
        Session.current = Session.create(Session.PRACTICE, "Practice", l, 0);
        if (answer) {
            Object right = AppTest.rightAnswer(q);
            if (right instanceof org.json.JSONArray && ((org.json.JSONArray) right).length() > 1) ((org.json.JSONArray) right).remove(0);
            Session.current.answers.put(q.id, right);
            Session.current.checked.add(q.id);
        }
        shot(Robolectric.buildActivity(QuizActivity.class).setup().get(), name);
    }

    @Test
    public void screens() throws Exception {
        Data.loaded = false;
        Data.ensure(RuntimeEnvironment.getApplication());
        Progress.ensure(RuntimeEnvironment.getApplication());
        int i = 0;
        for (Data.Q q : Data.pool()) { if (i++ % 3 == 0) Progress.record(q.id, i % 2 == 0); if (i > 400) break; }
        shot(Robolectric.buildActivity(MainActivity.class).setup().get(), "01-home");
        shot(Robolectric.buildActivity(PracticeSetupActivity.class).setup().get(), "02-setup");
        quiz("single", false, "03-single");
        quiz("single", true, "04-single-checked");
        quiz("multi", true, "05-multi-checked");
        quiz("matching", false, "06-matching");
        quiz("pulldown", true, "07-pulldown-checked");
        quiz("hotspot", false, "08-hotspot");
        quiz("hotspot", true, "09-hotspot-checked");
        quiz("fillblank", true, "10-fillblank-checked");
        Session.current = Session.create(Session.EXAM, "Mock exam · 180 questions", MainActivity.buildMock(Data.pool(), 180), 230 * 60000L);
        shot(Robolectric.buildActivity(QuizActivity.class).setup().get(), "11-exam");
        shot(Robolectric.buildActivity(PacksActivity.class).setup().get(), "12-packs");
        shot(Robolectric.buildActivity(FlashcardsActivity.class).setup().get(), "13-flashcards");
        shot(Robolectric.buildActivity(FormulasActivity.class).setup().get(), "14-formulas");
    }

    @Test
    @Config(qualifiers = "w411dp-h891dp-night-xxhdpi")
    public void darkScreens() throws Exception {
        Data.ensure(RuntimeEnvironment.getApplication());
        Progress.ensure(RuntimeEnvironment.getApplication());
        quiz("multi", true, "20-dark-multi-checked");
        quiz("hotspot", true, "21-dark-hotspot-checked");
    }
}
