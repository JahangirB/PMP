package com.jahangirb.pmpprep.ui

import android.app.Application
import android.net.Uri
import android.provider.OpenableColumns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.jahangirb.pmpprep.data.Answer
import com.jahangirb.pmpprep.data.Approach
import com.jahangirb.pmpprep.data.Attempt
import com.jahangirb.pmpprep.data.CardState
import com.jahangirb.pmpprep.data.ChatTurn
import com.jahangirb.pmpprep.data.CloudSync
import com.jahangirb.pmpprep.data.Coach
import com.jahangirb.pmpprep.data.CoachAction
import com.jahangirb.pmpprep.data.DiagnosticResult
import com.jahangirb.pmpprep.data.Gemini
import com.jahangirb.pmpprep.data.Plan
import com.jahangirb.pmpprep.data.PlanEngine
import com.jahangirb.pmpprep.data.PlanInput
import com.jahangirb.pmpprep.data.PlanSettings
import com.jahangirb.pmpprep.data.Session
import com.jahangirb.pmpprep.data.SessionKind
import com.jahangirb.pmpprep.data.SessionResult
import com.jahangirb.pmpprep.data.Step
import com.jahangirb.pmpprep.data.StepKind
import com.jahangirb.pmpprep.data.ContentRepository
import com.jahangirb.pmpprep.data.Domain
import com.jahangirb.pmpprep.data.Flashcard
import com.jahangirb.pmpprep.data.Grade
import com.jahangirb.pmpprep.data.MockResult
import com.jahangirb.pmpprep.data.ProgressStore
import com.jahangirb.pmpprep.data.QType
import com.jahangirb.pmpprep.data.Question
import com.jahangirb.pmpprep.data.QuestionPack
import com.jahangirb.pmpprep.data.Readiness
import com.jahangirb.pmpprep.data.Scenario
import com.jahangirb.pmpprep.data.ScenarioRun
import com.jahangirb.pmpprep.data.Scoring
import com.jahangirb.pmpprep.data.Srs
import com.jahangirb.pmpprep.data.SyncMerge
import com.jahangirb.pmpprep.data.Support
import com.jahangirb.pmpprep.data.WidgetStats
import com.jahangirb.pmpprep.data.SittingEvent
import com.jahangirb.pmpprep.data.CheckIn
import com.jahangirb.pmpprep.data.ExamFitness
import com.jahangirb.pmpprep.data.FitnessInput
import com.jahangirb.pmpprep.data.FitnessReport
import com.jahangirb.pmpprep.data.WeeklyReports
import com.jahangirb.pmpprep.data.QuestionPicker
import com.jahangirb.pmpprep.data.Mastery
import com.jahangirb.pmpprep.data.Booking
import com.jahangirb.pmpprep.data.BookingReport
import com.jahangirb.pmpprep.data.Deck
import com.jahangirb.pmpprep.data.QuestionReview
import com.jahangirb.pmpprep.data.DrillProblem
import com.jahangirb.pmpprep.data.DrillVerdict
import com.jahangirb.pmpprep.data.FormulaDrills
import com.jahangirb.pmpprep.data.Hint
import com.jahangirb.pmpprep.data.HintLadder
import com.jahangirb.pmpprep.data.MockPlan
import com.jahangirb.pmpprep.data.CasePlan
import com.jahangirb.pmpprep.data.CaseStudy
import com.jahangirb.pmpprep.data.MockRow
import com.jahangirb.pmpprep.data.MockTimeReport
import com.jahangirb.pmpprep.ui.components.ReadingPrefs
import com.jahangirb.pmpprep.data.ReminderBrain
import com.jahangirb.pmpprep.data.ReminderSnapshot
import com.jahangirb.pmpprep.reminders.Reminders
import com.jahangirb.pmpprep.widget.PmpWidget
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.File
import kotlin.math.roundToInt
import kotlin.random.Random

enum class Dest(val title: String) {
    DASHBOARD("PMP Prep"),
    PRACTICE("Practice"),
    MOCK("Mock Exam"),
    SITUATIONAL("Situational Trainer"),
    LEARN("Learn"),
    FLASHCARDS("Flashcards"),
    FORMULAS("Formula Lab"),
    REFERENCE("ECO & Reference"),
    ANALYTICS("Analytics"),
    MISTAKES("Mistake Journal"),
    PLANNER("Study Planner"),
    DATA("Banks & Backup"),
    COACH("Coach and notes"),
    MENU("Menu"),
    GUIDE("How it works"),
    FITNESS("Exam fitness"),
    REPORTS("Weekly reports"),
    SESSION("Session"),
    SETTINGS("Study settings"),
    BOOK("Ready to book?"),
    SEARCH("Search"),
    SYNC("Cloud sync"),
    SUPPORT("Support the app"),
}

/** What to practise. Null means "any". */
data class PracticeFilter(
    val domain: Domain? = null,
    val task: String? = null,
    val pack: String? = null,
    val type: QType? = null,
    val approach: Approach? = null,
    val which: Which = Which.ALL,
    val count: Int = 20,
    /** Drawn toward your weakest ECO tasks (index.html §10.02 adaptive drill) rather than at random. */
    val adaptive: Boolean = false,
) {
    enum class Which(val title: String) {
        ALL("All questions"), NEW("Not answered yet"), MISSED("Wrong last time"), FLAGGED("Flagged"),
    }
}

class PmpViewModel(app: Application) : AndroidViewModel(app) {

    val repo = ContentRepository(app)
    val store = ProgressStore(app.filesDir)
    val revision = store.revision

    var ready by mutableStateOf(false)
        private set
    var loadError by mutableStateOf<String?>(null)
        private set
    /** Bumped when packs change, so screens that list questions recompute. */
    var contentVersion by mutableStateOf(0)
        private set

    lateinit var readiness: Readiness
        private set
    lateinit var srs: Srs
        private set

    init {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) { repo.load() }
                readiness = Readiness(repo.extras.readiness, repo.extras.exam, repo.extras.tasks, repo.extras.domainWeights)
                srs = Srs(repo.extras.srs)
                restoreMock()
                // Switch back a Do Not Disturb left on if the app was closed mid-mock; the exam screen turns it on again.
                Focus.end(app)
                if (mock == null) changeFocusMock(false)
                // The weekly check of which Gemini models are current.
                checkModels()
                ready = true
                // onStart came before content was loaded: count this open, then sync.
                appOpened()
                autoSync()
                // Keep the home-screen widget current: a second after progress settles.
                launch {
                    store.revision.collectLatest {
                        delay(1000)
                        runCatching { publishWeekly() }
                        runCatching { publishWidget() }
                        runCatching { publishReminders() }
                    }
                }
            } catch (e: Exception) {
                loadError = e.message ?: e.toString()
            }
        }
    }

    /* ── study settings ──────────────────────────────────────────────────── */

    private val settings = app.getSharedPreferences("settings", android.content.Context.MODE_PRIVATE)
    /** Ask "how sure?" before checking a practice answer. */
    var askConfidence by mutableStateOf(settings.getBoolean("confidence", true))
        private set
    var reading by mutableStateOf(ReadingPrefs(settings.getFloat("scale", 1f), settings.getBoolean("roomy", false), settings.getBoolean("guide", false)))
        private set
    /** Offer Do Not Disturb and keep the screen on when a mock starts. */
    var focusOffer by mutableStateOf(settings.getBoolean("focus", true))
        private set

    /** The running mock uses focus mode: Do Not Disturb while it's on screen, and the screen kept awake. */
    var focusMock by mutableStateOf(settings.getBoolean("focusMock", false))
        private set

    /** Offer the hint ladder in practice (index.html "Show hints in practice"); on by default, as on the web. */
    var showHints by mutableStateOf(settings.getBoolean("hints", true))
        private set

    fun changeShowHints(on: Boolean) { showHints = on; settings.edit().putBoolean("hints", on).apply() }

    fun changeFocusMock(on: Boolean) { focusMock = on; settings.edit().putBoolean("focusMock", on).apply() }

    fun changeAskConfidence(on: Boolean) { askConfidence = on; settings.edit().putBoolean("confidence", on).apply() }
    fun changeFocusOffer(on: Boolean) { focusOffer = on; settings.edit().putBoolean("focus", on).apply() }
    fun changeReading(r: ReadingPrefs) {
        reading = r
        settings.edit().putFloat("scale", r.scale).putBoolean("roomy", r.roomy).putBoolean("guide", r.guide).apply()
    }
    fun setRestDays(on: Boolean) = store.setPlan(store.plan.copy(restDays = on))

    /* ── navigation ──────────────────────────────────────────────────────── */

    var screen by mutableStateOf(Dest.DASHBOARD)
        private set
    private val backStack = ArrayList<Dest>()

    fun navigate(d: Dest) {
        if (d == screen) return
        backStack += screen
        screen = d
    }

    /** Back to the path, forgetting the trail. */
    fun home() {
        backStack.clear()
        screen = Dest.DASHBOARD
    }

    /** Returns false when there is nowhere to go back to. */
    fun back(): Boolean {
        // Inner screens first: a running drill, an open lesson, an open deck.
        when (screen) {
            Dest.PRACTICE -> if (practice != null) { practice = null; return true }
            Dest.LEARN -> if (openLesson != null) { openLesson = null; return true }
            Dest.FLASHCARDS -> if (cardQueue != null) { cardQueue = null; return true }
            Dest.MOCK -> if (mockReview != null) { mockReview = null; return true }
            else -> {}
        }
        if (backStack.isEmpty()) return false
        screen = backStack.removeAt(backStack.size - 1)
        return true
    }

    /* ── practice ────────────────────────────────────────────────────────── */

    var filter by mutableStateOf(PracticeFilter())
    private var practiceState by mutableStateOf<QuizSession?>(null)
    /** The practice set on screen. Replacing or clearing one part-way records it as left (Exam fitness). */
    var practice: QuizSession?
        get() = practiceState
        set(v) {
            val old = practiceState
            if (old != null && old !== v && !old.finished && !old.reviewOnly && !old.diagnostic && old.checkedCount > 0) {
                store.recordSitting(SittingEvent(old.key, "practice", System.currentTimeMillis(), old.questions.size, old.checkedCount, false))
            }
            practiceState = v
        }

    /** A practice set reached its end. */
    fun finishQuiz(s: QuizSession) {
        s.finished = true
        if (!s.reviewOnly && !s.diagnostic) store.recordSitting(SittingEvent(s.key, "practice", System.currentTimeMillis(), s.questions.size, s.checkedCount, true))
        autoSync()
    }

    fun pool(): List<Question> = repo.pool()

    fun matching(f: PracticeFilter): List<Question> = pool().filter { q ->
        (f.domain == null || q.domain == f.domain) &&
            (f.task == null || q.ecoTask == f.task) &&
            (f.pack == null || q.packId == f.pack) &&
            (f.type == null || q.base == f.type) &&
            (f.approach == null || q.approach == f.approach) &&
            when (f.which) {
                PracticeFilter.Which.ALL -> true
                PracticeFilter.Which.NEW -> q.id !in store.last
                PracticeFilter.Which.MISSED -> store.last[q.id]?.correct == false
                PracticeFilter.Which.FLAGGED -> q.id in store.flags
            }
    }

    fun startPractice(f: PracticeFilter = filter, title: String = "Practice") {
        val pool = matching(f)
        val qs = if (f.adaptive) QuestionPicker.adaptive(pool, store.attempts, { store.last[it]?.correct }, f.count, repo.extras.adaptive)
            else pool.shuffled().take(f.count)
        if (qs.isEmpty()) return
        practice = QuizSession(title, qs)
        if (screen != Dest.PRACTICE) navigate(Dest.PRACTICE)
    }

    /** "Case studies" quick start: one whole case, in authored order, the one with fewest items answered first unless [c] is given. */
    fun startCase(c: CaseStudy? = null) {
        val case = c ?: CasePlan.nextCase(repo.extras.cases, { it in store.last }) ?: return
        practice = QuizSession("Case: ${case.title}", case.questions)
        if (screen != Dest.PRACTICE) navigate(Dest.PRACTICE)
    }

    fun startPracticeWith(qs: List<Question>, title: String) {
        if (qs.isEmpty()) return
        practice = QuizSession(title, qs.shuffled())
        if (screen != Dest.PRACTICE) navigate(Dest.PRACTICE)
    }

    fun answer(s: QuizSession, q: Question, a: Answer) {
        if (s.isChecked(q)) return
        val now = System.currentTimeMillis()
        s.touch(now)
        if (s.firstTapAt == 0L) { s.firstTapAt = now; s.awayAtFirstTap = s.awayMs }
        changeDirection(q, s.answers[q.id], a)?.let { if (it) s.wrongToRight++ else s.rightToWrong++ }
        s.answers[q.id] = a
        // Choosing a crossed-out option brings it back.
        s.struck[q.id]?.let { st -> if (a.choices.any { it in st }) s.struck[q.id] = st - a.choices }
    }

    /** Crosses an option out, or restores it. Crossing out the chosen option unchooses it. */
    fun toggleStrike(s: QuizSession, q: Question, optionId: String) {
        if (s.isChecked(q)) return
        s.touch(System.currentTimeMillis())
        val st = s.struck[q.id].orEmpty()
        s.struck[q.id] = if (optionId in st) st - optionId else st + optionId
        s.answers[q.id]?.let { a -> if (optionId in a.choices && optionId !in st) s.answers[q.id] = a.copy(choices = a.choices - optionId) }
    }

    /** Whether the Hint button shows on this question: hints switched on, a practice set, not yet checked, rungs left. */
    fun canHint(s: QuizSession, q: Question): Boolean =
        showHints && s.hintsAllowed && !s.isChecked(q) && (s.hints[q.id]?.size ?: 0) < HintLadder.RUNGS

    /** Opens the next rung of the hint ladder. Rung 3 may cross out one wrong option the person hasn't chosen. */
    fun nextHint(s: QuizSession, q: Question) {
        if (!canHint(s, q)) return
        s.touch(System.currentTimeMillis())
        val have = s.hints[q.id].orEmpty()
        val lesson = HintLadder.lessonFor(repo.extras.lessons, repo.extras.modules, q.ecoTask)
        val h = HintLadder.hint(q, have.size + 1, s.order[q.id] ?: q.options, s.answers[q.id]?.choices.orEmpty(), lesson)
        s.hints[q.id] = have + h
        h.strike?.let { id ->
            s.hintStruck[q.id] = id
            s.struck[q.id] = s.struck[q.id].orEmpty() + id
        }
    }

    /** For pick-type questions: true if a change went wrong → right, false if right → wrong, null otherwise. */
    private fun changeDirection(q: Question, before: Answer?, after: Answer): Boolean? {
        if (q.base != QType.SINGLE && q.base != QType.MULTI && q.base != QType.HOTSPOT) return null
        if (before == null || !Scoring.isAnswered(q, before) || !Scoring.isAnswered(q, after)) return null
        val was = Scoring.score(q, before).correct
        val now = Scoring.score(q, after).correct
        return if (was == now) null else now
    }

    /** Reading drill: you've said what the question is asking; the options appear. */
    fun showOptions(s: QuizSession) {
        val now = System.currentTimeMillis()
        s.touch(now)
        if (s.firstTapAt == 0L) { s.firstTapAt = now; s.awayAtFirstTap = s.awayMs }
        s.optionsShown = true
    }

    /** [confidence] is [Attempt.SURE], [Attempt.THINK], [Attempt.GUESS], or 0 when not asked. */
    fun check(s: QuizSession, confidence: Int = 0) {
        val q = s.current
        if (s.isChecked(q) || !Scoring.isAnswered(q, s.answers[q.id])) return
        val r = Scoring.score(q, s.answers[q.id])
        if (confidence > 0) s.confidence[q.id] = confidence
        s.results[q.id] = r
        val now = System.currentTimeMillis()
        s.touch(now)
        s.totalRightToWrong += s.rightToWrong
        s.totalWrongToRight += s.wrongToRight
        store.record(attempt(q, r.credit, r.correct, now - s.shownAt - s.awayMs, s.source,
            firstTapMs = if (s.firstTapAt > 0) s.firstTapAt - s.shownAt - s.awayAtFirstTap else 0L,
            idleMs = s.maxIdle, rightToWrong = s.rightToWrong, wrongToRight = s.wrongToRight,
            words = wordsOf(q, withOptions = !s.readingDrill), timed = s.timed,
            // An option the hint crossed out wasn't the person's own elimination.
            confidence = confidence, eliminated = (s.struck[q.id].orEmpty() - setOfNotNull(s.hintStruck[q.id])).size,
            hints = s.hints[q.id]?.size ?: 0))
    }

    fun moveTo(s: QuizSession, i: Int) {
        s.index = i.coerceIn(0, s.questions.size - 1)
        s.shownAt = System.currentTimeMillis()
        s.resetTracking(s.shownAt)
    }

    private fun attempt(
        q: Question, credit: Double, correct: Boolean, ms: Long, source: String,
        firstTapMs: Long = 0, idleMs: Long = 0, rightToWrong: Int = 0, wrongToRight: Int = 0, words: Int = wordsOf(q), timed: Boolean = false,
        confidence: Int = 0, eliminated: Int = 0, hints: Int = 0,
    ) = Attempt(
        qid = q.id, ecoTask = q.ecoTask, domain = q.domain, approach = q.approach, type = q.type,
        credit = credit, correct = correct, ms = ms.coerceAtLeast(0), at = System.currentTimeMillis(), source = source,
        firstTapMs = firstTapMs.coerceAtLeast(0), idleMs = idleMs.coerceAtLeast(0), rightToWrong = rightToWrong,
        wrongToRight = wrongToRight, words = words, timed = timed, confidence = confidence, eliminated = eliminated,
        difficulty = q.difficulty.coerceIn(0, 5), hints = hints,
    )

    /** Accuracy with and without crossing options out first, over answers where it was possible; null until both have 10. */
    fun eliminationEffect(): Pair<Double, Double>? {
        val pick = store.attempts.filter { (it.type.base == QType.SINGLE || it.type.base == QType.MULTI) && it.source != Readiness.SOURCE_LESSON }.takeLast(400)
        val (with, without) = pick.partition { it.eliminated > 0 }
        if (with.size < 10 || without.size < 10) return null
        return with.count { it.correct }.toDouble() / with.size to without.count { it.correct }.toDouble() / without.size
    }

    /** Words to read: the stem, plus the options unless they're hidden (reading drill). */
    private fun wordsOf(q: Question, withOptions: Boolean = true): Int {
        val w = Regex("\\w+")
        return w.findAll(q.stem).count() + if (withOptions) q.options.sumOf { w.findAll(it.text).count() } else 0
    }

    /** No touch for this long on a practice question brings up the gentle "take a breath" prompt. */
    fun freezePromptMs(): Long {
        val times = store.attempts.takeLast(100).filter { it.ms > 500 && !it.timed }.map { it.ms }.sorted()
        val median = if (times.isEmpty()) 76_000L else times[times.size / 2]
        return maxOf(150_000L, 3 * median)
    }

    /* ── review of missed questions ──────────────────────────────────────── */

    private fun dueReviews(now: Long): List<Question> {
        val enabled = pool().associateBy { it.id }
        return QuestionReview.due(store.attempts, now).mapNotNull { enabled[it.qid] }
    }

    fun reviewDueCount(now: Long = System.currentTimeMillis()): Int = dueReviews(now).size

    /** For a warm-up: due reviews, within today's allowance. */
    private fun warmUpReviews(now: Long): List<Question> = dueReviews(now).take(QuestionReview.allowance(store.attempts, now))

    /** Every due review, from the practice screen. */
    fun startReview() {
        val qs = dueReviews(System.currentTimeMillis())
        if (qs.isEmpty()) return
        practice = QuizSession("Missed questions, back for review", qs, source = QuestionReview.SOURCE)
        if (screen != Dest.PRACTICE) navigate(Dest.PRACTICE)
    }

    /* ── mock exam ───────────────────────────────────────────────────────── */

    var mock by mutableStateOf<MockSession?>(null)
        private set
    var lastMock by mutableStateOf<MockResult?>(null)
        private set
    /** The finished sitting, kept for its review. */
    var finishedMock by mutableStateOf<MockSession?>(null)
        private set
    var mockReview by mutableStateOf<QuizSession?>(null)
    /** The finished sitting's "where the time went" analysis, and the questions it missed (pretest included). */
    var lastMockTime by mutableStateOf<MockTimeReport?>(null)
        private set
    var lastMockMissed by mutableStateOf<List<String>>(emptyList())
        private set
    private var timerJob: Job? = null
    private val mockFile: File get() = File(getApplication<Application>().filesDir, "exam.json")

    /**
     * Draws a mock: the case-study block first (whole cases, sized as index.html's layoutFor() sizes it for
     * a sitting of [n]; empty when there are no cases), then the rest weighted to the domain split, unseen first.
     */
    fun buildMock(n: Int): List<Question> {
        val layout = repo.extras.exam
        val target = CasePlan.caseItemsFor(n, layout.totalItems, layout.caseBlockItems)
        val picks = CasePlan.pickCases(repo.extras.cases, target, { it in store.last })
        val block = CasePlan.block(picks)
        return CasePlan.paper(block, buildRest(n - block.size))
    }

    /** The non-case part of a mock: weighted to the domain split, unseen questions first, shuffled. */
    private fun buildRest(n: Int): List<Question> {
        if (n <= 0) return emptyList()
        val pool = pool()
        val weights = repo.extras.domainWeights
        val out = ArrayList<Question>()
        val spare = ArrayList<Question>()
        for (d in Domain.entries) {
            val l = pool.filter { it.domain == d }.shuffled()
            val ordered = l.filter { it.id !in store.last } + l.filter { it.id in store.last }
            val want = (n * (weights[d] ?: 0.0)).roundToInt()
            out += ordered.take(want)
            spare += ordered.drop(want)
        }
        out += spare.shuffled().take((n - out.size).coerceAtLeast(0))
        return out.take(n).shuffled()
    }

    fun startMock(n: Int) {
        val qs = buildMock(n)
        if (qs.isEmpty()) return
        val layout = repo.extras.exam
        val full = n >= layout.totalItems
        val minutes = (layout.minutes.toDouble() * n / layout.totalItems).roundToInt().coerceAtLeast(5)
        val rnd = Random(System.nanoTime())
        mock = MockSession(
            title = if (full) "Full mock exam" else "Mock exam · $n questions",
            questions = qs,
            order = qs.associate { it.id to shuffledOptions(it, rnd) },
            durationMs = minutes * 60_000L,
            breakAfter = if (full) layout.breakAfter.filter { it in 1 until qs.size } else emptyList(),
            breakMs = layout.breakMinutes * 60_000L,
            // Unscored pretest items, in proportion to the full 170 + 10 (index.html §10.03.1).
            pretest = MockPlan.pickPretest(qs.map { it.id }, layout.totalItems, layout.scoredItems, rnd),
        )
        lastMock = null
        lastMockTime = null
        lastMockMissed = emptyList()
        finishedMock = null
        saveMock()
        startTimer()
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            var last = System.currentTimeMillis()
            var sinceSave = 0L
            while (isActive) {
                delay(1000)
                val m = mock ?: break
                val now = System.currentTimeMillis()
                val dt = now - last
                last = now
                // The clock only runs while the exam is on screen; leaving it pauses the sitting.
                if (screen != Dest.MOCK || !appVisible) continue
                if (m.onBreak) {
                    m.breakRemainingMs -= dt
                    if (m.breakRemainingMs <= 0) endBreak()
                } else {
                    m.remainingMs -= dt
                    m.spentMs[m.current.id] = (m.spentMs[m.current.id] ?: 0L) + dt
                    if (m.remainingMs <= 0) { m.remainingMs = 0; submitMock(); break }
                }
                sinceSave += dt
                if (sinceSave >= 15_000) { saveMock(); sinceSave = 0 }
            }
        }
    }

    private var awaySince = 0L
    /** Set by the activity. The exam clock stops while the app is in the background, and practice timings leave that time out. */
    var appVisible = true
        set(v) {
            val now = System.currentTimeMillis()
            if (!v && field) awaySince = now
            if (v && !field && awaySince > 0) {
                val away = now - awaySince
                listOfNotNull(practice, run?.current, run?.review).forEach { it.awayMs += away; it.awayTotal += away }
                awaySince = 0
            }
            field = v
        }

    fun clearScorecard() {
        lastMock = null
        finishedMock = null
        lastMockTime = null
        lastMockMissed = emptyList()
    }

    /* ── the drill queue (index.html §10.03.12 "send misses to the drill queue") ── */

    /** Question ids sent from mock results, waiting on the Practice screen. Device-only, as the web's queue is. */
    var mockQueue by mutableStateOf(settings.getString("mockQueue", "").orEmpty().split(',').filter { it.isNotBlank() })
        private set

    private fun saveQueue(ids: List<String>) {
        mockQueue = ids
        settings.edit().putString("mockQueue", ids.joinToString(",")).apply()
    }

    /** Sends the last sitting's misses to the queue; returns how many were sent. */
    fun sendMissesToQueue(): Int {
        val ids = lastMockMissed
        saveQueue(MockPlan.enqueue(mockQueue, ids))
        return ids.size
    }

    /** The queue's questions that are still in an enabled bank. */
    fun queuedQuestions(): List<Question> {
        val enabled = pool().associateBy { it.id }
        return mockQueue.mapNotNull { enabled[it] }
    }

    /** Drills up to 20 from the queue; the ones taken leave it. */
    fun startQueue() {
        val take = queuedQuestions().shuffled().take(20)
        if (take.isEmpty()) return
        val taken = take.map { it.id }.toSet()
        saveQueue(mockQueue.filter { it !in taken })
        startPracticeWith(take, "From your last mock")
    }

    fun clearQueue() = saveQueue(emptyList())

    /* ── formula lab drills ──────────────────────────────────────────────── */

    /** Logs a drill problem marked ([verdict]) or shown (null) to the shared drills log. */
    fun recordDrill(p: DrillProblem, verdict: DrillVerdict?) {
        store.recordDrill(FormulaDrills.logRow(p, verdict, System.currentTimeMillis()))
    }

    fun mockAnswer(q: Question, a: Answer) {
        val m = mock ?: return
        if (m.questions.indexOf(q) !in m.inSection) return
        if (q.id !in m.firstTapMs) m.firstTapMs[q.id] = (m.spentMs[q.id] ?: 0L).coerceAtLeast(1L)
        changeDirection(q, m.answers[q.id], a)?.let { up ->
            if (up) m.wrongToRight[q.id] = (m.wrongToRight[q.id] ?: 0) + 1 else m.rightToWrong[q.id] = (m.rightToWrong[q.id] ?: 0) + 1
        }
        m.answers[q.id] = a
        m.struck[q.id]?.let { st -> if (a.choices.any { it in st }) m.struck[q.id] = st - a.choices }
        saveMock()
    }

    fun mockStrike(q: Question, optionId: String) {
        val m = mock ?: return
        if (m.questions.indexOf(q) !in m.inSection) return
        val st = m.struck[q.id].orEmpty()
        m.struck[q.id] = if (optionId in st) st - optionId else st + optionId
        m.answers[q.id]?.let { a -> if (optionId in a.choices && optionId !in st) m.answers[q.id] = a.copy(choices = a.choices - optionId) }
        saveMock()
    }

    fun mockMarks(q: Question, words: Set<Int>) {
        val m = mock ?: return
        m.marks[q.id] = words
        saveMock()
    }

    fun mockGo(i: Int) {
        val m = mock ?: return
        m.index = i.coerceIn(m.inSection.first, m.inSection.last)
    }

    fun toggleMark(q: Question) {
        val m = mock ?: return
        m.marked[q.id] = m.marked[q.id] != true
    }

    /** Closes the current section. Offers the break; the earlier section can't be reopened after it. */
    fun endSection(takeBreak: Boolean) {
        val m = mock ?: return
        if (m.section >= m.sectionCount - 1) { submitMock(); return }
        if (takeBreak) {
            m.onBreak = true
            m.breakRemainingMs = m.breakMs
        } else {
            nextSection()
        }
        saveMock()
    }

    fun endBreak() {
        val m = mock ?: return
        m.onBreak = false
        nextSection()
        saveMock()
    }

    private fun nextSection() {
        val m = mock ?: return
        m.section = (m.section + 1).coerceAtMost(m.sectionCount - 1)
        m.index = m.inSection.first
    }

    fun submitMock() {
        val m = mock ?: return
        timerJob?.cancel()
        m.finished = true
        val now = System.currentTimeMillis()
        var right = 0
        val dom = HashMap<Domain, IntArray>()
        val perQ = if (m.questions.isEmpty()) 0L else (m.durationMs - m.remainingMs) / m.questions.size
        val rows = ArrayList<MockRow>()
        for ((i, q) in m.questions.withIndex()) {
            val a = m.answers[q.id]
            val r = Scoring.score(q, a)
            val pre = q.id in m.pretest
            rows += MockRow(q.id, MockPlan.blockOf(i, m.breakAfter), pre, Scoring.isAnswered(q, a), r.correct, m.marked[q.id] == true,
                m.spentMs[q.id] ?: perQ)
            // Pretest items don't count toward the score, as on the exam; their answers still go to history.
            if (!pre) {
                if (r.correct) right++
                val e = dom.getOrPut(q.domain) { IntArray(2) }
                e[1]++
                if (r.correct) e[0]++
            }
            // Only answered items feed history; a blank at the bell says nothing about the question.
            if (Scoring.isAnswered(q, a)) store.record(attempt(q, r.credit, r.correct, m.spentMs[q.id] ?: perQ, Readiness.SOURCE_EXAM,
                firstTapMs = m.firstTapMs[q.id] ?: 0L, rightToWrong = m.rightToWrong[q.id] ?: 0, wrongToRight = m.wrongToRight[q.id] ?: 0, timed = true,
                eliminated = m.struck[q.id]?.size ?: 0))
        }
        val result = MockResult(now, m.title, m.questions.count { it.id !in m.pretest }, right, m.durationMs - m.remainingMs.coerceAtLeast(0),
            dom.mapValues { it.value[0] to it.value[1] })
        store.recordMock(result)
        store.recordSitting(SittingEvent("mock:${m.startedAt}", "mock", now, m.questions.size, m.answeredCount, true))
        endFocus()
        lastMock = result
        finishedMock = m
        lastMockTime = MockPlan.timeReport(rows, repo.extras.exam.paceTargetSec, m.sectionCount)
        lastMockMissed = rows.filter { !it.correct }.map { it.qid }
        mock = null
        mockFile.delete()
        autoSync()
    }

    fun abandonMock() {
        mock?.let { m -> if (m.answeredCount > 0) store.recordSitting(SittingEvent("mock:${m.startedAt}", "mock", System.currentTimeMillis(), m.questions.size, m.answeredCount, false)) }
        timerJob?.cancel()
        mock = null
        mockFile.delete()
        endFocus()
        autoSync()
    }

    private fun endFocus() {
        Focus.end(getApplication<Application>())
        changeFocusMock(false)
    }

    fun reviewMock(onlyWrong: Boolean) {
        val m = finishedMock ?: return
        val qs = if (onlyWrong) m.questions.filter { !Scoring.score(it, m.answers[it.id]).correct } else m.questions
        if (qs.isEmpty()) return
        mockReview = QuizSession("Review", qs, reviewOnly = true, givenAnswers = m.answers.toMap(), givenOrder = m.order, pretest = m.pretest)
    }

    fun saveMock() {
        val m = mock ?: return
        val json = m.toJson().toString()
        viewModelScope.launch(Dispatchers.IO) { runCatching { mockFile.writeText(json) } }
    }

    private fun restoreMock() {
        if (!mockFile.exists()) return
        val m = runCatching { MockSession.fromJson(JSONObject(mockFile.readText()), repo.byId) }.getOrNull()
        if (m == null) { mockFile.delete(); return }
        mock = m
        startTimer()
    }

    /* ── situational trainer ─────────────────────────────────────────────── */

    var scenarioDomain by mutableStateOf<Domain?>(null)
    var scenarioIndex by mutableStateOf(0)
    var scenarioPick by mutableStateOf<String?>(null)

    fun scenarios(): List<Scenario> = repo.extras.scenarios.filter { scenarioDomain == null || it.domain == scenarioDomain }

    fun submitScenario(s: Scenario) {
        val pick = scenarioPick ?: return
        val opt = s.options.firstOrNull { it.id == pick } ?: return
        store.recordScenario(ScenarioRun(s.id, pick, opt.rank, System.currentTimeMillis()))
    }

    /* ── learn ───────────────────────────────────────────────────────────── */

    var openLesson by mutableStateOf<String?>(null)

    fun recordLessonCheck(q: Question, a: Answer) {
        val r = Scoring.score(q, a)
        // Lesson checks are learning, not evidence: they are kept out of readiness (see Readiness.SOURCE_LESSON).
        store.record(attempt(q, r.credit, r.correct, 0, Readiness.SOURCE_LESSON))
    }

    /* ── flashcards ──────────────────────────────────────────────────────── */

    var cardQueue by mutableStateOf<List<Flashcard>?>(null)
        private set
    var cardIndex by mutableStateOf(0)
    var cardFlipped by mutableStateOf(false)
    val newLeeches = mutableStateListOf<String>()

    fun cardState(id: String): CardState = store.cards[id] ?: srs.fresh()

    /** The built-in decks, plus "My cards" once you've made one. */
    fun decks(): List<Deck> = repo.extras.decks +
        if (store.myCards.isEmpty()) emptyList() else listOf(Deck(ProgressStore.MY_DECK, "My cards", null, "Cards you made from your own mistakes."))

    fun allCards(): List<Flashcard> = repo.extras.flashcards + store.myCards

    /** A card drafted from a missed question: the situation in one line, then the right move and why. */
    fun cardDraft(q: Question): Pair<String, String> {
        val first = q.stem.split(Regex("(?<=[.?!])\\s+")).firstOrNull().orEmpty().trim()
        val front = if (first.length > 160) first.take(157).trimEnd() + "…" else first
        val right = when (q.base) {
            QType.SINGLE, QType.MULTI -> q.options.filter { it.correct }.joinToString("; ") { it.text }
            QType.HOTSPOT -> q.regions.filter { it.correct }.joinToString("; ") { it.label }
            QType.MATCHING -> q.prompts.mapNotNull { p -> q.options.firstOrNull { it.match == p.id }?.let { "${p.text} → ${it.text}" } }.joinToString("; ")
            QType.FILLBLANK -> q.blanks.joinToString("; ") { "${it.label} ${Scoring.blankAnswer(it)}" }
            else -> ""
        }
        val back = listOf(right.takeIf { it.isNotBlank() }?.let { "Right move: $it." }, q.explanation.takeIf { it.isNotBlank() }, q.mindset.takeIf { it.isNotBlank() })
            .filterNotNull().joinToString("\n\n")
        return front to back
    }

    fun makeCard(q: Question, front: String, back: String) {
        if (front.isBlank() || back.isBlank()) return
        store.addMyCard(Flashcard("my:${q.id}", ProgressStore.MY_DECK, q.ecoTask, front.trim(), back.trim(), ""))
    }

    fun deleteMyCard(id: String) {
        store.removeMyCard(id)
        cardQueue = cardQueue?.filter { it.id != id }
        cardFlipped = false
    }

    data class DeckCounts(val total: Int, val due: Int, val new: Int, val leeches: Int)

    fun deckCounts(deck: String?): DeckCounts {
        val now = System.currentTimeMillis()
        val cards = allCards().filter { deck == null || it.deck == deck }
        var due = 0; var new = 0; var leech = 0
        for (c in cards) {
            val s = store.cards[c.id]
            // A card suspended in another app is neither new nor due here.
            if (s != null && s.suspended) { if (srs.isLeech(s)) leech++; continue }
            if (s == null || s.isNew) new++ else if (s.isDue(now)) due++
            if (s != null && srs.isLeech(s)) leech++
        }
        return DeckCounts(cards.size, due, new, leech)
    }

    /** Cards introduced for the first time today, against the daily cap. */
    private fun newIntroducedToday(): Int {
        val today = Srs.startOfDay(System.currentTimeMillis())
        return store.cards.values.count { it.first >= today }
    }

    fun startDeck(deck: String?) {
        val now = System.currentTimeMillis()
        val cards = allCards().filter { deck == null || it.deck == deck }
        val due = cards.filter { c -> store.cards[c.id]?.isDue(now) == true }.sortedBy { store.cards[it.id]?.due }
        val allowance = (repo.extras.srs.newPerDay - newIntroducedToday()).coerceAtLeast(0)
        val fresh = cards.filter { c -> store.cards[c.id]?.let { it.isNew && !it.suspended } ?: true }.take(allowance)
        val q = due + fresh
        cardQueue = q
        cardIndex = 0
        cardFlipped = false
        newLeeches.clear()
    }

    /** Practise a whole deck regardless of schedule; grades still update it. */
    fun cramDeck(deck: String?) {
        cardQueue = allCards().filter { deck == null || it.deck == deck }.shuffled()
        cardIndex = 0
        cardFlipped = false
        newLeeches.clear()
    }

    fun grade(g: Grade) {
        val q = cardQueue ?: return
        val card = q.getOrNull(cardIndex) ?: return
        val before = cardState(card.id)
        val after = srs.review(before, g, System.currentTimeMillis())
        store.setCard(card.id, after)
        if (srs.isLeech(after) && !srs.isLeech(before)) newLeeches += card.front
        // A failed card comes back before the session ends.
        if (g == Grade.AGAIN) cardQueue = q + card
        cardIndex++
        cardFlipped = false
        // The last card of a deck session; a session on the path syncs when the whole session ends.
        if (run == null && cardIndex >= (cardQueue?.size ?: 0)) autoSync()
    }

    /* ── guided path ─────────────────────────────────────────────────────── */

    /** Rebuilt from scratch on every call: the plan is a view of the current state, never a stored calendar. */
    fun plan(now: Long = System.currentTimeMillis()): Plan? {
        val p = store.plan
        val date = p.examDate ?: return null
        val leeches = allCards().filter { c -> c.ecoTask != null && store.cards[c.id]?.let { srs.isLeech(it) } == true }
            .groupingBy { it.ecoTask!! }.eachCount()
        val f = fitness(now)
        return PlanEngine.build(PlanInput(repo.extras, store.attempts, store.lessonsDone, store.sessionResults,
            store.mocks, store.diagnostic, PlanSettings(date, p.hoursPerWeek, p.studyDays, p.byArea), now,
            mistakeTags = store.mistakeTags.mapValues { it.value.cause }, leeches = leeches,
            ladderNext = f.ladderNext, readingSlow = f.wpm?.let { it < ExamFitness.TARGET_WPM * 0.9 } == true))
    }

    /* ── exam fitness ────────────────────────────────────────────────────── */

    fun fitness(now: Long = System.currentTimeMillis()): FitnessReport = ExamFitness.report(FitnessInput(
        store.attempts, store.sittings, store.checkIns, store.sessionResults, store.plan.studyDays, store.plan.hoursPerWeek, now))

    fun recordCheckIn(kind: String, value: Int, items: List<Int> = emptyList()) =
        store.recordCheckIn(CheckIn(System.currentTimeMillis(), kind, value, items))

    fun booking(now: Long = System.currentTimeMillis()): BookingReport = Booking.check(
        readiness.report(store, now).score, fitness(now).score, store.mocks, repo.extras.exam.scoredItems, store.weekly, store.plan.examDate)

    /** The weekly five-statement check is offered once a week, once you've started studying. */
    fun weeklyCheckDue(now: Long = System.currentTimeMillis()): Boolean =
        store.attempts.isNotEmpty() && store.checkIns.none { it.kind == "weekly" && now - it.at < 6 * PlanEngine.DAY }

    /** Keeps this week's report current; last week's stays as it was when the week ended. */
    private fun publishWeekly() {
        val now = System.currentTimeMillis()
        val tasks = PlanEngine.taskStates(repo.extras, store.attempts, now)
        store.putWeekly(WeeklyReports.build(now, readiness.report(store, now), tasks.count { it.answers > 0 },
            tasks.count { it.mastery == Mastery.MASTERED }, store.attempts, store.sessionResults, store.mocks, fitness(now),
            store.mistakeTags.values))
    }

    /** A session being played. Kept while the app runs, so closing it and coming back resumes at the same step. */
    class PathRun(val session: Session, val startedAt: Long, val mocksAtStart: Int) {
        var step by mutableStateOf(0)
        /** The once-a-day "how do you feel" check-in has been answered or skipped. */
        var checkedIn by mutableStateOf(false)
        val practices = ArrayList<QuizSession>()
        var current by mutableStateOf<QuizSession?>(null)
        /** Missed questions due for review, after the warm-up cards. */
        var review by mutableStateOf<QuizSession?>(null)
    }

    var run by mutableStateOf<PathRun?>(null)
        private set

    fun startSession(s: Session) {
        if (run?.session?.key != s.key) run = PathRun(s, System.currentTimeMillis(), store.mocks.size).apply {
            val today = PlanEngine.startOfDay(System.currentTimeMillis())
            checkedIn = store.checkIns.any { it.kind == "pre" && PlanEngine.startOfDay(it.at) == today }
        }
        navigate(Dest.SESSION)
    }

    /* ── home-screen widget ──────────────────────────────────────────────── */

    /** Set when a widget tile opened the app; acted on once content has loaded. */
    var pendingOpen by mutableStateOf<String?>(null)

    fun openPending() {
        val target = pendingOpen ?: return
        pendingOpen = null
        if (screen == Dest.MOCK && mock != null) return // never pull you out of a running mock
        home()
        when (target) {
            PmpWidget.DAYS -> navigate(Dest.PLANNER)
            PmpWidget.TODAY -> (run?.session ?: plan()?.next)?.let { startSession(it) }
            PmpWidget.READY -> navigate(Dest.ANALYTICS)
            PmpWidget.SYLLABUS -> navigate(Dest.LEARN)
            Reminders.SHORT -> (run?.session ?: plan()?.next)?.let { startSession(shortVersion(it)) }
            Reminders.CARDS -> { navigate(Dest.FLASHCARDS); startDeck(null) }
            PmpWidget.FITNESS -> navigate(Dest.FITNESS)
            PmpWidget.REPORTS -> navigate(Dest.REPORTS)
            PmpWidget.BOOK -> navigate(Dest.BOOK)
        }
    }

    private suspend fun publishWidget() {
        val now = System.currentTimeMillis()
        val lessons = repo.extras.lessons
        val ready = readyForWidget(now)
        PmpWidget.publish(getApplication(), WidgetStats(
            examDate = store.plan.examDate,
            day = PlanEngine.startOfDay(now),
            todayMinutes = WidgetStats.minutesToday(store.sessionResults, store.attempts, now),
            targetMinutes = plan(now)?.minutesPerDay ?: 0,
            readiness = readiness.report(store, now).score,
            lessonsDone = lessons.count { it.id in store.lessonsDone },
            lessonsTotal = lessons.size,
            fitness = fitness(now).score ?: -1,
            readyBy = ready.second,
            readyState = ready.first,
        ))
    }

    /** The widget's ready-by tile: now, a projected date, stalled, or unknown. */
    private fun readyForWidget(now: Long): Pair<Int, Long> {
        val b = booking(now)
        val p = b.projection
        return when {
            b.criteria.take(2).all { it.met } -> WidgetStats.READY_NOW to now
            p == null -> WidgetStats.READY_UNKNOWN to 0L
            p.stalled.isNotEmpty() -> WidgetStats.READY_STALLED to 0L
            p.latest != null -> WidgetStats.READY_PROJECTED to p.latest
            p.earliest != null -> WidgetStats.READY_PROJECTED to p.earliest
            else -> WidgetStats.READY_UNKNOWN to 0L
        }
    }

    /* ── smart reminders ─────────────────────────────────────────────────── */

    /** Hands the reminders what they need to write themselves while the app is closed, then re-sets today's alarms. */
    private suspend fun publishReminders() {
        val now = System.currentTimeMillis()
        val snapshot = reminderSnapshot(now)
        val results = store.sessionResults.toList()
        val attempts = store.attempts.toList()
        withContext(Dispatchers.IO) { Reminders.publish(getApplication(), snapshot, results, attempts) }
    }

    private fun reminderSnapshot(now: Long): ReminderSnapshot {
        val p = plan(now)
        val next = run?.session ?: p?.next
        val today = PlanEngine.startOfDay(now)
        val weekAhead = p?.sessions?.filter { it.day > today && it.day <= today + 7 * PlanEngine.DAY }.orEmpty()
        val focus = weekAhead.mapNotNull { it.domain }.groupingBy { it }.eachCount().maxByOrNull { it.value }?.key
            ?.let { "${it.title} domain" + if (weekAhead.any { s -> s.kind == SessionKind.MOCK }) " and a full mock" else "" }
        return ReminderSnapshot(
            day = today,
            nextTitle = next?.title,
            nextMinutes = next?.minutes ?: 0,
            nextWhat = next?.let { describeSteps(it) },
            cardsDue = deckCounts(null).due,
            streak = readiness.streak(store, now).current,
            studyDays = store.plan.studyDays,
            todayMinutes = WidgetStats.minutesToday(store.sessionResults, store.attempts, now),
            targetMinutes = p?.minutesPerDay ?: 0,
            lastStudyAt = maxOf(store.attempts.maxOfOrNull { it.at } ?: 0L, store.sessionResults.maxOfOrNull { it.at } ?: 0L),
            finalWeek = next?.kind == SessionKind.TAPER,
            readiness = readiness.report(store, now).score,
            mastered = p?.tasks?.count { it.mastery == Mastery.MASTERED } ?: 0,
            weekMinutes = ReminderBrain.studyMinutes(store.sessionResults, store.attempts, now - 7 * PlanEngine.DAY, now),
            nextWeekFocus = focus,
            restDayFree = readiness.streak(store, now).restDayFree,
        )
    }

    /** "Warm-up cards, a lesson and 15 questions" */
    private fun describeSteps(s: Session): String {
        val parts = s.steps.mapNotNull {
            when (it.kind) {
                StepKind.CARDS -> "warm-up cards"
                StepKind.LESSON -> "a lesson"
                StepKind.PRACTICE -> "${it.count} questions"
                StepKind.SCENARIOS -> "${it.count} scenarios"
                StepKind.MOCK -> "a full mock exam"
                StepKind.REFLECT -> "a quick reflect"
                StepKind.DEBRIEF -> null
            }
        }
        val text = if (parts.size > 1) parts.dropLast(1).joinToString(", ") + " and " + parts.last() else parts.firstOrNull().orEmpty()
        return text.replaceFirstChar { it.uppercase() }
    }

    /** A 15-minute version of a session for a short day: warm-up cards and 10 questions on the same ground. */
    fun shortVersion(s: Session): Session = s.copy(
        key = "short:" + s.key, title = "Short day: " + s.title, minutes = 15,
        why = "A quick version so the day still counts.",
        steps = listOf(Step(StepKind.CARDS), Step(StepKind.PRACTICE, 10), Step(StepKind.DEBRIEF)),
    )

    /** Questions for a practice step: the session's task or domain, least-recently-right first. */
    fun questionsFor(s: Session, st: Step): List<Question> {
        var qs = pool()
        when {
            s.taskId != null -> qs = qs.filter { it.ecoTask == s.taskId }
            s.domain != null -> qs = qs.filter { it.domain == s.domain }
            s.kind == SessionKind.REENTRY -> {
                val recent = store.attempts.sortedByDescending { it.at }.map { it.ecoTask }.distinct().take(3).toSet()
                if (recent.isNotEmpty()) qs = qs.filter { it.ecoTask in recent }
            }
        }
        if (st.easy) qs = qs.filter { it.difficulty <= 3 }.ifEmpty { qs }
        // Unseen first, then the ones missed last time, then the rest, leaning toward your weakest approach.
        return QuestionPicker.order(qs, { store.last[it]?.correct }, QuestionPicker.weakApproach(store.attempts), st.count)
    }

    /** Called when a step is shown, so the step has what it needs. */
    fun prepareStep(r: PathRun) {
        val st = r.session.steps.getOrNull(r.step) ?: return
        when (st.kind) {
            StepKind.CARDS -> {
                if (r.review != null) return
                val now = System.currentTimeMillis()
                cardQueue = allCards().filter { c -> store.cards[c.id]?.isDue(now) == true }
                    .sortedBy { store.cards[it.id]?.due }.take(15)
                cardIndex = 0; cardFlipped = false; newLeeches.clear()
                if (cardQueue.isNullOrEmpty()) cardsDone(r)
            }
            StepKind.PRACTICE -> if (r.current == null) {
                val qs = questionsFor(r.session, st)
                if (qs.isEmpty()) nextStep(r)
                else QuizSession(r.session.title, qs, timed = st.timed, readingDrill = st.reading).also { r.current = it; r.practices += it }
            }
            else -> {}
        }
    }

    /** Warm-up cards are done: due reviews of missed questions follow, if any. */
    fun cardsDone(r: PathRun) {
        cardQueue = null
        val qs = warmUpReviews(System.currentTimeMillis())
        if (qs.isEmpty()) nextStep(r)
        else r.review = QuizSession("Missed questions, back for review", qs, source = QuestionReview.SOURCE)
    }

    fun nextStep(r: PathRun) {
        val st = r.session.steps.getOrNull(r.step)
        if (st?.kind == StepKind.LESSON) r.session.lessonId?.let { store.setLessonDone(it, true) }
        if (st?.kind == StepKind.CARDS) cardQueue = null
        r.current = null
        r.review = null
        r.step = (r.step + 1).coerceAtMost(r.session.steps.size)
    }

    /** Practice score for the session, or 1.0 when it was reading only. Mocks score as the sitting did. */
    fun sessionScore(r: PathRun): Double {
        if (r.session.steps.any { it.kind == StepKind.MOCK }) {
            return store.mocks.drop(r.mocksAtStart).lastOrNull()?.pct ?: 0.0
        }
        val checked = r.practices.flatMap { p -> p.questions.mapNotNull { p.results[it.id] } }
        return if (checked.isEmpty()) 1.0 else checked.sumOf { it.credit } / checked.size
    }

    private fun pathQuestions(r: PathRun) = r.session.steps.filter { it.kind == StepKind.PRACTICE }.sumOf { it.count }
    private fun pathAnswered(r: PathRun) = r.practices.sumOf { it.checkedCount }

    fun finishSession(r: PathRun) {
        store.recordSitting(SittingEvent(r.session.key, "path", System.currentTimeMillis(), pathQuestions(r), pathAnswered(r), true))
        val minutes = ((System.currentTimeMillis() - r.startedAt) / 60_000).toInt().coerceIn(5, r.session.minutes.coerceAtLeast(5))
        store.recordSession(SessionResult(r.session.key, r.session.kind, r.session.taskId, r.session.domain, sessionScore(r), minutes, System.currentTimeMillis()))
        run = null
        cardQueue = null
        home()
        autoSync()
    }

    /** Leaves the player; the run is kept so Continue resumes it. */
    fun closeSession() {
        run?.let { r -> if (pathAnswered(r) > 0) store.recordSitting(SittingEvent(r.session.key, "path", System.currentTimeMillis(), pathQuestions(r), pathAnswered(r), false)) }
        cardQueue = null
        back()
    }

    fun finishOnboarding() {
        store.setPlan(store.plan.copy(onboarded = true))
    }

    /* ── diagnostic ──────────────────────────────────────────────────────── */

    /**
     * Twenty-five questions split 8 / 10 / 7, the closest whole-number split to
     * the domain weights, drawn from the core pack across the difficulty range
     * (§10.09.1): the figure being estimated is where you stand, and a set at
     * one difficulty would estimate something else.
     */
    fun diagnosticSet(): List<Question> {
        val core = repo.packs.firstOrNull { it.id == "core" }?.questions.orEmpty().ifEmpty { pool() }
        val want = mapOf(Domain.PEOPLE to 8, Domain.PROCESS to 10, Domain.BUSINESS to 7)
        val out = ArrayList<Question>()
        for ((d, n) in want) {
            val byDiff = core.filter { it.domain == d }.shuffled().groupBy { it.difficulty }.mapValues { it.value.toMutableList() }
            val levels = byDiff.keys.sorted()
            var i = 0
            while (out.count { it.domain == d } < n && byDiff.values.any { it.isNotEmpty() }) {
                byDiff[levels[i % levels.size]]?.let { l -> if (l.isNotEmpty()) out += l.removeAt(0) }
                i++
            }
        }
        return out.shuffled()
    }

    fun startDiagnostic() {
        val qs = diagnosticSet()
        if (qs.size < 5) return
        practice = QuizSession("Diagnostic", qs, diagnostic = true)
        if (screen != Dest.PRACTICE) navigate(Dest.PRACTICE)
    }

    fun finishDiagnostic(s: QuizSession) {
        val now = System.currentTimeMillis()
        val dom = HashMap<Domain, IntArray>()
        var right = 0
        for (q in s.questions) {
            val a = s.answers[q.id]
            val r = Scoring.score(q, a)
            s.results[q.id] = r
            if (r.correct) right++
            val e = dom.getOrPut(q.domain) { IntArray(2) }
            e[1]++
            if (r.correct) e[0]++
            if (Scoring.isAnswered(q, a)) store.record(attempt(q, r.credit, r.correct, 0, SOURCE_DIAGNOSTIC))
        }
        store.recordDiagnostic(DiagnosticResult(now, s.questions.size, right, dom.mapValues { it.value[0] to it.value[1] }))
        s.index = 0
        s.finished = true
        autoSync()
    }

    /* ── coach ───────────────────────────────────────────────────────────── */

    fun coach(): Coach = Coach(repo.extras, pool(), store, readiness)

    /** Where a coach line or step sends you. */
    fun coachGo(action: CoachAction, arg: String?) {
        when (action) {
            CoachAction.PLANNER -> navigate(Dest.PLANNER)
            CoachAction.DIAGNOSTIC -> startDiagnostic()
            CoachAction.PRACTICE -> startPractice(PracticeFilter(count = 10), "Mixed practice")
            CoachAction.TASK -> if (arg != null) startPractice(PracticeFilter(task = arg, count = 10), "Practice: ${repo.task(arg)?.label ?: ""}") else navigate(Dest.PRACTICE)
            CoachAction.MOCK -> navigate(Dest.MOCK)
            CoachAction.LEARN -> { openLesson = arg; navigate(Dest.LEARN) }
            CoachAction.CARDS -> navigate(Dest.FLASHCARDS)
            CoachAction.SCENARIOS -> navigate(Dest.SITUATIONAL)
            CoachAction.JOURNAL -> navigate(Dest.MISTAKES)
            CoachAction.FORMULAS -> navigate(Dest.FORMULAS)
            CoachAction.ANALYTICS -> navigate(Dest.ANALYTICS)
        }
    }

    fun snoozeCoach(id: String, days: Int) {
        store.snooze(id, System.currentTimeMillis() + days * Srs.DAY_MS)
    }

    /* The key is the one secret here, so it lives in its own preferences file,
       kept out of progress backups and out of Android's cloud backup. */
    private val aiPrefs = app.getSharedPreferences("gemini", android.content.Context.MODE_PRIVATE)
    var geminiKey by mutableStateOf(aiPrefs.getString("key", "").orEmpty())
        private set
    /**
     * The Flash models the coach uses, newest first (see [Gemini.lineup]). Kept on the phone and
     * brought up to date from Google once a week, so new models are picked up with no app update.
     */
    var geminiModels by mutableStateOf(savedLineup())
        private set
    /** The model that last answered in this app session. */
    private var sessionModel: String? = null
    /** Where the next message starts in the line-up: each starts one model on, so every model takes turns. */
    private var turn = 0
    var aiBusy by mutableStateOf(false)
        private set
    var aiError by mutableStateOf<String?>(null)
    /** Text waiting in the coach's input, e.g. a question sent from practice. */
    var coachDraft by mutableStateOf("")
    var coachTab by mutableStateOf(0)

    /** Checks a pasted key by listing its models, then keeps it with its line-up. */
    fun useKey(key: String) {
        val k = key.trim()
        if (k.isEmpty() || aiBusy) return
        aiBusy = true
        aiError = null
        viewModelScope.launch {
            try {
                updateModels(k)
                geminiKey = k
                aiPrefs.edit().putString("key", k).apply()
            } catch (e: Exception) {
                aiError = e.message
            } finally {
                aiBusy = false
            }
        }
    }

    /** The weekly check: when the line-up is a week old (or missing), asks Google which models are current. */
    fun checkModels() {
        if (geminiKey.isEmpty() || !lineupStale()) return
        viewModelScope.launch { runCatching { updateModels(geminiKey) } }
    }

    private fun lineupStale() = geminiModels.isEmpty() || System.currentTimeMillis() - aiPrefs.getLong("lineupAt", 0L) > LINEUP_MAX_AGE

    /** Fetches Google's current models for [key] and keeps the line-up: Flash 3.5 and newer, at most five. */
    private suspend fun updateModels(key: String) {
        val lineup = Gemini.lineup(withContext(Dispatchers.IO) { Gemini.listModels(key) })
        if (lineup.isEmpty()) throw Gemini.GeminiException(
            "This key works, but Google offers it no Gemini ${Gemini.BASE_VERSION} Flash or newer, and the coach doesn't use older models.")
        geminiModels = lineup
        if (lineup.none { it.name == sessionModel }) sessionModel = null
        aiPrefs.edit().putString("lineup", org.json.JSONArray(lineup.map { org.json.JSONObject().put("n", it.name).put("l", it.label).put("r", it.rank) }).toString())
            .putLong("lineupAt", System.currentTimeMillis()).apply()
    }

    private fun savedLineup(): List<Gemini.Model> = runCatching {
        val a = org.json.JSONArray(aiPrefs.getString("lineup", "[]"))
        (0 until a.length()).map { a.getJSONObject(it).let { o -> Gemini.Model(o.getString("n"), o.getString("l"), o.getDouble("r")) } }
    }.getOrDefault(emptyList())

    /**
     * Sends one request round the line-up: each message starts one model further along than the last,
     * wrapping round, so every model (3.5 included) shares the work and its own free quota; an error moves
     * the request on to the next model. A refused key or no connection stops at once; when every model
     * fails, the error says whether the key's limit is used up.
     */
    private suspend fun callGemini(system: String, turns: List<ChatTurn>): String {
        val key = geminiKey
        if (lineupStale()) runCatching { updateModels(key) }
        val names = geminiModels.map { it.name }
        if (names.isEmpty()) throw Gemini.GeminiException("Couldn't get the list of Gemini models from Google. Check your connection and try again.")
        val start = turn % names.size
        turn = start + 1
        var last: Gemini.GeminiException? = null
        var limited = false
        for (model in names.drop(start) + names.take(start)) {
            try {
                val reply = withContext(Dispatchers.IO) { Gemini.generate(key, model, system, turns) }
                sessionModel = model
                return reply
            } catch (e: Gemini.GeminiException) {
                if (!Gemini.tryNextModel(e)) throw e
                last = e
                if (e.status == 429) limited = true
            }
        }
        throw Gemini.GeminiException(
            if (limited) "Your Gemini key has reached its limit on every model the coach uses. ${last?.message.orEmpty()}"
            else "None of the Gemini models answered. Google said: ${last?.message.orEmpty()}", last?.status ?: 0)
    }

    fun forgetKey() {
        geminiKey = ""
        sessionModel = null
        aiPrefs.edit().remove("key").apply()
    }

    /** Sends a message with a fresh snapshot of the person's progress. */
    fun ask(text: String) {
        val t = text.trim()
        if (t.isEmpty() || aiBusy || geminiKey.isEmpty()) return
        store.addChat(ChatTurn("user", t, System.currentTimeMillis()))
        aiBusy = true
        aiError = null
        val system = Coach.SYSTEM + "\n\nLearner progress snapshot, computed by the app just now:\n" + coach().snapshot()
        val turns = store.chat.takeLast(SEND_TURNS).toList()
        viewModelScope.launch {
            try {
                val reply = callGemini(system, turns)
                store.addChat(ChatTurn("model", reply, System.currentTimeMillis()))
            } catch (e: Exception) {
                aiError = e.message ?: "Something went wrong."
            } finally {
                aiBusy = false
            }
        }
    }

    /** Explain-it-back: what Gemini made of your explanation. Not kept in the chat. */
    var explainReply by mutableStateOf<String?>(null)
        private set
    var explainBusy by mutableStateOf(false)
        private set
    var explainError by mutableStateOf<String?>(null)
        private set

    fun clearExplain() { explainReply = null; explainError = null }

    /** Sends your own explanation of [topic] for feedback: what you got right, and what you missed. */
    fun explainBack(topic: String, text: String) {
        val t = text.trim()
        if (t.isEmpty() || explainBusy || geminiKey.isEmpty()) return
        explainBusy = true
        explainError = null
        explainReply = null
        val turns = listOf(ChatTurn("user", "What I was studying:\n$topic\n\nMy explanation in my own words:\n$t", System.currentTimeMillis()))
        viewModelScope.launch {
            try {
                explainReply = callGemini(Coach.EXPLAIN_SYSTEM, turns)
            } catch (e: Exception) {
                explainError = e.message ?: "Something went wrong."
            } finally {
                explainBusy = false
            }
        }
    }

    /** Opens the coach's chat with a question from practice ready to send. */
    fun askAbout(q: Question, s: QuizSession) {
        val opts = s.order[q.id] ?: q.options
        val a = s.answers[q.id]
        val sb = StringBuilder("Help me with this question:\n\n").append(q.stem).append("\n")
        opts.forEachIndexed { i, o -> sb.append("\n").append("ABCDEFGH"[i % 8]).append(") ").append(o.text) }
        q.prompts.forEach { sb.append("\n• ").append(it.text) }
        q.regions.forEach { sb.append("\n").append(it.n).append(") ").append(it.label) }
        q.blanks.forEach { sb.append("\n• ").append(it.label) }
        if (a != null && a.choices.isNotEmpty() && opts.isNotEmpty()) {
            sb.append("\n\nI chose ").append(a.choices.joinToString { id -> opts.indexOfFirst { it.id == id }.let { if (it >= 0) "ABCDEFGH"[it].toString() else id } }).append(".")
        }
        if (s.isChecked(q)) sb.append(" I've seen the answer; explain why it is right and the others are not.")
        else sb.append("\n\nDon't give me the answer yet: give me a hint about how to think about it.")
        coachDraft = sb.toString()
        coachTab = 1
        navigate(Dest.COACH)
    }

    /* ── cloud sync ──────────────────────────────────────────────────────── */

    /* The URL and code together can read and replace the person's progress, so like the Gemini key they
       live in their own preferences file, kept out of progress backups and out of Android's cloud backup. */
    private val syncPrefs = app.getSharedPreferences("sync", android.content.Context.MODE_PRIVATE)
    private val cloud = CloudSync(CloudSync.Http())
    /** The document as it was after this phone's last sync: the merge's base (docs/progress-format.md §3). */
    private val syncBaseFile: File get() = File(getApplication<Application>().filesDir, "sync-base.json")
    private var syncJob: Job? = null
    /** A trigger came while a sync was running: run once more when it ends. */
    private var syncAgain = false

    var syncUrl by mutableStateOf(syncPrefs.getString("url", "").orEmpty())
        private set
    var syncCode by mutableStateOf(syncPrefs.getString("code", "").orEmpty())
        private set
    /** The code put into a new script. Kept until sync is disconnected, so the script copied earlier still matches. */
    var syncDraftCode by mutableStateOf(syncPrefs.getString("draft", null) ?: newDraftCode())
        private set
    var syncBusy by mutableStateOf(false)
        private set
    /** When the last sync succeeded (ms), or 0. */
    var syncLastAt by mutableStateOf(syncPrefs.getLong("lastAt", 0L))
        private set
    /** Why the last sync failed; shown on the Cloud sync screen, never as a pop-up. */
    var syncError by mutableStateOf(syncPrefs.getString("error", null))
        private set
    /** What Test connection found, while setting up. */
    var syncSetupError by mutableStateOf<String?>(null)
        private set

    val syncOn: Boolean get() = syncUrl.isNotEmpty() && syncCode.isNotEmpty()

    private fun newDraftCode(): String = CloudSync.newCode().also { syncPrefs.edit().putString("draft", it).apply() }

    /** The script to deploy, with this phone's new code in it; empty if the build carries no script. */
    fun syncScript(): String = CloudSync.script(repo.extras.syncScript, syncDraftCode)

    /** One line for the menu and the backup screen. */
    fun syncStatus(now: Long = System.currentTimeMillis()): String = when {
        !syncOn -> "Off · share progress through your own Google Drive"
        syncBusy -> "Syncing…"
        syncError != null -> "Last sync failed · tap to see why"
        syncLastAt == 0L -> "On · not synced yet"
        else -> "On · synced " + whenText(syncLastAt, now)
    }

    fun whenText(t: Long, now: Long = System.currentTimeMillis()): String =
        if (Srs.startOfDay(t) == Srs.startOfDay(now)) "today at " + java.text.DateFormat.getTimeInstance(java.text.DateFormat.SHORT).format(java.util.Date(t))
        else java.text.DateFormat.getDateTimeInstance(java.text.DateFormat.MEDIUM, java.text.DateFormat.SHORT).format(java.util.Date(t))

    /**
     * Test connection: checks the URL and code with a ping, and when the script answers, keeps them
     * and runs the first sync. A fresh connection starts from an empty base.
     */
    fun connectSync(rawUrl: String, rawCode: String) {
        if (syncBusy) return
        val url = CloudSync.cleanUrl(rawUrl)
        if (url == null) { syncSetupError = "Paste the Web app URL: it starts with https://script.google.com/macros/s/ and ends with /exec."; return }
        val code = CloudSync.cleanCode(rawCode)
        if (code == null) { syncSetupError = "The sync code is the 32 letters and digits between the quotes on the SYNC_CODE line of the script."; return }
        syncBusy = true
        syncSetupError = null
        viewModelScope.launch {
            var ok = false
            try {
                withContext(Dispatchers.IO) {
                    cloud.ping(url, code)
                    syncBaseFile.delete()
                }
                syncPrefs.edit().putString("url", url).putString("code", code).putLong("lastAt", 0L).remove("error").apply()
                syncUrl = url
                syncCode = code
                syncLastAt = 0L
                syncError = null
                ok = true
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                syncSetupError = e.message ?: e.toString()
            } finally {
                syncBusy = false
            }
            if (ok) syncNow()
        }
    }

    /** Stops syncing on this phone. Progress here and the file in Drive are both kept. */
    fun disconnectSync() {
        syncJob?.cancel()
        syncJob = null
        syncAgain = false
        syncBusy = false
        syncPrefs.edit().remove("url").remove("code").remove("lastAt").remove("error").apply()
        syncUrl = ""
        syncCode = ""
        syncLastAt = 0L
        syncError = null
        syncSetupError = null
        syncDraftCode = newDraftCode()
        viewModelScope.launch(Dispatchers.IO) { syncBaseFile.delete() }
    }

    /** The Sync now button. */
    fun syncNow() = sync()

    /** Opening, leaving, and finishing a session, set, mock or card review. Quiet: errors wait on the Cloud sync screen. */
    fun autoSync() {
        if (ready) sync()
    }

    /* ── support reminder (docs/progress-format.md §4) ─────────────────── */

    /** The support message on screen, or NONE. */
    var supportAsk by mutableStateOf(Support.Due.NONE)
        private set

    /** On start and on coming to the front: note today's date, then show the support message if it's due. */
    fun appOpened() {
        if (!ready) return
        val now = System.currentTimeMillis()
        val today = Support.day(now)
        store.recordOpen(today)
        if (supportAsk != Support.Due.NONE) return
        val busy = mock != null || screen == Dest.SESSION
        val due = Support.due(store.opens, store.support, today, now, busy)
        if (due == Support.Due.NONE) return
        store.setSupport(Support.markShown(store.support, now, today))
        supportAsk = due
    }

    fun dismissSupportAsk() { supportAsk = Support.Due.NONE }

    /** The support button, in the message or the menu: the app can't see payments, so pressing is all it knows. */
    fun supportPressed() {
        supportAsk = Support.Due.NONE
        store.setSupport(Support.markPressed(store.support, System.currentTimeMillis()))
    }

    /**
     * One sync (docs/progress-format.md §5): this phone's progress against the cloud copy, the result
     * taken as progress here and kept as the next base. Never during a mock: the exam's answers go
     * in together when it's submitted.
     */
    private fun sync() {
        if (!syncOn || mock != null) return
        if (syncJob?.isActive == true) { syncAgain = true; return }
        val url = syncUrl
        val code = syncCode
        syncBusy = true
        syncJob = viewModelScope.launch {
            try {
                // A consistent snapshot: changes bump the revision inside the store's lock.
                val (rev0, local) = withContext(Dispatchers.Default) { synchronized(store) { store.revision.value to store.toJson() } }
                val device = local.getJSONObject("device")
                val (result, localChanged) = withContext(Dispatchers.IO) {
                    val base = runCatching { JSONObject(syncBaseFile.readText()) }.getOrNull()
                    val r = cloud.sync(url, code, base, local, System.currentTimeMillis(), device)
                    r to !SyncMerge.same(r.merged, local)
                }
                // Disconnected, or a mock started, while the network was busy: leave it all for next time.
                if (syncUrl == url && syncCode == code && mock == null) {
                    if (store.revision.value != rev0) {
                        // Answers recorded during the sync go on top of the merge, and up to the cloud next time.
                        store.applySynced(SyncMerge.merge(local, store.toJson(), result.merged, System.currentTimeMillis(), device))
                    } else if (localChanged) {
                        store.applySynced(result.merged)
                    }
                    val baseText = result.merged.toString()
                    withContext(Dispatchers.IO) {
                        val tmp = File(syncBaseFile.path + ".tmp")
                        tmp.writeText(baseText)
                        if (!tmp.renameTo(syncBaseFile)) { syncBaseFile.delete(); tmp.renameTo(syncBaseFile) }
                    }
                    syncLastAt = System.currentTimeMillis()
                    syncError = null
                    syncPrefs.edit().putLong("lastAt", syncLastAt).remove("error").apply()
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                syncError = e.message ?: e.toString()
                syncPrefs.edit().putString("error", syncError).apply()
            } finally {
                syncBusy = false
            }
            if (syncAgain) {
                syncAgain = false
                syncJob = null  // this one is ending; let the next start
                sync()
            }
        }
    }

    /* ── banks and backup ────────────────────────────────────────────────── */

    fun setPackEnabled(p: QuestionPack, on: Boolean) {
        repo.setEnabled(p, on)
        contentVersion++
    }

    fun deletePack(p: QuestionPack) {
        repo.deletePack(p)
        contentVersion++
    }

    /** Imports each file and returns a report to show. */
    suspend fun importPacks(uris: List<Uri>): String = withContext(Dispatchers.IO) {
        val cr = getApplication<Application>().contentResolver
        val sb = StringBuilder()
        for (u in uris) {
            val name = displayName(u)
            try {
                val text = cr.openInputStream(u)?.bufferedReader()?.use { it.readText() } ?: throw Exception("The file could not be opened.")
                val p = repo.importPack(text, name)
                sb.append("✓ ${p.name}: ${p.questions.size} questions added")
                if (p.skippedDuplicates > 0) sb.append(", ${p.skippedDuplicates} already in the app")
                if (p.invalid > 0) sb.append(", ${p.invalid} invalid skipped:\n   ").append(p.errors.joinToString("\n   "))
                sb.append("\n\n")
            } catch (e: Exception) {
                sb.append("✗ $name: ${e.message}\n\n")
            }
        }
        withContext(Dispatchers.Main) { contentVersion++ }
        sb.toString().trim()
    }

    suspend fun exportProgress(uri: Uri): String = withContext(Dispatchers.IO) {
        try {
            getApplication<Application>().contentResolver.openOutputStream(uri, "wt")?.use {
                it.write(store.toJson().toString(1).toByteArray())
            } ?: throw Exception("The file could not be written.")
            "Backup saved."
        } catch (e: Exception) {
            "Could not save: ${e.message}"
        }
    }

    suspend fun restoreProgress(uri: Uri): String = withContext(Dispatchers.IO) {
        try {
            val text = getApplication<Application>().contentResolver.openInputStream(uri)?.bufferedReader()?.use { it.readText() }
                ?: throw Exception("The file could not be opened.")
            withContext(Dispatchers.Main) { store.restore(text) }
            "Progress restored."
        } catch (e: Exception) {
            "Could not restore: ${e.message}"
        }
    }

    companion object {
        const val SOURCE_DIAGNOSTIC = "diagnostic"
        private const val SEND_TURNS = 30
        /** Google is asked which models are current once a week. */
        private const val LINEUP_MAX_AGE = 7 * 24 * 60 * 60 * 1000L
    }

    fun resetProgress() {
        store.reset()
        abandonMock()
        lastMock = null
        finishedMock = null
    }

    private fun displayName(u: Uri): String {
        val cr = getApplication<Application>().contentResolver
        runCatching {
            cr.query(u, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)?.use { c ->
                if (c.moveToFirst()) return c.getString(0)
            }
        }
        return u.lastPathSegment ?: "pack.json"
    }

    override fun onCleared() {
        saveMock()
        super.onCleared()
    }
}
