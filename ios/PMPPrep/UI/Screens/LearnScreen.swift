import SwiftUI

/** Lesson modules, or the open lesson. */
struct LearnScreen: View {
    @EnvironmentObject private var m: AppModel

    var body: some View {
        if let id = m.openLesson, let l = m.repo.extras.lessons.first(where: { $0.id == id }) {
            LessonView(lesson: l)
        } else {
            LessonList()
        }
    }
}

private struct LessonList: View {
    @EnvironmentObject private var m: AppModel

    var body: some View {
        let x: Extras = m.repo.extras
        let done = m.store.lessonsDone
        ScrollView {
            LazyVStack(alignment: .leading, spacing: 10) {
                VStack(alignment: .leading, spacing: 4) {
                    Text("\(done.count) of \(x.lessons.count) lessons done").font(.nunito(15, .bold))
                    Text("Read a lesson, try the worked example and the quick checks, then practise the task.")
                        .font(.nunito(13)).foregroundStyle(Palette.onSurfaceVariant)
                }
                .padding(.top, 8)
                ForEach(x.modules.sorted { $0.order < $1.order }) { mod in
                    let lessons = x.lessons.filter { $0.module == mod.id }.sorted { $0.order < $1.order }
                    if !lessons.isEmpty {
                        VStack(alignment: .leading, spacing: 2) {
                            SectionLabel("\(mod.label) · \(lessons.filter { done.contains($0.id) }.count)/\(lessons.count)")
                            Text(mod.blurb).font(.nunito(12)).foregroundStyle(Palette.onSurfaceVariant)
                        }
                        .padding(.top, 6)
                        ForEach(lessons) { l in
                            LessonRow(lesson: l, done: done.contains(l.id), color: Palette.domain(mod.domain), task: m.repo.task(l.ecoTask)) {
                                m.openLesson = l.id
                            }
                        }
                    }
                }
                Spacer().frame(height: 16)
            }
            .padding(.horizontal, 16)
            .frame(maxWidth: 720)
            .frame(maxWidth: .infinity)
        }
    }
}

private struct LessonRow: View {
    let lesson: Lesson
    let done: Bool
    let color: Color
    let task: EcoTask?
    let onTap: () -> Void

    var body: some View {
        PmpCard(onTap: onTap) {
            HStack(spacing: 10) {
                Image(systemName: done ? "checkmark.circle.fill" : "circle")
                    .font(.system(size: 20))
                    .foregroundStyle(done ? Palette.success : color)
                    .accessibilityLabel(done ? "Done" : "Not done")
                VStack(alignment: .leading, spacing: 2) {
                    Text("\(lesson.id) · \(lesson.title)").font(.nunito(14, .semibold)).multilineTextAlignment(.leading)
                    Text("\(lesson.minutes) min · \(lesson.checks.count) checks" + (task.map { " · \($0.label)" } ?? ""))
                        .font(.nunito(12)).foregroundStyle(Palette.onSurfaceVariant).multilineTextAlignment(.leading)
                }
                Spacer(minLength: 0)
            }
        }
    }
}

/** One lesson. inSession: shown inside a guided-path session, which handles marking done and moving on. */
struct LessonView: View {
    let lesson: Lesson
    var inSession: Bool = false

    init(lesson: Lesson, inSession: Bool = false) {
        self.lesson = lesson
        self.inSession = inSession
    }

    var body: some View {
        // A fresh identity per lesson: its answers reset and it starts at the top.
        LessonBody(l: lesson, inSession: inSession).id(lesson.id)
    }
}

private struct LessonBody: View {
    @EnvironmentObject private var m: AppModel
    @Environment(\.reading) private var reading
    let l: Lesson
    let inSession: Bool

    @State private var workedPick: String? = nil
    @State private var workedShown = false
    @State private var checkAnswers: [String: Answer] = [:]
    @State private var checked: [String: Bool] = [:]
    @State private var explaining = false

    var body: some View {
        let task = m.repo.task(l.ecoTask)
        let done = m.store.lessonsDone.contains(l.id)
        ScrollView {
            VStack(alignment: .leading, spacing: 14) {
                SwiftUI.Group {
                    Text(l.title).font(.nunito(22, .heavy)).fixedSize(horizontal: false, vertical: true)
                    Text("\(l.id) · \(l.minutes) minutes" + (task.map { " · \($0.code) \($0.label)" } ?? ""))
                        .font(.nunito(12, .semibold)).foregroundStyle(Palette.primary)
                    if !l.objectives.isEmpty {
                        PmpCard {
                            SectionLabel("You will be able to")
                            BulletList(items: l.objectives).padding(.top, 6)
                        }
                    }
                    ForEach(l.sections.indices, id: \.self) { i in
                        let s = l.sections[i]
                        VStack(alignment: .leading, spacing: 6) {
                            Text(s.heading).font(.nunito(reading.size(18), .bold)).fixedSize(horizontal: false, vertical: true)
                            StemText(s.body, baseSize: 15)
                            if let fig = s.figure { FigureView(fig: fig).padding(.top, 4) }
                        }
                    }
                    if !l.lenses.isEmpty {
                        PmpCard {
                            SectionLabel("Across delivery approaches")
                            ForEach(l.lenses.indices, id: \.self) { i in
                                let lens = l.lenses[i]
                                VStack(alignment: .leading, spacing: 2) {
                                    Text(String(lens.0.prefix(1)).uppercased() + String(lens.0.dropFirst())).font(.nunito(14, .bold))
                                    Text(lens.1).font(.nunito(14)).lineSpacing(reading.spacing(14)).fixedSize(horizontal: false, vertical: true)
                                }
                                .padding(.top, 8)
                            }
                        }
                    }
                }
                SwiftUI.Group {
                    if let w = l.worked {
                        SectionLabel("Worked example")
                        ScenarioBlock(scenario: w, shown: workedShown, pick: workedPick, onPick: { workedPick = $0 })
                        if !workedShown {
                            Button { workedShown = true } label: { Text("Show the ranking").frame(maxWidth: .infinity) }
                                .primaryButton()
                                .disabled(workedPick == nil)
                        }
                    }
                    if !l.traps.isEmpty {
                        PmpCard {
                            SectionLabel("Traps")
                            BulletList(items: l.traps, bullet: "⚠").padding(.top, 6)
                        }
                    }
                    if !l.checks.isEmpty {
                        SectionLabel("Quick checks")
                        Text("These don't count towards readiness: they are for learning.")
                            .font(.nunito(12)).foregroundStyle(Palette.onSurfaceVariant)
                        ForEach(l.checks.indices, id: \.self) { i in
                            check(i, l.checks[i])
                        }
                    }
                }
                SwiftUI.Group {
                    if !l.terms.isEmpty {
                        PmpCard {
                            SectionLabel("Key terms")
                            Text(l.terms.joined(separator: " · ")).font(.nunito(14)).padding(.top, 6)
                        }
                    }
                    if !l.recap.isEmpty {
                        PmpCard {
                            SectionLabel("Recap")
                            BulletList(items: l.recap).padding(.top, 6)
                        }
                    }
                    MindsetCallout(mindset: l.mindset)
                    Button { explaining = true } label: { Text("Explain it back in your words").frame(maxWidth: .infinity) }
                        .outlinedButton()
                    if !inSession {
                        Button { m.store.setLessonDone(l.id, !done) } label: {
                            Text(done ? "Mark as not done" : "Mark lesson done").frame(maxWidth: .infinity)
                        }
                        .primaryButton()
                    }
                    if !inSession && !l.ecoTask.trimmingCharacters(in: .whitespaces).isEmpty {
                        Button {
                            m.startPractice(PracticeFilter(task: l.ecoTask, count: 10), title: "Practice: \(l.title)")
                        } label: { Text("Practise this task (10 questions)").frame(maxWidth: .infinity) }
                            .outlinedButton()
                    }
                    Spacer().frame(height: 16)
                }
            }
            .padding(16)
            .frame(maxWidth: 720, alignment: .leading)
            .frame(maxWidth: .infinity)
        }
        .sheet(isPresented: $explaining) {
            ExplainBackSheet(
                topic: "The lesson \"\(l.title)\". Its main points:\n" + (l.recap.isEmpty ? l.objectives : l.recap).map { "• \($0)" }.joined(separator: "\n"),
                prompt: "Explain the main idea of this lesson as if teaching a colleague: what it is, when it applies, and the trap to avoid.",
                onDone: { explaining = false })
            .environmentObject(m)
        }
    }

    @ViewBuilder private func check(_ i: Int, _ q: Question) -> some View {
        let shown = checked[q.id] == true
        PmpCard {
            Text("\(i + 1). \(q.stem)").font(.nunito(reading.size(15))).lineSpacing(reading.spacing(15))
                .fixedSize(horizontal: false, vertical: true)
            QuestionBody(q: q, options: q.options, answer: checkAnswers[q.id], revealed: shown, onAnswer: { a in
                if !shown { checkAnswers[q.id] = a }
            })
            .padding(.vertical, 10)
            if !shown {
                Button {
                    checked[q.id] = true
                    m.recordLessonCheck(q, checkAnswers[q.id] ?? Answer())
                } label: { Text("Check") }
                    .outlinedButton()
                    .disabled(!Scoring.isAnswered(q, checkAnswers[q.id]))
            } else {
                Feedback(q: q, result: Scoring.score(q, checkAnswers[q.id]), answered: true)
            }
        }
    }
}
