package com.jahangirb.pmpprep.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.jahangirb.pmpprep.data.AppUpdate
import com.jahangirb.pmpprep.data.Release
import com.jahangirb.pmpprep.ui.PmpViewModel
import com.jahangirb.pmpprep.ui.PmpViewModel.UpdateState
import com.jahangirb.pmpprep.ui.components.ClayButton as Button
import com.jahangirb.pmpprep.ui.components.ClayOutlinedButton as OutlinedButton
import com.jahangirb.pmpprep.ui.components.PmpCard
import com.jahangirb.pmpprep.ui.components.SectionLabel
import com.jahangirb.pmpprep.ui.theme.ColorError
import com.jahangirb.pmpprep.update.UpdateInstaller

private fun mb(bytes: Long) = "%.1f MB".format(bytes / 1_048_576.0)
private fun releaseLine(r: Release) = "Build ${r.build} is available" +
    (r.publishedAt.take(10).takeIf { it.length == 10 }?.let { " (released $it)" } ?: "") + (if (r.bytes > 0) ", ${mb(r.bytes)}." else ".")

/** Menu → App updates: the newest build from GitHub, downloaded here and handed to Android to install. */
@Composable
fun UpdateScreen(vm: PmpViewModel) {
    val context = LocalContext.current
    val (code, name) = vm.appVersion
    var allowed by remember { mutableStateOf(UpdateInstaller.canInstall(context)) }
    val failure by UpdateInstaller.failure.collectAsState()
    // Back from "Allow from this source": carry on with the install.
    val owner = androidx.compose.ui.platform.LocalLifecycleOwner.current
    DisposableEffect(owner) {
        val o = LifecycleEventObserver { _, e ->
            if (e == Lifecycle.Event.ON_RESUME) {
                val was = allowed
                allowed = UpdateInstaller.canInstall(context)
                if (allowed && !was) vm.installUpdate()
            }
        }
        owner.lifecycle.addObserver(o)
        onDispose { owner.lifecycle.removeObserver(o) }
    }

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text("App updates", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
        Text("This phone has version $name (build ${AppUpdate.buildOf(code)}).", fontSize = 15.sp)
        PmpCard {
            when (val u = vm.update) {
                UpdateState.Idle -> {
                    Text("New builds are published on GitHub. The app also looks once a day and shows \"Update available\" in the menu.", fontSize = 14.sp, lineHeight = 20.sp)
                    Spacer(Modifier.height(10.dp))
                    Button(onClick = { vm.checkForUpdate() }, modifier = Modifier.fillMaxWidth()) { Text("Check for updates", fontWeight = FontWeight.Bold) }
                }
                UpdateState.Checking -> {
                    CircularProgressIndicator(Modifier.size(24.dp))
                    Spacer(Modifier.height(8.dp))
                    Text("Asking GitHub for the newest build…", fontSize = 14.sp)
                }
                UpdateState.UpToDate -> {
                    Text("You have the newest version.", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(10.dp))
                    OutlinedButton(onClick = { vm.checkForUpdate() }) { Text("Check again") }
                }
                is UpdateState.Available -> {
                    Text(releaseLine(u.r), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(10.dp))
                    Button(onClick = { vm.downloadUpdate(u.r) }, modifier = Modifier.fillMaxWidth()) { Text("Download and install", fontWeight = FontWeight.Bold) }
                }
                is UpdateState.Downloading -> {
                    Text("Downloading build ${u.r.build}", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(10.dp))
                    if (u.total > 0) LinearProgressIndicator(progress = { (u.done.toFloat() / u.total).coerceIn(0f, 1f) }, modifier = Modifier.fillMaxWidth())
                    else LinearProgressIndicator(Modifier.fillMaxWidth())
                    Spacer(Modifier.height(6.dp))
                    Text(if (u.total > 0) "${mb(u.done)} of ${mb(u.total)}" else mb(u.done), fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    TextButton(onClick = { vm.cancelUpdate() }) { Text("Cancel") }
                }
                is UpdateState.Ready -> {
                    Text("Build ${u.r.build} is downloaded.", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(6.dp))
                    if (!allowed) {
                        Text("First, Android needs your permission for PMP Prep to install updates. Switch on \"Allow from this source\", then come back.",
                            fontSize = 14.sp, lineHeight = 20.sp)
                        Spacer(Modifier.height(10.dp))
                        Button(onClick = { context.startActivity(UpdateInstaller.permissionIntent(context)) }, modifier = Modifier.fillMaxWidth()) {
                            Text("Allow installs from PMP Prep", fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Text("Android asks you to confirm the update. Your progress stays; the app restarts on the new version.",
                            fontSize = 14.sp, lineHeight = 20.sp)
                        Spacer(Modifier.height(10.dp))
                        Button(onClick = { vm.installUpdate() }, modifier = Modifier.fillMaxWidth()) { Text("Install", fontWeight = FontWeight.Bold) }
                    }
                }
                is UpdateState.Failed -> {
                    Text(u.message, color = ColorError, fontSize = 14.sp, lineHeight = 20.sp)
                    Spacer(Modifier.height(10.dp))
                    Button(onClick = { if (u.r != null) vm.downloadUpdate(u.r) else vm.checkForUpdate() }) { Text("Try again") }
                }
            }
            failure?.let {
                Spacer(Modifier.height(8.dp))
                Text(it, color = ColorError, fontSize = 13.sp)
            }
        }
        PmpCard {
            SectionLabel("How it works")
            Spacer(Modifier.height(6.dp))
            Text("Updates come from this app's releases on GitHub. The download is the same APK as the Download button on the website, signed with the same key, so it installs over this one and keeps your progress. Android always asks you to confirm an update; the first time it also asks you to allow installs from PMP Prep.",
                fontSize = 13.sp, lineHeight = 19.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            TextButton(onClick = { openLink(context, AppUpdate.RELEASES_PAGE) }) { Text("See all releases") }
        }
        Spacer(Modifier.height(16.dp))
    }
}
