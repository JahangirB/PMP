import SwiftUI

/**
 * Menu → App updates: the newest build from the SideStore/AltStore source (AppUpdate). iOS lets no app
 * replace itself, so the update opens in SideStore (or AltStore), which re-signs it with your Apple ID.
 */
struct UpdateScreen: View {
    @EnvironmentObject private var m: AppModel
    @Environment(\.openURL) private var openURL
    @State private var noStore = false

    /** Opens the store's link; when neither store is on this phone, says so. */
    private func open(_ store: String, install: Bool) {
        guard let url = install ? AppUpdate.installLink(store) : AppUpdate.sourceLink(store) else { return }
        openURL(url) { accepted in noStore = !accepted }
    }

    var body: some View {
        ScreenScroll {
            Text("App updates").font(.nunito(22, .heavy))
            Text("This iPhone has version \(AppUpdate.installedVersion) (build \(AppUpdate.installedBuild)).")
                .font(.nunito(15)).fixedSize(horizontal: false, vertical: true)
            PmpCard {
                switch m.update {
                case .idle:
                    Text("New builds are published on GitHub. The app also looks once a day and shows \"Update available\" in the menu.")
                        .font(.nunito(14)).fixedSize(horizontal: false, vertical: true)
                    Button { m.checkForUpdate() } label: { Text("Check for updates").frame(maxWidth: .infinity) }
                        .primaryButton().padding(.top, 10)
                case .checking:
                    HStack(spacing: 10) { ProgressView(); Text("Asking GitHub for the newest build…").font(.nunito(14)) }
                case .upToDate:
                    Text("You have the newest version.").font(.nunito(16, .bold))
                    Button("Check again") { m.checkForUpdate() }.textButton().padding(.top, 6)
                case .available(let r):
                    Text("Build \(r.build) is available" + (r.date.count >= 10 ? " (released \(r.date.prefix(10)))" : "") + ".")
                        .font(.nunito(16, .bold))
                    Text("It installs through SideStore, which keeps your progress.").font(.nunito(14))
                        .fixedSize(horizontal: false, vertical: true).padding(.top, 4)
                    Button { open("sidestore", install: true) } label: { Text("Update with SideStore").frame(maxWidth: .infinity) }
                        .primaryButton().padding(.top, 10)
                    Button("Using AltStore? Update with AltStore") { open("altstore", install: true) }.textButton().padding(.top, 4)
                case .failed(let msg):
                    Text(msg).font(.nunito(14)).foregroundStyle(Palette.error).fixedSize(horizontal: false, vertical: true)
                    Button("Try again") { m.checkForUpdate() }.textButton().padding(.top, 6)
                }
                if noStore {
                    Text("Neither SideStore nor AltStore opened. Install SideStore first (docs.sidestore.io), or download the IPA from the website and install it as you did the first time.")
                        .font(.nunito(13)).foregroundStyle(Palette.error).fixedSize(horizontal: false, vertical: true).padding(.top, 8)
                }
            }
            PmpCard {
                SectionLabel("Get updates in SideStore (one-time setup)")
                BulletList(items: [
                    "Add the PMP Prep source to SideStore with the button below (or in SideStore: Sources → + → paste the address).",
                    "From then on SideStore lists new builds under My Apps; tap Update there, or use the button above.",
                    "If you first installed the IPA file by hand, add the source once; your installed app and progress stay.",
                ]).padding(.top, 6)
                Button { open("sidestore", install: false) } label: { Text("Add the source to SideStore").frame(maxWidth: .infinity) }
                    .primaryButton().padding(.top, 10)
                Button("Add it to AltStore instead") { open("altstore", install: false) }.textButton().padding(.top, 4)
                Text(AppUpdate.source).font(.system(size: 12, design: .monospaced)).foregroundStyle(Palette.onSurfaceVariant)
                    .textSelection(.enabled).fixedSize(horizontal: false, vertical: true).padding(.top, 6)
            }
            Text("With a free Apple ID, SideStore still refreshes the app every 7 days as before; an update doesn't change that.")
                .font(.nunito(12)).foregroundStyle(Palette.onSurfaceVariant).fixedSize(horizontal: false, vertical: true)
            Spacer().frame(height: 16)
        }
    }
}
