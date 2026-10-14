package com.jahangirb.pmpprep

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.key
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.IntOffset
import com.jahangirb.pmpprep.data.StepKind
import com.jahangirb.pmpprep.ui.theme.ClayBackdrop
import com.jahangirb.pmpprep.ui.theme.ClayRim
import com.jahangirb.pmpprep.widget.PmpWidget
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.jahangirb.pmpprep.ui.theme.ClayShade
import com.jahangirb.pmpprep.ui.theme.EnterStagger
import com.jahangirb.pmpprep.ui.theme.LocalEnter
import com.jahangirb.pmpprep.ui.theme.clay
import kotlin.math.roundToInt
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
        if (savedInstanceState == null) { incoming = urisFrom(intent); widgetTarget(intent) }
        setContent {
            PmpTheme {
                if (!vm.ready) Loading(vm.loadError) else App(vm, incoming) { incoming = emptyList() }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        incoming = urisFrom(intent)
        widgetTarget(intent)
    }

    /** A tap on one of the home-screen widget's tiles names the screen to open. */
    private fun widgetTarget(i: Intent?) {
        i?.getStringExtra(PmpWidget.OPEN)?.let { vm.pendingOpen = it }
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

    LaunchedEffect(vm.ready, vm.pendingOpen) { if (vm.ready) vm.openPending() }

    LaunchedEffect(incoming) {
        if (incoming.isNotEmpty()) {
            importReport = vm.importPacks(incoming)
            onIncomingHandled()
        }
    }

    BackHandler(enabled = vm.screen != Dest.DASHBOARD) { vm.back() }

    val context = LocalContext.current
    val reduceMotion = remember {
        Settings.Global.getFloat(context.contentResolver, Settings.Global.ANIMATOR_DURATION_SCALE, 1f) == 0f
    }

    // A running mock exam and a guided session take the whole screen: nothing to wander off through.
    val examRunning = vm.screen == Dest.MOCK && vm.mock != null
    val fullScreen = examRunning || vm.screen == Dest.SESSION

    Scaffold(
        // The clay backdrop: a soft gradient behind every screen.
        modifier = Modifier.background(Brush.verticalGradient(ClayBackdrop)),
        containerColor = Color.Transparent,
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
                    containerColor = Color.Transparent,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                ),
            )
        },
    ) { inner ->
        // consumeWindowInsets: the bars' space is already in `inner`, so a screen's
        // imePadding adds only the part of the keyboard that rises above them.
        Box(Modifier.fillMaxSize().padding(inner).consumeWindowInsets(inner)) {
            // A fresh stagger per screen visit, so its cards slide in one after another.
            key(vm.screen) {
                val enter = remember { EnterStagger(reduce = reduceMotion) }
                CompositionLocalProvider(LocalEnter provides enter) {
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
                        Dest.MENU -> MenuScreen(vm)
                        Dest.SESSION -> SessionScreen(vm)
                    }
                }
            }
            // The coach floats over everything except itself and anything with answers to pick:
            // there it would cover the options (each practice question has its own coach button).
            val answering = (vm.screen == Dest.PRACTICE && vm.practice?.let { !it.finished } == true) ||
                (vm.screen == Dest.SESSION && vm.run?.let { r -> r.session.steps.getOrNull(r.step)?.kind in setOf(StepKind.PRACTICE, StepKind.MOCK) } == true)
            if (vm.screen != Dest.COACH && !examRunning && !answering) {
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

/** The coach, a draggable clay bubble that carries a count of what it has to say. Notes live inside it. */
@Composable
private fun CoachBubble(vm: PmpViewModel, onOpen: () -> Unit) {
    val rev by vm.revision.collectAsState()
    val count = remember(rev, vm.contentVersion) { vm.coach().report().messages.size }
    val density = LocalDensity.current
    var offset by remember { mutableStateOf(Offset.Zero) }
    val interaction = remember { MutableInteractionSource() }
    BoxWithConstraints(Modifier.fillMaxSize()) {
        val maxX = with(density) { (maxWidth - 88.dp).toPx() }
        val maxY = with(density) { (maxHeight - 88.dp).toPx() }
        Box(
            Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 28.dp)
                .offset { IntOffset(offset.x.roundToInt(), offset.y.roundToInt()) }
                .pointerInput(Unit) {
                    detectDragGestures { change, drag ->
                        change.consume()
                        offset = Offset((offset.x + drag.x).coerceIn(-maxX, 0f), (offset.y + drag.y).coerceIn(-maxY, 0f))
                    }
                },
        ) {
            BadgedBox(badge = { if (count > 0) Badge { Text("$count") } }) {
                Box(
                    Modifier
                        .size(60.dp)
                        .clay(CircleShape, MaterialTheme.colorScheme.primary, ClayRim, ClayShade, MaterialTheme.colorScheme.primary, interaction, elevation = 14f)
                        .clickable(interaction, LocalIndication.current, onClickLabel = "Open coach and notes", onClick = onOpen)
                        .semantics { contentDescription = "Coach and notes" + if (count > 0) ", $count messages" else "" },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    }
}
