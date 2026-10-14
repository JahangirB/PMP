package com.jahangirb.pmpprep.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** Two minutes of paced breathing: in for 4 seconds, out for 6, twelve times. One tap skips it. */
@Composable
fun BreathingReset(onDone: () -> Unit) {
    val grow = remember { Animatable(0.55f) }
    var phase by remember { mutableStateOf("Breathe in") }
    var left by remember { mutableIntStateOf(CYCLES) }
    LaunchedEffect(Unit) {
        repeat(CYCLES) {
            phase = "Breathe in"
            grow.animateTo(1f, tween(4000, easing = FastOutSlowInEasing))
            phase = "Breathe out"
            grow.animateTo(0.55f, tween(6000, easing = FastOutSlowInEasing))
            left--
        }
        onDone()
    }
    Column(Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text("A two-minute reset", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
        Text("Follow the circle. A longer breath out than in slows things down.", textAlign = TextAlign.Center, fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(32.dp))
        Box(Modifier.size(220.dp), contentAlignment = Alignment.Center) {
            Box(Modifier.size(220.dp).scale(grow.value).background(MaterialTheme.colorScheme.primaryContainer, CircleShape))
            Text(phase, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite })
        }
        Spacer(Modifier.height(24.dp))
        Text("$left breaths to go", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(12.dp))
        TextButton(onClick = onDone) { Text("I'm ready, start now") }
    }
}

private const val CYCLES = 12
