import SwiftUI

/**
 * Every question whose latest answer was wrong. Tagging why it went wrong is
 * what turns a list of misses into a pattern you can fix.
 */
struct MistakeJournalScreen: View {
    @EnvironmentObject private var m: AppModel
    /** nil: all; "untagged"; or a RootCause raw value. */
    @State private var cause: String? = nil
    @State private var tagging: Question? = nil

    var body: some View {
        let tags = m.store.mistakeTags
        // Sure-but-wrong first: the most dangerous kind of mistake on exam day.
        let missed: [Question] = m.store.last.values.filter { !$0.correct }
            .sorted { a, b in a.sureWrong != b.sureWrong ? a.sureWrong : a.at > b.at }
            .compactMap { m.repo.byId[$0.qid] }
        let shown = missed.filter { q in
            switch cause {
            case nil: return true
            case "untagged"?: return tags[q.id] == nil
            case let c?: return tags[q.id]?.cause.rawValue == c
            }
        }
        let untagged = missed.filter { tags[$0.id] == nil }.count

        ScrollView {
            LazyVStack(alignment: .leading, spacing: 10) {
                VStack(alignment: .leading, spacing: 4) {
                    Text("\(missed.count) questions wrong last time").font(.nunito(20, .heavy))
                    Text("Tag why each one went wrong. Answer it correctly and it leaves the journal.")
                        .font(.nunito(13)).foregroundStyle(Palette.onSurfaceVariant)
                    ScrollView(.horizontal, showsIndicators: false) {
                        HStack(spacing: 6) {
                            Chip(label: "All \(missed.count)", selected: cause == nil) { cause = nil }
                            Chip(label: "Untagged \(untagged)", selected: cause == "untagged") { cause = "untagged" }
                            ForEach(RootCause.allCases) { c in
                                Chip(label: "\(c.title) \(missed.filter { tags[$0.id]?.cause == c }.count)", selected: cause == c.rawValue) { cause = c.rawValue }
                            }
                        }
                        .padding(.vertical, 2)
                    }
                    .padding(.top, 8)
                    Button {
                        m.startPracticeWith(Array(shown.prefix(40)), title: "Mistake review")
                    } label: { Text("Retry \(min(40, shown.count)) of these").frame(maxWidth: .infinity) }
                        .primaryButton()
                        .disabled(shown.isEmpty)
                        .padding(.top, 8)
                }
                .padding(.top, 8)

                if missed.isEmpty {
                    EmptyNote(text: "Nothing here yet. Questions you get wrong in practice or a mock show up here.")
                }
                ForEach(shown) { q in
                    MistakeCard(q: q, tag: tags[q.id], sure: m.store.last[q.id]?.sureWrong == true, task: m.repo.task(q.ecoTask),
                                onTag: { tagging = q },
                                onRetry: { m.startPracticeWith([q], title: "Retry") })
                }
                Spacer().frame(height: 16)
            }
            .padding(.horizontal, 16)
            .frame(maxWidth: 720)
            .frame(maxWidth: .infinity)
        }
        .sheet(item: $tagging) { q in
            TagSheet(initial: m.store.mistakeTags[q.id]) { pick, note in
                m.store.tagMistake(q.id, pick, note)
                tagging = nil
            } onCancel: {
                tagging = nil
            }
        }
    }
}

private struct MistakeCard: View {
    let q: Question
    let tag: MistakeTag?
    let sure: Bool
    let task: EcoTask?
    let onTag: () -> Void
    let onRetry: () -> Void

    var body: some View {
        PmpCard {
            HStack(spacing: 6) {
                DomainChip(domain: q.domain)
                if sure { Pill(text: "You were sure", color: Palette.error) }
                if let task { Pill(text: task.label, color: Palette.onSurfaceVariant) }
                Spacer(minLength: 0)
            }
            Text(q.stem).font(.nunito(14)).lineSpacing(4).lineLimit(4).padding(.top, 6)
            if let tag {
                Text("Cause: \(tag.cause.title)").font(.nunito(13, .bold)).foregroundStyle(Palette.error).padding(.top, 6)
                if !tag.note.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty {
                    Text(tag.note).font(.nunito(13)).foregroundStyle(Palette.onSurfaceVariant)
                }
            }
            if !q.mindset.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty {
                Text(q.mindset).font(.nunito(12)).foregroundStyle(Palette.primary).lineSpacing(2)
            }
            HStack(spacing: 8) {
                Button(action: onTag) { Text(tag == nil ? "Tag the cause" : "Change tag").frame(maxWidth: .infinity) }
                    .outlinedButton()
                Button(action: onRetry) { Text("Retry").frame(maxWidth: .infinity) }
                    .primaryButton()
            }
            .padding(.top, 6)
        }
    }
}

/** "Why did this one go wrong?": a cause and an optional note. */
private struct TagSheet: View {
    let onSave: (RootCause, String) -> Void
    let onCancel: () -> Void
    @State private var pick: RootCause
    @State private var note: String

    init(initial: MistakeTag?, onSave: @escaping (RootCause, String) -> Void, onCancel: @escaping () -> Void) {
        self.onSave = onSave
        self.onCancel = onCancel
        _pick = State(initialValue: initial?.cause ?? .KNOWLEDGE_GAP)
        _note = State(initialValue: initial?.note ?? "")
    }

    var body: some View {
        VStack(spacing: 0) {
            HStack {
                Button("Cancel", action: onCancel).textButton()
                Spacer()
                Button("Save") { onSave(pick, note) }.textButton()
            }
            .padding(.horizontal, 20).padding(.top, 8)
            ScrollView {
                VStack(alignment: .leading, spacing: 8) {
                    Text("Why did this one go wrong?").font(.nunito(20, .heavy)).padding(.bottom, 4)
                    ForEach(RootCause.allCases) { c in
                        let selected = pick == c
                        Button { pick = c } label: {
                            HStack(alignment: .top, spacing: 10) {
                                Image(systemName: selected ? "checkmark.circle.fill" : "circle")
                                    .foregroundStyle(selected ? Palette.primary : Palette.onSurfaceVariant)
                                VStack(alignment: .leading, spacing: 2) {
                                    Text(c.title).font(.nunito(15, .bold))
                                    Text(c.description).font(.nunito(12)).multilineTextAlignment(.leading)
                                }
                                Spacer(minLength: 0)
                            }
                            .foregroundStyle(selected ? Palette.onSecondaryContainer : Palette.onSurface)
                            .padding(12)
                            .background(RoundedRectangle(cornerRadius: 12).fill(selected ? Palette.secondaryContainer : Palette.surface))
                            .overlay(RoundedRectangle(cornerRadius: 12).strokeBorder(selected ? Palette.primary.opacity(0.5) : Palette.outline, lineWidth: 1))
                        }
                        .buttonStyle(.plain)
                        .accessibilityAddTraits(selected ? .isSelected : [])
                    }
                    Text("Note (optional)").font(.nunito(12, .semibold)).foregroundStyle(Palette.onSurfaceVariant).padding(.top, 6)
                    TextField("Note (optional)", text: $note, axis: .vertical)
                        .font(.nunito(15))
                        .lineLimit(2...5)
                        .padding(12)
                        .background(RoundedRectangle(cornerRadius: 10).fill(Palette.surface))
                        .overlay(RoundedRectangle(cornerRadius: 10).strokeBorder(Palette.outline, lineWidth: 1))
                }
                .padding(20)
            }
            .scrollDismissesKeyboard(.interactively)
        }
        .foregroundStyle(Palette.onSurface)
        .background(Palette.background.ignoresSafeArea())
        .presentationDetents([.large])
    }
}
