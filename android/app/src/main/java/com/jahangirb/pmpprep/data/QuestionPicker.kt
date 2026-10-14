package com.jahangirb.pmpprep.data

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
}
