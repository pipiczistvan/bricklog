package hu.piware.bricklog.feature.user.presentation.password_reset

sealed interface PasswordResetEvent {
    data object EmailSent : PasswordResetEvent
}
