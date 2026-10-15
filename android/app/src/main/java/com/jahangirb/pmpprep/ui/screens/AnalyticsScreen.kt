package com.jahangirb.pmpprep.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jahangirb.pmpprep.data.Approach
import com.jahangirb.pmpprep.data.Domain
import com.jahangirb.pmpprep.data.MockBands
import com.jahangirb.pmpprep.data.QType
import com.jahangirb.pmpprep.ui.PmpViewModel
import com.jahangirb.pmpprep.ui.components.PmpCard
import com.jahangirb.pmpprep.ui.components.ProgressRow
import com.jahangirb.pmpprep.ui.components.SectionLabel
import com.jahangirb.pmpprep.ui.components.domainColor
import com.jahangirb.pmpprep.ui.components.pct
import com.jahangirb.pmpprep.ui.theme.ColorError
import com.jahangirb.pmpprep.ui.theme.ColorSuccess
import com.jahangirb.pmpprep.ui.theme.ColorWarning

@Composable
fun AnalyticsScreen(vm: PmpViewModel) {
    val rev by vm.revision.collectAsState()
    val report = remember(rev) { vm.readiness.report(vm.store) }
    val atts = vm.store.attempts
    val byTask = remember(rev) { vm.readiness.group(atts) { it.ecoTask } }
    val byApproach = remember(rev) { vm.readiness.group(atts) { it.approach?.id } }
    val byType = remember(rev) { vm.readiness.group(atts) { it.type.base.id } }
    val streak = remember(rev) { vm.readiness.streak(vm.store, System.currentTimeMillis()) }
    val x = vm.repo.extras

    LazyColumn(Modifier.fillMaxSize().padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Spacer(Modifier.height(8.dp))
            Text("Readiness ${report.score} / 100", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
            Text(if (report.provisional) "Provisional: " + report.gates.joinToString("; ") else "Built on enough evidence to mean something.",
                fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            androidx.compose.material3.TextButton(onClick = { vm.navigate(com.jahangirb.pmpprep.ui.Dest.GUIDE) }) { Text("How the score works") }
        }
        item {
            PmpCard {
                SectionLabel("How the score is built")
                report.components.forEach { c ->
                    Spacer(Modifier.height(10.dp))
                    ProgressRow("${c.label} (${c.weight} pts) · ${c.figure}", c.score, "+${"%.1f".format(c.points)}")
                    Text(c.detail, fontSize = 12.sp, lineHeight = 16.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
        item {
            PmpCard {
                SectionLabel("Study days · last ${streak.last14.size}")
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    streak.last14.forEach { on ->
                        Box(Modifier.weight(1f).height(22.dp).clip(RoundedCornerShape(4.dp))
                            .background(if (on) ColorSuccess else Color.Gray.copy(alpha = 0.25f)))
                    }
                }
                Spacer(Modifier.height(6.dp))
                Text("Current streak ${streak.current} · longest ${streak.longest}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        if (vm.store.mocks.size >= 2) {
            item {
                PmpCard {
                    SectionLabel("Mock exam trend")
                    Spacer(Modifier.height(8.dp))
                    MockTrend(vm.store.mocks.sortedBy { it.at }.map { it.pct }, vm.repo.extras.rules.mockBands)
                }
            }
        }
        item {
            PmpCard {
                SectionLabel("By ECO task (weakest first)")
                Text("Tasks with fewer than five answers are marked thin.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                val rows = x.tasks.map { t -> t to byTask[t.id] }
                    .sortedWith(compareBy({ it.second == null }, { it.second?.accuracy ?: 0.0 }))
                rows.forEach { (t, g) ->
                    Spacer(Modifier.height(8.dp))
                    ProgressRow(
                        "${t.code} ${t.label}" + if (g != null && g.n < 5) " (thin)" else "",
                        g?.accuracy ?: 0.0,
                        if (g == null) "—" else "${pct(g.accuracy)} · ${g.n}",
                        domainColor(t.domain),
                    )
                }
            }
        }
        item {
            PmpCard {
                SectionLabel("By approach")
                Approach.entries.forEach { a ->
                    val g = byApproach[a.id]
                    Spacer(Modifier.height(8.dp))
                    ProgressRow(a.title, g?.accuracy ?: 0.0, if (g == null) "—" else "${pct(g.accuracy)} · ${g.n}")
                }
                Spacer(Modifier.height(12.dp))
                SectionLabel("By question type")
                listOf(QType.SINGLE, QType.MULTI, QType.MATCHING, QType.HOTSPOT, QType.FILLBLANK).forEach { t ->
                    val g = byType[t.id]
                    Spacer(Modifier.height(8.dp))
                    ProgressRow(t.title, g?.accuracy ?: 0.0, if (g == null) "—" else "${pct(g.accuracy)} · ${g.n}")
                }
            }
        }
        item {
            PmpCard {
                SectionLabel("Pace")
                val (median, n) = vm.readiness.medianPace(atts)
                val band = x.readiness.paceBand
                val color = when {
                    median == null -> MaterialTheme.colorScheme.onSurfaceVariant
                    median < band[0] -> ColorWarning
                    median <= band[1] -> ColorSuccess
                    else -> ColorError
                }
                Text(if (median == null) "No timed answers yet." else "Median ${median.toInt()}s per question over $n answers",
                    fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, color = color)
                Text("The exam allows about ${x.exam.paceTargetSec}s a question. ${band[0].toInt()}–${band[1].toInt()}s is the healthy band; much faster usually means guessing.",
                    fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Domain.entries.forEach { d ->
                    val g = vm.readiness.group(atts) { it.domain.id }[d.id]
                    if (g?.meanSec != null) Text("${d.short}: mean ${g.meanSec.toInt()}s", fontSize = 12.sp)
                }
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun MockTrend(points: List<Double>, bands: MockBands) {
    val line = MaterialTheme.colorScheme.primary
    val grid = MaterialTheme.colorScheme.outline
    Canvas(Modifier.fillMaxWidth().height(140.dp)) {
        val w = size.width; val h = size.height
        // Guides at the band cuts: Below Target, Target, Above Target.
        listOf(bands.below.toFloat(), bands.target.toFloat(), bands.above.toFloat()).forEach { g ->
            val y = h - g * h
            drawLine(grid, Offset(0f, y), Offset(w, y), strokeWidth = 1f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f)))
        }
        val step = if (points.size > 1) w / (points.size - 1) else w
        var prev: Offset? = null
        points.forEachIndexed { i, p ->
            val pt = Offset(i * step, h - p.toFloat() * h)
            prev?.let { drawLine(line, it, pt, strokeWidth = 4f) }
            drawCircle(line, 7f, pt)
            prev = pt
        }
    }
    Text("Dashed lines at ${pct(bands.below)}, ${pct(bands.target)} and ${pct(bands.above)}.", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    Spacer(Modifier.size(1.dp))
}
