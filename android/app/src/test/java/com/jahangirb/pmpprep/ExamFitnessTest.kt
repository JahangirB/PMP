package com.jahangirb.pmpprep

import com.jahangirb.pmpprep.data.Attempt
import com.jahangirb.pmpprep.data.CheckIn
import com.jahangirb.pmpprep.data.Domain
import com.jahangirb.pmpprep.data.ExamFitness
import com.jahangirb.pmpprep.data.FitnessInput
import com.jahangirb.pmpprep.data.ProgressStore
import com.jahangirb.pmpprep.data.QType
import com.jahangirb.pmpprep.data.Readiness
import com.jahangirb.pmpprep.data.SittingEvent
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

class ExamFitnessTest {
    private val D = ExamFitness.DAY
    /** A Wednesday noon. */
    private val now = Calendar.getInstance().apply { set(2027, Calendar.MARCH, 10, 12, 0, 0); set(Calendar.MILLISECOND, 0) }.timeInMillis

    private fun a(at: Long, right: Boolean, ms: Long = 70_000, source: String = "practice", firstTap: Long = 0, idle: Long = 0,
                  words: Int = 150, timed: Boolean = false, rw: Int = 0, wr: Int = 0) =
        Attempt("q$at", "t1", Domain.PEOPLE, null, QType.SINGLE, if (right) 1.0 else 0.0, right, ms, at, source, firstTap, idle, rw, wr, words, timed)

    /** n answers one after another from [start], [acc] right, each [ms] long. */
    private fun run(start: Long, n: Int, acc: (Int) -> Boolean, ms: (Int) -> Long = { 70_000 }, source: String = "practice", timed: Boolean = false) =
        (0 until n).map { i -> a(start + i * 75_000L, acc(i), ms(i), source, timed = timed) }

    private fun input(atts: List<Attempt>, sittings: List<SittingEvent> = emptyList(), checkIns: List<CheckIn> = emptyList()) =
        FitnessInput(atts, sittings, checkIns, emptyList(), setOf(2, 3, 4, 5, 6), 10, now)

    @Test fun sittingsSplitOnLongGapsAndAroundMocks() {
        val atts = run(now - 5 * D, 10, { true }) + run(now - 5 * D + 3_600_000, 5, { true }) + run(now - 5 * D + 3_600_000 + 6 * 75_000, 4, { true }, source = Readiness.SOURCE_EXAM)
        assertEquals(listOf(10, 5, 4), ExamFitness.sittings(atts).map { it.size })
    }

    @Test fun staminaSeesTheFadeAndDriftInLongSittings() {
        // Two 180-question sittings: last quarter 20 points worse and 50% slower.
        val sit = { start: Long -> run(start, 180, { i -> if (i < 135) i % 10 < 8 else i % 10 < 6 }, { i -> if (i < 135) 60_000L + (i % 7) * 1000 else 90_000L + (i % 7) * 1000 }) }
        val atts = sit(now - 3 * D) + sit(now - D)
        val r = ExamFitness.report(input(atts))
        assertEquals(1.0, r.signal("longest").score!!, 1e-9)
        assertTrue("fade of 20 points scores zero: ${r.signal("fade").score}", r.signal("fade").score!! < 0.05)
        assertTrue("50% slower scores low: ${r.signal("drift").score}", r.signal("drift").score!! < 0.4)
        assertNotNull(r.part("stamina").score)
        assertNotNull(r.finding)
    }

    @Test fun staminaWaitsForTwoLongSittings() {
        val r = ExamFitness.report(input(run(now - D, 50, { true })))
        assertNull(r.part("stamina").score)
        assertEquals(45, r.ladderRung)
        assertEquals(70, r.ladderNext)
    }

    @Test fun aSittingThatFadesDoesNotClimbTheLadder() {
        val faded = run(now - D, 100, { i -> i < 75 })
        assertEquals(45, ExamFitness.report(input(run(now - 2 * D, 50, { true }) + faded)).ladderRung)
    }

    @Test fun readingPaceIsWordsPerMinuteBeforeTheFirstTap() {
        fun withTap(tapMs: Long) = (0 until 40).map { i -> a(now - D + i * 80_000L, true, firstTap = tapMs) }
        val fast = ExamFitness.report(input(withTap(45_000)))
        assertEquals(200.0, fast.wpm!!, 0.5)
        assertEquals(1.0, fast.signal("reading").score!!, 1e-6)
        val slow = ExamFitness.report(input(withTap(90_000)))
        assertEquals(0.0, slow.signal("reading").score!!, 1e-6)
        assertTrue(slow.finding!!.contains("words a minute"))
    }

    @Test fun freezesRushingPressureAndDoubt() {
        val idle = (0 until 40).map { i -> a(now - D + i * 80_000L, true, idle = if (i % 5 == 0) 250_000 else 5_000) }
        assertTrue("20% freezes scores zero", ExamFitness.report(input(idle)).signal("freeze").score!! < 0.01)

        // Slow question, then three rushed wrong ones, repeated.
        val rush = (0 until 120).map { i ->
            val k = i % 6
            a(now - D + i * 60_000L, k == 0 || k > 3, ms = when (k) { 0 -> 200_000; 1, 2, 3 -> 10_000; else -> 70_000 })
        }
        assertTrue("panic pattern found", ExamFitness.report(input(rush)).signal("rush").score!! < 0.5)

        val pressure = run(now - 3 * D, 40, { i -> i % 10 < 8 }) + run(now - D, 40, { i -> i % 10 < 5 }, timed = true)
        assertTrue("30-point gap scores zero", ExamFitness.report(input(pressure)).signal("pressure").score!! < 0.01)

        val doubt = (0 until 20).map { i -> a(now - D + i * 80_000L, false, rw = 1) }
        assertEquals(0.0, ExamFitness.report(input(doubt)).signal("doubt").score!!, 1e-9)
    }

    @Test fun fallingMomentumIsNoticed() {
        // Three busy weeks, then a near-empty one.
        val busy = (7 until 28).flatMap { d -> run(now - d * D, 30, { true }) }
        val quiet = run(now - D, 5, { true })
        val r = ExamFitness.report(input(busy + quiet))
        assertTrue(r.momentumLow)
        assertNotNull(r.part("consistency").score)
    }

    @Test fun abandonedSittingsCount() {
        val ev = listOf(
            SittingEvent("a", "path", now - 5 * D, 40, 10, false),
            SittingEvent("a", "path", now - 5 * D + 3_600_000, 40, 40, true), // resumed and finished: not abandoned
            SittingEvent("b", "practice", now - 4 * D, 40, 12, false),
            SittingEvent("c", "practice", now - 3 * D, 40, 40, true),
        )
        val r = ExamFitness.report(input(run(now - 30 * D, 5, { true }), ev))
        assertEquals("2 of 3", r.signal("sessions").figure)
    }

    @Test fun weeklyCheckGivesAWorryIndexAndRepeatedHighWorryIsFlagged() {
        val high = listOf(5, 5, 5, 5, 1) // all worries agreed, "able to focus" disagreed
        val c = (0 until 3).map { i -> CheckIn(now - (14 - 7 * i) * D, "weekly", 0, high) }
        val r = ExamFitness.report(input(emptyList(), checkIns = c))
        assertEquals(100, r.worry)
        assertTrue(r.worryHighForWeeks)
        val calm = ExamFitness.report(input(emptyList(), checkIns = listOf(CheckIn(now, "weekly", 0, listOf(1, 1, 1, 1, 5)))))
        assertEquals(0, calm.worry)
        assertFalse(calm.worryHighForWeeks)
    }

    @Test fun weeksStartOnMondayAndComparisonsKnowWhichWayIsBetter() {
        val ws = WeeklyReports.weekStart(now)
        assertEquals(Calendar.MONDAY, Calendar.getInstance().apply { timeInMillis = ws }.get(Calendar.DAY_OF_WEEK))
        assertTrue(now - ws in 0 until 7 * D)
        val prev = WeeklyReport(ws - 7 * D, 0, 60, 70, 20, 5, 300, 4, 100, 0, null, 55, 50, 50, 70, 160, 60, 10, 60)
        val cur = prev.copy(weekStart = ws, readiness = 65, freezePct = 6, worry = 70)
        val rows = WeeklyReports.compare(cur, prev).flatMap { it.rows }.associateBy { it.label }
        assertEquals(5, rows["Readiness"]!!.delta)
        assertEquals(1, rows["Readiness"]!!.direction)
        assertEquals("fewer freezes is better", 1, rows["Freezes"]!!.direction)
        assertEquals("more worry is worse", -1, rows["Exam worry"]!!.direction)
        assertEquals(0, rows["Stamina"]!!.direction)
    }

    @Test fun newDataSurvivesSaving() {
        val dir = Files.createTempDirectory("pmp").toFile()
        val s = ProgressStore(dir)
        s.record(a(now, true, firstTap = 30_000, idle = 4_000, words = 140, timed = true, rw = 1, wr = 2))
        s.recordSitting(SittingEvent("k", "path", now, 20, 12, false))
        s.recordCheckIn(CheckIn(now, "weekly", 0, listOf(1, 2, 3, 4, 5)))
        s.putWeekly(WeeklyReport(now, now, 60, 70, 20, 5, 300, 4, 100, 1, 64, 55, null, 50, 70, 160, 60, 10, null))
        val back = ProgressStore(dir).apply { restore(s.toJson().toString()) }
        assertEquals(s.attempts, back.attempts)
        assertEquals(s.sittings, back.sittings)
        assertEquals(s.checkIns, back.checkIns)
        assertEquals(s.weekly, back.weekly)
    }
}
