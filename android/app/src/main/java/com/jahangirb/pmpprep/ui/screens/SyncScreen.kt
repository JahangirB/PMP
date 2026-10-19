package com.jahangirb.pmpprep.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jahangirb.pmpprep.ui.PmpViewModel
import com.jahangirb.pmpprep.ui.components.ClayButton as Button
import com.jahangirb.pmpprep.ui.components.ClayOutlinedButton as OutlinedButton
import com.jahangirb.pmpprep.ui.components.PmpCard
import com.jahangirb.pmpprep.ui.components.SectionLabel
import com.jahangirb.pmpprep.ui.theme.ColorError
import com.jahangirb.pmpprep.ui.theme.ColorSuccess

private const val SCRIPT_HOME = "https://script.google.com"

/**
 * Cloud sync (docs/progress-format.md §5). Off until set up: the person deploys our small
 * Apps Script in their own Google account, and the apps keep one progress file in their
 * Drive through it. Set up here once; every other device joins with the same URL and code.
 * Errors stay on this screen with the time of the last good sync, never as pop-ups.
 */
@Composable
fun SyncScreen(vm: PmpViewModel) {
    val context = LocalContext.current
    var copied by remember { mutableStateOf<String?>(null) }
    val copy: (String, String) -> Unit = { label, text -> copyText(context, label, text); copied = label }

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Cloud sync", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
        Text("Keeps one copy of your progress and notes in your own Google Drive, so this phone, the web app and your other devices share it. " +
            "It goes through a small script you deploy in your Google account. Nothing is sent anywhere else, and nobody else runs a server for it.",
            fontSize = 13.sp, lineHeight = 18.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        if (vm.syncOn) Connected(vm, copy) else Setup(vm, context, copy)
        copied?.let { Text("$it copied.", fontSize = 12.sp, color = ColorSuccess) }
        Text("Sync never runs during a mock exam: the exam's answers go up together when you submit it. The Gemini key and this phone's settings never go in the file.",
            fontSize = 12.sp, lineHeight = 16.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun Connected(vm: PmpViewModel, copy: (String, String) -> Unit) {
    var confirmOff by remember { mutableStateOf(false) }
    PmpCard {
        SectionLabel("Status")
        Spacer(Modifier.height(6.dp))
        Text(if (vm.syncLastAt > 0) "Last synced ${vm.whenText(vm.syncLastAt)}" else "Not synced yet",
            fontSize = 15.sp, fontWeight = FontWeight.Bold)
        if (vm.syncBusy) Busy("Syncing…")
        vm.syncError?.let {
            Spacer(Modifier.height(6.dp))
            Text(it, fontSize = 13.sp, lineHeight = 18.sp, color = ColorError)
        }
        Spacer(Modifier.height(10.dp))
        Button(onClick = { vm.syncNow() }, enabled = !vm.syncBusy && vm.mock == null, modifier = Modifier.fillMaxWidth()) {
            Text("Sync now", fontWeight = FontWeight.Bold)
        }
        if (vm.mock != null) Text("A mock exam is in progress: sync waits until it's finished.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(6.dp))
        Text("It also syncs when the app opens or goes to the background, and after each session, practice set, mock and card review.",
            fontSize = 12.sp, lineHeight = 16.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
    PmpCard {
        SectionLabel("Your other devices")
        Spacer(Modifier.height(6.dp))
        Text("On each one, open Cloud sync, choose \"I already have a script\", and enter this URL and sync code. " +
            "Keep both private: together they can read and replace your progress.", fontSize = 13.sp, lineHeight = 18.sp)
        Spacer(Modifier.height(8.dp))
        Copyable("Web app URL", vm.syncUrl, copy)
        Spacer(Modifier.height(8.dp))
        Copyable("Sync code", vm.syncCode, copy)
    }
    PmpCard {
        SectionLabel("Turn it off")
        Spacer(Modifier.height(6.dp))
        Text("Disconnecting stops syncing on this phone. Your progress here and the file in your Drive are both kept. " +
            "To lock out every device, change the code in your script and deploy a new version.", fontSize = 13.sp, lineHeight = 18.sp)
        Spacer(Modifier.height(8.dp))
        OutlinedButton(onClick = { confirmOff = true }, modifier = Modifier.fillMaxWidth()) { Text("Disconnect", color = ColorError) }
    }
    if (confirmOff) {
        AlertDialog(
            onDismissRequest = { confirmOff = false },
            title = { Text("Disconnect cloud sync?") },
            text = { Text("This phone stops syncing. Nothing is deleted here or in your Drive, and you can connect again with the same URL and code.") },
            confirmButton = { TextButton(onClick = { vm.disconnectSync(); confirmOff = false }) { Text("Disconnect", color = ColorError) } },
            dismissButton = { TextButton(onClick = { confirmOff = false }) { Text("Cancel") } },
        )
    }
}

@Composable
private fun Setup(vm: PmpViewModel, context: Context, copy: (String, String) -> Unit) {
    var existing by rememberSaveable { mutableStateOf(false) }
    var url by rememberSaveable { mutableStateOf("") }
    var code by rememberSaveable { mutableStateOf("") }

    Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        FilterChip(selected = !existing, onClick = { existing = false }, label = { Text("Set up a new script") })
        FilterChip(selected = existing, onClick = { existing = true }, label = { Text("I already have a script") })
    }

    if (!existing) {
        val script = vm.syncScript()
        PmpCard {
            SectionLabel("1. Your sync code")
            Spacer(Modifier.height(6.dp))
            Text("A random code that only your script and your devices know. It's already in the script below; you'll need it again on your other devices.",
                fontSize = 13.sp, lineHeight = 18.sp)
            Spacer(Modifier.height(8.dp))
            Copyable("Sync code", vm.syncDraftCode, copy)
        }
        PmpCard {
            SectionLabel("2. Copy the script")
            Spacer(Modifier.height(6.dp))
            if (script.isBlank()) {
                Text("This build doesn't carry the script. It's sync/pmp-sync.gs in the project; put your sync code where it says __SYNC_CODE__.",
                    fontSize = 13.sp, color = ColorError)
            } else {
                Box(
                    Modifier.fillMaxWidth().heightIn(max = 260.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(10.dp))
                        .verticalScroll(rememberScrollState())
                        .padding(10.dp),
                ) {
                    SelectionContainer {
                        Text(script, fontFamily = FontFamily.Monospace, fontSize = 11.sp, lineHeight = 15.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                Spacer(Modifier.height(8.dp))
                Button(onClick = { copy("Script", script) }, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Default.ContentCopy, contentDescription = null)
                    Text("  Copy the script", fontWeight = FontWeight.Bold)
                }
            }
        }
        PmpCard {
            SectionLabel("3. Deploy it in your Google account")
            Spacer(Modifier.height(6.dp))
            Steps(
                "Open script.google.com and sign in with your Google account. Choose New project.",
                "Replace everything in Code.gs with the script you copied, and save it.",
                "Choose Deploy, then New deployment. Next to Select type, choose Web app.",
                "Set Execute as: Me, and Who has access: Anyone. Then choose Deploy.",
                "Authorise it: choose your Google account. On \"Google hasn't verified this app\", choose Advanced, then Go to (your project) (unsafe). It's your own script. Then Allow.",
                "Copy the Web app URL. It looks like https://script.google.com/macros/s/…/exec.",
            )
            TextButton(onClick = { openLink(context, SCRIPT_HOME) }) {
                Icon(Icons.Default.Link, contentDescription = null, modifier = Modifier.size(18.dp))
                Text("  Open script.google.com")
            }
            Text("\"Anyone\" only means the URL answers without a Google sign-in. Every request must carry your sync code, and the script refuses anything without it. " +
                "Whoever has both the URL and the code can read and replace your progress file, so keep them private.",
                fontSize = 12.sp, lineHeight = 16.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        PmpCard {
            SectionLabel("4. Paste the URL here")
            Spacer(Modifier.height(6.dp))
            UrlField(url) { url = it }
            Spacer(Modifier.height(8.dp))
            TestButton(vm) { vm.connectSync(url, vm.syncDraftCode) }
            SetupResult(vm)
        }
    } else {
        PmpCard {
            SectionLabel("Your script")
            Spacer(Modifier.height(6.dp))
            Text("Enter the Web app URL and the sync code from the device where you set it up: its Cloud sync screen shows both. In the web app they're in its Cloud sync settings.",
                fontSize = 13.sp, lineHeight = 18.sp)
            Spacer(Modifier.height(8.dp))
            UrlField(url) { url = it }
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = code, onValueChange = { code = it.trim() }, label = { Text("Sync code") }, singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii),
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(8.dp))
            TestButton(vm) { vm.connectSync(url, code) }
            SetupResult(vm)
        }
    }
}

@Composable
private fun UrlField(value: String, onChange: (String) -> Unit) {
    OutlinedTextField(
        value = value, onValueChange = onChange, label = { Text("Web app URL") }, singleLine = true,
        placeholder = { Text("https://script.google.com/macros/s/…/exec") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun TestButton(vm: PmpViewModel, onClick: () -> Unit) {
    Button(onClick = onClick, enabled = !vm.syncBusy, modifier = Modifier.fillMaxWidth().height(50.dp)) {
        Text("Test connection", fontWeight = FontWeight.Bold)
    }
}

/** What Test connection found: a busy line while it checks, then the reason if it failed. */
@Composable
private fun SetupResult(vm: PmpViewModel) {
    if (vm.syncBusy) Busy("Checking the script…")
    vm.syncSetupError?.let {
        Spacer(Modifier.height(6.dp))
        Text(it, fontSize = 13.sp, lineHeight = 18.sp, color = ColorError)
    }
}

@Composable
private fun Busy(text: String) {
    Row(Modifier.padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        CircularProgressIndicator(Modifier.size(16.dp), strokeWidth = 2.dp)
        Spacer(Modifier.width(8.dp))
        Text(text, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

/** A value shown in full, in monospace, with a copy button beside it. */
@Composable
private fun Copyable(label: String, value: String, copy: (String, String) -> Unit) {
    Text(label, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurfaceVariant)
    Row(verticalAlignment = Alignment.CenterVertically) {
        SelectionContainer(Modifier.weight(1f)) {
            Text(value, fontFamily = FontFamily.Monospace, fontSize = 13.sp, lineHeight = 18.sp)
        }
        TextButton(onClick = { copy(label, value) }) {
            Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(18.dp))
            Text(" Copy")
        }
    }
}

@Composable
private fun Steps(vararg steps: String) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        steps.forEachIndexed { i, s ->
            Row {
                Text("${i + 1}", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary, modifier = Modifier.width(20.dp))
                Text(s, fontSize = 14.sp, lineHeight = 20.sp)
            }
        }
    }
}

internal fun copyText(context: Context, label: String, text: String) {
    val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager ?: return
    cm.setPrimaryClip(ClipData.newPlainText(label, text))
}

internal fun openLink(context: Context, url: String) {
    runCatching { context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)) }
}
