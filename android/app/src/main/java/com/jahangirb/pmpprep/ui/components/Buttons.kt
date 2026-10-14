package com.jahangirb.pmpprep.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import com.jahangirb.pmpprep.ui.theme.ClayBorder
import com.jahangirb.pmpprep.ui.theme.magnetic

/*
 * Material buttons with the clay interactions: magnetic lift and glow while pressed, Material's
 * ink ripple on tap. Same parameters as Material's, so screens import them under Material's names:
 *   import com.jahangirb.pmpprep.ui.components.ClayButton as Button
 */

@Composable
fun ClayButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = ButtonDefaults.shape,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    elevation: ButtonElevation? = null,
    border: BorderStroke? = null,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    interactionSource: MutableInteractionSource? = null,
    content: @Composable RowScope.() -> Unit,
) {
    val src = interactionSource ?: remember { MutableInteractionSource() }
    androidx.compose.material3.Button(
        onClick = onClick,
        modifier = modifier.magnetic(src, shape, MaterialTheme.colorScheme.primary, enabled),
        enabled = enabled, shape = shape, colors = colors, elevation = elevation, border = border,
        contentPadding = contentPadding, interactionSource = src, content = content,
    )
}

@Composable
fun ClayOutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = ButtonDefaults.outlinedShape,
    colors: ButtonColors = ButtonDefaults.outlinedButtonColors(),
    elevation: ButtonElevation? = null,
    border: BorderStroke? = BorderStroke(ClayBorder, MaterialTheme.colorScheme.outline.copy(alpha = if (enabled) 1f else 0.4f)),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    interactionSource: MutableInteractionSource? = null,
    content: @Composable RowScope.() -> Unit,
) {
    val src = interactionSource ?: remember { MutableInteractionSource() }
    androidx.compose.material3.OutlinedButton(
        onClick = onClick,
        modifier = modifier.magnetic(src, shape, MaterialTheme.colorScheme.primary.copy(alpha = 0.6f), enabled, rest = 0f),
        enabled = enabled, shape = shape, colors = colors, elevation = elevation, border = border,
        contentPadding = contentPadding, interactionSource = src, content = content,
    )
}
