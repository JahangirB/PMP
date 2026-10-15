import Foundation

/** A labelled result with the working shown, so the lab teaches the formula rather than hiding it. */
struct CalcLine: Identifiable { var id: String { label }; let label: String; let formula: String; let working: String; let value: String; var reading: String = "" }

/** The exam's arithmetic, worked step by step. */
enum Calculators {
    static func n(_ v: Double, _ dp: Int = 2) -> String {
        if v.isNaN || v.isInfinite { return "—" }
        let f = NumberFormatter()
        f.numberStyle = .decimal
        f.locale = Locale(identifier: "en_US")
        f.minimumFractionDigits = dp; f.maximumFractionDigits = dp
        return f.string(from: NSNumber(value: v)) ?? String(v)
    }

    static func evm(pv: Double, ev: Double, ac: Double, bac: Double) -> [CalcLine] {
        let cv = ev - ac, sv = ev - pv, cpi = ev / ac, spi = ev / pv
        let eac1 = bac / cpi, eac2 = ac + (bac - ev), eac3 = ac + (bac - ev) / (cpi * spi)
        let etc = eac1 - ac, vac = bac - eac1
        let tcpiBac = (bac - ev) / (bac - ac), tcpiEac = (bac - ev) / (eac1 - ac)
        return [
            CalcLine(label: "Cost variance", formula: "CV = EV − AC", working: "\(n(ev, 0)) − \(n(ac, 0))", value: n(cv, 0), reading: cv < 0 ? "Over budget for the work done" : "Under or on budget"),
            CalcLine(label: "Schedule variance", formula: "SV = EV − PV", working: "\(n(ev, 0)) − \(n(pv, 0))", value: n(sv, 0), reading: sv < 0 ? "Behind schedule" : "Ahead of or on schedule"),
            CalcLine(label: "Cost performance index", formula: "CPI = EV ÷ AC", working: "\(n(ev, 0)) ÷ \(n(ac, 0))", value: n(cpi), reading: "Each unit spent earns \(n(cpi)) of planned value"),
            CalcLine(label: "Schedule performance index", formula: "SPI = EV ÷ PV", working: "\(n(ev, 0)) ÷ \(n(pv, 0))", value: n(spi), reading: spi < 1 ? "Progressing at \(n(spi * 100, 0))% of the planned rate" : "At or ahead of plan"),
            CalcLine(label: "EAC — current CPI continues", formula: "EAC = BAC ÷ CPI", working: "\(n(bac, 0)) ÷ \(n(cpi))", value: n(eac1, 0), reading: "The usual forecast when today's efficiency is expected to hold"),
            CalcLine(label: "EAC — variance was a one-off", formula: "EAC = AC + (BAC − EV)", working: "\(n(ac, 0)) + (\(n(bac, 0)) − \(n(ev, 0)))", value: n(eac2, 0), reading: "Remaining work at the planned rate"),
            CalcLine(label: "EAC — cost and schedule both bite", formula: "EAC = AC + (BAC − EV) ÷ (CPI × SPI)", working: "\(n(ac, 0)) + \(n(bac - ev, 0)) ÷ \(n(cpi * spi))", value: n(eac3, 0), reading: "When a fixed date is forcing cost"),
            CalcLine(label: "Estimate to complete", formula: "ETC = EAC − AC", working: "\(n(eac1, 0)) − \(n(ac, 0))", value: n(etc, 0)),
            CalcLine(label: "Variance at completion", formula: "VAC = BAC − EAC", working: "\(n(bac, 0)) − \(n(eac1, 0))", value: n(vac, 0), reading: vac < 0 ? "Forecast overrun" : "Forecast underrun"),
            CalcLine(label: "TCPI to hit BAC", formula: "TCPI = (BAC − EV) ÷ (BAC − AC)", working: "\(n(bac - ev, 0)) ÷ \(n(bac - ac, 0))", value: n(tcpiBac), reading: tcpiBac > 1.1 ? "Rarely achievable: above 1.1" : "Efficiency needed on the remaining work"),
            CalcLine(label: "TCPI to hit EAC", formula: "TCPI = (BAC − EV) ÷ (EAC − AC)", working: "\(n(bac - ev, 0)) ÷ \(n(eac1 - ac, 0))", value: n(tcpiEac)),
        ]
    }

    static func pert(o: Double, m: Double, p: Double) -> [CalcLine] {
        let beta = (o + 4 * m + p) / 6, tri = (o + m + p) / 3, sd = (p - o) / 6
        return [
            CalcLine(label: "Beta (PERT) estimate", formula: "E = (O + 4M + P) ÷ 6", working: "(\(n(o)) + 4×\(n(m)) + \(n(p))) ÷ 6", value: n(beta), reading: "Weights the most likely value"),
            CalcLine(label: "Triangular estimate", formula: "E = (O + M + P) ÷ 3", working: "(\(n(o)) + \(n(m)) + \(n(p))) ÷ 3", value: n(tri), reading: "Equal weights"),
            CalcLine(label: "Standard deviation", formula: "σ = (P − O) ÷ 6", working: "(\(n(p)) − \(n(o))) ÷ 6", value: n(sd)),
            CalcLine(label: "Range at ~68%", formula: "E ± 1σ", working: "\(n(beta)) ± \(n(sd))", value: "\(n(beta - sd)) – \(n(beta + sd))"),
            CalcLine(label: "Range at ~95%", formula: "E ± 2σ", working: "\(n(beta)) ± \(n(2 * sd))", value: "\(n(beta - 2 * sd)) – \(n(beta + 2 * sd))"),
        ]
    }

    static func pta(targetCost: Double, targetPrice: Double, ceilingPrice: Double, buyerShare: Double) -> [CalcLine] {
        let fee = targetPrice - targetCost
        let pta = (ceilingPrice - targetPrice) / buyerShare + targetCost
        return [
            CalcLine(label: "Target fee", formula: "Fee = Target price − Target cost", working: "\(n(targetPrice, 0)) − \(n(targetCost, 0))", value: n(fee, 0)),
            CalcLine(label: "Point of total assumption", formula: "PTA = (Ceiling − Target price) ÷ Buyer share + Target cost",
                     working: "(\(n(ceilingPrice, 0)) − \(n(targetPrice, 0))) ÷ \(n(buyerShare)) + \(n(targetCost, 0))", value: n(pta, 0),
                     reading: "Above this cost the seller absorbs every further unit of overrun"),
        ]
    }

    static func channels(_ people: Int) -> [CalcLine] {
        let c = Int64(people) * Int64(people - 1) / 2
        return [CalcLine(label: "Communication channels", formula: "n(n − 1) ÷ 2", working: "\(people) × \(people - 1) ÷ 2", value: n(Double(c), 0),
                         reading: "Adding one person adds \(people) new channels")]
    }

    static func emv(probabilityPct: Double, impact: Double) -> [CalcLine] {
        [CalcLine(label: "Expected monetary value", formula: "EMV = P × I", working: "\(n(probabilityPct / 100)) × \(n(impact, 0))", value: n(probabilityPct / 100 * impact, 0),
                  reading: "Threats carry a negative impact, opportunities a positive one")]
    }

    static func finance(investment: Double, annualBenefit: Double, years: Int, ratePct: Double) -> [CalcLine] {
        let r = ratePct / 100
        var npv = -investment
        if years >= 1 { for t in 1...years { npv += annualBenefit / pow(1 + r, Double(t)) } }
        let total = annualBenefit * Double(years)
        let roi = (total - investment) / investment
        let payback = annualBenefit > 0 ? investment / annualBenefit : .nan
        let pvB = npv + investment
        return [
            CalcLine(label: "Net present value", formula: "NPV = Σ B ÷ (1 + r)^t − I", working: "\(years) years at \(n(ratePct, 1))%", value: n(npv, 0), reading: npv > 0 ? "Worth doing at this rate" : "Destroys value at this rate"),
            CalcLine(label: "Benefit–cost ratio", formula: "BCR = PV(benefits) ÷ cost", working: "\(n(pvB, 0)) ÷ \(n(investment, 0))", value: n(pvB / investment), reading: "Above 1 means benefits exceed cost"),
            CalcLine(label: "Return on investment", formula: "ROI = (Benefit − Cost) ÷ Cost", working: "(\(n(total, 0)) − \(n(investment, 0))) ÷ \(n(investment, 0))", value: "\(n(roi * 100, 1))%", reading: "Undiscounted"),
            CalcLine(label: "Payback period", formula: "Investment ÷ annual benefit", working: "\(n(investment, 0)) ÷ \(n(annualBenefit, 0))", value: "\(n(payback, 1)) years", reading: "Ignores everything after payback"),
        ]
    }

    static func littlesLaw(wip: Double, throughputPerWeek: Double) -> [CalcLine] {
        [CalcLine(label: "Average cycle time", formula: "Cycle time = WIP ÷ Throughput", working: "\(n(wip, 0)) ÷ \(n(throughputPerWeek))", value: "\(n(wip / throughputPerWeek)) weeks",
                  reading: "Cut WIP and cycle time falls in proportion")]
    }

    static func sprints(remainingPoints: Double, velocity: Double) -> [CalcLine] {
        [CalcLine(label: "Sprints to finish", formula: "⌈Remaining ÷ Velocity⌉", working: "⌈\(n(remainingPoints, 0)) ÷ \(n(velocity, 0))⌉", value: n((remainingPoints / velocity).rounded(.up), 0))]
    }
}
