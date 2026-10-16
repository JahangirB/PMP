import Foundation

/*
 * The formula lab's arithmetic that works on rows rather than fixed fields: an activity
 * network (forward and backward pass), a discounted cash flow (NPV, IRR, payback) and a
 * decision tree. Ported from the web app's §10.06.3, value for value. No formatting here.
 */

/** One activity in a network, as entered. */
struct NetInput {
    let id: String
    let dur: Double
    let preds: [String]
}

/** One activity after both passes. Day numbering: the first activity starts on day 1. */
struct NetActivity {
    let id: String
    let dur: Double
    var preds: [String]
    var succs: [String]
    var es: Double = 0
    var ef: Double = 0
    var ls: Double = 0
    var lf: Double = 0
    var tf: Double = 0
    var ff: Double = 0
    var rank: Int = 0
    var crit: Bool = false
}

struct NetResult {
    let ok: Bool
    let errors: [String]
    /** In topological order. */
    let acts: [NetActivity]
    let duration: Double
    let critical: [String]
    let multipleCritical: Bool
    /** Activities grouped by their longest chain from a start, for the diagram. */
    let columns: [[NetActivity]]

    func activity(_ id: String) -> NetActivity? { acts.first { $0.id == id } }

    static func failed(_ errors: [String]) -> NetResult {
        NetResult(ok: false, errors: errors, acts: [], duration: 0, critical: [], multipleCritical: false, columns: [])
    }
}

struct CashResult {
    let discounted: [Double]
    let npv: Double
    let pvIn: Double
    let roi: Double?
    let bcr: Double?
    let payback: Double?
    let discountedPayback: Double?
    let irr: Double?
}

struct TreeOutcome { let p: Double; let payoff: Double }
struct TreeBranchInput { let name: String; let cost: Double; let outcomes: [TreeOutcome] }
struct TreeBranch {
    let name: String
    var cost: Double
    let outcomes: [TreeOutcome]
    let pTotal: Double
    let pOk: Bool
    let emv: Double
    var net: Double
    var best: Bool
}

enum FormulaMath {
    /** Forward and backward pass over an activity-on-node network. */
    static func network(_ list: [NetInput]) -> NetResult {
        var byId: [String: NetActivity] = [:]
        var ids: [String] = []
        var errs: [String] = []
        for a in list {
            let id: String = a.id.trimmingCharacters(in: .whitespaces)
            if id.isEmpty { continue }
            if byId[id] != nil { errs.append("There are two activities called \(id)."); continue }
            if !a.dur.isFinite || a.dur <= 0 { errs.append("Activity \(id) needs a duration of at least 1."); continue }
            byId[id] = NetActivity(id: id, dur: a.dur, preds: [], succs: [])
            ids.append(id)
        }
        if ids.isEmpty { return NetResult.failed(["Add at least one activity."]) }
        for a in list {
            let id: String = a.id.trimmingCharacters(in: .whitespaces)
            guard byId[id] != nil else { continue }
            for raw in a.preds {
                let p: String = raw.trimmingCharacters(in: .whitespaces)
                if p.isEmpty { continue }
                if byId[p] == nil { errs.append("Activity \(id) depends on \(p), which is not in the list."); continue }
                if p == id { errs.append("Activity \(id) depends on itself."); continue }
                if !(byId[id]!.preds.contains(p)) {
                    byId[id]!.preds.append(p)
                    byId[p]!.succs.append(id)
                }
            }
        }
        if !errs.isEmpty { return NetResult.failed(errs) }

        // Kahn's algorithm; a short order means a loop.
        var indeg: [String: Int] = [:]
        var queue: [String] = []
        var order: [String] = []
        for id in ids {
            let n: Int = byId[id]!.preds.count
            indeg[id] = n
            if n == 0 { queue.append(id) }
        }
        while !queue.isEmpty {
            let id: String = queue.removeFirst()
            order.append(id)
            for s in byId[id]!.succs {
                let left: Int = (indeg[s] ?? 0) - 1
                indeg[s] = left
                if left == 0 { queue.append(s) }
            }
        }
        if order.count != ids.count { return NetResult.failed(["The dependencies form a loop, so the network has no start."]) }

        for id in order {
            var a: NetActivity = byId[id]!
            if a.preds.isEmpty {
                a.es = 1
                a.rank = 0
            } else {
                let efs: [Double] = a.preds.map { (p: String) -> Double in byId[p]!.ef }
                let ranks: [Int] = a.preds.map { (p: String) -> Int in byId[p]!.rank }
                a.es = (efs.max() ?? 0) + 1
                a.rank = (ranks.max() ?? 0) + 1
            }
            a.ef = a.es + a.dur - 1
            byId[id] = a
        }
        let finishes: [Double] = ids.map { (i: String) -> Double in byId[i]!.ef }
        let duration: Double = finishes.max() ?? 0

        for id in order.reversed() {
            var a: NetActivity = byId[id]!
            if a.succs.isEmpty {
                a.lf = duration
            } else {
                let lss: [Double] = a.succs.map { (s: String) -> Double in byId[s]!.ls }
                a.lf = (lss.min() ?? duration) - 1
            }
            a.ls = a.lf - a.dur + 1
            a.tf = a.ls - a.es
            if a.succs.isEmpty {
                a.ff = duration - a.ef
            } else {
                let ess: [Double] = a.succs.map { (s: String) -> Double in byId[s]!.es }
                a.ff = (ess.min() ?? 0) - a.ef - 1
            }
            a.crit = a.tf == 0
            byId[id] = a
        }

        let acts: [NetActivity] = order.map { (i: String) -> NetActivity in byId[i]! }
        let critical: [String] = acts.filter { $0.crit }.map { $0.id }
        var starts: Int = 0
        for a in acts where a.crit {
            let fedByCritical: Bool = a.preds.contains { (p: String) -> Bool in byId[p]!.crit }
            if !fedByCritical { starts += 1 }
        }
        var cols: [[NetActivity]] = []
        for a in acts {
            while cols.count <= a.rank { cols.append([]) }
            cols[a.rank].append(a)
        }
        return NetResult(ok: true, errors: [], acts: acts, duration: duration, critical: critical,
                         multipleCritical: starts > 1, columns: cols)
    }

    /** flows[0] is period 1; the outlay is period 0 and is never discounted. */
    static func npvAt(_ c0: Double, _ flows: [Double], _ rate: Double) -> Double {
        var s: Double = -c0
        for (i, c) in flows.enumerated() { s += c / pow(1 + rate, Double(i + 1)) }
        return s
    }

    static func cash(_ c0: Double, _ flows: [Double], _ rate: Double) -> CashResult {
        var discounted: [Double] = []
        for (i, c) in flows.enumerated() { discounted.append(c / pow(1 + rate, Double(i + 1))) }
        let inflow: Double = flows.reduce(0, +)
        let pvIn: Double = discounted.reduce(0, +)
        return CashResult(discounted: discounted, npv: pvIn - c0, pvIn: pvIn,
                          roi: c0 != 0 ? (inflow - c0) / c0 : nil,
                          bcr: c0 != 0 ? pvIn / c0 : nil,
                          payback: payback(c0, flows), discountedPayback: payback(c0, discounted),
                          irr: irr(c0, flows))
    }

    /** Years until the outlay is recovered, interpolated within the year; nil if never within the horizon. */
    static func payback(_ c0: Double, _ flows: [Double]) -> Double? {
        var outstanding: Double = c0
        for (i, f) in flows.enumerated() {
            if f >= outstanding { return Double(i) + (f != 0 ? outstanding / f : 0) }
            outstanding -= f
        }
        return nil
    }

    /** Bisection: cannot diverge, and returns nil when no single rate zeroes the stream. */
    static func irr(_ c0: Double, _ flows: [Double]) -> Double? {
        var lo: Double = -0.9999
        var hi: Double = 10
        var flo: Double = npvAt(c0, flows, lo)
        let fhi: Double = npvAt(c0, flows, hi)
        if !flo.isFinite || !fhi.isFinite || flo * fhi > 0 { return nil }
        for _ in 0..<200 {
            let mid: Double = (lo + hi) / 2
            let f: Double = npvAt(c0, flows, mid)
            if f == 0 { return mid }
            if flo * f < 0 { hi = mid } else { lo = mid; flo = f }
        }
        return (lo + hi) / 2
    }

    /** Rolls each chance node up to an EMV, takes the branch cost off once, and marks the best net value. */
    static func tree(_ branches: [TreeBranchInput]) -> [TreeBranch] {
        var out: [TreeBranch] = branches.map { (b: TreeBranchInput) -> TreeBranch in
            let p: Double = b.outcomes.reduce(0) { $0 + $1.p }
            let emv: Double = b.outcomes.reduce(0) { $0 + $1.p * $1.payoff }
            return TreeBranch(name: b.name, cost: b.cost, outcomes: b.outcomes, pTotal: p, pOk: abs(p - 1) < 1e-6,
                              emv: emv, net: emv - b.cost, best: false)
        }
        markBest(&out)
        return out
    }

    static func markBest(_ list: inout [TreeBranch]) {
        var bestIndex: Int = -1
        for i in list.indices where bestIndex < 0 || list[i].net > list[bestIndex].net { bestIndex = i }
        for i in list.indices { list[i].best = i == bestIndex }
    }
}
