package com.jahangirb.pmpprep.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jahangirb.pmpprep.data.Domain
import com.jahangirb.pmpprep.data.Srs
import com.jahangirb.pmpprep.ui.PmpViewModel
import com.jahangirb.pmpprep.ui.components.BulletList
import com.jahangirb.pmpprep.ui.components.PmpCard
import com.jahangirb.pmpprep.ui.components.ProgressRow
import com.jahangirb.pmpprep.ui.components.SectionLabel
import com.jahangirb.pmpprep.ui.components.domainColor
import com.jahangirb.pmpprep.ui.components.pct
import java.text.DateFormat
import java.util.Calendar
import java.util.Date
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.roundToInt

@Composable
fun PlannerScreen(vm: PmpViewModel) {
    val rev by vm.revision.collectAsState()
    val ctx = LocalContext.current
    val plan = vm.store.plan
    val x = vm.repo.extras
    val now = System.currentTimeMillis()
    val byDomain = remember(rev) { vm.readiness.group(vm.store.attempts) { it.domain.id } }

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Study plan", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
        Text("Built from your exam date, your hours and what your answers say is weakest.", fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant)

        val diag = vm.store.diagnostic
        PmpCard(
            container = if (diag == null) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f) else null,
            border = if (diag == null) MaterialTheme.colorScheme.primary else null,
        ) {
            SectionLabel("Your starting level")
            Spacer(Modifier.height(6.dp))
            if (diag == null) {
                Text("Take the 25-question diagnostic first, cold, without revising. It shows where you stand in each domain, and the plan below aims at whatever comes out lowest.",
                    fontSize = 14.sp, lineHeight = 20.sp)
                Spacer(Modifier.height(8.dp))
                Button(onClick = { vm.startDiagnostic() }, modifier = Modifier.fillMaxWidth()) { Text("Take the diagnostic (about 30 minutes)", fontWeight = FontWeight.Bold) }
            } else {
                Text("Diagnostic ${pct(diag.pct)} (${diag.right}/${diag.n}), ${DateFormat.getDateInstance(DateFormat.MEDIUM).format(Date(diag.at))}",
                    fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Domain.entries.forEach { d ->
                    diag.domains[d]?.let { (r, of) ->
                        Spacer(Modifier.height(6.dp))
                        ProgressRow(d.title, if (of == 0) 0.0 else r.toDouble() / of, "$r/$of", domainColor(d))
                    }
                }
                androidx.compose.material3.TextButton(onClick = { vm.startDiagnostic() }) { Text("Retake the diagnostic") }
            }
        }

        PmpCard {
            SectionLabel("Exam date")
            Spacer(Modifier.height(6.dp))
            val date = plan.examDate
            Text(if (date == null) "Not set" else DateFormat.getDateInstance(DateFormat.FULL).format(Date(date)), fontSize = 16.sp, fontWeight = FontWeight.Bold)
            if (date != null) {
                val daysLeft = ((Srs.startOfDay(date) - Srs.startOfDay(now)) / Srs.DAY_MS).toInt()
                Text(if (daysLeft >= 0) "$daysLeft days to go" else "The date has passed", fontFamily = FontFamily.Monospace,
                    color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(8.dp))
            Button(onClick = {
                val c = Calendar.getInstance().apply { timeInMillis = date ?: (now + 60 * Srs.DAY_MS) }
                DatePickerDialog(ctx, { _, y, m, d ->
                    val pick = Calendar.getInstance().apply { set(y, m, d, 9, 0, 0) }
                    vm.store.setPlan(plan.copy(examDate = pick.timeInMillis))
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).apply {
                    datePicker.minDate = now
                }.show()
            }) { Text(if (date == null) "Set exam date" else "Change date") }
        }

        PmpCard {
            SectionLabel("Hours a week")
            Text("${plan.hoursPerWeek} hours", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Slider(value = plan.hoursPerWeek.toFloat(), onValueChange = { vm.store.setPlan(plan.copy(hoursPerWeek = it.roundToInt())) },
                valueRange = 2f..40f, steps = 37)
            Text("Most candidates who pass study 8–15 hours a week for two to three months.", fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        val date = plan.examDate
        if (date != null && date > now) {
            val weeks = max(1, ceil((date - now).toDouble() / (7 * Srs.DAY_MS)).toInt())
            val totalHours = weeks * plan.hoursPerWeek
            val lessonsLeft = x.lessons.filter { it.id !in vm.store.lessonsDone }
            val lessonHours = lessonsLeft.sumOf { it.minutes } / 60.0
            val mockHours = (x.exam.minutes + 45) / 60.0
            val mocksPlanned = when {
                weeks >= 6 -> 4
                weeks >= 3 -> 3
                else -> 2
            }
            val practiceHours = (totalHours - lessonHours - mocksPlanned * mockHours).coerceAtLeast(0.0)
            // Weak domains get more of the practice time: weight ÷ accuracy, normalised.
            // Accuracy comes from practice once there is enough of it, otherwise from the diagnostic.
            val need = Domain.entries.associateWith { d ->
                val g = byDomain[d.id]
                val acc = if (g != null && g.n >= 30) g.accuracy else diag?.domainPct(d) ?: g?.accuracy ?: 0.6
                (x.domainWeights[d] ?: 0.0) / acc.coerceIn(0.3, 1.0)
            }
            val sum = need.values.sum().takeIf { it > 0 } ?: 1.0

            PmpCard {
                SectionLabel("Your $weeks weeks: $totalHours hours")
                Spacer(Modifier.height(6.dp))
                BulletList(listOf(
                    "Lessons: ${lessonsLeft.size} left, about ${"%.1f".format(lessonHours)} h",
                    "Full mock exams: $mocksPlanned, about ${"%.1f".format(mocksPlanned * mockHours)} h with review",
                    "Practice and review: ${"%.0f".format(practiceHours)} h, about ${(practiceHours * 60 / 2).roundToInt()} questions at 2 minutes each with review",
                ))
                if (totalHours < lessonHours + mocksPlanned * mockHours) {
                    Spacer(Modifier.height(6.dp))
                    Text("That is tight: lessons and mocks alone need more than your hours. Add hours or move the date.",
                        fontSize = 13.sp, color = MaterialTheme.colorScheme.error)
                }
            }
            PmpCard {
                SectionLabel("Where practice time should go")
                Text(if (diag == null) "Exam weight, adjusted for how you're doing in each domain. Take the diagnostic to aim this properly."
                    else "Exam weight, adjusted by your diagnostic and, as it builds up, your practice accuracy.",
                    fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Domain.entries.forEach { d ->
                    val share = need[d]!! / sum
                    Spacer(Modifier.height(8.dp))
                    ProgressRow("${d.title} · ${"%.0f".format(practiceHours * share)} h", share, pct(share), domainColor(d))
                }
            }
            PmpCard {
                SectionLabel("Week by week")
                Spacer(Modifier.height(6.dp))
                val learnWeeks = max(1, (weeks * 0.6).roundToInt()).coerceAtMost(weeks)
                val lessonsPerWeek = ceil(lessonsLeft.size.toDouble() / learnWeeks).toInt()
                (1..weeks).forEach { w ->
                    val start = Date(now + (w - 1) * 7 * Srs.DAY_MS)
                    val focus = when {
                        w == weeks -> "Light review: flashcards, the mistake journal, one short mock. Rest the day before."
                        w > weeks - mocksPlanned -> "Full mock exam, then review every wrong answer and drill the weakest tasks."
                        w <= learnWeeks && lessonsPerWeek > 0 -> "About $lessonsPerWeek lessons, their quick checks, then 10-question drills on each task."
                        else -> "Mixed practice on weak tasks, the situational trainer, and daily flashcards."
                    }
                    Text("Week $w · ${DateFormat.getDateInstance(DateFormat.MEDIUM).format(start)}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(focus, fontSize = 13.sp, lineHeight = 18.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
        Spacer(Modifier.height(16.dp))
    }
}
