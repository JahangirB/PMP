package com.jahangirb.pmpprep

import com.jahangirb.pmpprep.data.Attempt
import com.jahangirb.pmpprep.data.ContentParser
import com.jahangirb.pmpprep.data.DiagnosticResult
import com.jahangirb.pmpprep.data.Domain
import com.jahangirb.pmpprep.data.Extras
import com.jahangirb.pmpprep.data.Mastery
import com.jahangirb.pmpprep.data.MockResult
import com.jahangirb.pmpprep.data.Plan
import com.jahangirb.pmpprep.data.PlanEngine
import com.jahangirb.pmpprep.data.PlanInput
import com.jahangirb.pmpprep.data.PlanSettings
import com.jahangirb.pmpprep.data.QType
import com.jahangirb.pmpprep.data.Session
import com.jahangirb.pmpprep.data.SessionKind
import com.jahangirb.pmpprep.data.SessionResult
import com.jahangirb.pmpprep.data.StepKind
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File
import java.util.Calendar

/**
 * Simulated learners study day by day, doing exactly what the plan says at a
 * fixed level of skill. The plan must keep every promise in the design:
 * cover every task, schedule mocks, taper, absorb skipped days, remediate
 * failures, and count practice done outside the path.
 */
class PlanEngineTest {
    private val x: Extras = ContentParser.extras(File(System.getProperty("pmp.assets")!!, "content/extras.json").readText())
    private val weekdays = setOf(Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY)
    private val diag = DiagnosticResult(0, 25, 13, mapOf(Domain.PEOPLE to (5 to 8), Domain.PROCESS to (4 to 10), Domain.BUSINESS to (4 to 7)))

    /** A Monday at 9am, so weekday maths is predictable. */
    private val start: Long = Calendar.getInstance().apply {
        set(2027, Calendar.MARCH, 1, 9, 0, 0); set(Calendar.MILLISECOND, 0)
    }.timeInMillis

    private inner class Learner(val settings: PlanSettings, val skill: Double) {
        val attempts = ArrayList<Attempt>()
        val lessons = HashSet<String>()
        val results = ArrayList<SessionResult>()
        val mocks = ArrayList<MockResult>()
        val tags = HashMap<String, com.jahangirb.pmpprep.data.RootCause>()
        val leeches = HashMap<String, Int>()
        var ms = 60_000L
        var ladderNext: Int? = null
        var readingSlow = false

        fun plan(now: Long): Plan = PlanEngine.build(PlanInput(x, attempts, lessons, results, mocks, diag, settings, now, tags, leeches, ladderNext, readingSlow))

        fun answer(task: String, n: Int, score: Double, at: Long, source: String = "path") {
            val t = x.tasks.first { it.id == task }
            val right = Math.round(n * score).toInt()
            repeat(n) { i -> attempts += Attempt("$task-${attempts.size}", task, t.domain, null, QType.SINGLE, if (i < right) 1.0 else 0.0, i < right, ms, at + i, source) }
        }

        fun doSession(s: Session, at: Long, score: Double = skill) {
            var rr = 0
            for (st in s.steps) when (st.kind) {
                StepKind.LESSON -> s.lessonId?.let { lessons += it }
                StepKind.PRACTICE -> {
                    val pool = when {
                        s.taskId != null -> listOf(s.taskId)
                        s.domain != null -> x.tasks.filter { it.domain == s.domain }.map { it.id }
                        else -> x.tasks.map { it.id }
                    }
                    val right = Math.round(st.count * score).toInt()
                    repeat(st.count) { i -> answer(pool[(rr++) % pool.size], 1, if (i < right) 1.0 else 0.0, at + i) }
                }
                StepKind.MOCK -> mocks += MockResult(at, "mock", 180, Math.round(180 * score).toInt(), 0, emptyMap())
                else -> {}
            }
            results += SessionResult(s.key, s.kind, s.taskId, s.domain, score, s.minutes, at)
        }

        /** Does today's planned sessions, as the Continue button would serve them. */
        fun studyDay(now: Long, score: Double = skill): Plan {
            val p = plan(now)
            val today = PlanEngine.startOfDay(now)
            var t = now
            var guard = 0
            while (guard++ < 10) {
                val next = plan(t).sessions.firstOrNull { it.day == today } ?: break
                doSession(next, t, score)
                t += 60_000 * next.minutes
            }
            return p
        }
    }

    private fun weekday(t: Long) = Calendar.getInstance().apply { timeInMillis = t }.get(Calendar.DAY_OF_WEEK)

    private fun days(n: Int) = n * PlanEngine.DAY

    @Test
    fun aDiligentLearnerCoversEverythingAndTapers() {
        val exam = start + days(70)
        val l = Learner(PlanSettings(exam, 8, weekdays), skill = 0.9)
        var t = start
        while (t < exam - days(1)) {
            if (weekday(t) in weekdays) {
                val p = l.plan(t)
                assertTrue("nothing planned on or after exam day", p.sessions.all { it.day < PlanEngine.startOfDay(exam) })
                // No ordinary day is planned far beyond its time.
                p.sessions.filter { it.kind != SessionKind.MOCK }.groupBy { it.day }.forEach { (_, l2) ->
                    assertTrue("day over budget: ${l2.sumOf { it.minutes }} vs ${p.minutesPerDay}", l2.sumOf { it.minutes } <= p.minutesPerDay + 60)
                }
                if (exam - t <= days(7)) assertTrue("taper only: ${p.sessions.filter { it.day == PlanEngine.startOfDay(t) }.map { it.key + " " + it.why }}", p.sessions.filter { it.day == PlanEngine.startOfDay(t) }.all { it.kind == SessionKind.TAPER })
                l.studyDay(t)
            }
            t = PlanEngine.nextDay(t)
        }
        assertEquals("every lesson read", x.lessons.size, l.lessons.size)
        val states = PlanEngine.taskStates(x, l.attempts, t)
        assertTrue("every task practised to the bar: ${states.filter { it.answers < 10 }.map { it.task.id }}", states.all { it.answers >= 10 })
        assertTrue("at least two full mocks: ${l.mocks.size}", l.mocks.size >= 2)
        Domain.entries.forEach { d -> assertTrue("$d checkpoint passed", l.results.any { it.kind == SessionKind.CHECKPOINT && it.domain == d && it.score >= 0.7 }) }
    }

    @Test
    fun theWeakestDiagnosticDomainComesFirst() {
        val l = Learner(PlanSettings(start + days(70), 8, weekdays), 0.9)
        l.lessons += x.lessons.filter { it.module == "foundations" }.map { it.id }
        val first = l.plan(start).sessions.first { it.kind == SessionKind.BUILD }
        assertEquals(Domain.PROCESS, first.domain) // Process was 40% in the diagnostic, the lowest
    }

    @Test
    fun aWeekOffBringsAWarmUpAndKeepsEverythingBeforeTheExam() {
        val exam = start + days(60)
        val l = Learner(PlanSettings(exam, 8, weekdays), 0.85)
        var t = start
        repeat(10) { if (weekday(t) in weekdays) l.studyDay(t); t = PlanEngine.nextDay(t) }
        t += days(8)
        while (weekday(t) !in weekdays) t = PlanEngine.nextDay(t)
        val p = l.plan(t)
        assertEquals(SessionKind.REENTRY, p.sessions.first().kind)
        assertTrue(p.sessions.all { it.day < PlanEngine.startOfDay(exam) })
        assertTrue(p.adjustments.any { it.contains("Re-entry") })
        // After the warm-up, it is not offered again.
        l.studyDay(t)
        assertFalse(l.plan(PlanEngine.nextDay(t)).sessions.any { it.kind == SessionKind.REENTRY })
    }

    @Test
    fun losingThreeWeeksTriggersCatchUpButKeepsTwoMocks() {
        val exam = start + days(30)
        val l = Learner(PlanSettings(exam, 5, weekdays), 0.8)
        val p = l.plan(start + days(18))
        assertTrue("trimmed or flagged", p.dropped.isNotEmpty() || p.behind)
        assertTrue(p.neededMinutes > 0)
        assertTrue("two mocks kept", p.sessions.count { it.kind == SessionKind.MOCK } >= 2)
        if (p.behind) assertTrue(p.adjustments.any { it.contains("hours of work") })
    }

    @Test
    fun aFailedSessionIsFixedFirstNextDay() {
        val l = Learner(PlanSettings(start + days(60), 8, weekdays), 0.9)
        l.lessons += x.lessons.filter { it.module == "foundations" }.map { it.id }
        val build = l.plan(start).sessions.first { it.kind == SessionKind.BUILD }
        l.doSession(build, start, score = 0.4)
        val next = l.plan(PlanEngine.nextDay(start)).sessions.first()
        assertEquals(SessionKind.REMEDIATION, next.kind)
        assertEquals(build.taskId, next.taskId)
        assertTrue(next.steps.any { it.kind == StepKind.PRACTICE && it.easy })
        // Once remediated well, it goes away.
        l.doSession(next, PlanEngine.nextDay(start), 0.9)
        assertFalse(l.plan(PlanEngine.nextDay(PlanEngine.nextDay(start))).sessions.any { it.kind == SessionKind.REMEDIATION && it.taskId == build.taskId })
    }

    @Test
    fun aMiddlingSessionIsNotRepeatedTheSameDay() {
        val l = Learner(PlanSettings(start + days(60), 8, weekdays), 0.9)
        val task = x.tasks.first().id
        l.lessons += x.lessons.map { it.id }
        l.answer(task, 10, 0.5, start - days(1))
        val drill = l.plan(start).sessions.first { it.kind == SessionKind.DRILL && it.taskId == task }
        l.doSession(drill, start, 0.65)
        val again = l.plan(start + 3_600_000).sessions.filter { it.taskId == task && it.kind == SessionKind.DRILL }
        assertTrue("still drilled, but not today", again.isNotEmpty() && again.all { it.day > PlanEngine.startOfDay(start) })
    }

    @Test
    fun practiceOutsideThePathCounts() {
        val l = Learner(PlanSettings(start + days(60), 8, weekdays), 0.9)
        val t = x.tasks.first { it.id == "business-5" }
        l.lessons += x.lessons.first { it.ecoTask == t.id }.id
        val before = l.plan(start).sessions.count { it.taskId == t.id }
        assertTrue(before > 0)
        l.answer(t.id, 15, 0.9, start, source = "practice")
        val p = l.plan(start + 60_000)
        assertEquals(0, p.sessions.count { it.taskId == t.id })
        assertEquals(Mastery.MASTERED, p.tasks.first { it.task.id == t.id }.mastery)
        assertTrue(p.adjustments.any { it.contains(t.label) && it.contains("own practice") })
    }

    @Test
    fun masteredTasksFadeAndComeBack() {
        val l = Learner(PlanSettings(start + days(90), 8, weekdays), 0.9)
        val t = x.tasks.first { it.id == "people-2" }
        l.lessons += x.lessons.first { it.ecoTask == t.id }.id
        l.answer(t.id, 15, 0.9, start)
        assertEquals(0, l.plan(start + days(1)).sessions.count { it.taskId == t.id })
        val later = l.plan(start + days(20))
        assertTrue(later.sessions.any { it.kind == SessionKind.REVIEW && it.taskId == t.id })
    }

    /* ── the six fixes from the assessment review ── */

    private fun taskWithLesson() = x.tasks.first { t -> x.lessons.any { it.ecoTask == t.id } }
    private fun lessonOf(task: String) = x.lessons.first { it.ecoTask == task }

    @Test
    fun aDomainThatSlipsAfterItsCheckpointIsReChecked() {
        val l = Learner(PlanSettings(start + days(60), 10, weekdays), 0.8)
        l.results += SessionResult("checkpoint:people", SessionKind.CHECKPOINT, null, Domain.PEOPLE, 0.8, 35, start)
        val people = x.tasks.filter { it.domain == Domain.PEOPLE }.map { it.id }
        people.forEachIndexed { i, t -> l.answer(t, 5, 0.8, start + 3_600_000 + i * 1000L) }
        assertTrue("holding up: no re-check", l.plan(start + days(1)).sessions.none { it.title == "Re-check: People" })
        people.forEachIndexed { i, t -> l.answer(t, 4, 0.25, start + days(1) + i * 1000L) }
        assertTrue("slipped under 70%: re-check planned", l.plan(start + days(2)).sessions.any { it.title == "Re-check: People" })
    }

    @Test
    fun taggedCausesShapeTheTasksDrills() {
        val t = taskWithLesson()
        val l = Learner(PlanSettings(start + days(60), 10, weekdays), 0.5)
        l.lessons += lessonOf(t.id).id
        l.answer(t.id, 12, 0.5, start)
        l.attempts.filter { !it.correct }.take(3).forEach { l.tags[it.qid] = com.jahangirb.pmpprep.data.RootCause.KNOWLEDGE_GAP }
        val drill = l.plan(start + days(1)).sessions.first { it.key == "drill:${t.id}:0" }
        assertEquals("knowledge gap: lesson first", StepKind.LESSON, drill.steps.first().kind)
        l.tags.keys.forEach { l.tags[it] = com.jahangirb.pmpprep.data.RootCause.WRONG_MINDSET }
        val drill2 = l.plan(start + days(1)).sessions.first { it.key == "drill:${t.id}:0" }
        assertTrue("mindset: scenarios added", drill2.steps.any { it.kind == StepKind.SCENARIOS })
    }

    @Test
    fun accurateButSlowGetsAPacedDrill() {
        val t = taskWithLesson()
        val l = Learner(PlanSettings(start + days(60), 10, weekdays), 1.0)
        l.lessons += lessonOf(t.id).id
        l.answer(t.id, 15, 1.0, start)
        assertTrue("quick and right: no paced drill", l.plan(start + days(1)).sessions.none { it.key == "pace:${t.id}" })
        l.ms = 150_000
        l.answer(t.id, 15, 1.0, start + 1000)
        assertTrue("right but slow: paced drill", l.plan(start + days(1)).sessions.any { it.key == "pace:${t.id}" })
    }

    @Test
    fun leechCardsBringTheLessonBack() {
        val t = taskWithLesson()
        val l = Learner(PlanSettings(start + days(60), 10, weekdays), 1.0)
        l.lessons += lessonOf(t.id).id
        l.answer(t.id, 15, 1.0, start)
        l.leeches[t.id] = 2
        val s = l.plan(start + days(1)).sessions.first { it.key == "relearn:${t.id}:2" }
        assertEquals(StepKind.LESSON, s.steps.first().kind)
        l.doSession(s, start + days(1))
        assertTrue("done once: gone until another card slips", l.plan(start + days(2)).sessions.none { it.key.startsWith("relearn:${t.id}") })
    }

    @Test
    fun examFitnessAddsAWeeklyStaminaSittingAndReadingDrill() {
        val l = Learner(PlanSettings(start + days(60), 10, weekdays), 0.8)
        l.ladderNext = 45
        l.readingSlow = true
        val p = l.plan(start)
        val ladder = p.sessions.first { it.key.startsWith("ladder:45:") }
        assertEquals(45, ladder.steps.first().count)
        assertTrue(ladder.steps.first().timed)
        val reading = p.sessions.first { it.key.startsWith("reading:") }
        assertTrue(reading.steps.first().reading)
        l.doSession(ladder, start)
        assertTrue("one stamina sitting a week", l.plan(start + days(2)).sessions.none { it.key.startsWith("ladder:") })
        assertTrue("back the next week", l.plan(start + days(7)).sessions.any { it.key.startsWith("ladder:") })
    }
}
