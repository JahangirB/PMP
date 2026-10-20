import SwiftUI

/** The menu's Support button opens this page (docs/progress-format.md §4): why, what it pays for, and ways to help. */
struct SupportScreen: View {
    @EnvironmentObject private var m: AppModel
    @Environment(\.openURL) private var openURL

    private func open(_ s: String) {
        if let url: URL = URL(string: s) { openURL(url) }
    }

    var body: some View {
        ScreenScroll {
            Text(Support.pageTitle).font(.nunito(22, .heavy))
            Text(Support.pageBody).font(.nunito(15)).lineSpacing(4).fixedSize(horizontal: false, vertical: true)
            PmpCard {
                SectionLabel("What your support pays for")
                BulletList(items: Support.paysFor).padding(.top, 6)
            }
            PmpCard {
                SectionLabel("Ways to help")
                Button {
                    open(Support.url)
                    m.supportPressed()
                } label: {
                    Label(Support.kofiLabel, systemImage: "heart.fill")
                        .font(.nunito(15, .bold))
                        .frame(maxWidth: .infinity)
                        .padding(.vertical, 12)
                        .background(Palette.supportTint, in: Capsule())
                        .overlay(Capsule().stroke(Palette.warning, lineWidth: 1.5))
                        .foregroundStyle(Palette.supportInk)
                }
                .buttonStyle(.plain)
                .padding(.top, 8)
                Text(Support.kofiNote).font(.nunito(12)).foregroundStyle(Palette.onSurfaceVariant)
                    .fixedSize(horizontal: false, vertical: true)
                VStack(alignment: .leading, spacing: 8) {
                    ShareLink(item: Support.shareText) { Text("Share the app").font(.nunito(14, .semibold)) }
                    Button("Report a wrong question") { open(Support.reportUrl) }.font(.nunito(14, .semibold))
                    Button("Star it on GitHub") { open(Support.repoUrl) }.font(.nunito(14, .semibold))
                }
                .padding(.top, 10)
            }
            Text(Support.thanks).font(.nunito(13)).foregroundStyle(Palette.onSurfaceVariant)
                .fixedSize(horizontal: false, vertical: true)
            Spacer().frame(height: 16)
        }
    }
}
