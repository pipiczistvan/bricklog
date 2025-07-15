package hu.piware.bricklog.feature.user.presentation.password_reset

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.feature_user_password_reset_message_success
import hu.piware.bricklog.feature.core.domain.onSuccess
import hu.piware.bricklog.feature.core.presentation.showSnackbarOnError
import hu.piware.bricklog.feature.core.presentation.showSnackbarOnSuccess
import hu.piware.bricklog.feature.user.domain.usecase.ResetPassword
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class PasswordResetViewModel(
    private val resetPassword: ResetPassword,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PasswordResetState())
    val uiState = _uiState.asStateFlow()

    private val _eventChannel = Channel<PasswordResetEvent>()
    val eventChannel = _eventChannel.receiveAsFlow()

    fun onAction(action: PasswordResetAction) {
        when (action) {
            is PasswordResetAction.OnResetClick -> {
                onReset(action.email)
            }

            else -> Unit
        }
    }

    private fun onReset(email: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            resetPassword(email)
                .showSnackbarOnSuccess(Res.string.feature_user_password_reset_message_success)
                .showSnackbarOnError()
                .onSuccess {
                    _eventChannel.send(PasswordResetEvent.EmailSent)
                }
            _uiState.update { it.copy(isLoading = false) }
        }
    }
}
