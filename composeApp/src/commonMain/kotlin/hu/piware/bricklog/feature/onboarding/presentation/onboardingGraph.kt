package hu.piware.bricklog.feature.onboarding.presentation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import hu.piware.bricklog.feature.core.presentation.navigation.BackHandler
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
                    }
                },
                onNavigateToDataFetch = {
                    navController.navigate(OnboardingRoute.DataFetchScreen) {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable<OnboardingRoute.DataFetchScreen> {
            BackHandler {}

            DataFetchScreenRoot(
                onNavigateToDashboard = {
                    navController.navigate(SetRoute.DashboardScreen) {
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
