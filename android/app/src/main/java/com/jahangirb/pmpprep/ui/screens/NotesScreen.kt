package com.jahangirb.pmpprep.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jahangirb.pmpprep.ui.PmpViewModel
import com.jahangirb.pmpprep.ui.components.PmpCard
import com.jahangirb.pmpprep.ui.components.SectionLabel
import kotlinx.coroutines.delay

/*
 * One running page of notes, like the web app's notes pane (§12.2.1), without
 * its AI tab: the AI lives in the coach. Formatting is light markup the ribbon
 * writes around the selection (**bold**, *italic*, __underline__, ~~strike~~,
 * ==highlight==, bullet and numbered lines). The editor styles it as you type,
 * keeping the markers visible but faint, so the text never shifts under the cursor.
 */

private val MARKS = listOf(
    "**" to SpanStyle(fontWeight = FontWeight.Bold),
    "~~" to SpanStyle(textDecoration = TextDecoration.LineThrough),
    "__" to SpanStyle(textDecoration = TextDecoration.Underline),
    "==" to SpanStyle(background = Color(0x66FFD54F)),
    "*" to SpanStyle(fontStyle = FontStyle.Italic),
)

private fun styled(text: String, faint: Color, hideMarks: Boolean): AnnotatedString = buildAnnotatedString {
    append(text)
    val taken = BooleanArray(text.length)
    for ((m, style) in MARKS) {
        val q = Regex.escape(m)
        val re = Regex("$q(?=\\S)(.+?)(?<=\\S)$q")
        for (hit in re.findAll(text)) {
            val r = hit.range
            if ((r.first..r.last).any { taken[it] }) continue
            if (m == "*" && (text.getOrNull(r.first - 1) == '*' || text.getOrNull(r.last + 1) == '*')) continue
            addStyle(style, r.first + m.length, r.last + 1 - m.length)
            val markStyle = if (hideMarks) SpanStyle(color = Color.Transparent, fontSize = 1.sp) else SpanStyle(color = faint)
            addStyle(markStyle, r.first, r.first + m.length)
            addStyle(markStyle, r.last + 1 - m.length, r.last + 1)
            for (i in r) taken[i] = true
        }
    }
}

private class MarkupStyler(private val faint: Color) : VisualTransformation {
    override fun filter(text: AnnotatedString) = TransformedText(styled(text.text, faint, hideMarks = false), OffsetMapping.Identity)
    override fun equals(other: Any?) = other is MarkupStyler && other.faint == faint
    override fun hashCode() = faint.hashCode()
}

private fun wrap(v: TextFieldValue, mark: String): TextFieldValue {
    val s = v.selection.min
    val e = v.selection.max
    val t = v.text
    // Already wrapped: unwrap.
    if (s >= mark.length && e + mark.length <= t.length && t.substring(s - mark.length, s) == mark && t.substring(e, e + mark.length) == mark) {
        val nt = t.substring(0, s - mark.length) + t.substring(s, e) + t.substring(e + mark.length)
        return TextFieldValue(nt, TextRange(s - mark.length, e - mark.length))
    }
    val nt = t.substring(0, s) + mark + t.substring(s, e) + mark + t.substring(e)
    return TextFieldValue(nt, if (s == e) TextRange(s + mark.length) else TextRange(s + mark.length, e + mark.length))
}

private fun prefixLines(v: TextFieldValue, numbered: Boolean): TextFieldValue {
    val t = v.text
    val start = t.lastIndexOf('\n', (v.selection.min - 1).coerceAtLeast(0)).let { if (it < 0 || v.selection.min == 0) 0 else it + 1 }
    val endNl = t.indexOf('\n', v.selection.max).let { if (it < 0) t.length else it }
    val lines = t.substring(start, endNl).split("\n")
    val bullet = Regex("^(• |\\d+\\. )")
    val allMarked = lines.all { bullet.containsMatchIn(it) }
    val out = lines.mapIndexed { i, l ->
        val bare = l.replace(bullet, "")
        if (allMarked) bare else (if (numbered) "${i + 1}. " else "• ") + bare
    }.joinToString("\n")
    val nt = t.substring(0, start) + out + t.substring(endNl)
    return TextFieldValue(nt, TextRange(start + out.length))
}

@Composable
fun NotesScreen(vm: PmpViewModel) {
    val rev by vm.revision.collectAsState()
    var value by remember { mutableStateOf(TextFieldValue(vm.store.notepad)) }
    var reading by remember { mutableStateOf(false) }
    var confirmClear by remember { mutableStateOf(false) }
    val clip = LocalClipboardManager.current
    val faint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.45f)

    // Saved shortly after typing stops, and when leaving the screen.
    LaunchedEffect(value.text) { delay(700); vm.store.setNotepad(value.text) }
    DisposableEffect(Unit) { onDispose { vm.store.setNotepad(value.text) } }

    Column(Modifier.fillMaxSize().imePadding().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("Formulas, mnemonics, traps you fell for, questions to come back to. Saved on this phone as you type.",
            fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            FilterChip(selected = !reading, onClick = { reading = false }, label = { Text("Edit") })
            FilterChip(selected = reading, onClick = { reading = true }, label = { Text("Read") })
        }
        if (!reading) {
            Surface(shape = RoundedCornerShape(10.dp), color = MaterialTheme.colorScheme.surfaceVariant) {
                Row(Modifier.horizontalScroll(rememberScrollState()).padding(4.dp), horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    RibbonButton("B", SpanStyle(fontWeight = FontWeight.Bold)) { value = wrap(value, "**") }
                    RibbonButton("I", SpanStyle(fontStyle = FontStyle.Italic)) { value = wrap(value, "*") }
                    RibbonButton("U", SpanStyle(textDecoration = TextDecoration.Underline)) { value = wrap(value, "__") }
                    RibbonButton("S", SpanStyle(textDecoration = TextDecoration.LineThrough)) { value = wrap(value, "~~") }
                    RibbonButton("H", SpanStyle(background = Color(0x66FFD54F))) { value = wrap(value, "==") }
                    TextButton(onClick = { value = prefixLines(value, numbered = false) }) {
                        Icon(Icons.AutoMirrored.Filled.FormatListBulleted, contentDescription = "Bulleted list", modifier = Modifier.size(20.dp))
                    }
                    TextButton(onClick = { value = prefixLines(value, numbered = true) }) {
                        Icon(Icons.Filled.FormatListNumbered, contentDescription = "Numbered list", modifier = Modifier.size(20.dp))
                    }
                }
            }
            OutlinedTextField(
                value = value,
                onValueChange = { value = it },
                visualTransformation = MarkupStyler(faint),
                placeholder = { Text("Select text and use the ribbon to format it.") },
                modifier = Modifier.fillMaxWidth().heightIn(min = 320.dp),
            )
        } else {
            PmpCard {
                if (value.text.isBlank()) Text("No notes yet.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                else Text(styled(value.text, faint, hideMarks = true), fontSize = 16.sp, lineHeight = 24.sp)
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("${value.text.trim().split(Regex("\\s+")).count { it.isNotEmpty() }} words", fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1f).padding(top = 12.dp))
            TextButton(onClick = { clip.setText(AnnotatedString(value.text)) }, enabled = value.text.isNotBlank()) { Text("Copy") }
            TextButton(onClick = {
                vm.store.setNotepad(value.text)
                vm.coachDraft = "Here are my study notes. Check them for anything wrong or missing for the PMP exam, and suggest what to add:\n\n" + value.text.take(6000)
                vm.coachTab = 1
                vm.navigate(com.jahangirb.pmpprep.ui.Dest.COACH)
            }, enabled = value.text.isNotBlank()) { Text("Ask coach") }
            TextButton(onClick = { confirmClear = true }, enabled = value.text.isNotBlank()) { Text("Clear") }
        }

        val qNotes = remember(rev, vm.contentVersion) { vm.store.notes.entries.mapNotNull { (id, n) -> vm.repo.byId[id]?.let { it to n } } }
        if (qNotes.isNotEmpty()) {
            Spacer(Modifier.height(6.dp))
            SectionLabel("Notes on questions (${qNotes.size})")
            qNotes.forEach { (q, n) ->
                PmpCard(onClick = { vm.startPracticeWith(listOf(q), "Your noted question") }) {
                    Text(q.stem, fontSize = 13.sp, maxLines = 2, overflow = TextOverflow.Ellipsis, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(4.dp))
                    Text(n, fontSize = 14.sp)
                }
            }
        }
        Spacer(Modifier.height(16.dp))
    }

    if (confirmClear) {
        AlertDialog(
            onDismissRequest = { confirmClear = false },
            title = { Text("Clear your notes?") },
            text = { Text("This empties the notes page. Notes on questions are kept.") },
            confirmButton = { TextButton(onClick = { value = TextFieldValue(""); vm.store.setNotepad(""); confirmClear = false }) { Text("Clear") } },
            dismissButton = { TextButton(onClick = { confirmClear = false }) { Text("Cancel") } },
        )
    }
}

@Composable
private fun RibbonButton(label: String, style: SpanStyle, onClick: () -> Unit) {
    TextButton(onClick = onClick, border = BorderStroke(0.dp, Color.Transparent)) {
        Text(buildAnnotatedString { pushStyle(style); append(label); pop() }, fontSize = 16.sp)
    }
}
