package com.jahangirb.pmpprep.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.util.concurrent.Executors

/**
 * One formula-lab drill problem marked or shown (index.html §10.06.10a), the shared `drills` log:
 * [generator] is the problem kind's stable id ("evm-variance", "float" …), [ok] true only for a right
 * answer ("close" and "shown" are not). Kept apart from attempts, so drills never touch accuracy,
 * but a day with one on it is a study day.
 */
data class DrillRun(val at: Long, val generator: String, val ok: Boolean)

/** One answer, as it happened. Analytics, readiness and the mistake journal are all computed from these. */
data class Attempt(
    val qid: String,
    val ecoTask: String,
    val domain: Domain,
    val approach: Approach?,
    val type: QType,
    val credit: Double,
    val correct: Boolean,
    val ms: Long,
    val at: Long,
    val source: String,
    /** Time from the question appearing to the first touch of an answer (reading time); 0 if not captured. */
    val firstTapMs: Long = 0,
    /** Longest stretch with no touch while the question was on screen; 0 if not captured. */
    val idleMs: Long = 0,
    /** Answer changes before checking that went from right to wrong, and from wrong to right. */
    val rightToWrong: Int = 0,
    val wrongToRight: Int = 0,
    /** Words to read: the stem plus the options. */
    val words: Int = 0,
    /** Answered against a clock: a mock, or timed practice. */
    val timed: Boolean = false,
    /** How sure you were before checking: [SURE], [THINK], [GUESS], or 0 if not asked. */
    val confidence: Int = 0,
    /** Options crossed out before checking. */
    val eliminated: Int = 0,
    /** The question's difficulty, 1–5, when the app that recorded it kept it (df); 0 if not. */
    val difficulty: Int = 0,
    /** Hint rungs opened before answering (h, web only). */
    val hints: Int = 0,
) {
    /** Right, but by your own account a guess: not evidence that you know it. */
    val luckyGuess: Boolean get() = correct && confidence == GUESS
    /** Answered with a hint open (on the web): like a lucky guess, it stays out of accuracy, pace and mastery. */
    val hinted: Boolean get() = hints > 0
    /** Wrong while sure: the most dangerous kind of mistake on exam day. */
    val sureWrong: Boolean get() = !correct && confidence == SURE

    companion object {
        const val GUESS = 1
        const val THINK = 2
        const val SURE = 3
    }
}

/** A sitting started or ended: a practice set, a guided session or a mock. Used to tell finished from abandoned. */
data class SittingEvent(val key: String, val kind: String, val at: Long, val planned: Int, val answered: Int, val finished: Boolean)

/** A one-tap check-in. [kind] is "pre" (before a session), "post" (after a mock) or "weekly"; values 1–5. */
data class CheckIn(val at: Long, val kind: String, val value: Int, val items: List<Int> = emptyList())

/**
 * Why a missed question went wrong, in the shared order (docs/progress-format.md §1). The words
 * shown come from [Rules.causes]; these are the fallbacks, and the enum keeps the ids type-safe.
 */
enum class RootCause(val title: String, val description: String) {
    KNOWLEDGE_GAP("Knowledge gap", "Didn't know the process, term or formula"),
    MISREAD_STEM("Misread the question", "Missed a word like FIRST, NEXT, NOT or BEST"),
    WRONG_MINDSET("Wrong mindset", "Escalated, avoided or acted before understanding"),
    TWO_PLAUSIBLE("Two plausible answers", "Narrowed it to two and picked the weaker one"),
    CALCULATION("Calculation slip", "Arithmetic or an inverted ratio"),
    RUSHED("Rushed", "Answered before reading every option");

    companion object {
        /** A shared id, or one of the web's old ids. */
        fun of(id: String?): RootCause? = entries.firstOrNull { it.name == id } ?: when (id) {
            "knowledge" -> KNOWLEDGE_GAP
            "misread" -> MISREAD_STEM
            "rushed" -> RUSHED
            "mindset" -> WRONG_MINDSET
            "tossup" -> TWO_PLAUSIBLE
            else -> null
        }
    }
}

data class MistakeTag(val cause: RootCause, val note: String, val at: Long)

data class ScenarioRun(val id: String, val picked: String, val rank: Rank, val at: Long)

data class MockResult(
    val at: Long,
    val title: String,
    val n: Int,
    val right: Int,
    val usedMs: Long,
    /** domain → (right, of) */
    val domains: Map<Domain, Pair<Int, Int>>,
) {
    val pct: Double get() = if (n == 0) 0.0 else right.toDouble() / n
}

data class StudyPlan(
    val examDate: Long? = null,
    val hoursPerWeek: Int = 10,
    /** java.util.Calendar.DAY_OF_WEEK values; Monday to Friday unless chosen otherwise. */
    val studyDays: Set<Int> = setOf(2, 3, 4, 5, 6),
    /** Set when the first-run setup (date, hours, days, diagnostic) has been finished or skipped. */
    val onboarded: Boolean = false,
    /** One missed day a week doesn't break the streak. */
    val restDays: Boolean = true,
    /** New lessons in the classroom order (Extras.courseOrder) rather than weakest domain first; plan.order "course". */
    val byArea: Boolean = false,
)

/** The cold 25-question level check the plan and the coach aim from. */
data class DiagnosticResult(
    val at: Long,
    val n: Int,
    val right: Int,
    /** domain → (right, of) */
    val domains: Map<Domain, Pair<Int, Int>>,
) {
    val pct: Double get() = if (n == 0) 0.0 else right.toDouble() / n
    fun domainPct(d: Domain): Double? = domains[d]?.let { if (it.second == 0) null else it.first.toDouble() / it.second }
}

/** One turn of the conversation with the coach. role is "user" or "model". */
data class ChatTurn(val role: String, val text: String, val at: Long)

/**
 * Everything the person has done, in one JSON file in the app's private
 * storage. Every change bumps [revision] (which the UI observes) and is
 * written in the background.
 *
 * The file is the shared progress document (docs/progress-format.md §2), so a
 * backup or a cloud sync copy moves between the web, Android and iOS apps.
 * What Android doesn't understand (the web's own block, keys a newer app
 * added) is kept as it came and written back unchanged.
 */
class ProgressStore(private val dir: File) {
    private val file = File(dir, "progress.json")
    private val io = Executors.newSingleThreadExecutor()

    /** When this phone's progress last changed (ms). The merge gives the later copy the win on a both-sides edit. */
    var savedAt = 0L
        private set

    /**
     * This install's id in the document's "device". Its own file, kept out of Android's cloud backup,
     * so a phone restored from another's backup still gets an id of its own.
     */
    val deviceId: String by lazy {
        val f = File(dir, DEVICE_FILE)
        runCatching { f.readText().trim() }.getOrNull()?.takeIf { it.isNotEmpty() } ?: java.util.UUID.randomUUID().toString().also { id ->
            runCatching { dir.mkdirs(); f.writeText(id) }
        }
    }

    /** Top-level keys this app doesn't read (the web and native blocks, keys from newer apps), kept verbatim. */
    private var passthrough = JSONObject()

    private val _revision = MutableStateFlow(0)
    val revision: StateFlow<Int> = _revision.asStateFlow()

    val attempts = ArrayList<Attempt>()
    val flags = LinkedHashSet<String>()
    val notes = HashMap<String, String>()
    val mistakeTags = HashMap<String, MistakeTag>()
    val cards = HashMap<String, CardState>()
    val scenarioRuns = ArrayList<ScenarioRun>()
    val mocks = ArrayList<MockResult>()
    val lessonsDone = LinkedHashSet<String>()
    var plan = StudyPlan()
        private set
    /** The free-form notes page, in the light markup the notes editor writes. */
    var notepad = ""
        private set
    val diagnostics = ArrayList<DiagnosticResult>()
    val chat = ArrayList<ChatTurn>()
    /** Coach message id → time before which it stays hidden. */
    val coachSnoozed = HashMap<String, Long>()
    /** Every guided-path session finished, newest last. */
    val sessionResults = ArrayList<SessionResult>()
    val sittings = ArrayList<SittingEvent>()
    val checkIns = ArrayList<CheckIn>()
    /** One report per week, the current week's kept up to date until the week ends. */
    val weekly = ArrayList<WeeklyReport>()
    /** Flashcards you made from your own mistakes (deck [MY_DECK]). */
    val myCards = ArrayList<Flashcard>()
    /** Formula-lab drills, oldest first, capped at [MAX_DRILLS]. */
    val drills = ArrayList<DrillRun>()
    /** Local dates the app was opened (YYYY-MM-DD), sorted, newest [Support.MAX_OPENS] (docs/progress-format.md §4). */
    val opens = ArrayList<String>()
    /** The support reminder's state (§4). */
    var support = SupportState()
        private set

    /** Latest attempt per question. */
    val last = HashMap<String, Attempt>()

    init {
        if (file.exists()) try { fromJson(JSONObject(file.readText())) } catch (_: Exception) { /* start clean */ }
    }

    @Synchronized
    fun record(a: Attempt) {
        attempts += a
        if (attempts.size > MAX_ATTEMPTS) attempts.subList(0, attempts.size - MAX_ATTEMPTS).clear()
        last[a.qid] = a
        changed()
    }

    @Synchronized
    fun toggleFlag(id: String): Boolean {
        val on = if (flags.remove(id)) false else { flags += id; true }
        changed()
        return on
    }

    @Synchronized
    fun setNote(id: String, text: String) {
        if (text.isBlank()) notes.remove(id) else notes[id] = text
        changed()
    }

    @Synchronized
    fun tagMistake(id: String, cause: RootCause, note: String) {
        mistakeTags[id] = MistakeTag(cause, note, System.currentTimeMillis())
        changed()
    }

    @Synchronized
    fun setCard(id: String, s: CardState) {
        cards[id] = s
        changed()
    }

    @Synchronized
    fun recordScenario(r: ScenarioRun) {
        scenarioRuns += r
        changed()
    }

    @Synchronized
    fun recordMock(m: MockResult) {
        mocks += m
        changed()
    }

    @Synchronized
    fun setLessonDone(id: String, done: Boolean) {
        if (done) lessonsDone += id else lessonsDone -= id
        changed()
    }

    @Synchronized
    fun setPlan(p: StudyPlan) {
        plan = p
        changed()
    }

    @Synchronized
    fun recordSession(r: SessionResult) {
        sessionResults += r
        changed()
    }

    @Synchronized
    fun recordSitting(e: SittingEvent) {
        sittings += e
        if (sittings.size > 2000) sittings.subList(0, sittings.size - 2000).clear()
        changed()
    }

    @Synchronized
    fun recordDrill(d: DrillRun) {
        drills += d
        if (drills.size > MAX_DRILLS) drills.subList(0, drills.size - MAX_DRILLS).clear()
        changed()
    }

    /** Adds [today] to the opens; a change only when the date is new. */
    @Synchronized
    fun recordOpen(today: String) {
        if (today in opens) return
        val next = Support.recordOpen(opens, today)
        opens.clear(); opens += next
        changed()
    }

    @Synchronized
    fun setSupport(s: SupportState) {
        if (s == support) return
        support = s
        changed()
    }

    @Synchronized
    fun recordCheckIn(c: CheckIn) {
        checkIns += c
        changed()
    }

    /** Saves this week's report, replacing the week's earlier version. Doesn't bump the revision: it's derived. */
    @Synchronized
    fun putWeekly(r: WeeklyReport) {
        val i = weekly.indexOfFirst { it.weekStart == r.weekStart }
        if (i >= 0) { if (weekly[i] == r) return; weekly[i] = r } else weekly += r
        weekly.sortBy { it.weekStart }
        if (weekly.size > 104) weekly.subList(0, weekly.size - 104).clear()
        save()
    }

    @Synchronized
    fun addMyCard(c: Flashcard) {
        myCards.removeAll { it.id == c.id }
        myCards += c
        changed()
    }

    @Synchronized
    fun removeMyCard(id: String) {
        myCards.removeAll { it.id == id }
        cards.remove(id)
        changed()
    }

    @Synchronized
    fun setNotepad(text: String) {
        if (text == notepad) return
        notepad = text
        changed()
    }

    /** The person's own chat commands, the shared `commands` slice ([ChatCommands]). It passes through as a whole value. */
    val chatCommands: List<ChatCommand> @Synchronized get() = ChatCommands.parse(passthrough.optJSONArray("commands"))

    @Synchronized
    fun setChatCommands(list: List<ChatCommand>) {
        if (list.isEmpty()) passthrough.remove("commands") else passthrough.put("commands", ChatCommands.toJson(list))
        changed()
    }

    @Synchronized
    fun recordDiagnostic(d: DiagnosticResult) {
        diagnostics += d
        changed()
    }

    val diagnostic: DiagnosticResult? get() = diagnostics.maxByOrNull { it.at }

    @Synchronized
    fun addChat(t: ChatTurn) {
        chat += t
        if (chat.size > MAX_CHAT) chat.subList(0, chat.size - MAX_CHAT).clear()
        changed()
    }

    @Synchronized
    fun clearChat() {
        chat.clear()
        changed()
    }

    @Synchronized
    fun snooze(id: String, until: Long) {
        coachSnoozed[id] = until
        changed()
    }

    @Synchronized
    fun reset() {
        attempts.clear(); flags.clear(); notes.clear(); mistakeTags.clear(); cards.clear()
        scenarioRuns.clear(); mocks.clear(); lessonsDone.clear(); last.clear(); plan = StudyPlan()
        notepad = ""; diagnostics.clear(); chat.clear(); coachSnoozed.clear(); sessionResults.clear()
        sittings.clear(); checkIns.clear(); weekly.clear(); myCards.clear(); drills.clear()
        // opens and support survive: a reset is about study progress, and someone who pressed Support shouldn't be asked again at once.
        changed()
    }

    /** Replaces everything with a backup: this app's own, or the shared document from any of the apps. */
    @Synchronized
    fun restore(text: String) {
        val o = try {
            JSONObject(text.trim().removePrefix("\uFEFF"))
        } catch (e: Exception) {
            throw IllegalArgumentException("This file isn't a PMP Prep progress backup: it isn't valid JSON.")
        }
        checkReadable(o)
        fromJson(o)
        changed()
    }

    /** Takes the merged document from a cloud sync as this phone's progress. */
    @Synchronized
    fun applySynced(o: JSONObject) {
        checkReadable(o)
        fromJson(o)
        changed()
    }

    private fun changed() {
        savedAt = System.currentTimeMillis()
        _revision.value = _revision.value + 1
        save()
    }

    private fun save() {
        val snapshot = toJson().toString()
        io.execute {
            try {
                dir.mkdirs()
                val tmp = File(dir, "progress.json.tmp")
                tmp.writeText(snapshot)
                tmp.renameTo(file)
            } catch (_: Exception) { /* the next change will try again */ }
        }
    }

    /* ── JSON ────────────────────────────────────────────────────────────── */

    @Synchronized
    fun toJson(): JSONObject = JSONObject().apply {
        // Unknown keys first, so nothing they hold can shadow a slice this app writes.
        passthrough.keys().forEach { k -> put(k, passthrough.get(k)) }
        put("type", TYPE)
        put("version", VERSION)
        put("savedAt", savedAt)
        put("device", JSONObject().put("app", "android").put("id", deviceId))
        put("attempts", JSONArray().apply {
            attempts.forEach { a ->
                put(JSONObject().apply {
                    put("q", a.qid); put("t", a.ecoTask); put("d", a.domain.id); put("ap", a.approach?.id ?: "")
                    put("ty", a.type.id); put("cr", a.credit); put("ok", a.correct); put("ms", a.ms); put("at", a.at); put("src", a.source)
                    if (a.firstTapMs > 0) put("ft", a.firstTapMs)
                    if (a.idleMs > 0) put("idle", a.idleMs)
                    if (a.rightToWrong > 0) put("rw", a.rightToWrong)
                    if (a.wrongToRight > 0) put("wr", a.wrongToRight)
                    if (a.words > 0) put("w", a.words)
                    if (a.timed) put("tm", true)
                    if (a.confidence > 0) put("cf", a.confidence)
                    if (a.eliminated > 0) put("el", a.eliminated)
                    if (a.difficulty > 0) put("df", a.difficulty)
                    if (a.hints > 0) put("h", a.hints)
                })
            }
        })
        put("flags", JSONArray(flags.toList()))
        put("notes", JSONObject(notes as Map<*, *>))
        put("mistakes", JSONObject().apply {
            mistakeTags.forEach { (k, v) -> put(k, JSONObject().put("c", v.cause.name).put("n", v.note).put("at", v.at)) }
        })
        put("cards", JSONObject().apply {
            cards.forEach { (k, s) ->
                put(k, JSONArray().put(s.ease).put(s.interval).put(s.reps).put(s.lapses).put(s.due).put(s.last).put(s.first)
                    .put(if (s.suspended) 1 else 0))
            }
        })
        put("scenarios", JSONArray().apply {
            scenarioRuns.forEach { put(JSONObject().put("id", it.id).put("p", it.picked).put("r", it.rank.id).put("at", it.at)) }
        })
        put("mocks", JSONArray().apply {
            mocks.forEach { m ->
                put(JSONObject().apply {
                    put("at", m.at); put("title", m.title); put("n", m.n); put("right", m.right); put("usedMs", m.usedMs)
                    put("domains", JSONObject().apply { m.domains.forEach { (d, p) -> put(d.id, JSONArray().put(p.first).put(p.second)) } })
                })
            }
        })
        put("lessons", JSONArray(lessonsDone.toList()))
        put("plan", JSONObject().put("examDate", plan.examDate ?: 0L).put("hours", plan.hoursPerWeek)
            .put("days", JSONArray(plan.studyDays.toList())).put("onboarded", plan.onboarded).put("rest", plan.restDays)
            .apply { if (plan.byArea) put("order", "course") })
        put("sessions", JSONArray().apply {
            sessionResults.forEach { r ->
                put(JSONObject().put("k", r.key).put("kind", r.kind.name).put("t", r.taskId ?: "").put("d", r.domain?.id ?: "")
                    .put("s", r.score).put("m", r.minutes).put("at", r.at))
            }
        })
        put("notepad", notepad)
        put("diagnostics", JSONArray().apply {
            diagnostics.forEach { d ->
                put(JSONObject().apply {
                    put("at", d.at); put("n", d.n); put("right", d.right)
                    put("domains", JSONObject().apply { d.domains.forEach { (k, p) -> put(k.id, JSONArray().put(p.first).put(p.second)) } })
                })
            }
        })
        put("chat", JSONArray().apply { chat.forEach { put(JSONObject().put("r", it.role).put("t", it.text).put("at", it.at)) } })
        put("snoozed", JSONObject(coachSnoozed as Map<*, *>))
        put("sittings", JSONArray().apply {
            sittings.forEach { put(JSONObject().put("k", it.key).put("kind", it.kind).put("at", it.at).put("p", it.planned).put("a", it.answered).put("f", it.finished)) }
        })
        put("checkins", JSONArray().apply {
            checkIns.forEach { put(JSONObject().put("at", it.at).put("kind", it.kind).put("v", it.value).put("items", JSONArray(it.items))) }
        })
        put("weekly", JSONArray().apply { weekly.forEach { put(it.toJson()) } })
        put("opens", JSONArray(opens.toList()))
        put("support", JSONObject().apply {
            if (support.pressedAt > 0) put("pressedAt", support.pressedAt)
            if (support.shownAt > 0) put("shownAt", support.shownAt)
            if (support.anchor.isNotEmpty()) put("anchor", support.anchor)
        })
        put("drills", JSONArray().apply { drills.forEach { put(JSONObject().put("at", it.at).put("g", it.generator).put("ok", it.ok)) } })
        put("mycards", JSONArray().apply {
            myCards.forEach { put(JSONObject().put("id", it.id).put("t", it.ecoTask ?: "").put("f", it.front).put("b", it.back).put("n", it.note)) }
        })
    }

    private fun fromJson(o: JSONObject) {
        passthrough = JSONObject().apply { o.keys().forEach { k -> if (k !in KNOWN) put(k, o.get(k)) } }
        savedAt = o.optLong("savedAt", 0L)
        attempts.clear(); flags.clear(); notes.clear(); mistakeTags.clear(); cards.clear()
        scenarioRuns.clear(); mocks.clear(); lessonsDone.clear(); last.clear()
        diagnostics.clear(); chat.clear(); coachSnoozed.clear(); sessionResults.clear()
        sittings.clear(); checkIns.clear(); weekly.clear(); myCards.clear(); drills.clear()
        notepad = o.optString("notepad", "")
        opens.clear()
        o.optJSONArray("opens")?.let { arr ->
            val dates = (0 until arr.length()).map { arr.optString(it) }.filter { DATE.matches(it) }
            opens += dates.toSortedSet().toList().takeLast(Support.MAX_OPENS)
        }
        support = o.optJSONObject("support")?.let {
            SupportState(it.optLong("pressedAt"), it.optLong("shownAt"), it.optString("anchor").takeIf { a -> DATE.matches(a) } ?: "")
        } ?: SupportState()
        o.optJSONArray("mycards")?.let { arr ->
            for (i in 0 until arr.length()) {
                val c = arr.optJSONObject(i) ?: continue
                myCards += Flashcard(c.optString("id"), MY_DECK, c.optString("t").ifEmpty { null }, c.optString("f"), c.optString("b"), c.optString("n"))
            }
        }
        o.optJSONArray("drills")?.let { arr ->
            for (i in 0 until arr.length()) {
                val d = arr.optJSONObject(i) ?: continue
                val at = d.optLong("at")
                if (at > 0) drills += DrillRun(at, d.optString("g", "unknown"), d.optBoolean("ok"))
            }
            drills.sortBy { it.at }
            if (drills.size > MAX_DRILLS) drills.subList(0, drills.size - MAX_DRILLS).clear()
        }
        o.optJSONArray("sittings")?.let { arr ->
            for (i in 0 until arr.length()) {
                val e = arr.optJSONObject(i) ?: continue
                sittings += SittingEvent(e.optString("k"), e.optString("kind"), e.optLong("at"), e.optInt("p"), e.optInt("a"), e.optBoolean("f"))
            }
        }
        o.optJSONArray("checkins")?.let { arr ->
            for (i in 0 until arr.length()) {
                val c = arr.optJSONObject(i) ?: continue
                val items = c.optJSONArray("items")
                checkIns += CheckIn(c.optLong("at"), c.optString("kind"), c.optInt("v"), (0 until (items?.length() ?: 0)).map { items!!.optInt(it) })
            }
        }
        o.optJSONArray("weekly")?.let { arr -> for (i in 0 until arr.length()) arr.optJSONObject(i)?.let { weekly += WeeklyReport.fromJson(it) } }
        o.optJSONArray("sessions")?.let { arr ->
            for (i in 0 until arr.length()) {
                val r = arr.optJSONObject(i) ?: continue
                val kind = runCatching { SessionKind.valueOf(r.optString("kind")) }.getOrNull() ?: continue
                sessionResults += SessionResult(r.optString("k"), kind, r.optString("t").ifEmpty { null }, Domain.of(r.optString("d")),
                    r.optDouble("s", 0.0), r.optInt("m"), r.optLong("at"))
            }
        }
        o.optJSONArray("diagnostics")?.let { arr ->
            for (i in 0 until arr.length()) {
                val d = arr.optJSONObject(i) ?: continue
                val dm = d.optJSONObject("domains")
                diagnostics += DiagnosticResult(d.optLong("at"), d.optInt("n"), d.optInt("right"),
                    Domain.entries.mapNotNull { x -> dm?.optJSONArray(x.id)?.let { x to (it.optInt(0) to it.optInt(1)) } }.toMap())
            }
        }
        o.optJSONArray("chat")?.let { arr ->
            for (i in 0 until arr.length()) {
                val c = arr.optJSONObject(i) ?: continue
                chat += ChatTurn(c.optString("r"), c.optString("t"), c.optLong("at"))
            }
        }
        o.optJSONObject("snoozed")?.let { sn -> sn.keys().forEach { coachSnoozed[it] = sn.optLong(it) } }
        o.optJSONArray("attempts")?.let { arr ->
            for (i in 0 until arr.length()) {
                val a = arr.optJSONObject(i) ?: continue
                val at = Attempt(
                    qid = a.optString("q"), ecoTask = a.optString("t"),
                    domain = Domain.of(a.optString("d")) ?: Domain.PROCESS, approach = Approach.of(a.optString("ap")),
                    type = QType.of(a.optString("ty")) ?: QType.SINGLE, credit = a.optDouble("cr", 0.0),
                    correct = a.optBoolean("ok"), ms = a.optLong("ms"), at = a.optLong("at"), source = a.optString("src"),
                    firstTapMs = a.optLong("ft"), idleMs = a.optLong("idle"), rightToWrong = a.optInt("rw"), wrongToRight = a.optInt("wr"),
                    words = a.optInt("w"), timed = a.optBoolean("tm"), confidence = confidenceOf(a.opt("cf")), eliminated = a.optInt("el"),
                    difficulty = a.optInt("df"), hints = a.optInt("h"),
                )
                attempts += at
                last[at.qid] = at
            }
        }
        o.optJSONArray("flags")?.let { for (i in 0 until it.length()) flags += it.optString(i) }
        o.optJSONObject("notes")?.let { n -> n.keys().forEach { notes[it] = n.optString(it) } }
        o.optJSONObject("mistakes")?.let { m ->
            m.keys().forEach { k ->
                val v = m.optJSONObject(k) ?: return@forEach
                val cause = RootCause.of(v.optString("c")) ?: RootCause.KNOWLEDGE_GAP
                mistakeTags[k] = MistakeTag(cause, v.optString("n"), v.optLong("at"))
            }
        }
        o.optJSONObject("cards")?.let { c ->
            c.keys().forEach { k ->
                val a = c.optJSONArray(k) ?: return@forEach
                // Seven numbers from older Android backups; the eighth, suspended, from the shared format.
                cards[k] = CardState(a.optDouble(0, 2.5), a.optInt(1), a.optInt(2), a.optInt(3), a.optLong(4), a.optLong(5), a.optLong(6),
                    suspended = a.optInt(7) != 0)
            }
        }
        o.optJSONArray("scenarios")?.let { arr ->
            for (i in 0 until arr.length()) {
                val s = arr.optJSONObject(i) ?: continue
                scenarioRuns += ScenarioRun(s.optString("id"), s.optString("p"), Rank.of(s.optString("r")), s.optLong("at"))
            }
        }
        o.optJSONArray("mocks")?.let { arr ->
            for (i in 0 until arr.length()) {
                val m = arr.optJSONObject(i) ?: continue
                val d = m.optJSONObject("domains")
                mocks += MockResult(
                    at = m.optLong("at"), title = m.optString("title"), n = m.optInt("n"), right = m.optInt("right"),
                    usedMs = m.optLong("usedMs"),
                    domains = Domain.entries.mapNotNull { dm ->
                        d?.optJSONArray(dm.id)?.let { dm to (it.optInt(0) to it.optInt(1)) }
                    }.toMap(),
                )
            }
        }
        o.optJSONArray("lessons")?.let { for (i in 0 until it.length()) lessonsDone += it.optString(i) }
        o.optJSONObject("plan")?.let {
            val date = it.optLong("examDate")
            val days = it.optJSONArray("days")?.let { a -> (0 until a.length()).map { i -> a.optInt(i) }.toSet() }
            // Older backups have no onboarded flag: a date plus a diagnostic means setup was done.
            plan = StudyPlan(if (date > 0) date else null, it.optInt("hours", 10), days?.ifEmpty { null } ?: StudyPlan().studyDays,
                it.optBoolean("onboarded", date > 0 && diagnostics.isNotEmpty()), it.optBoolean("rest", true),
                it.optString("order") == "course")
        }
    }

    companion object {
        /** The shared document every app writes (docs/progress-format.md §2). */
        const val TYPE = "pmp-progress"
        const val VERSION = 3
        /** What this app wrote before the shared format; still read. */
        const val LEGACY_TYPE = "pmp-android-progress"
        const val DEVICE_FILE = "device-id"
        const val MY_DECK = "mine"

        /** Every top-level key this app reads and writes itself. The rest pass through untouched. */
        val KNOWN = setOf(
            "type", "version", "savedAt", "device", "attempts", "flags", "notes", "mistakes", "cards", "scenarios", "mocks",
            "lessons", "plan", "sessions", "notepad", "diagnostics", "chat", "snoozed", "sittings", "checkins", "weekly", "mycards",
            "drills", "opens", "support",
        )
        private val DATE = Regex("\\d{4}-\\d{2}-\\d{2}")

        /**
         * Throws, with a message to show, unless [o] is progress this app can read: the shared
         * version 3 document (from any of the apps) or Android's own version 2.
         */
        fun checkReadable(o: JSONObject) {
            when (val type = o.optString("type")) {
                LEGACY_TYPE -> return
                TYPE -> {
                    val v = o.optInt("version", 0)
                    if (v == VERSION) return
                    if (v > VERSION) throw IllegalArgumentException("This backup was made by a newer version of PMP Prep. Update the app, then restore it.")
                    throw IllegalArgumentException("This is a backup from an older version of the web app. Open it in the web app, " +
                        "save a new backup there, and restore that one.")
                }
                "" -> throw IllegalArgumentException("This isn't a PMP Prep progress backup.")
                "pmp-pack" -> throw IllegalArgumentException("This is a question pack, not a progress backup. Add it under Question banks.")
                else -> throw IllegalArgumentException("This isn't a PMP Prep progress backup (its type is \"$type\").")
            }
        }

        /** Confidence is 3 / 2 / 1; the web's old words map to it. */
        private fun confidenceOf(v: Any?): Int = when (v) {
            is Number -> v.toInt()
            "sure" -> Attempt.SURE
            "unsure", "Not sure" -> Attempt.THINK
            else -> 0
        }
        private const val MAX_ATTEMPTS = 20000
        private const val MAX_CHAT = 60
        /** The shared cap for the drills log (docs/progress-format.md §3). */
        const val MAX_DRILLS = 2000
    }
}
