package com.jahangirb.pmpprep.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Science
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jahangirb.pmpprep.data.Theory
import com.jahangirb.pmpprep.data.TheoryBlock
import com.jahangirb.pmpprep.data.TheoryChapter
import com.jahangirb.pmpprep.ui.PmpViewModel
import com.jahangirb.pmpprep.ui.components.ClayButton as Button
import com.jahangirb.pmpprep.ui.components.ClayOutlinedButton as OutlinedButton
import com.jahangirb.pmpprep.ui.components.LessonFigure
import com.jahangirb.pmpprep.ui.components.PmpCard
import com.jahangirb.pmpprep.ui.components.SectionLabel
import com.jahangirb.pmpprep.ui.components.inlineMarkdown
import com.jahangirb.pmpprep.ui.theme.ColorSuccess
import com.jahangirb.pmpprep.ui.theme.ColorWarning
import kotlinx.coroutines.launch

private val ROMAN = listOf("", "I", "II", "III", "IV", "V", "VI", "VII", "VIII")
private val BOX = mapOf("exam" to "On the exam", "myth" to "Common misconception", "example" to "The running case")

/** The flask that marks a feature still being tested (index.html §10.37 .labs). */
@Composable
fun LabsBadge(modifier: Modifier = Modifier) {
    Surface(shape = RoundedCornerShape(50), color = ColorWarning.copy(alpha = 0.14f), modifier = modifier.semantics { contentDescription = "Labs: still being tested" }) {
        Row(Modifier.padding(horizontal = 8.dp, vertical = 2.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Outlined.Science, contentDescription = null, tint = ColorWarning, modifier = Modifier.size(14.dp))
            Spacer(Modifier.width(4.dp))
            Text("Labs", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ColorWarning)
        }
    }
}

/** Menu → Theory: chapters on project management as a discipline (index.html §10.37), in Labs. */
@Composable
fun TheoryScreen(vm: PmpViewModel) {
    val t = vm.repo.extras.theory
    val open = vm.theoryChapter?.let { id -> t.chapters.firstOrNull { it.id == id } }
    // Back from a chapter returns to the contents (PmpViewModel.back).
    // A fresh reader, and scroll position, for each chapter.
    if (open == null) TheoryContents(vm, t) else key(open.id) { TheoryReader(vm, t, open) }
}

@Composable
private fun TheoryContents(vm: PmpViewModel, t: Theory) {
    val place = vm.theoryPlace?.let { (id, _) -> t.chapters.firstOrNull { it.id == id } }
    LazyColumn(Modifier.fillMaxSize().padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Spacer(Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Project management in depth", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, modifier = Modifier.weight(1f))
                LabsBadge()
            }
            Spacer(Modifier.height(6.dp))
            Text("Optional reading for people who want to understand the discipline itself: why the practices exist, where they came from, how they fit together and how experienced project managers think. It goes well past what the exam asks, and every chapter ends with what the exam does ask.",
                fontSize = 14.sp, lineHeight = 21.sp)
            Spacer(Modifier.height(6.dp))
            Text("${t.chapters.size} of ${t.planned} chapters written · ${vm.theoryRead.count { id -> t.chapters.any { it.id == id } }} read",
                fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            if (place != null) {
                Spacer(Modifier.height(10.dp))
                Button(onClick = { vm.openTheory(place.id, resume = true) }, modifier = Modifier.fillMaxWidth()) {
                    Text("Continue: ${place.title}", fontWeight = FontWeight.Bold)
                }
            }
        }
        t.parts.forEach { p ->
            val mine = t.chapters.filter { it.part == p.n }
            val first = t.parts.filter { it.n < p.n }.sumOf { it.planned.size }
            item {
                PmpCard {
                    SectionLabel("Part " + ROMAN.getOrElse(p.n) { p.n.toString() })
                    Text(p.title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text(p.blurb, fontSize = 13.sp, lineHeight = 19.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(6.dp))
                    p.planned.forEachIndexed { i, title ->
                        val c = mine.getOrNull(i)
                        Row(
                            Modifier.fillMaxWidth().then(if (c != null) Modifier.clickable { vm.openTheory(c.id) } else Modifier).padding(vertical = 8.dp),
                            verticalAlignment = Alignment.Top,
                        ) {
                            Text("${first + i + 1}.", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.width(30.dp))
                            Column(Modifier.weight(1f)) {
                                Text(title, fontSize = 15.sp, fontWeight = FontWeight.SemiBold,
                                    color = if (c != null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant)
                                if (c != null) Text(c.summary, fontSize = 12.sp, lineHeight = 17.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Spacer(Modifier.width(8.dp))
                            when {
                                c == null -> Text("Not written yet", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                c.id in vm.theoryRead -> Icon(Icons.Filled.CheckCircle, contentDescription = "Read", tint = ColorSuccess, modifier = Modifier.size(18.dp))
                                else -> Text("${c.minutes} min", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }
        }
        item { Spacer(Modifier.height(24.dp)) }
    }
}

@Composable
private fun TheoryReader(vm: PmpViewModel, t: Theory, c: TheoryChapter) {
    val list = rememberLazyListState(initialFirstVisibleItemIndex = vm.theoryStart)
    val scope = rememberCoroutineScope()
    val idx = t.chapters.indexOf(c)
    val prev = t.chapters.getOrNull(idx - 1)
    val next = t.chapters.getOrNull(idx + 1)
    // Items: 0 header, 1..n sections, then key ideas, reading, footer.
    val keyItem = c.sections.size + 1
    val total = keyItem + 3
    val progress by remember(c.id) { derivedStateOf { ((list.firstVisibleItemIndex + 1f) / total).coerceIn(0f, 1f) } }
    val keyVisible by remember(c.id) { derivedStateOf { list.layoutInfo.visibleItemsInfo.any { it.index == keyItem } } }
    LaunchedEffect(keyVisible) { if (keyVisible && c.id !in vm.theoryRead) vm.setTheoryRead(c.id, true) }
    LaunchedEffect(c.id) { list.snapshotFlowIndex { vm.saveTheoryPlace(c.id, it) } }
    var menu by remember { mutableStateOf(false) }
    val codeBg = MaterialTheme.colorScheme.surfaceVariant

    Column(Modifier.fillMaxSize()) {
        LinearProgressIndicator(progress = { progress }, modifier = Modifier.fillMaxWidth().height(3.dp))
        Row(Modifier.fillMaxWidth().padding(horizontal = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = { vm.theoryChapter = null }) { Text("All chapters") }
            Spacer(Modifier.weight(1f))
            LabsBadge()
            Column {
                TextButton(onClick = { menu = true }) {
                    Icon(Icons.AutoMirrored.Filled.List, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Sections")
                }
                DropdownMenu(expanded = menu, onDismissRequest = { menu = false }) {
                    c.sections.forEachIndexed { i, s ->
                        DropdownMenuItem(text = { Text(s.title) }, onClick = { menu = false; scope.launch { list.animateScrollToItem(i + 1) } })
                    }
                    DropdownMenuItem(text = { Text("Key ideas") }, onClick = { menu = false; scope.launch { list.animateScrollToItem(keyItem) } })
                }
            }
        }
        LazyColumn(Modifier.fillMaxSize().padding(horizontal = 16.dp), state = list, verticalArrangement = Arrangement.spacedBy(18.dp)) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    SectionLabel("Part ${ROMAN.getOrElse(c.part) { c.part.toString() }} · Chapter ${t.numberOf(c)} · ${c.minutes} min read")
                    Text(c.title, fontSize = 26.sp, lineHeight = 31.sp, fontWeight = FontWeight.ExtraBold)
                    Text(inlineMarkdown(c.summary, codeBg), fontSize = 16.sp, lineHeight = 24.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Surface(shape = RoundedCornerShape(12.dp), color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)) {
                        Column(Modifier.padding(14.dp)) {
                            SectionLabel("Why it matters")
                            Spacer(Modifier.height(4.dp))
                            Text(inlineMarkdown(c.why, codeBg), fontSize = 16.sp, lineHeight = 25.sp)
                        }
                    }
                }
            }
            c.sections.forEach { s ->
                item(key = s.id) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(s.title, fontSize = 20.sp, lineHeight = 26.sp, fontWeight = FontWeight.Bold)
                        s.blocks.forEach { b -> Block(b, codeBg) }
                    }
                }
            }
            item(key = "key") {
                PmpCard {
                    Text("Key ideas", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(6.dp))
                    Items(c.keyIdeas, codeBg, numbered = false)
                }
            }
            item(key = "reading") {
                if (c.reading.isNotEmpty()) Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Further reading", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Items(c.reading, codeBg, numbered = false, size = 14)
                }
            }
            item(key = "foot") {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    val read = c.id in vm.theoryRead
                    OutlinedButton(onClick = { vm.setTheoryRead(c.id, !read) }) { Text(if (read) "Read ✓" else "Mark as read") }
                    if (next != null) Button(onClick = { vm.openTheory(next.id) }, modifier = Modifier.fillMaxWidth()) { Text("Next: ${next.title}", fontWeight = FontWeight.Bold) }
                    if (prev != null) TextButton(onClick = { vm.openTheory(prev.id) }) { Text("Previous: ${prev.title}") }
                    Spacer(Modifier.height(24.dp))
                }
            }
        }
    }
}

/** Calls [save] with the first visible item whenever it changes. */
private suspend fun androidx.compose.foundation.lazy.LazyListState.snapshotFlowIndex(save: (Int) -> Unit) {
    androidx.compose.runtime.snapshotFlow { firstVisibleItemIndex }.collect { save(it) }
}

@Composable
private fun Block(b: TheoryBlock, codeBg: Color) {
    when (b) {
        is TheoryBlock.Para -> Text(inlineMarkdown(b.text, codeBg), fontSize = 16.sp, lineHeight = 25.sp)
        is TheoryBlock.Heading -> Text(b.text, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        is TheoryBlock.Bullets -> Items(b.items, codeBg, numbered = false)
        is TheoryBlock.Numbered -> Items(b.items, codeBg, numbered = true)
        is TheoryBlock.Fig -> LessonFigure(b.figure)
        is TheoryBlock.Box -> {
            val tint = when (b.kind) {
                "exam" -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f)
                "myth" -> ColorWarning.copy(alpha = 0.12f)
                else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
            }
            Surface(shape = RoundedCornerShape(12.dp), color = tint, modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(14.dp)) {
                    SectionLabel(BOX[b.kind] ?: b.kind)
                    Spacer(Modifier.height(4.dp))
                    Text(inlineMarkdown(b.text, codeBg), fontSize = 14.sp, lineHeight = 21.sp)
                }
            }
        }
    }
}

@Composable
private fun Items(items: List<String>, codeBg: Color, numbered: Boolean, size: Int = 16) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        items.forEachIndexed { i, it ->
            Row {
                Text(if (numbered) "${i + 1}." else "•", fontSize = size.sp, lineHeight = (size * 1.55).sp, modifier = Modifier.width(24.dp))
                Text(inlineMarkdown(it, codeBg), fontSize = size.sp, lineHeight = (size * 1.55).sp, modifier = Modifier.weight(1f))
            }
        }
    }
}
