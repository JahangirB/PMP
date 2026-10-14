package com.jahangirb.pmpprep.ui.theme

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import kotlinx.coroutines.delay

/*
 * Clay design system: fluffy pastel surfaces, soft tinted shadows, and spring motion.
 *
 * Motion runs at the chosen speed of 0.5x: the spec's spring (damping 0.56, stiffness 1500)
 * with a quarter of the stiffness, which halves its frequency. Everything here is skipped when
 * Android's "Remove animations" is on (LocalEnter.reduce).
 */

val ClayRadius = 32.dp
val ClayBorder = 2.dp

private const val SPEED = 0.5f
private val Stiffness = 1500f * SPEED * SPEED
/** The spec's spring at 0.5x speed. */
fun <T> claySpring() = spring<T>(dampingRatio = 0.56f, stiffness = Stiffness)
/** A bouncier spring for the release pop. */
private fun <T> popSpring() = spring<T>(dampingRatio = 0.4f, stiffness = Stiffness)

private const val STAGGER_MS = 90L
private const val MAX_STAGGERED = 10

/** One per screen visit: hands each card its place in the entrance stagger. */
class EnterStagger(val reduce: Boolean, private val start: Long = System.currentTimeMillis()) {
    private var count = 0
    /** Delay before this item enters, or null when it should simply appear (late, or motion off). */
    fun claim(): Long? {
        if (reduce) return null
        val i = count++.coerceAtMost(MAX_STAGGERED)
        val wait = start + i * STAGGER_MS - System.currentTimeMillis()
        // Items composed long after the screen opened (scrolled into view) don't animate.
        return if (wait < -400) null else wait.coerceAtLeast(0)
    }
}

val LocalEnter = staticCompositionLocalOf<EnterStagger?> { null }

/** Kinetic lateral slide: in from 36 dp left with a 4° skew that straightens as it lands. */
fun Modifier.kineticEnter(): Modifier = composed {
    val stagger = LocalEnter.current
    val wait = remember { stagger?.claim() }
    val p = remember { Animatable(if (wait == null) 1f else 0f) }
    LaunchedEffect(Unit) {
        if (wait != null) { delay(wait); p.animateTo(1f, claySpring()) }
    }
    this
        .graphicsLayer {
            val k = 1f - p.value
            translationX = k * -36.dp.toPx()
            alpha = (p.value * 1.4f).coerceIn(0f, 1f)
        }
        .drawWithContent {
            val k = 1f - p.value
            if (k == 0f) drawContent() else {
                // skewX(-4deg) about the vertical centre, so the item leans without shifting.
                val c = drawContext.canvas
                c.save()
                c.translate(0f, size.height / 2)
                c.skew(-4f * k, 0f)
                c.translate(0f, -size.height / 2)
                drawContent()
                c.restore()
            }
        }
}

/**
 * A clay surface: soft tinted drop shadow, pastel fill with a light top-left sheen,
 * and a 2 dp rim. When [interaction] is given the surface floats up while pressed,
 * pops back on release, and a glow follows the finger with the rim lit in [glow].
 */
fun Modifier.clay(
    shape: Shape,
    color: Color,
    rim: Color,
    shade: Color,
    glow: Color,
    interaction: InteractionSource? = null,
    elevation: Float = 10f,
): Modifier = composed {
    val sheen = if (LocalDarkTheme.current) 0.05f else 0.35f
    val pressed = interaction?.collectIsPressedAsState()?.value ?: false
    val reduce = LocalEnter.current?.reduce ?: false
    val lift by animateFloatAsState(if (pressed) 1f else 0f, if (reduce) spring<Float>(stiffness = Spring.StiffnessHigh) else claySpring<Float>(), label = "lift")
    val scale by animateFloatAsState(if (pressed && !reduce) 0.97f else 1f, popSpring<Float>(), label = "pop")
    var touch by remember { mutableStateOf(Offset.Unspecified) }
    this
        .then(if (interaction == null) Modifier else Modifier.pointerInput(Unit) {
            awaitPointerEventScope {
                while (true) {
                    val e = awaitPointerEvent(PointerEventPass.Initial)
                    e.changes.firstOrNull()?.let { if (it.pressed) touch = it.position }
                }
            }
        })
        .graphicsLayer {
            translationY = -lift * 4.dp.toPx()
            scaleX = scale
            scaleY = scale
        }
        .shadow((elevation + lift * 10f).dp, shape, clip = false, ambientColor = shade, spotColor = lerp(shade, glow, lift))
        .background(color, shape)
        .drawBehind {
            // Sheen from the top-left, a little shade toward the bottom-right: the clay "puff".
            drawRect(Brush.linearGradient(listOf(Color.White.copy(alpha = sheen), Color.Transparent), Offset.Zero, Offset(size.width * 0.6f, size.height * 0.6f)))
            drawRect(Brush.linearGradient(listOf(Color.Transparent, shade.copy(alpha = 0.10f)), Offset(size.width * 0.4f, size.height * 0.4f), Offset(size.width, size.height)))
            if (lift > 0.01f && touch != Offset.Unspecified) {
                drawCircle(Brush.radialGradient(listOf(glow.copy(alpha = 0.22f * lift), Color.Transparent), touch, size.minDimension * 0.9f),
                    radius = size.minDimension * 0.9f, center = touch)
            }
        }
        .border(ClayBorder, lerp(rim, glow, lift * 0.8f), shape)
        .clip(shape)
}

/** Magnetic lift and glow for buttons: rises, leans toward the finger, and glows while pressed. */
fun Modifier.magnetic(interaction: InteractionSource, shape: Shape, glow: Color, enabled: Boolean, rest: Float = 4f): Modifier = composed {
    val pressed by interaction.collectIsPressedAsState()
    val reduce = LocalEnter.current?.reduce ?: false
    var pull by remember { mutableStateOf(Offset.Zero) }
    val on = enabled && !reduce
    val lift by animateFloatAsState(if (pressed && on) 1f else 0f, claySpring<Float>(), label = "lift")
    val px by animateFloatAsState(if (on) pull.x else 0f, claySpring<Float>(), label = "px")
    val py by animateFloatAsState(if (on) pull.y else 0f, claySpring<Float>(), label = "py")
    this
        .pointerInput(Unit) {
            awaitPointerEventScope {
                while (true) {
                    val c = awaitPointerEvent(PointerEventPass.Initial).changes.firstOrNull() ?: continue
                    pull = if (c.pressed && size.width > 0 && size.height > 0) Offset(
                        (c.position.x / size.width - 0.5f).coerceIn(-0.5f, 0.5f) * 2f,
                        (c.position.y / size.height - 0.5f).coerceIn(-0.5f, 0.5f) * 2f,
                    ) else Offset.Zero
                }
            }
        }
        .graphicsLayer {
            translationX = px * 3.dp.toPx()
            translationY = py * 2.dp.toPx() - lift * 3.dp.toPx()
        }
        .shadow((if (enabled) rest else 0f).dp + (lift * 12f).dp, shape, clip = false, ambientColor = glow, spotColor = glow)
}

/** The light rim of a clay surface. */
val ClayRim: Color @androidx.compose.runtime.Composable @androidx.compose.runtime.ReadOnlyComposable
    get() = if (LocalDarkTheme.current) Color.White.copy(alpha = 0.07f) else Color.White.copy(alpha = 0.85f)
/** The tinted shadow under a clay surface. */
val ClayShade: Color @androidx.compose.runtime.Composable @androidx.compose.runtime.ReadOnlyComposable
    get() = if (LocalDarkTheme.current) Color.Black else Color(0xFF0B6684)
