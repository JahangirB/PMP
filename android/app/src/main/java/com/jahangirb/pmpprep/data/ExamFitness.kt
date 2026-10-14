package com.jahangirb.pmpprep.data

import java.util.Calendar
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/*
 * Exam fitness: are you ready to sit the exam, not just to know it?
 *   Stamina (40%)      can you hold accuracy and pace to question 180?
 *   Composure (35%)    can you read and decide steadily under the clock?
 *   Consistency (25%)  will you keep showing up until exam day?
 * Built from behaviour in the app, plus optional check-ins. It describes what happened;
 * it never diagnoses. A signal without enough evidence is left out (shown as "not yet"),
 * and a part is scored only once signals worth at least half its weight have evidence.
 */

/** One measured behaviour. [score] 0..1, or null while there isn't enough evidence. */
data class FitnessSignal(val id: String, val label: String, val weight: Int, val score: Double?, val figure: String, val detail: String)

data class FitnessPart(
    val id: String, val label: String, val weight: Int, val signals: List<FitnessSignal>,
    /** Why the part isn't scored yet, beyond missing signals; null when it may be. */
    val gate: String? = null,
) {
    private val ready get() = signals.filter { it.score != null }
    /** Weighted over the signals that have evidence, once they carry at least half the weight. */
    val score: Double? get() {
        if (gate != null) return null
        val w = ready.sumOf { it.weight }
        return if (w == 0 || w * 2 < signals.sumOf { it.weight }) null else ready.sumOf { it.score!! * it.weight } / w
    }
}

data class FitnessReport(
    val parts: List<FitnessPart>,
    /** 0–100, or null until at least one part can be scored. */
    val score: Int?,
    val provisional: Boolean,
    val finding: String?,
    val next: String?,
    val ladderRung: Int,
    val ladderNext: Int?,
    val wpm: Double?,
    val longest: Int,
    val freezeRate: Double?,
    val worry: Int?,
    val worryHighForWeeks: Boolean,
    val link: String?,
    val momentumLow: Boolean,
) {
    fun part(id: String) = parts.first { it.id == id }
    fun signal(id: String) = parts.flatMap { it.signals }.first { it.id == id }
}

class FitnessInput(
    val attempts: List<Attempt>,
    val sittings: List<SittingEvent>,
    val checkIns: List<CheckIn>,
    val results: List<SessionResult>,
    val studyDays: Set<Int>,
    val hoursPerWeek: Int,
    val now: Long,
)

object ExamFitness {
    const val DAY = 86_400_000L
    const val MINUTE = 60_000L
    /** A new sitting starts after a gap longer than this, or when a mock starts or ends. */
    const val SITTING_GAP = 10 * MINUTE
    val LADDER = listOf(20, 45, 70, 100, 140, 180)
    /** Reading speed for a typical question (about 150 words) to leave ~30 s of a ~76 s budget to decide. */
    const val TARGET_WPM = 200.0
    const val LOW_WPM = 100.0
    /** The five statements of the weekly check; the fifth is reversed (agreeing is good). */
    val WEEKLY_STATEMENTS = listOf(
        "When I see a long question, my mind goes blank.",
        "I worry about running out of time.",
        "Thinking about the exam makes it hard to start studying.",
        "I change answers I was sure about.",
        "I feel able to stay focused for four hours.",
    )

    private fun ratio(v: Double, lo: Double, hi: Double) = if (hi == lo) 0.0 else ((v - lo) / (hi - lo)).coerceIn(0.0, 1.0)
    private fun pct(v: Double) = "${(v * 100).roundToInt()}%"
    private fun median(xs: List<Double>): Double? {
        if (xs.isEmpty()) return null
        val s = xs.sorted(); val m = s.size / 2
        return if (s.size % 2 == 1) s[m] else (s[m - 1] + s[m]) / 2
    }
    private fun mean(xs: List<Double>) = if (xs.isEmpty()) 0.0 else xs.sum() / xs.size
    private fun startOfDay(t: Long) = Srs.startOfDay(t)

    /** Runs of answers with no long gap, in the order they were given. Mock answers are their own sitting. */
    fun sittings(attempts: List<Attempt>): List<List<Attempt>> {
        val out = ArrayList<MutableList<Attempt>>()
        var prev: Attempt? = null
        for (a in attempts) {
            if (a.source == Readiness.SOURCE_LESSON) continue
            val p = prev
            val split = p == null || a.at - p.at > SITTING_GAP || (a.source == Readiness.SOURCE_EXAM) != (p.source == Readiness.SOURCE_EXAM)
            if (split) out += ArrayList<Attempt>()
            out.last() += a
            prev = a
        }
        return out
    }

    /** Accuracy drop from the first quarter of a sitting to the last. */
    fun fade(s: List<Attempt>): Double {
        val q = s.size / 4
        if (q == 0) return 0.0
        return mean(s.take(q).map { it.credit }) - mean(s.takeLast(q).map { it.credit })
    }

    /** Last quarter's median time over the first quarter's, minus one; null when times weren't measured per question. */
    fun drift(s: List<Attempt>): Double? {
        val q = s.size / 4
        // Older mock records share one average time per question; those can't show drift.
        if (q < 5 || s.map { it.ms }.distinct().size < 3) return null
        val a = median(s.take(q).map { it.ms.toDouble() }) ?: return null
        val b = median(s.takeLast(q).map { it.ms.toDouble() }) ?: return null
        return if (a <= 0) null else b / a - 1
    }

    /** Sittings closed part-way and not finished within a day. */
    private fun abandoned(events: List<SittingEvent>, since: Long): Pair<Int, Int> {
        val recent = events.filter { it.at >= since }
        val finishedKeys = recent.filter { it.finished }.map { it.key }.toSet()
        val left = recent.filter { !it.finished && it.answered > 0 }.map { it.key }.toSet().filter { k ->
            k !in finishedKeys
        }
        return finishedKeys.size to left.size
    }

    private fun studyDaySet(inp: FitnessInput): Set<Long> {
        val d = HashSet<Long>()
        inp.attempts.forEach { d += startOfDay(it.at) }
        inp.results.forEach { d += startOfDay(it.at) }
        return d
    }

    private fun minutes(inp: FitnessInput, from: Long, to: Long): Int {
        val s = inp.results.filter { it.at in from until to }.sumOf { it.minutes }
        val a = (inp.attempts.filter { it.at in from until to }.sumOf { it.ms } / MINUTE.toDouble()).roundToInt()
        return max(s, a)
    }

    fun report(inp: FitnessInput): FitnessReport {
        val now = inp.now
        val atts = inp.attempts.filter { it.source != Readiness.SOURCE_LESSON }
        val sits = sittings(inp.attempts)
        val firstActivity = listOfNotNull(atts.minOfOrNull { it.at }, inp.results.minOfOrNull { it.at }).minOrNull()
        val ageDays = if (firstActivity == null) 0 else ((now - firstActivity) / DAY).toInt()

        /* ── stamina ── */
        val recentSits = sits.filter { it.last().at >= now - 30 * DAY }
        val longest = recentSits.maxOfOrNull { it.size } ?: 0
        val long = sits.filter { it.size >= 60 && it.last().at >= now - 60 * DAY }.takeLast(3)
        val fades = long.map { fade(it) }
        val drifts = long.mapNotNull { drift(it) }
        val (finLong, leftLong) = abandoned(inp.sittings.filter { it.planned >= 40 }, now - 60 * DAY)
        val sits40 = sits.count { it.size >= 40 }
        val avgFade = if (fades.isEmpty()) null else mean(fades)
        val avgDrift = if (drifts.isEmpty()) null else mean(drifts)
        val stamina = FitnessPart("stamina", "Stamina", 40, listOf(
            FitnessSignal("longest", "Longest sitting", 35, if (recentSits.isEmpty()) null else min(1.0, longest / 180.0),
                "$longest questions", "Your longest run without a break over 10 minutes, in the last 30 days. Full marks at 180, the whole exam."),
            FitnessSignal("fade", "Accuracy fade", 30, avgFade?.let { 1 - ratio(it, 0.05, 0.20) },
                avgFade?.let { "${if (it >= 0) "−" else "+"}${(kotlin.math.abs(it) * 100).roundToInt()} pts" } ?: "not yet",
                if (avgFade == null) "Needs a sitting of 60+ questions." else "Last quarter against first quarter of your last ${fades.size} long sitting(s). Up to 5 points down is full marks; 20 or more is zero."),
            FitnessSignal("drift", "Pace drift", 20, avgDrift?.let { r ->
                    if (r > 0.10) 1 - ratio(r, 0.10, 0.40) else if (r < -0.25) 1 - ratio(-r, 0.25, 0.50) else 1.0 },
                avgDrift?.let { "${if (it >= 0) "+" else "−"}${(kotlin.math.abs(it) * 100).roundToInt()}%" } ?: "not yet",
                if (avgDrift == null) "Needs a long sitting with each question timed." else "Time per question in the last quarter against the first. Up to 10% slower is full marks; much faster late on counts too, as it usually means guessing to finish."),
            FitnessSignal("finish", "Finishing long sittings", 15,
                if (finLong + leftLong >= 2) finLong.toDouble() / (finLong + leftLong) else null,
                if (finLong + leftLong == 0) "not yet" else "$finLong of ${finLong + leftLong}", "Mocks and sittings of 40+ questions finished rather than left part-way, last 60 days."),
        ), gate = if (sits40 >= 2) null else "Scored after two sittings of 40+ questions (you have $sits40).")

        /* ── composure ── */
        val recentAtts = atts.takeLast(200)
        val reading = recentAtts.filter { it.firstTapMs > 1000 && it.words > 0 }
        val wpm = if (reading.size >= 30) median(reading.map { it.words / (it.firstTapMs / 60_000.0) }) else null
        val withIdle = recentAtts.filter { it.idleMs > 0 }
        val medianMs = median(recentAtts.filter { it.ms > 500 && !it.timed }.map { it.ms.toDouble() }) ?: 76_000.0
        val freezeAt = max(150_000.0, 3 * medianMs)
        val freezeRate = if (withIdle.size >= 30) withIdle.count { it.idleMs >= freezeAt }.toDouble() / withIdle.size else null
        val longStemFreeze = if (withIdle.size >= 30) {
            val mw = median(withIdle.map { it.words.toDouble() }) ?: 0.0
            val longQ = withIdle.filter { it.words > mw }; val shortQ = withIdle.filter { it.words <= mw }
            longQ.isNotEmpty() && shortQ.isNotEmpty() &&
                longQ.count { it.idleMs >= freezeAt }.toDouble() / longQ.size > 2 * shortQ.count { it.idleMs >= freezeAt }.toDouble() / shortQ.size
        } else false
        var slow = 0; var panic = 0
        sits.filter { s -> s.isNotEmpty() && s.first().source != Readiness.SOURCE_EXAM }.takeLast(20).forEach { s ->
            val m = median(s.filter { it.ms > 500 }.map { it.ms.toDouble() }) ?: return@forEach
            val avg = mean(s.map { it.credit })
            for (i in 0 until s.size - 3) {
                if (s[i].ms > 2 * m) {
                    slow++
                    val next = s.subList(i + 1, i + 4)
                    if (next.all { it.ms < 0.5 * m } && mean(next.map { it.credit }) < avg) panic++
                }
            }
        }
        val panicRate = if (slow >= 10) panic.toDouble() / slow else null
        val timedA = atts.filter { it.timed }.takeLast(200)
        val untimedA = atts.filter { !it.timed }.takeLast(200)
        val gap = if (timedA.size >= 20 && untimedA.size >= 20) mean(untimedA.map { it.credit }) - mean(timedA.map { it.credit }) else null
        val changes = atts.takeLast(300)
        val rw = changes.sumOf { it.rightToWrong }; val wr = changes.sumOf { it.wrongToRight }
        val composure = FitnessPart("composure", "Composure", 35, listOf(
            FitnessSignal("reading", "Reading pace", 30, wpm?.let { ratio(it, LOW_WPM, TARGET_WPM) },
                wpm?.let { "${it.roundToInt()} wpm" } ?: "not yet",
                if (wpm == null) "Needs 30 answered questions with reading time recorded."
                else "Words read per minute before your first tap. About ${TARGET_WPM.toInt()} reads a typical 150-word question in 45 seconds, leaving 30 to decide."),
            FitnessSignal("freeze", "Freezes", 20, freezeRate?.let { 1 - ratio(it, 0.02, 0.15) },
                freezeRate?.let { pct(it) } ?: "not yet",
                if (freezeRate == null) "Needs 30 questions answered in practice." else "Questions where nothing happened for over ${(freezeAt / 60_000).roundToInt()} minutes" +
                    (if (longStemFreeze) ". They happen mostly on long questions." else ".")),
            FitnessSignal("rush", "Rushing after a slow one", 20, panicRate?.let { 1 - ratio(it, 0.10, 0.50) },
                panicRate?.let { pct(it) } ?: "not yet",
                if (panicRate == null) "Needs 10 slow questions in practice." else "After a question that took twice your usual time, how often the next three were rushed and went worse."),
            FitnessSignal("pressure", "Pressure gap", 15, gap?.let { 1 - ratio(it, 0.05, 0.20) },
                gap?.let { "${if (it >= 0) "−" else "+"}${(kotlin.math.abs(it) * 100).roundToInt()} pts" } ?: "not yet",
                if (gap == null) "Needs 20 timed and 20 untimed answers." else "Accuracy against the clock compared with relaxed practice. Up to 5 points lower is full marks."),
            FitnessSignal("doubt", "Second-guessing", 15, if (rw + wr >= 10) ratio(wr.toDouble() / (rw + wr), 0.35, 0.60) else null,
                if (rw + wr == 0) "not yet" else "$rw right→wrong, $wr wrong→right",
                "Answers changed before checking. More right-to-wrong than wrong-to-right means doubt is costing marks."),
        ))

        /* ── consistency ── */
        val days = studyDaySet(inp)
        val today = startOfDay(now)
        val planned28 = (0 until 28).count { i -> weekday(today - i * DAY) in inp.studyDays.ifEmpty { (1..7).toSet() } }
        val studied28 = days.count { it > today - 28 * DAY }
        val mins14 = minutes(inp, now - 14 * DAY, now + 1)
        val target14 = inp.hoursPerWeek * 2 * 60
        val thisWeek = minutes(inp, now - 7 * DAY, now + 1)
        val prevAvg = minutes(inp, now - 28 * DAY, now - 7 * DAY) / 3.0
        val momentum = when {
            ageDays < 14 -> null
            prevAvg <= 0 -> if (thisWeek > 0) 1.0 else 0.0
            else -> ratio(thisWeek / prevAvg, 0.5, 1.0)
        }
        val (finAll, leftAll) = abandoned(inp.sittings, now - 28 * DAY)
        val sortedDays = days.filter { it > today - 60 * DAY }.sorted()
        val gapsOff = sortedDays.zipWithNext().map { (a, b) -> ((b - a) / DAY - 1).toInt() }.filter { it >= 3 }
        val consistency = FitnessPart("consistency", "Consistency", 25, listOf(
            FitnessSignal("regular", "Regular days", 25, if (ageDays >= 7) min(1.0, studied28.toDouble() / planned28.coerceAtLeast(1)) else null,
                "$studied28 of $planned28", "Days you studied in the last 28, against your planned study days."),
            FitnessSignal("hours", "Hours kept", 25, if (ageDays >= 7 && target14 > 0) min(1.0, mins14.toDouble() / target14) else null,
                "${mins14 / 60} h ${mins14 % 60} m", "Study time in the last 14 days against your planned ${inp.hoursPerWeek} hours a week."),
            FitnessSignal("momentum", "Momentum", 20, momentum,
                if (ageDays < 14) "not yet" else "$thisWeek min vs ${prevAvg.roundToInt()} avg",
                "This week's minutes against your average for the three weeks before. A falling trend is the earliest sign motivation is slipping."),
            FitnessSignal("sessions", "Finishing sessions", 15, if (finAll + leftAll >= 3) finAll.toDouble() / (finAll + leftAll) else null,
                if (finAll + leftAll == 0) "not yet" else "$finAll of ${finAll + leftAll}", "Sessions and practice sets finished rather than left part-way, last 28 days."),
            FitnessSignal("bounce", "Bouncing back", 15, if (ageDays >= 21) (if (gapsOff.isEmpty()) 1.0 else 1 - ratio(mean(gapsOff.map { it.toDouble() }), 3.0, 10.0)) else null,
                if (gapsOff.isEmpty()) "no long gaps" else "${gapsOff.size} gap(s), avg ${mean(gapsOff.map { it.toDouble() }).roundToInt()} days",
                "Breaks of 3+ days in the last 60 days, and how long they lasted."),
        ))

        /* ── overall ── */
        val parts = listOf(stamina, composure, consistency)
        val scored = parts.filter { it.score != null }
        val total = if (scored.isEmpty()) null else (scored.sumOf { it.score!! * it.weight } / scored.sumOf { it.weight } * 100).roundToInt()

        // The biggest shortfall among signals with evidence decides the finding and the next step,
        // even before its part has enough evidence for a score. A gated part (stamina before two long
        // sittings) is left to the ladder instead.
        val worst = parts.filter { it.gate == null }.flatMap { p -> p.signals.filter { it.score != null }.map { p to it } }
            .maxByOrNull { (p, s) -> (1 - s.score!!) * s.weight * p.weight }?.takeIf { (_, s) -> s.score!! < 0.9 }?.second
        val rung = LADDER.filter { r -> sits.any { s -> s.size >= r && (s.size < 60 || fade(s) < 0.10) } }.maxOrNull() ?: 0
        val nextRung = LADDER.firstOrNull { it > rung }
        val (finding, next) = advice(worst, wpm, longest, longStemFreeze, nextRung)

        /* ── check-ins ── */
        val weekly = inp.checkIns.filter { it.kind == "weekly" && it.items.size == WEEKLY_STATEMENTS.size }.sortedBy { it.at }
        fun worryOf(c: CheckIn): Int {
            val adjusted = c.items.mapIndexed { i, v -> if (i == WEEKLY_STATEMENTS.size - 1) 6 - v else v }
            return ((adjusted.sum() - adjusted.size) * 100.0 / (adjusted.size * 4)).roundToInt()
        }
        val worry = weekly.lastOrNull()?.let { worryOf(it) }
        val worryHigh = weekly.size >= 3 && weekly.takeLast(3).all { worryOf(it) >= 75 }
        val link = feelingLink(inp)

        return FitnessReport(parts, total, scored.size < parts.size, finding, next, rung, nextRung, wpm, longest, freezeRate,
            worry, worryHigh, link, momentum != null && momentum < 0.5)
    }

    private fun weekday(t: Long) = Calendar.getInstance().apply { timeInMillis = t }.get(Calendar.DAY_OF_WEEK)

    private fun advice(worst: FitnessSignal?, wpm: Double?, longest: Int, longStem: Boolean, nextRung: Int?): Pair<String?, String?> = when (worst?.id) {
        null -> null to (nextRung?.let { "A $it-question timed sitting: the next rung of your stamina ladder." })
        "longest" -> "Your longest sitting is $longest questions; the exam is 180." to "A ${nextRung ?: 45}-question timed sitting, the next rung of your stamina ladder."
        "fade" -> "Your accuracy fades in the last part of long sittings." to "Keep to your ladder rung until the fade is under 10 points, and use the break points to stand, drink water and look away."
        "drift" -> "You slow down, or rush, late in long sittings." to "Watch the pacing checkpoints at questions 60 and 120 in your next long sitting."
        "finish" -> "Some long sittings were left part-way." to "Pick a sitting length you can finish; the ladder builds up from there."
        "reading" -> "You read about ${wpm?.roundToInt()} words a minute; the exam needs about ${TARGET_WPM.toInt()}." to "Reading drills: name what each question asks before you see the options, and read the last sentence first."
        "freeze" -> "Some questions stop you for minutes" + (if (longStem) ", mostly long ones." else ".") to "When stuck: breathe out slowly, read the last sentence, rule out two options, then move on."
        "rush" -> "After a slow question you tend to rush the next few." to "One slow question is normal. Take the next one at your usual pace; the clock has room for it."
        "pressure" -> "You score noticeably lower against the clock." to "More timed practice, starting with generous time and tightening week by week."
        "doubt" -> "Changing answers is costing you marks." to "Trust your first answer unless you find a specific reason; the debrief shows which changes cost marks."
        "regular" -> "You're studying on fewer days than planned." to "Minimum days: even 10 cards or 10 questions keep the rhythm."
        "hours" -> "You're short of your planned hours." to "Use the 15-minute version on busy days rather than skipping."
        "momentum" -> "Your study time has dropped compared with recent weeks." to "A lighter week of minimum days rebuilds the habit without pressure."
        "sessions" -> "Some sessions are left unfinished." to "Choose the 15-minute version when time is short, so the session gets finished."
        "bounce" -> "Breaks of several days are taking a while to recover from." to "After a break, start with the warm-up, not catch-up."
        else -> null to null
    }

    /** One link between how you felt and how you read, if the check-ins show a clear one. */
    private fun feelingLink(inp: FitnessInput): String? {
        val pre = inp.checkIns.filter { it.kind == "pre" }
        fun wpmAfter(cs: List<CheckIn>): Pair<Double?, Int> {
            val xs = cs.flatMap { c -> inp.attempts.filter { it.at in c.at..(c.at + 3 * 3_600_000L) && it.firstTapMs > 1000 && it.words > 0 } }
                .map { it.words / (it.firstTapMs / 60_000.0) }
            return median(xs) to xs.size
        }
        val (low, nl) = wpmAfter(pre.filter { it.value <= 2 })
        val (high, nh) = wpmAfter(pre.filter { it.value >= 4 })
        if (low == null || high == null || nl < 10 || nh < 10) return null
        val diff = 1 - low / high
        return if (diff >= 0.10) "On days you felt low before studying, you read ${(diff * 100).roundToInt()}% slower than on good days." else null
    }
}
