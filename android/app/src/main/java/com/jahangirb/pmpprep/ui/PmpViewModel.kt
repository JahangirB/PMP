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
import com.jahangirb.pmpprep.data.WidgetStats
import com.jahangirb.pmpprep.widget.PmpWidget
import kotlinx.coroutines.flow.collectLatest
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
    SESSION("Session"),
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
                readiness = Readiness(repo.extras.readiness, repo.extras.exam, repo.extras.tasks)
                srs = Srs(repo.extras.srs)
                restoreMock()
                ready = true
                // Keep the home-screen widget current: a second after progress settles.
                launch { store.revision.collectLatest { delay(1000); runCatching { publishWidget() } } }
            } catch (e: Exception) {
                loadError = e.message ?: e.toString()
            }
        }
    }

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
    var practice by mutableStateOf<QuizSession?>(null)

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
        val qs = matching(f).shuffled().take(f.count)
        if (qs.isEmpty()) return
        practice = QuizSession(title, qs)
        if (screen != Dest.PRACTICE) navigate(Dest.PRACTICE)
    }

    fun startPracticeWith(qs: List<Question>, title: String) {
        if (qs.isEmpty()) return
        practice = QuizSession(title, qs.shuffled())
        if (screen != Dest.PRACTICE) navigate(Dest.PRACTICE)
    }

    fun answer(s: QuizSession, q: Question, a: Answer) {
        if (s.isChecked(q)) return
        s.answers[q.id] = a
    }

    fun check(s: QuizSession, source: String = Readiness.SOURCE_PRACTICE) {
        val q = s.current
        if (s.isChecked(q) || !Scoring.isAnswered(q, s.answers[q.id])) return
        val r = Scoring.score(q, s.answers[q.id])
        s.results[q.id] = r
        store.record(attempt(q, r.credit, r.correct, System.currentTimeMillis() - s.shownAt, source))
    }

    fun moveTo(s: QuizSession, i: Int) {
        s.index = i.coerceIn(0, s.questions.size - 1)
        s.shownAt = System.currentTimeMillis()
    }

    private fun attempt(q: Question, credit: Double, correct: Boolean, ms: Long, source: String) = Attempt(
        qid = q.id, ecoTask = q.ecoTask, domain = q.domain, approach = q.approach, type = q.type,
        credit = credit, correct = correct, ms = ms, at = System.currentTimeMillis(), source = source,
    )

    /* ── mock exam ───────────────────────────────────────────────────────── */

    var mock by mutableStateOf<MockSession?>(null)
        private set
    var lastMock by mutableStateOf<MockResult?>(null)
        private set
    /** The finished sitting, kept for its review. */
    var finishedMock by mutableStateOf<MockSession?>(null)
        private set
    var mockReview by mutableStateOf<QuizSession?>(null)
    private var timerJob: Job? = null
    private val mockFile: File get() = File(getApplication<Application>().filesDir, "exam.json")

    /** Draws a mock weighted to the domain split, unseen questions first. */
    fun buildMock(n: Int): List<Question> {
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
        )
        lastMock = null
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
                    if (m.remainingMs <= 0) { m.remainingMs = 0; submitMock(); break }
                }
                sinceSave += dt
                if (sinceSave >= 15_000) { saveMock(); sinceSave = 0 }
            }
        }
    }

    /** Set by the activity; the exam clock stops while the app is in the background. */
    var appVisible = true

    fun clearScorecard() {
        lastMock = null
        finishedMock = null
    }

    fun mockAnswer(q: Question, a: Answer) {
        val m = mock ?: return
        if (m.questions.indexOf(q) !in m.inSection) return
        m.answers[q.id] = a
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
        for (q in m.questions) {
            val a = m.answers[q.id]
            val r = Scoring.score(q, a)
            if (r.correct) right++
            val e = dom.getOrPut(q.domain) { IntArray(2) }
            e[1]++
            if (r.correct) e[0]++
            // Only answered items feed history; a blank at the bell says nothing about the question.
            if (Scoring.isAnswered(q, a)) store.record(attempt(q, r.credit, r.correct, perQ, Readiness.SOURCE_EXAM))
        }
        val result = MockResult(now, m.title, m.questions.size, right, m.durationMs - m.remainingMs.coerceAtLeast(0),
            dom.mapValues { it.value[0] to it.value[1] })
        store.recordMock(result)
        lastMock = result
        finishedMock = m
        mock = null
        mockFile.delete()
    }

    fun abandonMock() {
        timerJob?.cancel()
        mock = null
        mockFile.delete()
    }

    fun reviewMock(onlyWrong: Boolean) {
        val m = finishedMock ?: return
        val qs = if (onlyWrong) m.questions.filter { !Scoring.score(it, m.answers[it.id]).correct } else m.questions
        if (qs.isEmpty()) return
        mockReview = QuizSession("Review", qs, reviewOnly = true, givenAnswers = m.answers.toMap(), givenOrder = m.order)
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

    data class DeckCounts(val total: Int, val due: Int, val new: Int, val leeches: Int)

    fun deckCounts(deck: String?): DeckCounts {
        val now = System.currentTimeMillis()
        val cards = repo.extras.flashcards.filter { deck == null || it.deck == deck }
        var due = 0; var new = 0; var leech = 0
        for (c in cards) {
            val s = store.cards[c.id]
            if (s == null || s.isNew) new++ else if (s.due <= now) due++
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
        val cards = repo.extras.flashcards.filter { deck == null || it.deck == deck }
        val due = cards.filter { c -> store.cards[c.id]?.let { !it.isNew && it.due <= now } == true }.sortedBy { store.cards[it.id]?.due }
        val allowance = (repo.extras.srs.newPerDay - newIntroducedToday()).coerceAtLeast(0)
        val fresh = cards.filter { c -> store.cards[c.id]?.isNew != false }.take(allowance)
        val q = due + fresh
        cardQueue = q
        cardIndex = 0
        cardFlipped = false
        newLeeches.clear()
    }

    /** Practise a whole deck regardless of schedule; grades still update it. */
    fun cramDeck(deck: String?) {
        cardQueue = repo.extras.flashcards.filter { deck == null || it.deck == deck }.shuffled()
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
    }

    /* ── guided path ─────────────────────────────────────────────────────── */

    /** Rebuilt from scratch on every call: the plan is a view of the current state, never a stored calendar. */
    fun plan(now: Long = System.currentTimeMillis()): Plan? {
        val p = store.plan
        val date = p.examDate ?: return null
        return PlanEngine.build(PlanInput(repo.extras, store.attempts, store.lessonsDone, store.sessionResults,
            store.mocks, store.diagnostic, PlanSettings(date, p.hoursPerWeek, p.studyDays), now))
    }

    /** A session being played. Kept while the app runs, so closing it and coming back resumes at the same step. */
    class PathRun(val session: Session, val startedAt: Long, val mocksAtStart: Int) {
        var step by mutableStateOf(0)
        val practices = ArrayList<QuizSession>()
        var current by mutableStateOf<QuizSession?>(null)
    }

    var run by mutableStateOf<PathRun?>(null)
        private set

    fun startSession(s: Session) {
        if (run?.session?.key != s.key) run = PathRun(s, System.currentTimeMillis(), store.mocks.size)
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
        }
    }

    private suspend fun publishWidget() {
        val now = System.currentTimeMillis()
        val lessons = repo.extras.lessons
        PmpWidget.publish(getApplication(), WidgetStats(
            examDate = store.plan.examDate,
            day = PlanEngine.startOfDay(now),
            todayMinutes = WidgetStats.minutesToday(store.sessionResults, store.attempts, now),
            targetMinutes = plan(now)?.minutesPerDay ?: 0,
            readiness = readiness.report(store, now).score,
            lessonsDone = lessons.count { it.id in store.lessonsDone },
            lessonsTotal = lessons.size,
        ))
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
        // Unseen first, then the ones missed last time, then the rest; shuffled within each group.
        val unseen = qs.filter { it.id !in store.last }.shuffled()
        val missed = qs.filter { store.last[it.id]?.correct == false }.shuffled()
        val rest = qs.filter { store.last[it.id]?.correct == true }.shuffled()
        return (unseen + missed + rest).take(st.count)
    }

    /** Called when a step is shown, so the step has what it needs. */
    fun prepareStep(r: PathRun) {
        val st = r.session.steps.getOrNull(r.step) ?: return
        when (st.kind) {
            StepKind.CARDS -> {
                val now = System.currentTimeMillis()
                cardQueue = repo.extras.flashcards.filter { c -> store.cards[c.id]?.let { !it.isNew && it.due <= now } == true }
                    .sortedBy { store.cards[it.id]?.due }.take(15)
                cardIndex = 0; cardFlipped = false; newLeeches.clear()
                if (cardQueue.isNullOrEmpty()) { cardQueue = null; nextStep(r) }
            }
            StepKind.PRACTICE -> if (r.current == null) {
                val qs = questionsFor(r.session, st)
                if (qs.isEmpty()) nextStep(r)
                else QuizSession(r.session.title, qs).also { r.current = it; r.practices += it }
            }
            else -> {}
        }
    }

    fun nextStep(r: PathRun) {
        val st = r.session.steps.getOrNull(r.step)
        if (st?.kind == StepKind.LESSON) r.session.lessonId?.let { store.setLessonDone(it, true) }
        if (st?.kind == StepKind.CARDS) cardQueue = null
        r.current = null
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

    fun finishSession(r: PathRun) {
        val minutes = ((System.currentTimeMillis() - r.startedAt) / 60_000).toInt().coerceIn(5, r.session.minutes.coerceAtLeast(5))
        store.recordSession(SessionResult(r.session.key, r.session.kind, r.session.taskId, r.session.domain, sessionScore(r), minutes, System.currentTimeMillis()))
        run = null
        cardQueue = null
        home()
    }

    /** Leaves the player; the run is kept so Continue resumes it. */
    fun closeSession() {
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
    var geminiModel by mutableStateOf(aiPrefs.getString("model", "").orEmpty())
        private set
    var geminiModels by mutableStateOf<List<Gemini.Model>>(emptyList())
        private set
    var aiBusy by mutableStateOf(false)
        private set
    var aiError by mutableStateOf<String?>(null)
    /** Text waiting in the coach's input, e.g. a question sent from practice. */
    var coachDraft by mutableStateOf("")
    var coachTab by mutableStateOf(0)

    /** Checks a pasted key by listing its models, then keeps it. */
    fun useKey(key: String) {
        val k = key.trim()
        if (k.isEmpty() || aiBusy) return
        aiBusy = true
        aiError = null
        viewModelScope.launch {
            try {
                val models = withContext(Dispatchers.IO) { Gemini.listModels(k) }
                geminiModels = models
                val keep = models.firstOrNull { it.name == geminiModel } ?: Gemini.defaultModel(models)
                geminiKey = k
                geminiModel = keep?.name.orEmpty()
                aiPrefs.edit().putString("key", k).putString("model", geminiModel).apply()
            } catch (e: Exception) {
                aiError = e.message
            } finally {
                aiBusy = false
            }
        }
    }

    fun loadModels() {
        if (geminiKey.isEmpty() || geminiModels.isNotEmpty() || aiBusy) return
        viewModelScope.launch {
            runCatching { withContext(Dispatchers.IO) { Gemini.listModels(geminiKey) } }.onSuccess { m ->
                geminiModels = m
                if (m.none { it.name == geminiModel }) setModel(Gemini.defaultModel(m)?.name.orEmpty())
            }
        }
    }

    fun setModel(name: String) {
        geminiModel = name
        aiPrefs.edit().putString("model", name).apply()
    }

    fun forgetKey() {
        geminiKey = ""
        geminiModels = emptyList()
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
        val key = geminiKey
        val model = geminiModel.ifEmpty { "models/gemini-3-flash" }
        viewModelScope.launch {
            try {
                val reply = withContext(Dispatchers.IO) { Gemini.generate(key, model, system, turns) }
                store.addChat(ChatTurn("model", reply, System.currentTimeMillis()))
            } catch (e: Exception) {
                aiError = e.message ?: "Something went wrong."
            } finally {
                aiBusy = false
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
