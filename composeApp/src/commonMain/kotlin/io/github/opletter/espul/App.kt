package io.github.opletter.espul

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.setSingletonImageLoaderFactory
import com.russhwolf.settings.Settings
import io.github.opletter.espul.components.icons.*
import io.github.opletter.espul.screens.FeedScreen
import io.github.opletter.espul.screens.SettingsScreen
import io.github.opletter.espul.state.EspulViewModel
import io.github.opletter.espul.state.NavState
import io.github.opletter.espul.theme.AppTheme
import io.github.opletter.espul.theme.LocalThemeIsDark
import io.github.opletter.espul.util.DoubleBackHandler
import io.github.opletter.espul.util.openUrl

@OptIn(ExperimentalCoilApi::class)
@Composable
internal fun App() {
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .build()
    }
    AppTheme {
        AppContent()
    }
}

val LocalSnackbarHostState =
    compositionLocalOf<SnackbarHostState> { error("SnackbarHostState is not found") }

data class ScreenData(
    val title: String,
    val icon: ImageVector,
    val content: @Composable (EspulViewModel) -> Unit,
)

enum class EspulScreen(val screen: ScreenData) {
    Feed(ScreenData("Feed", Icons.Filled.Feed) { FeedScreen(it) }),
    Bookmarks(ScreenData("Bookmarks", Icons.Filled.Bookmarks) { FeedScreen(it) }),
    Settings(ScreenData("Settings", Icons.Filled.Settings) { SettingsScreen(it) }),
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
internal fun AppContent() {
    val widthSizeClass = calculateWindowSizeClass().widthSizeClass
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val coroutineScope = rememberCoroutineScope()
    val viewModel = remember { EspulViewModel(coroutineScope) }

    DoubleBackHandler(onBack = { viewModel.onBack() })

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            @Composable
            fun RowScope.actions() {
                IconButton(
                    onClick = { openUrl("https://github.com/dennistsar/espul") }
                ) {
                    Icon(
                        Icons.Default.Code,
                        contentDescription = null
                    )
                }
                var isDark by LocalThemeIsDark.current
                IconButton(
                    onClick = { isDark = !isDark; Settings().putBoolean("isDark", isDark) }
                ) {
                    Icon(
                        if (isDark) Icons.Default.LightMode else Icons.Default.DarkMode,
                        contentDescription = "Toggle color mode"
                    )
                }
            }

            if (widthSizeClass < WindowWidthSizeClass.Expanded) {
                TopAppBar(
                    title = {
                        val state = viewModel.navState
                        Text(text = if (state is NavState.UserEvents) state.user.displayName else "Espul")
                    },
                    modifier = Modifier.heightIn(max = 75.dp),
                    scrollBehavior = scrollBehavior,
                    actions = { actions() }
                )
            } else {
                CenterAlignedTopAppBar(
                    title = { Text(text = "Espul") },
                    scrollBehavior = scrollBehavior,
                    actions = { actions() }
                )
            }
        },
        content = {
            Row(
                modifier = Modifier.padding(it).consumeWindowInsets(WindowInsets.systemBars)
            ) {
                if (widthSizeClass == WindowWidthSizeClass.Medium) {
                    NavigationRail(
                        modifier = Modifier.padding(6.dp)
                    ) {
                        EspulScreen.entries.forEach { entry ->
                            val screen = entry.screen
                            NavigationRailItem(
                                icon = { Icon(screen.icon, contentDescription = screen.title) },
                                label = { Text(screen.title) },
                                selected = viewModel.navState.screenData == entry,
                                onClick = {
                                    scrollBehavior.state.contentOffset = 0f
                                    viewModel.navItemClicked(entry)
                                }
                            )
                        }
                    }
                } else if (widthSizeClass == WindowWidthSizeClass.Expanded) {
                    PermanentDrawerSheet(
                        modifier = Modifier.padding(6.dp).widthIn(max = 250.dp),
//                        drawerContainerColor = MaterialTheme.colorScheme.surfaceTint,
                    ) {
                        EspulScreen.entries.forEach { entry ->
                            val screen = entry.screen
                            NavigationDrawerItem(
                                icon = { Icon(screen.icon, contentDescription = screen.title) },
                                label = { Text(screen.title) },
                                selected = viewModel.navState.screenData == entry,
                                onClick = {
                                    scrollBehavior.state.contentOffset = 0f
                                    viewModel.navItemClicked(entry)
                                },
                                shape = RectangleShape,
                            )
                        }
                    }
                }
                CompositionLocalProvider(
                    LocalSnackbarHostState provides snackbarHostState,
                ) {
                    Box(Modifier.widthIn(max = 800.dp)) {
                        viewModel.navState.screenData.screen.content(viewModel)
                    }
                }
            }
        },
        bottomBar = {
            if (widthSizeClass == WindowWidthSizeClass.Compact) {
                NavigationBar {
                    EspulScreen.entries.forEach { entry ->
                        val screen = entry.screen
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = screen.title) },
                            label = { Text(screen.title) },
                            selected = viewModel.navState.screenData == entry,
                            onClick = {
                                scrollBehavior.state.contentOffset = 0f
                                viewModel.navItemClicked(entry)
                            }
                        )
                    }
                }
            }
        }
    )
}
