package hu.piware.bricklog.feature.user.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.piware.bricklog.feature.core.presentation.asStateFlowIn
import hu.piware.bricklog.feature.user.domain.usecase.WatchCurrentUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class UserDetailsViewModel(
    private val watchCurrentUser: WatchCurrentUser,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserDetailsState())
    val uiState = _uiState
        .asStateFlowIn(viewModelScope) {
            observeCurrentUser()
        }

    fun onAction(action: UserDetailsAction) {
        when (action) {
            else -> Unit
        }
    }

    private fun observeCurrentUser() {
        watchCurrentUser()
            .onEach { user -> _uiState.update { it.copy(currentUser = user) } }
            .launchIn(viewModelScope)
    }
}
