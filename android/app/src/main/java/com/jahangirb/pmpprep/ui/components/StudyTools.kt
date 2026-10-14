package com.jahangirb.pmpprep.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jahangirb.pmpprep.data.Question
import com.jahangirb.pmpprep.ui.Dest
import com.jahangirb.pmpprep.ui.PmpViewModel

/** Turn a missed question into a card for the "My cards" deck. Both sides are drafted and editable. */
@Composable
fun MakeCardDialog(vm: PmpViewModel, q: Question, onDone: () -> Unit) {
    val draft = remember(q.id) { vm.cardDraft(q) }
    var front by remember(q.id) { mutableStateOf(draft.first) }
    var back by remember(q.id) { mutableStateOf(draft.second) }
    AlertDialog(
        onDismissRequest = onDone,
        title = { Text("Make a card") },
        text = {
            Column(Modifier.heightIn(max = 460.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Put it in your own words: writing the card is where the learning happens.", fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                OutlinedTextField(front, { front = it }, label = { Text("Front: the situation") }, minLines = 2, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(back, { back = it }, label = { Text("Back: the right move and why") }, minLines = 4, modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = {
            TextButton(onClick = { vm.makeCard(q, front, back); onDone() }, enabled = front.isNotBlank() && back.isNotBlank()) { Text("Save to My cards") }
        },
        dismissButton = { TextButton(onClick = onDone) { Text("Cancel") } },
    )
}

/** Explain it in your own words; Gemini says what you got right and what you missed. */
@Composable
fun ExplainBackDialog(vm: PmpViewModel, topic: String, prompt: String, onDone: () -> Unit) {
    var text by remember(topic) { mutableStateOf("") }
    DisposableEffect(topic) { vm.clearExplain(); onDispose { vm.clearExplain() } }
    AlertDialog(
        onDismissRequest = onDone,
        title = { Text("Explain it in your words") },
        text = {
            Column(Modifier.heightIn(max = 480.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                if (vm.geminiKey.isEmpty()) {
                    Text("This uses the Gemini coach. Add your Gemini key in Coach first; your text is sent to Google to get feedback.", fontSize = 14.sp)
                } else {
                    Text(prompt, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    OutlinedTextField(text, { text = it }, minLines = 4, enabled = !vm.explainBusy, modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("In my words…") })
                    if (vm.explainBusy) CircularProgressIndicator()
                    vm.explainError?.let { Text(it, color = MaterialTheme.colorScheme.error, fontSize = 13.sp) }
                    vm.explainReply?.let {
                        PmpCard {
                            SectionLabel("Feedback")
                            Text(it, fontSize = 14.sp, lineHeight = 20.sp)
                            Text("AI feedback can be wrong: treat it as a tutor's view, not a grade.", fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        },
        confirmButton = {
            if (vm.geminiKey.isEmpty()) TextButton(onClick = { onDone(); vm.coachTab = 1; vm.navigate(Dest.COACH) }) { Text("Open Coach") }
            else if (vm.explainReply == null) TextButton(onClick = { vm.explainBack(topic, text) }, enabled = text.isNotBlank() && !vm.explainBusy) {
                Text("Get feedback", fontWeight = FontWeight.Bold)
            } else TextButton(onClick = onDone) { Text("Done") }
        },
        dismissButton = { TextButton(onClick = onDone) { Text(if (vm.explainReply == null) "Cancel" else "Close") } },
    )
}

/** What the question was about, for the explain-back prompt. */
fun questionTopic(q: Question): String = buildString {
    append(q.stem)
    q.options.filter { it.correct }.takeIf { it.isNotEmpty() }?.let { append("\n\nThe right answer: ").append(it.joinToString("; ") { o -> o.text }) }
    if (q.explanation.isNotBlank()) append("\n\nThe app's explanation: ").append(q.explanation)
}
