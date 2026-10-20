package com.jahangirb.pmpprep.data

import org.json.JSONObject
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

/** A published build: its CI run number, where its APK is, and how big it is. */
data class Release(val build: Int, val apkUrl: String, val bytes: Long, val publishedAt: String)

/**
 * "Check for updates": the newest GitHub release of this repository (.github/workflows/android-apk.yml
 * publishes each build on the default branch as `apk-<run number>`, with PMP-Prep.apk attached) against
 * this app's own version code, which build.gradle.kts sets to 100 + that run number.
 */
object AppUpdate {
    const val LATEST = "https://api.github.com/repos/JahangirB/PMP/releases/latest"
    const val RELEASES_PAGE = "https://github.com/JahangirB/PMP/releases"
    const val CODE_BASE = 100
    const val CHECK_EVERY_MS = 24L * 60 * 60 * 1000
    private val TAG = Regex("^apk-(\\d+)$")

    fun parse(json: String): Release? {
        val o = runCatching { JSONObject(json) }.getOrNull() ?: return null
        val build = TAG.find(o.optString("tag_name"))?.groupValues?.get(1)?.toIntOrNull() ?: return null
        val assets = o.optJSONArray("assets") ?: return null
        val apk = (0 until assets.length()).mapNotNull { assets.optJSONObject(it) }
            .firstOrNull { it.optString("name").endsWith(".apk") } ?: return null
        val url = apk.optString("browser_download_url")
        if (!url.startsWith("https://")) return null
        return Release(build, url, apk.optLong("size"), o.optString("published_at"))
    }

    fun isNewer(r: Release, versionCode: Long): Boolean = CODE_BASE + r.build > versionCode

    /** The build number a version code stands for. */
    fun buildOf(versionCode: Long): Long = versionCode - CODE_BASE

    /** Blocking; call off the main thread. Null when there is no release with an APK. */
    fun fetchLatest(): Release? {
        val c = (URL(LATEST).openConnection() as HttpURLConnection).apply {
            connectTimeout = 15_000; readTimeout = 20_000
            setRequestProperty("Accept", "application/vnd.github+json")
            setRequestProperty("User-Agent", "PMP-Prep-Android")
        }
        try {
            if (c.responseCode == 404) return null
            if (c.responseCode !in 200..299) throw IllegalStateException("GitHub answered ${c.responseCode}. Try again later.")
            return parse(c.inputStream.bufferedReader().use { it.readText() })
        } finally { c.disconnect() }
    }

    /** Blocking download to [to], reporting (done, total) bytes; stops when [cancelled] says so. */
    fun download(r: Release, to: File, cancelled: () -> Boolean, progress: (Long, Long) -> Unit) {
        val c = (URL(r.apkUrl).openConnection() as HttpURLConnection).apply {
            connectTimeout = 15_000; readTimeout = 30_000; instanceFollowRedirects = true
            setRequestProperty("User-Agent", "PMP-Prep-Android")
        }
        try {
            if (c.responseCode !in 200..299) throw IllegalStateException("The download failed (${c.responseCode}). Try again.")
            val total = c.contentLengthLong.takeIf { it > 0 } ?: r.bytes
            to.parentFile?.mkdirs()
            val part = File(to.path + ".part")
            c.inputStream.use { input ->
                part.outputStream().use { out ->
                    val buf = ByteArray(64 * 1024)
                    var done = 0L
                    while (true) {
                        if (cancelled()) throw InterruptedException("Cancelled.")
                        val n = input.read(buf)
                        if (n < 0) break
                        out.write(buf, 0, n)
                        done += n
                        progress(done, total)
                    }
                }
            }
            if (!part.renameTo(to)) throw IllegalStateException("The download couldn't be saved.")
        } finally { c.disconnect() }
    }
}
