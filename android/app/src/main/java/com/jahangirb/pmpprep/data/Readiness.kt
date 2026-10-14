package com.jahangirb.pmpprep.data

import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.roundToInt

data class Component(
    val id: String,
    val label: String,
    val weight: Int,
    val score: Double,
    val ready: Boolean,
    val figure: String,
    val detail: String,
) {
    val points: Double get() = score * weight
}

data class ReadinessReport(
    val score: Int,
    val components: List<Component>,
    val provisional: Boolean,
    val gates: List<String>,
    val nextAction: String,
    val nextRoute: String,
)

data class Streak(val current: Int, val longest: Int, val last14: List<Boolean>, val activeInWindow: Int)

data class Group(val n: Int, val credit: Double, val right: Int, val meanSec: Double?) {
    val accuracy: Double get() = if (n == 0) 0.0 else credit / n
}

/**
 * The web app's readiness model (§10.00), ported line for line: five
 * components (accuracy, coverage, mocks, pace, habit) weighted by §01.3. A
 * component with no evidence scores zero rather than having its weight
 * shared out, and the total is marked provisional until enough has been
 * answered across enough tasks.
 */
class Readiness(
    private val r: ReadinessConfig,
    private val exam: ExamLayout,
    private val tasks: List<EcoTask>,
    /**
     * The exam's domain split. When given, accuracy is each domain's accuracy weighted by it, as
     * the exam weighs them, so a weak domain can't hide behind a strong one. Without it, one overall figure.
     */
    private val domainWeights: Map<Domain, Double> = emptyMap(),
) {

    /** Accuracy for the score: per domain and weighted like the exam, or overall. Also each domain's figure. */
    fun weightedAccuracy(attempts: List<Attempt>, now: Long): Triple<Double, Int, Map<Domain, Double>> {
        val (overall, n) = accuracy(attempts, now)
        if (domainWeights.isEmpty()) return Triple(overall, n, emptyMap())
        val byDomain = Domain.entries.associateWith { d -> accuracy(attempts, now) { it.domain == d }.first }
        val total = domainWeights.values.sum().takeIf { it > 0 } ?: return Triple(overall, n, byDomain)
        return Triple(Domain.entries.sumOf { (domainWeights[it] ?: 0.0) * byDomain[it]!! } / total, n, byDomain)
    }

    private fun ratio(v: Double, lo: Double, hi: Double) = if (hi == lo) 0.0 else ((v - lo) / (hi - lo)).coerceIn(0.0, 1.0)

    fun accuracy(attempts: List<Attempt>, now: Long, filter: ((Attempt) -> Boolean)? = null): Pair<Double, Int> {
        var list = attempts.filter { it.source != SOURCE_LESSON }
        if (filter != null) list = list.filter(filter)
        list = list.takeLast(r.accuracyWindow)
        var wSum = r.accuracyPriorN
        var cSum = r.accuracyPrior * r.accuracyPriorN
        for (a in list) {
            val ageDays = (now - a.at).toDouble() / Srs.DAY_MS
            val w = max(FLOOR, 0.5.pow(ageDays / r.accuracyHalflifeDays))
            wSum += w
            cSum += w * a.credit
        }
        return (cSum / wSum) to list.size
    }

    fun group(attempts: List<Attempt>, key: (Attempt) -> String?): Map<String, Group> {
        val m = HashMap<String, MutableList<Attempt>>()
        for (a in attempts) {
            if (a.source == SOURCE_LESSON) continue
            val k = key(a) ?: continue
            if (k.isEmpty()) continue
            m.getOrPut(k) { ArrayList() } += a
        }
        return m.mapValues { (_, l) ->
            val timed = l.filter { it.ms > 500 }
            Group(l.size, l.sumOf { it.credit }, l.count { it.correct }, if (timed.isEmpty()) null else timed.sumOf { it.ms } / timed.size / 1000.0)
        }
    }

    fun medianPace(attempts: List<Attempt>): Pair<Double?, Int> {
        val secs = attempts.filter { it.source != SOURCE_LESSON }.takeLast(r.accuracyWindow)
            .filter { it.ms > 500 }.map { it.ms / 1000.0 }.sorted()
        if (secs.isEmpty()) return null to 0
        val mid = secs.size / 2
        val median = if (secs.size % 2 == 1) secs[mid] else (secs[mid - 1] + secs[mid]) / 2
        return median to secs.size
    }

    fun streak(store: ProgressStore, now: Long): Streak {
        val days = HashSet<Long>()
        store.attempts.forEach { days += Srs.startOfDay(it.at) }
        store.mocks.forEach { days += Srs.startOfDay(it.at) }
        store.scenarioRuns.forEach { days += Srs.startOfDay(it.at) }
        store.cards.values.forEach { if (it.last > 0) days += Srs.startOfDay(it.last) }
        val today = Srs.startOfDay(now)
        var cursor = if (today in days) today else prevDay(today)
        var cur = 0
        while (cursor in days) { cur++; cursor = prevDay(cursor) }
        var best = 0; var run = 0; var prev: Long? = null
        for (d in days.sorted()) {
            run = if (prev != null && prevDay(d) == prev) run + 1 else 1
            best = max(best, run)
            prev = d
        }
        val window = (r.habitWindowDays - 1 downTo 0).map { i ->
            var d = today
            repeat(i) { d = prevDay(d) }
            d in days
        }
        return Streak(cur, best, window, window.count { it })
    }

    /** Calendar-correct across daylight-saving changes. */
    private fun prevDay(d: Long): Long = Srs.startOfDay(d - Srs.DAY_MS / 2)

    fun coverage(attempts: List<Attempt>): Triple<Double, Int, List<Pair<EcoTask, Int>>> {
        val byTask = group(attempts) { it.ecoTask }
        val target = r.coverageTargetPerTask
        var sum = 0.0; var touched = 0
        val short = ArrayList<Pair<EcoTask, Int>>()
        for (t in tasks) {
            val n = byTask[t.id]?.n ?: 0
            if (n > 0) touched++
            sum += min(n, target).toDouble() / target
            if (n < target) short += t to (target - n)
        }
        short.sortByDescending { it.second }
        return Triple(if (tasks.isEmpty()) 0.0 else sum / tasks.size, touched, short)
    }

    fun report(store: ProgressStore, now: Long = System.currentTimeMillis()): ReadinessReport {
        val atts = store.attempts
        val (acc, accN, accByDomain) = weightedAccuracy(atts, now)
        val (cov, touched, short) = coverage(atts)

        val recent = store.mocks.sortedByDescending { it.at }.take(r.mockWeights.size)
        var num = 0.0; var den = 0.0
        recent.forEachIndexed { i, m -> num += r.mockWeights[i] * m.pct; den += r.mockWeights[i] }
        val mockRaw = if (den > 0) num / den else 0.0
        var stale = 1.0
        var staleDays = 0
        if (recent.isNotEmpty()) {
            staleDays = floor((now - recent[0].at).toDouble() / Srs.DAY_MS).toInt()
            if (staleDays > r.mockStaleAfterDays) {
                stale = max(r.mockStaleFloor, 0.5.pow((staleDays - r.mockStaleAfterDays) / r.mockStaleHalflife))
            }
        }
        val mockScore = (if (den > 0) ratio(mockRaw, r.mockMin, r.mockMax) else 0.0) * stale

        val (median, paceN) = medianPace(atts)
        val paceReady = paceN >= r.paceMinSample
        val paceScore = when {
            !paceReady || median == null -> 0.0
            median >= r.paceBand[0] && median <= r.paceBand[1] -> 1.0
            median < r.paceBand[0] -> ratio(median, r.paceFastZero, r.paceBand[0])
            else -> ratio(r.paceSlowZero - median, 0.0, r.paceSlowZero - r.paceBand[1])
        }

        val st = streak(store, now)
        val pct = { v: Double -> "${(v * 100).roundToInt()}%" }
        val comps = listOf(
            Component("accuracy", "Accuracy", r.wAccuracy, ratio(acc, r.accuracyMin, r.accuracyMax), accN > 0,
                if (accN > 0) pct(acc) else "—",
                if (accN == 0) "No questions answered yet."
                else (if (accByDomain.isNotEmpty()) "Each domain weighted as on the exam: " +
                    Domain.entries.joinToString(", ") { "${it.short} ${pct(accByDomain[it]!!)} × ${pct(domainWeights[it] ?: 0.0)}" } + ". A domain you haven't practised counts as 50%. " else "") +
                    "Recent answers count more (half-life ${r.accuracyHalflifeDays.toInt()} days). Full marks at ${pct(r.accuracyMax)}, none below ${pct(r.accuracyMin)}. Across $accN answers."),
            Component("coverage", "ECO coverage", r.wCoverage, cov, touched > 0, "$touched / ${tasks.size}",
                "Counts up to ${r.coverageTargetPerTask} answers on each of the ${tasks.size} tasks, so breadth earns this, not volume."),
            Component("mocks", "Mock exams", r.wMocks, mockScore, recent.isNotEmpty(),
                if (recent.isNotEmpty()) pct(mockRaw) else "none sat",
                if (recent.isNotEmpty()) "Your last ${recent.size} sitting(s), the most recent counting most." +
                    (if (stale < 1) " Discounted: the last one was $staleDays days ago." else "")
                else "Scores zero until you sit one. A full sitting is the only evidence you can hold accuracy for ${exam.minutes} minutes."),
            Component("pace", "Pace", r.wPace, paceScore, paceReady,
                if (median != null) "${median.roundToInt()}s" else "—",
                if (!paceReady) "Needs ${r.paceMinSample} timed answers. You have $paceN."
                else "Median time per question. Full marks between ${r.paceBand[0].toInt()} and ${r.paceBand[1].toInt()} seconds; too fast scores as badly as too slow."),
            Component("habit", "Study habit", r.wHabit, (st.activeInWindow.toDouble() / r.habitTargetDays).coerceIn(0.0, 1.0),
                st.activeInWindow > 0, "${st.activeInWindow} / ${r.habitTargetDays}",
                "Days you studied in the last ${r.habitWindowDays}, against a target of ${r.habitTargetDays}."),
        )
        val total = comps.sumOf { it.points }
        val n = atts.count { it.source != SOURCE_LESSON }
        val gates = ArrayList<String>()
        if (n < r.gateItems) gates += "${r.gateItems - n} more answers needed"
        if (touched < r.gateTasks) gates += "${r.gateTasks - touched} more ECO tasks to touch"

        // The component with the most points still on the table decides the advice.
        val gap = comps.maxByOrNull { (1 - it.score) * it.weight }
        val (action, route) = when (gap?.id) {
            "coverage" -> "Answer questions on tasks you haven't covered, starting with ${short.take(3).joinToString(", ") { it.first.label }}." to "practice"
            "mocks" -> (if (recent.isEmpty()) "Sit your first full mock exam: it is worth ${r.wMocks} of the 100 points on its own." else "Sit another full mock exam.") to "mock"
            "pace" -> "Run a timed drill and aim for ${r.paceBand[0].toInt()}–${r.paceBand[1].toInt()} seconds a question." to "practice"
            "habit" -> "Do something small today: a few flashcards or ten questions keep the habit going." to "flashcards"
            else -> "Drill your weakest tasks and review every wrong answer in the mistake journal." to "practice"
        }
        return ReadinessReport(total.roundToInt(), comps, gates.isNotEmpty(), gates, action, route)
    }

    companion object {
        const val SOURCE_PRACTICE = "practice"
        const val SOURCE_EXAM = "exam"
        const val SOURCE_LESSON = "lesson"
        private const val FLOOR = 0.05
    }
}
