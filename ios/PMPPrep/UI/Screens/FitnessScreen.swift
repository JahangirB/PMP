import SwiftUI

/** Exam fitness: stamina, composure and consistency, what they're built from, and what to do next. */
struct FitnessScreen: View {
    @EnvironmentObject private var m: AppModel
    @State private var weekly = false

    var body: some View {
        let f = m.fitness()
        ScreenScroll {
            VStack(alignment: .leading, spacing: 4) {
                Text("Are you ready to sit it?").font(.nunito(22, .heavy))
                Text("Readiness asks whether you know the material. This asks whether you can sit for four hours, read 180 long questions calmly, and keep preparing until the day. It describes what you did; it isn't a diagnosis.")
                    .font(.nunito(13)).foregroundStyle(Palette.onSurfaceVariant).fixedSize(horizontal: false, vertical: true)
            }
            scoreCard(f)
            FitnessLadder(rung: f.ladderRung, next: f.ladderNext)
            if f.worryHighForWeeks {
                PmpCard(container: Palette.tertiaryContainer) {
                    Text("Your weekly checks have shown a lot of worry for three weeks.").font(.nunito(15, .bold))
                    Text("That's common before a big exam, and you don't have to carry it alone. Talking it through with someone you trust, or a doctor or counsellor, can help as much as more study.")
                        .font(.nunito(14)).fixedSize(horizontal: false, vertical: true)
                }
            }
            feelCard(f)
            ForEach(f.parts) { p in FitnessPartCard(p: p) }
            Button { m.navigate(.REPORTS) } label: { Text("Weekly reports and comparisons").frame(maxWidth: .infinity) }.outlinedButton()
            Spacer().frame(height: 24)
        }
        .sheet(isPresented: $weekly) {
            WeeklyCheckSheet(onDone: { items in m.recordCheckIn("weekly", 0, items: items); weekly = false }, onDismiss: { weekly = false })
        }
    }

    private func scoreCard(_ f: FitnessReport) -> some View {
        PmpCard {
            SectionLabel("Exam fitness")
            Text(f.score.map { "\($0) / 100" } ?? "Not enough yet").font(.nunito(30, .heavy))
            if f.provisional && f.score != nil {
                Text("Some parts still need evidence, so this is provisional.").font(.nunito(12)).foregroundStyle(Palette.onSurfaceVariant)
            }
            ForEach(f.parts) { p in
                ProgressRow(label: "\(p.label) · \(p.weight)%", value: p.score ?? 0, right: p.score.map { "\(Int($0 * 100))" } ?? "not yet").padding(.top, 4)
            }
            if let finding = f.finding { Text(finding).font(.nunito(14, .bold)).padding(.top, 6).fixedSize(horizontal: false, vertical: true) }
            if let next = f.next { Text("Next: \(next)").font(.nunito(14)).fixedSize(horizontal: false, vertical: true) }
        }
    }

    private func feelCard(_ f: FitnessReport) -> some View {
        PmpCard {
            SectionLabel("How you feel")
            Text(f.worry.map { "Exam worry: \($0) / 100 (last weekly check)" } ?? "No weekly check yet.").font(.nunito(14, .bold))
            if let link = f.link { Text(link).font(.nunito(13)) }
            Text("Five statements, about 30 seconds, once a week. It isn't scored against you; it shows whether worry eases as your preparation builds.")
                .font(.nunito(12)).foregroundStyle(Palette.onSurfaceVariant).fixedSize(horizontal: false, vertical: true)
            Button(m.weeklyCheckDue() ? "Take this week's check" : "Take it again") { weekly = true }.textButton()
        }
    }
}

private struct FitnessPartCard: View {
    let p: FitnessPart
    var body: some View {
        PmpCard {
            HStack {
                Text(p.label).font(.nunito(17, .heavy))
                Spacer()
                Text(p.score.map { "\(Int($0 * 100)) / 100" } ?? "not yet").font(.nunito(15, .bold)).foregroundStyle(Palette.primary)
            }
            if let gate = p.gate { Text(gate).font(.nunito(12)).foregroundStyle(Palette.onSurfaceVariant) }
            ForEach(p.signals) { s in
                VStack(alignment: .leading, spacing: 4) {
                    ProgressRow(label: "\(s.label) · \(s.weight)%", value: s.score ?? 0, right: s.figure)
                    Text(s.detail).font(.nunito(12)).foregroundStyle(Palette.onSurfaceVariant).fixedSize(horizontal: false, vertical: true)
                }
                .padding(.top, 6)
            }
        }
    }
}

/** 20 → 180 questions in one sitting, like a runner building to a marathon. */
private struct FitnessLadder: View {
    let rung: Int
    let next: Int?
    var body: some View {
        PmpCard {
            SectionLabel("Stamina ladder")
            HStack(spacing: 6) {
                ForEach(ExamFitness.LADDER, id: \.self) { r in
                    let done = r <= rung
                    let fill: Color = done ? Palette.primary : (r == next ? Palette.primaryContainer : Palette.surfaceVariant)
                    Text("\(r)").font(.nunito(14, .heavy))
                        .foregroundStyle(done ? Palette.onPrimary : Palette.onSurface)
                        .frame(maxWidth: .infinity, minHeight: 44)
                        .background(RoundedRectangle(cornerRadius: 14).fill(fill))
                }
            }
            .padding(.vertical, 6)
            .accessibilityElement(children: .ignore)
            .accessibilityLabel("Stamina ladder: reached \(rung) questions" + (next.map { ", next \($0)" } ?? ", complete"))
            Text(next.map { "Next: \($0) questions in one sitting at exam pace. It's on your path once a week. You move up when your last-quarter accuracy stays within 10 points of the first." }
                 ?? "You've held accuracy through a full 180-question sitting.")
                .font(.nunito(13)).fixedSize(horizontal: false, vertical: true)
        }
    }
}

/* ── check-ins ── */

/** How you feel before studying: 1 = very low … 5 = very good. */
let FACE_LABELS: [(String, String)] = [("😞", "Very low"), ("🙁", "Low"), ("😐", "OK"), ("🙂", "Good"), ("😄", "Very good")]

/** How tense, for after a mock: 1 = very tense … 5 = calm. */
let TENSION_LABELS: [(String, String)] = [("😣", "Very tense"), ("😟", "Tense"), ("😐", "So-so"), ("🙂", "Calm"), ("😌", "Very calm")]

/** One tap, five faces. [question] is what's asked; values are 1–5. */
struct CheckInRow: View {
    let question: String
    let labels: [(String, String)]
    let onPick: (Int) -> Void
    let onSkip: () -> Void

    init(question: String, labels: [(String, String)] = FACE_LABELS, onPick: @escaping (Int) -> Void, onSkip: @escaping () -> Void) {
        self.question = question
        self.labels = labels
        self.onPick = onPick
        self.onSkip = onSkip
    }

    var body: some View {
        PmpCard {
            Text(question).font(.nunito(15, .bold))
            Text("Optional. It helps the app show you how feelings and study relate.").font(.nunito(12)).foregroundStyle(Palette.onSurfaceVariant)
            FlowLayout(spacing: 6) {
                ForEach(labels.indices, id: \.self) { i in
                    Chip(label: "\(labels[i].0) \(labels[i].1)", selected: false) { onPick(i + 1) }
                        .accessibilityLabel(labels[i].1)
                }
            }
            .padding(.top, 8)
            Button("Skip", action: onSkip).textButton()
        }
    }
}

/** The weekly five-statement check. Each answer 1 (disagree) to 5 (agree). */
struct WeeklyCheckSheet: View {
    let onDone: ([Int]) -> Void
    let onDismiss: () -> Void
    @State private var answers: [Int] = Array(repeating: 0, count: ExamFitness.WEEKLY_STATEMENTS.count)
    private let scale = ["Disagree", "Somewhat disagree", "Neutral", "Somewhat agree", "Agree"]

    init(onDone: @escaping ([Int]) -> Void, onDismiss: @escaping () -> Void) {
        self.onDone = onDone
        self.onDismiss = onDismiss
    }

    var body: some View {
        NavigationStack {
            ScrollView {
                VStack(alignment: .leading, spacing: 16) {
                    Text("How much do you agree? There are no right answers.").font(.nunito(13))
                    ForEach(ExamFitness.WEEKLY_STATEMENTS.indices, id: \.self) { i in
                        VStack(alignment: .leading, spacing: 6) {
                            Text(ExamFitness.WEEKLY_STATEMENTS[i]).font(.nunito(14, .bold)).fixedSize(horizontal: false, vertical: true)
                            FlowLayout(spacing: 4) {
                                ForEach(scale.indices, id: \.self) { v in
                                    Chip(label: scale[v], selected: answers[i] == v + 1) { answers[i] = v + 1 }
                                }
                            }
                        }
                    }
                }
                .padding(16)
                .frame(maxWidth: .infinity, alignment: .leading)
            }
            .background(Palette.background.ignoresSafeArea())
            .navigationTitle("This week's check")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .cancellationAction) { Button("Not now", action: onDismiss) }
                ToolbarItem(placement: .confirmationAction) { Button("Save") { onDone(answers) }.disabled(answers.contains(0)) }
            }
        }
    }
}
