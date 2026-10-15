import SwiftUI

private func rankColor(_ r: Rank) -> Color {
    switch r {
    case .best: Palette.success
    case .acceptable: Palette.info
    case .premature: Palette.warning
    case .wrong: Palette.error
    }
}

struct SituationalScreen: View {
    @EnvironmentObject private var m: AppModel
    /** The scenario whose ranking is shown; cleared when you move to another. */
    @State private var revealedId: String? = nil

    var body: some View {
        let list = m.scenarios()
        if list.isEmpty {
            Color.clear
        } else {
            content(list)
        }
    }

    private func select(_ d: Domain?) {
        m.scenarioDomain = d; m.scenarioIndex = 0; m.scenarioPick = nil; revealedId = nil
    }

    private func content(_ list: [Scenario]) -> some View {
        let runs = m.store.scenarioRuns
        let avg: Double? = runs.isEmpty ? nil : runs.reduce(0.0) { $0 + $1.rank.credit } / Double(runs.count)
        let i = min(max(m.scenarioIndex, 0), list.count - 1)
        let sc = list[i]
        let revealed = revealedId == sc.id
        return ScrollViewReader { proxy in
            ScrollView {
                VStack(alignment: .leading, spacing: 12) {
                    Text("Every option is tempting. Pick the one a capable project manager would take, then see how each ranks.")
                        .font(.nunito(13)).foregroundStyle(Palette.onSurfaceVariant)
                        .id("top")
                    FlowLayout(spacing: 6) {
                        Chip(label: "All", selected: m.scenarioDomain == nil) { select(nil) }
                        ForEach(Domain.allCases) { d in
                            Chip(label: d.short, selected: m.scenarioDomain == d) { select(d) }
                        }
                    }
                    HStack(alignment: .firstTextBaseline) {
                        Text("Scenario \(i + 1) of \(list.count)").font(.nunito(15, .bold))
                        Spacer(minLength: 8)
                        if let avg {
                            Text("Your average: \(pct(avg)) over \(runs.count)").font(.nunito(12)).foregroundStyle(Palette.primary)
                        }
                    }
                    ScenarioBlock(scenario: sc, shown: revealed, pick: m.scenarioPick) { m.scenarioPick = $0 }
                    if !revealed {
                        Button {
                            m.submitScenario(sc)
                            revealedId = sc.id
                        } label: {
                            Text("Reveal the ranking").font(.nunito(15, .bold)).frame(maxWidth: .infinity)
                        }
                        .primaryButton()
                        .disabled(m.scenarioPick == nil)
                    }
                    HStack(spacing: 8) {
                        Button {
                            m.scenarioIndex = (i - 1 + list.count) % list.count; m.scenarioPick = nil; revealedId = nil
                        } label: { Text("Previous").frame(maxWidth: .infinity) }
                        .outlinedButton()
                        Button {
                            m.scenarioIndex = (i + 1) % list.count; m.scenarioPick = nil; revealedId = nil
                        } label: { Text("Next scenario").frame(maxWidth: .infinity) }
                        .primaryButton()
                    }
                    Spacer().frame(height: 72)
                }
                .padding(16)
                .frame(maxWidth: 720)
                .frame(maxWidth: .infinity)
            }
            .onChange(of: sc.id) { proxy.scrollTo("top", anchor: .top) }
        }
    }
}

/** A ranked dilemma: used here and for the worked example in each lesson. */
struct ScenarioBlock: View {
    @EnvironmentObject private var m: AppModel
    @Environment(\.reading) private var reading
    let scenario: Scenario
    let shown: Bool
    let pick: String?
    let onPick: (String) -> Void

    init(scenario: Scenario, shown: Bool, pick: String?, onPick: @escaping (String) -> Void) {
        self.scenario = scenario; self.shown = shown; self.pick = pick; self.onPick = onPick
    }

    var body: some View {
        let sc = scenario
        let task = m.repo.task(sc.ecoTask)
        let letters = Array("ABCDEFGH")
        VStack(alignment: .leading, spacing: 12) {
            HStack(spacing: 6) {
                if let d = sc.domain { DomainChip(domain: d) }
                if let t = task { Pill(text: "\(t.code) · \(t.label)", color: Palette.onSurfaceVariant) }
                if let a = sc.approach { Pill(text: a.title, color: Palette.primary) }
            }
            Text(sc.stem).font(.nunito(reading.size(16))).lineSpacing(reading.spacing(16))
                .fixedSize(horizontal: false, vertical: true)
            VStack(alignment: .leading, spacing: 10) {
                ForEach(sc.options.indices, id: \.self) { i in
                    let o = sc.options[i]
                    RankedChoice(letter: String(letters[i % 8]), text: o.text, selected: pick == o.id, correct: o.rank == .best,
                                 revealed: shown) { onPick(o.id) }
                    if shown {
                        PmpCard(container: rankColor(o.rank).opacity(0.08), border: rankColor(o.rank)) {
                            Pill(text: o.rank.title + (pick == o.id ? " · your pick" : ""), color: rankColor(o.rank), filled: true)
                            Text(o.why).font(.nunito(13)).padding(.top, 6).fixedSize(horizontal: false, vertical: true)
                        }
                    }
                }
            }
            if shown {
                if let picked = sc.options.first(where: { $0.id == pick }) {
                    Text("Credit for your pick: \(pct(picked.rank.credit))").font(.nunito(15, .bold)).foregroundStyle(rankColor(picked.rank))
                }
                if !sc.explanation.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty {
                    PmpCard {
                        SectionLabel("Why")
                        Text(sc.explanation).font(.nunito(15)).lineSpacing(4).padding(.top, 6)
                    }
                }
                MindsetCallout(mindset: sc.mindset)
            }
        }
        .frame(maxWidth: .infinity, alignment: .leading)
    }
}

/** One option of a ranked dilemma: tap to pick it; once revealed, the best one turns green and a wrong pick red. */
private struct RankedChoice: View {
    @Environment(\.reading) private var reading
    let letter: String
    let text: String
    let selected: Bool
    let correct: Bool
    let revealed: Bool
    let action: () -> Void

    var body: some View {
        let border: Color = revealed && correct ? Palette.success : (revealed && selected ? Palette.error : (selected ? Palette.primary : Palette.outline.opacity(0.4)))
        let fill: Color = revealed && correct ? Palette.success.opacity(0.10)
            : (revealed && selected ? Palette.error.opacity(0.10) : (selected ? Palette.primaryContainer.opacity(0.45) : Palette.surface))
        let shape = RoundedRectangle(cornerRadius: 12, style: .continuous)
        Button(action: action) {
            HStack(alignment: .top, spacing: 12) {
                Text(letter).font(.nunito(13, .bold))
                    .foregroundStyle(selected ? Palette.onPrimary : Palette.onSurfaceVariant)
                    .frame(width: 28, height: 28)
                    .background(Circle().fill(selected ? Palette.primary : Palette.surfaceVariant))
                Text(text).font(.nunito(reading.size(15))).lineSpacing(reading.spacing(15))
                    .foregroundStyle(Palette.onSurface)
                    .multilineTextAlignment(.leading)
                    .fixedSize(horizontal: false, vertical: true)
                    .frame(maxWidth: .infinity, alignment: .leading)
            }
            .padding(14)
            .background(shape.fill(fill))
            .overlay(shape.strokeBorder(border, lineWidth: selected || (revealed && correct) ? 2 : 1))
            .contentShape(shape)
            .animation(.easeOut(duration: 0.2), value: selected)
            .animation(.easeOut(duration: 0.2), value: revealed)
        }
        .buttonStyle(.plain)
        .allowsHitTesting(!revealed)
        .accessibilityLabel("\(letter). \(text)")
        .accessibilityAddTraits(selected ? .isSelected : [])
    }
}
