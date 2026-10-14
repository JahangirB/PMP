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
) {
    fun daysLeft(now: Long): Int? = examDate?.let { max(0, ((PlanEngine.startOfDay(it) - PlanEngine.startOfDay(now)) / PlanEngine.DAY).toInt()) }

    /** Share of today's plan done, 0..1; zero once the day has turned. */
    fun today(now: Long): Float =
        if (PlanEngine.startOfDay(now) != day || targetMinutes <= 0) 0f
        else (todayMinutes.toFloat() / targetMinutes).coerceIn(0f, 1f)

    fun syllabus(): Float = if (lessonsTotal == 0) 0f else (lessonsDone.toFloat() / lessonsTotal).coerceIn(0f, 1f)

    companion object {
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
