package hu.piware.bricklog.feature.authentication.presentation.register

import hu.piware.bricklog.feature.authentication.domain.model.AuthenticationMethod

sealed interface RegisterAction {
    data object OnBackClick : RegisterAction
    data object OnLoginClick : RegisterAction
    data class OnAuthenticate(val method: AuthenticationMethod) : RegisterAction
}
