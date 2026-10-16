package com.jahangirb.pmpprep.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import android.graphics.Paint
import com.jahangirb.pmpprep.data.ChartData
import com.jahangirb.pmpprep.ui.PracticeFilter
import com.jahangirb.pmpprep.ui.theme.ColorInfo
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
        item { TrendCard(vm, rev) }
        item { HeatCard(vm, rev) }
        item { PaceCard(vm, rev) }
        item { CalibrationCard(vm, rev) }
        item { CausesCard(vm, rev) }
        if (vm.store.drills.isNotEmpty()) item { DrillsCard(vm, rev) }
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

/* ── charts (index.html §11) ─────────────────────────────────────────────
   Each is drawn on a Canvas with a plain-text summary beside it and the same
   words as the canvas's content description, so a screen reader gets the
   reading, not a picture. */

@Composable
private fun ChartNote(text: String) {
    Text(text, fontSize = 12.sp, lineHeight = 17.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
}

/** Rolling accuracy over the last 50 answers, by answer number; the opening stretch before 50 is dashed. */
@Composable
private fun TrendCard(vm: PmpViewModel, rev: Int) {
    val pts = remember(rev) { ChartData.thin(ChartData.trend(vm.store.attempts)) }
    val overall = remember(rev) { vm.readiness.accuracy(vm.store.attempts, System.currentTimeMillis()).first }
    PmpCard {
        SectionLabel("Are you getting better?")
        if (pts.isEmpty()) { ChartNote("The accuracy trend needs ${ChartData.TREND_START} answers. Keep going."); return@PmpCard }
        val last = pts.last()
        val summary = "Accuracy over your last ${ChartData.TREND_WINDOW} answers is ${pct(last.value)} after ${last.n} answers" +
            (pts.firstOrNull { it.full }?.let { f -> ", against ${pct(f.value)} when the window first filled at answer ${f.n}" } ?: "") +
            ". Your time-weighted accuracy, the readiness figure, is ${pct(overall)}."
        val line = MaterialTheme.colorScheme.primary
        val grid = MaterialTheme.colorScheme.outline
        val ref = MaterialTheme.colorScheme.tertiary
        Spacer(Modifier.height(8.dp))
        Canvas(Modifier.fillMaxWidth().height(150.dp).semantics { contentDescription = summary }) {
            val w = size.width; val h = size.height
            val maxN = last.n.toFloat()
            fun x(n: Int) = n / maxN * w
            fun y(v: Double) = h - v.toFloat() * h
            listOf(0.0, 0.25, 0.5, 0.75, 1.0).forEach { g -> drawLine(grid, Offset(0f, y(g)), Offset(w, y(g)), strokeWidth = 1f) }
            drawLine(ref, Offset(0f, y(overall)), Offset(w, y(overall)), strokeWidth = 1.5f * density,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f * density, 4f * density)))
            for (i in 1 until pts.size) {
                val a = pts[i - 1]; val b = pts[i]
                drawLine(if (b.full) line else line.copy(alpha = 0.45f), Offset(x(a.n), y(a.value)), Offset(x(b.n), y(b.value)),
                    strokeWidth = (if (b.full) 3f else 2f) * density,
                    pathEffect = if (b.full) null else PathEffect.dashPathEffect(floatArrayOf(6f * density, 5f * density)))
            }
            drawCircle(line, 4f * density, Offset(x(last.n), y(last.value)))
        }
        Spacer(Modifier.height(6.dp))
        ChartNote(summary + " Grid lines at 0, 25, 50, 75 and 100%; the dashed opening is the same figure before ${ChartData.TREND_WINDOW} answers existed to average.")
    }
}

/** The 26 ECO tasks in the outline's three domains; a task under the minimum shows its count in grey, not a rate. Tap one to drill it. */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun HeatCard(vm: PmpViewModel, rev: Int) {
    val x = vm.repo.extras
    val min = x.readiness.coverageTargetPerTask
    val cells = remember(rev) { ChartData.heat(x.tasks, vm.store.attempts, x.rules.mockBands, min) }
    val colors = listOf(Color.Gray.copy(alpha = 0.22f), ColorError, ColorWarning, ColorInfo, ColorSuccess)
    PmpCard {
        SectionLabel("Where are the cold spots?")
        ChartNote("Your accuracy on each task. Grey cells have fewer than $min answers, so they show what's missing rather than a rate. Tap a cell to drill it.")
        Domain.entries.forEach { d ->
            val row = cells.filter { it.task.domain == d }
            if (row.isEmpty()) return@forEach
            Spacer(Modifier.height(8.dp))
            Text("${d.title} · ${pct(x.domainWeights[d] ?: 0.0)} of the exam", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                row.forEach { c ->
                    val bg = colors[c.band]
                    val value = if (c.thin) "${c.n}/$min" else pct(c.accuracy)
                    val label = "${c.task.code} ${c.task.label}: " + if (c.thin) (if (c.n == 0) "no answers yet" else "${c.n} answers, too few for a rate") else "$value across ${c.n} answers"
                    Box(
                        Modifier.size(width = 62.dp, height = 44.dp).clip(RoundedCornerShape(6.dp))
                            .background(if (c.thin) bg else bg.copy(alpha = 0.85f))
                            .clickable(onClickLabel = "Drill this task") { vm.startPractice(PracticeFilter(task = c.task.id, count = 10), c.task.label) }
                            .semantics(mergeDescendants = true) { contentDescription = label },
                        contentAlignment = Alignment.Center,
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(c.task.code, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (c.thin) MaterialTheme.colorScheme.onSurface else Color.White)
                            Text(value, fontSize = 11.sp, fontFamily = FontFamily.Monospace, color = if (c.thin) MaterialTheme.colorScheme.onSurfaceVariant else Color.White)
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        val b = x.rules.mockBands
        ChartNote("Red below ${pct(b.below)}, amber ${pct(b.below)}–${pct(b.target)}, blue ${pct(b.target)}–${pct(b.above)}, green ${pct(b.above)} and above (the mock bands). " +
            "Cold: " + cells.filter { it.thin }.joinToString(", ") { it.task.code }.ifEmpty { "none" } + ".")
    }
}

/** Time per answer in 15-second bins, with the healthy band shaded and the exam's pace line. */
@Composable
private fun PaceCard(vm: PmpViewModel, rev: Int) {
    val x = vm.repo.extras
    val bins = remember(rev) { ChartData.paceHistogram(vm.store.attempts) }
    val median = remember(rev) { ChartData.median(ChartData.paceSamples(vm.store.attempts)) }
    val n = bins.sumOf { it.n }
    PmpCard {
        SectionLabel("Are you working at exam speed?")
        if (n == 0) { ChartNote("No timed answers yet."); return@PmpCard }
        val band = x.readiness.paceBand
        val pace = x.exam.paceTargetSec
        val over = ChartData.paceSamples(vm.store.attempts).count { it > pace }
        val summary = "$n answers, median ${median?.toInt() ?: 0} seconds. ${pct(over.toDouble() / n)} took longer than the exam's $pace seconds a question. " +
            "Busiest bin: " + bins.maxBy { it.n }.let { "${it.lo}–${it.hi ?: "+"} s (${it.n})" } + "."
        val bar = MaterialTheme.colorScheme.primary
        val shade = ColorSuccess.copy(alpha = 0.14f)
        val line = ColorError
        val ink = MaterialTheme.colorScheme.onSurfaceVariant
        val peak = bins.maxOf { it.n }.coerceAtLeast(1)
        Spacer(Modifier.height(8.dp))
        Canvas(Modifier.fillMaxWidth().height(140.dp).semantics { contentDescription = summary }) {
            val total = (ChartData.PACE_BINS * ChartData.PACE_BIN_SEC).toFloat()
            val w = size.width; val h = size.height - 16.dp.toPx()
            fun xs(sec: Double) = (sec.coerceAtMost(total.toDouble()) / total).toFloat() * w
            drawRect(shade, Offset(xs(band[0]), 0f), Size(xs(band[1]) - xs(band[0]), h))
            val bw = w / ChartData.PACE_BINS
            bins.forEachIndexed { k, b ->
                if (b.n == 0) return@forEachIndexed
                val bh = b.n.toFloat() / peak * h
                drawRect(bar, Offset(k * bw + 2f, h - bh), Size(bw - 4f, bh))
            }
            drawLine(line, Offset(xs(pace.toDouble()), 0f), Offset(xs(pace.toDouble()), h), strokeWidth = 2f * density)
            drawLine(ink, Offset(0f, h), Offset(w, h), strokeWidth = 1f)
            val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply { textSize = 10.sp.toPx(); textAlign = Paint.Align.CENTER; color = ink.toArgb() }
            drawContext.canvas.nativeCanvas.apply {
                for (k in 0..ChartData.PACE_BINS step 2) drawText(if (k == ChartData.PACE_BINS) "180+" else "${k * ChartData.PACE_BIN_SEC}", k * bw, size.height - 2f, paint)
            }
        }
        Spacer(Modifier.height(6.dp))
        ChartNote("$summary The red line is the exam's $pace s a question; the shaded strip is the ${band[0].toInt()}–${band[1].toInt()} s band readiness gives full marks for. Much faster usually means guessing.")
    }
}

/** Sure against not sure (Think so and Guess), right against wrong: does your sense of a question carry information? */
@Composable
private fun CalibrationCard(vm: PmpViewModel, rev: Int) {
    val c = remember(rev) { ChartData.calibration(vm.store.attempts) }
    val min = vm.repo.extras.readiness.coverageTargetPerTask
    PmpCard {
        SectionLabel("Can you tell when you're right?")
        if (c.total == 0) { ChartNote("Turn on \"Ask how sure I am\" in Study settings and this fills in: how often Sure was right, against Think so or Guess."); return@PmpCard }
        val summary = "Sure: ${c.sureRight} right, ${c.sureWrong} wrong" + (c.sureAccuracy?.let { " (${pct(it)})" } ?: "") +
            ". Not sure: ${c.unsureRight} right, ${c.unsureWrong} wrong" + (c.unsureAccuracy?.let { " (${pct(it)})" } ?: "") + "."
        Spacer(Modifier.height(8.dp))
        Column(Modifier.semantics(mergeDescendants = true) { contentDescription = summary }) {
            Row {
                Spacer(Modifier.weight(1f))
                Text("Right", fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                Text("Wrong", fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            }
            CalRow("Sure", c.sureRight, c.sureWrong, ColorSuccess, ColorError)
            Spacer(Modifier.height(4.dp))
            CalRow("Not sure", c.unsureRight, c.unsureWrong, ColorInfo, ColorWarning)
        }
        Spacer(Modifier.height(6.dp))
        ChartNote(summary + " " + ChartData.calibrationVerdict(c, min) + " Sure-but-wrong is the corner to shrink: on the day nothing prompts a second look.")
    }
}

@Composable
private fun CalRow(label: String, right: Int, wrong: Int, okColor: Color, badColor: Color) {
    val n = (right + wrong).coerceAtLeast(1)
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(label, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
        listOf(right to okColor, wrong to badColor).forEach { (v, col) ->
            Box(Modifier.weight(1f).height(44.dp).padding(2.dp).clip(RoundedCornerShape(6.dp))
                .background(col.copy(alpha = 0.12f + 0.6f * v / n)), contentAlignment = Alignment.Center) {
                Text("$v · ${pct(v.toDouble() / n)}", fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
            }
        }
    }
}

/** Why the tagged mistakes went wrong, in the shared cause order. */
@Composable
private fun CausesCard(vm: PmpViewModel, rev: Int) {
    val rules = vm.repo.extras.rules
    val counts = remember(rev) { ChartData.causes(vm.store.mistakeTags.values, rules.causeOrder) }
    val total = counts.sumOf { it.second }
    PmpCard {
        SectionLabel("Why the misses happen")
        if (total == 0) { ChartNote("Tag why a missed question went wrong in the mistake journal and the pattern shows here."); return@PmpCard }
        val peak = counts.maxOf { it.second }.coerceAtLeast(1)
        val bar = MaterialTheme.colorScheme.primary
        val track = MaterialTheme.colorScheme.surfaceVariant
        val summary = "$total tagged mistakes: " + counts.filter { it.second > 0 }.sortedByDescending { it.second }
            .joinToString(", ") { "${rules.cause(it.first).short} ${it.second}" } + "."
        Column(Modifier.semantics(mergeDescendants = true) { contentDescription = summary }) {
            counts.forEach { (cause, v) ->
                Spacer(Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(rules.cause(cause).short, fontSize = 12.sp, modifier = Modifier.weight(1.4f))
                    Canvas(Modifier.weight(2f).height(14.dp)) {
                        drawRoundRect(track, cornerRadius = androidx.compose.ui.geometry.CornerRadius(4.dp.toPx()))
                        if (v > 0) drawRoundRect(bar, size = Size(size.width * v / peak, size.height), cornerRadius = androidx.compose.ui.geometry.CornerRadius(4.dp.toPx()))
                    }
                    Text("$v", fontSize = 12.sp, fontFamily = FontFamily.Monospace, modifier = Modifier.padding(start = 8.dp))
                }
            }
        }
        Spacer(Modifier.height(6.dp))
        ChartNote(summary)
    }
}

/** The formula lab's drill log by sheet group. Kept apart from accuracy; a drill day still counts as a study day. */
@Composable
private fun DrillsCard(vm: PmpViewModel, rev: Int) {
    val rows = remember(rev) { ChartData.drillsByGroup(vm.store.drills) }
    val label = vm.repo.extras.formulas.associate { it.id to it.label }
    PmpCard {
        SectionLabel("Formula lab drills")
        ChartNote("Every problem marked or shown, by group. These never count toward accuracy, but a day with a drill on it is a study day.")
        rows.forEach { (g, n, right) ->
            Spacer(Modifier.height(8.dp))
            ProgressRow(label[g] ?: if (g == "other") "Other" else g, if (n == 0) 0.0 else right.toDouble() / n, "$right/$n")
        }
    }
}
