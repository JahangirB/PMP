package com.jahangirb.pmpprep.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.AlertDialog
import com.jahangirb.pmpprep.ui.components.ClayButton as Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import com.jahangirb.pmpprep.ui.components.ClayOutlinedButton as OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jahangirb.pmpprep.data.PlanEngine
import com.jahangirb.pmpprep.data.Session
import com.jahangirb.pmpprep.data.SessionKind
import com.jahangirb.pmpprep.data.StudyPlan
import com.jahangirb.pmpprep.ui.Dest
import com.jahangirb.pmpprep.ui.PmpViewModel
import com.jahangirb.pmpprep.ui.components.PmpCard
import com.jahangirb.pmpprep.ui.components.SectionLabel
import com.jahangirb.pmpprep.ui.components.pct
import com.jahangirb.pmpprep.ui.theme.ColorSuccess
import com.jahangirb.pmpprep.ui.theme.ColorWarning
import java.text.DateFormat
import java.util.Calendar
import java.util.Date
import kotlin.math.roundToInt

/** Home. One path from today to the exam, one Continue button. */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PathScreen(vm: PmpViewModel) {
    val rev by vm.revision.collectAsState()
    if (rev >= 0 && vm.setupDue) {
        val w = vm.repo.extras.welcome
        if (!vm.welcomeRead && w != null) WelcomeScreen(vm) else Onboarding(vm)
        return
    }
    val now = System.currentTimeMillis()
    val plan = remember(rev, vm.contentVersion) { vm.plan(now) } ?: return
    val report = remember(rev) { vm.readiness.report(vm.store) }
    val streak = remember(rev) { vm.readiness.streak(vm.store, now) }
    val today = PlanEngine.startOfDay(now)
    val doneToday = vm.store.sessionResults.filter { PlanEngine.startOfDay(it.at) == today }
    val next = plan.next
    val resuming = vm.run
    var ahead by remember { mutableStateOf<Session?>(null) }
    var showChanges by remember { mutableStateOf(false) }
    val coachLine = remember(rev) { vm.coach().report(now).messages.firstOrNull() }
    val ctx = LocalContext.current
    val remindersAsked = remember(rev) { com.jahangirb.pmpprep.reminders.Reminders.load(ctx).settings.asked }
    val fit = remember(rev) { vm.fitness(now) }
    var weeklyOpen by remember { mutableStateOf(false) }

    LazyColumn(Modifier.fillMaxSize().padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text(if (plan.daysLeft > 0) "${plan.daysLeft} days to the exam" else "Exam day", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
                    Text("${plan.phase} · readiness ${report.score} · ${streak.current}-day streak", fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("Exam fitness ${fit.score?.let { "$it" } ?: "–"} · weekly report", fontSize = 13.sp, fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable { vm.navigate(com.jahangirb.pmpprep.ui.Dest.FITNESS) }.padding(vertical = 6.dp))
                }
            }
        }

        // Momentum falling: a lighter week of minimum days, not catch-up pressure.
        if (fit.momentumLow) item {
            PmpCard(container = MaterialTheme.colorScheme.secondaryContainer) {
                Text("A lighter week", fontWeight = FontWeight.Bold)
                Text("Your study time has dropped from recent weeks. That happens. For now, a minimum day counts: 10 cards or 10 questions. The rhythm matters more than the amount.",
                    fontSize = 13.sp, lineHeight = 18.sp)
                next?.let { n -> TextButton(onClick = { vm.startSession(vm.shortVersion(n)) }) { Text("Start a 15-minute version") } }
            }
        }
        if (vm.weeklyCheckDue(now)) item {
            PmpCard {
                Text("Weekly check · 30 seconds", fontWeight = FontWeight.Bold)
                Text("Five statements about how the exam feels. It shows whether worry eases as you prepare.", fontSize = 13.sp)
                TextButton(onClick = { weeklyOpen = true }) { Text("Take it") }
            }
        }

        // For anyone who finished setup before smart reminders existed: one prompt until it's answered.
        if (!remindersAsked) item {
            PmpCard {
                Text("Reminders that learn your rhythm", fontWeight = FontWeight.Bold)
                Text("Tell the app when you usually study. It tries a few times a day at first, then keeps to the hours that get you studying.",
                    fontSize = 13.sp, lineHeight = 18.sp)
                Spacer(Modifier.height(6.dp))
                Button(onClick = { vm.navigate(com.jahangirb.pmpprep.ui.Dest.PLANNER) }) { Text("Set up reminders") }
            }
        }

        if (plan.behind) item {
            PmpCard(container = ColorWarning.copy(alpha = 0.10f), border = ColorWarning) {
                Text("The plan doesn't fit the time left", fontWeight = FontWeight.Bold)
                Text("About ${plan.neededMinutes / 60} hours of work for ${plan.availableMinutes / 60} hours before the final week. Pick what to change:",
                    fontSize = 13.sp, lineHeight = 18.sp)
                Spacer(Modifier.height(6.dp))
                val p = vm.store.plan
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = { vm.store.setPlan(p.copy(hoursPerWeek = p.hoursPerWeek + 2)) }) { Text("+2 hours a week") }
                    if (p.studyDays.size < 7) OutlinedButton(onClick = {
                        vm.store.setPlan(p.copy(studyDays = p.studyDays + ((1..7).first { it !in p.studyDays })))
                    }) { Text("Add a study day") }
                    OutlinedButton(onClick = { vm.navigate(Dest.PLANNER) }) { Text("Move the date") }
                }
            }
        }

        item {
            PmpCard(container = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f), border = MaterialTheme.colorScheme.primary) {
                when {
                    resuming != null -> {
                        SectionLabel("In progress")
                        Text(resuming.session.title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text("Step ${resuming.step + 1} of ${resuming.session.steps.size}", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(Modifier.height(10.dp))
                        Button(onClick = { vm.startSession(resuming.session) }, modifier = Modifier.fillMaxWidth().height(52.dp)) {
                            Text("Resume", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }
                    next == null -> Text("Nothing left on the plan. Rest before the exam: you've done the work.", fontSize = 15.sp)
                    else -> {
                        val todayDone = doneToday.isNotEmpty() && next.day > today
                        SectionLabel(if (todayDone) "Today's done · next up" else if (next.day > today) "Next study day: ${dayLabel(next.day, today)}" else "Today")
                        Text(next.title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text(next.why, fontSize = 13.sp, lineHeight = 18.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("About ${next.minutes} minutes · ${stepSummary(next)}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(Modifier.height(10.dp))
                        Button(onClick = { vm.startSession(next) }, modifier = Modifier.fillMaxWidth().height(52.dp)) {
                            Text(if (todayDone || next.day > today) "Study ahead" else "Continue", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                        if (next.minutes > 20 && next.kind != SessionKind.MOCK) {
                            TextButton(onClick = { vm.startSession(vm.shortVersion(next)) }, modifier = Modifier.fillMaxWidth()) {
                                Text("Short on time? Do a 15-minute version")
                            }
                        }
                    }
                }
            }
        }

        coachLine?.let { m ->
            item {
                PmpCard(onClick = { vm.coachTab = 0; vm.navigate(Dest.COACH) }) {
                    Text("Coach", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Text(m.text, fontSize = 14.sp, lineHeight = 20.sp)
                }
            }
        }

        if (doneToday.isNotEmpty()) item {
            SectionLabel("Done today")
            doneToday.forEach { r ->
                Row(Modifier.padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = ColorSuccess, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(titleFor(vm, r.key), fontSize = 14.sp, modifier = Modifier.weight(1f))
                    if (r.score < 1.0) Text(pct(r.score), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        item { SectionLabel("Coming up") }
        val upcoming = plan.sessions.drop(1).filter { it.day < today + 15 * PlanEngine.DAY }.groupBy { it.day }
        upcoming.forEach { (day, list) ->
            item {
                Text(dayLabel(day, today), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                list.forEach { s ->
                    Row(Modifier.fillMaxWidth().clickable { ahead = s }.padding(vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.Circle, contentDescription = null, tint = MaterialTheme.colorScheme.outline, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(10.dp))
                        Text(s.title, fontSize = 15.sp, modifier = Modifier.weight(1f))
                        Text("${s.minutes} min", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
        val later = plan.sessions.drop(1).count { it.day >= today + 15 * PlanEngine.DAY }
        if (later > 0) item { Text("…and $later more sessions before the exam.", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant) }

        if (plan.adjustments.isNotEmpty() || plan.dropped.isNotEmpty()) item {
            PmpCard(onClick = { showChanges = !showChanges }) {
                Text("How your plan adjusted (${plan.adjustments.size})", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                if (showChanges) {
                    Spacer(Modifier.height(6.dp))
                    plan.adjustments.forEach { Text("• $it", fontSize = 13.sp, lineHeight = 18.sp) }
                    if (plan.dropped.isNotEmpty()) Text("Trimmed: " + plan.dropped.joinToString(", "), fontSize = 13.sp, lineHeight = 18.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                } else Text("Tap to see why the plan looks the way it does.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        item { Spacer(Modifier.height(16.dp)) }
    }

    ahead?.let { s ->
        AlertDialog(
            onDismissRequest = { ahead = null },
            title = { Text(s.title) },
            text = { Text(s.why + "\n\nThis is ahead of your plan. You can do it now; the plan adjusts.") },
            confirmButton = { TextButton(onClick = { ahead = null; vm.startSession(s) }) { Text("Start now") } },
            dismissButton = { TextButton(onClick = { ahead = null }) { Text("Not now") } },
        )
    }
    if (weeklyOpen) WeeklyCheckDialog(onDone = { vm.recordCheckIn("weekly", 0, it); weeklyOpen = false }, onDismiss = { weeklyOpen = false })
}

private fun dayLabel(day: Long, today: Long): String = when ((day - today + PlanEngine.DAY / 2) / PlanEngine.DAY) {
    0L -> "Today"
    1L -> "Tomorrow"
    else -> DateFormat.getDateInstance(DateFormat.FULL).format(Date(day)).substringBefore(",") + ", " +
        DateFormat.getDateInstance(DateFormat.MEDIUM).format(Date(day))
}

private fun stepSummary(s: Session): String = s.steps.mapNotNull {
    when (it.kind) {
        com.jahangirb.pmpprep.data.StepKind.CARDS -> "cards"
        com.jahangirb.pmpprep.data.StepKind.LESSON -> "lesson"
        com.jahangirb.pmpprep.data.StepKind.PRACTICE -> "${it.count} questions"
        com.jahangirb.pmpprep.data.StepKind.SCENARIOS -> "${it.count} scenarios"
        com.jahangirb.pmpprep.data.StepKind.MOCK -> "full mock"
        else -> null
    }
}.joinToString(" · ")

private fun titleFor(vm: PmpViewModel, key: String): String {
    val k = key.removePrefix("short:")
    val parts = k.split(":")
    val label = when (parts[0]) {
        "build", "drill", "review", "remediation" -> vm.repo.task(parts.getOrElse(1) { "" })?.label
        "checkpoint" -> "Checkpoint: " + parts.getOrElse(1) { "" }.replaceFirstChar { it.uppercase() }
        "lesson" -> vm.repo.extras.lessons.firstOrNull { it.id == parts.getOrElse(1) { "" } }?.title
        "mock" -> "Full mock exam"
        "reentry" -> "Welcome-back warm-up"
        "mixed" -> "Mixed timed practice"
        "taper" -> "Light review"
        else -> null
    } ?: k
    return (if (key.startsWith("short:")) "Short: " else "") + (if (parts[0] in setOf("drill", "review", "remediation")) parts[0].replaceFirstChar { it.uppercase() } + ": " else "") + label
}

/* ── first run ──────────────────────────────────────────────────────────── */

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Onboarding(vm: PmpViewModel) {
    var step by remember { mutableStateOf(0) }
    val rev by vm.revision.collectAsState()
    val p = if (rev >= 0) vm.store.plan else StudyPlan()
    val ctx = LocalContext.current
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Step ${step + 1} of 5", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        when (step) {
            0 -> {
                Text("Your PMP study, planned for you", fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, lineHeight = 32.sp)
                Text("Tell the app when your exam is and how much time you have. A short diagnostic shows where you stand. " +
                    "Then each day you press Continue and it walks you through lessons, practice, flashcards and mocks, adjusting to how you do.",
                    fontSize = 16.sp, lineHeight = 24.sp)
                Button(onClick = { step = 1 }, modifier = Modifier.fillMaxWidth().height(52.dp)) { Text("Get started", fontWeight = FontWeight.Bold) }
            }
            1 -> {
                Text("When is your exam?", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
                Text("A target date is fine if it isn't booked yet. You can change it later.", fontSize = 15.sp)
                val now = System.currentTimeMillis()
                Text(p.examDate?.let { DateFormat.getDateInstance(DateFormat.FULL).format(Date(it)) } ?: "No date yet", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                OutlinedButton(onClick = {
                    val c = Calendar.getInstance().apply { timeInMillis = p.examDate ?: (now + 60 * PlanEngine.DAY) }
                    DatePickerDialog(ctx, { _, y, m, d ->
                        vm.store.setPlan(vm.store.plan.copy(examDate = Calendar.getInstance().apply { set(y, m, d, 9, 0, 0) }.timeInMillis))
                    }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).apply { datePicker.minDate = now + PlanEngine.DAY }.show()
                }) { Text("Pick the date") }
                Button(onClick = { step = 2 }, enabled = p.examDate != null, modifier = Modifier.fillMaxWidth().height(52.dp)) { Text("Next", fontWeight = FontWeight.Bold) }
            }
            2 -> {
                Text("How much can you study?", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
                Text("An honest figure beats an ambitious one.", fontSize = 15.sp)
                Text("${p.hoursPerWeek} hours a week · about ${(p.hoursPerWeek * 60.0 / p.studyDays.size.coerceAtLeast(1)).roundToInt()} minutes a study day",
                    fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Slider(value = p.hoursPerWeek.toFloat(), onValueChange = { vm.store.setPlan(p.copy(hoursPerWeek = it.roundToInt())) }, valueRange = 2f..30f, steps = 27)
                Text("Which days?", fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                StudyDayChips(p) { vm.store.setPlan(it) }
                Button(onClick = { step = 3 }, enabled = p.studyDays.isNotEmpty(), modifier = Modifier.fillMaxWidth().height(52.dp)) { Text("Next", fontWeight = FontWeight.Bold) }
            }
            3 -> ReminderSetupStep { step = 4 }
            else -> {
                Text("Where do you stand?", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
                Text("25 questions across the three domains, about 30 minutes. Take it cold, without revising first: it measures you, not your last hour of reading. " +
                    "The plan starts with whatever comes out weakest.", fontSize = 15.sp, lineHeight = 22.sp)
                if (vm.store.diagnostic != null) Text("You've already taken it: ${pct(vm.store.diagnostic!!.pct)}.", fontWeight = FontWeight.Bold)
                Button(onClick = { vm.finishOnboarding(); vm.startDiagnostic() }, modifier = Modifier.fillMaxWidth().height(52.dp)) {
                    Text(if (vm.store.diagnostic != null) "Retake the diagnostic" else "Take the diagnostic", fontWeight = FontWeight.Bold)
                }
                TextButton(onClick = { vm.finishOnboarding() }, modifier = Modifier.fillMaxWidth()) {
                    Text(if (vm.store.diagnostic != null) "Keep my result and see the plan" else "Skip for now")
                }
            }
        }
        if (step > 0) TextButton(onClick = { step-- }) { Text("Back") }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StudyDayChips(p: StudyPlan, onChange: (StudyPlan) -> Unit) {
    val names = listOf(Calendar.MONDAY to "Mon", Calendar.TUESDAY to "Tue", Calendar.WEDNESDAY to "Wed", Calendar.THURSDAY to "Thu",
        Calendar.FRIDAY to "Fri", Calendar.SATURDAY to "Sat", Calendar.SUNDAY to "Sun")
    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        names.forEach { (d, n) ->
            FilterChip(selected = d in p.studyDays, onClick = {
                onChange(p.copy(studyDays = if (d in p.studyDays) p.studyDays - d else p.studyDays + d))
            }, label = { Text(n) })
        }
    }
}
