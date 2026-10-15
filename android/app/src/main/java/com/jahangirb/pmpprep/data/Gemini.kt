package com.jahangirb.pmpprep.data

import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

/**
 * Talks straight to Google's Generative Language API with the person's own
 * AI Studio key, as the web app's Gemini tab does (§12.2.2): no server in
 * between. The key travels in a header, never in a URL, and only to Google.
 */
object Gemini {
    const val API = "https://generativelanguage.googleapis.com/v1beta"

    /** [status] is Google's HTTP status, 0 when Google wasn't reached; [keyProblem] when the key itself was refused. */
    class GeminiException(message: String, val status: Int = 0, val keyProblem: Boolean = false) : IOException(message)

    data class Model(val name: String, val label: String, val rank: Double) {
        val stable: Boolean get() = !Regex("(preview|exp)", RegexOption.IGNORE_CASE).containsMatchIn(name)
    }

    /* ── pure parts, unit-tested ─────────────────────────────────────────── */

    private fun flashVersion(name: String): Double =
        Regex("gemini-(\\d+(?:\\.\\d+)?)-flash", RegexOption.IGNORE_CASE).find(name)?.groupValues?.get(1)?.toDoubleOrNull() ?: 0.0

    /**
     * Flash chat models, newest first; within a version the stable model leads,
     * then lite, then previews. The web app keeps to 3.0 and later; if a key
     * has none of those, older Flash models are offered rather than nothing.
     */
    fun parseModels(json: String): List<Model> {
        val arr = JSONObject(json).optJSONArray("models") ?: JSONArray()
        val all = (0 until arr.length()).mapNotNull { arr.optJSONObject(it) }.filter { m ->
            val name = m.optString("name")
            val methods = m.optJSONArray("supportedGenerationMethods")
            val gen = methods != null && (0 until methods.length()).any { methods.optString(it) == "generateContent" }
            gen && flashVersion(name) > 0 && !Regex("(tts|image|embedding|audio|live|thinking)", RegexOption.IGNORE_CASE).containsMatchIn(name)
        }.map { m ->
            val n = m.optString("name")
            val rank = flashVersion(n) * 100 -
                (if (Regex("(preview|exp)", RegexOption.IGNORE_CASE).containsMatchIn(n)) 2 else 0) -
                (if (n.contains("lite", ignoreCase = true)) 1 else 0)
            Model(n, m.optString("displayName").ifBlank { n.removePrefix("models/") }, rank)
        }.sortedWith(compareByDescending<Model> { it.rank }.thenBy { it.name })
        val modern = all.filter { flashVersion(it.name) >= 3 }
        return modern.ifEmpty { all }
    }

    /** The newest stable full Flash; failing that the newest stable model (a Lite), or the newest of any kind. */
    fun defaultModel(models: List<Model>): Model? =
        models.firstOrNull { it.stable && !it.name.contains("lite", ignoreCase = true) } ?: models.firstOrNull { it.stable } ?: models.firstOrNull()

    /** The oldest Flash the coach will use. */
    const val BASE_VERSION = 3.5
    /** The most models kept in the line-up. */
    const val MAX_MODELS = 5

    /**
     * The coach's line-up, tried in this order: full Flash models (no Lite) from [BASE_VERSION] up,
     * newest first, stable before preview within a version, at most [MAX_MODELS]. A [BASE_VERSION]
     * model keeps its place while Google still lists one, even when newer ones would fill the list.
     */
    fun lineup(models: List<Model>): List<Model> {
        val eligible = models.filter { flashVersion(it.name) >= BASE_VERSION - 1e-9 && !it.name.contains("lite", ignoreCase = true) }
            .sortedWith(compareByDescending<Model> { it.rank }.thenBy { it.name })
        if (eligible.size <= MAX_MODELS) return eligible
        val top = eligible.take(MAX_MODELS)
        if (top.any { isBase(it) }) return top
        val base = eligible.firstOrNull { isBase(it) } ?: return top
        return top.take(MAX_MODELS - 1) + base
    }

    private fun isBase(m: Model) = kotlin.math.abs(flashVersion(m.name) - BASE_VERSION) < 1e-9

    /**
     * Whether a failed request is worth trying on the next model down: yes for a model that's busy,
     * retired, rate-limited (limits are per model) or failing; no when the key was refused or Google
     * couldn't be reached, which no other model would change.
     */
    fun tryNextModel(e: GeminiException): Boolean = !e.keyProblem && e.status != 0

    fun requestBody(system: String, turns: List<ChatTurn>): String = JSONObject().apply {
        put("systemInstruction", JSONObject().put("parts", JSONArray().put(JSONObject().put("text", system))))
        put("contents", JSONArray().apply {
            turns.forEach { t ->
                put(JSONObject().put("role", if (t.role == "model") "model" else "user")
                    .put("parts", JSONArray().put(JSONObject().put("text", t.text))))
            }
        })
        put("generationConfig", JSONObject().put("temperature", 0.6))
    }.toString()

    fun parseReply(json: String): String {
        val o = JSONObject(json)
        val cand = o.optJSONArray("candidates")?.optJSONObject(0)
            ?: throw GeminiException(
                o.optJSONObject("promptFeedback")?.optString("blockReason")?.let { "Google declined to answer this ($it). Try rephrasing." }
                    ?: "Google sent back an empty reply. Try again."
            )
        val parts = cand.optJSONObject("content")?.optJSONArray("parts") ?: JSONArray()
        val text = (0 until parts.length()).mapNotNull { parts.optJSONObject(it) }
            .filter { !it.optBoolean("thought") }
            .joinToString("") { it.optString("text") }.trim()
        if (text.isEmpty()) throw GeminiException("Google sent back an empty reply (${cand.optString("finishReason", "no reason given")}). Try again.")
        return text
    }

    /** Google's error, in words someone can act on. Mirrors the web app's explain(). */
    fun explain(status: Int, body: String): String {
        val message = runCatching { JSONObject(body).optJSONObject("error")?.optString("message") }.getOrNull().orEmpty()
        return when {
            body.contains("API_KEY_INVALID") -> "Google says this key is not valid. Check you copied all of it, or create a new one in AI Studio."
            status == 429 -> "Google is limiting requests on this key: its free quota or per-minute rate is used up. Wait a minute and try again; a daily limit resets the next day."
            status == 403 -> "This key is not allowed to use the Gemini API. In AI Studio, check the key's project has the Generative Language API enabled."
            status == 404 -> "That model is not available to this key."
            else -> "Google returned an error ($status)" + if (message.isNotBlank()) ": $message" else "."
        }
    }

    /* ── network ─────────────────────────────────────────────────────────── */

    private fun call(path: String, key: String, body: String?): String {
        val conn = try {
            (URL(API + path).openConnection() as HttpURLConnection).apply {
                connectTimeout = 20_000
                readTimeout = 90_000
                requestMethod = if (body != null) "POST" else "GET"
                setRequestProperty("x-goog-api-key", key)
                if (body != null) {
                    doOutput = true
                    setRequestProperty("Content-Type", "application/json; charset=utf-8")
                }
            }
        } catch (e: IOException) {
            throw GeminiException("Could not reach Google. Check your internet connection and try again.")
        }
        try {
            if (body != null) conn.outputStream.use { it.write(body.toByteArray()) }
            val status = conn.responseCode
            val text = (if (status in 200..299) conn.inputStream else conn.errorStream)?.bufferedReader()?.use { it.readText() }.orEmpty()
            if (status !in 200..299) throw GeminiException(explain(status, text), status,
                keyProblem = text.contains("API_KEY_INVALID") || status == 401 || status == 403)
            return text
        } catch (e: GeminiException) {
            throw e
        } catch (e: IOException) {
            throw GeminiException("Could not reach Google. Check your internet connection and try again.")
        } finally {
            conn.disconnect()
        }
    }

    /** Lists the Flash models this key may use. Doubles as the check that a pasted key works. */
    fun listModels(key: String): List<Model> {
        val models = parseModels(call("/models?pageSize=1000", key, null))
        if (models.isEmpty()) throw GeminiException("This key works, but Google lists no Flash chat models for it.")
        return models
    }

    fun generate(key: String, model: String, system: String, turns: List<ChatTurn>): String =
        parseReply(call("/" + model.removePrefix("/") + ":generateContent", key, requestBody(system, turns)))
}
