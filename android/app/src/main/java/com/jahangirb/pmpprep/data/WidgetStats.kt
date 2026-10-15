package com.jahangirb.pmpprep.data

import kotlin.math.max
import kotlin.math.roundToInt

/**
 * What the home-screen widget shows, saved by the app whenever progress changes.
 * Days left and today's progress are worked out when the widget draws, so they
 * roll over at midnight without the app running.
 */
data class WidgetStats(
    val examDate: Long?,
    /** The study day [todayMinutes] belongs to (start of day, local time). */
    val day: Long,
    val todayMinutes: Int,
    val targetMinutes: Int,
    val readiness: Int,
    val lessonsDone: Int,
    val lessonsTotal: Int,
    /** Exam fitness 0–100, or −1 until there's enough evidence. */
    val fitness: Int = -1,
    /** When your pace says you'll be ready to sit it (see [Booking]), and how sure that is: one of the READY_ values. */
    val readyBy: Long = 0L,
    val readyState: Int = READY_UNKNOWN,
) {
    fun daysLeft(now: Long): Int? = examDate?.let { max(0, ((PlanEngine.startOfDay(it) - PlanEngine.startOfDay(now)) / PlanEngine.DAY).toInt()) }

    /** Share of today's plan done, 0..1; zero once the day has turned. */
    fun today(now: Long): Float =
        if (PlanEngine.startOfDay(now) != day || targetMinutes <= 0) 0f
        else (todayMinutes.toFloat() / targetMinutes).coerceIn(0f, 1f)

    /**
     * How the countdown looks: plenty of time is green, turning red as the exam gets close.
     * Two months or more is fully green; the last week is fully red.
     */
    fun daysLevel(now: Long): Float? = daysLeft(now)?.let { ((it - 7) / 53f).coerceIn(0f, 1f) }

    /** True when the projected ready date is on or before the exam (or you're ready now), false when after; null when unknown. */
    fun onTrack(): Boolean? = when (readyState) {
        READY_NOW -> true
        READY_STALLED -> false
        READY_PROJECTED -> examDate?.let { PlanEngine.startOfDay(readyBy) <= PlanEngine.startOfDay(it) }
        else -> null
    }

    fun syllabus(): Float = if (lessonsTotal == 0) 0f else (lessonsDone.toFloat() / lessonsTotal).coerceIn(0f, 1f)

    companion object {
        const val READY_UNKNOWN = 0
        const val READY_NOW = 1
        const val READY_PROJECTED = 2
        /** A score that has to rise isn't rising, so there's no date. */
        const val READY_STALLED = 3

        private const val RED = 0xFFD64545L
        private const val AMBER = 0xFFE3A21AL
        private const val GREEN = 0xFF2E9E5BL

        /** Red at 0, amber at ½, green at 1, blended in between. ARGB. */
        fun heat(level: Float): Long {
            val t = level.coerceIn(0f, 1f)
            return if (t < 0.5f) mix(RED, AMBER, t * 2) else mix(AMBER, GREEN, (t - 0.5f) * 2)
        }

        private fun mix(a: Long, b: Long, t: Float): Long {
            fun ch(c: Long, shift: Int) = ((c shr shift) and 0xFF).toInt()
            var out = 0xFF000000L
            for (shift in intArrayOf(16, 8, 0)) out = out or ((ch(a, shift) + (ch(b, shift) - ch(a, shift)) * t).roundToInt().toLong() shl shift)
            return out
        }

        /**
         * Minutes studied today: guided sessions, or the time spent answering questions,
         * whichever is more (practice inside a session counts toward both, so they aren't added).
         */
        fun minutesToday(results: List<SessionResult>, attempts: List<Attempt>, now: Long): Int {
            val today = PlanEngine.startOfDay(now)
            val sessions = results.filter { PlanEngine.startOfDay(it.at) == today }.sumOf { it.minutes }
            val answering = (attempts.filter { PlanEngine.startOfDay(it.at) == today }.sumOf { it.ms } / 60_000.0).roundToInt()
            return max(sessions, answering)
        }
    }
}
