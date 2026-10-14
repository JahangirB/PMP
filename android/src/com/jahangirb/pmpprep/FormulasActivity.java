package com.jahangirb.pmpprep;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

/** The formula sheet: each formula, what goes in it, how to read it, and the usual trap. */
public class FormulasActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Formulas");
        Ui.Pal p = Ui.pal(this);
        LinearLayout col = Ui.page(this);
        for (int i = 0; i < Data.formulas.length(); i++) {
            JSONObject g = Data.formulas.optJSONObject(i);
            col.addView(Ui.h2(this, g.optString("label")));
            if (!g.optString("blurb").isEmpty()) {
                col.addView(Ui.muted(this, g.optString("blurb")));
                col.addView(Ui.gap(this, 8));
            }
            JSONArray items = g.optJSONArray("items");
            if (items == null) continue;
            for (int k = 0; k < items.length(); k++) {
                JSONObject f = items.optJSONObject(k);
                LinearLayout c = Ui.card(this);
                LinearLayout r = Ui.row(this);
                r.addView(Ui.text(this, f.optString("name"), 16, p.text, true), Ui.weight(1));
                if (!f.optString("tag").isEmpty()) r.addView(Ui.chip(this, f.optString("tag"), p.primary));
                c.addView(r);
                TextView ex = Ui.text(this, f.optString("expr"), 17, p.primary, true);
                ex.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
                ex.setPadding(0, Ui.dp(this, 8), 0, Ui.dp(this, 6));
                ex.setTextIsSelectable(true);
                c.addView(ex);
                if (!f.optString("where").isEmpty()) c.addView(Ui.muted(this, f.optString("where")));
                if (!f.optString("reads").isEmpty()) {
                    TextView rd = Ui.body(this, f.optString("reads"));
                    rd.setPadding(0, Ui.dp(this, 6), 0, 0);
                    c.addView(rd);
                }
                if (!f.optString("trap").isEmpty()) {
                    TextView t = Ui.text(this, "Trap: " + f.optString("trap"), 14, p.warn, false);
                    t.setBackground(Ui.round(this, p.warnBg, 0, 8));
                    t.setPadding(Ui.dp(this, 10), Ui.dp(this, 8), Ui.dp(this, 10), Ui.dp(this, 8));
                    c.addView(t, Ui.margins(this, 0, 8, 0, 0));
                }
                col.addView(c);
            }
        }
    }
}
