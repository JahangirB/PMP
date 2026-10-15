import SwiftUI

/** "Am I ready to book?": the three conditions, and when the trend says you'll meet them. */
struct BookScreen: View {
    @EnvironmentObject private var m: AppModel

    var body: some View {
        let b = m.booking()
        ScreenScroll {
            Text("Am I ready to book?").font(.nunito(22, .heavy))
            PmpCard(container: (b.ready ? Palette.success : Palette.primary).opacity(0.10)) {
                Text(b.ready ? "Yes. You meet all three." : "Not yet.").font(.nunito(26, .heavy))
                    .foregroundStyle(b.ready ? Palette.success : Palette.onSurface)
                Text(b.ready ? "Knowledge, stamina and real exam practice are all where they need to be. Book it, and keep the plan going until the day."
                     : "Here's what's left, and when your trend says you'll get there.")
                    .font(.nunito(14)).fixedSize(horizontal: false, vertical: true)
                VStack(spacing: 0) {
                    ForEach(b.criteria) { c in
                        HStack(spacing: 0) {
                            Text(c.met ? "✓" : "○").font(.nunito(15, .bold))
                                .foregroundStyle(c.met ? Palette.success : Palette.onSurfaceVariant).frame(width: 24, alignment: .leading)
                            Text(c.label).font(.nunito(14)).frame(maxWidth: .infinity, alignment: .leading)
                            Text("\(c.now) / \(c.target)").font(.system(size: 14, weight: .bold, design: .monospaced))
                        }
                        .padding(.vertical, 4)
                        .accessibilityElement(children: .ignore)
                        .accessibilityLabel("\(c.label): \(c.now) of \(c.target), \(c.met ? "met" : "not met yet")")
                    }
                }
                .padding(.top, 10)
            }
            if !b.ready { projectionCard(b) }
            Text("The thresholds (readiness \(Booking.READINESS)+, exam fitness \(Booking.FITNESS)+, \(Booking.MOCKS) full mocks at Target) are this app's judgement, not PMI's. PMI doesn't publish a pass mark.")
                .font(.nunito(12)).foregroundStyle(Palette.onSurfaceVariant).fixedSize(horizontal: false, vertical: true)
            Spacer().frame(height: 16)
        }
    }

    private func day(_ t: Millis) -> String { Time.date(t).formatted(date: .abbreviated, time: .omitted) }

    private func projectionText(_ b: BookingReport) -> String {
        guard let p = b.projection else {
            return b.criteria.prefix(2).allSatisfy(\.met) ? "Readiness and exam fitness are there already."
                : "A projection needs at least two weekly reports. Keep studying and it appears next week."
        }
        if !p.stalled.isEmpty {
            return "\(p.stalled.joined(separator: " and ")) \(p.stalled.count == 1 ? "isn't" : "aren't") rising yet, so there's no date to project. Weekly reports show how it moves."
        }
        if let e = p.earliest, let l = p.latest { return "At your current pace you'll reach readiness and fitness between \(day(e)) and \(day(l))." }
        if let e = p.earliest { return "At your current pace, from \(day(e)); the trend is too uneven yet to say how late." }
        if let l = p.latest { return "At your current pace you'll reach readiness and fitness around \(day(l)). Rough: it's from fewer than four weeks of reports." }
        return "Not enough of a trend yet."
    }

    private func projectionCard(_ b: BookingReport) -> some View {
        PmpCard {
            SectionLabel("Projected ready date")
            Text(projectionText(b)).font(.nunito(14)).fixedSize(horizontal: false, vertical: true)
            if b.mocksNeeded > 0 {
                Text("Plus \(b.mocksNeeded) more full \(b.mocksNeeded == 1 ? "mock" : "mocks") at Target (65%) or above.")
                    .font(.nunito(14)).padding(.top, 6).fixedSize(horizontal: false, vertical: true)
            }
            if b.projection?.afterExam == true, let exam = m.store.plan.examDate {
                Text("That's after your exam date, \(day(exam)). Consider moving the exam, or adding study hours.")
                    .font(.nunito(14, .bold)).foregroundStyle(Palette.error).padding(.top, 10).fixedSize(horizontal: false, vertical: true)
                Button("Plan and schedule") { m.navigate(.PLANNER) }.outlinedButton().padding(.top, 6)
            }
        }
    }
}
