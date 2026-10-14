package com.jahangirb.pmpprep.data

import org.json.JSONArray
import org.json.JSONObject
import java.util.Calendar
import kotlin.math.ln
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt
import kotlin.random.Random

/*
 * Smart study reminders: learns which hours get you studying, from
 *   - your phone's screen and app-switch activity over the last two weeks (only with Usage access),
 *   - when you study in the app and how long the app stays open at each hour,
 *   - whether each reminder was opened, how quickly, and whether you then studied.
 * Every hour of the waking day carries a score; each day the app picks a few hours by
 * Thompson sampling (mostly the best, sometimes a less-tried one), within a daily cap.
 * Everything here is plain data and pure functions; the Android side only stores and schedules.
 */

enum class ReminderKind { STUDY, STREAK, SUMMARY }
enum class HourRule { AUTO, PINNED, BLOCKED }

/** Parts of the day offered at setup ("When do you usually study?"). */
enum class StudyWindow(val label: String, val hours: IntRange) {
    EARLY("Early morning", 5..7), MORNING("Morning", 8..11), LUNCH("Lunch", 12..13),
    AFTERNOON("Afternoon", 14..16), EVENING("Evening", 17..20), LATE("Late evening", 21..23),
}

/** One reminder sent. [id] is the time it was sent. */
data class ReminderLog(
    val id: Long,
    val hour: Int,
    val kind: ReminderKind,
    val openedAt: Long? = null,
    val tapped: Boolean = false,
    val studiedMinutes: Int = 0,
    val notToday: Boolean = false,
)

data class ReminderSettings(
    val enabled: Boolean = true,
    /** Waking hours: reminders only between wake + 1 and sleep − 1. */
    val wake: Int = 7,
    val sleep: Int = 23,
    val windows: Set<StudyWindow> = emptySet(),
    val rules: Map<Int, HourRule> = emptyMap(),
    val pausedUntil: Long = 0L,
    val usePhoneUsage: Boolean = false,
    /** Set once the setup question has been answered or skipped. */
    val asked: Boolean = false,
)

/** What reminders need to know about your study, saved by the app so they can be written while it is closed. */
data class ReminderSnapshot(
    val day: Long,
    val nextTitle: String?,
    val nextMinutes: Int,
    val nextWhat: String?,
    val cardsDue: Int,
    val streak: Int,
    val studyDays: Set<Int>,
    val todayMinutes: Int,
    val targetMinutes: Int,
    val lastStudyAt: Long,
    val finalWeek: Boolean,
    val readiness: Int,
    val mastered: Int,
    val weekMinutes: Int,
    val nextWeekFocus: String?,
    /** Today can be this week's rest day: no streak-saver. */
    val restDayFree: Boolean = false,
)

/** A reminder planned for a time today. */
data class PlannedReminder(val at: Long, val hour: Int, val kind: ReminderKind)

class ReminderState {
    var settings = ReminderSettings()
    val logs = ArrayList<ReminderLog>()
    /** Minutes the app has been open at each hour of the day, all time (your idea Y3). */
    val appMinutes = DoubleArray(24)
    /** Share of recent days with phone activity at each hour (Y1); null without Usage access. */
    var phoneUsage: DoubleArray? = null
    var phoneUsageAt = 0L
    /** Share of your active study days on which you studied at each hour, and how many such days. */
    var studyHabit = DoubleArray(24)
    var studyHabitDays = 0
    var snapshot: ReminderSnapshot? = null
    /** Readiness and mastered tasks at the last weekly summary: the baseline for the next one. */
    var lastSummaryReadiness = -1
    var lastSummaryMastered = -1
    var backoffUntil = 0L
    var notTodayDay = 0L
    /** Today's plan, chosen once per day so reopening the app doesn't reshuffle it. */
    var planDay = 0L
    val plan = ArrayList<PlannedReminder>()

    fun toJson(): JSONObject = JSONObject().apply {
        val s = settings
        put("settings", JSONObject().apply {
            put("enabled", s.enabled); put("wake", s.wake); put("sleep", s.sleep)
            put("windows", JSONArray(s.windows.map { it.name }))
            put("rules", JSONObject().apply { s.rules.forEach { (h, r) -> put(h.toString(), r.name) } })
            put("pausedUntil", s.pausedUntil); put("usePhoneUsage", s.usePhoneUsage); put("asked", s.asked)
        })
        put("logs", JSONArray(logs.map { l ->
            JSONObject().put("id", l.id).put("hour", l.hour).put("kind", l.kind.name).put("openedAt", l.openedAt ?: 0L)
                .put("tapped", l.tapped).put("studied", l.studiedMinutes).put("notToday", l.notToday)
        }))
        put("appMinutes", JSONArray(appMinutes.toList()))
        phoneUsage?.let { put("phoneUsage", JSONArray(it.toList())) }
        put("phoneUsageAt", phoneUsageAt)
        put("studyHabit", JSONArray(studyHabit.toList())); put("studyHabitDays", studyHabitDays)
        snapshot?.let { n ->
            put("snapshot", JSONObject().put("day", n.day).put("nextTitle", n.nextTitle ?: "").put("nextMinutes", n.nextMinutes)
                .put("nextWhat", n.nextWhat ?: "").put("cardsDue", n.cardsDue).put("streak", n.streak)
                .put("studyDays", JSONArray(n.studyDays.toList())).put("todayMinutes", n.todayMinutes).put("targetMinutes", n.targetMinutes)
                .put("lastStudyAt", n.lastStudyAt).put("finalWeek", n.finalWeek).put("readiness", n.readiness).put("mastered", n.mastered)
                .put("weekMinutes", n.weekMinutes).put("nextWeekFocus", n.nextWeekFocus ?: "").put("restDayFree", n.restDayFree))
        }
        put("lastSummaryReadiness", lastSummaryReadiness); put("lastSummaryMastered", lastSummaryMastered)
        put("backoffUntil", backoffUntil); put("notTodayDay", notTodayDay); put("planDay", planDay)
        put("plan", JSONArray(plan.map { JSONObject().put("at", it.at).put("hour", it.hour).put("kind", it.kind.name) }))
    }

    companion object {
        private fun doubles(a: JSONArray?, into: DoubleArray) { if (a != null) for (i in 0 until min(a.length(), into.size)) into[i] = a.optDouble(i, 0.0) }
        private fun str(o: JSONObject, k: String) = o.optString(k, "").ifEmpty { null }

        fun fromJson(o: JSONObject): ReminderState = ReminderState().apply {
            o.optJSONObject("settings")?.let { s ->
                val w = s.optJSONArray("windows")
                val r = s.optJSONObject("rules")
                settings = ReminderSettings(
                    enabled = s.optBoolean("enabled", true), wake = s.optInt("wake", 7), sleep = s.optInt("sleep", 23),
                    windows = (0 until (w?.length() ?: 0)).mapNotNull { i -> runCatching { StudyWindow.valueOf(w!!.getString(i)) }.getOrNull() }.toSet(),
                    rules = r?.keys()?.asSequence()?.mapNotNull { k -> runCatching { k.toInt() to HourRule.valueOf(r.getString(k)) }.getOrNull() }?.toMap() ?: emptyMap(),
                    pausedUntil = s.optLong("pausedUntil", 0L), usePhoneUsage = s.optBoolean("usePhoneUsage", false), asked = s.optBoolean("asked", false),
                )
            }
            o.optJSONArray("logs")?.let { a ->
                for (i in 0 until a.length()) {
                    val l = a.getJSONObject(i)
                    logs += ReminderLog(l.getLong("id"), l.getInt("hour"), runCatching { ReminderKind.valueOf(l.getString("kind")) }.getOrDefault(ReminderKind.STUDY),
                        l.optLong("openedAt", 0L).takeIf { it > 0 }, l.optBoolean("tapped"), l.optInt("studied"), l.optBoolean("notToday"))
                }
            }
            doubles(o.optJSONArray("appMinutes"), appMinutes)
            o.optJSONArray("phoneUsage")?.let { a -> phoneUsage = DoubleArray(24).also { doubles(a, it) } }
            phoneUsageAt = o.optLong("phoneUsageAt", 0L)
            doubles(o.optJSONArray("studyHabit"), studyHabit); studyHabitDays = o.optInt("studyHabitDays", 0)
            o.optJSONObject("snapshot")?.let { n ->
                val d = n.optJSONArray("studyDays")
                snapshot = ReminderSnapshot(n.optLong("day"), str(n, "nextTitle"), n.optInt("nextMinutes"), str(n, "nextWhat"), n.optInt("cardsDue"),
                    n.optInt("streak"), (0 until (d?.length() ?: 0)).map { d!!.getInt(it) }.toSet(), n.optInt("todayMinutes"), n.optInt("targetMinutes"),
                    n.optLong("lastStudyAt"), n.optBoolean("finalWeek"), n.optInt("readiness"), n.optInt("mastered"), n.optInt("weekMinutes"), str(n, "nextWeekFocus"), n.optBoolean("restDayFree"))
            }
            lastSummaryReadiness = o.optInt("lastSummaryReadiness", -1); lastSummaryMastered = o.optInt("lastSummaryMastered", -1)
            backoffUntil = o.optLong("backoffUntil", 0L); notTodayDay = o.optLong("notTodayDay", 0L); planDay = o.optLong("planDay", 0L)
            o.optJSONArray("plan")?.let { a ->
                for (i in 0 until a.length()) { val p = a.getJSONObject(i); plan += PlannedReminder(p.getLong("at"), p.getInt("hour"), ReminderKind.valueOf(p.getString("kind"))) }
            }
        }
    }
}

/** How well an hour works, as a Beta distribution over "a reminder then gets you studying". */
data class HourScore(val hour: Int, val prior: Double, val successes: Double, val failures: Double, val tried: Int, val rule: HourRule) {
    val alpha get() = 1 + ReminderBrain.PRIOR_STRENGTH * prior + successes
    val beta get() = 1 + ReminderBrain.PRIOR_STRENGTH * (1 - prior) + failures
    /** The expected chance; what the best-times chart shows. */
    val mean get() = alpha / (alpha + beta)
}

object ReminderBrain {
    const val HOUR = 3_600_000L
    const val MINUTE = 60_000L
    /** A reminder counts as answered if the app is opened within this time. */
    const val RESPONSE_WINDOW = 60 * MINUTE
    /** How many reminders' worth of evidence the starting guess is worth. */
    const val PRIOR_STRENGTH = 4.0
    /** Older outcomes count for less (half after this many days), so a changed routine is re-learned. */
    const val HALF_LIFE_DAYS = 30.0
    const val LEARNING_REMINDERS = 20
    /** Once learned, extra reminders go only to hours that have proven themselves (expected chance at least this)... */
    const val GOOD_HOUR = 0.4
    /** ...plus, on about one day in four, one try of another hour, so a changed routine is still noticed. */
    const val EXPLORE_RATE = 0.25
    const val SUMMARY_HOUR = 18
    const val SUMMARY_MINUTE = 30

    fun weekday(t: Long): Int = Calendar.getInstance().apply { timeInMillis = t }.get(Calendar.DAY_OF_WEEK)

    /** The hours reminders may use: from an hour after waking to an hour before sleep. */
    fun wakingHours(s: ReminderSettings): List<Int> = ((s.wake + 1)..(s.sleep - 1)).filter { it in 0..23 }

    /**
     * The starting guess for an hour, before reminders have taught anything. Phone activity (Y1)
     * and your answer at setup carry it in the first days; your own study times take over as they build up.
     */
    fun prior(st: ReminderState, h: Int): Double {
        var sum = 0.0; var weight = 0.0
        fun add(v: Double, w: Double) { sum += v * w; weight += w }
        st.phoneUsage?.let { add(0.1 + 0.8 * it[h].coerceIn(0.0, 1.0), 1.0) }
        if (st.settings.windows.isNotEmpty()) add(if (st.settings.windows.any { h in it.hours }) 0.7 else 0.2, 1.0)
        if (st.studyHabitDays >= 3) add(0.1 + 0.8 * st.studyHabit[h].coerceIn(0.0, 1.0), min(3.0, st.studyHabitDays / 5.0))
        val maxApp = st.appMinutes.maxOrNull() ?: 0.0
        if (maxApp >= 30) add(0.1 + 0.8 * st.appMinutes[h] / maxApp, 0.5)
        return if (weight == 0.0) 0.3 else sum / weight
    }

    /** How a past reminder turned out: 1 studied, 0.6 opened only, 0 ignored; null while still pending. */
    fun outcome(l: ReminderLog, now: Long): Double? = when {
        l.studiedMinutes >= 3 -> 1.0
        l.notToday -> 0.0
        l.openedAt != null -> 0.6
        now - l.id > RESPONSE_WINDOW -> 0.0
        else -> null
    }

    fun scores(st: ReminderState, now: Long): List<HourScore> = (0..23).map { h ->
        var s = 0.0; var f = 0.0; var n = 0
        st.logs.filter { it.hour == h && it.kind != ReminderKind.SUMMARY }.forEach { l ->
            val v = outcome(l, now) ?: return@forEach
            val age = (now - l.id) / (24.0 * HOUR)
            // "Not today" says more about the day than the hour, so it counts half.
            val w = 0.5.pow(age / HALF_LIFE_DAYS) * if (l.notToday) 0.5 else 1.0
            s += v * w; f += (1 - v) * w; n++
        }
        HourScore(h, prior(st, h), s, f, n, st.settings.rules[h] ?: HourRule.AUTO)
    }

    /** Daily cap: 3 while learning, then 2; 1 for three days after four ignored reminders in a row (which it records). */
    fun cap(st: ReminderState, now: Long): Int {
        val resolved = st.logs.filter { it.kind != ReminderKind.SUMMARY && outcome(it, now) != null }
        val last4 = resolved.takeLast(4)
        if (last4.size == 4 && last4.all { it.openedAt == null && !it.notToday && it.studiedMinutes == 0 } && st.backoffUntil < last4.last().id) {
            st.backoffUntil = last4.last().id + 3 * 24 * HOUR
        }
        if (now < st.backoffUntil) return 1
        return if (resolved.size < LEARNING_REMINDERS) 3 else 2
    }

    /**
     * Minutes to send before the hour (Y4): if you usually open this hour's reminder 25 minutes
     * late, it goes out 15 minutes earlier. Median of the last five openings, up to 30 minutes.
     */
    fun offsetMinutes(st: ReminderState, h: Int): Int {
        val delays = st.logs.filter { it.hour == h && it.openedAt != null && it.kind == ReminderKind.STUDY }
            .takeLast(5).map { ((it.openedAt!! - it.id) / MINUTE).toInt() }.sorted()
        if (delays.size < 3) return 0
        return (delays[delays.size / 2] - 10).coerceIn(0, 30)
    }

    /** Today's reminders: chosen once per day, then kept. */
    fun planToday(st: ReminderState, now: Long, rnd: Random = Random.Default): List<PlannedReminder> {
        val day = PlanEngine.startOfDay(now)
        if (st.planDay != day) {
            st.plan.clear()
            st.plan += choose(st, day, now, rnd)
            st.planDay = day
        }
        return st.plan
    }

    /** Throws away today's plan, e.g. after a settings change, and chooses again from now on. */
    fun replan(st: ReminderState, now: Long, rnd: Random = Random.Default): List<PlannedReminder> {
        st.planDay = 0L
        return planToday(st, now, rnd)
    }

    private fun choose(st: ReminderState, day: Long, now: Long, rnd: Random): List<PlannedReminder> {
        val s = st.settings
        if (!s.enabled || now < s.pausedUntil) return emptyList()
        val out = ArrayList<PlannedReminder>()
        val snap = st.snapshot
        val studyDay = snap == null || snap.studyDays.isEmpty() || weekday(day) in snap.studyDays
        val hours = wakingHours(s)
        fun at(h: Int, minute: Int = 0) = day + h * HOUR + minute * MINUTE

        if (studyDay) {
            // Reminders already sent today (before a re-plan) count toward the cap.
            val sentToday = st.logs.count { it.kind != ReminderKind.SUMMARY && it.id >= day }
            val cap = max(0, cap(st, now) - sentToday)
            val learning = st.logs.count { it.kind != ReminderKind.SUMMARY && outcome(it, now) != null } < LEARNING_REMINDERS
            val scores = scores(st, now).associateBy { it.hour }
            val streakHour = (s.sleep - 2).takeIf { it in hours && (snap?.streak ?: 0) >= 1 && snap?.restDayFree != true && cap >= 2 && now < at(it) }
            val studySlots = cap - if (streakHour != null) 1 else 0
            val open = hours.filter { scores[it]!!.rule != HourRule.BLOCKED && it != streakHour }
            val chosen = ArrayList<Int>()
            open.filter { scores[it]!!.rule == HourRule.PINNED }.forEach { if (chosen.size < studySlots) chosen += it }
            val draws = open.filter { scores[it]!!.rule == HourRule.AUTO }
                .map { it to sampleBeta(scores[it]!!.alpha, scores[it]!!.beta, rnd) }
                .sortedByDescending { it.second }
            // While learning, or while no hour has proven itself, every slot is a free draw, to explore.
            // After that, reminders go to the hours that work, plus an occasional try of another one.
            val proven = !learning && draws.any { scores[it.first]!!.mean >= GOOD_HOUR }
            var explore = proven && rnd.nextDouble() < EXPLORE_RATE
            draws.forEach { (h, _) ->
                val fits = chosen.size < studySlots && chosen.none { kotlin.math.abs(it - h) < 2 }
                if (!fits) return@forEach
                when {
                    !proven || scores[h]!!.mean >= GOOD_HOUR -> chosen += h
                    explore -> { chosen += h; explore = false }
                }
            }
            chosen.forEach { h -> out += PlannedReminder(at(h) - offsetMinutes(st, h) * MINUTE, h, ReminderKind.STUDY) }
            if (streakHour != null) out += PlannedReminder(at(streakHour), streakHour, ReminderKind.STREAK)
        }
        if (weekday(day) == Calendar.SUNDAY && SUMMARY_HOUR in (s.wake + 1)..(s.sleep - 1)) {
            out += PlannedReminder(at(SUMMARY_HOUR, SUMMARY_MINUTE), SUMMARY_HOUR, ReminderKind.SUMMARY)
        }
        return out.filter { it.at > now }.sortedBy { it.at }
    }

    /**
     * Checked again at the moment a reminder fires, because things change during the day:
     * no study reminder once today's plan is done or after "Not today", no streak-saver once you've studied.
     */
    fun shouldSend(st: ReminderState, p: PlannedReminder, now: Long): Boolean {
        val s = st.settings
        if (!s.enabled || now < s.pausedUntil) return false
        if (p.kind == ReminderKind.SUMMARY) return true
        val today = PlanEngine.startOfDay(now)
        if (st.notTodayDay == today) return false
        val snap = st.snapshot ?: return true
        val todayMinutes = if (snap.day == today) snap.todayMinutes else 0
        return when (p.kind) {
            ReminderKind.STREAK -> todayMinutes == 0 && !(snap.day == today && snap.restDayFree)
            else -> snap.targetMinutes <= 0 || todayMinutes < snap.targetMinutes
        }
    }

    /* ── recording outcomes ─────────────────────────────────────────────── */

    fun noteSent(st: ReminderState, p: PlannedReminder, now: Long) {
        st.logs += ReminderLog(now, p.hour, p.kind)
        if (p.kind == ReminderKind.SUMMARY) st.snapshot?.let { st.lastSummaryReadiness = it.readiness; st.lastSummaryMastered = it.mastered }
        while (st.logs.size > 400) st.logs.removeAt(0)
    }

    /** The app came to the front: from a reminder's tap ([tappedId]), or on its own soon after one. */
    fun noteOpened(st: ReminderState, now: Long, tappedId: Long?) {
        val i = if (tappedId != null) st.logs.indexOfLast { it.id == tappedId }
        else st.logs.indexOfLast { it.openedAt == null && !it.notToday && now - it.id in 0..RESPONSE_WINDOW }
        if (i >= 0 && st.logs[i].openedAt == null) st.logs[i] = st.logs[i].copy(openedAt = now, tapped = tappedId != null)
    }

    fun noteNotToday(st: ReminderState, id: Long, now: Long) {
        val i = st.logs.indexOfLast { it.id == id }
        if (i >= 0) st.logs[i] = st.logs[i].copy(notToday = true)
        st.notTodayDay = PlanEngine.startOfDay(now)
    }

    /** Minutes the app spent in front between [from] and [to], spread over the hours they fell in (Y3). */
    fun noteForeground(st: ReminderState, from: Long, to: Long) {
        var t = from
        while (t < to) {
            val c = Calendar.getInstance().apply { timeInMillis = t }
            val hourEnd = t - (c.get(Calendar.MINUTE) * MINUTE + c.get(Calendar.SECOND) * 1000L + c.get(Calendar.MILLISECOND)) + HOUR
            val end = min(to, hourEnd)
            st.appMinutes[c.get(Calendar.HOUR_OF_DAY)] += (end - t) / MINUTE.toDouble()
            t = end
        }
    }

    /** After opening from a reminder, how much did you study? Counted for the two hours that follow. */
    fun noteStudy(st: ReminderState, results: List<SessionResult>, attempts: List<Attempt>, now: Long) {
        st.logs.forEachIndexed { i, l ->
            val opened = l.openedAt ?: return@forEachIndexed
            if (now - opened > 3 * HOUR) return@forEachIndexed
            val m = studyMinutes(results, attempts, l.id, opened + 2 * HOUR)
            if (m != l.studiedMinutes) st.logs[i] = l.copy(studiedMinutes = m)
        }
    }

    /** Study minutes between two times: guided sessions or time answering, whichever is more. */
    fun studyMinutes(results: List<SessionResult>, attempts: List<Attempt>, from: Long, to: Long): Int {
        val sessions = results.filter { it.at in from..to }.sumOf { it.minutes }
        val answering = (attempts.filter { it.at in from..to }.sumOf { it.ms } / MINUTE.toDouble()).roundToInt()
        return max(sessions, answering)
    }

    /** On what share of your study days you studied at each hour, over the last eight weeks. */
    fun studyHabit(attempts: List<Attempt>, results: List<SessionResult>, now: Long): Pair<DoubleArray, Int> {
        val since = now - 56 * 24 * HOUR
        val byDay = HashMap<Long, HashSet<Int>>()
        fun add(t: Long) { if (t >= since) byDay.getOrPut(PlanEngine.startOfDay(t)) { HashSet() } += Calendar.getInstance().apply { timeInMillis = t }.get(Calendar.HOUR_OF_DAY) }
        attempts.forEach { add(it.at) }
        results.forEach { add(it.at - it.minutes * MINUTE) }
        val share = DoubleArray(24)
        if (byDay.isEmpty()) return share to 0
        byDay.values.forEach { hs -> hs.forEach { share[it] += 1.0 } }
        for (h in 0..23) share[h] /= byDay.size
        return share to byDay.size
    }

    /** Thompson sampling needs a draw from Beta(a, b): two gamma draws (Marsaglia–Tsang). */
    fun sampleBeta(a: Double, b: Double, rnd: Random): Double {
        val x = sampleGamma(a, rnd); val y = sampleGamma(b, rnd)
        return x / (x + y)
    }

    private fun sampleGamma(shape: Double, rnd: Random): Double {
        if (shape < 1) return sampleGamma(shape + 1, rnd) * rnd.nextDouble().pow(1 / shape)
        val d = shape - 1.0 / 3; val c = 1 / sqrt(9 * d)
        while (true) {
            var x: Double; var v: Double
            do { x = gaussian(rnd); v = 1 + c * x } while (v <= 0)
            v = v * v * v
            val u = rnd.nextDouble()
            if (u < 1 - 0.0331 * x * x * x * x || ln(u) < 0.5 * x * x + d * (1 - v + ln(v))) return d * v
        }
    }

    private fun gaussian(rnd: Random): Double {
        val u1 = rnd.nextDouble().coerceAtLeast(1e-12); val u2 = rnd.nextDouble()
        return sqrt(-2 * ln(u1)) * kotlin.math.cos(2 * Math.PI * u2)
    }

}

/** The words of each reminder (S1, S4, S5, S7). Pure, so it is tested like the rest. */
object ReminderText {
    data class Message(val title: String, val body: String)

    fun compose(kind: ReminderKind, st: ReminderState, now: Long, learnedHour: Boolean): Message {
        val n = st.snapshot
        return when (kind) {
            ReminderKind.STUDY -> {
                val away = if (n != null && n.lastStudyAt > 0) ((now - n.lastStudyAt) / (24 * ReminderBrain.HOUR)).toInt() else 0
                when {
                    n == null || n.nextTitle == null -> Message("Time for today's study", "Open your path to see today's session.")
                    away >= 3 -> Message("Welcome back: a 15-minute warm-up", "$away days off is fine. Let's pick up with what you studied last.")
                    n.finalWeek -> Message("Final week: ${n.nextTitle} · ${n.nextMinutes} min", "Light review only. Rest matters as much as revision now.")
                    else -> Message("Next up: ${n.nextTitle} · ${n.nextMinutes} min",
                        listOfNotNull(n.nextWhat, if (learnedHour) "You usually study around now." else null).joinToString(". ").ifEmpty { "Your next step on the path." })
                }
            }
            ReminderKind.STREAK -> {
                val streak = n?.streak ?: 0
                val cards = n?.cardsDue ?: 0
                Message("Keep your $streak-day streak",
                    if (cards > 0) "$cards flashcards are due. About 5 minutes." else "Ten quick questions keep it going. About 5 minutes.")
            }
            ReminderKind.SUMMARY -> {
                val m = n?.weekMinutes ?: 0
                val time = if (m >= 60) "${m / 60} h ${m % 60} m" else "$m min"
                val ready = n?.readiness ?: 0
                val change = if (st.lastSummaryReadiness >= 0) "readiness ${st.lastSummaryReadiness} → $ready" else "readiness $ready"
                val mastered = if (n != null && st.lastSummaryMastered >= 0) max(0, n.mastered - st.lastSummaryMastered) else null
                val parts = listOfNotNull(mastered?.let { "$it ${if (it == 1) "task" else "tasks"} mastered" }, n?.nextWeekFocus?.let { "Next week: $it" })
                Message("Your week: $time studied, $change", if (parts.isEmpty()) "Open the app to see how the week went." else parts.joinToString(". ") + ".")
            }
        }
    }
}
