package com.jahangirb.pmpprep

import com.jahangirb.pmpprep.data.AdaptiveConfig
import com.jahangirb.pmpprep.data.Approach
import com.jahangirb.pmpprep.data.Attempt
import com.jahangirb.pmpprep.data.ChartData
import com.jahangirb.pmpprep.data.DrillRun
import com.jahangirb.pmpprep.data.Domain
import com.jahangirb.pmpprep.data.EcoTask
import com.jahangirb.pmpprep.data.HintLadder
import com.jahangirb.pmpprep.data.Lesson
import com.jahangirb.pmpprep.data.LessonModule
import com.jahangirb.pmpprep.data.MistakeTag
import com.jahangirb.pmpprep.data.MockBands
import com.jahangirb.pmpprep.data.MockPlan
import com.jahangirb.pmpprep.data.MockRow
import com.jahangirb.pmpprep.data.Option
import com.jahangirb.pmpprep.data.ProgressStore
import com.jahangirb.pmpprep.data.QType
import com.jahangirb.pmpprep.data.Question
import com.jahangirb.pmpprep.data.QuestionPicker
import com.jahangirb.pmpprep.data.Readiness
import com.jahangirb.pmpprep.data.ReadinessConfig
import com.jahangirb.pmpprep.data.ExamLayout
import com.jahangirb.pmpprep.data.RootCause
import com.jahangirb.pmpprep.data.Srs
import com.jahangirb.pmpprep.data.SyncMerge
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.nio.file.Files
import kotlin.math.pow
import kotlin.random.Random

/** The hint ladder, the adaptive drill, the mock's pretest items and time analysis, the analytics charts and the drills log. */
class StudyLabTest {

    private fun opt(id: String, correct: Boolean) = Option(id, "Option $id", correct, "Why $id", null, false)

    private fun q(id: String, task: String = "t1", type: QType = QType.SINGLE, approach: Approach? = Approach.AGILE) = Question(
        id, type, Domain.PEOPLE, task, approach, 3, emptyList(), "Stem $id", "Explanation", "Mindset $id",
        options = if (type == QType.SINGLE) listOf(opt("a", false), opt("b", true), opt("c", false), opt("d", false)) else emptyList(),
    )

    private fun lesson(id: String, module: String, task: String) = Lesson(
        id, module, 1, task, 10, "Lesson $id", emptyList(), "", emptyList(),
        mapOf("agile" to "Agile lens of $id", "predictive" to "Predictive lens"), null, listOf("Trap of $id"), emptyList(), emptyList(), emptyList(), "",
    )

    private fun att(task: String, ok: Boolean, at: Long = 1, qid: String = "q-$task-$at", ms: Long = 60_000, cf: Int = 0, hints: Int = 0, src: String = "practice") =
        Attempt(qid, task, Domain.PEOPLE, null, QType.SINGLE, if (ok) 1.0 else 0.0, ok, ms, at, src, confidence = cf, hints = hints)

    /* ── hint ladder ─────────────────────────────────────────────────────── */

    @Test
    fun theLadderClimbsLensThenPrincipleThenStrike() {
        val question = q("q1")
        val l = lesson("L1", "people", "t1")
        val h1 = HintLadder.hint(question, 1, question.options, emptySet(), l)
        assertEquals(1, h1.rung)
        assertTrue(h1.text.contains("agile lens"))
        assertEquals("Agile lens of L1", h1.detail)
        assertEquals("Mindset q1", HintLadder.hint(question, 2, question.options, emptySet(), l).detail)
        // Rung 3 strikes the first wrong option on screen the person hasn't chosen, never the right one.
        val shown = listOf(opt("c", false), opt("b", true), opt("a", false))
        val h3 = HintLadder.hint(question, 3, shown, setOf("c"), l)
        assertEquals("a", h3.strike)
        assertEquals("Why a", h3.detail)
    }

    @Test
    fun noOptionsToStrikeGivesTheLessonsTrap() {
        val fill = q("q2", type = QType.FILLBLANK, approach = null)
        val l = lesson("L1", "people", "t1")
        assertTrue(HintLadder.hint(fill, 1, emptyList(), emptySet(), l).text.startsWith("Read the stem for the role"))
        val h3 = HintLadder.hint(fill, 3, emptyList(), emptySet(), l)
        assertNull(h3.strike)
        assertEquals("Trap of L1", h3.detail)
        // No lesson either: the principle again.
        assertEquals("Mindset q2", HintLadder.hint(fill, 3, emptyList(), emptySet(), null).detail)
    }

    @Test
    fun theLessonForATaskComesFromADomainModule() {
        val mods = listOf(LessonModule("found", 0, "F", "Foundations", null, ""), LessonModule("people", 1, "P", "People", Domain.PEOPLE, ""))
        val ls = listOf(lesson("F1", "found", "t1"), lesson("P1", "people", "t1"))
        assertEquals("P1", HintLadder.lessonFor(ls, mods, "t1")!!.id)
        assertNull(HintLadder.lessonFor(ls, mods, ""))
    }

    @Test
    fun hintedAnswersSurviveTheDocumentAndStayOutOfAccuracy() {
        val s = ProgressStore(Files.createTempDirectory("pmp").toFile())
        s.record(att("t1", true, at = 10, hints = 2))
        val back = ProgressStore(Files.createTempDirectory("pmp").toFile()).apply { restore(s.toJson().toString()) }
        assertEquals(2, back.attempts.single().hints)
        assertTrue(back.attempts.single().hinted)
        assertTrue(ChartData.counted(back.attempts).isEmpty())
    }

    /* ── adaptive ────────────────────────────────────────────────────────── */

    @Test
    fun adaptiveWeightsFollowTheWebsFormula() {
        val c = AdaptiveConfig()
        // No evidence: the prior, 0.5, so (0.5)^1.5 + 0.05, boosted for unseen.
        assertEquals((0.5.pow(1.5) + 0.05) * 1.4, QuestionPicker.adaptiveWeight(0 to 0.0, null, c), 1e-12)
        // Six right of six: (6 + 2) / 10 = 0.8 → 0.2^1.5 + 0.05, halved for right last time.
        assertEquals((0.2.pow(1.5) + 0.05) * 0.5, QuestionPicker.adaptiveWeight(6 to 6.0, true, c), 1e-12)
        // Wrong last time: no multiplier.
        assertEquals(1.0.pow(1.5) * 0 + (1 - 2.0 / 10).pow(1.5) + 0.05, QuestionPicker.adaptiveWeight(6 to 0.0, false, c), 1e-12)
    }

    @Test
    fun theWeightedSampleNeverDrawsAZeroWhileAPositiveRemains() {
        repeat(50) { seed ->
            assertEquals(listOf("c"), QuestionPicker.weightedSample(listOf("a", "b", "c"), listOf(0.0, 0.0, 1.0), 1, Random(seed)))
        }
        // All zero falls back to a flat draw rather than nothing.
        assertEquals(2, QuestionPicker.weightedSample(listOf("a", "b", "c"), listOf(0.0, 0.0, 0.0), 2, Random(1)).toSet().size)
    }

    @Test
    fun anAdaptiveDrillLeansToTheWeakTask() {
        val weak = (1..20).map { q("w$it", "weak") }
        val strong = (1..20).map { q("s$it", "strong") }
        val history = (1..20).map { att("weak", false, at = it.toLong()) } + (1..20).map { att("strong", true, at = it.toLong()) }
        val last = history.associate { it.qid to it.correct }
        var weakPicked = 0
        repeat(200) { seed ->
            weakPicked += QuestionPicker.adaptive(weak + strong, history, { last[it] }, 10, rnd = Random(seed)).count { it.ecoTask == "weak" }
        }
        assertTrue("weak share ${weakPicked / 2000.0}", weakPicked > 2000 * 0.7)
        // A pool no bigger than the drill is all of it.
        assertEquals(3, QuestionPicker.adaptive(weak.take(3), history, { null }, 10).size)
    }

    /* ── mock exam ───────────────────────────────────────────────────────── */

    @Test
    fun pretestItemsScaleWithTheSitting() {
        assertEquals(10, MockPlan.pretestCount(180))
        assertEquals(5, MockPlan.pretestCount(90))
        assertEquals(2, MockPlan.pretestCount(45))
        assertEquals(1, MockPlan.pretestCount(20))
        assertEquals(0, MockPlan.pretestCount(1))
        assertEquals(0, MockPlan.pretestCount(0))
        val ids = (1..180).map { "q$it" }
        assertEquals(10, MockPlan.pickPretest(ids, rnd = Random(3)).size)
        assertTrue(ids.containsAll(MockPlan.pickPretest(ids, rnd = Random(3))))
    }

    @Test
    fun theTimeReportSplitsAtThePaceLineAndByBlock() {
        assertEquals(listOf(0, 0, 1, 1, 2), listOf(0, 39, 40, 109, 110).map { MockPlan.blockOf(it, listOf(40, 110)) })
        val rows = listOf(
            MockRow("a", 0, false, true, true, false, 60_000),
            MockRow("b", 0, false, true, false, true, 120_000),
            MockRow("c", 1, true, true, true, false, 80_000),
            MockRow("d", 1, false, false, false, true, 5_000),
        )
        val r = MockPlan.timeReport(rows, 80, 3)
        assertEquals(2, r.inside.n); assertEquals(2, r.inside.right)
        assertEquals(1, r.over.n); assertEquals(0, r.over.right)
        assertEquals(2, r.flagged.n)
        assertEquals(listOf("Block 1", "Block 2"), r.blocks.map { it.label })
        assertEquals(1, r.unanswered)
        assertEquals(87L, r.avgSec)
        assertEquals(listOf("x", "a", "b"), MockPlan.enqueue(listOf("x", "a"), listOf("a", "b")))
    }

    /* ── analytics ───────────────────────────────────────────────────────── */

    @Test
    fun theTrendIsARollingMeanByAnswerNumber() {
        val atts = (1..60).map { att("t", it > 30, at = it.toLong()) }
        val t = ChartData.trend(atts)
        assertEquals(51, t.size) // from answer 10 to 60
        assertEquals(10, t.first().n)
        assertEquals(0.0, t.first().value, 0.0)
        assertTrue(!t.first().full && t.last().full)
        assertEquals(30.0 / 50, t.last().value, 1e-9)
        assertTrue(ChartData.trend(atts.take(9)).isEmpty())
    }

    @Test
    fun theHeatMapGreysThinTasksAndBandsTheRest() {
        val tasks = listOf(EcoTask("t1", Domain.PEOPLE, 1, "One", ""), EcoTask("t2", Domain.PEOPLE, 2, "Two", ""))
        val atts = (1..10).map { att("t1", it <= 9, at = it.toLong()) } + (1..3).map { att("t2", true, at = it.toLong()) }
        val cells = ChartData.heat(tasks, atts, MockBands(), 5)
        assertEquals(4, cells[0].band)
        assertTrue(cells[1].thin)
        assertEquals(3, cells[1].n)
    }

    @Test
    fun paceBinsAndCalibrationAndCauses() {
        val atts = listOf(att("t", true, ms = 10_000, at = 1), att("t", true, ms = 79_000, at = 2), att("t", false, ms = 400_000, at = 3),
            att("t", true, cf = 3, at = 4), att("t", false, cf = 3, at = 5), att("t", true, cf = 1, at = 6), att("t", false, cf = 2, at = 7),
            att("t", true, cf = 3, at = 8, hints = 1))
        val bins = ChartData.paceHistogram(atts)
        assertEquals(13, bins.size)
        assertEquals(1, bins[0].n); assertEquals(1, bins[12].n); assertNull(bins[12].hi)
        val c = ChartData.calibration(atts)
        assertEquals(listOf(1, 1, 1, 1), listOf(c.sureRight, c.sureWrong, c.unsureRight, c.unsureWrong))
        assertTrue(ChartData.calibrationVerdict(c, 5).contains("under 5"))
        val tags = listOf(MistakeTag(RootCause.RUSHED, "", 1), MistakeTag(RootCause.RUSHED, "", 2), MistakeTag(RootCause.CALCULATION, "", 3))
        val causes = ChartData.causes(tags, RootCause.entries)
        assertEquals(6, causes.size)
        assertEquals(2, causes.first { it.first == RootCause.RUSHED }.second)
    }

    /* ── the drills log ──────────────────────────────────────────────────── */

    @Test
    fun drillsRoundTripCapAndMerge() {
        val s = ProgressStore(Files.createTempDirectory("pmp").toFile())
        s.recordDrill(DrillRun(100, "npv", true))
        s.recordDrill(DrillRun(200, "float", false))
        val doc = s.toJson()
        assertEquals(2, doc.getJSONArray("drills").length())
        assertEquals("npv", doc.getJSONArray("drills").getJSONObject(0).getString("g"))
        val back = ProgressStore(Files.createTempDirectory("pmp").toFile()).apply { restore(doc.toString()) }
        assertEquals(listOf(DrillRun(100, "npv", true), DrillRun(200, "float", false)), back.drills.toList())
        assertEquals(listOf(Triple("finance", 1, 1), Triple("schedule", 1, 0)), ChartData.drillsByGroup(back.drills))

        // Union by at+g; the same row from both sides counts once.
        fun d(vararg rows: Pair<Long, String>) = JSONObject().put("type", "pmp-progress").put("version", 3)
            .put("drills", JSONArray().apply { rows.forEach { put(JSONObject().put("at", it.first).put("g", it.second).put("ok", true)) } })
        val merged = SyncMerge.merge(null, d(1L to "npv", 2L to "pta"), d(2L to "pta", 3L to "irr", 1L to "bcr"), 9, JSONObject())
        val got = merged.getJSONArray("drills")
        assertEquals(4, got.length())
        assertEquals(listOf(1L, 1L, 2L, 3L), (0 until got.length()).map { got.getJSONObject(it).getLong("at") })

        // Capped at the newest 2,000, in the store and in the merge.
        val big = JSONArray().apply { (1..2100).forEach { put(JSONObject().put("at", it.toLong()).put("g", "npv").put("ok", false)) } }
        val capped = SyncMerge.merge(null, JSONObject().put("drills", big), JSONObject(), 9, JSONObject()).getJSONArray("drills")
        assertEquals(2000, capped.length())
        assertEquals(101L, capped.getJSONObject(0).getLong("at"))
        repeat(2005) { s.recordDrill(DrillRun(1000L + it, "pta", true)) }
        assertEquals(ProgressStore.MAX_DRILLS, s.drills.size)
    }

    @Test
    fun aDayWithOnlyADrillIsAStudyDay() {
        val r = Readiness(ReadinessConfig(35, 20, 25, 10, 10, 21.0, 0.5, 8.0, 400, 0.4, 0.85, 5, listOf(0.5, 0.3, 0.2), 0.5, 0.85,
            14.0, 14.0, 0.25, listOf(60.0, 90.0), 150.0, 20.0, 30, 14, 10, 300, 20), ExamLayout(180, 240, 10, listOf(40, 110), 80), emptyList())
        val s = ProgressStore(Files.createTempDirectory("pmp").toFile())
        val now = System.currentTimeMillis()
        val yesterday = Srs.startOfDay(now) - Srs.DAY_MS / 2
        s.setPlan(s.plan.copy(restDays = false))
        assertEquals(0, r.streak(s, now).activeInWindow)
        s.recordDrill(DrillRun(yesterday, "npv", false))
        val st = r.streak(s, now)
        assertEquals(1, st.activeInWindow)
        assertEquals(1, st.current)
    }
}
