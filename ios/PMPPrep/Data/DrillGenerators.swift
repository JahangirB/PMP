import Foundation

/*
 * The formula lab's randomised drill (web §10.06.8–10). Eighteen generators, each returning a
 * finished problem: stem, givens, answer, tolerance, worked steps and the named wrong turns
 * ("alts") that marking reports by name. The generator ids are shared with the web and Android
 * apps through the progress document's `drills` log, so an id is never reused for another kind.
 */

struct DrillGiven: Hashable { let k: String; let v: String }
struct DrillAlt { let v: Double; let why: String }
struct DrillStep { let t: String; let m: String?; let plain: Bool }

struct DrillProblem {
    var gen: String = ""
    let group: String
    let label: String
    let stem: String
    let given: [DrillGiven]
    /** "£", "%", "index", "ratio", or a word such as "days". */
    let unit: String
    let answer: Double
    let dp: Int
    let tol: Double
    var alts: [DrillAlt]
    let steps: [DrillStep]
    let trap: String
    var seed: UInt32 = 0
}

enum DrillVerdict { case right, near, wrong, unreadable }

struct DrillMark {
    let verdict: DrillVerdict
    let given: Double?
    let why: String?
}

struct DrillGenerator {
    let id: String
    let group: String
    let make: (DrillRandom) -> DrillProblem
}

/** A small seeded generator (mulberry32), so a problem can be reproduced from its seed. */
final class DrillRandom {
    private var state: UInt32
    init(seed: UInt32) { state = seed }
    func next() -> Double {
        state = state &+ 0x6D2B79F5
        var t: UInt32 = state
        t = (t ^ (t >> 15)) &* (t | 1)
        t ^= t &+ ((t ^ (t >> 7)) &* (t | 61))
        let r: UInt32 = t ^ (t >> 14)
        return Double(r) / 4294967296.0
    }
}

/** Number formatting as the web lab does it: grouped thousands, a plain minus, a dash for nothing. */
enum DrillFmt {
    static func round(_ x: Double, _ dp: Int) -> Double {
        let f: Double = pow(10, Double(dp))
        let v: Double = x * f
        let r: Double = v >= 0 ? (v + 1e-9 + 0.5).rounded(.down) : -((-v + 1e-9 + 0.5).rounded(.down))
        return r / f
    }

    static func group(_ intPart: String) -> String {
        let chars: [Character] = Array(intPart)
        var out: String = ""
        for (i, c) in chars.enumerated() {
            if i > 0 && (chars.count - i) % 3 == 0 { out.append(",") }
            out.append(c)
        }
        return out
    }

    static func fmt(_ x: Double, _ dp: Int = 2) -> String {
        if !x.isFinite { return "—" }
        let r: Double = round(x, dp)
        let body: String = String(format: "%.\(dp)f", abs(r))
        let parts: [String] = body.components(separatedBy: ".")
        let head: String = group(parts[0])
        let tail: String = parts.count > 1 ? "." + parts[1] : ""
        let sign: String = (x < 0 && r != 0) ? "-" : ""
        return sign + head + tail
    }

    static func n0(_ x: Double) -> String { fmt(x, 0) }
    static func n2(_ x: Double) -> String { fmt(x, 2) }
    /** Whole amounts without pence, anything else to two places, so a chain still reconciles. */
    static func mn(_ x: Double) -> String { abs(x - x.rounded()) < 0.005 ? fmt(x, 0) : fmt(x, 2) }
    static func pc(_ x: Double, _ dp: Int = 1) -> String { fmt(x * 100, dp) + "%" }
    static func money(_ x: Double) -> String { "£" + fmt(x, 2) }

    /** A continuation chain: every "=" lines up under the first. */
    static func chain(_ lhs: String, _ lines: [String]) -> String {
        let pad: String = String(repeating: " ", count: lhs.count)
        var out: [String] = []
        for (i, ln) in lines.enumerated() { out.append((i == 0 ? lhs : pad) + " = " + ln) }
        return out.joined(separator: "\n")
    }

    static func padL(_ s: String, _ n: Int) -> String { s.count >= n ? s : String(repeating: " ", count: n - s.count) + s }
    static func padR(_ s: String, _ n: Int) -> String { s.count >= n ? s : s + String(repeating: " ", count: n - s.count) }

    /** The answer as the worked solution states it. */
    static func answer(_ p: DrillProblem) -> String {
        let s: String = fmt(p.answer, p.dp)
        if p.unit == "£" { return "£" + s }
        if p.unit == "%" { return s + "%" }
        if p.unit == "index" || p.unit == "ratio" { return s }
        return s + " " + p.unit
    }
}

enum DrillGenerators {
    /* ── helpers ─────────────────────────────────────────────────────────── */

    private static func fmt(_ x: Double, _ dp: Int = 2) -> String { DrillFmt.fmt(x, dp) }
    private static func n0(_ x: Double) -> String { DrillFmt.n0(x) }
    private static func n2(_ x: Double) -> String { DrillFmt.n2(x) }
    private static func mn(_ x: Double) -> String { DrillFmt.mn(x) }
    private static func pc(_ x: Double, _ dp: Int = 1) -> String { DrillFmt.pc(x, dp) }
    private static func money(_ x: Double) -> String { DrillFmt.money(x) }
    private static func chain(_ lhs: String, _ lines: [String]) -> String { DrillFmt.chain(lhs, lines) }
    private static func padL(_ s: String, _ n: Int) -> String { DrillFmt.padL(s, n) }
    private static func padR(_ s: String, _ n: Int) -> String { DrillFmt.padR(s, n) }
    private static func round(_ x: Double, _ dp: Int) -> Double { DrillFmt.round(x, dp) }
    private static func roundTo(_ x: Double, _ unit: Double) -> Double { (x / unit).rounded() * unit }

    private static func step(_ t: String, _ m: String? = nil, plain: Bool = false) -> DrillStep { DrillStep(t: t, m: m, plain: plain) }
    private static func given(_ k: String, _ v: String) -> DrillGiven { DrillGiven(k: k, v: v) }
    private static func alt(_ v: Double, _ why: String) -> DrillAlt { DrillAlt(v: v, why: why) }

    static func pick<T>(_ r: DrillRandom, _ list: [T]) -> T {
        let i: Int = Int((r.next() * Double(list.count)).rounded(.down))
        return list[min(max(i, 0), list.count - 1)]
    }

    static func between(_ r: DrillRandom, _ lo: Double, _ hi: Double, _ stepSize: Double) -> Double {
        let slots: Double = ((hi - lo) / stepSize + 1e-9).rounded(.down) + 1
        let n: Double = (r.next() * slots).rounded(.down)
        return round(lo + n * stepSize, 6)
    }

    private static let projects: [String] = [
        "the depot rollout", "the billing migration", "the site refit",
        "the fleet upgrade", "the platform rebuild", "the warehouse fit-out",
        "the payments programme", "the network refresh",
    ]
    private static func projectName(_ r: DrillRandom) -> String { pick(r, projects) }

    /* ── the registry ────────────────────────────────────────────────────── */

    static let all: [DrillGenerator] = [
        DrillGenerator(id: "evm-variance", group: "evm", make: DrillGenerators.evmVariance),
        DrillGenerator(id: "evm-forecast", group: "forecast", make: DrillGenerators.evmForecast),
        DrillGenerator(id: "tcpi", group: "forecast", make: DrillGenerators.tcpi),
        DrillGenerator(id: "pert", group: "estimate", make: DrillGenerators.pert),
        DrillGenerator(id: "pert-path", group: "estimate", make: DrillGenerators.pertPath),
        DrillGenerator(id: "channels", group: "comms", make: DrillGenerators.channels),
        DrillGenerator(id: "emv", group: "risk", make: DrillGenerators.emv),
        DrillGenerator(id: "decision-tree", group: "risk", make: DrillGenerators.decisionTree),
        DrillGenerator(id: "pta", group: "procure", make: DrillGenerators.pta),
        DrillGenerator(id: "fee", group: "procure", make: DrillGenerators.fee),
        DrillGenerator(id: "float", group: "schedule", make: DrillGenerators.totalFloat),
        DrillGenerator(id: "npv", group: "finance", make: DrillGenerators.npv),
        DrillGenerator(id: "payback", group: "finance", make: DrillGenerators.payback),
        DrillGenerator(id: "irr", group: "finance", make: DrillGenerators.irr),
        DrillGenerator(id: "bcr", group: "finance", make: DrillGenerators.bcr),
        DrillGenerator(id: "littles-law", group: "agile", make: DrillGenerators.littlesLaw),
        DrillGenerator(id: "velocity", group: "agile", make: DrillGenerators.velocity),
        DrillGenerator(id: "lead-time", group: "agile", make: DrillGenerators.leadTime),
    ]

    /** The groups that have generators, in registry order. */
    static var groups: [String] {
        var out: [String] = []
        for g in all where !out.contains(g.group) { out.append(g.group) }
        return out
    }

    static func groupOf(_ id: String) -> String? { all.first { $0.id == id }?.group }

    /** A fresh problem, from one group or from all of them. */
    static func newProblem(group: String?, seed: UInt32 = UInt32.random(in: 1...UInt32.max)) -> DrillProblem {
        let rand: DrillRandom = DrillRandom(seed: seed)
        var pool: [DrillGenerator] = all.filter { group == nil || $0.group == group }
        if pool.isEmpty { pool = all }
        let g: DrillGenerator = pick(rand, pool)
        return generate(g, seed: seed, rand: rand)
    }

    static func generate(_ g: DrillGenerator, seed: UInt32, rand: DrillRandom? = nil) -> DrillProblem {
        let r: DrillRandom = rand ?? DrillRandom(seed: seed)
        var p: DrillProblem = finalise(g.make(r))
        p.gen = g.id
        p.seed = seed
        return p
    }

    /* ── marking ─────────────────────────────────────────────────────────── */

    static func tolFor(_ p: DrillProblem) -> Double {
        let declared: Double = p.tol > 0 ? p.tol : 0.005
        return max(declared, 0.5 * pow(10, -Double(p.dp)) + 1e-9)
    }

    /** A named wrong turn that lands on the answer can never fire; drop it. */
    static func finalise(_ p: DrillProblem) -> DrillProblem {
        var q: DrillProblem = p
        let tol: Double = tolFor(p)
        q.alts = p.alts.filter { (a: DrillAlt) -> Bool in a.v.isFinite && abs(a.v - p.answer) > tol }
        return q
    }

    static func parse(_ raw: String) -> Double? {
        var s: String = raw
        for ch in [",", " ", "£", "$", "€", "%"] { s = s.replacingOccurrences(of: ch, with: "") }
        s = s.replacingOccurrences(of: "−", with: "-")
        guard let v = Double(s), v.isFinite else { return nil }
        return v
    }

    /** Right, a named wrong turn, near (a rounding difference), or plain wrong. The answer is checked first. */
    static func mark(_ p: DrillProblem, _ raw: String) -> DrillMark {
        guard let v = parse(raw) else { return DrillMark(verdict: .unreadable, given: nil, why: nil) }
        let tol: Double = tolFor(p)
        if abs(v - p.answer) <= tol { return DrillMark(verdict: .right, given: v, why: nil) }
        if let hit = p.alts.first(where: { (a: DrillAlt) -> Bool in abs(v - a.v) <= tol }) {
            return DrillMark(verdict: .wrong, given: v, why: hit.why)
        }
        let near: Double = max(tol * 25, abs(p.answer) * 0.02)
        if abs(v - p.answer) <= near {
            return DrillMark(verdict: .near, given: v,
                             why: "That is within a couple of per cent of the answer, which usually means an intermediate figure was rounded rather than the method being wrong. Carry full precision through and round only at the end.")
        }
        return DrillMark(verdict: .wrong, given: v, why: nil)
    }

    /* ── earned value ────────────────────────────────────────────────────── */

    private struct Evm {
        let bac: Double, pv: Double, ev: Double, ac: Double, planned: Double, actual: Double
        var cv: Double { ev - ac }
        var sv: Double { ev - pv }
        var cpi: Double { ev / ac }
        var spi: Double { ev / pv }
        var eacCpi: Double { bac / cpi }
        var eacAtyp: Double { ac + (bac - ev) }
        var etc: Double { eacCpi - ac }
        var vac: Double { bac - eacCpi }
        var tcpiBac: Double { (bac - ev) / (bac - ac) }
        var tcpiEac: Double { (bac - ev) / (eacCpi - ac) }
    }

    private static let pctComplete: [Double] = [0.20, 0.25, 0.40, 0.50, 0.80]

    /** Built backwards from AC so CPI is exact at two places and every line reconciles. */
    private static func evmSetup(_ r: DrillRandom) -> Evm {
        let ac: Double = between(r, 120000, 600000, 40000)
        let cpi: Double = between(r, 0.75, 1.20, 0.05)
        let ev: Double = round(ac * cpi, 2)
        let usable: [Double] = pctComplete.filter { $0 < cpi }
        let actual: Double = pick(r, usable.isEmpty ? [0.20] : usable)
        let bac: Double = round(ev / actual, 2)
        let drift: Double = between(r, -0.10, 0.15, 0.05)
        let planned: Double = max(0.05, min(0.95, round(actual + drift, 2)))
        let pv: Double = round(bac * planned, 2)
        return Evm(bac: bac, pv: pv, ev: ev, ac: ac, planned: planned, actual: actual)
    }

    private static func evmGivens(_ s: Evm) -> [DrillGiven] {
        [given("BAC", money(s.bac)), given("Planned % complete", pc(s.planned, 0)),
         given("Actual % complete", pc(s.actual, 0)), given("AC", money(s.ac))]
    }

    private static func evSteps(_ s: Evm, needPv: Bool) -> [DrillStep] {
        var out: [DrillStep] = [step(
            "Earned value is the anchor — it appears in all four of the variances and indices. It is the budget multiplied by the fraction of the work actually delivered, not by the fraction of the money spent.",
            chain("EV", ["BAC × actual % complete", "\(mn(s.bac)) × \(n2(s.actual))", mn(s.ev)]))]
        if needPv {
            out.append(step("Planned value is the same budget measured against the schedule instead: what should have been finished by today.",
                            chain("PV", ["BAC × planned % complete", "\(mn(s.bac)) × \(n2(s.planned))", mn(s.pv)])))
        }
        return out
    }

    static func evmVariance(_ r: DrillRandom) -> DrillProblem {
        let s: Evm = evmSetup(r)
        let which: String = pick(r, ["cv", "sv", "cpi", "spi"])
        let names: [String: String] = ["cv": "cost variance", "sv": "schedule variance", "cpi": "cost performance index", "spi": "schedule performance index"]
        let name: String = names[which] ?? ""
        let isMoney: Bool = which == "cv" || which == "sv"
        var steps: [DrillStep] = evSteps(s, needPv: which == "sv" || which == "spi")
        var alts: [DrillAlt] = []
        let answer: Double
        if which == "cv" {
            answer = s.cv
            steps.append(step("Cost variance is earned value minus actual cost. Variances subtract and indices divide, and both put earned value first.",
                              chain("CV", ["EV − AC", "\(mn(s.ev)) - \(mn(s.ac))", mn(s.cv)])))
            steps.append(step(s.cv >= 0
                ? "Positive, so the work completed cost less than it was budgeted to cost — under budget by \(money(s.cv)) on the work done so far."
                : "Negative, so the work completed cost more than budgeted — over budget by \(money(-s.cv)) on the work done so far."))
            alts.append(alt(s.ac - s.ev, "That is AC − EV. The convention puts earned value first, so every sign comes out inverted."))
        } else if which == "sv" {
            answer = s.sv
            steps.append(step("Schedule variance is earned value minus planned value. It is measured in currency even though it describes schedule.",
                              chain("SV", ["EV − PV", "\(mn(s.ev)) - \(mn(s.pv))", mn(s.sv)])))
            steps.append(step(s.sv >= 0
                ? "Positive, so more work has been earned than the plan called for by this date."
                : "Negative: \(money(-s.sv)) of budgeted work that should be finished is not. This is a value, not a number of days, and it falls back to zero by the end of even a late project because EV and PV both finish at BAC."))
            alts.append(alt(s.pv - s.ev, "That is PV − EV, which inverts the sign."))
            alts.append(alt(s.ev - s.ac, "That is the cost variance, EV − AC. Schedule variance compares against planned value."))
        } else if which == "cpi" {
            answer = s.cpi
            steps.append(step("The cost performance index is earned value divided by actual cost: work earned per pound spent.",
                              chain("CPI", ["EV ÷ AC", "\(mn(s.ev)) / \(mn(s.ac))", n2(s.cpi)])))
            steps.append(step(s.cpi >= 1
                ? "At or above 1.00, so every pound spent has bought at least a pound of budgeted work."
                : "Below 1.00: every pound spent has bought \(fmt(s.cpi * 100, 0))p of budgeted work."))
            alts.append(alt(s.ac / s.ev, "That is AC ÷ EV, the index inverted. Earned value is always the numerator."))
        } else {
            answer = s.spi
            steps.append(step("The schedule performance index is earned value divided by planned value: progress against the rate the plan assumed.",
                              chain("SPI", ["EV ÷ PV", "\(mn(s.ev)) / \(mn(s.pv))", n2(s.spi)])))
            steps.append(step(s.spi >= 1
                ? "At or above 1.00, so the team is at least keeping pace with the plan."
                : "Below 1.00: the team is running at \(pc(s.spi, 0)) of the planned pace."))
            alts.append(alt(s.pv / s.ev, "That is PV ÷ EV, the index inverted."))
            alts.append(alt(s.ev / s.ac, "That is the cost performance index. SPI divides by planned value."))
        }
        let label: String = name.prefix(1).uppercased() + name.dropFirst()
        let tol: Double = isMoney ? max(1, abs(answer) * 0.001) : 0.005
        return DrillProblem(group: "evm", label: label,
                            stem: "On \(projectName(r)), the figures below are reported at the end of the month. Calculate the \(name).",
                            given: evmGivens(s), unit: isMoney ? "£" : "index", answer: answer, dp: 2, tol: tol,
                            alts: alts, steps: steps,
                            trap: "Variances subtract and give a currency figure; indices divide and give a ratio. Earned value leads in all four.")
    }

    static func evmForecast(_ r: DrillRandom) -> DrillProblem {
        let s: Evm = evmSetup(r)
        let which: String = pick(r, ["eac", "etc", "vac", "atyp"])
        var steps: [DrillStep] = evSteps(s, needPv: false)
        var alts: [DrillAlt] = []
        let answer: Double
        let ask: String
        let note: String
        steps.append(step("The forecasts all run through the cost performance index, so take that next.",
                          chain("CPI", ["EV ÷ AC", "\(mn(s.ev)) / \(mn(s.ac))", n2(s.cpi)])))
        let eacLine: String = chain("EAC", ["BAC ÷ CPI", "\(mn(s.bac)) / \(n2(s.cpi))", mn(s.eacCpi)])
        if which == "atyp" {
            answer = s.eacAtyp
            ask = "The overrun came from a one-off supplier dispute that has now been settled, and the remaining work is expected to run at budgeted rates. Forecast the estimate at completion."
            steps.append(step("Because the variance will not recur, the money already spent stays spent and everything still to do is added at the original budgeted rates. The CPI above is deliberately not used.",
                              chain("EAC", ["AC + (BAC − EV)", "\(mn(s.ac)) + (\(mn(s.bac)) - \(mn(s.ev)))", "\(mn(s.ac)) + \(mn(s.bac - s.ev))", mn(s.eacAtyp)])))
            alts.append(alt(s.eacCpi, "That is BAC ÷ CPI, which assumes the variance is typical and will continue. The question says it was a one-off."))
            note = "Wording like a one-time event, a settled dispute or a problem now fixed is what selects this form over BAC ÷ CPI."
        } else if which == "eac" {
            answer = s.eacCpi
            ask = "The variances to date are considered typical of the work remaining. Forecast the estimate at completion."
            steps.append(step("Typical variances mean the efficiency achieved so far is projected across everything still to do, so the whole budget is divided by that efficiency.", eacLine))
            alts.append(alt(s.bac * s.cpi, "That multiplies by the CPI. A CPI below 1.00 must push the forecast above the budget, so the operation is division."))
            alts.append(alt(s.eacAtyp, "That is AC + (BAC − EV), the form for a one-off variance. This question says the variances are typical."))
            note = "This is the default form. It is right when the causes of the overrun are systemic and wrong when they were a single event that will not recur."
        } else if which == "etc" {
            answer = s.etc
            ask = "The variances to date are considered typical. How much more is the project forecast to cost from today?"
            steps.append(step("Take the estimate at completion first, on the typical-variance assumption.", eacLine))
            steps.append(step("The estimate to complete is whatever of that forecast has not been spent yet.",
                              chain("ETC", ["EAC − AC", "\(mn(s.eacCpi)) - \(mn(s.ac))", mn(s.etc)])))
            alts.append(alt(s.bac - s.ev, "That is the remaining work at budgeted rates, which is the ETC only if the variances are treated as one-off."))
            note = "ETC answers what is left to spend; EAC answers what the whole project will have cost."
        } else {
            answer = s.vac
            ask = "The variances to date are considered typical. What is the forecast variance at completion?"
            steps.append(step("Forecast the outturn first.", eacLine))
            steps.append(step("Variance at completion is the budget less that forecast, and it carries a sign in the same direction as every other variance.",
                              chain("VAC", ["BAC − EAC", "\(mn(s.bac)) - \(mn(s.eacCpi))", mn(s.vac)])))
            steps.append(step(s.vac >= 0
                ? "Positive, so the project is forecast to finish under budget by \(money(s.vac))."
                : "Negative, so the project is forecast to overrun by \(money(-s.vac))."))
            alts.append(alt(s.eacCpi - s.bac, "That is EAC − BAC, which inverts the sign. Budget comes first, as it does in every variance."))
            note = "A negative VAC is an overrun. The sign convention matches CV and SV."
        }
        let label: String = which == "etc" ? "Estimate to complete" : (which == "vac" ? "Variance at completion" : "Estimate at completion")
        return DrillProblem(group: "forecast", label: label,
                            stem: "On \(projectName(r)), the figures below are reported at the end of the month. \(ask)",
                            given: evmGivens(s), unit: "£", answer: answer, dp: 2, tol: max(1, abs(answer) * 0.002),
                            alts: alts, steps: steps, trap: note)
    }

    static func tcpi(_ r: DrillRandom) -> DrillProblem {
        let s: Evm = evmSetup(r)
        let toEac: Bool = r.next() < 0.4
        let answer: Double = toEac ? s.tcpiEac : s.tcpiBac
        var steps: [DrillStep] = evSteps(s, needPv: false)
        steps.append(step("Both TCPI formulas are the same idea: remaining work divided by remaining money. Take the remaining work first, which is the same either way.",
                          chain("Work left", ["BAC − EV", "\(mn(s.bac)) - \(mn(s.ev))", mn(s.bac - s.ev)])))
        if toEac {
            steps.append(step("The target here is the revised forecast, so the money left is measured against the estimate at completion rather than the baseline.",
                              chain("EAC", ["BAC ÷ CPI", "\(mn(s.bac)) / \(n2(s.cpi))", mn(s.eacCpi)])))
            steps.append(step("Money still available against that revised target.",
                              chain("Money left", ["EAC − AC", "\(mn(s.eacCpi)) - \(mn(s.ac))", mn(s.eacCpi - s.ac)])))
            steps.append(step("Divide the one by the other.",
                              chain("TCPI", ["(BAC − EV) ÷ (EAC − AC)", "\(mn(s.bac - s.ev)) / \(mn(s.eacCpi - s.ac))", n2(s.tcpiEac)])))
        } else {
            steps.append(step("The target is the original budget, so the money left is the budget less what has been spent.",
                              chain("Money left", ["BAC − AC", "\(mn(s.bac)) - \(mn(s.ac))", mn(s.bac - s.ac)])))
            steps.append(step("Divide the one by the other.",
                              chain("TCPI", ["(BAC − EV) ÷ (BAC − AC)", "\(mn(s.bac - s.ev)) / \(mn(s.bac - s.ac))", n2(s.tcpiBac)])))
        }
        steps.append(step(answer > 1
            ? "Above 1.00, so from here every remaining pound must buy \(n2(answer)) of work — against the \(n2(s.cpi)) the team has actually been achieving. The wider that gap, the less credible the recovery."
            : "At or below 1.00, so the target can be met at less than the efficiency already being delivered."))
        let target: String = toEac ? "on the revised forecast (the estimate at completion, assuming typical variances)" : "on the original budget"
        return DrillProblem(group: "forecast", label: "To-complete performance index",
                            stem: "On \(projectName(r)), the sponsor asks what efficiency the team must now achieve to finish \(target). Calculate the TCPI.",
                            given: evmGivens(s), unit: "index", answer: answer, dp: 2, tol: 0.005,
                            alts: [alt(toEac ? s.tcpiBac : s.tcpiEac, "That is the other TCPI. Both share the numerator; only the denominator changes with the target."),
                                   alt(1 / answer, "The ratio is inverted. Remaining work goes on top.")],
                            steps: steps, trap: "Remaining work over remaining money, every time. Change the target and only the denominator moves.")
    }

    /* ── estimating ──────────────────────────────────────────────────────── */

    /** O = M − 3a and P = O + 6k make tE, σ and σ² whole numbers. */
    private static func pertTriple(_ r: DrillRandom) -> (o: Double, m: Double, p: Double) {
        let m: Double = between(r, 16, 34, 1)
        let a: Double = between(r, 1, 4, 1)
        let k: Double = a + between(r, 1, 4, 1)
        return (m - 3 * a, m, m - 3 * a + 6 * k)
    }

    static func pert(_ r: DrillRandom) -> DrillProblem {
        let t = pertTriple(r)
        let o: Double = t.o, m: Double = t.m, p: Double = t.p
        let beta: Double = (o + 4 * m + p) / 6
        let tri: Double = (o + m + p) / 3
        let sd: Double = (p - o) / 6
        let which: String = pick(r, ["beta", "sd", "range"])
        let givens: [DrillGiven] = [given("Optimistic", "\(mn(o)) days"), given("Most likely", "\(mn(m)) days"), given("Pessimistic", "\(mn(p)) days")]
        var steps: [DrillStep] = []
        var alts: [DrillAlt] = []
        let answer: Double
        let ask: String
        let label: String
        if which == "beta" {
            label = "PERT expected duration"; answer = beta
            ask = "Calculate the PERT (beta) expected duration."
            steps.append(step("The beta estimate weights the most likely value by four and divides by the total of the weights, which is six.",
                              chain("tE", ["(O + 4M + P) ÷ 6", "(\(mn(o)) + 4 × \(mn(m)) + \(mn(p))) ÷ 6", "(\(mn(o)) + \(mn(4 * m)) + \(mn(p))) ÷ 6",
                                           "\(mn(o + 4 * m + p)) / 6", n2(beta)])))
            steps.append(step("The answer sits between the most likely value and the pessimistic one, pulled towards the most likely because of the weight of four. An estimate that lands outside O and P is arithmetic that has gone wrong."))
            alts.append(alt(tri, "That is the triangular estimate, (O + M + P) ÷ 3. This question asks for the beta form."))
            alts.append(alt((o + 4 * m + p) / 3, "The weights total six, so the divisor is six. Dividing by three double-counts."))
        } else if which == "sd" {
            label = "Standard deviation"; answer = sd
            ask = "Calculate the standard deviation of the estimate."
            steps.append(step("The spread depends only on the distance between the extremes. The most likely value plays no part in it at all.",
                              chain("σ", ["(P − O) ÷ 6", "(\(mn(p)) - \(mn(o))) ÷ 6", "\(mn(p - o)) / 6", n2(sd)])))
            steps.append(step("A wide gap between optimistic and pessimistic is what an uncertain estimate looks like numerically, whatever the most likely value happens to be."))
            alts.append(alt((p - m) / 6, "The numerator is pessimistic minus optimistic. The most likely value is not involved in the spread."))
            alts.append(alt(sd * sd, "That is the variance, the square of the standard deviation."))
        } else {
            label = "Confidence range"; answer = beta + 2 * sd
            ask = "Calculate the upper end of the range you could commit to with roughly 95% confidence."
            steps.append(step("Take the beta expected duration first.",
                              chain("tE", ["(O + 4M + P) ÷ 6", "(\(mn(o)) + \(mn(4 * m)) + \(mn(p))) ÷ 6", n2(beta)])))
            steps.append(step("Then the standard deviation, from the two extremes.",
                              chain("σ", ["(P − O) ÷ 6", "\(mn(p - o)) / 6", n2(sd)])))
            steps.append(step("Roughly 95% confidence is two standard deviations either side of the mean, and the commitment is the upper end of that band.",
                              chain("Upper", ["tE + 2σ", "\(n2(beta)) + 2 × \(n2(sd))", "\(n2(beta)) + \(n2(2 * sd))", n2(beta + 2 * sd)])))
            steps.append(step("Committing to the mean is committing to a coin toss: half the outcomes fall the other side of it. Two sigma is the figure to give a sponsor."))
            alts.append(alt(beta + sd, "One sigma is about 68% confidence. Roughly 95% needs two."))
            alts.append(alt(beta + 3 * sd, "Three sigma is 99.7%. Two is the band that corresponds to roughly 95%."))
        }
        return DrillProblem(group: "estimate", label: label,
                            stem: "A work package on \(projectName(r)) has been estimated three ways. \(ask)",
                            given: givens, unit: "days", answer: answer, dp: 2, tol: 0.02, alts: alts, steps: steps,
                            trap: "The beta form divides by six and weights M by four. The spread uses only P and O.")
    }

    static func pertPath(_ r: DrillRandom) -> DrillProblem {
        var ids: [String] = []
        var os: [Double] = [], ms: [Double] = [], ps: [Double] = [], sds: [Double] = []
        for id in ["A", "B", "C"] {
            let t = pertTriple(r)
            ids.append(id); os.append(t.o); ms.append(t.m); ps.append(t.p); sds.append((t.p - t.o) / 6)
        }
        let sumVar: Double = sds.reduce(0) { $0 + $1 * $1 }
        let pathSd: Double = sumVar.squareRoot()
        let sumSd: Double = sds.reduce(0, +)
        var sdLines: [String] = []
        var varLines: [String] = []
        var givens: [DrillGiven] = []
        for i in 0..<3 {
            sdLines.append(padR(ids[i], 3) + "σ = (" + padL(mn(ps[i]), 2) + " - " + padL(mn(os[i]), 2) + ") / 6 = " + n2(sds[i]))
            varLines.append(padR(ids[i], 3) + "σ² = " + padL(n2(sds[i]), 6) + "² = " + n2(sds[i] * sds[i]))
            givens.append(given(ids[i], "\(mn(os[i])) / \(mn(ms[i])) / \(mn(ps[i]))"))
        }
        varLines.append(padR("Σ", 3) + "σ² = " + n2(sumVar))
        let steps: [DrillStep] = [
            step("Take each activity's standard deviation from its two extremes.", sdLines.joined(separator: "\n"), plain: true),
            step("Square each one. Standard deviations cannot be added along a path, but variances can, and this is the step that makes the roll-up possible.",
                 varLines.joined(separator: "\n")),
            step("Take the square root of the total to get back to a standard deviation.",
                 chain("Path σ", ["√(Σ σ²)", "√" + n2(sumVar), n2(pathSd)])),
            step("Notice the result is smaller than the sum of the three individual figures, which comes to \(n2(sumSd)). Uncertainty grows more slowly than path length, because the root pulls the total back down. Adding the deviations always overstates the risk."),
        ]
        return DrillProblem(group: "estimate", label: "Path standard deviation",
                            stem: "Three activities run one after another on the critical path of \(projectName(r)). Calculate the standard deviation of the path as a whole.",
                            given: givens, unit: "days", answer: pathSd, dp: 2, tol: 0.03,
                            alts: [alt(sumSd, "That adds the standard deviations. They cannot be summed — only variances can, which is the whole reason the variance form exists."),
                                   alt(sumVar, "That is the path variance. The standard deviation is its square root.")],
                            steps: steps, trap: "Sum the variances, then take the root. Never sum the standard deviations.")
    }

    /* ── communications ──────────────────────────────────────────────────── */

    static func channels(_ r: DrillRandom) -> DrillProblem {
        let team: Double = between(r, 6, 18, 1)
        let joining: Double = between(r, 2, 6, 1)
        let asksDelta: Bool = r.next() < 0.5
        let n1: Double = team + 1
        let n2v: Double = n1 + joining
        func c(_ n: Double) -> Double { n * (n - 1) / 2 }
        var steps: [DrillStep] = []
        var alts: [DrillAlt] = []
        let answer: Double
        let ask: String
        let label: String
        if !asksDelta {
            label = "Communication channels"; answer = c(n1)
            ask = "How many lines of communication are there?"
            steps.append(step("Count everyone who communicates, and that includes you. This is the single most common slip on this formula.",
                              chain("n", ["\(mn(team)) team members + 1 project manager", mn(n1)])))
            steps.append(step("Then apply the formula.",
                              chain("C", ["n(n − 1) ÷ 2", "\(mn(n1)) × \(mn(n1 - 1)) / 2", "\(mn(n1 * (n1 - 1))) / 2", mn(c(n1))])))
            alts.append(alt(c(team), "That is \(mn(team)) people. You are on the project too, so n is \(mn(n1))."))
            alts.append(alt(n1 * (n1 - 1), "The pairs are counted twice without the division by two: A to B and B to A are one channel."))
        } else {
            label = "Channels added"; answer = c(n2v) - c(n1)
            ask = "How many additional lines of communication does that create?"
            steps.append(step("Count both totals, remembering to include yourself in each.",
                              "before = \(mn(team)) + 1 = \(mn(n1))\nafter  = \(mn(n1)) + \(mn(joining)) = \(mn(n2v))"))
            steps.append(step("Channels before the change.", chain("C₁", ["n₁(n₁ − 1) ÷ 2", "\(mn(n1)) × \(mn(n1 - 1)) / 2", mn(c(n1))])))
            steps.append(step("Channels after it.", chain("C₂", ["n₂(n₂ − 1) ÷ 2", "\(mn(n2v)) × \(mn(n2v - 1)) / 2", mn(c(n2v))])))
            steps.append(step("The question asks for the difference, not the total.",
                              chain("ΔC", ["C₂ − C₁", "\(mn(c(n2v))) - \(mn(c(n1)))", mn(c(n2v) - c(n1))])))
            steps.append(step("Adding \(mn(joining)) people has added \(mn(c(n2v) - c(n1))) channels. The growth is quadratic, which is why coordination cost rises so much faster than headcount and why a team that has doubled feels far more than twice as hard to run."))
            alts.append(alt(c(n2v), "That is the new total. The question asks how many were added, so subtract the old total."))
            alts.append(alt(c(team + joining) - c(team), "Both counts leave the project manager out. Add one to each before applying the formula."))
        }
        let middle: String = asksDelta ? ", and \(mn(joining)) more people are about to join. " : ". "
        let givens: [DrillGiven] = asksDelta
            ? [given("Team members", mn(team)), given("Joining", mn(joining)), given("Project manager", "1")]
            : [given("Team members", mn(team)), given("Project manager", "1")]
        return DrillProblem(group: "comms", label: label,
                            stem: "You are the project manager on \(projectName(r)) with \(mn(team)) team members\(middle)\(ask)",
                            given: givens, unit: "channels", answer: answer, dp: 0, tol: 0.4, alts: alts, steps: steps,
                            trap: "Count yourself. If the question changes the team size, work out both totals and subtract.")
    }

    /* ── risk ────────────────────────────────────────────────────────────── */

    static func emv(_ r: DrillRandom) -> DrillProblem {
        let count: Int = Int(between(r, 3, 4, 1))
        var ids: [String] = [], ps: [Double] = [], impacts: [Double] = [], kinds: [String] = []
        for i in 0..<count {
            // The last item is always an opportunity, so the register nets off.
            let opportunity: Bool = i == count - 1
            ids.append("R\(i + 1)")
            ps.append(between(r, 0.10, 0.60, 0.05))
            let size: Double = between(r, 20000, 200000, 5000)
            impacts.append(opportunity ? size : -size)
            kinds.append(opportunity ? "opportunity" : "threat")
        }
        var emvs: [Double] = []
        for i in 0..<count { emvs.append(ps[i] * impacts[i]) }
        let total: Double = emvs.reduce(0, +)
        let absTotal: Double = emvs.reduce(0) { $0 + abs($1) }
        var threatsOnly: Double = 0
        for i in 0..<count where impacts[i] < 0 { threatsOnly += emvs[i] }
        var lines: [String] = []
        var givens: [DrillGiven] = []
        for i in 0..<count {
            lines.append(padR(ids[i], 4) + padL(n2(ps[i]), 5) + " × " + padL(mn(impacts[i]), 9) + " = " + padL(mn(emvs[i]), 9))
            givens.append(given("\(ids[i]) · \(kinds[i])", "\(pc(ps[i], 0)) × \(money(impacts[i]))"))
        }
        let sumLine: String = emvs.map { mn($0) }.joined(separator: " + ").replacingOccurrences(of: "+ -", with: "- ")
        let steps: [DrillStep] = [
            step("Expected monetary value for each item is its probability multiplied by its impact. The sign carries the meaning: a threat costs money, so its impact is negative; an opportunity is positive.",
                 lines.joined(separator: "\n"), plain: true),
            step("The reserve is the sum across the register, threats and opportunities together.", chain("Σ EMV", [sumLine, mn(total)])),
            step("This is the contingency reserve for known risks. It sits inside the cost baseline and the project manager can spend it. Management reserve, for the risks nobody has identified, sits outside both the baseline and this calculation."),
        ]
        return DrillProblem(group: "risk", label: "Contingency reserve",
                            stem: "The risk register for \(projectName(r)) holds the items below. Calculate the expected monetary value of the register as a whole, which is the contingency reserve the project needs.",
                            given: givens, unit: "£", answer: total, dp: 2, tol: 1,
                            alts: [alt(-absTotal, "That treats every item as a threat. Opportunities carry a positive impact and reduce the reserve."),
                                   alt(absTotal, "That drops the signs entirely. Threats are negative and opportunities positive, and the register nets off."),
                                   alt(threatsOnly, "That is the threats alone. The opportunities in the register belong in the same sum.")],
                            steps: steps,
                            trap: "Sign convention is the mark. A 30% chance of a £40,000 loss is an EMV of -12,000, and an opportunity nets against it rather than adding to it.")
    }

    static func decisionTree(_ r: DrillRandom) -> DrillProblem {
        let names: [String] = pick(r, [["Build in house", "Buy the platform"], ["Run the pilot first", "Go straight to rollout"], ["Refurbish the depot", "Lease a new one"]])
        func branch(_ name: String) -> TreeBranchInput {
            let cost: Double = between(r, 80000, 400000, 20000)
            let p: Double = between(r, 0.45, 0.75, 0.05)
            let high: Double = between(r, 400000, 1400000, 50000)
            let low: Double = between(r, 50000, 350000, 25000)
            return TreeBranchInput(name: name, cost: cost, outcomes: [TreeOutcome(p: p, payoff: high), TreeOutcome(p: round(1 - p, 2), payoff: low)])
        }
        let first: TreeBranchInput = branch(names[0])
        let second: TreeBranchInput = branch(names[1])
        var built: [TreeBranch] = FormulaMath.tree([first, second])
        // A tie makes the decision meaningless, so nudge one branch.
        if abs(built[0].net - built[1].net) < 5000 {
            built[1].cost += 20000
            built[1].net = built[1].emv - built[1].cost
            FormulaMath.markBest(&built)
        }
        let a: TreeBranch = built[0]
        let b: TreeBranch = built[1]
        let best: TreeBranch = a.best ? a : b
        let other: TreeBranch = a.best ? b : a
        let outcomeLabels: [String] = ["strong demand", "weak demand"]
        var givens: [DrillGiven] = []
        var steps: [DrillStep] = []
        var table: [String] = []
        for br in built {
            givens.append(given("\(br.name) · cost", money(br.cost)))
            for (i, o) in br.outcomes.enumerated() {
                givens.append(given("\(br.name) · \(outcomeLabels[min(i, 1)])", "\(n2(o.p)) → \(money(o.payoff))"))
            }
            let products: String = br.outcomes.map { "\(n2($0.p)) × \(mn($0.payoff))" }.joined(separator: "  +  ")
            let values: String = br.outcomes.map { mn($0.p * $0.payoff) }.joined(separator: "  +  ")
            steps.append(step("Roll the chance node up for \(br.name): each outcome's probability multiplied by its payoff, then added.",
                              chain("EMV", [products, values, mn(br.emv)])))
            steps.append(step("Subtract what that branch costs to take. It comes off once, here at the branch, and never inside each outcome.",
                              chain("Net", ["EMV − cost", "\(mn(br.emv)) - \(mn(br.cost))", mn(br.net)])))
            table.append(padR(br.name, 22) + padL(mn(br.net), 10) + (br.best ? "   ◀ best" : ""))
        }
        steps.append(step("At the decision node, take the branch with the largest net value.", table.joined(separator: "\n")))
        steps.append(step("The recommendation is \(best.name), worth \(money(best.net)) net, which is \(money(abs(a.net - b.net))) ahead of the alternative. Work right to left through a tree: payoffs, then chance nodes, then the decision."))
        return DrillProblem(group: "risk", label: "Decision tree roll-up",
                            stem: "A decision on \(projectName(r)) comes down to two options, each with the same two possible states of demand. Calculate the net expected value of the option you should recommend.",
                            given: givens, unit: "£", answer: best.net, dp: 2, tol: 1,
                            alts: [alt(best.emv, "That is the EMV at the chance node, before the branch's own cost is taken off."),
                                   alt(other.net, "That is the net value of the other branch. The decision node takes the larger of the two.")],
                            steps: steps, trap: "Subtract the branch cost once, at the branch. Probabilities at any one chance node must total 1.0.")
    }

    /* ── procurement ─────────────────────────────────────────────────────── */

    private struct Fpif {
        let targetCost: Double, targetFee: Double, buyerPct: Double, ceiling: Double
        var targetPrice: Double { targetCost + targetFee }
        var buyerShare: Double { buyerPct / 100 }
        var sellerShare: Double { 1 - buyerShare }
        var pta: Double { (ceiling - targetPrice) / buyerShare + targetCost }
        func fee(_ actual: Double) -> Double { targetFee + (targetCost - actual) * sellerShare }
    }

    private static func ptaSetup(_ r: DrillRandom) -> Fpif {
        let targetCost: Double = between(r, 150000, 600000, 10000)
        let targetFee: Double = roundTo(targetCost * between(r, 0.08, 0.15, 0.01), 1000)
        let buyerPct: Double = pick(r, [60.0, 65, 70, 75, 80])
        let ceiling: Double = roundTo((targetCost + targetFee) * between(r, 1.08, 1.22, 0.02), 5000)
        return Fpif(targetCost: targetCost, targetFee: targetFee, buyerPct: buyerPct, ceiling: ceiling)
    }

    private static func ptaGivens(_ s: Fpif) -> [DrillGiven] {
        [given("Target cost", money(s.targetCost)), given("Target fee", money(s.targetFee)),
         given("Ceiling price", money(s.ceiling)), given("Share (buyer / seller)", "\(mn(s.buyerPct)) / \(mn(100 - s.buyerPct))")]
    }

    static func pta(_ r: DrillRandom) -> DrillProblem {
        let s: Fpif = ptaSetup(r)
        let headroom: Double = s.ceiling - s.targetPrice
        let steps: [DrillStep] = [
            step("Target price first, because the formula works from the headroom between it and the ceiling.",
                 chain("Target price", ["target cost + target fee", "\(mn(s.targetCost)) + \(mn(s.targetFee))", mn(s.targetPrice)])),
            step("The headroom is how much of a cost overrun the ceiling still leaves room for.",
                 chain("Headroom", ["ceiling − target price", "\(mn(s.ceiling)) - \(mn(s.targetPrice))", mn(headroom)])),
            step("Below the PTA the buyer absorbs their share of each pound of overrun, so the headroom stretches by one over the buyer's share. Then add the target cost, which is where the overrun is measured from.",
                 chain("PTA", ["(headroom ÷ buyer share) + target cost", "\(mn(headroom)) / \(n2(s.buyerShare)) + \(mn(s.targetCost))",
                               "\(mn(headroom / s.buyerShare)) + \(mn(s.targetCost))", mn(s.pta)])),
            step("At an actual cost of \(money(s.pta)) the seller's fee has been eroded to the point where every further pound comes out of their own pocket. Below it the overrun is shared \(mn(s.buyerPct))/\(mn(100 - s.buyerPct)); above it the split stops mattering."),
        ]
        return DrillProblem(group: "procure", label: "Point of total assumption",
                            stem: "A fixed price incentive fee contract on \(projectName(r)) is agreed on the terms below. Calculate the point of total assumption.",
                            given: ptaGivens(s), unit: "£", answer: s.pta, dp: 2, tol: 1,
                            alts: [alt(headroom / (1 - s.buyerShare) + s.targetCost, "The divisor is the buyer's share, not the seller's. The buyer is the one still absorbing cost below the PTA."),
                                   alt(headroom / s.buyerShare + s.targetPrice, "The addend is the target cost, not the target price. Adding the fee back in double-counts it."),
                                   alt(headroom, "That is only the headroom above the target price. It still has to be divided by the buyer's share and added to the target cost.")],
                            steps: steps, trap: "Divide by the buyer's share and add the target cost. Swapping either is the classic way to lose this mark.")
    }

    static func fee(_ r: DrillRandom) -> DrillProblem {
        let s: Fpif = ptaSetup(r)
        let over: Bool = r.next() < 0.65
        let factor: Double = over ? between(r, 1.04, 1.22, 0.02) : between(r, 0.86, 0.98, 0.02)
        let actualCost: Double = roundTo(s.targetCost * factor, 1000)
        let feeValue: Double = s.fee(actualCost)
        let uncapped: Double = actualCost + feeValue
        let price: Double = min(uncapped, s.ceiling)
        let capped: Bool = uncapped > s.ceiling
        let askPrice: Bool = r.next() < 0.45
        let seller: Double = s.sellerShare
        let variance: Double = s.targetCost - actualCost
        var steps: [DrillStep] = [
            step("The seller's share is whatever the buyer's is not. It applies to a saving and to an overrun alike.",
                 chain("Seller share", ["1 − buyer share", "1 - \(n2(s.buyerShare))", n2(seller)])),
            step("Then the cost variance against the target, keeping the sign: positive is an underrun, negative an overrun.",
                 chain("Variance", ["target cost − actual cost", "\(mn(s.targetCost)) - \(mn(actualCost))", mn(variance)])),
            step("The fee moves from the target fee by the seller's share of that variance.",
                 chain("Fee", ["target fee + variance × seller share", "\(mn(s.targetFee)) + \(mn(variance)) × \(n2(seller))",
                               "\(mn(s.targetFee)) + \(mn(variance * seller))", mn(feeValue)])),
        ]
        var alts: [DrillAlt] = []
        if askPrice {
            steps.append(step("The price is what the buyer pays: the cost actually incurred plus that fee.",
                              chain("Price", ["actual cost + fee", "\(mn(actualCost)) + \(mn(feeValue))", mn(uncapped)])))
            steps.append(step(capped
                ? "Now apply the ceiling, and apply it last. The computed price is above \(money(s.ceiling)), so the ceiling is what the buyer pays and the seller absorbs the difference."
                : "Check it against the ceiling of \(money(s.ceiling)). It comes in below, so the ceiling does not bind and the computed price stands.",
                chain("Final price", ["min(\(mn(uncapped)), \(mn(s.ceiling)))", mn(price)])))
            if capped { alts.append(alt(uncapped, "That is cost plus fee before the ceiling is applied. Here the ceiling binds, and it is applied last of all.")) }
            alts.append(alt(actualCost + s.targetFee, "That uses the target fee unchanged. The fee moves from target by the seller's share of the cost variance."))
        } else {
            steps.append(step(over
                ? "The seller has overrun the target, so they fund \(pc(seller, 0)) of the overrun out of their fee — \(money(-variance * seller)) of it."
                : "The seller has come in under the target, so they keep \(pc(seller, 0)) of the saving as extra fee — \(money(variance * seller)) of it."))
            alts.append(alt(s.targetFee + variance * s.buyerShare, "That applies the buyer's share to the variance. The seller's fee moves by the seller's share."))
            alts.append(alt(s.targetFee - variance * seller, "The sign is inverted: an underrun raises the fee and an overrun lowers it."))
        }
        var givens: [DrillGiven] = ptaGivens(s)
        givens.append(given("Actual cost", money(actualCost)))
        let what: String = askPrice ? "final price the buyer pays" : "final fee the seller earns"
        return DrillProblem(group: "procure", label: askPrice ? "Final price under an FPIF" : "Final fee under an FPIF",
                            stem: "The fixed price incentive fee contract below closes out at an actual cost of \(money(actualCost)). Calculate the \(what).",
                            given: givens, unit: "£", answer: askPrice ? price : feeValue, dp: 2, tol: 1, alts: alts, steps: steps,
                            trap: "The share is quoted buyer/seller. Apply the seller's fraction to the fee, and apply the ceiling last of all.")
    }

    /* ── schedule ────────────────────────────────────────────────────────── */

    static func totalFloat(_ r: DrillRandom) -> DrillProblem {
        let dA: Double = between(r, 2, 5, 1)
        let dB: Double = between(r, 3, 7, 1)
        let dC: Double = between(r, 1, 4, 1)
        var dD: Double = between(r, 3, 8, 1)
        let dE: Double = between(r, 2, 5, 1)
        let dF: Double = between(r, 2, 4, 1)
        // The lower branch must be the slack one, by a visible margin.
        let gap: Double = (dB + dD) - (dC + dE)
        if gap < 2 { dD += (2 - gap) + between(r, 0, 2, 1) }
        let list: [NetInput] = [
            NetInput(id: "A", dur: dA, preds: []), NetInput(id: "B", dur: dB, preds: ["A"]), NetInput(id: "C", dur: dC, preds: ["A"]),
            NetInput(id: "D", dur: dD, preds: ["B"]), NetInput(id: "E", dur: dE, preds: ["C"]), NetInput(id: "F", dur: dF, preds: ["D", "E"]),
        ]
        let net: NetResult = FormulaMath.network(list)
        let slack: Double = (dB + dD) - (dC + dE)
        let which: String = pick(r, ["duration", "tfC", "tfE", "ffE", "ffC"])
        let targets: [String: String] = ["tfC": "C", "tfE": "E", "ffE": "E", "ffC": "C"]
        let target: String = targets[which] ?? ""
        let act: NetActivity? = target.isEmpty ? nil : net.activity(target)
        let pathTop: Double = dA + dB + dD + dF
        let pathBottom: Double = dA + dC + dE + dF
        let asks: [String: String] = [
            "duration": "How long will the project take?",
            "tfC": "What is the total float of activity C?", "tfE": "What is the total float of activity E?",
            "ffE": "What is the free float of activity E?", "ffC": "What is the free float of activity C?",
        ]
        let isTotal: Bool = which == "tfC" || which == "tfE"
        let answer: Double = which == "duration" ? net.duration : (isTotal ? (act?.tf ?? 0) : (act?.ff ?? 0))

        let topSum: String = [dA, dB, dD, dF].map { n0($0) }.joined(separator: " + ")
        let bottomSum: String = [dA, dC, dE, dF].map { n0($0) }.joined(separator: " + ")
        let paths: String = "A-B-D-F : \(topSum) = \(mn(pathTop))   ◀ longest\nA-C-E-F : \(bottomSum) = \(mn(pathBottom))\nProject duration = \(mn(net.duration)) days"
        let forward: String = net.acts.map { (a: NetActivity) -> String in
            padR(a.id, 3) + "dur " + padL(mn(a.dur), 2) + "    ES " + padL(mn(a.es), 3) + "    EF " + padL(mn(a.ef), 3)
        }.joined(separator: "\n")
        let backward: String = net.acts.reversed().map { (a: NetActivity) -> String in
            padR(a.id, 3) + "dur " + padL(mn(a.dur), 2) + "    LS " + padL(mn(a.ls), 3) + "    LF " + padL(mn(a.lf), 3)
        }.joined(separator: "\n")
        let floats: String = net.acts.map { (a: NetActivity) -> String in
            padR(a.id, 3) + "TF " + padL(mn(a.tf), 3) + "    FF " + padL(mn(a.ff), 3) + (a.crit ? "    critical" : "")
        }.joined(separator: "\n")
        var steps: [DrillStep] = [
            step("There are two paths through this network. Add the durations along each: the longer one is the critical path and it sets the project duration.", paths),
            step("Forward pass, left to right. An activity starts the day after the latest of its predecessors finishes, and day numbering means a task of n days starting on day s finishes on day s + n − 1.", forward, plain: true),
            step("Backward pass, right to left from the project finish on day \(mn(net.duration)). Take the minimum of the successors' late starts, not the maximum: the tightest successor is the binding one.", backward, plain: true),
            step("Total float is the difference between the late and early dates, and free float is measured against the earliest start of the successors instead.", floats, plain: true),
        ]
        var alts: [DrillAlt] = []
        if which == "duration" {
            steps.append(step("The longest path is the answer, and it is worth saying why that is not a contradiction: the critical path is the longest route through the work, which is exactly what makes it the shortest time the project can be finished in.",
                              chain("Duration", ["\(mn(net.duration)) days"])))
            alts.append(alt(pathBottom, "That is the shorter path. A project cannot finish before its longest path does, which is exactly why the longest path sets the duration."))
        } else if let act {
            if isTotal {
                steps.append(step("For activity \(target), total float is how long it can slip before the project end date moves.",
                                  chain("TF", ["LS − ES", "\(mn(act.ls)) - \(mn(act.es))", mn(act.tf)])))
                steps.append(step("That float belongs to the path, not to the activity. The lower branch runs \(mn(pathBottom)) days against the critical \(mn(pathTop)), so C and E share the same \(mn(slack)) days of total float between them. Spend it on C and E has none left."))
                alts.append(alt(act.ff, "That is the free float. Total float measures against the project end date; free float measures against the next activity's early start."))
                alts.append(alt(0, "Zero float would mean \(target) is on the critical path. It is on the lower branch, which runs \(mn(pathBottom)) days against the critical \(mn(pathTop))."))
            } else {
                let succEs: [Double] = act.succs.compactMap { net.activity($0)?.es }
                let minEs: Double = succEs.min() ?? (net.duration + 1)
                steps.append(step("For activity \(target), free float is how long it can slip before it delays any successor's early start.",
                                  chain("FF", ["min(ES of successors) − EF − 1", "\(mn(minEs)) - \(mn(act.ef)) - 1", mn(act.ff)])))
                steps.append(step(target == "C"
                    ? "Zero, even though C carries \(mn(act.tf)) days of total float. E starts the very next day, so any slip on C pushes E straight away. The float is real but it belongs to the path, and C cannot use it without taking it from E."
                    : "E is the last activity on the slack branch before the merge at F, so it is the one that can actually absorb the \(mn(act.ff)) days without moving anything. Free float is never greater than total float, and a merge point is where the two most often differ."))
                alts.append(alt(act.tf, "That is the total float, which the whole branch shares. Free float is only what this one activity can absorb without moving its own successor."))
                alts.append(alt(0, "Zero is right for an activity whose successor starts the very next day. \(target) is the last activity before the merge at F, so it has room that C does not."))
            }
        }
        let label: String = which == "duration" ? "Project duration" : (isTotal ? "Total float" : "Free float")
        let givens: [DrillGiven] = list.map { (a: NetInput) -> DrillGiven in
            given("\(a.id) (\(mn(a.dur))d)", a.preds.isEmpty ? "start" : "after " + a.preds.joined(separator: ", "))
        }
        return DrillProblem(group: "schedule", label: label,
                            stem: "The network below describes \(projectName(r)). \(asks[which] ?? "")",
                            given: givens, unit: "days", answer: answer, dp: 0, tol: 0.4, alts: alts, steps: steps,
                            trap: "Total float is against the project finish; free float is against the next activity. Free float is never the larger of the two.")
    }

    /* ── finance ─────────────────────────────────────────────────────────── */

    static func npv(_ r: DrillRandom) -> DrillProblem {
        let outlay: Double = between(r, 200000, 700000, 20000)
        let ratePct: Double = pick(r, [8.0, 10, 12])
        let rate: Double = ratePct / 100
        let years: Int = Int(between(r, 3, 4, 1))
        var flows: [Double] = []
        for _ in 0..<years { flows.append(between(r, 80000, 320000, 10000)) }
        let c: CashResult = FormulaMath.cash(outlay, flows, rate)
        let undiscounted: Double = flows.reduce(0, +) - outlay
        var lines: [String] = []
        var givens: [DrillGiven] = [given("Outlay now", money(outlay)), given("Discount rate", "\(mn(ratePct))%")]
        for (i, f) in flows.enumerated() {
            let factor: Double = pow(1 + rate, Double(i + 1))
            lines.append("Year \(i + 1)   " + padL(mn(f), 8) + " / " + n2(factor) + "  =  " + padL(n2(c.discounted[i]), 11))
            givens.append(given("Year \(i + 1)", money(f)))
        }
        let pvSum: String = c.discounted.map { n2($0) }.joined(separator: " + ")
        let steps: [DrillStep] = [
            step("Discount each year's return back to today. The divisor is (1 + r) raised to the number of the year, so a return further out is worth proportionally less.",
                 lines.joined(separator: "\n"), plain: true),
            step("Add the discounted returns to get the present value of everything coming in.", chain("PV in", [pvSum, n2(c.pvIn)])),
            step("Then take off the outlay, which needs no discounting because it is paid today.",
                 chain("NPV", ["PV in − outlay", "\(n2(c.pvIn)) - \(mn(outlay))", n2(c.npv)])),
            step(c.npv >= 0
                ? "Positive, so the project earns more than the \(mn(ratePct))% the money could earn elsewhere, and it is worth doing on this test. When two projects are compared, the higher NPV wins and their durations are irrelevant."
                : "Negative, so at \(mn(ratePct))% the returns do not cover the outlay once the timing is accounted for. The project destroys value on this test."),
        ]
        return DrillProblem(group: "finance", label: "Net present value",
                            stem: "A business case for \(projectName(r)) needs an outlay of \(money(outlay)) today, against the returns below. At a discount rate of \(mn(ratePct))%, what is the net present value?",
                            given: givens, unit: "£", answer: c.npv, dp: 2, tol: max(50, abs(c.npv) * 0.01),
                            alts: [alt(undiscounted, "That is the undiscounted total less the outlay. Each year's return has to be divided by (1 + r) raised to that year."),
                                   alt(c.pvIn, "That is the present value of the returns. NPV is net, so the outlay still comes off.")],
                            steps: steps, trap: "NPV is already net of the investment. Do not subtract the outlay twice, and do not compare projects on duration.")
    }

    static func payback(_ r: DrillRandom) -> DrillProblem {
        let outlay: Double = between(r, 150000, 500000, 10000)
        var flows: [Double] = []
        for _ in 0..<4 { flows.append(between(r, 60000, 200000, 5000)) }
        let total: Double = flows.reduce(0, +)
        if total <= outlay { flows[3] += outlay - total + between(r, 20000, 80000, 5000) }
        let pb: Double = FormulaMath.payback(outlay, flows) ?? 0
        let fullYears: Int = Int(pb.rounded(.down))
        let recovered: Double = flows.prefix(fullYears).reduce(0, +)
        let outstanding: Double = outlay - recovered
        let yearFlow: Double = fullYears < flows.count ? flows[fullYears] : 1
        var cum: Double = -outlay
        var lines: [String] = ["Start     " + padL(mn(-outlay), 10)]
        var givens: [DrillGiven] = [given("Outlay", money(outlay))]
        for (i, f) in flows.enumerated() {
            cum += f
            lines.append("Year \(i + 1)   +" + padL(mn(f), 8) + "    cumulative " + padL(mn(cum), 10))
            givens.append(given("Year \(i + 1)", money(f)))
        }
        let fraction: Double = outstanding / yearFlow
        let steps: [DrillStep] = [
            step("Run the cumulative cash flow forward from the outlay and find the year the balance turns positive.", lines.joined(separator: "\n"), plain: true),
            step("After \(fullYears) full years, \(money(recovered)) has come back, so this much of the outlay is still outstanding.",
                 chain("Outstanding", ["outlay − recovered", "\(mn(outlay)) - \(mn(recovered))", mn(outstanding)])),
            step("That balance is cleared part way through year \(fullYears + 1). Assuming the cash arrives evenly across the year, the fraction is the outstanding balance over that year's return.",
                 chain("Fraction", ["\(mn(outstanding)) / \(mn(yearFlow))", n2(fraction)])),
            step("Add the two together.", chain("Payback", ["\(fullYears) + \(n2(fraction))", "\(n2(pb)) years"])),
            step("Payback says nothing about what happens after the recovery date and nothing about the time value of money. A project that pays back in two years and then stops beats one that pays back in three and runs for a decade, on this measure alone — which is exactly why it is never used alone."),
        ]
        return DrillProblem(group: "finance", label: "Payback period",
                            stem: "An investment of \(money(outlay)) in \(projectName(r)) returns the amounts below. Calculate the payback period in years.",
                            given: givens, unit: "years", answer: pb, dp: 2, tol: 0.02,
                            alts: [alt(Double(fullYears + 1), "That rounds up to the whole year. Payback is normally quoted with the part-year interpolated."),
                                   alt(Double(fullYears), "That is the last full year before recovery. The outstanding balance is recovered part way through the following one.")],
                            steps: steps, trap: "Interpolate the part-year. Payback ignores everything after recovery, and it is undiscounted unless the question says otherwise.")
    }

    static func irr(_ r: DrillRandom) -> DrillProblem {
        let r1: Double = pick(r, [8.0, 10, 12])
        let r2: Double = r1 + pick(r, [4.0, 5, 6])
        let span: Double = pick(r, [100000.0, 120000, 140000, 160000, 200000, 240000])
        // 0.50 is left out: it would make the midpoint shortcut accidentally right.
        let share: Double = pick(r, [0.25, 0.30, 0.35, 0.40, 0.45, 0.55, 0.60, 0.65, 0.70, 0.75])
        let npv1: Double = round(span * share, 2)
        let npv2: Double = round(npv1 - span, 2)
        let irrEst: Double = r1 + share * (r2 - r1)
        let fraction: Double = npv1 / (npv1 - npv2)
        let bracket: String = "\(mn(r1))%  →  " + padL(mn(npv1), 9) + "   positive\n" + "\(mn(r2))%  →  " + padL(mn(npv2), 9) + "   negative"
        let steps: [DrillStep] = [
            step("The internal rate of return is the discount rate at which net present value is zero. One of these rates gives a positive NPV and the other a negative one, so the crossing lies between them.",
                 bracket, plain: true),
            step("Work out how far along the gap the line crosses zero. The total vertical distance travelled is the first NPV minus the second, and subtracting a negative adds the two magnitudes together.",
                 chain("Fraction", ["NPV₁ ÷ (NPV₁ − NPV₂)", "\(mn(npv1)) / (\(mn(npv1)) - \(mn(npv2)))", "\(mn(npv1)) / \(mn(npv1 - npv2))", n2(fraction)])),
            step("Apply that fraction to the gap between the two rates and add it to the lower one.",
                 chain("IRR", ["r₁ + fraction × (r₂ − r₁)", "\(mn(r1)) + \(n2(fraction)) × \(mn(r2 - r1))", "\(mn(r1)) + \(n2(fraction * (r2 - r1)))", "\(n2(irrEst))%"])),
            step("This is a straight-line approximation across a curve, so it is an estimate rather than the exact rate. The exam never asks you to compute an IRR from first principles; it asks you to read one against a hurdle rate, and here anything above the cost of capital means the project clears its bar."),
        ]
        return DrillProblem(group: "finance", label: "IRR by interpolation",
                            stem: "The business case for \(projectName(r)) has been discounted at two rates, giving the net present values below. Estimate the internal rate of return by interpolating between them.",
                            given: [given("NPV at \(mn(r1))%", money(npv1)), given("NPV at \(mn(r2))%", money(npv2))],
                            unit: "%", answer: irrEst, dp: 2, tol: 0.1,
                            alts: [alt((r1 + r2) / 2, "That is the midpoint, which is only the answer when the two NPVs happen to be equal in size. Here they are not, so the crossing is nearer the smaller one."),
                                   alt(r2 - fraction * (r2 - r1), "The fraction is measured from the lower rate upwards. Start at \(mn(r1))% and add.")],
                            steps: steps, trap: "Interpolate upwards from the lower rate. The denominator subtracts a negative, so it is the sum of the two magnitudes.")
    }

    static func bcr(_ r: DrillRandom) -> DrillProblem {
        let costs: Double = between(r, 200000, 900000, 20000)
        let ratio: Double = between(r, 0.75, 1.9, 0.05)
        let benefits: Double = roundTo(costs * ratio, 5000)
        let value: Double = benefits / costs
        let steps: [DrillStep] = [
            step("The ratio is simply benefits over costs, both at present value so that the timing is already accounted for.",
                 chain("BCR", ["benefits ÷ costs", "\(mn(benefits)) / \(mn(costs))", n2(value)])),
            step(value >= 1
                ? "Above 1.0, so the benefits exceed the costs and the project clears the bar. Every \(money(1)) of cost returns \(money(value)) of benefit."
                : "Below 1.0, so the project loses money even though the figure is positive. The comparison is always against 1.0, never against zero — a BCR of \(n2(value)) is a loss-making case."),
        ]
        return DrillProblem(group: "finance", label: "Benefit–cost ratio",
                            stem: "Two figures have been prepared for \(projectName(r)), both discounted to present value. Calculate the benefit–cost ratio.",
                            given: [given("PV of benefits", money(benefits)), given("PV of costs", money(costs))],
                            unit: "ratio", answer: value, dp: 2, tol: 0.005,
                            alts: [alt(costs / benefits, "The ratio is inverted. Benefits go on top."),
                                   alt(benefits - costs, "That is the net benefit in currency. A ratio is a division.")],
                            steps: steps, trap: "Compare a BCR against 1.0, not against zero. Anything below 1.0 is a loss.")
    }

    /* ── agile and flow ──────────────────────────────────────────────────── */

    static func littlesLaw(_ r: DrillRandom) -> DrillProblem {
        let solveFor: String = pick(r, ["ct", "thr", "wip"])
        let thr: Double = pick(r, [2.0, 4, 6, 8, 10])
        let ct: Double = pick(r, [1.5, 2, 2.5, 3, 3.5, 4, 5])
        let wip: Double = thr * ct
        var steps: [DrillStep] = []
        var alts: [DrillAlt] = []
        let answer: Double
        let ask: String
        let unit: String
        if solveFor == "ct" {
            answer = ct; unit = "weeks"; ask = "the average cycle time"
            steps.append(step("Little's Law relates the three in one statement: the average cycle time of a stable system is the work in progress divided by the throughput.",
                              chain("CT", ["WIP ÷ throughput", "\(mn(wip)) items / \(mn(thr)) per week", "\(n2(ct)) weeks"])))
            steps.append(step("Check the units cancel: items divided by items per week leaves weeks, which is what a cycle time is measured in. If the units do not come out right, the formula has been used upside down."))
            steps.append(step("This is the argument for a work-in-progress limit stated arithmetically. Cut the WIP from \(mn(wip)) to \(mn(wip / 2)) while the team keeps finishing \(mn(thr)) items a week, and the cycle time halves to \(n2(ct / 2)) weeks. Nobody worked faster; there was simply less work sitting in the queue."))
            alts.append(alt(wip * thr, "That multiplies. Cycle time is WIP divided by throughput — check the units: items × items per week is not a time."))
            alts.append(alt(thr / wip, "The ratio is inverted, which gives a rate rather than a duration."))
        } else if solveFor == "thr" {
            answer = thr; unit = "items per week"; ask = "the throughput"
            steps.append(step("Rearranging Little's Law for throughput gives work in progress over cycle time.",
                              chain("Throughput", ["WIP ÷ cycle time", "\(mn(wip)) / \(n2(ct))", "\(n2(thr)) items per week"])))
            steps.append(step("Items divided by weeks gives items per week, which confirms the arrangement is the right way up."))
            alts.append(alt(wip * ct, "That multiplies rather than divides."))
        } else {
            answer = wip; unit = "items"; ask = "the work in progress"
            steps.append(step("Rearranging Little's Law for work in progress gives throughput multiplied by cycle time.",
                              chain("WIP", ["throughput × cycle time", "\(mn(thr)) × \(n2(ct))", "\(n2(wip)) items"])))
            steps.append(step("Items per week multiplied by weeks leaves items, which is what work in progress counts."))
            alts.append(alt(thr / ct, "That divides. Solving for WIP is the one arrangement that multiplies."))
        }
        var givens: [DrillGiven] = []
        if solveFor != "wip" { givens.append(given("Work in progress", "\(mn(wip)) items")) }
        if solveFor != "thr" { givens.append(given("Throughput", "\(mn(thr)) items per week")) }
        if solveFor != "ct" { givens.append(given("Cycle time", "\(n2(ct)) weeks")) }
        return DrillProblem(group: "agile", label: "Little's Law",
                            stem: "A Kanban team on \(projectName(r)) has been running steadily for a quarter. From the figures below, calculate \(ask).",
                            given: givens, unit: unit, answer: answer, dp: 2, tol: 0.02, alts: alts, steps: steps,
                            trap: "Only one of the three arrangements multiplies. Check the units and the right one falls out.")
    }

    static func velocity(_ r: DrillRandom) -> DrillProblem {
        var v: [Int] = [Int(between(r, 14, 34, 1)), Int(between(r, 14, 34, 1)), Int(between(r, 14, 34, 1))]
        let sum0: Int = v[0] + v[1] + v[2]
        v[2] += (3 - (sum0 % 3)) % 3
        let sum: Int = v[0] + v[1] + v[2]
        let avg: Double = Double(sum) / 3
        var remaining: Double = between(r, 120, 400, 5)
        // A backlog that divides exactly makes the rounding step do nothing.
        if remaining.truncatingRemainder(dividingBy: avg) == 0 { remaining += 5 }
        let iterations: Double = remaining / avg
        let up: Double = iterations.rounded(.up)
        let down: Double = iterations.rounded(.down)
        let velocities: String = v.map { String($0) }.joined(separator: " + ")
        let steps: [DrillStep] = [
            step("Average the observed velocities. Three iterations is the usual window: enough to smooth a bad sprint, recent enough to still describe this team.",
                 chain("Velocity", ["(\(velocities)) ÷ 3", "\(sum) / 3", "\(n2(avg)) points per iteration"])),
            step("Divide the remaining backlog by that rate.",
                 chain("Iterations", ["points remaining ÷ velocity", "\(mn(remaining)) / \(n2(avg))", n2(iterations)])),
            step("Round up: a part iteration still occupies a whole one.",
                 chain("Answer", ["⌈\(n2(iterations))⌉", "\(mn(up)) iterations"])),
            step("Only accepted work counts towards velocity, so a story that is nearly finished contributes nothing this iteration. And velocity is a forecasting tool for one team — comparing it between teams measures how they happened to size their points, not how much they deliver."),
        ]
        return DrillProblem(group: "agile", label: "Iterations remaining",
                            stem: "A team on \(projectName(r)) has completed three iterations at the velocities below and has \(mn(remaining)) story points left in the backlog. How many further iterations will it take, rounded up to a whole iteration?",
                            given: [given("Iteration 1", "\(v[0]) points"), given("Iteration 2", "\(v[1]) points"),
                                    given("Iteration 3", "\(v[2]) points"), given("Backlog remaining", "\(mn(remaining)) points")],
                            unit: "iterations", answer: up, dp: 0, tol: 0.4,
                            alts: [alt(round(iterations, 2), "That is the unrounded figure. Iterations are whole and a part iteration still needs a full one, so it rounds up."),
                                   alt(down, "Rounding down leaves \(n2((iterations - down) * avg)) points with nowhere to go.")],
                            steps: steps, trap: "Average first, then divide, then round up. Velocity counts accepted work only.")
    }

    static func leadTime(_ r: DrillRandom) -> DrillProblem {
        let thr: Double = pick(r, [2.0, 4, 6])
        let lead: Double = between(r, 4, 14, 1)
        let cycle: Double = pick(r, [0.5, 1, 1.5, 2])
        let backlog: Double = thr * lead
        let wip: Double = thr * cycle
        let steps: [DrillStep] = [
            step("Little's Law applies to whichever part of the system you draw the boundary around. Draw it around the queue as well as the work, and the answer is the lead time — what the customer actually waits.",
                 chain("Lead time", ["items in the system ÷ throughput", "\(mn(backlog)) / \(mn(thr))", "\(n2(lead)) weeks"])),
            step("Draw the boundary around only the items in progress and the same law gives the cycle time instead, which is much shorter.",
                 chain("Cycle time", ["WIP ÷ throughput", "\(mn(wip)) / \(mn(thr))", "\(n2(cycle)) weeks"])),
            step("The difference of \(n2(lead - cycle)) weeks is time the item spends waiting before anyone touches it. Lead time is always at least the cycle time and usually far longer, and the gap between them is queue. That gap is what shrinks when work in progress is limited — which is why the customer's experience improves even though nobody is working any faster."),
        ]
        return DrillProblem(group: "agile", label: "Lead time from the queue",
                            stem: "A support team on \(projectName(r)) has \(mn(backlog)) items waiting, including \(mn(wip)) actively being worked, and finishes \(mn(thr)) items a week. A new request arrives today and joins the back of the queue. How long before it is delivered?",
                            given: [given("Items in the system", mn(backlog)), given("Of those, in progress", mn(wip)), given("Throughput", "\(mn(thr)) per week")],
                            unit: "weeks", answer: lead, dp: 2, tol: 0.02,
                            alts: [alt(cycle, "That is the cycle time, which starts when someone picks the item up. A new request has to wait behind everything already in the system first, and that whole wait is what the customer experiences.")],
                            steps: steps, trap: "Lead time starts when the customer asks; cycle time starts when work begins. The gap is queue.")
    }
}
