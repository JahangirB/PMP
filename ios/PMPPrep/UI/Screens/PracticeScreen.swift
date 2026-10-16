import SwiftUI

struct PracticeScreen: View {
    @EnvironmentObject private var m: AppModel

    var body: some View {
        if let s = m.practice {
            PSHost(session: s).id(ObjectIdentifier(s))
        } else {
            PSSetup()
        }
    }
}

/** Watches the session so the screen moves to the results when it finishes. */
private struct PSHost: View {
    @EnvironmentObject private var m: AppModel
    @ObservedObject var session: QuizSession

    var body: some View {
        if session.finished {
            PSResults(session: session, onAgain: { m.practice = nil })
        } else {
            QuizRunner(session: session, onFinish: { m.finishQuiz(session) })
        }
    }
}

/* ── setup ──────────────────────────────────────────────────────────────── */

private struct PSSetup: View {
    @EnvironmentObject private var m: AppModel
    private let counts = [5, 10, 20, 40, 60, 100]

    private func change(_ edit: (inout PracticeFilter) -> Void) {
        var f = m.filter
        edit(&f)
        m.filter = f
    }

    private var domainOptions: [(Domain?, String)] {
        [(nil, "All domains")] + Domain.allCases.map { d -> (Domain?, String) in (d, d.title) }
    }

    private var taskOptions: [(String?, String)] {
        let f = m.filter
        let tasks: [EcoTask] = m.repo.extras.tasks.filter { f.domain == nil || $0.domain == f.domain }
        return [(nil, "All tasks")] + tasks.map { t -> (String?, String) in (t.id, "\(t.code) · \(t.label)") }
    }

    private var packOptions: [(String?, String)] {
        let packs = m.repo.packs.filter { m.repo.isEnabled($0) && !$0.questions.isEmpty }
        return [(nil, "All enabled banks")] + packs.map { p -> (String?, String) in (p.id, "\(p.name) (\(p.questions.count))") }
    }

    private var typeOptions: [(QType?, String)] {
        [(nil, "Any type"), (.single, "Multiple choice"), (.multi, "Multiple response"), (.matching, "Matching / drop-down"),
         (.hotspot, "Hotspot"), (.fillblank, "Fill in the blank")]
    }

    private var approachOptions: [(Approach?, String)] {
        [(nil, "Any approach")] + Approach.allCases.map { a -> (Approach?, String) in (a, a.title) }
    }

    private var whichOptions: [(PracticeFilter.Which, String)] {
        PracticeFilter.Which.allCases.map { ($0, $0.title) }
    }

    private var modeOptions: [(Bool, String)] {
        [(false, "Practice with feedback"), (true, "Adaptive: lean towards my weak tasks")]
    }

    var body: some View {
        let f = m.filter
        let matches = m.matching(f).count
        let missed = m.store.last.values.filter { !$0.correct }.count
        let flagged = m.store.flags.count
        let reviews = m.reviewDueCount()
        let queued: Int = m.queuedQuestions().count
        VStack(spacing: 0) {
            ScreenScroll {
                ScreenTitle(title: "Build a drill",
                            subtitle: "You see whether you were right, and why every option is right or wrong, after each answer.")

                // Quick starts first: the drills people run most need no form at all.
                SectionLabel("Quick start")
                FlowLayout(spacing: 8) {
                    if reviews > 0 {
                        Button("\(reviews) missed, due for review") { m.startReview() }.primaryButton()
                    }
                    if queued > 0 {
                        Button("From your last mock (\(queued))") { m.startQueue() }.primaryButton()
                    }
                    Button("10 mixed") { m.startPractice(PracticeFilter(count: 10), title: "Mixed practice") }.outlinedButton()
                    Button("10 adaptive") { m.startPractice(PracticeFilter(count: 10, adaptive: true), title: "Adaptive drill") }.outlinedButton()
                    Button("20 new") { m.startPractice(PracticeFilter(which: .NEW, count: 20), title: "New questions") }.outlinedButton()
                    Button("Redo \(missed) mistakes") { m.startPractice(PracticeFilter(which: .MISSED, count: 40), title: "Mistakes") }
                        .outlinedButton().disabled(missed == 0)
                    Button("\(flagged) flagged") { m.startPractice(PracticeFilter(which: .FLAGGED, count: 40), title: "Flagged") }
                        .outlinedButton().disabled(flagged == 0)
                }

                HStack {
                    SectionLabel("Or choose exactly")
                    Spacer()
                    Button("Reset filters") { m.filter = PracticeFilter() }.textButton()
                }
                .padding(.top, 4)

                filters(f)

                Text("How many").font(.nunito(12, .semibold)).foregroundStyle(Palette.onSurfaceVariant)
                FlowLayout(spacing: 8) {
                    ForEach(counts, id: \.self) { n in
                        Chip(label: "\(n)", selected: f.count == n) { change { $0.count = n } }
                    }
                }
                Spacer().frame(height: 8)
            }
            // The primary action stays in the thumb zone instead of below the fold.
            BottomBar {
                Text(matches == 0 ? "No questions match" : "\(matches) match\nyou'll get \(min(matches, f.count))")
                    .font(.nunito(13, .semibold))
                    .foregroundStyle(matches == 0 ? Palette.error : Palette.onSurfaceVariant)
                    .frame(maxWidth: .infinity, alignment: .leading)
                Button { m.startPractice() } label: { Text("Start practice") }
                    .primaryButton()
                    .disabled(matches == 0)
            }
        }
    }

    @ViewBuilder private func filters(_ f: PracticeFilter) -> some View {
        ChoiceSelector(label: "Domain", options: domainOptions, selected: f.domain) { d in
            change { nf in
                nf.domain = d
                if let d, m.repo.extras.tasks.first(where: { $0.id == nf.task })?.domain != d { nf.task = nil }
            }
        }
        ChoiceSelector(label: "ECO task", options: taskOptions, selected: f.task) { t in change { $0.task = t } }
        ChoiceSelector(label: "Question bank", options: packOptions, selected: f.pack) { p in change { $0.pack = p } }
        ChoiceSelector(label: "Question type", options: typeOptions, selected: f.type) { t in change { $0.type = t } }
        ChoiceSelector(label: "Approach", options: approachOptions, selected: f.approach) { a in change { $0.approach = a } }
        ChoiceSelector(label: "Which questions", options: whichOptions, selected: f.which) { w in change { $0.which = w } }
        ChoiceSelector(label: "Mode", options: modeOptions, selected: f.adaptive) { a in change { $0.adaptive = a } }
        if f.adaptive {
            Text("Questions are drawn towards the ECO tasks you are weakest on, with unseen ones favoured and ones you got right last time held back.")
                .font(.nunito(12)).foregroundStyle(Palette.onSurfaceVariant)
                .fixedSize(horizontal: false, vertical: true)
        }
    }
}

/* ── the drill ──────────────────────────────────────────────────────────── */

private enum PSToolKind { case card, explain }
private struct PSTool: Identifiable {
    let kind: PSToolKind
    let q: Question
    var id: String { "\(kind)-\(q.id)" }
}

private let psExplainPrompt = "Why is the right answer right, and why was your choice wrong? Write it as if teaching a friend."

/** Answer, check, see why, move on. Also plays guided-session drills and reviews of a finished mock. */
struct QuizRunner: View {
    @EnvironmentObject private var m: AppModel
    @ObservedObject private var s: QuizSession
    private let onFinish: () -> Void

    @Environment(\.reading) private var reading
    @State private var now: Millis = Time.now()
    @State private var noteOpen = false
    @State private var noteText = ""
    @State private var tool: PSTool? = nil
    @State private var calmed: Set<String> = []

    init(session: QuizSession, onFinish: @escaping () -> Void) {
        _s = ObservedObject(wrappedValue: session)
        self.onFinish = onFinish
    }

    var body: some View {
        let q = s.current
        let result = s.results[q.id]
        VStack(spacing: 0) {
            header(q)
            ScrollViewReader { proxy in
                ScrollView {
                    VStack(alignment: .leading, spacing: 0) {
                        Color.clear.frame(height: 0).id("psTop")
                        content(q, result)
                    }
                    .padding(16)
                    .frame(maxWidth: 720)
                    .frame(maxWidth: .infinity)
                }
                .scrollDismissesKeyboard(.interactively)
                .onChange(of: s.index) { proxy.scrollTo("psTop", anchor: .top) }
            }
            bottomBar(q, revealed: result != nil)
        }
        .task(id: "\(q.id)|\(result != nil)") {
            // The clock ticks each second while the question is open.
            now = Time.now()
            if result != nil || s.reviewOnly { return }
            while !Task.isCancelled {
                try? await Task.sleep(nanoseconds: 1_000_000_000)
                if Task.isCancelled { return }
                now = Time.now()
            }
        }
        .sheet(item: $tool) { t in
            Group {
                switch t.kind {
                case .card: MakeCardSheet(q: t.q) { tool = nil }
                case .explain: ExplainBackSheet(topic: questionTopic(t.q), prompt: psExplainPrompt) { tool = nil }
                }
            }
            .environmentObject(m)
            .environment(\.reading, reading)
        }
        .sheet(isPresented: $noteOpen) {
            PSNoteSheet(text: $noteText, onSave: {
                m.store.setNote(q.id, noteText)
                noteOpen = false
            }, onCancel: { noteOpen = false })
        }
    }

    /* header */

    private func header(_ q: Question) -> some View {
        let flagged = m.store.flags.contains(q.id)
        let hasNote = m.store.notes[q.id] != nil
        return VStack(spacing: 4) {
            HStack(spacing: 0) {
                VStack(alignment: .leading, spacing: 1) {
                    HStack(spacing: 8) {
                        Text("Question \(s.index + 1) of \(s.questions.count)").font(.nunito(15, .bold)).lineLimit(1)
                        if !s.reviewOnly && !s.diagnostic {
                            Text("\(s.correctCount)/\(s.checkedCount) right")
                                .font(.system(size: 12, design: .monospaced))
                                .foregroundStyle(Palette.onSurfaceVariant)
                                .lineLimit(1)
                        }
                    }
                    if s.diagnostic {
                        Text("Diagnostic · no feedback until the end").font(.nunito(11)).foregroundStyle(Palette.primary)
                    }
                }
                Spacer(minLength: 4)
                if !s.diagnostic {
                    Button { m.askAbout(q, s) } label: {
                        Image(systemName: "brain.head.profile").font(.system(size: 18)).foregroundStyle(Palette.primary)
                            .frame(width: 44, height: 44)
                    }
                    .accessibilityLabel("Ask the coach about this question")
                }
                Button {
                    noteText = m.store.notes[q.id] ?? ""
                    noteOpen = true
                } label: {
                    Image(systemName: hasNote ? "note.text" : "square.and.pencil").font(.system(size: 18))
                        .foregroundStyle(hasNote ? Palette.primary : Palette.onSurfaceVariant)
                        .frame(width: 44, height: 44)
                }
                .accessibilityLabel(hasNote ? "Edit note" : "Note")
                Button { m.store.toggleFlag(q.id) } label: {
                    Image(systemName: flagged ? "flag.fill" : "flag").font(.system(size: 18))
                        .foregroundStyle(flagged ? Palette.warning : Palette.onSurfaceVariant)
                        .frame(width: 44, height: 44)
                }
                .accessibilityLabel("Flag")
                .accessibilityAddTraits(flagged ? .isSelected : [])
            }
            Bar(value: Double(s.index + 1) / Double(max(s.questions.count, 1)))
        }
        .padding(.horizontal, 16)
        .padding(.vertical, 6)
        .background(Palette.surface)
    }

    /* the question */

    @ViewBuilder private func content(_ q: Question, _ result: ScoreResult?) -> some View {
        let revealed = result != nil
        if !revealed && !s.reviewOnly {
            PacingBar(secondsSpent: max(0, (now - s.shownAt - s.awayMs) / 1000), targetSeconds: m.repo.extras.practicePaceSec)
                .padding(.bottom, 12)
        }
        // Long sittings: a checkpoint at questions 60 and 120, so the clock feels known rather than looming.
        if !s.reviewOnly && !s.diagnostic && s.questions.count >= 60 && (s.index == 60 || s.index == 120) && !revealed {
            let used = Int((now - s.startedAt - s.awayTotal) / 60_000)
            let exam = s.index * 76 / 60
            PmpCard(container: Palette.primaryContainer) {
                Text("Checkpoint: \(s.index) questions done").font(.nunito(15, .bold))
                Text(used <= exam ? "\(used) minutes so far; exam pace is \(exam). You're on pace."
                                  : "\(used) minutes so far; exam pace is \(exam). Take the next ones at your usual speed, not faster.")
                    .font(.nunito(13))
                    .fixedSize(horizontal: false, vertical: true)
            }
            .padding(.bottom, 10)
        }
        // A long stop on a question in practice (never in mocks or the diagnostic): one gentle prompt.
        if !revealed && !s.reviewOnly && !s.diagnostic && !calmed.contains(q.id)
            && now - s.lastTouchAt - (s.awayMs - s.awayAtLastTouch) > m.freezePromptMs() {
            PmpCard(container: Palette.secondaryContainer) {
                Text("Take a breath.").font(.nunito(15, .bold))
                Text("Breathe out slowly. Read the last sentence first: what is it asking? Then rule out two options.")
                    .font(.nunito(13)).lineSpacing(3)
                    .fixedSize(horizontal: false, vertical: true)
                Button("OK") { calmed.insert(q.id) }.textButton()
            }
            .padding(.bottom, 10)
        }
        QuestionHeader(q: q, taskLabel: m.repo.task(q.ecoTask).map { "\($0.code) · \($0.label)" },
                       highlights: s.marks[q.id], onHighlights: s.reviewOnly ? nil : { marks in s.marks[q.id] = marks })
        if !s.optionsShown && !revealed {
            // Reading drill: the scenario alone first.
            Text("Before the options: what is this question asking you to decide?")
                .font(.nunito(14, .bold)).foregroundStyle(Palette.primary)
                .fixedSize(horizontal: false, vertical: true)
                .padding(.bottom, 8)
            Button { m.showOptions(s) } label: { Text("I know what it's asking: show the options").frame(maxWidth: .infinity) }
                .primaryButton()
        } else {
            QuestionBody(q: q, options: s.order[q.id] ?? q.options, answer: s.answers[q.id], revealed: revealed,
                         struck: s.struck[q.id] ?? [],
                         onStrike: revealed || s.reviewOnly ? nil : { id in m.toggleStrike(s, q, id) },
                         onAnswer: { a in m.answer(s, q, a) })
            if m.hintsAvailable(s) {
                PSHints(s: s, q: q)
            }
            if !revealed && !s.reviewOnly && s.index < 2 && (s.struck[q.id] ?? []).isEmpty && (s.marks[q.id] ?? []).isEmpty {
                Text("Tip: long-press an option to cross it out; long-press words in the question to highlight them.")
                    .font(.nunito(12)).foregroundStyle(Palette.onSurfaceVariant)
                    .fixedSize(horizontal: false, vertical: true)
                    .padding(.top, 8)
            }
        }
        if let result {
            afterCheck(q, result).padding(.top, 14)
        }
        Spacer().frame(height: 24)
    }

    @ViewBuilder private func afterCheck(_ q: Question, _ result: ScoreResult) -> some View {
        let conf = s.confidence[q.id]
        VStack(alignment: .leading, spacing: 10) {
            if conf == Attempt.SURE && !result.correct {
                PmpCard(container: Palette.error.opacity(0.10), border: Palette.error) {
                    Text("You were sure, and it was wrong.").font(.nunito(15, .bold))
                    Text("These are the ones to catch before exam day. It's at the top of your mistake journal: tag why it went wrong.")
                        .font(.nunito(13)).lineSpacing(3).fixedSize(horizontal: false, vertical: true)
                }
            } else if conf == Attempt.GUESS && result.correct {
                PmpCard(container: Palette.secondaryContainer) {
                    Text("Right, but a guess.").font(.nunito(15, .bold))
                    Text("It won't count toward mastery. Read the why below so next time you know it.")
                        .font(.nunito(13)).lineSpacing(3).fixedSize(horizontal: false, vertical: true)
                }
            }
            Feedback(q: q, result: result, answered: Scoring.isAnswered(q, s.answers[q.id]))
            if !result.correct {
                let made = m.store.myCards.contains { $0.id == "my:\(q.id)" }
                HStack(spacing: 8) {
                    Button { tool = PSTool(kind: .card, q: q) } label: { Text(made ? "Edit my card" : "Make a card").frame(maxWidth: .infinity) }
                        .outlinedButton()
                    Button { tool = PSTool(kind: .explain, q: q) } label: { Text("Explain it back").frame(maxWidth: .infinity) }
                        .outlinedButton()
                }
            }
            if let note = m.store.notes[q.id] {
                PmpCard {
                    SectionLabel("Your note")
                    Text(note).font(.nunito(14)).fixedSize(horizontal: false, vertical: true).padding(.top, 2)
                }
            }
            let hist = m.store.attempts.filter { $0.qid == q.id }
            if !hist.isEmpty {
                Text("Answered \(hist.count) time(s), \(hist.filter(\.correct).count) right · \(q.id)")
                    .font(.nunito(12)).foregroundStyle(Palette.onSurfaceVariant)
            }
        }
    }

    /* the bar at the bottom */

    @ViewBuilder private func bottomBar(_ q: Question, revealed: Bool) -> some View {
        let last = s.index == s.questions.count - 1
        let answered = Scoring.isAnswered(q, s.answers[q.id])
        let confidence = !s.diagnostic && !revealed && m.askConfidence && !s.reviewOnly && answered
        BottomBar {
            if confidence {
                Button { m.moveTo(s, s.index - 1) } label: {
                    Image(systemName: "chevron.left").font(.system(size: 16, weight: .bold))
                }
                .outlinedButton()
                .disabled(s.index == 0)
                .accessibilityLabel("Back")
                // One tap says how sure you are and checks the answer.
                VStack(alignment: .leading, spacing: 4) {
                    Text("How sure are you? Tap to check.").font(.nunito(12)).foregroundStyle(Palette.onSurfaceVariant)
                    HStack(spacing: 6) {
                        PSConfidenceButton(label: Rules.current.confidenceLabel(Attempt.SURE)) { m.check(s, confidence: Attempt.SURE) }
                        PSConfidenceButton(label: Rules.current.confidenceLabel(Attempt.THINK)) { m.check(s, confidence: Attempt.THINK) }
                        PSConfidenceButton(label: Rules.current.confidenceLabel(Attempt.GUESS)) { m.check(s, confidence: Attempt.GUESS) }
                    }
                }
                .frame(maxWidth: .infinity)
            } else {
                Button { m.moveTo(s, s.index - 1) } label: { Text("Back") }
                    .outlinedButton()
                    .disabled(s.index == 0)
                if s.diagnostic {
                    Button {
                        if last { m.finishDiagnostic(s) } else { m.moveTo(s, s.index + 1) }
                    } label: {
                        Text(last ? "Finish diagnostic" : (answered ? "Next" : "Skip")).frame(maxWidth: .infinity)
                    }
                    .primaryButton()
                } else if revealed {
                    Button {
                        if last { onFinish() } else { m.moveTo(s, s.index + 1) }
                    } label: {
                        Text(last ? "See results" : "Next").frame(maxWidth: .infinity)
                    }
                    .primaryButton()
                } else {
                    if !last {
                        Button("Skip") { m.moveTo(s, s.index + 1) }.textButton().padding(.horizontal, 6)
                    }
                    Button { m.check(s) } label: { Text("Check answer").frame(maxWidth: .infinity) }
                        .primaryButton()
                        .disabled(!answered)
                }
            }
        }
    }
}

/** The hint ladder: a button that shows the next rung, and the rungs shown so far. */
private struct PSHints: View {
    @EnvironmentObject private var m: AppModel
    @ObservedObject var s: QuizSession
    let q: Question

    var body: some View {
        let used: Int = s.hints[q.id] ?? 0
        let shown: [HintLadder.Hint] = s.hintsShown[q.id] ?? []
        let checked: Bool = s.isChecked(q)
        let left: Int = HintLadder.RUNGS - used
        VStack(alignment: .leading, spacing: 8) {
            if !checked {
                Button { m.nextHint(s, q) } label: {
                    Label(used == 0 ? "Hint" : (left > 0 ? "Another hint" : "No more hints"), systemImage: "lightbulb")
                }
                .outlinedButton()
                .disabled(left <= 0)
                .accessibilityHint(used == 0 ? "Shows a nudge without giving the answer away" : "")
            }
            if !shown.isEmpty {
                Text(HintLadder.NOTE)
                    .font(.nunito(12)).foregroundStyle(Palette.onSurfaceVariant)
                    .fixedSize(horizontal: false, vertical: true)
                ForEach(shown.indices, id: \.self) { i in
                    PSHintCard(hint: shown[i])
                }
            }
        }
        .frame(maxWidth: .infinity, alignment: .leading)
        .padding(.top, 12)
    }
}

private struct PSHintCard: View {
    let hint: HintLadder.Hint

    var body: some View {
        PmpCard(container: Palette.tertiaryContainer.opacity(0.55)) {
            Text("HINT \(hint.rung) OF \(HintLadder.RUNGS)")
                .font(.nunito(11, .bold)).tracking(0.8).foregroundStyle(Palette.onTertiaryContainer)
            Text(hint.text).font(.nunito(14, .bold)).fixedSize(horizontal: false, vertical: true)
            if let d = hint.detail {
                Text(d).font(.nunito(14)).lineSpacing(3).fixedSize(horizontal: false, vertical: true)
            }
        }
        .accessibilityElement(children: .combine)
    }
}

/** A compact filled button for the three confidence choices. */
private struct PSConfidenceButton: View {
    let label: String
    let action: () -> Void
    var body: some View {
        Button(action: action) {
            Text(label)
                .font(.nunito(14, .bold))
                .lineLimit(1)
                .minimumScaleFactor(0.75)
                .foregroundStyle(Palette.onPrimary)
                .padding(.horizontal, 6)
                .frame(maxWidth: .infinity, minHeight: 44)
                .background(Capsule().fill(Palette.primary))
                .contentShape(Capsule())
        }
        .buttonStyle(LiftStyle())
    }
}

private struct PSNoteSheet: View {
    @Binding var text: String
    let onSave: () -> Void
    let onCancel: () -> Void
    @FocusState private var focused: Bool

    init(text: Binding<String>, onSave: @escaping () -> Void, onCancel: @escaping () -> Void) {
        _text = text
        self.onSave = onSave
        self.onCancel = onCancel
    }

    var body: some View {
        NavigationStack {
            ScrollView {
                TextField("", text: $text, axis: .vertical)
                    .font(.nunito(15))
                    .lineLimit(3...)
                    .focused($focused)
                    .padding(12)
                    .background(RoundedRectangle(cornerRadius: 10, style: .continuous).fill(Palette.surface))
                    .overlay(RoundedRectangle(cornerRadius: 10, style: .continuous).strokeBorder(Palette.outline, lineWidth: 1))
                    .accessibilityLabel("Note")
                    .padding(16)
            }
            .background(Palette.background.ignoresSafeArea())
            .navigationTitle("Note on this question")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .cancellationAction) { Button("Cancel", action: onCancel) }
                ToolbarItem(placement: .confirmationAction) { Button("Save", action: onSave) }
            }
        }
        .presentationDetents([.medium, .large])
        .foregroundStyle(Palette.onSurface)
        .tint(Palette.primary)
        .onAppear { focused = true }
    }
}

/* ── results ────────────────────────────────────────────────────────────── */

private struct PSResults: View {
    @EnvironmentObject private var m: AppModel
    @ObservedObject var session: QuizSession
    let onAgain: () -> Void

    var body: some View {
        if session.diagnostic {
            PSDiagnosticResults(session: session)
        } else {
            summary
        }
    }

    private var summary: some View {
        let s = session
        let checked = s.questions.filter { s.isChecked($0) }
        let right = checked.filter { s.results[$0.id]?.correct == true }.count
        let score = checked.isEmpty ? 0 : Double(right) / Double(checked.count)
        let wrong = checked.filter { s.results[$0.id]?.correct != true }
        return ScreenScroll {
            PmpCard {
                Text(pct(score)).font(.nunito(44, .black))
                    .foregroundStyle(score >= 0.7 ? Palette.success : (score >= 0.6 ? Palette.warning : Palette.error))
                Text("\(right) of \(checked.count) checked answers correct").font(.nunito(15)).foregroundStyle(Palette.onSurfaceVariant)
            }
            if s.totalRightToWrong + s.totalWrongToRight > 0 {
                PmpCard {
                    Text("Changed answers: \(s.totalWrongToRight) wrong → right, \(s.totalRightToWrong) right → wrong.")
                        .font(.nunito(14, .bold)).fixedSize(horizontal: false, vertical: true)
                    if s.totalRightToWrong > s.totalWrongToRight {
                        Text("Your first instinct was right more often than your changes.").font(.nunito(13))
                    }
                }
            }
            if let e = m.eliminationEffect() {
                PmpCard {
                    Text("Crossing options out: \(pct(e.0)) right when you did, \(pct(e.1)) when you didn't.")
                        .font(.nunito(14, .bold)).fixedSize(horizontal: false, vertical: true)
                    Text(e.0 > e.1 + 0.03 ? "Ruling out two options first is working for you. Keep doing it on long questions."
                                          : "No clear gain yet. On situational questions, try ruling out the two weakest options before choosing.")
                        .font(.nunito(13)).lineSpacing(3).fixedSize(horizontal: false, vertical: true)
                }
            }
            PmpCard {
                SectionLabel("By domain").padding(.bottom, 6)
                ForEach(Domain.allCases) { d in
                    let inD = checked.filter { $0.domain == d }
                    if !inD.isEmpty {
                        let r = inD.filter { s.results[$0.id]?.correct == true }.count
                        ProgressRow(label: d.title, value: Double(r) / Double(inD.count), right: "\(r)/\(inD.count)", color: Palette.domain(d))
                            .padding(.bottom, 8)
                    }
                }
            }
            if !wrong.isEmpty {
                Button { m.startPracticeWith(wrong, title: "Retry the ones I missed") } label: {
                    Text("Retry the \(wrong.count) I missed").frame(maxWidth: .infinity)
                }
                .primaryButton()
            }
            Button(action: onAgain) { Text("New drill").frame(maxWidth: .infinity) }
                .outlinedButton()
        }
    }
}

private struct PSDiagnosticResults: View {
    @EnvironmentObject private var m: AppModel
    @ObservedObject var session: QuizSession
    @State private var review: QuizSession? = nil

    var body: some View {
        if let review {
            QuizRunner(session: review, onFinish: { self.review = nil })
        } else if let d = m.store.diagnostic {
            ScreenScroll {
                ScreenTitle(title: "Your starting point")
                PmpCard {
                    Text(pct(d.pct)).font(.nunito(44, .black)).foregroundStyle(Palette.primary)
                    Text("\(d.right) of \(d.n) right, taken cold.").font(.nunito(15)).foregroundStyle(Palette.onSurfaceVariant)
                }
                PmpCard {
                    SectionLabel("By domain").padding(.bottom, 6)
                    ForEach(Domain.allCases) { dm in
                        if let v = d.domains[dm], v.count >= 2 {
                            ProgressRow(label: dm.title, value: v[1] == 0 ? 0 : Double(v[0]) / Double(v[1]), right: "\(v[0])/\(v[1])",
                                        color: Palette.domain(dm))
                                .padding(.bottom, 8)
                        }
                    }
                    if let weakest = Domain.allCases.min(by: { (d.domainPct($0) ?? 1) < (d.domainPct($1) ?? 1) }) {
                        Text("Your plan will give \(weakest.title) the most practice time.").font(.nunito(13))
                    }
                }
                Button {
                    m.practice = nil
                    m.home()
                } label: { Text("See my study path").frame(maxWidth: .infinity, minHeight: 26) }
                    .primaryButton()
                Button {
                    review = QuizSession(title: "Diagnostic review", questions: session.questions, reviewOnly: true,
                                         givenAnswers: session.answers, givenOrder: session.order)
                } label: { Text("Review the answers").frame(maxWidth: .infinity) }
                    .outlinedButton()
                Button {
                    m.practice = nil
                    m.coachTab = 0
                    m.navigate(.COACH)
                } label: { Text("See what the coach suggests").frame(maxWidth: .infinity) }
                    .outlinedButton()
            }
        } else {
            EmptyNote(text: "No diagnostic result yet.")
        }
    }
}
