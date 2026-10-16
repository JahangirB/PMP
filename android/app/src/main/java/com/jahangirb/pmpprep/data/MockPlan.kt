package com.jahangirb.pmpprep.data

import kotlin.math.floor
import kotlin.random.Random

/** One question of a finished sitting, as the results screen reads it. [block] is the section it sat in (0-based). */
data class MockRow(
    val qid: String, val block: Int, val pretest: Boolean, val answered: Boolean, val correct: Boolean,
    val flagged: Boolean, val ms: Long,
)

/** A group in the time table: how many, how many right, and the time they took. */
data class MockTimeGroup(val label: String, val n: Int, val right: Int, val ms: Long) {
    val accuracy: Double? get() = if (n == 0) null else right.toDouble() / n
    val avgSec: Long get() = if (n == 0) 0 else Math.round(ms / n / 1000.0)
}

/** "Where the time went" (index.html §10.03.12): answered inside and over pace, flagged, and each block. */
data class MockTimeReport(
    val paceSec: Int, val inside: MockTimeGroup, val over: MockTimeGroup, val flagged: MockTimeGroup,
    val blocks: List<MockTimeGroup>, val unanswered: Int, val avgSec: Long,
)

/**
 * The mock exam's paper and its results arithmetic (index.html §10.03): which items are unscored
 * pretest items, and how the time was spent. Pure, so it's tested on the JVM.
 *
 * The real exam has 180 items of which 170 are scored and 10 are unscored pretest items nobody can
 * tell apart. A shorter sitting keeps the proportion, as the web's layoutFor() does: the scored count
 * is 170 × n / 180 rounded half up (at least 1), and the rest are pretest. So 90 → 5, 45 → 2, 20 → 1.
 * Pretest items are not identified during the sitting, only in the results.
 */
object MockPlan {

    /** Scored items in a sitting of [n], given the full exam's [total] and [scored]. */
    fun scoredCount(n: Int, total: Int = 180, scored: Int = 170): Int {
        if (n <= 0) return 0
        if (n >= total) return scored.coerceAtMost(n)
        // JS Math.round: half rounds up.
        return floor(scored.toDouble() * n / total + 0.5).toInt().coerceIn(1, n)
    }

    /** Unscored pretest items in a sitting of [n]: never negative. */
    fun pretestCount(n: Int, total: Int = 180, scored: Int = 170): Int = (n - scoredCount(n, total, scored)).coerceAtLeast(0)

    /** Picks which of [ids] are pretest: a random [pretestCount] of them. */
    fun pickPretest(ids: List<String>, total: Int = 180, scored: Int = 170, rnd: Random = Random.Default): Set<String> =
        ids.shuffled(rnd).take(pretestCount(ids.size, total, scored)).toSet()

    /** The section (0-based) the item at [index] sits in, given where the breaks fall. */
    fun blockOf(index: Int, breakAfter: List<Int>): Int = breakAfter.count { index >= it }

    /**
     * The time analysis. Inside/over pace and flagged read every answered item, pretest included, as the
     * web does: the time was spent either way. Blocks read every item. [paceSec] is the exam pace (80 s).
     */
    fun timeReport(rows: List<MockRow>, paceSec: Int, blocks: Int): MockTimeReport {
        val answered = rows.filter { it.answered }
        val limit = paceSec * 1000L
        fun group(label: String, l: List<MockRow>) = MockTimeGroup(label, l.size, l.count { it.correct }, l.sumOf { it.ms })
        val nBlocks = maxOf(blocks, (rows.maxOfOrNull { it.block } ?: -1) + 1)
        return MockTimeReport(
            paceSec,
            group("Answered inside ${paceSec}s", answered.filter { it.ms <= limit }),
            group("Answered over ${paceSec}s", answered.filter { it.ms > limit }),
            group("Flagged for review", rows.filter { it.flagged }),
            (0 until nBlocks).map { b -> group("Block ${b + 1}", rows.filter { it.block == b }) }.filter { it.n > 0 },
            rows.size - answered.size,
            if (answered.isEmpty()) 0 else Math.round(answered.sumOf { it.ms } / answered.size / 1000.0),
        )
    }

    /** Adds [ids] to a drill [queue], keeping its order and skipping ones already in it. */
    fun enqueue(queue: List<String>, ids: List<String>): List<String> {
        val have = LinkedHashSet(queue)
        ids.forEach { have += it }
        return have.toList()
    }
}
