package com.jahangirb.pmpprep.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** **bold**, *italic*, `code`, the light inline markup Gemini replies use. */
fun inlineMarkdown(text: String, codeBg: Color): AnnotatedString = buildAnnotatedString {
    val re = Regex("\\*\\*(.+?)\\*\\*|__(.+?)__|`([^`]+)`|(?<![*\\w])\\*(?!\\s)(.+?)(?<!\\s)\\*(?!\\*)")
    var last = 0
    for (m in re.findAll(text)) {
        if (m.range.first > last) append(text.substring(last, m.range.first))
        val g = m.groupValues
        when {
            g[1].isNotEmpty() -> withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append(g[1]) }
            g[2].isNotEmpty() -> withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append(g[2]) }
            g[3].isNotEmpty() -> withStyle(SpanStyle(fontFamily = FontFamily.Monospace, background = codeBg)) { append(g[3]) }
            else -> withStyle(SpanStyle(fontStyle = FontStyle.Italic)) { append(g[4]) }
        }
        last = m.range.last + 1
    }
    if (last < text.length) append(text.substring(last))
}

private sealed class Block {
    data class Para(val text: String) : Block()
    data class Heading(val text: String) : Block()
    data class Item(val marker: String, val text: String, val indent: Int, val numbered: Boolean) : Block()
    data class Code(val text: String) : Block()
}

private fun parse(text: String): List<Block> {
    val out = ArrayList<Block>()
    val lines = text.replace("\r", "").split("\n")
    val para = ArrayList<String>()
    fun flush() { if (para.isNotEmpty()) { out += Block.Para(para.joinToString(" ")); para.clear() } }
    var i = 0
    while (i < lines.size) {
        val line = lines[i]
        val t = line.trim()
        when {
            t.startsWith("```") -> {
                flush()
                val code = ArrayList<String>()
                i++
                while (i < lines.size && !lines[i].trim().startsWith("```")) { code += lines[i]; i++ }
                out += Block.Code(code.joinToString("\n"))
            }
            t.isEmpty() -> flush()
            t.startsWith("#") -> { flush(); out += Block.Heading(t.trimStart('#').trim()) }
            Regex("^([-*\u2022])\\s+.*").matches(t) || Regex("^\\d+[.)]\\s+.*").matches(t) -> {
                flush()
                val numbered = t[0].isDigit()
                out += Block.Item(if (numbered) t.substringBefore(' ') else "\u2022", t.substringAfter(' ').trim(),
                    line.length - line.trimStart().length, numbered)
            }
            else -> para += t
        }
        i++
    }
    flush()
    return out
}

/**
 * Renders a reply's light markdown as text: headings, bullet and numbered
 * lists, fenced code and paragraphs. Anything else reads as the plain text it is.
 */
@Composable
fun MarkdownText(text: String, color: Color = MaterialTheme.colorScheme.onSurface) {
    val codeBg = MaterialTheme.colorScheme.surfaceVariant
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        parse(text).forEach { b ->
            when (b) {
                is Block.Para -> Text(inlineMarkdown(b.text, codeBg), fontSize = 15.sp, lineHeight = 22.sp, color = color)
                is Block.Heading -> Text(inlineMarkdown(b.text, codeBg), fontSize = 16.sp, fontWeight = FontWeight.Bold, color = color)
                is Block.Code -> Text(b.text, fontFamily = FontFamily.Monospace, fontSize = 13.sp, color = color,
                    modifier = Modifier.fillMaxWidth().background(codeBg, RoundedCornerShape(8.dp)).padding(8.dp))
                is Block.Item -> Row(Modifier.padding(start = (b.indent.coerceAtMost(8) * 4).dp)) {
                    Text(b.marker, fontSize = 15.sp, color = MaterialTheme.colorScheme.primary, modifier = Modifier.width(if (b.numbered) 24.dp else 14.dp))
                    Text(inlineMarkdown(b.text, codeBg), fontSize = 15.sp, lineHeight = 22.sp, color = color)
                }
            }
        }
    }
}
