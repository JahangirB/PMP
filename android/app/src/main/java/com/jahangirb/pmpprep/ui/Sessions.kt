package com.jahangirb.pmpprep.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.jahangirb.pmpprep.data.Answer
import com.jahangirb.pmpprep.data.Hint
import com.jahangirb.pmpprep.data.Option
import com.jahangirb.pmpprep.data.Question
import com.jahangirb.pmpprep.data.Readiness
import com.jahangirb.pmpprep.data.ScoreResult
import com.jahangirb.pmpprep.data.Scoring
import org.json.JSONArray
import org.json.JSONObject
import kotlin.random.Random

/** Option order is shuffled once per session; options marked fixed keep their place. */
fun shuffledOptions(q: Question, rnd: Random): List<Option> {
    val free = q.options.filter { !it.fixed }.shuffled(rnd).iterator()
    return q.options.map { if (it.fixed) it else free.next() }
}

/** A practice drill: answer, check, see why, move on. Also used to review a finished mock. */
class QuizSession(
    val title: String,
    val questions: List<Question>,
    val reviewOnly: Boolean = false,
    /** The cold level check: no feedback until the end, scored as one set. */
    val diagnostic: Boolean = false,
    /** For review: the answers given and the order they were shown in. */
    givenAnswers: Map<String, Answer> = emptyMap(),
    givenOrder: Map<String, List<Option>>? = null,
    /** Against the clock: answers count as timed for the pressure gap. */
    val timed: Boolean = false,
    /** Reading drill: the stem shows alone until you say you know what it's asking. */
    val readingDrill: Boolean = false,
    /** Where the answers are recorded as coming from. */
    val source: String = Readiness.SOURCE_PRACTICE,
    /** A mock review: the sitting's unscored pretest items, labelled as such. */
    val pretest: Set<String> = emptySet(),
) {
    /** Options crossed out, highlighted words of the stem, and how sure you were, per question. */
    val struck = mutableStateMapOf<String, Set<String>>()
    val marks = mutableStateMapOf<String, Set<Int>>()
    val confidence = mutableStateMapOf<String, Int>()
    /** The hint rungs opened per question, in order, and the option the third rung crossed out. */
    val hints = mutableStateMapOf<String, List<Hint>>()
    val hintStruck = mutableStateMapOf<String, String>()
    /**
     * The hint ladder is offered in practice only (index.html §10.02.6a): a review, the diagnostic, a timed
     * drill, a reading drill and a lesson's set are measurements, and the exam gives no hints.
     */
    val hintsAllowed: Boolean get() = !reviewOnly && !diagnostic && !timed && !readingDrill && source != Readiness.SOURCE_LESSON
    private val rnd = Random(System.nanoTime())
    val startedAt = System.currentTimeMillis()
    val key = "practice:$startedAt"

    /* How the current question is being handled, for Exam fitness. Times exclude time spent outside the app. */
    var firstTapAt = 0L
    var awayAtFirstTap = 0L
    var lastTouchAt = System.currentTimeMillis()
    var awayAtLastTouch = 0L
    var maxIdle = 0L
    var rightToWrong = 0
    var wrongToRight = 0
    /** Time spent outside the app while this question was showing, and in the whole set. */
    var awayMs = 0L
    var awayTotal = 0L
    /** Whole-set totals, for the debrief. */
    var totalRightToWrong by mutableIntStateOf(0)
    var totalWrongToRight by mutableIntStateOf(0)
    var optionsShown by mutableStateOf(!readingDrill)

    /** A touch: closes the idle stretch that ran since the last one. */
    fun touch(now: Long) {
        maxIdle = maxOf(maxIdle, now - lastTouchAt - (awayMs - awayAtLastTouch))
        lastTouchAt = now
        awayAtLastTouch = awayMs
    }

    /** A new question is on screen. */
    fun resetTracking(now: Long) {
        firstTapAt = 0L; awayAtFirstTap = 0L; lastTouchAt = now; awayAtLastTouch = 0L
        maxIdle = 0L; rightToWrong = 0; wrongToRight = 0; awayMs = 0L
        optionsShown = !readingDrill
    }
    val order: Map<String, List<Option>> = givenOrder ?: questions.associate { it.id to shuffledOptions(it, rnd) }
    var index by mutableIntStateOf(0)
    val answers = mutableStateMapOf<String, Answer>().apply { putAll(givenAnswers) }
    val results = mutableStateMapOf<String, ScoreResult>()
    var shownAt by mutableLongStateOf(System.currentTimeMillis())
    var finished by mutableStateOf(false)

    init {
        if (reviewOnly) questions.forEach { results[it.id] = Scoring.score(it, answers[it.id]) }
    }

    val current: Question get() = questions[index.coerceIn(0, questions.size - 1)]
    fun isChecked(q: Question) = results.containsKey(q.id)
    val checkedCount: Int get() = results.size
    val correctCount: Int get() = results.values.count { it.correct }
}

/**
 * A timed mock exam. Sections follow the layout in index.html §01.2: after
 * each break the earlier section is closed, as it is in the real exam.
 */
class MockSession(
    val title: String,
    val questions: List<Question>,
    val order: Map<String, List<Option>>,
    val durationMs: Long,
    /** Question counts at which a section ends and an optional break is offered. */
    val breakAfter: List<Int>,
    val breakMs: Long,
    /** Unscored pretest items (index.html §10.03.3): not told apart during the sitting, named in the results. */
    val pretest: Set<String> = emptySet(),
) {
    var index by mutableIntStateOf(0)
    var section by mutableIntStateOf(0)
    val answers = mutableStateMapOf<String, Answer>()
    val marked = mutableStateMapOf<String, Boolean>()
    val struck = mutableStateMapOf<String, Set<String>>()
    val marks = mutableStateMapOf<String, Set<Int>>()
    var remainingMs by mutableLongStateOf(durationMs)
    var onBreak by mutableStateOf(false)
    var breakRemainingMs by mutableLongStateOf(0L)
    var finished by mutableStateOf(false)
    var startedAt: Long = System.currentTimeMillis()
    /** Per question: time on screen, time to first answer, and answer changes (for Exam fitness). */
    val spentMs = HashMap<String, Long>()
    val firstTapMs = HashMap<String, Long>()
    val rightToWrong = HashMap<String, Int>()
    val wrongToRight = HashMap<String, Int>()

    val sectionCount: Int get() = breakAfter.size + 1
    fun sectionStart(s: Int): Int = if (s == 0) 0 else breakAfter[s - 1]
    fun sectionEnd(s: Int): Int = if (s >= breakAfter.size) questions.size else breakAfter[s]
    val inSection: IntRange get() = sectionStart(section) until sectionEnd(section)
    val current: Question get() = questions[index.coerceIn(0, questions.size - 1)]
    val answeredCount: Int get() = questions.count { Scoring.isAnswered(it, answers[it.id]) }

    fun toJson(): JSONObject = JSONObject().apply {
        put("title", title)
        put("ids", JSONArray(questions.map { it.id }))
        put("order", JSONObject().apply { order.forEach { (k, v) -> put(k, JSONArray(v.map { it.id })) } })
        put("answers", JSONObject().apply { answers.forEach { (k, v) -> put(k, answerJson(v)) } })
        put("marked", JSONArray(marked.filterValues { it }.keys.toList()))
        put("duration", durationMs); put("remaining", remainingMs)
        put("breakAfter", JSONArray(breakAfter)); put("breakMs", breakMs)
        put("index", index); put("section", section)
        put("onBreak", onBreak); put("breakRemaining", breakRemainingMs)
        put("startedAt", startedAt)
        put("pretest", JSONArray(pretest.toList()))
        put("spent", JSONObject(spentMs as Map<*, *>)); put("firstTap", JSONObject(firstTapMs as Map<*, *>))
        put("rw", JSONObject(rightToWrong as Map<*, *>)); put("wr", JSONObject(wrongToRight as Map<*, *>))
        put("struck", JSONObject().apply { struck.forEach { (k, v) -> put(k, JSONArray(v.toList())) } })
        put("marks", JSONObject().apply { marks.forEach { (k, v) -> put(k, JSONArray(v.toList())) } })
    }

    companion object {
        fun fromJson(o: JSONObject, byId: Map<String, Question>): MockSession? {
            val idsArr = o.optJSONArray("ids") ?: return null
            val qs = (0 until idsArr.length()).mapNotNull { byId[idsArr.optString(it)] }
            if (qs.isEmpty()) return null
            val ord = o.optJSONObject("order")
            val order = qs.associate { q ->
                val arr = ord?.optJSONArray(q.id)
                val ids = if (arr == null) emptyList() else (0 until arr.length()).map { arr.optString(it) }
                val byOpt = q.options.associateBy { it.id }
                q.id to (ids.mapNotNull { byOpt[it] }.takeIf { it.size == q.options.size } ?: q.options)
            }
            val ba = o.optJSONArray("breakAfter")
            val s = MockSession(
                o.optString("title", "Mock exam"), qs, order, o.optLong("duration"),
                if (ba == null) emptyList() else (0 until ba.length()).map { ba.optInt(it) }.filter { it in 1 until qs.size },
                o.optLong("breakMs"),
                o.optJSONArray("pretest")?.let { p -> (0 until p.length()).map { p.optString(it) }.toSet() } ?: emptySet(),
            )
            o.optJSONObject("answers")?.let { a -> a.keys().forEach { k -> s.answers[k] = answerFrom(a.optJSONObject(k)) } }
            o.optJSONArray("marked")?.let { m -> for (i in 0 until m.length()) s.marked[m.optString(i)] = true }
            s.remainingMs = o.optLong("remaining")
            s.section = o.optInt("section").coerceIn(0, s.sectionCount - 1)
            s.index = o.optInt("index").coerceIn(s.inSection.first, s.inSection.last)
            s.onBreak = o.optBoolean("onBreak")
            s.breakRemainingMs = o.optLong("breakRemaining")
            s.startedAt = o.optLong("startedAt", System.currentTimeMillis())
            o.optJSONObject("spent")?.let { j -> j.keys().forEach { s.spentMs[it] = j.optLong(it) } }
            o.optJSONObject("firstTap")?.let { j -> j.keys().forEach { s.firstTapMs[it] = j.optLong(it) } }
            o.optJSONObject("rw")?.let { j -> j.keys().forEach { s.rightToWrong[it] = j.optInt(it) } }
            o.optJSONObject("wr")?.let { j -> j.keys().forEach { s.wrongToRight[it] = j.optInt(it) } }
            o.optJSONObject("struck")?.let { j -> j.keys().forEach { k -> j.optJSONArray(k)?.let { a -> s.struck[k] = (0 until a.length()).map { a.optString(it) }.toSet() } } }
            o.optJSONObject("marks")?.let { j -> j.keys().forEach { k -> j.optJSONArray(k)?.let { a -> s.marks[k] = (0 until a.length()).map { a.optInt(it) }.toSet() } } }
            return s
        }

        fun answerJson(a: Answer): JSONObject = JSONObject().put("c", JSONArray(a.choices.toList())).put("m", JSONObject(a.map as Map<*, *>))

        fun answerFrom(o: JSONObject?): Answer {
            if (o == null) return Answer()
            val c = o.optJSONArray("c")
            val m = o.optJSONObject("m")
            return Answer(
                choices = if (c == null) emptySet() else (0 until c.length()).map { c.optString(it) }.toSet(),
                map = m?.keys()?.asSequence()?.associateWith { m.optString(it) } ?: emptyMap(),
            )
        }
    }
}
