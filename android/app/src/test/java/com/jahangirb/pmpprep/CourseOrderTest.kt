package com.jahangirb.pmpprep

import com.jahangirb.pmpprep.data.ContentParser
import com.jahangirb.pmpprep.data.DiagnosticResult
import com.jahangirb.pmpprep.data.Domain
import com.jahangirb.pmpprep.data.Extras
import com.jahangirb.pmpprep.data.PlanEngine
import com.jahangirb.pmpprep.data.PlanInput
import com.jahangirb.pmpprep.data.PlanSettings
import com.jahangirb.pmpprep.data.SessionKind
import com.jahangirb.pmpprep.data.SessionResult
import com.jahangirb.pmpprep.data.StudyPlan
import com.jahangirb.pmpprep.data.ProgressStore
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File
import java.nio.file.Files
import java.util.Calendar

/** Study order (index.html §10.24, tests/24-lectures): by knowledge area, new lessons come in the classroom order. */
class CourseOrderTest {
    private val x: Extras = ContentParser.extras(File(System.getProperty("pmp.assets")!!, "content/extras.json").readText())
    private val now: Long = Calendar.getInstance().apply { set(2027, Calendar.MARCH, 1, 9, 0, 0); set(Calendar.MILLISECOND, 0) }.timeInMillis
    private val diag = DiagnosticResult(0, 25, 13, mapOf(Domain.PEOPLE to (6 to 10), Domain.PROCESS to (5 to 10), Domain.BUSINESS to (5 to 9)))

    private fun builds(byArea: Boolean): List<String> {
        val plan = PlanEngine.build(PlanInput(x, emptyList(), emptySet(), emptyList(), emptyList(), diag,
            PlanSettings(now + 90 * PlanEngine.DAY, 10, (1..7).toSet(), byArea), now))
        return plan.sessions.filter { it.kind == SessionKind.BUILD }.mapNotNull { it.lessonId }
    }

    @Test fun classroomOrder() {
        val course = builds(true)
        val idx = course.map { x.courseOrder.indexOf(it) }
        assertTrue("at least ten new lessons planned", course.size >= 10)
        assertEquals(idx.sorted(), idx)
        assertNotEquals(builds(false), course)
    }

    @Test fun contentCarriesTheTours() {
        assertEquals(x.lessons.size, x.courseOrder.size)
        assertEquals(x.lessons.map { it.id }.toSet(), x.courseOrder.toSet())
        assertEquals(10, x.areaTours.size)
        assertTrue(x.areaTours.all { it.figure != null && it.lessons.isNotEmpty() })
    }

    @Test fun theSettingTravelsInThePlan() {
        val s = ProgressStore(Files.createTempDirectory("pmp").toFile())
        s.setPlan(StudyPlan(hoursPerWeek = 9, byArea = true))
        val doc = s.toJson()
        assertEquals("course", doc.getJSONObject("plan").getString("order"))
        val back = ProgressStore(Files.createTempDirectory("pmp").toFile())
        back.restore(doc.toString())
        assertTrue(back.plan.byArea)
        s.setPlan(StudyPlan(hoursPerWeek = 9))
        assertTrue(!s.toJson().getJSONObject("plan").has("order"))
    }

    /** A finished "lesson:" or "build:" session counts its lesson as read even if the lesson set lost it (sync). */
    @Test fun finishedSessionsCountTheirLessons() {
        val f0 = x.lessons.first { it.module == "foundations" }.id
        val build = x.lessons.first { it.ecoTask.isNotBlank() }
        fun keys(results: List<SessionResult>) = PlanEngine.build(PlanInput(x, emptyList(), emptySet(), results, emptyList(), diag,
            PlanSettings(now + 90 * PlanEngine.DAY, 10, (1..7).toSet()), now)).sessions.map { it.key }
        val before = keys(emptyList())
        assertTrue("lesson:$f0" in before && "build:${build.ecoTask}" in before)
        val after = keys(listOf(
            SessionResult("lesson:$f0", SessionKind.FOUNDATION, null, null, 1.0, 20, now - PlanEngine.DAY),
            SessionResult("build:${build.ecoTask}", SessionKind.BUILD, build.ecoTask, null, 0.9, 40, now - PlanEngine.DAY)))
        assertTrue("lesson:$f0" !in after)
        assertTrue(after.none { it == "build:${build.ecoTask}" })
    }
}
