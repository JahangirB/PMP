import SwiftUI

/** Calculators with the working shown, the formula sheet, and calculation drills. */
struct FormulaLabScreen: View {
    @State private var tab = 0

    var body: some View {
        VStack(spacing: 0) {
            Picker("Section", selection: $tab) {
                Text("Calculators").tag(0)
                Text("Formula sheet").tag(1)
                Text("Drills").tag(2)
            }
            .pickerStyle(.segmented)
            .padding(.horizontal, 16).padding(.vertical, 8)
            switch tab {
            case 0: CalculatorTab()
            case 1: FormulaSheet()
            default: DrillLab()
            }
        }
    }
}

/** "1,234.5" → 1234.5; anything unreadable is NaN, which the calculators show as "—". */
func labNum(_ s: String) -> Double {
    Double(s.replacingOccurrences(of: ",", with: "").trimmingCharacters(in: .whitespaces)) ?? .nan
}

struct NumField: View {
    let label: String
    @Binding var value: String
    var negative = false

    var body: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text(label).font(.nunito(12, .semibold)).foregroundStyle(Palette.onSurfaceVariant).lineLimit(1).minimumScaleFactor(0.8)
            TextField(label, text: $value)
                .keyboardType(negative ? .numbersAndPunctuation : .decimalPad)
                .font(.system(size: 15, design: .monospaced))
                .padding(.horizontal, 12)
                .frame(minHeight: 48)
                .background(RoundedRectangle(cornerRadius: 10).fill(Palette.surface))
                .overlay(RoundedRectangle(cornerRadius: 10).strokeBorder(Palette.outline, lineWidth: 1))
        }
        .frame(maxWidth: .infinity)
    }
}

struct CalcResults: View {
    let lines: [CalcLine]
    var body: some View {
        ForEach(lines) { l in
            PmpCard {
                HStack(alignment: .firstTextBaseline) {
                    Text(l.label).font(.nunito(14, .bold))
                    Spacer()
                    Text(l.value).font(.system(size: 16, weight: .black, design: .monospaced)).foregroundStyle(Palette.primary)
                }
                Text(l.formula).font(.system(size: 13, design: .monospaced)).foregroundStyle(Palette.onSurfaceVariant)
                Text("= \(l.working)").font(.system(size: 12, design: .monospaced)).foregroundStyle(Palette.onSurfaceVariant)
                if !l.reading.trimmingCharacters(in: .whitespaces).isEmpty {
                    Text(l.reading).font(.nunito(13)).lineSpacing(3)
                }
            }
            .accessibilityElement(children: .combine)
        }
    }
}

private struct CalculatorTab: View {
    @State private var calc = 0
    private let names = ["EVM", "PERT", "PTA", "EMV", "Channels", "Finance", "Agile", "Decision tree", "Float & critical path", "NPV · IRR · payback"]

    @State private var pv = "80000"
    @State private var ev = "70000"
    @State private var ac = "84000"
    @State private var bac = "200000"
    @State private var o = "10"
    @State private var ml = "14"
    @State private var p = "24"
    @State private var tc = "1000000"
    @State private var tp = "1100000"
    @State private var cp = "1200000"
    @State private var share = "0.8"
    @State private var prob = "30"
    @State private var impact = "-50000"
    @State private var people = "12"
    @State private var inv = "500000"
    @State private var ben = "150000"
    @State private var yrs = "5"
    @State private var rate = "8"
    @State private var wip = "24"
    @State private var thr = "6"
    @State private var rem = "120"
    @State private var vel = "25"

    var body: some View {
        ScreenScroll(spacing: 10) {
            FlowLayout(spacing: 6) {
                ForEach(names.indices, id: \.self) { i in
                    Chip(label: names[i], selected: calc == i) { calc = i }
                }
            }
            switch calc {
            case 0:
                HStack(spacing: 8) { NumField(label: "PV", value: $pv); NumField(label: "EV", value: $ev) }
                HStack(spacing: 8) { NumField(label: "AC", value: $ac); NumField(label: "BAC", value: $bac) }
                CalcResults(lines: Calculators.evm(pv: labNum(pv), ev: labNum(ev), ac: labNum(ac), bac: labNum(bac)))
            case 1:
                HStack(spacing: 8) {
                    NumField(label: "Optimistic", value: $o)
                    NumField(label: "Most likely", value: $ml)
                    NumField(label: "Pessimistic", value: $p)
                }
                CalcResults(lines: Calculators.pert(o: labNum(o), m: labNum(ml), p: labNum(p)))
            case 2:
                Text("Fixed price incentive fee contract").font(.nunito(13)).foregroundStyle(Palette.onSurfaceVariant)
                HStack(spacing: 8) { NumField(label: "Target cost", value: $tc); NumField(label: "Target price", value: $tp) }
                HStack(spacing: 8) { NumField(label: "Ceiling price", value: $cp); NumField(label: "Buyer share (0–1)", value: $share) }
                CalcResults(lines: Calculators.pta(targetCost: labNum(tc), targetPrice: labNum(tp), ceilingPrice: labNum(cp), buyerShare: labNum(share)))
            case 3:
                HStack(spacing: 8) { NumField(label: "Probability %", value: $prob); NumField(label: "Impact", value: $impact, negative: true) }
                CalcResults(lines: Calculators.emv(probabilityPct: labNum(prob), impact: labNum(impact)))
            case 4:
                NumField(label: "People on the project", value: $people)
                let n = labNum(people)
                if !n.isNaN && n >= 1 && n < 100_000 {
                    CalcResults(lines: Calculators.channels(Int(n)))
                }
            case 5:
                HStack(spacing: 8) { NumField(label: "Investment", value: $inv); NumField(label: "Benefit / year", value: $ben) }
                HStack(spacing: 8) { NumField(label: "Years", value: $yrs); NumField(label: "Discount rate %", value: $rate) }
                let y = labNum(yrs)
                if !y.isNaN && y >= 1 && y <= 100 {
                    CalcResults(lines: Calculators.finance(investment: labNum(inv), annualBenefit: labNum(ben), years: Int(y), ratePct: labNum(rate)))
                }
            case 7:
                TreeCalc()
            case 8:
                NetworkCalc()
            case 9:
                CashCalc()
            default:
                HStack(spacing: 8) { NumField(label: "Work in progress", value: $wip); NumField(label: "Throughput / week", value: $thr) }
                CalcResults(lines: Calculators.littlesLaw(wip: labNum(wip), throughputPerWeek: labNum(thr)))
                HStack(spacing: 8) { NumField(label: "Points remaining", value: $rem); NumField(label: "Velocity", value: $vel) }
                CalcResults(lines: Calculators.sprints(remainingPoints: labNum(rem), velocity: labNum(vel)))
            }
            Spacer().frame(height: 16)
        }
    }
}

private struct FormulaSheet: View {
    @EnvironmentObject private var m: AppModel

    var body: some View {
        ScrollView {
            LazyVStack(alignment: .leading, spacing: 10) {
                ForEach(m.repo.extras.formulas) { g in
                    SectionLabel(g.label).padding(.top, 4)
                    if !g.blurb.trimmingCharacters(in: .whitespaces).isEmpty {
                        Text(g.blurb).font(.nunito(13)).foregroundStyle(Palette.onSurfaceVariant)
                    }
                    ForEach(g.items) { f in
                        FormulaCard(f: f)
                    }
                }
                Spacer().frame(height: 16)
            }
            .padding(16)
            .frame(maxWidth: 720)
            .frame(maxWidth: .infinity)
        }
    }
}

private struct FormulaCard: View {
    let f: FormulaItem
    private func has(_ s: String) -> Bool { !s.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty }

    var body: some View {
        PmpCard {
            HStack(alignment: .firstTextBaseline) {
                Text(f.name).font(.nunito(15, .bold))
                Spacer()
                if has(f.tag) { Text(f.tag).font(.nunito(14, .bold)).foregroundStyle(Palette.primary) }
            }
            Text(f.expr).font(.system(size: 16, weight: .bold, design: .monospaced)).foregroundStyle(Palette.primary)
                .padding(.top, 6)
            if has(f.where) {
                Text(f.where).font(.nunito(13)).foregroundStyle(Palette.onSurfaceVariant).padding(.top, 4)
            }
            if has(f.reads) {
                Text(f.reads).font(.nunito(14)).lineSpacing(4).padding(.top, 4)
            }
            if has(f.trap) {
                Text("Trap: \(f.trap)").font(.nunito(13)).foregroundStyle(Palette.warning).lineSpacing(3).padding(.top, 6)
            }
            if let e = f.example {
                WorkedExampleView(e: e).padding(.top, 6)
            }
        }
    }
}

/** A formula's worked example, folded until asked for (index.html §10.06.2). */
private struct WorkedExampleView: View {
    let e: FormulaExample
    @State private var open: Bool = false

    var body: some View {
        VStack(alignment: .leading, spacing: 4) {
            Button(open ? "Hide the worked example" : "Worked example") { open.toggle() }
                .font(.nunito(14, .bold))
            if open {
                Text(e.situation).font(.nunito(14)).lineSpacing(4)
                ForEach(Array(e.steps.enumerated()), id: \.offset) { pair in
                    Text(pair.element).font(.system(size: 13, design: .monospaced))
                }
                if !e.means.isEmpty {
                    Text(e.means).font(.nunito(14)).foregroundStyle(Palette.onSurfaceVariant).lineSpacing(4).padding(.top, 2)
                }
            }
        }
    }
}

/** Worked calculation questions from the question banks, under the generated drills. */
struct BankCalcDrills: View {
    @EnvironmentObject private var m: AppModel

    var body: some View {
        let numeric = m.pool().filter { q in q.base == .fillblank && q.blanks.contains { $0.numeric } }
        VStack(alignment: .leading, spacing: 12) {
            Text("Calculation questions from your banks").font(.nunito(17, .heavy))
            Text("\(numeric.count) worked calculation questions from your question banks, marked to the tolerance their authors set.")
                .font(.nunito(13)).foregroundStyle(Palette.onSurfaceVariant)
            ForEach([5, 10, 20], id: \.self) { n in
                Button {
                    m.startPracticeWith(Array(numeric.shuffled().prefix(n)), title: "Calculation drill")
                } label: { Text("\(n) calculation questions").frame(maxWidth: .infinity) }
                    .primaryButton()
                    .disabled(numeric.isEmpty)
            }
            Button {
                m.startPractice(PracticeFilter(type: .fillblank, which: .MISSED, count: 20), title: "Calculations I missed")
            } label: { Text("Calculations I got wrong").frame(maxWidth: .infinity) }
                .primaryButton()
        }
    }
}
