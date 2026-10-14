package com.jahangirb.pmpprep.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// Fluffy pastel clay in leaf green: cream cards on a sage-to-straw gradient. Measured, WCAG ratio:
//   light: primary on surface 6.0, on the gradient 4.6–4.8; white on primary 6.5; onSurfaceVariant 5.5–7.2
//   dark:  primary on surface 8.6; onPrimary on primary 9.2; onSurfaceVariant 7.1–9.6
val LightPrimary = Color(0xFF326B00)
val LightOnPrimary = Color(0xFFFFFFFF)
val LightPrimaryContainer = Color(0xFFDCEBC8)
val LightOnPrimaryContainer = Color(0xFF1F3A00)
val LightSecondary = Color(0xFF4A5A3C)
val LightOnSecondary = Color(0xFFFFFFFF)
val LightSecondaryContainer = Color(0xFFE5EFDA)
val LightOnSecondaryContainer = Color(0xFF1B2A10)
val LightTertiary = Color(0xFFA04A08)
val LightOnTertiary = Color(0xFFFFFFFF)
val LightTertiaryContainer = Color(0xFFFDE9D3)
val LightOnTertiaryContainer = Color(0xFF5A2604)
val LightBackground = Color(0xFFD5E3B8)
val LightSurface = Color(0xFFF6F8EE)
val LightOnSurface = Color(0xFF1A2412)
val LightSurfaceVariant = Color(0xFFE5EFDA)
val LightOnSurfaceVariant = Color(0xFF4A5740)
val LightOutline = Color(0xFFC2CFAC)
val LightContainerLow = Color(0xFFF2F6E8)
val LightContainer = Color(0xFFECF2E1)
val LightContainerHigh = Color(0xFFE5EFDA)
val LightContainerHighest = Color(0xFFDEE9D0)
/** The app's backdrop, top to bottom: sage to straw. */
val LightGradient = listOf(Color(0xFFCBE6BE), Color(0xFFD5E3B8), Color(0xFFDEDCAD))

val DarkPrimary = Color(0xFF9AD35A)
val DarkOnPrimary = Color(0xFF142400)
val DarkPrimaryContainer = Color(0xFF2F4D12)
val DarkOnPrimaryContainer = Color(0xFFDCF0C4)
val DarkSecondary = Color(0xFFB6C7A3)
val DarkOnSecondary = Color(0xFF142400)
val DarkSecondaryContainer = Color(0xFF2C3A22)
val DarkOnSecondaryContainer = Color(0xFFE6F0DA)
val DarkTertiary = Color(0xFFFBBF24)
val DarkOnTertiary = Color(0xFF4A2A04)
val DarkTertiaryContainer = Color(0xFF5A3A0C)
val DarkOnTertiaryContainer = Color(0xFFFEF3C7)
val DarkBackground = Color(0xFF161D10)
val DarkSurface = Color(0xFF1F2818)
val DarkOnSurface = Color(0xFFEEF2E6)
val DarkSurfaceVariant = Color(0xFF2B3622)
val DarkOnSurfaceVariant = Color(0xFFBAC5AE)
val DarkOutline = Color(0xFF46523B)
val DarkContainerLow = Color(0xFF1A2214)
val DarkContainer = Color(0xFF222C1B)
val DarkContainerHigh = Color(0xFF283320)
val DarkContainerHighest = Color(0xFF2F3B26)
val DarkGradient = listOf(Color(0xFF13200F), Color(0xFF161D10), Color(0xFF1D1E10))

// Status, metric and domain colours. Each is used as text as well as fill, and
// no single value reaches WCAG AA (4.5:1) on both the light and the dark
// surfaces, so each theme gets its own, measured with the contrast checker:
//   light, on #F6F8EE / #E5EFDA    dark, on #1F2818 / #2B3622 (cards: surface / surfaceVariant)
//   success  #047857  5.1 / 4.6       #34D399  7.9 / 6.6
//   warning  #A04A08  5.6 / 5.1       #FBBF24  9.1 / 7.6
//   error    #B91C1C  6.0 / 5.5       #F87171  5.5 / 4.6
//   info     #1D4ED8  6.2 / 5.6       #60A5FA  6.0 / 5.0
//   people   #4F46E5  5.9 / 5.3       #A5B4FC  7.7 / 6.4
//   process  #0369A1  5.5 / 5.0       #38BDF8  7.1 / 5.9
//   business #A04A08  5.6 / 5.1       #FBBF24  9.1 / 7.6
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
val OnStatus: Color @Composable @ReadOnlyComposable get() = pick(LocalDarkTheme.current, 0xFFFFFFFF, 0xFF142400)

val PmpGoldTertiary: Color @Composable @ReadOnlyComposable get() = pick(LocalDarkTheme.current, 0xFFA04A08, 0xFFFBBF24)
