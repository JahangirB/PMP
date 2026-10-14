package com.jahangirb.pmpprep.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import com.jahangirb.pmpprep.ui.components.ClayOutlinedButton as OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jahangirb.pmpprep.data.Domain
import com.jahangirb.pmpprep.ui.PmpViewModel
import com.jahangirb.pmpprep.ui.PracticeFilter
import com.jahangirb.pmpprep.ui.components.BulletList
import com.jahangirb.pmpprep.ui.components.DomainChip
import com.jahangirb.pmpprep.ui.components.PmpCard
import com.jahangirb.pmpprep.ui.components.SectionLabel
import com.jahangirb.pmpprep.ui.components.pct
import com.jahangirb.pmpprep.ui.theme.ColorWarning

@Composable
fun ReferenceScreen(vm: PmpViewModel) {
    var tab by rememberSaveable { mutableIntStateOf(0) }
    val tabs = listOf("ECO tasks", "Glossary", "Abbreviations", "Principles", "Perf. domains", "Processes", "Agile", "Tables")
    Column(Modifier.fillMaxSize()) {
        ScrollableTabRow(selectedTabIndex = tab, edgePadding = 8.dp) {
            tabs.forEachIndexed { i, t -> Tab(selected = tab == i, onClick = { tab = i }, text = { Text(t) }) }
        }
        when (tab) {
            0 -> EcoTasks(vm)
            1 -> Glossary(vm)
            2 -> Abbreviations(vm)
            3 -> Principles(vm)
            4 -> PerfDomains(vm)
            5 -> Processes(vm)
            6 -> Agile(vm)
            else -> Tables(vm)
        }
    }
}

@Composable
private fun SearchBox(value: String, hint: String, onChange: (String) -> Unit) {
    OutlinedTextField(
        value = value, onValueChange = onChange, singleLine = true, placeholder = { Text(hint) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
    )
}

@Composable
private fun EcoTasks(vm: PmpViewModel) {
    val rev by vm.revision.collectAsState()
    val x = vm.repo.extras
    val pool = remember(vm.contentVersion) { vm.pool() }
    val byTask = remember(rev) { vm.readiness.group(vm.store.attempts) { it.ecoTask } }
    LazyColumn(Modifier.fillMaxSize().padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Domain.entries.forEach { d ->
            item {
                Spacer(Modifier.height(8.dp))
                Row { DomainChip(d, x.domainWeights[d]) }
            }
            items(x.tasks.filter { it.domain == d }, key = { it.id }) { t ->
                val g = byTask[t.id]
                val n = pool.count { it.ecoTask == t.id }
                PmpCard {
                    Text("${t.code} · ${t.label}", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Text(t.summary, fontSize = 14.sp, lineHeight = 20.sp)
                    Spacer(Modifier.height(6.dp))
                    Row {
                        Text("$n questions" + if (g != null) " · you: ${pct(g.accuracy)} over ${g.n}" else " · not practised yet",
                            fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1f))
                        OutlinedButton(onClick = { vm.startPractice(PracticeFilter(task = t.id, count = 10), "Practice: ${t.label}") }, enabled = n > 0) {
                            Text("Practise")
                        }
                    }
                }
            }
        }
        item { Spacer(Modifier.height(16.dp)) }
    }
}

@Composable
private fun Glossary(vm: PmpViewModel) {
    var q by rememberSaveable { mutableStateOf("") }
    val terms = vm.repo.extras.glossary
    val shown = remember(q) {
        val n = q.trim().lowercase()
        if (n.isEmpty()) terms else terms.filter { it.term.lowercase().contains(n) } + terms.filter { !it.term.lowercase().contains(n) && it.definition.lowercase().contains(n) }
    }
    Column(Modifier.fillMaxSize()) {
        SearchBox(q, "Search ${terms.size} terms") { q = it }
        LazyColumn(Modifier.fillMaxSize().padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(shown) { t ->
                PmpCard {
                    Row {
                        Text(t.term, fontWeight = FontWeight.Bold, fontSize = 15.sp, modifier = Modifier.weight(1f))
                        Text(t.group, fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)
                    }
                    Text(t.definition, fontSize = 14.sp, lineHeight = 20.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

@Composable
private fun Abbreviations(vm: PmpViewModel) {
    var q by rememberSaveable { mutableStateOf("") }
    val all = vm.repo.extras.abbreviations
    val shown = remember(q) {
        val n = q.trim().lowercase()
        if (n.isEmpty()) all else all.filter { it.abbr.lowercase().contains(n) || it.full.lowercase().contains(n) }
    }
    Column(Modifier.fillMaxSize()) {
        SearchBox(q, "Search ${all.size} abbreviations") { q = it }
        LazyColumn(Modifier.fillMaxSize().padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            items(shown) { a ->
                Row(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Text(a.abbr, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary, modifier = Modifier.width(76.dp))
                    Column(Modifier.weight(1f)) {
                        Text(a.full, fontSize = 14.sp)
                        if (a.note.isNotBlank()) Text(a.note, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}

@Composable
private fun Principles(vm: PmpViewModel) {
    LazyColumn(Modifier.fillMaxSize().padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item { Spacer(Modifier.height(8.dp)); SectionLabel("The 12 principles of project management") }
        items(vm.repo.extras.reference.principles) { p ->
            PmpCard {
                Text("${p.n}. ${p.label}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(p.line, fontSize = 14.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(6.dp))
                Text(p.body, fontSize = 14.sp, lineHeight = 20.sp)
                if (p.practice.isNotEmpty()) { Spacer(Modifier.height(6.dp)); BulletList(p.practice) }
                if (p.trap.isNotBlank()) { Spacer(Modifier.height(6.dp)); Text("Trap: ${p.trap}", fontSize = 13.sp, color = ColorWarning) }
            }
        }
        item { Spacer(Modifier.height(16.dp)) }
    }
}

@Composable
private fun PerfDomains(vm: PmpViewModel) {
    LazyColumn(Modifier.fillMaxSize().padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item { Spacer(Modifier.height(8.dp)); SectionLabel("Project performance domains") }
        items(vm.repo.extras.reference.domains) { d ->
            PmpCard {
                Text("${d.n}. ${d.label}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(d.line, fontSize = 14.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(6.dp))
                Text(d.body, fontSize = 14.sp, lineHeight = 20.sp)
                if (d.outcomes.isNotEmpty()) { Spacer(Modifier.height(6.dp)); SectionLabel("Outcomes"); BulletList(d.outcomes) }
                if (d.checks.isNotEmpty()) { Spacer(Modifier.height(6.dp)); SectionLabel("Ask yourself"); BulletList(d.checks, bullet = "?") }
            }
        }
        item { Spacer(Modifier.height(16.dp)) }
    }
}

@Composable
private fun Processes(vm: PmpViewModel) {
    val ref = vm.repo.extras.reference
    var area by rememberSaveable { mutableStateOf<String?>(null) }
    var open by rememberSaveable { mutableStateOf<String?>(null) }
    val list = ref.processes.filter { area == null || it.area == area }
    Column(Modifier.fillMaxSize()) {
        Row(Modifier.horizontalScroll(rememberScrollState()).padding(horizontal = 12.dp, vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Chip("All ${ref.processes.size}", area == null) { area = null }
            ref.areas.forEach { a -> Chip(a.label, area == a.id) { area = a.id } }
        }
        LazyColumn(Modifier.fillMaxSize().padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(list, key = { it.id }) { p ->
                val group = ref.groups.firstOrNull { it.id == p.group }?.label.orEmpty()
                PmpCard(onClick = { open = if (open == p.id) null else p.id }) {
                    Text("${p.n} ${p.name}", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Text(group, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                    Text(p.purpose, fontSize = 14.sp, lineHeight = 20.sp)
                    if (open == p.id) {
                        Spacer(Modifier.height(8.dp)); SectionLabel("Inputs"); BulletList(p.inputs)
                        Spacer(Modifier.height(8.dp)); SectionLabel("Tools and techniques"); BulletList(p.tools)
                        Spacer(Modifier.height(8.dp)); SectionLabel("Outputs"); BulletList(p.outputs)
                        if (p.key.isNotBlank()) { Spacer(Modifier.height(8.dp)); Text(p.key, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = ColorWarning) }
                    } else {
                        Text("Tap for inputs, tools and outputs", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
            item { Spacer(Modifier.height(16.dp)) }
        }
    }
}

@Composable
private fun Chip(text: String, selected: Boolean, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier.clickable(onClick = onClick),
    ) {
        Text(text, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface)
    }
}

@Composable
private fun Agile(vm: PmpViewModel) {
    LazyColumn(Modifier.fillMaxSize().padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        vm.repo.extras.reference.agile.forEach { g ->
            item {
                Spacer(Modifier.height(8.dp))
                SectionLabel(g.label)
                Text(g.blurb, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            items(g.items) { it2 ->
                PmpCard {
                    Text(it2.name, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Text(it2.body, fontSize = 14.sp, lineHeight = 20.sp)
                    if (it2.points.isNotEmpty()) { Spacer(Modifier.height(6.dp)); BulletList(it2.points) }
                }
            }
        }
        item { Spacer(Modifier.height(16.dp)) }
    }
}

@Composable
private fun Tables(vm: PmpViewModel) {
    LazyColumn(Modifier.fillMaxSize().padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        items(vm.repo.extras.reference.tables) { t ->
            PmpCard {
                Text(t.label, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                if (t.blurb.isNotBlank()) Text(t.blurb, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                // Rows read as stacked cards: a phone is too narrow for a four-column table.
                t.rows.forEach { row ->
                    Spacer(Modifier.height(8.dp))
                    Column(Modifier.fillMaxWidth()) {
                        row.forEachIndexed { i, cell ->
                            if (i == 0) Text(cell, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.primary)
                            else Text("${t.cols.getOrNull(i) ?: ""}: $cell", fontSize = 13.sp, lineHeight = 18.sp)
                        }
                    }
                }
                if (t.note.isNotBlank()) { Spacer(Modifier.height(8.dp)); Text(t.note, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant) }
            }
        }
        item { Spacer(Modifier.height(16.dp)) }
    }
}
