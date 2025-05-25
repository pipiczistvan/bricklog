package hu.piware.bricklog.feature.set.presentation

import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import hu.piware.bricklog.LocalNavAnimatedVisibilityScope
import hu.piware.bricklog.feature.collection.CollectionRoute
import hu.piware.bricklog.feature.core.presentation.navigation.CustomNavType
import hu.piware.bricklog.feature.set.presentation.dashboard.DashboardScreenRoot
import hu.piware.bricklog.feature.set.presentation.set_detail.SetDetailArguments
import hu.piware.bricklog.feature.set.presentation.set_detail.SetDetailScreenRoot
import hu.piware.bricklog.feature.set.presentation.set_image.SetImageArguments
import hu.piware.bricklog.feature.set.presentation.set_image.SetImageScreenRoot
import hu.piware.bricklog.feature.set.presentation.set_list.SetListArguments
import hu.piware.bricklog.feature.set.presentation.set_list.SetListScreenRoot
import hu.piware.bricklog.feature.set.presentation.set_scanner.SetScannerScreenRoot
import hu.piware.bricklog.feature.settings.presentation.SettingsRoute
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

sealed interface SetRoute {
    @Serializable
    data object Graph : SetRoute

    @Serializable
    data object DashboardScreen : SetRoute

    @Serializable
    data class SetDetails(val arguments: SetDetailArguments) : SetRoute

    @Serializable
    data class SetImage(val arguments: SetImageArguments) : SetRoute

    @Serializable
    data class SetListScreen(val arguments: SetListArguments) : SetRoute

    @Serializable
    data object SetScannerScreen : SetRoute
}

fun NavGraphBuilder.setGraph(navController: NavHostController) {
    navigation<SetRoute.Graph>(
        startDestination = SetRoute.DashboardScreen
    ) {
        composable<SetRoute.DashboardScreen> { entry ->
            val selectedThemes = entry.savedStateHandle.get<Set<String>>("selected_themes")
            val selectedPackagingTypes =
                entry.savedStateHandle.get<Set<String>>("selected_packaging_types")

            CompositionLocalProvider(LocalNavAnimatedVisibilityScope provides this) {
                DashboardScreenRoot(
                    onSearchSets = { arguments ->
                        navController.navigate(SetRoute.SetListScreen(arguments)) {
                            launchSingleTop = true
                        }
                    },
                    onSetClick = { arguments ->
                        navController.navigate(SetRoute.SetDetails(arguments)) {
                            launchSingleTop = true
                        }
                    },
                    onNotificationSettingsClick = {
                        navController.navigate(SettingsRoute.NotificationsScreen) {
                            launchSingleTop = true
                        }
                    },
                    onAppearanceClick = {
                        navController.navigate(SettingsRoute.AppearanceScreen) {
                            launchSingleTop = true
                        }
                    },
                    onCollectionEditClick = {
                        navController.navigate(CollectionRoute.CollectionEditScreen(it)) {
                            launchSingleTop = true
                        }
                    },
                    onAboutClick = {
                        navController.navigate(SettingsRoute.AboutScreen) {
                            launchSingleTop = true
                        }
                    },
                    onScanClick = {
                        navController.navigate(SetRoute.SetScannerScreen) {
                            launchSingleTop = true
                        }
                    },
                    selectedThemes = selectedThemes,
                    selectedPackagingTypes = selectedPackagingTypes
                )
            }
        }
        composable<SetRoute.SetDetails>(
            typeMap = mapOf(
                typeOf<SetDetailArguments>() to CustomNavType.SetDetailArgumentsType
            )
        ) {
            CompositionLocalProvider(LocalNavAnimatedVisibilityScope provides this) {
                SetDetailScreenRoot(
                    onBackClick = navController::navigateUp,
                    onImageClick = { arguments ->
                        navController.navigate(SetRoute.SetImage(arguments)) {
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
        composable<SetRoute.SetImage>(
            typeMap = mapOf(
                typeOf<SetImageArguments>() to CustomNavType.SetImageArgumentsType
            )
        ) {
            CompositionLocalProvider(LocalNavAnimatedVisibilityScope provides this) {
                SetImageScreenRoot(
                    onBackClick = navController::navigateUp,
                )
            }
        }
        composable<SetRoute.SetListScreen>(
            typeMap = mapOf(
                typeOf<SetListArguments>() to CustomNavType.SetListArgumentsType
            )
        ) {
            SetListScreenRoot(
                onBackClick = navController::navigateUp,
                onSetClick = { arguments ->
                    navController.navigate(SetRoute.SetDetails(arguments)) {
                        launchSingleTop = true
                    }
                }
            )
        }
        composable<SetRoute.SetScannerScreen> {
            SetScannerScreenRoot(
                onBackClick = navController::navigateUp,
                onSetClick = { arguments ->
                    navController.navigate(SetRoute.SetDetails(arguments)) {
                        popUpTo(SetRoute.SetScannerScreen) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
