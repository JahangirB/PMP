package com.jahangirb.pmpprep.data

import android.content.Context
import java.io.File

/**
 * All questions and study content. Built-in content comes from assets (made
 * at build time from index.html and packs/); imported packs live as files in
 * filesDir/packs and load after the built-in ones. An id that is already
 * loaded is skipped, never overwritten, so answer history always points at
 * the question it was recorded against.
 */
class ContentRepository(private val context: Context) {

    lateinit var extras: Extras
        private set
    val packs = ArrayList<QuestionPack>()
    val byId = LinkedHashMap<String, Question>()
    private val prefs = context.getSharedPreferences("packs", Context.MODE_PRIVATE)
    private val disabled = HashSet<String>()

    private val userDir: File get() = File(context.filesDir, "packs").apply { mkdirs() }

    @Synchronized
    fun load() {
        packs.clear(); byId.clear()
        extras = ContentParser.extras(asset("content/extras.json"))
        add(ContentParser.parsePack(asset("content/core.json"), "core", "Core pack (built in)", null, true))
        val files = (context.assets.list("packs") ?: emptyArray())
            .filter { it.endsWith(".json") && it != "index.json" }
            .sortedWith(NATURAL)
        for (f in files) add(ContentParser.parsePack(asset("packs/$f"), "asset:$f", null, f, true))
        userDir.listFiles()?.filter { it.name.endsWith(".json") }?.sortedBy { it.name }?.forEach { f ->
            // A damaged imported file must not stop the app from opening.
            runCatching { add(ContentParser.parsePack(f.readText(), "user:${f.name}", null, f.name, false)) }
        }
        disabled.clear()
        disabled += prefs.getStringSet("disabled", emptySet()).orEmpty()
    }

    private fun add(p: QuestionPack): QuestionPack {
        val fresh = p.questions.filter { it.id !in byId }
        val pack = p.copy(questions = fresh, skippedDuplicates = p.questions.size - fresh.size)
        fresh.forEach { byId[it.id] = it }
        packs += pack
        return pack
    }

    fun isEnabled(p: QuestionPack) = p.id !in disabled

    @Synchronized
    fun setEnabled(p: QuestionPack, on: Boolean) {
        if (on) disabled -= p.id else disabled += p.id
        prefs.edit().putStringSet("disabled", HashSet(disabled)).apply()
    }

    /** Questions from every enabled pack. */
    fun pool(): List<Question> = packs.filter { isEnabled(it) }.flatMap { it.questions }

    /** Validates, stores and loads an imported pack, returning it as loaded. */
    @Synchronized
    fun importPack(text: String, displayName: String): QuestionPack {
        var base = displayName.removeSuffix(".json").replace(Regex("[^A-Za-z0-9._-]+"), "-").trim('-')
        if (base.isEmpty()) base = "pack"
        val file = "$base-${System.currentTimeMillis()}.json"
        val parsed = ContentParser.parsePack(text, "user:$file", base, file, false)
        if (parsed.questions.isEmpty()) {
            throw ContentParser.PackException(
                "No usable questions in this file." + if (parsed.errors.isEmpty()) "" else "\n\n" + parsed.errors.joinToString("\n")
            )
        }
        val fresh = parsed.questions.count { it.id !in byId }
        if (fresh == 0) throw ContentParser.PackException("All ${parsed.questions.size} questions in this file are already in the app.")
        File(userDir, file).writeText(text)
        return add(parsed)
    }

    @Synchronized
    fun deletePack(p: QuestionPack) {
        if (p.builtin || p.file == null) return
        File(userDir, p.file).delete()
        packs.remove(p)
        p.questions.forEach { byId.remove(it.id) }
    }

    fun task(id: String): EcoTask? = extras.tasks.firstOrNull { it.id == id }

    private fun asset(path: String): String = context.assets.open(path).bufferedReader().use { it.readText() }

    companion object {
        /** "people-2" before "people-10". */
        val NATURAL = Comparator<String> { a, b ->
            val ra = Regex("(\\d+)|(\\D+)").findAll(a).map { it.value }.toList()
            val rb = Regex("(\\d+)|(\\D+)").findAll(b).map { it.value }.toList()
            for (i in 0 until minOf(ra.size, rb.size)) {
                val x = ra[i]; val y = rb[i]
                val c = if (x[0].isDigit() && y[0].isDigit()) x.toBigInteger().compareTo(y.toBigInteger()) else x.compareTo(y)
                if (c != 0) return@Comparator c
            }
            ra.size - rb.size
        }
    }
}
