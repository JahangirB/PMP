package com.jahangirb.pmpprep.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.AssistChip
import com.jahangirb.pmpprep.ui.components.ClayButton as Button
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jahangirb.pmpprep.data.CoachReport
import com.jahangirb.pmpprep.ui.PmpViewModel
import com.jahangirb.pmpprep.ui.components.BulletList
import com.jahangirb.pmpprep.ui.components.MarkdownText
import com.jahangirb.pmpprep.ui.components.PmpCard
import com.jahangirb.pmpprep.ui.components.SectionLabel
import com.jahangirb.pmpprep.ui.theme.ColorError
import com.jahangirb.pmpprep.ui.theme.ColorSuccess
import com.jahangirb.pmpprep.ui.theme.ColorWarning

const val REVIEW_PROMPT = "How am I doing overall? Look at all my figures, tell me honestly where I stand, what is going well, and the three things I should change this week."

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
    LaunchedEffect(chat.size, vm.aiBusy, rev, typing) { if (chat.isNotEmpty()) list.animateScrollToItem(chat.size + 1) }

    Column(Modifier.fillMaxSize().imePadding()) {
        LazyColumn(Modifier.weight(1f).fillMaxWidth().padding(horizontal = 12.dp), state = list, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            item {
                Spacer(Modifier.height(8.dp))
                TextButton(onClick = { vm.forgetKey() }) { Text("Disconnect this key", fontSize = 12.sp) }
                Text("Every message carries a snapshot of your progress (scores, tasks, mocks, habits), so the coach answers about you. It goes only to Google, with your key.",
                    fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 6.dp))
            }
            if (chat.isEmpty()) item {
                PmpCard {
                    Text("Ask about your progress, a PMP topic, or a question that confused you.", fontSize = 14.sp)
                }
            }
            items(chat) { t -> Bubble(t.role == "user", t.text) }
            item {
                if (vm.aiBusy) Row(Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(Modifier.size(18.dp), strokeWidth = 2.dp)
                    Spacer(Modifier.width(8.dp))
                    Text("Thinking…", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                vm.aiError?.let { Text(it, color = ColorError, fontSize = 13.sp, modifier = Modifier.padding(8.dp)) }
                if (chat.isNotEmpty()) TextButton(onClick = { vm.store.clearChat() }) { Text("Clear conversation") }
                Spacer(Modifier.height(4.dp))
            }
        }
        // While typing, the conversation needs the room more than the shortcuts do.
        if (!WindowInsets.isImeVisible) Row(Modifier.horizontalScroll(rememberScrollState()).padding(horizontal = 12.dp), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            listOf(
                "How am I doing?" to REVIEW_PROMPT,
                "What should I improve?" to "What are the three weakest areas in my figures, and exactly what should I do about each this week in this app?",
                "Plan my day" to "I have about an hour today. Using my figures, what should I do, in order, and why?",
                "Explain my weakest task" to "Teach me the ECO task where my accuracy is lowest: what PMI expects, the common traps, and one example question with the reasoning.",
            ).forEach { (label, prompt) ->
                AssistChip(onClick = { vm.ask(prompt) }, enabled = !vm.aiBusy, label = { Text(label) })
            }
        }
        var input by remember { mutableStateOf("") }
        LaunchedEffect(vm.coachDraft) { if (vm.coachDraft.isNotEmpty()) { input = vm.coachDraft; vm.coachDraft = "" } }
        Row(Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.Bottom) {
            OutlinedTextField(value = input, onValueChange = { input = it }, placeholder = { Text("Ask your coach…") },
                maxLines = 6, modifier = Modifier.weight(1f))
            IconButton(onClick = { vm.ask(input); input = "" }, enabled = input.isNotBlank() && !vm.aiBusy) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send")
            }
        }
    }
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
