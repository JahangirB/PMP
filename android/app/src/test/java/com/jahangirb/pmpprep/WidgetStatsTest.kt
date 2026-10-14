package com.jahangirb.pmpprep

import com.jahangirb.pmpprep.data.Attempt
import com.jahangirb.pmpprep.data.Domain
import com.jahangirb.pmpprep.data.PlanEngine
import com.jahangirb.pmpprep.data.QType
import com.jahangirb.pmpprep.data.SessionKind
import com.jahangirb.pmpprep.data.SessionResult
import com.jahangirb.pmpprep.data.WidgetStats
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.util.Calendar

class WidgetStatsTest {
    private val noon = Calendar.getInstance().apply { set(2026, Calendar.MARCH, 10, 12, 0, 0); set(Calendar.MILLISECOND, 0) }.timeInMillis
    private val day = PlanEngine.DAY

    private fun stats(examDate: Long? = noon + 42 * day, minutes: Int = 35) =
        WidgetStats(examDate, PlanEngine.startOfDay(noon), minutes, 60, 68, 18, 34)

    @Test fun countsDaysAndRollsOverAtMidnight() {
        val s = stats()
        assertEquals(42, s.daysLeft(noon))
        assertEquals(41, s.daysLeft(PlanEngine.startOfDay(noon) + day + 60_000))
        assertEquals(0, s.daysLeft(noon + 50 * day))
        assertNull(stats(examDate = null).daysLeft(noon))
    }

    @Test fun todayIsAShareOfThePlanAndResetsTomorrow() {
        assertEquals(35f / 60, stats().today(noon), 1e-4f)
        assertEquals(1f, stats(minutes = 90).today(noon), 0f)
        assertEquals(0f, stats().today(noon + day), 0f)
    }

    @Test fun syllabusIsLessonsDone() {
        assertEquals(18f / 34, stats().syllabus(), 1e-4f)
    }

    @Test fun minutesTodayTakesSessionsOrAnsweringWithoutAddingThem() {
        val results = listOf(
            SessionResult("build:P1", SessionKind.BUILD, "P1", Domain.PEOPLE, 0.8, 25, noon - 3600_000),
            SessionResult("build:P2", SessionKind.BUILD, "P2", Domain.PEOPLE, 0.8, 30, noon - day),
        )
        fun attempt(at: Long) = Attempt("q$at", "P1", Domain.PEOPLE, null, QType.SINGLE, 1.0, true, 120_000, at, "practice")
        // 25 session minutes today; 10 × 2 minutes answering = 20, all inside the session.
        assertEquals(25, WidgetStats.minutesToday(results, (1..10).map { attempt(noon - 3600_000 + it) }, noon))
        // 20 questions × 2 minutes outside any session beats 25 session minutes.
        assertEquals(40, WidgetStats.minutesToday(results, (1..20).map { attempt(noon - it) }, noon))
    }
}
