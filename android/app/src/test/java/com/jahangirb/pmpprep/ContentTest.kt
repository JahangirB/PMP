package com.jahangirb.pmpprep

import com.jahangirb.pmpprep.data.Answer
import com.jahangirb.pmpprep.data.Calculators
import com.jahangirb.pmpprep.data.ContentParser
import com.jahangirb.pmpprep.data.Grade
import com.jahangirb.pmpprep.data.ProgressStore
import com.jahangirb.pmpprep.data.QType
import com.jahangirb.pmpprep.data.Question
import com.jahangirb.pmpprep.data.Readiness
import com.jahangirb.pmpprep.data.Scoring
import com.jahangirb.pmpprep.data.Srs
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.BeforeClass
import org.junit.Test
import java.io.File
import java.nio.file.Files

/**
 * Runs against the assets the build generates from index.html and packs/, so
 * a malformed pack or a scoring regression fails CI rather than the app.
 */
class ContentTest {

    companion object {
        private lateinit var dir: File
        private lateinit var all: List<Question>

        @BeforeClass
        @JvmStatic
        fun load() {
            dir = File(System.getProperty("pmp.assets") ?: error("pmp.assets not set; run through Gradle"))
            val seen = HashSet<String>()
            val out = ArrayList<Question>()
            val files = listOf(File(dir, "content/core.json")) +
                (File(dir, "packs").listFiles()!!.filter { it.name.endsWith(".json") }.sortedBy { it.name })
            for (f in files) {
                val p = ContentParser.parsePack(f.readText(), f.name, null, f.name, true)
                assertEquals("${f.name}: ${p.errors}", 0, p.invalid)
                p.questions.filter { seen.add(it.id) }.forEach { out += it }
            }
            all = out
        }

        fun rightAnswer(q: Question): Answer = when (q.base) {
            QType.SINGLE, QType.MULTI -> Answer(choices = q.options.filter { it.correct }.map { it.id }.toSet())
            QType.HOTSPOT -> Answer(choices = q.regions.filter { it.correct }.map { it.id }.toSet())
            QType.MATCHING -> Answer(map = q.prompts.associate { it.id to Scoring.matchFor(q, it.id)!! })
            QType.FILLBLANK -> Answer(map = q.blanks.associate { it.id to if (it.numeric) it.expect.toString() else it.accept.first() })
            else -> Answer()
        }
    }

    @Test
    fun everyPackLoads() {
        assertTrue("expected the full pool, got ${all.size}", all.size > 2500)
        QType.entries.filter { it != QType.GRAPHIC }.forEach { t -> assertTrue("no $t items", all.any { it.type == t }) }
    }

    @Test
    fun extrasLoad() {
        val x = ContentParser.extras(File(dir, "content/extras.json").readText())
        assertEquals(26, x.tasks.size)
        assertTrue(x.scenarios.size >= 50)
        assertTrue(x.lessons.size >= 20)
        assertTrue(x.flashcards.size >= 200)
        assertTrue(x.glossary.size >= 300)
        assertTrue(x.reference.processes.size >= 40)
        assertEquals(180, x.exam.totalItems)
        assertTrue(x.scenarios.all { s -> s.options.count { it.rank.name == "BEST" } == 1 })
        assertTrue(x.lessons.sumOf { it.checks.size } > 50)
    }

    @Test
    fun theRightAnswerScoresFullMarksOnEveryQuestion() {
        for (q in all) {
            val a = rightAnswer(q)
            assertTrue("${q.id} should count as answered", Scoring.isAnswered(q, a))
            assertTrue("${q.id} should be correct", Scoring.score(q, a).correct)
        }
    }

    @Test
    fun wrongAndPartialAnswersAreNotCorrect() {
        val multi = all.first { it.base == QType.MULTI && it.correctCount >= 2 }
        val right = rightAnswer(multi).choices
        val partial = Scoring.score(multi, Answer(choices = setOf(right.first())))
        assertFalse(partial.correct)
        assertTrue(partial.partial)
        // Picking everything cancels out.
        assertFalse(Scoring.score(multi, Answer(choices = multi.options.map { it.id }.toSet())).correct)
        val single = all.first { it.base == QType.SINGLE }
        val wrong = single.options.first { !it.correct }
        assertEquals(0.0, Scoring.score(single, Answer(choices = setOf(wrong.id))).credit, 0.0)
    }

    @Test
    fun numbersAreReadTheWayPeopleTypeThem() {
        assertEquals(720000.0, Scoring.parseNumber("£720,000")!!, 0.0)
        assertEquals(-30000.0, Scoring.parseNumber("(30,000)")!!, 0.0)
        assertEquals(0.83, Scoring.parseNumber(" 0.83 ")!!, 0.0)
        assertEquals(null, Scoring.parseNumber("about 5"))
        assertEquals("schedule variance", Scoring.normalise("Schedule  Variance."))
    }

    @Test
    fun badPacksAreRefusedWithAReason() {
        try {
            ContentParser.parsePack("{\"type\":\"other\"}", "x", null, null, false); fail()
        } catch (e: ContentParser.PackException) { assertTrue(e.message!!.contains("not a question pack")) }
        val p = ContentParser.parsePack(
            """{"type":"pmp-pack","questions":[{"id":"a","type":"single","domain":"people","stem":"s","options":[{"id":"x","text":"t","correct":true},{"id":"y","text":"u"}]},{"id":"b","type":"single","domain":"mars","stem":"s"}]}""",
            "x", null, null, false,
        )
        assertEquals(1, p.questions.size)
        assertEquals(1, p.invalid)
        assertTrue(p.errors.first().contains("domain"))
    }

    @Test
    fun sm2MatchesTheWebApp() {
        val x = ContentParser.extras(File(dir, "content/extras.json").readText())
        val srs = Srs(x.srs)
        val t0 = 1_700_000_000_000L
        var s = srs.review(srs.fresh(), Grade.GOOD, t0)
        assertEquals(1, s.interval)
        s = srs.review(s, Grade.GOOD, t0)
        assertEquals(6, s.interval)
        s = srs.review(s, Grade.GOOD, t0)
        assertEquals(15, s.interval) // 6 × 2.5
        s = srs.review(s, Grade.AGAIN, t0)
        assertEquals(1, s.interval)
        assertEquals(1, s.lapses)
        assertEquals(2.3, s.ease, 1e-9)
        assertEquals(4, srs.review(srs.fresh(), Grade.EASY, t0).interval)
    }

    @Test
    fun readinessStartsAtZeroAndIsProvisional() {
        val x = ContentParser.extras(File(dir, "content/extras.json").readText())
        val store = ProgressStore(Files.createTempDirectory("pmp").toFile())
        val r = Readiness(x.readiness, x.exam, x.tasks).report(store)
        assertTrue(r.provisional)
        assertEquals(0, r.components.first { it.id == "mocks" }.score.toInt())
        assertEquals(5, r.components.size)
        assertEquals(100, r.components.sumOf { it.weight })
    }

    @Test
    fun evmArithmetic() {
        val lines = Calculators.evm(pv = 200_000.0, ev = 150_000.0, ac = 180_000.0, bac = 600_000.0)
        val cpi = lines.first { it.label.startsWith("Cost performance") }.value
        assertEquals("0.83", cpi)
        val eac = lines.first { it.formula == "EAC = BAC ÷ CPI" }.value
        assertEquals("720,000", eac)
    }
}
