package com.jahangirb.pmpprep

import com.jahangirb.pmpprep.data.ProgressStore
import com.jahangirb.pmpprep.data.Support
import com.jahangirb.pmpprep.data.Support.Due
import com.jahangirb.pmpprep.data.SupportState
import com.jahangirb.pmpprep.data.SyncMerge
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Test
import java.nio.file.Files
import java.util.TimeZone

/** The support reminder (docs/progress-format.md §4) and its two shared slices. */
class SupportTest {

    private val utc: TimeZone = TimeZone.getTimeZone("UTC")
    private val day = 24L * 60 * 60 * 1000
    /** 2026-10-01, noon UTC. */
    private val t0 = 1_790_856_000_000L

    /** The app, opened once a day: records the date, asks, and marks the message shown when it is. */
    private inner class Sim(var opens: List<String> = emptyList(), var support: SupportState = SupportState()) {
        fun open(i: Int, busy: Boolean = false): Due {
            val now = t0 + i * day
            val today = Support.day(now, utc)
            opens = Support.recordOpen(opens, today)
            val due = Support.due(opens, support, today, now, busy, utc)
            if (due != Due.NONE) support = Support.markShown(support, now, today)
            return due
        }
    }

    @Test fun dayAndDates() {
        assertEquals("2026-10-01", Support.day(t0, utc))
        assertEquals("2026-11-01", Support.addDays("2026-10-31", 1))
        assertEquals("2026-02-28", Support.addDays("2026-03-01", -1))
    }

    @Test fun noMessageBeforeSevenDaysThenFirstOnDaySeven() {
        val s = Sim()
        for (i in 0 until 6) assertEquals(Due.NONE, s.open(i))
        assertEquals(Due.FIRST, s.open(6))
        assertEquals(Support.addDays("2026-10-07", 1), s.support.anchor)
        assertEquals(t0 + 6 * day, s.support.shownAt)
    }

    @Test fun countRestartsAfterShowing() {
        val s = Sim()
        for (i in 0..6) s.open(i)
        for (i in 7..12) assertEquals("day $i", Due.NONE, s.open(i))
        assertEquals(Due.FIRST, s.open(13))
    }

    @Test fun aGapResetsTheStreak() {
        val s = Sim()
        for (i in 0..4) s.open(i)
        // Day 5 missed.
        for (i in 6..11) assertEquals(Due.NONE, s.open(i))
        assertEquals(Due.FIRST, s.open(12))
    }

    @Test fun pressedIsQuietFor30DaysThenAlreadyOnceThenTheWeeklyCycle() {
        val s = Sim()
        for (i in 0..6) s.open(i)
        s.support = Support.markPressed(s.support, t0 + 6 * day + 1000)
        for (i in 7..36) assertEquals("day $i", Due.NONE, s.open(i))
        assertEquals(Due.ALREADY, s.open(37))
        for (i in 38..43) assertEquals("day $i", Due.NONE, s.open(i))
        assertEquals(Due.ALREADY, s.open(44))
    }

    @Test fun busyShowsNothingAndTheNextOpenDoes() {
        val s = Sim()
        for (i in 0..5) s.open(i)
        assertEquals(Due.NONE, s.open(6, busy = true))
        assertEquals(SupportState(), s.support)
        assertEquals(Due.FIRST, s.open(6))
    }

    @Test fun atMostOnceADay() {
        val opens = (0..9).map { Support.day(t0 + it * day, utc) }
        val now = t0 + 9 * day
        val today = Support.day(now, utc)
        // Shown earlier today on another device whose anchor hasn't arrived: still nothing.
        assertEquals(Due.NONE, Support.due(opens, SupportState(shownAt = now - 3_600_000), today, now, false, utc))
        assertEquals(Due.FIRST, Support.due(opens, SupportState(shownAt = now - day), today, now, false, utc))
    }

    @Test fun opensKeepTheNewest60() {
        var opens = emptyList<String>()
        for (i in 0 until 70) opens = Support.recordOpen(opens, Support.day(t0 + i * day, utc))
        assertEquals(60, opens.size)
        assertEquals(Support.day(t0 + 10 * day, utc), opens.first())
        assertEquals(opens, Support.recordOpen(opens, opens.last()))
    }

    @Test fun progressDocumentRoundTrip() {
        val dir = Files.createTempDirectory("pmp").toFile()
        val a = ProgressStore(dir)
        a.recordOpen("2026-10-02")
        a.recordOpen("2026-10-01")
        a.setSupport(SupportState(pressedAt = 5, shownAt = 7, anchor = "2026-10-03"))
        val doc = a.toJson()
        assertEquals(JSONArray(listOf("2026-10-01", "2026-10-02")).toString(), doc.getJSONArray("opens").toString())
        assertEquals("2026-10-03", doc.getJSONObject("support").getString("anchor"))
        val b = ProgressStore(Files.createTempDirectory("pmp").toFile())
        b.restore(doc.toString())
        assertEquals(listOf("2026-10-01", "2026-10-02"), b.opens)
        assertEquals(SupportState(5, 7, "2026-10-03"), b.support)
        // Unset fields stay out of the document.
        val c = ProgressStore(Files.createTempDirectory("pmp").toFile())
        assertEquals("{}", c.toJson().getJSONObject("support").toString())
    }

    @Test fun mergeKeepsTheHigherSupportValueAndMergesOpensAsASet() {
        fun doc(savedAt: Long, opens: List<String>, support: JSONObject) = SyncMerge.emptyDoc().put("savedAt", savedAt)
            .put("opens", JSONArray(opens)).put("support", support)
        val base = doc(1, listOf("2026-10-01", "2026-10-02"), JSONObject().put("pressedAt", 100).put("anchor", "2026-10-01"))
        val local = doc(9, listOf("2026-10-02", "2026-10-03"), JSONObject().put("pressedAt", 200).put("shownAt", 50).put("anchor", "2026-10-05"))
        val remote = doc(5, listOf("2026-10-01", "2026-10-02", "2026-10-04"), JSONObject().put("pressedAt", 300).put("shownAt", 80).put("anchor", "2026-10-04"))
        val m = SyncMerge.merge(base, local, remote, 10, JSONObject())
        assertEquals("[\"2026-10-02\",\"2026-10-03\",\"2026-10-04\"]", SyncMerge.canonical(m.getJSONArray("opens")))
        val s = m.getJSONObject("support")
        assertEquals(300L, s.getLong("pressedAt"))
        assertEquals(80L, s.getLong("shownAt"))
        assertEquals("2026-10-05", s.getString("anchor"))
    }
}
