import SwiftUI

/*
 * The formula lab's row-editing calculators (decision tree, float and the critical path with its
 * network diagram, NPV / IRR / payback) and the randomised drill (web §10.06.6–11).
 */

private func fn(_ v: Double, _ dp: Int = 2) -> String { Calculators.n(v, dp) }

/** A compact text field for one cell of an editable row. */
private struct RowField: View {
    let label: String
    @Binding var text: String
    var numeric: Bool = true

    var body: some View {
        TextField(label, text: $text)
            .keyboardType(numeric ? .numbersAndPunctuation : .default)
            .textInputAutocapitalization(.never)
            .autocorrectionDisabled()
            .font(.system(size: 14, design: .monospaced))
            .padding(.horizontal, 8)
            .frame(minHeight: 44)
            .background(RoundedRectangle(cornerRadius: 8).fill(Palette.surface))
            .overlay(RoundedRectangle(cornerRadius: 8).strokeBorder(Palette.outline, lineWidth: 1))
            .accessibilityLabel(label)
    }
}

private struct RemoveRowButton: View {
    let label: String
    let enabled: Bool
    let action: () -> Void

    var body: some View {
        Button(action: action) {
            Image(systemName: "minus.circle").font(.system(size: 18)).foregroundStyle(Palette.onSurfaceVariant).frame(width: 36, height: 44)
        }
        .buttonStyle(.plain)
        .disabled(!enabled)
        .accessibilityLabel(label)
    }
}

/* ── decision tree ───────────────────────────────────────────────────────── */

private struct TreeRow: Identifiable {
    let id = UUID()
    var branch: String
    var p: String
    var payoff: String
}

struct TreeCalc: View {
    @State private var rows: [TreeRow] = [
        TreeRow(branch: "Build new", p: "0.6", payoff: "1200000"),
        TreeRow(branch: "Build new", p: "0.4", payoff: "400000"),
        TreeRow(branch: "Upgrade", p: "0.6", payoff: "700000"),
        TreeRow(branch: "Upgrade", p: "0.4", payoff: "300000"),
    ]
    @State private var costs: [String: String] = ["Build new": "500000", "Upgrade": "180000"]

    private var names: [String] {
        var out: [String] = []
        for r in rows {
            let n: String = r.branch.trimmingCharacters(in: .whitespaces)
            if !n.isEmpty && !out.contains(n) { out.append(n) }
        }
        return out
    }

    private func costBinding(_ name: String) -> Binding<String> {
        Binding<String>(get: { costs[name] ?? "" }, set: { (v: String) in costs[name] = v })
    }

    private func lines() -> [CalcLine] {
        let inputs: [TreeBranchInput] = names.map { (n: String) -> TreeBranchInput in
            let outs: [TreeOutcome] = rows.filter { $0.branch.trimmingCharacters(in: .whitespaces) == n }.map { (r: TreeRow) -> TreeOutcome in
                let p: Double = labNum(r.p)
                let pay: Double = labNum(r.payoff)
                return TreeOutcome(p: p.isNaN ? 0 : p, payoff: pay.isNaN ? 0 : pay)
            }
            let c: Double = labNum(costs[n] ?? "")
            return TreeBranchInput(name: n, cost: c.isNaN ? 0 : c, outcomes: outs)
        }
        let built: [TreeBranch] = FormulaMath.tree(inputs)
        var out: [CalcLine] = []
        for b in built {
            let products: String = b.outcomes.map { "\(fn($0.p)) × \(fn($0.payoff, 0))" }.joined(separator: " + ")
            let pNote: String = b.pOk ? "" : "The probabilities on this branch total \(fn(b.pTotal)), not 1.00."
            out.append(CalcLine(label: "\(b.name): EMV at the chance node", formula: "Σ (P × payoff)", working: products, value: fn(b.emv, 0), reading: pNote))
            out.append(CalcLine(label: "\(b.name): net value", formula: "EMV − cost of the branch", working: "\(fn(b.emv, 0)) − \(fn(b.cost, 0))",
                                value: fn(b.net, 0), reading: b.best ? "The recommendation" : ""))
        }
        if let best = built.first(where: { $0.best }) {
            out.append(CalcLine(label: "Decision", formula: "Largest net value", working: best.name, value: best.name,
                                reading: "Work right to left: roll each chance node up to an EMV, subtract that branch's own cost once, then take the largest net value at the decision node."))
        }
        return out
    }

    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Roll chance nodes up to an EMV, subtract each branch's own cost, and take the best net value.")
                .font(.nunito(13)).foregroundStyle(Palette.onSurfaceVariant).fixedSize(horizontal: false, vertical: true)
            ForEach($rows) { $row in
                HStack(spacing: 6) {
                    RowField(label: "Branch", text: $row.branch, numeric: false)
                    RowField(label: "Probability", text: $row.p).frame(width: 64)
                    RowField(label: "Payoff", text: $row.payoff)
                    RemoveRowButton(label: "Remove this outcome", enabled: rows.count > 2) { rows.removeAll { $0.id == row.id } }
                }
            }
            Button("Add an outcome") { rows.append(TreeRow(branch: names.first ?? "Option A", p: "", payoff: "")) }.textButton()
            SectionLabel("Cost of each branch")
            ForEach(names, id: \.self) { n in
                NumField(label: n, value: costBinding(n))
            }
            CalcResults(lines: lines())
        }
    }
}

/* ── float and the critical path ─────────────────────────────────────────── */

private struct NetRow: Identifiable {
    let id = UUID()
    var name: String
    var dur: String
    var preds: String
}

struct NetworkCalc: View {
    @State private var rows: [NetRow] = [
        NetRow(name: "A", dur: "3", preds: ""), NetRow(name: "B", dur: "4", preds: "A"), NetRow(name: "C", dur: "2", preds: "A"),
        NetRow(name: "D", dur: "5", preds: "B"), NetRow(name: "E", dur: "3", preds: "C"), NetRow(name: "F", dur: "2", preds: "D, E"),
    ]

    private func result() -> NetResult {
        let inputs: [NetInput] = rows.map { (r: NetRow) -> NetInput in
            let d: Double = labNum(r.dur)
            let preds: [String] = r.preds.components(separatedBy: CharacterSet(charactersIn: ", ")).filter { !$0.isEmpty }
            return NetInput(id: r.name, dur: d.isNaN ? 0 : d, preds: preds)
        }
        return FormulaMath.network(inputs)
    }

    var body: some View {
        let r: NetResult = result()
        VStack(alignment: .leading, spacing: 8) {
            Text("Forward and backward passes over a small network, with total float, free float and the critical path.")
                .font(.nunito(13)).foregroundStyle(Palette.onSurfaceVariant).fixedSize(horizontal: false, vertical: true)
            ForEach($rows) { $row in
                HStack(spacing: 6) {
                    RowField(label: "Activity", text: $row.name, numeric: false).frame(width: 64)
                    RowField(label: "Duration", text: $row.dur).frame(width: 64)
                    RowField(label: "Predecessors", text: $row.preds, numeric: false)
                    RemoveRowButton(label: "Remove this activity", enabled: rows.count > 1) { rows.removeAll { $0.id == row.id } }
                }
            }
            Button("Add an activity") { rows.append(NetRow(name: "", dur: "", preds: "")) }.textButton()
            Text("Day numbering: the first activity starts on day 1, so a three-day activity beginning on day 1 finishes on day 3.")
                .font(.nunito(12)).foregroundStyle(Palette.onSurfaceVariant).fixedSize(horizontal: false, vertical: true)
            if r.ok {
                NetworkOutput(r: r)
            } else {
                ForEach(r.errors, id: \.self) { e in
                    Text(e).font(.nunito(13, .semibold)).foregroundStyle(Palette.error)
                }
            }
        }
    }
}

private struct NetworkOutput: View {
    let r: NetResult

    var body: some View {
        let differs: [String] = r.acts.filter { $0.tf != $0.ff }.map { $0.id }
        let critical: String = r.critical.joined(separator: " → ")
        VStack(alignment: .leading, spacing: 8) {
            Text("Each box reads ES · duration · EF across the top and LS · total float · LF beneath. A heavy border marks the critical path.")
                .font(.nunito(12)).foregroundStyle(Palette.onSurfaceVariant).fixedSize(horizontal: false, vertical: true)
            NetDiagram(r: r)
            NetTable(r: r)
            CalcResults(lines: summary(critical: critical, differs: differs))
        }
    }

    private func summary(critical: String, differs: [String]) -> [CalcLine] {
        var out: [CalcLine] = [
            CalcLine(label: "Duration", formula: "The longest path through the network", working: "EF of the last activity", value: "\(fn(r.duration, 0)) days"),
            CalcLine(label: "Critical path", formula: r.multipleCritical ? "More than one chain carries zero float" : "Every activity with zero total float",
                     working: critical, value: critical),
        ]
        if !differs.isEmpty {
            out.append(CalcLine(label: "Float differs on", formula: "Total float ≠ free float", working: differs.joined(separator: ", "), value: differs.joined(separator: ", "),
                                reading: "On these, slipping uses float that belongs to the path rather than to the activity: it delays no successor's early start, but it does consume the total float of everything behind it."))
        }
        return out
    }
}

/** ES, EF, LS, LF, TF and FF for every activity. */
private struct NetTable: View {
    let r: NetResult
    private let heads: [String] = ["Dur", "ES", "EF", "LS", "LF", "TF", "FF"]

    var body: some View {
        VStack(spacing: 4) {
            HStack(spacing: 2) {
                Text("Act").font(.nunito(11, .bold)).frame(width: 36, alignment: .leading)
                ForEach(heads, id: \.self) { h in
                    Text(h).font(.nunito(11, .bold)).foregroundStyle(Palette.onSurfaceVariant).frame(maxWidth: .infinity, alignment: .trailing)
                }
            }
            ForEach(r.acts, id: \.id) { a in
                row(a)
            }
        }
        .padding(10)
        .background(RoundedRectangle(cornerRadius: 10).fill(Palette.surface))
    }

    private func row(_ a: NetActivity) -> some View {
        let values: [Double] = [a.dur, a.es, a.ef, a.ls, a.lf, a.tf, a.ff]
        return HStack(spacing: 2) {
            Text(a.id).font(.system(size: 13, weight: a.crit ? .black : .regular, design: .monospaced)).frame(width: 36, alignment: .leading)
            ForEach(values.indices, id: \.self) { i in
                Text(fn(values[i], 0)).font(.system(size: 12, design: .monospaced)).frame(maxWidth: .infinity, alignment: .trailing)
            }
        }
        .foregroundStyle(a.crit ? Palette.error : Palette.onSurface)
        .accessibilityElement(children: .ignore)
        .accessibilityLabel("\(a.id)\(a.crit ? ", critical" : ""): duration \(fn(a.dur, 0)), early start \(fn(a.es, 0)), early finish \(fn(a.ef, 0)), late start \(fn(a.ls, 0)), late finish \(fn(a.lf, 0)), total float \(fn(a.tf, 0)), free float \(fn(a.ff, 0))")
    }
}

/** Activity-on-node boxes in columns by longest chain from a start, drawn on a Canvas. */
private struct NetDiagram: View {
    let r: NetResult
    private let boxW: CGFloat = 96
    private let boxH: CGFloat = 60
    private let colGap: CGFloat = 30
    private let rowGap: CGFloat = 14

    private func origin(_ id: String) -> CGPoint? {
        for (c, col) in r.columns.enumerated() {
            if let i = col.firstIndex(where: { $0.id == id }) {
                return CGPoint(x: CGFloat(c) * (boxW + colGap) + 2, y: CGFloat(i) * (boxH + rowGap) + 2)
            }
        }
        return nil
    }

    var body: some View {
        let rowsMax: Int = r.columns.map { $0.count }.max() ?? 1
        let width: CGFloat = CGFloat(r.columns.count) * (boxW + colGap) - colGap + 4
        let height: CGFloat = CGFloat(rowsMax) * (boxH + rowGap) - rowGap + 4
        let label: String = "Network diagram. Critical path " + r.critical.joined(separator: ", ") + ", \(fn(r.duration, 0)) days."
        ScrollView(.horizontal, showsIndicators: true) {
            Canvas { ctx, _ in
                drawEdges(ctx)
                for a in r.acts { drawNode(ctx, a) }
            }
            .frame(width: max(width, 10), height: max(height, 10))
            .padding(.vertical, 4)
        }
        .accessibilityElement(children: .ignore)
        .accessibilityLabel(label)
    }

    private func drawEdges(_ ctx: GraphicsContext) {
        for a in r.acts {
            guard let to = origin(a.id) else { continue }
            for p in a.preds {
                guard let from = origin(p) else { continue }
                let start: CGPoint = CGPoint(x: from.x + boxW, y: from.y + boxH / 2)
                let end: CGPoint = CGPoint(x: to.x, y: to.y + boxH / 2)
                var line = Path()
                line.move(to: start)
                line.addLine(to: CGPoint(x: end.x - 5, y: end.y))
                let crit: Bool = a.crit && (r.activity(p)?.crit ?? false)
                ctx.stroke(line, with: .color(crit ? Palette.error : Palette.onSurfaceVariant), lineWidth: crit ? 2 : 1.2)
                var tip = Path()
                tip.move(to: CGPoint(x: end.x - 6, y: end.y - 4))
                tip.addLine(to: end)
                tip.addLine(to: CGPoint(x: end.x - 6, y: end.y + 4))
                tip.closeSubpath()
                ctx.fill(tip, with: .color(crit ? Palette.error : Palette.onSurfaceVariant))
            }
        }
    }

    private func drawNode(_ ctx: GraphicsContext, _ a: NetActivity) {
        guard let o = origin(a.id) else { return }
        let rect: CGRect = CGRect(x: o.x, y: o.y, width: boxW, height: boxH)
        let box: Path = Path(roundedRect: rect, cornerRadius: 6)
        ctx.fill(box, with: .color(a.crit ? Palette.error.opacity(0.10) : Palette.surface))
        ctx.stroke(box, with: .color(a.crit ? Palette.error : Palette.outline), lineWidth: a.crit ? 2.5 : 1)
        let small: Font = .system(size: 10, design: .monospaced)
        let third: CGFloat = boxW / 3
        let top: CGFloat = o.y + 10
        let bottom: CGFloat = o.y + boxH - 10
        let mid: CGFloat = o.y + boxH / 2
        ctx.draw(Text(fn(a.es, 0)).font(small), at: CGPoint(x: o.x + third / 2, y: top))
        ctx.draw(Text(fn(a.dur, 0)).font(small), at: CGPoint(x: o.x + third * 1.5, y: top))
        ctx.draw(Text(fn(a.ef, 0)).font(small), at: CGPoint(x: o.x + third * 2.5, y: top))
        ctx.draw(Text(a.id).font(.system(size: 14, weight: .bold)), at: CGPoint(x: o.x + boxW / 2, y: mid))
        ctx.draw(Text(fn(a.ls, 0)).font(small), at: CGPoint(x: o.x + third / 2, y: bottom))
        ctx.draw(Text("TF " + fn(a.tf, 0)).font(small), at: CGPoint(x: o.x + third * 1.5, y: bottom))
        ctx.draw(Text(fn(a.lf, 0)).font(small), at: CGPoint(x: o.x + third * 2.5, y: bottom))
    }
}

/* ── NPV, IRR and payback ────────────────────────────────────────────────── */

private struct CashRow: Identifiable {
    let id = UUID()
    var c: String
}

struct CashCalc: View {
    @State private var c0 = "400000"
    @State private var rate = "10"
    @State private var rows: [CashRow] = [CashRow(c: "120000"), CashRow(c: "160000"), CashRow(c: "180000"), CashRow(c: "150000")]

    private func lines() -> [CalcLine] {
        let outlay: Double = labNum(c0)
        let ratePct: Double = labNum(rate)
        if outlay.isNaN || ratePct.isNaN { return [] }
        let r: Double = ratePct / 100
        let flows: [Double] = rows.map { (x: CashRow) -> Double in
            let v: Double = labNum(x.c)
            return v.isNaN ? 0 : v
        }
        let c: CashResult = FormulaMath.cash(outlay, flows, r)
        var out: [CalcLine] = []
        for (i, d) in c.discounted.enumerated() {
            let factor: Double = pow(1 + r, Double(i + 1))
            out.append(CalcLine(label: "Year \(i + 1) discounted", formula: "C ÷ (1 + r)^\(i + 1)", working: "\(fn(flows[i], 0)) ÷ \(fn(factor))", value: fn(d)))
        }
        let pvWorking: String = c.discounted.map { fn($0, 0) }.joined(separator: " + ")
        out.append(CalcLine(label: "Present value in", formula: "Σ discounted returns", working: pvWorking, value: fn(c.pvIn)))
        out.append(CalcLine(label: "NPV", formula: "PV in − outlay", working: "\(fn(c.pvIn)) − \(fn(outlay, 0))", value: fn(c.npv),
                            reading: c.npv >= 0 ? "Positive, so the project earns more than the \(fn(ratePct, 0))% discount rate. NPV is already net of the outlay."
                                                : "Negative at \(fn(ratePct, 0))%: the returns do not cover the outlay once discounted."))
        let irrText: String = c.irr.map { "\(fn($0 * 100, 1))%" } ?? "—"
        let irrRead: String
        if let irr = c.irr {
            irrRead = irr >= r ? "Above the discount rate, which is the same verdict the NPV gives." : "Below the discount rate, so the project destroys value."
        } else {
            irrRead = "No single rate zeroes this stream, which happens when the signs alternate."
        }
        out.append(CalcLine(label: "IRR", formula: "The rate at which NPV = 0", working: "found by bisection", value: irrText, reading: irrRead))
        let pb: String = c.payback.map { "\(fn($0)) years" } ?? "not within the horizon"
        out.append(CalcLine(label: "Payback", formula: "Undiscounted, interpolated within the year", working: "cumulative returns against \(fn(outlay, 0))", value: pb,
                            reading: "Payback ignores everything after the recovery date and the time value of money, which is why it is never the only test."))
        let dpb: String = c.discountedPayback.map { "\(fn($0)) years" } ?? "not within the horizon"
        out.append(CalcLine(label: "Discounted payback", formula: "The same test on the discounted flows", working: "cumulative discounted returns", value: dpb))
        if let bcr = c.bcr {
            out.append(CalcLine(label: "BCR", formula: "PV of benefits ÷ outlay", working: "\(fn(c.pvIn, 0)) ÷ \(fn(outlay, 0))", value: fn(bcr),
                                reading: "Compare against 1.0, not against zero."))
        }
        if let roi = c.roi {
            out.append(CalcLine(label: "ROI", formula: "Undiscounted gain ÷ outlay", working: "(Σ returns − outlay) ÷ outlay", value: "\(fn(roi * 100, 1))%"))
        }
        return out
    }

    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Discounted cash flow over an outlay and a stream of returns.")
                .font(.nunito(13)).foregroundStyle(Palette.onSurfaceVariant)
            HStack(spacing: 8) {
                NumField(label: "Initial investment", value: $c0)
                NumField(label: "Discount rate %", value: $rate)
            }
            SectionLabel("Returns by year")
            ForEach(Array(rows.enumerated()), id: \.element.id) { i, row in
                HStack(spacing: 6) {
                    Text("Year \(i + 1)").font(.nunito(13, .semibold)).frame(width: 60, alignment: .leading)
                    RowField(label: "Cash flow, year \(i + 1)", text: cashBinding(row.id))
                    RemoveRowButton(label: "Remove year \(i + 1)", enabled: rows.count > 1) { rows.removeAll { $0.id == row.id } }
                }
            }
            Button("Add a year") { rows.append(CashRow(c: "")) }.textButton()
            CalcResults(lines: lines())
        }
    }

    private func cashBinding(_ id: UUID) -> Binding<String> {
        Binding<String>(
            get: { rows.first(where: { $0.id == id })?.c ?? "" },
            set: { (v: String) in
                if let i = rows.firstIndex(where: { $0.id == id }) { rows[i].c = v }
            })
    }
}

/* ── the randomised drill ────────────────────────────────────────────────── */

/** One generated problem at a time, marked with named wrong turns, the working always shown after. */
struct DrillLab: View {
    @EnvironmentObject private var m: AppModel
    @State private var group: String? = nil
    @State private var problem: DrillProblem = DrillGenerators.newProblem(group: nil)
    @State private var input = ""
    @State private var mark: DrillMark? = nil
    @State private var revealed = false
    @State private var asked = 0
    @State private var right = 0
    @State private var streak = 0
    @State private var best = 0
    @FocusState private var focused: Bool

    private func groupLabel(_ id: String) -> String {
        let x: Extras = m.repo.extras
        return x.formulas.first { $0.id == id }?.label ?? id.capitalized
    }

    private func fresh() {
        problem = DrillGenerators.newProblem(group: group)
        input = ""
        mark = nil
        revealed = false
    }

    private func submit() {
        if revealed { return }
        let res: DrillMark = DrillGenerators.mark(problem, input)
        if res.verdict == .unreadable { return }
        asked += 1
        if res.verdict == .right {
            right += 1
            streak += 1
            best = max(best, streak)
        } else {
            streak = 0
        }
        mark = res
        revealed = true
        focused = false
        m.recordDrill(problem, ok: res.verdict == .right)
    }

    private func showWorking() {
        if revealed { return }
        asked += 1
        streak = 0
        mark = nil
        revealed = true
        focused = false
        m.recordDrill(problem, ok: false)
    }

    private var tally: String {
        if asked == 0 { return "No problems marked yet this session." }
        let share: String = pct(Double(right) / Double(asked))
        let bestText: String = best > streak ? " (best \(best))" : ""
        return "\(right) / \(asked) correct · \(share) · streak \(streak)\(bestText)"
    }

    var body: some View {
        ScreenScroll {
            ScreenTitle(title: "Calculation drill",
                        subtitle: "A fresh problem each time, with the full working after. A known wrong turn is named, so you learn which mistake you made.")
            groupPicker
            Text(tally).font(.system(size: 13, weight: .semibold, design: .monospaced)).foregroundStyle(Palette.primary)
                .accessibilityLabel(asked == 0 ? tally : "\(right) of \(asked) correct, streak \(streak)")
            problemCard
            if revealed {
                DrillResult(problem: problem, mark: mark)
                Button { fresh() } label: { Text("Another problem").frame(maxWidth: .infinity) }.primaryButton()
            }
            BankCalcDrills().padding(.top, 12)
            Spacer().frame(height: 16)
        }
    }

    private var groupPicker: some View {
        let groups: [String] = DrillGenerators.groups
        return FlowLayout(spacing: 6) {
            Chip(label: "Every group", selected: group == nil) { group = nil; fresh() }
            ForEach(groups, id: \.self) { g in
                Chip(label: groupLabel(g), selected: group == g) { group = g; fresh() }
            }
        }
    }

    private var problemCard: some View {
        let p: DrillProblem = problem
        let unitText: String = p.unit == "index" ? "as an index" : (p.unit == "ratio" ? "as a ratio" : p.unit)
        return PmpCard {
            Text("\(groupLabel(p.group).uppercased()) · \(p.label)").font(.nunito(11, .bold)).tracking(0.6).foregroundStyle(Palette.onSurfaceVariant)
            Text(p.stem).font(.nunito(15)).lineSpacing(4).fixedSize(horizontal: false, vertical: true).padding(.top, 4)
            DrillGivens(given: p.given).padding(.top, 6)
            HStack(spacing: 8) {
                TextField(p.unit == "£" ? "0.00" : "0", text: $input)
                    .keyboardType(.numbersAndPunctuation)
                    .font(.system(size: 16, design: .monospaced))
                    .focused($focused)
                    .submitLabel(.done)
                    .onSubmit { submit() }
                    .disabled(revealed)
                    .padding(.horizontal, 12)
                    .frame(minHeight: 48)
                    .background(RoundedRectangle(cornerRadius: 10).fill(Palette.surface))
                    .overlay(RoundedRectangle(cornerRadius: 10).strokeBorder(Palette.outline, lineWidth: 1))
                    .accessibilityLabel("Your answer")
                Text(unitText).font(.nunito(13)).foregroundStyle(Palette.onSurfaceVariant)
            }
            .padding(.top, 8)
            if !revealed {
                HStack(spacing: 8) {
                    Button { submit() } label: { Text("Check").frame(maxWidth: .infinity) }
                        .primaryButton()
                        .disabled(DrillGenerators.parse(input) == nil)
                    Button { showWorking() } label: { Text("Show the working").frame(maxWidth: .infinity) }
                        .outlinedButton()
                }
                .padding(.top, 6)
            }
        }
    }
}

private struct DrillGivens: View {
    let given: [DrillGiven]
    var body: some View {
        VStack(spacing: 4) {
            ForEach(given, id: \.self) { g in
                HStack(alignment: .firstTextBaseline) {
                    Text(g.k).font(.nunito(13)).foregroundStyle(Palette.onSurfaceVariant)
                    Spacer(minLength: 8)
                    Text(g.v).font(.system(size: 13, weight: .semibold, design: .monospaced)).multilineTextAlignment(.trailing)
                }
            }
        }
        .padding(10)
        .background(RoundedRectangle(cornerRadius: 10).fill(Palette.surfaceVariant.opacity(0.6)))
    }
}

/** The verdict, the named wrong turn if there was one, then the worked solution. */
private struct DrillResult: View {
    let problem: DrillProblem
    let mark: DrillMark?

    private var verdict: (String, Color)? {
        guard let mk = mark, let given = mk.given else { return nil }
        let answer: String = DrillFmt.answer(problem)
        let mine: String = DrillFmt.fmt(given, problem.dp)
        switch mk.verdict {
        case .right: return ("Correct — \(answer).", Palette.success)
        case .near: return ("Close: you answered \(mine), and the answer is \(answer).", Palette.warning)
        default: return ("Not right: you answered \(mine), and the answer is \(answer).", Palette.error)
        }
    }

    var body: some View {
        VStack(alignment: .leading, spacing: 10) {
            if let v = verdict {
                Text(v.0).font(.nunito(15, .bold)).foregroundStyle(v.1)
                    .padding(12)
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .background(RoundedRectangle(cornerRadius: 12).fill(v.1.opacity(0.12)))
                    .accessibilityAddTraits(.isHeader)
            }
            if let why = mark?.why {
                Text(why).font(.nunito(14)).lineSpacing(3).fixedSize(horizontal: false, vertical: true)
            }
            PmpCard {
                SectionLabel("Worked solution")
                ForEach(problem.steps.indices, id: \.self) { i in
                    DrillStepView(n: i + 1, step: problem.steps[i])
                }
                HStack {
                    Text("Answer").font(.nunito(14, .bold))
                    Spacer()
                    Text(DrillFmt.answer(problem)).font(.system(size: 16, weight: .black, design: .monospaced)).foregroundStyle(Palette.primary)
                }
                .padding(.top, 6)
                if !problem.trap.isEmpty {
                    Text("Where the mark goes. \(problem.trap)").font(.nunito(13)).foregroundStyle(Palette.warning)
                        .fixedSize(horizontal: false, vertical: true).padding(.top, 4)
                }
                Text("seed \(String(problem.seed, radix: 36))").font(.system(size: 11, design: .monospaced)).foregroundStyle(Palette.onSurfaceVariant)
            }
        }
    }
}

private struct DrillStepView: View {
    let n: Int
    let step: DrillStep

    var body: some View {
        VStack(alignment: .leading, spacing: 6) {
            HStack(alignment: .firstTextBaseline, spacing: 6) {
                Text("\(n).").font(.nunito(14, .bold)).foregroundStyle(Palette.primary)
                Text(step.t).font(.nunito(14)).lineSpacing(3).fixedSize(horizontal: false, vertical: true)
            }
            if let math = step.m {
                ScrollView(.horizontal, showsIndicators: false) {
                    Text(math).font(.system(size: 12, design: .monospaced)).fixedSize()
                        .padding(10)
                }
                .background(RoundedRectangle(cornerRadius: 8).fill(Palette.surfaceVariant.opacity(0.7)))
            }
        }
        .padding(.top, 8)
    }
}
