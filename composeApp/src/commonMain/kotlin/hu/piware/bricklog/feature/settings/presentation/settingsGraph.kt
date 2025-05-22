package hu.piware.bricklog.feature.settings.presentation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import hu.piware.bricklog.feature.settings.presentation.about.AboutScreenRoot
import hu.piware.bricklog.feature.settings.presentation.appearance.AppearanceScreenRoot
import hu.piware.bricklog.feature.settings.presentation.changelog.ChangelogScreenRoot
import hu.piware.bricklog.feature.settings.presentation.license.LicenseScreenRoot
import hu.piware.bricklog.feature.settings.presentation.notifications.NotificationsScreenRoot
import kotlinx.serialization.Serializable

sealed interface SettingsRoute {
    @Serializable
    data object NotificationsScreen : SettingsRoute

    @Serializable
    data object AboutScreen : SettingsRoute

    @Serializable
    data object ChangelogScreen : SettingsRoute

    @Serializable
    data object LicenseScreen : SettingsRoute

    @Serializable
    data object AppearanceScreen : SettingsRoute
}

fun NavGraphBuilder.settingsGraph(navController: NavHostController) {
    composable<SettingsRoute.NotificationsScreen> {
        NotificationsScreenRoot(
            onBackClick = navController::navigateUp
        )
    }

    composable<SettingsRoute.AboutScreen> {
        AboutScreenRoot(
            onBackClick = navController::navigateUp,
            onChangelogClick = {
                navController.navigate(SettingsRoute.ChangelogScreen) {
                    launchSingleTop = true
                }
            },
            onLicenseClick = {
                navController.navigate(SettingsRoute.LicenseScreen) {
                    launchSingleTop = true
                }
            }
        )
    }

    composable<SettingsRoute.ChangelogScreen> {
        ChangelogScreenRoot(
            onBackClick = navController::navigateUp
        )
    }

    composable<SettingsRoute.LicenseScreen> {
        LicenseScreenRoot(
            onBackClick = navController::navigateUp
        )
    }

    composable<SettingsRoute.AppearanceScreen> {
        AppearanceScreenRoot(
            onBackClick = navController::navigateUp
        )
    }
}
