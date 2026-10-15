import SwiftUI

/** Readiness, how it's built, study days, the mock trend, and accuracy by task, approach, type and pace. */
struct AnalyticsScreen: View {
    @EnvironmentObject private var m: AppModel

    var body: some View {
        let report = m.readiness.report(m.store)
        let atts = m.store.attempts
        let streak = m.readiness.streak(m.store, Time.now())
        ScreenScroll {
            VStack(alignment: .leading, spacing: 4) {
                Text("Readiness \(report.score) / 100").font(.nunito(22, .heavy))
                Text(report.provisional ? "Provisional: " + report.gates.joined(separator: "; ") : "Built on enough evidence to mean something.")
                    .font(.nunito(13)).foregroundStyle(Palette.onSurfaceVariant)
                Button("How the score works") { m.navigate(.GUIDE) }.textButton()
            }
            componentsCard(report)
            studyDaysCard(streak)
            if m.store.mocks.count >= 2 {
                PmpCard {
                    SectionLabel("Mock exam trend")
                    AnalyticsMockTrend(points: m.store.mocks.sorted { $0.at < $1.at }.map(\.pct)).padding(.top, 8)
                }
            }
            taskCard(atts)
            approachCard(atts)
            paceCard(atts)
            Spacer().frame(height: 16)
        }
    }

    private func componentsCard(_ report: ReadinessReport) -> some View {
        PmpCard {
            SectionLabel("How the score is built")
            ForEach(report.components) { c in
                VStack(alignment: .leading, spacing: 4) {
                    ProgressRow(label: "\(c.label) (\(c.weight) pts) · \(c.figure)", value: c.score, right: "+" + String(format: "%.1f", c.points))
                    Text(c.detail).font(.nunito(12)).foregroundStyle(Palette.onSurfaceVariant).fixedSize(horizontal: false, vertical: true)
                }
                .padding(.top, 10)
            }
        }
    }

    private func studyDaysCard(_ streak: Streak) -> some View {
        PmpCard {
            SectionLabel("Study days · last \(streak.last14.count)")
            HStack(spacing: 4) {
                ForEach(Array(streak.last14.enumerated()), id: \.offset) { _, on in
                    RoundedRectangle(cornerRadius: 4).fill(on ? Palette.success : Color.gray.opacity(0.25))
                        .frame(maxWidth: .infinity).frame(height: 22)
                }
            }
            .padding(.top, 8)
            .accessibilityElement(children: .ignore)
            .accessibilityLabel("Studied on \(streak.last14.filter { $0 }.count) of the last \(streak.last14.count) days")
            Text("Current streak \(streak.current) · longest \(streak.longest)")
                .font(.nunito(12)).foregroundStyle(Palette.onSurfaceVariant).padding(.top, 6)
        }
    }

    private func taskCard(_ atts: [Attempt]) -> some View {
        let byTask = m.readiness.group(atts) { $0.ecoTask }
        let rows: [(EcoTask, GroupStat?)] = m.repo.extras.tasks.map { ($0, byTask[$0.id]) }.sorted { a, b in
            if (a.1 == nil) != (b.1 == nil) { return a.1 != nil }
            return (a.1?.accuracy ?? 0) < (b.1?.accuracy ?? 0)
        }
        return PmpCard {
            SectionLabel("By ECO task (weakest first)")
            Text("Tasks with fewer than five answers are marked thin.").font(.nunito(12)).foregroundStyle(Palette.onSurfaceVariant)
            ForEach(rows.indices, id: \.self) { i in
                let t = rows[i].0, g = rows[i].1
                ProgressRow(label: "\(t.code) \(t.label)" + (g != nil && g!.n < 5 ? " (thin)" : ""),
                            value: g?.accuracy ?? 0,
                            right: g.map { "\(pct($0.accuracy)) · \($0.n)" } ?? "—",
                            color: Palette.domain(t.domain))
                    .padding(.top, 8)
            }
        }
    }

    private func approachCard(_ atts: [Attempt]) -> some View {
        let byApproach = m.readiness.group(atts) { $0.approach?.id }
        let byType = m.readiness.group(atts) { $0.type.base.id }
        let types: [QType] = [.single, .multi, .matching, .hotspot, .fillblank]
        return PmpCard {
            SectionLabel("By approach")
            ForEach(Approach.allCases) { a in
                let g = byApproach[a.id]
                ProgressRow(label: a.title, value: g?.accuracy ?? 0, right: g.map { "\(pct($0.accuracy)) · \($0.n)" } ?? "—").padding(.top, 8)
            }
            SectionLabel("By question type").padding(.top, 12)
            ForEach(types) { t in
                let g = byType[t.id]
                ProgressRow(label: t.title, value: g?.accuracy ?? 0, right: g.map { "\(pct($0.accuracy)) · \($0.n)" } ?? "—").padding(.top, 8)
            }
        }
    }

    private func paceCard(_ atts: [Attempt]) -> some View {
        let (median, n) = m.readiness.medianPace(atts)
        let x: Extras = m.repo.extras
        let band = x.readiness.paceBand
        let color: Color = median == nil ? Palette.onSurfaceVariant : (median! < band[0] ? Palette.warning : (median! <= band[1] ? Palette.success : Palette.error))
        let byDomain = m.readiness.group(atts) { $0.domain.id }
        return PmpCard {
            SectionLabel("Pace")
            Text(median.map { "Median \(Int($0))s per question over \(n) answers" } ?? "No timed answers yet.")
                .font(.system(size: 15, weight: .bold, design: .monospaced)).foregroundStyle(color)
            Text("The exam allows about \(x.exam.paceTargetSec)s a question. \(Int(band[0]))–\(Int(band[1]))s is the healthy band; much faster usually means guessing.")
                .font(.nunito(12)).foregroundStyle(Palette.onSurfaceVariant).fixedSize(horizontal: false, vertical: true)
            ForEach(Domain.allCases) { d in
                if let s = byDomain[d.id]?.meanSec { Text("\(d.short): mean \(Int(s))s").font(.nunito(12)) }
            }
        }
    }
}

/** Mock scores over time, with dashed guides at the score bands. */
private struct AnalyticsMockTrend: View {
    let points: [Double]
    var body: some View {
        VStack(alignment: .leading, spacing: 4) {
            Canvas { ctx, size in
                let w = size.width, h = size.height
                let guides: [Double] = [Rules.current.bandBelow, Rules.current.bandTarget, Rules.current.bandAbove]
                for g in guides {
                    let y = h - CGFloat(g) * h
                    var p = Path(); p.move(to: CGPoint(x: 0, y: y)); p.addLine(to: CGPoint(x: w, y: y))
                    ctx.stroke(p, with: .color(Palette.outline), style: StrokeStyle(lineWidth: 1, dash: [4, 4]))
                }
                let step = points.count > 1 ? w / CGFloat(points.count - 1) : w
                let pts = points.enumerated().map { CGPoint(x: CGFloat($0.offset) * step, y: h - CGFloat(min(max($0.element, 0), 1)) * h) }
                var line = Path()
                for (i, pt) in pts.enumerated() { if i == 0 { line.move(to: pt) } else { line.addLine(to: pt) } }
                ctx.stroke(line, with: .color(Palette.primary), style: StrokeStyle(lineWidth: 2, lineCap: .round, lineJoin: .round))
                for pt in pts { ctx.fill(Path(ellipseIn: CGRect(x: pt.x - 3.5, y: pt.y - 3.5, width: 7, height: 7)), with: .color(Palette.primary)) }
            }
            .frame(height: 140)
            .padding(.vertical, 4)
            .accessibilityElement()
            .accessibilityLabel("Mock exam scores, oldest to newest: " + points.map { pct($0) }.joined(separator: ", "))
            Text("Dashed lines at \(pct(Rules.current.bandBelow)), \(pct(Rules.current.bandTarget)) and \(pct(Rules.current.bandAbove)).").font(.nunito(11)).foregroundStyle(Palette.onSurfaceVariant)
        }
    }
}
