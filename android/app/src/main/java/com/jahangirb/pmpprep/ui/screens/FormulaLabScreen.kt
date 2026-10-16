package com.jahangirb.pmpprep.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import com.jahangirb.pmpprep.ui.components.ClayButton as Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.TextButton
import com.jahangirb.pmpprep.ui.components.ClayOutlinedButton as OutlinedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jahangirb.pmpprep.data.CalcLine
import com.jahangirb.pmpprep.data.Calculators
import com.jahangirb.pmpprep.data.DrillMark
import com.jahangirb.pmpprep.data.DrillTally
import com.jahangirb.pmpprep.data.DrillVerdict
import com.jahangirb.pmpprep.data.FormulaDrills
import com.jahangirb.pmpprep.data.NetInput
import com.jahangirb.pmpprep.data.NetResult
import com.jahangirb.pmpprep.data.QType
import com.jahangirb.pmpprep.data.TreeBranch
import com.jahangirb.pmpprep.data.TreeOutcome
import com.jahangirb.pmpprep.ui.PmpViewModel
import com.jahangirb.pmpprep.ui.PracticeFilter
import com.jahangirb.pmpprep.ui.components.PmpCard
import com.jahangirb.pmpprep.ui.components.SectionLabel
import com.jahangirb.pmpprep.ui.components.Selector
import com.jahangirb.pmpprep.ui.components.pct
import com.jahangirb.pmpprep.ui.theme.ColorError
import com.jahangirb.pmpprep.ui.theme.ColorSuccess
import com.jahangirb.pmpprep.ui.theme.ColorWarning

@Composable
fun FormulaLabScreen(vm: PmpViewModel) {
    var tab by rememberSaveable { mutableIntStateOf(0) }
    val tabs = listOf("Calculators", "Formula sheet", "Drills")
    Column(Modifier.fillMaxSize()) {
        ScrollableTabRow(selectedTabIndex = tab, edgePadding = 8.dp) {
            tabs.forEachIndexed { i, t -> Tab(selected = tab == i, onClick = { tab = i }, text = { Text(t) }) }
        }
        when (tab) {
            0 -> CalculatorTab()
            1 -> FormulaSheet(vm)
            else -> Drills(vm)
        }
    }
}

@Composable
private fun NumField(label: String, value: String, onChange: (String) -> Unit, modifier: Modifier = Modifier) {
    OutlinedTextField(
        value = value, onValueChange = onChange, label = { Text(label) }, singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), modifier = modifier,
    )
}

private fun d(s: String): Double = s.replace(",", "").trim().toDoubleOrNull() ?: Double.NaN

@Composable
private fun Results(lines: List<CalcLine>) {
    lines.forEach { l ->
        PmpCard {
            Row {
                Text(l.label, fontWeight = FontWeight.Bold, fontSize = 14.sp, modifier = Modifier.weight(1f))
                Text(l.value, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Black, fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)
            }
            Text(l.formula, fontFamily = FontFamily.Monospace, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("= ${l.working}", fontFamily = FontFamily.Monospace, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            if (l.reading.isNotBlank()) Text(l.reading, fontSize = 13.sp, lineHeight = 18.sp)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CalculatorTab() {
    var calc by rememberSaveable { mutableIntStateOf(0) }
    val names = listOf("EVM", "PERT", "PTA", "EMV", "Channels", "Finance", "Agile", "Decision tree", "Float & critical path", "NPV, IRR & payback")
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        FlowRow(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            names.forEachIndexed { i, n -> FilterChip(selected = calc == i, onClick = { calc = i }, label = { Text(n) }) }
        }
        when (calc) {
            7 -> TreeCalculator()
            8 -> NetworkCalculator()
            9 -> CashCalculator()
            0 -> {
                var pv by rememberSaveable { mutableStateOf("80000") }
                var ev by rememberSaveable { mutableStateOf("70000") }
                var ac by rememberSaveable { mutableStateOf("84000") }
                var bac by rememberSaveable { mutableStateOf("200000") }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    NumField("PV", pv, { pv = it }, Modifier.weight(1f)); NumField("EV", ev, { ev = it }, Modifier.weight(1f))
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    NumField("AC", ac, { ac = it }, Modifier.weight(1f)); NumField("BAC", bac, { bac = it }, Modifier.weight(1f))
                }
                Results(Calculators.evm(d(pv), d(ev), d(ac), d(bac)))
            }
            1 -> {
                var o by rememberSaveable { mutableStateOf("10") }
                var m by rememberSaveable { mutableStateOf("14") }
                var p by rememberSaveable { mutableStateOf("24") }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    NumField("Optimistic", o, { o = it }, Modifier.weight(1f))
                    NumField("Most likely", m, { m = it }, Modifier.weight(1f))
                    NumField("Pessimistic", p, { p = it }, Modifier.weight(1f))
                }
                Results(Calculators.pert(d(o), d(m), d(p)))
            }
            2 -> {
                var tc by rememberSaveable { mutableStateOf("1000000") }
                var tp by rememberSaveable { mutableStateOf("1100000") }
                var cp by rememberSaveable { mutableStateOf("1200000") }
                var share by rememberSaveable { mutableStateOf("0.8") }
                Text("Fixed price incentive fee contract", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    NumField("Target cost", tc, { tc = it }, Modifier.weight(1f)); NumField("Target price", tp, { tp = it }, Modifier.weight(1f))
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    NumField("Ceiling price", cp, { cp = it }, Modifier.weight(1f)); NumField("Buyer share (0–1)", share, { share = it }, Modifier.weight(1f))
                }
                Results(Calculators.pta(d(tc), d(tp), d(cp), d(share)))
            }
            3 -> {
                var prob by rememberSaveable { mutableStateOf("30") }
                var impact by rememberSaveable { mutableStateOf("-50000") }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    NumField("Probability %", prob, { prob = it }, Modifier.weight(1f)); NumField("Impact", impact, { impact = it }, Modifier.weight(1f))
                }
                Results(Calculators.emv(d(prob), d(impact)))
            }
            4 -> {
                var people by rememberSaveable { mutableStateOf("12") }
                NumField("People on the project", people, { people = it }, Modifier.fillMaxWidth())
                val n = d(people)
                if (!n.isNaN() && n >= 1 && n < 100000) Results(Calculators.channels(n.toInt()))
            }
            5 -> {
                var inv by rememberSaveable { mutableStateOf("500000") }
                var ben by rememberSaveable { mutableStateOf("150000") }
                var yrs by rememberSaveable { mutableStateOf("5") }
                var rate by rememberSaveable { mutableStateOf("8") }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    NumField("Investment", inv, { inv = it }, Modifier.weight(1f)); NumField("Benefit / year", ben, { ben = it }, Modifier.weight(1f))
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    NumField("Years", yrs, { yrs = it }, Modifier.weight(1f)); NumField("Discount rate %", rate, { rate = it }, Modifier.weight(1f))
                }
                val y = d(yrs)
                if (!y.isNaN() && y >= 1 && y <= 100) Results(Calculators.finance(d(inv), d(ben), y.toInt(), d(rate)))
            }
            else -> {
                var wip by rememberSaveable { mutableStateOf("24") }
                var thr by rememberSaveable { mutableStateOf("6") }
                var rem by rememberSaveable { mutableStateOf("120") }
                var vel by rememberSaveable { mutableStateOf("25") }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    NumField("Work in progress", wip, { wip = it }, Modifier.weight(1f)); NumField("Throughput / week", thr, { thr = it }, Modifier.weight(1f))
                }
                Results(Calculators.littlesLaw(d(wip), d(thr)))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    NumField("Points remaining", rem, { rem = it }, Modifier.weight(1f)); NumField("Velocity", vel, { vel = it }, Modifier.weight(1f))
                }
                Results(Calculators.sprints(d(rem), d(vel)))
            }
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun FormulaSheet(vm: PmpViewModel) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        vm.repo.extras.formulas.forEach { g ->
            SectionLabel(g.label)
            if (g.blurb.isNotBlank()) Text(g.blurb, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            g.items.forEach { f ->
                PmpCard {
                    Row {
                        Text(f.name, fontWeight = FontWeight.Bold, fontSize = 15.sp, modifier = Modifier.weight(1f))
                        if (f.tag.isNotBlank()) Text(f.tag, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }
                    Spacer(Modifier.height(6.dp))
                    Text(f.expr, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)
                    if (f.where.isNotBlank()) { Spacer(Modifier.height(4.dp)); Text(f.where, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant) }
                    if (f.reads.isNotBlank()) { Spacer(Modifier.height(4.dp)); Text(f.reads, fontSize = 14.sp, lineHeight = 20.sp) }
                    if (f.trap.isNotBlank()) {
                        Spacer(Modifier.height(6.dp))
                        Text("Trap: ${f.trap}", fontSize = 13.sp, color = ColorWarning, lineHeight = 18.sp)
                    }
                }
            }
        }
        Spacer(Modifier.height(16.dp))
    }
}

/* ── the three calculators that edit rows (index.html §10.06.6) ────────── */

/** One decision option as typed: its name, its cost, and its outcomes as (probability, payoff) text. */
private data class BranchDraft(val name: String, val cost: String, val outcomes: List<Pair<String, String>>)

@Composable
private fun TreeCalculator() {
    // The web's worked default: build new against upgrade.
    var branches by remember {
        mutableStateOf(listOf(
            BranchDraft("Build new", "500000", listOf("0.6" to "1200000", "0.4" to "400000")),
            BranchDraft("Upgrade", "180000", listOf("0.6" to "700000", "0.4" to "300000")),
        ))
    }
    fun edit(i: Int, b: BranchDraft) { branches = branches.toMutableList().also { it[i] = b } }
    Text("Roll chance nodes up to an EMV, subtract each branch's own cost, and take the best net value.",
        fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    branches.forEachIndexed { i, b ->
        PmpCard {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(b.name, { edit(i, b.copy(name = it)) }, label = { Text("Branch") }, singleLine = true, modifier = Modifier.weight(1.3f))
                NumField("Cost", b.cost, { edit(i, b.copy(cost = it)) }, Modifier.weight(1f))
            }
            b.outcomes.forEachIndexed { k, (p, pay) ->
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    NumField("Probability", p, { v -> edit(i, b.copy(outcomes = b.outcomes.toMutableList().also { it[k] = v to pay })) }, Modifier.weight(1f))
                    NumField("Payoff", pay, { v -> edit(i, b.copy(outcomes = b.outcomes.toMutableList().also { it[k] = p to v })) }, Modifier.weight(1.3f))
                }
            }
            Row {
                TextButton(onClick = { edit(i, b.copy(outcomes = b.outcomes + ("" to ""))) }) { Text("Add an outcome") }
                if (b.outcomes.size > 1) TextButton(onClick = { edit(i, b.copy(outcomes = b.outcomes.dropLast(1))) }) { Text("Remove last") }
                if (branches.size > 1) TextButton(onClick = { branches = branches.filterIndexed { j, _ -> j != i } }) { Text("Remove branch") }
            }
        }
    }
    TextButton(onClick = { branches = branches + BranchDraft("Option ${branches.size + 1}", "0", listOf("" to "")) }) { Text("Add a branch") }
    val parsed = branches.filter { it.name.isNotBlank() }.map { b ->
        TreeBranch(b.name.trim(), d(b.cost).takeIf { !it.isNaN() } ?: 0.0,
            b.outcomes.map { (p, pay) -> TreeOutcome(d(p).takeIf { !it.isNaN() } ?: 0.0, d(pay).takeIf { !it.isNaN() } ?: 0.0) })
    }
    if (parsed.isEmpty()) Text("Fill in at least one branch to see the results.", fontSize = 13.sp)
    else Results(Calculators.treeLines(parsed))
}

/** One activity as typed. */
private data class ActDraft(val id: String, val dur: String, val preds: String)

@Composable
private fun NetworkCalculator() {
    // The web's default network: C and E carry total float, and E's free float differs from C's at the merge.
    var rows by remember {
        mutableStateOf(listOf(ActDraft("A", "3", ""), ActDraft("B", "4", "A"), ActDraft("C", "2", "A"),
            ActDraft("D", "5", "B"), ActDraft("E", "3", "C"), ActDraft("F", "2", "D, E")))
    }
    fun edit(i: Int, a: ActDraft) { rows = rows.toMutableList().also { it[i] = a } }
    Text("Forward and backward passes over a small network, with total float, free float and the critical path.",
        fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    rows.forEachIndexed { i, a ->
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            OutlinedTextField(a.id, { edit(i, a.copy(id = it)) }, label = { Text("Activity") }, singleLine = true, modifier = Modifier.weight(1f))
            NumField("Days", a.dur, { edit(i, a.copy(dur = it)) }, Modifier.weight(0.9f))
            OutlinedTextField(a.preds, { edit(i, a.copy(preds = it)) }, label = { Text("After") }, singleLine = true, modifier = Modifier.weight(1.4f))
        }
    }
    Row {
        TextButton(onClick = { rows = rows + ActDraft("", "", "") }) { Text("Add an activity") }
        if (rows.size > 1) TextButton(onClick = { rows = rows.dropLast(1) }) { Text("Remove last") }
    }
    Text("Day numbering: the first activity starts on day 1, so a three-day activity beginning on day 1 finishes on day 3.",
        fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    val r = Calculators.network(rows.map { a -> NetInput(a.id, d(a.dur), a.preds.split(Regex("[,\\s]+")).filter { it.isNotBlank() }) })
    if (!r.ok) {
        r.errors.forEach { Text(it, color = ColorError, fontSize = 13.sp) }
        return
    }
    NetworkDiagram(r)
    PmpCard {
        NetRow(listOf("Act", "Dur", "ES", "EF", "LS", "LF", "TF", "FF"), header = true)
        r.acts.forEach { a ->
            NetRow(listOf(a.id + if (a.critical) " ●" else "", fmt0(a.dur), fmt0(a.es), fmt0(a.ef), fmt0(a.ls), fmt0(a.lf), fmt0(a.tf), fmt0(a.ff)), bold = a.critical)
        }
        Text("● marks the critical path (zero total float).", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
    Results(Calculators.networkLines(r))
}

private fun fmt0(v: Double): String = if (v == Math.rint(v)) v.toLong().toString() else "%.1f".format(v)

@Composable
private fun NetRow(cells: List<String>, header: Boolean = false, bold: Boolean = false) {
    Row(Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
        cells.forEachIndexed { i, c ->
            Text(c, fontSize = 13.sp, fontFamily = FontFamily.Monospace, fontWeight = if (header || bold) FontWeight.Bold else FontWeight.Normal,
                modifier = Modifier.weight(if (i == 0) 1.3f else 1f))
        }
    }
}

/**
 * Activity-on-node boxes in columns by longest chain from a start, arrows from each predecessor. Each box
 * reads ES · duration · EF across the top and LS · TF · LF beneath. The critical path is a heavier border
 * and a bold label, never colour alone; the canvas carries a text description for screen readers.
 */
@Composable
private fun NetworkDiagram(r: NetResult) {
    val line = MaterialTheme.colorScheme.outline
    val ink = MaterialTheme.colorScheme.onSurface
    val muted = MaterialTheme.colorScheme.onSurfaceVariant
    val crit = MaterialTheme.colorScheme.primary
    val fill = MaterialTheme.colorScheme.surface
    val cols = r.acts.groupBy { it.rank }.toSortedMap().values.toList()
    val maxRows = cols.maxOf { it.size }
    val boxW = 104.dp; val boxH = 66.dp; val gapX = 34.dp; val gapY = 14.dp
    val width = boxW * cols.size + gapX * (cols.size - 1)
    val height = boxH * maxRows + gapY * (maxRows - 1)
    val description = "Network diagram. " + r.acts.joinToString(". ") { a ->
        "${a.id}: ${fmt0(a.dur)} days, early ${fmt0(a.es)} to ${fmt0(a.ef)}, late ${fmt0(a.ls)} to ${fmt0(a.lf)}, total float ${fmt0(a.tf)}" +
            (if (a.preds.isNotEmpty()) ", after ${a.preds.joinToString(" and ")}" else "") + if (a.critical) ", critical" else ""
    } + ". Duration ${fmt0(r.duration)} days."
    Text("Each box reads ES · duration · EF across the top and LS · total float · LF beneath. A heavy border marks the critical path.",
        fontSize = 12.sp, color = muted)
    Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState())) {
        Canvas(Modifier.width(width).height(height).semantics { contentDescription = description }) {
            val bw = boxW.toPx(); val bh = boxH.toPx(); val gx = gapX.toPx(); val gy = gapY.toPx()
            val pos = HashMap<String, Offset>()
            cols.forEachIndexed { c, col ->
                val colH = col.size * bh + (col.size - 1) * gy
                val top = (size.height - colH) / 2
                col.forEachIndexed { k, a -> pos[a.id] = Offset(c * (bw + gx), top + k * (bh + gy)) }
            }
            // Arrows first, so boxes sit on top of them.
            val head = 6.dp.toPx()
            r.acts.forEach { a ->
                val to = pos[a.id]!!
                a.preds.forEach { p ->
                    val from = pos[p] ?: return@forEach
                    val start = Offset(from.x + bw, from.y + bh / 2)
                    val end = Offset(to.x, to.y + bh / 2)
                    val both = a.critical && r.byId(p)?.critical == true
                    drawLine(if (both) crit else line, start, end, strokeWidth = (if (both) 2.5f else 1.4f) * density)
                    val ang = kotlin.math.atan2(end.y - start.y, end.x - start.x)
                    val path = Path().apply {
                        moveTo(end.x, end.y)
                        lineTo(end.x - head * kotlin.math.cos(ang - 0.45f), end.y - head * kotlin.math.sin(ang - 0.45f))
                        lineTo(end.x - head * kotlin.math.cos(ang + 0.45f), end.y - head * kotlin.math.sin(ang + 0.45f))
                        close()
                    }
                    drawPath(path, if (both) crit else line)
                }
            }
            val small = Paint(Paint.ANTI_ALIAS_FLAG).apply { textSize = 11.sp.toPx(); textAlign = Paint.Align.CENTER; color = muted.toArgb() }
            val big = Paint(Paint.ANTI_ALIAS_FLAG).apply { textSize = 15.sp.toPx(); textAlign = Paint.Align.CENTER; color = ink.toArgb() }
            r.acts.forEach { a ->
                val o = pos[a.id]!!
                drawRoundRect(fill, o, Size(bw, bh), CornerRadius(6.dp.toPx()))
                drawRoundRect(if (a.critical) crit else line, o, Size(bw, bh), CornerRadius(6.dp.toPx()),
                    style = Stroke(width = (if (a.critical) 3f else 1.2f) * density))
                big.typeface = if (a.critical) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
                drawContext.canvas.nativeCanvas.apply {
                    val third = bw / 3
                    listOf(fmt0(a.es), fmt0(a.dur), fmt0(a.ef)).forEachIndexed { i, t -> drawText(t, o.x + third * (i + 0.5f), o.y + 14.sp.toPx(), small) }
                    drawText(a.id, o.x + bw / 2, o.y + bh / 2 + 5.sp.toPx(), big)
                    listOf(fmt0(a.ls), "TF " + fmt0(a.tf), fmt0(a.lf)).forEachIndexed { i, t -> drawText(t, o.x + third * (i + 0.5f), o.y + bh - 6.sp.toPx(), small) }
                }
            }
        }
    }
}

/** NPV, IRR and payback over a year-by-year stream (the existing Finance calculator assumes equal returns). */
@Composable
private fun CashCalculator() {
    var outlay by rememberSaveable { mutableStateOf("400000") }
    var rate by rememberSaveable { mutableStateOf("10") }
    var flows by remember { mutableStateOf(listOf("120000", "160000", "180000", "150000")) }
    Text("Discounted cash flow over an outlay and a stream of returns: NPV, IRR, payback and discounted payback.",
        fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        NumField("Initial investment", outlay, { outlay = it }, Modifier.weight(1f)); NumField("Discount rate %", rate, { rate = it }, Modifier.weight(1f))
    }
    SectionLabel("Returns by year")
    flows.forEachIndexed { i, f ->
        NumField("Year ${i + 1}", f, { v -> flows = flows.toMutableList().also { it[i] = v } }, Modifier.fillMaxWidth())
    }
    Row {
        TextButton(onClick = { flows = flows + "" }) { Text("Add a year") }
        if (flows.size > 1) TextButton(onClick = { flows = flows.dropLast(1) }) { Text("Remove last") }
    }
    val c0 = d(outlay); val r = d(rate)
    if (c0.isNaN() || r.isNaN()) Text("Fill in the outlay and the discount rate to see the results.", fontSize = 13.sp)
    else Results(Calculators.cashLines(c0, flows.map { d(it).takeIf { v -> !v.isNaN() } ?: 0.0 }, r))
}

/* ── drills (index.html §10.06.8–11) ─────────────────────────────────── */

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Drills(vm: PmpViewModel) {
    var group by rememberSaveable { mutableStateOf<String?>(null) }
    var problem by remember { mutableStateOf(FormulaDrills.next(null)) }
    var input by remember { mutableStateOf("") }
    var mark by remember { mutableStateOf<DrillMark?>(null) }
    var shown by remember { mutableStateOf(false) }
    var tally by remember { mutableStateOf(DrillTally()) }
    var empty by remember { mutableStateOf(false) }
    val revealed = mark != null || shown
    val groups = vm.repo.extras.formulas.filter { g -> FormulaDrills.GENERATORS.any { it.group == g.id } }
    val label = groups.associate { it.id to it.label }
    fun fresh(g: String?) { problem = FormulaDrills.next(g); input = ""; mark = null; shown = false; empty = false }
    val p = problem
    val numeric = remember(vm.contentVersion) { vm.pool().filter { q -> q.base == QType.FILLBLANK && q.blanks.any { it.numeric } } }

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("Calculation drills", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
        Text("A fresh problem every time, with the whole worked solution and the classic wrong turns named.",
            fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Selector("Group", listOf<Pair<String?, String>>(null to "Every group") + groups.map { it.id to it.label }, group,
            { group = it; fresh(it) })
        Text(if (tally.asked == 0) "No problems marked yet this visit."
            else "${tally.right} / ${tally.asked} correct · ${pct(tally.right.toDouble() / tally.asked)} · streak ${tally.streak}" +
                if (tally.best > tally.streak) " (best ${tally.best})" else "",
            fontSize = 13.sp, fontWeight = FontWeight.SemiBold, fontFamily = FontFamily.Monospace)

        PmpCard {
            SectionLabel("${label[p.group] ?: p.group} · ${p.label}")
            Spacer(Modifier.height(4.dp))
            Text(p.stem, fontSize = 15.sp, lineHeight = 21.sp)
            Spacer(Modifier.height(8.dp))
            p.given.forEach { (k, v) ->
                Row(Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
                    Text(k, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1f))
                    Text(v, fontSize = 13.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = input, onValueChange = { input = it; empty = false }, enabled = !revealed, singleLine = true,
                label = { Text("Your answer" + when (p.unit) { "index" -> ", as an index"; "ratio" -> ", as a ratio"; "£" -> " (£)"; "%" -> " (%)"; else -> " (${p.unit})" }) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), modifier = Modifier.fillMaxWidth(),
            )
            if (empty) Text("Type a number before checking.", fontSize = 12.sp, color = ColorWarning)
            if (!revealed) Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 8.dp)) {
                Button(onClick = {
                    val m = FormulaDrills.mark(p, input)
                    if (m.verdict == DrillVerdict.NONE) empty = true
                    else {
                        mark = m
                        tally = tally.after(m.verdict)
                        vm.recordDrill(p, m.verdict)
                    }
                }, modifier = Modifier.weight(1f)) { Text("Check") }
                OutlinedButton(onClick = {
                    shown = true
                    tally = tally.after(DrillVerdict.NONE)
                    vm.recordDrill(p, null)
                }, modifier = Modifier.weight(1f)) { Text("Show the working") }
            }
        }

        if (revealed) {
            mark?.let { m ->
                val (text, color) = when (m.verdict) {
                    DrillVerdict.RIGHT -> "Correct — ${FormulaDrills.answerText(p)}." to ColorSuccess
                    DrillVerdict.NEAR -> "Close: you answered ${FormulaDrills.fmt(m.given, p.dp)}, and the answer is ${FormulaDrills.answerText(p)}." to ColorWarning
                    else -> "Not right: you answered ${FormulaDrills.fmt(m.given, p.dp)}, and the answer is ${FormulaDrills.answerText(p)}." to ColorError
                }
                PmpCard(container = color.copy(alpha = 0.08f), border = color) {
                    Text(text, fontWeight = FontWeight.Bold, color = color)
                    m.why?.let { Text(it, fontSize = 13.sp, lineHeight = 18.sp) }
                }
            }
            PmpCard {
                SectionLabel("Worked solution")
                p.steps.forEachIndexed { i, st ->
                    Spacer(Modifier.height(8.dp))
                    Text("${i + 1}. ${st.text}", fontSize = 14.sp, lineHeight = 20.sp)
                    st.math?.let { math ->
                        Spacer(Modifier.height(4.dp))
                        Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState())) {
                            Text(math, fontFamily = FontFamily.Monospace, fontSize = 12.sp, lineHeight = 17.sp,
                                fontWeight = if (st.plain) FontWeight.Normal else FontWeight.Medium, color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
                Spacer(Modifier.height(10.dp))
                Row {
                    Text("Answer", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                    Text(FormulaDrills.answerText(p), fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
                }
                Spacer(Modifier.height(8.dp))
                Text("Where the mark goes. ${p.trap}", fontSize = 13.sp, lineHeight = 18.sp, color = ColorWarning)
            }
            Button(onClick = { fresh(group) }, modifier = Modifier.fillMaxWidth()) { Text("Another problem") }
        }

        Spacer(Modifier.height(8.dp))
        SectionLabel("From your question banks")
        Text("${numeric.size} worked calculation questions from your question banks, marked to the tolerance their authors set.",
            fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(5, 10, 20).forEach { n ->
                OutlinedButton(onClick = { vm.startPracticeWith(numeric.shuffled().take(n), "Calculation drill") }, enabled = numeric.isNotEmpty()) { Text("$n questions") }
            }
            OutlinedButton(onClick = { vm.startPractice(PracticeFilter(type = QType.FILLBLANK, which = PracticeFilter.Which.MISSED, count = 20), "Calculations I missed") }) {
                Text("Ones I got wrong")
            }
        }
        Spacer(Modifier.height(16.dp))
    }
}
