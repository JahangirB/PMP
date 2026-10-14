package com.jahangirb.pmpprep;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Decks of flashcards: tap to turn over, then mark whether you knew it. */
public class FlashcardsActivity extends BaseActivity {
    private List<JSONObject> cards;
    private int index;
    private boolean flipped;
    private String deckLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Flashcards");
        decks();
    }

    @Override
    public void onBackPressed() {
        if (cards != null) { cards = null; setTitle("Flashcards"); decks(); return; }
        super.onBackPressed();
    }

    private void decks() {
        Ui.Pal p = Ui.pal(this);
        LinearLayout col = Ui.page(this);
        col.addView(Ui.muted(this, "Pick a deck. Cards you mark \"Knew it\" are skipped next time unless you include them."));
        col.addView(Ui.gap(this, 10));
        for (int i = 0; i < Data.decks.length(); i++) {
            final JSONObject d = Data.decks.optJSONObject(i);
            final String id = d.optString("id");
            int total = 0, known = 0;
            for (int k = 0; k < Data.flashcards.length(); k++) {
                JSONObject c = Data.flashcards.optJSONObject(k);
                if (!id.equals(c.optString("deck"))) continue;
                total++;
                if (Progress.knownCards.contains(c.optString("id"))) known++;
            }
            LinearLayout card = Ui.card(this);
            card.addView(Ui.text(this, d.optString("label"), 17, p.text, true));
            card.addView(Ui.muted(this, d.optString("summary")));
            card.addView(Ui.muted(this, known + " of " + total + " known"));
            card.addView(Ui.bar(this, total == 0 ? 0 : known / (double) total, p.primary));
            final int fTotal = total, fKnown = known;
            Ui.onClick(card, new Runnable() {
                @Override public void run() { start(id, d.optString("label"), fKnown < fTotal); }
            });
            col.addView(card);
        }
    }

    private void start(String deck, String label, boolean skipKnown) {
        cards = new ArrayList<JSONObject>();
        for (int k = 0; k < Data.flashcards.length(); k++) {
            JSONObject c = Data.flashcards.optJSONObject(k);
            if (!deck.equals(c.optString("deck"))) continue;
            if (skipKnown && Progress.knownCards.contains(c.optString("id"))) continue;
            cards.add(c);
        }
        Collections.shuffle(cards);
        index = 0; flipped = false; deckLabel = label;
        setTitle(label);
        show();
    }

    private void show() {
        Ui.Pal p = Ui.pal(this);
        LinearLayout col = Ui.page(this);
        if (index >= cards.size()) {
            col.addView(Ui.h1(this, "Deck done"));
            col.addView(Ui.muted(this, "You went through every card in " + deckLabel + "."));
            col.addView(Ui.gap(this, 16));
            col.addView(Ui.button(this, "Back to decks", true, new Runnable() {
                @Override public void run() { onBackPressed(); }
            }));
            return;
        }
        final JSONObject c = cards.get(index);
        col.addView(Ui.muted(this, "Card " + (index + 1) + " of " + cards.size() + (flipped ? "" : " · tap the card to turn it over")));
        col.addView(Ui.gap(this, 8));
        LinearLayout face = Ui.card(this);
        face.setMinimumHeight(Ui.dp(this, 260));
        face.setGravity(Gravity.CENTER);
        TextView front = Ui.text(this, c.optString("front"), flipped ? 17 : 22, p.text, true);
        front.setGravity(Gravity.CENTER);
        face.addView(front);
        if (flipped) {
            face.addView(Ui.gap(this, 12));
            face.addView(Ui.body(this, c.optString("back")));
            if (!c.optString("note").isEmpty()) {
                TextView n = Ui.text(this, c.optString("note"), 14, p.primary, false);
                n.setPadding(0, Ui.dp(this, 12), 0, 0);
                face.addView(n);
            }
        }
        Ui.onClick(face, new Runnable() { @Override public void run() { flipped = !flipped; show(); } });
        col.addView(face);
        if (flipped) {
            LinearLayout r = Ui.row(this);
            r.addView(Ui.button(this, "Again", false, new Runnable() {
                @Override public void run() { mark(c, false); }
            }), Ui.weight(1));
            spacer(r);
            r.addView(Ui.button(this, "Knew it", true, new Runnable() {
                @Override public void run() { mark(c, true); }
            }), Ui.weight(1));
            col.addView(r);
        } else {
            col.addView(Ui.button(this, "Show answer", true, new Runnable() {
                @Override public void run() { flipped = true; show(); }
            }));
        }
    }

    private void spacer(LinearLayout r) {
        r.addView(new android.view.View(this), new LinearLayout.LayoutParams(Ui.dp(this, 10), 1));
    }

    private void mark(JSONObject c, boolean known) {
        if (known) Progress.knownCards.add(c.optString("id"));
        else {
            Progress.knownCards.remove(c.optString("id"));
            // Missed cards come round again at the end of this pass.
            if (cards.size() < 400) cards.add(c);
        }
        Progress.save(this);
        index++; flipped = false;
        show();
    }
}
