package hu.piware.bricklog.feature.authentication.presentation.register

sealed interface RegisterAction {
    data object OnBackClick : RegisterAction
}
