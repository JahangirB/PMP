package com.jahangirb.pmpprep.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.AlertDialog
import com.jahangirb.pmpprep.ui.components.ClayButton as Button
import com.jahangirb.pmpprep.ui.components.ClayOutlinedButton as OutlinedButton
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jahangirb.pmpprep.data.ChatCommand
import com.jahangirb.pmpprep.data.ChatCommands
import com.jahangirb.pmpprep.data.CoachReport
import com.jahangirb.pmpprep.data.Typewriter
import kotlinx.coroutines.delay
import com.jahangirb.pmpprep.ui.PmpViewModel
import com.jahangirb.pmpprep.ui.components.BulletList
import com.jahangirb.pmpprep.ui.components.MarkdownText
import com.jahangirb.pmpprep.ui.components.PmpCard
import com.jahangirb.pmpprep.ui.components.SectionLabel
import com.jahangirb.pmpprep.ui.theme.ColorError
import com.jahangirb.pmpprep.ui.theme.ColorSuccess
import com.jahangirb.pmpprep.ui.theme.ColorWarning

const val REVIEW_PROMPT = ChatCommands.REVIEW

@Composable
fun CoachScreen(vm: PmpViewModel) {
    Column(Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = vm.coachTab) {
            Tab(selected = vm.coachTab == 0, onClick = { vm.coachTab = 0 }, text = { Text("Today") })
            Tab(selected = vm.coachTab == 1, onClick = { vm.coachTab = 1 }, text = { Text("Ask Gemini") })
            Tab(selected = vm.coachTab == 2, onClick = { vm.coachTab = 2 }, text = { Text("Notes") })
        }
        when (vm.coachTab) {
            0 -> CoachToday(vm)
            1 -> CoachChat(vm)
            else -> NotesScreen(vm)
        }
    }
}

@Composable
private fun CoachToday(vm: PmpViewModel) {
    val rev by vm.revision.collectAsState()
    val r: CoachReport = remember(rev, vm.contentVersion) { vm.coach().report() }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        PmpCard(container = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f), border = MaterialTheme.colorScheme.primary) {
            Text(r.headline, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
            Spacer(Modifier.height(4.dp))
            Text(r.verdict, fontSize = 14.sp, lineHeight = 20.sp)
            Spacer(Modifier.height(10.dp))
            Button(onClick = {
                vm.coachTab = 1
                if (vm.geminiKey.isNotEmpty()) vm.ask(REVIEW_PROMPT) else vm.coachDraft = REVIEW_PROMPT
            }, modifier = Modifier.fillMaxWidth()) { Text("Ask Gemini for a full review", fontWeight = FontWeight.Bold) }
        }
        if (r.strengths.isNotEmpty()) PmpCard { SectionLabel("Going well"); Spacer(Modifier.height(6.dp)); BulletList(r.strengths, color = ColorSuccess, bullet = "✓") }
        if (r.improve.isNotEmpty()) PmpCard { SectionLabel("To improve"); Spacer(Modifier.height(6.dp)); BulletList(r.improve, bullet = "→") }

        if (r.steps.isNotEmpty()) {
            SectionLabel("Today's session")
            r.steps.forEach { s ->
                PmpCard(onClick = { vm.coachGo(s.action, s.arg) }) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(if (s.done) Icons.Filled.CheckCircle else Icons.Outlined.Circle, contentDescription = null,
                            tint = if (s.done) ColorSuccess else MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.width(10.dp))
                        Column(Modifier.weight(1f)) {
                            Text(s.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(s.why, fontSize = 12.sp, lineHeight = 16.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        TextButton(onClick = { vm.coachGo(s.action, s.arg) }) { Text(if (s.done) "Again" else "Go") }
                    }
                }
            }
        }

        if (r.messages.isNotEmpty()) {
            SectionLabel("From your coach")
            r.messages.forEach { m ->
                PmpCard(container = ColorWarning.copy(alpha = 0.07f), border = ColorWarning.copy(alpha = 0.5f)) {
                    Text(m.text, fontSize = 14.sp, lineHeight = 20.sp)
                    Row {
                        Spacer(Modifier.weight(1f))
                        TextButton(onClick = { vm.snoozeCoach(m.id, m.cooldownDays) }) { Text("Not now") }
                        m.action?.let { a -> TextButton(onClick = { vm.coachGo(a, m.arg) }) { Text(a.label) } }
                    }
                }
            }
        }
        Spacer(Modifier.height(16.dp))
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CoachChat(vm: PmpViewModel) {
    if (vm.geminiKey.isEmpty()) { KeySetup(vm); return }
    val rev by vm.revision.collectAsState()
    LaunchedEffect(vm.geminiKey) { vm.checkModels() }
    val list = rememberLazyListState()
    val chat = vm.store.chat.toList()
    val typing = WindowInsets.isImeVisible
    /* The message just sent and what follows it share the last item, at least a screen tall, so that
       message can sit at the top while the reply is written out below it; the person scrolls on. */
    val split = vm.chatAnchor?.let { a -> chat.indexOfFirst { it.at == a && it.role == "user" }.takeIf { it >= 0 } } ?: chat.size
    val tailIndex = 1 + (if (chat.isEmpty()) 1 else 0) + split
    LaunchedEffect(Unit) { list.scrollToItem(tailIndex) }
    LaunchedEffect(vm.chatAnchor) { if (vm.chatAnchor != null) list.animateScrollToItem(tailIndex) }
    LaunchedEffect(typing) { if (typing && vm.typingAt == null) list.animateScrollToItem(tailIndex) }

    Column(Modifier.fillMaxSize().imePadding()) {
        BoxWithConstraints(Modifier.weight(1f).fillMaxWidth()) {
        val viewport = maxHeight
        LazyColumn(Modifier.fillMaxSize().padding(horizontal = 12.dp), state = list, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            item {
                // One short line on top, so the conversation keeps the room.
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text("Gemini, with your key", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1f))
                    TextButton(onClick = { vm.forgetKey() }, contentPadding = PaddingValues(horizontal = 8.dp)) { Text("Disconnect key", fontSize = 12.sp) }
                }
                if (!vm.chatNoteClosed) Surface(shape = RoundedCornerShape(10.dp), border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))) {
                    Row(Modifier.padding(start = 10.dp), verticalAlignment = Alignment.Top) {
                        Text("Every message carries a snapshot of your progress (scores, tasks, mocks, habits), so the coach answers about you. It goes only to Google, with your key.",
                            fontSize = 11.sp, lineHeight = 15.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1f).padding(vertical = 8.dp))
                        IconButton(onClick = { vm.closeChatNote() }) { Icon(Icons.Filled.Close, contentDescription = "Close this note") }
                    }
                }
            }
            if (chat.isEmpty()) item {
                PmpCard {
                    Text("Ask about your progress, a PMP topic, or a question that confused you.", fontSize = 14.sp)
                }
            }
            items(chat.take(split)) { t -> Bubble(t.role == "user", t.text) }
            item {
                Column(if (split < chat.size) Modifier.heightIn(min = viewport) else Modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                chat.drop(split).forEach { t ->
                    if (t.role != "user" && t.at == vm.typingAt) TypedReply(t.text) { vm.typingAt = null }
                    else Bubble(t.role == "user", t.text)
                }
                if (vm.aiBusy) Row(Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(Modifier.size(18.dp), strokeWidth = 2.dp)
                    Spacer(Modifier.width(8.dp))
                    Text("Thinking…", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                // The last message went unanswered: why, and one button to send it again.
                if (!vm.aiBusy && chat.lastOrNull()?.let { it.role != "model" } == true) {
                    Column(Modifier.padding(horizontal = 8.dp)) {
                        Text(vm.aiError ?: "No answer came back for this message.", color = ColorError, fontSize = 13.sp)
                        OutlinedButton(onClick = { vm.retryAsk() }) {
                            Icon(Icons.Filled.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("Try again")
                        }
                    }
                } else vm.aiError?.let { Text(it, color = ColorError, fontSize = 13.sp, modifier = Modifier.padding(8.dp)) }
                if (chat.isNotEmpty() && vm.typingAt == null) TextButton(onClick = { vm.chatAnchor = null; vm.store.clearChat() }) { Text("Clear conversation") }
                Spacer(Modifier.height(4.dp))
                }
            }
        }
        }
        var input by remember { mutableStateOf("") }
        var editing by remember { mutableStateOf<ChatCommand?>(null) }
        var adding by remember { mutableStateOf(false) }
        LaunchedEffect(vm.coachDraft) { if (vm.coachDraft.isNotEmpty()) { input = vm.coachDraft; vm.coachDraft = "" } }
        val own = remember(rev) { vm.store.chatCommands }
        // "/" opens the commands: the four built in, then the person's own.
        ChatCommands.matching(input, own)?.let { found ->
            CommandMenu(found, onPick = { input = it.text }, onEdit = { editing = it }, onAdd = { input = ""; adding = true })
        }
        if (adding || editing != null) CommandDialog(vm, editing) { adding = false; editing = null }
        Row(Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.Bottom) {
            OutlinedTextField(value = input, onValueChange = { input = it }, placeholder = { Text("Ask your coach, or type / for commands") },
                maxLines = 6, modifier = Modifier.weight(1f))
            IconButton(onClick = { vm.ask(input); input = "" }, enabled = input.isNotBlank() && !vm.aiBusy) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send")
            }
        }
    }
}

/** A reply written out word by word, though it arrived whole ([Typewriter]). */
@Composable
private fun TypedReply(text: String, done: () -> Unit) {
    var n by remember(text) { mutableStateOf(0) }
    LaunchedEffect(text) {
        while (n < text.length) { delay(Typewriter.FRAME_MS); n = Typewriter.next(text, n) }
        done()
    }
    // Leaving the screen part-way shows the whole reply next time.
    DisposableEffect(text) { onDispose { if (n < text.length) done() } }
    Bubble(false, if (n < text.length) Typewriter.shown(text, n) + " ▍" else text)
}

@Composable
private fun CommandMenu(found: List<ChatCommand>, onPick: (ChatCommand) -> Unit, onEdit: (ChatCommand) -> Unit, onAdd: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(12.dp), tonalElevation = 3.dp, shadowElevation = 4.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)),
        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
    ) {
        Column(Modifier.heightIn(max = 260.dp).verticalScroll(rememberScrollState()).padding(vertical = 4.dp)) {
            found.forEachIndexed { i, c ->
                if (i == 0 || c.own != found[i - 1].own) {
                    Text(if (c.own) "YOURS" else "COMMANDS", fontSize = 11.sp, letterSpacing = 0.6.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(start = 12.dp, top = 6.dp))
                }
                Row(Modifier.fillMaxWidth().clickable { onPick(c) }.padding(start = 12.dp).heightIn(min = 40.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("/" + c.name, fontFamily = FontFamily.Monospace, fontSize = 13.sp, color = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.width(10.dp))
                    Text(c.label, fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1f))
                    if (c.own) IconButton(onClick = { onEdit(c) }) { Icon(Icons.Filled.Edit, contentDescription = "Edit /${c.name}") }
                    else Spacer(Modifier.width(12.dp))
                }
            }
            Text("+ New command", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth().clickable(onClick = onAdd).padding(horizontal = 12.dp, vertical = 10.dp))
        }
    }
}

/** Makes one of the person's own commands, or changes or deletes [cmd]. */
@Composable
private fun CommandDialog(vm: PmpViewModel, cmd: ChatCommand?, done: () -> Unit) {
    var name by remember { mutableStateOf(cmd?.name ?: "") }
    var text by remember { mutableStateOf(cmd?.text ?: "") }
    var err by remember { mutableStateOf<String?>(null) }
    AlertDialog(
        onDismissRequest = done,
        title = { Text(if (cmd == null) "New command" else "Edit /${cmd.name}") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                err?.let { Text(it, color = ColorError, fontSize = 13.sp) }
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name, typed after /") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = text, onValueChange = { text = it }, label = { Text("Message it sends") }, minLines = 3, maxLines = 6, modifier = Modifier.fillMaxWidth())
                Text("Your commands travel with your progress, so they are on your other devices after a sync.", fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        },
        confirmButton = { TextButton(onClick = { err = vm.saveChatCommand(cmd?.name, name, text); if (err == null) done() }) { Text("Save") } },
        dismissButton = {
            Row {
                if (cmd != null) TextButton(onClick = { vm.deleteChatCommand(cmd.name); done() }) { Text("Delete", color = ColorError) }
                TextButton(onClick = done) { Text("Cancel") }
            }
        },
    )
}

@Composable
private fun Bubble(mine: Boolean, text: String) {
    Box(Modifier.fillMaxWidth(), contentAlignment = if (mine) Alignment.CenterEnd else Alignment.CenterStart) {
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = if (mine) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
            border = if (mine) null else BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)),
            modifier = Modifier.widthIn(max = 340.dp),
        ) {
            Box(Modifier.padding(12.dp)) {
                if (mine) Text(text, color = MaterialTheme.colorScheme.onPrimary, fontSize = 15.sp, lineHeight = 21.sp)
                else MarkdownText(text)
            }
        }
    }
}

@Composable
private fun KeySetup(vm: PmpViewModel) {
    var key by remember { mutableStateOf("") }
    val uri = LocalUriHandler.current
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).imePadding().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Connect Gemini", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
        Text("The coach's chat runs on Google's newest Gemini Flash (3.5 or later) with your own API key; the app keeps up with new models by itself. It reads a snapshot of your progress with each message, so it can tell you how you're doing and what to improve, and it answers your PMP questions.",
            fontSize = 14.sp, lineHeight = 20.sp)
        PmpCard {
            SectionLabel("Get a free key")
            BulletList(listOf("Open Google AI Studio and sign in.", "Create an API key.", "Copy it and paste it below."))
            TextButton(onClick = { uri.openUri("https://aistudio.google.com/apikey") }) { Text("Open aistudio.google.com/apikey") }
        }
        OutlinedTextField(
            value = key, onValueChange = { key = it }, label = { Text("Gemini API key") }, singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
        )
        Button(onClick = { vm.useKey(key) }, enabled = key.isNotBlank() && !vm.aiBusy, modifier = Modifier.fillMaxWidth()) {
            Text(if (vm.aiBusy) "Checking the key…" else "Use this key", fontWeight = FontWeight.Bold)
        }
        vm.aiError?.let { Text(it, color = ColorError, fontSize = 13.sp) }
        Text("The key stays on this phone in the app's private storage. It is never in a progress backup, and it is sent only to Google, in a request header.",
            fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text("The Today tab works without a key.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.clickable { vm.coachTab = 0 })
    }
}
