package com.jahangirb.pmpprep.reminders

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.AppOpsManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Process
import android.provider.Settings
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.jahangirb.pmpprep.MainActivity
import com.jahangirb.pmpprep.R
import com.jahangirb.pmpprep.data.Attempt
import com.jahangirb.pmpprep.data.PlanEngine
import com.jahangirb.pmpprep.data.PlannedReminder
import com.jahangirb.pmpprep.data.ReminderBrain
import com.jahangirb.pmpprep.data.ReminderKind
import com.jahangirb.pmpprep.data.ReminderSnapshot
import com.jahangirb.pmpprep.data.ReminderState
import com.jahangirb.pmpprep.data.ReminderText
import com.jahangirb.pmpprep.data.SessionResult
import com.jahangirb.pmpprep.widget.PmpWidget
import org.json.JSONObject
import java.io.File

/**
 * The Android side of smart reminders: keeps [ReminderState] in a file, sets an alarm for each of
 * today's reminders, posts them, and records what you did with them. The deciding is in [ReminderBrain].
 * Nothing here leaves the phone.
 */
object Reminders {
    const val EXTRA_REMINDER = "reminder"
    private const val CHANNEL_STUDY = "study"
    private const val CHANNEL_WEEKLY = "weekly"
    internal const val ACTION_FIRE = "com.jahangirb.pmpprep.REMINDER_FIRE"
    internal const val ACTION_NOT_TODAY = "com.jahangirb.pmpprep.REMINDER_NOT_TODAY"
    internal const val ACTION_REPLAN = "com.jahangirb.pmpprep.REMINDER_REPLAN"
    /** Alarm slots: at most 3 study reminders, a streak-saver and the weekly summary on one day. */
    private const val SLOTS = 6
    private const val NOTIFICATION_ID = 7100

    private val lock = Any()
    private fun file(ctx: Context) = File(ctx.filesDir, "reminders.json")

    fun load(ctx: Context): ReminderState = synchronized(lock) {
        runCatching { ReminderState.fromJson(JSONObject(file(ctx).readText())) }.getOrElse { ReminderState() }
    }

    /** Reads, changes and saves the state under one lock. */
    fun <T> update(ctx: Context, block: (ReminderState) -> T): T = synchronized(lock) {
        val st = load(ctx)
        val r = block(st)
        file(ctx).writeText(st.toJson().toString())
        r
    }

    /* ── permissions ─────────────────────────────────────────────────────── */

    fun notificationsAllowed(ctx: Context): Boolean =
        (Build.VERSION.SDK_INT < 33 || ContextCompat.checkSelfPermission(ctx, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) &&
            NotificationManagerCompat.from(ctx).areNotificationsEnabled()

    /** Usage access is a switch you turn on in Settings; no app can grant it to itself. */
    fun usageAccessGranted(ctx: Context): Boolean {
        val ops = ctx.getSystemService(AppOpsManager::class.java) ?: return false
        val mode = if (Build.VERSION.SDK_INT >= 29) ops.unsafeCheckOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, Process.myUid(), ctx.packageName)
        else @Suppress("DEPRECATION") ops.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, Process.myUid(), ctx.packageName)
        return if (mode == AppOpsManager.MODE_DEFAULT) ctx.checkSelfPermission(Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED
        else mode == AppOpsManager.MODE_ALLOWED
    }

    fun usageAccessSettings(): Intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)

    /**
     * Your idea Y1: on what share of the last 14 days the phone was in use at each hour,
     * from screen-on, unlock and app-switch events. Only times are read, never which apps.
     */
    fun readPhoneUsage(ctx: Context, now: Long): DoubleArray? {
        if (!usageAccessGranted(ctx)) return null
        val usm = ctx.getSystemService(UsageStatsManager::class.java) ?: return null
        val events = runCatching { usm.queryEvents(now - 14 * 24 * ReminderBrain.HOUR, now) }.getOrNull() ?: return null
        val byDay = HashMap<Long, BooleanArray>()
        val e = UsageEvents.Event()
        val cal = java.util.Calendar.getInstance()
        while (events.hasNextEvent()) {
            events.getNextEvent(e)
            @Suppress("DEPRECATION")
            val active = e.eventType == UsageEvents.Event.MOVE_TO_FOREGROUND ||
                (Build.VERSION.SDK_INT >= 28 && (e.eventType == UsageEvents.Event.SCREEN_INTERACTIVE || e.eventType == UsageEvents.Event.KEYGUARD_HIDDEN))
            if (!active) continue
            cal.timeInMillis = e.timeStamp
            byDay.getOrPut(PlanEngine.startOfDay(e.timeStamp)) { BooleanArray(24) }[cal.get(java.util.Calendar.HOUR_OF_DAY)] = true
        }
        if (byDay.size < 2) return null
        return DoubleArray(24) { h -> byDay.values.count { it[h] }.toDouble() / byDay.size }
    }

    private fun refreshPhoneUsage(ctx: Context, st: ReminderState, now: Long) {
        if (!st.settings.usePhoneUsage) { st.phoneUsage = null; return }
        if (st.phoneUsage == null || now - st.phoneUsageAt > 24 * ReminderBrain.HOUR) {
            readPhoneUsage(ctx, now)?.let { st.phoneUsage = it; st.phoneUsageAt = now }
        }
    }

    /* ── scheduling ──────────────────────────────────────────────────────── */

    /** Sets today's alarms (choosing today's times first if needed), and one to re-plan after midnight. */
    fun schedule(ctx: Context, replan: Boolean = false) {
        val now = System.currentTimeMillis()
        val plan = update(ctx) { st ->
            refreshPhoneUsage(ctx, st, now)
            if (replan) ReminderBrain.replan(st, now) else ReminderBrain.planToday(st, now)
            st.plan.filter { it.at > now }
        }
        val am = ctx.getSystemService(AlarmManager::class.java) ?: return
        for (i in 0 until SLOTS) PendingIntent.getBroadcast(ctx, i, fireIntent(ctx, null), PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE)?.let { am.cancel(it) }
        // Inexact alarms allowed in Doze: no special permission, and a few minutes either way is fine for studying.
        plan.take(SLOTS).forEachIndexed { i, p ->
            am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, p.at,
                PendingIntent.getBroadcast(ctx, i, fireIntent(ctx, p), PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE))
        }
        val midnight = PlanEngine.nextDay(PlanEngine.startOfDay(now)) + 2 * ReminderBrain.MINUTE
        am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, midnight,
            PendingIntent.getBroadcast(ctx, 100, Intent(ctx, ReminderReceiver::class.java).setAction(ACTION_REPLAN), PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE))
    }

    private fun fireIntent(ctx: Context, p: PlannedReminder?) = Intent(ctx, ReminderReceiver::class.java).setAction(ACTION_FIRE).apply {
        if (p != null) putExtra("at", p.at).putExtra("hour", p.hour).putExtra("kind", p.kind.name)
    }

    /* ── firing ──────────────────────────────────────────────────────────── */

    @SuppressLint("MissingPermission") // checked by notificationsAllowed
    internal fun fire(ctx: Context, p: PlannedReminder) {
        if (!notificationsAllowed(ctx)) return
        val now = System.currentTimeMillis()
        val sent = update(ctx) { st ->
            if (!ReminderBrain.shouldSend(st, p, now)) return@update null
            ReminderBrain.noteSent(st, p, now)
            val score = ReminderBrain.scores(st, now)[p.hour]
            ReminderText.compose(p.kind, st, now, learnedHour = score.tried >= 3 && score.mean >= ReminderBrain.GOOD_HOUR)
        } ?: return
        channels(ctx)
        val id = now
        val n = NotificationCompat.Builder(ctx, if (p.kind == ReminderKind.SUMMARY) CHANNEL_WEEKLY else CHANNEL_STUDY)
            .setSmallIcon(R.drawable.ic_stat_reminder)
            .setContentTitle(sent.title)
            .setContentText(sent.body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(sent.body))
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setAutoCancel(true)
            .setTimeoutAfter(3 * ReminderBrain.HOUR)
        when (p.kind) {
            ReminderKind.STUDY -> {
                n.setContentIntent(open(ctx, id, PmpWidget.TODAY, 0))
                n.addAction(0, "Start", open(ctx, id, PmpWidget.TODAY, 1))
                n.addAction(0, "15-minute version", open(ctx, id, SHORT, 2))
                n.addAction(0, "Not today", notToday(ctx, id))
            }
            ReminderKind.STREAK -> {
                n.setContentIntent(open(ctx, id, CARDS, 0))
                n.addAction(0, "Review cards", open(ctx, id, CARDS, 1))
                n.addAction(0, "Not today", notToday(ctx, id))
            }
            ReminderKind.SUMMARY -> {
                n.setContentIntent(open(ctx, id, PmpWidget.READY, 0))
                n.addAction(0, "See analytics", open(ctx, id, PmpWidget.READY, 1))
            }
        }
        NotificationManagerCompat.from(ctx).notify(NOTIFICATION_ID + p.kind.ordinal, n.build())
    }

    const val SHORT = "short"
    const val CARDS = "cards"

    private fun open(ctx: Context, id: Long, target: String, slot: Int): PendingIntent {
        val i = Intent(ctx, MainActivity::class.java).putExtra(PmpWidget.OPEN, target).putExtra(EXTRA_REMINDER, id)
        return PendingIntent.getActivity(ctx, 200 + slot, i, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    }

    private fun notToday(ctx: Context, id: Long): PendingIntent {
        val i = Intent(ctx, ReminderReceiver::class.java).setAction(ACTION_NOT_TODAY).putExtra(EXTRA_REMINDER, id)
        return PendingIntent.getBroadcast(ctx, 300, i, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    }

    private fun channels(ctx: Context) {
        if (Build.VERSION.SDK_INT < 26) return
        val nm = ctx.getSystemService(NotificationManager::class.java) ?: return
        nm.createNotificationChannel(NotificationChannel(CHANNEL_STUDY, "Study reminders", NotificationManager.IMPORTANCE_DEFAULT).apply {
            description = "Today's session, at the times that work for you"
        })
        nm.createNotificationChannel(NotificationChannel(CHANNEL_WEEKLY, "Weekly summary", NotificationManager.IMPORTANCE_LOW).apply {
            description = "Sunday evening: your week in numbers"
        })
    }

    /* ── outcomes ────────────────────────────────────────────────────────── */

    internal fun notToday(ctx: Context, id: Long) {
        update(ctx) { ReminderBrain.noteNotToday(it, id, System.currentTimeMillis()) }
        NotificationManagerCompat.from(ctx).cancel(NOTIFICATION_ID + ReminderKind.STUDY.ordinal)
        NotificationManagerCompat.from(ctx).cancel(NOTIFICATION_ID + ReminderKind.STREAK.ordinal)
    }

    /** The app came to the front, from a reminder ([tappedId]) or on its own. */
    fun opened(ctx: Context, tappedId: Long?) = update(ctx) { ReminderBrain.noteOpened(it, System.currentTimeMillis(), tappedId) }

    /** The app went to the back after being in front since [from] (your idea Y3). */
    fun foreground(ctx: Context, from: Long) {
        val now = System.currentTimeMillis()
        if (now - from in 5_000..(12 * ReminderBrain.HOUR)) update(ctx) { ReminderBrain.noteForeground(it, from, now) }
    }

    /** The app's latest picture of your study; then today's alarms are refreshed. */
    fun publish(ctx: Context, snapshot: ReminderSnapshot, results: List<SessionResult>, attempts: List<Attempt>) {
        val now = System.currentTimeMillis()
        update(ctx) { st ->
            st.snapshot = snapshot
            val (habit, days) = ReminderBrain.studyHabit(attempts, results, now)
            habit.copyInto(st.studyHabit); st.studyHabitDays = days
            ReminderBrain.noteStudy(st, results, attempts, now)
        }
        schedule(ctx)
    }
}

/** Alarms, the "Not today" button, and the after-midnight re-plan. Not exported: only this app's own intents. */
class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(ctx: Context, intent: Intent) {
        when (intent.action) {
            Reminders.ACTION_FIRE -> {
                val kind = runCatching { ReminderKind.valueOf(intent.getStringExtra("kind") ?: "") }.getOrNull() ?: return
                Reminders.fire(ctx, PlannedReminder(intent.getLongExtra("at", 0L), intent.getIntExtra("hour", 0), kind))
            }
            Reminders.ACTION_NOT_TODAY -> Reminders.notToday(ctx, intent.getLongExtra(Reminders.EXTRA_REMINDER, 0L))
            Reminders.ACTION_REPLAN -> Reminders.schedule(ctx)
        }
    }
}

/** Alarms don't survive a restart or an app update; this sets them again. */
class ReminderBootReceiver : BroadcastReceiver() {
    override fun onReceive(ctx: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED || intent.action == Intent.ACTION_MY_PACKAGE_REPLACED ||
            intent.action == Intent.ACTION_TIMEZONE_CHANGED || intent.action == Intent.ACTION_TIME_CHANGED) Reminders.schedule(ctx)
    }
}
