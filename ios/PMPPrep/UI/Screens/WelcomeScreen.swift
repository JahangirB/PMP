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
                    HStack(spacing: 8) {
                        Button("Back") { if at > 0 { at -= 1 } }.textButton()
                            .opacity(at > 0 ? 1 : 0).disabled(at == 0)
                        Button {
                            if !last { at += 1; return }
                            let setup = m.setupDue
                            m.markWelcomeRead()
                            at = 0
                            // From the menu: back where it was opened, or to the path when the setup is still due.
                            if m.screen == .WELCOME { if setup { m.home() } else { m.back() } }
                        } label: {
                            Text(!last ? "Next" : (m.setupDue ? w.button : w.done)).font(.nunito(16, .bold)).frame(maxWidth: .infinity, minHeight: 28)
                        }
                        .primaryButton()
                    }
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
