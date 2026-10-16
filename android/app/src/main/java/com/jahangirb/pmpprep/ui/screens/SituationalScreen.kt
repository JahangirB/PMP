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
import androidx.compose.foundation.verticalScroll
import com.jahangirb.pmpprep.ui.components.ClayButton as Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import com.jahangirb.pmpprep.ui.components.ClayOutlinedButton as OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jahangirb.pmpprep.data.Domain
import com.jahangirb.pmpprep.data.Rank
import com.jahangirb.pmpprep.data.Scenario
import com.jahangirb.pmpprep.ui.PmpViewModel
import com.jahangirb.pmpprep.ui.components.ChoiceCard
import com.jahangirb.pmpprep.ui.components.DomainChip
import com.jahangirb.pmpprep.ui.components.MindsetCallout
import com.jahangirb.pmpprep.ui.components.Pill
import com.jahangirb.pmpprep.ui.components.PmpCard
import com.jahangirb.pmpprep.ui.components.SectionLabel
import com.jahangirb.pmpprep.ui.components.pct
import com.jahangirb.pmpprep.ui.theme.ColorError
import com.jahangirb.pmpprep.ui.theme.ColorInfo
import com.jahangirb.pmpprep.ui.theme.ColorSuccess
import com.jahangirb.pmpprep.ui.theme.ColorWarning

@Composable
fun rankColor(r: Rank): Color = when (r) {
    Rank.BEST -> ColorSuccess
    Rank.ACCEPTABLE -> ColorInfo
    Rank.PREMATURE -> ColorWarning
    Rank.WRONG -> ColorError
}

@Composable
fun SituationalScreen(vm: PmpViewModel) {
    val rev by vm.revision.collectAsState()
    val list = remember(vm.scenarioDomain) { vm.scenarios() }
    val runs = vm.store.scenarioRuns
    val avg = if (runs.isEmpty()) null else runs.sumOf { vm.repo.extras.rules.credit(it.rank) } / runs.size
    val scroll = rememberScrollState()
    if (list.isEmpty()) return
    val i = vm.scenarioIndex.coerceIn(0, list.size - 1)
    val sc = list[i]
    var revealed by remember(sc.id) { mutableStateOf(false) }
    LaunchedEffect(sc.id) { scroll.scrollTo(0) }

    Column(Modifier.fillMaxSize().verticalScroll(scroll).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Every option is tempting. Pick the one a capable project manager would take, then see how each ranks.",
            fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            FilterChip(selected = vm.scenarioDomain == null, onClick = { vm.scenarioDomain = null; vm.scenarioIndex = 0; vm.scenarioPick = null }, label = { Text("All") })
            Domain.entries.forEach { d ->
                FilterChip(selected = vm.scenarioDomain == d, onClick = { vm.scenarioDomain = d; vm.scenarioIndex = 0; vm.scenarioPick = null }, label = { Text(d.short) })
            }
        }
        Row {
            Text("Scenario ${i + 1} of ${list.size}", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            if (rev >= 0 && avg != null) Text("Your average: ${pct(avg)} over ${runs.size}", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
        }
        ScenarioBlock(vm, sc, revealed, vm.scenarioPick) { vm.scenarioPick = it }
        if (!revealed) {
            Button(onClick = { vm.submitScenario(sc); revealed = true }, enabled = vm.scenarioPick != null, modifier = Modifier.fillMaxWidth()) {
                Text("Reveal the ranking", fontWeight = FontWeight.Bold)
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = { vm.scenarioIndex = (i - 1 + list.size) % list.size; vm.scenarioPick = null }, modifier = Modifier.weight(1f)) { Text("Previous") }
            Button(onClick = { vm.scenarioIndex = (i + 1) % list.size; vm.scenarioPick = null }, modifier = Modifier.weight(1f)) { Text("Next scenario") }
        }
        Spacer(Modifier.height(16.dp))
    }
}

/** A ranked dilemma: used here and for the worked example in each lesson. */
@Composable
fun ScenarioBlock(vm: PmpViewModel, sc: Scenario, revealed: Boolean, pick: String?, onPick: (String) -> Unit) {
    val task = vm.repo.task(sc.ecoTask)
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        sc.domain?.let { DomainChip(it) }
        if (task != null) Pill("${task.code} · ${task.label}", MaterialTheme.colorScheme.onSurfaceVariant)
        sc.approach?.let { Pill(it.title, MaterialTheme.colorScheme.primary) }
    }
    Text(sc.stem, fontSize = 16.sp, lineHeight = 23.sp)
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        sc.options.forEachIndexed { i, o ->
            ChoiceCard(
                letter = "ABCDEFGH"[i % 8].toString(), text = o.text, selected = pick == o.id, correct = o.rank == Rank.BEST,
                square = false, revealed = revealed, rationale = "",
            ) { onPick(o.id) }
            if (revealed) {
                PmpCard(container = rankColor(o.rank).copy(alpha = 0.08f), border = rankColor(o.rank)) {
                    Pill(o.rank.title + if (pick == o.id) " · your pick" else "", rankColor(o.rank), filled = true)
                    Spacer(Modifier.height(6.dp))
                    Text(o.why, fontSize = 13.sp, lineHeight = 18.sp)
                }
            }
        }
    }
    if (revealed) {
        val picked = sc.options.firstOrNull { it.id == pick }
        if (picked != null) {
            Text("Credit for your pick: ${pct(vm.repo.extras.rules.credit(picked.rank))}", fontWeight = FontWeight.Bold, color = rankColor(picked.rank))
        }
        if (sc.explanation.isNotBlank()) PmpCard { SectionLabel("Why"); Spacer(Modifier.height(6.dp)); Text(sc.explanation, fontSize = 15.sp, lineHeight = 22.sp) }
        MindsetCallout(sc.mindset)
    }
}
