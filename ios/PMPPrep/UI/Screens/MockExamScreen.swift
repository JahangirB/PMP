import SwiftUI
import UIKit

struct MockExamScreen: View {
    @EnvironmentObject private var m: AppModel

    var body: some View {
        if let s = m.mock {
            MockRunning(s: s).id(ObjectIdentifier(s))
        } else if let rv = m.mockReview {
            QuizRunner(session: rv, onFinish: { m.mockReview = nil }).id(ObjectIdentifier(rv))
        } else if let r = m.lastMock {
            Scorecard(r: r).id(r.at)
        } else {
            MockIntro()
        }
    }
}

/** A rough guide only: PMI reports bands per domain but does not publish the cut scores behind them. */
private func band(_ p: Double) -> (String, Color) {
    if p >= 0.80 { return ("Above Target", Palette.success) }
    if p >= 0.65 { return ("Target", Palette.info) }
    if p >= 0.50 { return ("Below Target", Palette.warning) }
    return ("Needs Improvement", Palette.error)
}

/** The sitting on screen. The screen stays awake while it is, so it doesn't lock mid-question. */
private struct MockRunning: View {
    @ObservedObject var s: MockSession

    var body: some View {
        Group {
            if s.onBreak { BreakScreen(s: s) } else { ExamRunner(s: s) }
        }
        .onAppear { UIApplication.shared.isIdleTimerDisabled = true }
        .onDisappear { UIApplication.shared.isIdleTimerDisabled = false }
    }
}

private struct MockIntro: View {
    @EnvironmentObject private var m: AppModel
    @State private var breathing = false
    @State private var writing = false
    @State private var worries = ""

    var body: some View {
        if breathing {
            BreathingReset { breathing = false }
        } else {
            intro
        }
    }

    private var intro: some View {
        let x = m.repo.extras.exam
        let pool = m.pool().count
        let sizes: [(Int, String)] = [(x.totalItems, "Full exam"), (x.totalItems / 2, "Half exam"), (x.totalItems / 4, "Quarter exam"), (20, "Sprint")]
        return ScreenScroll {
            Text("Mock Exam Simulator").font(.nunito(22, .heavy))
            Text("Drawn from your \(pool) enabled questions, weighted to the published domain split, unseen questions first.")
                .font(.nunito(13)).foregroundStyle(Palette.onSurfaceVariant)
            PmpCard {
                SectionLabel("Full exam layout")
                BulletList(items: [
                    "\(x.totalItems) questions in \(x.minutes) minutes: about \(x.paceTargetSec) seconds each.",
                    "Three sections. After questions \(x.breakAfter.map(String.init).joined(separator: " and ")) you may take a \(x.breakMinutes)-minute break; the clock stops.",
                    "Once a section is closed you cannot go back to it, as in the real exam.",
                    "Mark questions to revisit, and jump around the current section from the question grid.",
                    "Scored at the end with every question type marked the same way as practice.",
                ])
                .padding(.top, 8)
            }
            // Writing worries out before a test improved scores for anxious students (Ramirez & Beilock, Science, 2011).
            PmpCard {
                SectionLabel("Before you start (optional)")
                Text("Nervous about it? Spend about 10 minutes writing down exactly what worries you about this sitting. Getting it on paper frees up the attention it was using. Nothing you write is saved.")
                    .font(.nunito(13))
                if !writing {
                    FlowLayout(spacing: 12) {
                        Button("Write it down") { writing = true }.textButton()
                        Button("Two-minute breathing reset") { breathing = true }.textButton()
                    }
                } else {
                    TextField("What's on your mind about this exam?", text: $worries, axis: .vertical)
                        .lineLimit(5...12)
                        .font(.nunito(15))
                        .padding(12)
                        .background(RoundedRectangle(cornerRadius: 10).fill(Palette.surface))
                        .overlay(RoundedRectangle(cornerRadius: 10).strokeBorder(Palette.outline, lineWidth: 1))
                    Button("Done, clear it") { writing = false; worries = "" }.textButton()
                }
            }
            Text("Tip: turn on a Focus (Do Not Disturb) from Control Center before you start, so notifications wait until you finish.")
                .font(.nunito(13)).foregroundStyle(Palette.onSurfaceVariant)
            ForEach(sizes.indices, id: \.self) { i in
                let n = sizes[i].0
                let minutes = Int((Double(x.minutes) * Double(n) / Double(max(x.totalItems, 1))).rounded())
                Button { m.startMock(n) } label: {
                    Text("\(sizes[i].1) · \(n) questions · \(minutes) min").font(.nunito(15, .bold)).frame(maxWidth: .infinity, minHeight: 28)
                }
                .primaryButton()
                .disabled(pool == 0)
            }
            if !m.store.mocks.isEmpty {
                SectionLabel("Your sittings")
                let sorted = m.store.mocks.sorted { $0.at > $1.at }
                ForEach(sorted.indices, id: \.self) { i in HistoryRow(r: sorted[i]) }
            }
        }
    }
}

private struct HistoryRow: View {
    let r: MockResult
    var body: some View {
        let b = band(r.pct)
        let label = b.0
        let color = b.1
        PmpCard {
            HStack(alignment: .center) {
                VStack(alignment: .leading, spacing: 2) {
                    Text(Time.date(r.at).formatted(date: .abbreviated, time: .shortened)).font(.nunito(14, .semibold))
                    Text("\(r.title) · \(r.right)/\(r.n) · \(clock(r.usedMs)) used").font(.nunito(12)).foregroundStyle(Palette.onSurfaceVariant)
                    Text(domainLine(r)).font(.nunito(12)).foregroundStyle(Palette.onSurfaceVariant)
                }
                .frame(maxWidth: .infinity, alignment: .leading)
                VStack(alignment: .trailing, spacing: 0) {
                    Text(pct(r.pct)).font(.nunito(22, .black)).foregroundStyle(color)
                    Text(label).font(.nunito(11, .bold)).foregroundStyle(color)
                }
            }
        }
    }

    private func domainLine(_ r: MockResult) -> String {
        Domain.allCases.compactMap { d -> String? in
            guard let e = r.domains[d], e.count >= 2 else { return nil }
            return "\(d.short) \(pct(e[1] == 0 ? 0 : Double(e[0]) / Double(e[1])))"
        }.joined(separator: " · ")
    }
}

private struct ExamRunner: View {
    @EnvironmentObject private var m: AppModel
    @ObservedObject var s: MockSession
    @State private var grid = false
    @State private var endAfterGrid = false
    @State private var endDialog = false
    @State private var leave = false

    var body: some View {
        let q = s.current
        let range = s.inSection
        let lastInSection = s.index == range.upperBound - 1
        let finalSection = s.section == s.sectionCount - 1
        let marked = s.marked[q.id] == true
        let answeredInSection = range.filter { Scoring.isAnswered(s.questions[$0], s.answers[s.questions[$0].id]) }.count

        VStack(spacing: 0) {
            VStack(spacing: 6) {
                HStack(spacing: 8) {
                    VStack(alignment: .leading, spacing: 1) {
                        Text("Question \(s.index + 1) of \(s.questions.count)").font(.nunito(15, .bold))
                        if s.sectionCount > 1 {
                            Text("Section \(s.section + 1) of \(s.sectionCount)").font(.nunito(11)).foregroundStyle(Palette.onSurfaceVariant)
                        }
                    }
                    Spacer(minLength: 0)
                    Text(clock(s.remainingMs))
                        .font(.system(size: 16, weight: .bold, design: .monospaced))
                        .foregroundStyle(s.remainingMs < 5 * 60_000 ? Palette.error : Palette.onSurface)
                        .accessibilityLabel("Time left \(clock(s.remainingMs))")
                    Button { m.toggleMark(q) } label: {
                        Image(systemName: marked ? "flag.fill" : "flag")
                            .font(.system(size: 18, weight: .semibold))
                            .foregroundStyle(marked ? Palette.warning : Palette.onSurfaceVariant)
                            .frame(width: 44, height: 44)
                    }
                    .accessibilityLabel("Mark for review")
                    .accessibilityAddTraits(marked ? .isSelected : [])
                }
                Bar(value: Double(s.index + 1) / Double(max(s.questions.count, 1)))
            }
            .padding(.horizontal, 16).padding(.vertical, 6)
            .background(Palette.surface)

            ScrollViewReader { proxy in
                ScrollView {
                    VStack(alignment: .leading, spacing: 0) {
                        Color.clear.frame(height: 0).id("top")
                        QuestionHeader(q: q, taskLabel: m.repo.task(q.ecoTask).map { "\($0.code) · \($0.label)" },
                                       highlights: s.marks[q.id], onHighlights: { w in m.mockMarks(q, w) })
                        QuestionBody(q: q, options: s.order[q.id] ?? q.options, answer: s.answers[q.id], revealed: false,
                                     struck: s.struck[q.id] ?? [], onStrike: { id in m.mockStrike(q, id) },
                                     onAnswer: { a in m.mockAnswer(q, a) })
                        Button("Leave or abandon this exam") { leave = true }
                            .textButton()
                            .padding(.top, 24)
                    }
                    .padding(16)
                    .frame(maxWidth: 720)
                    .frame(maxWidth: .infinity)
                }
                .onChange(of: s.index) { proxy.scrollTo("top", anchor: .top) }
            }

            BottomBar {
                Button { m.mockGo(s.index - 1) } label: { Text("Back").frame(maxWidth: .infinity) }
                    .outlinedButton()
                    .disabled(s.index <= range.lowerBound)
                Button { grid = true } label: { Text("\(answeredInSection)/\(range.count)").frame(maxWidth: .infinity) }
                    .outlinedButton()
                    .accessibilityLabel("Question grid, \(answeredInSection) of \(range.count) answered")
                Button {
                    if lastInSection { endDialog = true } else { m.mockGo(s.index + 1) }
                } label: {
                    Text(!lastInSection ? "Next" : (finalSection ? "Finish" : "End section")).font(.nunito(15, .bold)).frame(maxWidth: .infinity)
                }
                .primaryButton()
                .layoutPriority(1)
            }
        }
        .sheet(isPresented: $grid, onDismiss: {
            if endAfterGrid { endAfterGrid = false; endDialog = true }
        }) {
            QuestionGrid(s: s, finalSection: finalSection,
                         onGo: { i in m.mockGo(i); grid = false },
                         onEnd: { endAfterGrid = true; grid = false },
                         onClose: { grid = false })
        }
        .alert(endTitle(finalSection), isPresented: $endDialog) {
            if finalSection {
                Button("Submit") { m.submitMock() }
            } else {
                Button("Take the break") { m.endSection(takeBreak: true) }
                Button("Skip break") { m.endSection(takeBreak: false) }
            }
            Button("Keep reviewing", role: .cancel) {}
        } message: {
            Text(endMessage(range: range, finalSection: finalSection))
        }
        .alert("Leave the exam?", isPresented: $leave) {
            Button("Pause") { m.saveMock(); m.back() }
            Button("Abandon", role: .destructive) { m.abandonMock() }
            Button("Stay", role: .cancel) {}
        } message: {
            Text("Pause keeps your answers, and the clock stops until you come back; resume from Home. Abandon throws this sitting away.")
        }
    }

    private func endTitle(_ finalSection: Bool) -> String {
        finalSection ? "Submit the exam?" : "Close section \(s.section + 1)?"
    }

    private func endMessage(range: Range<Int>, finalSection: Bool) -> String {
        let open = range.filter { !Scoring.isAnswered(s.questions[$0], s.answers[s.questions[$0].id]) }.count
        let marked = range.filter { s.marked[s.questions[$0].id] == true }.count
        var out = open > 0 ? "\(open) unanswered question(s) in this section will be scored as wrong. " : "Every question in this section is answered. "
        if marked > 0 { out += "\(marked) are marked for review. " }
        if !finalSection { out += "You cannot come back to this section afterwards." }
        return out.trimmingCharacters(in: .whitespaces)
    }
}

/** The current section at a glance: answered, unanswered and marked; tap one to go to it. */
private struct QuestionGrid: View {
    @ObservedObject var s: MockSession
    let finalSection: Bool
    let onGo: (Int) -> Void
    let onEnd: () -> Void
    let onClose: () -> Void

    var body: some View {
        let columns = Array(repeating: GridItem(.flexible(minimum: 36), spacing: 6), count: 6)
        NavigationStack {
            ScrollView {
                VStack(alignment: .leading, spacing: 8) {
                    Text("Filled: answered · outlined: unanswered · gold: marked").font(.nunito(12)).foregroundStyle(Palette.onSurfaceVariant)
                    LazyVGrid(columns: columns, spacing: 6) {
                        ForEach(Array(s.inSection), id: \.self) { i in
                            cell(i)
                        }
                    }
                    Button { onEnd() } label: {
                        Text(finalSection ? "Finish exam" : "End section").frame(maxWidth: .infinity)
                    }
                    .outlinedButton()
                    .padding(.top, 12)
                }
                .padding(16)
            }
            .navigationTitle(s.sectionCount > 1 ? "Section \(s.section + 1)" : "All questions")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .cancellationAction) { Button("Close", action: onClose) }
            }
        }
        .presentationDetents([.medium, .large])
    }

    private func cell(_ i: Int) -> some View {
        let qq = s.questions[i]
        let ans = Scoring.isAnswered(qq, s.answers[qq.id])
        let mk = s.marked[qq.id] == true
        let current = i == s.index
        let shape = RoundedRectangle(cornerRadius: 8)
        return Button { onGo(i) } label: {
            Text("\(i + 1)").font(.nunito(12, .bold))
                .foregroundStyle(ans ? Palette.onPrimary : Palette.onSurface)
                .frame(maxWidth: .infinity, minHeight: 40)
                .background(shape.fill(ans ? Palette.primary : Palette.surfaceVariant))
                .overlay(shape.strokeBorder(mk ? Palette.warning : (current ? Palette.onSurface : Color.clear), lineWidth: 2))
        }
        .buttonStyle(.plain)
        .accessibilityLabel("Question \(i + 1), \(ans ? "answered" : "unanswered")\(mk ? ", marked" : "")\(current ? ", current" : "")")
    }
}

private struct BreakScreen: View {
    @EnvironmentObject private var m: AppModel
    @ObservedObject var s: MockSession

    var body: some View {
        ScrollView {
            VStack(spacing: 0) {
                Image(systemName: "cup.and.saucer.fill").font(.system(size: 48)).foregroundStyle(Palette.tertiary).accessibilityHidden(true)
                Text("Optional break").font(.nunito(22, .bold)).padding(.top, 12)
                Text("The exam clock is stopped. Section \(s.section + 1) is closed.")
                    .font(.nunito(15)).multilineTextAlignment(.center).foregroundStyle(Palette.onSurfaceVariant)
                Text(clock(s.breakRemainingMs)).font(.system(size: 40, weight: .black, design: .monospaced)).padding(.top, 16)
                Text("Use it: stand up, drink some water, look at something far away. Then the reset for the next section: breathe out slowly, and on each question read the last sentence first.")
                    .font(.nunito(14)).multilineTextAlignment(.center).padding(.top, 12)
                Button("Start section \(s.section + 2) now") { m.endBreak() }.primaryButton().padding(.top, 16)
            }
            .padding(24)
            .frame(maxWidth: 720)
            .frame(maxWidth: .infinity)
        }
        .defaultScrollAnchor(.center)
    }
}

private struct Scorecard: View {
    @EnvironmentObject private var m: AppModel
    let r: MockResult
    @State private var asked = false

    var body: some View {
        let b = band(r.pct)
        let label = b.0
        let color = b.1
        ScreenScroll {
            Text("Mock exam scorecard").font(.nunito(22, .heavy))
            if !asked {
                CheckInRow(question: "How tense did you feel during it?", labels: TENSION_LABELS,
                           onPick: { v in m.recordCheckIn("post", v); asked = true }, onSkip: { asked = true })
            }
            PmpCard(container: color.opacity(0.08), border: color) {
                HStack {
                    VStack(alignment: .leading, spacing: 2) {
                        Text(label).font(.nunito(18, .bold)).foregroundStyle(color)
                        Text("\(r.right) of \(r.n) correct · \(clock(r.usedMs)) used").font(.nunito(13)).foregroundStyle(Palette.onSurfaceVariant)
                    }
                    .frame(maxWidth: .infinity, alignment: .leading)
                    Text(pct(r.pct)).font(.nunito(40, .black)).foregroundStyle(color)
                }
                Text("Bands are a rough guide: PMI reports Above Target, Target, Below Target and Needs Improvement but does not publish the cut scores.")
                    .font(.nunito(11)).foregroundStyle(Palette.onSurfaceVariant).padding(.top, 6)
            }
            PmpCard {
                SectionLabel("By domain")
                VStack(spacing: 10) {
                    ForEach(Domain.allCases) { d in
                        if let e = r.domains[d], e.count >= 2 {
                            let p = e[1] == 0 ? 0 : Double(e[0]) / Double(e[1])
                            ProgressRow(label: "\(d.title) · \(band(p).0)", value: p, right: "\(e[0])/\(e[1])", color: Palette.domain(d))
                        }
                    }
                }
                .padding(.top, 8)
            }
            Button { m.reviewMock(onlyWrong: true) } label: { Text("Review the ones I got wrong").frame(maxWidth: .infinity) }.primaryButton()
            Button { m.reviewMock(onlyWrong: false) } label: { Text("Review every question").frame(maxWidth: .infinity) }.outlinedButton()
            Button { m.clearScorecard() } label: { Text("Done").frame(maxWidth: .infinity) }.textButton()
        }
    }
}
