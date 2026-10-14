package com.jahangirb.pmpprep

import com.jahangirb.pmpprep.data.HourRule
import com.jahangirb.pmpprep.data.PlanEngine
import com.jahangirb.pmpprep.data.PlannedReminder
import com.jahangirb.pmpprep.data.ReminderBrain
import com.jahangirb.pmpprep.data.ReminderKind
import com.jahangirb.pmpprep.data.ReminderSettings
import com.jahangirb.pmpprep.data.ReminderSnapshot
import com.jahangirb.pmpprep.data.ReminderState
import com.jahangirb.pmpprep.data.ReminderText
import com.jahangirb.pmpprep.data.StudyWindow
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Calendar
import kotlin.random.Random

class RemindersTest {
    private val H = ReminderBrain.HOUR
    private val M = ReminderBrain.MINUTE
    /** A Monday, 06:00. */
    private val monday = Calendar.getInstance().apply { set(2026, Calendar.MARCH, 2, 6, 0, 0); set(Calendar.MILLISECOND, 0) }.timeInMillis
    private val allDays = (1..7).toSet()

    private fun snapshot(day: Long, today: Int = 0, target: Int = 60, streak: Int = 3, studyDays: Set<Int> = allDays, lastStudy: Long = day - H) =
        ReminderSnapshot(PlanEngine.startOfDay(day), "Manage conflict", 35, "Lesson, 15 questions and a quick reflect", 12, streak, studyDays,
            today, target, lastStudy, false, 68, 9, 400, "Process domain")

    private fun state(usage: DoubleArray? = null) = ReminderState().apply {
        settings = ReminderSettings(wake = 7, sleep = 23, asked = true)
        phoneUsage = usage
        snapshot = snapshot(monday)
    }

    @Test fun phoneUsageGivesTheFirstDayAStartingGuess() {
        val usage = DoubleArray(24) { h -> if (h in 19..21) 0.9 else 0.15 }
        val st = state(usage)
        val best = ReminderBrain.scores(st, monday).filter { it.hour in ReminderBrain.wakingHours(st.settings) }.maxBy { it.mean }
        assertTrue("evening should start on top, got ${best.hour}", best.hour in 19..21)
    }

    @Test fun learnsTheHourThatWorksAndStopsSpreadingReminders() = (1..5).forEach { seed -> learns(seed) }

    private fun learns(seed: Int) {
        // A learner who only answers reminders between 19:00 and 20:59, phone usage unknown.
        val st = state()
        val rnd = Random(seed)
        val learner = Random(seed * 31)
        val late = HashMap<Int, Int>()
        for (d in 0 until 35) {
            val dayStart = monday + d * 24 * H
            st.snapshot = snapshot(dayStart, streak = 0)
            val plan = ReminderBrain.planToday(st, dayStart, rnd).toList()
            assertTrue("never more than 3 a day", plan.count { it.kind != ReminderKind.SUMMARY } <= 3)
            plan.filter { it.kind == ReminderKind.STUDY }.forEach { p ->
                ReminderBrain.noteSent(st, p, p.at)
                val works = p.hour in 19..20 && learner.nextDouble() < 0.85
                if (works) {
                    ReminderBrain.noteOpened(st, p.at + 10 * M, null)
                    val i = st.logs.indexOfLast { it.id == p.at }
                    st.logs[i] = st.logs[i].copy(studiedMinutes = 30)
                }
                if (d >= 28) late.merge(p.hour, 1, Int::plus)
            }
        }
        val inWindow = late.filterKeys { it in 19..20 }.values.sum()
        val total = late.values.sum()
        assertTrue("seed $seed: by week 5 most reminders land at 19–20 (got $late)", inWindow * 10 >= total * 6)
        assertTrue("after learning the cap drops to 2 a day", total <= 7 * 2)
        val best = ReminderBrain.scores(st, monday + 35 * 24 * H).filter { it.hour in ReminderBrain.wakingHours(st.settings) }.maxBy { it.mean }
        assertTrue("19–20 is learned as the best hour (got ${best.hour})", best.hour in 19..20)
    }

    @Test fun followsARoutineThatChanges() = (1..3).forEach { seed ->
        // Evenings (19–20) work for five weeks, then only mornings (8–9) do.
        val st = state()
        val rnd = Random(seed)
        val learner = Random(seed * 17)
        var day = 0
        fun live(days: Int, good: IntRange): Map<Int, Int> {
            val seen = HashMap<Int, Int>()
            repeat(days) {
                val dayStart = monday + day++ * 24 * H
                st.snapshot = snapshot(dayStart, streak = 0)
                ReminderBrain.planToday(st, dayStart, rnd).filter { it.kind == ReminderKind.STUDY }.forEach { p ->
                    ReminderBrain.noteSent(st, p, p.at)
                    if (p.hour in good && learner.nextDouble() < 0.85) {
                        ReminderBrain.noteOpened(st, p.at + 10 * M, p.at)
                        val i = st.logs.indexOfLast { it.id == p.at }
                        st.logs[i] = st.logs[i].copy(studiedMinutes = 30)
                    }
                    seen.merge(p.hour, 1, Int::plus)
                }
            }
            return seen
        }
        live(35, 19..20)
        live(42, 8..9)
        val after = live(7, 8..9)
        val morning = after.filterKeys { it in 8..9 }.values.sum()
        assertTrue("seed $seed: six weeks after the change, most reminders are in the morning (got $after)", morning * 10 >= after.values.sum() * 6)
    }

    @Test fun fourIgnoredInARowBacksOffToOneADay() {
        val st = state()
        repeat(4) { i -> ReminderBrain.noteSent(st, PlannedReminder(monday + i * H, 9 + i, ReminderKind.STUDY), monday + i * H) }
        val later = monday + 2 * 24 * H
        assertEquals(1, ReminderBrain.cap(st, later))
        assertEquals(3, ReminderBrain.cap(st, monday + 4 * H + 5 * 24 * H))
    }

    @Test fun restDaysGetNoStudyRemindersButSundayGetsTheSummary() {
        val sunday = monday + 6 * 24 * H
        val st = state().apply { snapshot = snapshot(sunday, studyDays = setOf(Calendar.MONDAY)) }
        val plan = ReminderBrain.planToday(st, sunday, Random(1))
        assertEquals(listOf(ReminderKind.SUMMARY), plan.map { it.kind })
        assertEquals(18, plan.single().hour)
    }

    @Test fun firingChecksThePlanAndTheStreak() {
        val st = state()
        val study = PlannedReminder(monday + 12 * H, 18, ReminderKind.STUDY)
        val streak = PlannedReminder(monday + 15 * H, 21, ReminderKind.STREAK)
        assertTrue(ReminderBrain.shouldSend(st, study, study.at))
        st.snapshot = snapshot(monday, today = 60, target = 60)
        assertFalse("plan done today", ReminderBrain.shouldSend(st, study, study.at))
        assertFalse("studied today, no streak-saver", ReminderBrain.shouldSend(st, streak, streak.at))
        st.snapshot = snapshot(monday, today = 0)
        ReminderBrain.noteSent(st, study, study.at)
        ReminderBrain.noteNotToday(st, study.at, study.at + M)
        assertFalse("not today", ReminderBrain.shouldSend(st, streak, streak.at))
    }

    @Test fun pinnedAndBlockedHoursAreRespectedAndRemindersAreSpacedOut() {
        val st = state().apply { settings = settings.copy(rules = mapOf(8 to HourRule.PINNED) + (9..16).associateWith { HourRule.BLOCKED }) }
        val plan = ReminderBrain.planToday(st, monday, Random(3)).filter { it.kind == ReminderKind.STUDY }
        assertTrue(plan.any { it.hour == 8 })
        assertTrue(plan.none { it.hour in 9..16 })
        val hours = plan.map { it.hour }.sorted()
        hours.zipWithNext().forEach { (a, b) -> assertTrue("at least 2 h apart: $hours", b - a >= 2) }
    }

    @Test fun replanningLaterInTheDayKeepsToTheCap() {
        val st = state()
        val first = ReminderBrain.planToday(st, monday, Random(5)).filter { it.kind == ReminderKind.STUDY }
        first.take(2).forEach { ReminderBrain.noteSent(st, it, it.at) }
        val again = ReminderBrain.replan(st, first[1].at + 1, Random(6)).filter { it.kind == ReminderKind.STUDY }
        assertTrue("2 sent + ${again.size} planned must stay within 3", 2 + again.size <= 3)
    }

    @Test fun lateOpeningMovesTheReminderEarlier() {
        val st = state()
        repeat(5) { d ->
            val at = monday + d * 24 * H + 12 * H
            ReminderBrain.noteSent(st, PlannedReminder(at, 18, ReminderKind.STUDY), at)
            ReminderBrain.noteOpened(st, at + 35 * M, at)
        }
        assertEquals(25, ReminderBrain.offsetMinutes(st, 18))
    }

    @Test fun answeringTheSetupQuestionShiftsTheGuess() {
        val st = state().apply { settings = settings.copy(windows = setOf(StudyWindow.MORNING)) }
        assertTrue(ReminderBrain.prior(st, 9) > ReminderBrain.prior(st, 19))
    }

    @Test fun foregroundMinutesAreSplitAcrossHours() {
        val st = state()
        ReminderBrain.noteForeground(st, monday + 13 * H + 50 * M, monday + 14 * H + 20 * M) // 19:50 to 20:20
        assertEquals(10.0, st.appMinutes[19], 1e-6)
        assertEquals(20.0, st.appMinutes[20], 1e-6)
    }

    @Test fun stateSurvivesSaving() {
        val st = state(DoubleArray(24) { it / 24.0 }).apply {
            settings = settings.copy(windows = setOf(StudyWindow.EVENING), rules = mapOf(9 to HourRule.BLOCKED), usePhoneUsage = true)
            ReminderBrain.planToday(this, monday, Random(2))
            ReminderBrain.noteSent(this, plan.first(), plan.first().at)
            ReminderBrain.noteOpened(this, plan.first().at + 5 * M, plan.first().at)
            appMinutes[20] = 42.0
        }
        val back = ReminderState.fromJson(org.json.JSONObject(st.toJson().toString()))
        assertEquals(st.settings, back.settings)
        assertEquals(st.logs, back.logs)
        assertEquals(st.plan, back.plan)
        assertEquals(st.snapshot, back.snapshot)
        assertEquals(42.0, back.appMinutes[20], 0.0)
        assertEquals(st.phoneUsage!!.toList(), back.phoneUsage!!.toList())
    }

    @Test fun textsNameTheSessionAndWelcomeYouBack() {
        val st = state()
        assertEquals("Next up: Manage conflict · 35 min", ReminderText.compose(ReminderKind.STUDY, st, monday + 12 * H, false).title)
        st.snapshot = snapshot(monday, lastStudy = monday - 4 * 24 * H)
        assertTrue(ReminderText.compose(ReminderKind.STUDY, st, monday + 12 * H, false).title.startsWith("Welcome back"))
        st.lastSummaryReadiness = 61; st.lastSummaryMastered = 6
        val week = ReminderText.compose(ReminderKind.SUMMARY, st, monday, false)
        assertEquals("Your week: 6 h 40 m studied, readiness 61 → 68", week.title)
        assertEquals("3 tasks mastered. Next week: Process domain.", week.body)
    }
}
