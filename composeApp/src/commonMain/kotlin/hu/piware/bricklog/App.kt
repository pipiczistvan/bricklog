@file:OptIn(ExperimentalSharedTransitionApi::class)

package hu.piware.bricklog

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.dashboard_section_new_sets
import co.touchlab.kermit.Logger
import hu.piware.bricklog.feature.core.NotificationController
import hu.piware.bricklog.feature.core.NotificationEvent
import hu.piware.bricklog.feature.core.presentation.LocalizedApp
import hu.piware.bricklog.feature.core.presentation.SnackbarController
import hu.piware.bricklog.feature.core.presentation.observeAsEvents
import hu.piware.bricklog.feature.set.domain.model.DateFilter
import hu.piware.bricklog.feature.set.presentation.SetRoute
import hu.piware.bricklog.feature.set.presentation.dashboard.utils.newSetsNotificationFilter
import hu.piware.bricklog.feature.set.presentation.set_list.SetListArguments
import hu.piware.bricklog.feature.settings.domain.model.ThemeOption
import hu.piware.bricklog.feature.settings.domain.usecase.WatchThemeOption
import hu.piware.bricklog.ui.drawFlavorRibbon
import hu.piware.bricklog.ui.navigation.RootRoute
import hu.piware.bricklog.ui.navigation.ScaleTransitionDirection
import hu.piware.bricklog.ui.navigation.rootGraph
import hu.piware.bricklog.ui.navigation.scaleIntoContainer
import hu.piware.bricklog.ui.navigation.scaleOutOfContainer
import hu.piware.bricklog.ui.theme.BricklogTheme
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

val LocalNavAnimatedVisibilityScope = compositionLocalOf<AnimatedVisibilityScope?> { null }
val LocalSharedTransitionScope = compositionLocalOf<SharedTransitionScope?> { null }

object App {
    var firstScreenLoaded = false
}

private val logger = Logger.withTag("App")

@Composable
@Preview
fun App(
    modifier: Modifier = Modifier,
) {
    var themeOption by remember { mutableStateOf(ThemeOption.SYSTEM) }

    val watchThemeOption: WatchThemeOption = koinInject()
    watchThemeOption()
        .onEach { themeOption = it }
        .collectAsStateWithLifecycle(ThemeOption.SYSTEM)

    BricklogTheme(
        themeOption = themeOption
    ) {
        LocalizedApp {
            val navController = rememberNavController()
            val snackbarHostState = remember { SnackbarHostState() }

            observeSnackbarEvents(snackbarHostState)
            observeNotificationEvents(navController)

            Scaffold(
                modifier = modifier
                    .drawFlavorRibbon(),
                snackbarHost = {
                    SnackbarHost(
                        hostState = snackbarHostState
                    )
                }
            ) {
                SharedTransitionLayout {
                    CompositionLocalProvider(LocalSharedTransitionScope provides this) {
                        NavHost(
                            navController = navController,
                            startDestination = RootRoute.Graph,
                            enterTransition = {
                                scaleIntoContainer()
                            },
                            exitTransition = {
                                scaleOutOfContainer(direction = ScaleTransitionDirection.INWARDS)
                            },
                            popEnterTransition = {
                                scaleIntoContainer(direction = ScaleTransitionDirection.OUTWARDS)
                            },
                            popExitTransition = {
                                scaleOutOfContainer()
                            }
                        ) {
                            rootGraph(navController)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun observeSnackbarEvents(
    snackbarHostState: SnackbarHostState,
) {
    val scope = rememberCoroutineScope()

    observeAsEvents(flow = SnackbarController.events, snackbarHostState) { event ->
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()

            val result = snackbarHostState.showSnackbar(
                message = event.message,
                actionLabel = event.action?.name,
                duration = SnackbarDuration.Long
            )

            if (result == SnackbarResult.ActionPerformed) {
                event.action?.action?.invoke()
            }
        }
    }
}

@Composable
private fun observeNotificationEvents(
    navController: NavHostController,
) {
    val scope = rememberCoroutineScope()

    observeAsEvents(flow = NotificationController.events) { event ->
        logger.d { "Notification event received. Dispatching." }

        when (event) {
            is NotificationEvent.NewSets ->
                scope.launch {
                    navController.navigate(
                        SetRoute.SetListScreen(
                            arguments = SetListArguments(
                                title = getString(Res.string.dashboard_section_new_sets),
                                filterOverrides = newSetsNotificationFilter.copy(
                                    appearanceDate = DateFilter.Custom(
                                        startDate = event.startDate
                                    ),
                                ),
                                showFilterBar = false
                            )
                        )
                    ) {
                        launchSingleTop = true
                    }
                }

            is NotificationEvent.Empty -> Unit
        }
    }
}
