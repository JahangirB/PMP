package com.jahangirb.pmpprep.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jahangirb.pmpprep.data.Domain
import com.jahangirb.pmpprep.ui.Dest
import com.jahangirb.pmpprep.ui.PmpViewModel
import com.jahangirb.pmpprep.ui.components.ActionCard
import com.jahangirb.pmpprep.ui.components.PmpCard
import com.jahangirb.pmpprep.ui.components.ProgressRow
import com.jahangirb.pmpprep.ui.components.ReadinessMeterCard
import com.jahangirb.pmpprep.ui.components.SectionLabel
import com.jahangirb.pmpprep.ui.components.StatTile
import com.jahangirb.pmpprep.ui.components.clock
import com.jahangirb.pmpprep.ui.components.domainColor
import com.jahangirb.pmpprep.ui.components.pct
import com.jahangirb.pmpprep.ui.theme.PmpGoldTertiary

@Composable
fun DashboardScreen(vm: PmpViewModel) {
    val rev by vm.revision.collectAsState()
    val cv = vm.contentVersion
    val report = remember(rev, cv) { vm.readiness.report(vm.store) }
    val coach = remember(rev, cv) { vm.coach().report() }
    val streak = remember(rev) { vm.readiness.streak(vm.store, System.currentTimeMillis()) }
    val pool = remember(cv) { vm.pool() }
    val cards = remember(rev) { vm.deckCounts(null) }
    val byDomain = remember(rev) { vm.readiness.group(vm.store.attempts) { it.domain.id } }
    val missed = remember(rev, cv) { pool.count { vm.store.last[it.id]?.correct == false } }
    val x = vm.repo.extras
    val status = when {
        report.score >= 75 -> "On course for the exam"
        report.score >= 60 -> "Building well"
        report.score >= 40 -> "Foundations forming"
        else -> "Just getting started"
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Spacer(Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("Today", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
                    Text(x.ecoLabel.ifBlank { "Examination Content Outline" }, fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                }
                Surface(
                    color = PmpGoldTertiary.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, PmpGoldTertiary.copy(alpha = 0.4f)),
                ) {
                    Row(Modifier.padding(horizontal = 10.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocalFireDepartment, contentDescription = null, tint = PmpGoldTertiary, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(4.dp))
                        Text(if (streak.current == 1) "1 day" else "${streak.current} days", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PmpGoldTertiary)
                    }
                }
            }
        }

        vm.mock?.let { m ->
            item {
                PmpCard(container = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.6f), border = MaterialTheme.colorScheme.tertiary) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Timer, contentDescription = null, tint = MaterialTheme.colorScheme.tertiary)
                        Spacer(Modifier.width(8.dp))
                        Column(Modifier.weight(1f)) {
                            Text("Mock exam in progress", fontWeight = FontWeight.Bold)
                            Text("${m.answeredCount} of ${m.questions.size} answered · ${clock(m.remainingMs)} left", fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Button(onClick = { vm.navigate(Dest.MOCK) }) { Text("Resume") }
                    }
                }
            }
        }

        // One primary action per screen: the coach's next step, at the top, as the
        // only filled button above the fold. The rest of the session sits under it.
        val steps = coach.steps.filter { !it.done }
        steps.firstOrNull()?.let { first ->
            item {
                PmpCard(container = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f), border = MaterialTheme.colorScheme.primary) {
                    SectionLabel(if (coach.steps.size > steps.size) "Continue today's session · ${coach.steps.size - steps.size}/${coach.steps.size} done" else "Today's session")
                    Spacer(Modifier.height(6.dp))
                    Text(first.title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text(first.why, fontSize = 13.sp, lineHeight = 18.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(10.dp))
                    Button(onClick = { vm.coachGo(first.action, first.arg) }, modifier = Modifier.fillMaxWidth().height(52.dp)) {
                        Text("Start", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                    if (steps.size > 1) {
                        Spacer(Modifier.height(6.dp))
                        steps.drop(1).take(3).forEach { st ->
                            Row(Modifier.fillMaxWidth().clickable { vm.coachGo(st.action, st.arg) }.padding(vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                                Text("Then: ${st.title}", fontSize = 14.sp, modifier = Modifier.weight(1f))
                                Text("›", fontSize = 20.sp, color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }
            }
        }

        item {
            ReadinessMeterCard(report.score, status, report.gates, report.nextAction) {
                vm.navigate(
                    when (report.nextRoute) {
                        "mock" -> Dest.MOCK
                        "flashcards" -> Dest.FLASHCARDS
                        else -> Dest.PRACTICE
                    }
                )
            }
        }

        item {
            val total = vm.store.last.size
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                StatTile("Answered", "$total", "${vm.store.last.values.count { it.correct }} right last time", Modifier.weight(1f)) { vm.navigate(Dest.ANALYTICS) }
                StatTile("Cards due", "${cards.due}", "${cards.new} new", Modifier.weight(1f)) { vm.switchTab(Dest.FLASHCARDS) }
                StatTile("Mistakes", "$missed", "to review", Modifier.weight(1f)) { vm.navigate(Dest.MISTAKES) }
            }
        }

        item {
            SectionLabel("More ways to study")
            Spacer(Modifier.height(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                ActionCard("Mock Exam Simulator", "${x.exam.totalItems} questions, ${x.exam.minutes} minutes, optional ${x.exam.breakMinutes}-minute breaks, real scoring.",
                    Icons.Default.Assignment, "Timed") { vm.navigate(Dest.MOCK) }
                ActionCard("Situational Trainer", "${x.scenarios.size} dilemmas where every option is tempting: ranked Best, Acceptable, Premature, Wrong.",
                    Icons.Default.Psychology, "Mindset") { vm.navigate(Dest.SITUATIONAL) }
                ActionCard("Formula Lab", "EVM, PERT, PTA, EMV, finance and agile calculators with the working shown.",
                    Icons.Default.Calculate, "${x.formulas.sumOf { it.items.size }} formulas") { vm.navigate(Dest.FORMULAS) }
            }
        }

        item {
            SectionLabel("By domain")
            Spacer(Modifier.height(8.dp))
            PmpCard {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Domain.entries.forEach { d ->
                        val g = byDomain[d.id]
                        val inPool = pool.count { it.domain == d }
                        ProgressRow(
                            "${d.title} (${pct(x.domainWeights[d] ?: 0.0)} of the exam)",
                            g?.accuracy ?: 0.0,
                            if (g == null) "0/$inPool" else "${pct(g.accuracy)} · ${g.n}",
                            domainColor(d),
                        )
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}
