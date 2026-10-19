package com.jahangirb.pmpprep.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.jahangirb.pmpprep.data.Support
import com.jahangirb.pmpprep.ui.theme.ColorWarning
import com.jahangirb.pmpprep.ui.theme.SupportInk
import com.jahangirb.pmpprep.ui.theme.SupportTint
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jahangirb.pmpprep.ui.Dest
import com.jahangirb.pmpprep.ui.PmpViewModel
import com.jahangirb.pmpprep.ui.PracticeFilter
import com.jahangirb.pmpprep.ui.components.SectionLabel

private class Entry(val icon: ImageVector, val label: String, val detail: String, val go: () -> Unit)

/**
 * Extra work, beside the guided path. Grouped by what the person wants to do,
 * each row labelled with a live status. Everything done here counts: the path
 * reads it and adjusts.
 */
@Composable
fun MenuScreen(vm: PmpViewModel) {
    val rev by vm.revision.collectAsState()
    val x = vm.repo.extras
    val missed = if (rev >= 0) vm.store.last.values.count { !it.correct } else 0
    val flagged = vm.store.flags.size
    val groups = listOf(
        "Start here" to listOf(
            Entry(Icons.AutoMirrored.Filled.HelpOutline, "How it works", "What you get, what's measured, how weak spots are handled") { vm.navigate(Dest.GUIDE) },
            Entry(Icons.Default.Search, "Search everything", "Lessons, glossary, formulas, your notes, explanations") { vm.navigate(Dest.SEARCH) },
        ),
        "Practise" to listOf(
            Entry(Icons.Default.Quiz, "Build a drill", "Pick domain, task, type, approach, bank") { vm.navigate(Dest.PRACTICE) },
            Entry(Icons.Default.Replay, "Redo mistakes", if (missed == 0) "None right now" else "$missed wrong last time") {
                if (missed > 0) vm.startPractice(PracticeFilter(which = PracticeFilter.Which.MISSED, count = 40), "Mistakes")
            },
            Entry(Icons.Default.Flag, "Flagged questions", if (flagged == 0) "None flagged" else "$flagged flagged") {
                if (flagged > 0) vm.startPractice(PracticeFilter(which = PracticeFilter.Which.FLAGGED, count = 40), "Flagged")
            },
            Entry(Icons.Default.Psychology, "Situational trainer", "${x.scenarios.size} ranked dilemmas") { vm.navigate(Dest.SITUATIONAL) },
            Entry(Icons.Default.Calculate, "Formula lab", "Calculators, formula sheet, calculation drills") { vm.navigate(Dest.FORMULAS) },
        ),
        "Test yourself" to listOf(
            Entry(Icons.Default.Assignment, "Mock exam", if (vm.mock != null) "In progress · resume" else "${x.exam.totalItems} questions or shorter · ${vm.store.mocks.size} sat") { vm.navigate(Dest.MOCK) },
            Entry(Icons.Default.Speed, "Diagnostic", vm.store.diagnostic?.let { "Last: ${(it.pct * 100).toInt()}% · retake" } ?: "25 questions, taken cold") { vm.startDiagnostic() },
        ),
        "Study" to listOf(
            Entry(Icons.AutoMirrored.Filled.MenuBook, "Lessons", "${vm.store.lessonsDone.size} of ${x.lessons.size} done") { vm.navigate(Dest.LEARN) },
            Entry(Icons.Default.Style, "Flashcards", "${vm.deckCounts(null).due} due" + if (vm.store.myCards.isNotEmpty()) " · ${vm.store.myCards.size} of your own" else "") { vm.navigate(Dest.FLASHCARDS) },
            Entry(Icons.AutoMirrored.Filled.LibraryBooks, "Reference and glossary", "ECO tasks, ${x.glossary.size} terms, processes") { vm.navigate(Dest.REFERENCE) },
        ),
        "My progress" to listOf(
            Entry(Icons.Default.AutoAwesome, "Coach", if (vm.geminiKey.isEmpty()) "How you're doing; connect Gemini to chat" else "How you're doing, and Gemini chat") { vm.coachTab = 0; vm.navigate(Dest.COACH) },
            Entry(Icons.Default.EventAvailable, "Am I ready to book?", if (vm.booking().ready) "Yes: all three conditions met" else "What's left, and a projected date") { vm.navigate(Dest.BOOK) },
            Entry(Icons.Default.SelfImprovement, "Exam fitness", "Stamina, composure and consistency") { vm.navigate(Dest.FITNESS) },
            Entry(Icons.Default.DateRange, "Weekly reports", "Each week compared with the one before") { vm.navigate(Dest.REPORTS) },
            Entry(Icons.Default.Insights, "Analytics", "Readiness ${vm.readiness.report(vm.store).score}, by task and type") { vm.navigate(Dest.ANALYTICS) },
            Entry(Icons.Default.ErrorOutline, "Mistake journal", "Tag causes, retry") { vm.navigate(Dest.MISTAKES) },
            Entry(Icons.AutoMirrored.Filled.Notes, "Notes", "Your notes page, also in the coach bubble") { vm.coachTab = 2; vm.navigate(Dest.COACH) },
        ),
        "Settings" to listOf(
            Entry(Icons.Default.CalendarMonth, "Plan and schedule", "Exam date, hours, study days") { vm.navigate(Dest.PLANNER) },
            Entry(Icons.Default.TextFields, "Study settings", "Text size, reading guide, confidence, focus mode, rest days") { vm.navigate(Dest.SETTINGS) },
            Entry(Icons.Default.Storage, "Question banks and backup", "${vm.pool().size} questions enabled") { vm.navigate(Dest.DATA) },
            Entry(Icons.Default.Cloud, "Cloud sync", vm.syncStatus()) { vm.navigate(Dest.SYNC) },
        ),
    )
    val context = LocalContext.current
    LazyColumn(Modifier.fillMaxSize()) {
        item {
            val pill = RoundedCornerShape(50)
            Row(
                Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp).fillMaxWidth().clip(pill)
                    .background(SupportTint).border(1.5.dp, ColorWarning, pill)
                    .clickable(role = Role.Button) { openLink(context, Support.URL); vm.supportPressed() }
                    .semantics { contentDescription = "Support the app on Ko-fi" }
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Icon(Icons.Default.Favorite, contentDescription = null, tint = SupportInk, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text("Support the app", color = SupportInk, fontSize = 16.sp, fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clearAndSetSemantics { })
            }
        }
        item {
            Text("Extra work beside your plan. Anything you do here counts, and the path adjusts to it.",
                fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(16.dp))
        }
        groups.forEach { (title, entries) ->
            item {
                SectionLabel(title, Modifier.padding(start = 16.dp, top = 12.dp, bottom = 4.dp))
                entries.forEach { e ->
                    Row(
                        Modifier.fillMaxWidth().clickable(role = Role.Button, onClick = e.go).padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(e.icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
                        Spacer(Modifier.width(16.dp))
                        Column(Modifier.weight(1f)) {
                            Text(e.label, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                            Text(e.detail, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
                HorizontalDivider(Modifier.padding(top = 4.dp))
            }
        }
        item { Spacer(Modifier.height(24.dp)) }
    }
}
