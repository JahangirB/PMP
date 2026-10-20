package com.jahangirb.pmpprep.ui.screens

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jahangirb.pmpprep.data.Support
import com.jahangirb.pmpprep.ui.PmpViewModel
import com.jahangirb.pmpprep.ui.components.BulletList
import com.jahangirb.pmpprep.ui.components.ClayButton as Button
import com.jahangirb.pmpprep.ui.components.ClayOutlinedButton as OutlinedButton
import com.jahangirb.pmpprep.ui.components.PmpCard
import com.jahangirb.pmpprep.ui.components.SectionLabel
import com.jahangirb.pmpprep.ui.theme.SupportInk
import com.jahangirb.pmpprep.ui.theme.SupportTint

/** The menu's Support button opens this page (docs/progress-format.md §4): why, what it pays for, and ways to help. */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SupportScreen(vm: PmpViewModel) {
    val context = LocalContext.current
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text(Support.PAGE_TITLE, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
        Text(Support.PAGE_BODY, fontSize = 15.sp, lineHeight = 22.sp)
        PmpCard {
            SectionLabel("What your support pays for")
            Spacer(Modifier.height(6.dp))
            BulletList(Support.PAYS_FOR)
        }
        PmpCard {
            SectionLabel("Ways to help")
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = { openLink(context, Support.URL); vm.supportPressed() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = SupportTint, contentColor = SupportInk),
            ) {
                Icon(Icons.Default.Favorite, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(Support.KOFI_LABEL, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(6.dp))
            Text(Support.KOFI_NOTE, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(10.dp))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = {
                    val send = Intent(Intent.ACTION_SEND).apply { type = "text/plain"; putExtra(Intent.EXTRA_TEXT, Support.SHARE_TEXT) }
                    context.startActivity(Intent.createChooser(send, "Share the app"))
                }) { Text("Share the app") }
                OutlinedButton(onClick = { openLink(context, Support.REPORT_URL) }) { Text("Report a wrong question") }
                OutlinedButton(onClick = { openLink(context, Support.REPO_URL) }) { Text("Star it on GitHub") }
            }
        }
        Text(Support.THANKS, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(16.dp))
    }
}
