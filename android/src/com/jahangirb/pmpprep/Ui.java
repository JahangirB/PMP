package com.jahangirb.pmpprep;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.content.res.ColorStateList;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/** Small view-building helpers so screens can be written in code without XML layouts. */
public final class Ui {
    private Ui() {}

    public static final class Pal {
        public int bg, surface, surface2, line, text, muted, primary, onPrimary, ok, okBg, bad, badBg, warn, warnBg, people, process, business;
    }

    public static boolean dark(Context c) {
        return (c.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
    }

    public static Pal pal(Context c) {
        Pal p = new Pal();
        if (dark(c)) {
            p.bg = 0xFF12161C; p.surface = 0xFF1B212A; p.surface2 = 0xFF242C37; p.line = 0xFF323C4A;
            p.text = 0xFFE7ECF2; p.muted = 0xFF9AA6B5; p.primary = 0xFF8AB4F8; p.onPrimary = 0xFF0B1A33;
            p.ok = 0xFF6FCF97; p.okBg = 0xFF16322A; p.bad = 0xFFF28B82; p.badBg = 0xFF3A1E1E;
            p.warn = 0xFFF2C14E; p.warnBg = 0xFF3A3018;
            p.people = 0xFFB39DFF; p.process = 0xFF6EC6FF; p.business = 0xFFFFB74D;
        } else {
            p.bg = 0xFFF4F6F9; p.surface = 0xFFFFFFFF; p.surface2 = 0xFFEEF2F7; p.line = 0xFFD6DDE6;
            p.text = 0xFF1A2230; p.muted = 0xFF5D6B7C; p.primary = 0xFF1F3A5F; p.onPrimary = 0xFFFFFFFF;
            p.ok = 0xFF1E7B4A; p.okBg = 0xFFE3F4EA; p.bad = 0xFFB3261E; p.badBg = 0xFFFBE7E6;
            p.warn = 0xFF8A5A00; p.warnBg = 0xFFFFF3D6;
            p.people = 0xFF6A4FC8; p.process = 0xFF1C74B8; p.business = 0xFFB86E00;
        }
        return p;
    }

    public static int domainColor(Context c, String d) {
        Pal p = pal(c);
        if ("people".equals(d)) return p.people;
        if ("process".equals(d)) return p.process;
        if ("business".equals(d)) return p.business;
        return p.muted;
    }

    public static int dp(Context c, float v) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, v, c.getResources().getDisplayMetrics()));
    }

    public static GradientDrawable round(Context c, int fill, int stroke, float radiusDp) {
        GradientDrawable g = new GradientDrawable();
        g.setColor(fill);
        g.setCornerRadius(dp(c, radiusDp));
        if (stroke != 0) g.setStroke(dp(c, 1.5f), stroke);
        return g;
    }

    /** Scrollable page; returns the vertical column to add content to. */
    public static LinearLayout page(Activity a) {
        ScrollView sv = new ScrollView(a);
        sv.setFillViewport(true);
        sv.setBackgroundColor(pal(a).bg);
        LinearLayout col = column(a);
        int pad = dp(a, 16);
        col.setPadding(pad, pad, pad, dp(a, 32));
        sv.addView(col);
        a.setContentView(sv);
        return col;
    }

    public static LinearLayout column(Context c) {
        LinearLayout l = new LinearLayout(c);
        l.setOrientation(LinearLayout.VERTICAL);
        return l;
    }

    public static LinearLayout row(Context c) {
        LinearLayout l = new LinearLayout(c);
        l.setOrientation(LinearLayout.HORIZONTAL);
        l.setGravity(Gravity.CENTER_VERTICAL);
        return l;
    }

    public static LinearLayout card(Context c) {
        LinearLayout l = column(c);
        Pal p = pal(c);
        l.setBackground(round(c, p.surface, p.line, 14));
        int pad = dp(c, 14);
        l.setPadding(pad, pad, pad, pad);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.bottomMargin = dp(c, 12);
        l.setLayoutParams(lp);
        return l;
    }

    public static TextView text(Context c, CharSequence s, float sp, int color, boolean bold) {
        TextView t = new TextView(c);
        t.setText(s);
        t.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
        t.setTextColor(color);
        t.setLineSpacing(0, 1.15f);
        if (bold) t.setTypeface(Typeface.DEFAULT_BOLD);
        return t;
    }

    public static TextView h1(Context c, String s) { return text(c, s, 22, pal(c).text, true); }
    public static TextView h2(Context c, String s) {
        TextView t = text(c, s, 17, pal(c).text, true);
        t.setPadding(0, dp(c, 6), 0, dp(c, 6));
        return t;
    }
    public static TextView body(Context c, CharSequence s) { return text(c, s, 16, pal(c).text, false); }
    public static TextView muted(Context c, CharSequence s) { return text(c, s, 14, pal(c).muted, false); }

    public static TextView chip(Context c, String s, int color) {
        TextView t = text(c, s, 12, color, true);
        t.setBackground(round(c, 0x00000000, color, 20));
        t.setPadding(dp(c, 8), dp(c, 2), dp(c, 8), dp(c, 2));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.rightMargin = dp(c, 6);
        t.setLayoutParams(lp);
        return t;
    }

    public static Button button(Context c, String label, boolean primary, final Runnable onClick) {
        Button b = new Button(c);
        b.setText(label);
        b.setAllCaps(false);
        b.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        Pal p = pal(c);
        int fill = primary ? p.primary : p.surface2;
        b.setTextColor(primary ? p.onPrimary : p.text);
        b.setBackground(new RippleDrawable(ColorStateList.valueOf(0x33888888), round(c, fill, 0, 12), null));
        b.setStateListAnimator(null);
        b.setMinHeight(dp(c, 48));
        b.setPadding(dp(c, 14), 0, dp(c, 14), 0);
        if (onClick != null) b.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { onClick.run(); }
        });
        return b;
    }

    public static void onClick(View v, final Runnable r) {
        v.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View x) { r.run(); }
        });
    }

    public static LinearLayout.LayoutParams fill() {
        return new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public static LinearLayout.LayoutParams weight(float w) {
        return new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, w);
    }

    public static LinearLayout.LayoutParams margins(Context c, int l, int t, int r, int b) {
        LinearLayout.LayoutParams lp = fill();
        lp.setMargins(dp(c, l), dp(c, t), dp(c, r), dp(c, b));
        return lp;
    }

    public static View gap(Context c, int dp) {
        View v = new View(c);
        v.setLayoutParams(new LinearLayout.LayoutParams(1, dp(c, dp)));
        return v;
    }

    /** A horizontal progress bar: fraction 0–1. */
    public static View bar(Context c, double fraction, int color) {
        FrameLayout f = new FrameLayout(c);
        f.setBackground(round(c, pal(c).surface2, 0, 6));
        View fillv = new View(c);
        fillv.setBackground(round(c, color, 0, 6));
        f.addView(fillv);
        final double fr = Math.max(0, Math.min(1, fraction));
        final View fv = fillv;
        f.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override public void onLayoutChange(View v, int l, int t, int r, int b, int ol, int ot, int or, int ob) {
                int w = (int) Math.round((r - l) * fr);
                if (fv.getLayoutParams().width != w) {
                    fv.setLayoutParams(new FrameLayout.LayoutParams(w, ViewGroup.LayoutParams.MATCH_PARENT));
                }
            }
        });
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(c, 10));
        lp.topMargin = dp(c, 4);
        f.setLayoutParams(lp);
        return f;
    }

    public static String pct(double x) { return Math.round(x * 100) + "%"; }

    public static String clock(long ms) {
        long s = Math.max(0, ms / 1000);
        long h = s / 3600, m = (s % 3600) / 60, ss = s % 60;
        return h > 0 ? String.format("%d:%02d:%02d", h, m, ss) : String.format("%d:%02d", m, ss);
    }
}
