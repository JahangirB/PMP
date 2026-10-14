package com.jahangirb.pmpprep;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;

/** Every mock exam taken, newest first. */
public class HistoryActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Mock exam history");
        Ui.Pal p = Ui.pal(this);
        LinearLayout col = Ui.page(this);
        if (Progress.mocks.length() == 0) {
            col.addView(Ui.muted(this, "No mock exams yet. Start one from the home screen."));
            return;
        }
        java.text.DateFormat df = DateFormat.getMediumDateFormat(this);
        java.text.DateFormat tf = DateFormat.getTimeFormat(this);
        for (int i = Progress.mocks.length() - 1; i >= 0; i--) {
            JSONObject m = Progress.mocks.optJSONObject(i);
            double sc = m.optDouble("score");
            LinearLayout c = Ui.card(this);
            LinearLayout r = Ui.row(this);
            Date d = new Date(m.optLong("t"));
            r.addView(Ui.text(this, df.format(d) + " " + tf.format(d), 15, p.text, true), Ui.weight(1));
            r.addView(Ui.text(this, Ui.pct(sc), 20, sc >= 0.7 ? p.ok : sc >= 0.6 ? p.warn : p.bad, true));
            c.addView(r);
            c.addView(Ui.muted(this, m.optInt("correct") + " of " + m.optInt("n") + " correct · " + Ui.clock(m.optLong("usedMs")) + " used"));
            JSONObject dom = m.optJSONObject("domains");
            if (dom != null) {
                StringBuilder sb = new StringBuilder();
                for (String k : Data.DOMAINS) {
                    JSONArray a = dom.optJSONArray(k);
                    if (a == null || a.optInt(1) == 0) continue;
                    if (sb.length() > 0) sb.append(" · ");
                    sb.append(Data.domainLabel(k).split(" ")[0]).append(" ").append(Ui.pct(a.optInt(0) / (double) a.optInt(1)));
                }
                c.addView(Ui.muted(this, sb.toString()));
            }
            col.addView(c);
        }
    }
}
