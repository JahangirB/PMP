package com.jahangirb.pmpprep.ui.components

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jahangirb.pmpprep.data.Answer
import com.jahangirb.pmpprep.data.Blank
import com.jahangirb.pmpprep.data.Option
import com.jahangirb.pmpprep.data.QType
import com.jahangirb.pmpprep.data.Question
import com.jahangirb.pmpprep.data.Region
import com.jahangirb.pmpprep.data.ScoreResult
import com.jahangirb.pmpprep.data.Scoring
import com.jahangirb.pmpprep.ui.theme.ColorError
import com.jahangirb.pmpprep.ui.theme.ColorSuccess
import com.jahangirb.pmpprep.ui.theme.ColorWarning

private const val LETTERS = "ABCDEFGHIJKLMNOP"

/** The answer area of any question type. Set [revealed] to show what was right and why. */
@Composable
fun QuestionBody(
    q: Question,
    options: List<Option>,
    answer: Answer?,
    revealed: Boolean,
    struck: Set<String> = emptySet(),
    onStrike: ((String) -> Unit)? = null,
    onAnswer: (Answer) -> Unit,
) {
    val a = answer ?: Answer()
    when (q.base) {
        QType.SINGLE, QType.MULTI -> Choices(q, options, a, revealed, struck, onStrike, onAnswer)
        QType.MATCHING -> Matching(q, options, a, revealed, onAnswer)
        QType.HOTSPOT -> Hotspot(q, a, revealed, onAnswer)
        QType.FILLBLANK -> Blanks(q, a, revealed, onAnswer)
        else -> {}
    }
}

@Composable
fun QuestionHeader(q: Question, taskLabel: String?, highlights: Set<Int>? = null, onHighlights: ((Set<Int>) -> Unit)? = null) {
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
        DomainChip(q.domain)
        if (taskLabel != null) Pill(taskLabel, MaterialTheme.colorScheme.onSurfaceVariant, Modifier.weight(1f, fill = false))
        Pill(q.type.title, MaterialTheme.colorScheme.primary)
    }
    Spacer(Modifier.height(10.dp))
    StemText(q.stem, 16, 23, highlights, onHighlights)
    Spacer(Modifier.height(12.dp))
}

@Composable
private fun Hint(text: String) {
    Text(text, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(bottom = 8.dp))
}

private fun countWord(n: Int) = when (n) { 2 -> "two"; 3 -> "three"; 4 -> "four"; 5 -> "five"; else -> n.toString() }

/* ── single and multiple response ───────────────────────────────────────── */

@Composable
private fun Choices(
    q: Question, options: List<Option>, a: Answer, revealed: Boolean,
    struck: Set<String>, onStrike: ((String) -> Unit)?, onAnswer: (Answer) -> Unit,
) {
    val multi = q.base == QType.MULTI
    if (multi) Hint("Select ${countWord(q.correctCount)}.")
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        options.forEachIndexed { i, o ->
            val sel = o.id in a.choices
            ChoiceCard(
                letter = LETTERS[i % LETTERS.length].toString(),
                text = o.text,
                selected = sel,
                correct = o.correct,
                square = multi,
                revealed = revealed,
                rationale = o.rationale,
                missedNote = multi,
                struck = o.id in struck,
                onLongClick = onStrike?.let { f -> { f(o.id) } },
            ) {
                onAnswer(if (multi) a.copy(choices = if (sel) a.choices - o.id else a.choices + o.id) else Answer(choices = setOf(o.id)))
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChoiceCard(
    letter: String,
    text: String,
    selected: Boolean,
    correct: Boolean,
    square: Boolean,
    revealed: Boolean,
    rationale: String,
    missedNote: Boolean = false,
    /** Crossed out: long-press toggles it, as on the exam's strike-out tool. */
    struck: Boolean = false,
    onLongClick: (() -> Unit)? = null,
    onClick: () -> Unit,
) {
    val reading = LocalReading.current
    val haptic = LocalHapticFeedback.current
    val border by animateColorAsState(
        when {
            revealed && correct -> ColorSuccess
            revealed && selected -> ColorError
            selected -> MaterialTheme.colorScheme.primary
            else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
        }, label = "border",
    )
    val fill by animateColorAsState(
        when {
            revealed && correct -> ColorSuccess.copy(alpha = 0.10f)
            revealed && selected -> ColorError.copy(alpha = 0.10f)
            selected -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)
            else -> MaterialTheme.colorScheme.surface
        }, label = "fill",
    )
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = fill),
        border = BorderStroke(if (selected || (revealed && correct)) 2.dp else 1.dp, border),
        modifier = Modifier.fillMaxWidth().combinedClickable(
            enabled = !revealed, onClick = onClick,
            onLongClickLabel = if (onLongClick != null) (if (struck) "Restore option" else "Cross out option") else null,
            onLongClick = onLongClick?.let { f -> { haptic.performHapticFeedback(HapticFeedbackType.LongPress); f() } },
        ),
    ) {
        Column(modifier = Modifier.padding(14.dp).alpha(if (struck && !revealed) 0.5f else 1f)) {
            Row(verticalAlignment = Alignment.Top) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(28.dp).clip(if (square) RoundedCornerShape(6.dp) else CircleShape)
                        .background(if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant),
                ) {
                    Text(letter, fontWeight = FontWeight.Bold, fontSize = 13.sp,
                        color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Spacer(Modifier.width(12.dp))
                Text(text, fontSize = reading.size(15), lineHeight = reading.line(21), color = MaterialTheme.colorScheme.onSurface,
                    textDecoration = if (struck) TextDecoration.LineThrough else null, modifier = Modifier.weight(1f))
            }
            if (revealed && rationale.isNotBlank()) {
                Spacer(Modifier.height(10.dp))
                val lead = when {
                    correct && selected -> "Correct. "
                    correct -> if (missedNote) "Correct, and you missed it. " else "This was the answer. "
                    selected -> "Your choice. "
                    else -> ""
                }
                RationaleBox(lead, rationale, good = correct, bad = selected && !correct)
            }
        }
    }
}

@Composable
fun RationaleBox(lead: String, text: String, good: Boolean, bad: Boolean) {
    val tint = when {
        good -> ColorSuccess
        bad -> ColorError
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier.fillMaxWidth()
            .background(if (good) ColorSuccess.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
            .padding(8.dp),
    ) {
        Icon(if (good) Icons.Default.CheckCircle else Icons.Default.Cancel, contentDescription = null, tint = tint, modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(6.dp))
        Text(
            buildAnnotatedString {
                if (lead.isNotEmpty()) withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = tint)) { append(lead) }
                append(text)
            },
            fontSize = 13.sp, lineHeight = 18.sp, color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

/* ── matching and drop-down ─────────────────────────────────────────────── */

@Composable
private fun Matching(q: Question, options: List<Option>, a: Answer, revealed: Boolean, onAnswer: (Answer) -> Unit) {
    Hint(if (q.type == QType.PULLDOWN) "Complete each sentence." else "Choose a match for each item.")
    var picking by remember { mutableStateOf<String?>(null) }
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        q.prompts.forEachIndexed { i, p ->
            val chosen = options.firstOrNull { it.id == a.map[p.id] }
            val want = Scoring.matchFor(q, p.id)
            val ok = chosen != null && chosen.id == want
            PmpCard(
                container = if (!revealed) null else if (ok) ColorSuccess.copy(alpha = 0.08f) else ColorError.copy(alpha = 0.08f),
                border = if (!revealed) null else if (ok) ColorSuccess else ColorError,
            ) {
                Text("${i + 1}. ${p.text}", fontSize = 15.sp, lineHeight = 21.sp)
                Spacer(Modifier.height(8.dp))
                if (!revealed) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        border = BorderStroke(1.dp, if (chosen == null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline),
                        modifier = Modifier.fillMaxWidth().heightIn(min = 44.dp).clickable { picking = p.id },
                    ) {
                        Row(Modifier.padding(horizontal = 12.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text(chosen?.text ?: "Choose…", fontSize = 14.sp, modifier = Modifier.weight(1f),
                                color = if (chosen == null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                                fontWeight = if (chosen == null) FontWeight.SemiBold else FontWeight.Normal)
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        }
                    }
                } else {
                    Text((if (ok) "✓ " else "✗ ") + "Your answer: " + (chosen?.text ?: "none"), fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold, color = if (ok) ColorSuccess else ColorError)
                    val right = q.options.firstOrNull { it.id == want }
                    if (!ok && right != null) {
                        Spacer(Modifier.height(4.dp))
                        Text("Correct: ${right.text}", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = ColorSuccess)
                    }
                    if (right != null) {
                        Spacer(Modifier.height(8.dp))
                        RationaleBox("", right.rationale, good = ok, bad = !ok)
                    }
                }
            }
        }
        if (revealed) {
            options.filter { it.match == null }.forEach { o ->
                PmpCard {
                    Text("Not used: ${o.text}", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(6.dp))
                    RationaleBox("", o.rationale, good = false, bad = false)
                }
            }
        }
    }
    val target = picking
    if (target != null) {
        AlertDialog(
            onDismissRequest = { picking = null },
            confirmButton = {},
            dismissButton = { TextButton(onClick = { picking = null }) { Text("Cancel") } },
            title = { Text("Choose", fontWeight = FontWeight.Bold) },
            text = {
                Column(Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    options.forEach { o ->
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (a.map[target] == o.id) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.fillMaxWidth().clickable {
                                onAnswer(a.copy(map = a.map + (target to o.id)))
                                picking = null
                            },
                        ) { Text(o.text, fontSize = 14.sp, modifier = Modifier.padding(12.dp)) }
                    }
                }
            },
        )
    }
}

/* ── hotspot and graphic ────────────────────────────────────────────────── */

@Composable
private fun Hotspot(q: Question, a: Answer, revealed: Boolean, onAnswer: (Answer) -> Unit) {
    val g = q.graphic
    if (g != null && g.title.isNotBlank()) Hint(g.title)
    Hint(if (q.multiSelect) "Tap the ${countWord(q.correctCount)} correct areas, in the diagram or the list." else "Tap one area, in the diagram or the list.")
    val toggle = { id: String ->
        onAnswer(
            if (q.multiSelect) a.copy(choices = if (id in a.choices) a.choices - id else a.choices + id)
            else Answer(choices = setOf(id))
        )
    }
    PmpCard { HotspotDiagram(q, a.choices, revealed, if (revealed) null else toggle) }
    Spacer(Modifier.height(10.dp))
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        q.regions.forEach { r ->
            ChoiceCard(
                letter = r.n.toString(), text = r.label, selected = r.id in a.choices, correct = r.correct,
                square = q.multiSelect, revealed = revealed, rationale = r.rationale,
            ) { toggle(r.id) }
        }
    }
}

/**
 * Draws the regions in their viewBox coordinates with the left-to-right flow
 * arrows the web app draws. Text is drawn at a fixed readable size rather than
 * scaled with the diagram, so labels stay legible on a phone.
 */
@Composable
fun HotspotDiagram(q: Question, selected: Set<String>, revealed: Boolean, onTap: ((String) -> Unit)?) {
    val vb = q.graphic?.viewBox ?: listOf(0f, 0f, 660f, 130f)
    val (vx, vy, vw, vh) = vb
    val flow = q.graphic?.flow ?: true
    val surface = MaterialTheme.colorScheme.surface
    val surfaceVar = MaterialTheme.colorScheme.surfaceVariant
    val line = MaterialTheme.colorScheme.outline
    val primary = MaterialTheme.colorScheme.primary
    val text = MaterialTheme.colorScheme.onSurface
    val muted = MaterialTheme.colorScheme.onSurfaceVariant
    val ok = ColorSuccess
    val bad = ColorError
    val density = LocalDensity.current
    val sp = density.fontScale * density.density

    Canvas(
        modifier = Modifier.fillMaxWidth().aspectRatio((vw / vh).coerceIn(0.5f, 8f))
            .pointerInput(q.id, onTap) {
                if (onTap != null) detectTapGestures { pos ->
                    val s = size.width / vw
                    val x = pos.x / s + vx
                    val y = pos.y / s + vy
                    // The smallest region under the finger wins, so nested regions stay reachable.
                    q.regions.filter { x >= it.x && x <= it.x + it.w && y >= it.y && y <= it.y + it.h }
                        .minByOrNull { it.w * it.h }?.let { onTap(it.id) }
                }
            },
    ) {
        val s = size.width / vw
        fun sx(x: Float) = (x - vx) * s
        fun sy(y: Float) = (y - vy) * s
        if (flow) {
            val ord = q.regions.sortedBy { it.x }
            for (i in 0 until ord.size - 1) {
                val r1 = ord[i]; val r2 = ord[i + 1]
                val y = sy(r1.y + r1.h / 2)
                val x1 = sx(r1.x + r1.w); val x2 = sx(r2.x)
                if (x2 - x1 < 6 * s) continue
                val head = 5.dp.toPx()
                drawLine(muted, Offset(x1, y), Offset(x2 - head, y), strokeWidth = 1.5.dp.toPx())
                val path = Path().apply { moveTo(x2 - head, y - head * 0.6f); lineTo(x2, y); lineTo(x2 - head, y + head * 0.6f); close() }
                drawPath(path, muted)
            }
        }
        val labelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { textAlign = Paint.Align.CENTER; color = text.toArgb() }
        val numPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { typeface = Typeface.DEFAULT_BOLD; textSize = 12 * sp }
        for (r in q.regions) {
            val sel = r.id in selected
            val (fill, stroke) = when {
                revealed && r.correct -> ok.copy(alpha = 0.18f) to ok
                revealed && sel -> bad.copy(alpha = 0.18f) to bad
                sel -> primary.copy(alpha = 0.16f) to primary
                else -> surface.copy(alpha = 0.6f) to line
            }
            val tl = Offset(sx(r.x), sy(r.y))
            val sz = Size(r.w * s, r.h * s)
            val rad = CornerRadius(5.dp.toPx())
            drawRoundRect(fill, tl, sz, rad)
            drawRoundRect(stroke, tl, sz, rad, style = Stroke(width = (if (sel || (revealed && r.correct)) 2.5f else 1.2f) * density.density))
            numPaint.color = (if (stroke == line) primary else stroke).toArgb()
            drawContext.canvas.nativeCanvas.apply {
                drawText(r.n.toString(), tl.x + 4 * density.density, tl.y + 13 * sp, numPaint)
                var fontPx = 11 * sp
                var lines: List<String> = emptyList()
                while (true) {
                    labelPaint.textSize = fontPx
                    lines = wrap(r.label, labelPaint, sz.width - 6 * density.density)
                    if (lines.size * fontPx * 1.15f + 14 * sp <= sz.height || fontPx <= 8 * sp) break
                    fontPx -= 0.5f * sp
                }
                val lh = fontPx * 1.15f
                val top = tl.y + sz.height / 2 + 6 * sp - (lines.size - 1) * lh / 2 + fontPx * 0.35f
                save()
                clipRect(tl.x, tl.y, tl.x + sz.width, tl.y + sz.height)
                lines.forEachIndexed { i, ln -> drawText(ln, tl.x + sz.width / 2, top + i * lh, labelPaint) }
                restore()
            }
        }
        if (q.regions.isEmpty()) drawRect(surfaceVar)
    }
}

private fun wrap(s: String, p: Paint, max: Float): List<String> {
    val out = ArrayList<String>()
    var cur = ""
    for (w in s.split(Regex("\\s+"))) {
        val t = if (cur.isEmpty()) w else "$cur $w"
        if (p.measureText(t) > max && cur.isNotEmpty()) { out += cur; cur = w } else cur = t
    }
    if (cur.isNotEmpty()) out += cur
    return out
}

/* ── fill in the blank ──────────────────────────────────────────────────── */

@Composable
private fun Blanks(q: Question, a: Answer, revealed: Boolean, onAnswer: (Answer) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        q.blanks.forEachIndexed { i, b ->
            val value = a.map[b.id].orEmpty()
            val ok = Scoring.blankIsRight(b, value)
            PmpCard(
                container = if (!revealed) null else if (ok) ColorSuccess.copy(alpha = 0.08f) else ColorError.copy(alpha = 0.08f),
                border = if (!revealed) null else if (ok) ColorSuccess else ColorError,
            ) {
                Text(b.label, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(6.dp))
                val prefixUnit = b.unit.matches(Regex("[$£€]"))
                OutlinedTextField(
                    value = value,
                    onValueChange = { onAnswer(a.copy(map = a.map + (b.id to it))) },
                    enabled = !revealed,
                    singleLine = true,
                    prefix = if (prefixUnit) ({ Text(b.unit) }) else null,
                    suffix = if (!prefixUnit && b.unit.isNotEmpty()) ({ Text(b.unit) }) else null,
                    // The keyboard's action key moves to the next blank, and closes the keyboard on the last.
                    keyboardOptions = KeyboardOptions(keyboardType = if (b.numeric) KeyboardType.Decimal else KeyboardType.Text,
                        imeAction = if (i < q.blanks.size - 1) ImeAction.Next else ImeAction.Done),
                    modifier = Modifier.fillMaxWidth(),
                )
                if (revealed) {
                    Spacer(Modifier.height(8.dp))
                    Text(if (ok) "✓ Correct" else "✗ Answer: ${Scoring.blankAnswer(b)}", fontSize = 14.sp,
                        fontWeight = FontWeight.Bold, color = if (ok) ColorSuccess else ColorError)
                    Spacer(Modifier.height(6.dp))
                    RationaleBox("", b.rationale, good = ok, bad = !ok)
                }
            }
        }
    }
}

/* ── after checking ─────────────────────────────────────────────────────── */

@Composable
fun Feedback(q: Question, r: ScoreResult, answered: Boolean) {
    val color = when {
        r.correct -> ColorSuccess
        r.partial -> ColorWarning
        else -> ColorError
    }
    Surface(
        color = color.copy(alpha = 0.12f),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, color),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            when {
                !answered -> "Not answered"
                r.correct -> "Correct"
                r.partial -> "Partly right (${pct(r.credit)})"
                else -> "Not quite"
            },
            fontSize = 17.sp, fontWeight = FontWeight.Bold, color = color, modifier = Modifier.padding(12.dp),
        )
    }
    if (q.explanation.isNotBlank()) {
        Spacer(Modifier.height(10.dp))
        PmpCard {
            SectionLabel("Why")
            Spacer(Modifier.height(6.dp))
            Text(q.explanation, fontSize = 15.sp, lineHeight = 22.sp)
        }
    }
    if (q.mindset.isNotBlank()) {
        Spacer(Modifier.height(10.dp))
        MindsetCallout(q.mindset)
    }
}
