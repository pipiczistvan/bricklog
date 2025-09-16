package hu.piware.bricklog.feature.user.presentation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import hu.piware.bricklog.feature.core.presentation.navigation.CustomNavType
import hu.piware.bricklog.feature.user.presentation.details.UserDetailsScreenRoot
import hu.piware.bricklog.feature.user.presentation.friend_edit.FriendEditArguments
import hu.piware.bricklog.feature.user.presentation.friend_edit.FriendEditScreenRoot
import hu.piware.bricklog.feature.user.presentation.friend_list.FriendListScreenRoot
import hu.piware.bricklog.feature.user.presentation.user_scanner.UserScannerScreenRoot
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

sealed interface UserRoute {
    @Serializable
    data object Graph : UserRoute

    @Serializable
    data object UserDetailsScreen : UserRoute

    @Serializable
    data object FriendListScreen : UserRoute

    @Serializable
    data class FriendEditScreen(
        val arguments: FriendEditArguments,
    ) : UserRoute

    @Serializable
    data object UserScannerScreen : UserRoute
}

fun NavGraphBuilder.userGraph(navController: NavController) {
    navigation<UserRoute.Graph>(
        startDestination = UserRoute.UserDetailsScreen,
    ) {
        composable<UserRoute.UserDetailsScreen> {
            UserDetailsScreenRoot(
                onBackClick = navController::navigateUp,
                onLoginRequired = {
                    navController.navigate(AuthenticationRoute.LoginScreen) {
                        launchSingleTop = true
                    }
                },
                onUserDeleted = {
                    navController.popBackStack<UserRoute.Graph>(inclusive = true)
                },
            )
        }
        composable<UserRoute.FriendListScreen> {
            FriendListScreenRoot(
                onBackClick = navController::navigateUp,
                onFriendEditClick = { friend ->
                    navController.navigate(
                        UserRoute.FriendEditScreen(
                            FriendEditArguments(
                                isNew = friend == null,
                                userId = friend?.id,
                                userName = friend?.name,
                            ),
                        ),
                    )
                },
                onUserScannerClick = {
                    navController.navigate(UserRoute.UserScannerScreen)
                },
            )
        }
        composable<UserRoute.FriendEditScreen>(
            typeMap = mapOf(
                typeOf<FriendEditArguments>() to CustomNavType.FriendEditArgumentsType,
            ),
        ) {
            FriendEditScreenRoot(
                onBackClick = {
                    if (!navController.popBackStack(UserRoute.UserScannerScreen, true)) {
                        navController.navigateUp()
                    }
                },
            )
        }
        composable<UserRoute.UserScannerScreen> {
            UserScannerScreenRoot(
                onBackClick = navController::navigateUp,
                onUserScanned = { userId, userName ->
                    navController.navigate(
                        UserRoute.FriendEditScreen(
                            FriendEditArguments(
                                isNew = true,
                                userId = userId,
                                userName = userName,
                            ),
                        ),
                    )
                },
            )
        }
    }
}
