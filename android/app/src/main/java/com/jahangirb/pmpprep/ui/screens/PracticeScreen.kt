package com.jahangirb.pmpprep.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jahangirb.pmpprep.data.Approach
import com.jahangirb.pmpprep.data.Domain
import com.jahangirb.pmpprep.data.QType
import com.jahangirb.pmpprep.data.Scoring
import com.jahangirb.pmpprep.ui.PmpViewModel
import com.jahangirb.pmpprep.ui.PracticeFilter
import com.jahangirb.pmpprep.ui.QuizSession
import com.jahangirb.pmpprep.ui.components.Feedback
import com.jahangirb.pmpprep.ui.components.PacingBar
import com.jahangirb.pmpprep.ui.components.PmpCard
import com.jahangirb.pmpprep.ui.components.ProgressRow
import com.jahangirb.pmpprep.ui.components.QuestionBody
import com.jahangirb.pmpprep.ui.components.QuestionHeader
import com.jahangirb.pmpprep.ui.components.SectionLabel
import com.jahangirb.pmpprep.ui.components.Selector
import com.jahangirb.pmpprep.ui.components.domainColor
import com.jahangirb.pmpprep.ui.components.pct
import com.jahangirb.pmpprep.ui.theme.ColorError
import com.jahangirb.pmpprep.ui.theme.ColorSuccess
import com.jahangirb.pmpprep.ui.theme.ColorWarning
import kotlinx.coroutines.delay

@Composable
fun PracticeScreen(vm: PmpViewModel) {
    val s = vm.practice
    if (s == null) PracticeSetup(vm)
    else if (s.finished) QuizResults(vm, s, onAgain = { vm.practice = null })
    else QuizRunner(vm, s, onFinish = { s.finished = true })
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PracticeSetup(vm: PmpViewModel) {
    val rev by vm.revision.collectAsState()
    val f = vm.filter
    val cv = vm.contentVersion
    val matches = remember(f, rev, cv) { vm.matching(f).size }
    val x = vm.repo.extras
    val missed = remember(rev, cv) { vm.store.last.values.count { !it.correct } }
    val flagged = remember(rev, cv) { vm.store.flags.size }
    Column(Modifier.fillMaxSize()) {
    Column(
        Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("Build a drill", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
        Text("You see whether you were right, and why every option is right or wrong, after each answer.",
            fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

        // Quick starts first: the drills people run most need no form at all.
        SectionLabel("Quick start")
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = { vm.startPractice(PracticeFilter(count = 10), "Mixed practice") }) { Text("10 mixed") }
            OutlinedButton(onClick = { vm.startPractice(PracticeFilter(which = PracticeFilter.Which.NEW, count = 20), "New questions") }) { Text("20 new") }
            OutlinedButton(onClick = { vm.startPractice(PracticeFilter(which = PracticeFilter.Which.MISSED, count = 40), "Mistakes") },
                enabled = missed > 0) { Text("Redo $missed mistakes") }
            OutlinedButton(onClick = { vm.startPractice(PracticeFilter(which = PracticeFilter.Which.FLAGGED, count = 40), "Flagged") },
                enabled = flagged > 0) { Text("$flagged flagged") }
        }
        Spacer(Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            SectionLabel("Or choose exactly", Modifier.weight(1f))
            TextButton(onClick = { vm.filter = PracticeFilter() }) { Text("Reset filters") }
        }

        Selector("Domain", listOf<Pair<Domain?, String>>(null to "All domains") + Domain.entries.map { it to it.title }, f.domain,
            { vm.filter = f.copy(domain = it, task = if (it != null && x.tasks.firstOrNull { t -> t.id == f.task }?.domain != it) null else f.task) })
        Selector("ECO task", listOf<Pair<String?, String>>(null to "All tasks") +
            x.tasks.filter { f.domain == null || it.domain == f.domain }.map { it.id to "${it.code} · ${it.label}" }, f.task,
            { vm.filter = f.copy(task = it) })
        Selector("Question bank", listOf<Pair<String?, String>>(null to "All enabled banks") +
            vm.repo.packs.filter { vm.repo.isEnabled(it) && it.questions.isNotEmpty() }.map { it.id to "${it.name} (${it.questions.size})" }, f.pack,
            { vm.filter = f.copy(pack = it) })
        Selector("Question type", listOf<Pair<QType?, String>>(null to "Any type",
            QType.SINGLE to "Multiple choice", QType.MULTI to "Multiple response", QType.MATCHING to "Matching / drop-down",
            QType.HOTSPOT to "Hotspot", QType.FILLBLANK to "Fill in the blank"), f.type, { vm.filter = f.copy(type = it) })
        Selector("Approach", listOf<Pair<Approach?, String>>(null to "Any approach") + Approach.entries.map { it to it.title }, f.approach,
            { vm.filter = f.copy(approach = it) })
        Selector("Which questions", PracticeFilter.Which.entries.map { it to it.title }, f.which, { vm.filter = f.copy(which = it) })

        Text("How many", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurfaceVariant)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(5, 10, 20, 40, 60, 100).forEach { n ->
                FilterChip(selected = f.count == n, onClick = { vm.filter = f.copy(count = n) }, label = { Text("$n") })
            }
        }

        Spacer(Modifier.height(8.dp))
    }
    // The primary action stays in the thumb zone instead of below the fold.
    HorizontalDivider()
    Surface(color = MaterialTheme.colorScheme.surface) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(if (matches == 0) "No questions match" else "$matches match\nyou'll get ${minOf(matches, f.count)}",
                fontSize = 13.sp, lineHeight = 17.sp, color = if (matches == 0) ColorError else MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
            Button(onClick = { vm.startPractice() }, enabled = matches > 0, modifier = Modifier.height(52.dp)) {
                Text("Start practice", fontWeight = FontWeight.Bold)
            }
        }
    }
    }
}

@Composable
fun QuizRunner(vm: PmpViewModel, s: QuizSession, onFinish: () -> Unit) {
    val rev by vm.revision.collectAsState()
    val q = s.current
    val result = s.results[q.id]
    val revealed = result != null
    val task = vm.repo.task(q.ecoTask)
    var now by remember { mutableLongStateOf(System.currentTimeMillis()) }
    var noteOpen by remember { mutableStateOf(false) }
    val scroll = rememberScrollState()

    LaunchedEffect(q.id, revealed) {
        scroll.scrollTo(0)
        while (!revealed && !s.reviewOnly) { now = System.currentTimeMillis(); delay(1000) }
    }

    Column(Modifier.fillMaxSize()) {
        Surface(color = MaterialTheme.colorScheme.surface) {
            Column(Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Question ${s.index + 1} of ${s.questions.size}", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                    if (!s.reviewOnly && !s.diagnostic) Text("${s.correctCount}/${s.checkedCount} right", fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    if (s.diagnostic) Text("Diagnostic · no feedback until the end", fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)
                    if (!s.diagnostic) IconButton(onClick = { vm.askAbout(q, s) }) {
                        Icon(Icons.Default.Psychology, contentDescription = "Ask the coach about this question", tint = MaterialTheme.colorScheme.primary)
                    }
                    IconButton(onClick = { noteOpen = true }) {
                        Icon(Icons.Default.EditNote, contentDescription = "Note",
                            tint = if (vm.store.notes.containsKey(q.id)) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    val flagged = rev >= 0 && q.id in vm.store.flags
                    IconButton(onClick = { vm.store.toggleFlag(q.id) }) {
                        Icon(if (flagged) Icons.Filled.Flag else Icons.Outlined.Flag, contentDescription = "Flag",
                            tint = if (flagged) ColorWarning else MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                LinearProgressIndicator(progress = { (s.index + 1f) / s.questions.size }, modifier = Modifier.fillMaxWidth())
            }
        }

        Column(Modifier.weight(1f).verticalScroll(scroll).padding(16.dp)) {
            if (!revealed && !s.reviewOnly) {
                PacingBar(((now - s.shownAt) / 1000).coerceAtLeast(0), vm.repo.extras.practicePaceSec)
                Spacer(Modifier.height(12.dp))
            }
            QuestionHeader(q, task?.let { "${it.code} · ${it.label}" })
            QuestionBody(q, s.order[q.id] ?: q.options, s.answers[q.id], revealed) { a -> vm.answer(s, q, a) }
            if (result != null) {
                Spacer(Modifier.height(14.dp))
                Feedback(q, result, Scoring.isAnswered(q, s.answers[q.id]))
                vm.store.notes[q.id]?.let {
                    Spacer(Modifier.height(10.dp))
                    PmpCard { SectionLabel("Your note"); Spacer(Modifier.height(4.dp)); Text(it, fontSize = 14.sp) }
                }
                val hist = vm.store.attempts.filter { it.qid == q.id }
                if (hist.isNotEmpty()) {
                    Spacer(Modifier.height(8.dp))
                    Text("Answered ${hist.size} time(s), ${hist.count { it.correct }} right · ${q.id}", fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Spacer(Modifier.height(24.dp))
        }

        HorizontalDivider()
        Row(Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = { vm.moveTo(s, s.index - 1) }, enabled = s.index > 0, modifier = Modifier.weight(1f)) { Text("Back") }
            val last = s.index == s.questions.size - 1
            if (s.diagnostic) {
                Button(onClick = { if (last) vm.finishDiagnostic(s) else vm.moveTo(s, s.index + 1) }, modifier = Modifier.weight(1.6f)) {
                    Text(if (last) "Finish diagnostic" else if (Scoring.isAnswered(q, s.answers[q.id])) "Next" else "Skip", fontWeight = FontWeight.Bold)
                }
            } else when {
                revealed -> Button(onClick = { if (last) onFinish() else vm.moveTo(s, s.index + 1) }, modifier = Modifier.weight(1.6f)) {
                    Text(if (last) "See results" else "Next", fontWeight = FontWeight.Bold)
                }
                else -> {
                    if (!last) TextButton(onClick = { vm.moveTo(s, s.index + 1) }, modifier = Modifier.weight(0.8f)) { Text("Skip") }
                    Button(onClick = { vm.check(s) }, enabled = Scoring.isAnswered(q, s.answers[q.id]), modifier = Modifier.weight(1.6f)) {
                        Text("Check answer", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    if (noteOpen) {
        var text by remember { mutableStateOf(vm.store.notes[q.id].orEmpty()) }
        AlertDialog(
            onDismissRequest = { noteOpen = false },
            title = { Text("Note on this question") },
            text = { OutlinedTextField(value = text, onValueChange = { text = it }, minLines = 3, modifier = Modifier.fillMaxWidth()) },
            confirmButton = { TextButton(onClick = { vm.store.setNote(q.id, text); noteOpen = false }) { Text("Save") } },
            dismissButton = { TextButton(onClick = { noteOpen = false }) { Text("Cancel") } },
        )
    }
}

@Composable
fun QuizResults(vm: PmpViewModel, s: QuizSession, onAgain: () -> Unit) {
    if (s.diagnostic) { DiagnosticResults(vm, s); return }
    val checked = s.questions.filter { s.isChecked(it) }
    val right = checked.count { s.results[it.id]?.correct == true }
    val score = if (checked.isEmpty()) 0.0 else right.toDouble() / checked.size
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        PmpCard {
            Text(pct(score), fontSize = 44.sp, fontWeight = FontWeight.Black,
                color = if (score >= 0.7) ColorSuccess else if (score >= 0.6) ColorWarning else ColorError)
            Text("$right of ${checked.size} checked answers correct", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        PmpCard {
            SectionLabel("By domain")
            Spacer(Modifier.height(8.dp))
            Domain.entries.forEach { d ->
                val inD = checked.filter { it.domain == d }
                if (inD.isNotEmpty()) {
                    val r = inD.count { s.results[it.id]?.correct == true }
                    ProgressRow(d.title, r.toDouble() / inD.size, "$r/${inD.size}", domainColor(d))
                    Spacer(Modifier.height(10.dp))
                }
            }
        }
        val wrong = checked.filter { s.results[it.id]?.correct != true }
        if (wrong.isNotEmpty()) {
            Button(onClick = { vm.startPracticeWith(wrong, "Retry the ones I missed") }, modifier = Modifier.fillMaxWidth()) {
                Text("Retry the ${wrong.size} I missed")
            }
        }
        OutlinedButton(onClick = onAgain, modifier = Modifier.fillMaxWidth()) { Text("New drill") }
        Spacer(Modifier.width(1.dp))
    }
}


@Composable
private fun DiagnosticResults(vm: PmpViewModel, s: QuizSession) {
    val d = vm.store.diagnostic ?: return
    var reviewing by remember { mutableStateOf(false) }
    if (reviewing) {
        val review = remember { QuizSession("Diagnostic review", s.questions, reviewOnly = true, givenAnswers = s.answers.toMap(), givenOrder = s.order) }
        QuizRunner(vm, review, onFinish = { reviewing = false })
        return
    }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Your starting point", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
        PmpCard {
            Text(pct(d.pct), fontSize = 44.sp, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
            Text("${d.right} of ${d.n} right, taken cold.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        PmpCard {
            SectionLabel("By domain")
            Spacer(Modifier.height(8.dp))
            Domain.entries.forEach { dm ->
                d.domains[dm]?.let { (r, of) ->
                    ProgressRow(dm.title, if (of == 0) 0.0 else r.toDouble() / of, "$r/$of", domainColor(dm))
                    Spacer(Modifier.height(10.dp))
                }
            }
            val weakest = Domain.entries.minByOrNull { d.domainPct(it) ?: 1.0 }
            if (weakest != null) Text("Your plan will give ${weakest.title} the most practice time.", fontSize = 13.sp)
        }
        Button(onClick = { vm.practice = null; vm.home() }, modifier = Modifier.fillMaxWidth().height(50.dp)) {
            Text("See my study path", fontWeight = FontWeight.Bold)
        }
        OutlinedButton(onClick = { reviewing = true }, modifier = Modifier.fillMaxWidth()) { Text("Review the answers") }
        OutlinedButton(onClick = { vm.practice = null; vm.coachTab = 0; vm.navigate(com.jahangirb.pmpprep.ui.Dest.COACH) }, modifier = Modifier.fillMaxWidth()) { Text("See what the coach suggests") }
    }
}
