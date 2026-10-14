package com.jahangirb.pmpprep.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jahangirb.pmpprep.data.ExamFitness
import com.jahangirb.pmpprep.data.FitnessPart
import com.jahangirb.pmpprep.ui.Dest
import com.jahangirb.pmpprep.ui.PmpViewModel
import com.jahangirb.pmpprep.ui.components.PmpCard
import com.jahangirb.pmpprep.ui.components.ProgressRow
import com.jahangirb.pmpprep.ui.components.SectionLabel
import com.jahangirb.pmpprep.ui.components.ClayButton as Button
import com.jahangirb.pmpprep.ui.components.ClayOutlinedButton as OutlinedButton

/** Exam fitness: stamina, composure and consistency, what they're built from, and what to do next. */
@Composable
fun FitnessScreen(vm: PmpViewModel) {
    val rev by vm.revision.collectAsState()
    val f = remember(rev) { vm.fitness() }
    var weekly by remember { mutableStateOf(false) }

    LazyColumn(Modifier.fillMaxSize().padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Spacer(Modifier.height(4.dp))
            Text("Are you ready to sit it?", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
            Text("Readiness asks whether you know the material. This asks whether you can sit for four hours, read 180 long questions calmly, and keep preparing until the day. It describes what you did; it isn't a diagnosis.",
                fontSize = 13.sp, lineHeight = 19.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        item {
            PmpCard {
                SectionLabel("Exam fitness")
                Text(f.score?.let { "$it / 100" } ?: "Not enough yet", fontSize = 30.sp, fontWeight = FontWeight.ExtraBold)
                if (f.provisional && f.score != null) Text("Some parts still need evidence, so this is provisional.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(8.dp))
                f.parts.forEach { p ->
                    ProgressRow("${p.label} · ${p.weight}%", p.score ?: 0.0, p.score?.let { "${(it * 100).toInt()}" } ?: "not yet")
                    Spacer(Modifier.height(4.dp))
                }
                f.finding?.let { Spacer(Modifier.height(6.dp)); Text(it, fontWeight = FontWeight.Bold, fontSize = 14.sp) }
                f.next?.let { Text("Next: $it", fontSize = 14.sp, lineHeight = 20.sp) }
            }
        }
        item { Ladder(f.ladderRung, f.ladderNext) }
        if (f.worryHighForWeeks) item {
            PmpCard(container = MaterialTheme.colorScheme.tertiaryContainer) {
                Text("Your weekly checks have shown a lot of worry for three weeks.", fontWeight = FontWeight.Bold)
                Text("That's common before a big exam, and you don't have to carry it alone. Talking it through with someone you trust, or a doctor or counsellor, can help as much as more study.",
                    fontSize = 14.sp, lineHeight = 20.sp)
            }
        }
        item {
            PmpCard {
                SectionLabel("How you feel")
                Text(f.worry?.let { "Exam worry: $it / 100 (last weekly check)" } ?: "No weekly check yet.", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                f.link?.let { Text(it, fontSize = 13.sp) }
                Text("Five statements, about 30 seconds, once a week. It isn't scored against you; it shows whether worry eases as your preparation builds.",
                    fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                TextButton(onClick = { weekly = true }) { Text(if (vm.weeklyCheckDue()) "Take this week's check" else "Take it again") }
            }
        }
        f.parts.forEach { p -> item { PartCard(p) } }
        item {
            OutlinedButton(onClick = { vm.navigate(Dest.REPORTS) }, modifier = Modifier.fillMaxWidth()) { Text("Weekly reports and comparisons") }
            Spacer(Modifier.height(24.dp))
        }
    }
    if (weekly) WeeklyCheckDialog(onDone = { items -> vm.recordCheckIn("weekly", 0, items); weekly = false }, onDismiss = { weekly = false })
}

@Composable
private fun PartCard(p: FitnessPart) {
    PmpCard {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(p.label, fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, modifier = Modifier.weight(1f))
            Text(p.score?.let { "${(it * 100).toInt()} / 100" } ?: "not yet", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        }
        p.gate?.let { Text(it, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant) }
        Spacer(Modifier.height(6.dp))
        p.signals.forEach { s ->
            ProgressRow("${s.label} · ${s.weight}%", s.score ?: 0.0, s.figure)
            Text(s.detail, fontSize = 12.sp, lineHeight = 17.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(8.dp))
        }
    }
}

/** 20 → 180 questions in one sitting, like a runner building to a marathon. */
@Composable
private fun Ladder(rung: Int, next: Int?) {
    PmpCard {
        SectionLabel("Stamina ladder")
        Spacer(Modifier.height(6.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.semantics {
            contentDescription = "Stamina ladder: reached $rung questions" + (next?.let { ", next $it" } ?: ", complete")
        }) {
            ExamFitness.LADDER.forEach { r ->
                val done = r <= rung
                val isNext = r == next
                Box(
                    Modifier.weight(1f).heightIn(min = 44.dp)
                        .background(if (done) MaterialTheme.colorScheme.primary else if (isNext) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center,
                ) {
                    Text("$r", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp,
                        color = if (done) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface)
                }
            }
        }
        Spacer(Modifier.height(6.dp))
        Text(next?.let { "Next: $it questions in one sitting at exam pace. It's on your path once a week. You move up when your last-quarter accuracy stays within 10 points of the first." }
            ?: "You've held accuracy through a full 180-question sitting.", fontSize = 13.sp, lineHeight = 18.sp)
    }
}

/* ── check-ins ─────────────────────────────────────────────────────────── */

private val FACES = listOf("😞" to "Very low", "🙁" to "Low", "😐" to "OK", "🙂" to "Good", "😄" to "Very good")

/** One tap, five faces. [question] is what's asked; values are 1–5. */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CheckInRow(question: String, onPick: (Int) -> Unit, onSkip: () -> Unit, labels: List<Pair<String, String>> = FACES) {
    PmpCard {
        Text(question, fontWeight = FontWeight.Bold, fontSize = 15.sp)
        Text("Optional. It helps the app show you how feelings and study relate.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(8.dp))
        FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            labels.forEachIndexed { i, (face, label) ->
                FilterChip(selected = false, onClick = { onPick(i + 1) }, label = { Text("$face $label") },
                    modifier = Modifier.semantics { contentDescription = label })
            }
        }
        TextButton(onClick = onSkip) { Text("Skip") }
    }
}

/** How tense, for after a mock: 1 = very tense … 5 = calm. */
val TENSION = listOf("😣" to "Very tense", "😟" to "Tense", "😐" to "So-so", "🙂" to "Calm", "😌" to "Very calm")

/** The weekly five-statement check. Each answer 1 (disagree) to 5 (agree). */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WeeklyCheckDialog(onDone: (List<Int>) -> Unit, onDismiss: () -> Unit) {
    val answers = remember { mutableStateListOf<Int>().apply { repeat(ExamFitness.WEEKLY_STATEMENTS.size) { add(0) } } }
    val scale = listOf("Disagree", "Somewhat disagree", "Neutral", "Somewhat agree", "Agree")
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("This week's check") },
        text = {
            Column(Modifier.heightIn(max = 480.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("How much do you agree? There are no right answers.", fontSize = 13.sp)
                ExamFitness.WEEKLY_STATEMENTS.forEachIndexed { i, st ->
                    Column {
                        Text(st, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        FlowRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            scale.forEachIndexed { v, label ->
                                FilterChip(selected = answers[i] == v + 1, onClick = { answers[i] = v + 1 }, label = { Text(label, fontSize = 12.sp) })
                            }
                        }
                    }
                }
            }
        },
        confirmButton = { TextButton(onClick = { onDone(answers.toList()) }, enabled = answers.all { it > 0 }) { Text("Save") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Not now") } },
    )
}
