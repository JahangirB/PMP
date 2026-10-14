package com.jahangirb.pmpprep.ui.screens

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.jahangirb.pmpprep.data.HourRule
import com.jahangirb.pmpprep.data.ReminderBrain
import com.jahangirb.pmpprep.data.ReminderKind
import com.jahangirb.pmpprep.data.ReminderState
import com.jahangirb.pmpprep.data.StudyWindow
import com.jahangirb.pmpprep.reminders.Reminders
import com.jahangirb.pmpprep.ui.components.PmpCard
import com.jahangirb.pmpprep.ui.components.SectionLabel
import com.jahangirb.pmpprep.ui.components.Selector
import com.jahangirb.pmpprep.ui.components.ClayButton as Button
import com.jahangirb.pmpprep.ui.components.ClayOutlinedButton as OutlinedButton
import java.text.DateFormat
import java.util.Date

/** The reminder state, read from its file, with a way to change it and re-plan today. */
private class ReminderHolder(private val ctx: android.content.Context) {
    var st by mutableStateOf(Reminders.load(ctx))
        private set
    var notifications by mutableStateOf(Reminders.notificationsAllowed(ctx))
    var usageAccess by mutableStateOf(Reminders.usageAccessGranted(ctx))

    fun change(block: (ReminderState) -> Unit) {
        Reminders.update(ctx, block)
        Reminders.schedule(ctx, replan = true)
        st = Reminders.load(ctx)
    }

    fun refresh() {
        notifications = Reminders.notificationsAllowed(ctx)
        usageAccess = Reminders.usageAccessGranted(ctx)
        if (usageAccess && st.settings.usePhoneUsage && st.phoneUsage == null) change { it.phoneUsageAt = 0L }
        st = Reminders.load(ctx)
    }
}

@Composable
private fun rememberReminders(): ReminderHolder {
    val ctx = LocalContext.current
    val h = remember { ReminderHolder(ctx) }
    // Coming back from Settings (notifications or Usage access): look again.
    @Suppress("DEPRECATION")
    val owner = androidx.compose.ui.platform.LocalLifecycleOwner.current
    DisposableEffect(owner) {
        val o = LifecycleEventObserver { _, e -> if (e == Lifecycle.Event.ON_RESUME) h.refresh() }
        owner.lifecycle.addObserver(o)
        onDispose { owner.lifecycle.removeObserver(o) }
    }
    return h
}

private fun hourLabel(h: Int) = "%02d:00".format(h)

/** Asks for notifications (Android 13+), then sets reminders going. */
@Composable
private fun NotificationSwitch(h: ReminderHolder) {
    val ask = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { h.refresh() }
    if (!h.notifications) {
        Text("Notifications are off for this app, so reminders can't be shown.", fontSize = 13.sp, color = MaterialTheme.colorScheme.error)
        Button(onClick = {
            if (Build.VERSION.SDK_INT >= 33) ask.launch(Manifest.permission.POST_NOTIFICATIONS)
        }, enabled = Build.VERSION.SDK_INT >= 33, modifier = Modifier.fillMaxWidth()) { Text("Allow reminders") }
        if (Build.VERSION.SDK_INT < 33) Text("Turn them on in Android Settings → Apps → PMP Prep → Notifications.", fontSize = 12.sp)
    }
}

/** Your idea Y1, with the most the system allows: Usage access, which only you can switch on in Settings. */
@Composable
private fun PhoneUsageSwitch(h: ReminderHolder) {
    val ctx = LocalContext.current
    val on = h.st.settings.usePhoneUsage
    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f)) {
            Text("Learn from when I use my phone", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text("Reads only the times your screen was on or unlocked over the last two weeks, never which apps. Stays on this phone.",
                fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Switch(checked = on, onCheckedChange = { want ->
            h.change { it.settings = it.settings.copy(usePhoneUsage = want); it.phoneUsageAt = 0L }
            if (want && !h.usageAccess) ctx.startActivity(Reminders.usageAccessSettings())
        })
    }
    if (on && !h.usageAccess) {
        Text("Usage access is off. Find PMP Prep in the list that opens and switch it on.", fontSize = 12.sp, color = MaterialTheme.colorScheme.error)
        TextButton(onClick = { ctx.startActivity(Reminders.usageAccessSettings()) }) { Text("Open Usage access") }
    }
}

/** S2: "When do you usually study?", plus the waking hours that bound every reminder. */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun WhenQuestion(h: ReminderHolder) {
    val s = h.st.settings
    Text("When do you usually study?", fontWeight = FontWeight.Bold, fontSize = 14.sp)
    Text("Pick any that fit. It's only a starting guess; the app learns your real times.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        StudyWindow.entries.forEach { w ->
            FilterChip(selected = w in s.windows, onClick = {
                h.change { it.settings = it.settings.copy(windows = if (w in it.settings.windows) it.settings.windows - w else it.settings.windows + w, asked = true) }
            }, label = { Text(w.label) })
        }
    }
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        Selector("I wake up", (4..11).map { it to hourLabel(it) }, s.wake, { v -> h.change { it.settings = it.settings.copy(wake = v) } }, Modifier.weight(1f))
        Selector("I go to sleep", (20..23).map { it to hourLabel(it) } + listOf(24 to "Midnight"), s.sleep, { v -> h.change { it.settings = it.settings.copy(sleep = v) } }, Modifier.weight(1f))
    }
    Text("No reminders before ${hourLabel(s.wake + 1)} or after ${hourLabel(s.sleep - 1)}.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
}

/** Onboarding step: set reminders up in one go. */
@Composable
fun ReminderSetupStep(onNext: () -> Unit) {
    val h = rememberReminders()
    Text("Reminders that learn your rhythm", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
    Text("A few reminders a day at first. The app watches which ones get you studying, then keeps to the hours that work and drops the rest.",
        fontSize = 15.sp, lineHeight = 22.sp)
    WhenQuestion(h)
    PhoneUsageSwitch(h)
    NotificationSwitch(h)
    Button(onClick = { h.change { it.settings = it.settings.copy(asked = true) }; onNext() }, modifier = Modifier.fillMaxWidth().height(52.dp)) {
        Text("Next", fontWeight = FontWeight.Bold)
    }
}

/** S6: the full controls, in Plan and schedule. */
@Composable
fun RemindersCard() {
    val h = rememberReminders()
    val st = h.st
    val s = st.settings
    val now = System.currentTimeMillis()
    PmpCard {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                SectionLabel("Smart reminders")
                Text(if (s.enabled) "On" else "Off", fontWeight = FontWeight.Bold)
            }
            Switch(checked = s.enabled, onCheckedChange = { on -> h.change { it.settings = it.settings.copy(enabled = on, asked = true) } })
        }
        if (s.enabled) {
            Spacer(Modifier.height(8.dp))
            NotificationSwitch(h)
            val paused = now < s.pausedUntil
            if (paused) {
                Text("Paused until ${DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(Date(s.pausedUntil))}.", fontSize = 13.sp)
                TextButton(onClick = { h.change { it.settings = it.settings.copy(pausedUntil = 0L) } }) { Text("Resume now") }
            }

            Spacer(Modifier.height(10.dp))
            BestTimes(h, now)

            Spacer(Modifier.height(12.dp))
            WhenQuestion(h)
            Spacer(Modifier.height(12.dp))
            PhoneUsageSwitch(h)

            if (!paused) {
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = { h.change { it.settings = it.settings.copy(pausedUntil = now + 24 * ReminderBrain.HOUR) } }, modifier = Modifier.weight(1f)) { Text("Pause a day") }
                    OutlinedButton(onClick = { h.change { it.settings = it.settings.copy(pausedUntil = now + 3 * 24 * ReminderBrain.HOUR) } }, modifier = Modifier.weight(1f)) { Text("Pause 3 days") }
                }
            }
        }
    }
}

/** Each waking hour's learned chance; tap an hour to pin it (always remind) or block it (never). */
@Composable
private fun BestTimes(h: ReminderHolder, now: Long) {
    val st = h.st
    val hours = ReminderBrain.wakingHours(st.settings)
    val scores = ReminderBrain.scores(st, now)
    val resolved = st.logs.count { it.kind != ReminderKind.SUMMARY && ReminderBrain.outcome(it, now) != null }
    val best = scores.filter { it.hour in hours && it.rule != HourRule.BLOCKED && it.tried > 0 }.maxByOrNull { it.mean }

    Text("Your best study times", fontWeight = FontWeight.Bold, fontSize = 14.sp)
    Text(
        if (resolved < ReminderBrain.LEARNING_REMINDERS) "Learning: $resolved of ${ReminderBrain.LEARNING_REMINDERS} reminders so far. Up to 3 a day while it learns."
        else best?.let { "Best so far: ${hourLabel(it.hour)}, opened and studied ${(it.mean * 100).toInt()}% of the time. Up to 2 a day." } ?: "Up to 2 a day.",
        fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
    Spacer(Modifier.height(8.dp))
    val primary = MaterialTheme.colorScheme.primary
    val track = MaterialTheme.colorScheme.secondaryContainer
    Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.Bottom) {
        hours.forEach { hr ->
            val sc = scores[hr]
            val label = when (sc.rule) { HourRule.PINNED -> "pinned"; HourRule.BLOCKED -> "blocked"; HourRule.AUTO -> "automatic" }
            Column(
                Modifier.width(30.dp)
                    .clickable {
                        val next = when (sc.rule) { HourRule.AUTO -> HourRule.PINNED; HourRule.PINNED -> HourRule.BLOCKED; HourRule.BLOCKED -> HourRule.AUTO }
                        h.change { it.settings = it.settings.copy(rules = if (next == HourRule.AUTO) it.settings.rules - hr else it.settings.rules + (hr to next)) }
                    }
                    .semantics { contentDescription = "${hourLabel(hr)}: ${(sc.mean * 100).toInt()}% chance, tried ${sc.tried} times, $label. Tap to change." },
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(Modifier.width(18.dp).height(72.dp).background(track, RoundedCornerShape(9.dp)), contentAlignment = Alignment.BottomCenter) {
                    if (sc.rule != HourRule.BLOCKED) Box(
                        Modifier.width(18.dp).fillMaxHeight(sc.mean.toFloat().coerceIn(0.08f, 1f))
                            .background(if (sc.rule == HourRule.PINNED) primary else primary.copy(alpha = 0.35f + 0.65f * sc.mean.toFloat()), RoundedCornerShape(9.dp))
                    )
                }
                Text(if (sc.rule == HourRule.BLOCKED) "✕" else if (sc.rule == HourRule.PINNED) "★" else " ", fontSize = 11.sp, color = primary)
                Text("$hr", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
    Text("Tap an hour: ★ always remind, ✕ never, then back to automatic.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    val today = st.plan.filter { it.at > now && st.planDay == com.jahangirb.pmpprep.data.PlanEngine.startOfDay(now) }
    if (today.isNotEmpty()) {
        Spacer(Modifier.height(6.dp))
        Text("Still to come today: " + today.joinToString(", ") {
            DateFormat.getTimeInstance(DateFormat.SHORT).format(Date(it.at)) + when (it.kind) { ReminderKind.STREAK -> " (streak)"; ReminderKind.SUMMARY -> " (weekly summary)"; else -> "" }
        }, fontSize = 13.sp)
    }
}
