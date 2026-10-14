package com.jahangirb.pmpprep.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material3.AlertDialog
import com.jahangirb.pmpprep.ui.components.ClayButton as Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import com.jahangirb.pmpprep.ui.components.ClayOutlinedButton as OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jahangirb.pmpprep.data.Domain
import com.jahangirb.pmpprep.data.MockResult
import com.jahangirb.pmpprep.data.Scoring
import com.jahangirb.pmpprep.ui.MockSession
import com.jahangirb.pmpprep.ui.PmpViewModel
import com.jahangirb.pmpprep.ui.components.BulletList
import com.jahangirb.pmpprep.ui.components.PmpCard
import com.jahangirb.pmpprep.ui.components.ProgressRow
import com.jahangirb.pmpprep.ui.components.QuestionBody
import com.jahangirb.pmpprep.ui.components.QuestionHeader
import com.jahangirb.pmpprep.ui.components.SectionLabel
import com.jahangirb.pmpprep.ui.components.clock
import com.jahangirb.pmpprep.ui.components.domainColor
import com.jahangirb.pmpprep.ui.components.pct
import com.jahangirb.pmpprep.ui.theme.ColorError
import com.jahangirb.pmpprep.ui.theme.ColorInfo
import com.jahangirb.pmpprep.ui.theme.ColorSuccess
import com.jahangirb.pmpprep.ui.theme.ColorWarning
import java.text.DateFormat
import java.util.Date

@Composable
fun MockExamScreen(vm: PmpViewModel) {
    val m = vm.mock
    val review = vm.mockReview
    when {
        m != null && m.onBreak -> BreakScreen(vm, m)
        m != null -> ExamRunner(vm, m)
        review != null -> QuizRunner(vm, review, onFinish = { vm.mockReview = null })
        vm.lastMock != null -> Scorecard(vm, vm.lastMock!!)
        else -> MockIntro(vm)
    }
}

/** A rough guide only: PMI reports bands per domain but does not publish the cut scores behind them. */
@Composable
private fun band(p: Double): Pair<String, Color> = when {
    p >= 0.80 -> "Above Target" to ColorSuccess
    p >= 0.65 -> "Target" to ColorInfo
    p >= 0.50 -> "Below Target" to ColorWarning
    else -> "Needs Improvement" to ColorError
}

@Composable
private fun MockIntro(vm: PmpViewModel) {
    val rev by vm.revision.collectAsState()
    val x = vm.repo.extras.exam
    val pool = remember(vm.contentVersion) { vm.pool().size }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Mock Exam Simulator", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
        Text("Drawn from your $pool enabled questions, weighted to the published domain split, unseen questions first.",
            fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        PmpCard {
            SectionLabel("Full exam layout")
            Spacer(Modifier.height(8.dp))
            BulletList(listOf(
                "${x.totalItems} questions in ${x.minutes} minutes: about ${x.paceTargetSec} seconds each.",
                "Three sections. After questions ${x.breakAfter.joinToString(" and ")} you may take a ${x.breakMinutes}-minute break; the clock stops.",
                "Once a section is closed you cannot go back to it, as in the real exam.",
                "Mark questions to revisit, and jump around the current section from the question grid.",
                "Scored at the end with every question type marked the same way as practice.",
            ))
        }
        val sizes = listOf(
            x.totalItems to "Full exam",
            x.totalItems / 2 to "Half exam",
            x.totalItems / 4 to "Quarter exam",
            20 to "Sprint",
        )
        sizes.forEach { (n, label) ->
            val minutes = Math.round(x.minutes.toDouble() * n / x.totalItems)
            Button(onClick = { vm.startMock(n) }, enabled = pool > 0, modifier = Modifier.fillMaxWidth().height(52.dp)) {
                Text("$label · $n questions · $minutes min", fontWeight = FontWeight.Bold)
            }
        }
        if (rev >= 0 && vm.store.mocks.isNotEmpty()) {
            SectionLabel("Your sittings")
            vm.store.mocks.sortedByDescending { it.at }.forEach { r -> HistoryRow(r) }
        }
    }
}

@Composable
private fun HistoryRow(r: MockResult) {
    val (label, color) = band(r.pct)
    PmpCard {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(Date(r.at)), fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                Text("${r.title} · ${r.right}/${r.n} · ${clock(r.usedMs)} used", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(Domain.entries.mapNotNull { d -> r.domains[d]?.let { "${d.short} ${pct(if (it.second == 0) 0.0 else it.first.toDouble() / it.second)}" } }.joinToString(" · "),
                    fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(pct(r.pct), fontSize = 22.sp, fontWeight = FontWeight.Black, color = color)
                Text(label, fontSize = 11.sp, color = color, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun ExamRunner(vm: PmpViewModel, m: MockSession) {
    val q = m.current
    var grid by remember { mutableStateOf(false) }
    var endDialog by remember { mutableStateOf(false) }
    var leave by remember { mutableStateOf(false) }
    val scroll = rememberScrollState()
    LaunchedEffect(m.index) { scroll.scrollTo(0) }
    val range = m.inSection
    val lastInSection = m.index == range.last
    val finalSection = m.section == m.sectionCount - 1

    Column(Modifier.fillMaxSize()) {
        Surface(color = MaterialTheme.colorScheme.surface) {
            Column(Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("Question ${m.index + 1} of ${m.questions.size}", fontWeight = FontWeight.Bold)
                        if (m.sectionCount > 1) Text("Section ${m.section + 1} of ${m.sectionCount}", fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Text(clock(m.remainingMs), fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold,
                        color = if (m.remainingMs < 5 * 60_000) ColorError else MaterialTheme.colorScheme.onSurface)
                    val mk = m.marked[q.id] == true
                    IconButton(onClick = { vm.toggleMark(q) }) {
                        Icon(if (mk) Icons.Filled.Flag else Icons.Outlined.Flag, contentDescription = "Mark for review",
                            tint = if (mk) ColorWarning else MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                LinearProgressIndicator(progress = { (m.index + 1f) / m.questions.size }, modifier = Modifier.fillMaxWidth())
            }
        }
        Column(Modifier.weight(1f).verticalScroll(scroll).padding(16.dp)) {
            QuestionHeader(q, vm.repo.task(q.ecoTask)?.let { "${it.code} · ${it.label}" })
            QuestionBody(q, m.order[q.id] ?: q.options, m.answers[q.id], revealed = false) { a -> vm.mockAnswer(q, a) }
            Spacer(Modifier.height(24.dp))
            TextButton(onClick = { leave = true }) { Text("Leave or abandon this exam") }
        }
        HorizontalDivider()
        Row(Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = { vm.mockGo(m.index - 1) }, enabled = m.index > range.first, modifier = Modifier.weight(1f)) { Text("Back") }
            OutlinedButton(onClick = { grid = true }, modifier = Modifier.weight(1f)) {
                Text("${range.count { Scoring.isAnswered(m.questions[it], m.answers[m.questions[it].id]) }}/${range.count()}")
            }
            Button(onClick = { if (lastInSection) endDialog = true else vm.mockGo(m.index + 1) }, modifier = Modifier.weight(1.4f)) {
                Text(if (!lastInSection) "Next" else if (finalSection) "Finish" else "End section", fontWeight = FontWeight.Bold)
            }
        }
    }

    if (grid) {
        AlertDialog(
            onDismissRequest = { grid = false },
            title = { Text(if (m.sectionCount > 1) "Section ${m.section + 1}" else "All questions") },
            text = {
                Column {
                    Text("Filled: answered · outlined: unanswered · gold: marked", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(8.dp))
                    LazyVerticalGrid(columns = GridCells.Fixed(6), modifier = Modifier.heightIn(max = 380.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(range.toList()) { i ->
                            val qq = m.questions[i]
                            val ans = Scoring.isAnswered(qq, m.answers[qq.id])
                            val mk = m.marked[qq.id] == true
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (ans) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                border = BorderStroke(if (mk || i == m.index) 2.dp else 0.dp,
                                    if (mk) ColorWarning else if (i == m.index) MaterialTheme.colorScheme.onSurface else Color.Transparent),
                                modifier = Modifier.size(40.dp).clickable { vm.mockGo(i); grid = false },
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("${i + 1}", fontSize = 12.sp, fontWeight = FontWeight.Bold,
                                        color = if (ans) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface)
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = { TextButton(onClick = { grid = false; endDialog = true }) { Text(if (finalSection) "Finish exam" else "End section") } },
            dismissButton = { TextButton(onClick = { grid = false }) { Text("Close") } },
        )
    }

    if (endDialog) {
        val open = range.count { !Scoring.isAnswered(m.questions[it], m.answers[m.questions[it].id]) }
        val marked = range.count { m.marked[m.questions[it].id] == true }
        AlertDialog(
            onDismissRequest = { endDialog = false },
            title = { Text(if (finalSection) "Submit the exam?" else "Close section ${m.section + 1}?") },
            text = {
                Text(buildString {
                    append(if (open > 0) "$open unanswered question(s) in this section will be scored as wrong. " else "Every question in this section is answered. ")
                    if (marked > 0) append("$marked are marked for review. ")
                    if (!finalSection) append("You cannot come back to this section afterwards.")
                })
            },
            confirmButton = {
                if (finalSection) TextButton(onClick = { endDialog = false; vm.submitMock() }) { Text("Submit") }
                else TextButton(onClick = { endDialog = false; vm.endSection(takeBreak = true) }) { Text("Take the break") }
            },
            dismissButton = {
                Row {
                    if (!finalSection) TextButton(onClick = { endDialog = false; vm.endSection(takeBreak = false) }) { Text("Skip break") }
                    TextButton(onClick = { endDialog = false }) { Text("Keep reviewing") }
                }
            },
        )
    }

    if (leave) {
        AlertDialog(
            onDismissRequest = { leave = false },
            title = { Text("Leave the exam?") },
            text = { Text("Pause keeps your answers, and the clock stops until you come back; resume from Home. Abandon throws this sitting away.") },
            confirmButton = { TextButton(onClick = { leave = false; vm.saveMock(); vm.back() }) { Text("Pause") } },
            dismissButton = {
                Row {
                    TextButton(onClick = { leave = false; vm.abandonMock() }) { Text("Abandon", color = ColorError) }
                    TextButton(onClick = { leave = false }) { Text("Stay") }
                }
            },
        )
    }
}

@Composable
private fun BreakScreen(vm: PmpViewModel, m: MockSession) {
    Column(Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Icon(Icons.Default.Coffee, contentDescription = null, tint = MaterialTheme.colorScheme.tertiary, modifier = Modifier.size(56.dp))
        Spacer(Modifier.height(12.dp))
        Text("Optional break", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text("The exam clock is stopped. Section ${m.section + 1} is closed.", textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(16.dp))
        Text(clock(m.breakRemainingMs), fontSize = 40.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Black)
        Spacer(Modifier.height(16.dp))
        Button(onClick = { vm.endBreak() }) { Text("Start section ${m.section + 2} now") }
    }
}

@Composable
private fun Scorecard(vm: PmpViewModel, r: MockResult) {
    val (label, color) = band(r.pct)
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Mock exam scorecard", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
        PmpCard(container = color.copy(alpha = 0.08f), border = color) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text(label, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = color)
                    Text("${r.right} of ${r.n} correct · ${clock(r.usedMs)} used", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Text(pct(r.pct), fontSize = 40.sp, fontWeight = FontWeight.Black, color = color)
            }
            Spacer(Modifier.height(6.dp))
            Text("Bands are a rough guide: PMI reports Above Target, Target, Below Target and Needs Improvement but does not publish the cut scores.",
                fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        PmpCard {
            SectionLabel("By domain")
            Spacer(Modifier.height(8.dp))
            Domain.entries.forEach { d ->
                r.domains[d]?.let { (right, of) ->
                    val p = if (of == 0) 0.0 else right.toDouble() / of
                    ProgressRow("${d.title} · ${band(p).first}", p, "$right/$of", domainColor(d))
                    Spacer(Modifier.height(10.dp))
                }
            }
        }
        Button(onClick = { vm.reviewMock(onlyWrong = true) }, modifier = Modifier.fillMaxWidth()) { Text("Review the ones I got wrong") }
        OutlinedButton(onClick = { vm.reviewMock(onlyWrong = false) }, modifier = Modifier.fillMaxWidth()) { Text("Review every question") }
        TextButton(onClick = { vm.clearScorecard() }, modifier = Modifier.fillMaxWidth()) { Text("Done") }
    }
}
