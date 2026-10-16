import SwiftUI

/*
 * The web app's analytics charts (§11, §10.11), each in a card headed by the question it answers:
 * accuracy trend (rolling 50), the ECO task heat map, the pace histogram against the exam pace,
 * calibration (Sure against Think so / Guess, right against wrong), the mistake causes, and the
 * formula drill log. Every chart carries an accessibility label with its figures.
 */
struct AnalyticsCharts: View {
    @EnvironmentObject private var m: AppModel

    var body: some View {
        let atts: [Attempt] = m.store.attempts
        let x: Extras = m.repo.extras
        let points: [TrendPoint] = AnalyticsData.trend(atts)
        let cells: [HeatCell] = AnalyticsData.heat(atts, tasks: x.tasks)
        let samples: [Double] = AnalyticsData.paceSamples(atts)
        let cal: Calibration = AnalyticsData.calibration(atts)
        let causes: [(RootCause, Int)] = AnalyticsData.causes(Array(m.store.mistakeTags.values))
        let drills: [DrillGroupStat] = AnalyticsData.drillGroups(m.store.drills)
        VStack(alignment: .leading, spacing: 12) {
            ChartCard(eyebrow: "Trend", question: "Are you getting better?") {
                TrendChart(points: points)
            }
            ChartCard(eyebrow: "Coverage and strength", question: "Where are the cold spots?") {
                HeatMap(cells: cells)
            }
            ChartCard(eyebrow: "Pace", question: "Are you working at exam speed?") {
                PaceHistogram(samples: samples, band: x.readiness.paceBand, examSec: x.exam.paceTargetSec)
            }
            ChartCard(eyebrow: "Calibration", question: "Can you tell when you are right?") {
                CalibrationGrid(c: cal)
            }
            ChartCard(eyebrow: "Mistakes", question: "Why do the misses happen?") {
                CauseChart(causes: causes)
            }
            if !drills.isEmpty {
                ChartCard(eyebrow: "Formula drills", question: "Which calculations still slip?") {
                    DrillLogTable(rows: drills)
                }
            }
        }
    }
}

private struct ChartCard<Content: View>: View {
    let eyebrow: String
    let question: String
    @ViewBuilder var content: () -> Content

    var body: some View {
        PmpCard {
            SectionLabel(eyebrow)
            Text(question).font(.nunito(17, .heavy)).accessibilityAddTraits(.isHeader)
            content().padding(.top, 8)
        }
    }
}

private struct ChartNote: View {
    let text: String
    var body: some View {
        Text(text).font(.nunito(12)).foregroundStyle(Palette.onSurfaceVariant).fixedSize(horizontal: false, vertical: true)
    }
}

private func bandColor(_ v: Double) -> Color {
    let b: MockBand = Rules.current.band(v)
    switch b {
    case .above: return Palette.success
    case .target: return Palette.info
    case .below: return Palette.warning
    case .needs: return Palette.error
    }
}

/* ── accuracy trend ──────────────────────────────────────────────────────── */

private struct TrendChart: View {
    let points: [TrendPoint]

    private var plotted: [TrendPoint] {
        let step: Int = max(1, Int((Double(points.count) / 240).rounded(.up)))
        var out: [TrendPoint] = []
        for (k, p) in points.enumerated() where k % step == 0 || k == points.count - 1 { out.append(p) }
        return out
    }

    var body: some View {
        if points.isEmpty {
            ChartNote(text: "This chart needs \(AnalyticsData.START) answers before it says anything true.")
        } else {
            let pts: [TrendPoint] = plotted
            let last: TrendPoint = pts[pts.count - 1]
            VStack(alignment: .leading, spacing: 6) {
                Canvas { ctx, size in
                    draw(ctx, size, pts)
                }
                .frame(height: 170)
                .accessibilityElement()
                .accessibilityLabel("Accuracy over your last \(AnalyticsData.WINDOW) answers, across \(last.i) answers, currently \(pct(last.v)).")
                ChartNote(text: "Rolling mean of your last \(AnalyticsData.WINDOW) answers, using part marks and leaving out answers reached with hints. The dashed opening is the same figure before \(AnalyticsData.WINDOW) answers existed to average. Now \(pct(last.v)).")
            }
        }
    }

    private func draw(_ ctx: GraphicsContext, _ size: CGSize, _ pts: [TrendPoint]) {
        let left: CGFloat = 34
        let w: CGFloat = max(1, size.width - left - 6)
        let h: CGFloat = max(1, size.height - 18)
        let maxI: CGFloat = CGFloat(max(pts.last?.i ?? 1, 1))
        func px(_ i: Int) -> CGFloat { left + CGFloat(i) / maxI * w }
        func py(_ v: Double) -> CGFloat { 4 + (1 - CGFloat(min(max(v, 0), 1))) * (h - 4) }
        for g in [0.0, 0.25, 0.5, 0.75, 1.0] {
            var line = Path()
            line.move(to: CGPoint(x: left, y: py(g)))
            line.addLine(to: CGPoint(x: left + w, y: py(g)))
            ctx.stroke(line, with: .color(Palette.outline.opacity(0.6)), lineWidth: 0.6)
            ctx.draw(Text("\(Int(g * 100))%").font(.system(size: 9)).foregroundColor(Palette.onSurfaceVariant),
                     at: CGPoint(x: left - 4, y: py(g)), anchor: .trailing)
        }
        var thin = Path()
        var full = Path()
        var thinStarted = false
        var fullStarted = false
        for p in pts {
            let pt: CGPoint = CGPoint(x: px(p.i), y: py(p.v))
            if !p.full {
                if thinStarted { thin.addLine(to: pt) } else { thin.move(to: pt); thinStarted = true }
            } else {
                if !fullStarted {
                    if thinStarted { thin.addLine(to: pt) }
                    full.move(to: pt)
                    fullStarted = true
                } else {
                    full.addLine(to: pt)
                }
            }
        }
        ctx.stroke(thin, with: .color(Palette.primary.opacity(0.45)), style: StrokeStyle(lineWidth: 1.5, dash: [4, 3]))
        ctx.stroke(full, with: .color(Palette.primary), style: StrokeStyle(lineWidth: 2, lineCap: .round, lineJoin: .round))
        if let last = pts.last {
            let c: CGPoint = CGPoint(x: px(last.i), y: py(last.v))
            ctx.fill(Path(ellipseIn: CGRect(x: c.x - 4, y: c.y - 4, width: 8, height: 8)), with: .color(Palette.primary))
        }
        let lastI: Int = pts.last?.i ?? 1
        ctx.draw(Text("1").font(.system(size: 9)).foregroundColor(Palette.onSurfaceVariant), at: CGPoint(x: px(1), y: size.height - 6))
        ctx.draw(Text("\(lastI) answers").font(.system(size: 9)).foregroundColor(Palette.onSurfaceVariant),
                 at: CGPoint(x: left + w, y: size.height - 6), anchor: .trailing)
    }
}

/* ── ECO task heat map ───────────────────────────────────────────────────── */

private struct HeatMap: View {
    @EnvironmentObject private var m: AppModel
    let cells: [HeatCell]

    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            ForEach(Domain.allCases) { d in
                let inD: [HeatCell] = cells.filter { $0.task.domain == d }
                if !inD.isEmpty {
                    Text(d.title).font(.nunito(13, .bold)).foregroundStyle(Palette.domain(d))
                    LazyVGrid(columns: [GridItem(.adaptive(minimum: 96), spacing: 6)], spacing: 6) {
                        ForEach(inD, id: \.task.id) { c in
                            cell(c)
                        }
                    }
                }
            }
            ChartNote(text: "Grey cells have fewer than \(AnalyticsData.MIN_SAMPLE) answers and show how many you have instead of a rate, because a rate from two answers is not a rate. Tap any cell to drill that task.")
        }
    }

    private func cell(_ c: HeatCell) -> some View {
        let fill: Color = c.thin ? Color.gray.opacity(0.18) : bandColor(c.accuracy).opacity(0.22)
        let edge: Color = c.thin ? Color.gray.opacity(0.4) : bandColor(c.accuracy)
        let value: String = c.thin ? "\(c.n)/\(AnalyticsData.MIN_SAMPLE)" : pct(c.accuracy)
        let spoken: String = c.thin
            ? (c.n == 0 ? "no answers yet" : "\(c.n) answers so far, too few to report a rate")
            : "\(pct(c.accuracy)) across \(c.n) answers"
        return Button {
            m.startPractice(PracticeFilter(task: c.task.id, count: 10), title: "Practice: \(c.task.label)")
        } label: {
            VStack(alignment: .leading, spacing: 2) {
                Text(c.task.code).font(.nunito(12, .bold)).foregroundStyle(Palette.onSurface)
                Text(value).font(.system(size: 13, weight: .bold, design: .monospaced)).foregroundStyle(c.thin ? Palette.onSurfaceVariant : Palette.onSurface)
            }
            .padding(8)
            .frame(maxWidth: .infinity, minHeight: 48, alignment: .leading)
            .background(RoundedRectangle(cornerRadius: 8).fill(fill))
            .overlay(RoundedRectangle(cornerRadius: 8).strokeBorder(edge, lineWidth: 1))
        }
        .buttonStyle(.plain)
        .accessibilityLabel("\(c.task.code) \(c.task.label), \(spoken)")
        .accessibilityHint("Opens a drill on this task")
    }
}

/* ── pace histogram ──────────────────────────────────────────────────────── */

private struct PaceHistogram: View {
    let samples: [Double]
    let band: [Double]
    let examSec: Int

    var body: some View {
        if samples.isEmpty {
            ChartNote(text: "No timed answers yet.")
        } else {
            let bins: [PaceBin] = AnalyticsData.paceBins(samples)
            let median: Double = AnalyticsData.median(samples) ?? 0
            let over: Int = samples.filter { $0 > Double(examSec) }.count
            VStack(alignment: .leading, spacing: 6) {
                Canvas { ctx, size in
                    draw(ctx, size, bins, median)
                }
                .frame(height: 170)
                .accessibilityElement()
                .accessibilityLabel(spoken(bins, median, over))
                ChartNote(text: "Every timed answer, in 15-second bins. The shaded strip is the \(Int(band.first ?? 45))–\(Int(band.count > 1 ? band[1] : 80))-second band the readiness score gives full marks for; the dashed line is the exam's \(examSec) seconds a question. Median \(Int(median.rounded()))s; \(over) of \(samples.count) answers took longer than \(examSec)s.")
            }
        }
    }

    private func spoken(_ bins: [PaceBin], _ median: Double, _ over: Int) -> String {
        let parts: [String] = bins.filter { $0.n > 0 }.map { (b: PaceBin) -> String in
            let range: String = b.hi == nil ? "\(b.lo) seconds and over" : "\(b.lo) to \(b.hi ?? 0) seconds"
            return "\(range): \(b.n)"
        }
        return "Time per question. Median \(Int(median.rounded())) seconds, \(over) over \(examSec) seconds. " + parts.joined(separator: ", ")
    }

    private func draw(_ ctx: GraphicsContext, _ size: CGSize, _ bins: [PaceBin], _ median: Double) {
        let left: CGFloat = 6
        let top: CGFloat = 14
        let w: CGFloat = max(1, size.width - left * 2)
        let h: CGFloat = max(1, size.height - top - 18)
        let total: CGFloat = CGFloat(AnalyticsData.BIN * AnalyticsData.BINS)
        let peak: Int = max(1, bins.map { $0.n }.max() ?? 1)
        let bw: CGFloat = w / CGFloat(bins.count)
        func xs(_ sec: Double) -> CGFloat { left + min(CGFloat(sec), total) / total * w }
        let b0: Double = band.first ?? 45
        let b1: Double = band.count > 1 ? band[1] : 80
        let shade: CGRect = CGRect(x: xs(b0), y: top, width: xs(b1) - xs(b0), height: h)
        ctx.fill(Path(shade), with: .color(Palette.success.opacity(0.12)))
        for (k, b) in bins.enumerated() where b.n > 0 {
            let bh: CGFloat = max(1, CGFloat(b.n) / CGFloat(peak) * h)
            let rect: CGRect = CGRect(x: left + CGFloat(k) * bw + 2, y: top + h - bh, width: max(1, bw - 4), height: bh)
            ctx.fill(Path(roundedRect: rect, cornerRadius: 2), with: .color(Palette.primary.opacity(0.8)))
        }
        var base = Path()
        base.move(to: CGPoint(x: left, y: top + h))
        base.addLine(to: CGPoint(x: left + w, y: top + h))
        ctx.stroke(base, with: .color(Palette.outline), lineWidth: 1)
        var exam = Path()
        exam.move(to: CGPoint(x: xs(Double(examSec)), y: top - 4))
        exam.addLine(to: CGPoint(x: xs(Double(examSec)), y: top + h))
        ctx.stroke(exam, with: .color(Palette.error), style: StrokeStyle(lineWidth: 1.5, dash: [4, 3]))
        ctx.draw(Text("\(examSec)s").font(.system(size: 9, weight: .bold)).foregroundColor(Palette.error), at: CGPoint(x: xs(Double(examSec)), y: 5))
        var med = Path()
        med.move(to: CGPoint(x: xs(median), y: top))
        med.addLine(to: CGPoint(x: xs(median), y: top + h))
        ctx.stroke(med, with: .color(Palette.onSurface), lineWidth: 1)
        for k in stride(from: 0, through: AnalyticsData.BINS, by: 4) {
            let label: String = k == AnalyticsData.BINS ? "\((k - 1) * AnalyticsData.BIN)+" : "\(k * AnalyticsData.BIN)"
            ctx.draw(Text(label).font(.system(size: 9)).foregroundColor(Palette.onSurfaceVariant), at: CGPoint(x: left + CGFloat(k) * bw, y: size.height - 6))
        }
    }
}

/* ── calibration ─────────────────────────────────────────────────────────── */

private struct CalibrationGrid: View {
    let c: Calibration

    private var verdict: String {
        let need: Int = AnalyticsData.MIN_SAMPLE
        if c.sure < need || c.unsure < need {
            return "One of the two rows is still under \(need) answers, so the comparison is not being drawn yet."
        }
        let sureAcc: Double = Double(c.sureRight) / Double(c.sure)
        let unsureAcc: Double = Double(c.unsureRight) / Double(c.unsure)
        if sureAcc < 0.7 {
            return "You are wrong about a third of the time when you feel sure, and that is the expensive kind of wrong: on the day nothing will prompt you to look again. The mistake journal is where to find out why."
        }
        if sureAcc - unsureAcc < 0.1 {
            return "Your two confidence states score about the same, which means the feeling is not carrying information yet."
        }
        return "Sure and unsure are separating cleanly, so your own sense of a question is worth trusting, including when it tells you to slow down and re-read."
    }

    var body: some View {
        if c.total == 0 {
            ChartNote(text: "No confidence marks yet. Turn on \"Ask how sure I am\" in Study settings and answer Sure, Think so or Guess before checking.")
        } else {
            VStack(alignment: .leading, spacing: 8) {
                Grid(horizontalSpacing: 6, verticalSpacing: 6) {
                    GridRow {
                        Text("").gridColumnAlignment(.leading)
                        head("Right")
                        head("Wrong")
                    }
                    GridRow {
                        rowLabel(Rules.current.confidenceLabel(Attempt.SURE), c.sure)
                        box(c.sureRight, of: c.sure, good: true)
                        box(c.sureWrong, of: c.sure, good: false)
                    }
                    GridRow {
                        rowLabel("\(Rules.current.confidenceLabel(Attempt.THINK)) / \(Rules.current.confidenceLabel(Attempt.GUESS))", c.unsure)
                        box(c.unsureRight, of: c.unsure, good: true)
                        box(c.unsureWrong, of: c.unsure, good: false)
                    }
                }
                .accessibilityElement(children: .ignore)
                .accessibilityLabel("Calibration. Sure: \(c.sureRight) right, \(c.sureWrong) wrong. Think so or guess: \(c.unsureRight) right, \(c.unsureWrong) wrong.")
                ChartNote(text: "The cell that matters is Sure and wrong: those are the ones a second look on the day will not catch. " + verdict)
            }
        }
    }

    private func head(_ t: String) -> some View {
        Text(t).font(.nunito(12, .bold)).foregroundStyle(Palette.onSurfaceVariant).frame(maxWidth: .infinity)
    }

    private func rowLabel(_ t: String, _ n: Int) -> some View {
        VStack(alignment: .leading, spacing: 0) {
            Text(t).font(.nunito(13, .semibold))
            Text("\(n) answers").font(.nunito(11)).foregroundStyle(Palette.onSurfaceVariant)
        }
        .frame(minWidth: 80, alignment: .leading)
    }

    private func box(_ k: Int, of n: Int, good: Bool) -> some View {
        let thin: Bool = n < AnalyticsData.MIN_SAMPLE
        let share: String = n == 0 ? "—" : pct(Double(k) / Double(n))
        let tint: Color = thin ? Color.gray : (good ? Palette.success : Palette.error)
        return VStack(spacing: 0) {
            Text("\(k)").font(.system(size: 18, weight: .bold, design: .monospaced))
            Text(thin ? "too few" : share).font(.nunito(11)).foregroundStyle(Palette.onSurfaceVariant)
        }
        .frame(maxWidth: .infinity, minHeight: 56)
        .background(RoundedRectangle(cornerRadius: 8).fill(tint.opacity(0.14)))
        .overlay(RoundedRectangle(cornerRadius: 8).strokeBorder(tint.opacity(0.6), lineWidth: 1))
    }
}

/* ── mistake causes ──────────────────────────────────────────────────────── */

private struct CauseChart: View {
    let causes: [(RootCause, Int)]

    var body: some View {
        let total: Int = causes.reduce(0) { $0 + $1.1 }
        if total == 0 {
            ChartNote(text: "No tagged mistakes yet. Tag why a miss happened in the mistake journal and the pattern shows here.")
        } else {
            let peak: Int = max(1, causes.map { $0.1 }.max() ?? 1)
            VStack(alignment: .leading, spacing: 8) {
                Canvas { ctx, size in
                    draw(ctx, size, peak)
                }
                .frame(height: CGFloat(causes.count) * 26)
                .accessibilityElement()
                .accessibilityLabel("Mistake causes: " + causes.map { "\($0.0.title) \($0.1)" }.joined(separator: ", "))
                ChartNote(text: "\(total) tagged misses. Under eight tags a leading cause is arithmetic rather than news.")
            }
        }
    }

    private func draw(_ ctx: GraphicsContext, _ size: CGSize, _ peak: Int) {
        let labelW: CGFloat = min(170, size.width * 0.45)
        let barMax: CGFloat = max(1, size.width - labelW - 34)
        for (i, item) in causes.enumerated() {
            let y: CGFloat = CGFloat(i) * 26
            ctx.draw(Text(item.0.title).font(.system(size: 12)).foregroundColor(Palette.onSurface),
                     at: CGPoint(x: 0, y: y + 12), anchor: .leading)
            let bw: CGFloat = CGFloat(item.1) / CGFloat(peak) * barMax
            let rect: CGRect = CGRect(x: labelW, y: y + 4, width: max(item.1 > 0 ? 2 : 0, bw), height: 16)
            ctx.fill(Path(roundedRect: rect, cornerRadius: 3), with: .color(Palette.tertiary.opacity(0.8)))
            ctx.draw(Text("\(item.1)").font(.system(size: 11, weight: .bold, design: .monospaced)).foregroundColor(Palette.onSurface),
                     at: CGPoint(x: labelW + bw + 6, y: y + 12), anchor: .leading)
        }
    }
}

/* ── formula drill log ───────────────────────────────────────────────────── */

private struct DrillLogTable: View {
    @EnvironmentObject private var m: AppModel
    let rows: [DrillGroupStat]

    private func label(_ id: String) -> String {
        if id == "other" { return "Other" }
        let x: Extras = m.repo.extras
        return x.formulas.first { $0.id == id }?.label ?? id
    }

    var body: some View {
        let total: Int = rows.reduce(0) { $0 + $1.n }
        VStack(alignment: .leading, spacing: 6) {
            ForEach(rows, id: \.group) { r in
                ProgressRow(label: label(r.group), value: r.n == 0 ? 0 : Double(r.right) / Double(r.n),
                            right: "\(r.right)/\(r.n) · \(Time.date(r.last).formatted(date: .abbreviated, time: .omitted))")
            }
            ChartNote(text: "\(total) problems marked in the formula lab. They count as study days but stay out of accuracy: a generated sum is practice at arithmetic, not an exam item.")
        }
    }
}
