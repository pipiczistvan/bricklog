package hu.piware.bricklog.feature.authentication.presentation.register

sealed interface RegisterEvent {
    data object UserRegistered : RegisterEvent
}
