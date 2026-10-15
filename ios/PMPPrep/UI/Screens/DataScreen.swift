import SwiftUI
import UniformTypeIdentifiers

/** The progress backup as a file to save: the same JSON the Android app reads and writes. */
private struct BackupDocument: FileDocument {
    static var readableContentTypes: [UTType] { [.json] }
    var text: String
    init(text: String) { self.text = text }
    init(configuration: ReadConfiguration) throws {
        text = configuration.file.regularFileContents.flatMap { String(data: $0, encoding: .utf8) } ?? ""
    }
    func fileWrapper(configuration: WriteConfiguration) throws -> FileWrapper { FileWrapper(regularFileWithContents: Data(text.utf8)) }
}

private struct DataMessage { let title: String; let text: String }

/** Question banks, the content audit, the progress backup and the reset. */
struct DataScreen: View {
    @EnvironmentObject private var m: AppModel
    @State private var message: DataMessage? = nil
    @State private var confirmReset = false
    @State private var deleting: QuestionPack? = nil
    @State private var importing = false
    @State private var restoring = false
    @State private var exporting = false
    @State private var backup: BackupDocument? = nil

    var body: some View {
        let pool = m.pool()
        let packs = m.repo.packs
        let firstUser = packs.firstIndex { !$0.builtin }
        ScreenScroll {
            VStack(alignment: .leading, spacing: 4) {
            Text("Question banks").font(.nunito(22, .heavy))
            Text("Add your own bank from a .json question pack (the format in AUTHORING.md, or a plain list of questions). Questions already in the app are skipped, and invalid ones are listed with the reason. Turn a bank off to leave it out of practice and mocks.")
                .font(.nunito(13)).foregroundStyle(Palette.onSurfaceVariant).fixedSize(horizontal: false, vertical: true)
            }
            Button { importing = true } label: {
                Label("Add question bank (.json)", systemImage: "square.and.arrow.down").frame(maxWidth: .infinity)
            }
            .primaryButton()
            .fileImporter(isPresented: $importing, allowedContentTypes: [.json], allowsMultipleSelection: true) { result in
                switch result {
                case .success(let urls): if !urls.isEmpty { message = DataMessage(title: "Import", text: m.importPacks(urls)) }
                case .failure(let e): message = DataMessage(title: "Import", text: e.localizedDescription)
                }
            }
            SectionLabel("\(pool.count) questions enabled")
            ForEach(packs.indices, id: \.self) { i in
                if i == firstUser { SectionLabel("Your imported banks") }
                packCard(packs[i])
            }
            SectionLabel("Content audit")
            auditCard(pool)
            backupSection
            dangerZone
            Text("PMP, PMBOK and PMI are registered marks of the Project Management Institute, Inc. This is an independent study aid, not endorsed by or affiliated with PMI.")
                .font(.nunito(11)).foregroundStyle(Palette.onSurfaceVariant).padding(.top, 8).fixedSize(horizontal: false, vertical: true)
            Spacer().frame(height: 16)
        }
        .alert(message?.title ?? "", isPresented: Binding(get: { message != nil }, set: { if !$0 { message = nil } })) {
            Button("OK") { message = nil }
        } message: {
            Text(message?.text ?? "")
        }
        .alert("Reset all progress?", isPresented: $confirmReset) {
            Button("Reset", role: .destructive) { m.resetProgress() }
            Button("Cancel", role: .cancel) {}
        } message: {
            Text("This deletes every answer, flag, note, card schedule, mock result and your plan. Question banks are kept.")
        }
        .alert("Delete \"\(deleting?.name ?? "")\"?", isPresented: Binding(get: { deleting != nil }, set: { if !$0 { deleting = nil } })) {
            Button("Delete", role: .destructive) { if let p = deleting { m.deletePack(p) }; deleting = nil }
            Button("Cancel", role: .cancel) { deleting = nil }
        } message: {
            Text("Its \(deleting?.questions.count ?? 0) questions leave the app. Your answer history is kept and comes back if you import the file again.")
        }
    }

    private func packCard(_ p: QuestionPack) -> some View {
        var info = "\(p.questions.count) questions"
        if p.skippedDuplicates > 0 { info += " · \(p.skippedDuplicates) duplicates skipped" }
        if p.invalid > 0 { info += " · \(p.invalid) invalid" }
        return PmpCard {
            Toggle(isOn: Binding(get: { m.repo.isEnabled(p) }, set: { m.setPackEnabled(p, $0) })) {
                VStack(alignment: .leading, spacing: 2) {
                    Text(p.name).font(.nunito(14, .bold))
                    Text(info).font(.nunito(12)).foregroundStyle(Palette.onSurfaceVariant)
                }
            }
            .tint(Palette.primary)
            // Not beside the switch: a slip on the toggle must never land on Delete.
            if !p.builtin {
                Button { deleting = p } label: { Label("Delete this bank", systemImage: "trash") }
                    .textButton(Palette.error)
                    .padding(.top, 4)
            }
        }
    }

    private func auditCard(_ pool: [Question]) -> some View {
        let x: Extras = m.repo.extras
        let thin = x.tasks.filter { t in pool.filter { $0.ecoTask == t.id }.count < 10 }
        return PmpCard {
            Text("Enabled pool against the published domain weights").font(.nunito(13, .semibold))
            ForEach(Domain.allCases) { d in
                let share = pool.isEmpty ? 0 : Double(pool.filter { $0.domain == d }.count) / Double(pool.count)
                let target = x.domainWeights[d] ?? 0
                ProgressRow(label: "\(d.title) · target \(pct(target))", value: share, right: pct(share),
                            color: abs(share - target) > 0.08 ? Palette.warning : Palette.domain(d))
                    .padding(.top, 8)
            }
            Text("By type: " + QType.allCases.map { t in "\(t.title) \(pool.filter { $0.type == t }.count)" }.joined(separator: " · "))
                .font(.nunito(12)).padding(.top, 10).fixedSize(horizontal: false, vertical: true)
            Text(thin.isEmpty ? "Every ECO task has at least 10 questions." : "Under 10 questions: " + thin.map(\.code).joined(separator: ", "))
                .font(.nunito(12)).foregroundStyle(thin.isEmpty ? Palette.onSurfaceVariant : Palette.warning)
                .padding(.top, 6).fixedSize(horizontal: false, vertical: true)
        }
    }

    private var backupSection: some View {
        VStack(alignment: .leading, spacing: 12) {
            SectionLabel("Backup")
            Text("Answers, flags, notes, mistakes, flashcard schedule, mock results and your plan stay on this phone. Save a backup to keep them or move them to another phone. The backup file works with the Android app too, both ways.")
                .font(.nunito(13)).foregroundStyle(Palette.onSurfaceVariant).fixedSize(horizontal: false, vertical: true)
            Button {
                backup = BackupDocument(text: m.backupText())
                exporting = true
            } label: { Text("Save progress backup").frame(maxWidth: .infinity) }
                .outlinedButton()
                .fileExporter(isPresented: $exporting, document: backup, contentType: .json, defaultFilename: backupName()) { result in
                    switch result {
                    case .success(let url): message = DataMessage(title: "Backup", text: "Saved as \(url.lastPathComponent). Restore it here or in the Android app.")
                    case .failure(let e): message = DataMessage(title: "Backup", text: "Could not save: \(e.localizedDescription)")
                    }
                    backup = nil
                }
            Button { restoring = true } label: { Text("Restore from backup").frame(maxWidth: .infinity) }
                .outlinedButton()
                .fileImporter(isPresented: $restoring, allowedContentTypes: [.json]) { result in
                    switch result {
                    case .success(let url): message = DataMessage(title: "Restore", text: m.restoreProgress(url))
                    case .failure(let e): message = DataMessage(title: "Restore", text: "Could not restore: \(e.localizedDescription)")
                    }
                }
        }
    }

    // Destructive actions sit apart from everyday ones, and ask first.
    private var dangerZone: some View {
        VStack(alignment: .leading, spacing: 12) {
            SectionLabel("Danger zone").padding(.top, 16)
            PmpCard(border: Palette.error.opacity(0.5)) {
                Text("Reset deletes every answer, flag, note, card schedule, mock result and your plan on this phone. Save a backup first if you might want them back.")
                    .font(.nunito(13)).fixedSize(horizontal: false, vertical: true)
                Button { confirmReset = true } label: { Text("Reset all progress").foregroundStyle(Palette.error).frame(maxWidth: .infinity) }
                    .outlinedButton()
                    .padding(.top, 8)
            }
        }
    }

    private func backupName() -> String {
        let f = DateFormatter()
        f.locale = Locale(identifier: "en_US_POSIX")
        f.dateFormat = "yyyy-MM-dd"
        return "pmp-progress-" + f.string(from: Date())
    }
}
