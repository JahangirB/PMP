package com.jahangirb.pmpprep.data

/** One rung of the ladder: the line that introduces it, the material it shows, and an option to cross out (rung 3). */
data class Hint(val rung: Int, val text: String, val detail: String?, val strike: String? = null)

/**
 * The coach's hint ladder (index.html §10.16.10), offered in practice only: never in a mock, the
 * diagnostic, a timed drill or a reading drill, because those are measurements and the exam gives
 * no hints. Three rungs, each taken from the question or its lesson, none naming the answer:
 *  1. the lesson's lens for the question's delivery approach,
 *  2. the question's own mindset sentence,
 *  3. one wrong option crossed out with its rationale, always one the person has not chosen, so the
 *     ladder never says whether their pick is right. A type with no options to strike gets a trap
 *     from the lesson on the task instead.
 * An answer given after a hint is recorded with `h` = rungs used, which keeps it out of accuracy,
 * pace and mastery (docs/progress-format.md §2), the same as on the web.
 */
object HintLadder {
    const val RUNGS = 3

    /** Shown once, above the first hint. */
    const val NOTE = "Hints keep the thinking yours. An answer given after one is kept out of your accuracy and pace."

    /**
     * The lesson the web's PMP.Learn.forTask() returns: the first in the lesson list that teaches the
     * task inside a domain module (the foundation modules have no domain and teach no single task).
     */
    fun lessonFor(lessons: List<Lesson>, modules: List<LessonModule>, ecoTask: String): Lesson? {
        if (ecoTask.isEmpty()) return null
        val withDomain = modules.filter { it.domain != null }.map { it.id }.toSet()
        return lessons.firstOrNull { it.ecoTask == ecoTask && it.module in withDomain }
    }

    /**
     * The hint at [rung] (1–3) for [q]. [shown] is the options in the order on screen, so the
     * option struck is the first wrong one the person can see; [chosen] is what they have picked so far.
     */
    fun hint(q: Question, rung: Int, shown: List<Option>, chosen: Set<String>, lesson: Lesson?): Hint {
        if (rung <= 1) {
            val ap = q.approach
            return if (ap != null) Hint(1,
                "Read the stem through the ${ap.id} lens. What would that kind of team usually do first?",
                lesson?.lenses?.get(ap.id)?.takeIf { it.isNotBlank() })
            else Hint(1, "Read the stem for the role you hold and the stage the work is at. What comes first from there?", null)
        }
        val mindset = q.mindset.takeIf { it.isNotBlank() }
        if (rung == 2) return Hint(2, "The principle this turns on:", mindset)
        if (q.base == QType.SINGLE || q.base == QType.MULTI) {
            val o = shown.firstOrNull { !it.correct && it.id !in chosen }
            if (o != null) return Hint(3, "One option you can rule out, and why:", o.rationale.takeIf { it.isNotBlank() }, o.id)
        }
        val trap = lesson?.traps?.firstOrNull()?.takeIf { it.isNotBlank() }
        if (trap != null) return Hint(3, "A trap the lesson on this task warns about:", trap)
        return Hint(3, "The principle this turns on:", mindset)
    }
}
