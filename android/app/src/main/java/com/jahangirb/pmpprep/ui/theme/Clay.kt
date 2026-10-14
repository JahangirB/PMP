package com.jahangirb.pmpprep.ui.theme

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
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
import kotlinx.coroutines.delay

/*
 * Clay design system: fluffy pastel surfaces, soft tinted shadows, and calm motion.
 *
 * Motion is eased, not sprung: things glide into place and stop, with no overshoot or wobble.
 * Everything here is skipped when Android's "Remove animations" is on (LocalEnter.reduce).
 */

val ClayRadius = 32.dp
val ClayBorder = 2.dp

private const val ENTER_MS = 800
private const val PRESS_MS = 260
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

/** Lateral slide: in from 36 dp left while fading up, easing to a stop. */
fun Modifier.kineticEnter(): Modifier = composed {
    val stagger = LocalEnter.current
    val wait = remember { stagger?.claim() }
    val p = remember { Animatable(if (wait == null) 1f else 0f) }
    LaunchedEffect(Unit) {
        if (wait != null) { delay(wait); p.animateTo(1f, tween(ENTER_MS, easing = LinearOutSlowInEasing)) }
    }
    graphicsLayer {
        translationX = (1f - p.value) * -36.dp.toPx()
        alpha = p.value
    }
}

/**
 * A clay surface: soft tinted drop shadow, pastel fill with a light top-left sheen,
 * and a 2 dp rim. When [interaction] is given the surface lifts a little while pressed,
 * and a soft glow sits under the finger with the rim lit in [glow].
 *
 * Everything drawn is clipped to [shape]: drawn before the clip, the sheen showed as a
 * pale rectangle around the rounded corners.
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
    val sheen = if (LocalDarkTheme.current) 0.05f else 0.45f
    val pressed = interaction?.collectIsPressedAsState()?.value ?: false
    val reduce = LocalEnter.current?.reduce ?: false
    val lift by animateFloatAsState(if (pressed) 1f else 0f, tween(if (reduce) 0 else PRESS_MS, easing = FastOutSlowInEasing), label = "lift")
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
        .graphicsLayer { translationY = -lift * 3.dp.toPx() }
        .shadow((elevation + lift * 6f).dp, shape, clip = false, ambientColor = shade, spotColor = lerp(shade, glow, lift))
        .clip(shape)
        .background(color)
        .drawBehind {
            // Sheen from the top-left, a little shade toward the bottom-right: the clay "puff".
            drawRect(Brush.linearGradient(listOf(Color.White.copy(alpha = sheen), Color.Transparent), Offset.Zero, Offset(size.width * 0.6f, size.height * 0.6f)))
            drawRect(Brush.linearGradient(listOf(Color.Transparent, shade.copy(alpha = 0.08f)), Offset(size.width * 0.4f, size.height * 0.4f), Offset(size.width, size.height)))
            if (lift > 0.01f && touch != Offset.Unspecified) {
                drawCircle(Brush.radialGradient(listOf(glow.copy(alpha = 0.18f * lift), Color.Transparent), touch, size.minDimension * 0.9f),
                    radius = size.minDimension * 0.9f, center = touch)
            }
        }
        .border(ClayBorder, lerp(rim, glow, lift * 0.6f), shape)
}

/** Buttons lift a little and glow softly while pressed; Material's ripple does the tap. */
fun Modifier.magnetic(interaction: InteractionSource, shape: Shape, glow: Color, enabled: Boolean, rest: Float = 4f): Modifier = composed {
    val pressed by interaction.collectIsPressedAsState()
    val reduce = LocalEnter.current?.reduce ?: false
    val lift by animateFloatAsState(if (pressed && enabled) 1f else 0f, tween(if (reduce) 0 else PRESS_MS, easing = FastOutSlowInEasing), label = "lift")
    this
        .graphicsLayer { translationY = -lift * 2.dp.toPx() }
        .shadow((if (enabled) rest else 0f).dp + (lift * 8f).dp, shape, clip = false, ambientColor = glow, spotColor = glow)
}

/** The light rim of a clay surface. */
val ClayRim: Color @Composable @ReadOnlyComposable
    get() = if (LocalDarkTheme.current) Color.White.copy(alpha = 0.07f) else Color.White.copy(alpha = 0.9f)
/** The tinted shadow under a clay surface. */
val ClayShade: Color @Composable @ReadOnlyComposable
    get() = if (LocalDarkTheme.current) Color.Black else Color(0xFF0B6684)
/** The app's backdrop gradient. */
val ClayBackdrop: List<Color> @Composable @ReadOnlyComposable
    get() = if (LocalDarkTheme.current) DarkGradient else LightGradient
