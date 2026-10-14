package com.jahangirb.pmpprep.widget

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.lifecycle.lifecycleScope
import com.jahangirb.pmpprep.ui.components.ClayButton
import com.jahangirb.pmpprep.ui.theme.ClayBackdrop
import com.jahangirb.pmpprep.ui.theme.DarkColors
import com.jahangirb.pmpprep.ui.theme.LightColors
import com.jahangirb.pmpprep.ui.theme.PmpTheme
import kotlinx.coroutines.launch

/** Asks "app colours or system colours?" when the widget is placed, and again from its settings button (Android 12+). */
class WidgetConfigActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val id = intent?.extras?.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
            ?: AppWidgetManager.INVALID_APPWIDGET_ID
        // Backing out without choosing leaves the widget unplaced.
        setResult(Activity.RESULT_CANCELED, Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id))
        if (id == AppWidgetManager.INVALID_APPWIDGET_ID) { finish(); return }
        val current = getSharedPreferences("widget", MODE_PRIVATE).getBoolean(PmpWidget.systemKey(id), false)

        setContent {
            PmpTheme {
                var system by remember { mutableStateOf(current) }
                Column(
                    Modifier.fillMaxSize().background(Brush.verticalGradient(ClayBackdrop)).safeDrawingPadding().padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    Text("Widget colours", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
                    Text("Light and dark follow your phone in both.", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    val dark = isSystemInDarkTheme()
                    Choice("App colours", "The app's own sky blue and lilac.", !system, if (dark) DarkColors else LightColors) { system = false }
                    if (Build.VERSION.SDK_INT >= 31) {
                        val ctx = LocalContext.current
                        val dynamic = if (dark) dynamicDarkColorScheme(ctx) else dynamicLightColorScheme(ctx)
                        Choice("System colours", "Taken from your wallpaper, like your other widgets.", system, dynamic) { system = true }
                    }
                    Spacer(Modifier.weight(1f))
                    ClayButton(onClick = {
                        PmpWidget.useSystemColors(this@WidgetConfigActivity, id, system)
                        lifecycleScope.launch {
                            runCatching { PmpWidget().update(this@WidgetConfigActivity, GlanceAppWidgetManager(this@WidgetConfigActivity).getGlanceIdBy(id)) }
                            setResult(Activity.RESULT_OK, Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id))
                            finish()
                        }
                    }, modifier = Modifier.fillMaxWidth().height(52.dp)) { Text("Save", fontWeight = FontWeight.Bold, fontSize = 16.sp) }
                }
            }
        }
    }
}

@Composable
private fun Choice(title: String, hint: String, selected: Boolean, scheme: ColorScheme, onPick: () -> Unit) {
    val shape = RoundedCornerShape(28.dp)
    Column(
        Modifier.fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, shape)
            .border(2.dp, if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline, shape)
            .clickable(role = Role.RadioButton, onClick = onPick)
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(selected = selected, onClick = null)
            Spacer(Modifier.width(8.dp))
            Column {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(hint, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Preview(scheme)
    }
}

/** A small drawing of the widget in [scheme]'s colours. */
@Composable
private fun Preview(s: ColorScheme) {
    Row(
        Modifier.fillMaxWidth().height(60.dp).background(s.surface, RoundedCornerShape(24.dp)).padding(5.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        listOf(Triple("42", "days left", null), Triple("58%", "today", 0.58f), Triple("68", "ready", null), Triple("53%", "syllabus", 0.53f)).forEach { (v, l, p) ->
            Box(Modifier.weight(1f).fillMaxSize().background(s.secondaryContainer, RoundedCornerShape(19.dp)), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(v, color = s.onSurface, fontSize = 15.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                    if (p != null) LinearProgressIndicator(progress = { p }, color = s.primary, trackColor = s.primaryContainer,
                        modifier = Modifier.width(36.dp).height(3.dp))
                    Text(l, color = s.onSurfaceVariant, fontSize = 10.sp)
                }
            }
        }
    }
}
