import SwiftUI

/** "1 day", "12 days", "2.5 mo", "1.2 yr". */
private func cardDays(_ n: Int) -> String {
    if n <= 1 { return "1 day" }
    if n < 30 { return "\(n) days" }
    if n < 365 { return String(format: "%.1f mo", Double(n) / 30) }
    return String(format: "%.1f yr", Double(n) / 365)
}

/** The decks, or the card session when one is running. */
struct FlashcardsScreen: View {
    @EnvironmentObject private var m: AppModel

    var body: some View {
        if m.cardQueue != nil { CardSession() } else { DeckList() }
    }
}

private struct DeckList: View {
    @EnvironmentObject private var m: AppModel

    var body: some View {
        let x: Extras = m.repo.extras
        let all = m.deckCounts(nil)
        ScrollView {
            LazyVStack(alignment: .leading, spacing: 10) {
                VStack(alignment: .leading, spacing: 4) {
                    Text("Spaced repetition (SM-2)").font(.nunito(20, .heavy))
                    Text("Cards come back just before you would forget them. Up to \(x.srs.newPerDay) new cards a day; a card forgotten \(x.srs.leechThreshold) times is flagged as a leech.")
                        .font(.nunito(13)).foregroundStyle(Palette.onSurfaceVariant)
                    Button { m.startDeck(nil) } label: {
                        Text("Study all decks · \(all.due) due · \(all.new) new").frame(maxWidth: .infinity).frame(minHeight: 26)
                    }
                    .primaryButton()
                    .disabled(all.due + all.new == 0)
                    .padding(.top, 8)
                }
                .padding(.top, 8)
                ForEach(m.decks()) { d in
                    DeckCard(deck: d, c: m.deckCounts(d.id))
                }
                Spacer().frame(height: 16)
            }
            .padding(.horizontal, 16)
            .frame(maxWidth: 720)
            .frame(maxWidth: .infinity)
        }
    }
}

private struct DeckCard: View {
    @EnvironmentObject private var m: AppModel
    let deck: Deck
    let c: AppModel.DeckCounts

    var body: some View {
        let seen = c.total - c.new
        PmpCard {
            Text(deck.label).font(.nunito(16, .bold))
            Text(deck.summary).font(.nunito(13)).foregroundStyle(Palette.onSurfaceVariant)
            ProgressRow(label: "\(seen) of \(c.total) studied" + (c.leeches > 0 ? " · \(c.leeches) leech" : ""),
                        value: c.total == 0 ? 0 : Double(seen) / Double(c.total), right: "\(c.due) due")
                .padding(.vertical, 8)
            HStack(spacing: 8) {
                Button { m.startDeck(deck.id) } label: { Text("Study").frame(maxWidth: .infinity) }
                    .primaryButton()
                    .disabled(c.due + c.new == 0)
                Button { m.cramDeck(deck.id) } label: { Text("Go through all").frame(maxWidth: .infinity) }
                    .outlinedButton()
            }
        }
    }
}

/** Reviews m.cardQueue. onDone: shown inside a guided-path session, which moves on instead of returning to the decks. */
struct CardSession: View {
    @EnvironmentObject private var m: AppModel
    let onDone: (() -> Void)?

    init(onDone: (() -> Void)? = nil) { self.onDone = onDone }

    var body: some View {
        if let q = m.cardQueue {
            if m.cardIndex >= q.count { finished(q) } else { reviewing(q) }
        }
    }

    private func finished(_ q: [Flashcard]) -> some View {
        ScrollView {
            VStack(spacing: 8) {
                Text("Session done").font(.nunito(24, .bold))
                Text(q.isEmpty ? "Nothing due right now. Come back tomorrow, or go through a whole deck." : "\(q.count) reviews.")
                    .font(.nunito(15)).multilineTextAlignment(.center).foregroundStyle(Palette.onSurfaceVariant)
                if !m.newLeeches.isEmpty {
                    PmpCard {
                        SectionLabel("New leeches")
                        Text("You keep forgetting these. Rewrite them in your own words or study the lesson behind them.").font(.nunito(13))
                        ForEach(m.newLeeches.indices, id: \.self) { i in
                            Text("• \(m.newLeeches[i])").font(.nunito(14))
                        }
                    }
                    .padding(.top, 12)
                }
                SwiftUI.Group {
                    if let onDone {
                        Button("Continue", action: onDone).primaryButton()
                    } else {
                        Button("Back to decks") { m.back() }.primaryButton()
                    }
                }
                .padding(.top, 16)
            }
            .padding(24)
            .frame(maxWidth: 720)
            .frame(maxWidth: .infinity, minHeight: 420)
        }
    }

    private func reviewing(_ q: [Flashcard]) -> some View {
        let card = q[m.cardIndex]
        let state = m.cardState(card.id)
        let preview = m.srs.preview(state, Time.now())
        let deck = m.decks().first { $0.id == card.deck }
        return ScrollView {
            VStack(alignment: .leading, spacing: 12) {
                HStack {
                    Text("\(m.cardIndex + 1) / \(q.count)").font(.nunito(15, .bold))
                    Spacer()
                    Text(deck?.label ?? "").font(.nunito(12)).foregroundStyle(Palette.primary)
                }
                FlipCard(angle: m.cardFlipped ? 180 : 0, card: card)
                    .animation(.easeInOut(duration: 0.35), value: m.cardFlipped)
                    .contentShape(Rectangle())
                    .onTapGesture { m.cardFlipped.toggle() }
                    .accessibilityElement(children: .combine)
                    .accessibilityAddTraits(.isButton)
                    .accessibilityHint(m.cardFlipped ? "Turns the card back to the front" : "Turns the card over")
                    // A new card is a new view: it never shows its answer while turning back.
                    .id(m.cardIndex)
                if !m.cardFlipped {
                    Button { m.cardFlipped = true } label: { Text("Show answer").frame(maxWidth: .infinity).frame(minHeight: 26) }
                        .primaryButton()
                } else {
                    Text("How well did you remember it?").font(.nunito(13)).foregroundStyle(Palette.onSurfaceVariant)
                    HStack(spacing: 6) {
                        ForEach(Grade.allCases, id: \.self) { g in
                            gradeButton(g, preview[g] ?? 1)
                        }
                    }
                    if state.lapses > 0 {
                        Text("Forgotten \(state.lapses) time(s) · ease \(String(format: "%.2f", state.ease))")
                            .font(.nunito(11)).foregroundStyle(Palette.onSurfaceVariant)
                    }
                    if card.deck == ProgressStore.MY_DECK {
                        Button("Delete this card") { m.deleteMyCard(card.id) }.textButton(Palette.error)
                    }
                }
            }
            .padding(16)
            .frame(maxWidth: 720)
            .frame(maxWidth: .infinity)
        }
    }

    private func gradeButton(_ g: Grade, _ interval: Int) -> some View {
        let color: Color
        switch g {
        case .again: color = Palette.error
        case .hard: color = Palette.warning
        case .good: color = Palette.info
        case .easy: color = Palette.success
        }
        let when = g == .again ? "again today" : cardDays(interval)
        return Button { m.grade(g) } label: {
            VStack(spacing: 2) {
                Text(g.title).font(.nunito(13, .bold))
                Text(when).font(.nunito(11)).lineLimit(1).minimumScaleFactor(0.8)
            }
            .foregroundStyle(Palette.onStatus)
            .frame(maxWidth: .infinity, minHeight: 52)
            .padding(.horizontal, 4)
            .background(RoundedRectangle(cornerRadius: 18, style: .continuous).fill(color))
            .shadow(color: color.opacity(0.25), radius: 4, y: 2)
        }
        .buttonStyle(LiftStyle())
        .accessibilityLabel("\(g.title), \(when)")
    }
}

/** The card, turning about its vertical axis. The face switches at the halfway point; the back is mirrored so it reads correctly. */
private struct FlipCard: View, Animatable {
    var angle: Double
    let card: Flashcard

    var animatableData: Double {
        get { angle }
        set { angle = newValue }
    }

    var body: some View {
        let showBack = angle > 90
        VStack(alignment: .leading, spacing: 0) {
            if !showBack {
                Spacer().frame(height: 60)
                Text(card.front).font(.nunito(22, .bold)).multilineTextAlignment(.center)
                    .frame(maxWidth: .infinity)
                Text("Tap to turn over").font(.nunito(12)).foregroundStyle(Palette.onSurfaceVariant)
                    .opacity(0.8).frame(maxWidth: .infinity).padding(.top, 24)
            } else {
                Text(card.front).font(.nunito(15, .bold)).foregroundStyle(Palette.primary)
                Text(card.back).font(.nunito(16)).lineSpacing(5).padding(.top, 10)
                    .fixedSize(horizontal: false, vertical: true)
                if !card.note.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty {
                    Text(card.note).font(.nunito(14)).lineSpacing(4).foregroundStyle(Palette.onSurfaceVariant).padding(.top, 12)
                        .fixedSize(horizontal: false, vertical: true)
                }
            }
        }
        .scaleEffect(x: showBack ? -1 : 1, y: 1)
        .frame(maxWidth: .infinity, minHeight: 244, alignment: .topLeading)
        .padding(.horizontal, 20).padding(.vertical, 18)
        .foregroundStyle(Palette.onSurface)
        .clay()
        .rotation3DEffect(.degrees(angle), axis: (x: 0, y: 1, z: 0), perspective: 0.5)
    }
}
