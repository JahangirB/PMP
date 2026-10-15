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

    @Test fun heatRunsRedThroughAmberToGreen() {
        assertEquals(0xFFD64545L, WidgetStats.heat(0f))
        assertEquals(0xFFE3A21AL, WidgetStats.heat(0.5f))
        assertEquals(0xFF2E9E5BL, WidgetStats.heat(1f))
        assertEquals("out of range is clamped", WidgetStats.heat(1f), WidgetStats.heat(3f))
        // Halfway from red to amber: each channel halfway.
        val q = WidgetStats.heat(0.25f)
        assertEquals((0xD6 + 0xE3 + 1) / 2, ((q shr 16) and 0xFF).toInt())
    }

    @Test fun countdownIsGreenFarOffAndRedInTheLastWeek() {
        assertEquals(1f, stats(examDate = noon + 90 * day).daysLevel(noon)!!, 0f)
        assertEquals(0f, stats(examDate = noon + 5 * day).daysLevel(noon)!!, 0f)
        assertEquals(0.5f, stats(examDate = noon + 33 * day).daysLevel(noon)!!, 0.02f)
        assertNull(stats(examDate = null).daysLevel(noon))
    }

    @Test fun readyByIsOnTrackWhenItIsNotAfterTheExam() {
        val exam = noon + 42 * day
        fun ready(state: Int, at: Long = 0L, examDate: Long? = exam) =
            WidgetStats(examDate, PlanEngine.startOfDay(noon), 0, 60, 68, 18, 34, readyBy = at, readyState = state).onTrack()
        assertEquals(true, ready(WidgetStats.READY_PROJECTED, noon + 30 * day))
        assertEquals(true, ready(WidgetStats.READY_PROJECTED, exam))
        assertEquals(false, ready(WidgetStats.READY_PROJECTED, exam + day))
        assertEquals(true, ready(WidgetStats.READY_NOW))
        assertEquals(false, ready(WidgetStats.READY_STALLED))
        assertNull(ready(WidgetStats.READY_UNKNOWN))
        assertNull("no exam date to compare with", ready(WidgetStats.READY_PROJECTED, noon, examDate = null))
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
