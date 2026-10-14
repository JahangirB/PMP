package com.jahangirb.pmpprep.listen

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.core.content.ContextCompat
import com.jahangirb.pmpprep.MainActivity
import com.jahangirb.pmpprep.R

/** Keeps Listen mode reading with the screen off, with pause and stop in its notification. */
class ListenService : Service() {
    private var wake: PowerManager.WakeLock? = null

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            PAUSE -> Listen.pause()
            RESUME -> Listen.resume()
            STOP -> { Listen.stop(); stopSelf(); return START_NOT_STICKY }
        }
        if (Listen.status == Listen.Status.IDLE && intent?.action != null) { stopSelf(); return START_NOT_STICKY }
        ServiceCompat.startForeground(this, ID, notification(this),
            if (Build.VERSION.SDK_INT >= 29) ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK else 0)
        if (wake == null) wake = getSystemService(PowerManager::class.java)
            .newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "pmpprep:listen").apply { acquire(3 * 60 * 60 * 1000L) }
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        wake?.let { if (it.isHeld) it.release() }
        wake = null
        super.onDestroy()
    }

    companion object {
        private const val CHANNEL = "listen"
        private const val ID = 4040
        private const val PAUSE = "pause"
        private const val RESUME = "resume"
        private const val STOP = "stop"

        fun start(ctx: Context) {
            runCatching { ContextCompat.startForegroundService(ctx, Intent(ctx, ListenService::class.java)) }
        }

        /** Refreshes the notification after a pause or resume. */
        fun update(ctx: Context) {
            if (Listen.status == Listen.Status.IDLE) return
            runCatching { ctx.getSystemService(NotificationManager::class.java).notify(ID, notification(ctx)) }
        }

        fun stop(ctx: Context) {
            runCatching { ctx.stopService(Intent(ctx, ListenService::class.java)) }
        }

        private fun action(ctx: Context, a: String) = PendingIntent.getService(ctx, a.hashCode(),
            Intent(ctx, ListenService::class.java).setAction(a), PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        private fun notification(ctx: Context): android.app.Notification {
            val nm = ctx.getSystemService(NotificationManager::class.java)
            if (Build.VERSION.SDK_INT >= 26 && nm.getNotificationChannel(CHANNEL) == null) {
                nm.createNotificationChannel(NotificationChannel(CHANNEL, "Listen mode", NotificationManager.IMPORTANCE_LOW).apply {
                    description = "Shows what's being read aloud, with pause and stop."
                })
            }
            val open = PendingIntent.getActivity(ctx, 0, Intent(ctx, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
            val paused = Listen.status == Listen.Status.PAUSED
            return NotificationCompat.Builder(ctx, CHANNEL)
                .setSmallIcon(R.drawable.ic_stat_reminder)
                .setContentTitle(Listen.title.ifBlank { "Listen mode" })
                .setContentText(when (Listen.status) {
                    Listen.Status.PREPARING -> "Preparing the voice…"
                    Listen.Status.PAUSED -> "Paused"
                    else -> "Reading part ${Listen.part + 1} of ${Listen.parts}"
                })
                .setContentIntent(open)
                .setOngoing(!paused)
                .setSilent(true)
                .setCategory(NotificationCompat.CATEGORY_TRANSPORT)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .addAction(0, if (paused) "Resume" else "Pause", action(ctx, if (paused) RESUME else PAUSE))
                .addAction(0, "Stop", action(ctx, STOP))
                .build()
        }
    }
}
