package com.jahangirb.pmpprep.data

import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.security.SecureRandom

/**
 * Cloud sync through the person's own Google Apps Script (docs/progress-format.md §4).
 * The script keeps one file in their Drive; every call is a POST of a small JSON
 * request carrying the sync code. This class is the protocol and the client
 * algorithm only: get, merge three ways, put with the revision it was based on,
 * and on a conflict merge again with what the other device wrote. The network
 * sits behind [Transport] so the whole of it is tested on the JVM.
 */
class CloudSync(private val transport: Transport) {

    /** Sends one request body to the script and returns the response body. */
    fun interface Transport {
        @Throws(IOException::class)
        fun post(url: String, body: String): String
    }

    /** Something to show on the Cloud sync screen. [badCode] means the code doesn't match the script's. */
    class SyncException(message: String, val badCode: Boolean = false) : Exception(message)

    /** [merged] is the new local progress and base; [pushed] says whether the cloud copy changed. */
    data class Result(val merged: JSONObject, val rev: Long, val pushed: Boolean)

    /** Checks the URL and code: the script's revision and when it was last written. */
    fun ping(url: String, code: String): Pair<Long, Long> {
        val r = call(url, JSONObject().put("code", code).put("op", "ping"))
        return r.optLong("rev") to r.optLong("savedAt")
    }

    /**
     * One sync: get the cloud copy, merge it with [local] against [base], and put the result back
     * if it differs, retrying up to [RETRIES] times when another device wrote in between.
     */
    fun sync(url: String, code: String, base: JSONObject?, local: JSONObject, now: Long, device: JSONObject): Result {
        val got = call(url, JSONObject().put("code", code).put("op", "get"))
        var rev = got.optLong("rev")
        var remote = remoteDoc(got)
        var attempt = 0
        while (true) {
            val merged = SyncMerge.merge(base, local, remote ?: SyncMerge.emptyDoc(), now, device)
            if (remote != null && SyncMerge.same(merged, remote)) return Result(merged, rev, false)
            val put = request(url, JSONObject().put("code", code).put("op", "put").put("baseRev", rev).put("doc", merged))
            if (put.optBoolean("ok")) return Result(merged, put.optLong("rev", rev + 1), true)
            if (!put.optBoolean("conflict")) throw failure(put)
            if (attempt++ >= RETRIES) throw SyncException("Another device kept changing the sync file while this one synced. Try Sync now again.")
            rev = put.optLong("rev")
            remote = remoteDoc(put)
        }
    }

    /** The cloud copy in a get or conflict reply, or null when there is none yet. */
    private fun remoteDoc(reply: JSONObject): JSONObject? {
        val doc = reply.optJSONObject("doc") ?: return null
        try {
            ProgressStore.checkReadable(doc)
        } catch (e: IllegalArgumentException) {
            throw SyncException("The file in your Drive isn't progress this app can read. ${e.message}")
        }
        return doc
    }

    /** A call that must succeed. */
    private fun call(url: String, req: JSONObject): JSONObject {
        val r = request(url, req)
        if (!r.optBoolean("ok")) throw failure(r)
        return r
    }

    private fun request(url: String, req: JSONObject): JSONObject {
        val text = try {
            transport.post(url, req.toString())
        } catch (e: IOException) {
            throw SyncException("Couldn't reach your sync script. Check your connection and the URL. (${e.message ?: e.javaClass.simpleName})")
        }
        return try {
            JSONObject(text.trim())
        } catch (e: Exception) {
            // Usually a Google sign-in page: the deployment's "Who has access" isn't "Anyone".
            throw SyncException("The URL didn't answer like the PMP Prep sync script. Check it's the Web app URL ending in /exec, " +
                "and that the deployment has Execute as: Me and Who has access: Anyone.")
        }
    }

    private fun failure(r: JSONObject): SyncException = when (val e = r.optString("error")) {
        "bad-code" -> SyncException("The sync code doesn't match the one in your script. Check it, or copy the script again with this code and deploy a new version.", badCode = true)
        "busy" -> SyncException("Your sync script was busy with another device. Try again in a moment.")
        "bad-doc" -> SyncException("The script refused the progress file. Update the script to the latest version.")
        "bad-op", "bad-request" -> SyncException("The script didn't understand the request. Copy the script again and deploy a new version.")
        "" -> SyncException("The sync script answered with an error.")
        else -> SyncException("The sync script answered with an error: $e.")
    }

    companion object {
        /** Puts after the first one when another device wrote in between. */
        const val RETRIES = 3
        /** In the script as shipped, replaced by the person's code. */
        const val CODE_PLACEHOLDER = "__SYNC_CODE__"
        private const val CODE_CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789"

        /** A random 32-character sync code, from letters and digits that are hard to mix up when typed. */
        fun newCode(random: java.util.Random = SecureRandom()): String =
            String(CharArray(32) { CODE_CHARS[random.nextInt(CODE_CHARS.length)] })

        /** The script to deploy, with [code] filled in. */
        fun script(template: String, code: String): String = template.replace(CODE_PLACEHOLDER, code)

        /** Tidies a pasted URL and returns null unless it looks like an https web-app URL. */
        fun cleanUrl(raw: String): String? {
            val u = raw.trim()
            if (!u.startsWith("https://") || u.length < 12 || u.any { it.isWhitespace() }) return null
            return u
        }

        /**
         * A code typed or pasted on another device: at least 16 letters, digits, - or _. Nothing that
         * could break out of the quotes it sits in inside the script.
         */
        fun cleanCode(raw: String): String? =
            raw.trim().takeIf { c -> c.length >= 16 && c.all { it in 'A'..'Z' || it in 'a'..'z' || it in '0'..'9' || it == '-' || it == '_' } }
    }

    /**
     * The real network: HTTPS POST with a text/plain body (no CORS preflight on the web, and the same
     * request from every app). Apps Script answers a POST with a 302 to script.googleusercontent.com,
     * which must be fetched with GET; HttpURLConnection won't follow a redirect across hosts, so the
     * Location is followed here by hand. Call it off the main thread.
     */
    class Http(private val connectMs: Int = 20_000, private val readMs: Int = 45_000) : Transport {
        override fun post(url: String, body: String): String {
            var conn = open(URL(url))
            conn.requestMethod = "POST"
            conn.doOutput = true
            conn.setRequestProperty("Content-Type", "text/plain;charset=utf-8")
            conn.outputStream.use { it.write(body.toByteArray(Charsets.UTF_8)) }
            var status = conn.responseCode
            var hops = 0
            while (status in 300..399 && hops < MAX_REDIRECTS) {
                val location = conn.getHeaderField("Location") ?: break
                val next = URL(conn.url, location)
                conn.disconnect()
                conn = open(next)
                conn.requestMethod = "GET"
                status = conn.responseCode
                hops++
            }
            try {
                if (status !in 200..299) throw IOException("the server answered HTTP $status")
                return conn.inputStream.bufferedReader(Charsets.UTF_8).use { it.readText() }
            } finally {
                conn.disconnect()
            }
        }

        private fun open(u: URL): HttpURLConnection = (u.openConnection() as HttpURLConnection).apply {
            instanceFollowRedirects = false
            connectTimeout = connectMs
            readTimeout = readMs
            useCaches = false
        }

        private companion object {
            const val MAX_REDIRECTS = 5
        }
    }
}
