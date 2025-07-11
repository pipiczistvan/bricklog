package hu.piware.bricklog.feature.authentication.presentation.login

sealed interface LoginAction {
    data object OnRegisterClick : LoginAction
    data object OnBackClick : LoginAction
}
