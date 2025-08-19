package hu.piware.bricklog.feature.user.presentation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import hu.piware.bricklog.feature.user.presentation.details.UserDetailsScreenRoot
import hu.piware.bricklog.feature.user.presentation.login.LoginScreenRoot
import hu.piware.bricklog.feature.user.presentation.password_reset.PasswordResetScreenRoot
import hu.piware.bricklog.feature.user.presentation.register.RegisterScreenRoot
import kotlinx.serialization.Serializable

sealed interface AuthenticationRoute {
    @Serializable
    data object Graph : AuthenticationRoute

    @Serializable
    data object LoginScreen : AuthenticationRoute

    @Serializable
    data object RegisterScreen : AuthenticationRoute

    @Serializable
    data object PasswordResetScreen : AuthenticationRoute

    @Serializable
    data object UserDetailsScreen : AuthenticationRoute
}

fun NavGraphBuilder.authenticationGraph(navController: NavController) {
    navigation<AuthenticationRoute.Graph>(
        startDestination = AuthenticationRoute.LoginScreen,
    ) {
        composable<AuthenticationRoute.LoginScreen> {
            LoginScreenRoot(
                onRegisterClick = {
                    navController.navigate(AuthenticationRoute.RegisterScreen) {
                        launchSingleTop = true
                        popUpTo<AuthenticationRoute.RegisterScreen> {
                            inclusive = true
                        }
                    }
                },
                onPasswordResetClick = {
                    navController.navigate(AuthenticationRoute.PasswordResetScreen) {
                        launchSingleTop = true
                    }
                },
                onUserLoggedIn = {
                    navController.popBackStack<AuthenticationRoute.Graph>(inclusive = true)
                },
                onBackClick = navController::navigateUp,
            )
        }
        composable<AuthenticationRoute.RegisterScreen> {
            RegisterScreenRoot(
                onLoginClick = {
                    navController.navigate(AuthenticationRoute.LoginScreen) {
                        launchSingleTop = true
                        popUpTo<AuthenticationRoute.LoginScreen> {
                            inclusive = true
                        }
                    }
                },
                onUserRegistered = {
                    navController.popBackStack<AuthenticationRoute.Graph>(inclusive = true)
                },
                onBackClick = navController::navigateUp,
            )
        }
        composable<AuthenticationRoute.PasswordResetScreen> {
            PasswordResetScreenRoot(
                onEmailSent = {
                    navController.navigate(AuthenticationRoute.LoginScreen) {
                        launchSingleTop = true
                        popUpTo<AuthenticationRoute.LoginScreen> {
                            inclusive = true
                        }
                    }
                },
                onBackClick = navController::navigateUp,
            )
        }
        composable<AuthenticationRoute.UserDetailsScreen> {
            UserDetailsScreenRoot(
                onBackClick = navController::navigateUp,
            )
        }
    }
}
