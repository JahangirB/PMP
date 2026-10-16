package com.jahangirb.pmpprep.data

import java.util.Locale
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random

/** One step of a worked solution: the sentence, and an optional monospace block whose last line is the result unless [plain]. */
data class DrillStep(val text: String, val math: String? = null, val plain: Boolean = false)

/** A value a named mistake produces, and what that mistake was. */
data class DrillAlt(val value: Double, val why: String)

/**
 * A finished problem from one of the generators: the stem, the givens, the answer and the
 * tolerance it is marked to, the full working, and the wrong turns it invites, by name.
 */
data class DrillProblem(
    /** The generator's stable id, which names the problem kind in the shared `drills` log. */
    val generator: String,
    /** The formula sheet group it belongs to (extras formulas ids: evm, forecast, estimate …). */
    val group: String,
    val label: String,
    val stem: String,
    val given: List<Pair<String, String>>,
    /** "£", "%", "index", "ratio", or a plain unit such as "days". */
    val unit: String,
    val answer: Double,
    val dp: Int,
    val tol: Double,
    val alts: List<DrillAlt>,
    val steps: List<DrillStep>,
    val trap: String,
    val seed: Long = 0,
)

enum class DrillVerdict { NONE, RIGHT, NEAR, WRONG }

data class DrillMark(val verdict: DrillVerdict, val given: Double? = null, val why: String? = null)

/** The asked / right / streak tally the drill tab keeps per visit. A streak is a mood, not a record, so it isn't saved. */
data class DrillTally(val asked: Int = 0, val right: Int = 0, val streak: Int = 0, val best: Int = 0) {
    fun after(v: DrillVerdict): DrillTally = if (v == DrillVerdict.RIGHT) {
        val s = streak + 1
        DrillTally(asked + 1, right + 1, s, max(best, s))
    } else DrillTally(asked + 1, right, 0, best)
}

/**
 * The formula lab's randomised drills (index.html §10.06.8–10), ported generator by generator so a
 * problem reads and marks the same on both apps. Two rules hold across all eighteen:
 *
 * The numbers are chosen to divide. Budgets are multiples of round figures and percentages multiples
 * of 5, so the person is tested on the method, not on long division, and every line of the working
 * reconciles exactly when checked on a calculator.
 *
 * Each problem carries the values its classic mistakes produce. Marking checks the answer first, then
 * those, so someone who inverted CV is told they inverted CV rather than just "incorrect".
 */
object FormulaDrills {

    /** A generator: a stable id (never reused for a different kind of problem), its sheet group and the function. */
    class Generator(val id: String, val group: String, internal val fn: (Rand) -> DrillProblem)

    /** A source of doubles in [0, 1). */
    fun interface Rand { fun next(): Double }

    val GENERATORS: List<Generator> = listOf(
        Generator("evm-variance", "evm", ::gEvmVariance),
        Generator("evm-forecast", "forecast", ::gEvmForecast),
        Generator("tcpi", "forecast", ::gTcpi),
        Generator("pert", "estimate", ::gPert),
        Generator("pert-path", "estimate", ::gPertPath),
        Generator("channels", "comms", ::gChannels),
        Generator("emv", "risk", ::gEmv),
        Generator("decision-tree", "risk", ::gTree),
        Generator("pta", "procure", ::gPta),
        Generator("fee", "procure", ::gFee),
        Generator("float", "schedule", ::gFloat),
        Generator("npv", "finance", ::gNpv),
        Generator("payback", "finance", ::gPayback),
        Generator("irr", "finance", ::gIrr),
        Generator("bcr", "finance", ::gBcr),
        Generator("littles-law", "agile", ::gLittle),
        Generator("velocity", "agile", ::gVelocity),
        Generator("lead-time", "agile", ::gLeadTime),
    )

    /** The sheet group a generator id belongs to, or null for an id this app doesn't know (a newer app's). */
    fun groupOf(generatorId: String): String? = GENERATORS.firstOrNull { it.id == generatorId }?.group

    /** A problem from [generator] for [seed]: the same seed always gives the same problem. */
    fun generate(generator: Generator, seed: Long): DrillProblem {
        val r = Random(seed)
        return finalise(generator.fn(Rand { r.nextDouble() })).copy(generator = generator.id, seed = seed)
    }

    /** A new problem from any generator in [group] (null: every group). */
    fun next(group: String?, seed: Long = System.nanoTime()): DrillProblem {
        val pool = GENERATORS.filter { group == null || it.group == group }.ifEmpty { GENERATORS }
        val g = pool[(Random(seed xor 0x5DEECE66DL).nextDouble() * pool.size).toInt().coerceIn(0, pool.size - 1)]
        return generate(g, seed)
    }

    /* ── marking (§10.06.9) ──────────────────────────────────────────────── */

    /** The declared tolerance, widened to half a unit of the last displayed place, so a correct answer quoted to [DrillProblem.dp] always marks right. */
    fun tolFor(p: DrillProblem): Double = max(if (p.tol > 0) p.tol else 0.005, 0.5 * 10.0.pow(-p.dp) + 1e-9)

    /**
     * Reads what was typed: commas, spaces, £ $ € and % are ignored, and like the web's parseFloat the
     * number is read from the front, so "24 days" is 24.
     */
    fun parse(raw: String): Double? {
        val s = raw.replace(Regex("[,\\s£$€%]"), "")
        val m = Regex("^[+-]?(\\d+\\.?\\d*|\\.\\d+)([eE][+-]?\\d+)?").find(s) ?: return null
        return m.value.toDoubleOrNull()?.takeIf { it.isFinite() }
    }

    /**
     * Three outcomes, not two: right; a named wrong turn, reported as that mistake; close (within a couple of
     * per cent, usually an intermediate rounded early), reported as rounding rather than method; or plain wrong.
     */
    fun mark(p: DrillProblem, raw: String): DrillMark {
        val v = parse(raw) ?: return DrillMark(DrillVerdict.NONE)
        val tol = tolFor(p)
        if (abs(v - p.answer) <= tol) return DrillMark(DrillVerdict.RIGHT, v)
        p.alts.firstOrNull { abs(v - it.value) <= tol }?.let { return DrillMark(DrillVerdict.WRONG, v, it.why) }
        val near = max(tol * 25, abs(p.answer) * 0.02)
        if (abs(v - p.answer) <= near) return DrillMark(DrillVerdict.NEAR, v,
            "That is within a couple of per cent of the answer, which usually means an intermediate figure was rounded rather than the method being wrong. Carry full precision through and round only at the end.")
        return DrillMark(DrillVerdict.WRONG, v)
    }

    /** Random draws sometimes make a named wrong turn land on the answer; such an alt can never fire honestly, so it's dropped. */
    fun finalise(p: DrillProblem): DrillProblem {
        val tol = tolFor(p)
        return p.copy(alts = p.alts.filter { it.value.isFinite() && abs(it.value - p.answer) > tol })
    }

    /** The answer as the worked solution states it. */
    fun answerText(p: DrillProblem): String {
        val s = fmt(p.answer, p.dp)
        return when (p.unit) {
            "£" -> "£$s"
            "%" -> "$s%"
            "index", "ratio" -> s
            else -> "$s ${p.unit}"
        }
    }

    /** The row for the shared `drills` log: "close" and "shown" are not ok. */
    fun logRow(p: DrillProblem, verdict: DrillVerdict?, at: Long): DrillRun = DrillRun(at, p.generator.ifEmpty { "unknown" }, verdict == DrillVerdict.RIGHT)

    /* ── numbers (§10.06.1) ──────────────────────────────────────────────── */

    /** Rounds half away from zero, nudged so 1.005 rounds the way it reads. */
    fun round(x: Double, dp: Int = 0): Double {
        val f = 10.0.pow(dp)
        val v = x * f
        return (if (v >= 0) floor(v + 1e-9 + 0.5) else -floor(-v + 1e-9 + 0.5)) / f
    }

    /** Grouped thousands, ASCII minus, [dp] places; an em dash for a value that can't be shown. */
    fun fmt(x: Double?, dp: Int = 2): String {
        if (x == null || !x.isFinite()) return "—"
        val r = round(x, dp)
        val parts = String.format(Locale.US, "%.${dp}f", abs(r)).split(".")
        val grouped = parts[0].reversed().chunked(3).joinToString(",").reversed()
        return (if (x < 0 && r != 0.0) "-" else "") + grouped + if (parts.size > 1) "." + parts[1] else ""
    }

    private fun n0(x: Double) = fmt(x, 0)
    private fun n2(x: Double) = fmt(x, 2)
    /** Money inside a working: whole amounts without pence, anything fractional to two places, so a chain still reconciles. */
    private fun mn(x: Double) = if (abs(x - Math.round(x)) < 0.005) fmt(x, 0) else fmt(x, 2)
    private fun mn(x: Int) = fmt(x.toDouble(), 0)
    private fun pc(x: Double, dp: Int = 1) = fmt(x * 100, dp) + "%"
    private fun money(x: Double) = "£" + fmt(x, 2)
    private fun padL(s: String, n: Int) = s.padStart(n)
    private fun padR(s: String, n: Int) = s.padEnd(n)

    /** A continuation chain, every "=" under the first: `CV = EV − AC` / `   = 220,000 - 260,000` / `   = -40,000`. */
    private fun chain(lhs: String, lines: List<String>): String {
        val pad = " ".repeat(lhs.length)
        return lines.mapIndexed { i, ln -> (if (i > 0) pad else lhs) + " = " + ln }.joinToString("\n")
    }

    private fun <T> pick(r: Rand, list: List<T>): T = list[(r.next() * list.size).toInt().coerceIn(0, list.size - 1)]
    private fun between(r: Rand, lo: Double, hi: Double, step: Double): Double {
        val k = floor(r.next() * (floor((hi - lo) / step) + 1))
        return round(lo + k * step, 6)
    }
    private fun between(r: Rand, lo: Int, hi: Int, step: Int = 1): Int = between(r, lo.toDouble(), hi.toDouble(), step.toDouble()).toInt()
    private fun roundTo(x: Double, unit: Double) = Math.round(x / unit) * unit

    private val PROJECTS = listOf(
        "the depot rollout", "the billing migration", "the site refit", "the fleet upgrade",
        "the platform rebuild", "the warehouse fit-out", "the payments programme", "the network refresh",
    )
    private fun projectName(r: Rand) = pick(r, PROJECTS)

    /* ── earned value ────────────────────────────────────────────────────── */

    private class Evm(val bac: Double, val pv: Double, val ev: Double, val ac: Double, val planned: Double, val actual: Double) {
        val cv = ev - ac
        val sv = ev - pv
        val cpi = ev / ac
        val spi = ev / pv
        val eacCpi = bac / cpi
        val eacAtyp = ac + (bac - ev)
        val etc = eacCpi - ac
        val vac = bac - eacCpi
        val tcpiBac = (bac - ev) / (bac - ac)
        val tcpiEac = (bac - ev) / (eacCpi - ac)
    }

    private val PCT_COMPLETE = listOf(0.20, 0.25, 0.40, 0.50, 0.80)

    /**
     * Built backwards from the actual cost so the CPI is exact at two places: CPI is the one figure later
     * steps divide by, and an endless decimal would make "820,000 / 1.10 = 745,556" visibly wrong.
     * Percent complete stays below the CPI, which keeps AC below BAC so a TCPI has money to divide by.
     */
    private fun evmSetup(r: Rand): Evm {
        val ac = between(r, 120000.0, 600000.0, 40000.0)
        val cpi = between(r, 0.75, 1.20, 0.05)
        val ev = round(ac * cpi, 2)
        val usable = PCT_COMPLETE.filter { it < cpi }
        val actual = pick(r, usable.ifEmpty { listOf(0.20) })
        val bac = round(ev / actual, 2)
        val planned = max(0.05, min(0.95, round(actual + between(r, -0.10, 0.15, 0.05), 2)))
        val pv = round(bac * planned, 2)
        return Evm(bac, pv, ev, ac, planned, actual)
    }

    private fun evmGivens(s: Evm) = listOf(
        "BAC" to money(s.bac), "Planned % complete" to pc(s.planned, 0), "Actual % complete" to pc(s.actual, 0), "AC" to money(s.ac),
    )

    private fun evSteps(s: Evm, needPv: Boolean): MutableList<DrillStep> {
        val out = mutableListOf(DrillStep(
            "Earned value is the anchor — it appears in all four of the variances and indices. It is the budget multiplied by the fraction of the work actually delivered, not by the fraction of the money spent.",
            chain("EV", listOf("BAC × actual % complete", mn(s.bac) + " × " + n2(s.actual), mn(s.ev)))))
        if (needPv) out += DrillStep(
            "Planned value is the same budget measured against the schedule instead: what should have been finished by today.",
            chain("PV", listOf("BAC × planned % complete", mn(s.bac) + " × " + n2(s.planned), mn(s.pv))))
        return out
    }

    private fun gEvmVariance(r: Rand): DrillProblem {
        val s = evmSetup(r)
        val which = pick(r, listOf("cv", "sv", "cpi", "spi"))
        val name = mapOf("cv" to "cost variance", "sv" to "schedule variance", "cpi" to "cost performance index", "spi" to "schedule performance index")[which]!!
        val isMoney = which == "cv" || which == "sv"
        val steps = evSteps(s, which == "sv" || which == "spi")
        val alts = ArrayList<DrillAlt>()
        val answer: Double
        when (which) {
            "cv" -> {
                answer = s.cv
                steps += DrillStep("Cost variance is earned value minus actual cost. Variances subtract and indices divide, and both put earned value first.",
                    chain("CV", listOf("EV − AC", mn(s.ev) + " - " + mn(s.ac), mn(s.cv))))
                steps += DrillStep(if (s.cv >= 0) "Positive, so the work completed cost less than it was budgeted to cost — under budget by ${money(s.cv)} on the work done so far."
                    else "Negative, so the work completed cost more than budgeted — over budget by ${money(-s.cv)} on the work done so far.")
                alts += DrillAlt(s.ac - s.ev, "That is AC − EV. The convention puts earned value first, so every sign comes out inverted.")
            }
            "sv" -> {
                answer = s.sv
                steps += DrillStep("Schedule variance is earned value minus planned value. It is measured in currency even though it describes schedule.",
                    chain("SV", listOf("EV − PV", mn(s.ev) + " - " + mn(s.pv), mn(s.sv))))
                steps += DrillStep(if (s.sv >= 0) "Positive, so more work has been earned than the plan called for by this date."
                    else "Negative: ${money(-s.sv)} of budgeted work that should be finished is not. This is a value, not a number of days, and it falls back to zero by the end of even a late project because EV and PV both finish at BAC.")
                alts += DrillAlt(s.pv - s.ev, "That is PV − EV, which inverts the sign.")
                alts += DrillAlt(s.ev - s.ac, "That is the cost variance, EV − AC. Schedule variance compares against planned value.")
            }
            "cpi" -> {
                answer = s.cpi
                steps += DrillStep("The cost performance index is earned value divided by actual cost: work earned per pound spent.",
                    chain("CPI", listOf("EV ÷ AC", mn(s.ev) + " / " + mn(s.ac), n2(s.cpi))))
                steps += DrillStep(if (s.cpi >= 1) "At or above 1.00, so every pound spent has bought at least a pound of budgeted work."
                    else "Below 1.00: every pound spent has bought ${fmt(s.cpi * 100, 0)}p of budgeted work.")
                alts += DrillAlt(s.ac / s.ev, "That is AC ÷ EV, the index inverted. Earned value is always the numerator.")
            }
            else -> {
                answer = s.spi
                steps += DrillStep("The schedule performance index is earned value divided by planned value: progress against the rate the plan assumed.",
                    chain("SPI", listOf("EV ÷ PV", mn(s.ev) + " / " + mn(s.pv), n2(s.spi))))
                steps += DrillStep(if (s.spi >= 1) "At or above 1.00, so the team is at least keeping pace with the plan."
                    else "Below 1.00: the team is running at ${pc(s.spi, 0)} of the planned pace.")
                alts += DrillAlt(s.pv / s.ev, "That is PV ÷ EV, the index inverted.")
                alts += DrillAlt(s.ev / s.ac, "That is the cost performance index. SPI divides by planned value.")
            }
        }
        return DrillProblem("evm-variance", "evm", name.replaceFirstChar { it.uppercase() },
            "On ${projectName(r)}, the figures below are reported at the end of the month. Calculate the $name.",
            evmGivens(s), if (isMoney) "£" else "index", answer, 2,
            if (isMoney) max(1.0, abs(answer) * 0.001) else 0.005, alts, steps,
            "Variances subtract and give a currency figure; indices divide and give a ratio. Earned value leads in all four.")
    }

    private fun gEvmForecast(r: Rand): DrillProblem {
        val s = evmSetup(r)
        val which = pick(r, listOf("eac", "etc", "vac", "atyp"))
        val steps = evSteps(s, false)
        val alts = ArrayList<DrillAlt>()
        steps += DrillStep("The forecasts all run through the cost performance index, so take that next.",
            chain("CPI", listOf("EV ÷ AC", mn(s.ev) + " / " + mn(s.ac), n2(s.cpi))))
        val answer: Double; val ask: String; val note: String
        when (which) {
            "atyp" -> {
                answer = s.eacAtyp
                ask = "The overrun came from a one-off supplier dispute that has now been settled, and the remaining work is expected to run at budgeted rates. Forecast the estimate at completion."
                steps += DrillStep("Because the variance will not recur, the money already spent stays spent and everything still to do is added at the original budgeted rates. The CPI above is deliberately not used.",
                    chain("EAC", listOf("AC + (BAC − EV)", mn(s.ac) + " + (" + mn(s.bac) + " - " + mn(s.ev) + ")", mn(s.ac) + " + " + mn(s.bac - s.ev), mn(s.eacAtyp))))
                alts += DrillAlt(s.eacCpi, "That is BAC ÷ CPI, which assumes the variance is typical and will continue. The question says it was a one-off.")
                note = "Wording like a one-time event, a settled dispute or a problem now fixed is what selects this form over BAC ÷ CPI."
            }
            "eac" -> {
                answer = s.eacCpi
                ask = "The variances to date are considered typical of the work remaining. Forecast the estimate at completion."
                steps += DrillStep("Typical variances mean the efficiency achieved so far is projected across everything still to do, so the whole budget is divided by that efficiency.",
                    chain("EAC", listOf("BAC ÷ CPI", mn(s.bac) + " / " + n2(s.cpi), mn(s.eacCpi))))
                alts += DrillAlt(s.bac * s.cpi, "That multiplies by the CPI. A CPI below 1.00 must push the forecast above the budget, so the operation is division.")
                alts += DrillAlt(s.eacAtyp, "That is AC + (BAC − EV), the form for a one-off variance. This question says the variances are typical.")
                note = "This is the default form. It is right when the causes of the overrun are systemic and wrong when they were a single event that will not recur."
            }
            "etc" -> {
                answer = s.etc
                ask = "The variances to date are considered typical. How much more is the project forecast to cost from today?"
                steps += DrillStep("Take the estimate at completion first, on the typical-variance assumption.",
                    chain("EAC", listOf("BAC ÷ CPI", mn(s.bac) + " / " + n2(s.cpi), mn(s.eacCpi))))
                steps += DrillStep("The estimate to complete is whatever of that forecast has not been spent yet.",
                    chain("ETC", listOf("EAC − AC", mn(s.eacCpi) + " - " + mn(s.ac), mn(s.etc))))
                alts += DrillAlt(s.bac - s.ev, "That is the remaining work at budgeted rates, which is the ETC only if the variances are treated as one-off.")
                note = "ETC answers what is left to spend; EAC answers what the whole project will have cost."
            }
            else -> {
                answer = s.vac
                ask = "The variances to date are considered typical. What is the forecast variance at completion?"
                steps += DrillStep("Forecast the outturn first.", chain("EAC", listOf("BAC ÷ CPI", mn(s.bac) + " / " + n2(s.cpi), mn(s.eacCpi))))
                steps += DrillStep("Variance at completion is the budget less that forecast, and it carries a sign in the same direction as every other variance.",
                    chain("VAC", listOf("BAC − EAC", mn(s.bac) + " - " + mn(s.eacCpi), mn(s.vac))))
                steps += DrillStep(if (s.vac >= 0) "Positive, so the project is forecast to finish under budget by ${money(s.vac)}."
                    else "Negative, so the project is forecast to overrun by ${money(-s.vac)}.")
                alts += DrillAlt(s.eacCpi - s.bac, "That is EAC − BAC, which inverts the sign. Budget comes first, as it does in every variance.")
                note = "A negative VAC is an overrun. The sign convention matches CV and SV."
            }
        }
        return DrillProblem("evm-forecast", "forecast",
            when (which) { "etc" -> "Estimate to complete"; "vac" -> "Variance at completion"; else -> "Estimate at completion" },
            "On ${projectName(r)}, the figures below are reported at the end of the month. $ask",
            evmGivens(s), "£", answer, 2, max(1.0, abs(answer) * 0.002), alts, steps, note)
    }

    private fun gTcpi(r: Rand): DrillProblem {
        val s = evmSetup(r)
        val toEac = r.next() < 0.4
        val answer = if (toEac) s.tcpiEac else s.tcpiBac
        val steps = evSteps(s, false)
        steps += DrillStep("Both TCPI formulas are the same idea: remaining work divided by remaining money. Take the remaining work first, which is the same either way.",
            chain("Work left", listOf("BAC − EV", mn(s.bac) + " - " + mn(s.ev), mn(s.bac - s.ev))))
        if (toEac) {
            steps += DrillStep("The target here is the revised forecast, so the money left is measured against the estimate at completion rather than the baseline.",
                chain("EAC", listOf("BAC ÷ CPI", mn(s.bac) + " / " + n2(s.cpi), mn(s.eacCpi))))
            steps += DrillStep("Money still available against that revised target.",
                chain("Money left", listOf("EAC − AC", mn(s.eacCpi) + " - " + mn(s.ac), mn(s.eacCpi - s.ac))))
            steps += DrillStep("Divide the one by the other.",
                chain("TCPI", listOf("(BAC − EV) ÷ (EAC − AC)", mn(s.bac - s.ev) + " / " + mn(s.eacCpi - s.ac), n2(s.tcpiEac))))
        } else {
            steps += DrillStep("The target is the original budget, so the money left is the budget less what has been spent.",
                chain("Money left", listOf("BAC − AC", mn(s.bac) + " - " + mn(s.ac), mn(s.bac - s.ac))))
            steps += DrillStep("Divide the one by the other.",
                chain("TCPI", listOf("(BAC − EV) ÷ (BAC − AC)", mn(s.bac - s.ev) + " / " + mn(s.bac - s.ac), n2(s.tcpiBac))))
        }
        steps += DrillStep(if (answer > 1) "Above 1.00, so from here every remaining pound must buy ${n2(answer)} of work — against the ${n2(s.cpi)} the team has actually been achieving. The wider that gap, the less credible the recovery."
            else "At or below 1.00, so the target can be met at less than the efficiency already being delivered.")
        return DrillProblem("tcpi", "forecast", "To-complete performance index",
            "On ${projectName(r)}, the sponsor asks what efficiency the team must now achieve to finish " +
                (if (toEac) "on the revised forecast (the estimate at completion, assuming typical variances)" else "on the original budget") + ". Calculate the TCPI.",
            evmGivens(s), "index", answer, 2, 0.005,
            listOf(
                DrillAlt(if (toEac) s.tcpiBac else s.tcpiEac, "That is the other TCPI. Both share the numerator; only the denominator changes with the target."),
                DrillAlt(1 / answer, "The ratio is inverted. Remaining work goes on top."),
            ), steps, "Remaining work over remaining money, every time. Change the target and only the denominator moves.")
    }

    /* ── estimating ──────────────────────────────────────────────────────── */

    /** O = M − 3a and P = O + 6k make tE, σ and σ² whole numbers, so every line of a path roll-up reconciles. */
    private fun pertTriple(r: Rand): Triple<Int, Int, Int> {
        val m = between(r, 16, 34)
        val a = between(r, 1, 4)
        val k = a + between(r, 1, 4)
        return Triple(m - 3 * a, m, m - 3 * a + 6 * k)
    }

    private fun gPert(r: Rand): DrillProblem {
        val (oi, mi, pi) = pertTriple(r)
        val o = oi.toDouble(); val m = mi.toDouble(); val p = pi.toDouble()
        val beta = (o + 4 * m + p) / 6
        val tri = (o + m + p) / 3
        val sd = (p - o) / 6
        val which = pick(r, listOf("beta", "sd", "range"))
        val givens = listOf("Optimistic" to "${mn(o)} days", "Most likely" to "${mn(m)} days", "Pessimistic" to "${mn(p)} days")
        val steps = ArrayList<DrillStep>(); val alts = ArrayList<DrillAlt>()
        val answer: Double; val ask: String; val label: String
        when (which) {
            "beta" -> {
                label = "PERT expected duration"; answer = beta
                ask = "Calculate the PERT (beta) expected duration."
                steps += DrillStep("The beta estimate weights the most likely value by four and divides by the total of the weights, which is six.",
                    chain("tE", listOf("(O + 4M + P) ÷ 6", "(${mn(o)} + 4 × ${mn(m)} + ${mn(p)}) ÷ 6", "(${mn(o)} + ${mn(4 * m)} + ${mn(p)}) ÷ 6", mn(o + 4 * m + p) + " / 6", n2(beta))))
                steps += DrillStep("The answer sits between the most likely value and the pessimistic one, pulled towards the most likely because of the weight of four. An estimate that lands outside O and P is arithmetic that has gone wrong.")
                alts += DrillAlt(tri, "That is the triangular estimate, (O + M + P) ÷ 3. This question asks for the beta form.")
                alts += DrillAlt((o + 4 * m + p) / 3, "The weights total six, so the divisor is six. Dividing by three double-counts.")
            }
            "sd" -> {
                label = "Standard deviation"; answer = sd
                ask = "Calculate the standard deviation of the estimate."
                steps += DrillStep("The spread depends only on the distance between the extremes. The most likely value plays no part in it at all.",
                    chain("σ", listOf("(P − O) ÷ 6", "(${mn(p)} - ${mn(o)}) ÷ 6", mn(p - o) + " / 6", n2(sd))))
                steps += DrillStep("A wide gap between optimistic and pessimistic is what an uncertain estimate looks like numerically, whatever the most likely value happens to be.")
                alts += DrillAlt((p - m) / 6, "The numerator is pessimistic minus optimistic. The most likely value is not involved in the spread.")
                alts += DrillAlt(sd * sd, "That is the variance, the square of the standard deviation.")
            }
            else -> {
                label = "Confidence range"; answer = beta + 2 * sd
                ask = "Calculate the upper end of the range you could commit to with roughly 95% confidence."
                steps += DrillStep("Take the beta expected duration first.",
                    chain("tE", listOf("(O + 4M + P) ÷ 6", "(${mn(o)} + ${mn(4 * m)} + ${mn(p)}) ÷ 6", n2(beta))))
                steps += DrillStep("Then the standard deviation, from the two extremes.", chain("σ", listOf("(P − O) ÷ 6", mn(p - o) + " / 6", n2(sd))))
                steps += DrillStep("Roughly 95% confidence is two standard deviations either side of the mean, and the commitment is the upper end of that band.",
                    chain("Upper", listOf("tE + 2σ", n2(beta) + " + 2 × " + n2(sd), n2(beta) + " + " + n2(2 * sd), n2(beta + 2 * sd))))
                steps += DrillStep("Committing to the mean is committing to a coin toss: half the outcomes fall the other side of it. Two sigma is the figure to give a sponsor.")
                alts += DrillAlt(beta + sd, "One sigma is about 68% confidence. Roughly 95% needs two.")
                alts += DrillAlt(beta + 3 * sd, "Three sigma is 99.7%. Two is the band that corresponds to roughly 95%.")
            }
        }
        return DrillProblem("pert", "estimate", label, "A work package on ${projectName(r)} has been estimated three ways. $ask",
            givens, "days", answer, 2, 0.02, alts, steps, "The beta form divides by six and weights M by four. The spread uses only P and O.")
    }

    private fun gPertPath(r: Rand): DrillProblem {
        data class Act(val id: String, val o: Double, val m: Double, val p: Double) {
            val sd get() = (p - o) / 6
        }
        val acts = listOf("A", "B", "C").map { id -> val (o, m, p) = pertTriple(r); Act(id, o.toDouble(), m.toDouble(), p.toDouble()) }
        val sumVar = acts.sumOf { it.sd * it.sd }
        val pathSd = sqrt(sumVar)
        val sumSd = acts.sumOf { it.sd }
        return DrillProblem("pert-path", "estimate", "Path standard deviation",
            "Three activities run one after another on the critical path of ${projectName(r)}. Calculate the standard deviation of the path as a whole.",
            acts.map { it.id to "${mn(it.o)} / ${mn(it.m)} / ${mn(it.p)}" }, "days", pathSd, 2, 0.03,
            listOf(
                DrillAlt(sumSd, "That adds the standard deviations. They cannot be summed — only variances can, which is the whole reason the variance form exists."),
                DrillAlt(sumVar, "That is the path variance. The standard deviation is its square root."),
            ),
            listOf(
                DrillStep("Take each activity's standard deviation from its two extremes.",
                    acts.joinToString("\n") { padR(it.id, 3) + "σ = (" + padL(mn(it.p), 2) + " - " + padL(mn(it.o), 2) + ") / 6 = " + n2(it.sd) }, plain = true),
                DrillStep("Square each one. Standard deviations cannot be added along a path, but variances can, and this is the step that makes the roll-up possible.",
                    acts.joinToString("\n") { padR(it.id, 3) + "σ² = " + padL(n2(it.sd), 6) + "² = " + n2(it.sd * it.sd) } + "\n" + padR("Σ", 3) + "σ² = " + n2(sumVar)),
                DrillStep("Take the square root of the total to get back to a standard deviation.", chain("Path σ", listOf("√(Σ σ²)", "√" + n2(sumVar), n2(pathSd)))),
                DrillStep("Notice the result is smaller than the sum of the three individual figures, which comes to ${n2(sumSd)}. Uncertainty grows more slowly than path length, because the root pulls the total back down. Adding the deviations always overstates the risk."),
            ), "Sum the variances, then take the root. Never sum the standard deviations.")
    }

    /* ── communication ───────────────────────────────────────────────────── */

    private fun gChannels(r: Rand): DrillProblem {
        val team = between(r, 6, 18)
        val joining = between(r, 2, 6)
        val asksDelta = r.next() < 0.5
        val n1 = team + 1 // the project manager is in the count
        val n2v = n1 + joining
        fun c(n: Int) = n * (n - 1) / 2.0
        val steps = ArrayList<DrillStep>(); val alts = ArrayList<DrillAlt>()
        val answer: Double; val ask: String; val label: String
        if (!asksDelta) {
            label = "Communication channels"; answer = c(n1)
            ask = "How many lines of communication are there?"
            steps += DrillStep("Count everyone who communicates, and that includes you. This is the single most common slip on this formula.",
                chain("n", listOf("${mn(team)} team members + 1 project manager", mn(n1))))
            steps += DrillStep("Then apply the formula.", chain("C", listOf("n(n − 1) ÷ 2", "${mn(n1)} × ${mn(n1 - 1)} / 2", mn(n1 * (n1 - 1)) + " / 2", mn(c(n1)))))
            alts += DrillAlt(c(team), "That is ${mn(team)} people. You are on the project too, so n is ${mn(n1)}.")
            alts += DrillAlt((n1 * (n1 - 1)).toDouble(), "The pairs are counted twice without the division by two: A to B and B to A are one channel.")
        } else {
            label = "Channels added"; answer = c(n2v) - c(n1)
            ask = "How many additional lines of communication does that create?"
            steps += DrillStep("Count both totals, remembering to include yourself in each.",
                "before = ${mn(team)} + 1 = ${mn(n1)}\nafter  = ${mn(n1)} + ${mn(joining)} = ${mn(n2v)}")
            steps += DrillStep("Channels before the change.", chain("C₁", listOf("n₁(n₁ − 1) ÷ 2", "${mn(n1)} × ${mn(n1 - 1)} / 2", mn(c(n1)))))
            steps += DrillStep("Channels after it.", chain("C₂", listOf("n₂(n₂ − 1) ÷ 2", "${mn(n2v)} × ${mn(n2v - 1)} / 2", mn(c(n2v)))))
            steps += DrillStep("The question asks for the difference, not the total.", chain("ΔC", listOf("C₂ − C₁", mn(c(n2v)) + " - " + mn(c(n1)), mn(c(n2v) - c(n1)))))
            steps += DrillStep("Adding ${mn(joining)} people has added ${mn(c(n2v) - c(n1))} channels. The growth is quadratic, which is why coordination cost rises so much faster than headcount and why a team that has doubled feels far more than twice as hard to run.")
            alts += DrillAlt(c(n2v), "That is the new total. The question asks how many were added, so subtract the old total.")
            alts += DrillAlt(c(team + joining) - c(team), "Both counts leave the project manager out. Add one to each before applying the formula.")
        }
        return DrillProblem("channels", "comms", label,
            "You are the project manager on ${projectName(r)} with ${mn(team)} team members" +
                (if (asksDelta) ", and ${mn(joining)} more people are about to join. " else ". ") + ask,
            if (asksDelta) listOf("Team members" to mn(team), "Joining" to mn(joining), "Project manager" to "1")
            else listOf("Team members" to mn(team), "Project manager" to "1"),
            "channels", answer, 0, 0.4, alts, steps, "Count yourself. If the question changes the team size, work out both totals and subtract.")
    }

    /* ── risk ────────────────────────────────────────────────────────────── */

    private fun gEmv(r: Rand): DrillProblem {
        data class Risk(val id: String, val p: Double, val impact: Double, val kind: String) { val emv get() = p * impact }
        val count = between(r, 3, 4)
        // The last item is always an opportunity: a register of pure threats makes all three distractors the same number.
        val risks = (0 until count).map { i ->
            val opp = i == count - 1
            val p = between(r, 0.10, 0.60, 0.05)
            val impact = (if (opp) 1 else -1) * between(r, 20000.0, 200000.0, 5000.0)
            Risk("R${i + 1}", p, impact, if (opp) "opportunity" else "threat")
        }
        val total = risks.sumOf { it.emv }
        val absTotal = risks.sumOf { abs(it.emv) }
        val threatsOnly = risks.sumOf { if (it.impact < 0) it.emv else 0.0 }
        return DrillProblem("emv", "risk", "Contingency reserve",
            "The risk register for ${projectName(r)} holds the items below. Calculate the expected monetary value of the register as a whole, which is the contingency reserve the project needs.",
            risks.map { "${it.id} · ${it.kind}" to "${pc(it.p, 0)} × ${money(it.impact)}" }, "£", total, 2, 1.0,
            listOf(
                DrillAlt(-absTotal, "That treats every item as a threat. Opportunities carry a positive impact and reduce the reserve."),
                DrillAlt(absTotal, "That drops the signs entirely. Threats are negative and opportunities positive, and the register nets off."),
                DrillAlt(threatsOnly, "That is the threats alone. The opportunities in the register belong in the same sum."),
            ),
            listOf(
                DrillStep("Expected monetary value for each item is its probability multiplied by its impact. The sign carries the meaning: a threat costs money, so its impact is negative; an opportunity is positive.",
                    risks.joinToString("\n") { padR(it.id, 4) + padL(n2(it.p), 5) + " × " + padL(mn(it.impact), 9) + " = " + padL(mn(it.emv), 9) }, plain = true),
                DrillStep("The reserve is the sum across the register, threats and opportunities together.",
                    chain("Σ EMV", listOf(risks.joinToString(" + ") { mn(it.emv) }.replace("+ -", "- "), mn(total)))),
                DrillStep("This is the contingency reserve for known risks. It sits inside the cost baseline and the project manager can spend it. Management reserve, for the risks nobody has identified, sits outside both the baseline and this calculation."),
            ), "Sign convention is the mark. A 30% chance of a £40,000 loss is an EMV of -12,000, and an opportunity nets against it rather than adding to it.")
    }

    private fun gTree(r: Rand): DrillProblem {
        val names = pick(r, listOf(
            listOf("Build in house", "Buy the platform"), listOf("Run the pilot first", "Go straight to rollout"), listOf("Refurbish the depot", "Lease a new one"),
        ))
        val labels = listOf("strong demand", "weak demand")
        fun branch(name: String): TreeBranch {
            val cost = between(r, 80000.0, 400000.0, 20000.0)
            val p = between(r, 0.45, 0.75, 0.05)
            val high = between(r, 400000.0, 1400000.0, 50000.0)
            val low = between(r, 50000.0, 350000.0, 25000.0)
            return TreeBranch(name, cost, listOf(TreeOutcome(p, high), TreeOutcome(round(1 - p, 2), low)))
        }
        val raw = listOf(branch(names[0]), branch(names[1]))
        var built = Calculators.tree(raw)
        // A tie makes the decision meaningless, so nudge one branch.
        if (abs(built[0].net - built[1].net) < 5000) built = Calculators.tree(listOf(raw[0], raw[1].copy(cost = raw[1].cost + 20000)))
        val a = built[0]; val b = built[1]
        val best = Calculators.bestBranch(built)!!
        val other = if (best === a) b else a
        val given = built.flatMap { br ->
            listOf("${br.name} · cost" to money(br.cost)) + br.outcomes.mapIndexed { i, o -> "${br.name} · ${labels[i]}" to "${n2(o.p)} → ${money(o.payoff)}" }
        }
        val steps = ArrayList<DrillStep>()
        built.forEach { br ->
            steps += DrillStep("Roll the chance node up for ${br.name}: each outcome's probability multiplied by its payoff, then added.",
                chain("EMV", listOf(br.outcomes.joinToString("  +  ") { n2(it.p) + " × " + mn(it.payoff) }, br.outcomes.joinToString("  +  ") { mn(it.p * it.payoff) }, mn(br.emv))))
            steps += DrillStep("Subtract what that branch costs to take. It comes off once, here at the branch, and never inside each outcome.",
                chain("Net", listOf("EMV − cost", mn(br.emv) + " - " + mn(br.cost), mn(br.net))))
        }
        steps += DrillStep("At the decision node, take the branch with the largest net value.",
            built.joinToString("\n") { padR(it.name, 22) + padL(mn(it.net), 10) + if (it === best) "   ◀ best" else "" })
        steps += DrillStep("The recommendation is ${best.name}, worth ${money(best.net)} net, which is ${money(abs(a.net - b.net))} ahead of the alternative. Work right to left through a tree: payoffs, then chance nodes, then the decision.")
        return DrillProblem("decision-tree", "risk", "Decision tree roll-up",
            "A decision on ${projectName(r)} comes down to two options, each with the same two possible states of demand. Calculate the net expected value of the option you should recommend.",
            given, "£", best.net, 2, 1.0,
            listOf(
                DrillAlt(best.emv, "That is the EMV at the chance node, before the branch's own cost is taken off."),
                DrillAlt(other.net, "That is the net value of the other branch. The decision node takes the larger of the two."),
            ), steps, "Subtract the branch cost once, at the branch. Probabilities at any one chance node must total 1.0.")
    }

    /* ── procurement ─────────────────────────────────────────────────────── */

    private class Pta(val targetCost: Double, val targetFee: Double, val buyerPct: Int, val ceiling: Double) {
        val targetPrice get() = targetCost + targetFee
        val buyerShare get() = buyerPct / 100.0
        val pta get() = (ceiling - targetPrice) / buyerShare + targetCost
    }

    private fun ptaSetup(r: Rand): Pta {
        val targetCost = between(r, 150000.0, 600000.0, 10000.0)
        val targetFee = roundTo(targetCost * between(r, 0.08, 0.15, 0.01), 1000.0)
        val buyerPct = pick(r, listOf(60, 65, 70, 75, 80))
        val ceiling = roundTo((targetCost + targetFee) * between(r, 1.08, 1.22, 0.02), 5000.0)
        return Pta(targetCost, targetFee, buyerPct, ceiling)
    }

    private fun ptaGivens(s: Pta) = listOf(
        "Target cost" to money(s.targetCost), "Target fee" to money(s.targetFee), "Ceiling price" to money(s.ceiling),
        "Share (buyer / seller)" to "${mn(s.buyerPct)} / ${mn(100 - s.buyerPct)}",
    )

    private fun gPta(r: Rand): DrillProblem {
        val s = ptaSetup(r)
        return DrillProblem("pta", "procure", "Point of total assumption",
            "A fixed price incentive fee contract on ${projectName(r)} is agreed on the terms below. Calculate the point of total assumption.",
            ptaGivens(s), "£", s.pta, 2, 1.0,
            listOf(
                DrillAlt((s.ceiling - s.targetPrice) / (1 - s.buyerShare) + s.targetCost, "The divisor is the buyer's share, not the seller's. The buyer is the one still absorbing cost below the PTA."),
                DrillAlt((s.ceiling - s.targetPrice) / s.buyerShare + s.targetPrice, "The addend is the target cost, not the target price. Adding the fee back in double-counts it."),
                DrillAlt(s.ceiling - s.targetPrice, "That is only the headroom above the target price. It still has to be divided by the buyer's share and added to the target cost."),
            ),
            listOf(
                DrillStep("Target price first, because the formula works from the headroom between it and the ceiling.",
                    chain("Target price", listOf("target cost + target fee", mn(s.targetCost) + " + " + mn(s.targetFee), mn(s.targetPrice)))),
                DrillStep("The headroom is how much of a cost overrun the ceiling still leaves room for.",
                    chain("Headroom", listOf("ceiling − target price", mn(s.ceiling) + " - " + mn(s.targetPrice), mn(s.ceiling - s.targetPrice)))),
                DrillStep("Below the PTA the buyer absorbs their share of each pound of overrun, so the headroom stretches by one over the buyer's share. Then add the target cost, which is where the overrun is measured from.",
                    chain("PTA", listOf("(headroom ÷ buyer share) + target cost", mn(s.ceiling - s.targetPrice) + " / " + n2(s.buyerShare) + " + " + mn(s.targetCost),
                        mn((s.ceiling - s.targetPrice) / s.buyerShare) + " + " + mn(s.targetCost), mn(s.pta)))),
                DrillStep("At an actual cost of ${money(s.pta)} the seller's fee has been eroded to the point where every further pound comes out of their own pocket. Below it the overrun is shared ${mn(s.buyerPct)}/${mn(100 - s.buyerPct)}; above it the split stops mattering."),
            ), "Divide by the buyer's share and add the target cost. Swapping either is the classic way to lose this mark.")
    }

    private fun gFee(r: Rand): DrillProblem {
        val s = ptaSetup(r)
        val over = r.next() < 0.65
        val actualCost = if (over) roundTo(s.targetCost * between(r, 1.04, 1.22, 0.02), 1000.0)
            else roundTo(s.targetCost * between(r, 0.86, 0.98, 0.02), 1000.0)
        val sellerShare = 1 - s.buyerShare
        val variance = s.targetCost - actualCost
        val fee = s.targetFee + variance * sellerShare
        val uncapped = actualCost + fee
        val price = min(uncapped, s.ceiling)
        val capped = uncapped > s.ceiling
        val askPrice = r.next() < 0.45
        val steps = mutableListOf(
            DrillStep("The seller's share is whatever the buyer's is not. It applies to a saving and to an overrun alike.",
                chain("Seller share", listOf("1 − buyer share", "1 - " + n2(s.buyerShare), n2(sellerShare)))),
            DrillStep("Then the cost variance against the target, keeping the sign: positive is an underrun, negative an overrun.",
                chain("Variance", listOf("target cost − actual cost", mn(s.targetCost) + " - " + mn(actualCost), mn(variance)))),
            DrillStep("The fee moves from the target fee by the seller's share of that variance.",
                chain("Fee", listOf("target fee + variance × seller share", mn(s.targetFee) + " + " + mn(variance) + " × " + n2(sellerShare),
                    mn(s.targetFee) + " + " + mn(variance * sellerShare), mn(fee)))),
        )
        if (askPrice) {
            steps += DrillStep("The price is what the buyer pays: the cost actually incurred plus that fee.",
                chain("Price", listOf("actual cost + fee", mn(actualCost) + " + " + mn(fee), mn(uncapped))))
            steps += DrillStep(if (capped) "Now apply the ceiling, and apply it last. The computed price is above ${money(s.ceiling)}, so the ceiling is what the buyer pays and the seller absorbs the difference."
                else "Check it against the ceiling of ${money(s.ceiling)}. It comes in below, so the ceiling does not bind and the computed price stands.",
                chain("Final price", listOf("min(${mn(uncapped)}, ${mn(s.ceiling)})", mn(price))))
        } else {
            steps += DrillStep(if (over) "The seller has overrun the target, so they fund ${pc(sellerShare, 0)} of the overrun out of their fee — ${money(-variance * sellerShare)} of it."
                else "The seller has come in under the target, so they keep ${pc(sellerShare, 0)} of the saving as extra fee — ${money(variance * sellerShare)} of it.")
        }
        // The uncapped price is a mistake only when the ceiling binds; otherwise it is the answer.
        val alts = if (askPrice) listOfNotNull(
            if (capped) DrillAlt(uncapped, "That is cost plus fee before the ceiling is applied. Here the ceiling binds, and it is applied last of all.") else null,
            DrillAlt(actualCost + s.targetFee, "That uses the target fee unchanged. The fee moves from target by the seller's share of the cost variance."),
        ) else listOf(
            DrillAlt(s.targetFee + variance * s.buyerShare, "That applies the buyer's share to the variance. The seller's fee moves by the seller's share."),
            DrillAlt(s.targetFee - variance * sellerShare, "The sign is inverted: an underrun raises the fee and an overrun lowers it."),
        )
        return DrillProblem("fee", "procure", if (askPrice) "Final price under an FPIF" else "Final fee under an FPIF",
            "The fixed price incentive fee contract below closes out at an actual cost of ${money(actualCost)}. Calculate the " +
                (if (askPrice) "final price the buyer pays" else "final fee the seller earns") + ".",
            ptaGivens(s) + ("Actual cost" to money(actualCost)), "£", if (askPrice) price else fee, 2, 1.0, alts, steps,
            "The share is quoted buyer/seller. Apply the seller's fraction to the fee, and apply the ceiling last of all.")
    }

    /* ── schedule ────────────────────────────────────────────────────────── */

    private fun gFloat(r: Rand): DrillProblem {
        val d = linkedMapOf(
            "A" to between(r, 2, 5), "B" to between(r, 3, 7), "C" to between(r, 1, 4),
            "D" to between(r, 3, 8), "E" to between(r, 2, 5), "F" to between(r, 2, 4),
        )
        // The lower branch must be the slack one, by a visible margin, or the float question has no content.
        val gap = (d["B"]!! + d["D"]!!) - (d["C"]!! + d["E"]!!)
        if (gap < 2) d["D"] = d["D"]!! + (2 - gap) + between(r, 0, 2)
        val list = listOf(
            NetInput("A", d["A"]!!.toDouble(), emptyList()), NetInput("B", d["B"]!!.toDouble(), listOf("A")),
            NetInput("C", d["C"]!!.toDouble(), listOf("A")), NetInput("D", d["D"]!!.toDouble(), listOf("B")),
            NetInput("E", d["E"]!!.toDouble(), listOf("C")), NetInput("F", d["F"]!!.toDouble(), listOf("D", "E")),
        )
        val net = Calculators.network(list)
        val slack = (d["B"]!! + d["D"]!!) - (d["C"]!! + d["E"]!!)
        val which = pick(r, listOf("duration", "tfC", "tfE", "ffE", "ffC"))
        val target = mapOf("tfC" to "C", "tfE" to "E", "ffE" to "E", "ffC" to "C")[which]
        val act = target?.let { net.byId(it) }
        val pathTop = d["A"]!! + d["B"]!! + d["D"]!! + d["F"]!!
        val pathBottom = d["A"]!! + d["C"]!! + d["E"]!! + d["F"]!!
        val ask = mapOf(
            "duration" to "How long will the project take?", "tfC" to "What is the total float of activity C?",
            "tfE" to "What is the total float of activity E?", "ffE" to "What is the free float of activity E?",
            "ffC" to "What is the free float of activity C?",
        )[which]!!
        val total = which.startsWith("t")
        val answer = when { which == "duration" -> net.duration; total -> act!!.tf; else -> act!!.ff }
        val f0 = { x: Double -> n0(x) }
        val steps = mutableListOf(
            DrillStep("There are two paths through this network. Add the durations along each: the longer one is the critical path and it sets the project duration.",
                "A-B-D-F : " + listOf("A", "B", "D", "F").joinToString(" + ") { d[it]!!.toString() } + " = " + mn(pathTop) + "   ◀ longest\n" +
                    "A-C-E-F : " + listOf("A", "C", "E", "F").joinToString(" + ") { d[it]!!.toString() } + " = " + mn(pathBottom) + "\n" +
                    "Project duration = " + mn(net.duration) + " days"),
            DrillStep("Forward pass, left to right. An activity starts the day after the latest of its predecessors finishes, and day numbering means a task of n days starting on day s finishes on day s + n − 1.",
                net.acts.joinToString("\n") { padR(it.id, 3) + "dur " + padL(mn(it.dur), 2) + "    ES " + padL(mn(it.es), 3) + "    EF " + padL(mn(it.ef), 3) }, plain = true),
            DrillStep("Backward pass, right to left from the project finish on day ${mn(net.duration)}. Take the minimum of the successors' late starts, not the maximum: the tightest successor is the binding one.",
                net.acts.reversed().joinToString("\n") { padR(it.id, 3) + "dur " + padL(mn(it.dur), 2) + "    LS " + padL(mn(it.ls), 3) + "    LF " + padL(mn(it.lf), 3) }, plain = true),
            DrillStep("Total float is the difference between the late and early dates, and free float is measured against the earliest start of the successors instead.",
                net.acts.joinToString("\n") { padR(it.id, 3) + "TF " + padL(mn(it.tf), 3) + "    FF " + padL(mn(it.ff), 3) + if (it.critical) "    critical" else "" }, plain = true),
        )
        when {
            which == "duration" -> steps += DrillStep("The longest path is the answer, and it is worth saying why that is not a contradiction: the critical path is the longest route through the work, which is exactly what makes it the shortest time the project can be finished in.",
                chain("Duration", listOf(mn(net.duration) + " days")))
            total -> {
                steps += DrillStep("For activity $target, total float is how long it can slip before the project end date moves.",
                    chain("TF", listOf("LS − ES", mn(act!!.ls) + " - " + mn(act.es), mn(act.tf))))
                steps += DrillStep("That float belongs to the path, not to the activity. The lower branch runs ${mn(pathBottom)} days against the critical ${mn(pathTop)}, so C and E share the same ${mn(slack)} days of total float between them. Spend it on C and E has none left.")
            }
            else -> {
                val a = act!!
                val succEs = if (a.succs.isNotEmpty()) a.succs.minOf { net.byId(it)!!.es } else net.duration + 1
                steps += DrillStep("For activity $target, free float is how long it can slip before it delays any successor's early start.",
                    chain("FF", listOf("min(ES of successors) − EF − 1", mn(succEs) + " - " + mn(a.ef) + " - 1", mn(a.ff))))
                steps += DrillStep(if (target == "C") "Zero, even though C carries ${mn(a.tf)} days of total float. E starts the very next day, so any slip on C pushes E straight away. The float is real but it belongs to the path, and C cannot use it without taking it from E."
                    else "E is the last activity on the slack branch before the merge at F, so it is the one that can actually absorb the ${mn(a.ff)} days without moving anything. Free float is never greater than total float, and a merge point is where the two most often differ.")
            }
        }
        val alts = when {
            which == "duration" -> listOf(DrillAlt(pathBottom.toDouble(), "That is the shorter path. A project cannot finish before its longest path does, which is exactly why the longest path sets the duration."))
            total -> listOf(
                DrillAlt(act!!.ff, "That is the free float. Total float measures against the project end date; free float measures against the next activity's early start."),
                DrillAlt(0.0, "Zero float would mean $target is on the critical path. It is on the lower branch, which runs ${mn(pathBottom)} days against the critical ${mn(pathTop)}."),
            )
            else -> listOf(
                DrillAlt(act!!.tf, "That is the total float, which the whole branch shares. Free float is only what this one activity can absorb without moving its own successor."),
                DrillAlt(0.0, "Zero is right for an activity whose successor starts the very next day. $target is the last activity before the merge at F, so it has room that C does not."),
            )
        }
        return DrillProblem("float", "schedule", if (which == "duration") "Project duration" else if (total) "Total float" else "Free float",
            "The network below describes ${projectName(r)}. $ask",
            list.map { "${it.id} (${f0(it.dur)}d)" to if (it.preds.isEmpty()) "start" else "after " + it.preds.joinToString(", ") },
            "days", answer, 0, 0.4, alts, steps,
            "Total float is against the project finish; free float is against the next activity. Free float is never the larger of the two.")
    }

    /* ── finance ─────────────────────────────────────────────────────────── */

    private fun gNpv(r: Rand): DrillProblem {
        val outlay = between(r, 200000.0, 700000.0, 20000.0)
        val ratePct = pick(r, listOf(8, 10, 12))
        val rate = ratePct / 100.0
        val years = between(r, 3, 4)
        val flows = (0 until years).map { between(r, 80000.0, 320000.0, 10000.0) }
        val c = Calculators.cash(outlay, flows, rate)
        val undiscounted = flows.sum() - outlay
        return DrillProblem("npv", "finance", "Net present value",
            "A business case for ${projectName(r)} needs an outlay of ${money(outlay)} today, against the returns below. At a discount rate of ${mn(ratePct)}%, what is the net present value?",
            listOf("Outlay now" to money(outlay), "Discount rate" to "${mn(ratePct)}%") + flows.mapIndexed { i, f -> "Year ${i + 1}" to money(f) },
            "£", c.npv, 2, max(50.0, abs(c.npv) * 0.01),
            listOf(
                DrillAlt(undiscounted, "That is the undiscounted total less the outlay. Each year's return has to be divided by (1 + r) raised to that year."),
                DrillAlt(c.pvIn, "That is the present value of the returns. NPV is net, so the outlay still comes off."),
            ),
            listOf(
                DrillStep("Discount each year's return back to today. The divisor is (1 + r) raised to the number of the year, so a return further out is worth proportionally less.",
                    flows.mapIndexed { i, f -> "Year ${i + 1}   " + padL(mn(f), 8) + " / " + n2((1 + rate).pow(i + 1)) + "  =  " + padL(n2(c.discounted[i]), 11) }.joinToString("\n"), plain = true),
                DrillStep("Add the discounted returns to get the present value of everything coming in.",
                    chain("PV in", listOf(c.discounted.joinToString(" + ") { n2(it) }, n2(c.pvIn)))),
                DrillStep("Then take off the outlay, which needs no discounting because it is paid today.",
                    chain("NPV", listOf("PV in − outlay", n2(c.pvIn) + " - " + mn(outlay), n2(c.npv)))),
                DrillStep(if (c.npv >= 0) "Positive, so the project earns more than the ${mn(ratePct)}% the money could earn elsewhere, and it is worth doing on this test. When two projects are compared, the higher NPV wins and their durations are irrelevant."
                    else "Negative, so at ${mn(ratePct)}% the returns do not cover the outlay once the timing is accounted for. The project destroys value on this test."),
            ), "NPV is already net of the investment. Do not subtract the outlay twice, and do not compare projects on duration.")
    }

    private fun gPayback(r: Rand): DrillProblem {
        val outlay = between(r, 150000.0, 500000.0, 10000.0)
        val flows = (0 until 4).map { between(r, 60000.0, 200000.0, 5000.0) }.toMutableList()
        // Guarantee recovery inside the horizon, or the answer is "never".
        val total = flows.sum()
        if (total <= outlay) flows[3] += outlay - total + between(r, 20000.0, 80000.0, 5000.0)
        val pb = Calculators.payback(outlay, flows)!!
        val fullYears = floor(pb).toInt()
        val recovered = flows.take(fullYears).sum()
        val outstanding = outlay - recovered
        var cum = -outlay
        val lines = flows.mapIndexed { i, f -> cum += f; "Year ${i + 1}   +" + padL(mn(f), 8) + "    cumulative " + padL(mn(cum), 10) }
        val inYear = flows[min(fullYears, flows.size - 1)]
        return DrillProblem("payback", "finance", "Payback period",
            "An investment of ${money(outlay)} in ${projectName(r)} returns the amounts below. Calculate the payback period in years.",
            listOf("Outlay" to money(outlay)) + flows.mapIndexed { i, f -> "Year ${i + 1}" to money(f) },
            "years", pb, 2, 0.02,
            listOf(
                DrillAlt((fullYears + 1).toDouble(), "That rounds up to the whole year. Payback is normally quoted with the part-year interpolated."),
                DrillAlt(fullYears.toDouble(), "That is the last full year before recovery. The outstanding balance is recovered part way through the following one."),
            ),
            listOf(
                DrillStep("Run the cumulative cash flow forward from the outlay and find the year the balance turns positive.",
                    "Start     " + padL(mn(-outlay), 10) + "\n" + lines.joinToString("\n"), plain = true),
                DrillStep("After ${mn(fullYears)} full years, ${money(recovered)} has come back, so this much of the outlay is still outstanding.",
                    chain("Outstanding", listOf("outlay − recovered", mn(outlay) + " - " + mn(recovered), mn(outstanding)))),
                DrillStep("That balance is cleared part way through year ${mn(fullYears + 1)}. Assuming the cash arrives evenly across the year, the fraction is the outstanding balance over that year's return.",
                    chain("Fraction", listOf(mn(outstanding) + " / " + mn(inYear), n2(outstanding / inYear)))),
                DrillStep("Add the two together.", chain("Payback", listOf(mn(fullYears) + " + " + n2(outstanding / inYear), n2(pb) + " years"))),
                DrillStep("Payback says nothing about what happens after the recovery date and nothing about the time value of money. A project that pays back in two years and then stops beats one that pays back in three and runs for a decade, on this measure alone — which is exactly why it is never used alone."),
            ), "Interpolate the part-year. Payback ignores everything after recovery, and it is undiscounted unless the question says otherwise.")
    }

    private fun gIrr(r: Rand): DrillProblem {
        val r1 = pick(r, listOf(8, 10, 12))
        val r2 = r1 + pick(r, listOf(4, 5, 6))
        // The fraction is drawn as an exact two-place share (never 0.50, which would make the midpoint shortcut right).
        val span = pick(r, listOf(100000.0, 120000.0, 140000.0, 160000.0, 200000.0, 240000.0))
        val share = pick(r, listOf(0.25, 0.30, 0.35, 0.40, 0.45, 0.55, 0.60, 0.65, 0.70, 0.75))
        val npv1 = round(span * share, 2)
        val npv2 = round(npv1 - span, 2)
        val irrEst = r1 + share * (r2 - r1)
        val frac = npv1 / (npv1 - npv2)
        return DrillProblem("irr", "finance", "IRR by interpolation",
            "The business case for ${projectName(r)} has been discounted at two rates, giving the net present values below. Estimate the internal rate of return by interpolating between them.",
            listOf("NPV at ${mn(r1)}%" to money(npv1), "NPV at ${mn(r2)}%" to money(npv2)), "%", irrEst, 2, 0.1,
            listOf(
                DrillAlt((r1 + r2) / 2.0, "That is the midpoint, which is only the answer when the two NPVs happen to be equal in size. Here they are not, so the crossing is nearer the smaller one."),
                DrillAlt(r2 - frac * (r2 - r1), "The fraction is measured from the lower rate upwards. Start at ${mn(r1)}% and add."),
            ),
            listOf(
                DrillStep("The internal rate of return is the discount rate at which net present value is zero. One of these rates gives a positive NPV and the other a negative one, so the crossing lies between them.",
                    "${mn(r1)}%  →  " + padL(mn(npv1), 9) + "   positive\n" + "${mn(r2)}%  →  " + padL(mn(npv2), 9) + "   negative", plain = true),
                DrillStep("Work out how far along the gap the line crosses zero. The total vertical distance travelled is the first NPV minus the second, and subtracting a negative adds the two magnitudes together.",
                    chain("Fraction", listOf("NPV₁ ÷ (NPV₁ − NPV₂)", mn(npv1) + " / (" + mn(npv1) + " - " + mn(npv2) + ")", mn(npv1) + " / " + mn(npv1 - npv2), n2(frac)))),
                DrillStep("Apply that fraction to the gap between the two rates and add it to the lower one.",
                    chain("IRR", listOf("r₁ + fraction × (r₂ − r₁)", mn(r1) + " + " + n2(frac) + " × " + mn(r2 - r1), mn(r1) + " + " + n2(frac * (r2 - r1)), n2(irrEst) + "%"))),
                DrillStep("This is a straight-line approximation across a curve, so it is an estimate rather than the exact rate. The exam never asks you to compute an IRR from first principles; it asks you to read one against a hurdle rate, and here anything above the cost of capital means the project clears its bar."),
            ), "Interpolate upwards from the lower rate. The denominator subtracts a negative, so it is the sum of the two magnitudes.")
    }

    private fun gBcr(r: Rand): DrillProblem {
        val costs = between(r, 200000.0, 900000.0, 20000.0)
        val ratio = between(r, 0.75, 1.9, 0.05)
        val benefits = roundTo(costs * ratio, 5000.0)
        val bcr = benefits / costs
        return DrillProblem("bcr", "finance", "Benefit–cost ratio",
            "Two figures have been prepared for ${projectName(r)}, both discounted to present value. Calculate the benefit–cost ratio.",
            listOf("PV of benefits" to money(benefits), "PV of costs" to money(costs)), "ratio", bcr, 2, 0.005,
            listOf(
                DrillAlt(costs / benefits, "The ratio is inverted. Benefits go on top."),
                DrillAlt(benefits - costs, "That is the net benefit in currency. A ratio is a division."),
            ),
            listOf(
                DrillStep("The ratio is simply benefits over costs, both at present value so that the timing is already accounted for.",
                    chain("BCR", listOf("benefits ÷ costs", mn(benefits) + " / " + mn(costs), n2(bcr)))),
                DrillStep(if (bcr >= 1) "Above 1.0, so the benefits exceed the costs and the project clears the bar. Every ${money(1.0)} of cost returns ${money(bcr)} of benefit."
                    else "Below 1.0, so the project loses money even though the figure is positive. The comparison is always against 1.0, never against zero — a BCR of ${n2(bcr)} is a loss-making case."),
            ), "Compare a BCR against 1.0, not against zero. Anything below 1.0 is a loss.")
    }

    /* ── agile and flow ──────────────────────────────────────────────────── */

    private fun gLittle(r: Rand): DrillProblem {
        val solveFor = pick(r, listOf("ct", "thr", "wip"))
        // An even throughput and a cycle time in half weeks keep all three figures exact.
        val thr = pick(r, listOf(2.0, 4.0, 6.0, 8.0, 10.0))
        val ct = pick(r, listOf(1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 5.0))
        val wip = thr * ct
        val steps = ArrayList<DrillStep>(); val alts = ArrayList<DrillAlt>()
        val answer: Double; val ask: String; val unit: String
        when (solveFor) {
            "ct" -> {
                answer = ct; unit = "weeks"; ask = "the average cycle time"
                steps += DrillStep("Little's Law relates the three in one statement: the average cycle time of a stable system is the work in progress divided by the throughput.",
                    chain("CT", listOf("WIP ÷ throughput", "${mn(wip)} items / ${mn(thr)} per week", "${n2(ct)} weeks")))
                steps += DrillStep("Check the units cancel: items divided by items per week leaves weeks, which is what a cycle time is measured in. If the units do not come out right, the formula has been used upside down.")
                steps += DrillStep("This is the argument for a work-in-progress limit stated arithmetically. Cut the WIP from ${mn(wip)} to ${mn(wip / 2)} while the team keeps finishing ${mn(thr)} items a week, and the cycle time halves to ${n2(ct / 2)} weeks. Nobody worked faster; there was simply less work sitting in the queue.")
                alts += DrillAlt(wip * thr, "That multiplies. Cycle time is WIP divided by throughput — check the units: items × items per week is not a time.")
                alts += DrillAlt(thr / wip, "The ratio is inverted, which gives a rate rather than a duration.")
            }
            "thr" -> {
                answer = thr; unit = "items per week"; ask = "the throughput"
                steps += DrillStep("Rearranging Little's Law for throughput gives work in progress over cycle time.",
                    chain("Throughput", listOf("WIP ÷ cycle time", mn(wip) + " / " + n2(ct), "${n2(thr)} items per week")))
                steps += DrillStep("Items divided by weeks gives items per week, which confirms the arrangement is the right way up.")
                alts += DrillAlt(wip * ct, "That multiplies rather than divides.")
            }
            else -> {
                answer = wip; unit = "items"; ask = "the work in progress"
                steps += DrillStep("Rearranging Little's Law for work in progress gives throughput multiplied by cycle time.",
                    chain("WIP", listOf("throughput × cycle time", mn(thr) + " × " + n2(ct), "${n2(wip)} items")))
                steps += DrillStep("Items per week multiplied by weeks leaves items, which is what work in progress counts.")
                alts += DrillAlt(thr / ct, "That divides. Solving for WIP is the one arrangement that multiplies.")
            }
        }
        val given = ArrayList<Pair<String, String>>()
        if (solveFor != "wip") given += "Work in progress" to "${mn(wip)} items"
        if (solveFor != "thr") given += "Throughput" to "${mn(thr)} items per week"
        if (solveFor != "ct") given += "Cycle time" to "${n2(ct)} weeks"
        return DrillProblem("littles-law", "agile", "Little's Law",
            "A Kanban team on ${projectName(r)} has been running steadily for a quarter. From the figures below, calculate $ask.",
            given, unit, answer, 2, 0.02, alts, steps, "Only one of the three arrangements multiplies. Check the units and the right one falls out.")
    }

    private fun gVelocity(r: Rand): DrillProblem {
        val v = intArrayOf(between(r, 14, 34), between(r, 14, 34), between(r, 14, 34))
        // The average is divided into the backlog next, so it is nudged onto a whole number.
        v[2] += (3 - ((v[0] + v[1] + v[2]) % 3)) % 3
        val avg = (v[0] + v[1] + v[2]) / 3.0
        var remaining = between(r, 120, 400, 5)
        // A backlog that divides exactly makes the rounding step, the point of this item, do nothing.
        if (remaining % avg == 0.0) remaining += 5
        val iterations = remaining / avg
        return DrillProblem("velocity", "agile", "Iterations remaining",
            "A team on ${projectName(r)} has completed three iterations at the velocities below and has ${mn(remaining)} story points left in the backlog. How many further iterations will it take, rounded up to a whole iteration?",
            listOf("Iteration 1" to "${mn(v[0])} points", "Iteration 2" to "${mn(v[1])} points", "Iteration 3" to "${mn(v[2])} points", "Backlog remaining" to "${mn(remaining)} points"),
            "iterations", ceil(iterations), 0, 0.4,
            listOf(
                DrillAlt(round(iterations, 2), "That is the unrounded figure. Iterations are whole and a part iteration still needs a full one, so it rounds up."),
                DrillAlt(floor(iterations), "Rounding down leaves ${n2((iterations - floor(iterations)) * avg)} points with nowhere to go."),
            ),
            listOf(
                DrillStep("Average the observed velocities. Three iterations is the usual window: enough to smooth a bad sprint, recent enough to still describe this team.",
                    chain("Velocity", listOf("(" + v.joinToString(" + ") + ") ÷ 3", mn(v.sum()) + " / 3", n2(avg) + " points per iteration"))),
                DrillStep("Divide the remaining backlog by that rate.", chain("Iterations", listOf("points remaining ÷ velocity", mn(remaining) + " / " + n2(avg), n2(iterations)))),
                DrillStep("Round up: a part iteration still occupies a whole one.", chain("Answer", listOf("⌈" + n2(iterations) + "⌉", mn(ceil(iterations)) + " iterations"))),
                DrillStep("Only accepted work counts towards velocity, so a story that is nearly finished contributes nothing this iteration. And velocity is a forecasting tool for one team — comparing it between teams measures how they happened to size their points, not how much they deliver."),
            ), "Average first, then divide, then round up. Velocity counts accepted work only.")
    }

    private fun gLeadTime(r: Rand): DrillProblem {
        // Backlog and WIP are whole multiples of the throughput, so both divisions are exact.
        val thr = pick(r, listOf(2, 4, 6))
        val lead = between(r, 4, 14)
        val cycle = pick(r, listOf(0.5, 1.0, 1.5, 2.0))
        val backlog = thr * lead
        val wip = thr * cycle
        return DrillProblem("lead-time", "agile", "Lead time from the queue",
            "A support team on ${projectName(r)} has ${mn(backlog)} items waiting, including ${mn(wip)} actively being worked, and finishes ${mn(thr)} items a week. A new request arrives today and joins the back of the queue. How long before it is delivered?",
            listOf("Items in the system" to mn(backlog), "Of those, in progress" to mn(wip), "Throughput" to "${mn(thr)} per week"),
            "weeks", lead.toDouble(), 2, 0.02,
            listOf(DrillAlt(cycle, "That is the cycle time, which starts when someone picks the item up. A new request has to wait behind everything already in the system first, and that whole wait is what the customer experiences.")),
            listOf(
                DrillStep("Little's Law applies to whichever part of the system you draw the boundary around. Draw it around the queue as well as the work, and the answer is the lead time — what the customer actually waits.",
                    chain("Lead time", listOf("items in the system ÷ throughput", mn(backlog) + " / " + mn(thr), n2(lead.toDouble()) + " weeks"))),
                DrillStep("Draw the boundary around only the items in progress and the same law gives the cycle time instead, which is much shorter.",
                    chain("Cycle time", listOf("WIP ÷ throughput", mn(wip) + " / " + mn(thr), n2(cycle) + " weeks"))),
                DrillStep("The difference of ${n2(lead - cycle)} weeks is time the item spends waiting before anyone touches it. Lead time is always at least the cycle time and usually far longer, and the gap between them is queue. That gap is what shrinks when work in progress is limited — which is why the customer's experience improves even though nobody is working any faster."),
            ), "Lead time starts when the customer asks; cycle time starts when work begins. The gap is queue.")
    }
}
