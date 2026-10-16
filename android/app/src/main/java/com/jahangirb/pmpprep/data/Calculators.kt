package com.jahangirb.pmpprep.data

import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.pow
import kotlin.math.sqrt

/** A labelled result with the working shown, so the lab teaches the formula rather than hiding it. */
data class CalcLine(val label: String, val formula: String, val working: String, val value: String, val reading: String = "")

/* ── decision tree, network and cash flow (index.html §10.06.3) ────────── */

/** One outcome at a chance node. */
data class TreeOutcome(val p: Double, val payoff: Double)

/** One decision option: its own cost and its chance outcomes. */
data class TreeBranch(val name: String, val cost: Double, val outcomes: List<TreeOutcome>)

data class TreeResult(val name: String, val cost: Double, val outcomes: List<TreeOutcome>, val pTotal: Double, val emv: Double) {
    val net: Double get() = emv - cost
    /** Probabilities that don't total 1.0 are a mistake in the question; it's flagged, never silently normalised. */
    val pOk: Boolean get() = abs(pTotal - 1) < 1e-6
}

/** An activity as entered: an id, a duration in days, and the ids it waits for. */
data class NetInput(val id: String, val dur: Double, val preds: List<String>)

/**
 * One activity after the forward and backward passes, day numbering (the first activity starts on
 * day 1, so a three-day activity starting on day 1 finishes on day 3). [rank] is its column in the diagram.
 */
data class NetActivity(
    val id: String, val dur: Double, val preds: List<String>, val succs: List<String>,
    val es: Double, val ef: Double, val ls: Double, val lf: Double, val tf: Double, val ff: Double, val rank: Int,
) {
    val critical: Boolean get() = tf == 0.0
}

/** The passes, or [errors] when the network can't be computed (a loop, a missing predecessor …). */
data class NetResult(
    val errors: List<String>, val acts: List<NetActivity> = emptyList(), val duration: Double = 0.0,
    /** More than one chain carries zero float. */
    val multipleCritical: Boolean = false,
) {
    val ok: Boolean get() = errors.isEmpty()
    val critical: List<String> get() = acts.filter { it.critical }.map { it.id }
    fun byId(id: String): NetActivity? = acts.firstOrNull { it.id == id }
}

/** Discounted cash flow over an outlay [c0] paid now and returns [flows] (year 1 first). Nulls are "can't be computed". */
data class CashResult(
    val discounted: List<Double>, val npv: Double, val pvIn: Double, val roi: Double?, val bcr: Double?,
    val payback: Double?, val discPayback: Double?, val irr: Double?,
)

/** The exam's arithmetic, worked step by step. */
object Calculators {
    private fun n(v: Double, dp: Int = 2): String =
        if (v.isNaN() || v.isInfinite()) "—" else if (dp == 0) String.format("%,.0f", v) else String.format("%,.${dp}f", v)

    fun evm(pv: Double, ev: Double, ac: Double, bac: Double): List<CalcLine> {
        val cv = ev - ac
        val sv = ev - pv
        val cpi = ev / ac
        val spi = ev / pv
        val eac1 = bac / cpi
        val eac2 = ac + (bac - ev)
        val eac3 = ac + (bac - ev) / (cpi * spi)
        val etc = eac1 - ac
        val vac = bac - eac1
        val tcpiBac = (bac - ev) / (bac - ac)
        val tcpiEac = (bac - ev) / (eac1 - ac)
        return listOf(
            CalcLine("Cost variance", "CV = EV − AC", "${n(ev, 0)} − ${n(ac, 0)}", n(cv, 0), if (cv < 0) "Over budget for the work done" else "Under or on budget"),
            CalcLine("Schedule variance", "SV = EV − PV", "${n(ev, 0)} − ${n(pv, 0)}", n(sv, 0), if (sv < 0) "Behind schedule" else "Ahead of or on schedule"),
            CalcLine("Cost performance index", "CPI = EV ÷ AC", "${n(ev, 0)} ÷ ${n(ac, 0)}", n(cpi), "Each unit spent earns ${n(cpi)} of planned value"),
            CalcLine("Schedule performance index", "SPI = EV ÷ PV", "${n(ev, 0)} ÷ ${n(pv, 0)}", n(spi), if (spi < 1) "Progressing at ${n(spi * 100, 0)}% of the planned rate" else "At or ahead of plan"),
            CalcLine("EAC — current CPI continues", "EAC = BAC ÷ CPI", "${n(bac, 0)} ÷ ${n(cpi)}", n(eac1, 0), "The usual forecast when today's efficiency is expected to hold"),
            CalcLine("EAC — variance was a one-off", "EAC = AC + (BAC − EV)", "${n(ac, 0)} + (${n(bac, 0)} − ${n(ev, 0)})", n(eac2, 0), "Remaining work at the planned rate"),
            CalcLine("EAC — cost and schedule both bite", "EAC = AC + (BAC − EV) ÷ (CPI × SPI)", "${n(ac, 0)} + ${n(bac - ev, 0)} ÷ ${n(cpi * spi)}", n(eac3, 0), "When a fixed date is forcing cost"),
            CalcLine("Estimate to complete", "ETC = EAC − AC", "${n(eac1, 0)} − ${n(ac, 0)}", n(etc, 0)),
            CalcLine("Variance at completion", "VAC = BAC − EAC", "${n(bac, 0)} − ${n(eac1, 0)}", n(vac, 0), if (vac < 0) "Forecast overrun" else "Forecast underrun"),
            CalcLine("TCPI to hit BAC", "TCPI = (BAC − EV) ÷ (BAC − AC)", "${n(bac - ev, 0)} ÷ ${n(bac - ac, 0)}", n(tcpiBac), if (tcpiBac > 1.1) "Rarely achievable: above 1.1" else "Efficiency needed on the remaining work"),
            CalcLine("TCPI to hit EAC", "TCPI = (BAC − EV) ÷ (EAC − AC)", "${n(bac - ev, 0)} ÷ ${n(eac1 - ac, 0)}", n(tcpiEac)),
        )
    }

    fun pert(o: Double, m: Double, p: Double): List<CalcLine> {
        val beta = (o + 4 * m + p) / 6
        val tri = (o + m + p) / 3
        val sd = (p - o) / 6
        return listOf(
            CalcLine("Beta (PERT) estimate", "E = (O + 4M + P) ÷ 6", "(${n(o)} + 4×${n(m)} + ${n(p)}) ÷ 6", n(beta), "Weights the most likely value"),
            CalcLine("Triangular estimate", "E = (O + M + P) ÷ 3", "(${n(o)} + ${n(m)} + ${n(p)}) ÷ 3", n(tri), "Equal weights"),
            CalcLine("Standard deviation", "σ = (P − O) ÷ 6", "(${n(p)} − ${n(o)}) ÷ 6", n(sd)),
            CalcLine("Range at ~68%", "E ± 1σ", "${n(beta)} ± ${n(sd)}", "${n(beta - sd)} – ${n(beta + sd)}"),
            CalcLine("Range at ~95%", "E ± 2σ", "${n(beta)} ± ${n(2 * sd)}", "${n(beta - 2 * sd)} – ${n(beta + 2 * sd)}"),
        )
    }

    fun pta(targetCost: Double, targetPrice: Double, ceilingPrice: Double, buyerShare: Double): List<CalcLine> {
        val targetFee = targetPrice - targetCost
        val pta = (ceilingPrice - targetPrice) / buyerShare + targetCost
        return listOf(
            CalcLine("Target fee", "Fee = Target price − Target cost", "${n(targetPrice, 0)} − ${n(targetCost, 0)}", n(targetFee, 0)),
            CalcLine("Point of total assumption", "PTA = (Ceiling − Target price) ÷ Buyer share + Target cost",
                "(${n(ceilingPrice, 0)} − ${n(targetPrice, 0)}) ÷ ${n(buyerShare)} + ${n(targetCost, 0)}", n(pta, 0),
                "Above this cost the seller absorbs every further unit of overrun"),
        )
    }

    fun channels(people: Int): List<CalcLine> {
        val c = people.toLong() * (people - 1) / 2
        return listOf(CalcLine("Communication channels", "n(n − 1) ÷ 2", "$people × ${people - 1} ÷ 2", "%,d".format(c),
            "Adding one person adds ${people} new channels"))
    }

    fun emv(probabilityPct: Double, impact: Double): List<CalcLine> {
        val v = probabilityPct / 100 * impact
        return listOf(CalcLine("Expected monetary value", "EMV = P × I", "${n(probabilityPct / 100)} × ${n(impact, 0)}", n(v, 0),
            "Threats carry a negative impact, opportunities a positive one"))
    }

    fun finance(investment: Double, annualBenefit: Double, years: Int, ratePct: Double): List<CalcLine> {
        val r = ratePct / 100
        var npv = -investment
        for (t in 1..years) npv += annualBenefit / (1 + r).pow(t)
        val totalBenefit = annualBenefit * years
        val roi = (totalBenefit - investment) / investment
        val payback = if (annualBenefit > 0) investment / annualBenefit else Double.NaN
        val pvBenefits = npv + investment
        return listOf(
            CalcLine("Net present value", "NPV = Σ B ÷ (1 + r)^t − I", "$years years at ${n(ratePct, 1)}%", n(npv, 0), if (npv > 0) "Worth doing at this rate" else "Destroys value at this rate"),
            CalcLine("Benefit–cost ratio", "BCR = PV(benefits) ÷ cost", "${n(pvBenefits, 0)} ÷ ${n(investment, 0)}", n(pvBenefits / investment), "Above 1 means benefits exceed cost"),
            CalcLine("Return on investment", "ROI = (Benefit − Cost) ÷ Cost", "(${n(totalBenefit, 0)} − ${n(investment, 0)}) ÷ ${n(investment, 0)}", "${n(roi * 100, 1)}%", "Undiscounted"),
            CalcLine("Payback period", "Investment ÷ annual benefit", "${n(investment, 0)} ÷ ${n(annualBenefit, 0)}", "${n(payback, 1)} years", "Ignores everything after payback"),
        )
    }

    fun littlesLaw(wip: Double, throughputPerWeek: Double): List<CalcLine> {
        val cycle = wip / throughputPerWeek
        return listOf(CalcLine("Average cycle time", "Cycle time = WIP ÷ Throughput", "${n(wip, 0)} ÷ ${n(throughputPerWeek)}", "${n(cycle)} weeks",
            "Cut WIP and cycle time falls in proportion"))
    }

    fun sprints(remainingPoints: Double, velocity: Double): List<CalcLine> {
        val s = ceil(remainingPoints / velocity)
        return listOf(CalcLine("Sprints to finish", "⌈Remaining ÷ Velocity⌉", "⌈${n(remainingPoints, 0)} ÷ ${n(velocity, 0)}⌉", n(s, 0)))
    }

    /** Sigma for a sum of independent PERT activities: σ = √Σσᵢ². */
    fun pathSigma(sigmas: List<Double>): Double = sqrt(sigmas.sumOf { it * it })
    /* ── the three that edit rows ─────────────────────────────────────────── */

    /** Decision tree roll-up: each branch's EMV at its chance node, less the branch's own cost. Best first is not assumed. */
    fun tree(branches: List<TreeBranch>): List<TreeResult> = branches.map { b ->
        TreeResult(b.name, b.cost, b.outcomes, b.outcomes.sumOf { it.p }, b.outcomes.sumOf { it.p * it.payoff })
    }

    /** The branch with the largest net value, or null with no branches. On a tie the first wins, as on the web. */
    fun bestBranch(results: List<TreeResult>): TreeResult? = results.fold(null as TreeResult?) { best, b -> if (best == null || b.net > best.net) b else best }

    /** The tree as worked lines: per branch, each outcome, the EMV, the cost, the net; then the decision. */
    fun treeLines(branches: List<TreeBranch>): List<CalcLine> {
        val rs = tree(branches)
        val best = bestBranch(rs)
        val out = ArrayList<CalcLine>()
        rs.forEach { b ->
            out += CalcLine("${b.name}: EMV at the chance node", "EMV = Σ (P × payoff)",
                b.outcomes.joinToString(" + ") { "${n(it.p)} × ${n(it.payoff, 0)}" }, n(b.emv, 0),
                if (b.pOk) "" else "The probabilities on this branch total ${n(b.pTotal)}, not 1.00.")
            out += CalcLine("${b.name}: net value", "Net = EMV − cost of the branch", "${n(b.emv, 0)} − ${n(b.cost, 0)}", n(b.net, 0),
                if (b === best) "The recommendation" else "")
        }
        if (best != null) out += CalcLine("Decision", "Choose the largest net value", rs.joinToString(" vs ") { n(it.net, 0) }, best.name,
            "Work right to left: roll each chance node up to an EMV, subtract that branch's own cost once, then take the largest net value at the decision node.")
        return out
    }

    /**
     * Activity-on-node forward and backward pass (index.html network()). Errors are reported, never
     * guessed past: a duplicate id, a duration under 1, an unknown or self predecessor, or a loop.
     */
    fun network(list: List<NetInput>): NetResult {
        val ids = ArrayList<String>()
        val dur = HashMap<String, Double>()
        val errs = ArrayList<String>()
        for (a in list) {
            val id = a.id.trim()
            if (id.isEmpty()) continue
            if (id in dur) { errs += "There are two activities called $id."; continue }
            if (a.dur.isNaN() || a.dur <= 0) { errs += "Activity $id needs a duration of at least 1."; continue }
            dur[id] = a.dur; ids += id
        }
        if (ids.isEmpty()) return NetResult(listOf("Add at least one activity."))
        val preds = ids.associateWith { ArrayList<String>() }
        val succs = ids.associateWith { ArrayList<String>() }
        for (a in list) {
            val id = a.id.trim()
            if (id !in dur) continue
            for (raw in a.preds) {
                val p = raw.trim()
                if (p.isEmpty()) continue
                if (p !in dur) { errs += "Activity $id depends on $p, which is not in the list."; continue }
                if (p == id) { errs += "Activity $id depends on itself."; continue }
                if (p !in preds[id]!!) { preds[id]!! += p; succs[p]!! += id }
            }
        }
        if (errs.isNotEmpty()) return NetResult(errs)
        // Kahn's algorithm: an order shorter than the list means a loop.
        val indeg = ids.associateWith { preds[it]!!.size }.toMutableMap()
        val queue = ArrayDeque(ids.filter { indeg[it] == 0 })
        val order = ArrayList<String>()
        while (queue.isNotEmpty()) {
            val id = queue.removeFirst()
            order += id
            for (s in succs[id]!!) { indeg[s] = indeg[s]!! - 1; if (indeg[s] == 0) queue.addLast(s) }
        }
        if (order.size != ids.size) return NetResult(listOf("The dependencies form a loop, so the network has no start."))
        val es = HashMap<String, Double>(); val ef = HashMap<String, Double>(); val rank = HashMap<String, Int>()
        for (id in order) {
            val ps = preds[id]!!
            es[id] = if (ps.isEmpty()) 1.0 else ps.maxOf { ef[it]!! } + 1
            ef[id] = es[id]!! + dur[id]!! - 1
            rank[id] = if (ps.isEmpty()) 0 else ps.maxOf { rank[it]!! } + 1
        }
        val duration = ids.maxOf { ef[it]!! }
        val ls = HashMap<String, Double>(); val lf = HashMap<String, Double>()
        for (id in order.reversed()) {
            val ss = succs[id]!!
            lf[id] = if (ss.isEmpty()) duration else ss.minOf { ls[it]!! } - 1
            ls[id] = lf[id]!! - dur[id]!! + 1
        }
        val acts = order.map { id ->
            val ss = succs[id]!!
            // A terminal activity can slip until the project end, so its free float is measured against the finish.
            val ff = if (ss.isEmpty()) duration - ef[id]!! else ss.minOf { es[it]!! } - ef[id]!! - 1
            NetActivity(id, dur[id]!!, preds[id]!!.toList(), ss.toList(), es[id]!!, ef[id]!!, ls[id]!!, lf[id]!!, ls[id]!! - es[id]!!, ff, rank[id]!!)
        }
        val crit = acts.filter { it.critical }.map { it.id }.toSet()
        val starts = acts.count { it.critical && it.preds.none { p -> p in crit } }
        return NetResult(emptyList(), acts, duration, starts > 1)
    }

    /** The network's summary lines: duration, critical path, and where total and free float differ. */
    fun networkLines(r: NetResult): List<CalcLine> {
        if (!r.ok) return emptyList()
        val out = arrayListOf(
            CalcLine("Duration", "The longest path through the network", r.acts.filter { it.succs.isEmpty() }.joinToString(", ") { "${it.id} finishes day ${n(it.ef, 0)}" },
                "${n(r.duration, 0)} days"),
            CalcLine("Critical path", "Every activity with zero total float", "TF = LS − ES = 0", r.critical.joinToString(" → "),
                if (r.multipleCritical) "More than one chain carries zero float." else ""),
        )
        val differ = r.acts.filter { it.tf != it.ff }
        if (differ.isNotEmpty()) out += CalcLine("Float differs on", "Total float ≠ free float",
            differ.joinToString("; ") { "${it.id}: TF ${n(it.tf, 0)}, FF ${n(it.ff, 0)}" }, differ.joinToString(", ") { it.id },
            "Slipping these uses float that belongs to the path: it delays no successor's early start, but it consumes the total float of everything behind it.")
        return out
    }

    fun npvAt(c0: Double, flows: List<Double>, rate: Double): Double {
        var s = -c0
        flows.forEachIndexed { i, c -> s += c / (1 + rate).pow(i + 1) }
        return s
    }

    /** Years to recover [c0], the last year interpolated; null if never recovered in the horizon. */
    fun payback(c0: Double, flows: List<Double>): Double? {
        var outstanding = c0
        flows.forEachIndexed { i, f ->
            if (f >= outstanding) return i + if (f != 0.0) outstanding / f else 0.0
            outstanding -= f
        }
        return null
    }

    /** IRR by bisection: it can't diverge, and a stream with no sign change gives null rather than a wrong root. */
    fun irr(c0: Double, flows: List<Double>): Double? {
        var lo = -0.9999; var hi = 10.0
        var flo = npvAt(c0, flows, lo); val fhi = npvAt(c0, flows, hi)
        if (!flo.isFinite() || !fhi.isFinite() || flo * fhi > 0) return null
        repeat(200) {
            val mid = (lo + hi) / 2
            val f = npvAt(c0, flows, mid)
            if (f == 0.0) return mid
            if (flo * f < 0) hi = mid else { lo = mid; flo = f }
        }
        return (lo + hi) / 2
    }

    /** Discounted cash flow: NPV, IRR, payback (plain and discounted), BCR and ROI. */
    fun cash(c0: Double, flows: List<Double>, rate: Double): CashResult {
        val disc = flows.mapIndexed { i, c -> c / (1 + rate).pow(i + 1) }
        val pvIn = disc.sum()
        return CashResult(disc, pvIn - c0, pvIn,
            if (c0 != 0.0) (flows.sum() - c0) / c0 else null, if (c0 != 0.0) pvIn / c0 else null,
            payback(c0, flows), payback(c0, disc), irr(c0, flows))
    }

    /** The cash-flow calculator as worked lines, [ratePct] as a percentage. */
    fun cashLines(c0: Double, flows: List<Double>, ratePct: Double): List<CalcLine> {
        val rate = ratePct / 100
        val r = cash(c0, flows, rate)
        val out = ArrayList<CalcLine>()
        flows.forEachIndexed { i, c ->
            out += CalcLine("Year ${i + 1}, discounted", "C ÷ (1 + r)^${i + 1}", "${n(c, 0)} ÷ ${n((1 + rate).pow(i + 1), 4)}", n(r.discounted[i], 0))
        }
        out += CalcLine("Present value in", "Σ discounted returns", r.discounted.joinToString(" + ") { n(it, 0) }, n(r.pvIn, 0))
        out += CalcLine("Net present value", "NPV = PV in − outlay", "${n(r.pvIn, 0)} − ${n(c0, 0)}", n(r.npv, 0),
            if (r.npv >= 0) "Positive, so the project earns more than the ${n(ratePct, 0)}% discount rate. NPV is already net of the outlay."
            else "Negative at ${n(ratePct, 0)}%: the returns do not cover the outlay once discounted.")
        out += CalcLine("Internal rate of return", "The rate at which NPV = 0", "found by bisection", r.irr?.let { "${n(it * 100, 2)}%" } ?: "—",
            when {
                r.irr == null -> "No single rate zeroes this stream, which happens when the signs alternate."
                r.irr >= rate -> "Above the discount rate, which is the same verdict the NPV gives."
                else -> "Below the discount rate, so the project destroys value."
            })
        out += CalcLine("Payback", "Years until the cumulative returns cover the outlay", "undiscounted, last year interpolated",
            r.payback?.let { "${n(it)} years" } ?: "not within the horizon",
            "Payback ignores everything after the recovery date and the time value of money, which is why it is never the only test.")
        out += CalcLine("Discounted payback", "The same test on the discounted returns", "discounted flows", r.discPayback?.let { "${n(it)} years" } ?: "not within the horizon")
        out += CalcLine("Benefit–cost ratio", "BCR = PV in ÷ outlay", "${n(r.pvIn, 0)} ÷ ${n(c0, 0)}", r.bcr?.let { n(it) } ?: "—", "Compare against 1.0, not against zero.")
        out += CalcLine("Return on investment", "ROI = (Σ returns − outlay) ÷ outlay", "(${n(flows.sum(), 0)} − ${n(c0, 0)}) ÷ ${n(c0, 0)}", r.roi?.let { "${n(it * 100, 1)}%" } ?: "—", "Undiscounted")
        return out
    }
}
