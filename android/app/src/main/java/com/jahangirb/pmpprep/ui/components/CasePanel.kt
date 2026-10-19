package com.jahangirb.pmpprep.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jahangirb.pmpprep.data.CaseExhibit
import com.jahangirb.pmpprep.data.CaseStudy

/**
 * The case passage shown above each of a case's items: a "Case: <title>" header that folds the passage
 * and exhibits away. It opens expanded, and the open/closed state is kept per case, so it stays as the
 * reader left it while they move between that case's items.
 */
@Composable
fun CasePanel(c: CaseStudy, position: String? = null) {
    var open by rememberSaveable(c.id) { mutableStateOf(true) }
    PmpCard {
        Row(
            Modifier.fillMaxWidth().clickable(role = Role.Button) { open = !open }
                .semantics { contentDescription = (if (open) "Hide" else "Show") + " the case: " + c.title },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(Modifier.weight(1f)) {
                Text("Case: ${c.title}", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                if (position != null) Text(position, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Icon(if (open) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown, contentDescription = null)
        }
        if (open) {
            Spacer(Modifier.height(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                c.passage.forEach { p -> Text(p, fontSize = 14.sp, lineHeight = 20.sp) }
                c.exhibits.forEach { x -> ExhibitTable(x) }
            }
        }
    }
    Spacer(Modifier.height(12.dp))
}

/** "Question 3 of 10 in this case", or null if [qid] is not one of the case's items. */
fun casePosition(c: CaseStudy, qid: String): String? {
    val i = c.questions.indexOfFirst { it.id == qid }
    return if (i < 0) null else "Question ${i + 1} of ${c.questions.size} in this case"
}

@Composable
private fun ExhibitTable(x: CaseExhibit) {
    Column(Modifier.fillMaxWidth().padding(top = 4.dp)) {
        Text(listOf(x.label, x.title).filter { it.isNotBlank() }.joinToString(": "), fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
        Spacer(Modifier.height(4.dp))
        val cells = x.cols.size.coerceAtLeast(1)
        // The first column holds labels and gets more room than the figures beside it.
        val first = if (cells > 2) 2f else 1f
        Row(Modifier.fillMaxWidth()) {
            x.cols.forEachIndexed { i, h ->
                Text(h, Modifier.weight(if (i == 0) first else 1f), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
        HorizontalDivider()
        x.rows.forEach { r ->
            Row(Modifier.fillMaxWidth().padding(vertical = 3.dp)) {
                for (i in 0 until cells) {
                    Text(r.getOrElse(i) { "" }, Modifier.weight(if (i == 0) first else 1f), fontSize = 12.sp)
                }
            }
        }
        if (x.note.isNotBlank()) Text(x.note, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
