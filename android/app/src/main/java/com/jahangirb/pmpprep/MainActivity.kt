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
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jahangirb.pmpprep.ui.Dest
import com.jahangirb.pmpprep.ui.PmpViewModel
import com.jahangirb.pmpprep.ui.screens.AnalyticsScreen
import com.jahangirb.pmpprep.ui.screens.CoachScreen
import com.jahangirb.pmpprep.ui.screens.NotesScreen
import com.jahangirb.pmpprep.ui.screens.DashboardScreen
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

private data class NavItem(val dest: Dest, val icon: ImageVector, val label: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun App(vm: PmpViewModel, incoming: List<Uri>, onIncomingHandled: () -> Unit) {
    var menuOpen by remember { mutableStateOf(false) }
    var importReport by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(incoming) {
        if (incoming.isNotEmpty()) {
            importReport = vm.importPacks(incoming)
            onIncomingHandled()
        }
    }

    BackHandler(enabled = vm.screen != Dest.DASHBOARD) { vm.back() }

    val bottom = listOf(
        NavItem(Dest.DASHBOARD, Icons.Default.Dashboard, "Home"),
        NavItem(Dest.PRACTICE, Icons.Default.Quiz, "Practice"),
        NavItem(Dest.MOCK, Icons.Default.Assignment, "Mock"),
        NavItem(Dest.LEARN, Icons.AutoMirrored.Filled.MenuBook, "Learn"),
        NavItem(Dest.FLASHCARDS, Icons.Default.Style, "Cards"),
    )
    val more = listOf(
        NavItem(Dest.COACH, Icons.Default.AutoAwesome, "Coach"),
        NavItem(Dest.NOTES, Icons.AutoMirrored.Filled.Notes, "Notes"),
        NavItem(Dest.SITUATIONAL, Icons.Default.Psychology, "Situational Trainer"),
        NavItem(Dest.FORMULAS, Icons.Default.Calculate, "Formula Lab"),
        NavItem(Dest.REFERENCE, Icons.AutoMirrored.Filled.LibraryBooks, "ECO Tasks & Reference"),
        NavItem(Dest.ANALYTICS, Icons.Default.Insights, "Analytics & Readiness"),
        NavItem(Dest.MISTAKES, Icons.Default.ErrorOutline, "Mistake Journal"),
        NavItem(Dest.PLANNER, Icons.Default.CalendarMonth, "Study Planner"),
        NavItem(Dest.DATA, Icons.Default.Storage, "Question Banks & Backup"),
    )
    // A running mock exam takes the whole screen: no tabs to wander off through.
    val examRunning = vm.screen == Dest.MOCK && vm.mock != null

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(vm.screen.title, fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    if (vm.screen != Dest.DASHBOARD) {
                        IconButton(onClick = { vm.back() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back") }
                    }
                },
                actions = {
                    Box {
                        IconButton(onClick = { menuOpen = true }) { Icon(Icons.Default.MoreVert, contentDescription = "More") }
                        DropdownMenu(expanded = menuOpen, onDismissRequest = { menuOpen = false }) {
                            more.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(item.label) },
                                    leadingIcon = { Icon(item.icon, contentDescription = null) },
                                    onClick = { menuOpen = false; vm.navigate(item.dest) },
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                ),
            )
        },
        bottomBar = {
            if (!examRunning) {
                NavigationBar(containerColor = MaterialTheme.colorScheme.surface, tonalElevation = 6.dp) {
                    bottom.forEach { item ->
                        NavigationBarItem(
                            selected = vm.screen == item.dest,
                            onClick = { vm.navigate(item.dest) },
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label, fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                        )
                    }
                }
            }
        },
    ) { inner ->
        Box(Modifier.fillMaxSize().padding(inner)) {
            when (vm.screen) {
                Dest.DASHBOARD -> DashboardScreen(vm)
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
            }
            // The coach floats over everything except itself, a running mock and the cold diagnostic.
            val diagnosing = vm.practice?.let { it.diagnostic && !it.finished } == true && vm.screen == Dest.PRACTICE
            if (vm.screen != Dest.COACH && !examRunning && !diagnosing) {
                CoachBubble(vm) { vm.coachTab = 0; vm.navigate(Dest.COACH) }
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

/** The coach, a draggable bubble that carries a count of what it has to say. */
@Composable
private fun CoachBubble(vm: PmpViewModel, onOpen: () -> Unit) {
    val rev by vm.revision.collectAsState()
    val count = remember(rev, vm.contentVersion) { vm.coach().report().messages.size }
    val density = LocalDensity.current
    var offset by remember { mutableStateOf(Offset.Zero) }
    BoxWithConstraints(Modifier.fillMaxSize()) {
        val maxX = with(density) { (maxWidth - 72.dp).toPx() }
        val maxY = with(density) { (maxHeight - 72.dp).toPx() }
        Box(
            Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 96.dp)
                .offset { IntOffset(offset.x.roundToInt(), offset.y.roundToInt()) }
                .pointerInput(Unit) {
                    detectDragGestures { change, drag ->
                        change.consume()
                        offset = Offset((offset.x + drag.x).coerceIn(-maxX, 0f), (offset.y + drag.y).coerceIn(-maxY, 0f))
                    }
                },
        ) {
            FloatingActionButton(
                onClick = onOpen,
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ) {
                BadgedBox(badge = { if (count > 0) Badge { Text("$count") } }) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = "Coach")
                }
            }
        }
    }
}
