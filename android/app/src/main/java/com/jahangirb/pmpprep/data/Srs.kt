package com.jahangirb.pmpprep.data

import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

enum class Grade(val title: String) { AGAIN("Again"), HARD("Hard"), GOOD("Good"), EASY("Easy") }

/** SM-2 state for one flashcard. A card with reps == 0 and due == 0 has never been studied. */
data class CardState(
    val ease: Double,
    val interval: Int = 0,
    val reps: Int = 0,
    val lapses: Int = 0,
    val due: Long = 0L,
    val last: Long = 0L,
    /** When the card was first studied; drives the new-cards-per-day cap. */
    val first: Long = 0L,
) {
    val isNew: Boolean get() = last == 0L
}

/** SM-2 exactly as the web app runs it (§10.05.1), with every number taken from its §01.4 settings. */
class Srs(private val c: SrsConfig) {

    fun fresh() = CardState(ease = c.easeStart)

    fun isLeech(s: CardState) = s.lapses >= c.leechThreshold

    fun review(s: CardState, g: Grade, now: Long): CardState {
        val failed = g == Grade.AGAIN
        // Again and Hard lower the ease, Good leaves it, Easy raises it.
        var e = s.ease
        when (g) {
            Grade.AGAIN -> e -= c.easeAgain
            Grade.HARD -> e -= c.easeHard
            Grade.EASY -> e += c.easeEasy
            Grade.GOOD -> {}
        }
        val ease = max(c.easeMin, Math.round(e * 100) / 100.0)
        // Two fixed steps, then geometric growth by the ease.
        val interval = when {
            failed -> c.lapseInterval
            s.reps == 0 -> if (g == Grade.EASY) c.intervalEasy else c.interval1
            s.reps == 1 -> if (g == Grade.EASY) (c.interval2 * c.easyBonus).roundToInt() else c.interval2
            else -> {
                val base = max(1, s.interval).toDouble()
                val days = when (g) {
                    Grade.HARD -> base * c.hardFactor
                    Grade.EASY -> base * ease * c.easyBonus
                    else -> base * ease
                }
                min(c.maxInterval, max(s.interval + 1, days.roundToInt()))
            }
        }
        return CardState(
            ease = ease,
            interval = interval,
            reps = if (failed) 0 else s.reps + 1,
            lapses = if (failed) s.lapses + 1 else s.lapses,
            due = startOfDay(now) + interval * DAY_MS,
            last = now,
            first = if (s.first == 0L) now else s.first,
        )
    }

    /** The interval each grade would give, for the labels on the buttons. */
    fun preview(s: CardState, now: Long): Map<Grade, Int> = Grade.entries.associateWith { review(s, it, now).interval }

    companion object {
        const val DAY_MS = 24L * 60 * 60 * 1000

        fun startOfDay(t: Long): Long {
            val c = java.util.Calendar.getInstance()
            c.timeInMillis = t
            c.set(java.util.Calendar.HOUR_OF_DAY, 0)
            c.set(java.util.Calendar.MINUTE, 0)
            c.set(java.util.Calendar.SECOND, 0)
            c.set(java.util.Calendar.MILLISECOND, 0)
            return c.timeInMillis
        }
    }
}
