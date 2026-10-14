package com.jahangirb.pmpprep.data

import java.text.Normalizer
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/**
 * One answer to one question. choices holds the picked option or region ids
 * (single, multi, hotspot); map holds prompt → option (matching, drop-down)
 * or blank → typed text (fill in the blank).
 */
data class Answer(
    val choices: Set<String> = emptySet(),
    val map: Map<String, String> = emptyMap(),
)

data class ScoreResult(val credit: Double) {
    val correct: Boolean get() = credit >= 0.9999
    val partial: Boolean get() = credit > 0 && !correct
}

/**
 * The web app's scoring rules (§10.04.2), unchanged: credit runs 0–1 and an
 * item is correct only at full credit. On multiple-response and hotspot items
 * a wrong pick cancels a right one, so selecting everything scores zero.
 */
object Scoring {

    fun score(q: Question, a: Answer?): ScoreResult {
        val ans = a ?: Answer()
        return when (q.base) {
            QType.SINGLE -> {
                val right = q.options.firstOrNull { it.correct }?.id
                ScoreResult(if (right != null && ans.choices.size == 1 && ans.choices.first() == right) 1.0 else 0.0)
            }
            QType.MULTI, QType.HOTSPOT -> {
                val want = if (q.base == QType.MULTI) q.options.filter { it.correct }.map { it.id }.toSet()
                else q.regions.filter { it.correct }.map { it.id }.toSet()
                if (want.isEmpty()) return ScoreResult(0.0)
                val hits = ans.choices.count { it in want }
                val wrong = ans.choices.size - hits
                if (hits == want.size && wrong == 0) ScoreResult(1.0)
                else ScoreResult(clamp((hits - wrong).toDouble() / want.size))
            }
            QType.MATCHING -> {
                if (q.prompts.isEmpty()) return ScoreResult(0.0)
                val hit = q.prompts.count { p -> matchFor(q, p.id)?.let { it == ans.map[p.id] } == true }
                ScoreResult(hit.toDouble() / q.prompts.size)
            }
            QType.FILLBLANK -> {
                if (q.blanks.isEmpty()) return ScoreResult(0.0)
                val hit = q.blanks.count { blankIsRight(it, ans.map[it.id]) }
                ScoreResult(hit.toDouble() / q.blanks.size)
            }
            else -> ScoreResult(0.0)
        }
    }

    fun isAnswered(q: Question, a: Answer?): Boolean {
        if (a == null) return false
        return when (q.base) {
            QType.SINGLE, QType.MULTI, QType.HOTSPOT -> a.choices.isNotEmpty()
            QType.MATCHING -> q.prompts.all { !a.map[it.id].isNullOrBlank() }
            QType.FILLBLANK -> q.blanks.all { !a.map[it.id].isNullOrBlank() }
            else -> false
        }
    }

    /** The option that answers a matching prompt. */
    fun matchFor(q: Question, promptId: String): String? = q.options.firstOrNull { it.match == promptId }?.id

    fun blankIsRight(b: Blank, raw: String?): Boolean {
        if (raw.isNullOrBlank()) return false
        if (b.numeric) {
            val v = parseNumber(raw) ?: return false
            return abs(v - b.expect) <= b.tol + 1e-9
        }
        val got = normalise(raw)
        return b.accept.any { normalise(it) == got }
    }

    /** Accepts the things people type: thousands separators, currency signs, %, and (accounting) negatives. */
    fun parseNumber(s: String): Double? {
        var t = s.trim()
        if (t.isEmpty()) return null
        if (t.startsWith("(") && t.endsWith(")")) t = "-" + t.substring(1, t.length - 1)
        t = t.replace(Regex("[,\\s]"), "").replace(Regex("[$£€%]"), "")
        if (!t.matches(Regex("^[-+]?\\d*\\.?\\d+([eE][-+]?\\d+)?$"))) return null
        return t.toDoubleOrNull()
    }

    fun normalise(s: String): String {
        val t = Normalizer.normalize(s.lowercase(), Normalizer.Form.NFKD).replace(Regex("[\\u0300-\\u036f]"), "")
        return t.replace(Regex("[^a-z0-9 ]+"), " ").replace(Regex("\\s+"), " ").trim()
    }

    /** A blank's expected answer, written the way a person would read it. */
    fun blankAnswer(b: Blank): String {
        if (!b.numeric) return b.accept.firstOrNull().orEmpty()
        val n = formatNumber(b.expect)
        val withUnit = when {
            b.unit.matches(Regex("[$£€]")) -> b.unit + n
            b.unit.isEmpty() -> n
            else -> "$n ${b.unit}"
        }
        return if (b.tol > 0) "$withUnit (±${formatNumber(b.tol)})" else withUnit
    }

    fun formatNumber(v: Double): String =
        if (v == Math.rint(v) && abs(v) < 1e15) String.format("%,d", v.toLong())
        else String.format("%,.4f", v).trimEnd('0').trimEnd('.')

    private fun clamp(v: Double) = max(0.0, min(1.0, v))
}
