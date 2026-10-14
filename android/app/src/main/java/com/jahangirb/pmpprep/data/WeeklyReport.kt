package com.jahangirb.pmpprep.data

import org.json.JSONObject
import java.util.Calendar
import kotlin.math.roundToInt

/** Where you stood at the end of a week (Monday to Sunday). The current week's is updated as you go. */
data class WeeklyReport(
    val weekStart: Long,
    val updatedAt: Long,
    val readiness: Int,
    val accuracy: Int,
    val tasksTouched: Int,
    val mastered: Int,
    val minutes: Int,
    val days: Int,
    val answers: Int,
    val mocks: Int,
    val mockAvg: Int?,
    val fitness: Int?,
    val stamina: Int?,
    val composure: Int?,
    val consistency: Int?,
    val wpm: Int?,
    val longest: Int,
    val freezePct: Int?,
    val worry: Int?,
) {
    fun toJson(): JSONObject = JSONObject().apply {
        put("w", weekStart); put("u", updatedAt); put("r", readiness); put("acc", accuracy); put("tt", tasksTouched)
        put("ma", mastered); put("min", minutes); put("d", days); put("n", answers); put("mk", mocks)
        mockAvg?.let { put("mka", it) }; fitness?.let { put("f", it) }; stamina?.let { put("st", it) }
        composure?.let { put("co", it) }; consistency?.let { put("cn", it) }; wpm?.let { put("wpm", it) }
        put("lg", longest); freezePct?.let { put("fr", it) }; worry?.let { put("wo", it) }
    }

    companion object {
        private fun JSONObject.intOrNull(k: String) = if (has(k)) optInt(k) else null
        fun fromJson(o: JSONObject) = WeeklyReport(
            o.optLong("w"), o.optLong("u"), o.optInt("r"), o.optInt("acc"), o.optInt("tt"), o.optInt("ma"), o.optInt("min"),
            o.optInt("d"), o.optInt("n"), o.optInt("mk"), o.intOrNull("mka"), o.intOrNull("f"), o.intOrNull("st"),
            o.intOrNull("co"), o.intOrNull("cn"), o.intOrNull("wpm"), o.optInt("lg"), o.intOrNull("fr"), o.intOrNull("wo"),
        )
    }
}

object WeeklyReports {
    /** Monday 00:00 of the week containing [t], local time. */
    fun weekStart(t: Long): Long {
        val c = Calendar.getInstance().apply {
            timeInMillis = Srs.startOfDay(t)
            firstDayOfWeek = Calendar.MONDAY
            set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        }
        var m = Srs.startOfDay(c.timeInMillis)
        if (m > t) m = Srs.startOfDay(m - 6 * ExamFitness.DAY)
        return m
    }

    /** This week so far, from the same figures the rest of the app shows. */
    fun build(
        now: Long, readiness: ReadinessReport, tasksTouched: Int, mastered: Int,
        attempts: List<Attempt>, results: List<SessionResult>, mocks: List<MockResult>, fitness: FitnessReport,
    ): WeeklyReport {
        val from = weekStart(now)
        val week = attempts.filter { it.at >= from && it.source != Readiness.SOURCE_LESSON }
        val sessionMin = results.filter { it.at >= from }.sumOf { it.minutes }
        val answerMin = (week.sumOf { it.ms } / 60_000.0).roundToInt()
        val days = (week.map { Srs.startOfDay(it.at) } + results.filter { it.at >= from }.map { Srs.startOfDay(it.at) }).toSet().size
        val wm = mocks.filter { it.at >= from }
        val acc = readiness.components.firstOrNull { it.id == "accuracy" }
        fun part(id: String) = fitness.part(id).score?.let { (it * 100).roundToInt() }
        return WeeklyReport(
            weekStart = from, updatedAt = now, readiness = readiness.score,
            accuracy = acc?.figure?.removeSuffix("%")?.toIntOrNull() ?: 0,
            tasksTouched = tasksTouched, mastered = mastered,
            minutes = maxOf(sessionMin, answerMin), days = days, answers = week.size,
            mocks = wm.size, mockAvg = if (wm.isEmpty()) null else (wm.map { it.pct }.average() * 100).roundToInt(),
            fitness = fitness.score, stamina = part("stamina"), composure = part("composure"), consistency = part("consistency"),
            wpm = fitness.wpm?.roundToInt(), longest = fitness.longest, freezePct = fitness.freezeRate?.let { (it * 100).roundToInt() },
            worry = fitness.worry,
        )
    }

    /** One line of a comparison: this week's value, the week before's, and which way is better. */
    data class Row(val label: String, val value: Int?, val previous: Int?, val unit: String, val higherIsBetter: Boolean = true) {
        val delta: Int? get() = if (value != null && previous != null) value - previous else null
        /** +1 better, −1 worse, 0 same or not comparable. */
        val direction: Int get() = delta?.let { d -> if (d == 0) 0 else if ((d > 0) == higherIsBetter) 1 else -1 } ?: 0
    }

    data class Section(val title: String, val rows: List<Row>)

    fun compare(r: WeeklyReport, p: WeeklyReport?): List<Section> = listOf(
        Section("Knowledge", listOf(
            Row("Readiness", r.readiness, p?.readiness, "/100"),
            Row("Accuracy", r.accuracy, p?.accuracy, "%"),
            Row("Tasks practised", r.tasksTouched, p?.tasksTouched, "/26"),
            Row("Tasks mastered", r.mastered, p?.mastered, ""),
            Row("Mock average", r.mockAvg, p?.mockAvg, "%"),
        )),
        Section("Exam fitness", listOf(
            Row("Exam fitness", r.fitness, p?.fitness, "/100"),
            Row("Stamina", r.stamina, p?.stamina, "/100"),
            Row("Composure", r.composure, p?.composure, "/100"),
            Row("Consistency", r.consistency, p?.consistency, "/100"),
            Row("Reading pace", r.wpm, p?.wpm, " wpm"),
            Row("Longest sitting", r.longest, p?.longest, " q"),
            Row("Freezes", r.freezePct, p?.freezePct, "%", higherIsBetter = false),
            Row("Exam worry", r.worry, p?.worry, "/100", higherIsBetter = false),
        )),
        Section("Effort", listOf(
            Row("Minutes studied", r.minutes, p?.minutes, " min"),
            Row("Study days", r.days, p?.days, "/7"),
            Row("Questions answered", r.answers, p?.answers, ""),
            Row("Mocks sat", r.mocks, p?.mocks, ""),
        )),
    )
}
