package com.jahangirb.pmpprep

import com.jahangirb.pmpprep.data.Answer
import com.jahangirb.pmpprep.data.CasePlan
import com.jahangirb.pmpprep.data.CaseStudy
import com.jahangirb.pmpprep.data.ContentParser
import com.jahangirb.pmpprep.data.QType
import com.jahangirb.pmpprep.data.Scoring
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File
import kotlin.random.Random

/**
 * Case studies (index.html §05.3) and their place in a mock (§10.03.1 layoutFor). The fixture cases are
 * built inline; the staged content in content/staging/build is checked too when it is present.
 */
class CasePlanTest {

    private fun item(id: String, domain: String = "process"): JSONObject = JSONObject()
        .put("id", id).put("type", "single").put("domain", domain).put("ecoTask", "$domain-1").put("approach", "hybrid")
        .put("stem", "A stem long enough to be a stem for $id.").put("explanation", "e").put("mindset", "m")
        .put("options", JSONArray()
            .put(JSONObject().put("id", "a").put("text", "Right").put("correct", true).put("rationale", "r"))
            .put(JSONObject().put("id", "b").put("text", "Wrong").put("correct", false).put("rationale", "r")))

    private fun case(id: String, n: Int): JSONObject {
        val qs = JSONArray()
        for (i in 1..n) qs.put(item("pmp-$id-%03d".format(i)))
        return JSONObject().put("id", id).put("title", "Case $id").put("approach", "hybrid").put("setting", "s")
            .put("passage", JSONArray().put("First paragraph.").put("Second paragraph."))
            .put("exhibits", JSONArray().put(JSONObject().put("id", "ex1").put("label", "Exhibit 1").put("title", "T")
                .put("cols", JSONArray().put("A").put("B")).put("rows", JSONArray().put(JSONArray().put("1").put("2")))))
            .put("questions", qs)
    }

    private fun cases(vararg sizes: Int): List<CaseStudy> {
        val a = JSONArray()
        sizes.forEachIndexed { i, n -> a.put(case("c${i + 1}", n)) }
        return ContentParser.cases(a)
    }

    @Test
    fun parsesTheCaseShape() {
        val c = cases(10).single()
        assertEquals("c1", c.id)
        assertEquals(listOf("First paragraph.", "Second paragraph."), c.passage)
        assertEquals("Exhibit 1", c.exhibits.single().label)
        assertEquals(listOf(listOf("1", "2")), c.exhibits.single().rows)
        assertEquals(10, c.questions.size)
        assertEquals("pmp-c1-001", c.questions.first().id)
        assertEquals("case:c1", c.questions.first().packId)
    }

    @Test
    fun missingOrEmptyCasesAreSafe() {
        assertTrue(ContentParser.cases(null).isEmpty())
        assertTrue(ContentParser.cases(JSONArray()).isEmpty())
        // A case whose items are all invalid is dropped rather than shown empty.
        val bad = JSONObject().put("id", "x").put("questions", JSONArray().put(JSONObject().put("id", "q")))
        assertTrue(ContentParser.cases(JSONArray().put(bad)).isEmpty())
        assertTrue(CasePlan.pickCases(emptyList(), 40).isEmpty())
        assertNull(CasePlan.nextCase(emptyList(), { false }))
        val rest = cases(5).single().questions
        assertEquals(rest, CasePlan.paper(emptyList(), rest))
    }

    @Test
    fun extrasWithoutCasesStillParse() {
        val assets = System.getProperty("pmp.assets") ?: return
        val f = File(assets, "content/extras.json")
        if (!f.exists()) return
        val x = ContentParser.extras(f.readText())
        assertEquals(40, x.exam.caseBlockItems)
        // Whatever index.html ships today, every case that parsed has items.
        assertTrue(x.cases.all { it.questions.isNotEmpty() })
    }

    @Test
    fun blockSizeFollowsTheWebLayout() {
        assertEquals(40, CasePlan.caseItemsFor(180, 180, 40))
        assertEquals(40, CasePlan.caseItemsFor(200, 180, 40))
        assertEquals(20, CasePlan.caseItemsFor(90, 180, 40))
        assertEquals(10, CasePlan.caseItemsFor(45, 180, 40))   // 10.0
        assertEquals(4, CasePlan.caseItemsFor(20, 180, 40))    // 4.44
        assertEquals(1, CasePlan.caseItemsFor(2, 180, 40))     // 0.44, floored at 1
        assertEquals(0, CasePlan.caseItemsFor(0, 180, 40))
        assertEquals(0, CasePlan.caseItemsFor(90, 180, 0))
    }

    @Test
    fun fullMockTakesWholeCasesUpToTheBlock() {
        val all = cases(10, 10, 10, 10, 10)
        val picks = CasePlan.pickCases(all, 40, rnd = Random(1))
        assertEquals(4, picks.size)
        picks.forEach { (c, qs) -> assertEquals(c.questions, qs) }   // whole and in authored order
        assertEquals(40, CasePlan.block(picks).size)
        // Mixed sizes: never over the block, and as full as whole cases allow.
        val mixed = cases(12, 9, 11, 8)
        val m = CasePlan.pickCases(mixed, 40, rnd = Random(2))
        val n = CasePlan.block(m).size
        assertTrue("$n", n in 29..40)
        assertTrue(m.all { (c, qs) -> qs == c.questions })
    }

    @Test
    fun shortSittingTakesTheOpeningOfOneCase() {
        val all = cases(10, 10)
        val picks = CasePlan.pickCases(all, 4, rnd = Random(3))
        assertEquals(1, picks.size)
        val (c, qs) = picks.single()
        assertEquals(c.questions.take(4), qs)
    }

    @Test
    fun casesYouHaveSeenComeLast() {
        val all = cases(10, 10, 10)
        val seen = all[0].questions.map { it.id }.toSet() + all[2].questions.take(3).map { it.id }
        repeat(20) { s ->
            assertEquals("c2", CasePlan.nextCase(all, { it in seen }, Random(s))!!.id)
            assertEquals("c2", CasePlan.pickCases(all, 10, { it in seen }, Random(s)).single().first.id)
        }
    }

    @Test
    fun paperPutsTheBlockFirstWithNoRepeats() {
        val all = cases(10, 10)
        val block = CasePlan.block(CasePlan.pickCases(all, 10, rnd = Random(4)))
        val rest = all.flatMap { it.questions }.shuffled(Random(5))
        val paper = CasePlan.paper(block, rest)
        assertEquals(block, paper.take(10))
        assertEquals(20, paper.size)
        assertEquals(paper.size, paper.map { it.id }.toSet().size)
        val idx = CasePlan.index(all)
        assertTrue(paper.take(10).all { idx[it.id]?.id == block.first().let { q -> idx[q.id]!!.id } })
    }

    /* ── the staged content, when present ───────────────────────────────── */

    private fun staged(name: String): File? {
        val fx = System.getProperty("pmp.fixtures") ?: return null
        val f = File(File(fx).parentFile.parentFile.parentFile, "content/staging/build/$name")
        return if (f.exists()) f else null
    }

    @Test
    fun stagedCasesParseAndScore() {
        val f = staged("cases.json") ?: return
        val raw = JSONArray(f.readText())
        val parsed = ContentParser.cases(raw)
        assertEquals(raw.length(), parsed.size)
        for (i in 0 until raw.length()) {
            assertEquals("every item in ${parsed[i].id} validates", raw.getJSONObject(i).getJSONArray("questions").length(), parsed[i].questions.size)
        }
        parsed.flatMap { it.questions }.forEach { q ->
            assertTrue(q.id, Scoring.score(q, ContentTest.rightAnswer(q)).correct)
            assertFalse(q.id, Scoring.score(q, Answer()).correct)
        }
        val picks = CasePlan.pickCases(parsed, CasePlan.caseItemsFor(180), rnd = Random(6))
        assertEquals(40, CasePlan.block(picks).size)
    }

    @Test
    fun stagedGraphicItemsRenderAsHotspots() {
        val f = staged("graphic-questions.json") ?: return
        val p = ContentParser.parsePack(f.readText(), "staged", "staged", null, true)
        assertEquals(p.errors.toString(), 0, p.invalid)
        assertEquals(12, p.questions.size)
        p.questions.forEach { q ->
            assertEquals(QType.GRAPHIC, q.type)
            assertEquals(QType.HOTSPOT, q.base)
            assertTrue(q.id, q.graphic != null && q.graphic!!.viewBox.size == 4)
            assertEquals(q.id, q.regions.count { it.correct } > 1, q.multiSelect)
            assertTrue(q.id, Scoring.score(q, ContentTest.rightAnswer(q)).correct)
            assertFalse(q.id, Scoring.score(q, Answer(choices = setOf(q.regions.first { !it.correct }.id))).correct)
        }
        assertTrue(p.questions.any { it.graphic!!.flow.not() })
    }
}
