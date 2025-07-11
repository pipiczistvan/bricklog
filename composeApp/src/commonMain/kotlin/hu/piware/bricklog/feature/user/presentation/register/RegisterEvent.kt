package hu.piware.bricklog.feature.user.presentation.register

sealed interface RegisterEvent {
    data object UserRegistered : RegisterEvent
}
