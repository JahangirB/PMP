package com.jahangirb.pmpprep.ui.components

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jahangirb.pmpprep.data.Figure
import com.jahangirb.pmpprep.ui.theme.ColorError
import com.jahangirb.pmpprep.ui.theme.ColorSuccess
import com.jahangirb.pmpprep.ui.theme.ColorWarning
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

/** A lesson figure (index.html §10.31), scaled to the width it is given, with its caption. */
@Composable
fun LessonFigure(fig: Figure) {
    val ink = MaterialTheme.colorScheme.onSurface
    val tones = mapOf(
        "ink" to ink, "muted" to MaterialTheme.colorScheme.onSurfaceVariant, "accent" to MaterialTheme.colorScheme.primary,
        "good" to ColorSuccess, "warn" to ColorWarning, "bad" to ColorError,
    )
    fun tone(t: String): Color = tones[t] ?: ink
    Column {
        if (fig.w > 0f && fig.h > 0f) {
            Canvas(Modifier.fillMaxWidth().aspectRatio(fig.w / fig.h).semantics { contentDescription = fig.alt.ifBlank { fig.title } }) {
                val k = size.width / fig.w
                val paint = Paint(Paint.ANTI_ALIAS_FLAG)
                fun text(x: Float, y: Float, t: String, sizeU: Float, align: Paint.Align, bold: Boolean, color: Color) {
                    paint.textSize = sizeU * k
                    paint.textAlign = align
                    paint.typeface = if (bold) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
                    paint.color = color.toArgb()
                    val rows = t.split("\n")
                    val lh = sizeU * 1.25f
                    val top = y - (rows.size - 1) * lh / 2
                    rows.forEachIndexed { i, r ->
                        // Centre each line on its y, as the web's dominant-baseline: central does.
                        drawContext.canvas.nativeCanvas.drawText(r, x * k, (top + i * lh) * k + paint.textSize * 0.35f, paint)
                    }
                }
                fig.shapes.forEach { s ->
                    val c = tone(s.tone)
                    when (s.k) {
                        "box" -> {
                            val tl = Offset(s.x * k, s.y * k); val sz = Size(s.w * k, s.h * k); val rad = CornerRadius(6 * k)
                            if (s.fill) drawRoundRect(c.copy(alpha = 0.12f), tl, sz, rad)
                            drawRoundRect(c, tl, sz, rad, style = Stroke(1.5f * k))
                            if (s.t.isNotEmpty()) text(s.x + s.w / 2, s.y + s.h / 2, s.t, s.s, Paint.Align.CENTER, s.b, ink)
                        }
                        "line" -> if (s.p.size >= 4) {
                            val path = Path().apply {
                                moveTo(s.p[0] * k, s.p[1] * k)
                                var i = 2
                                while (i + 1 < s.p.size) { lineTo(s.p[i] * k, s.p[i + 1] * k); i += 2 }
                            }
                            drawPath(path, c, style = Stroke(2f * k, cap = StrokeCap.Round,
                                pathEffect = if (s.dash) PathEffect.dashPathEffect(floatArrayOf(5 * k, 4 * k)) else null))
                            if (s.arrow) {
                                val n = s.p.size
                                val x2 = s.p[n - 2] * k; val y2 = s.p[n - 1] * k
                                val ang = atan2(y2 - s.p[n - 3] * k, x2 - s.p[n - 4] * k)
                                val len = 9f * k; val wid = 0.45f
                                drawPath(Path().apply {
                                    moveTo(x2, y2)
                                    lineTo(x2 - len * cos(ang - wid), y2 - len * sin(ang - wid))
                                    lineTo(x2 - len * cos(ang + wid), y2 - len * sin(ang + wid))
                                    close()
                                }, c)
                            }
                        }
                        "text" -> text(s.x, s.y, s.t, s.s,
                            when (s.a) { "middle" -> Paint.Align.CENTER; "end" -> Paint.Align.RIGHT; else -> Paint.Align.LEFT },
                            s.b, if (s.tone == "ink") ink else c)
                        "dot" -> drawCircle(c, s.r * k, Offset(s.x * k, s.y * k))
                    }
                }
            }
        }
        Spacer(Modifier.height(6.dp))
        Text(buildAnnotatedString {
            if (fig.title.isNotBlank()) withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append(fig.title + ". ") }
            append(fig.caption)
        }, fontSize = 13.sp, lineHeight = 18.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
