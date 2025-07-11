package hu.piware.bricklog.feature.authentication.presentation.password_reset

sealed interface PasswordResetAction {
    data class OnResetClick(val email: String) : PasswordResetAction
    data object OnBackClick : PasswordResetAction
}
