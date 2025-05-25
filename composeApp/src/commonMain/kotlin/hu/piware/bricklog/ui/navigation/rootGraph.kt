package hu.piware.bricklog.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigation
import hu.piware.bricklog.feature.collection.collectionGraph
import hu.piware.bricklog.feature.onboarding.presentation.OnboardingRoute
import hu.piware.bricklog.feature.onboarding.presentation.onboardingGraph
import hu.piware.bricklog.feature.set.presentation.setGraph
import hu.piware.bricklog.feature.settings.presentation.settingsGraph
import kotlinx.serialization.Serializable

sealed interface RootRoute {

    @Serializable
    data object Graph : RootRoute
}

fun NavGraphBuilder.rootGraph(navController: NavHostController) {
    navigation<RootRoute.Graph>(
        startDestination = OnboardingRoute.Graph
    ) {
        onboardingGraph(navController)
        setGraph(navController)
        collectionGraph(navController)
        settingsGraph(navController)
    }
}
