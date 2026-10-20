package com.jahangirb.pmpprep.data

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/** The shared `support` slice (docs/progress-format.md §4); 0 and "" mean not set. */
data class SupportState(val pressedAt: Long = 0, val shownAt: Long = 0, val anchor: String = "")

/**
 * The "please support" reminder (docs/progress-format.md §4), as pure functions over the shared
 * `opens` and `support` slices, so every device counts the same once they sync.
 */
object Support {
    const val URL = "https://ko-fi.com/T4R728ULEV"
    const val MAX_OPENS = 60
    const val STREAK = 7
    const val QUIET_MS = 30L * 24 * 60 * 60 * 1000

    /* The menu button's own page (docs/progress-format.md §4), worded apart from the reminder. */
    const val PAGE_TITLE = "Keep PMP Prep free"
    const val PAGE_BODY = "PMP Prep is built by one person, Jahangir Babayev, in his own time. Every question, lesson, " +
        "mock and update is free for everyone, with no ads, no account and no tracking."
    val PAYS_FOR = listOf("New question packs and lessons as the exam changes", "Testing on real Android phones and iPhones",
        "Hosting the website", "Time to fix what learners report")
    const val KOFI_LABEL = "Buy a coffee on Ko-fi"
    const val KOFI_NOTE = "One-off, any amount, no account needed. Ko-fi handles the payment; the app never sees it."
    const val THANKS = "Already supported? Thank you. Pressing the Ko-fi button also quiets the weekly reminder for a month."
    const val SITE = "https://pmp-sandy.vercel.app/"
    const val SHARE_TEXT = "Free PMP exam prep that works offline, on the web, Android and iPhone: $SITE"
    const val REPORT_URL = "https://github.com/JahangirB/PMP/issues/new?template=question-problem.yml"
    const val REPO_URL = "https://github.com/JahangirB/PMP"

    const val TITLE = "Enjoying PMP Prep?"
    const val FIRST_BODY = "You've studied 7 days in a row. If the app is helping you, please support it. " +
        "It's free, has no ads, and your support keeps it going."
    const val ALREADY_BODY = "If the app is helping you, please support it. It's free, has no ads, and your support " +
        "keeps it going. If you have already supported, please ignore this message."

    enum class Due { NONE, FIRST, ALREADY }

    /** The local date of [ms], YYYY-MM-DD. */
    fun day(ms: Long, tz: TimeZone = TimeZone.getDefault()): String =
        SimpleDateFormat("yyyy-MM-dd", Locale.US).apply { timeZone = tz }.format(Date(ms))

    /** [date] (YYYY-MM-DD) moved by [days]: calendar arithmetic in UTC, so no clock change gets in the way. */
    fun addDays(date: String, days: Int): String {
        val utc = TimeZone.getTimeZone("UTC")
        val f = SimpleDateFormat("yyyy-MM-dd", Locale.US).apply { timeZone = utc }
        val c = Calendar.getInstance(utc).apply { time = f.parse(date)!!; add(Calendar.DAY_OF_MONTH, days) }
        return f.format(c.time)
    }

    /** [opens] with [today] added, sorted, the newest [MAX_OPENS] kept. */
    fun recordOpen(opens: Collection<String>, today: String): List<String> = (opens + today).toSortedSet().toList().takeLast(MAX_OPENS)

    /** Consecutive dates ending [today] in [opens], counting only dates on or after the anchor. */
    fun streak(opens: Collection<String>, anchor: String, today: String): Int {
        val set = opens.toHashSet()
        var n = 0
        var d = today
        while (d in set && (anchor.isEmpty() || d >= anchor)) { n++; d = addDays(d, -1) }
        return n
    }

    /** Which message to show now, if any. [busy]: a mock exam or a guided session is under way. */
    fun due(opens: Collection<String>, support: SupportState, today: String, nowMs: Long, busy: Boolean,
            tz: TimeZone = TimeZone.getDefault()): Due {
        if (busy) return Due.NONE
        if (support.shownAt > 0 && day(support.shownAt, tz) == today) return Due.NONE
        val pressed = support.pressedAt > 0
        if (pressed && nowMs - support.pressedAt < QUIET_MS) return Due.NONE
        if (pressed && support.shownAt < support.pressedAt + QUIET_MS) return Due.ALREADY
        if (streak(opens, support.anchor, today) >= STREAK) return if (pressed) Due.ALREADY else Due.FIRST
        return Due.NONE
    }

    /** After the message shows: once today, and the 7-day count starts again tomorrow. */
    fun markShown(s: SupportState, nowMs: Long, today: String): SupportState = s.copy(shownAt = nowMs, anchor = addDays(today, 1))

    /** The support button was pressed, in the message or in the menu. */
    fun markPressed(s: SupportState, nowMs: Long): SupportState = s.copy(pressedAt = nowMs)
}
