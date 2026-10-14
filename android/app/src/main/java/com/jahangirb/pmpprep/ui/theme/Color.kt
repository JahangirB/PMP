package com.jahangirb.pmpprep.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// Light Theme Colors (Clean, Executive Navy & Slate with Gold/Emerald Accents)
val SlateNavyPrimary = Color(0xFF1E3A8A)
val SlateNavyOnPrimary = Color(0xFFFFFFFF)
val SlateNavyContainer = Color(0xFFDBEAFE)
val SlateNavyOnContainer = Color(0xFF1E293B)

val SlateSecondary = Color(0xFF334155)
val SlateOnSecondary = Color(0xFFFFFFFF)
val SlateSecondaryContainer = Color(0xFFF1F5F9)
val SlateOnSecondaryContainer = Color(0xFF0F172A)

val PmpGoldTertiaryLight = Color(0xFFB45309)
val PmpGoldOnTertiary = Color(0xFFFFFFFF)
val PmpGoldContainer = Color(0xFFFEF3C7)
val PmpGoldOnContainer = Color(0xFF78350F)

val SlateBackground = Color(0xFFF8FAFC)
val SlateOnBackground = Color(0xFF0F172A)
val SlateSurface = Color(0xFFFFFFFF)
val SlateOnSurface = Color(0xFF0F172A)
val SlateSurfaceVariant = Color(0xFFF1F5F9)
val SlateOnSurfaceVariant = Color(0xFF475569)
val SlateOutline = Color(0xFFCBD5E1)

// Dark Theme Colors (Deep Instrument Panel Dark with Neon Cyan/Gold)
val DarkNavyPrimary = Color(0xFF38BDF8)
val DarkNavyOnPrimary = Color(0xFF0F172A)
val DarkNavyPrimaryContainer = Color(0xFF0369A1)
val DarkNavyOnPrimaryContainer = Color(0xFFE0F2FE)

val DarkSecondary = Color(0xFF94A3B8)
val DarkOnSecondary = Color(0xFF0F172A)
val DarkSecondaryContainer = Color(0xFF334155)
val DarkOnSecondaryContainer = Color(0xFFF8FAFC)

val DarkPmpGoldTertiary = Color(0xFFFBBF24)
val DarkPmpGoldOnTertiary = Color(0xFF78350F)
val DarkPmpGoldContainer = Color(0xFF78350F)
val DarkPmpGoldOnContainer = Color(0xFFFEF3C7)

val DarkBackground = Color(0xFF090D16)
val DarkOnBackground = Color(0xFFF1F5F9)
val DarkSurface = Color(0xFF0F172A)
val DarkOnSurface = Color(0xFFF8FAFC)
val DarkSurfaceVariant = Color(0xFF1E293B)
val DarkOnSurfaceVariant = Color(0xFF94A3B8)
val DarkOutline = Color(0xFF334155)

// Status, metric and domain colours. Each is used as text as well as fill, and
// no single value reaches WCAG AA (4.5:1) on both the light and the dark
// surfaces, so each theme gets its own, measured with the contrast checker:
//   light, on #FFFFFF / #F1F5F9    dark, on #0F172A / #1E293B
//   success  #047857  5.5 / 5.0       #34D399  9.3 / 7.6
//   warning  #B45309  5.0 / 4.6       #FBBF24  >8 / 8.8
//   error    #B91C1C  6.5 / 5.9       #F87171  6.5 / 5.3
//   info     #1D4ED8  6.7             #60A5FA  7.0 / 5.8
//   people   #4F46E5  6.3 / 5.7       #A5B4FC  9.0
//   process  #0369A1  5.9             #38BDF8  6.8
//   business #B45309  5.0 / 4.6       #FBBF24  8.8
// The earlier single values (#10B981, #F59E0B, #EF4444, #3B82F6, #0284C7)
// measured 2.2–4.1:1 as text on white.
val LocalDarkTheme = staticCompositionLocalOf { false }

private fun pick(dark: Boolean, light: Long, night: Long) = Color(if (dark) night else light)

val ColorSuccess: Color @Composable @ReadOnlyComposable get() = pick(LocalDarkTheme.current, 0xFF047857, 0xFF34D399)
val ColorWarning: Color @Composable @ReadOnlyComposable get() = pick(LocalDarkTheme.current, 0xFFB45309, 0xFFFBBF24)
val ColorError: Color @Composable @ReadOnlyComposable get() = pick(LocalDarkTheme.current, 0xFFB91C1C, 0xFFF87171)
val ColorInfo: Color @Composable @ReadOnlyComposable get() = pick(LocalDarkTheme.current, 0xFF1D4ED8, 0xFF60A5FA)
val ColorPaceGood: Color @Composable @ReadOnlyComposable get() = ColorSuccess
val ColorPaceWarn: Color @Composable @ReadOnlyComposable get() = ColorWarning
val ColorPaceDanger: Color @Composable @ReadOnlyComposable get() = ColorError

val PeopleAccent: Color @Composable @ReadOnlyComposable get() = pick(LocalDarkTheme.current, 0xFF4F46E5, 0xFFA5B4FC)
val ProcessAccent: Color @Composable @ReadOnlyComposable get() = pick(LocalDarkTheme.current, 0xFF0369A1, 0xFF38BDF8)
val BusinessAccent: Color @Composable @ReadOnlyComposable get() = pick(LocalDarkTheme.current, 0xFFB45309, 0xFFFBBF24)

/** Text on a filled status colour: white on the dark light-theme fills, navy on the bright dark-theme ones. */
val OnStatus: Color @Composable @ReadOnlyComposable get() = pick(LocalDarkTheme.current, 0xFFFFFFFF, 0xFF0F172A)

val PmpGoldTertiary: Color @Composable @ReadOnlyComposable get() = pick(LocalDarkTheme.current, 0xFFB45309, 0xFFFBBF24)
