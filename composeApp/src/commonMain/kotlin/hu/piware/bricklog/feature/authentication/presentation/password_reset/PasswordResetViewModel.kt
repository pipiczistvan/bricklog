package hu.piware.bricklog.feature.authentication.presentation.password_reset

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.piware.bricklog.feature.authentication.domain.usecase.ResetPassword
import hu.piware.bricklog.feature.core.presentation.showSnackbarOnError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class PasswordResetViewModel(
    private val resetPassword: ResetPassword,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PasswordResetState())
    val uiState = _uiState.asStateFlow()

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
                .showSnackbarOnError()
            _uiState.update { it.copy(isLoading = false) }
        }
    }
}
