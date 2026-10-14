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
import kotlin.math.roundToInt

/**
 * The 5×1 home-screen widget: days left, today's progress, readiness and syllabus,
 * as four clay tiles. Each tile opens its own screen in the app.
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
                    Tile(if (days == null) "–" else "$days", null, "days left", null, DAYS)
                    Gap()
                    val today = stats?.today(now) ?: 0f
                    Tile("${(today * 100).roundToInt()}%", null, "today", today, TODAY)
                    Gap()
                    Tile(stats?.readiness?.toString() ?: "–", "/100", "ready", null, READY)
                    Gap()
                    val syllabus = stats?.syllabus() ?: 0f
                    Tile("${(syllabus * 100).roundToInt()}%", null, "syllabus", syllabus, SYLLABUS)
                    Gap()
                    val fit = stats?.fitness ?: -1
                    Tile(if (fit < 0) "–" else "$fit", null, "fitness", null, FITNESS)
                }
            }
        }
    }

    @Composable
    private fun Gap() = Spacer(GlanceModifier.width(5.dp))

    @Composable
    private fun androidx.glance.layout.RowScope.Tile(value: String, unit: String?, label: String, progress: Float?, open: String) {
        val c = GlanceTheme.colors
        Column(
            GlanceModifier.defaultWeight().fillMaxHeight().background(c.secondaryContainer).cornerRadius(21.dp)
                .clickable(actionStartActivity<MainActivity>(actionParametersOf(OpenKey to open))),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(verticalAlignment = Alignment.Bottom) {
                Text(value, style = TextStyle(color = c.onSurface, fontSize = 17.sp, fontWeight = FontWeight.Bold), maxLines = 1)
                if (unit != null) Text(unit, style = TextStyle(color = c.onSurfaceVariant, fontSize = 10.sp), maxLines = 1)
            }
            if (progress != null) {
                Spacer(GlanceModifier.height(2.dp))
                LinearProgressIndicator(progress, GlanceModifier.width(44.dp).height(4.dp), color = c.primary, backgroundColor = c.primaryContainer)
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
        private val OpenKey = ActionParameters.Key<String>(OPEN)

        private val AppColors = ColorProviders(light = LightColors, dark = DarkColors)
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
            )
        }

        /** Saves the app's latest figures and redraws every placed widget. */
        suspend fun publish(context: Context, s: WidgetStats) {
            prefs(context).edit()
                .putLong("exam", s.examDate ?: 0L).putLong("day", s.day)
                .putInt("todayMinutes", s.todayMinutes).putInt("targetMinutes", s.targetMinutes)
                .putInt("readiness", s.readiness).putInt("lessonsDone", s.lessonsDone).putInt("lessonsTotal", s.lessonsTotal)
                .putInt("fitness", s.fitness)
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
