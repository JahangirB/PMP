package com.jahangirb.pmpprep

import com.jahangirb.pmpprep.data.Attempt
import com.jahangirb.pmpprep.data.Booking
import com.jahangirb.pmpprep.data.Domain
import com.jahangirb.pmpprep.data.Flashcard
import com.jahangirb.pmpprep.data.MistakeTag
import com.jahangirb.pmpprep.data.MockResult
import com.jahangirb.pmpprep.data.ProgressStore
import com.jahangirb.pmpprep.data.QType
import com.jahangirb.pmpprep.data.QuestionReview
import com.jahangirb.pmpprep.data.Readiness
import com.jahangirb.pmpprep.data.RootCause
import com.jahangirb.pmpprep.data.Srs
import com.jahangirb.pmpprep.data.WeeklyReport
import com.jahangirb.pmpprep.data.WeeklyReports
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.nio.file.Files
import java.util.Calendar

class StudyToolsTest {
    private val D = Srs.DAY_MS
    /** A Wednesday noon. */
    private val now = Calendar.getInstance().apply { set(2027, Calendar.MARCH, 10, 12, 0, 0); set(Calendar.MILLISECOND, 0) }.timeInMillis

    private fun a(qid: String, at: Long, right: Boolean, confidence: Int = 0, source: String = "practice") =
        Attempt(qid, "t1", Domain.PEOPLE, null, QType.SINGLE, if (right) 1.0 else 0.0, right, 60_000, at, source, confidence = confidence)

    /* ── L2: spaced review of missed questions ─────────────────────────── */

    @Test fun missedQuestionComesBackAfterOneDayThenThreeSevenTwentyOneThenGraduates() {
        val miss = a("q", now, false)
        val s0 = QuestionReview.state(listOf(miss))!!
        assertEquals(0, s0.step)
        assertEquals(Srs.startOfDay(now + D), s0.due)
        // Right the same day doesn't move it on.
        assertEquals(0, QuestionReview.state(listOf(miss, a("q", now + 3_600_000, true)))!!.step)
        var h = listOf(miss)
        var t = now
        for ((step, days) in listOf(1 to 3, 2 to 7, 3 to 21)) {
            t = QuestionReview.state(h)!!.due + 10 * 3_600_000L
            h = h + a("q", t, true)
            val s = QuestionReview.state(h)!!
            assertEquals(step, s.step)
            assertEquals(Srs.startOfDay(Srs.startOfDay(t) + days * D + D / 2), s.due)
        }
        t = QuestionReview.state(h)!!.due + 3_600_000L
        assertNull("graduates after the 21-day review", QuestionReview.state(h + a("q", t, true)))
    }

    @Test fun aWrongAnswerOrAGuessDoesNotAdvanceAReview() {
        val miss = a("q", now, false)
        val due = QuestionReview.state(listOf(miss))!!.due
        assertEquals(0, QuestionReview.state(listOf(miss, a("q", due + 1000, true, Attempt.GUESS)))!!.step)
        val again = QuestionReview.state(listOf(miss, a("q", due + 1000, true), a("q", due + 2 * D, false)))!!
        assertEquals(0, again.step)
        assertEquals(Srs.startOfDay(due + 2 * D + D), again.due)
        assertNull("never missed", QuestionReview.state(listOf(a("x", now, true))))
    }

    @Test fun dueListAndDailyAllowance() {
        val atts = (1..8).map { a("q$it", now - (2 + it) * D, false) } + a("fresh", now, false)
        val due = QuestionReview.due(atts, now)
        assertEquals(8, due.size)
        assertEquals("most overdue first", "q8", due.first().qid)
        assertEquals(5, QuestionReview.allowance(atts, now))
        val reviewed = atts + listOf(a("q1", now, true, source = QuestionReview.SOURCE), a("q2", now, false, source = QuestionReview.SOURCE))
        assertEquals(3, QuestionReview.allowance(reviewed, now))
    }

    /* ── L1: confidence ──────────────────────────────────────────────────── */

    @Test fun confidenceMarksLuckyGuessesAndSureMistakes() {
        assertTrue(a("q", now, true, Attempt.GUESS).luckyGuess)
        assertFalse(a("q", now, true, Attempt.THINK).luckyGuess)
        assertTrue(a("q", now, false, Attempt.SURE).sureWrong)
        assertFalse(a("q", now, false, Attempt.GUESS).sureWrong)
    }

    @Test fun confidenceStruckOptionsAndMyCardsSurviveABackup() {
        val dir = Files.createTempDirectory("pmp").toFile()
        val s = ProgressStore(dir)
        s.record(a("q1", now, false, Attempt.SURE).copy(eliminated = 2))
        s.addMyCard(Flashcard("my:q1", ProgressStore.MY_DECK, "t1", "front", "back", ""))
        s.setPlan(s.plan.copy(restDays = false))
        val back = ProgressStore(Files.createTempDirectory("pmp").toFile())
        back.restore(s.toJson().toString())
        assertEquals(Attempt.SURE, back.attempts.single().confidence)
        assertEquals(2, back.attempts.single().eliminated)
        assertEquals("front", back.myCards.single().front)
        assertEquals(ProgressStore.MY_DECK, back.myCards.single().deck)
        assertFalse(back.plan.restDays)
        back.removeMyCard("my:q1")
        assertTrue(back.myCards.isEmpty())
    }

    /* ── C3: rest days ───────────────────────────────────────────────────── */

    @Test fun oneMissedDayAWeekIsKeptBetweenStudyDays() {
        val today = Srs.startOfDay(now) // Wednesday
        fun day(n: Int) = Srs.startOfDay(today + n * D + D / 2)
        // Studied Mon, Tue of last week... a gap on last Thursday and on last Saturday (same week): only one is kept.
        val studied = setOf(-8, -7, -5, -3, -1, 0).map { day(it) }.toSet()
        val kept = Readiness.withRestDays(studied, today)
        assertTrue("Thursday kept", day(-6) in kept)
        assertFalse("Saturday not kept: that week's rest day is used", day(-4) in kept)
        // Monday of this week sits in a new week: kept.
        assertTrue(day(-2) in kept)
        // Two days in a row missed are never bridged.
        val gap = Readiness.withRestDays(setOf(day(-5), day(-2), day(0)), today)
        assertFalse(day(-4) in gap)
    }

    /* ── D1: ready to book ───────────────────────────────────────────────── */

    private fun week(i: Int, readiness: Int, fitness: Int?) =
        WeeklyReport(WeeklyReports.weekStart(now) - (5 - i) * 7 * D, now, readiness, 70, 10, 2, 300, 5, 100, 0, null, fitness,
            null, null, null, null, 0, null, null)

    private fun mock(pct: Double, n: Int = 180) = MockResult(now, "Full", n, (pct * n).toInt(), 0, emptyMap())

    @Test fun readyWhenAllThreeAreMet() {
        val b = Booking.check(80, 72, listOf(mock(0.7), mock(0.68), mock(0.9, n = 45)), 180, emptyList(), null)
        assertTrue(b.ready)
        assertNull(b.projection)
        assertEquals(0, b.mocksNeeded)
        val short = Booking.check(80, 72, listOf(mock(0.7), mock(0.9, n = 45)), 180, emptyList(), null)
        assertFalse("a short mock doesn't count", short.ready)
        assertEquals(1, short.mocksNeeded)
    }

    @Test fun projectsARoughDateFromTwoWeeksAndARangeFromFour() {
        val two = listOf(week(4, 60, 60), week(5, 65, 65))
        val p = Booking.check(65, 65, emptyList(), 180, two, null).projection!!
        assertTrue(p.rough)
        assertNull(p.earliest)
        // Readiness: 5 a week from 65 to 75 → 2 weeks after the last report.
        assertEquals(two.last().weekStart + 14 * D, p.latest)
        val four = listOf(week(2, 50, 52), week(3, 56, 55), week(4, 59, 62), week(5, 65, 64))
        val r = Booking.check(65, 64, emptyList(), 180, four, null).projection!!
        assertFalse(r.rough)
        assertNotNull(r.earliest); assertNotNull(r.latest)
        assertTrue(r.earliest!! <= r.latest!!)
    }

    @Test fun flagsStalledScoresAndADateAfterTheExam() {
        val flat = listOf(week(4, 60, 60), week(5, 58, 66))
        val p = Booking.check(58, 66, emptyList(), 180, flat, null).projection!!
        assertEquals(listOf("Readiness"), p.stalled)
        assertNull(p.latest)
        val slow = listOf(week(4, 60, 71), week(5, 61, 71))
        val late = Booking.check(61, 71, emptyList(), 180, slow, now + 7 * D).projection!!
        assertTrue(late.afterExam)
    }

    /* ── D4 and L1 in the weekly report ──────────────────────────────────── */

    @Test fun weeklyComparisonShowsConfidenceAndMistakeCauses() {
        val base = week(5, 60, null)
        val r = base.copy(sureWrong = 3, luckyGuesses = 2, causes = mapOf(RootCause.MISREAD_STEM to 4))
        val p = base.copy(sureWrong = 5, luckyGuesses = 2, causes = mapOf(RootCause.MISREAD_STEM to 6, RootCause.RUSHED to 1))
        val secs = WeeklyReports.compare(r, p).associateBy { it.title }
        assertEquals(1, secs["Confidence"]!!.rows.first { it.label == "Sure but wrong" }.direction)
        val causes = secs["Mistake causes"]!!.rows.associateBy { it.label }
        assertEquals(1, causes["Misread the question"]!!.direction)
        assertEquals(0, causes["Rushed"]!!.value)
        assertEquals(1, causes["Rushed"]!!.direction)
        // Round trip.
        assertEquals(r, WeeklyReport.fromJson(r.toJson()))
        // No confidence asked and nothing tagged: those sections are left out.
        assertFalse(WeeklyReports.compare(base, null).any { it.title == "Confidence" || it.title == "Mistake causes" })
    }

    @Test fun weeklyBuildCountsThisWeeksTags() {
        val tags = listOf(MistakeTag(RootCause.RUSHED, "", now), MistakeTag(RootCause.RUSHED, "", now - 30 * D))
        val atts = listOf(a("q", now, false, Attempt.SURE), a("r", now, true, Attempt.GUESS))
        val dir = Files.createTempDirectory("pmp").toFile()
        val w = WeeklyReports.build(now, com.jahangirb.pmpprep.data.ReadinessReport(50, emptyList(), false, emptyList(), "", ""), 1, 0, atts, emptyList(), emptyList(),
            com.jahangirb.pmpprep.data.ExamFitness.report(com.jahangirb.pmpprep.data.FitnessInput(atts, emptyList(), emptyList(), emptyList(), setOf(2, 3, 4, 5, 6), 10, now)), tags)
        assertEquals(mapOf(RootCause.RUSHED to 1), w.causes)
        assertEquals(1, w.sureWrong)
        assertEquals(1, w.luckyGuesses)
        dir.delete()
    }
}
