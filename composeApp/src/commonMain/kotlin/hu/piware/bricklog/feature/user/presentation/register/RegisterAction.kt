package hu.piware.bricklog.feature.user.presentation.register

import hu.piware.bricklog.feature.user.domain.model.AuthenticationMethod

sealed interface RegisterAction {
    data object OnBackClick : RegisterAction
    data object OnLoginClick : RegisterAction
    data class OnAuthenticate(val method: AuthenticationMethod) : RegisterAction
}
