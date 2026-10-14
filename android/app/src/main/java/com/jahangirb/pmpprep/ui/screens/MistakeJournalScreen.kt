package com.jahangirb.pmpprep.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.AlertDialog
import com.jahangirb.pmpprep.ui.components.ClayButton as Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import com.jahangirb.pmpprep.ui.components.ClayOutlinedButton as OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jahangirb.pmpprep.data.Question
import com.jahangirb.pmpprep.data.RootCause
import com.jahangirb.pmpprep.ui.PmpViewModel
import com.jahangirb.pmpprep.ui.components.DomainChip
import com.jahangirb.pmpprep.ui.components.EmptyNote
import com.jahangirb.pmpprep.ui.components.Pill
import com.jahangirb.pmpprep.ui.components.PmpCard
import com.jahangirb.pmpprep.ui.theme.ColorError

/**
 * Every question whose latest answer was wrong. Tagging why it went wrong is
 * what turns a list of misses into a pattern you can fix.
 */
@Composable
fun MistakeJournalScreen(vm: PmpViewModel) {
    val rev by vm.revision.collectAsState()
    var cause by rememberSaveable { mutableStateOf<String?>(null) }
    var tagging by remember { mutableStateOf<Question?>(null) }
    val missed = remember(rev, vm.contentVersion) {
        vm.store.last.values.filter { !it.correct }.sortedByDescending { it.at }.mapNotNull { vm.repo.byId[it.qid] }
    }
    val shown = missed.filter { q ->
        when (cause) {
            null -> true
            "untagged" -> q.id !in vm.store.mistakeTags
            else -> vm.store.mistakeTags[q.id]?.cause?.name == cause
        }
    }
    val counts = RootCause.entries.associateWith { c -> missed.count { vm.store.mistakeTags[it.id]?.cause == c } }

    LazyColumn(Modifier.fillMaxSize().padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item {
            Spacer(Modifier.height(8.dp))
            Text("${missed.size} questions wrong last time", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
            Text("Tag why each one went wrong. Answer it correctly and it leaves the journal.", fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(8.dp))
            Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                FilterChip(selected = cause == null, onClick = { cause = null }, label = { Text("All ${missed.size}") })
                FilterChip(selected = cause == "untagged", onClick = { cause = "untagged" },
                    label = { Text("Untagged ${missed.count { it.id !in vm.store.mistakeTags }}") })
                RootCause.entries.forEach { c ->
                    FilterChip(selected = cause == c.name, onClick = { cause = c.name }, label = { Text("${c.title} ${counts[c]}") })
                }
            }
            Spacer(Modifier.height(8.dp))
            Button(onClick = { vm.startPracticeWith(shown.take(40), "Mistake review") }, enabled = shown.isNotEmpty(), modifier = Modifier.fillMaxWidth()) {
                Text("Retry ${minOf(40, shown.size)} of these", fontWeight = FontWeight.Bold)
            }
        }
        if (missed.isEmpty()) item { EmptyNote("Nothing here yet. Questions you get wrong in practice or a mock show up here.") }
        items(shown, key = { it.id }) { q ->
            val tag = vm.store.mistakeTags[q.id]
            val task = vm.repo.task(q.ecoTask)
            PmpCard {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    DomainChip(q.domain)
                    if (task != null) Pill(task.label, MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Spacer(Modifier.height(6.dp))
                Text(q.stem, fontSize = 14.sp, lineHeight = 20.sp, maxLines = 4, overflow = TextOverflow.Ellipsis)
                Spacer(Modifier.height(6.dp))
                if (tag != null) {
                    Text("Cause: ${tag.cause.title}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = ColorError)
                    if (tag.note.isNotBlank()) Text(tag.note, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                if (q.mindset.isNotBlank()) Text(q.mindset, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary, lineHeight = 16.sp)
                Spacer(Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = { tagging = q }, modifier = Modifier.weight(1f)) { Text(if (tag == null) "Tag the cause" else "Change tag") }
                    Button(onClick = { vm.startPracticeWith(listOf(q), "Retry") }, modifier = Modifier.weight(1f)) { Text("Retry") }
                }
            }
        }
        item { Spacer(Modifier.height(16.dp)) }
    }

    tagging?.let { q ->
        var pick by remember(q.id) { mutableStateOf(vm.store.mistakeTags[q.id]?.cause ?: RootCause.KNOWLEDGE_GAP) }
        var note by remember(q.id) { mutableStateOf(vm.store.mistakeTags[q.id]?.note.orEmpty()) }
        AlertDialog(
            onDismissRequest = { tagging = null },
            title = { Text("Why did this one go wrong?") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    RootCause.entries.forEach { c ->
                        FilterChip(selected = pick == c, onClick = { pick = c }, label = {
                            Column(Modifier.padding(vertical = 4.dp)) {
                                Text(c.title, fontWeight = FontWeight.Bold)
                                Text(c.description, fontSize = 11.sp)
                            }
                        }, modifier = Modifier.fillMaxWidth())
                    }
                    OutlinedTextField(value = note, onValueChange = { note = it }, label = { Text("Note (optional)") }, modifier = Modifier.fillMaxWidth())
                }
            },
            confirmButton = { TextButton(onClick = { vm.store.tagMistake(q.id, pick, note); tagging = null }) { Text("Save") } },
            dismissButton = { TextButton(onClick = { tagging = null }) { Text("Cancel") } },
        )
    }
}
