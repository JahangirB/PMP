package com.jahangirb.pmpprep.ui.screens

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jahangirb.pmpprep.data.CalcLine
import com.jahangirb.pmpprep.data.Calculators
import com.jahangirb.pmpprep.data.QType
import com.jahangirb.pmpprep.ui.PmpViewModel
import com.jahangirb.pmpprep.ui.PracticeFilter
import com.jahangirb.pmpprep.ui.components.PmpCard
import com.jahangirb.pmpprep.ui.components.SectionLabel
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

@Composable
private fun CalculatorTab() {
    var calc by rememberSaveable { mutableIntStateOf(0) }
    val names = listOf("EVM", "PERT", "PTA", "EMV", "Channels", "Finance", "Agile")
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            names.take(4).forEachIndexed { i, n -> FilterChip(selected = calc == i, onClick = { calc = i }, label = { Text(n) }) }
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            names.drop(4).forEachIndexed { i, n -> FilterChip(selected = calc == i + 4, onClick = { calc = i + 4 }, label = { Text(n) }) }
        }
        when (calc) {
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

@Composable
private fun Drills(vm: PmpViewModel) {
    val numeric = remember(vm.contentVersion) { vm.pool().filter { q -> q.base == QType.FILLBLANK && q.blanks.any { it.numeric } } }
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Calculation drills", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
        Text("${numeric.size} worked calculation questions from your question banks, marked to the tolerance their authors set.",
            fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        listOf(5, 10, 20).forEach { n ->
            Button(onClick = { vm.startPracticeWith(numeric.shuffled().take(n), "Calculation drill") }, enabled = numeric.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()) { Text("$n calculation questions") }
        }
        Button(onClick = { vm.startPractice(PracticeFilter(type = QType.FILLBLANK, which = PracticeFilter.Which.MISSED, count = 20), "Calculations I missed") },
            modifier = Modifier.fillMaxWidth()) { Text("Calculations I got wrong") }
    }
}
