package com.jahangirb.pmpprep.ui.components

import androidx.compose.foundation.BorderStroke
import com.jahangirb.pmpprep.ui.theme.kineticEnter
import com.jahangirb.pmpprep.ui.theme.clay
import com.jahangirb.pmpprep.ui.theme.ClayShade
import com.jahangirb.pmpprep.ui.theme.ClayRim
import com.jahangirb.pmpprep.ui.theme.ClayRadius
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.LocalContentColor
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jahangirb.pmpprep.data.Domain
import com.jahangirb.pmpprep.data.EcoTask
import com.jahangirb.pmpprep.ui.theme.BusinessAccent
import com.jahangirb.pmpprep.ui.theme.ColorPaceDanger
import com.jahangirb.pmpprep.ui.theme.ColorPaceGood
import com.jahangirb.pmpprep.ui.theme.ColorPaceWarn
import com.jahangirb.pmpprep.ui.theme.PeopleAccent
import com.jahangirb.pmpprep.ui.theme.ProcessAccent

@Composable
fun domainColor(d: Domain?): Color = when (d) {
    Domain.PEOPLE -> PeopleAccent
    Domain.PROCESS -> ProcessAccent
    Domain.BUSINESS -> BusinessAccent
    null -> Color.Gray
}

fun pct(v: Double): String = "${Math.round(v * 100)}%"

fun clock(ms: Long): String {
    val s = (ms / 1000).coerceAtLeast(0)
    val h = s / 3600
    val m = (s % 3600) / 60
    val ss = s % 60
    return if (h > 0) "%d:%02d:%02d".format(h, m, ss) else "%d:%02d".format(m, ss)
}

@Composable
fun Pill(text: String, color: Color, modifier: Modifier = Modifier, filled: Boolean = false) {
    Surface(
        color = if (filled) color else color.copy(alpha = 0.12f),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, color.copy(alpha = 0.45f)),
        modifier = modifier,
    ) {
        Text(
            text = text,
            color = if (filled) com.jahangirb.pmpprep.ui.theme.OnStatus else color,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
        )
    }
}

@Composable
fun DomainChip(domain: Domain, weight: Double? = null, modifier: Modifier = Modifier) {
    Pill(if (weight != null) "${domain.short} (${pct(weight)})" else domain.short, domainColor(domain), modifier)
}

@Composable
fun TaskChip(task: EcoTask, modifier: Modifier = Modifier) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)),
        modifier = modifier,
    ) {
        Text(
            text = "${task.code}: ${task.label}",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
        )
    }
}

@Composable
fun SectionLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text.uppercase(),
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        letterSpacing = 1.sp,
        modifier = modifier,
    )
}

/** A clay card. Tappable cards float up while pressed, pop back on release, and light where touched. */
@Composable
fun PmpCard(modifier: Modifier = Modifier, onClick: (() -> Unit)? = null, container: Color? = null, border: Color? = null, content: @Composable ColumnScope.() -> Unit) {
    val shape = RoundedCornerShape(ClayRadius)
    val bg = container ?: MaterialTheme.colorScheme.surface
    val interaction = remember(onClick != null) { if (onClick != null) MutableInteractionSource() else null }
    CompositionLocalProvider(LocalContentColor provides contentColorFor(bg)) {
        Column(
            modifier = modifier.fillMaxWidth()
                .kineticEnter()
                .clay(shape, bg, border ?: ClayRim, ClayShade, MaterialTheme.colorScheme.primary, interaction)
                .let { if (onClick != null && interaction != null) it.clickable(interaction, LocalIndication.current, onClick = onClick) else it }
                .padding(horizontal = 20.dp, vertical = 18.dp),
            content = content,
        )
    }
}
@Composable
fun ProgressRow(label: String, value: Double, right: String, color: Color = MaterialTheme.colorScheme.primary) {
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f), maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(right, fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, color = color)
        }
        Spacer(Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { value.toFloat().coerceIn(0f, 1f) },
            color = color,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
        )
    }
}

@Composable
fun PacingBar(secondsSpent: Long, targetSeconds: Int, modifier: Modifier = Modifier) {
    val ratio = secondsSpent.toFloat() / targetSeconds
    val color = when {
        ratio <= 0.75f -> ColorPaceGood
        ratio <= 1f -> ColorPaceWarn
        else -> ColorPaceDanger
    }
    Column(modifier = modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Speed, contentDescription = null, tint = color, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(4.dp))
            Text("Pace: ${secondsSpent}s / ${targetSeconds}s target", fontSize = 12.sp, fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Medium, color = color, modifier = Modifier.weight(1f))
            if (ratio > 1f) Text("Over target", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ColorPaceDanger)
        }
        Spacer(Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { (ratio / 1.5f).coerceIn(0f, 1f) },
            color = color,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
        )
    }
}

@Composable
fun MindsetCallout(mindset: String, modifier: Modifier = Modifier) {
    if (mindset.isBlank()) return
    Surface(
        color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.7f),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f)),
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
            Icon(Icons.Default.Lightbulb, contentDescription = null, tint = MaterialTheme.colorScheme.onTertiaryContainer, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(10.dp))
            Column {
                Text("MINDSET", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onTertiaryContainer, letterSpacing = 0.8.sp)
                Spacer(Modifier.height(2.dp))
                Text(mindset, fontSize = 13.sp, fontWeight = FontWeight.Medium, fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.onTertiaryContainer, lineHeight = 18.sp)
            }
        }
    }
}

/** A labelled drop-down that wraps long choices, built on DropdownMenu so it needs no experimental API. */
@Composable
fun <T> Selector(label: String, options: List<Pair<T, String>>, selected: T, onSelect: (T) -> Unit, modifier: Modifier = Modifier) {
    var open by remember { mutableStateOf(false) }
    Column(modifier = modifier) {
        Text(label, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(4.dp))
        Box {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp).clickable { open = true },
            ) {
                Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(options.firstOrNull { it.first == selected }?.second ?: "", fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.weight(1f))
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }
            }
            DropdownMenu(expanded = open, onDismissRequest = { open = false }) {
                options.forEach { (value, text) ->
                    DropdownMenuItem(text = { Text(text, fontSize = 14.sp) }, onClick = { onSelect(value); open = false })
                }
            }
        }
    }
}

@Composable
fun BulletList(items: List<String>, color: Color = MaterialTheme.colorScheme.onSurface, bullet: String = "•") {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        items.forEach {
            Row {
                Text("$bullet ", fontSize = 14.sp, color = color)
                Text(it, fontSize = 14.sp, color = color, lineHeight = 20.sp)
            }
        }
    }
}

@Composable
fun EmptyNote(text: String) {
    Text(text, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(vertical = 24.dp))
}
