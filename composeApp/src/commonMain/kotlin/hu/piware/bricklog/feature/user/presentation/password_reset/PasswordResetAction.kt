package hu.piware.bricklog.feature.user.presentation.password_reset

sealed interface PasswordResetAction {
    data class OnResetClick(val email: String) : PasswordResetAction
    data object OnBackClick : PasswordResetAction
}
