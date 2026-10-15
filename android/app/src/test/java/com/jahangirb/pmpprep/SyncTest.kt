package com.jahangirb.pmpprep

import com.jahangirb.pmpprep.data.Attempt
import com.jahangirb.pmpprep.data.CardState
import com.jahangirb.pmpprep.data.CloudSync
import com.jahangirb.pmpprep.data.ContentParser
import com.jahangirb.pmpprep.data.Domain
import com.jahangirb.pmpprep.data.MasteryRule
import com.jahangirb.pmpprep.data.MockBands
import com.jahangirb.pmpprep.data.ProgressStore
import com.jahangirb.pmpprep.data.QType
import com.jahangirb.pmpprep.data.Rank
import com.jahangirb.pmpprep.data.Readiness
import com.jahangirb.pmpprep.data.RootCause
import com.jahangirb.pmpprep.data.Rules
import com.jahangirb.pmpprep.data.Srs
import com.jahangirb.pmpprep.data.SyncMerge
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import java.io.File
import java.io.IOException
import java.nio.file.Files

/** The shared progress format, the three-way merge, the shared rules and the cloud sync protocol. */
class SyncTest {

    private fun dir() = Files.createTempDirectory("pmp").toFile()

    private fun a(q: String, at: Long, ok: Boolean = true) =
        Attempt(q, "people-1", Domain.PEOPLE, null, QType.SINGLE, if (ok) 1.0 else 0.0, ok, 60_000, at, "practice")

    /* ── the shared fixture (tests/fixtures/sync) ────────────────────────── */

    /** The fixture folder: -Dpmp.fixtures, or found by walking up from the working directory. */
    private fun fixtures(): File {
        System.getProperty("pmp.fixtures")?.let { return File(it) }
        var d: File? = File(System.getProperty("user.dir")).absoluteFile
        while (d != null) {
            val f = File(d, "tests/fixtures/sync")
            if (File(f, "merged.json").exists()) return f
            d = d.parentFile
        }
        error("tests/fixtures/sync not found; set -Dpmp.fixtures")
    }

    private fun fixture(name: String) = JSONObject(File(fixtures(), "$name.json").readText())

    @Test fun mergeMatchesTheSharedFixture() {
        val device = JSONObject().put("app", "android").put("id", "test")
        val merged = SyncMerge.merge(fixture("base"), fixture("local"), fixture("remote"), 5000, device)
        val want = fixture("merged")
        assertEquals(SyncMerge.canonical(SyncMerge.content(want)), SyncMerge.canonical(SyncMerge.content(merged)))
        assertTrue(SyncMerge.same(merged, want))
        // Stamped as this device's copy.
        assertEquals(5000L, merged.getLong("savedAt"))
        assertEquals("android", merged.getJSONObject("device").getString("app"))
    }

    @Test fun swappingTheSidesKeepsTheSameWinners() {
        // Swapping local and remote changes only what the later savedAt decides; here the sides agree on it.
        val base = fixture("base")
        val local = fixture("local")
        val remote = fixture("remote")
        val m1 = SyncMerge.merge(base, local, remote, 1, JSONObject())
        val m2 = SyncMerge.merge(base, remote, local, 1, JSONObject())
        // remote's savedAt (2000) is older than local's (3000), so with the sides swapped the "later" rule still picks local's note.
        assertEquals(m1.getJSONObject("notes").toString(), m2.getJSONObject("notes").toString())
        assertEquals(SyncMerge.canonical(m1.getJSONObject("cards")), SyncMerge.canonical(m2.getJSONObject("cards")))
    }

    @Test fun firstSyncWithNoBaseUnitesBothSides() {
        val local = JSONObject().put("type", "pmp-progress").put("version", 3).put("savedAt", 10)
            .put("flags", JSONArray().put("a")).put("notes", JSONObject().put("q1", "mine"))
        val remote = JSONObject().put("type", "pmp-progress").put("version", 3).put("savedAt", 20)
            .put("flags", JSONArray().put("b")).put("notes", JSONObject().put("q1", "theirs").put("q2", "x"))
            .put("plan", JSONObject().put("hours", 12)).put("notepad", "remote page")
        val m = SyncMerge.merge(null, local, remote, 30, JSONObject())
        assertEquals(listOf("a", "b"), (0 until 2).map { m.getJSONArray("flags").getString(it) })
        // Both added q1: the later copy (remote) wins.
        assertEquals("theirs", m.getJSONObject("notes").getString("q1"))
        assertEquals("x", m.getJSONObject("notes").getString("q2"))
        // An empty local notes page is no change, so the remote page is kept.
        assertEquals("remote page", m.getString("notepad"))
        assertEquals(12, m.getJSONObject("plan").getInt("hours"))
    }

    @Test fun logsUniteByIdentityKeepLocalSortAndCap() {
        val chat = { r: String, at: Int, t: String -> JSONObject().put("r", r).put("at", at).put("t", t) }
        val local = JSONObject().put("savedAt", 1).put("chat", JSONArray().apply { (1..40).forEach { put(chat("user", it, "local $it")) } })
        val remote = JSONObject().put("savedAt", 2).put("chat", JSONArray().apply { (21..70).forEach { put(chat("user", it, "remote $it")) } })
        val c = SyncMerge.merge(null, local, remote, 3, JSONObject()).getJSONArray("chat")
        assertEquals(60, c.length())
        assertEquals(11, c.getJSONObject(0).getInt("at"))
        assertEquals(70, c.getJSONObject(59).getInt("at"))
        // Same identity on both sides: the local record.
        assertEquals("local 30", c.getJSONObject(19).getString("t"))
        assertEquals("remote 50", c.getJSONObject(39).getString("t"))
    }

    @Test fun mistakesBothChangedKeepTheLaterTag() {
        val m = { at: Int, c: String -> JSONObject().put("mistakes", JSONObject().put("q", JSONObject().put("c", c).put("n", "").put("at", at))) }
        val base = m(1, "KNOWLEDGE_GAP")
        val local = m(5, "RUSHED").put("savedAt", 100)
        val remote = m(9, "MISREAD_STEM").put("savedAt", 50)
        val out = SyncMerge.merge(base, local, remote, 0, JSONObject())
        assertEquals("MISREAD_STEM", out.getJSONObject("mistakes").getJSONObject("q").getString("c"))
    }

    @Test fun canonicalJsonSortsKeysAndWritesWholeNumbersPlainly() {
        assertEquals("""{"a":1,"b":[2.5,3]}""", SyncMerge.canonical(JSONObject("""{"b":[2.5,3.0],"a":1}""")))
        assertTrue(SyncMerge.same(JSONObject("""{"type":"pmp-progress","savedAt":1}"""), JSONObject("""{"savedAt":2,"type":"pmp-progress","flags":[]}""")))
    }

    /* ── the progress document, version 3 ────────────────────────────────── */

    @Test fun storeWritesVersion3AndKeepsWhatItDoesNotRead() {
        val d = dir()
        val s = ProgressStore(d)
        val doc = JSONObject()
            .put("type", "pmp-progress").put("version", 3).put("savedAt", 1234)
            .put("device", JSONObject().put("app", "web").put("id", "w1"))
            .put("attempts", JSONArray().put(JSONObject().put("q", "q1").put("t", "people-1").put("d", "people").put("ap", "")
                .put("ty", "single").put("cr", 1).put("ok", true).put("ms", 30000).put("at", 50).put("src", "practice")
                .put("df", 4).put("h", 2)))
            .put("cards", JSONObject().put("c1", JSONArray("[2.5,1,1,0,500,100,100,1]")).put("c2", JSONArray("[2.5,1,1,0,500,100,100]")))
            .put("mistakes", JSONObject().put("q1", JSONObject().put("c", "tossup").put("n", "").put("at", 5)))
            .put("web", JSONObject().put("settings", JSONObject().put("dailyTarget", 40)).put("notepadHtml", "<b>x</b>"))
            .put("native", JSONObject().put("ios", JSONObject().put("k", 1)))
            .put("future", JSONObject().put("x", 1))
        s.restore(doc.toString())
        assertEquals(4, s.attempts[0].difficulty)
        assertEquals(2, s.attempts[0].hints)
        assertTrue(s.attempts[0].hinted)
        assertTrue(s.cards["c1"]!!.suspended)
        assertFalse(s.cards["c2"]!!.suspended)
        assertEquals(RootCause.TWO_PLAUSIBLE, s.mistakeTags["q1"]!!.cause)

        val out = s.toJson()
        assertEquals("pmp-progress", out.getString("type"))
        assertEquals(3, out.getInt("version"))
        assertTrue(out.getLong("savedAt") > 1234)  // restoring is a change on this phone
        assertEquals("android", out.getJSONObject("device").getString("app"))
        assertEquals(s.deviceId, out.getJSONObject("device").getString("id"))
        assertEquals(8, out.getJSONObject("cards").getJSONArray("c1").length())
        assertEquals(1, out.getJSONObject("cards").getJSONArray("c1").getInt(7))
        assertEquals(0, out.getJSONObject("cards").getJSONArray("c2").getInt(7))
        val att = out.getJSONArray("attempts").getJSONObject(0)
        assertEquals(4, att.getInt("df"))
        assertEquals(2, att.getInt("h"))
        assertEquals(SyncMerge.canonical(doc.get("web")), SyncMerge.canonical(out.get("web")))
        assertEquals(SyncMerge.canonical(doc.get("native")), SyncMerge.canonical(out.get("native")))
        assertEquals(SyncMerge.canonical(doc.get("future")), SyncMerge.canonical(out.get("future")))

        // The device id stays with the install; the file round-trips through a restart.
        Thread.sleep(300)  // the write is in the background
        val again = ProgressStore(d)
        assertEquals(s.deviceId, again.deviceId)
        assertEquals(s.savedAt, again.savedAt)
        assertEquals(SyncMerge.canonical(out.get("web")), SyncMerge.canonical(again.toJson().get("web")))
        assertTrue(again.cards["c1"]!!.suspended)
    }

    @Test fun storeReadsAndroidVersion2() {
        val s = ProgressStore(dir())
        s.restore("""{"type":"pmp-android-progress","version":2,"attempts":[{"q":"q1","t":"people-1","d":"people","ap":"","ty":"single",
            "cr":1,"ok":true,"ms":1,"at":5,"src":"practice","cf":3}],"cards":{"c":[2.5,1,1,0,9,8,7]},"flags":["q1"]}""")
        assertEquals(1, s.attempts.size)
        assertEquals(Attempt.SURE, s.attempts[0].confidence)
        assertEquals(CardState(2.5, 1, 1, 0, 9, 8, 7), s.cards["c"])
        assertEquals(setOf("q1"), s.flags.toSet())
        assertEquals(3, s.toJson().getInt("version"))
    }

    @Test fun restoreRejectsWhatItCannotRead() {
        val s = ProgressStore(dir())
        fun msg(text: String): String = try { s.restore(text); "" } catch (e: IllegalArgumentException) { e.message.orEmpty() }
        assertTrue(msg("""{"type":"pmp-progress","schemaVersion":2}""").contains("older version of the web app"))
        assertTrue(msg("""{"type":"pmp-progress","version":4}""").contains("newer version"))
        assertTrue(msg("""{"type":"pmp-pack","questions":[]}""").contains("question pack"))
        assertTrue(msg("""{"type":"something"}""").contains("isn't a PMP Prep progress backup"))
        assertTrue(msg("not json").contains("isn't valid JSON"))
        assertEquals("", msg("""{"type":"pmp-progress","version":3}"""))
    }

    @Test fun suspendedCardsAreNeverDueAndKeepTheirFlagThroughAReview() {
        val srs = Srs(ContentParser.extras(minimalExtras()).srs)
        val s = CardState(2.5, 1, 1, 0, due = 100, last = 50, first = 50, suspended = true)
        assertFalse(s.isDue(1000))
        assertTrue(s.copy(suspended = false).isDue(1000))
        assertTrue(srs.review(s, com.jahangirb.pmpprep.data.Grade.GOOD, 1000).suspended)
    }

    @Test fun hintedAnswersStayOutOfAccuracyAndPace() {
        val x = ContentParser.extras(minimalExtras())
        val r = Readiness(x.readiness, x.exam, x.tasks)
        val plain = (1..20).map { a("q$it", 1000L + it, ok = false) }
        val hinted = (1..20).map { a("h$it", 2000L + it, ok = true).copy(hints = 1, ms = 5_000) }
        assertEquals(r.accuracy(plain, 5000).first, r.accuracy(plain + hinted, 5000).first, 1e-9)
        assertEquals(r.medianPace(plain), r.medianPace(plain + hinted))
    }

    /* ── shared rules ────────────────────────────────────────────────────── */

    @Test fun rulesDefaultToTheDocumentedValues() {
        val r = ContentParser.extras(minimalExtras()).rules
        assertEquals(Rules(), r)
        assertEquals(MockBands(0.80, 0.65, 0.50), r.mockBands)
        assertEquals(MasteryRule(10, 15, 0.75, 14), r.mastery)
        assertEquals(5, r.reentryDays)
        assertEquals(listOf(1.0, 0.5, 0.25, 0.0), Rank.entries.map { r.credit(it) })
        assertEquals(listOf("KNOWLEDGE_GAP", "MISREAD_STEM", "WRONG_MINDSET", "TWO_PLAUSIBLE", "CALCULATION", "RUSHED"), r.causes.map { it.id })
        assertEquals(listOf(3, 2, 1), r.confidence.map { it.v })
        assertEquals("Above Target", r.mockBands.label(0.8))
        assertEquals("Target", r.mockBands.label(0.79))
        assertEquals("Below Target", r.mockBands.label(0.5))
        assertEquals("Needs Improvement", r.mockBands.label(0.49))
    }

    @Test fun rulesAreReadFromExtras() {
        val rules = JSONObject("""{"mockBands":{"above":0.9,"target":0.7,"below":0.4},
            "scenarioCredit":{"best":1,"acceptable":0.6,"premature":0.3,"wrong":0},
            "mastery":{"minAnswers":8,"window":12,"accuracy":0.8,"fadeDays":10},
            "causes":[{"id":"RUSHED","short":"Rushed","label":"I rushed","hint":"Went too fast"},
                      {"id":"TWO_PLAUSIBLE","short":"Two plausible answers","label":"It was between two","hint":"Picked the weaker"}],
            "reentryDays":6,
            "confidence":[{"v":3,"id":"sure","label":"Sure"},{"v":1,"id":"guess","label":"Guess"}]}""")
        val r = ContentParser.extras(minimalExtras(rules)).rules
        assertEquals(MockBands(0.9, 0.7, 0.4), r.mockBands)
        assertEquals(0.6, r.credit(Rank.ACCEPTABLE), 1e-9)
        assertEquals(MasteryRule(8, 12, 0.8, 10), r.mastery)
        assertEquals(6, r.reentryDays)
        assertEquals("I rushed", r.cause(RootCause.RUSHED).label)
        // A cause the rules leave out keeps its built-in words and still appears, after the listed ones.
        assertEquals("Knowledge gap", r.cause(RootCause.KNOWLEDGE_GAP).short)
        assertEquals(listOf(RootCause.RUSHED, RootCause.TWO_PLAUSIBLE), r.causeOrder.take(2))
        assertEquals(RootCause.entries.size, r.causeOrder.size)
        assertEquals(listOf(3, 1), r.confidence.map { it.v })
        assertEquals("print('__SYNC_CODE__')", ContentParser.extras(minimalExtras(rules)).syncScript)
    }

    /** When the build's extras carry PMP.RULES and the script, they must agree with docs/progress-format.md §1 and §4. */
    @Test fun theBuildsRulesMatchTheDocumentedValues() {
        val dir = System.getProperty("pmp.assets") ?: return
        val f = File(dir, "content/extras.json")
        if (!f.exists()) return
        val o = JSONObject(f.readText())
        val x = ContentParser.extras(f.readText())
        if (o.has("rules")) {
            val d = Rules()
            assertEquals(d.mockBands, x.rules.mockBands)
            assertEquals(d.scenarioCredit, x.rules.scenarioCredit)
            assertEquals(d.mastery, x.rules.mastery)
            assertEquals(d.reentryDays, x.rules.reentryDays)
            assertEquals(d.causes.map { it.id }, x.rules.causes.map { it.id })
            assertEquals(d.causes.map { it.short }, x.rules.causes.map { it.short })
            assertEquals(d.confidence.map { it.v to it.id }, x.rules.confidence.map { it.v to it.id })
        }
        if (o.has("syncScript")) assertTrue(x.syncScript.contains("'" + CloudSync.CODE_PLACEHOLDER + "'"))
    }

    @Test fun oldWebCauseIdsMap() {
        assertEquals(RootCause.KNOWLEDGE_GAP, RootCause.of("knowledge"))
        assertEquals(RootCause.MISREAD_STEM, RootCause.of("misread"))
        assertEquals(RootCause.RUSHED, RootCause.of("rushed"))
        assertEquals(RootCause.WRONG_MINDSET, RootCause.of("mindset"))
        assertEquals(RootCause.TWO_PLAUSIBLE, RootCause.of("tossup"))
        assertNull(RootCause.of("nonsense"))
    }

    /** The smallest extras.json the parser accepts. */
    private fun minimalExtras(rules: JSONObject? = null): String = JSONObject()
        .put("eco", JSONObject().put("LABEL", "ECO").put("DOMAIN_WEIGHTS", JSONObject().put("people", 0.42).put("process", 0.5).put("business", 0.08)))
        .put("exam", JSONObject()).put("srs", JSONObject()).put("readiness", JSONObject().put("WEIGHTS", JSONObject()))
        .put("tasks", JSONArray().put(JSONObject().put("id", "people-1").put("domain", "people").put("n", 1).put("label", "Conflict")))
        .put("reference", JSONObject())
        .apply { if (rules != null) put("rules", rules).put("syncScript", "print('__SYNC_CODE__')") }
        .toString()

    /* ── the sync protocol, against a fake script ────────────────────────── */

    /** Behaves like sync/pmp-sync.gs: a revision, one stored document, the code check. */
    private class FakeScript(val code: String = "C".repeat(32)) : CloudSync.Transport {
        var rev = 0L
        var doc: JSONObject? = null
        val ops = ArrayList<String>()
        /** Runs before each put is answered, to simulate another device writing in between. */
        var beforePut: (() -> Unit)? = null

        override fun post(url: String, body: String): String {
            val req = JSONObject(body)
            ops += req.getString("op")
            if (req.optString("code") != code) return """{"ok":false,"error":"bad-code"}"""
            return when (req.getString("op")) {
                "ping" -> JSONObject().put("ok", true).put("rev", rev).put("savedAt", 0).toString()
                "get" -> JSONObject().put("ok", true).put("rev", rev).put("doc", doc ?: JSONObject.NULL).toString()
                "put" -> {
                    beforePut?.invoke()
                    if (req.getLong("baseRev") != rev) {
                        JSONObject().put("ok", false).put("conflict", true).put("rev", rev).put("doc", doc ?: JSONObject.NULL).toString()
                    } else {
                        doc = req.getJSONObject("doc")
                        rev++
                        JSONObject().put("ok", true).put("rev", rev).toString()
                    }
                }
                else -> """{"ok":false,"error":"bad-op"}"""
            }
        }
    }

    private val url = "https://script.google.com/macros/s/abc/exec"
    private val device = JSONObject().put("app", "android").put("id", "me")

    private fun localDoc(vararg flags: String, savedAt: Long = 10) = JSONObject().put("type", "pmp-progress").put("version", 3)
        .put("savedAt", savedAt).put("flags", JSONArray(flags.toList()))

    @Test fun firstSyncPutsWhenTheCloudIsEmpty() {
        val f = FakeScript()
        val r = CloudSync(f).sync(url, f.code, null, localDoc("q1"), 100, device)
        assertTrue(r.pushed)
        assertEquals(1L, r.rev)
        assertEquals(listOf("get", "put"), f.ops)
        assertEquals("q1", f.doc!!.getJSONArray("flags").getString(0))
    }

    @Test fun noPutWhenNothingChanged() {
        val f = FakeScript()
        val sync = CloudSync(f)
        val first = sync.sync(url, f.code, null, localDoc("q1"), 100, device)
        f.ops.clear()
        val second = sync.sync(url, f.code, first.merged, first.merged, 200, device)
        assertFalse(second.pushed)
        assertEquals(listOf("get"), f.ops)
    }

    @Test fun aConflictMergesAgainWithTheOtherDevicesCopy() {
        val f = FakeScript()
        f.doc = localDoc("base"); f.rev = 1
        // Another device adds "theirs" between our get and our put, once.
        f.beforePut = {
            f.doc = localDoc("base", "theirs"); f.rev = 2
            f.beforePut = null
        }
        val r = CloudSync(f).sync(url, f.code, localDoc("base"), localDoc("base", "mine"), 100, device)
        assertTrue(r.pushed)
        assertEquals(listOf("get", "put", "put"), f.ops)
        assertEquals(listOf("base", "mine", "theirs"), (0 until 3).map { f.doc!!.getJSONArray("flags").getString(it) })
        assertEquals(3L, r.rev)
    }

    @Test fun givesUpAfterThreeRetries() {
        val f = FakeScript()
        f.beforePut = { f.rev++; f.doc = localDoc("x${f.rev}") }
        try {
            CloudSync(f).sync(url, f.code, null, localDoc("mine"), 100, device)
            fail("expected a sync error")
        } catch (e: CloudSync.SyncException) {
            assertTrue(e.message!!.contains("Another device"))
        }
        assertEquals(1 + 1 + CloudSync.RETRIES, f.ops.size)
    }

    @Test fun aWrongCodeAndAPageThatIsNotTheScriptAreClearErrors() {
        val f = FakeScript()
        try { CloudSync(f).ping(url, "wrong"); fail() } catch (e: CloudSync.SyncException) { assertTrue(e.badCode) }
        val login = CloudSync { _, _ -> "<html>Sign in</html>" }
        try { login.ping(url, "x"); fail() } catch (e: CloudSync.SyncException) { assertTrue(e.message!!.contains("Who has access: Anyone")) }
        val offline = CloudSync { _, _ -> throw IOException("no route") }
        try { offline.ping(url, "x"); fail() } catch (e: CloudSync.SyncException) { assertTrue(e.message!!.contains("Couldn't reach")) }
    }

    @Test fun aCloudFileOfAnotherKindIsRefused() {
        val f = FakeScript()
        f.doc = JSONObject().put("type", "pmp-pack")
        try { CloudSync(f).sync(url, f.code, null, localDoc(), 1, device); fail() } catch (e: CloudSync.SyncException) {
            assertTrue(e.message!!.contains("isn't progress"))
        }
    }

    @Test fun codesAndUrls() {
        val c = CloudSync.newCode()
        assertEquals(32, c.length)
        assertNotEquals(c, CloudSync.newCode())
        assertEquals(c, CloudSync.cleanCode("  $c "))
        assertNull(CloudSync.cleanCode("short"))
        assertNull(CloudSync.cleanCode("has'quote" + "x".repeat(20)))
        assertTrue(CloudSync.script("const SYNC_CODE = '__SYNC_CODE__';", c).contains("'$c'"))
        assertEquals(url, CloudSync.cleanUrl("  $url\n"))
        assertNull(CloudSync.cleanUrl("http://script.google.com/macros/s/abc/exec"))
    }
}
