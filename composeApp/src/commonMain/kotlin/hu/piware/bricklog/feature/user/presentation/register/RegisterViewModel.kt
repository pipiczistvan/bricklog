package hu.piware.bricklog.feature.user.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.piware.bricklog.feature.core.domain.onSuccess
import hu.piware.bricklog.feature.core.presentation.showSnackbarOnError
import hu.piware.bricklog.feature.onboarding.domain.usecase.InitializeDefaultCollections
import hu.piware.bricklog.feature.user.domain.model.AuthenticationMethod
import hu.piware.bricklog.feature.user.domain.usecase.RegisterUser
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Provided

@KoinViewModel
class RegisterViewModel(
    @Provided private val registerUser: RegisterUser,
    private val initializeDefaultCollections: InitializeDefaultCollections,
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterState())
    val uiState = _uiState.asStateFlow()

    private val _eventChannel = Channel<RegisterEvent>()
    val eventChannel = _eventChannel.receiveAsFlow()

    fun onAction(action: RegisterAction) {
        when (action) {
            is RegisterAction.OnAuthenticate -> onRegister(action.method)
            else -> Unit
        }
    }

    private fun onRegister(method: AuthenticationMethod) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            registerUser(method)
                .showSnackbarOnError()
                .onSuccess {
                    initializeDefaultCollections()
                        .showSnackbarOnError()
                    _eventChannel.send(RegisterEvent.UserRegistered)
                }
            _uiState.update { it.copy(isLoading = false) }
        }
    }
}
