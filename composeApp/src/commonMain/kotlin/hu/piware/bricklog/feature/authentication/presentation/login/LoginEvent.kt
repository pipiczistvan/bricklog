package hu.piware.bricklog.feature.authentication.presentation.login

sealed interface LoginEvent {
    data object UserLoggedIn : LoginEvent
}
