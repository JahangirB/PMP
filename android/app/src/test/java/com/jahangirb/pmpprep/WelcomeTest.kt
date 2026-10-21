package com.jahangirb.pmpprep

import com.jahangirb.pmpprep.data.ContentParser
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

/** Before you start reaches the phone with Android's wording (index.html §05.13, tests/27-welcome). */
class WelcomeTest {
    private val json = File(System.getProperty("pmp.assets")!!, "content/extras.json").readText()
    private val w = ContentParser.extras(json).welcome!!

    @Test fun sixScreensOpeningWithTheRequestToReadCarefully() {
        assertEquals("Please read this carefully before you start", w.warn)
        assertEquals(5, w.sections.size)
        assertEquals(listOf(true, false, false, false, false), w.sections.map { it.main })
        assertEquals("Start with a fixed exam date", w.sections[0].h)
        assertTrue(w.sections.all { it.items.isNotEmpty() && it.items.none(String::isBlank) })
    }

    @Test fun platformLinesNameThisPhone() {
        val all = w.sections.flatMap { it.items } + w.again
        assertTrue(all.any { it.startsWith("Your progress is saved on this phone") })
        assertTrue(w.again.contains("Menu → Before you start"))
        assertTrue(all.none { it.contains("browser") || it.contains("iPhone") || it.contains("Settings and About") })
    }
}
