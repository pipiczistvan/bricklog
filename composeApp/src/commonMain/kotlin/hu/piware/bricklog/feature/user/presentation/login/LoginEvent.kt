package hu.piware.bricklog.feature.user.presentation.login

sealed interface LoginEvent {
    data object UserLoggedIn : LoginEvent
}
