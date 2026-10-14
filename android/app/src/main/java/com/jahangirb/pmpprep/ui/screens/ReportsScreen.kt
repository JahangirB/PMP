package com.jahangirb.pmpprep.ui.screens

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jahangirb.pmpprep.data.ExamFitness
import com.jahangirb.pmpprep.data.WeeklyReport
import com.jahangirb.pmpprep.data.WeeklyReports
import com.jahangirb.pmpprep.ui.PmpViewModel
import com.jahangirb.pmpprep.ui.components.PmpCard
import com.jahangirb.pmpprep.ui.components.SectionLabel
import com.jahangirb.pmpprep.ui.theme.ColorError
import com.jahangirb.pmpprep.ui.theme.ColorSuccess
import java.text.DateFormat
import java.util.Date

/**
 * Weekly reports: where you stood each week (Monday to Sunday), with every figure compared to the
 * week before. This week's is live; a finished week stays as it was.
 */
@Composable
fun ReportsScreen(vm: PmpViewModel) {
    val rev by vm.revision.collectAsState()
    val weeks = remember(rev) { vm.store.weekly.sortedByDescending { it.weekStart } }
    var against by rememberSaveable { mutableStateOf("last") }

    LazyColumn(Modifier.fillMaxSize().padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Spacer(Modifier.height(4.dp))
            Text("Weekly reports", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
            Text("Each week's knowledge, exam fitness and effort, compared with the week before. ▲ and ▼ show better or worse, not just up or down.",
                fontSize = 13.sp, lineHeight = 19.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        if (weeks.isEmpty()) {
            item { PmpCard { Text("Your first report appears as soon as you study. It's updated through the week and kept when the week ends.") } }
            return@LazyColumn
        }
        if (weeks.size >= 2) item {
            PmpCard {
                SectionLabel("Trend")
                Spacer(Modifier.height(6.dp))
                val ordered = weeks.reversed()
                Spark("Readiness", ordered.map { it.readiness })
                Spacer(Modifier.height(10.dp))
                Spark("Exam fitness", ordered.map { it.fitness })
                Spacer(Modifier.height(10.dp))
                Spark("Minutes studied", ordered.map { it.minutes })
            }
        }
        item {
            val cur = weeks.first()
            val prev = weeks.getOrNull(1)
            val first = weeks.last().takeIf { weeks.size > 2 }
            PmpCard {
                SectionLabel(if (cur.weekStart == WeeklyReports.weekStart(System.currentTimeMillis())) "This week so far" else "Last recorded week")
                Text(weekLabel(cur), fontWeight = FontWeight.Bold)
                if (first != null) Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.padding(vertical = 4.dp)) {
                    Toggle("vs last week", against == "last") { against = "last" }
                    Toggle("vs your first week", against == "first") { against = "first" }
                }
                Spacer(Modifier.height(6.dp))
                Comparison(cur, if (against == "first" && first != null) first else prev)
            }
        }
        if (weeks.size > 1) {
            item { Text("Earlier weeks", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold) }
            items(weeks.drop(1), key = { it.weekStart }) { w ->
                val before = weeks.getOrNull(weeks.indexOf(w) + 1)
                var open by rememberSaveable(w.weekStart) { mutableStateOf(false) }
                PmpCard(onClick = { open = !open }) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text(weekLabel(w), fontWeight = FontWeight.Bold)
                            Text("Readiness ${w.readiness} · fitness ${w.fitness ?: "–"} · ${w.minutes} min", fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Text(if (open) "Hide" else "Show", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    }
                    if (open) { Spacer(Modifier.height(8.dp)); Comparison(w, before) }
                }
            }
        }
        item { Spacer(Modifier.height(24.dp)) }
    }
}

private fun weekLabel(w: WeeklyReport): String {
    val f = DateFormat.getDateInstance(DateFormat.MEDIUM)
    return "${f.format(Date(w.weekStart))} – ${f.format(Date(w.weekStart + 6 * ExamFitness.DAY))}"
}

@Composable
private fun Toggle(label: String, on: Boolean, onClick: () -> Unit) {
    Text(label, fontWeight = if (on) FontWeight.ExtraBold else FontWeight.Normal,
        color = if (on) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.clickable(role = Role.Tab, onClick = onClick).padding(vertical = 8.dp))
}

/** Every figure with its change; the arrow says better or worse, the sign says up or down. */
@Composable
private fun Comparison(r: WeeklyReport, p: WeeklyReport?) {
    WeeklyReports.compare(r, p).forEach { sec ->
        Text(sec.title, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp, bottom = 2.dp))
        sec.rows.forEach { row ->
            Row(Modifier.fillMaxWidth().padding(vertical = 3.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(row.label, fontSize = 14.sp, modifier = Modifier.weight(1f))
                Text(row.value?.let { "$it${row.unit}" } ?: "–", fontSize = 14.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                Spacer(Modifier.width(10.dp))
                val d = row.delta
                val text = when {
                    d == null -> "new"
                    d == 0 -> "same"
                    else -> (if (row.direction > 0) "▲ " else "▼ ") + (if (d > 0) "+$d" else "$d")
                }
                Text(text, fontSize = 13.sp, fontFamily = FontFamily.Monospace, modifier = Modifier.width(72.dp),
                    color = when (row.direction) { 1 -> ColorSuccess; -1 -> ColorError; else -> MaterialTheme.colorScheme.onSurfaceVariant })
            }
        }
    }
}

/** One series over the weeks: a thin line, the latest week marked and labelled. */
@Composable
private fun Spark(title: String, values: List<Int?>) {
    val pts = values.mapIndexedNotNull { i, v -> v?.let { i to it } }
    val color = MaterialTheme.colorScheme.primary
    val grid = MaterialTheme.colorScheme.outline
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(title, fontSize = 13.sp, modifier = Modifier.width(110.dp))
        if (pts.size < 2) {
            Text("needs two weeks", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1f))
        } else {
            val lo = pts.minOf { it.second }; val hi = pts.maxOf { it.second }
            Canvas(Modifier.weight(1f).height(36.dp).semantics { contentDescription = "$title by week: " + pts.joinToString(", ") { "${it.second}" } }) {
                val n = (values.size - 1).coerceAtLeast(1)
                fun at(i: Int, v: Int) = Offset(size.width * i / n, size.height - 4.dp.toPx() - (size.height - 8.dp.toPx()) * (if (hi == lo) 0.5f else (v - lo).toFloat() / (hi - lo)))
                drawLine(grid.copy(alpha = 0.4f), Offset(0f, size.height - 1), Offset(size.width, size.height - 1), 1.dp.toPx())
                pts.zipWithNext().forEach { (a, b) -> drawLine(color, at(a.first, a.second), at(b.first, b.second), 2.dp.toPx(), StrokeCap.Round) }
                drawCircle(color, 4.dp.toPx(), at(pts.last().first, pts.last().second))
            }
        }
        Text(pts.lastOrNull()?.second?.toString() ?: "–", fontSize = 14.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace,
            modifier = Modifier.width(44.dp).padding(start = 8.dp))
    }
}
