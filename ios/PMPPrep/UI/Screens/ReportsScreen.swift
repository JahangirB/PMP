import SwiftUI

/**
 * Weekly reports: where you stood each week (Monday to Sunday), with every figure compared to the
 * week before. This week's is live; a finished week stays as it was.
 */
struct ReportsScreen: View {
    @EnvironmentObject private var m: AppModel
    @State private var against = "last"
    @State private var open: Set<Millis> = []

    var body: some View {
        let weeks = m.store.weekly.sorted { $0.weekStart > $1.weekStart }
        ScreenScroll {
            VStack(alignment: .leading, spacing: 4) {
                Text("Weekly reports").font(.nunito(22, .heavy))
                Text("Each week's knowledge, exam fitness and effort, compared with the week before. ▲ and ▼ show better or worse, not just up or down.")
                    .font(.nunito(13)).foregroundStyle(Palette.onSurfaceVariant).fixedSize(horizontal: false, vertical: true)
            }
            if weeks.isEmpty {
                PmpCard { Text("Your first report appears as soon as you study. It's updated through the week and kept when the week ends.").font(.nunito(15)) }
            } else {
                if weeks.count >= 2 { trendCard(weeks) }
                currentCard(weeks)
                if weeks.count > 1 {
                    Text("Earlier weeks").font(.nunito(17, .heavy))
                    ForEach(1..<weeks.count, id: \.self) { i in
                        earlierCard(weeks[i], before: i + 1 < weeks.count ? weeks[i + 1] : nil)
                    }
                }
            }
            Spacer().frame(height: 24)
        }
    }

    private func trendCard(_ weeks: [WeeklyReport]) -> some View {
        let ordered = Array(weeks.reversed())
        return PmpCard {
            SectionLabel("Trend")
            VStack(spacing: 10) {
                ReportSpark(title: "Readiness", values: ordered.map { Optional($0.readiness) })
                ReportSpark(title: "Exam fitness", values: ordered.map(\.fitness))
                ReportSpark(title: "Minutes studied", values: ordered.map { Optional($0.minutes) })
            }
            .padding(.top, 6)
        }
    }

    private func currentCard(_ weeks: [WeeklyReport]) -> some View {
        let cur = weeks[0]
        let prev: WeeklyReport? = weeks.count > 1 ? weeks[1] : nil
        let first: WeeklyReport? = weeks.count > 2 ? weeks[weeks.count - 1] : nil
        return PmpCard {
            SectionLabel(cur.weekStart == WeeklyReports.weekStart(Time.now()) ? "This week so far" : "Last recorded week")
            Text(reportWeekLabel(cur)).font(.nunito(15, .bold))
            if first != nil {
                HStack(spacing: 16) {
                    ReportToggle(label: "vs last week", on: against == "last") { against = "last" }
                    ReportToggle(label: "vs your first week", on: against == "first") { against = "first" }
                }
                .padding(.vertical, 4)
            }
            ReportComparison(r: cur, p: against == "first" && first != nil ? first : prev).padding(.top, 6)
        }
    }

    private func earlierCard(_ w: WeeklyReport, before: WeeklyReport?) -> some View {
        let isOpen = open.contains(w.weekStart)
        return PmpCard(onTap: { if isOpen { open.remove(w.weekStart) } else { open.insert(w.weekStart) } }) {
            HStack {
                VStack(alignment: .leading, spacing: 2) {
                    Text(reportWeekLabel(w)).font(.nunito(15, .bold))
                    Text("Readiness \(w.readiness) · fitness \(w.fitness.map(String.init) ?? "–") · \(w.minutes) min")
                        .font(.nunito(13)).foregroundStyle(Palette.onSurfaceVariant)
                }
                Spacer()
                Text(isOpen ? "Hide" : "Show").font(.nunito(15, .bold)).foregroundStyle(Palette.primary)
            }
            if isOpen { ReportComparison(r: w, p: before).padding(.top, 8) }
        }
        .accessibilityHint(isOpen ? "Hides the comparison" : "Shows the comparison with the week before")
    }
}

private func reportWeekLabel(_ w: WeeklyReport) -> String {
    let a = Time.date(w.weekStart).formatted(date: .abbreviated, time: .omitted)
    let b = Time.date(w.weekStart + 6 * ExamFitness.DAY).formatted(date: .abbreviated, time: .omitted)
    return "\(a) – \(b)"
}

private struct ReportToggle: View {
    let label: String
    let on: Bool
    let action: () -> Void
    var body: some View {
        Button(action: action) {
            Text(label).font(.nunito(15, on ? .heavy : .regular))
                .foregroundStyle(on ? Palette.primary : Palette.onSurfaceVariant)
                .padding(.vertical, 8)
                .contentShape(Rectangle())
        }
        .buttonStyle(.plain)
        .accessibilityAddTraits(on ? [.isSelected] : [])
    }
}

/** Every figure with its change; the arrow says better or worse, the sign says up or down. */
private struct ReportComparison: View {
    let r: WeeklyReport
    let p: WeeklyReport?
    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            ForEach(WeeklyReports.compare(r, p)) { sec in
                Text(sec.title).font(.nunito(12, .heavy)).foregroundStyle(Palette.onSurfaceVariant).padding(.top, 8).padding(.bottom, 2)
                ForEach(sec.rows) { row in rowView(row) }
            }
        }
    }

    private func rowView(_ row: WeeklyReports.Row) -> some View {
        let text: String
        if let d = row.delta {
            text = d == 0 ? "same" : (row.direction > 0 ? "▲ " : "▼ ") + (d > 0 ? "+\(d)" : "\(d)")
        } else {
            text = "new"
        }
        let color: Color = row.direction == 1 ? Palette.success : (row.direction == -1 ? Palette.error : Palette.onSurfaceVariant)
        return HStack(spacing: 10) {
            Text(row.label).font(.nunito(14)).frame(maxWidth: .infinity, alignment: .leading)
            Text(row.value.map { "\($0)\(row.unit)" } ?? "–").font(.system(size: 14, weight: .bold, design: .monospaced))
            Text(text).font(.system(size: 13, design: .monospaced)).foregroundStyle(color).frame(width: 72, alignment: .leading)
        }
        .padding(.vertical, 3)
        .accessibilityElement(children: .combine)
    }
}

/** One series over the weeks: a thin line, the latest week marked and labelled. */
private struct ReportSpark: View {
    let title: String
    let values: [Int?]
    var body: some View {
        let pts: [(Int, Int)] = values.enumerated().compactMap { i, v in v.map { (i, $0) } }
        HStack(spacing: 0) {
            Text(title).font(.nunito(13)).frame(width: 110, alignment: .leading)
            if pts.count < 2 {
                Text("needs two weeks").font(.nunito(12)).foregroundStyle(Palette.onSurfaceVariant).frame(maxWidth: .infinity, alignment: .leading)
            } else {
                Canvas { ctx, size in
                    let lo = pts.map(\.1).min()!, hi = pts.map(\.1).max()!
                    let n = CGFloat(max(values.count - 1, 1))
                    func at(_ i: Int, _ v: Int) -> CGPoint {
                        let f: CGFloat = hi == lo ? 0.5 : CGFloat(v - lo) / CGFloat(hi - lo)
                        return CGPoint(x: size.width * CGFloat(i) / n, y: size.height - 4 - (size.height - 8) * f)
                    }
                    var base = Path()
                    base.move(to: CGPoint(x: 0, y: size.height - 1)); base.addLine(to: CGPoint(x: size.width, y: size.height - 1))
                    ctx.stroke(base, with: .color(Palette.outline.opacity(0.4)), lineWidth: 1)
                    var line = Path()
                    for (k, p) in pts.enumerated() { if k == 0 { line.move(to: at(p.0, p.1)) } else { line.addLine(to: at(p.0, p.1)) } }
                    ctx.stroke(line, with: .color(Palette.primary), style: StrokeStyle(lineWidth: 2, lineCap: .round, lineJoin: .round))
                    let last = at(pts[pts.count - 1].0, pts[pts.count - 1].1)
                    ctx.fill(Path(ellipseIn: CGRect(x: last.x - 4, y: last.y - 4, width: 8, height: 8)), with: .color(Palette.primary))
                }
                .frame(maxWidth: .infinity)
                .frame(height: 36)
                .accessibilityElement()
                .accessibilityLabel("\(title) by week: " + pts.map { "\($0.1)" }.joined(separator: ", "))
            }
            Text(pts.last.map { "\($0.1)" } ?? "–").font(.system(size: 14, weight: .bold, design: .monospaced))
                .frame(width: 44, alignment: .leading).padding(.leading, 8)
        }
    }
}
