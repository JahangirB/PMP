package com.jahangirb.pmpprep.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jahangirb.pmpprep.ui.Dest
import com.jahangirb.pmpprep.ui.PmpViewModel
import com.jahangirb.pmpprep.ui.components.Pill
import com.jahangirb.pmpprep.ui.components.PmpCard

private class Hit(val kind: String, val title: String, val snippet: String, val detail: String?, val go: (() -> Unit)?)

/** One search across lessons, glossary, abbreviations, formulas, your notes and the questions' explanations. */
@Composable
fun SearchScreen(vm: PmpViewModel) {
    val rev by vm.revision.collectAsState()
    var query by rememberSaveable { mutableStateOf("") }
    var open by remember { mutableStateOf<String?>(null) }
    val focus = remember { FocusRequester() }
    LaunchedEffect(Unit) { runCatching { focus.requestFocus() } }
    val hits = remember(query, rev, vm.contentVersion) { if (query.trim().length < 2) emptyList() else search(vm, query) }

    LazyColumn(Modifier.fillMaxSize().padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item {
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(query, { query = it }, singleLine = true, leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                placeholder = { Text("Search lessons, terms, formulas, notes…") }, modifier = Modifier.fillMaxWidth().focusRequester(focus))
            if (query.trim().length >= 2) Text(if (hits.isEmpty()) "Nothing found." else if (hits.size >= MAX) "First $MAX results." else "${hits.size} found.",
                fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 6.dp))
        }
        items(hits) { h ->
            val key = h.kind + h.title + h.snippet
            PmpCard(onClick = { if (h.go != null) h.go.invoke() else open = if (open == key) null else key }) {
                Pill(h.kind, MaterialTheme.colorScheme.primary)
                Spacer(Modifier.height(4.dp))
                Text(h.title, fontWeight = FontWeight.Bold, maxLines = 2, overflow = TextOverflow.Ellipsis)
                if (open == key && h.detail != null) Text(h.detail, fontSize = 14.sp, lineHeight = 20.sp)
                else Text(h.snippet, fontSize = 13.sp, lineHeight = 18.sp, maxLines = 3, overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        item { Spacer(Modifier.height(24.dp)) }
    }
}

private const val MAX = 60

private fun search(vm: PmpViewModel, query: String): List<Hit> {
    val words = query.lowercase().split(Regex("\\s+")).filter { it.isNotBlank() }
    fun matches(vararg texts: String): Boolean { val t = texts.joinToString(" ").lowercase(); return words.all { it in t } }
    fun snip(text: String): String {
        val i = text.lowercase().indexOf(words.first()).coerceAtLeast(0)
        val from = (i - 50).coerceAtLeast(0)
        return (if (from > 0) "…" else "") + text.substring(from, minOf(text.length, from + 160)).replace('\n', ' ').trim() + if (from + 160 < text.length) "…" else ""
    }
    val x = vm.repo.extras
    val out = ArrayList<Hit>()
    x.lessons.forEach { l ->
        val body = l.sections.joinToString(" ") { it.heading + " " + it.body } + " " + l.recap.joinToString(" ") + " " + l.traps.joinToString(" ")
        if (matches(l.title, body)) out += Hit("Lesson", l.title, snip(if (matches(l.title)) l.objectives.joinToString(" ") else body), null) {
            vm.openLesson = l.id; vm.navigate(Dest.LEARN)
        }
    }
    x.glossary.forEach { g -> if (matches(g.term, g.definition)) out += Hit("Glossary", g.term, snip(g.definition), g.definition, null) }
    x.abbreviations.forEach { a -> if (matches(a.abbr, a.full, a.note)) out += Hit("Abbreviation", "${a.abbr} · ${a.full}", snip(a.note.ifBlank { a.full }), a.note.ifBlank { null }, null) }
    x.formulas.forEach { g ->
        g.items.forEach { f ->
            if (matches(f.name, f.expr, f.reads, f.where, f.trap)) out += Hit("Formula", "${f.name}: ${f.expr}", snip(f.reads.ifBlank { f.where }),
                listOf(f.expr, f.where, f.reads, f.trap.takeIf { it.isNotBlank() }?.let { "Trap: $it" }).filter { !it.isNullOrBlank() }.joinToString("\n\n"), null)
        }
    }
    if (matches(vm.store.notepad)) out += Hit("Your notes", "Notes page", snip(vm.store.notepad), null) { vm.coachTab = 2; vm.navigate(Dest.COACH) }
    vm.store.notes.forEach { (id, note) ->
        val q = vm.repo.byId[id] ?: return@forEach
        if (matches(note, q.stem)) out += Hit("Your note on a question", note.lines().first(), snip(q.stem), null) { vm.startPracticeWith(listOf(q), "From search") }
    }
    for (q in vm.pool()) {
        if (out.size >= MAX) break
        if (matches(q.stem, q.explanation, q.mindset)) out += Hit("Question", q.stem.take(120), snip(q.explanation.ifBlank { q.stem }), null) {
            vm.startPracticeWith(listOf(q), "From search")
        }
    }
    return out.take(MAX)
}
