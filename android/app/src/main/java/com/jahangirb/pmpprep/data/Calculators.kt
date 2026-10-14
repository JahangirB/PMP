package com.jahangirb.pmpprep.data

import kotlin.math.ceil
import kotlin.math.pow
import kotlin.math.sqrt

/** A labelled result with the working shown, so the lab teaches the formula rather than hiding it. */
data class CalcLine(val label: String, val formula: String, val working: String, val value: String, val reading: String = "")

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
}
