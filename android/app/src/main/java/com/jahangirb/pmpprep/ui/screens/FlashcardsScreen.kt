package com.jahangirb.pmpprep.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jahangirb.pmpprep.data.Grade
import com.jahangirb.pmpprep.ui.PmpViewModel
import com.jahangirb.pmpprep.ui.components.PmpCard
import com.jahangirb.pmpprep.ui.components.ProgressRow
import com.jahangirb.pmpprep.ui.components.SectionLabel
import com.jahangirb.pmpprep.ui.theme.ColorError
import com.jahangirb.pmpprep.ui.theme.ColorInfo
import com.jahangirb.pmpprep.ui.theme.ColorSuccess
import com.jahangirb.pmpprep.ui.theme.ColorWarning

fun days(n: Int): String = when {
    n <= 1 -> "1 day"
    n < 30 -> "$n days"
    n < 365 -> "${"%.1f".format(n / 30.0)} mo"
    else -> "${"%.1f".format(n / 365.0)} yr"
}

@Composable
fun FlashcardsScreen(vm: PmpViewModel) {
    if (vm.cardQueue != null) CardSession(vm) else DeckList(vm)
}

@Composable
private fun DeckList(vm: PmpViewModel) {
    val rev by vm.revision.collectAsState()
    val x = vm.repo.extras
    val all = remember(rev) { vm.deckCounts(null) }
    LazyColumn(Modifier.fillMaxSize().padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item {
            Spacer(Modifier.height(8.dp))
            Text("Spaced repetition (SM-2)", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
            Text("Cards come back just before you would forget them. Up to ${x.srs.newPerDay} new cards a day; a card forgotten ${x.srs.leechThreshold} times is flagged as a leech.",
                fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(8.dp))
            Button(onClick = { vm.startDeck(null) }, enabled = all.due + all.new > 0, modifier = Modifier.fillMaxWidth().height(50.dp)) {
                Text("Study all decks · ${all.due} due · ${all.new} new", fontWeight = FontWeight.Bold)
            }
        }
        items(x.decks, key = { it.id }) { d ->
            val c = remember(rev) { vm.deckCounts(d.id) }
            PmpCard {
                Text(d.label, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(d.summary, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(8.dp))
                val seen = c.total - c.new
                ProgressRow("$seen of ${c.total} studied" + if (c.leeches > 0) " · ${c.leeches} leech" else "",
                    if (c.total == 0) 0.0 else seen.toDouble() / c.total, "${c.due} due")
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { vm.startDeck(d.id) }, enabled = c.due + c.new > 0, modifier = Modifier.weight(1f)) { Text("Study") }
                    OutlinedButton(onClick = { vm.cramDeck(d.id) }, modifier = Modifier.weight(1f)) { Text("Go through all") }
                }
            }
        }
        item { Spacer(Modifier.height(16.dp)) }
    }
}

/** onDone: shown inside a guided-path session, which moves on instead of returning to the decks. */
@Composable
fun CardSession(vm: PmpViewModel, onDone: (() -> Unit)? = null) {
    val q = vm.cardQueue ?: return
    if (vm.cardIndex >= q.size) {
        Column(Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text("Session done", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(if (q.isEmpty()) "Nothing due right now. Come back tomorrow, or go through a whole deck." else "${q.size} reviews.",
                textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant)
            if (vm.newLeeches.isNotEmpty()) {
                Spacer(Modifier.height(12.dp))
                PmpCard {
                    SectionLabel("New leeches")
                    Text("You keep forgetting these. Rewrite them in your own words or study the lesson behind them.", fontSize = 13.sp)
                    vm.newLeeches.forEach { Text("• $it", fontSize = 14.sp) }
                }
            }
            Spacer(Modifier.height(16.dp))
            if (onDone != null) Button(onClick = onDone) { Text("Continue") } else Button(onClick = { vm.back() }) { Text("Back to decks") }
        }
        return
    }
    val card = q[vm.cardIndex]
    val state = vm.cardState(card.id)
    val preview = vm.srs.preview(state, System.currentTimeMillis())
    val rot by animateFloatAsState(if (vm.cardFlipped) 180f else 0f, tween(350), label = "flip")
    val deck = vm.repo.extras.decks.firstOrNull { it.id == card.deck }

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row {
            Text("${vm.cardIndex + 1} / ${q.size}", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            Text(deck?.label ?: "", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
        }
        PmpCard(
            modifier = Modifier.heightIn(min = 280.dp).graphicsLayer { rotationY = rot; cameraDistance = 12 * density }.clickable { vm.cardFlipped = !vm.cardFlipped },
        ) {
            // The back face is drawn mirrored so it reads correctly after the turn.
            Column(Modifier.fillMaxWidth().graphicsLayer { rotationY = if (rot > 90f) 180f else 0f }) {
                if (rot <= 90f) {
                    Spacer(Modifier.height(60.dp))
                    Text(card.front, fontSize = 22.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                    Spacer(Modifier.height(24.dp))
                    Text("Tap to turn over", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().alpha(0.8f))
                } else {
                    Text(card.front, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.height(10.dp))
                    Text(card.back, fontSize = 16.sp, lineHeight = 23.sp)
                    if (card.note.isNotBlank()) {
                        Spacer(Modifier.height(12.dp))
                        Text(card.note, fontSize = 14.sp, lineHeight = 20.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
        if (!vm.cardFlipped) {
            Button(onClick = { vm.cardFlipped = true }, modifier = Modifier.fillMaxWidth().height(50.dp)) { Text("Show answer") }
        } else {
            Text("How well did you remember it?", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Grade.entries.forEach { g ->
                    val color = when (g) { Grade.AGAIN -> ColorError; Grade.HARD -> ColorWarning; Grade.GOOD -> ColorInfo; Grade.EASY -> ColorSuccess }
                    Button(
                        onClick = { vm.grade(g) },
                        colors = ButtonDefaults.buttonColors(containerColor = color),
                        modifier = Modifier.weight(1f),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 4.dp, vertical = 8.dp),
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(g.title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text(if (g == Grade.AGAIN) "again today" else days(preview[g] ?: 1), fontSize = 11.sp)
                        }
                    }
                }
            }
            if (state.lapses > 0) Text("Forgotten ${state.lapses} time(s) · ease ${"%.2f".format(state.ease)}", fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
