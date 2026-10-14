package com.jahangirb.pmpprep;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/** Searchable glossary of exam vocabulary. */
public class GlossaryActivity extends BaseActivity {
    private final List<JSONObject> shown = new ArrayList<JSONObject>();
    private BaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Glossary");
        final Ui.Pal p = Ui.pal(this);
        LinearLayout root = Ui.column(this);
        root.setBackgroundColor(p.bg);
        int pad = Ui.dp(this, 12);
        root.setPadding(pad, pad, pad, 0);
        EditText search = new EditText(this);
        search.setHint("Search " + Data.glossary.length() + " terms");
        search.setSingleLine(true);
        search.setTextColor(p.text);
        root.addView(search, Ui.fill());
        ListView list = new ListView(this);
        list.setDivider(null);
        adapter = new BaseAdapter() {
            @Override public int getCount() { return shown.size(); }
            @Override public Object getItem(int i) { return shown.get(i); }
            @Override public long getItemId(int i) { return i; }
            @Override public View getView(int i, View cv, ViewGroup parent) {
                LinearLayout c = Ui.column(GlossaryActivity.this);
                c.setPadding(Ui.dp(GlossaryActivity.this, 4), Ui.dp(GlossaryActivity.this, 10), Ui.dp(GlossaryActivity.this, 4), Ui.dp(GlossaryActivity.this, 10));
                JSONObject g = shown.get(i);
                LinearLayout r = Ui.row(GlossaryActivity.this);
                r.addView(Ui.text(GlossaryActivity.this, g.optString("t"), 16, p.text, true), Ui.weight(1));
                r.addView(Ui.text(GlossaryActivity.this, g.optString("g"), 12, p.muted, false));
                c.addView(r);
                TextView d = Ui.text(GlossaryActivity.this, g.optString("d"), 15, p.muted, false);
                c.addView(d);
                return c;
            }
        };
        list.setAdapter(adapter);
        root.addView(list, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
        setContentView(root);
        search.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int a, int b, int c) {}
            @Override public void onTextChanged(CharSequence s, int a, int b, int c) {}
            @Override public void afterTextChanged(Editable e) { filter(e.toString()); }
        });
        filter("");
    }

    private void filter(String q) {
        String needle = q.trim().toLowerCase();
        shown.clear();
        List<JSONObject> second = new ArrayList<JSONObject>();
        for (int i = 0; i < Data.glossary.length(); i++) {
            JSONObject g = Data.glossary.optJSONObject(i);
            if (needle.isEmpty() || g.optString("t").toLowerCase().contains(needle)) shown.add(g);
            else if (g.optString("d").toLowerCase().contains(needle)) second.add(g);
        }
        shown.addAll(second);
        adapter.notifyDataSetChanged();
    }
}
