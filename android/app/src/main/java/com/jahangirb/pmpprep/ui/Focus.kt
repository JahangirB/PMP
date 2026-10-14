package com.jahangirb.pmpprep.ui

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.provider.Settings

/**
 * Focus mode for mocks: Do Not Disturb while the exam is on screen. Only switches it on when it was
 * off, and only switches back what it switched on itself, so your own Do Not Disturb is left alone.
 */
object Focus {
    private fun nm(ctx: Context) = ctx.getSystemService(NotificationManager::class.java)
    private fun prefs(ctx: Context) = ctx.getSharedPreferences("focus", Context.MODE_PRIVATE)

    fun hasAccess(ctx: Context): Boolean = runCatching { nm(ctx).isNotificationPolicyAccessGranted }.getOrDefault(false)

    /** The settings page where the app is given "Do Not Disturb access". */
    fun accessIntent(): Intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

    fun begin(ctx: Context) {
        if (!hasAccess(ctx)) return
        val n = nm(ctx)
        if (n.currentInterruptionFilter == NotificationManager.INTERRUPTION_FILTER_ALL) {
            runCatching { n.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_PRIORITY) }
                .onSuccess { prefs(ctx).edit().putBoolean("on", true).apply() }
        }
    }

    fun end(ctx: Context) {
        val p = prefs(ctx)
        if (!p.getBoolean("on", false)) return
        p.edit().remove("on").apply()
        if (!hasAccess(ctx)) return
        val n = nm(ctx)
        if (n.currentInterruptionFilter == NotificationManager.INTERRUPTION_FILTER_PRIORITY) {
            runCatching { n.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL) }
        }
    }
}
