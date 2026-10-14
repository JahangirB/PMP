package com.jahangirb.pmpprep.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jahangirb.pmpprep.data.RootCause
import com.jahangirb.pmpprep.data.StepKind
import com.jahangirb.pmpprep.ui.Dest
import com.jahangirb.pmpprep.ui.PmpViewModel
import com.jahangirb.pmpprep.ui.components.PmpCard
import com.jahangirb.pmpprep.ui.components.SectionLabel
import com.jahangirb.pmpprep.ui.components.pct
import com.jahangirb.pmpprep.ui.theme.ColorError
import com.jahangirb.pmpprep.ui.theme.ColorSuccess
import com.jahangirb.pmpprep.ui.theme.ColorWarning

/** Plays one guided-path session: one step at a time, one button forward. */
@Composable
fun SessionScreen(vm: PmpViewModel) {
    val r = vm.run ?: run { LaunchedEffect(Unit) { vm.back() }; return }
    val s = r.session
    val st = s.steps.getOrNull(r.step)
    LaunchedEffect(r, r.step) { vm.prepareStep(r) }

    Column(Modifier.fillMaxSize()) {
        Surface(color = MaterialTheme.colorScheme.surface) {
            Column(Modifier.padding(start = 4.dp, end = 16.dp, top = 4.dp, bottom = 6.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { vm.closeSession() }) { Icon(Icons.Default.Close, contentDescription = "Close; your place is kept") }
                    Column(Modifier.weight(1f)) {
                        Text(s.title, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Text(stepName(st?.kind) + " · step ${(r.step + 1).coerceAtMost(s.steps.size)} of ${s.steps.size}", fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                LinearProgressIndicator(progress = { (r.step + 1f) / s.steps.size }, modifier = Modifier.fillMaxWidth().padding(start = 12.dp))
            }
        }
        Box(Modifier.weight(1f)) {
            when (st?.kind) {
                StepKind.CARDS -> if (vm.cardQueue != null) CardSession(vm) { vm.nextStep(r) }
                StepKind.LESSON -> LessonStep(vm, r)
                StepKind.PRACTICE -> r.current?.let { q -> QuizRunner(vm, q, onFinish = { vm.nextStep(r) }) }
                StepKind.REFLECT -> ReflectStep(vm, r)
                StepKind.SCENARIOS -> ScenarioStep(vm, r, st.count)
                StepKind.MOCK -> MockStep(vm, r)
                StepKind.DEBRIEF, null -> Debrief(vm, r)
            }
        }
    }
}

private fun stepName(k: StepKind?) = when (k) {
    StepKind.CARDS -> "Warm-up cards"
    StepKind.LESSON -> "Lesson"
    StepKind.PRACTICE -> "Practice"
    StepKind.REFLECT -> "Reflect"
    StepKind.SCENARIOS -> "Scenarios"
    StepKind.MOCK -> "Mock exam"
    StepKind.DEBRIEF, null -> "Debrief"
}

@Composable
private fun NextBar(label: String, enabled: Boolean = true, onClick: () -> Unit) {
    HorizontalDivider()
    Surface(color = MaterialTheme.colorScheme.surface) {
        Button(onClick = onClick, enabled = enabled, modifier = Modifier.fillMaxWidth().padding(12.dp).height(52.dp)) {
            Text(label, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

@Composable
private fun LessonStep(vm: PmpViewModel, r: PmpViewModel.PathRun) {
    val l = vm.repo.extras.lessons.firstOrNull { it.id == r.session.lessonId } ?: run { LaunchedEffect(r.step) { vm.nextStep(r) }; return }
    Column(Modifier.fillMaxSize()) {
        Box(Modifier.weight(1f)) { LessonView(vm, l, inSession = true) }
        NextBar("I've read it · Continue") { vm.nextStep(r) }
    }
}

/** Tag why each miss in this session happened, while it is fresh. */
@OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable
private fun ReflectStep(vm: PmpViewModel, r: PmpViewModel.PathRun) {
    val rev by vm.revision.collectAsState()
    val wrong = r.practices.flatMap { p -> p.questions.filter { p.results[it.id]?.correct == false } }
    if (wrong.isEmpty()) { LaunchedEffect(r.step) { vm.nextStep(r) }; return }
    Column(Modifier.fillMaxSize()) {
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("Why did these go wrong?", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
            Text("One tap each. Naming the cause is what stops it happening again, and the plan uses it.", fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            wrong.forEach { q ->
                PmpCard {
                    Text(q.stem, fontSize = 13.sp, maxLines = 3, overflow = TextOverflow.Ellipsis)
                    Spacer(Modifier.height(6.dp))
                    val tag = if (rev >= 0) vm.store.mistakeTags[q.id]?.cause else null
                    androidx.compose.foundation.layout.FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        RootCause.entries.forEach { c ->
                            FilterChip(selected = tag == c, onClick = { vm.store.tagMistake(q.id, c, "") }, label = { Text(c.title, fontSize = 12.sp) })
                        }
                    }
                }
            }
        }
        NextBar("Continue") { vm.nextStep(r) }
    }
}

@Composable
private fun ScenarioStep(vm: PmpViewModel, r: PmpViewModel.PathRun, count: Int) {
    val list = remember(r) { vm.repo.extras.scenarios.shuffled().take(count.coerceAtLeast(1)) }
    var i by remember(r) { mutableIntStateOf(0) }
    var pick by remember(r, i) { mutableStateOf<String?>(null) }
    var shown by remember(r, i) { mutableStateOf(false) }
    val sc = list.getOrNull(i) ?: run { LaunchedEffect(r.step) { vm.nextStep(r) }; return }
    Column(Modifier.fillMaxSize()) {
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Scenario ${i + 1} of ${list.size}", fontWeight = FontWeight.Bold)
            ScenarioBlock(vm, sc, shown, pick) { pick = it }
        }
        when {
            !shown -> NextBar("Reveal the ranking", enabled = pick != null) {
                vm.scenarioPick = pick
                vm.submitScenario(sc)
                shown = true
            }
            i < list.size - 1 -> NextBar("Next scenario") { i++ }
            else -> NextBar("Continue") { vm.nextStep(r) }
        }
    }
}

@Composable
private fun MockStep(vm: PmpViewModel, r: PmpViewModel.PathRun) {
    val rev by vm.revision.collectAsState()
    val done = rev >= 0 && vm.store.mocks.size > r.mocksAtStart
    Column(Modifier.fillMaxSize()) {
        Column(Modifier.weight(1f).padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            if (done) {
                val m = vm.store.mocks.last()
                Text("Mock done: ${pct(m.pct)}", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
                Text("${m.right} of ${m.n} correct. Your plan adjusts to the domain scores.", fontSize = 15.sp)
            } else {
                val x = vm.repo.extras.exam
                Text("Full mock exam", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
                Text("${x.totalItems} questions in ${x.minutes} minutes, with optional breaks. Find a quiet place and the whole sitting. " +
                    "You can pause and come back; the clock stops while you're away.", fontSize = 15.sp, lineHeight = 22.sp)
            }
        }
        if (done) NextBar("Continue") { vm.nextStep(r) }
        else NextBar(if (vm.mock != null) "Resume the mock" else "Start the mock") {
            if (vm.mock == null) vm.startMock(vm.repo.extras.exam.totalItems)
            vm.navigate(Dest.MOCK)
        }
    }
}

@Composable
private fun Debrief(vm: PmpViewModel, r: PmpViewModel.PathRun) {
    val score = vm.sessionScore(r)
    val answered = r.practices.flatMap { p -> p.questions.filter { p.isChecked(it) } }
    val next = remember { vm.plan()?.sessions?.firstOrNull { it.key != r.session.key } }
    val color = when {
        answered.isEmpty() -> MaterialTheme.colorScheme.primary
        score >= 0.8 -> ColorSuccess
        score >= 0.5 -> ColorWarning
        else -> ColorError
    }
    Column(Modifier.fillMaxSize()) {
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Session done", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
            if (answered.isNotEmpty()) {
                PmpCard {
                    Text(pct(score), fontSize = 40.sp, fontWeight = FontWeight.Black, color = color)
                    Text("${answered.count { q -> r.practices.any { it.results[q.id]?.correct == true } }} of ${answered.size} right", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                // Went well / watch, by task, from this session only.
                val byTask = answered.groupBy { it.ecoTask }.mapValues { (_, qs) ->
                    qs.sumOf { q -> r.practices.firstNotNullOfOrNull { it.results[q.id] }?.credit ?: 0.0 } / qs.size
                }
                val best = byTask.maxByOrNull { it.value }
                val worst = byTask.minByOrNull { it.value }
                PmpCard {
                    best?.let { (t, v) -> Text("Went well: ${vm.repo.task(t)?.label ?: t}, ${pct(v)}", fontSize = 14.sp) }
                    if (worst != null && worst.value < 0.8) {
                        Text("Watch: ${vm.repo.task(worst.key)?.label ?: worst.key}, ${pct(worst.value)}" +
                            if (worst.value < 0.5) ". Tomorrow starts with a fix-up on it." else ". It comes back for review in a couple of days.",
                            fontSize = 14.sp)
                    }
                }
            } else {
                Text("Nicely done. That's on your record.", fontSize = 15.sp)
            }
            next?.let {
                PmpCard {
                    SectionLabel("Next on your path")
                    Text(it.title, fontWeight = FontWeight.Bold)
                    Text(it.why, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
        NextBar("Finish") { vm.finishSession(r) }
    }
}
