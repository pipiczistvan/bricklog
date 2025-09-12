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
import hu.piware.bricklog.feature.user.presentation.login.LoginScreenRoot
import hu.piware.bricklog.feature.user.presentation.password_reset.PasswordResetScreenRoot
import hu.piware.bricklog.feature.user.presentation.register.RegisterScreenRoot
import hu.piware.bricklog.feature.user.presentation.user_scanner.UserScannerScreenRoot
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

sealed interface UserRoute {
    @Serializable
    data object Graph : UserRoute

    @Serializable
    data object LoginScreen : UserRoute

    @Serializable
    data object RegisterScreen : UserRoute

    @Serializable
    data object PasswordResetScreen : UserRoute

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

fun NavGraphBuilder.authenticationGraph(navController: NavController) {
    navigation<UserRoute.Graph>(
        startDestination = UserRoute.LoginScreen,
    ) {
        composable<UserRoute.LoginScreen> {
            LoginScreenRoot(
                onRegisterClick = {
                    navController.navigate(UserRoute.RegisterScreen) {
                        launchSingleTop = true
                        popUpTo<UserRoute.RegisterScreen> {
                            inclusive = true
                        }
                    }
                },
                onPasswordResetClick = {
                    navController.navigate(UserRoute.PasswordResetScreen) {
                        launchSingleTop = true
                    }
                },
                onUserLoggedIn = {
                    navController.popBackStack<UserRoute.Graph>(inclusive = true)
                },
                onBackClick = navController::navigateUp,
            )
        }
        composable<UserRoute.RegisterScreen> {
            RegisterScreenRoot(
                onLoginClick = {
                    navController.navigate(UserRoute.LoginScreen) {
                        launchSingleTop = true
                        popUpTo<UserRoute.LoginScreen> {
                            inclusive = true
                        }
                    }
                },
                onUserRegistered = {
                    navController.popBackStack<UserRoute.Graph>(inclusive = true)
                },
                onBackClick = navController::navigateUp,
            )
        }
        composable<UserRoute.PasswordResetScreen> {
            PasswordResetScreenRoot(
                onEmailSent = {
                    navController.navigate(UserRoute.LoginScreen) {
                        launchSingleTop = true
                        popUpTo<UserRoute.LoginScreen> {
                            inclusive = true
                        }
                    }
                },
                onBackClick = navController::navigateUp,
            )
        }
        composable<UserRoute.UserDetailsScreen> {
            UserDetailsScreenRoot(
                onBackClick = navController::navigateUp,
                onLoginClick = {
                    navController.navigate(UserRoute.LoginScreen) {
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
