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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jahangirb.pmpprep.data.Domain
import com.jahangirb.pmpprep.data.Mastery
import com.jahangirb.pmpprep.data.PlanEngine
import com.jahangirb.pmpprep.data.QuestionReview
import com.jahangirb.pmpprep.data.QuestionPicker
import com.jahangirb.pmpprep.ui.Dest
import com.jahangirb.pmpprep.ui.PmpViewModel
import com.jahangirb.pmpprep.ui.components.PmpCard
import com.jahangirb.pmpprep.ui.components.ProgressRow
import com.jahangirb.pmpprep.ui.components.pct

/**
 * "How it works": a user guide to the app — what you get, how a day runs, what is measured and
 * how, and what the app does about weak spots. Where it helps, your own current numbers are shown.
 */
@Composable
fun GuideScreen(vm: PmpViewModel) {
    val rev by vm.revision.collectAsState()
    val now = System.currentTimeMillis()
    val x = vm.repo.extras
    val report = remember(rev) { vm.readiness.report(vm.store, now) }
    val states = remember(rev) { PlanEngine.taskStates(x, vm.store.attempts, now) }
    val weakApproach = remember(rev) { QuestionPicker.weakApproach(vm.store.attempts) }
    val r = x.readiness

    LazyColumn(Modifier.fillMaxSize().padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Spacer(Modifier.height(4.dp))
            Text("How PMP Prep works", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
            Text("What you get, how a study day runs, how the app measures you, and how it works on your weak spots. Tap a section to open it.",
                fontSize = 14.sp, lineHeight = 20.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        item {
            Section("What you get", open = true) {
                Bullets(
                    "A plan from today to your exam date that rebuilds itself from what you actually do: missed days, weak scores and extra practice all reshape it.",
                    "One Continue button. Each session walks you through warm-up cards, a lesson, practice, a short reflection and a debrief.",
                    "An honest readiness score out of 100, built from five kinds of evidence, with the next thing that would raise it most.",
                    "Weak spots found and worked on automatically, and checked again until they hold.",
                    "Reminders at the hours that actually get you studying, learned from your own habits.",
                    "Everything works offline and stays on this phone. Only the Gemini chat, which you choose to use, goes online.",
                )
            }
        }

        item {
            Section("How a study day runs") {
                Bullets(
                    "Open the app: the path shows today's session and why it was chosen.",
                    "Press Continue. Each step leads to the next; ✕ closes and keeps your place.",
                    "Short on time? The 15-minute version keeps the day counting.",
                    "Anything from the ☰ menu (drills, mocks, cards) counts too, and the path adjusts.",
                    "Finish the session: the debrief shows what went well and what to watch, and the plan updates at once.",
                )
            }
        }

        item {
            Section("Your readiness score", open = true) {
                Text("${report.score} / 100" + if (report.provisional) " (provisional)" else "", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
                if (report.provisional) Text("It firms up after ${r.gateItems} answers across ${r.gateTasks} tasks: " + report.gates.joinToString("; ") + ".", fontSize = 13.sp)
                Spacer(Modifier.height(8.dp))
                report.components.forEach { c ->
                    ProgressRow("${c.label} · ${"%.1f".format(c.points)} of ${c.weight}", c.score, c.figure)
                    Text(c.detail, fontSize = 12.sp, lineHeight = 17.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(8.dp))
                }
                Text("A part with no evidence yet scores zero; its points aren't handed to the others. The advice always points at the part with the most points still missing:",
                    fontSize = 13.sp, lineHeight = 18.sp)
                Text(report.nextAction, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                TextButton(onClick = { vm.navigate(Dest.ANALYTICS) }) { Text("See the full analytics") }
            }
        }

        item {
            Section("How each exam task is judged") {
                Text("The exam outline has ${x.tasks.size} tasks. Each one is judged on its last 15 answers:", fontSize = 13.sp)
                Spacer(Modifier.height(6.dp))
                val counts = states.groupingBy { it.mastery }.eachCount()
                Ladder(Mastery.NOT_STARTED, "Not started", "no answers yet", counts)
                Ladder(Mastery.LEARNING, "Learning", "fewer than ${x.rules.mastery.minAnswers} answers", counts)
                Ladder(Mastery.SHAKY, "Shaky", "under ${pct(x.rules.mastery.accuracy)} correct", counts)
                Ladder(Mastery.MASTERED, "Mastered", "${pct(x.rules.mastery.accuracy)} or better", counts)
                Ladder(Mastery.FADING, "Fading", "mastered, but untouched for ${x.rules.mastery.fadeDays}+ days", counts)
                Spacer(Modifier.height(6.dp))
                Text("A mastered task that you answer slower than ${PlanEngine.SLOW_SEC.toInt()} seconds a question also gets a paced drill: the exam allows about ${x.exam.paceTargetSec}.",
                    fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        item {
            Section("How the app works on weak spots") {
                Steps(
                    "Find" to "A task under ${pct(x.rules.mastery.accuracy)}, a session under 50%, a task fading, your weakest domain, untouched tasks, flashcards that keep slipping.",
                    "Rank" to "Exam weight × how much is missing. Fix-ups jump the queue; your weakest domain's work comes first.",
                    "Plan" to "A session under 50% gets a fix-up next (lesson, easier questions, reflect). A shaky task gets drills, a middling one a review two days later, a fading one a refresher.",
                    "Pick" to "Questions on that task: ones you haven't seen first, then ones you got wrong last time. If one approach (predictive, agile, hybrid) is clearly your weakest, it takes every other question.",
                    "Reflect" to "Tag why each miss happened. When most tagged misses on a task share a cause, its sessions change: the lesson first for knowledge gaps, scenarios for mindset, a reading tip for misread stems.",
                    "Prove" to "Back to ${pct(x.rules.mastery.accuracy)} and drills stop. A domain checkpoint at ${pct(PlanEngine.PASS)} closes the domain, and it's re-checked if your answers there later fall under ${pct(PlanEngine.RECHECK_BELOW)}.",
                )
                if (weakApproach != null) {
                    Spacer(Modifier.height(6.dp))
                    Text("Right now your practice leans toward ${weakApproach.title} questions: it's your weakest approach.", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        item {
            Section("Exam fitness: ready to sit it") {
                Bullets(
                    "Readiness asks whether you know the material. Exam fitness asks whether you can sit for four hours, read 180 long questions calmly, and keep preparing until the day.",
                    "Stamina (40%): your longest sitting, how much accuracy fades and pace drifts in the last quarter of long sittings, and whether you finish them.",
                    "Composure (35%): reading speed (about ${com.jahangirb.pmpprep.data.ExamFitness.TARGET_WPM.toInt()} words a minute leaves time to decide), freezes, rushing after a slow question, accuracy against the clock, and changing right answers to wrong.",
                    "Consistency (25%): regular days, hours kept, the trend in your weekly minutes, finishing sessions, and bouncing back after breaks.",
                    "It trains what it finds: a weekly stamina sitting that climbs 20 → 45 → 70 → 100 → 140 → 180 questions, reading drills, a gentle prompt after a long freeze in practice, pacing checkpoints, and lighter weeks when momentum dips.",
                    "Optional check-ins (one tap before a session or after a mock, and five statements once a week) show how feelings and study relate. It describes what you did; it isn't a diagnosis.",
                )
                TextButton(onClick = { vm.navigate(Dest.FITNESS) }) { Text("See your exam fitness") }
            }
        }

        item {
            Section("Weekly reports") {
                Bullets(
                    "Every week (Monday to Sunday) is kept as a report: readiness, accuracy, tasks, mocks, exam fitness, reading pace, longest sitting, minutes and days.",
                    "Each figure is compared with the week before, or with your first week, and marked better or worse. Trend lines show the weeks together.",
                    "The Sunday evening reminder opens it.",
                )
                TextButton(onClick = { vm.navigate(Dest.REPORTS) }) { Text("See your weekly reports") }
            }
        }

        item {
            Section("The other checks") {
                Bullets(
                    "Diagnostic: 25 questions taken cold (8 People, 10 Process, 7 Business). It sets which domain comes first until you have enough practice there.",
                    "Accuracy by domain: each domain is weighted as on the exam (${Domain.entries.joinToString(", ") { "${it.short} ${pct(x.domainWeights[it] ?: 0.0)}" }}), so a weak domain can't hide behind a strong one.",
                    "Checkpoints: 20 mixed questions per domain; ${pct(PlanEngine.PASS)} to pass.",
                    x.rules.mockBands.let { b -> "Mock exams: Above Target ${pct(b.above)}+, Target ${pct(b.target)} or more, Below Target ${pct(b.below)} or more, Needs Improvement under ${pct(b.below)}. Your last three count for the score, newest most." },
                    "Flashcards: spaced repetition. A card comes back just before you'd forget it; one forgotten ${x.srs.leechThreshold} times is a leech, and its task's lesson comes back as a review.",
                    "Credit per question: single choice is all or nothing; multiple response loses a point for each wrong pick; matching and fill-in give credit for each part right.",
                )
            }
        }

        item {
            Section("Tools while you study") {
                Bullets(
                    "How sure? When an answer is ready, tap Sure, Think so or Guess to check it. A right guess doesn't count toward mastery; a sure-but-wrong answer goes to the top of the mistake journal. Never asked in mocks; switch it off in Study settings.",
                    "Strike out and highlight, like the exam: long-press an option to cross it out; long-press words in the question and drag to highlight them. The results show whether crossing out first helps you.",
                    "Missed questions come back: after 1 day, then 3, 7 and 21, and they leave once you've got them right a week or more later. Up to ${QuestionReview.PER_DAY} a day join the warm-up.",
                    "Make a card: on a missed question, turn it into your own flashcard in My cards. Explain it back: write what you understood and Gemini says what you got right and missed (needs your key).",
                    "Reading comfort: text size, line spacing and a reading guide in Study settings. Focus mode for mocks: Do Not Disturb and the screen kept awake. A two-minute breathing reset is offered when your check-in is low.",
                    "Rest days: one missed day a week doesn't break your streak.",
                )
                TextButton(onClick = { vm.navigate(Dest.SETTINGS) }) { Text("Study settings") }
                TextButton(onClick = { vm.navigate(Dest.BOOK) }) { Text("Am I ready to book?") }
            }
        }

        item {
            Section("Reminders that learn") {
                Bullets(
                    "At first a few a day, at hours you said you study (and, if you allow Usage access, hours your phone is in use).",
                    "Each hour keeps a score from what happens next: opened, studied, ignored, or Not today.",
                    "After about 20 reminders it keeps to the hours that work, with an occasional try of another. Old results fade, so a new routine is picked up.",
                    "Nothing on rest days or once today's plan is done; fewer if several are ignored.",
                )
                TextButton(onClick = { vm.navigate(Dest.PLANNER) }) { Text("Reminder settings and your best hours") }
            }
        }

        item {
            Section("Your data") {
                Bullets(
                    "Progress, notes and reminder learning are stored on this phone.",
                    "Back it up or move it with Question banks and backup in the menu. The backup file opens in the web app and the iPhone app too.",
                    "Cloud sync (off until you set it up) keeps one copy of your progress and notes in your own Google Drive, through a small script you deploy in your Google account, and shares it with the web app and your other devices. Nothing goes to anyone else.",
                    "The Gemini chat sends your question and a summary of your progress to Google, only when you send a message, using your own key.",
                )
                TextButton(onClick = { vm.navigate(Dest.SYNC) }) { Text("Cloud sync") }
            }
        }
        item { Spacer(Modifier.height(24.dp)) }
    }
}

/** A clay card whose title opens and closes it. */
@Composable
private fun Section(title: String, open: Boolean = false, content: @Composable () -> Unit) {
    var expanded by rememberSaveable(title) { mutableStateOf(open) }
    PmpCard {
        Row(
            Modifier.fillMaxWidth().clickable(role = Role.Button, onClickLabel = if (expanded) "Close" else "Open") { expanded = !expanded },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(title, fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, modifier = Modifier.weight(1f))
            Icon(if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore, contentDescription = null)
        }
        if (expanded) {
            Spacer(Modifier.height(8.dp))
            Column { content() }
        }
    }
}

@Composable
private fun Bullets(vararg lines: String) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        lines.forEach { l ->
            Row {
                Text("•", fontSize = 14.sp, modifier = Modifier.width(14.dp))
                Text(l, fontSize = 14.sp, lineHeight = 20.sp)
            }
        }
    }
}

@Composable
private fun Steps(vararg steps: Pair<String, String>) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        steps.forEachIndexed { i, (name, text) ->
            Row {
                Text("${i + 1}", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary, modifier = Modifier.width(20.dp))
                Column {
                    Text(name, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text(text, fontSize = 13.sp, lineHeight = 19.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

@Composable
private fun Ladder(m: Mastery, name: String, rule: String, counts: Map<Mastery, Int>) {
    Row(Modifier.fillMaxWidth().padding(vertical = 2.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(name, fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(96.dp))
        Text(rule, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1f))
        Text("${counts[m] ?: 0}", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
    }
}
