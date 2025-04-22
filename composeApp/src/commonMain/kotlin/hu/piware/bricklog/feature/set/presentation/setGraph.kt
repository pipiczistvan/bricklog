package hu.piware.bricklog.feature.set.presentation

import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import hu.piware.bricklog.LocalNavAnimatedVisibilityScope
import hu.piware.bricklog.feature.core.presentation.navigation.CustomNavType
import hu.piware.bricklog.feature.set.presentation.dashboard.DashboardScreenRoot
import hu.piware.bricklog.feature.set.presentation.set_detail.SetDetailArguments
import hu.piware.bricklog.feature.set.presentation.set_detail.SetDetailScreenRoot
import hu.piware.bricklog.feature.set.presentation.set_image.SetImageArguments
import hu.piware.bricklog.feature.set.presentation.set_image.SetImageScreenRoot
import hu.piware.bricklog.feature.set.presentation.set_list.SetListArguments
import hu.piware.bricklog.feature.set.presentation.set_list.SetListScreenRoot
import hu.piware.bricklog.feature.set.presentation.set_scanner.SetScannerScreenRoot
import hu.piware.bricklog.feature.set.presentation.theme_multi_select.ThemeMultiSelectArguments
import hu.piware.bricklog.feature.set.presentation.theme_multi_select.ThemeMultiSelectScreenRoot
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
    data class ThemeMultiSelect(val arguments: ThemeMultiSelectArguments) : SetRoute

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

            CompositionLocalProvider(LocalNavAnimatedVisibilityScope provides this) {
                DashboardScreenRoot(
                    onSearchSets = { arguments ->
                        navController.navigate(SetRoute.SetListScreen(arguments))
                    },
                    onSetClick = { arguments ->
                        navController.navigate(SetRoute.SetDetails(arguments))
                    },
                    onNotificationSettingsClick = {
                        navController.navigate(SettingsRoute.NotificationsScreen)
                    },
                    onAppearanceClick = {
                        navController.navigate(SettingsRoute.AppearanceScreen)
                    },
                    onAboutClick = {
                        navController.navigate(SettingsRoute.AboutScreen)
                    },
                    onThemeMultiselectClick = { arguments ->
                        navController.navigate(SetRoute.ThemeMultiSelect(arguments))
                    },
                    onScanClick = {
                        navController.navigate(SetRoute.SetScannerScreen)
                    },
                    selectedThemes = selectedThemes
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
                    onImageClick = { arguments -> navController.navigate(SetRoute.SetImage(arguments)) }
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
                    onBackClick = {
                        navController.navigateUp()
                    }
                )
            }
        }
        composable<SetRoute.ThemeMultiSelect>(
            typeMap = mapOf(
                typeOf<ThemeMultiSelectArguments>() to CustomNavType.ThemeMultiSelectArgumentsType
            )
        ) {
            CompositionLocalProvider(LocalNavAnimatedVisibilityScope provides this) {
                ThemeMultiSelectScreenRoot(
                    onBackClick = {
                        navController.navigateUp()
                    },
                    onApplyClick = { arguments ->
                        navController.previousBackStackEntry?.savedStateHandle?.set(
                            "selected_themes",
                            arguments.themes
                        )
                        navController.navigateUp()
                    }
                )
            }
        }
        composable<SetRoute.SetListScreen>(
            typeMap = mapOf(
                typeOf<SetListArguments>() to CustomNavType.SetListArgumentsType
            )
        ) { entry ->
            val selectedThemes = entry.savedStateHandle.get<Set<String>>("selected_themes")

            SetListScreenRoot(
                onBackClick = navController::navigateUp,
                onSetClick = { arguments ->
                    navController.navigate(SetRoute.SetDetails(arguments))
                },
                onThemeMultiselectClick = { arguments ->
                    navController.navigate(SetRoute.ThemeMultiSelect(arguments))
                },
                selectedThemes = selectedThemes
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
                    }
                }
            )
        }
    }
}
