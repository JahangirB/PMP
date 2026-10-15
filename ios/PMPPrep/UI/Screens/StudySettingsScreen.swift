import SwiftUI
import UIKit

/** Reading comfort, confidence ratings and rest days. (iPhone apps can't switch Focus on, so there's no focus mode.) */
struct StudySettingsScreen: View {
    @EnvironmentObject private var m: AppModel

    var body: some View {
        let r = m.reading
        ScreenScroll {
            Text("Study settings").font(.nunito(22, .heavy))
            PmpCard {
                SectionLabel("Reading comfort")
                Text("For questions and lessons. The real exam's screen can't be changed this way, so the reading drills still matter.")
                    .font(.nunito(13)).foregroundStyle(Palette.onSurfaceVariant).fixedSize(horizontal: false, vertical: true)
                Text("Text size").font(.nunito(13, .semibold)).padding(.top, 10)
                HStack(spacing: 8) {
                    ForEach(ReadingPrefs.sizes.indices, id: \.self) { i in
                        let size = ReadingPrefs.sizes[i]
                        Chip(label: size.1, selected: r.scale == size.0) { var n = r; n.scale = size.0; m.changeReading(n) }
                    }
                }
                SettingsSwitch(title: "More space between lines", detail: nil, on: r.roomy) { v in var n = r; n.roomy = v; m.changeReading(n) }
                SettingsSwitch(title: "Reading guide", detail: "A soft band on one line of the question; tap a line to move it.", on: r.guide) { v in
                    var n = r; n.guide = v; m.changeReading(n)
                }
                SectionLabel("Preview").padding(.top, 8)
                SettingsStemPreview(text: "A team member keeps missing stand-ups and the sprint goal is at risk. What should the project manager do FIRST?", prefs: r)
            }
            PmpCard {
                SectionLabel("While answering")
                SettingsSwitch(title: "Ask how sure I am",
                               detail: "Sure, Think so or Guess, before each practice answer is checked. A right guess won't count toward mastery; a sure-but-wrong answer goes to the top of the mistake journal. Never asked in mocks.",
                               on: m.askConfidence) { m.changeAskConfidence($0) }
            }
            PmpCard {
                SectionLabel("Streak")
                SettingsSwitch(title: "Rest days",
                               detail: "One missed day a week doesn't break your streak, and the evening streak reminder leaves you alone on it. Readiness still counts real study days.",
                               on: m.store.plan.restDays) { m.setRestDays($0) }
            }
            Spacer().frame(height: 16)
        }
    }
}

private struct SettingsSwitch: View {
    let title: String
    let detail: String?
    let on: Bool
    let onChange: (Bool) -> Void
    var body: some View {
        Toggle(isOn: Binding(get: { on }, set: onChange)) {
            VStack(alignment: .leading, spacing: 2) {
                Text(title).font(.nunito(15, .semibold))
                if let detail {
                    Text(detail).font(.nunito(12)).foregroundStyle(Palette.onSurfaceVariant).fixedSize(horizontal: false, vertical: true)
                }
            }
        }
        .tint(Palette.primary)
        .padding(.vertical, 8)
    }
}

/** A question stem at the chosen size and spacing, with the reading guide if it's on: tap a line to move the band. */
private struct SettingsStemPreview: View {
    let text: String
    let prefs: ReadingPrefs
    @State private var line = 0

    var body: some View {
        let size = prefs.size(16)
        let spacing = prefs.spacing(16)
        let lineHeight = UIFont(name: "NunitoExtraLight-SemiBold", size: size)?.lineHeight ?? size * 1.3
        Text(text)
            .font(.nunito(size, .semibold))
            .lineSpacing(spacing)
            .fixedSize(horizontal: false, vertical: true)
            .frame(maxWidth: .infinity, alignment: .leading)
            .background(alignment: .topLeading) {
                if prefs.guide {
                    GeometryReader { g in
                        let step = lineHeight + spacing
                        let lines = max(1, Int((g.size.height + spacing) / step))
                        RoundedRectangle(cornerRadius: 6)
                            .fill(Palette.primary.opacity(0.14))
                            .frame(width: g.size.width + 8, height: step)
                            .offset(x: -4, y: CGFloat(min(line, lines - 1)) * step - spacing / 2)
                    }
                }
            }
            .contentShape(Rectangle())
            .onTapGesture(coordinateSpace: .local) { p in
                if prefs.guide { line = max(0, Int(p.y / (lineHeight + spacing))) }
            }
            .padding(.vertical, 4)
            .accessibilityLabel(text)
    }
}
