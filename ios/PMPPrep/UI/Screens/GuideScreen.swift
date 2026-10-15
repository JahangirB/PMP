import SwiftUI

/**
 * "How it works": a user guide to the app — what you get, how a day runs, what is measured and
 * how, and what the app does about weak spots. Where it helps, your own current numbers are shown.
 */
struct GuideScreen: View {
    @EnvironmentObject private var m: AppModel

    var body: some View {
        let now = Time.now()
        let x: Extras = m.repo.extras
        let report = m.readiness.report(m.store, now)
        let states = PlanEngine.taskStates(x, m.store.attempts, now)
        let weakApproach = QuestionPicker.weakApproach(m.store.attempts)
        let rules: Rules = x.rules
        let bands: String = "Mock exams: Above Target from \(pct(rules.bandAbove)), Target from \(pct(rules.bandTarget)), Below Target from \(pct(rules.bandBelow)), Needs Improvement under \(pct(rules.bandBelow)). Your last three count for the score, newest most."
        ScreenScroll {
            VStack(alignment: .leading, spacing: 4) {
                Text("How PMP Prep works").font(.nunito(24, .heavy))
                Text("What you get, how a study day runs, how the app measures you, and how it works on your weak spots. Tap a section to open it.")
                    .font(.nunito(14)).foregroundStyle(Palette.onSurfaceVariant).fixedSize(horizontal: false, vertical: true)
            }
            SwiftUI.Group {
            GuideSection(title: "What you get", open: true) {
                GuideBullets(lines: [
                    "A plan from today to your exam date that rebuilds itself from what you actually do: missed days, weak scores and extra practice all reshape it.",
                    "One Continue button. Each session walks you through warm-up cards, a lesson, practice, a short reflection and a debrief.",
                    "An honest readiness score out of 100, built from five kinds of evidence, with the next thing that would raise it most.",
                    "Weak spots found and worked on automatically, and checked again until they hold.",
                    "Reminders at the hours that actually get you studying, learned from your own habits.",
                    "Everything works offline and stays on this phone. Only the Gemini chat and cloud sync, which you choose to use, go online.",
                ])
            }
            GuideSection(title: "How a study day runs") {
                GuideBullets(lines: [
                    "Open the app: the path shows today's session and why it was chosen.",
                    "Press Continue. Each step leads to the next; ✕ closes and keeps your place.",
                    "Short on time? The 15-minute version keeps the day counting.",
                    "Anything from the ☰ menu (drills, mocks, cards) counts too, and the path adjusts.",
                    "Finish the session: the debrief shows what went well and what to watch, and the plan updates at once.",
                ])
            }
            GuideSection(title: "Your readiness score", open: true) { readinessContent(report, x) }
            GuideSection(title: "How each exam task is judged") { tasksContent(states, x) }
            GuideSection(title: "How the app works on weak spots") {
                GuideSteps(steps: [
                    ("Find", "A task under \(pct(PlanEngine.MASTERY_ACCURACY)), a session under 50%, a task fading, your weakest domain, untouched tasks, flashcards that keep slipping."),
                    ("Rank", "Exam weight × how much is missing. Fix-ups jump the queue; your weakest domain's work comes first."),
                    ("Plan", "A session under 50% gets a fix-up next (lesson, easier questions, reflect). A shaky task gets drills, a middling one a review two days later, a fading one a refresher."),
                    ("Pick", "Questions on that task: ones you haven't seen first, then ones you got wrong last time. If one approach (predictive, agile, hybrid) is clearly your weakest, it takes every other question."),
                    ("Reflect", "Tag why each miss happened. When most tagged misses on a task share a cause, its sessions change: the lesson first for knowledge gaps, scenarios for mindset, a reading tip for misread stems."),
                    ("Prove", "Back to \(pct(PlanEngine.MASTERY_ACCURACY)) and drills stop. A domain checkpoint at \(pct(PlanEngine.PASS)) closes the domain, and it's re-checked if your answers there later fall under \(pct(PlanEngine.RECHECK_BELOW))."),
                ])
                if let w = weakApproach {
                    Text("Right now your practice leans toward \(w.title) questions: it's your weakest approach.")
                        .font(.nunito(13, .bold)).padding(.top, 6).fixedSize(horizontal: false, vertical: true)
                }
            }
            }
            SwiftUI.Group {
            GuideSection(title: "Exam fitness: ready to sit it") {
                GuideBullets(lines: [
                    "Readiness asks whether you know the material. Exam fitness asks whether you can sit for four hours, read 180 long questions calmly, and keep preparing until the day.",
                    "Stamina (40%): your longest sitting, how much accuracy fades and pace drifts in the last quarter of long sittings, and whether you finish them.",
                    "Composure (35%): reading speed (about \(Int(ExamFitness.TARGET_WPM)) words a minute leaves time to decide), freezes, rushing after a slow question, accuracy against the clock, and changing right answers to wrong.",
                    "Consistency (25%): regular days, hours kept, the trend in your weekly minutes, finishing sessions, and bouncing back after breaks.",
                    "It trains what it finds: a weekly stamina sitting that climbs 20 → 45 → 70 → 100 → 140 → 180 questions, reading drills, a gentle prompt after a long freeze in practice, pacing checkpoints, and lighter weeks when momentum dips.",
                    "Optional check-ins (one tap before a session or after a mock, and five statements once a week) show how feelings and study relate. It describes what you did; it isn't a diagnosis.",
                ])
                Button("See your exam fitness") { m.navigate(.FITNESS) }.textButton()
            }
            GuideSection(title: "Weekly reports") {
                GuideBullets(lines: [
                    "Every week (Monday to Sunday) is kept as a report: readiness, accuracy, tasks, mocks, exam fitness, reading pace, longest sitting, minutes and days.",
                    "Each figure is compared with the week before, or with your first week, and marked better or worse. Trend lines show the weeks together.",
                    "The Sunday evening reminder opens it.",
                ])
                Button("See your weekly reports") { m.navigate(.REPORTS) }.textButton()
            }
            GuideSection(title: "The other checks") {
                GuideBullets(lines: [
                    "Diagnostic: 25 questions taken cold (8 People, 10 Process, 7 Business). It sets which domain comes first until you have enough practice there.",
                    "Accuracy by domain: each domain is weighted as on the exam (" + Domain.allCases.map { "\($0.short) \(pct(x.domainWeights[$0] ?? 0))" }.joined(separator: ", ") + "), so a weak domain can't hide behind a strong one.",
                    "Checkpoints: 20 mixed questions per domain; \(pct(PlanEngine.PASS)) to pass.",
                    bands,
                    "Flashcards: spaced repetition. A card comes back just before you'd forget it; one forgotten \(x.srs.leechThreshold) times is a leech, and its task's lesson comes back as a review.",
                    "Credit per question: single choice is all or nothing; multiple response loses a point for each wrong pick; matching and fill-in give credit for each part right.",
                ])
            }
            GuideSection(title: "Tools while you study") {
                GuideBullets(lines: [
                    "How sure? When an answer is ready, tap Sure, Think so or Guess to check it. A right guess doesn't count toward mastery; a sure-but-wrong answer goes to the top of the mistake journal. Never asked in mocks; switch it off in Study settings.",
                    "Strike out and highlight, like the exam: long-press an option to cross it out; long-press words in the question and drag to highlight them. The results show whether crossing out first helps you.",
                    "Missed questions come back: after 1 day, then 3, 7 and 21, and they leave once you've got them right a week or more later. Up to \(QuestionReview.PER_DAY) a day join the warm-up.",
                    "Make a card: on a missed question, turn it into your own flashcard in My cards. Explain it back: write what you understood and Gemini says what you got right and missed (needs your key).",
                    "Reading comfort: text size, line spacing and a reading guide in Study settings. A two-minute breathing reset is offered when your check-in is low.",
                    "Rest days: one missed day a week doesn't break your streak.",
                ])
                Button("Study settings") { m.navigate(.SETTINGS) }.textButton()
                Button("Am I ready to book?") { m.navigate(.BOOK) }.textButton()
            }
            GuideSection(title: "Reminders that learn") {
                GuideBullets(lines: [
                    "At first a few a day, at hours you said you study and hours you tend to open the app.",
                    "Each hour keeps a score from what happens next: opened, studied, ignored, or Not today.",
                    "After about 20 reminders it keeps to the hours that work, with an occasional try of another. Old results fade, so a new routine is picked up.",
                    "Nothing on rest days or once today's plan is done; fewer if several are ignored.",
                ])
                Button("Reminder settings and your best hours") { m.navigate(.PLANNER) }.textButton()
            }
            GuideSection(title: "Your data") {
                GuideBullets(lines: [
                    "Progress, notes and reminder learning are stored only on this phone.",
                    "Back it up or move it with Question banks and backup in the menu. The backup file works with the Android and web apps too.",
                    "Cloud sync keeps this phone, the web app and the Android app in step through one file in your own Google Drive, using a small script you deploy in your Google account. Nothing goes to anyone else's server.",
                    "The Gemini chat sends your question and a summary of your progress to Google, only when you send a message, using your own key.",
                ])
                Button("Set up cloud sync") { m.navigate(.SYNC) }.textButton()
            }
            }
            Spacer().frame(height: 24)
        }
    }

    @ViewBuilder private func readinessContent(_ report: ReadinessReport, _ x: Extras) -> some View {
        let r = x.readiness
        Text("\(report.score) / 100" + (report.provisional ? " (provisional)" : "")).font(.nunito(22, .heavy))
        if report.provisional {
            Text("It firms up after \(r.gateItems) answers across \(r.gateTasks) tasks: " + report.gates.joined(separator: "; ") + ".")
                .font(.nunito(13)).fixedSize(horizontal: false, vertical: true)
        }
        ForEach(report.components) { c in
            VStack(alignment: .leading, spacing: 4) {
                ProgressRow(label: "\(c.label) · \(String(format: "%.1f", c.points)) of \(c.weight)", value: c.score, right: c.figure)
                Text(c.detail).font(.nunito(12)).foregroundStyle(Palette.onSurfaceVariant).fixedSize(horizontal: false, vertical: true)
            }
            .padding(.top, 8)
        }
        Text("A part with no evidence yet scores zero; its points aren't handed to the others. The advice always points at the part with the most points still missing:")
            .font(.nunito(13)).padding(.top, 8).fixedSize(horizontal: false, vertical: true)
        Text(report.nextAction).font(.nunito(13, .bold)).fixedSize(horizontal: false, vertical: true)
        Button("See the full analytics") { m.navigate(.ANALYTICS) }.textButton()
    }

    @ViewBuilder private func tasksContent(_ states: [TaskState], _ x: Extras) -> some View {
        let counts = Dictionary(grouping: states, by: \.mastery).mapValues(\.count)
        Text("The exam outline has \(x.tasks.count) tasks. Each one is judged on its last 15 answers:").font(.nunito(13)).padding(.bottom, 6)
        GuideLadderRow(name: "Not started", rule: "no answers yet", count: counts[.NOT_STARTED] ?? 0)
        GuideLadderRow(name: "Learning", rule: "fewer than \(PlanEngine.MASTERY_ANSWERS) answers", count: counts[.LEARNING] ?? 0)
        GuideLadderRow(name: "Shaky", rule: "under \(pct(PlanEngine.MASTERY_ACCURACY)) correct", count: counts[.SHAKY] ?? 0)
        GuideLadderRow(name: "Mastered", rule: "\(pct(PlanEngine.MASTERY_ACCURACY)) or better", count: counts[.MASTERED] ?? 0)
        GuideLadderRow(name: "Fading", rule: "mastered, but untouched for \(PlanEngine.FADE_DAYS)+ days", count: counts[.FADING] ?? 0)
        Text("A mastered task that you answer slower than \(Int(PlanEngine.SLOW_SEC)) seconds a question also gets a paced drill: the exam allows about \(x.exam.paceTargetSec).")
            .font(.nunito(12)).foregroundStyle(Palette.onSurfaceVariant).padding(.top, 6).fixedSize(horizontal: false, vertical: true)
    }
}

/** A clay card whose title opens and closes it. */
private struct GuideSection<Content: View>: View {
    let title: String
    @ViewBuilder let content: () -> Content
    @State private var expanded: Bool

    init(title: String, open: Bool = false, @ViewBuilder content: @escaping () -> Content) {
        self.title = title
        self.content = content
        _expanded = State(initialValue: open)
    }

    var body: some View {
        PmpCard {
            Button { withAnimation(.easeOut(duration: 0.2)) { expanded.toggle() } } label: {
                HStack {
                    Text(title).font(.nunito(17, .heavy)).foregroundStyle(Palette.onSurface).multilineTextAlignment(.leading)
                    Spacer()
                    Image(systemName: expanded ? "chevron.up" : "chevron.down").foregroundStyle(Palette.onSurfaceVariant).accessibilityHidden(true)
                }
                .frame(minHeight: 44)
                .contentShape(Rectangle())
            }
            .buttonStyle(.plain)
            .accessibilityAddTraits(.isHeader)
            .accessibilityHint(expanded ? "Closes the section" : "Opens the section")
            if expanded {
                VStack(alignment: .leading, spacing: 4) { content() }.padding(.top, 8)
            }
        }
    }
}

private struct GuideBullets: View {
    let lines: [String]
    var body: some View {
        VStack(alignment: .leading, spacing: 6) {
            ForEach(lines.indices, id: \.self) { i in
                HStack(alignment: .firstTextBaseline, spacing: 0) {
                    Text("•").font(.nunito(14)).frame(width: 14, alignment: .leading)
                    Text(lines[i]).font(.nunito(14)).fixedSize(horizontal: false, vertical: true)
                }
            }
        }
    }
}

private struct GuideSteps: View {
    let steps: [(String, String)]
    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            ForEach(steps.indices, id: \.self) { i in
                HStack(alignment: .firstTextBaseline, spacing: 0) {
                    Text("\(i + 1)").font(.nunito(14, .heavy)).foregroundStyle(Palette.primary).frame(width: 20, alignment: .leading)
                    VStack(alignment: .leading, spacing: 2) {
                        Text(steps[i].0).font(.nunito(14, .bold))
                        Text(steps[i].1).font(.nunito(13)).foregroundStyle(Palette.onSurfaceVariant).fixedSize(horizontal: false, vertical: true)
                    }
                }
                .accessibilityElement(children: .combine)
            }
        }
    }
}

private struct GuideLadderRow: View {
    let name: String
    let rule: String
    let count: Int
    var body: some View {
        HStack(spacing: 0) {
            Text(name).font(.nunito(14, .bold)).frame(width: 96, alignment: .leading)
            Text(rule).font(.nunito(13)).foregroundStyle(Palette.onSurfaceVariant).frame(maxWidth: .infinity, alignment: .leading)
            Text("\(count)").font(.nunito(14, .heavy)).foregroundStyle(Palette.primary)
        }
        .padding(.vertical, 2)
        .accessibilityElement(children: .combine)
    }
}
