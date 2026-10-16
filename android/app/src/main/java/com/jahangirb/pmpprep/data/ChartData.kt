package com.jahangirb.pmpprep.data

import kotlin.math.floor
import kotlin.math.min

/** One point of the rolling accuracy line: after [n] answers, the mean of the last [window]. [full] once the window has filled. */
data class TrendPoint(val n: Int, val value: Double, val full: Boolean)

/** One ECO task cell: [band] 0 when thin (fewer than the minimum answers), else 1–4 on the mock-band scale. */
data class HeatCell(val task: EcoTask, val n: Int, val accuracy: Double, val band: Int) {
    val thin: Boolean get() = band == 0
}

/** A pace histogram bin, [hi] null for the open-ended last one. */
data class PaceBin(val lo: Int, val hi: Int?, val n: Int)

/** Sure vs not sure, against right vs wrong. Think so and Guess both count as not sure. */
data class Calibration(val sureRight: Int, val sureWrong: Int, val unsureRight: Int, val unsureWrong: Int) {
    val sure: Int get() = sureRight + sureWrong
    val unsure: Int get() = unsureRight + unsureWrong
    val total: Int get() = sure + unsure
    val sureAccuracy: Double? get() = if (sure == 0) null else sureRight.toDouble() / sure
    val unsureAccuracy: Double? get() = if (unsure == 0) null else unsureRight.toDouble() / unsure
}

/**
 * The numbers behind the analytics charts (index.html §11): accuracy trend, ECO task heat map, pace
 * histogram, confidence calibration and the mistake causes. Pure, so the screen only draws.
 *
 * Every chart reads the same answers as readiness: lesson checks and hinted answers are left out.
 */
object ChartData {
    const val TREND_WINDOW = 50
    /** The trend isn't drawn before this many answers. */
    const val TREND_START = 10
    const val PACE_BIN_SEC = 15
    const val PACE_BINS = 13

    fun counted(attempts: List<Attempt>): List<Attempt> = attempts.filter { it.source != Readiness.SOURCE_LESSON && !it.hinted }

    /**
     * A rolling mean of credit over the last [window] answers, by answer number rather than date, so equal
     * evidence gets equal width. Points before the window fills are marked not [TrendPoint.full] (drawn dashed).
     */
    fun trend(attempts: List<Attempt>, window: Int = TREND_WINDOW, start: Int = TREND_START): List<TrendPoint> {
        val list = counted(attempts).sortedBy { it.at }
        if (list.size < start) return emptyList()
        val out = ArrayList<TrendPoint>()
        var sum = 0.0
        for (i in list.indices) {
            sum += list[i].credit
            if (i >= window) sum -= list[i - window].credit
            if (i + 1 < start) continue
            val w = min(i + 1, window)
            out += TrendPoint(i + 1, sum / w, w >= window)
        }
        return out
    }

    /** Thins a long trend for drawing: every [step]th point and the last; the shape of a rolling mean survives. */
    fun thin(points: List<TrendPoint>, max: Int = 240): List<TrendPoint> {
        val step = maxOf(1, (points.size + max - 1) / max)
        return points.filterIndexed { k, _ -> k % step == 0 || k == points.size - 1 }
    }

    /** The 26 tasks with accuracy and band; a task under [minSample] answers is thin (shown grey, with its count). */
    fun heat(tasks: List<EcoTask>, attempts: List<Attempt>, bands: MockBands, minSample: Int): List<HeatCell> {
        val by = counted(attempts).groupBy { it.ecoTask }
        return tasks.map { t ->
            val l = by[t.id].orEmpty()
            val acc = if (l.isEmpty()) 0.0 else l.sumOf { it.credit } / l.size
            val band = when {
                l.size < minSample -> 0
                acc >= bands.above -> 4
                acc >= bands.target -> 3
                acc >= bands.below -> 2
                else -> 1
            }
            HeatCell(t, l.size, acc, band)
        }
    }

    /** Seconds per answer for the pace chart: answers with a real time on them (over half a second). */
    fun paceSamples(attempts: List<Attempt>): List<Double> = counted(attempts).filter { it.ms > 500 }.map { it.ms / 1000.0 }

    /** 15-second bins, 0–15 … 165–180, then 180+. */
    fun paceHistogram(attempts: List<Attempt>): List<PaceBin> {
        val counts = IntArray(PACE_BINS)
        paceSamples(attempts).forEach { s -> counts[min(PACE_BINS - 1, floor(s / PACE_BIN_SEC).toInt().coerceAtLeast(0))]++ }
        return (0 until PACE_BINS).map { k -> PaceBin(k * PACE_BIN_SEC, if (k == PACE_BINS - 1) null else (k + 1) * PACE_BIN_SEC, counts[k]) }
    }

    fun median(values: List<Double>): Double? {
        if (values.isEmpty()) return null
        val s = values.sorted()
        val m = s.size / 2
        return if (s.size % 2 == 1) s[m] else (s[m - 1] + s[m]) / 2
    }

    /** Answers with a confidence mark, split Sure (3) against Think so / Guess (2, 1). */
    fun calibration(attempts: List<Attempt>): Calibration {
        var sr = 0; var sw = 0; var ur = 0; var uw = 0
        // Hinted answers already leave; a guess stays, since a right guess is exactly what this chart is about.
        for (a in attempts) {
            if (a.source == Readiness.SOURCE_LESSON || a.hinted || a.confidence < 1) continue
            if (a.confidence >= Attempt.SURE) { if (a.correct) sr++ else sw++ } else { if (a.correct) ur++ else uw++ }
        }
        return Calibration(sr, sw, ur, uw)
    }

    /** The verdict under the calibration chart, in the web's words. */
    fun calibrationVerdict(c: Calibration, minSample: Int): String {
        val sure = c.sureAccuracy; val unsure = c.unsureAccuracy
        return when {
            c.sure < minSample || c.unsure < minSample -> "One of the two rows is still under $minSample answers, so the comparison isn't drawn yet."
            sure != null && sure < 0.7 -> "You are wrong about a third of the time when you feel sure, and that is the expensive kind of wrong: on the day nothing will prompt you to look again. The mistake journal is where to find out why."
            sure != null && unsure != null && sure - unsure < 0.1 -> "Your two confidence states score about the same, which means the feeling is not carrying information yet. Marking more answers will either separate them or confirm it."
            else -> "Sure and Not sure are separating cleanly, so your own sense of a question is worth trusting — including when it tells you to slow down and re-read."
        }
    }

    /** Tagged mistakes per cause, in the shared order; causes with none are kept so the chart's axis is stable. */
    fun causes(tags: Collection<MistakeTag>, order: List<RootCause>): List<Pair<RootCause, Int>> {
        val by = tags.groupingBy { it.cause }.eachCount()
        return order.map { it to (by[it] ?: 0) }
    }

    /** The formula-lab drill log by sheet group: (answered, right), groups in first-seen order. Unknown generators fall under "other". */
    fun drillsByGroup(drills: List<DrillRun>): List<Triple<String, Int, Int>> {
        val m = LinkedHashMap<String, IntArray>()
        drills.forEach { d ->
            val e = m.getOrPut(FormulaDrills.groupOf(d.generator) ?: "other") { IntArray(2) }
            e[0]++
            if (d.ok) e[1]++
        }
        return m.map { (k, v) -> Triple(k, v[0], v[1]) }
    }
}
