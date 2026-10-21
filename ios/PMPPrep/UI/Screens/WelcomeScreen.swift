import SwiftUI

/**
 * Before you start (index.html §10.38, WelcomeScreen.kt): an opening screen that asks the person
 * to read carefully, then one screen per section, moved with Next and Back. On the path it comes
 * before the setup, once; Menu → Before you start opens it again.
 */
struct WelcomeScreen: View {
    @EnvironmentObject private var m: AppModel
    @State private var at = 0

    var body: some View {
        if let w = m.repo.extras.welcome {
            let n = w.sections.count + 1
            let last = at == n - 1
            VStack(spacing: 0) {
                HStack {
                    SectionLabel("Before you start")
                    Spacer()
                    Text("\(at + 1) of \(n)").font(.nunito(12)).foregroundStyle(Palette.onSurfaceVariant).monospacedDigit()
                }
                .padding(.horizontal, 20).padding(.top, 12)
                ScrollViewReader { proxy in
                    ScrollView {
                        VStack(alignment: .leading, spacing: 16) {
                            Color.clear.frame(height: 0).id("top")
                            if at == 0 { opening(w) } else { section(w, at - 1, last) }
                        }
                        .padding(20)
                        .frame(maxWidth: 680)
                        .frame(maxWidth: .infinity)
                    }
                    .onChange(of: at) { proxy.scrollTo("top", anchor: .top) }
                }
                VStack(spacing: 10) {
                    dots(n)
                    SplitButtons(split: at > 0, next: !last ? "Next" : (m.setupDue ? w.button : w.done),
                                 onBack: { if at > 0 { at -= 1 } },
                                 onNext: {
                                     if !last { at += 1; return }
                                     let setup = m.setupDue
                                     m.markWelcomeRead()
                                     at = 0
                                     // From the menu: back where it was opened, or to the path when the setup is still due.
                                     if m.screen == .WELCOME { if setup { m.home() } else { m.back() } }
                                 })
                }
                .padding(.horizontal, 20).padding(.vertical, 12)
                .frame(maxWidth: 680)
            }
        }
    }

    @ViewBuilder private func opening(_ w: Welcome) -> some View {
        HStack(alignment: .top, spacing: 12) {
            Image(systemName: "exclamationmark.triangle").font(.system(size: 22, weight: .semibold)).foregroundStyle(Palette.warning)
            VStack(alignment: .leading, spacing: 4) {
                Text(w.warn).font(.nunito(19, .heavy)).foregroundStyle(Palette.warning).fixedSize(horizontal: false, vertical: true)
                Text(w.warnSub).font(.nunito(14)).fixedSize(horizontal: false, vertical: true)
            }
        }
        .frame(maxWidth: .infinity, alignment: .leading)
        .padding(16)
        .background(RoundedRectangle(cornerRadius: 16).fill(Palette.warning.opacity(0.12)))
        .overlay(RoundedRectangle(cornerRadius: 16).stroke(Palette.warning.opacity(0.6), lineWidth: 2))
        .accessibilityElement(children: .combine)
        Text(w.title).font(.nunito(24, .heavy)).fixedSize(horizontal: false, vertical: true).accessibilityAddTraits(.isHeader)
        Text(w.intro).font(.nunito(16)).lineSpacing(4).fixedSize(horizontal: false, vertical: true)
        VStack(alignment: .leading, spacing: 6) {
            ForEach(Array(w.sections.enumerated()), id: \.offset) { i, s in
                Text("\(i + 1).  \(s.h)").font(.nunito(15, .semibold)).foregroundStyle(Palette.onSurfaceVariant)
            }
        }
    }

    @ViewBuilder private func section(_ w: Welcome, _ i: Int, _ last: Bool) -> some View {
        let s = w.sections[i]
        PmpCard(border: s.main ? Palette.primary : nil) {
            VStack(alignment: .leading, spacing: 10) {
                if s.main { Text("MOST IMPORTANT").font(.nunito(11, .bold)).tracking(1).foregroundStyle(Palette.primary) }
                Text(s.h).font(.nunito(20, .heavy)).fixedSize(horizontal: false, vertical: true).accessibilityAddTraits(.isHeader)
                if !s.p.isEmpty { Text(s.p).font(.nunito(16)).lineSpacing(4).fixedSize(horizontal: false, vertical: true) }
                ForEach(Array(s.items.enumerated()), id: \.offset) { _, item in
                    HStack(alignment: .firstTextBaseline, spacing: 6) {
                        Text("•").font(.nunito(16)).frame(width: 14, alignment: .leading)
                        Text(item).font(.nunito(16)).lineSpacing(3).fixedSize(horizontal: false, vertical: true)
                    }
                }
            }
        }
        if last {
            Text(w.again).font(.nunito(13)).foregroundStyle(Palette.onSurfaceVariant).fixedSize(horizontal: false, vertical: true)
        }
    }

    private func dots(_ n: Int) -> some View {
        HStack(spacing: 6) {
            ForEach(0..<n, id: \.self) { i in
                Capsule().fill(i == at ? Palette.primary : i < at ? Palette.primary.opacity(0.45) : Palette.outline)
                    .frame(width: i == at ? 20 : 8, height: 8)
            }
        }
        .accessibilityHidden(true)
    }
}

/**
 * Next alone, centred, on the opening screen. The first Next tears a piece of clay off its left
 * side, which slides out and becomes Back; Back on the second screen merges it home again
 * (SplitButtons in WelcomeScreen.kt). Between the later screens nothing moves. The piece starts
 * hidden under Next in Next's own colour, so the split reads as one button coming apart.
 */
private struct SplitButtons: View {
    let split: Bool
    let next: String
    let onBack: () -> Void
    let onNext: () -> Void
    @Environment(\.accessibilityReduceMotion) private var reduce

    var body: some View {
        GeometryReader { g in
            let w = g.size.width, gap: CGFloat = 10, backW = w * 0.3
            let nextX = split ? backW + gap : w * 0.14
            let nextW = split ? w - backW - gap : w * 0.72
            let pieceX = split ? 0 : w * 0.14
            let pieceW = split ? backW : w * 0.22
            ZStack(alignment: .topLeading) {
                Button(action: onBack) {
                    Text("Back").font(.nunito(16, .bold)).lineLimit(1)
                        .opacity(split ? 1 : 0)
                        .frame(width: pieceW, height: 52)
                        .foregroundStyle(split ? Palette.onPrimaryContainer : Palette.onPrimary)
                        .background(Capsule().fill(split ? Palette.primaryContainer : Palette.primary))
                        .overlay(Capsule().stroke(Palette.rim.opacity(split ? 1 : 0), lineWidth: 2))
                        .shadow(color: Palette.shade.opacity(split ? 0.18 : 0), radius: 6, y: 3)
                }
                .buttonStyle(.plain)
                .disabled(!split)
                .accessibilityHidden(!split)
                .offset(x: pieceX)
                Button(action: onNext) {
                    Text(next).font(.nunito(16, .bold)).lineLimit(1).minimumScaleFactor(0.8)
                        .padding(.horizontal, 12)
                        .frame(width: nextW, height: 52)
                        .foregroundStyle(Palette.onPrimary)
                        .background(Capsule().fill(Palette.primary))
                        .overlay(Capsule().stroke(Palette.rim, lineWidth: 2))
                        .shadow(color: Palette.shade.opacity(0.22), radius: 8, y: 4)
                }
                .buttonStyle(.plain)
                .offset(x: nextX)
            }
            .animation(reduce ? nil : .spring(response: 0.5, dampingFraction: 0.58), value: split)
        }
        .frame(height: 52)
    }
}
