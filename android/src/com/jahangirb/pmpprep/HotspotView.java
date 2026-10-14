package com.jahangirb.pmpprep;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Draws a hotspot item's regions in its viewBox coordinates, with the
 * left-to-right flow arrows the web app draws, and reports taps by region id.
 */
public class HotspotView extends View {
    public interface OnRegion { void tapped(String id); }

    private final List<JSONObject> regions = new ArrayList<JSONObject>();
    private float vx, vy, vw = 660, vh = 130;
    private boolean flow = true;
    private Set<String> selected = new HashSet<String>();
    private boolean revealed;
    private OnRegion listener;
    private final Paint fill = new Paint(Paint.ANTI_ALIAS_FLAG), stroke = new Paint(Paint.ANTI_ALIAS_FLAG),
        txt = new Paint(Paint.ANTI_ALIAS_FLAG), num = new Paint(Paint.ANTI_ALIAS_FLAG), line = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Ui.Pal pal;

    public HotspotView(Context c, JSONObject graphic, JSONArray regs) {
        super(c);
        pal = Ui.pal(c);
        if (graphic != null) {
            String[] vb = graphic.optString("viewBox", "0 0 660 130").trim().split("[\\s,]+");
            if (vb.length == 4) {
                try {
                    vx = Float.parseFloat(vb[0]); vy = Float.parseFloat(vb[1]);
                    vw = Float.parseFloat(vb[2]); vh = Float.parseFloat(vb[3]);
                } catch (NumberFormatException e) { /* keep defaults */ }
            }
            flow = graphic.optBoolean("flow", true);
        }
        for (int i = 0; i < regs.length(); i++) regions.add(regs.optJSONObject(i));
        stroke.setStyle(Paint.Style.STROKE);
        line.setStyle(Paint.Style.STROKE);
        line.setColor(pal.muted);
        num.setTypeface(Typeface.DEFAULT_BOLD);
        txt.setTextAlign(Paint.Align.CENTER);
        setContentDescription(graphic == null ? "Diagram" : graphic.optString("title", "Diagram"));
    }

    public void setListener(OnRegion l) { listener = l; }

    public void setState(Set<String> sel, boolean reveal) {
        selected = sel;
        revealed = reveal;
        invalidate();
    }

    @Override
    protected void onMeasure(int wSpec, int hSpec) {
        int w = MeasureSpec.getSize(wSpec);
        setMeasuredDimension(w, Math.round(w * vh / vw));
    }

    private float scale() { return getWidth() / vw; }

    @Override
    protected void onDraw(Canvas c) {
        // Geometry is scaled from the viewBox; text is drawn at a fixed readable
        // size rather than scaled, because a 660-unit diagram on a phone would
        // otherwise shrink its labels to a few pixels.
        float s = scale();
        float dp = getResources().getDisplayMetrics().density;
        float sp = getResources().getDisplayMetrics().scaledDensity;

        if (flow) {
            List<JSONObject> ord = new ArrayList<JSONObject>(regions);
            Collections.sort(ord, new Comparator<JSONObject>() {
                @Override public int compare(JSONObject a, JSONObject b) { return Double.compare(a.optDouble("x"), b.optDouble("x")); }
            });
            line.setStrokeWidth(1.5f * dp);
            for (int i = 0; i < ord.size() - 1; i++) {
                JSONObject a = ord.get(i), b = ord.get(i + 1);
                float y = sy(a.optDouble("y") + a.optDouble("h") / 2);
                float x1 = sx(a.optDouble("x") + a.optDouble("w")), x2 = sx(b.optDouble("x"));
                if (x2 - x1 < 6 * s) continue;
                float head = 5 * dp;
                c.drawLine(x1, y, x2 - head, y, line);
                Path p = new Path();
                p.moveTo(x2 - head, y - head * 0.6f); p.lineTo(x2, y); p.lineTo(x2 - head, y + head * 0.6f); p.close();
                fill.setColor(pal.muted);
                c.drawPath(p, fill);
            }
        }

        for (JSONObject r : regions) {
            String id = r.optString("id");
            boolean sel = selected.contains(id), correct = r.optBoolean("correct");
            int f = pal.surface, st = pal.line;
            if (revealed && correct) { f = pal.okBg; st = pal.ok; }
            else if (revealed && sel) { f = pal.badBg; st = pal.bad; }
            else if (sel) { f = pal.surface2; st = pal.primary; }
            RectF rect = new RectF(sx(r.optDouble("x")), sy(r.optDouble("y")),
                sx(r.optDouble("x") + r.optDouble("w")), sy(r.optDouble("y") + r.optDouble("h")));
            float rad = 5 * dp;
            fill.setColor(f);
            // Translucent, because some diagrams nest one region inside another.
            fill.setAlpha(150);
            c.drawRoundRect(rect, rad, rad, fill);
            fill.setAlpha(255);
            stroke.setColor(st);
            stroke.setStrokeWidth((sel || (revealed && correct) ? 2.5f : 1.2f) * dp);
            c.drawRoundRect(rect, rad, rad, stroke);

            num.setColor(st == pal.line ? pal.primary : st);
            num.setTextSize(12 * sp);
            c.drawText(String.valueOf(r.optInt("n", regions.indexOf(r) + 1)), rect.left + 4 * dp, rect.top + 13 * sp, num);

            txt.setColor(pal.text);
            float size = 11 * sp;
            List<String> lines;
            // Shrink a little, down to 8sp, if the label does not fit the box.
            while (true) {
                txt.setTextSize(size);
                lines = wrap(r.optString("label"), txt, rect.width() - 6 * dp);
                float need = lines.size() * size * 1.15f + 14 * sp;
                if (need <= rect.height() || size <= 8 * sp) break;
                size -= 0.5f * sp;
            }
            float lh = size * 1.15f;
            float top = rect.centerY() + 6 * sp - (lines.size() - 1) * lh / 2 + size * 0.35f;
            c.save();
            c.clipRect(rect);
            for (int i = 0; i < lines.size(); i++) c.drawText(lines.get(i), rect.centerX(), top + i * lh, txt);
            c.restore();
        }
    }

    private float sx(double x) { return (float) ((x - vx) * scale()); }
    private float sy(double y) { return (float) ((y - vy) * scale()); }

    private static List<String> wrap(String s, Paint p, float max) {
        List<String> out = new ArrayList<String>();
        String cur = "";
        for (String w : s.split("\\s+")) {
            String t = cur.isEmpty() ? w : cur + " " + w;
            if (p.measureText(t) > max && !cur.isEmpty()) { out.add(cur); cur = w; } else cur = t;
        }
        if (!cur.isEmpty()) out.add(cur);
        return out;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_DOWN) return true;
        if (e.getAction() != MotionEvent.ACTION_UP) return super.onTouchEvent(e);
        float s = scale();
        float x = e.getX() / s + vx, y = e.getY() / s + vy;
        // Smallest region under the finger wins, so a nested region stays reachable.
        JSONObject best = null;
        double bestArea = Double.MAX_VALUE;
        for (JSONObject r : regions) {
            if (x >= r.optDouble("x") && x <= r.optDouble("x") + r.optDouble("w")
                && y >= r.optDouble("y") && y <= r.optDouble("y") + r.optDouble("h")) {
                double a = r.optDouble("w") * r.optDouble("h");
                if (a < bestArea) { bestArea = a; best = r; }
            }
        }
        if (best != null) {
            if (listener != null) listener.tapped(best.optString("id"));
            performClick();
        }
        return true;
    }

    @Override
    public boolean performClick() { return super.performClick(); }
}
