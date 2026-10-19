import SwiftUI
import UIKit
import UserNotifications

/** The study plan's inputs: starting level, exam date, hours, study days, and the reminders. */
struct PlannerScreen: View {
    @EnvironmentObject private var m: AppModel
    @State private var pickingDate = false
    @State private var draftDate = Date()

    var body: some View {
        let plan = m.store.plan
        ScreenScroll {
            VStack(alignment: .leading, spacing: 4) {
                Text("Study plan").font(.nunito(22, .heavy))
                Text("Your path is built from these. Change them any time; it re-plans at once.")
                    .font(.nunito(13)).foregroundStyle(Palette.onSurfaceVariant)
            }
            diagnosticCard
            examDateCard(plan)
            PmpCard {
                SectionLabel("Hours a week")
                Text("\(plan.hoursPerWeek) hours").font(.nunito(18, .bold))
                Slider(value: Binding(get: { Double(m.store.plan.hoursPerWeek) },
                                      set: { v in var p = m.store.plan; let h = Int(v.rounded()); if p.hoursPerWeek != h { p.hoursPerWeek = h; m.store.setPlan(p) } }),
                       in: 2...40, step: 1)
                    .accessibilityLabel("Hours a week")
                    .accessibilityValue("\(plan.hoursPerWeek) hours")
                Text("Most candidates who pass study 8–15 hours a week for two to three months.")
                    .font(.nunito(12)).foregroundStyle(Palette.onSurfaceVariant).fixedSize(horizontal: false, vertical: true)
            }
            PmpCard {
                SectionLabel("Study days")
                PlannerDayChips(plan: plan) { m.store.setPlan($0) }.padding(.vertical, 6)
                Text("Your path spreads the week's hours over these days. Miss one and it re-plans from where you are.")
                    .font(.nunito(12)).foregroundStyle(Palette.onSurfaceVariant).fixedSize(horizontal: false, vertical: true)
            }
            PmpCard {
                SectionLabel("Study order")
                Picker("Study order", selection: Binding(get: { m.store.plan.byArea },
                                                         set: { v in var p = m.store.plan; if p.byArea != v { p.byArea = v; m.store.setPlan(p) } })) {
                    Text("By exam domain").tag(false)
                    Text("By knowledge area").tag(true)
                }
                .pickerStyle(.segmented)
                .padding(.vertical, 6)
                Text("Which order new lessons come in: weakest domain first, or the classroom order. Fixes, reviews and mocks are planned the same either way.")
                    .font(.nunito(12)).foregroundStyle(Palette.onSurfaceVariant).fixedSize(horizontal: false, vertical: true)
            }
            PlannerRemindersCard()
            Spacer().frame(height: 16)
        }
        .sheet(isPresented: $pickingDate) { dateSheet }
    }

    @ViewBuilder private var diagnosticCard: some View {
        let diag = m.store.diagnostic
        PmpCard(container: diag == nil ? Palette.primaryContainer.opacity(0.5) : nil, border: diag == nil ? Palette.primary : nil) {
            SectionLabel("Your starting level")
            if let diag {
                Text("Diagnostic \(pct(diag.pct)) (\(diag.right)/\(diag.n)), \(Time.date(diag.at).formatted(date: .abbreviated, time: .omitted))")
                    .font(.nunito(14, .bold)).padding(.top, 6)
                ForEach(Domain.allCases) { d in
                    if let e = diag.domains[d], e.count >= 2 {
                        ProgressRow(label: d.title, value: e[1] == 0 ? 0 : Double(e[0]) / Double(e[1]), right: "\(e[0])/\(e[1])", color: Palette.domain(d))
                            .padding(.top, 6)
                    }
                }
                Button("Retake the diagnostic") { m.startDiagnostic() }.textButton()
            } else {
                Text("Take the 25-question diagnostic first, cold, without revising. It shows where you stand in each domain, and the plan below aims at whatever comes out lowest.")
                    .font(.nunito(14)).padding(.top, 6).fixedSize(horizontal: false, vertical: true)
                Button { m.startDiagnostic() } label: { Text("Take the diagnostic (about 30 minutes)").frame(maxWidth: .infinity) }
                    .primaryButton().padding(.top, 8)
            }
        }
    }

    private func examDateCard(_ plan: StudyPlan) -> some View {
        PmpCard {
            SectionLabel("Exam date")
            Text(plan.examDate.map { Time.date($0).formatted(date: .complete, time: .omitted) } ?? "Not set")
                .font(.nunito(16, .bold)).padding(.top, 6)
            if let date = plan.examDate {
                let daysLeft = Int((Double(Time.startOfDay(date) - Time.startOfDay(Time.now())) / Double(Time.dayMs)).rounded())
                Text(daysLeft >= 0 ? "\(daysLeft) days to go" : "The date has passed")
                    .font(.system(size: 15, weight: .bold, design: .monospaced)).foregroundStyle(Palette.primary)
            }
            Button(plan.examDate == nil ? "Set exam date" : "Change date") {
                draftDate = Time.date(plan.examDate ?? (Time.now() + 60 * Time.dayMs))
                if draftDate < Date() { draftDate = Date() }
                pickingDate = true
            }
            .primaryButton()
            .padding(.top, 8)
        }
    }

    private var dateSheet: some View {
        NavigationStack {
            DatePicker("Exam date", selection: $draftDate, in: Calendar.current.startOfDay(for: Date())..., displayedComponents: .date)
                .datePickerStyle(.graphical)
                .padding(16)
                .navigationTitle("Exam date")
                .navigationBarTitleDisplayMode(.inline)
                .toolbar {
                    ToolbarItem(placement: .cancellationAction) { Button("Cancel") { pickingDate = false } }
                    ToolbarItem(placement: .confirmationAction) {
                        Button("Save") {
                            let at = Calendar.current.date(bySettingHour: 9, minute: 0, second: 0, of: draftDate) ?? draftDate
                            var p = m.store.plan
                            p.examDate = Time.millis(at)
                            m.store.setPlan(p)
                            pickingDate = false
                        }
                    }
                }
        }
        .presentationDetents([.medium, .large])
    }
}

/** Monday to Sunday, as chips; values are Calendar weekdays (Sunday 1 … Saturday 7). */
private struct PlannerDayChips: View {
    let plan: StudyPlan
    let onChange: (StudyPlan) -> Void
    private let names: [(Int, String)] = [(2, "Mon"), (3, "Tue"), (4, "Wed"), (5, "Thu"), (6, "Fri"), (7, "Sat"), (1, "Sun")]
    var body: some View {
        FlowLayout(spacing: 6) {
            ForEach(names.indices, id: \.self) { i in
                let d = names[i].0, n = names[i].1
                Chip(label: n, selected: plan.studyDays.contains(d)) {
                    var p = plan
                    if p.studyDays.contains(d) { p.studyDays.remove(d) } else { p.studyDays.insert(d) }
                    onChange(p)
                }
            }
        }
    }
}

/* ── smart reminders ── */

private func reminderHourLabel(_ h: Int) -> String { String(format: "%02d:00", h) }

/** The full reminder controls. iPhone has no Usage access, so only the app's own signals are learned from. */
private struct PlannerRemindersCard: View {
    @EnvironmentObject private var m: AppModel
    @Environment(\.scenePhase) private var phase
    @Environment(\.openURL) private var openURL
    @State private var status: UNAuthorizationStatus? = nil

    var body: some View {
        let s = m.reminders.settings
        let now = Time.now()
        let paused = now < s.pausedUntil
        PmpCard {
            Toggle(isOn: Binding(get: { m.reminders.settings.enabled }, set: { on in
                m.updateReminders { $0.settings.enabled = on; $0.settings.asked = true }
                if on && status == .notDetermined { ask() }
            })) {
                VStack(alignment: .leading, spacing: 2) {
                    SectionLabel("Smart reminders")
                    Text(s.enabled ? "On" : "Off").font(.nunito(15, .bold))
                }
            }
            .tint(Palette.primary)
            if s.enabled {
                notificationNotice.padding(.top, 8)
                if paused {
                    Text("Paused until \(Time.date(s.pausedUntil).formatted(date: .abbreviated, time: .shortened)).").font(.nunito(13))
                    Button("Resume now") { m.updateReminders { $0.settings.pausedUntil = 0 } }.textButton()
                }
                ReminderBestTimes(now: now).padding(.top, 10)
                ReminderWhenQuestion().padding(.top, 12)
                if !paused {
                    HStack(spacing: 8) {
                        Button { m.updateReminders { $0.settings.pausedUntil = now + 24 * ReminderBrain.HOUR } } label: { Text("Pause a day").frame(maxWidth: .infinity) }
                            .outlinedButton()
                        Button { m.updateReminders { $0.settings.pausedUntil = now + 3 * 24 * ReminderBrain.HOUR } } label: { Text("Pause 3 days").frame(maxWidth: .infinity) }
                            .outlinedButton()
                    }
                    .padding(.top, 8)
                }
            }
        }
        .onAppear(perform: refresh)
        .onChange(of: phase) { _, p in if p == .active { refresh() } }
    }

    @ViewBuilder private var notificationNotice: some View {
        switch status {
        case .denied?:
            Text("Notifications are off for this app, so reminders can't be shown.").font(.nunito(13)).foregroundStyle(Palette.error)
                .fixedSize(horizontal: false, vertical: true)
            Button { if let u = URL(string: UIApplication.openSettingsURLString) { openURL(u) } } label: { Text("Open Settings").frame(maxWidth: .infinity) }
                .primaryButton()
            Text("Turn them on in Settings → PMP Prep → Notifications.").font(.nunito(12))
        case .notDetermined?:
            Text("Reminders need your permission to show notifications.").font(.nunito(13)).foregroundStyle(Palette.error)
            Button { ask() } label: { Text("Allow reminders").frame(maxWidth: .infinity) }.primaryButton()
        default:
            EmptyView()
        }
    }

    private func ask() {
        ReminderScheduler.requestPermission { granted in
            refresh()
            // Book them now that they can be shown.
            if granted { m.updateReminders { $0.settings.asked = true } }
        }
    }

    private func refresh() {
        Task {
            let st = await UNUserNotificationCenter.current().notificationSettings()
            status = st.authorizationStatus
        }
    }
}

/** "When do you usually study?", plus the waking hours that bound every reminder. */
private struct ReminderWhenQuestion: View {
    @EnvironmentObject private var m: AppModel
    var body: some View {
        let s = m.reminders.settings
        VStack(alignment: .leading, spacing: 6) {
            Text("When do you usually study?").font(.nunito(14, .bold))
            Text("Pick any that fit. It's only a starting guess; the app learns your real times.")
                .font(.nunito(12)).foregroundStyle(Palette.onSurfaceVariant).fixedSize(horizontal: false, vertical: true)
            FlowLayout(spacing: 6) {
                ForEach(StudyWindow.allCases) { w in
                    Chip(label: w.label, selected: s.windows.contains(w)) {
                        m.updateReminders {
                            if $0.settings.windows.contains(w) { $0.settings.windows.remove(w) } else { $0.settings.windows.insert(w) }
                            $0.settings.asked = true
                        }
                    }
                }
            }
            HStack(spacing: 10) {
                ChoiceSelector(label: "I wake up", options: (4...11).map { ($0, reminderHourLabel($0)) }, selected: s.wake) { v in
                    m.updateReminders { $0.settings.wake = v }
                }
                ChoiceSelector(label: "I go to sleep", options: (20...23).map { ($0, reminderHourLabel($0)) } + [(24, "Midnight")], selected: s.sleep) { v in
                    m.updateReminders { $0.settings.sleep = v }
                }
            }
            Text("No reminders before \(reminderHourLabel(s.wake + 1)) or after \(reminderHourLabel(s.sleep - 1)).")
                .font(.nunito(12)).foregroundStyle(Palette.onSurfaceVariant)
        }
    }
}

/** Each waking hour's learned chance; tap an hour to pin it (always remind) or block it (never). */
private struct ReminderBestTimes: View {
    @EnvironmentObject private var m: AppModel
    let now: Millis

    var body: some View {
        let st = m.reminders
        let hours = ReminderBrain.wakingHours(st.settings)
        let scores = ReminderBrain.scores(st, now)
        let resolved = st.logs.filter { $0.kind != .SUMMARY && ReminderBrain.outcome($0, now) != nil }.count
        let best = scores.filter { hours.contains($0.hour) && $0.rule != .BLOCKED && $0.tried > 0 }.max { $0.mean < $1.mean }
        let today = (st.plans[Time.startOfDay(now)] ?? []).filter { $0.at > now }
        VStack(alignment: .leading, spacing: 6) {
            Text("Your best study times").font(.nunito(14, .bold))
            Text(resolved < ReminderBrain.LEARNING_REMINDERS
                 ? "Learning: \(resolved) of \(ReminderBrain.LEARNING_REMINDERS) reminders so far. Up to 3 a day while it learns."
                 : (best.map { "Best so far: \(reminderHourLabel($0.hour)), opened and studied \(Int($0.mean * 100))% of the time. Up to 2 a day." } ?? "Up to 2 a day."))
                .font(.nunito(12)).foregroundStyle(Palette.onSurfaceVariant).fixedSize(horizontal: false, vertical: true)
            ScrollView(.horizontal, showsIndicators: false) {
                HStack(alignment: .bottom, spacing: 4) {
                    ForEach(hours, id: \.self) { hr in hourBar(scores[hr]) }
                }
                .padding(.vertical, 4)
            }
            Text("Tap an hour: ★ always remind, ✕ never, then back to automatic.")
                .font(.nunito(12)).foregroundStyle(Palette.onSurfaceVariant).fixedSize(horizontal: false, vertical: true)
            if !today.isEmpty {
                Text("Still to come today: " + today.map { p in
                    Time.date(p.at).formatted(date: .omitted, time: .shortened) + (p.kind == .STREAK ? " (streak)" : (p.kind == .SUMMARY ? " (weekly summary)" : ""))
                }.joined(separator: ", "))
                .font(.nunito(13)).padding(.top, 2)
            }
        }
    }

    private func hourBar(_ sc: HourScore) -> some View {
        let ruleName: String = sc.rule == .PINNED ? "pinned" : (sc.rule == .BLOCKED ? "blocked" : "automatic")
        let fraction = CGFloat(min(max(sc.mean, 0.08), 1))
        return Button {
            let next: HourRule = sc.rule == .AUTO ? .PINNED : (sc.rule == .PINNED ? .BLOCKED : .AUTO)
            m.updateReminders { $0.settings.rules[sc.hour] = next == .AUTO ? nil : next }
        } label: {
            VStack(spacing: 2) {
                ZStack(alignment: .bottom) {
                    RoundedRectangle(cornerRadius: 9).fill(Palette.secondaryContainer).frame(width: 18, height: 72)
                    if sc.rule != .BLOCKED {
                        RoundedRectangle(cornerRadius: 9)
                            .fill(sc.rule == .PINNED ? Palette.primary : Palette.primary.opacity(0.35 + 0.65 * sc.mean))
                            .frame(width: 18, height: 72 * fraction)
                    }
                }
                Text(sc.rule == .BLOCKED ? "✕" : (sc.rule == .PINNED ? "★" : " ")).font(.nunito(11)).foregroundStyle(Palette.primary)
                Text("\(sc.hour)").font(.nunito(11)).foregroundStyle(Palette.onSurfaceVariant)
            }
            .frame(width: 30)
            .contentShape(Rectangle())
        }
        .buttonStyle(.plain)
        .accessibilityLabel("\(reminderHourLabel(sc.hour)): \(Int(sc.mean * 100))% chance, tried \(sc.tried) times, \(ruleName). Tap to change.")
    }
}
