package com.jahangirb.pmpprep.data

import kotlin.math.max
import kotlin.math.pow
import kotlin.random.Random

/**
 * Chooses the questions for a guided-path practice step: unseen first, then the ones you got
 * wrong last time, then the rest. Within each group, if one exam approach (predictive, agile,
 * hybrid) is clearly your weakest, its questions take every other place, so drills lean
 * toward it without turning into a one-approach set.
 */
object QuestionPicker {
    /** Answers with an approach needed before judging, overall and per approach. */
    const val MIN_ANSWERS = 30
    const val MIN_PER_APPROACH = 10
    /** How far below your overall accuracy an approach must be to count as clearly weakest. */
    const val GAP = 0.10

    fun weakApproach(attempts: List<Attempt>): Approach? {
        val atts = attempts.filter { it.source != Readiness.SOURCE_LESSON && it.approach != null }
        if (atts.size < MIN_ANSWERS) return null
        val overall = atts.sumOf { it.credit } / atts.size
        return atts.groupBy { it.approach!! }
            .filterValues { it.size >= MIN_PER_APPROACH }
            .mapValues { (_, l) -> l.sumOf { it.credit } / l.size }
            .minByOrNull { it.value }
            ?.takeIf { it.value <= overall - GAP }?.key
    }

    /** [lastCorrect] is how a question went the last time it was answered, or null if never. */
    fun order(qs: List<Question>, lastCorrect: (String) -> Boolean?, weak: Approach?, count: Int, rnd: Random = Random.Default): List<Question> {
        val unseen = qs.filter { lastCorrect(it.id) == null }.shuffled(rnd)
        val missed = qs.filter { lastCorrect(it.id) == false }.shuffled(rnd)
        val rest = qs.filter { lastCorrect(it.id) == true }.shuffled(rnd)
        return (tilt(unseen, weak) + tilt(missed, weak) + tilt(rest, weak)).take(count)
    }

    private fun tilt(list: List<Question>, weak: Approach?): List<Question> {
        if (weak == null) return list
        val a = list.filter { it.approach == weak }.toMutableList()
        val b = list.filter { it.approach != weak }.toMutableList()
        val out = ArrayList<Question>(list.size)
        while (a.isNotEmpty() || b.isNotEmpty()) {
            if (a.isNotEmpty()) out += a.removeAt(0)
            if (b.isNotEmpty()) out += b.removeAt(0)
        }
        return out
    }
    /* ── the adaptive drill (index.html §10.02.2) ────────────────────────── */

    /**
     * How strongly a question is pulled into an adaptive drill: (1 − acc)^exponent + floor, where
     * acc is its ECO task's accuracy shrunk toward the prior, so a task with two answers is not
     * settled evidence. Never seen ×[AdaptiveConfig.unseenBoost]; right last time ×[AdaptiveConfig.rightDamp].
     * [task] is the task's (answers, credit); [lastCorrect] how the question went last time, or null.
     */
    fun adaptiveWeight(task: Pair<Int, Double>, lastCorrect: Boolean?, c: AdaptiveConfig): Double {
        val acc = (task.second + c.prior * c.priorN) / (task.first + c.priorN)
        var w = (1 - acc).coerceAtLeast(0.0).pow(c.exponent) + c.floor
        when (lastCorrect) {
            null -> w *= c.unseenBoost
            true -> w *= c.rightDamp
            false -> {}
        }
        return w
    }

    /**
     * Answers and credit per ECO task, as the adaptive weighting reads them. Lesson checks and
     * hinted answers stay out, as they do from accuracy: neither is evidence of what you know unaided.
     */
    fun taskTotals(attempts: List<Attempt>): Map<String, Pair<Int, Double>> {
        val m = HashMap<String, Pair<Int, Double>>()
        for (a in attempts) {
            if (a.source == Readiness.SOURCE_LESSON || a.hinted || a.ecoTask.isEmpty()) continue
            val e = m[a.ecoTask] ?: (0 to 0.0)
            m[a.ecoTask] = (e.first + 1) to (e.second + a.credit)
        }
        return m
    }

    /** An adaptive drill of [count] from [qs]: a weighted draw without replacement. A pool no bigger than [count] is just shuffled. */
    fun adaptive(
        qs: List<Question>, attempts: List<Attempt>, lastCorrect: (String) -> Boolean?, count: Int,
        c: AdaptiveConfig = AdaptiveConfig(), rnd: Random = Random.Default,
    ): List<Question> {
        if (qs.size <= count) return qs.shuffled(rnd)
        val tasks = taskTotals(attempts)
        return weightedSample(qs, qs.map { adaptiveWeight(tasks[it.ecoTask] ?: (0 to 0.0), lastCorrect(it.id), c) }, count, rnd)
    }

    /**
     * Weighted sample without replacement (index.html §09.4 sample()). Weights are relative. A zero
     * weight is never drawn while a positive one remains; an all-zero pool falls back to a flat draw.
     */
    fun <T> weightedSample(items: List<T>, weights: List<Double>, k: Int, rnd: Random = Random.Default): List<T> {
        val pool = items.indices.map { items[it] to max(0.0, weights.getOrElse(it) { 0.0 }) }.toMutableList()
        val out = ArrayList<T>()
        val want = minOf(k, pool.size)
        while (out.size < want && pool.isNotEmpty()) {
            val total = pool.sumOf { it.second }
            var hit: Int
            if (total <= 0) hit = (rnd.nextDouble() * pool.size).toInt().coerceIn(0, pool.size - 1)
            else {
                val t = rnd.nextDouble() * total
                var acc = 0.0
                hit = 0
                while (hit < pool.size) { acc += pool[hit].second; if (acc >= t) break; hit++ }
                if (hit >= pool.size) hit = pool.size - 1
            }
            out += pool.removeAt(hit).first
        }
        return out
    }
}
