@file:OptIn(ExperimentalSharedTransitionApi::class)

package hu.piware.bricklog

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import hu.piware.bricklog.feature.core.NotificationEventHandler
import hu.piware.bricklog.feature.core.presentation.AppEventHandler
import hu.piware.bricklog.feature.core.presentation.LocalizedApp
import hu.piware.bricklog.feature.core.presentation.SnackbarEventHandler
import hu.piware.bricklog.feature.settings.domain.model.ThemeOption
import hu.piware.bricklog.feature.settings.domain.usecase.WatchThemeOption
import hu.piware.bricklog.ui.drawDevLevelRibbon
import hu.piware.bricklog.ui.navigation.RootRoute
import hu.piware.bricklog.ui.navigation.ScaleTransitionDirection
import hu.piware.bricklog.ui.navigation.rootGraph
import hu.piware.bricklog.ui.navigation.scaleIntoContainer
import hu.piware.bricklog.ui.navigation.scaleOutOfContainer
import hu.piware.bricklog.ui.theme.BricklogTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

val LocalNavAnimatedVisibilityScope = compositionLocalOf<AnimatedVisibilityScope?> { null }
val LocalSharedTransitionScope = compositionLocalOf<SharedTransitionScope?> { null }

object App {
    var firstScreenLoaded = false
}

@Composable
@Preview
fun App(
    modifier: Modifier = Modifier,
) {
    val themeOption by koinInject<WatchThemeOption>()().collectAsStateWithLifecycle(ThemeOption.SYSTEM)

    BricklogTheme(
        themeOption = themeOption,
    ) {
        LocalizedApp {
            val navController = rememberNavController()
            val snackbarHostState = remember { SnackbarHostState() }

            AppEventHandler(navController)
            SnackbarEventHandler(snackbarHostState)
            NotificationEventHandler()

            Scaffold(
                modifier = modifier
                    .drawDevLevelRibbon(),
                snackbarHost = {
                    SnackbarHost(
                        hostState = snackbarHostState,
                    )
                },
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
                            },
                        ) {
                            rootGraph(navController)
                        }
                    }
                }
            }
        }
    }
}
