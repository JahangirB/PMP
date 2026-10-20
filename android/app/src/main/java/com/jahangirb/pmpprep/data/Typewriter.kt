package com.jahangirb.pmpprep.data

/**
 * A Gemini reply written out word by word (index.html §12.2.2 typedText/tick), though it arrives
 * whole: [next] is how far to show after [n], [shown] the text so far with an open ** or ``` closed.
 */
object Typewriter {
    const val FRAME_MS = 16L

    fun step(text: String): Int = maxOf(3, (text.length + 199) / 200)

    fun next(text: String, n: Int): Int {
        val from = n + step(text)
        if (from >= text.length) return text.length
        val ws = (from until text.length).firstOrNull { text[it].isWhitespace() }
        return ws ?: text.length
    }

    fun shown(text: String, n: Int): String {
        var out = text.take(n)
        if (Regex("\\*\\*").findAll(out).count() % 2 == 1) out += "**"
        if (Regex("```").findAll(out).count() % 2 == 1) out += "\n```"
        return out
    }
}
