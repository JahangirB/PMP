package com.jahangirb.pmpprep.data

import kotlin.math.ceil
import kotlin.math.sqrt

/** One condition for booking, and whether it is met. */
data class BookingCriterion(val label: String, val target: String, val now: String, val met: Boolean)

/**
 * When the trend says the unmet scores will reach their targets. [earliest]..[latest] is a range once
 * there are four weeks of reports; before that only [latest] is set and it's [rough]. [stalled] names
 * scores that aren't rising, which can't be projected.
 */
data class BookingProjection(val earliest: Long?, val latest: Long?, val rough: Boolean, val stalled: List<String>, val afterExam: Boolean)

data class BookingReport(val criteria: List<BookingCriterion>, val projection: BookingProjection?, val mocksNeeded: Int) {
    val ready: Boolean get() = criteria.all { it.met }
}

/**
 * "Am I ready to book?" The thresholds are the app's judgement, not PMI's: readiness 75+, exam
 * fitness 70+, and two full mocks at Target (65%) or above.
 */
object Booking {
    const val READINESS = 75
    const val FITNESS = 70
    const val MOCKS = 2
    const val MOCK_PASS = 0.65
    private const val WEEK = 7 * Srs.DAY_MS

    fun check(readiness: Int, fitness: Int?, mocks: List<MockResult>, fullItems: Int, weekly: List<WeeklyReport>, examDate: Long?): BookingReport {
        val good = mocks.count { it.n >= fullItems && it.pct >= MOCK_PASS }
        val criteria = listOf(
            BookingCriterion("Readiness", "$READINESS+", "$readiness", readiness >= READINESS),
            BookingCriterion("Exam fitness", "$FITNESS+", fitness?.toString() ?: "not yet", (fitness ?: 0) >= FITNESS),
            BookingCriterion("Full mocks at Target or above", "$MOCKS", "$good", good >= MOCKS),
        )
        val weeks = weekly.sortedBy { it.weekStart }
        val series = buildList {
            if (readiness < READINESS) add(Triple("Readiness", READINESS, weeks.map { it.weekStart to it.readiness }))
            if ((fitness ?: 0) < FITNESS) add(Triple("Exam fitness", FITNESS, weeks.mapNotNull { w -> w.fitness?.let { w.weekStart to it } }))
        }
        val projection = if (series.isEmpty() || weeks.size < 2) null else project(series, examDate)
        return BookingReport(criteria, projection, (MOCKS - good).coerceAtLeast(0))
    }

    private fun project(series: List<Triple<String, Int, List<Pair<Long, Int>>>>, examDate: Long?): BookingProjection {
        val stalled = ArrayList<String>()
        var earliest = 0L; var latest = 0L; var open = false
        val range = series.all { it.third.size >= 4 }
        for ((label, target, pts) in series) {
            val fit = fit(pts)
            if (fit == null || fit.slope <= 0.0) { stalled += label; continue }
            val lastX = pts.last().first
            fun at(slope: Double): Long {
                val weeks = ((target - fit.valueAt(lastX)) / slope).coerceAtLeast(0.0)
                return lastX + ceil(weeks * 7 - 1e-6).toLong() * Srs.DAY_MS
            }
            if (range) {
                earliest = maxOf(earliest, at(fit.slope + fit.se))
                if (fit.slope - fit.se <= 0.0) open = true else latest = maxOf(latest, at(fit.slope - fit.se))
            } else {
                latest = maxOf(latest, at(fit.slope))
            }
        }
        if (stalled.isNotEmpty()) return BookingProjection(null, null, !range, stalled, false)
        val late = if (open) null else latest
        val after = examDate != null && (late == null || late > examDate)
        return BookingProjection(if (range) earliest else null, late, !range, stalled, after)
    }

    private class Fit(val slope: Double, val intercept: Double, val se: Double) {
        fun valueAt(x: Long) = intercept + slope * (x.toDouble() / WEEK)
    }

    /** Least squares in points per week. */
    private fun fit(pts: List<Pair<Long, Int>>): Fit? {
        if (pts.size < 2) return null
        val xs = pts.map { it.first.toDouble() / WEEK }
        val ys = pts.map { it.second.toDouble() }
        val mx = xs.average(); val my = ys.average()
        val sxx = xs.sumOf { (it - mx) * (it - mx) }
        if (sxx == 0.0) return null
        val slope = xs.indices.sumOf { (xs[it] - mx) * (ys[it] - my) } / sxx
        val intercept = my - slope * mx
        val se = if (pts.size < 3) 0.0 else sqrt(xs.indices.sumOf { val r = ys[it] - (intercept + slope * xs[it]); r * r } / (pts.size - 2) / sxx)
        return Fit(slope, intercept, se)
    }
}
