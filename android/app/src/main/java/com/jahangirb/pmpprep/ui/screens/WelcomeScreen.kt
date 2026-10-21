package com.jahangirb.pmpprep.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jahangirb.pmpprep.data.Welcome
import com.jahangirb.pmpprep.ui.Dest
import com.jahangirb.pmpprep.ui.PmpViewModel
import com.jahangirb.pmpprep.ui.theme.ColorWarning

/**
 * Before you start (index.html §10.38): an opening screen that asks the
 * person to read carefully, then one screen per section, moved with Next and
 * Back. On the path it comes before the setup, once; Menu → Before you start
 * opens it again.
 */
@Composable
fun WelcomeScreen(vm: PmpViewModel) {
    val w = vm.repo.extras.welcome ?: return
    var at by rememberSaveable { mutableIntStateOf(0) }
    val n = w.sections.size + 1
    val last = at == n - 1
    val scroll = rememberScrollState()
    LaunchedEffect(at) { scroll.scrollTo(0) }
    Column(Modifier.fillMaxSize()) {
        Row(Modifier.fillMaxWidth().padding(start = 20.dp, end = 20.dp, top = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("BEFORE YOU START", fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.weight(1f))
            Text("${at + 1} of $n", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Column(Modifier.weight(1f).verticalScroll(scroll).padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            if (at == 0) Opening(w) else Section(w, at - 1, last)
        }
        Column(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Dots(n, at)
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = { if (at > 0) at-- }, enabled = at > 0, modifier = Modifier.alpha(if (at > 0) 1f else 0f)) {
                    Text("Back", fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.width(8.dp))
                Button(onClick = {
                    if (!last) { at++; return@Button }
                    val setup = vm.setupDue
                    vm.markWelcomeRead()
                    at = 0
                    // From the menu: back where it was opened, or to the path when the setup is still due.
                    if (vm.screen == Dest.WELCOME) { if (setup) vm.home() else vm.back() }
                }, modifier = Modifier.weight(1f).height(52.dp)) {
                    Text(if (!last) "Next" else if (vm.setupDue) w.button else w.done, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun Opening(w: Welcome) {
    Surface(shape = RoundedCornerShape(16.dp), color = ColorWarning.copy(alpha = 0.12f), border = BorderStroke(2.dp, ColorWarning.copy(alpha = 0.6f))) {
        Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Icon(Icons.Outlined.WarningAmber, contentDescription = null, tint = ColorWarning, modifier = Modifier.size(26.dp))
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(w.warn, fontSize = 19.sp, fontWeight = FontWeight.Black, lineHeight = 24.sp, color = ColorWarning)
                Text(w.warnSub, fontSize = 14.sp, lineHeight = 20.sp)
            }
        }
    }
    Text(w.title, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, lineHeight = 30.sp, modifier = Modifier.semantics { heading() })
    Text(w.intro, fontSize = 16.sp, lineHeight = 24.sp)
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        w.sections.forEachIndexed { i, s ->
            Text("${i + 1}.  ${s.h}", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun Section(w: Welcome, i: Int, last: Boolean) {
    val s = w.sections[i]
    Card(
        shape = RoundedCornerShape(18.dp),
        border = if (s.main) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            if (s.main) Text("MOST IMPORTANT", fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp, color = MaterialTheme.colorScheme.primary)
            Text(s.h, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, lineHeight = 26.sp, modifier = Modifier.semantics { heading() })
            if (s.p.isNotEmpty()) Text(s.p, fontSize = 16.sp, lineHeight = 24.sp)
            s.items.forEach { item ->
                Row {
                    Text("•", fontSize = 16.sp, modifier = Modifier.width(18.dp))
                    Text(item, fontSize = 16.sp, lineHeight = 23.sp)
                }
            }
        }
    }
    if (last) Text(w.again, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.fillMaxWidth())
}

@Composable
private fun Dots(n: Int, at: Int) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        repeat(n) { i ->
            val c = MaterialTheme.colorScheme.primary
            Box(
                Modifier.padding(horizontal = 3.dp).size(width = if (i == at) 20.dp else 8.dp, height = 8.dp)
                    .background(
                        when { i == at -> c; i < at -> c.copy(alpha = 0.45f); else -> MaterialTheme.colorScheme.outlineVariant },
                        RoundedCornerShape(4.dp),
                    )
            )
        }
    }
}
