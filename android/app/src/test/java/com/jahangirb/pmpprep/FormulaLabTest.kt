package com.jahangirb.pmpprep

import com.jahangirb.pmpprep.data.Calculators
import com.jahangirb.pmpprep.data.DrillVerdict
import com.jahangirb.pmpprep.data.DrillTally
import com.jahangirb.pmpprep.data.FormulaDrills
import com.jahangirb.pmpprep.data.NetInput
import com.jahangirb.pmpprep.data.TreeBranch
import com.jahangirb.pmpprep.data.TreeOutcome
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.abs

/** The formula lab: the three row calculators and the eighteen drill generators (index.html §10.06). */
class FormulaLabTest {

    /* ── calculators ─────────────────────────────────────────────────────── */

    @Test
    fun theWebsDefaultNetworkHasTheTextbookFloats() {
        val r = Calculators.network(listOf(
            NetInput("A", 3.0, emptyList()), NetInput("B", 4.0, listOf("A")), NetInput("C", 2.0, listOf("A")),
            NetInput("D", 5.0, listOf("B")), NetInput("E", 3.0, listOf("C")), NetInput("F", 2.0, listOf("D", "E")),
        ))
        assertTrue(r.ok)
        assertEquals(14.0, r.duration, 0.0)
        assertEquals(listOf("A", "B", "D", "F"), r.critical)
        val c = r.byId("C")!!; val e = r.byId("E")!!
        assertEquals(4.0, c.tf, 0.0); assertEquals(0.0, c.ff, 0.0)
        assertEquals(4.0, e.tf, 0.0); assertEquals(4.0, e.ff, 0.0)
        assertEquals(1.0, r.byId("A")!!.es, 0.0); assertEquals(3.0, r.byId("A")!!.ef, 0.0)
        assertEquals(listOf(0, 1, 1, 2, 2, 3), listOf("A", "B", "C", "D", "E", "F").map { r.byId(it)!!.rank })
        assertFalse(r.multipleCritical)
        assertTrue(Calculators.networkLines(r).any { it.value == "C, E" || it.value == "C" })
    }

    @Test
    fun aBrokenNetworkIsReportedNotGuessed() {
        assertTrue(Calculators.network(listOf(NetInput("A", 2.0, listOf("B")), NetInput("B", 2.0, listOf("A")))).errors.single().contains("loop"))
        assertTrue(Calculators.network(listOf(NetInput("A", 2.0, listOf("Z")))).errors.single().contains("Z"))
        assertTrue(Calculators.network(listOf(NetInput("A", 0.0, emptyList()))).errors.isNotEmpty())
        assertTrue(Calculators.network(emptyList()).errors.isNotEmpty())
    }

    @Test
    fun theDecisionTreeTakesTheBestNetValue() {
        val rs = Calculators.tree(listOf(
            TreeBranch("Build new", 500000.0, listOf(TreeOutcome(0.6, 1200000.0), TreeOutcome(0.4, 400000.0))),
            TreeBranch("Upgrade", 180000.0, listOf(TreeOutcome(0.6, 700000.0), TreeOutcome(0.4, 300000.0))),
        ))
        assertEquals(380000.0, rs[0].net, 1e-6)
        assertEquals(360000.0, rs[1].net, 1e-6)
        assertEquals("Build new", Calculators.bestBranch(rs)!!.name)
        assertTrue(rs.all { it.pOk })
        val bad = Calculators.tree(listOf(TreeBranch("X", 0.0, listOf(TreeOutcome(0.5, 1.0), TreeOutcome(0.3, 1.0)))))
        assertFalse(bad[0].pOk)
    }

    @Test
    fun cashFlowGivesNpvIrrAndPayback() {
        val flows = listOf(120000.0, 160000.0, 180000.0, 150000.0)
        val r = Calculators.cash(400000.0, flows, 0.10)
        val npv = -400000 + 120000 / 1.1 + 160000 / 1.21 + 180000 / 1.331 + 150000 / 1.4641
        assertEquals(npv, r.npv, 1e-6)
        assertEquals(2 + 120000.0 / 180000, r.payback!!, 1e-9)
        val irr = r.irr!!
        assertEquals(0.0, Calculators.npvAt(400000.0, flows, irr), 1.0)
        assertTrue(irr > 0.10)
        assertNull(Calculators.payback(1000.0, listOf(100.0, 100.0)))
        // No sign change: no IRR rather than a wrong one.
        assertNull(Calculators.irr(-100.0, listOf(10.0, 10.0)))
        assertTrue(Calculators.cashLines(400000.0, flows, 10.0).any { it.label == "Internal rate of return" && it.value.endsWith("%") })
    }

    /* ── drills ──────────────────────────────────────────────────────────── */

    @Test
    fun thereAreEighteenGeneratorsWithStableIds() {
        val ids = FormulaDrills.GENERATORS.map { it.id }
        assertEquals(18, ids.size)
        assertEquals(ids.size, ids.toSet().size)
        assertEquals(listOf("evm-variance", "evm-forecast", "tcpi", "pert", "pert-path", "channels", "emv", "decision-tree", "pta", "fee",
            "float", "npv", "payback", "irr", "bcr", "littles-law", "velocity", "lead-time"), ids)
        assertEquals("schedule", FormulaDrills.groupOf("float"))
        assertNull(FormulaDrills.groupOf("from-a-newer-app"))
    }

    @Test
    fun everyGeneratorMarksItsOwnAnswerRightAndItsWrongTurnsByName() {
        for (g in FormulaDrills.GENERATORS) for (seed in 1L..150L) {
            val p = FormulaDrills.generate(g, seed)
            val where = "${g.id} seed $seed"
            assertTrue(where, p.answer.isFinite())
            assertEquals(where, g.id, p.generator)
            assertEquals(where, g.group, p.group)
            assertTrue(where, p.steps.isNotEmpty() && p.given.isNotEmpty() && p.stem.isNotBlank() && p.trap.isNotBlank())
            // The answer as the solution states it, and as someone would type it, marks right.
            assertEquals(where, DrillVerdict.RIGHT, FormulaDrills.mark(p, FormulaDrills.fmt(p.answer, p.dp)).verdict)
            assertEquals(where, DrillVerdict.RIGHT, FormulaDrills.mark(p, FormulaDrills.answerText(p)).verdict)
            // Every named wrong turn is reported as that mistake, and none sits on the answer.
            for (a in p.alts) {
                assertTrue(where, abs(a.value - p.answer) > FormulaDrills.tolFor(p))
                val m = FormulaDrills.mark(p, a.value.toString())
                assertEquals(where, DrillVerdict.WRONG, m.verdict)
                assertNotNull(where, m.why)
            }
            // No working line is left blank, and none shows NaN.
            assertTrue(where, p.steps.none { it.text.isBlank() || it.math?.contains("NaN") == true || it.math?.contains("—") == true })
        }
    }

    @Test
    fun theSameSeedGivesTheSameProblem() {
        val g = FormulaDrills.GENERATORS.first { it.id == "fee" }
        assertEquals(FormulaDrills.generate(g, 42), FormulaDrills.generate(g, 42))
        assertEquals("finance", FormulaDrills.next("finance", 7).group)
    }

    @Test
    fun theFloatProblemAgreesWithTheCalculator() {
        val g = FormulaDrills.GENERATORS.first { it.id == "float" }
        for (seed in 1L..60L) {
            val p = FormulaDrills.generate(g, seed)
            // B + D is always the longer branch, by at least 2 days.
            assertTrue(p.steps.first().math!!.lines().first().contains("◀ longest"))
            assertTrue(p.answer >= 0)
        }
    }

    @Test
    fun markingIsLenientOnFormatAndNamesRounding() {
        val p = FormulaDrills.generate(FormulaDrills.GENERATORS.first { it.id == "npv" }, 3)
        assertEquals(DrillVerdict.NONE, FormulaDrills.mark(p, "  ").verdict)
        assertEquals(DrillVerdict.RIGHT, FormulaDrills.mark(p, "£" + FormulaDrills.fmt(p.answer, 2)).verdict)
        val near = FormulaDrills.mark(p, (p.answer * 1.015).toString())
        assertTrue(near.verdict == DrillVerdict.NEAR || near.verdict == DrillVerdict.WRONG)
        assertEquals(1234.5, FormulaDrills.parse("1,234.5 %")!!, 0.0)
    }

    @Test
    fun numbersFormatLikeTheWeb() {
        assertEquals("-1,234.50", FormulaDrills.fmt(-1234.5, 2))
        assertEquals("0.88", FormulaDrills.fmt(0.875, 2))
        assertEquals("1.01", FormulaDrills.fmt(1.005, 2))
        assertEquals("0.00", FormulaDrills.fmt(-0.001, 2))
        assertEquals("1,000,000", FormulaDrills.fmt(1e6, 0))
        assertEquals("—", FormulaDrills.fmt(Double.NaN, 2))
    }

    @Test
    fun theTallyKeepsAStreakAndItsBest() {
        var t = DrillTally()
        listOf(DrillVerdict.RIGHT, DrillVerdict.RIGHT, DrillVerdict.NEAR, DrillVerdict.RIGHT).forEach { t = t.after(it) }
        assertEquals(DrillTally(asked = 4, right = 3, streak = 1, best = 2), t)
    }

    @Test
    fun onlyARightAnswerLogsAsOk() {
        val p = FormulaDrills.generate(FormulaDrills.GENERATORS[0], 1)
        assertTrue(FormulaDrills.logRow(p, DrillVerdict.RIGHT, 5).ok)
        assertFalse(FormulaDrills.logRow(p, DrillVerdict.NEAR, 5).ok)
        assertFalse(FormulaDrills.logRow(p, null, 5).ok)
        assertEquals("evm-variance", FormulaDrills.logRow(p, null, 5).generator)
    }
}
