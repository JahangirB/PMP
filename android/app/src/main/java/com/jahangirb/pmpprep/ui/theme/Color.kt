package com.jahangirb.pmpprep.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// Sky Clay: pastel sky blue and soft lilac. Measured pairs, WCAG ratio:
//   light: primary on surface 6.2, on background 5.8; white on primary 6.5; onSurfaceVariant 6.6–7.7
//   dark:  primary on surface 8.3; onPrimary on primary 8.7; onSurfaceVariant 6.3–9.4
val LightPrimary = Color(0xFF0B6684)
val LightOnPrimary = Color(0xFFFFFFFF)
val LightPrimaryContainer = Color(0xFFC8EDF9)
val LightOnPrimaryContainer = Color(0xFF072F40)
val LightSecondary = Color(0xFF395460)
val LightOnSecondary = Color(0xFFFFFFFF)
val LightSecondaryContainer = Color(0xFFDAEBF1)
val LightOnSecondaryContainer = Color(0xFF12232B)
val LightTertiary = Color(0xFFA04A08)
val LightOnTertiary = Color(0xFFFFFFFF)
val LightTertiaryContainer = Color(0xFFFDE9D3)
val LightOnTertiaryContainer = Color(0xFF5A2604)
val LightBackground = Color(0xFFE8F4F7)
val LightSurface = Color(0xFFF7FBFD)
val LightOnSurface = Color(0xFF12232B)
val LightSurfaceVariant = Color(0xFFDEEDF2)
val LightOnSurfaceVariant = Color(0xFF395460)
val LightOutline = Color(0xFFB3D1DB)
val LightContainerLow = Color(0xFFEFF8FA)
val LightContainer = Color(0xFFEAF4F8)
val LightContainerHigh = Color(0xFFE3F0F5)
val LightContainerHighest = Color(0xFFDCECF2)
/** The app's backdrop, top to bottom: sky to lilac. */
val LightGradient = listOf(Color(0xFFDDF0F5), Color(0xFFE8F4F7), Color(0xFFEEE9F6))

val DarkPrimary = Color(0xFF4CC7F0)
val DarkOnPrimary = Color(0xFF0A1F29)
val DarkPrimaryContainer = Color(0xFF185062)
val DarkOnPrimaryContainer = Color(0xFFD1F0FA)
val DarkSecondary = Color(0xFFA6C1C9)
val DarkOnSecondary = Color(0xFF0A1F29)
val DarkSecondaryContainer = Color(0xFF2E434C)
val DarkOnSecondaryContainer = Color(0xFFEAF3F6)
val DarkTertiary = Color(0xFFFBBF24)
val DarkOnTertiary = Color(0xFF4A2A04)
val DarkTertiaryContainer = Color(0xFF5A3A0C)
val DarkOnTertiaryContainer = Color(0xFFFEF3C7)
val DarkBackground = Color(0xFF0D171C)
val DarkSurface = Color(0xFF152228)
val DarkOnSurface = Color(0xFFEEF4F6)
val DarkSurfaceVariant = Color(0xFF223239)
val DarkOnSurfaceVariant = Color(0xFFABBEC4)
val DarkOutline = Color(0xFF3D535C)
val DarkContainerLow = Color(0xFF121D21)
val DarkContainer = Color(0xFF1A282E)
val DarkContainerHigh = Color(0xFF203037)
val DarkContainerHighest = Color(0xFF263840)
val DarkGradient = listOf(Color(0xFF0D171C), Color(0xFF101B21), Color(0xFF141826))

// Status, metric and domain colours. Each is used as text as well as fill, and
// no single value reaches WCAG AA (4.5:1) on both the light and the dark
// surfaces, so each theme gets its own, measured with the contrast checker:
//   light, on #F7FBFD / #DEEDF2    dark, on #152228 / #223239 (cards: surface / surfaceVariant)
//   success  #047857  5.3 / 4.6       #34D399  8.5 / 6.9
//   warning  #A04A08  5.8 / 5.0       #FBBF24  9.7 / 7.9
//   error    #B91C1C  6.2 / 5.4       #F87171  5.9 / 4.8
//   info     #1D4ED8  6.4 / 5.6       #60A5FA  6.4 / 5.2
//   people   #4F46E5  6.0 / 5.2       #A5B4FC  8.2 / 6.7
//   process  #0369A1  5.7 / 4.9       #38BDF8  7.6 / 6.2
//   business #A04A08  5.8 / 5.0       #FBBF24  9.7 / 7.9
// The earlier single values (#10B981, #F59E0B, #EF4444, #3B82F6, #0284C7)
// measured 2.2–4.1:1 as text on white.
val LocalDarkTheme = staticCompositionLocalOf { false }

private fun pick(dark: Boolean, light: Long, night: Long) = Color(if (dark) night else light)

val ColorSuccess: Color @Composable @ReadOnlyComposable get() = pick(LocalDarkTheme.current, 0xFF047857, 0xFF34D399)
val ColorWarning: Color @Composable @ReadOnlyComposable get() = pick(LocalDarkTheme.current, 0xFFA04A08, 0xFFFBBF24)
val ColorError: Color @Composable @ReadOnlyComposable get() = pick(LocalDarkTheme.current, 0xFFB91C1C, 0xFFF87171)
val ColorInfo: Color @Composable @ReadOnlyComposable get() = pick(LocalDarkTheme.current, 0xFF1D4ED8, 0xFF60A5FA)
val ColorPaceGood: Color @Composable @ReadOnlyComposable get() = ColorSuccess
val ColorPaceWarn: Color @Composable @ReadOnlyComposable get() = ColorWarning
val ColorPaceDanger: Color @Composable @ReadOnlyComposable get() = ColorError

val PeopleAccent: Color @Composable @ReadOnlyComposable get() = pick(LocalDarkTheme.current, 0xFF4F46E5, 0xFFA5B4FC)
val ProcessAccent: Color @Composable @ReadOnlyComposable get() = pick(LocalDarkTheme.current, 0xFF0369A1, 0xFF38BDF8)
val BusinessAccent: Color @Composable @ReadOnlyComposable get() = pick(LocalDarkTheme.current, 0xFFA04A08, 0xFFFBBF24)

/** Text on a filled status colour: white on the dark light-theme fills, ink on the bright dark-theme ones. */
val OnStatus: Color @Composable @ReadOnlyComposable get() = pick(LocalDarkTheme.current, 0xFFFFFFFF, 0xFF0A1F29)

val PmpGoldTertiary: Color @Composable @ReadOnlyComposable get() = pick(LocalDarkTheme.current, 0xFFA04A08, 0xFFFBBF24)
