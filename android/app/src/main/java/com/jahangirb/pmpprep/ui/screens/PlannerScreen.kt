package com.jahangirb.pmpprep.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.clickable
import androidx.compose.material3.RadioButton
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import com.jahangirb.pmpprep.ui.components.ClayButton as Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jahangirb.pmpprep.data.Domain
import com.jahangirb.pmpprep.data.Srs
import com.jahangirb.pmpprep.ui.PmpViewModel
import com.jahangirb.pmpprep.ui.components.PmpCard
import com.jahangirb.pmpprep.ui.components.ProgressRow
import com.jahangirb.pmpprep.ui.components.SectionLabel
import com.jahangirb.pmpprep.ui.components.domainColor
import com.jahangirb.pmpprep.ui.components.pct
import java.text.DateFormat
import java.util.Calendar
import java.util.Date
import kotlin.math.roundToInt

@Composable
fun PlannerScreen(vm: PmpViewModel) {
    vm.revision.collectAsState().value // recompose when the plan changes
    val ctx = LocalContext.current
    val plan = vm.store.plan
    val now = System.currentTimeMillis()

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Study plan", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
        Text("Your path is built from these. Change them any time; it re-plans at once.", fontSize = 13.sp,
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

        PmpCard {
            SectionLabel("Study days")
            Spacer(Modifier.height(6.dp))
            StudyDayChips(plan) { vm.store.setPlan(it) }
            Text("Your path spreads the week's hours over these days. Miss one and it re-plans from where you are.", fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        PmpCard {
            SectionLabel("Study order")
            Spacer(Modifier.height(6.dp))
            listOf(false to "By exam domain: weakest first", true to "By knowledge area: classroom order").forEach { (byArea, label) ->
                Row(Modifier.fillMaxWidth().clickable { vm.store.setPlan(plan.copy(byArea = byArea)) }, verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = plan.byArea == byArea, onClick = { vm.store.setPlan(plan.copy(byArea = byArea)) })
                    Text(label, fontSize = 14.sp)
                }
            }
            Text("Which order new lessons come in. Fixes, reviews and mocks are planned the same either way.", fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        RemindersCard()
        Spacer(Modifier.height(16.dp))
    }
}
