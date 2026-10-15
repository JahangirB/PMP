package com.jahangirb.pmpprep.widget

import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.ImageProvider
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.LinearProgressIndicator
import androidx.glance.action.actionStartActivity
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.updateAll
import androidx.glance.background
import androidx.glance.color.ColorProvider
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxHeight
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.material3.ColorProviders
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.jahangirb.pmpprep.MainActivity
import com.jahangirb.pmpprep.R
import com.jahangirb.pmpprep.data.PlanEngine
import com.jahangirb.pmpprep.data.WidgetStats
import com.jahangirb.pmpprep.ui.theme.DarkColors
import com.jahangirb.pmpprep.ui.theme.LightColors
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

/**
 * The 5×1 home-screen widget: days left, today's progress, readiness, syllabus, exam fitness and
 * the date your pace says you'll be ready, as six clay tiles. Each tile opens its own screen.
 * Each bar runs from red (low) to green (good); the countdown runs the other way, from green
 * with plenty of time to red in the last week. The ready date is green when it's on or before
 * the exam, red when it's after.
 *
 * The app saves a [WidgetStats] snapshot whenever progress changes; the widget only reads it.
 */
class PmpWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val stats = load(context)
        val system = Build.VERSION.SDK_INT >= 31 &&
            prefs(context).getBoolean(systemKey(GlanceAppWidgetManager(context).getAppWidgetId(id)), false)
        provideContent {
            if (system) GlanceTheme { Content(stats) }
            else GlanceTheme(colors = AppColors) { Content(stats) }
        }
    }

    @Composable
    private fun Content(stats: WidgetStats?) {
        val now = System.currentTimeMillis()
        val c = GlanceTheme.colors
        // Shadow image behind, then a light rim, then the cream capsule holding the tiles.
        Box(GlanceModifier.fillMaxSize().background(ImageProvider(R.drawable.widget_shadow)).padding(start = 4.dp, end = 4.dp, top = 3.dp, bottom = 5.dp)) {
            Box(GlanceModifier.fillMaxSize().background(Rim).cornerRadius(28.dp).padding(2.dp)) {
                Row(GlanceModifier.fillMaxSize().background(c.surface).cornerRadius(26.dp).padding(5.dp), verticalAlignment = Alignment.CenterVertically) {
                    val days = stats?.daysLeft(now)
                    Tile(if (days == null) "–" else "$days", "days left", stats?.daysLevel(now), DAYS)
                    Gap()
                    val today = stats?.today(now) ?: 0f
                    Tile("${(today * 100).roundToInt()}%", "today", today, TODAY)
                    Gap()
                    Tile(stats?.readiness?.toString() ?: "–", "ready", stats?.readiness?.let { it / 100f }, READY)
                    Gap()
                    val syllabus = stats?.syllabus() ?: 0f
                    Tile("${(syllabus * 100).roundToInt()}%", "syllabus", syllabus, SYLLABUS)
                    Gap()
                    val fit = stats?.fitness ?: -1
                    Tile(if (fit < 0) "–" else "$fit", "fitness", if (fit < 0) null else fit / 100f, FITNESS)
                    Gap()
                    val track = stats?.onTrack()
                    val readyText = when (stats?.readyState) {
                        WidgetStats.READY_NOW -> "Now"
                        WidgetStats.READY_STALLED -> "Stalled"
                        WidgetStats.READY_PROJECTED -> SimpleDateFormat("d MMM", Locale.getDefault()).format(Date(stats.readyBy))
                        else -> "–"
                    }
                    Tile(readyText, "ready by", track?.let { if (it) 1f else 0f }, BOOK, colourValue = track != null)
                }
            }
        }
    }

    @Composable
    private fun Gap() = Spacer(GlanceModifier.width(4.dp))

    /** [level] 0..1 fills the bar and sets its colour, red to green; null shows no bar. */
    @Composable
    private fun androidx.glance.layout.RowScope.Tile(value: String, label: String, level: Float?, open: String, colourValue: Boolean = false) {
        val heat = level?.let { Heat(it) }
        val c = GlanceTheme.colors
        Column(
            GlanceModifier.defaultWeight().fillMaxHeight().background(c.secondaryContainer).cornerRadius(21.dp)
                .clickable(actionStartActivity<MainActivity>(actionParametersOf(OpenKey to open))),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(value, style = TextStyle(color = if (colourValue && heat != null) heat else c.onSurface, fontSize = 15.sp, fontWeight = FontWeight.Bold), maxLines = 1)
            if (level != null && heat != null) {
                Spacer(GlanceModifier.height(2.dp))
                LinearProgressIndicator(level.coerceIn(0f, 1f), GlanceModifier.width(36.dp).height(4.dp), color = heat, backgroundColor = c.primaryContainer)
            }
            Text(label, style = TextStyle(color = c.onSurfaceVariant, fontSize = 10.sp, fontWeight = FontWeight.Medium), maxLines = 1)
        }
    }

    companion object {
        /** Intent extra naming the screen a tile opens. */
        const val OPEN = "open"
        const val DAYS = "days"
        const val TODAY = "today"
        const val READY = "ready"
        const val SYLLABUS = "syllabus"
        const val FITNESS = "fitness"
        const val REPORTS = "reports"
        const val BOOK = "book"
        private val OpenKey = ActionParameters.Key<String>(OPEN)

        private val AppColors = ColorProviders(light = LightColors, dark = DarkColors)
        /** The red-to-green scale, one shade for light and dark alike: it reads on both tile colours. */
        private fun Heat(level: Float) = Color(WidgetStats.heat(level)).let { ColorProvider(day = it, night = it) }
        private val Rim = ColorProvider(day = Color.White.copy(alpha = 0.9f), night = Color.White.copy(alpha = 0.07f))

        private fun prefs(context: Context) = context.getSharedPreferences("widget", Context.MODE_PRIVATE)
        fun systemKey(appWidgetId: Int) = "system_colors_$appWidgetId"

        fun useSystemColors(context: Context, appWidgetId: Int, system: Boolean) =
            prefs(context).edit().putBoolean(systemKey(appWidgetId), system).apply()

        private fun load(context: Context): WidgetStats? {
            val p = prefs(context)
            if (!p.contains("day")) return null
            return WidgetStats(
                examDate = p.getLong("exam", 0L).takeIf { it > 0 },
                day = p.getLong("day", 0L),
                todayMinutes = p.getInt("todayMinutes", 0),
                targetMinutes = p.getInt("targetMinutes", 0),
                readiness = p.getInt("readiness", 0),
                lessonsDone = p.getInt("lessonsDone", 0),
                lessonsTotal = p.getInt("lessonsTotal", 0),
                fitness = p.getInt("fitness", -1),
                readyBy = p.getLong("readyBy", 0L),
                readyState = p.getInt("readyState", WidgetStats.READY_UNKNOWN),
            )
        }

        /** Saves the app's latest figures and redraws every placed widget. */
        suspend fun publish(context: Context, s: WidgetStats) {
            prefs(context).edit()
                .putLong("exam", s.examDate ?: 0L).putLong("day", s.day)
                .putInt("todayMinutes", s.todayMinutes).putInt("targetMinutes", s.targetMinutes)
                .putInt("readiness", s.readiness).putInt("lessonsDone", s.lessonsDone).putInt("lessonsTotal", s.lessonsTotal)
                .putInt("fitness", s.fitness).putLong("readyBy", s.readyBy).putInt("readyState", s.readyState)
                .apply()
            PmpWidget().updateAll(context)
        }
    }
}

/** Redraws the widget after a session (via [PmpWidget.publish]) and just after each midnight. */
class PmpWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = PmpWidget()

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        scheduleMidnight(context)
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        context.getSystemService(AlarmManager::class.java).cancel(midnightIntent(context))
    }

    private fun midnightIntent(context: Context): PendingIntent {
        val ids = AppWidgetManager.getInstance(context).getAppWidgetIds(android.content.ComponentName(context, PmpWidgetReceiver::class.java))
        val i = Intent(context, PmpWidgetReceiver::class.java)
            .setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
            .putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        return PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
    }

    /** Inexact, so it needs no special permission; it lands a minute or so after midnight. */
    private fun scheduleMidnight(context: Context) {
        val next = PlanEngine.nextDay(PlanEngine.startOfDay(System.currentTimeMillis())) + 60_000
        context.getSystemService(AlarmManager::class.java).set(AlarmManager.RTC, next, midnightIntent(context))
    }
}
