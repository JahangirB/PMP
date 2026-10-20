"""Helpers for writing theory/figures.json by script (used while authoring)."""
import json
P = "theory/figures.json"
def load(): return json.load(open(P))
def save(d): open(P, "w").write(json.dumps(d, indent=2, ensure_ascii=False) + "\n")
def box(x, y, w, h, t=None, tone="ink", s=12, b=False, fill=True):
    o = {"k": "box", "x": x, "y": y, "w": w, "h": h, "tone": tone, "s": s}
    if t: o["t"] = t
    if b: o["b"] = True
    if not fill: o["fill"] = False
    return o
def line(*p, arrow=False, dash=False, tone=None):
    o = {"k": "line", "p": [round(v, 1) for v in p]}
    if arrow: o["arrow"] = True
    if dash: o["dash"] = True
    if tone: o["tone"] = tone
    return o
def text(x, y, t, a="middle", s=11, b=False, tone=None):
    o = {"k": "text", "x": x, "y": y, "t": t, "a": a, "s": s}
    if b: o["b"] = True
    if tone: o["tone"] = tone
    return o
def dot(x, y, tone="ink", r=4):
    return {"k": "dot", "x": round(x, 1), "y": round(y, 1), "r": r, "tone": tone}
