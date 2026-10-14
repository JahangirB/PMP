package com.jahangirb.pmpprep.listen

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.os.Build
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.k2fsa.sherpa.onnx.OfflineTts
import com.k2fsa.sherpa.onnx.OfflineTtsConfig
import com.k2fsa.sherpa.onnx.OfflineTtsKokoroModelConfig
import com.k2fsa.sherpa.onnx.OfflineTtsModelConfig
import java.io.File
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit

/**
 * Listen mode: lessons, flashcards and explanations read aloud by an offline voice (Kokoro,
 * built into the app). One sentence is generated while the one before plays, so speech starts
 * after the first sentence. Nothing leaves the phone.
 */
object Listen {
    enum class Status { IDLE, PREPARING, PLAYING, PAUSED }

    var status by mutableStateOf(Status.IDLE)
        private set
    var title by mutableStateOf("")
        private set
    /** Which part is being read, of how many. */
    var part by mutableIntStateOf(0)
        private set
    var parts by mutableIntStateOf(0)
        private set
    var error by mutableStateOf<String?>(null)
    /** 0.85, 1.0 or 1.15. */
    var speed by mutableStateOf(1f)

    private const val VOICE = 1 // af_bella
    private const val DIR = "tts"
    private val lock = Any()
    private var tts: OfflineTts? = null
    private var track: AudioTrack? = null
    private var run: Run? = null
    private lateinit var app: Context

    /** One reading: generation and playback threads that stop together. */
    private class Run(val texts: List<String>) {
        @Volatile var cancelled = false
        val queue = LinkedBlockingQueue<FloatArray>(8)
    }

    /** Reads [texts] in order, a short pause between them. Replaces anything already playing. */
    fun play(ctx: Context, what: String, texts: List<String>) {
        val clean = texts.map { tidy(it) }.filter { it.isNotBlank() }
        if (clean.isEmpty()) return
        app = ctx.applicationContext
        stop()
        val r = Run(clean)
        run = r
        title = what; part = 0; parts = clean.size; error = null
        status = Status.PREPARING
        ListenService.start(app)
        Thread({ generate(r) }, "listen-generate").start()
        Thread({ playback(r) }, "listen-play").start()
    }

    fun pause() {
        if (status != Status.PLAYING) return
        track?.pause()
        status = Status.PAUSED
        ListenService.update(app)
    }

    fun resume() {
        if (status != Status.PAUSED) return
        if (!focus(true)) return
        track?.play()
        status = Status.PLAYING
        ListenService.update(app)
    }

    fun stop() {
        val r = run ?: return
        r.cancelled = true
        r.queue.clear()
        run = null
        track?.let { runCatching { it.pause(); it.flush() } }
        status = Status.IDLE
        focus(false)
        if (::app.isInitialized) ListenService.stop(app)
    }

    private fun generate(r: Run) {
        try {
            val engine = engine()
            if (r.cancelled) return
            val pause = FloatArray(engine.sampleRate() * 6 / 10)
            r.texts.forEachIndexed { i, text ->
                if (r.cancelled) return
                engine.generateWithCallback(text, VOICE, speed, Sink(r))
                r.queue.put(MARK)
                if (i < r.texts.size - 1) r.queue.put(pause)
            }
            r.queue.put(END)
        } catch (e: Throwable) {
            if (!r.cancelled) { error = "The voice couldn't start: ${e.message ?: e}"; r.queue.offer(END) }
        }
    }

    /**
     * Receives each generated stretch of speech. The speech library finds this by JNI as a method
     * `invoke(float[]): Integer`, which a Kotlin lambda doesn't have (lambdas compile to
     * invokedynamic), so it must be a real class, kept by name in proguard-rules.pro.
     */
    private class Sink(private val r: Run) : (FloatArray) -> Int {
        override fun invoke(samples: FloatArray): Int {
            if (r.cancelled) return 0
            r.queue.put(samples.copyOf())
            return 1
        }
    }

    private fun playback(r: Run) {
        try { play(r) } catch (e: Throwable) {
            if (!r.cancelled) error = "Playback stopped: ${e.message ?: e}"
            r.cancelled = true
            if (run === r) stop()
        }
    }

    private fun play(r: Run) {
        var started = false
        while (!r.cancelled) {
            val chunk = r.queue.poll(200, TimeUnit.MILLISECONDS) ?: continue
            when {
                chunk === END -> break
                chunk === MARK -> { part = minOf(part + 1, parts - 1) }
                else -> {
                    val t = track(engine().sampleRate())
                    if (!started) {
                        started = true
                        if (!focus(true)) { r.cancelled = true; break }
                        t.play()
                        status = Status.PLAYING
                        ListenService.update(app)
                    }
                    var off = 0
                    while (off < chunk.size && !r.cancelled) {
                        val n = t.write(chunk, off, chunk.size - off, AudioTrack.WRITE_BLOCKING)
                        if (n <= 0) break
                        off += n
                    }
                }
            }
        }
        if (!r.cancelled) {
            // Let the last of it play out, then finish.
            Thread.sleep(400)
            if (run === r) stop()
        }
    }

    /** Loads the voice once; a few seconds the first time. */
    private fun engine(): OfflineTts = synchronized(lock) {
        tts ?: run {
            val data = copyData()
            val cfg = OfflineTtsConfig(
                model = OfflineTtsModelConfig(
                    kokoro = OfflineTtsKokoroModelConfig(
                        model = "$DIR/model.int8.onnx", voices = "$DIR/voices.bin", tokens = "$DIR/tokens.txt", dataDir = data,
                    ),
                    numThreads = Runtime.getRuntime().availableProcessors().coerceIn(2, 4),
                ),
                maxNumSentences = 1,
            )
            OfflineTts(app.assets, cfg).also { tts = it }
        }
    }

    /** The pronunciation data must be real files: copied out of the APK once per app version. */
    private fun copyData(): String {
        val root = File(app.filesDir, "tts")
        val version = app.packageManager.getPackageInfo(app.packageName, 0).lastUpdateTime.toString()
        val stamp = File(root, ".version")
        val data = File(root, "espeak-ng-data")
        if (stamp.exists() && stamp.readText() == version && data.isDirectory) return data.absolutePath
        root.deleteRecursively()
        copyAsset("$DIR/espeak-ng-data", data)
        stamp.writeText(version)
        return data.absolutePath
    }

    private fun copyAsset(path: String, to: File) {
        val children = app.assets.list(path).orEmpty()
        if (children.isEmpty()) {
            to.parentFile?.mkdirs()
            app.assets.open(path).use { input -> to.outputStream().use { input.copyTo(it) } }
        } else {
            to.mkdirs()
            children.forEach { copyAsset("$path/$it", File(to, it)) }
        }
    }

    private fun track(rate: Int): AudioTrack = track ?: run {
        val min = AudioTrack.getMinBufferSize(rate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_FLOAT)
        AudioTrack(
            AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA).setContentType(AudioAttributes.CONTENT_TYPE_SPEECH).build(),
            AudioFormat.Builder().setEncoding(AudioFormat.ENCODING_PCM_FLOAT).setChannelMask(AudioFormat.CHANNEL_OUT_MONO).setSampleRate(rate).build(),
            maxOf(min, rate / 2 * 4), AudioTrack.MODE_STREAM, AudioManager.AUDIO_SESSION_ID_GENERATE,
        ).also { track = it }
    }

    /* ── audio focus: pause for calls and other apps, resume when they're done ── */

    private var focusRequest: AudioFocusRequest? = null
    private var pausedByFocus = false
    private val focusListener = AudioManager.OnAudioFocusChangeListener { change ->
        when (change) {
            AudioManager.AUDIOFOCUS_LOSS -> { pausedByFocus = false; pause() }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT, AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK ->
                if (status == Status.PLAYING) { pausedByFocus = true; pause() }
            AudioManager.AUDIOFOCUS_GAIN -> if (pausedByFocus) { pausedByFocus = false; resume() }
        }
    }

    private fun focus(take: Boolean): Boolean {
        if (!::app.isInitialized) return true
        val am = app.getSystemService(AudioManager::class.java) ?: return true
        if (Build.VERSION.SDK_INT >= 26) {
            if (take) {
                val req = focusRequest ?: AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                    .setAudioAttributes(AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA).setContentType(AudioAttributes.CONTENT_TYPE_SPEECH).build())
                    .setOnAudioFocusChangeListener(focusListener).build().also { focusRequest = it }
                return am.requestAudioFocus(req) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
            }
            focusRequest?.let { am.abandonAudioFocusRequest(it) }
            return true
        }
        @Suppress("DEPRECATION")
        return if (take) am.requestAudioFocus(focusListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
        else { am.abandonAudioFocus(focusListener); true }
    }

    /** Spoken text: no bullets or markup, abbreviations the voice would spell oddly said in words. */
    fun tidy(s: String): String = s
        .replace(Regex("[*_#`>|•▲▼✓✗⚠]"), " ")
        .replace(Regex("\\s*→\\s*"), " gives ")
        .replace(Regex("\\be\\.g\\.", RegexOption.IGNORE_CASE), "for example")
        .replace(Regex("\\bi\\.e\\.", RegexOption.IGNORE_CASE), "that is")
        .replace(Regex("\\s+"), " ")
        .trim()

    private val END = FloatArray(0)
    private val MARK = FloatArray(0)
}
