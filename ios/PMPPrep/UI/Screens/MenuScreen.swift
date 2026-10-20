import SwiftUI

private struct MenuEntry: Identifiable {
    var id: String { label }
    let icon: String
    let label: String
    let detail: String
    /** Still being tested: a Labs flask beside the label. */
    var labs: Bool = false
    let go: () -> Void
}

/**
 * Extra work, beside the guided path. Grouped by what the person wants to do,
 * each row labelled with a live status. Everything done here counts: the path
 * reads it and adjusts.
 */
struct MenuScreen: View {
    @EnvironmentObject private var m: AppModel
    @Environment(\.openURL) private var openURL

    private var groups: [(String, [MenuEntry])] {
        let x: Extras = m.repo.extras
        let missed = m.store.last.values.filter { !$0.correct }.count
        let flagged = m.store.flags.count
        let myCards = m.store.myCards.count
        return [
            ("Start here", [
                MenuEntry(icon: "questionmark.circle", label: "How it works", detail: "What you get, what's measured, how weak spots are handled") { m.navigate(.GUIDE) },
                MenuEntry(icon: "magnifyingglass", label: "Search everything", detail: "Lessons, glossary, formulas, your notes, explanations") { m.navigate(.SEARCH) },
            ]),
            ("Practise", [
                MenuEntry(icon: "list.bullet.clipboard", label: "Build a drill", detail: "Pick domain, task, type, approach, bank") { m.navigate(.PRACTICE) },
                MenuEntry(icon: "arrow.counterclockwise", label: "Redo mistakes", detail: missed == 0 ? "None right now" : "\(missed) wrong last time") {
                    if missed > 0 { m.startPractice(PracticeFilter(which: .MISSED, count: 40), title: "Mistakes") }
                },
                MenuEntry(icon: "flag", label: "Flagged questions", detail: flagged == 0 ? "None flagged" : "\(flagged) flagged") {
                    if flagged > 0 { m.startPractice(PracticeFilter(which: .FLAGGED, count: 40), title: "Flagged") }
                },
                MenuEntry(icon: "brain.head.profile", label: "Situational trainer", detail: "\(x.scenarios.count) ranked dilemmas") { m.navigate(.SITUATIONAL) },
                MenuEntry(icon: "function", label: "Formula lab", detail: "Calculators, formula sheet, calculation drills") { m.navigate(.FORMULAS) },
            ]),
            ("Test yourself", [
                MenuEntry(icon: "doc.text", label: "Mock exam",
                          detail: m.mock != nil ? "In progress · resume" : "\(x.exam.totalItems) questions or shorter · \(m.store.mocks.count) sat") { m.navigate(.MOCK) },
                MenuEntry(icon: "speedometer", label: "Diagnostic",
                          detail: m.store.diagnostic.map { "Last: \(Int($0.pct * 100))% · retake" } ?? "25 questions, taken cold") { m.startDiagnostic() },
            ]),
            ("Study", [
                MenuEntry(icon: "book", label: "Lessons", detail: "\(m.store.lessonsDone.count) of \(x.lessons.count) done") { m.navigate(.LEARN) },
                MenuEntry(icon: "books.vertical", label: "Theory", detail: "\(x.theory.chapters.count) chapters on the discipline in depth · \(m.theoryRead.count) read", labs: true) {
                    m.theoryChapter = nil; m.navigate(.THEORY)
                },
                MenuEntry(icon: "rectangle.on.rectangle", label: "Flashcards",
                          detail: "\(m.deckCounts(nil).due) due" + (myCards > 0 ? " · \(myCards) of your own" : "")) { m.navigate(.FLASHCARDS) },
                MenuEntry(icon: "books.vertical", label: "Reference and glossary", detail: "ECO tasks, \(x.glossary.count) terms, processes") { m.navigate(.REFERENCE) },
            ]),
            ("My progress", [
                MenuEntry(icon: "sparkles", label: "Coach",
                          detail: m.geminiKey.isEmpty ? "How you're doing; connect Gemini to chat" : "How you're doing, and Gemini chat") { m.coachTab = 0; m.navigate(.COACH) },
                MenuEntry(icon: "calendar.badge.checkmark", label: "Am I ready to book?",
                          detail: m.booking().ready ? "Yes: all three conditions met" : "What's left, and a projected date") { m.navigate(.BOOK) },
                MenuEntry(icon: "figure.mind.and.body", label: "Exam fitness", detail: "Stamina, composure and consistency") { m.navigate(.FITNESS) },
                MenuEntry(icon: "calendar", label: "Weekly reports", detail: "Each week compared with the one before") { m.navigate(.REPORTS) },
                MenuEntry(icon: "chart.line.uptrend.xyaxis", label: "Analytics",
                          detail: "Readiness \(m.readiness.report(m.store).score), by task and type") { m.navigate(.ANALYTICS) },
                MenuEntry(icon: "exclamationmark.circle", label: "Mistake journal", detail: "Tag causes, retry") { m.navigate(.MISTAKES) },
                MenuEntry(icon: "note.text", label: "Notes", detail: "Your notes page, also in the coach bubble") { m.coachTab = 2; m.navigate(.COACH) },
            ]),
            ("Settings", [
                MenuEntry(icon: "calendar.badge.clock", label: "Plan and schedule", detail: "Exam date, hours, study days, reminders") { m.navigate(.PLANNER) },
                MenuEntry(icon: "textformat.size", label: "Study settings", detail: "Text size, reading guide, confidence, rest days") { m.navigate(.SETTINGS) },
                MenuEntry(icon: "externaldrive", label: "Question banks and backup", detail: "\(m.pool().count) questions enabled") { m.navigate(.DATA) },
                MenuEntry(icon: "arrow.triangle.2.circlepath", label: "Cloud sync", detail: m.syncStatusLine) { m.navigate(.SYNC) },
                MenuEntry(icon: "arrow.down.app", label: "App updates",
                          detail: m.updateAvailable.map { "Update available: build \($0.build)" } ?? "Version \(AppUpdate.installedVersion)") { m.navigate(.UPDATES) },
            ]),
        ]
    }

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 0) {
                SupportPill { m.navigate(.SUPPORT) }
                .padding(.horizontal, 16)
                .padding(.top, 16)
                // Found by the daily check: one tap to the update.
                if let r = m.updateAvailable {
                    Button { m.navigate(.UPDATES) } label: {
                        HStack(spacing: 10) {
                            Image(systemName: "arrow.down.app.fill").font(.system(size: 20)).foregroundStyle(Palette.primary)
                            VStack(alignment: .leading, spacing: 2) {
                                Text("Update available").font(.nunito(15, .bold)).foregroundStyle(Palette.onSurface)
                                Text("Build \(r.build): tap to update").font(.nunito(13)).foregroundStyle(Palette.onSurfaceVariant)
                            }
                            Spacer(minLength: 0)
                        }
                        .padding(.horizontal, 16).padding(.vertical, 12)
                        .background(RoundedRectangle(cornerRadius: 14).fill(Palette.primaryContainer))
                    }
                    .buttonStyle(.plain)
                    .padding(.horizontal, 16).padding(.top, 12)
                }
                Text("Extra work beside your plan. Anything you do here counts, and the path adjusts to it.")
                    .font(.nunito(13)).foregroundStyle(Palette.onSurfaceVariant)
                    .fixedSize(horizontal: false, vertical: true)
                    .padding(16)
                let all = groups
                ForEach(all.indices, id: \.self) { gi in
                    let title = all[gi].0, entries = all[gi].1
                    SectionLabel(title).padding(.leading, 16).padding(.top, 12).padding(.bottom, 4)
                        .accessibilityAddTraits(.isHeader)
                    ForEach(entries) { e in MenuRow(entry: e) }
                    Divider().padding(.top, 4)
                }
                Spacer().frame(height: 24)
            }
            .frame(maxWidth: 720)
            .frame(maxWidth: .infinity)
        }
    }
}

/** "Warm heart": the amber support pill at the top of the menu. */
private struct SupportPill: View {
    let action: () -> Void
    var body: some View {
        Button(action: action) {
            HStack(spacing: 8) {
                Image(systemName: "heart.fill").font(.system(size: 17, weight: .semibold))
                Text("Support the app").font(.nunito(16, .semibold))
            }
            .foregroundStyle(Palette.supportInk)
            .frame(maxWidth: .infinity)
            .padding(.vertical, 12)
            .background(Capsule().fill(Palette.supportTint))
            .overlay(Capsule().stroke(Palette.warning, lineWidth: 1.5))
            .contentShape(Capsule())
        }
        .buttonStyle(.plain)
        .accessibilityElement(children: .ignore)
        .accessibilityLabel("Support the app on Ko-fi")
        .accessibilityAddTraits(.isButton)
    }
}

private struct MenuRow: View {
    let entry: MenuEntry
    var body: some View {
        Button(action: entry.go) {
            HStack(spacing: 16) {
                Image(systemName: entry.icon).font(.system(size: 20)).foregroundStyle(Palette.primary).frame(width: 24)
                    .accessibilityHidden(true)
                VStack(alignment: .leading, spacing: 2) {
                    HStack(spacing: 8) {
                        Text(entry.label).font(.nunito(16, .semibold)).foregroundStyle(Palette.onSurface)
                        if entry.labs { LabsBadge() }
                    }
                    Text(entry.detail).font(.nunito(13)).foregroundStyle(Palette.onSurfaceVariant).multilineTextAlignment(.leading)
                }
                Spacer(minLength: 0)
                Image(systemName: "chevron.right").font(.system(size: 13, weight: .semibold)).foregroundStyle(Palette.outline)
                    .accessibilityHidden(true)
            }
            .padding(.horizontal, 16).padding(.vertical, 12)
            .contentShape(Rectangle())
        }
        .buttonStyle(.plain)
        .accessibilityElement(children: .combine)
    }
}
