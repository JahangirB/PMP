import SwiftUI
import UIKit

/**
 * Cloud sync: the setup guide for the person's own Google Apps Script (docs/progress-format.md §4),
 * then the connection's status, Sync now and Disconnect. Errors show here, never as pop-ups.
 */
struct SyncScreen: View {
    @EnvironmentObject private var m: AppModel
    @State private var url: String = ""
    @State private var code: String = ""
    @State private var haveScript: Bool = false

    var body: some View {
        ScreenScroll {
            ScreenTitle(title: "Cloud sync",
                        subtitle: "Keep this phone, the web app and the Android app in step through one file in your own Google Drive.")
            if m.syncEnabled {
                SyncConnected()
            } else if haveScript {
                existingScript
            } else {
                SyncGuide(code: $code, url: $url, haveScript: $haveScript)
            }
            SyncPrivacyNote()
            Spacer().frame(height: 24)
        }
        .onAppear {
            if code.isEmpty { code = m.syncDraftCode() }
        }
    }

    /** "I already have a script": the URL and the code from the first device. */
    private var existingScript: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("Paste the Web app URL and the sync code you used on your first device. Both are on that device's Cloud sync screen.")
                .font(.nunito(14)).fixedSize(horizontal: false, vertical: true)
            SyncField(title: "Web app URL", placeholder: "https://script.google.com/macros/s/…/exec", text: $url)
            SyncField(title: "Sync code", placeholder: "32 letters and digits", text: $code)
            SyncConnectButton(url: url, code: code)
            Button("Back to the setup guide") {
                haveScript = false
                code = m.syncDraftCode()
            }
            .textButton()
        }
    }
}

/** The six steps, with the script ready to copy. */
private struct SyncGuide: View {
    @EnvironmentObject private var m: AppModel
    @Binding var code: String
    @Binding var url: String
    @Binding var haveScript: Bool

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            Button("I already have a script") { haveScript = true; code = "" }
                .outlinedButton()
            SyncStep(n: 1, text: "This phone made a random 32-character sync code and filled it into the script below. Copy the script.")
            SyncCodeBox(code: code) { code = m.newSyncDraftCode() }
            SyncScriptBox(script: m.syncScript(code))
            SyncStep(n: 2, text: "Open script.google.com, choose **New project**, replace everything in **Code.gs** with the copied script and save it.")
            Link(destination: URL(string: "https://script.google.com")!) {
                Label("Open script.google.com", systemImage: "arrow.up.right.square")
            }
            .font(.nunito(15, .semibold))
            SyncDeploySteps()
            SyncField(title: "Web app URL", placeholder: "https://script.google.com/macros/s/…/exec", text: $url)
            SyncConnectButton(url: url, code: code)
        }
    }
}

/** Steps 3 to 6: the deployment settings, spelled out. */
private struct SyncDeploySteps: View {
    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            SyncStep(n: 3, text: "Choose **Deploy → New deployment → Select type: Web app**. Set **Execute as: Me** and **Who has access: Anyone**, then **Deploy**.")
            SyncStep(n: 4, text: "Authorise it. Choose your Google account. On \"Google hasn't verified this app\", choose **Advanced → Go to (project) (unsafe)** (it's your own script), then **Allow**.")
            SyncStep(n: 5, text: "Copy the **Web app URL** (https://script.google.com/macros/s/…/exec) and paste it below. Press **Test connection**.")
            SyncStep(n: 6, text: "On every other device, open Cloud sync, choose **I already have a script**, and paste the same URL and sync code.")
        }
    }
}

private struct SyncStep: View {
    let n: Int
    let text: String
    var body: some View {
        HStack(alignment: .top, spacing: 10) {
            Text("\(n)").font(.nunito(14, .heavy)).foregroundStyle(Palette.onPrimaryContainer)
                .frame(width: 28, height: 28)
                .background(Circle().fill(Palette.primaryContainer))
                .accessibilityLabel("Step \(n)")
            Text(LocalizedStringKey(text)).font(.nunito(14)).fixedSize(horizontal: false, vertical: true)
            Spacer(minLength: 0)
        }
    }
}

private struct SyncCodeBox: View {
    let code: String
    let renew: () -> Void
    var body: some View {
        PmpCard {
            SectionLabel("Sync code")
            Text(code).font(.system(size: 15, weight: .semibold, design: .monospaced)).textSelection(.enabled)
                .fixedSize(horizontal: false, vertical: true)
            HStack(spacing: 16) {
                Button { UIPasteboard.general.string = code } label: { Label("Copy code", systemImage: "doc.on.doc") }
                    .textButton()
                Button("Make a new code", action: renew).textButton(Palette.onSurfaceVariant)
            }
        }
    }
}

private struct SyncScriptBox: View {
    let script: String
    @State private var copied: Bool = false
    var body: some View {
        if script.isEmpty {
            Text("This build doesn't carry the script. It is sync/pmp-sync.gs in the PMP Prep project; replace __SYNC_CODE__ in it with the code above.")
                .font(.nunito(13)).foregroundStyle(Palette.warning).fixedSize(horizontal: false, vertical: true)
        } else {
            VStack(alignment: .leading, spacing: 8) {
                ScrollView {
                    Text(script).font(.system(size: 11, design: .monospaced)).textSelection(.enabled)
                        .frame(maxWidth: .infinity, alignment: .leading)
                        .padding(12)
                }
                .frame(height: 240)
                .background(RoundedRectangle(cornerRadius: 12).fill(Palette.surfaceVariant))
                Button {
                    UIPasteboard.general.string = script
                    copied = true
                } label: {
                    Label(copied ? "Script copied" : "Copy the script", systemImage: copied ? "checkmark" : "doc.on.doc").frame(maxWidth: .infinity)
                }
                .primaryButton()
            }
        }
    }
}

private struct SyncField: View {
    let title: String
    let placeholder: String
    @Binding var text: String
    var body: some View {
        VStack(alignment: .leading, spacing: 4) {
            SectionLabel(title)
            TextField(placeholder, text: $text)
                .font(.system(size: 14, design: .monospaced))
                .textInputAutocapitalization(.never)
                .autocorrectionDisabled(true)
                .keyboardType(.URL)
                .padding(12)
                .background(RoundedRectangle(cornerRadius: 12).fill(Palette.surface))
                .overlay(RoundedRectangle(cornerRadius: 12).strokeBorder(Palette.outline, lineWidth: 1))
        }
    }
}

/** Test connection, with the error (if any) right under it. */
private struct SyncConnectButton: View {
    @EnvironmentObject private var m: AppModel
    let url: String
    let code: String
    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            Button { m.connectSync(url: url, code: code) } label: {
                HStack(spacing: 8) {
                    if m.syncBusy { ProgressView() }
                    Text(m.syncBusy ? "Testing…" : "Test connection")
                }
                .frame(maxWidth: .infinity)
            }
            .primaryButton()
            .disabled(m.syncBusy || url.trimmingCharacters(in: .whitespaces).isEmpty)
            SyncErrorText()
        }
    }
}

private struct SyncErrorText: View {
    @EnvironmentObject private var m: AppModel
    var body: some View {
        if let e = m.syncError {
            Label(e, systemImage: "exclamationmark.triangle")
                .font(.nunito(13)).foregroundStyle(Palette.error).fixedSize(horizontal: false, vertical: true)
        }
    }
}

/** Connected: status, Sync now, the code for other devices, and Disconnect. */
private struct SyncConnected: View {
    @EnvironmentObject private var m: AppModel
    @State private var showCode: Bool = false
    @State private var confirmDisconnect: Bool = false

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            statusCard
            Button { m.syncNow() } label: {
                HStack(spacing: 8) {
                    if m.syncBusy { ProgressView() }
                    Text(m.syncBusy ? "Syncing…" : "Sync now")
                }
                .frame(maxWidth: .infinity)
            }
            .primaryButton()
            .disabled(m.syncBusy || m.mock != nil)
            if m.mock != nil {
                Text("Sync waits until your mock exam is finished.").font(.nunito(13)).foregroundStyle(Palette.onSurfaceVariant)
            }
            otherDevices
            Button { confirmDisconnect = true } label: { Text("Disconnect").foregroundStyle(Palette.error).frame(maxWidth: .infinity) }
                .outlinedButton()
        }
        .alert("Disconnect cloud sync?", isPresented: $confirmDisconnect) {
            Button("Disconnect", role: .destructive) { m.disconnectSync() }
            Button("Cancel", role: .cancel) {}
        } message: {
            Text("This phone forgets the URL and the code. Your progress stays here and in your Google Drive.")
        }
    }

    private var statusCard: some View {
        PmpCard {
            Label("Connected", systemImage: "checkmark.icloud").font(.nunito(16, .bold)).foregroundStyle(Palette.success)
            Text(m.syncURL).font(.system(size: 12, design: .monospaced)).foregroundStyle(Palette.onSurfaceVariant)
                .lineLimit(2).truncationMode(.middle)
            Text(m.lastSyncAt > 0 ? "Last synced " + AppModel.relative(m.lastSyncAt) + "." : "Not synced yet.")
                .font(.nunito(14)).padding(.top, 4)
            Text("Syncs when the app opens or goes to the background, after each session, practice set, mock and card session, and when you press Sync now. Never during a mock.")
                .font(.nunito(12)).foregroundStyle(Palette.onSurfaceVariant).fixedSize(horizontal: false, vertical: true)
            SyncErrorText().padding(.top, 4)
        }
    }

    private var otherDevices: some View {
        PmpCard {
            Text("Another device").font(.nunito(15, .bold))
            Text("On it, open Cloud sync, choose I already have a script, and paste this URL and sync code.")
                .font(.nunito(13)).foregroundStyle(Palette.onSurfaceVariant).fixedSize(horizontal: false, vertical: true)
            if showCode {
                Text(m.syncCode).font(.system(size: 15, weight: .semibold, design: .monospaced)).textSelection(.enabled).padding(.top, 4)
                HStack(spacing: 16) {
                    Button { UIPasteboard.general.string = m.syncCode } label: { Label("Copy code", systemImage: "doc.on.doc") }.textButton()
                    Button { UIPasteboard.general.string = m.syncURL } label: { Label("Copy URL", systemImage: "link") }.textButton()
                }
            } else {
                Button("Show the sync code") { showCode = true }.textButton()
            }
        }
    }
}

private struct SyncPrivacyNote: View {
    var body: some View {
        PmpCard(container: Palette.secondaryContainer) {
            Text("Your data, your account").font(.nunito(15, .bold)).foregroundStyle(Palette.onSecondaryContainer)
            Text("The script runs in your Google account and keeps one file, PMP Prep/pmp-sync.json, in your Drive. \"Anyone\" only means the URL answers without a Google sign-in: every request must carry the sync code, and the script refuses anything without it. Whoever has both the URL and the code can read and replace the progress file, so keep them private. To revoke access, change the code in the script and deploy a new version. Your Gemini key never leaves this phone.")
                .font(.nunito(13)).foregroundStyle(Palette.onSecondaryContainer).fixedSize(horizontal: false, vertical: true)
        }
    }
}
