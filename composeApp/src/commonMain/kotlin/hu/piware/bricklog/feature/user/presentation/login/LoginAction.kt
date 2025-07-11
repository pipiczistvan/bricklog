package hu.piware.bricklog.feature.user.presentation.login

import hu.piware.bricklog.feature.user.domain.model.AuthenticationMethod

sealed interface LoginAction {
    data object OnRegisterClick : LoginAction
    data object OnBackClick : LoginAction
    data object OnPasswordResetClick : LoginAction
    data class OnAuthenticate(val method: AuthenticationMethod) : LoginAction
}
