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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jahangirb.pmpprep.data.Booking
import com.jahangirb.pmpprep.ui.Dest
import com.jahangirb.pmpprep.ui.PmpViewModel
import com.jahangirb.pmpprep.ui.components.ClayOutlinedButton as OutlinedButton
import com.jahangirb.pmpprep.ui.components.PmpCard
import com.jahangirb.pmpprep.ui.components.SectionLabel
import com.jahangirb.pmpprep.ui.theme.ColorError
import com.jahangirb.pmpprep.ui.theme.ColorSuccess
import java.text.DateFormat
import java.util.Date

/** "Am I ready to book?": the three conditions, and when the trend says you'll meet them. */
@Composable
fun BookScreen(vm: PmpViewModel) {
    val rev by vm.revision.collectAsState()
    val b = remember(rev) { vm.booking() }
    val fmt = DateFormat.getDateInstance(DateFormat.MEDIUM)
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Am I ready to book?", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
        PmpCard(container = (if (b.ready) ColorSuccess else MaterialTheme.colorScheme.primary).copy(alpha = 0.10f)) {
            Text(if (b.ready) "Yes. You meet all three." else "Not yet.", fontSize = 26.sp, fontWeight = FontWeight.ExtraBold,
                color = if (b.ready) ColorSuccess else MaterialTheme.colorScheme.onSurface)
            Text(if (b.ready) "Knowledge, stamina and real exam practice are all where they need to be. Book it, and keep the plan going until the day."
                else "Here's what's left, and when your trend says you'll get there.", fontSize = 14.sp, lineHeight = 20.sp)
            Spacer(Modifier.height(10.dp))
            b.criteria.forEach { c ->
                Row(Modifier.fillMaxWidth().padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(if (c.met) "✓" else "○", fontWeight = FontWeight.Bold, color = if (c.met) ColorSuccess else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.width(24.dp))
                    Text(c.label, fontSize = 14.sp, modifier = Modifier.weight(1f))
                    Text("${c.now} / ${c.target}", fontSize = 14.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                }
            }
        }
        if (!b.ready) PmpCard {
            SectionLabel("Projected ready date")
            val p = b.projection
            val text = when {
                p == null && b.criteria.take(2).all { it.met } -> "Readiness and exam fitness are there already."
                p == null -> "A projection needs at least two weekly reports. Keep studying and it appears next week."
                p.stalled.isNotEmpty() -> "${p.stalled.joinToString(" and ")} ${if (p.stalled.size == 1) "isn't" else "aren't"} rising yet, so there's no date to project. Weekly reports show how it moves."
                p.earliest != null && p.latest != null -> "At your current pace you'll reach readiness and fitness between ${fmt.format(Date(p.earliest))} and ${fmt.format(Date(p.latest))}."
                p.earliest != null -> "At your current pace, from ${fmt.format(Date(p.earliest))}; the trend is too uneven yet to say how late."
                p.latest != null -> "At your current pace you'll reach readiness and fitness around ${fmt.format(Date(p.latest))}. Rough: it's from fewer than four weeks of reports."
                else -> "Not enough of a trend yet."
            }
            Text(text, fontSize = 14.sp, lineHeight = 20.sp)
            if (b.mocksNeeded > 0) Text("Plus ${b.mocksNeeded} more full ${if (b.mocksNeeded == 1) "mock" else "mocks"} at Target (65%) or above.",
                fontSize = 14.sp, lineHeight = 20.sp, modifier = Modifier.padding(top = 6.dp))
            val exam = vm.store.plan.examDate
            if (p?.afterExam == true && exam != null) {
                Spacer(Modifier.height(10.dp))
                Text("That's after your exam date, ${fmt.format(Date(exam))}. Consider moving the exam, or adding study hours.",
                    fontSize = 14.sp, fontWeight = FontWeight.Bold, color = ColorError)
                OutlinedButton(onClick = { vm.navigate(Dest.PLANNER) }, modifier = Modifier.padding(top = 6.dp)) { Text("Plan and schedule") }
            }
        }
        Text("The thresholds (readiness ${Booking.READINESS}+, exam fitness ${Booking.FITNESS}+, ${Booking.MOCKS} full mocks at Target) are this app's judgement, not PMI's. " +
            "PMI doesn't publish a pass mark.", fontSize = 12.sp, lineHeight = 17.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(16.dp))
    }
}
