package hu.piware.bricklog.feature.authentication.presentation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import hu.piware.bricklog.feature.authentication.presentation.login.LoginScreenRoot
import hu.piware.bricklog.feature.authentication.presentation.register.RegisterScreenRoot
import kotlinx.serialization.Serializable

sealed interface AuthenticationRoute {
    @Serializable
    data object Graph : AuthenticationRoute

    @Serializable
    data object LoginScreen : AuthenticationRoute

    @Serializable
    data object RegisterScreen : AuthenticationRoute
}

fun NavGraphBuilder.authenticationGraph(navController: NavController) {
    navigation<AuthenticationRoute.Graph>(
        startDestination = AuthenticationRoute.LoginScreen
    ) {
        composable<AuthenticationRoute.LoginScreen> {
            LoginScreenRoot(
                onRegisterClick = {
                    navController.navigate(AuthenticationRoute.RegisterScreen) {
                        launchSingleTop = true
                    }
                },
                onBackClick = navController::navigateUp
            )
        }
        composable<AuthenticationRoute.RegisterScreen> {
            RegisterScreenRoot(
                onBackClick = navController::navigateUp
            )
        }
    }
}
