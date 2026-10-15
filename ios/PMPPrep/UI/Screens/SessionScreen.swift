import SwiftUI

/** Plays one guided-path session: one step at a time, one button forward. */
struct SessionScreen: View {
    @EnvironmentObject private var m: AppModel

    var body: some View {
        if let r = m.run {
            SessionPlayer(r: r).id(ObjectIdentifier(r))
        } else {
            Color.clear.onAppear { m.back() }
        }
    }
}

private func stepName(_ k: StepKind?) -> String {
    switch k {
    case .CARDS?: return "Warm-up cards"
    case .LESSON?: return "Lesson"
    case .PRACTICE?: return "Practice"
    case .REFLECT?: return "Reflect"
    case .SCENARIOS?: return "Scenarios"
    case .MOCK?: return "Mock exam"
    case .DEBRIEF?, nil: return "Debrief"
    }
}

private struct SessionPlayer: View {
    @EnvironmentObject private var m: AppModel
    @ObservedObject var r: PathRun
    @State private var offer = false
    @State private var breathing = false

    var body: some View {
        let s = r.session
        let st: Step? = r.step < s.steps.count ? s.steps[r.step] : nil
        VStack(spacing: 0) {
            VStack(alignment: .leading, spacing: 6) {
                HStack(spacing: 4) {
                    Button { m.closeSession() } label: {
                        Image(systemName: "xmark").font(.system(size: 18, weight: .semibold)).frame(width: 44, height: 44)
                    }
                    .foregroundStyle(Palette.onSurface)
                    .accessibilityLabel("Close; your place is kept")
                    VStack(alignment: .leading, spacing: 1) {
                        Text(s.title).font(.nunito(16, .bold)).lineLimit(1)
                        Text(stepName(st?.kind) + " · step \(min(r.step + 1, s.steps.count)) of \(s.steps.count)")
                            .font(.nunito(12)).foregroundStyle(Palette.onSurfaceVariant)
                    }
                    Spacer(minLength: 0)
                }
                Bar(value: Double(r.step + 1) / Double(max(s.steps.count, 1))).padding(.leading, 12)
            }
            .padding(.leading, 4).padding(.trailing, 16).padding(.top, 4).padding(.bottom, 6)
            .background(Palette.surface)

            content(st)
                .frame(maxWidth: .infinity, maxHeight: .infinity)
        }
        .task(id: r.step) { m.prepareStep(r) }
    }

    @ViewBuilder private func content(_ st: Step?) -> some View {
        // Once a day, before the first session: how do you feel about studying right now? (optional)
        if !r.checkedIn && r.step == 0 {
            ScrollView {
                CheckInRow(question: "How do you feel about studying right now?", onPick: { v in
                    m.recordCheckIn("pre", v)
                    r.checkedIn = true
                    offer = v <= 2
                }, onSkip: { r.checkedIn = true })
                .padding(16)
            }
        } else if breathing {
            BreathingReset { breathing = false }
        } else if offer {
            ScrollView {
                PmpCard {
                    Text("Want a two-minute reset first?").font(.nunito(16, .bold))
                    Text("Slow breathing before you start makes the first questions easier to read. Optional.").font(.nunito(13))
                    HStack(spacing: 8) {
                        Button { offer = false; breathing = true } label: { Text("Breathe for 2 minutes").frame(maxWidth: .infinity) }
                            .primaryButton()
                        Button { offer = false } label: { Text("Skip").frame(maxWidth: .infinity) }
                            .outlinedButton()
                    }
                    .padding(.top, 8)
                }
                .padding(16)
            }
        } else {
            switch st?.kind {
            case .CARDS?:
                if m.cardQueue != nil {
                    CardSession(onDone: { m.cardsDone(r) })
                } else if let rv = r.review {
                    QuizRunner(session: rv, onFinish: { m.nextStep(r) }).id(ObjectIdentifier(rv))
                } else {
                    Color.clear
                }
            case .LESSON?:
                LessonStep(r: r)
            case .PRACTICE?:
                if let q = r.current {
                    QuizRunner(session: q, onFinish: { m.nextStep(r) }).id(ObjectIdentifier(q))
                } else {
                    Color.clear
                }
            case .REFLECT?:
                ReflectStep(r: r)
            case .SCENARIOS?:
                ScenarioStep(r: r, all: m.repo.extras.scenarios, count: st?.count ?? 1).id(r.step)
            case .MOCK?:
                MockStep(r: r)
            case .DEBRIEF?, nil:
                Debrief(r: r)
            }
        }
    }
}

/** The one button forward, pinned to the bottom. */
private struct NextBar: View {
    let label: String
    var enabled = true
    let action: () -> Void
    var body: some View {
        BottomBar {
            Button(action: action) {
                Text(label).font(.nunito(16, .bold)).frame(maxWidth: .infinity, minHeight: 28)
            }
            .primaryButton()
            .disabled(!enabled)
        }
    }
}

/** Moves past a step that has nothing to show. */
private struct SkipStep: View {
    let action: () -> Void
    var body: some View { Color.clear.onAppear(perform: action) }
}

private struct LessonStep: View {
    @EnvironmentObject private var m: AppModel
    let r: PathRun

    var body: some View {
        if let l = m.repo.extras.lessons.first(where: { $0.id == r.session.lessonId }) {
            VStack(spacing: 0) {
                LessonView(lesson: l, inSession: true).frame(maxHeight: .infinity)
                NextBar(label: "I've read it · Continue") { m.nextStep(r) }
            }
        } else {
            SkipStep { m.nextStep(r) }
        }
    }
}

/** Tag why each miss in this session happened, while it is fresh. */
private struct ReflectStep: View {
    @EnvironmentObject private var m: AppModel
    let r: PathRun

    var body: some View {
        let wrong = r.practices.flatMap { p in p.questions.filter { p.results[$0.id]?.correct == false } }
        if wrong.isEmpty {
            SkipStep { m.nextStep(r) }
        } else {
            VStack(spacing: 0) {
                ScreenScroll(spacing: 10) {
                    Text("Why did these go wrong?").font(.nunito(20, .heavy))
                    Text("One tap each. Naming the cause is what stops it happening again, and the plan uses it.")
                        .font(.nunito(13)).foregroundStyle(Palette.onSurfaceVariant)
                    ForEach(wrong.indices, id: \.self) { i in
                        let q = wrong[i]
                        let tag = m.store.mistakeTags[q.id]?.cause
                        PmpCard {
                            Text(q.stem).font(.nunito(13)).lineLimit(3)
                            FlowLayout(spacing: 6) {
                                ForEach(RootCause.allCases) { c in
                                    Chip(label: c.title, selected: tag == c) { m.store.tagMistake(q.id, c, "") }
                                }
                            }
                            .padding(.top, 6)
                        }
                    }
                }
                NextBar(label: "Continue") { m.nextStep(r) }
            }
        }
    }
}

private struct ScenarioStep: View {
    @EnvironmentObject private var m: AppModel
    let r: PathRun
    @State private var list: [Scenario]
    @State private var i = 0
    @State private var pick: String? = nil
    @State private var shown = false

    init(r: PathRun, all: [Scenario], count: Int) {
        self.r = r
        _list = State(initialValue: Array(all.shuffled().prefix(max(count, 1))))
    }

    var body: some View {
        if i < list.count {
            let sc = list[i]
            VStack(spacing: 0) {
                ScrollViewReader { proxy in
                    ScrollView {
                        VStack(alignment: .leading, spacing: 12) {
                            Text("Scenario \(i + 1) of \(list.count)").font(.nunito(15, .bold)).id("top")
                            ScenarioBlock(scenario: sc, shown: shown, pick: pick) { pick = $0 }
                        }
                        .padding(16)
                        .frame(maxWidth: 720)
                        .frame(maxWidth: .infinity)
                    }
                    .onChange(of: i) { proxy.scrollTo("top", anchor: .top) }
                }
                if !shown {
                    NextBar(label: "Reveal the ranking", enabled: pick != nil) {
                        m.scenarioPick = pick
                        m.submitScenario(sc)
                        shown = true
                    }
                } else if i < list.count - 1 {
                    NextBar(label: "Next scenario") { i += 1; pick = nil; shown = false }
                } else {
                    NextBar(label: "Continue") { m.nextStep(r) }
                }
            }
        } else {
            SkipStep { m.nextStep(r) }
        }
    }
}

private struct MockStep: View {
    @EnvironmentObject private var m: AppModel
    let r: PathRun

    var body: some View {
        let done = m.store.mocks.count > r.mocksAtStart
        VStack(spacing: 0) {
            ScrollView {
                VStack(alignment: .leading, spacing: 12) {
                    if done, let mk = m.store.mocks.last {
                        Text("Mock done: \(pct(mk.pct))").font(.nunito(22, .heavy))
                        Text("\(mk.right) of \(mk.n) correct. Your plan adjusts to the domain scores.").font(.nunito(15))
                    } else {
                        let x = m.repo.extras.exam
                        Text("Full mock exam").font(.nunito(22, .heavy))
                        Text("\(x.totalItems) questions in \(x.minutes) minutes, with optional breaks. Find a quiet place and the whole sitting. " +
                             "You can pause and come back; the clock stops while you're away.").font(.nunito(15)).lineSpacing(3)
                    }
                }
                .padding(24)
                .frame(maxWidth: .infinity, alignment: .leading)
            }
            if done {
                NextBar(label: "Continue") { m.nextStep(r) }
            } else {
                NextBar(label: m.mock != nil ? "Resume the mock" : "Start the mock") {
                    if m.mock == nil { m.startMock(m.repo.extras.exam.totalItems) }
                    m.navigate(.MOCK)
                }
            }
        }
    }
}

private struct Debrief: View {
    @EnvironmentObject private var m: AppModel
    let r: PathRun
    @State private var next: Session? = nil
    @State private var looked = false

    var body: some View {
        let score = m.sessionScore(r)
        let answered = r.practices.flatMap { p in p.questions.filter { p.isChecked($0) } }
        let color: Color = answered.isEmpty ? Palette.primary : (score >= 0.8 ? Palette.success : (score >= 0.5 ? Palette.warning : Palette.error))
        VStack(spacing: 0) {
            ScreenScroll {
                Text("Session done").font(.nunito(24, .heavy))
                if !answered.isEmpty {
                    let right = rightCount(answered)
                    PmpCard {
                        Text(pct(score)).font(.nunito(40, .black)).foregroundStyle(color)
                        Text("\(right) of \(answered.count) right").foregroundStyle(Palette.onSurfaceVariant)
                    }
                    // Went well / watch, by task, from this session only.
                    let byTask = taskScores(answered)
                    let best = byTask.max { $0.value < $1.value }
                    let worst = byTask.min { $0.value < $1.value }
                    let rw = r.practices.reduce(0) { $0 + $1.totalRightToWrong }
                    let wr = r.practices.reduce(0) { $0 + $1.totalWrongToRight }
                    if rw + wr > 0 {
                        PmpCard {
                            Text("Changed answers: \(wr) wrong → right, \(rw) right → wrong.").font(.nunito(14, .bold))
                            Text(rw > wr ? "Your first instinct was right more often than your changes. Change an answer only when you find a specific reason."
                                 : "Your changes helped. Keep checking, but don't reopen answers without a reason.").font(.nunito(13))
                        }
                    }
                    PmpCard {
                        if let b = best {
                            Text("Went well: \(m.repo.task(b.key)?.label ?? b.key), \(pct(b.value))").font(.nunito(14))
                        }
                        if let w = worst, w.value < 0.8 {
                            Text("Watch: \(m.repo.task(w.key)?.label ?? w.key), \(pct(w.value))" +
                                 (w.value < 0.5 ? ". Tomorrow starts with a fix-up on it." : ". It comes back for review in a couple of days."))
                                .font(.nunito(14))
                        }
                    }
                } else {
                    Text("Nicely done. That's on your record.").font(.nunito(15))
                }
                if let n = next {
                    PmpCard {
                        SectionLabel("Next on your path")
                        Text(n.title).font(.nunito(15, .bold))
                        Text(n.why).font(.nunito(13)).foregroundStyle(Palette.onSurfaceVariant)
                    }
                }
            }
            NextBar(label: "Finish") { m.finishSession(r) }
        }
        .onAppear {
            if !looked {
                looked = true
                next = m.plan()?.sessions.first { $0.key != r.session.key }
            }
        }
    }

    private func rightCount(_ answered: [Question]) -> Int {
        answered.filter { q in r.practices.contains { $0.results[q.id]?.correct == true } }.count
    }

    private func taskScores(_ answered: [Question]) -> [String: Double] {
        var sums: [String: Double] = [:], counts: [String: Int] = [:]
        for q in answered {
            let credit = r.practices.lazy.compactMap { $0.results[q.id] }.first?.credit ?? 0
            sums[q.ecoTask, default: 0] += credit
            counts[q.ecoTask, default: 0] += 1
        }
        var out: [String: Double] = [:]
        for (k, v) in sums { out[k] = v / Double(max(counts[k] ?? 1, 1)) }
        return out
    }
}
