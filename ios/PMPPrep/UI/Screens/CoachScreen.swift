import SwiftUI

private let REVIEW_PROMPT = ChatCommands.REVIEW

/** The coach: today's offline report, the Gemini chat, and the notes page. */
struct CoachScreen: View {
    @EnvironmentObject private var m: AppModel

    var body: some View {
        VStack(spacing: 0) {
            Picker("Coach", selection: $m.coachTab) {
                Text("Today").tag(0)
                Text("Ask Gemini").tag(1)
                Text("Notes").tag(2)
            }
            .pickerStyle(.segmented)
            .padding(.horizontal, 16)
            .padding(.vertical, 8)
            switch m.coachTab {
            case 0: CoachToday()
            case 1: CoachChat()
            default: NotesView()
            }
        }
    }
}

private struct CoachToday: View {
    @EnvironmentObject private var m: AppModel

    var body: some View {
        let r = m.coach().report()
        ScreenScroll {
            PmpCard(container: Palette.primaryContainer.opacity(0.5), border: Palette.primary) {
                Text(r.headline).font(.nunito(20, .heavy)).fixedSize(horizontal: false, vertical: true)
                Text(r.verdict).font(.nunito(14)).padding(.top, 4).fixedSize(horizontal: false, vertical: true)
                Button {
                    m.coachTab = 1
                    if !m.geminiKey.isEmpty { m.ask(REVIEW_PROMPT) } else { m.coachDraft = REVIEW_PROMPT }
                } label: { Text("Ask Gemini for a full review").frame(maxWidth: .infinity) }
                    .primaryButton()
                    .padding(.top, 10)
            }
            if !r.strengths.isEmpty {
                PmpCard { SectionLabel("Going well"); BulletList(items: r.strengths, color: Palette.success, bullet: "✓").padding(.top, 6) }
            }
            if !r.improve.isEmpty {
                PmpCard { SectionLabel("To improve"); BulletList(items: r.improve, bullet: "→").padding(.top, 6) }
            }
            if !r.steps.isEmpty {
                SectionLabel("Today's session")
                ForEach(r.steps) { s in
                    PmpCard(onTap: { m.coachGo(s.action, s.arg) }) {
                        HStack(spacing: 10) {
                            Image(systemName: s.done ? "checkmark.circle.fill" : "circle")
                                .font(.system(size: 20))
                                .foregroundStyle(s.done ? Palette.success : Palette.primary)
                                .accessibilityHidden(true)
                            VStack(alignment: .leading, spacing: 2) {
                                Text(s.title).font(.nunito(14, .bold)).multilineTextAlignment(.leading)
                                Text(s.why).font(.nunito(12)).foregroundStyle(Palette.onSurfaceVariant).multilineTextAlignment(.leading)
                            }
                            .frame(maxWidth: .infinity, alignment: .leading)
                            Text(s.done ? "Again" : "Go").font(.nunito(15, .semibold)).foregroundStyle(Palette.primary)
                        }
                    }
                    .accessibilityElement(children: .combine)
                    .accessibilityValue(s.done ? "Done" : "")
                }
            }
            if !r.messages.isEmpty {
                SectionLabel("From your coach")
                ForEach(r.messages) { msg in
                    PmpCard(container: Palette.warning.opacity(0.07), border: Palette.warning.opacity(0.5)) {
                        Text(msg.text).font(.nunito(14)).fixedSize(horizontal: false, vertical: true)
                        HStack(spacing: 12) {
                            Spacer()
                            Button("Not now") { m.snoozeCoach(msg.id, days: msg.cooldownDays) }.textButton()
                            if let a = msg.action { Button(a.label) { m.coachGo(a, msg.arg) }.textButton() }
                        }
                    }
                }
            }
            Spacer().frame(height: 16)
        }
    }
}

private struct CoachChat: View {
    @EnvironmentObject private var m: AppModel
    @State private var input = ""
    @FocusState private var typing: Bool
    /** The command being made (a blank one) or changed, shown in a sheet. */
    @State private var editing: ChatCommand? = nil

    var body: some View {
        if m.geminiKey.isEmpty {
            CoachKeySetup()
        } else {
            chat
        }
    }

    private var chat: some View {
        let turns = m.store.chat
        return VStack(spacing: 0) {
            ScrollViewReader { proxy in
                ScrollView {
                    LazyVStack(alignment: .leading, spacing: 8) {
                        // One short line on top, so the conversation keeps the room.
                        HStack {
                            Text("Gemini, with your key").font(.nunito(12)).foregroundStyle(Palette.onSurfaceVariant)
                            Spacer()
                            Button { m.forgetKey() } label: { Text("Disconnect key").font(.nunito(12, .semibold)) }.textButton()
                        }
                        if !m.chatNoteClosed {
                            HStack(alignment: .top, spacing: 4) {
                                Text("Every message carries a snapshot of your progress (scores, tasks, mocks, habits), so the coach answers about you. It goes only to Google, with your key.")
                                    .font(.nunito(11)).foregroundStyle(Palette.onSurfaceVariant).fixedSize(horizontal: false, vertical: true)
                                    .frame(maxWidth: .infinity, alignment: .leading)
                                Button { m.closeChatNote() } label: {
                                    Image(systemName: "xmark").font(.system(size: 13, weight: .semibold)).frame(width: 32, height: 32)
                                }
                                .foregroundStyle(Palette.onSurfaceVariant)
                                .accessibilityLabel("Close this note")
                            }
                            .padding(.leading, 10).padding(.vertical, 4)
                            .overlay(RoundedRectangle(cornerRadius: 10).strokeBorder(Palette.outline.opacity(0.5), lineWidth: 1))
                        }
                        if turns.isEmpty {
                            PmpCard { Text("Ask about your progress, a PMP topic, or a question that confused you.").font(.nunito(14)) }
                        }
                        ForEach(Array(turns.enumerated()), id: \.offset) { _, t in CoachBubbleView(mine: t.role == "user", text: t.text) }
                        if m.aiBusy {
                            HStack(spacing: 8) {
                                ProgressView()
                                Text("Thinking…").font(.nunito(13)).foregroundStyle(Palette.onSurfaceVariant)
                            }
                            .padding(8)
                        }
                        if let err = m.aiError { Text(err).font(.nunito(13)).foregroundStyle(Palette.error).padding(8) }
                        if !turns.isEmpty { Button("Clear conversation") { m.store.clearChat() }.textButton() }
                        Color.clear.frame(height: 4).id("chatEnd")
                    }
                    .padding(.horizontal, 12)
                    .padding(.top, 4)
                    .frame(maxWidth: 720)
                    .frame(maxWidth: .infinity)
                }
                .scrollDismissesKeyboard(.interactively)
                .onAppear { proxy.scrollTo("chatEnd", anchor: .bottom) }
                .onChange(of: turns.count) { _, _ in withAnimation { proxy.scrollTo("chatEnd", anchor: .bottom) } }
                .onChange(of: m.aiBusy) { _, _ in withAnimation { proxy.scrollTo("chatEnd", anchor: .bottom) } }
                .onChange(of: typing) { _, _ in withAnimation { proxy.scrollTo("chatEnd", anchor: .bottom) } }
            }
            // "/" opens the commands: the four built in, then the person's own.
            if let found = ChatCommands.matching(input, m.store.chatCommands) {
                CommandMenu(found: found, pick: { input = $0.text }, edit: { editing = $0 },
                            add: { input = ""; editing = ChatCommand("", "", own: true) })
                    .padding(.horizontal, 12)
                    .frame(maxWidth: 720)
            }
            HStack(alignment: .bottom, spacing: 8) {
                TextField("Ask your coach, or type / for commands", text: $input, axis: .vertical)
                    .lineLimit(1...6)
                    .font(.nunito(15))
                    .focused($typing)
                    .padding(12)
                    .background(RoundedRectangle(cornerRadius: 12).fill(Palette.surface))
                    .overlay(RoundedRectangle(cornerRadius: 12).strokeBorder(Palette.outline, lineWidth: 1))
                Button {
                    m.ask(input)
                    input = ""
                } label: {
                    Image(systemName: "paperplane.fill").font(.system(size: 20)).frame(width: 44, height: 44)
                }
                .disabled(input.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty || m.aiBusy)
                .accessibilityLabel("Send")
            }
            .padding(12)
            .frame(maxWidth: 720)
        }
        .onAppear {
            m.checkModels()
            takeDraft()
        }
        .onChange(of: m.coachDraft) { _, _ in takeDraft() }
        .sheet(item: $editing) { c in CommandEditor(command: c.name.isEmpty ? nil : c).environmentObject(m) }
    }

    private func takeDraft() {
        if !m.coachDraft.isEmpty { input = m.coachDraft; m.coachDraft = "" }
    }
}

private struct CommandMenu: View {
    let found: [ChatCommand]
    let pick: (ChatCommand) -> Void
    let edit: (ChatCommand) -> Void
    let add: () -> Void

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 0) {
                ForEach(Array(found.enumerated()), id: \.element.name) { i, c in
                    if i == 0 || c.own != found[i - 1].own {
                        Text(c.own ? "YOURS" : "COMMANDS").font(.nunito(11, .semibold)).kerning(0.6)
                            .foregroundStyle(Palette.onSurfaceVariant).padding(.leading, 12).padding(.top, 6)
                    }
                    HStack(spacing: 10) {
                        Button { pick(c) } label: {
                            HStack(spacing: 10) {
                                Text("/" + c.name).font(.system(size: 13, design: .monospaced)).foregroundStyle(Palette.primary)
                                Text(c.label).font(.nunito(13)).foregroundStyle(Palette.onSurfaceVariant).lineLimit(1)
                                    .frame(maxWidth: .infinity, alignment: .leading)
                            }
                            .frame(minHeight: 40)
                            .contentShape(Rectangle())
                        }
                        .buttonStyle(.plain)
                        if c.own {
                            Button { edit(c) } label: { Image(systemName: "pencil").frame(width: 36, height: 36) }
                                .foregroundStyle(Palette.onSurfaceVariant)
                                .accessibilityLabel("Edit /\(c.name)")
                        }
                    }
                    .padding(.horizontal, 12)
                }
                Button(action: add) {
                    Text("+ New command").font(.nunito(13, .bold)).foregroundStyle(Palette.primary)
                        .frame(maxWidth: .infinity, minHeight: 40, alignment: .leading).contentShape(Rectangle())
                }
                .buttonStyle(.plain)
                .padding(.horizontal, 12)
            }
            .padding(.vertical, 4)
        }
        .frame(maxHeight: 260)
        .fixedSize(horizontal: false, vertical: true)
        .background(RoundedRectangle(cornerRadius: 12).fill(Palette.surface).shadow(color: .black.opacity(0.15), radius: 8, y: 4))
        .overlay(RoundedRectangle(cornerRadius: 12).strokeBorder(Palette.outline.opacity(0.5), lineWidth: 1))
    }
}

/** Makes one of the person's own commands, or changes or deletes `command`. */
private struct CommandEditor: View {
    @EnvironmentObject private var m: AppModel
    @Environment(\.dismiss) private var dismiss
    let command: ChatCommand?
    @State private var name = ""
    @State private var text = ""
    @State private var error: String? = nil

    var body: some View {
        NavigationStack {
            Form {
                if let error { Text(error).font(.nunito(13)).foregroundStyle(Palette.error) }
                Section("Name, typed after /") {
                    TextField("agile", text: $name).textInputAutocapitalization(.never).autocorrectionDisabled()
                }
                Section {
                    TextField("Ask me three hard agile questions, one at a time, and mark each answer.", text: $text, axis: .vertical).lineLimit(3...8)
                } header: { Text("Message it sends") } footer: {
                    Text("Your commands travel with your progress, so they are on your other devices after a sync.")
                }
                if let command {
                    Button("Delete", role: .destructive) { m.deleteChatCommand(command.name); dismiss() }
                }
            }
            .navigationTitle(command.map { "Edit /\($0.name)" } ?? "New command")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .cancellationAction) { Button("Cancel") { dismiss() } }
                ToolbarItem(placement: .confirmationAction) {
                    Button("Save") {
                        error = m.saveChatCommand(old: command?.name, name: name, text: text)
                        if error == nil { dismiss() }
                    }
                }
            }
        }
        .onAppear { name = command?.name ?? ""; text = command?.text ?? "" }
    }
}

private struct CoachBubbleView: View {
    let mine: Bool
    let text: String
    var body: some View {
        HStack {
            if mine { Spacer(minLength: 40) }
            SwiftUI.Group {
                if mine {
                    Text(text).font(.nunito(15)).foregroundStyle(Palette.onPrimary).textSelection(.enabled)
                } else {
                    MarkdownText(text: text)
                }
            }
            .padding(12)
            .background(RoundedRectangle(cornerRadius: 14).fill(mine ? Palette.primary : Palette.surface))
            .overlay(RoundedRectangle(cornerRadius: 14).strokeBorder(mine ? Color.clear : Palette.outline.opacity(0.4), lineWidth: 1))
            .frame(maxWidth: 340, alignment: mine ? .trailing : .leading)
            if !mine { Spacer(minLength: 40) }
        }
        .frame(maxWidth: .infinity, alignment: mine ? .trailing : .leading)
        .accessibilityElement(children: .combine)
        .accessibilityLabel((mine ? "You: " : "Coach: ") + text)
    }
}

private struct CoachKeySetup: View {
    @EnvironmentObject private var m: AppModel
    @State private var key = ""

    var body: some View {
        ScreenScroll {
            Text("Connect Gemini").font(.nunito(22, .heavy))
            Text("The coach's chat runs on Google's newest Gemini Flash (\(String(format: "%.1f", Gemini.BASE_VERSION)) or later) with your own API key; the app keeps up with new models by itself. It reads a snapshot of your progress with each message, so it can tell you how you're doing and what to improve, and it answers your PMP questions.")
                .font(.nunito(14)).fixedSize(horizontal: false, vertical: true)
            PmpCard {
                SectionLabel("Get a free key")
                BulletList(items: ["Open Google AI Studio and sign in.", "Create an API key.", "Copy it and paste it below."]).padding(.top, 4)
                Link("Open aistudio.google.com/apikey", destination: URL(string: "https://aistudio.google.com/apikey")!)
                    .font(.nunito(15, .semibold)).foregroundStyle(Palette.primary).padding(.vertical, 8)
            }
            SecureField("Gemini API key", text: $key)
                .textContentType(.password)
                .autocorrectionDisabled()
                .textInputAutocapitalization(.never)
                .font(.nunito(15))
                .padding(12)
                .frame(minHeight: 48)
                .background(RoundedRectangle(cornerRadius: 10).fill(Palette.surface))
                .overlay(RoundedRectangle(cornerRadius: 10).strokeBorder(Palette.outline, lineWidth: 1))
                .submitLabel(.done)
                .onSubmit { m.useKey(key) }
            Button { m.useKey(key) } label: { Text(m.aiBusy ? "Checking the key…" : "Use this key").frame(maxWidth: .infinity) }
                .primaryButton()
                .disabled(key.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty || m.aiBusy)
            if let err = m.aiError { Text(err).font(.nunito(13)).foregroundStyle(Palette.error).fixedSize(horizontal: false, vertical: true) }
            Text("The key stays on this phone, in the iPhone Keychain. It is never in a progress backup, and it is sent only to Google, in a request header.")
                .font(.nunito(12)).foregroundStyle(Palette.onSurfaceVariant).fixedSize(horizontal: false, vertical: true)
            Button { m.coachTab = 0 } label: { Text("The Today tab works without a key.").font(.nunito(12)) }
                .textButton(Palette.onSurfaceVariant)
        }
    }
}
