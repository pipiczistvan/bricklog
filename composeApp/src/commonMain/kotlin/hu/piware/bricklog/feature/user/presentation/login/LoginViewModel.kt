package hu.piware.bricklog.feature.user.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.feature_user_login_message_success
import hu.piware.bricklog.feature.core.domain.onSuccess
import hu.piware.bricklog.feature.core.presentation.showSnackbarOnError
import hu.piware.bricklog.feature.core.presentation.showSnackbarOnSuccess
import hu.piware.bricklog.feature.user.domain.model.AuthenticationMethod
import hu.piware.bricklog.feature.user.domain.usecase.LogInUser
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class LoginViewModel(
    private val logInUser: LogInUser,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginState())
    val uiState = _uiState.asStateFlow()

    private val _eventChannel = Channel<LoginEvent>()
    val eventChannel = _eventChannel.receiveAsFlow()

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.OnAuthenticate -> onAuthenticate(action.method)
            else -> Unit
        }
    }

    private fun onAuthenticate(method: AuthenticationMethod) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            logInUser(method)
                .showSnackbarOnSuccess(Res.string.feature_user_login_message_success)
                .showSnackbarOnError()
                .onSuccess {
                    _eventChannel.send(LoginEvent.UserLoggedIn)
                }
            _uiState.update { it.copy(isLoading = false) }
        }
    }
}
