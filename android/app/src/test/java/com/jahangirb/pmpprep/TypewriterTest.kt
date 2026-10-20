package com.jahangirb.pmpprep

import com.jahangirb.pmpprep.data.Typewriter
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/** The reply written out word by word (index.html §12.2.2, tests/10-ai-assist). */
class TypewriterTest {
    @Test fun wholeWordsAndClosedMarkup() {
        val text = "Your **weakest** area is scope. Review five questions today."
        var n = 0
        val cuts = ArrayList<Int>()
        while (n < text.length) { n = Typewriter.next(text, n); cuts += n }
        assertEquals(text.length, cuts.last())
        assertTrue(cuts.dropLast(1).all { text[it].isWhitespace() })
        assertEquals("Your **we**", Typewriter.shown(text, 9))
        assertEquals("a\n```\ncode\n```", Typewriter.shown("a\n```\ncode", 10))
    }
}
