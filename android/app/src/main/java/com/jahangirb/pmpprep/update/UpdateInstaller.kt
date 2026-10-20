package com.jahangirb.pmpprep.update

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInstaller
import android.net.Uri
import android.os.Build
import android.provider.Settings
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.File

/**
 * Hands a downloaded APK to Android's package installer, which replaces this app in place
 * (same signing key, so progress stays). Android asks the person to confirm; an app can't
 * update itself silently. The first time, it must also be allowed to install apps.
 */
object UpdateInstaller {
    /** A failure Android reported after the hand-over, for the update screen to show. */
    val failure = MutableStateFlow<String?>(null)

    fun canInstall(ctx: Context): Boolean =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.O || ctx.packageManager.canRequestPackageInstalls()

    /** The settings page where "Allow from this source" is switched on for this app. */
    fun permissionIntent(ctx: Context): Intent =
        Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, Uri.parse("package:" + ctx.packageName))

    fun install(ctx: Context, apk: File) {
        failure.value = null
        val installer = ctx.packageManager.packageInstaller
        val params = PackageInstaller.SessionParams(PackageInstaller.SessionParams.MODE_FULL_INSTALL).apply {
            setAppPackageName(ctx.packageName)
            // Once this app installed itself, Android 12+ may let later updates through without asking.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) setRequireUserAction(PackageInstaller.SessionParams.USER_ACTION_NOT_REQUIRED)
        }
        val id = installer.createSession(params)
        installer.openSession(id).use { s ->
            apk.inputStream().use { input ->
                s.openWrite("PMP-Prep.apk", 0, apk.length()).use { out ->
                    input.copyTo(out)
                    s.fsync(out)
                }
            }
            val flags = PendingIntent.FLAG_UPDATE_CURRENT or (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_MUTABLE else 0)
            val done = PendingIntent.getBroadcast(ctx, id, Intent(ctx, UpdateReceiver::class.java).setPackage(ctx.packageName), flags)
            s.commit(done.intentSender)
        }
    }
}

/** Android's answer to the install: show its confirmation, or report why it failed. */
class UpdateReceiver : BroadcastReceiver() {
    override fun onReceive(ctx: Context, intent: Intent) {
        when (intent.getIntExtra(PackageInstaller.EXTRA_STATUS, PackageInstaller.STATUS_FAILURE)) {
            PackageInstaller.STATUS_PENDING_USER_ACTION -> {
                val confirm: Intent? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                    intent.getParcelableExtra(Intent.EXTRA_INTENT, Intent::class.java)
                else @Suppress("DEPRECATION") intent.getParcelableExtra(Intent.EXTRA_INTENT)
                confirm?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                runCatching { ctx.startActivity(confirm) }
                    .onFailure { UpdateInstaller.failure.value = "Android couldn't show the update. Open the app and press Install again." }
            }
            PackageInstaller.STATUS_SUCCESS -> Unit // the new version replaces this one and starts fresh
            PackageInstaller.STATUS_FAILURE_ABORTED -> UpdateInstaller.failure.value = "The update was cancelled."
            else -> UpdateInstaller.failure.value = intent.getStringExtra(PackageInstaller.EXTRA_STATUS_MESSAGE)
                ?.let { "Android didn't install the update: $it" } ?: "Android didn't install the update."
        }
    }
}
