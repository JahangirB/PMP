package com.jahangirb.pmpprep

import com.jahangirb.pmpprep.data.ContentParser
import com.jahangirb.pmpprep.data.Extras
import com.jahangirb.pmpprep.data.TheoryBlock
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

/** Theory reaches the phone whole (index.html §05.12, tests/26-theory): every chapter, block and figure. */
class TheoryTest {
    private val x: Extras = ContentParser.extras(File(System.getProperty("pmp.assets")!!, "content/extras.json").readText())

    @Test fun everyChapterArrives() {
        val t = x.theory
        assertEquals(6, t.parts.size)
        assertEquals(26, t.planned)
        assertEquals(t.planned, t.chapters.size)
        assertEquals(listOf(1, 2, 3, 25, 26), listOf(0, 1, 2, 24, 25).map { t.numberOf(t.chapters[it]) })
        assertTrue(t.chapters.all { it.sections.isNotEmpty() && it.keyIdeas.isNotEmpty() && it.minutes > 0 })
        val blocks = t.chapters.flatMap { c -> c.sections.flatMap { it.blocks } }
        assertTrue(blocks.count { it is TheoryBlock.Fig } >= 26)
        assertTrue(blocks.any { it is TheoryBlock.Numbered } && blocks.any { it is TheoryBlock.Heading })
        assertTrue(blocks.filterIsInstance<TheoryBlock.Box>().map { it.kind }.toSet() == setOf("exam", "myth", "example"))
    }

    @Test fun abbreviationsAreSpelledOutOnce() {
        val text = x.theory.chapters[1].sections.flatMap { it.blocks }.filterIsInstance<TheoryBlock.Para>().joinToString(" ") { it.text }
        assertEquals(1, Regex("Project Management Institute \\(PMI\\)").findAll(text).count())
        // Spelled out in the source itself, so not expanded a second time.
        assertTrue(text.contains("**program evaluation and review technique**, PERT,") && !text.contains("(PERT)"))
    }
}
