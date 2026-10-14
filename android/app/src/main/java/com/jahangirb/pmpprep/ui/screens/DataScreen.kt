package com.jahangirb.pmpprep.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material3.AlertDialog
import com.jahangirb.pmpprep.ui.components.ClayButton as Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import com.jahangirb.pmpprep.ui.components.ClayOutlinedButton as OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jahangirb.pmpprep.data.Domain
import com.jahangirb.pmpprep.data.QType
import com.jahangirb.pmpprep.data.QuestionPack
import com.jahangirb.pmpprep.ui.PmpViewModel
import com.jahangirb.pmpprep.ui.components.PmpCard
import com.jahangirb.pmpprep.ui.components.ProgressRow
import com.jahangirb.pmpprep.ui.components.SectionLabel
import com.jahangirb.pmpprep.ui.components.domainColor
import com.jahangirb.pmpprep.ui.components.pct
import com.jahangirb.pmpprep.ui.theme.ColorError
import com.jahangirb.pmpprep.ui.theme.ColorWarning
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DataScreen(vm: PmpViewModel) {
    val scope = rememberCoroutineScope()
    var message by remember { mutableStateOf<Pair<String, String>?>(null) }
    var confirmReset by remember { mutableStateOf(false) }
    var deleting by remember { mutableStateOf<QuestionPack?>(null) }
    val cv = vm.contentVersion

    val importLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenMultipleDocuments()) { uris ->
        if (uris.isNotEmpty()) scope.launch { message = "Import" to vm.importPacks(uris) }
    }
    val exportLauncher = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/json")) { uri ->
        if (uri != null) scope.launch { message = "Backup" to vm.exportProgress(uri) }
    }
    val restoreLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) scope.launch { message = "Restore" to vm.restoreProgress(uri) }
    }

    val pool = remember(cv) { vm.pool() }

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Question banks", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
        Text("Add your own bank from a .json question pack (the format in AUTHORING.md, or a plain list of questions). " +
            "Questions already in the app are skipped, and invalid ones are listed with the reason. Turn a bank off to leave it out of practice and mocks.",
            fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        // "*/*" because many file managers label .json files as octet-stream and would hide them otherwise.
        Button(onClick = { importLauncher.launch(arrayOf("*/*")) }, modifier = Modifier.fillMaxWidth().height(50.dp)) {
            Icon(Icons.Default.FileUpload, contentDescription = null)
            Text("  Add question bank (.json)", fontWeight = FontWeight.Bold)
        }
        SectionLabel("${pool.size} questions enabled")
        var userHeader = false
        vm.repo.packs.toList().forEach { p ->
            if (!p.builtin && !userHeader) { SectionLabel("Your imported banks"); userHeader = true }
            PmpCard {
                Row {
                    Column(Modifier.weight(1f)) {
                        Text(p.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(buildString {
                            append("${p.questions.size} questions")
                            if (p.skippedDuplicates > 0) append(" · ${p.skippedDuplicates} duplicates skipped")
                            if (p.invalid > 0) append(" · ${p.invalid} invalid")
                        }, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Switch(checked = cv >= 0 && vm.repo.isEnabled(p), onCheckedChange = { vm.setPackEnabled(p, it) })
                }
                // Not beside the switch: a slip on the toggle must never land on Delete.
                if (!p.builtin) TextButton(onClick = { deleting = p }) {
                    Icon(Icons.Default.Delete, contentDescription = null, tint = ColorError)
                    Text(" Delete this bank", color = ColorError)
                }
            }
        }

        SectionLabel("Content audit")
        PmpCard {
            Text("Enabled pool against the published domain weights", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            Domain.entries.forEach { d ->
                val share = if (pool.isEmpty()) 0.0 else pool.count { it.domain == d }.toDouble() / pool.size
                val target = vm.repo.extras.domainWeights[d] ?: 0.0
                Spacer(Modifier.height(8.dp))
                ProgressRow("${d.title} · target ${pct(target)}", share, pct(share), if (kotlin.math.abs(share - target) > 0.08) ColorWarning else domainColor(d))
            }
            Spacer(Modifier.height(10.dp))
            Text("By type: " + listOf(QType.SINGLE, QType.MULTI, QType.MATCHING, QType.PULLDOWN, QType.HOTSPOT, QType.GRAPHIC, QType.FILLBLANK)
                .joinToString(" · ") { t -> "${t.title} ${pool.count { it.type == t }}" }, fontSize = 12.sp)
            val thin = vm.repo.extras.tasks.filter { t -> pool.count { it.ecoTask == t.id } < 10 }
            Spacer(Modifier.height(6.dp))
            Text(if (thin.isEmpty()) "Every ECO task has at least 10 questions." else "Under 10 questions: " + thin.joinToString { it.code },
                fontSize = 12.sp, color = if (thin.isEmpty()) MaterialTheme.colorScheme.onSurfaceVariant else ColorWarning)
        }

        SectionLabel("Backup")
        Text("Answers, flags, notes, mistakes, flashcard schedule, mock results and your plan stay on this phone. Save a backup to keep them or move them to another phone.",
            fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        OutlinedButton(onClick = {
            exportLauncher.launch("pmp-progress-" + SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date()) + ".json")
        }, modifier = Modifier.fillMaxWidth()) { Text("Save progress backup") }
        OutlinedButton(onClick = { restoreLauncher.launch(arrayOf("*/*")) }, modifier = Modifier.fillMaxWidth()) { Text("Restore from backup") }

        // Destructive actions sit apart from everyday ones, and ask first.
        Spacer(Modifier.height(16.dp))
        SectionLabel("Danger zone")
        PmpCard(border = ColorError.copy(alpha = 0.5f)) {
            Text("Reset deletes every answer, flag, note, card schedule, mock result and your plan on this phone. Save a backup first if you might want them back.",
                fontSize = 13.sp, lineHeight = 18.sp)
            Spacer(Modifier.height(8.dp))
            OutlinedButton(onClick = { confirmReset = true }, modifier = Modifier.fillMaxWidth(),
                border = androidx.compose.foundation.BorderStroke(1.dp, ColorError)) { Text("Reset all progress", color = ColorError) }
        }

        Spacer(Modifier.height(8.dp))
        Text("PMP, PMBOK and PMI are registered marks of the Project Management Institute, Inc. This is an independent study aid, not endorsed by or affiliated with PMI.",
            fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(16.dp))
    }

    message?.let { (title, text) ->
        AlertDialog(onDismissRequest = { message = null }, title = { Text(title) }, text = { Text(text) },
            confirmButton = { TextButton(onClick = { message = null }) { Text("OK") } })
    }
    if (confirmReset) {
        AlertDialog(
            onDismissRequest = { confirmReset = false },
            title = { Text("Reset all progress?") },
            text = { Text("This deletes every answer, flag, note, card schedule, mock result and your plan. Question banks are kept.") },
            confirmButton = { TextButton(onClick = { vm.resetProgress(); confirmReset = false }) { Text("Reset", color = ColorError) } },
            dismissButton = { TextButton(onClick = { confirmReset = false }) { Text("Cancel") } },
        )
    }
    deleting?.let { p ->
        AlertDialog(
            onDismissRequest = { deleting = null },
            title = { Text("Delete \"${p.name}\"?") },
            text = { Text("Its ${p.questions.size} questions leave the app. Your answer history is kept and comes back if you import the file again.") },
            confirmButton = { TextButton(onClick = { vm.deletePack(p); deleting = null }) { Text("Delete", color = ColorError) } },
            dismissButton = { TextButton(onClick = { deleting = null }) { Text("Cancel") } },
        )
    }
}
