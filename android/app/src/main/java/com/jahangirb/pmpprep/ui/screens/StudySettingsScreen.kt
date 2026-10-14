package com.jahangirb.pmpprep.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jahangirb.pmpprep.ui.PmpViewModel
import com.jahangirb.pmpprep.ui.components.PmpCard
import com.jahangirb.pmpprep.ui.components.ReadingPrefs
import com.jahangirb.pmpprep.ui.components.SectionLabel
import com.jahangirb.pmpprep.ui.components.StemText

/** Reading comfort, confidence ratings, focus mode and rest days. */
@Composable
fun StudySettingsScreen(vm: PmpViewModel) {
    val rev by vm.revision.collectAsState()
    val r = vm.reading
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Study settings", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)

        PmpCard {
            SectionLabel("Reading comfort")
            Text("For questions and lessons. The real exam's screen can't be changed this way, so the reading drills still matter.",
                fontSize = 13.sp, lineHeight = 18.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(10.dp))
            Text("Text size", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ReadingPrefs.SIZES.forEach { (scale, label) ->
                    FilterChip(selected = r.scale == scale, onClick = { vm.setReading(r.copy(scale = scale)) }, label = { Text(label) })
                }
            }
            Toggle("More space between lines", null, r.roomy) { vm.setReading(r.copy(roomy = it)) }
            Toggle("Reading guide", "A soft band on one line of the question; tap a line to move it.", r.guide) { vm.setReading(r.copy(guide = it)) }
            Spacer(Modifier.height(8.dp))
            SectionLabel("Preview")
            StemText("A team member keeps missing stand-ups and the sprint goal is at risk. What should the project manager do FIRST?", 16, 23)
        }

        PmpCard {
            SectionLabel("While answering")
            Toggle("Ask how sure I am", "Sure, Think so or Guess, before each practice answer is checked. A right guess won't count toward mastery; a sure-but-wrong answer goes to the top of the mistake journal. Never asked in mocks.",
                vm.askConfidence) { vm.setAskConfidence(it) }
            Toggle("Offer focus mode for mocks", "Do Not Disturb and the screen kept awake while a mock is on screen.", vm.focusOffer) { vm.setFocusOffer(it) }
        }

        PmpCard {
            SectionLabel("Streak")
            Toggle("Rest days", "One missed day a week doesn't break your streak, and the evening streak reminder leaves you alone on it. Readiness still counts real study days.",
                rev >= 0 && vm.store.plan.restDays) { vm.setRestDays(it) }
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun Toggle(title: String, detail: String?, on: Boolean, onChange: (Boolean) -> Unit) {
    Row(
        Modifier.fillMaxWidth().toggleable(value = on, role = Role.Switch, onValueChange = onChange).padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f)) {
            Text(title, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            if (detail != null) Text(detail, fontSize = 12.sp, lineHeight = 17.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Spacer(Modifier.width(12.dp))
        Switch(checked = on, onCheckedChange = null)
    }
}
