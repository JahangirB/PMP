package com.jahangirb.pmpprep.data

import org.json.JSONArray
import org.json.JSONObject
import kotlin.math.abs

/**
 * The three-way merge of progress documents that every app runs the same way
 * (docs/progress-format.md §3): [base] is the document as it was after this
 * device's last sync, [local] this phone's progress now, [remote] the copy in
 * the cloud. Pure: it reads its inputs and never changes them, so it is tested
 * on the JVM against tests/fixtures/sync, the same fixture the web and iOS use.
 *
 * Equality is canonical JSON (object keys sorted, whole numbers written without
 * a fraction), so two apps that wrote the same value differently still agree.
 */
object SyncMerge {

    /** A log: records are only ever added. [id] is a record's identity, [at] its time for sorting; [cap] keeps the newest. */
    private class Log(val key: String, val cap: Int, val at: (JSONObject) -> Long, val id: (JSONObject) -> String)

    private fun ids(vararg keys: String): (JSONObject) -> String = { o -> keys.joinToString("|") { canonical(o.opt(it)) } }
    private val byAt: (JSONObject) -> Long = { it.optLong("at") }

    private val LOGS = listOf(
        Log("attempts", 20_000, byAt, ids("q", "at")),
        Log("scenarios", 0, byAt, ids("id", "at")),
        Log("mocks", 0, byAt, ids("at", "title")),
        Log("sessions", 0, byAt, ids("k", "at")),
        Log("diagnostics", 0, byAt, ids("at")),
        Log("chat", 60, byAt, ids("at", "r")),
        Log("sittings", 2_000, byAt, ids("k", "at")),
        Log("checkins", 0, byAt, ids("kind", "at")),
        // A weekly report's identity and its place in time are both its week start.
        Log("weekly", 104, { it.optLong("w") }, ids("w")),
    )

    /** Sets of ids: an element stays only if neither side removed it. */
    private val SETS = listOf("flags", "lessons")

    /** Maps merged key by key. [MY_CARDS] is a list keyed by id and is handled as a map too. */
    private val MAPS = listOf("notes", "mistakes", "cards", "snoozed")
    private const val MY_CARDS = "mycards"

    /** App-only blocks: merged key by key, never read. Written only when one of the documents has them. */
    private val BLOCKS = listOf("web", "native")

    /** Every key the merge handles by name; anything else at the top level is merged as a whole value. */
    private val NAMED: Set<String> = setOf("type", "version", "savedAt", "device", "plan", "notepad", MY_CARDS) +
        LOGS.map { it.key } + SETS + MAPS + BLOCKS

    /** An empty document: the base before a device's first sync. */
    fun emptyDoc(): JSONObject = JSONObject().put("type", ProgressStore.TYPE).put("version", ProgressStore.VERSION)

    /**
     * Merges [local] and [remote] against [base] (null: never synced). The result is stamped with
     * [now] and [device]; it becomes this phone's progress and its new base.
     */
    fun merge(base: JSONObject?, local: JSONObject, remote: JSONObject, now: Long, device: JSONObject): JSONObject {
        val b = base ?: emptyDoc()
        val localNewer = local.optLong("savedAt") >= remote.optLong("savedAt")
        // When both sides changed the same thing and nothing better decides, the later copy wins (local on a tie).
        val later: (Any?, Any?) -> Any? = { l, r -> if (localNewer) l else r }
        val out = JSONObject()
        out.put("type", ProgressStore.TYPE)
        out.put("version", ProgressStore.VERSION)
        out.put("savedAt", now)
        out.put("device", device)

        for (log in LOGS) out.put(log.key, mergeLog(log, local.optJSONArray(log.key), remote.optJSONArray(log.key)))
        for (key in SETS) out.put(key, mergeSet(b.optJSONArray(key), local.optJSONArray(key), remote.optJSONArray(key)))
        for (key in MAPS) {
            val both: (Any?, Any?) -> Any? = when (key) {
                // The card studied last has the true schedule; the mistake tagged last is the current tag.
                "cards" -> { l, r -> higher(l, r, later) { (it as? JSONArray)?.optLong(5) } }
                "mistakes" -> { l, r -> higher(l, r, later) { (it as? JSONObject)?.optLong("at") } }
                else -> later
            }
            out.put(key, mergeMap(b.optJSONObject(key), local.optJSONObject(key), remote.optJSONObject(key), both))
        }
        val cards = mergeMap(byId(b.optJSONArray(MY_CARDS)), byId(local.optJSONArray(MY_CARDS)), byId(remote.optJSONArray(MY_CARDS)), later)
        out.put(MY_CARDS, JSONArray().apply { cards.keys().asSequence().sorted().forEach { put(cards.get(it)) } })
        for (key in BLOCKS) {
            if (listOf(b, local, remote).any { it.has(key) }) {
                out.put(key, mergeMap(b.optJSONObject(key), local.optJSONObject(key), remote.optJSONObject(key), later))
            }
        }

        // Whole values: the plan, the notes page, and keys from apps newer than this one.
        val notepad = pick(b.opt("notepad") ?: "", local.opt("notepad") ?: "", remote.opt("notepad") ?: "", later)
        out.put("notepad", notepad ?: "")
        val values = (listOf(b, local, remote).flatMap { it.keys().asSequence().toList() }.toSet() - NAMED) + "plan"
        for (key in values.sorted()) {
            pick(b.opt(key), local.opt(key), remote.opt(key), later)?.let { out.put(key, it) }
        }
        return out
    }

    /** True when the two documents hold the same progress: everything but savedAt and device, compared canonically. */
    fun same(a: JSONObject, b: JSONObject): Boolean = canonical(content(a)) == canonical(content(b))

    /**
     * The document as the fixture compares it (tests/fixtures/sync/README.md): no savedAt or device,
     * a missing shared slice as an empty one, logs sorted by time then identity, sets and my cards sorted.
     */
    fun content(doc: JSONObject): JSONObject {
        val out = JSONObject()
        doc.keys().forEach { k -> if (k != "savedAt" && k != "device") out.put(k, doc.get(k)) }
        for (log in LOGS) out.put(log.key, sortedLog(log, records(doc.optJSONArray(log.key))))
        for (key in SETS) out.put(key, JSONArray(strings(doc.optJSONArray(key)).sorted()))
        for (key in MAPS) if (!doc.has(key)) out.put(key, JSONObject())
        val cards = byId(doc.optJSONArray(MY_CARDS))
        out.put(MY_CARDS, JSONArray().apply { cards.keys().asSequence().sorted().forEach { put(cards.get(it)) } })
        if (!doc.has("notepad")) out.put("notepad", "")
        return out
    }

    /* ── the four kinds ──────────────────────────────────────────────────── */

    /** Union by identity, keeping the local record when both have it, sorted by time and capped to the newest. */
    private fun mergeLog(log: Log, local: JSONArray?, remote: JSONArray?): JSONArray {
        val seen = HashSet<String>()
        val all = ArrayList<JSONObject>()
        for (o in records(local) + records(remote)) if (seen.add(log.id(o))) all += o
        val sorted = sortedLog(log, all)
        if (log.cap > 0 && sorted.length() > log.cap) {
            val keep = JSONArray()
            for (i in sorted.length() - log.cap until sorted.length()) keep.put(sorted.get(i))
            return keep
        }
        return sorted
    }

    private fun sortedLog(log: Log, list: List<JSONObject>): JSONArray =
        JSONArray(list.sortedWith(compareBy<JSONObject> { log.at(it) }.thenBy { log.id(it) }))

    /** Kept on both sides stays; removed on either side (relative to base) goes; added on either side comes. */
    private fun mergeSet(base: JSONArray?, local: JSONArray?, remote: JSONArray?): JSONArray {
        val b = strings(base).toSet()
        val l = strings(local).toSet()
        val r = strings(remote).toSet()
        val keep = (b + l + r).filter { e -> if (e in b) e in l && e in r else e in l || e in r }
        return JSONArray(keep.sorted())
    }

    /** Key by key, three-way: [both] decides a key changed on both sides. A null result deletes the key. */
    private fun mergeMap(base: JSONObject?, local: JSONObject?, remote: JSONObject?, both: (Any?, Any?) -> Any?): JSONObject {
        val out = JSONObject()
        val keys = sortedSetOf<String>()
        listOf(base, local, remote).forEach { m -> m?.keys()?.forEach { keys += it } }
        for (k in keys) pick(base?.opt(k), local?.opt(k), remote?.opt(k), both)?.let { out.put(k, it) }
        return out
    }

    /**
     * One value, three-way. Null means absent. If only one side changed it from [base] (deleting
     * counts), that side's value; if both changed it differently, [both] decides.
     */
    private fun pick(base: Any?, local: Any?, remote: Any?, both: (Any?, Any?) -> Any?): Any? {
        val l = key(local)
        val r = key(remote)
        if (l == r) return local
        val b = key(base)
        if (l == b) return remote
        if (r == b) return local
        return both(local, remote)
    }

    /** The side whose [stamp] is higher (a deleted side has none); on a tie, [tie]. */
    private fun higher(local: Any?, remote: Any?, tie: (Any?, Any?) -> Any?, stamp: (Any?) -> Long?): Any? {
        val l = local?.let(stamp) ?: Long.MIN_VALUE
        val r = remote?.let(stamp) ?: Long.MIN_VALUE
        return when {
            l > r -> local
            r > l -> remote
            else -> tie(local, remote)
        }
    }

    /* ── helpers ─────────────────────────────────────────────────────────── */

    private fun records(a: JSONArray?): List<JSONObject> =
        if (a == null) emptyList() else (0 until a.length()).mapNotNull { a.optJSONObject(it) }

    private fun strings(a: JSONArray?): List<String> =
        if (a == null) emptyList() else (0 until a.length()).mapNotNull { i -> a.opt(i)?.takeIf { it != JSONObject.NULL }?.toString() }

    /** A list of records with an "id" as a map from id to record (the last one wins on a repeated id). */
    private fun byId(a: JSONArray?): JSONObject = JSONObject().apply {
        records(a).forEach { o -> o.optString("id").takeIf { it.isNotEmpty() }?.let { put(it, o) } }
    }

    private const val ABSENT = "\u0000absent"

    private fun key(v: Any?): String = if (v == null) ABSENT else canonical(v)

    /** Canonical JSON: object keys sorted, whole numbers without a fraction. */
    fun canonical(v: Any?): String = StringBuilder().also { write(it, v) }.toString()

    private fun write(sb: StringBuilder, v: Any?) {
        when {
            v == null || v === JSONObject.NULL -> sb.append("null")
            v is JSONObject -> {
                sb.append('{')
                v.keys().asSequence().sorted().forEachIndexed { i, k ->
                    if (i > 0) sb.append(',')
                    sb.append(JSONObject.quote(k)).append(':')
                    write(sb, v.opt(k))
                }
                sb.append('}')
            }
            v is JSONArray -> {
                sb.append('[')
                for (i in 0 until v.length()) {
                    if (i > 0) sb.append(',')
                    write(sb, v.opt(i))
                }
                sb.append(']')
            }
            v is String -> sb.append(JSONObject.quote(v))
            v is Boolean -> sb.append(v.toString())
            v is Number -> sb.append(number(v))
            else -> sb.append(JSONObject.quote(v.toString()))
        }
    }

    private fun number(n: Number): String = when (n) {
        is Int, is Long, is Short, is Byte, is java.math.BigInteger -> n.toString()
        else -> {
            val d = n.toDouble()
            if (!d.isNaN() && !d.isInfinite() && d == Math.rint(d) && abs(d) < 9.0e15) d.toLong().toString() else d.toString()
        }
    }
}
