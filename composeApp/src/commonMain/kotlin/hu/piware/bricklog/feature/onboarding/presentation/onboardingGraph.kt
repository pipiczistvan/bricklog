@file:OptIn(ExperimentalComposeUiApi::class)

package hu.piware.bricklog.feature.onboarding.presentation

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.backhandler.BackHandler
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import hu.piware.bricklog.feature.onboarding.presentation.data_fetch.DataFetchScreenRoot
import hu.piware.bricklog.feature.onboarding.presentation.dispatcher.DispatcherScreenRoot
import hu.piware.bricklog.feature.set.presentation.SetRoute
import kotlinx.serialization.Serializable

sealed interface OnboardingRoute {

    @Serializable
    data object Graph : OnboardingRoute

    @Serializable
    data object DispatcherScreen : OnboardingRoute

    @Serializable
    data object DataFetchScreen : OnboardingRoute
}

fun NavGraphBuilder.onboardingGraph(navController: NavHostController) {
    navigation<OnboardingRoute.Graph>(
        startDestination = OnboardingRoute.DispatcherScreen
    ) {
        composable<OnboardingRoute.DispatcherScreen> {
            DispatcherScreenRoot(
                onNavigateToDashboard = {
                    navController.navigate(SetRoute.DashboardScreen) {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                onNavigateToDataFetch = {
                    navController.navigate(OnboardingRoute.DataFetchScreen) {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable<OnboardingRoute.DataFetchScreen> {
            BackHandler {}

            DataFetchScreenRoot(
                onNavigateToDispatcher = {
                    navController.navigate(OnboardingRoute.DispatcherScreen) {
                        popUpTo<OnboardingRoute.DataFetchScreen> {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
