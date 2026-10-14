package com.jahangirb.pmpprep.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Circle
import com.jahangirb.pmpprep.ui.components.ClayButton as Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import com.jahangirb.pmpprep.ui.components.ClayOutlinedButton as OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jahangirb.pmpprep.data.Answer
import com.jahangirb.pmpprep.data.Lesson
import com.jahangirb.pmpprep.data.Scoring
import com.jahangirb.pmpprep.ui.PmpViewModel
import com.jahangirb.pmpprep.ui.components.BulletList
import com.jahangirb.pmpprep.ui.components.Feedback
import com.jahangirb.pmpprep.ui.components.MindsetCallout
import com.jahangirb.pmpprep.ui.components.PmpCard
import com.jahangirb.pmpprep.ui.components.QuestionBody
import com.jahangirb.pmpprep.ui.components.SectionLabel
import com.jahangirb.pmpprep.ui.components.domainColor
import com.jahangirb.pmpprep.ui.theme.ColorSuccess

@Composable
fun LearnScreen(vm: PmpViewModel) {
    val open = vm.openLesson?.let { id -> vm.repo.extras.lessons.firstOrNull { it.id == id } }
    if (open != null) LessonView(vm, open) else LessonList(vm)
}

@Composable
private fun LessonList(vm: PmpViewModel) {
    val rev by vm.revision.collectAsState()
    val x = vm.repo.extras
    LazyColumn(Modifier.fillMaxSize().padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item {
            Spacer(Modifier.height(8.dp))
            Text("${vm.store.lessonsDone.size} of ${x.lessons.size} lessons done", fontWeight = FontWeight.Bold)
            Text("Read a lesson, try the worked example and the quick checks, then practise the task.",
                fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        x.modules.sortedBy { it.order }.forEach { m ->
            val lessons = x.lessons.filter { it.module == m.id }.sortedBy { it.order }
            if (lessons.isEmpty()) return@forEach
            item {
                Spacer(Modifier.height(6.dp))
                SectionLabel("${m.label} · ${lessons.count { it.id in vm.store.lessonsDone }}/${lessons.size}")
                Text(m.blurb, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            items(lessons, key = { it.id }) { l ->
                val done = rev >= 0 && l.id in vm.store.lessonsDone
                PmpCard(onClick = { vm.openLesson = l.id }) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(if (done) Icons.Filled.CheckCircle else Icons.Outlined.Circle, contentDescription = null,
                            tint = if (done) ColorSuccess else domainColor(m.domain))
                        Spacer(Modifier.width(10.dp))
                        Column(Modifier.weight(1f)) {
                            Text("${l.id} · ${l.title}", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                            Text("${l.minutes} min · ${l.checks.size} checks" + (vm.repo.task(l.ecoTask)?.let { " · ${it.label}" } ?: ""),
                                fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }
        item { Spacer(Modifier.height(16.dp)) }
    }
}

/** inSession: shown inside a guided-path session, which handles marking done and moving on. */
@Composable
fun LessonView(vm: PmpViewModel, l: Lesson, inSession: Boolean = false) {
    val rev by vm.revision.collectAsState()
    val scroll = rememberScrollState()
    LaunchedEffect(l.id) { scroll.scrollTo(0) }
    var workedPick by remember(l.id) { mutableStateOf<String?>(null) }
    var workedShown by remember(l.id) { mutableStateOf(false) }
    val checkAnswers = remember(l.id) { mutableStateMapOf<String, Answer>() }
    val checked = remember(l.id) { mutableStateMapOf<String, Boolean>() }

    Column(Modifier.fillMaxSize().verticalScroll(scroll).padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text(l.title, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, lineHeight = 28.sp)
        Text("${l.id} · ${l.minutes} minutes" + (vm.repo.task(l.ecoTask)?.let { " · ${it.code} ${it.label}" } ?: ""),
            fontSize = 12.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
        if (l.objectives.isNotEmpty()) PmpCard { SectionLabel("You will be able to"); Spacer(Modifier.height(6.dp)); BulletList(l.objectives) }
        l.sections.forEach { s ->
            Column {
                Text(s.heading, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(6.dp))
                Text(s.body, fontSize = 15.sp, lineHeight = 23.sp)
            }
        }
        if (l.lenses.isNotEmpty()) {
            PmpCard {
                SectionLabel("Across delivery approaches")
                l.lenses.forEach { (k, v) ->
                    Spacer(Modifier.height(8.dp))
                    Text(k.replaceFirstChar { it.uppercase() }, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(v, fontSize = 14.sp, lineHeight = 20.sp)
                }
            }
        }
        l.worked?.let { w ->
            SectionLabel("Worked example")
            ScenarioBlock(vm, w, workedShown, workedPick) { workedPick = it }
            if (!workedShown) Button(onClick = { workedShown = true }, enabled = workedPick != null, modifier = Modifier.fillMaxWidth()) { Text("Show the ranking") }
        }
        if (l.traps.isNotEmpty()) PmpCard { SectionLabel("Traps"); Spacer(Modifier.height(6.dp)); BulletList(l.traps, bullet = "⚠") }
        if (l.checks.isNotEmpty()) {
            SectionLabel("Quick checks")
            Text("These don't count towards readiness: they are for learning.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            l.checks.forEachIndexed { i, q ->
                PmpCard {
                    Text("${i + 1}. ${q.stem}", fontSize = 15.sp, lineHeight = 22.sp)
                    Spacer(Modifier.height(10.dp))
                    val shown = checked[q.id] == true
                    QuestionBody(q, q.options, checkAnswers[q.id], shown) { a -> if (!shown) checkAnswers[q.id] = a }
                    Spacer(Modifier.height(10.dp))
                    if (!shown) {
                        OutlinedButton(onClick = {
                            checked[q.id] = true
                            vm.recordLessonCheck(q, checkAnswers[q.id] ?: Answer())
                        }, enabled = Scoring.isAnswered(q, checkAnswers[q.id])) { Text("Check") }
                    } else {
                        Feedback(q, Scoring.score(q, checkAnswers[q.id]), true)
                    }
                }
            }
        }
        if (l.terms.isNotEmpty()) PmpCard { SectionLabel("Key terms"); Spacer(Modifier.height(6.dp)); Text(l.terms.joinToString(" · "), fontSize = 14.sp) }
        if (l.recap.isNotEmpty()) PmpCard { SectionLabel("Recap"); Spacer(Modifier.height(6.dp)); BulletList(l.recap) }
        MindsetCallout(l.mindset)
        val done = rev >= 0 && l.id in vm.store.lessonsDone
        if (!inSession) Button(onClick = { vm.store.setLessonDone(l.id, !done) }, modifier = Modifier.fillMaxWidth()) {
            Text(if (done) "Mark as not done" else "Mark lesson done", fontWeight = FontWeight.Bold)
        }
        if (!inSession && l.ecoTask.isNotBlank()) {
            OutlinedButton(onClick = {
                vm.startPractice(com.jahangirb.pmpprep.ui.PracticeFilter(task = l.ecoTask, count = 10), "Practice: ${l.title}")
            }, modifier = Modifier.fillMaxWidth()) { Text("Practise this task (10 questions)") }
        }
        Spacer(Modifier.height(16.dp))
    }
}
