import SwiftUI

/** A labelled multi-line field, for the study-tool sheets. */
private struct STField: View {
    let label: String?
    let placeholder: String
    @Binding var text: String
    let minLines: Int
    var enabled = true

    var body: some View {
        VStack(alignment: .leading, spacing: 4) {
            if let label { Text(label).font(.nunito(12, .semibold)).foregroundStyle(Palette.onSurfaceVariant) }
            TextField(placeholder, text: $text, axis: .vertical)
                .font(.nunito(15))
                .lineLimit(minLines...)
                .disabled(!enabled)
                .padding(12)
                .background(RoundedRectangle(cornerRadius: 10, style: .continuous).fill(Palette.surface))
                .overlay(RoundedRectangle(cornerRadius: 10, style: .continuous).strokeBorder(Palette.outline, lineWidth: 1))
                .accessibilityLabel(label ?? placeholder)
        }
    }
}

/** Turn a missed question into a card for the "My cards" deck. Both sides are drafted and editable. */
struct MakeCardSheet: View {
    @EnvironmentObject private var m: AppModel
    private let q: Question
    private let onDone: () -> Void
    @State private var front = ""
    @State private var back = ""
    @State private var loaded = false

    init(q: Question, onDone: @escaping () -> Void) {
        self.q = q
        self.onDone = onDone
    }

    private var canSave: Bool {
        !front.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty && !back.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty
    }

    var body: some View {
        NavigationStack {
            ScrollView {
                VStack(alignment: .leading, spacing: 12) {
                    Text("Put it in your own words: writing the card is where the learning happens.")
                        .font(.nunito(13)).foregroundStyle(Palette.onSurfaceVariant)
                        .fixedSize(horizontal: false, vertical: true)
                    STField(label: "Front: the situation", placeholder: "", text: $front, minLines: 2)
                    STField(label: "Back: the right move and why", placeholder: "", text: $back, minLines: 4)
                    Button {
                        m.makeCard(q, front: front, back: back)
                        onDone()
                    } label: { Text("Save to My cards").frame(maxWidth: .infinity) }
                        .primaryButton()
                        .disabled(!canSave)
                        .padding(.top, 4)
                }
                .padding(16)
            }
            .scrollDismissesKeyboard(.interactively)
            .background(Palette.background.ignoresSafeArea())
            .navigationTitle("Make a card")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .cancellationAction) { Button("Cancel", action: onDone) }
            }
        }
        .foregroundStyle(Palette.onSurface)
        .tint(Palette.primary)
        .onAppear {
            if loaded { return }
            loaded = true
            // Editing a card made earlier starts from what you wrote; otherwise from a draft.
            if let mine = m.store.myCards.first(where: { $0.id == "my:\(q.id)" }) {
                front = mine.front
                back = mine.back
            } else {
                let d = m.cardDraft(q)
                front = d.0
                back = d.1
            }
        }
    }
}

/** Explain it in your own words; Gemini says what you got right and what you missed. */
struct ExplainBackSheet: View {
    @EnvironmentObject private var m: AppModel
    private let topic: String
    private let prompt: String
    private let onDone: () -> Void
    @State private var text = ""

    init(topic: String, prompt: String, onDone: @escaping () -> Void) {
        self.topic = topic
        self.prompt = prompt
        self.onDone = onDone
    }

    var body: some View {
        NavigationStack {
            ScrollView {
                VStack(alignment: .leading, spacing: 12) {
                    if m.geminiKey.isEmpty {
                        Text("This uses the Gemini coach. Add your Gemini key in Coach first; your text is sent to Google to get feedback.")
                            .font(.nunito(14))
                            .fixedSize(horizontal: false, vertical: true)
                        Button {
                            onDone()
                            m.coachTab = 1
                            m.navigate(.COACH)
                        } label: { Text("Open Coach").frame(maxWidth: .infinity) }
                            .primaryButton()
                    } else {
                        Text(prompt).font(.nunito(13)).foregroundStyle(Palette.onSurfaceVariant)
                            .fixedSize(horizontal: false, vertical: true)
                        STField(label: nil, placeholder: "In my words…", text: $text, minLines: 4, enabled: !m.explainBusy)
                        if m.explainBusy {
                            ProgressView().frame(maxWidth: .infinity)
                        }
                        if let e = m.explainError {
                            Text(e).font(.nunito(13)).foregroundStyle(Palette.error)
                                .fixedSize(horizontal: false, vertical: true)
                        }
                        if let reply = m.explainReply {
                            PmpCard {
                                SectionLabel("Feedback")
                                MarkdownText(text: reply).padding(.top, 2)
                                Text("AI feedback can be wrong: treat it as a tutor's view, not a grade.")
                                    .font(.nunito(11)).foregroundStyle(Palette.onSurfaceVariant)
                                    .padding(.top, 4)
                            }
                            Button(action: onDone) { Text("Done").frame(maxWidth: .infinity) }
                                .primaryButton()
                        } else {
                            Button { m.explainBack(topic: topic, text: text) } label: {
                                Text("Get feedback").frame(maxWidth: .infinity)
                            }
                            .primaryButton()
                            .disabled(text.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty || m.explainBusy)
                        }
                    }
                }
                .padding(16)
            }
            .scrollDismissesKeyboard(.interactively)
            .background(Palette.background.ignoresSafeArea())
            .navigationTitle("Explain it in your words")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .cancellationAction) {
                    Button(m.explainReply == nil ? "Cancel" : "Close", action: onDone)
                }
            }
        }
        .foregroundStyle(Palette.onSurface)
        .tint(Palette.primary)
        .onAppear { m.clearExplain() }
        .onDisappear { m.clearExplain() }
    }
}

/** What the question was about, for the explain-back prompt. */
func questionTopic(_ q: Question) -> String {
    var s = q.stem
    let right = q.options.filter(\.correct)
    if !right.isEmpty { s += "\n\nThe right answer: " + right.map(\.text).joined(separator: "; ") }
    if !q.explanation.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty { s += "\n\nThe app's explanation: " + q.explanation }
    return s
}
