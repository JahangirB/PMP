package com.jahangirb.pmpprep.data

import java.util.Calendar
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/*
 * The guided path. Nothing here is a stored calendar: every build starts from
 * what the person still needs (lessons not done, tasks not mastered, mocks not
 * sat) and the study time left before the exam, so skipped days are absorbed
 * rather than piling up, and results change what comes next.
 */

enum class Mastery { NOT_STARTED, LEARNING, SHAKY, MASTERED, FADING }

data class TaskState(
    val task: EcoTask, val answers: Int, val recentAccuracy: Double, val lastAt: Long, val mastery: Mastery,
    /** Median seconds per question over the same recent answers; null without timed answers. */
    val medianSec: Double? = null,
)

enum class SessionKind { REENTRY, REMEDIATION, FOUNDATION, BUILD, DRILL, REVIEW, CHECKPOINT, SKILLS, MIXED, MOCK, TAPER }

enum class StepKind { CARDS, LESSON, PRACTICE, REFLECT, SCENARIOS, MOCK, DEBRIEF }

/** count: questions or scenarios; for PRACTICE, easy = drawn from difficulty 1–3 only. */
data class Step(
    val kind: StepKind, val count: Int = 0, val timed: Boolean = false, val easy: Boolean = false,
    /** Reading drill: each stem is shown alone first, and you say what it's asking before the options appear. */
    val reading: Boolean = false,
)

data class Session(
    /** Stable for the same piece of work across rebuilds, e.g. "build:people-3". */
    val key: String,
    val kind: SessionKind,
    val title: String,
    val why: String,
    val minutes: Int,
    val steps: List<Step>,
    val taskId: String? = null,
    val domain: Domain? = null,
    val lessonId: String? = null,
    /** Start of the study day this is planned for. */
    val day: Long = 0L,
    internal val priority: Double = 0.0,
    internal val notBefore: Long = 0L,
)

data class SessionResult(val key: String, val kind: SessionKind, val taskId: String?, val domain: Domain?, val score: Double, val minutes: Int, val at: Long)

data class PlanSettings(val examDate: Long, val hoursPerWeek: Int, val studyDays: Set<Int>, val byArea: Boolean = false)

data class Plan(
    val sessions: List<Session>,
    val phase: String,
    val daysLeft: Int,
    val minutesPerDay: Int,
    val neededMinutes: Int,
    val availableMinutes: Int,
    /** True when the work left does not fit the time left even after trimming. */
    val behind: Boolean,
    val dropped: List<String>,
    val adjustments: List<String>,
    val tasks: List<TaskState>,
) {
    val next: Session? get() = sessions.firstOrNull()
}

class PlanInput(
    val x: Extras,
    val attempts: List<Attempt>,
    val lessonsDone: Set<String>,
    val results: List<SessionResult>,
    val mocks: List<MockResult>,
    val diagnostic: DiagnosticResult?,
    val settings: PlanSettings,
    val now: Long,
    /** The cause you tagged on each missed question (question id → cause). */
    val mistakeTags: Map<String, RootCause> = emptyMap(),
    /** ECO task id → how many of its flashcards are leeches (forgotten 6+ times). */
    val leeches: Map<String, Int> = emptyMap(),
    /** Exam fitness: the next rung of the stamina ladder (questions in one sitting), or null when done. */
    val ladderNext: Int? = null,
    /** Exam fitness: reading pace is below what the exam needs. */
    val readingSlow: Boolean = false,
)

object PlanEngine {
    const val DAY = Srs.DAY_MS
    /* The mastery bar and the re-entry gap are shared rules: see Extras.rules (MasteryRule, Rules.reentryDays). */
    const val PASS = 0.70
    const val TAPER_DAYS = 7
    /** A mastered task answered slower than this (median seconds) gets a paced drill. On the day it's about 80. */
    const val SLOW_SEC = 110.0
    /** A passed domain is re-checked when its answers since fall below this: under the pass mark with a margin, so it doesn't flip-flop. */
    const val RECHECK_BELOW = 0.65
    /** Tagged misses on a task before its most common cause changes the task's sessions. */
    const val CAUSE_MIN = 2

    fun startOfDay(t: Long) = Srs.startOfDay(t)

    /** Next calendar day, correct across daylight-saving changes. */
    fun nextDay(d: Long) = startOfDay(d + DAY + DAY / 2)

    private fun weekday(d: Long) = Calendar.getInstance().apply { timeInMillis = d }.get(Calendar.DAY_OF_WEEK)

    private fun pct(v: Double) = "${(v * 100).roundToInt()}%"

    fun taskStates(x: Extras, attempts: List<Attempt>, now: Long): List<TaskState> {
        val rule = x.rules.mastery
        val byTask = attempts.filter { it.source != Readiness.SOURCE_LESSON }.groupBy { it.ecoTask }
        return x.tasks.map { t ->
            // A right answer you called a guess, or got with a hint, isn't evidence you know it.
            val list = byTask[t.id].orEmpty().filter { !it.luckyGuess && !it.hinted }.sortedBy { it.at }
            val recent = list.takeLast(rule.window)
            val acc = if (recent.isEmpty()) 0.0 else recent.sumOf { it.credit } / recent.size
            val last = list.lastOrNull()?.at ?: 0L
            val m = when {
                list.isEmpty() -> Mastery.NOT_STARTED
                list.size < rule.minAnswers -> Mastery.LEARNING
                acc < rule.accuracy -> Mastery.SHAKY
                now - last > rule.fadeDays * DAY -> Mastery.FADING
                else -> Mastery.MASTERED
            }
            val secs = recent.filter { it.ms > 500 }.map { it.ms / 1000.0 }.sorted()
            TaskState(t, list.size, acc, last, m, if (secs.isEmpty()) null else secs[secs.size / 2])
        }
    }

    fun build(inp: PlanInput): Plan {
        val x = inp.x
        val now = inp.now
        val today = startOfDay(now)
        val exam = startOfDay(inp.settings.examDate)
        val studyDays = inp.settings.studyDays.ifEmpty { (1..7).toSet() }
        val perDay = max(15, inp.settings.hoursPerWeek * 60 / studyDays.size)
        val tasks = taskStates(x, inp.attempts, now)
        val adjustments = ArrayList<String>()

        /* ── how weak each domain is: practice once there is enough, else the diagnostic ── */
        val domainAcc = Domain.entries.associateWith { d ->
            val a = inp.attempts.filter { it.domain == d && it.source != Readiness.SOURCE_LESSON }
            if (a.size >= 30) a.sumOf { it.credit } / a.size else inp.diagnostic?.domainPct(d) ?: 0.6
        }
        fun taskPriority(s: TaskState): Double {
            val acc = if (s.answers >= 5) s.recentAccuracy else domainAcc[s.task.domain] ?: 0.6
            return (x.domainWeights[s.task.domain] ?: 0.3) * (1.0 - acc.coerceIn(0.0, 0.95)) * 10
        }

        val items = ArrayList<Session>()
        val lessonsByTask = x.lessons.filter { it.ecoTask.isNotBlank() }.associateBy { it.ecoTask }
        val lastResult = inp.results.groupBy { it.taskId }.mapValues { (_, l) -> l.maxByOrNull { it.at }!! }
        val worked = tasks.filter { s -> inp.results.any { it.taskId == s.task.id && startOfDay(it.at) == today } }.map { it.task.id }.toSet()

        /* ── 1. coming back after a gap ── */
        val lastActivity = listOfNotNull(inp.attempts.maxOfOrNull { it.at }, inp.results.maxOfOrNull { it.at }, inp.mocks.maxOfOrNull { it.at }).maxOrNull()
        val gapDays = if (lastActivity == null) 0 else ((today - startOfDay(lastActivity)) / DAY).toInt()
        if (gapDays >= x.rules.reentryDays) {
            items += Session("reentry:${startOfDay(lastActivity!!)}", SessionKind.REENTRY, "Welcome back: warm-up",
                "Your last study was $gapDays days ago. Due cards and 10 questions on what you learned last, before anything new.",
                25, listOf(Step(StepKind.CARDS), Step(StepKind.PRACTICE, 10), Step(StepKind.DEBRIEF)), priority = 1000.0)
            adjustments += "Re-entry warm-up added: your last study was $gapDays days ago."
        }

        /* ── causes you tagged on misses, per task: the most common one shapes that task's sessions ── */
        val taskOfQuestion = inp.attempts.associate { it.qid to it.ecoTask }
        val leadCause: Map<String, RootCause> = inp.mistakeTags.entries
            .mapNotNull { (qid, cause) -> taskOfQuestion[qid]?.let { it to cause } }
            .groupBy({ it.first }, { it.second })
            .mapNotNull { (task, causes) -> causes.groupingBy { it }.eachCount().maxByOrNull { it.value }?.takeIf { it.value >= CAUSE_MIN }?.let { task to it.key } }
            .toMap()
        /** Adds what the cause calls for: the lesson for a knowledge gap, scenarios for mindset, a pointer otherwise. */
        fun shaped(base: Session, lesson: Lesson?): Session {
            val cause = base.taskId?.let { leadCause[it] } ?: return base
            val label = base.title.substringAfter(": ")
            return when (cause) {
                RootCause.KNOWLEDGE_GAP -> if (lesson == null || base.steps.any { it.kind == StepKind.LESSON }) base else base.copy(
                    steps = listOf(Step(StepKind.LESSON)) + base.steps, lessonId = lesson.id, minutes = base.minutes + lesson.minutes,
                    why = base.why + " Most misses you tagged here were knowledge gaps, so the lesson comes first.")
                RootCause.WRONG_MINDSET -> base.copy(
                    steps = base.steps.flatMap { if (it.kind == StepKind.REFLECT) listOf(Step(StepKind.SCENARIOS, 3), it) else listOf(it) },
                    minutes = base.minutes + 8,
                    why = base.why + " Most misses you tagged on $label were mindset, so three situational scenarios are added.")
                RootCause.MISREAD_STEM -> base.copy(why = base.why + " Most misses here were misread stems: read the last sentence first and look for FIRST, NEXT, NOT, BEST.")
                RootCause.TWO_PLAUSIBLE -> base.copy(why = base.why + " Most misses here came down to two plausible answers: before choosing, name what makes one better (sooner, more collaborative, within your authority).")
                RootCause.RUSHED -> base.copy(why = base.why + " Most misses here were rushed: read every option before choosing; the pace bar shows the time you have.")
                RootCause.CALCULATION -> base.copy(why = base.why + " Most misses here were calculation slips: write the formula before the numbers. The Formula lab has the sheet.")
            }
        }

        /* ── 2. per task: remediation, build, drills, reviews ── */
        for (s in tasks) {
            val t = s.task
            val res = lastResult[t.id]
            val pri = taskPriority(s)
            val lesson = lessonsByTask[t.id]
            val tomorrowIfWorked = if (t.id in worked) nextDay(today) else 0L
            if (res != null && res.score < 0.5 && now - res.at < 3 * DAY && res.kind != SessionKind.REMEDIATION) {
                items += shaped(Session("remediation:${t.id}:${res.at}", SessionKind.REMEDIATION, "Fix: ${t.label}",
                    "Your last session on ${t.label} scored ${pct(res.score)}. Re-read the lesson, then easier questions to rebuild it.",
                    30, listOfNotNull(lesson?.let { Step(StepKind.LESSON) }, Step(StepKind.PRACTICE, 10, easy = true), Step(StepKind.REFLECT), Step(StepKind.DEBRIEF)),
                    t.id, t.domain, lesson?.id, priority = 900.0), lesson)
                adjustments += "Fix-up session for ${t.label}: your last session there scored ${pct(res.score)}."
                continue
            }
            if (lesson != null && lesson.id !in inp.lessonsDone) {
                if (s.mastery == Mastery.MASTERED) {
                    adjustments += "${t.label}: already mastered through your own practice, so only the lesson stays (as reading)."
                }
                items += Session("build:${t.id}", SessionKind.BUILD, "${t.code} · ${t.label}",
                    when (s.mastery) {
                        Mastery.MASTERED -> "You already answer ${t.label} well; this is the lesson for completeness."
                        else -> "Learn it, check it, then practise it. " + (inp.diagnostic?.domainPct(t.domain)?.let { "${t.domain.title} scored ${pct(it)} in your diagnostic." } ?: "")
                    },
                    lesson.minutes + if (s.mastery == Mastery.MASTERED) 0 else 20,
                    if (s.mastery == Mastery.MASTERED) listOf(Step(StepKind.LESSON), Step(StepKind.DEBRIEF))
                    else listOf(Step(StepKind.CARDS), Step(StepKind.LESSON), Step(StepKind.PRACTICE, 10), Step(StepKind.REFLECT), Step(StepKind.DEBRIEF)),
                    t.id, t.domain, lesson.id, priority = pri + 5)
                continue
            }
            when (s.mastery) {
                Mastery.NOT_STARTED, Mastery.LEARNING, Mastery.SHAKY -> {
                    val drills = if (s.mastery == Mastery.SHAKY && s.recentAccuracy < 0.5) 2 else 1
                    repeat(drills) { i ->
                        items += shaped(Session("drill:${t.id}:$i", SessionKind.DRILL, "Drill: ${t.label}",
                            if (s.answers == 0) "No answers on ${t.label} yet." else "${t.label} is at ${pct(s.recentAccuracy)} over your last ${min(s.answers, x.rules.mastery.window)} answers; the bar is ${pct(x.rules.mastery.accuracy)}.",
                            25, listOf(Step(StepKind.PRACTICE, 15), Step(StepKind.REFLECT), Step(StepKind.DEBRIEF)),
                            t.id, t.domain, priority = pri - i, notBefore = max(tomorrowIfWorked, if (i > 0) nextDay(nextDay(today)) else 0L)), if (i == 0) lesson else null)
                    }
                    if (res != null && res.score in 0.5..0.799 && now - res.at < 3 * DAY) {
                        adjustments += "Review of ${t.label} spaced 2 days out: your last session there scored ${pct(res.score)}."
                    }
                }
                Mastery.FADING -> {
                    items += Session("review:${t.id}", SessionKind.REVIEW, "Review: ${t.label}",
                        "You had ${t.label} mastered, but it's been ${((now - s.lastAt) / DAY)} days. A short refresher keeps it.",
                        15, listOf(Step(StepKind.PRACTICE, 10), Step(StepKind.DEBRIEF)), t.id, t.domain, priority = pri, notBefore = tomorrowIfWorked)
                    adjustments += "${t.label} is fading (${(now - s.lastAt) / DAY} days since practice): review added."
                }
                Mastery.MASTERED -> if (inp.results.none { it.taskId == t.id }) adjustments += "${t.label}: mastered through your own practice, so no drill is planned."
            }
            // Accurate but slow: mastery shouldn't hide a pace problem the exam will punish.
            val median = s.medianSec
            if (s.mastery == Mastery.MASTERED && median != null && median > SLOW_SEC) {
                items += Session("pace:${t.id}", SessionKind.DRILL, "Paced drill: ${t.label}",
                    "You answer ${t.label} well (${pct(s.recentAccuracy)}) but slowly: a median of ${median.toInt()} s against about 80 s on the day. 15 questions with the pace bar in view.",
                    20, listOf(Step(StepKind.PRACTICE, 15, timed = true), Step(StepKind.DEBRIEF)), t.id, t.domain, priority = pri, notBefore = tomorrowIfWorked)
                adjustments += "${t.label}: accurate but slow (median ${median.toInt()} s), so a paced drill was added."
            }
            // Flashcards that keep slipping point back to the lesson behind them.
            val leech = inp.leeches[t.id] ?: 0
            val relearnKey = "relearn:${t.id}:$leech"
            if (leech > 0 && lesson != null && lesson.id in inp.lessonsDone && inp.results.none { it.key == relearnKey }) {
                items += Session(relearnKey, SessionKind.REVIEW, "Relearn: ${t.label}",
                    "${if (leech == 1) "A flashcard" else "$leech flashcards"} on ${t.label} keep slipping. Re-read the lesson, then your due cards.",
                    lesson.minutes + 5, listOf(Step(StepKind.LESSON), Step(StepKind.CARDS), Step(StepKind.DEBRIEF)), t.id, t.domain, lesson.id, priority = pri)
                adjustments += "${t.label}: flashcards keep slipping, so its lesson comes back as a review."
            }
            // A middling session result books a spaced review even after mastery is reached.
            if (s.mastery == Mastery.MASTERED && res != null && res.score in 0.5..0.799 && res.kind != SessionKind.REVIEW) {
                items += Session("review:${t.id}", SessionKind.REVIEW, "Review: ${t.label}",
                    "Your last session on ${t.label} scored ${pct(res.score)}. A spaced review makes it stick.",
                    15, listOf(Step(StepKind.PRACTICE, 10), Step(StepKind.DEBRIEF)), t.id, t.domain, priority = pri, notBefore = res.at + 2 * DAY)
            }
        }

        /* ── 3. foundations, checkpoints, exam skills, mocks ── */
        x.lessons.filter { it.module == "foundations" && it.id !in inp.lessonsDone }.forEachIndexed { i, l ->
            items += Session("lesson:${l.id}", SessionKind.FOUNDATION, l.title, "Foundations come first: how the exam works and the mindset it rewards.",
                l.minutes, listOf(Step(StepKind.LESSON), Step(StepKind.DEBRIEF)), lessonId = l.id, priority = if (i == 0) 800.0 else 60.0 - i)
        }
        for (d in Domain.entries) {
            val lastPass = inp.results.filter { it.kind == SessionKind.CHECKPOINT && it.domain == d && it.score >= PASS }.maxByOrNull { it.at }
            if (lastPass != null) {
                // Passed once, but has the domain slipped since? Your answers there since the pass decide.
                val since = inp.attempts.filter { it.domain == d && it.source != Readiness.SOURCE_LESSON && it.at > lastPass.at }.takeLast(30)
                val acc = if (since.isEmpty()) 1.0 else since.sumOf { it.credit } / since.size
                val finalWeek = inp.settings.examDate - now <= TAPER_DAYS * DAY
                if (since.size >= 20 && acc < RECHECK_BELOW && !finalWeek) {
                    items += Session("checkpoint:${d.id}:${lastPass.at}", SessionKind.CHECKPOINT, "Re-check: ${d.title}",
                        "Your ${d.title} answers since you passed its checkpoint average ${pct(acc)}, well under the ${pct(PASS)} pass mark. 20 mixed questions show whether it has slipped.",
                        35, listOf(Step(StepKind.PRACTICE, 20), Step(StepKind.REFLECT), Step(StepKind.DEBRIEF)), domain = d, priority = 4.0)
                    adjustments += "${d.title} has slipped to ${pct(acc)} since its checkpoint, so it will be re-checked."
                }
                continue
            }
            val failed = inp.results.filter { it.kind == SessionKind.CHECKPOINT && it.domain == d }.maxByOrNull { it.at }
            items += Session("checkpoint:${d.id}", SessionKind.CHECKPOINT, "Checkpoint: ${d.title}",
                if (failed != null) "Last checkpoint scored ${pct(failed.score)}; ${pct(PASS)} moves you on. Retest after the reviews." else "20 mixed ${d.title} questions. ${pct(PASS)} or more moves you on.",
                35, listOf(Step(StepKind.PRACTICE, 20), Step(StepKind.REFLECT), Step(StepKind.DEBRIEF)), domain = d, priority = 4.0,
                notBefore = if (failed != null) failed.at + DAY else 0L)
        }
        /* ── exam fitness: one stamina sitting and, if reading is slow, one reading drill a week ── */
        val week = today / (7 * DAY)
        fun doneThisWeek(prefix: String) = inp.results.any { it.key.startsWith(prefix) && now - it.at < 6 * DAY }
        inp.ladderNext?.takeIf { it < x.exam.totalItems && !doneThisWeek("ladder:") }?.let { n ->
            items += Session("ladder:$n:$week", SessionKind.MIXED, "Stamina sitting: $n questions",
                "The next rung of your stamina ladder: $n mixed questions at exam pace, in one go. You move up when your accuracy holds in the last quarter.",
                (n * 76 / 60) + 5, listOf(Step(StepKind.PRACTICE, n, timed = true), Step(StepKind.DEBRIEF)), priority = 3.5)
        }
        if (inp.readingSlow && !doneThisWeek("reading:")) {
            items += Session("reading:$week", SessionKind.SKILLS, "Reading drill",
                "Each question shows its scenario first. Say to yourself what it's asking, then reveal the options. It trains reading speed and calm on long questions.",
                15, listOf(Step(StepKind.PRACTICE, 10, reading = true), Step(StepKind.DEBRIEF)), priority = 3.0)
        }
        x.lessons.filter { it.module == "skills" && it.id !in inp.lessonsDone }.forEach { l ->
            items += Session("lesson:${l.id}", SessionKind.SKILLS, l.title, "Exam technique, once the content is in place.",
                l.minutes, listOf(Step(StepKind.LESSON), Step(StepKind.DEBRIEF)), lessonId = l.id, priority = 2.0)
        }

        /* ── 4. capacity ── */
        val taperStart = max(today, startOfDay(exam - TAPER_DAYS * DAY))
        val days = ArrayList<Long>()
        var d = today
        while (d < exam) { if (weekday(d) in studyDays) days += d; d = nextDay(d) }
        val doneToday = inp.results.filter { startOfDay(it.at) == today }.sumOf { it.minutes }
        fun cap(day: Long) = if (day == today) max(0, perDay - doneToday) else perDay
        val buildDays = days.filter { it < taperStart }
        val available = buildDays.sumOf { cap(it) }

        val weeksLeft = ((exam - today) / (7 * DAY)).toInt()
        val recentMocks = inp.mocks.count { now - it.at < 45 * DAY }
        val mockTarget = if (weeksLeft >= 4) 3 else 2
        var mocksNeeded = max(0, mockTarget - recentMocks)
        val mockMinimum = max(0, 2 - recentMocks)

        var needed = items.sumOf { it.minutes } + mocksNeeded * perDay
        val dropped = ArrayList<String>()
        if (needed > available) {
            // Trim the least valuable work first: second drills, then reviews, extra foundations, skills, extra mocks.
            val order = items.filter { it.kind in setOf(SessionKind.DRILL, SessionKind.REVIEW, SessionKind.FOUNDATION, SessionKind.SKILLS) && it.priority < 500 }
                .sortedWith(compareBy({ dropRank(it.kind, it.key) }, { it.priority }))
            for (s in order) {
                if (needed <= available) break
                items.remove(s); needed -= s.minutes; dropped += s.title
            }
            while (needed > available && mocksNeeded > mockMinimum) { mocksNeeded--; needed -= perDay; dropped += "A full mock exam" }
            if (dropped.isNotEmpty()) adjustments += "Time is short, so ${dropped.size} lower-value sessions were trimmed."
        }
        val behind = needed > available

        /* ── 5. order: urgent, foundations, weakest domains first, checkpoints closing each domain ── */
        val domainOrder = Domain.entries.sortedBy { domainAcc[it] ?: 0.6 }
        /* By knowledge area, lessons' build sessions come in the classroom order, ahead of the drills and
           checkpoints, as a course teaches every lecture before its practice stage. */
        val course = if (inp.settings.byArea) x.courseOrder else emptyList()
        fun orderKey(s: Session): Double = if (s.kind == SessionKind.BUILD && s.lessonId in course) -400.0 + course.indexOf(s.lessonId) else when (s.kind) {
            SessionKind.REENTRY, SessionKind.REMEDIATION -> -1000.0 - s.priority
            SessionKind.FOUNDATION -> if (s.priority >= 800) -900.0 else -500.0 - s.priority
            SessionKind.BUILD, SessionKind.DRILL, SessionKind.REVIEW ->
                domainOrder.indexOf(s.domain) * 100.0 - s.priority - (if (s.kind == SessionKind.BUILD) 50 else 0)
            SessionKind.CHECKPOINT -> domainOrder.indexOf(s.domain) * 100.0 + 90
            SessionKind.SKILLS -> 400.0 - s.priority
            SessionKind.MIXED -> if (s.key.startsWith("ladder:")) 300.0 else 500.0
            else -> 500.0
        }
        val queue = items.sortedBy { orderKey(it) }.toMutableList()

        /* ── 6. lay the queue onto study days, then mocks and mixed practice in what's left ── */
        val planned = ArrayList<Session>()
        val used = HashMap<Long, Int>()
        for (day in buildDays) {
            var i = 0
            while (i < queue.size && (used[day] ?: 0) < cap(day)) {
                val s = queue[i]
                if (s.notBefore > day) { i++; continue }
                planned += s.copy(day = day); used[day] = (used[day] ?: 0) + s.minutes
                queue.removeAt(i)
            }
        }
        // Anything that did not fit still shows, on the last build day, so nothing silently vanishes.
        val lastBuild = buildDays.lastOrNull() ?: today
        queue.forEach { planned += it.copy(day = max(lastBuild, it.notBefore.coerceAtMost(lastBuild))) }

        val free = buildDays.filter { (used[it] ?: 0) == 0 && cap(it) > 0 }
        val mockDays = if (mocksNeeded == 0) emptyList() else spread(free, mocksNeeded, lastBuild)
        mockDays.forEachIndexed { i, day ->
            planned += Session("mock:${recentMocks + i + 1}", SessionKind.MOCK, "Full mock exam ${recentMocks + i + 1}",
                "The only evidence you can hold accuracy for ${x.exam.minutes} minutes. Set aside the whole sitting.",
                x.exam.minutes + 30, listOf(Step(StepKind.MOCK), Step(StepKind.DEBRIEF)), day = day, priority = 3.0)
        }
        free.filter { it !in mockDays }.forEach { day ->
                planned += Session("mixed:$day", SessionKind.MIXED, "Mixed timed practice",
                    "Every domain mixed, against the clock, the way the exam asks.",
                    min(perDay, 45), listOf(Step(StepKind.CARDS), Step(StepKind.PRACTICE, 20, timed = true), Step(StepKind.SCENARIOS, 3), Step(StepKind.DEBRIEF)), day = day)
            }
        days.filter { it >= taperStart && it < startOfDay(exam - DAY) }.forEach { day ->
            planned += Session("taper:$day", SessionKind.TAPER, "Light review",
                "Final week: nothing new. Cards, a few mixed questions, then rest.",
                min(perDay, 30), listOf(Step(StepKind.CARDS), Step(StepKind.PRACTICE, 10), Step(StepKind.DEBRIEF)), day = day)
        }

        val sessions = planned.sortedWith(compareBy({ it.day }, { orderKey(it) }))
        val first = sessions.firstOrNull()
        val phase = when (first?.kind) {
            null -> "Exam day"
            SessionKind.FOUNDATION -> "Foundations"
            SessionKind.SKILLS, SessionKind.MOCK, SessionKind.MIXED -> "Exam readiness"
            SessionKind.TAPER -> "Final week"
            else -> "Build"
        }
        if (behind) adjustments += "Behind: about ${needed / 60} hours of work for ${available / 60} hours left. Add hours, a study day, or move the date."
        return Plan(sessions, phase, ((exam - today) / DAY).toInt(), perDay, needed, available, behind, dropped, adjustments, tasks)
    }

    private fun dropRank(k: SessionKind, key: String): Int = when {
        k == SessionKind.DRILL && key.endsWith(":1") -> 0
        k == SessionKind.REVIEW -> 1
        k == SessionKind.FOUNDATION -> 2
        k == SessionKind.SKILLS -> 3
        else -> 4
    }

    /** n days spread over the free days, the last as late as possible. */
    private fun spread(free: List<Long>, n: Int, fallback: Long): List<Long> {
        if (free.isEmpty()) return List(n) { fallback }
        if (free.size <= n) return free + List(n - free.size) { free.last() }
        return (1..n).map { i -> free[((free.size - 1) * i.toDouble() / n).roundToInt()] }.distinct()
    }
}
