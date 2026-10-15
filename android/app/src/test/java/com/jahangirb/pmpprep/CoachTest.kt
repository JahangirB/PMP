package com.jahangirb.pmpprep

import com.jahangirb.pmpprep.data.Attempt
import com.jahangirb.pmpprep.data.ChatTurn
import com.jahangirb.pmpprep.data.Coach
import com.jahangirb.pmpprep.data.CoachAction
import com.jahangirb.pmpprep.data.ContentParser
import com.jahangirb.pmpprep.data.DiagnosticResult
import com.jahangirb.pmpprep.data.Domain
import com.jahangirb.pmpprep.data.Gemini
import com.jahangirb.pmpprep.data.ProgressStore
import com.jahangirb.pmpprep.data.Question
import com.jahangirb.pmpprep.data.Readiness
import com.jahangirb.pmpprep.data.RootCause
import com.jahangirb.pmpprep.data.Srs
import com.jahangirb.pmpprep.data.StudyPlan
import com.jahangirb.pmpprep.data.SessionResult
import com.jahangirb.pmpprep.data.SessionKind
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import java.io.File
import java.nio.file.Files

class CoachTest {
    private val dir = File(System.getProperty("pmp.assets") ?: error("pmp.assets not set"))
    private val x = ContentParser.extras(File(dir, "content/extras.json").readText())
    private val pool: List<Question> = ContentParser.parsePack(File(dir, "content/core.json").readText(), "core", null, null, true).questions

    private fun store() = ProgressStore(Files.createTempDirectory("pmp").toFile())
    private fun coach(s: ProgressStore) = Coach(x, pool, s, Readiness(x.readiness, x.exam, x.tasks))

    @Test
    fun aNewLearnerIsSentToTheDiagnosticAndThePlanner() {
        val r = coach(store()).report()
        assertTrue(r.steps.any { it.action == CoachAction.DIAGNOSTIC })
        assertTrue(r.steps.any { it.action == CoachAction.PLANNER })
        assertTrue(r.messages.any { it.id == "diagnostic_first" })
        assertTrue(r.messages.any { it.id == "plan_offer" })
    }

    @Test
    fun theReportReflectsRealAnswers() {
        val s = store()
        val now = System.currentTimeMillis()
        s.setPlan(StudyPlan(now + 30 * Srs.DAY_MS, 10))
        s.recordDiagnostic(DiagnosticResult(now, 25, 15, mapOf(Domain.PEOPLE to (6 to 8), Domain.PROCESS to (6 to 10), Domain.BUSINESS to (3 to 7))))
        // Weak on one task, strong on another, misreading the stem.
        val weak = pool.filter { it.ecoTask == "process-6" }.take(8)
        val strong = pool.filter { it.ecoTask == "people-2" }.take(8)
        weak.forEach { q ->
            s.record(Attempt(q.id, q.ecoTask, q.domain, q.approach, q.type, 0.0, false, 60_000, now, "practice"))
            s.tagMistake(q.id, RootCause.MISREAD_STEM, "")
        }
        strong.forEach { q -> s.record(Attempt(q.id, q.ecoTask, q.domain, q.approach, q.type, 1.0, true, 60_000, now, "practice")) }
        val r = coach(s).report(now)
        assertFalse(r.steps.any { it.action == CoachAction.DIAGNOSTIC })
        assertTrue(r.improve.joinToString(), r.improve.any { it.contains("Project finance") || it.contains("PR6") })
        assertTrue(r.strengths.joinToString(), r.strengths.any { it.contains("P2") })
        assertTrue(r.messages.any { it.id == "cause_misread" })
        assertTrue(r.messages.any { it.id == "first_mock_due" })

        val snap = coach(s).snapshot(now)
        assertTrue(snap, snap.contains("Diagnostic: 15/25"))
        assertTrue(snap.contains("Exam date: in 30 days"))
        assertTrue(snap.contains("Misread the stem 8"))
        assertTrue(snap.contains("PR6"))
    }

    @Test
    fun snoozedMessagesStayQuiet() {
        val s = store()
        val now = System.currentTimeMillis()
        s.snooze("diagnostic_first", now + Srs.DAY_MS)
        assertFalse(coach(s).report(now).messages.any { it.id == "diagnostic_first" })
    }

    @Test
    fun notesDiagnosticsAndChatSurviveAReload() {
        val d = Files.createTempDirectory("pmp").toFile()
        val s = ProgressStore(d)
        s.setNotepad("**EAC** = BAC / CPI")
        s.recordDiagnostic(DiagnosticResult(1L, 25, 20, mapOf(Domain.PEOPLE to (7 to 8))))
        s.addChat(ChatTurn("user", "hi", 1L))
        s.setPlan(StudyPlan(5_000L, 6, setOf(1, 7), onboarded = true))
        s.recordSession(SessionResult("build:people-3", SessionKind.BUILD, "people-3", Domain.PEOPLE, 0.8, 55, 9L))
        Thread.sleep(500) // the store writes in the background
        val back = ProgressStore(d)
        assertEquals("**EAC** = BAC / CPI", back.notepad)
        assertEquals(20, back.diagnostic!!.right)
        assertEquals(7 to 8, back.diagnostic!!.domains[Domain.PEOPLE])
        assertEquals("hi", back.chat.single().text)
        assertEquals(StudyPlan(5_000L, 6, setOf(1, 7), onboarded = true), back.plan)
        assertEquals(SessionResult("build:people-3", SessionKind.BUILD, "people-3", Domain.PEOPLE, 0.8, 55, 9L), back.sessionResults.single())
        // The key is never part of a backup.
        assertFalse(back.toJson().toString().contains("key"))
    }

    @Test
    fun geminiModelsAreFlashNewestStableFirst() {
        val json = """{"models":[
            {"name":"models/gemini-2.5-flash","displayName":"Gemini 2.5 Flash","supportedGenerationMethods":["generateContent"]},
            {"name":"models/gemini-3-flash-preview","displayName":"Gemini 3 Flash Preview","supportedGenerationMethods":["generateContent"]},
            {"name":"models/gemini-3-flash","displayName":"Gemini 3 Flash","supportedGenerationMethods":["generateContent"]},
            {"name":"models/gemini-3.1-flash-lite","displayName":"Gemini 3.1 Flash-Lite","supportedGenerationMethods":["generateContent"]},
            {"name":"models/gemini-3-pro","displayName":"Gemini 3 Pro","supportedGenerationMethods":["generateContent"]},
            {"name":"models/gemini-3-flash-image","supportedGenerationMethods":["generateContent"]},
            {"name":"models/text-embedding-004","supportedGenerationMethods":["embedContent"]}
        ]}"""
        val m = Gemini.parseModels(json)
        assertEquals(listOf("models/gemini-3.1-flash-lite", "models/gemini-3-flash", "models/gemini-3-flash-preview"), m.map { it.name })
        assertEquals("the newest full Flash, not a newer Lite", "models/gemini-3-flash", Gemini.defaultModel(m)!!.name)
        // The coach's line-up: full Flash from 3.5 up, newest first, at most five, 3.5 kept while listed.
        fun models(vararg names: String) = Gemini.parseModels("""{"models":[""" + names.joinToString(",") {
            """{"name":"models/$it","supportedGenerationMethods":["generateContent"]}""" } + "]}")
        assertEquals("nothing below 3.5, and no Lite", emptyList<String>(), Gemini.lineup(m).map { it.name })
        assertEquals(listOf("models/gemini-4-flash", "models/gemini-3.5-flash", "models/gemini-3.5-flash-preview"),
            Gemini.lineup(models("gemini-3.5-flash", "gemini-3.5-flash-preview", "gemini-4-flash", "gemini-4-flash-lite", "gemini-3-flash")).map { it.name })
        val crowded = Gemini.lineup(models("gemini-3.5-flash", "gemini-4-flash", "gemini-4.5-flash", "gemini-5-flash", "gemini-5.5-flash", "gemini-6-flash", "gemini-6-flash-preview"))
        assertEquals(5, crowded.size)
        assertEquals("newest first", "models/gemini-6-flash", crowded.first().name)
        assertEquals("3.5 keeps its place until Google drops it", "models/gemini-3.5-flash", crowded.last().name)
        val retired = Gemini.lineup(models("gemini-4-flash", "gemini-4.5-flash", "gemini-5-flash", "gemini-5.5-flash", "gemini-6-flash", "gemini-6.5-flash"))
        assertEquals(listOf("models/gemini-6.5-flash", "models/gemini-6-flash", "models/gemini-5.5-flash", "models/gemini-5-flash", "models/gemini-4.5-flash"), retired.map { it.name })
        // Which errors move a message on to the next model down.
        assertTrue(Gemini.tryNextModel(Gemini.GeminiException("busy", 503)))
        assertTrue("limits are per model", Gemini.tryNextModel(Gemini.GeminiException("limit", 429)))
        assertTrue(Gemini.tryNextModel(Gemini.GeminiException("retired", 404)))
        assertTrue(!Gemini.tryNextModel(Gemini.GeminiException("bad key", 400, keyProblem = true)))
        assertTrue("no connection", !Gemini.tryNextModel(Gemini.GeminiException("offline", 0)))
        // A key with only older Flash models still gets something to use.
        val old = Gemini.parseModels("""{"models":[{"name":"models/gemini-2.5-flash","supportedGenerationMethods":["generateContent"]}]}""")
        assertEquals("models/gemini-2.5-flash", old.single().name)
    }

    @Test
    fun geminiRequestsAndRepliesRoundTrip() {
        val body = JSONObject(Gemini.requestBody("system text", listOf(ChatTurn("user", "q", 0), ChatTurn("model", "a", 0))))
        assertEquals("system text", body.getJSONObject("systemInstruction").getJSONArray("parts").getJSONObject(0).getString("text"))
        assertEquals("model", body.getJSONArray("contents").getJSONObject(1).getString("role"))
        val reply = """{"candidates":[{"content":{"parts":[{"text":"thinking","thought":true},{"text":"Hello "},{"text":"there"}]}}]}"""
        assertEquals("Hello there", Gemini.parseReply(reply))
        try { Gemini.parseReply("""{"promptFeedback":{"blockReason":"SAFETY"}}"""); fail() } catch (e: Gemini.GeminiException) {
            assertTrue(e.message!!.contains("SAFETY"))
        }
        assertTrue(Gemini.explain(400, """{"error":{"details":[{"reason":"API_KEY_INVALID"}]}}""").contains("not valid"))
        assertTrue(Gemini.explain(429, "{}").contains("limiting"))
        assertNotNull(Coach.SYSTEM)
    }
}
