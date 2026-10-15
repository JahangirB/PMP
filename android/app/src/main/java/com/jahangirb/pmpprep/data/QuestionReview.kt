package com.jahangirb.pmpprep.data

/**
 * Spaced review for questions you got wrong. A miss comes back after 1 day; each later right answer
 * (on or after the day it was due, and not a guess) moves it to 3, 7 and 21 days, then it graduates.
 * A wrong answer starts it again. Worked out from the answer history, so nothing extra is stored.
 */
object QuestionReview {
    val STEP_DAYS = listOf(1, 3, 7, 21)
    /** At most this many review questions a day, so warm-ups stay short. */
    const val PER_DAY = 5
    const val SOURCE = "review"

    /** A missed question still on its way: due from [due], at step [step] (0 = first review). */
    data class Item(val qid: String, val due: Long, val step: Int)

    /** Where one question stands, from its answers oldest first; null if never missed or graduated. */
    fun state(history: List<Attempt>): Item? {
        val h = history.filter { it.source != Readiness.SOURCE_LESSON }
        val miss = h.indexOfLast { !it.correct }
        if (miss < 0) return null
        var step = 0
        var due = dueAfter(h[miss].at, 0)
        for (a in h.drop(miss + 1)) {
            // Right the same day proves little; only an answer on or after the due day moves it on.
            if (a.correct && !a.luckyGuess && !a.hinted && Srs.startOfDay(a.at) >= due) {
                step++
                if (step >= STEP_DAYS.size) return null
                due = dueAfter(a.at, step)
            }
        }
        return Item(h[miss].qid, due, step)
    }

    private fun dueAfter(at: Long, step: Int): Long = Srs.startOfDay(Srs.startOfDay(at) + STEP_DAYS[step] * Srs.DAY_MS + Srs.DAY_MS / 2)

    /** Every missed question due by [now], most overdue first. */
    fun due(attempts: List<Attempt>, now: Long): List<Item> =
        attempts.groupBy { it.qid }.values.mapNotNull { state(it.sortedBy { a -> a.at }) }
            .filter { it.due <= now }.sortedBy { it.due }

    /** How many more may be reviewed today. */
    fun allowance(attempts: List<Attempt>, now: Long): Int {
        val today = Srs.startOfDay(now)
        val done = attempts.filter { it.source == SOURCE && it.at >= today }.map { it.qid }.toSet().size
        return (PER_DAY - done).coerceAtLeast(0)
    }
}
