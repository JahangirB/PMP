package com.jahangirb.pmpprep

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jahangirb.pmpprep.ui.Dest
import com.jahangirb.pmpprep.ui.PmpViewModel
import com.jahangirb.pmpprep.ui.screens.AnalyticsScreen
import com.jahangirb.pmpprep.ui.screens.CoachScreen
import com.jahangirb.pmpprep.ui.screens.NotesScreen
import com.jahangirb.pmpprep.ui.screens.MenuScreen
import com.jahangirb.pmpprep.ui.screens.PathScreen
import com.jahangirb.pmpprep.ui.screens.SessionScreen
import com.jahangirb.pmpprep.ui.screens.DataScreen
import com.jahangirb.pmpprep.ui.screens.FlashcardsScreen
import com.jahangirb.pmpprep.ui.screens.FormulaLabScreen
import com.jahangirb.pmpprep.ui.screens.LearnScreen
import com.jahangirb.pmpprep.ui.screens.MistakeJournalScreen
import com.jahangirb.pmpprep.ui.screens.MockExamScreen
import com.jahangirb.pmpprep.ui.screens.PlannerScreen
import com.jahangirb.pmpprep.ui.screens.PracticeScreen
import com.jahangirb.pmpprep.ui.screens.ReferenceScreen
import com.jahangirb.pmpprep.ui.screens.SituationalScreen
import com.jahangirb.pmpprep.ui.theme.PmpTheme

class MainActivity : ComponentActivity() {

    private val vm: PmpViewModel by viewModels()
    /** A .json pack opened or shared into the app, waiting for content to finish loading. */
    private var incoming by mutableStateOf<List<Uri>>(emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (savedInstanceState == null) incoming = urisFrom(intent)
        setContent {
            PmpTheme {
                if (!vm.ready) Loading(vm.loadError) else App(vm, incoming) { incoming = emptyList() }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        incoming = urisFrom(intent)
    }

    override fun onStart() {
        super.onStart()
        vm.appVisible = true
    }

    override fun onStop() {
        vm.appVisible = false
        vm.saveMock()
        super.onStop()
    }

    @Suppress("DEPRECATION")
    private fun streamOf(i: Intent): Uri? =
        if (Build.VERSION.SDK_INT >= 33) i.getParcelableExtra(Intent.EXTRA_STREAM, Uri::class.java)
        else i.getParcelableExtra(Intent.EXTRA_STREAM)

    private fun urisFrom(i: Intent?): List<Uri> {
        if (i == null) return emptyList()
        return when (i.action) {
            Intent.ACTION_VIEW -> listOfNotNull(i.data)
            Intent.ACTION_SEND -> listOfNotNull(streamOf(i))
            else -> emptyList()
        }
    }
}

@Composable
private fun Loading(error: String?) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (error == null) {
                CircularProgressIndicator()
                Spacer(Modifier.height(12.dp))
                Text("Loading question banks…", color = MaterialTheme.colorScheme.onSurfaceVariant)
            } else {
                Text("Could not load content", fontWeight = FontWeight.Bold)
                Text(error, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(16.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun App(vm: PmpViewModel, incoming: List<Uri>, onIncomingHandled: () -> Unit) {
    var importReport by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(incoming) {
        if (incoming.isNotEmpty()) {
            importReport = vm.importPacks(incoming)
            onIncomingHandled()
        }
    }

    BackHandler(enabled = vm.screen != Dest.DASHBOARD) { vm.back() }

    // A running mock exam and a guided session take the whole screen: nothing to wander off through.
    val examRunning = vm.screen == Dest.MOCK && vm.mock != null
    val fullScreen = examRunning || vm.screen == Dest.SESSION

    Scaffold(
        topBar = {
            if (!fullScreen) TopAppBar(
                title = { Text(vm.screen.title, fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    if (vm.screen != Dest.DASHBOARD) {
                        IconButton(onClick = { vm.back() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back") }
                    }
                },
                // The path leads; everything beside it is one tap away in the menu.
                actions = {
                    if (vm.screen != Dest.MENU) IconButton(onClick = { vm.navigate(Dest.MENU) }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu: extra practice, tests, study tools and settings")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                ),
            )
        },
    ) { inner ->
        // consumeWindowInsets: the bars' space is already in `inner`, so a screen's
        // imePadding adds only the part of the keyboard that rises above them.
        Box(Modifier.fillMaxSize().padding(inner).consumeWindowInsets(inner)) {
            when (vm.screen) {
                Dest.DASHBOARD -> PathScreen(vm)
                Dest.PRACTICE -> PracticeScreen(vm)
                Dest.MOCK -> MockExamScreen(vm)
                Dest.SITUATIONAL -> SituationalScreen(vm)
                Dest.LEARN -> LearnScreen(vm)
                Dest.FLASHCARDS -> FlashcardsScreen(vm)
                Dest.FORMULAS -> FormulaLabScreen(vm)
                Dest.REFERENCE -> ReferenceScreen(vm)
                Dest.ANALYTICS -> AnalyticsScreen(vm)
                Dest.MISTAKES -> MistakeJournalScreen(vm)
                Dest.PLANNER -> PlannerScreen(vm)
                Dest.DATA -> DataScreen(vm)
                Dest.COACH -> CoachScreen(vm)
                Dest.NOTES -> NotesScreen(vm)
                Dest.MENU -> MenuScreen(vm)
                Dest.SESSION -> SessionScreen(vm)
            }
        }
    }

    importReport?.let { report ->
        AlertDialog(
            onDismissRequest = { importReport = null },
            confirmButton = { TextButton(onClick = { importReport = null }) { Text("OK") } },
            title = { Text("Import") },
            text = { Text(report) },
        )
    }
}
