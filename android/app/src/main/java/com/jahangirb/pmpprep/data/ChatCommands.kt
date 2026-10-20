package com.jahangirb.pmpprep.data

import org.json.JSONArray
import org.json.JSONObject

/** One "/" command in the Gemini chat: typed as /[name], it sends [text]. [label] is what the menu shows. */
data class ChatCommand(val name: String, val text: String, val label: String = text, val own: Boolean = true)

/**
 * The chat's commands (index.html §12.2.2 COMMANDS): four built in, then the person's own,
 * kept in the shared `commands` slice as { n, t } so they reach every app after a sync.
 */
object ChatCommands {
    const val REVIEW = "How am I doing overall? Look at all my figures, tell me honestly where I stand, what is going well, and the three things I should change this week."
    val BUILT_IN = listOf(
        ChatCommand("progress", REVIEW, "How am I doing?", own = false),
        ChatCommand("improve", "What are the three weakest areas in my figures, and exactly what should I do about each this week in this app?", "What should I improve?", own = false),
        ChatCommand("plan", "I have about an hour today. Using my figures, what should I do, in order, and why?", "Plan my day", own = false),
        ChatCommand("weakest", "Teach me the ECO task where my accuracy is lowest: what PMI expects, the common traps, and one example question with the reasoning.", "Explain my weakest task", own = false),
    )
    private val NAME = Regex("^[a-z0-9-]{1,24}$")
    private val TYPED = Regex("^/([A-Za-z0-9-]+)(?:\\s+([\\s\\S]*))?$")
    private val PARTIAL = Regex("^/([A-Za-z0-9-]*)$")

    fun parse(a: JSONArray?): List<ChatCommand> {
        if (a == null) return emptyList()
        return (0 until a.length()).mapNotNull { i ->
            val o = a.optJSONObject(i) ?: return@mapNotNull null
            val n = o.optString("n"); val t = o.optString("t")
            if (NAME.matches(n) && t.isNotBlank()) ChatCommand(n, t) else null
        }
    }

    fun toJson(list: List<ChatCommand>): JSONArray = JSONArray().apply { list.forEach { put(JSONObject().put("n", it.name).put("t", it.text)) } }

    fun all(own: List<ChatCommand>): List<ChatCommand> = BUILT_IN + own

    /** The commands the box's text asks for while it is "/" plus a name being typed; null when the menu is closed. */
    fun matching(input: String, own: List<ChatCommand>): List<ChatCommand>? {
        val m = PARTIAL.matchEntire(input) ?: return null
        val q = m.groupValues[1].lowercase()
        return all(own).filter { it.name.startsWith(q) }
    }

    /** "/name", alone or followed by more words, becomes that command's message. */
    fun expand(input: String, own: List<ChatCommand>): String {
        val m = TYPED.matchEntire(input) ?: return input
        val c = all(own).firstOrNull { it.name == m.groupValues[1].lowercase() } ?: return input
        val rest = m.groupValues[2].trim()
        return if (rest.isEmpty()) c.text else c.text + "\n\n" + rest
    }

    /** The cleaned name, or the reason it can't be saved (replacing [old], or new when null). */
    fun check(name: String, text: String, own: List<ChatCommand>, old: String?): Pair<String?, String?> {
        val n = name.trim().removePrefix("/").lowercase()
        return when {
            !NAME.matches(n) -> null to "Use up to 24 letters, digits or hyphens for the name, with no spaces."
            BUILT_IN.any { it.name == n } || own.any { it.name == n && it.name != old } -> null to "/$n is already a command. Pick another name."
            text.isBlank() -> null to "Write the message the command sends."
            else -> n to null
        }
    }

    /** [own] with [old] replaced by (or, when null, followed by) the command [name] → [text]. */
    fun saved(own: List<ChatCommand>, old: String?, name: String, text: String): List<ChatCommand> {
        val row = ChatCommand(name, text.trim())
        return if (old == null) own + row else own.map { if (it.name == old) row else it }
    }
}
