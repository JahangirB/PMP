package com.jahangirb.pmpprep.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jahangirb.pmpprep.listen.Listen

/** "Listen": reads [texts] aloud with the built-in offline voice. */
@Composable
fun ListenButton(label: String, title: String, modifier: Modifier = Modifier, texts: () -> List<String>) {
    val ctx = LocalContext.current
    TextButton(onClick = { Listen.play(ctx, title, texts()) }, modifier = modifier) {
        Icon(Icons.Default.Headphones, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(6.dp))
        Text(label)
    }
}

/** The mini player while something is being read: what, where, pause or resume, speed and stop. */
@Composable
fun ListenBar(modifier: Modifier = Modifier) {
    val status = Listen.status
    val error = Listen.error
    if (status == Listen.Status.IDLE && error == null) return
    Surface(
        modifier = modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.secondaryContainer,
        shadowElevation = 6.dp,
    ) {
        Row(Modifier.padding(start = 14.dp, end = 4.dp, top = 4.dp, bottom = 4.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Headphones, contentDescription = null, tint = MaterialTheme.colorScheme.onSecondaryContainer)
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                if (error != null && status == Listen.Status.IDLE) {
                    Text(error, fontSize = 13.sp, color = MaterialTheme.colorScheme.error, maxLines = 2, overflow = TextOverflow.Ellipsis)
                } else {
                    Text(Listen.title, fontWeight = FontWeight.Bold, fontSize = 14.sp, maxLines = 1, overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSecondaryContainer)
                    Text(when (status) {
                        Listen.Status.PREPARING -> "Preparing the voice…"
                        Listen.Status.PAUSED -> "Paused · part ${Listen.part + 1} of ${Listen.parts}"
                        else -> "Part ${Listen.part + 1} of ${Listen.parts}"
                    }, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSecondaryContainer)
                }
            }
            if (error == null || status != Listen.Status.IDLE) {
                // Speed applies from the next sentence.
                TextButton(onClick = { Listen.speed = when (Listen.speed) { 1f -> 1.15f; 1.15f -> 0.85f; else -> 1f } }) {
                    Text("${Listen.speed}×", fontSize = 13.sp)
                }
                when (status) {
                    Listen.Status.PREPARING -> CircularProgressIndicator(Modifier.padding(10.dp).size(24.dp), strokeWidth = 2.dp)
                    Listen.Status.PAUSED -> IconButton(onClick = { Listen.resume() }) { Icon(Icons.Default.PlayArrow, contentDescription = "Resume") }
                    else -> IconButton(onClick = { Listen.pause() }) { Icon(Icons.Default.Pause, contentDescription = "Pause") }
                }
            }
            IconButton(onClick = { Listen.stop(); Listen.error = null }) { Icon(Icons.Default.Close, contentDescription = "Stop") }
        }
    }
}
