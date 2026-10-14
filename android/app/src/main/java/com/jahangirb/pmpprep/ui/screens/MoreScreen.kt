package com.jahangirb.pmpprep.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jahangirb.pmpprep.ui.Dest
import com.jahangirb.pmpprep.ui.PmpViewModel
import com.jahangirb.pmpprep.ui.components.SectionLabel

private data class Tile(val dest: Dest, val icon: ImageVector, val label: String, val detail: String)

/**
 * Everything not on the bottom bar, as a labelled grid: icon, name and a live
 * one-line status, so each module is recognisable without opening it. Whole
 * tiles are the touch target, far above the 48 dp minimum.
 */
@Composable
fun MoreScreen(vm: PmpViewModel) {
    val rev by vm.revision.collectAsState()
    val x = vm.repo.extras
    val tiles = remember(rev, vm.contentVersion) {
        val missed = vm.store.last.values.count { !it.correct }
        val untagged = vm.store.last.values.count { !it.correct && it.qid !in vm.store.mistakeTags }
        val report = vm.readiness.report(vm.store)
        listOf(
            Tile(Dest.MOCK, Icons.Default.Assignment, "Mock exam",
                if (vm.mock != null) "In progress · resume" else "${x.exam.totalItems} questions, ${x.exam.minutes} min · ${vm.store.mocks.size} sat"),
            Tile(Dest.COACH, Icons.Default.AutoAwesome, "Coach", "Today's session and Gemini chat"),
            Tile(Dest.MISTAKES, Icons.Default.ErrorOutline, "Mistake journal", if (missed == 0) "Nothing to review" else "$missed to review · $untagged untagged"),
            Tile(Dest.SITUATIONAL, Icons.Default.Psychology, "Situational trainer", "${x.scenarios.size} ranked dilemmas"),
            Tile(Dest.PLANNER, Icons.Default.CalendarMonth, "Study planner",
                if (vm.store.diagnostic == null) "Start with the diagnostic" else if (vm.store.plan.examDate == null) "Set your exam date" else "Your week by week plan"),
            Tile(Dest.ANALYTICS, Icons.Default.Insights, "Analytics", "Readiness ${report.score} · by task and type"),
            Tile(Dest.FORMULAS, Icons.Default.Calculate, "Formula lab", "Calculators, sheet and drills"),
            Tile(Dest.REFERENCE, Icons.AutoMirrored.Filled.LibraryBooks, "Reference", "ECO tasks, glossary, processes"),
            Tile(Dest.NOTES, Icons.AutoMirrored.Filled.Notes, "Notes", if (vm.store.notepad.isBlank()) "Your study notes" else "${vm.store.notepad.split(Regex("\\s+")).count { it.isNotEmpty() }} words"),
            Tile(Dest.DATA, Icons.Default.Storage, "Banks & backup", "${vm.pool().size} questions enabled"),
        )
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item(span = { GridItemSpan(2) }) { Column { Spacer(Modifier.height(8.dp)); SectionLabel("All study tools") } }
        items(tiles, key = { it.dest.name }) { t ->
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth().heightIn(min = 118.dp).clickable(role = Role.Button) { vm.navigate(t.dest) },
            ) {
                Column(Modifier.padding(14.dp)) {
                    Surface(color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f), shape = RoundedCornerShape(10.dp), modifier = Modifier.size(40.dp)) {
                        Box(contentAlignment = androidx.compose.ui.Alignment.Center) {
                            Icon(t.icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(22.dp))
                        }
                    }
                    Spacer(Modifier.height(10.dp))
                    Text(t.label, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Text(t.detail, fontSize = 12.sp, lineHeight = 16.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
        item(span = { GridItemSpan(2) }) { Spacer(Modifier.height(16.dp)) }
    }
}
