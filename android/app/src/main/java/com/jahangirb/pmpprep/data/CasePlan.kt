package com.jahangirb.pmpprep.data

import kotlin.math.floor
import kotlin.random.Random

/**
 * Where case studies go in a sitting, and which case a practice run uses. Pure, so it's tested on the JVM.
 *
 * The real exam opens with a case-study block. index.html §01.2 assumes it is EXAM.CASE_BLOCK_ITEMS (40)
 * of the 180 items, and §10.03.1 layoutFor() scales it for a shorter sitting: max(1, round(40 × n / 180)).
 * The block is filled with whole cases, so every item can show its passage; it is never shuffled into the
 * rest of the paper, and a case's items stay in their authored order. With no cases the block is empty and
 * the paper is exactly what it was before cases existed.
 */
object CasePlan {

    /** Case-block size for a sitting of [n] (index.html layoutFor): the full block at [total] or more, else scaled, at least 1, never more than [n]. */
    fun caseItemsFor(n: Int, total: Int = 180, caseBlock: Int = 40): Int {
        if (n <= 0 || caseBlock <= 0) return 0
        if (n >= total) return caseBlock.coerceAtMost(n)
        // JS Math.round: half rounds up.
        return floor(caseBlock.toDouble() * n / total + 0.5).toInt().coerceIn(1, n)
    }

    /** How many of a case's items have been answered before, per [seen]. */
    fun seenCount(c: CaseStudy, seen: (String) -> Boolean): Int = c.questions.count { seen(it.id) }

    /** Cases with the fewest items answered before first, ties in random order. */
    fun freshestFirst(cases: List<CaseStudy>, seen: (String) -> Boolean, rnd: Random = Random.Default): List<CaseStudy> =
        cases.shuffled(rnd).sortedBy { seenCount(it, seen) }

    /**
     * The case block: whole cases, freshest first, each taken while it still fits in [target]. If not even
     * one case fits whole (a short sitting), the opening items of the freshest case, so the block still
     * reads as one case. Returns the cases used with the items taken from each, in paper order.
     */
    fun pickCases(
        cases: List<CaseStudy>, target: Int, seen: (String) -> Boolean = { false }, rnd: Random = Random.Default,
    ): List<Pair<CaseStudy, List<Question>>> {
        if (target <= 0) return emptyList()
        val order = freshestFirst(cases.filter { it.questions.isNotEmpty() }, seen, rnd)
        if (order.isEmpty()) return emptyList()
        val out = ArrayList<Pair<CaseStudy, List<Question>>>()
        var used = 0
        for (c in order) {
            if (used + c.questions.size <= target) {
                out += c to c.questions
                used += c.questions.size
            }
        }
        if (out.isEmpty()) {
            val c = order.first()
            out += c to c.questions.take(target)
        }
        return out
    }

    /** The block's items in paper order. */
    fun block(picks: List<Pair<CaseStudy, List<Question>>>): List<Question> = picks.flatMap { it.second }

    /** The whole paper: the case block first, then the rest, with no item twice. */
    fun paper(caseBlock: List<Question>, rest: List<Question>): List<Question> {
        val ids = caseBlock.map { it.id }.toHashSet()
        return caseBlock + rest.filter { it.id !in ids }
    }

    /** Question id to its case, for showing the passage above any case item wherever it appears. */
    fun index(cases: List<CaseStudy>): Map<String, CaseStudy> {
        val m = LinkedHashMap<String, CaseStudy>()
        for (c in cases) for (q in c.questions) if (q.id !in m) m[q.id] = c
        return m
    }

    /** The case a "Case studies" practice run should use: the one with fewest items answered before. */
    fun nextCase(cases: List<CaseStudy>, seen: (String) -> Boolean, rnd: Random = Random.Default): CaseStudy? =
        freshestFirst(cases.filter { it.questions.isNotEmpty() }, seen, rnd).firstOrNull()
}
