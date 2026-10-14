package com.jahangirb.pmpprep.data

import kotlin.math.roundToInt

/** Where a coach line or step sends you. */
enum class CoachAction(val label: String) {
    PLANNER("Open the planner"),
    DIAGNOSTIC("Take the diagnostic"),
    PRACTICE("Practise"),
    TASK("Practise this task"),
    MOCK("Open the mock exam"),
    LEARN("Open the lesson"),
    CARDS("Review cards"),
    SCENARIOS("Open scenarios"),
    JOURNAL("Open the journal"),
    FORMULAS("Formula sheet"),
    ANALYTICS("See analytics"),
}

data class CoachMessage(val id: String, val text: String, val priority: Int, val cooldownDays: Int, val action: CoachAction? = null, val arg: String? = null)

data class CoachStep(val title: String, val why: String, val action: CoachAction, val arg: String? = null, val done: Boolean = false)

data class CoachReport(
    val stage: String,
    val headline: String,
    val verdict: String,
    val strengths: List<String>,
    val improve: List<String>,
    val messages: List<CoachMessage>,
    val steps: List<CoachStep>,
)

/**
 * The coach. Everything it says offline is derived from the person's own
 * history with the rules the web app's coach uses (§10.16, PMP.COACH_MESSAGES),
 * in the same voice: plain, second person, no guilt and no pass prediction.
 * [snapshot] is the same picture written out for Gemini, so the AI coach
 * talks about real numbers rather than guessing.
 */
class Coach(
    private val x: Extras,
    private val pool: List<Question>,
    private val store: ProgressStore,
    private val readiness: Readiness,
) {
    private val day = Srs.DAY_MS

    private fun pct(v: Double) = "${(v * 100).roundToInt()}%"
    private fun plural(n: Int, w: String) = if (n == 1) "1 $w" else "$n ${w}s"

    fun daysToExam(now: Long): Int? = store.plan.examDate?.let { ((Srs.startOfDay(it) - Srs.startOfDay(now)).toDouble() / day).roundToInt() }

    private fun lastActivity(): Long = listOfNotNull(
        store.attempts.maxOfOrNull { it.at }, store.mocks.maxOfOrNull { it.at },
        store.scenarioRuns.maxOfOrNull { it.at }, store.cards.values.maxOfOrNull { it.last },
    ).maxOrNull() ?: 0L

    fun stage(now: Long): String {
        val d = daysToExam(now)
        if (d != null && d in 0..7) return "examweek"
        val last = lastActivity()
        if (last > 0 && now - last >= 5 * day) return "reentry"
        return "normal"
    }

    private fun leadCause(): Pair<RootCause, Int>? {
        val wrongNow = store.last.values.filter { !it.correct }.map { it.qid }.toSet()
        val tags = store.mistakeTags.filterKeys { it in wrongNow }.values.groupingBy { it.cause }.eachCount()
        if (tags.values.sum() < 3) return null
        return tags.maxByOrNull { it.value }?.toPair()
    }

    private fun nextLesson(): Lesson? {
        val order = x.modules.associate { it.id to it.order }
        return x.lessons.sortedWith(compareBy({ order[it.module] ?: 99 }, { it.order })).firstOrNull { it.id !in store.lessonsDone }
    }

    private fun cardsDue(now: Long): Int = x.flashcards.count { c -> store.cards[c.id]?.let { !it.isNew && it.due <= now } == true }

    private fun leeches(): Int = store.cards.values.count { it.lapses >= x.srs.leechThreshold }

    fun report(now: Long = System.currentTimeMillis()): CoachReport {
        val r = readiness.report(store, now)
        val st = stage(now)
        val days = daysToExam(now)
        val atts = store.attempts.filter { it.source != Readiness.SOURCE_LESSON }
        val byTask = readiness.group(atts) { it.ecoTask }
        val byDomain = readiness.group(atts) { it.domain.id }
        val (median, paceN) = readiness.medianPace(atts)
        val paceReady = paceN >= x.readiness.paceMinSample
        val untouched = x.tasks.count { (byTask[it.id]?.n ?: 0) == 0 }
        val wrongNow = store.last.values.filter { !it.correct }
        val untagged = wrongNow.count { it.qid !in store.mistakeTags }
        val lead = leadCause()
        val due = cardsDue(now)
        val diag = store.diagnostic
        val lastMockDays = store.mocks.maxOfOrNull { it.at }?.let { ((now - it) / day).toInt() }
        val next = nextLesson()

        /* ── how you're doing ── */
        val headline = when {
            atts.isEmpty() && diag == null -> "Let's find your starting point"
            r.score >= 75 -> "On course for the exam"
            r.score >= 60 -> "Building well"
            r.score >= 40 -> "Foundations forming"
            else -> "Early days"
        }
        val domainLine = Domain.entries.mapNotNull { d -> byDomain[d.id]?.takeIf { it.n >= 5 }?.let { "${d.short} ${pct(it.accuracy)}" } }
        val verdict = buildString {
            append("Readiness ${r.score} out of 100")
            append(if (r.provisional) " (provisional: ${r.gates.joinToString("; ")}). " else ". ")
            if (domainLine.isNotEmpty()) append("Accuracy by domain: ${domainLine.joinToString(", ")}. ")
            if (diag != null) append("Your diagnostic scored ${pct(diag.pct)}. ")
            store.mocks.maxByOrNull { it.at }?.let { append("Last mock ${pct(it.pct)}. ") }
            if (days != null && days >= 0) append("Exam in ${plural(days, "day")}.")
        }.trim()

        val strengths = ArrayList<String>()
        Domain.entries.forEach { d -> byDomain[d.id]?.let { if (it.n >= 10 && it.accuracy >= 0.75) strengths += "${d.title}: ${pct(it.accuracy)} over ${it.n} answers" } }
        x.tasks.mapNotNull { t -> byTask[t.id]?.takeIf { it.n >= 5 && it.accuracy >= 0.8 }?.let { t to it } }
            .sortedByDescending { it.second.accuracy }.take(3)
            .forEach { (t, g) -> strengths += "${t.code} ${t.label}: ${pct(g.accuracy)}" }
        if (paceReady && median != null && median >= x.readiness.paceBand[0] && median <= x.readiness.paceBand[1]) strengths += "Pace: ${median.roundToInt()}s a question, inside the healthy band"

        val weakTasks = x.tasks.mapNotNull { t -> byTask[t.id]?.takeIf { it.n >= 3 && it.accuracy < 0.65 }?.let { t to it } }
            .sortedBy { it.second.accuracy }
        val improve = ArrayList<String>()
        weakTasks.take(3).forEach { (t, g) -> improve += "${t.code} ${t.label}: ${pct(g.accuracy)} over ${g.n} answers" }
        if (untouched > 0 && atts.isNotEmpty()) improve += "${plural(untouched, "ECO task")} not practised yet"
        lead?.let { improve += "Most tagged misses are “${it.first.title}” (${it.second})" }
        if (paceReady && median != null && median > x.readiness.paceBand[1]) improve += "Pace: median ${median.roundToInt()}s against about ${x.exam.paceTargetSec}s on the day"
        if (paceReady && median != null && median < x.readiness.paceBand[0]) improve += "Pace: median ${median.roundToInt()}s is fast enough to be costing marks"
        if (store.mocks.isEmpty() && atts.size >= 100) improve += "No full mock exam yet"
        if (untagged >= 5) improve += "$untagged misses without a root cause"

        /* ── messages: PMP.COACH_MESSAGES, where the Android app has the evidence ── */
        val msgs = ArrayList<CoachMessage>()
        if (st == "reentry") msgs += CoachMessage("welcome_back", "Welcome back. Short session today: cards and ten mixed questions. Then see if the plan needs adjusting.", 100, 1, CoachAction.PRACTICE)
        if (st == "examweek" && days != null) msgs += CoachMessage("taper", "Exam in ${plural(days, "day")}. Nothing new now. Re-read your journal, skim the formula sheet, sleep.", 100, 1, CoachAction.FORMULAS)
        if (store.plan.examDate == null) msgs += CoachMessage("plan_offer", "There's no exam date yet. Setting one takes a minute and fits the work to the time you have.", 75, 2, CoachAction.PLANNER)
        if (diag == null) msgs += CoachMessage("diagnostic_first", "The plan is guessing where you're weak. A 25-question diagnostic, taken cold, lets it stop guessing.", 70, 3, CoachAction.DIAGNOSTIC)
        if (store.mocks.isEmpty() && ((days != null && days <= 42) || atts.size >= 300))
            msgs += CoachMessage("first_mock_due", "Time for a first full mock. It's the only evidence you can hold accuracy for ${x.exam.minutes / 60} hours. Block out a weekend morning.", 65, 5, CoachAction.MOCK)
        when (lead?.first) {
            RootCause.MISREAD_STEM -> msgs += CoachMessage("cause_misread", "Most of your misses are reading, not knowledge. Find the qualifier (first, next, best, except) before looking at the options.", 60, 7)
            RootCause.WRONG_MINDSET -> msgs += CoachMessage("cause_mindset", "Most misses are the right move at the wrong moment. Two scenario sets this week will help more than more questions.", 60, 7, CoachAction.SCENARIOS)
            RootCause.KNOWLEDGE_GAP -> msgs += CoachMessage("cause_knowledge", "Most misses are material you hadn't met. Lessons first, then the questions on them.", 60, 7, CoachAction.LEARN, next?.id)
            RootCause.CALCULATION -> msgs += CoachMessage("cause_calc", "Most misses are arithmetic. Work the Formula Lab drills until the formulas are automatic.", 60, 7, CoachAction.FORMULAS)
            RootCause.RUSHED -> msgs += CoachMessage("cause_rushed", "Most misses come from rushing. Read all four options before choosing, even when the first looks right.", 60, 7)
            null -> {}
        }
        if (paceReady && median != null && median < x.readiness.paceBand[0]) msgs += CoachMessage("pace_fast", "Your median is ${median.roundToInt()}s. Fast answers are missing marks, and you have the time to read the stem twice.", 55, 7)
        if (paceReady && median != null && median > x.readiness.paceBand[1]) msgs += CoachMessage("pace_slow", "Your median is ${median.roundToInt()}s against ${x.exam.paceTargetSec}s on the day. A timed drill twice a week closes it slowly, and that's normal.", 55, 7, CoachAction.PRACTICE)
        val fit = fitness(now)
        if (fit.momentumLow) msgs += CoachMessage("momentum", "Your study time has dipped compared with recent weeks. That's normal over a long preparation. Try a lighter week of minimum days: 10 cards or 10 questions keep the habit going without pressure.", 60, 3)
        if (fit.finding != null && fit.next != null) msgs += CoachMessage("fitness", "Exam fitness: ${fit.finding} ${fit.next}", 45, 5)
        if (due >= 20) msgs += CoachMessage("cards_due", "$due cards are waiting. Ten minutes now saves twenty later in the week.", 50, 2, CoachAction.CARDS)
        if (atts.size >= 50 && untouched > 0) msgs += CoachMessage("coverage_gap", (if (untouched == 1) "One task hasn't" else "$untouched tasks haven't") + " been touched yet. Coverage is worth more readiness per minute than anything else right now.", 50, 5, CoachAction.PRACTICE)
        if (untagged >= 5) msgs += CoachMessage("journal_waiting", "$untagged misses haven't got a cause yet. Tagging them is the cheapest insight on the board.", 45, 3, CoachAction.JOURNAL)
        if (lastMockDays != null && lastMockDays > x.readiness.mockStaleAfterDays) msgs += CoachMessage("mock_stale", "Your last mock was $lastMockDays days ago, so it's counting for less. Another one will show where you are now.", 45, 10, CoachAction.MOCK)
        if (leeches() >= 1) msgs += CoachMessage("leech", (if (leeches() == 1) "One card keeps" else "${leeches()} cards keep") + " slipping. Rewrite it in your own words or study the lesson behind it.", 40, 7, CoachAction.CARDS)
        if (r.provisional && atts.isNotEmpty()) msgs += CoachMessage("provisional", "Your readiness score is provisional for now: ${r.gates.joinToString("; ")}. It firms up on its own as you work.", 30, 7)
        val visible = msgs.filter { (store.coachSnoozed[it.id] ?: 0L) <= now }.sortedByDescending { it.priority }

        /* ── today's session ── */
        val today = Srs.startOfDay(now)
        val todayAtts = atts.filter { it.at >= today }
        val steps = ArrayList<CoachStep>()
        if (diag == null) steps += CoachStep("25-question diagnostic", "Taken cold, it shows where you stand in each domain so the plan can aim at the weakest.", CoachAction.DIAGNOSTIC)
        if (store.plan.examDate == null) steps += CoachStep("Set your exam date and weekly hours", "The plan is built around the time you really have.", CoachAction.PLANNER)
        val reviewedToday = store.cards.values.count { it.last >= today }
        if (due > 0 || reviewedToday > 0) steps += CoachStep(if (due > 0) "Flashcards: $due due" else "Flashcards", "Reviews first. They hold up the accuracy your score is built on, and they make a good warm-up.", CoachAction.CARDS, done = due == 0)
        if (st == "examweek") {
            steps += CoachStep("Skim the formula sheet", "Light work only: nothing new goes in now.", CoachAction.FORMULAS)
            steps += CoachStep("10 mixed questions", "Light mixed practice keeps you sharp without opening anything new.", CoachAction.PRACTICE, done = todayAtts.size >= 10)
        } else if (st == "reentry") {
            steps += CoachStep("10 mixed questions", "Ten mixed questions show where things stand without asking much of you.", CoachAction.PRACTICE, done = todayAtts.size >= 10)
        } else {
            if (next != null) {
                steps += CoachStep("Lesson ${next.id}: ${next.title}", "Teaching comes before the questions on it, so lessons take the front of the day.", CoachAction.LEARN, next.id)
            }
            // Coverage first, then the weakest task with evidence, then the diagnostic's weakest domain.
            val target = x.tasks.firstOrNull { (byTask[it.id]?.n ?: 0) < x.readiness.coverageTargetPerTask && pool.any { q -> q.ecoTask == it.id } }
                ?: weakTasks.firstOrNull()?.first
                ?: diag?.let { dg -> Domain.entries.minByOrNull { dg.domainPct(it) ?: 1.0 } }?.let { d -> x.tasks.filter { it.domain == d }.minByOrNull { byTask[it.id]?.accuracy ?: 0.0 } }
            if (target != null) {
                val n = todayAtts.count { it.ecoTask == target.id }
                steps += CoachStep("10 questions on ${target.label}", if ((byTask[target.id]?.n ?: 0) < x.readiness.coverageTargetPerTask)
                    "Every task gets a block until it has enough answers to count." else "This is where your answers are weakest, so it gets the time.",
                    CoachAction.TASK, target.id, n >= 10)
            }
            if (untagged >= 5) steps += CoachStep("Tag 5 misses in the journal", "Knowing why you missed is what turns practice into a plan.", CoachAction.JOURNAL)
            if (lead?.first == RootCause.WRONG_MINDSET) steps += CoachStep("5 situational scenarios", "Ordering the right moves is what situational items test, and it's the last skill to come good.", CoachAction.SCENARIOS)
            if (days != null && days in 8..42 && (lastMockDays == null || lastMockDays >= 7)) steps += CoachStep("Full mock exam this week", "A full sitting is the only evidence you can hold accuracy for the whole exam.", CoachAction.MOCK)
        }

        return CoachReport(st, headline, verdict, strengths, improve, visible, steps)
    }

    /**
     * The person's progress as plain text, sent with every message to Gemini so
     * its answers rest on real figures. Nothing personal: only study numbers.
     */
    private fun fitness(now: Long) = ExamFitness.report(FitnessInput(store.attempts, store.sittings, store.checkIns,
        store.sessionResults, store.plan.studyDays, store.plan.hoursPerWeek, now))

    fun snapshot(now: Long = System.currentTimeMillis()): String {
        val r = readiness.report(store, now)
        val atts = store.attempts.filter { it.source != Readiness.SOURCE_LESSON }
        val byTask = readiness.group(atts) { it.ecoTask }
        val byDomain = readiness.group(atts) { it.domain.id }
        val byApproach = readiness.group(atts) { it.approach?.id }
        val byType = readiness.group(atts) { it.type.base.id }
        val (median, paceN) = readiness.medianPace(atts)
        val streak = readiness.streak(store, now)
        val c = report(now)
        val sb = StringBuilder()
        sb.appendLine("Date: ${java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US).format(java.util.Date(now))}")
        sb.appendLine("Exam: ${x.exam.totalItems} questions in ${x.exam.minutes} minutes. Domain weights: " +
            Domain.entries.joinToString { "${it.title} ${pct(x.domainWeights[it] ?: 0.0)}" })
        val days = daysToExam(now)
        sb.appendLine("Exam date: " + (store.plan.examDate?.let { "in $days days" } ?: "not set") + "; planned study ${store.plan.hoursPerWeek} h/week")
        sb.appendLine("Readiness: ${r.score}/100" + if (r.provisional) " (provisional: ${r.gates.joinToString("; ")})" else "")
        val fit = fitness(now)
        sb.appendLine("Exam fitness (stamina, composure under the clock, consistency; behaviour-based, not a diagnosis): ${fit.score ?: "not enough evidence yet"}" +
            fit.parts.joinToString(prefix = " — ", separator = ", ") { p -> "${p.label} ${p.score?.let { (it * 100).toInt() } ?: "n/a"}" } +
            (fit.wpm?.let { "; reading about ${it.toInt()} words a minute (exam needs ~${ExamFitness.TARGET_WPM.toInt()})" } ?: "") +
            "; longest sitting ${fit.longest} questions" + (fit.worry?.let { "; self-reported exam worry $it/100" } ?: "") +
            (fit.finding?.let { ". Main finding: $it" } ?: ""))
        r.components.forEach { sb.appendLine("  - ${it.label} (${it.weight} pts): ${it.figure}, earning ${"%.1f".format(it.points)}") }
        store.diagnostic?.let { d ->
            sb.appendLine("Diagnostic: ${d.right}/${d.n} (${pct(d.pct)}); " + Domain.entries.joinToString { dm -> "${dm.short} " + (d.domainPct(dm)?.let(::pct) ?: "-") })
        } ?: sb.appendLine("Diagnostic: not taken")
        sb.appendLine("Answers recorded: ${atts.size}; distinct questions: ${store.last.size} of ${pool.size} in the bank; wrong last time: ${store.last.values.count { !it.correct }}")
        sb.appendLine("Accuracy by domain: " + Domain.entries.joinToString { d -> byDomain[d.id]?.let { "${d.short} ${pct(it.accuracy)} (n=${it.n})" } ?: "${d.short} none" })
        sb.appendLine("Accuracy by approach: " + Approach.entries.joinToString { a -> byApproach[a.id]?.let { "${a.title} ${pct(it.accuracy)} (n=${it.n})" } ?: "${a.title} none" })
        sb.appendLine("Accuracy by question type: " + listOf(QType.SINGLE, QType.MULTI, QType.MATCHING, QType.HOTSPOT, QType.FILLBLANK)
            .joinToString { t -> byType[t.id]?.let { "${t.title} ${pct(it.accuracy)} (n=${it.n})" } ?: "${t.title} none" })
        sb.appendLine("ECO tasks (code, label, accuracy, answers):")
        x.tasks.forEach { t -> sb.appendLine("  ${t.code} ${t.label}: " + (byTask[t.id]?.let { "${pct(it.accuracy)}, ${it.n}" } ?: "not practised")) }
        if (store.mocks.isEmpty()) sb.appendLine("Mock exams: none")
        else {
            sb.appendLine("Mock exams (newest first):")
            store.mocks.sortedByDescending { it.at }.take(5).forEach { m ->
                sb.appendLine("  ${((now - m.at) / Srs.DAY_MS)} days ago: ${m.right}/${m.n} (${pct(m.pct)}); " +
                    Domain.entries.joinToString { d -> m.domains[d]?.let { "${d.short} ${it.first}/${it.second}" } ?: "" })
            }
        }
        val causes = store.mistakeTags.values.groupingBy { it.cause.title }.eachCount()
        sb.appendLine("Tagged mistake causes: " + if (causes.isEmpty()) "none" else causes.entries.joinToString { "${it.key} ${it.value}" })
        sb.appendLine("Pace: " + (median?.let { "median ${it.roundToInt()}s over $paceN timed answers" } ?: "no timed answers") + "; target about ${x.exam.paceTargetSec}s")
        sb.appendLine("Flashcards: ${cardsDue(now)} due, ${x.flashcards.count { store.cards[it.id] == null }} never studied, ${leeches()} leeches, of ${x.flashcards.size}")
        sb.appendLine("Lessons done: ${store.lessonsDone.size} of ${x.lessons.size}" + (nextLesson()?.let { "; next: ${it.id} ${it.title}" } ?: ""))
        val sc = store.scenarioRuns
        sb.appendLine("Situational scenarios: ${sc.size} answered" + if (sc.isNotEmpty()) ", average credit ${pct(sc.sumOf { it.rank.credit.toDouble() } / sc.size)}" else "")
        sb.appendLine("Study days in the last ${streak.last14.size}: ${streak.activeInWindow}; current streak ${streak.current}")
        sb.appendLine("App coach's suggestions today: " + c.steps.joinToString { it.title + if (it.done) " (done)" else "" })
        return sb.toString()
    }

    companion object {
        const val SYSTEM = "You are the study coach inside a PMP exam preparation app (PMI's Project Management Professional certification). " +
            "You can see the learner's real progress in the snapshot below; base anything you say about their progress on those figures, " +
            "quote the numbers, and never invent results. When asked how they are doing, give an honest overall picture, what is going well, " +
            "and the two or three changes that would help most, tied to specific ECO tasks, question types or habits. Do not predict whether they will pass. " +
            "When they ask a PMP question, answer accurately and concisely, explain the reasoning the exam rewards rather than just the answer, " +
            "and reason the way PMI expects: collaborative, proactive, following the agreed process, servant leadership, and fitted to whether the work " +
            "is predictive, agile or hybrid. Point them to parts of the app when useful: Practice, Mock Exam, Learn, Flashcards, Situational Trainer, " +
            "Formula Lab, Reference, Mistake Journal, Study Planner. Use short paragraphs and short bullet lists, plain English, no tables. " +
            "If you are unsure of something, say so."
    }
}
