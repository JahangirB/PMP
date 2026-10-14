package com.jahangirb.pmpprep.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.jahangirb.pmpprep.R
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

internal val DarkColors = darkColorScheme(
    primary = DarkPrimary, onPrimary = DarkOnPrimary,
    primaryContainer = DarkPrimaryContainer, onPrimaryContainer = DarkOnPrimaryContainer,
    secondary = DarkSecondary, onSecondary = DarkOnSecondary,
    secondaryContainer = DarkSecondaryContainer, onSecondaryContainer = DarkOnSecondaryContainer,
    tertiary = DarkTertiary, onTertiary = DarkOnTertiary,
    tertiaryContainer = DarkTertiaryContainer, onTertiaryContainer = DarkOnTertiaryContainer,
    background = DarkBackground, onBackground = DarkOnSurface,
    surface = DarkSurface, onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant, onSurfaceVariant = DarkOnSurfaceVariant,
    // Dialogs, sheets, chips and pickers draw on these; left unset they fall back to Material's lavender.
    surfaceContainerLowest = DarkSurface, surfaceContainerLow = DarkContainerLow, surfaceContainer = DarkContainer,
    surfaceContainerHigh = DarkContainerHigh, surfaceContainerHighest = DarkContainerHighest,
    surfaceBright = DarkSurface, surfaceDim = DarkContainerHighest,
    outline = DarkOutline, outlineVariant = DarkSurfaceVariant,
    error = Color(0xFFF87171),
)

internal val LightColors = lightColorScheme(
    primary = LightPrimary, onPrimary = LightOnPrimary,
    primaryContainer = LightPrimaryContainer, onPrimaryContainer = LightOnPrimaryContainer,
    secondary = LightSecondary, onSecondary = LightOnSecondary,
    secondaryContainer = LightSecondaryContainer, onSecondaryContainer = LightOnSecondaryContainer,
    tertiary = LightTertiary, onTertiary = LightOnTertiary,
    tertiaryContainer = LightTertiaryContainer, onTertiaryContainer = LightOnTertiaryContainer,
    background = LightBackground, onBackground = LightOnSurface,
    surface = LightSurface, onSurface = LightOnSurface,
    surfaceVariant = LightSurfaceVariant, onSurfaceVariant = LightOnSurfaceVariant,
    // Dialogs, sheets, chips and pickers draw on these; left unset they fall back to Material's lavender.
    surfaceContainerLowest = LightSurface, surfaceContainerLow = LightContainerLow, surfaceContainer = LightContainer,
    surfaceContainerHigh = LightContainerHigh, surfaceContainerHighest = LightContainerHighest,
    surfaceBright = LightSurface, surfaceDim = LightContainerHighest,
    outline = LightOutline, outlineVariant = LightSurfaceVariant,
    error = Color(0xFFB91C1C),
)

/** Clay: big soft corners everywhere; cards use ClayRadius (32 dp). */
private val ClayShapes = Shapes(
    extraSmall = RoundedCornerShape(12.dp),
    small = RoundedCornerShape(16.dp),
    medium = RoundedCornerShape(24.dp),
    large = RoundedCornerShape(ClayRadius),
    extraLarge = RoundedCornerShape(ClayRadius),
)

/** Nunito, bundled in res/font: rounded and friendly, readable at small sizes. */
val Nunito = FontFamily(
    Font(R.font.nunito_regular, FontWeight.Normal),
    Font(R.font.nunito_semibold, FontWeight.Medium),
    Font(R.font.nunito_semibold, FontWeight.SemiBold),
    Font(R.font.nunito_bold, FontWeight.Bold),
    Font(R.font.nunito_extrabold, FontWeight.ExtraBold),
    Font(R.font.nunito_extrabold, FontWeight.Black),
)

/** Material's type scale, set in Nunito. Body text keeps its slightly looser reading spacing. */
private val AppTypography = Typography().run {
    fun TextStyle.n() = copy(fontFamily = Nunito)
    Typography(
        displayLarge = displayLarge.n(), displayMedium = displayMedium.n(), displaySmall = displaySmall.n(),
        headlineLarge = headlineLarge.n(), headlineMedium = headlineMedium.n(), headlineSmall = headlineSmall.n(),
        titleLarge = titleLarge.n(), titleMedium = titleMedium.n(), titleSmall = titleSmall.n(),
        bodyLarge = bodyLarge.n().copy(lineHeight = 24.sp, letterSpacing = 0.2.sp), bodyMedium = bodyMedium.n(), bodySmall = bodySmall.n(),
        labelLarge = labelLarge.n(), labelMedium = labelMedium.n(), labelSmall = labelSmall.n(),
    )
}

@Composable
fun PmpTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalDarkTheme provides darkTheme) {
        MaterialTheme(
            colorScheme = if (darkTheme) DarkColors else LightColors,
            typography = AppTypography,
            shapes = ClayShapes,
            content = content,
        )
    }
}
