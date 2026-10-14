package com.jahangirb.pmpprep.ui.components

import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jahangirb.pmpprep.ui.theme.ColorWarning

/** Reading comfort: text size, line spacing and the reading guide, for questions and lessons. */
data class ReadingPrefs(val scale: Float = 1f, val roomy: Boolean = false, val guide: Boolean = false) {
    fun size(base: Int): TextUnit = (base * scale).sp
    /** [base] is the normal line height for the normal size. */
    fun line(base: Int): TextUnit = (base * scale * if (roomy) 1.18f else 1f).sp

    companion object {
        val SIZES = listOf(1f to "Normal", 1.15f to "Large", 1.3f to "Larger")
    }
}

val LocalReading = compositionLocalOf { ReadingPrefs() }

/**
 * Question text with the exam's highlighter: long-press a word and drag to mark words like NEXT or
 * FIRST; long-press a marked word to clear its run. With the reading guide on, a soft band sits on
 * one line and a tap moves it.
 */
@Composable
fun StemText(text: String, baseSize: Int, baseLine: Int, highlights: Set<Int>? = null, onHighlights: ((Set<Int>) -> Unit)? = null) {
    val prefs = LocalReading.current
    val words = remember(text) { Regex("\\S+").findAll(text).map { it.range }.toList() }
    var layout by remember { mutableStateOf<TextLayoutResult?>(null) }
    var guideLine by remember(text) { mutableIntStateOf(0) }
    var preview by remember(text) { mutableStateOf<Pair<IntRange, Boolean>?>(null) }
    val marks by rememberUpdatedState(highlights.orEmpty())
    val onMarks by rememberUpdatedState(onHighlights)
    val haptic = LocalHapticFeedback.current
    val marker = ColorWarning.copy(alpha = 0.35f)
    val band = MaterialTheme.colorScheme.primary.copy(alpha = 0.10f)

    fun wordAt(o: Offset): Int? {
        val l = layout ?: return null
        val c = l.getOffsetForPosition(o)
        return words.indexOfLast { it.first <= c }.takeIf { it >= 0 }
    }
    fun shown(): Set<Int> {
        val p = preview ?: return marks
        return if (p.second) marks + p.first else marks - p.first.toSet()
    }

    val annotated = buildAnnotatedString {
        val on = shown()
        var i = 0
        words.forEachIndexed { w, r ->
            append(text.substring(i, r.first))
            if (w in on) withStyle(SpanStyle(background = marker)) { append(text.substring(r.first, r.last + 1)) }
            else append(text.substring(r.first, r.last + 1))
            i = r.last + 1
        }
        append(text.substring(i))
    }
    var mod = Modifier.fillMaxWidth()
    if (prefs.guide) mod = mod
        .drawBehind {
            val l = layout ?: return@drawBehind
            val line = guideLine.coerceIn(0, (l.lineCount - 1).coerceAtLeast(0))
            val pad = 3.dp.toPx()
            drawRoundRect(band, Offset(-pad, l.getLineTop(line) - pad / 2), Size(size.width + 2 * pad, l.getLineBottom(line) - l.getLineTop(line) + pad),
                CornerRadius(6.dp.toPx()))
        }
        .pointerInput(text) {
            detectTapGestures { o -> layout?.let { guideLine = it.getLineForVerticalPosition(o.y) } }
        }
    if (onHighlights != null) mod = mod.pointerInput(text) {
        var start = 0
        detectDragGesturesAfterLongPress(
            onDragStart = begin@{ o ->
                val w = wordAt(o) ?: return@begin
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                start = w
                preview = w..w to (w !in marks)
                if (w in marks) {
                    // Clear the whole marked run this word belongs to.
                    var a = w; var b = w
                    while (a - 1 in marks) a--
                    while (b + 1 in marks) b++
                    preview = a..b to false
                }
            },
            onDrag = move@{ change, _ ->
                val p = preview ?: return@move
                if (!p.second) return@move
                val w = wordAt(change.position) ?: return@move
                preview = minOf(start, w)..maxOf(start, w) to true
            },
            onDragEnd = { onMarks?.invoke(shown()); preview = null },
            onDragCancel = { preview = null },
        )
    }
    Text(annotated, fontSize = prefs.size(baseSize), lineHeight = prefs.line(baseLine), color = MaterialTheme.colorScheme.onSurface,
        onTextLayout = { layout = it }, modifier = mod)
}
