import SwiftUI
import UIKit
import UserNotifications

/** Home. One path from today to the exam, one Continue button. */
struct PathScreen: View {
    @EnvironmentObject private var m: AppModel

    var body: some View {
        if m.setupDue {
            if !m.welcomeRead && m.repo.extras.welcome != nil { WelcomeScreen() } else { Onboarding() }
        } else if let plan = m.plan() {
            PathHome(plan: plan)
        } else {
            Color.clear
        }
    }
}

private struct PathHome: View {
    @EnvironmentObject private var m: AppModel
    let plan: Plan
    @State private var ahead: Session? = nil
    @State private var showChanges = false
    @State private var weeklyOpen = false

    var body: some View {
        let now = Time.now()
        let report = m.readiness.report(m.store, now)
        let streak = m.readiness.streak(m.store, now)
        let today = Time.startOfDay(now)
        let doneToday = m.store.sessionResults.filter { Time.startOfDay($0.at) == today }
        let next = plan.next
        let resuming = m.run
        let coachLine = m.coach().report(now).messages.first
        let remindersAsked = m.reminders.settings.asked
        let fit = m.fitness(now)
        let upcoming = upcomingGroups(plan, today)
        let later = plan.sessions.dropFirst().filter { $0.day >= today + 15 * Time.dayMs }.count

        ScreenScroll {
            VStack(alignment: .leading, spacing: 2) {
                Text(plan.daysLeft > 0 ? "\(plan.daysLeft) days to the exam" : "Exam day").font(.nunito(22, .heavy))
                Text("\(plan.phase) · readiness \(report.score) · \(streak.current)-day streak")
                    .font(.nunito(13)).foregroundStyle(Palette.onSurfaceVariant)
                Button { m.navigate(.FITNESS) } label: {
                    Text("Exam fitness \(fit.score.map { "\($0)" } ?? "–") · weekly report").font(.nunito(13, .bold))
                }
                .textButton()
            }
            .frame(maxWidth: .infinity, alignment: .leading)

            // Momentum falling: a lighter week of minimum days, not catch-up pressure.
            if fit.momentumLow {
                PmpCard(container: Palette.secondaryContainer) {
                    Text("A lighter week").font(.nunito(15, .bold))
                    Text("Your study time has dropped from recent weeks. That happens. For now, a minimum day counts: 10 cards or 10 questions. The rhythm matters more than the amount.")
                        .font(.nunito(13))
                    if let n = next {
                        Button("Start a 15-minute version") { m.startSession(m.shortVersion(n)) }.textButton()
                    }
                }
            }

            if m.weeklyCheckDue(now) {
                PmpCard {
                    Text("Weekly check · 30 seconds").font(.nunito(15, .bold))
                    Text("Five statements about how the exam feels. It shows whether worry eases as you prepare.").font(.nunito(13))
                    Button("Take it") { weeklyOpen = true }.textButton()
                }
            }

            // For anyone who finished setup before smart reminders existed: one prompt until it's answered.
            if !remindersAsked {
                PmpCard {
                    Text("Reminders that learn your rhythm").font(.nunito(15, .bold))
                    Text("Tell the app when you usually study. It tries a few times a day at first, then keeps to the hours that get you studying.")
                        .font(.nunito(13))
                    Button("Set up reminders") { m.navigate(.PLANNER) }.primaryButton().padding(.top, 6)
                }
            }

            if plan.behind {
                PmpCard(container: Palette.warning.opacity(0.10), border: Palette.warning) {
                    Text("The plan doesn't fit the time left").font(.nunito(15, .bold))
                    Text("About \(plan.neededMinutes / 60) hours of work for \(plan.availableMinutes / 60) hours before the final week. Pick what to change:")
                        .font(.nunito(13))
                    let p = m.store.plan
                    FlowLayout(spacing: 8) {
                        Button("+2 hours a week") {
                            var np = m.store.plan; np.hoursPerWeek += 2; m.store.setPlan(np)
                        }.outlinedButton()
                        if p.studyDays.count < 7 {
                            Button("Add a study day") {
                                var np = m.store.plan
                                if let d = (1...7).first(where: { !np.studyDays.contains($0) }) { np.studyDays.insert(d); m.store.setPlan(np) }
                            }.outlinedButton()
                        }
                        Button("Move the date") { m.navigate(.PLANNER) }.outlinedButton()
                    }
                    .padding(.top, 6)
                }
            }

            PmpCard(container: Palette.primaryContainer.opacity(0.55), border: Palette.primary) {
                if let r = resuming {
                    SectionLabel("In progress")
                    Text(r.session.title).font(.nunito(18, .bold))
                    Text("Step \(r.step + 1) of \(r.session.steps.count)").font(.nunito(13)).foregroundStyle(Palette.onSurfaceVariant)
                    Button { m.startSession(r.session) } label: {
                        Text("Resume").font(.nunito(16, .bold)).frame(maxWidth: .infinity, minHeight: 28)
                    }
                    .primaryButton().padding(.top, 10)
                } else if let n = next {
                    let todayDone = !doneToday.isEmpty && n.day > today
                    SectionLabel(todayDone ? "Today's done · next up" : (n.day > today ? "Next study day: \(dayLabel(n.day, today))" : "Today"))
                    Text(n.title).font(.nunito(18, .bold))
                    Text(n.why).font(.nunito(13)).foregroundStyle(Palette.onSurfaceVariant)
                    Text("About \(n.minutes) minutes · \(stepSummary(n))").font(.nunito(12)).foregroundStyle(Palette.onSurfaceVariant)
                    Button { m.startSession(n) } label: {
                        Text(todayDone || n.day > today ? "Study ahead" : "Continue").font(.nunito(16, .bold)).frame(maxWidth: .infinity, minHeight: 28)
                    }
                    .primaryButton().padding(.top, 10)
                    if n.minutes > 20 && n.kind != .MOCK {
                        Button { m.startSession(m.shortVersion(n)) } label: {
                            Text("Short on time? Do a 15-minute version").frame(maxWidth: .infinity)
                        }
                        .textButton()
                    }
                } else {
                    Text("Nothing left on the plan. Rest before the exam: you've done the work.").font(.nunito(15))
                }
            }

            if let line = coachLine {
                PmpCard(onTap: { m.coachTab = 0; m.navigate(.COACH) }) {
                    Text("Coach").font(.nunito(12, .bold)).foregroundStyle(Palette.primary)
                    Text(line.text).font(.nunito(14)).multilineTextAlignment(.leading)
                }
            }

            if !doneToday.isEmpty {
                VStack(alignment: .leading, spacing: 0) {
                    SectionLabel("Done today")
                    ForEach(doneToday.indices, id: \.self) { ri in
                        let r = doneToday[ri]
                        HStack(spacing: 8) {
                            Image(systemName: "checkmark.circle.fill").foregroundStyle(Palette.success).font(.system(size: 18))
                                .accessibilityHidden(true)
                            Text(titleFor(m, r.key)).font(.nunito(14)).frame(maxWidth: .infinity, alignment: .leading)
                            if r.score < 1.0 { Text(pct(r.score)).font(.nunito(13, .bold)) }
                        }
                        .padding(.vertical, 4)
                    }
                }
            }

            SectionLabel("Coming up")
            ForEach(upcoming.indices, id: \.self) { gi in
                let day = upcoming[gi].0
                let list = upcoming[gi].1
                VStack(alignment: .leading, spacing: 0) {
                    Text(dayLabel(day, today)).font(.nunito(12, .bold)).foregroundStyle(Palette.onSurfaceVariant)
                    ForEach(list.indices, id: \.self) { si in
                        let s = list[si]
                        Button { ahead = s } label: {
                            HStack(spacing: 10) {
                                Image(systemName: "circle").foregroundStyle(Palette.outline).font(.system(size: 16)).accessibilityHidden(true)
                                Text(s.title).font(.nunito(15)).foregroundStyle(Palette.onSurface)
                                    .multilineTextAlignment(.leading).frame(maxWidth: .infinity, alignment: .leading)
                                Text("\(s.minutes) min").font(.nunito(12)).foregroundStyle(Palette.onSurfaceVariant)
                            }
                            .padding(.vertical, 10)
                            .contentShape(Rectangle())
                        }
                        .buttonStyle(.plain)
                    }
                }
            }
            if later > 0 {
                Text("…and \(later) more sessions before the exam.").font(.nunito(13)).foregroundStyle(Palette.onSurfaceVariant)
            }

            if !plan.adjustments.isEmpty || !plan.dropped.isEmpty {
                PmpCard(onTap: { showChanges.toggle() }) {
                    Text("How your plan adjusted (\(plan.adjustments.count))").font(.nunito(14, .bold))
                    if showChanges {
                        VStack(alignment: .leading, spacing: 2) {
                            ForEach(plan.adjustments.indices, id: \.self) { i in
                                Text("• \(plan.adjustments[i])").font(.nunito(13)).multilineTextAlignment(.leading)
                            }
                            if !plan.dropped.isEmpty {
                                Text("Trimmed: " + plan.dropped.joined(separator: ", ")).font(.nunito(13))
                                    .foregroundStyle(Palette.onSurfaceVariant).multilineTextAlignment(.leading)
                            }
                        }
                        .padding(.top, 6)
                    } else {
                        Text("Tap to see why the plan looks the way it does.").font(.nunito(12)).foregroundStyle(Palette.onSurfaceVariant)
                    }
                }
            }
            Spacer().frame(height: 72)
        }
        .alert(ahead?.title ?? "", isPresented: Binding(get: { ahead != nil }, set: { if !$0 { ahead = nil } }), presenting: ahead) { s in
            Button("Start now") { ahead = nil; m.startSession(s) }
            Button("Not now", role: .cancel) { ahead = nil }
        } message: { s in
            Text(s.why + "\n\nThis is ahead of your plan. You can do it now; the plan adjusts.")
        }
        .sheet(isPresented: $weeklyOpen) {
            WeeklyCheckSheet(onDone: { items in m.recordCheckIn("weekly", 0, items: items); weeklyOpen = false },
                             onDismiss: { weeklyOpen = false })
                .environmentObject(m)
        }
    }
}

/** The sessions after the next one within 15 days, grouped by day in plan order. */
private func upcomingGroups(_ plan: Plan, _ today: Millis) -> [(Millis, [Session])] {
    var groups: [(Millis, [Session])] = []
    for s in plan.sessions.dropFirst() where s.day < today + 15 * Time.dayMs {
        if let i = groups.firstIndex(where: { $0.0 == s.day }) { groups[i].1.append(s) } else { groups.append((s.day, [s])) }
    }
    return groups
}

private func dayLabel(_ day: Millis, _ today: Millis) -> String {
    switch (day - today + Time.dayMs / 2) / Time.dayMs {
    case 0: return "Today"
    case 1: return "Tomorrow"
    default:
        let d = Time.date(day)
        return d.formatted(.dateTime.weekday(.wide)) + ", " + d.formatted(date: .abbreviated, time: .omitted)
    }
}

private func stepSummary(_ s: Session) -> String {
    s.steps.compactMap { st -> String? in
        switch st.kind {
        case .CARDS: return "cards"
        case .LESSON: return "lesson"
        case .PRACTICE: return "\(st.count) questions"
        case .SCENARIOS: return "\(st.count) scenarios"
        case .MOCK: return "full mock"
        default: return nil
        }
    }.joined(separator: " · ")
}

private func capitalizedFirst(_ s: String) -> String { s.prefix(1).uppercased() + s.dropFirst() }

@MainActor
private func titleFor(_ m: AppModel, _ key: String) -> String {
    let k = key.hasPrefix("short:") ? String(key.dropFirst("short:".count)) : key
    let parts = k.components(separatedBy: ":")
    let arg = parts.count > 1 ? parts[1] : ""
    let label: String?
    switch parts[0] {
    case "build", "drill", "review", "remediation": label = m.repo.task(arg)?.label
    case "checkpoint": label = "Checkpoint: " + capitalizedFirst(arg)
    case "lesson": label = m.repo.extras.lessons.first { $0.id == arg }?.title
    case "mock": label = "Full mock exam"
    case "reentry": label = "Welcome-back warm-up"
    case "mixed": label = "Mixed timed practice"
    case "taper": label = "Light review"
    default: label = nil
    }
    let prefix = ["drill", "review", "remediation"].contains(parts[0]) ? capitalizedFirst(parts[0]) + ": " : ""
    return (key.hasPrefix("short:") ? "Short: " : "") + prefix + (label ?? k)
}

/* ── first run ──────────────────────────────────────────────────────────── */

private struct Onboarding: View {
    @EnvironmentObject private var m: AppModel
    @State private var step = 0
    @State private var picking = false
    @State private var pickDate = Date()

    var body: some View {
        let p = m.store.plan
        ScrollView {
            VStack(alignment: .leading, spacing: 16) {
                Text("Step \(step + 1) of 5").font(.nunito(12)).foregroundStyle(Palette.onSurfaceVariant)
                switch step {
                case 0:
                    Text("Your PMP study, planned for you").font(.nunito(26, .heavy))
                    Text("Tell the app when your exam is and how much time you have. A short diagnostic shows where you stand. " +
                         "Then each day you press Continue and it walks you through lessons, practice, flashcards and mocks, adjusting to how you do.")
                        .font(.nunito(16)).lineSpacing(4)
                    bigButton("Get started") { step = 1 }
                case 1:
                    Text("When is your exam?").font(.nunito(24, .heavy))
                    Text("A target date is fine if it isn't booked yet. You can change it later.").font(.nunito(15))
                    Text(p.examDate.map { Time.date($0).formatted(date: .complete, time: .omitted) } ?? "No date yet").font(.nunito(18, .bold))
                    Button("Pick the date") {
                        pickDate = Time.date(p.examDate ?? (Time.now() + 60 * Time.dayMs))
                        picking = true
                    }
                    .outlinedButton()
                    bigButton("Next", enabled: p.examDate != nil) { step = 2 }
                case 2:
                    Text("How much can you study?").font(.nunito(24, .heavy))
                    Text("An honest figure beats an ambitious one.").font(.nunito(15))
                    Text("\(p.hoursPerWeek) hours a week · about \(Int((Double(p.hoursPerWeek) * 60 / Double(max(p.studyDays.count, 1))).rounded())) minutes a study day")
                        .font(.nunito(16, .bold))
                    Slider(value: Binding(get: { Double(m.store.plan.hoursPerWeek) }, set: { v in
                        var np = m.store.plan
                        let h = Int(v.rounded())
                        if np.hoursPerWeek != h { np.hoursPerWeek = h; m.store.setPlan(np) }
                    }), in: 2...30, step: 1)
                    .accessibilityLabel("Hours a week")
                    .accessibilityValue("\(p.hoursPerWeek) hours")
                    Text("Which days?").font(.nunito(15, .semibold))
                    StudyDayChips(plan: p) { m.store.setPlan($0) }
                    bigButton("Next", enabled: !p.studyDays.isEmpty) { step = 3 }
                case 3:
                    ReminderSetupStep { step = 4 }
                default:
                    Text("Where do you stand?").font(.nunito(24, .heavy))
                    Text("25 questions across the three domains, about 30 minutes. Take it cold, without revising first: it measures you, not your last hour of reading. " +
                         "The plan starts with whatever comes out weakest.").font(.nunito(15)).lineSpacing(3)
                    if let d = m.store.diagnostic {
                        Text("You've already taken it: \(pct(d.pct)).").font(.nunito(15, .bold))
                    }
                    bigButton(m.store.diagnostic != nil ? "Retake the diagnostic" : "Take the diagnostic") {
                        m.finishOnboarding(); m.startDiagnostic()
                    }
                    Button { m.finishOnboarding() } label: {
                        Text(m.store.diagnostic != nil ? "Keep my result and see the plan" : "Skip for now").frame(maxWidth: .infinity)
                    }
                    .textButton()
                }
                if step > 0 { Button("Back") { step -= 1 }.textButton() }
            }
            .padding(24)
            .frame(maxWidth: 720)
            .frame(maxWidth: .infinity)
        }
        .sheet(isPresented: $picking) {
            ExamDatePicker(date: $pickDate, onCancel: { picking = false }, onDone: {
                var np = m.store.plan
                np.examDate = Time.millis(Calendar.current.date(bySettingHour: 9, minute: 0, second: 0, of: pickDate) ?? pickDate)
                m.store.setPlan(np)
                picking = false
            })
        }
    }

    private func bigButton(_ label: String, enabled: Bool = true, action: @escaping () -> Void) -> some View {
        Button(action: action) { Text(label).font(.nunito(16, .bold)).frame(maxWidth: .infinity, minHeight: 28) }
            .primaryButton()
            .disabled(!enabled)
    }
}

private struct ExamDatePicker: View {
    @Binding var date: Date
    let onCancel: () -> Void
    let onDone: () -> Void

    var body: some View {
        let tomorrow = Calendar.current.startOfDay(for: Date().addingTimeInterval(24 * 60 * 60))
        NavigationStack {
            ScrollView {
                DatePicker("Exam date", selection: $date, in: tomorrow..., displayedComponents: .date)
                    .datePickerStyle(.graphical)
                    .padding()
            }
            .navigationTitle("Exam date")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .cancellationAction) { Button("Cancel", action: onCancel) }
                ToolbarItem(placement: .confirmationAction) { Button("Done", action: onDone) }
            }
        }
        .presentationDetents([.large])
        .onAppear { if date < tomorrow { date = tomorrow } }
    }
}

/** Monday to Sunday, as calendar weekday values (Sunday 1 … Saturday 7). */
private struct StudyDayChips: View {
    let plan: StudyPlan
    let onChange: (StudyPlan) -> Void
    private static let names: [(Int, String)] = [(2, "Mon"), (3, "Tue"), (4, "Wed"), (5, "Thu"), (6, "Fri"), (7, "Sat"), (1, "Sun")]

    var body: some View {
        FlowLayout(spacing: 6) {
            ForEach(Self.names.indices, id: \.self) { i in
                let d = Self.names[i].0
                Chip(label: Self.names[i].1, selected: plan.studyDays.contains(d)) {
                    var p = plan
                    if p.studyDays.contains(d) { p.studyDays.remove(d) } else { p.studyDays.insert(d) }
                    onChange(p)
                }
            }
        }
    }
}

private func hourLabel(_ h: Int) -> String { String(format: "%02d:00", h) }

/** Onboarding step: set reminders up in one go. */
private struct ReminderSetupStep: View {
    @EnvironmentObject private var m: AppModel
    @Environment(\.scenePhase) private var phase
    @State private var status: UNAuthorizationStatus? = nil
    let onNext: () -> Void

    var body: some View {
        let s = m.reminders.settings
        VStack(alignment: .leading, spacing: 16) {
            Text("Reminders that learn your rhythm").font(.nunito(24, .heavy))
            Text("A few reminders a day at first. The app watches which ones get you studying, then keeps to the hours that work and drops the rest.")
                .font(.nunito(15)).lineSpacing(3)

            VStack(alignment: .leading, spacing: 8) {
                Text("When do you usually study?").font(.nunito(14, .bold))
                Text("Pick any that fit. It's only a starting guess; the app learns your real times.")
                    .font(.nunito(12)).foregroundStyle(Palette.onSurfaceVariant)
                FlowLayout(spacing: 6) {
                    ForEach(StudyWindow.allCases) { w in
                        Chip(label: w.label, selected: s.windows.contains(w)) {
                            m.updateReminders { st in
                                if st.settings.windows.contains(w) { st.settings.windows.remove(w) } else { st.settings.windows.insert(w) }
                                st.settings.asked = true
                            }
                        }
                    }
                }
                HStack(alignment: .top, spacing: 10) {
                    ChoiceSelector(label: "I wake up", options: (4...11).map { ($0, hourLabel($0)) }, selected: s.wake) { v in
                        m.updateReminders { $0.settings.wake = v }
                    }
                    ChoiceSelector(label: "I go to sleep", options: (20...23).map { ($0, hourLabel($0)) } + [(24, "Midnight")], selected: s.sleep) { v in
                        m.updateReminders { $0.settings.sleep = v }
                    }
                }
                Text("No reminders before \(hourLabel(s.wake + 1)) or after \(hourLabel(s.sleep - 1)).")
                    .font(.nunito(12)).foregroundStyle(Palette.onSurfaceVariant)
            }

            // Asks for notifications, so reminders can be shown.
            if status == .notDetermined {
                Text("Notifications are off for this app, so reminders can't be shown.").font(.nunito(13)).foregroundStyle(Palette.error)
                Button {
                    ReminderScheduler.requestPermission { _ in refresh() }
                } label: { Text("Allow reminders").frame(maxWidth: .infinity) }
                .primaryButton()
            } else if status == .denied {
                Text("Notifications are off for this app, so reminders can't be shown.").font(.nunito(13)).foregroundStyle(Palette.error)
                Text("Turn them on in Settings → PMP Prep → Notifications.").font(.nunito(12))
                Button("Open Settings") {
                    if let url = URL(string: UIApplication.openSettingsURLString) { UIApplication.shared.open(url) }
                }
                .textButton()
            }

            Button {
                m.updateReminders { $0.settings.asked = true }
                onNext()
            } label: { Text("Next").font(.nunito(16, .bold)).frame(maxWidth: .infinity, minHeight: 28) }
            .primaryButton()
        }
        .onAppear { refresh() }
        .onChange(of: phase) { if phase == .active { refresh() } }
    }

    private func refresh() {
        UNUserNotificationCenter.current().getNotificationSettings { st in
            let a = st.authorizationStatus
            DispatchQueue.main.async { status = a }
        }
    }
}
