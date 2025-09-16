package hu.piware.bricklog.feature.user.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.feature_user_delete_user_data_action_reauthenticate
import bricklog.composeapp.generated.resources.feature_user_delete_user_data_message_success
import hu.piware.bricklog.feature.core.domain.UserError
import hu.piware.bricklog.feature.core.domain.onSuccess
import hu.piware.bricklog.feature.core.presentation.SnackbarAction
import hu.piware.bricklog.feature.core.presentation.UiText
import hu.piware.bricklog.feature.core.presentation.asStateFlowIn
import hu.piware.bricklog.feature.core.presentation.showSnackbarOnError
import hu.piware.bricklog.feature.core.presentation.showSnackbarOnSuccess
import hu.piware.bricklog.feature.user.domain.usecase.DeleteUserData
import hu.piware.bricklog.feature.user.domain.usecase.SaveUserPreferences
import hu.piware.bricklog.feature.user.domain.usecase.WatchCurrentUser
import hu.piware.bricklog.feature.user.domain.usecase.WatchUserPreferences
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Provided

@KoinViewModel
class UserDetailsViewModel(
    private val watchCurrentUser: WatchCurrentUser,
    private val watchUserPreferences: WatchUserPreferences,
    private val saveUserPreferences: SaveUserPreferences,
    @Provided private val deleteUserData: DeleteUserData,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserDetailsState())
    val uiState = _uiState
        .asStateFlowIn(viewModelScope) {
            observeCurrentUser()
            observeUserPreferences()
        }

    private val _eventChannel = Channel<UserDetailsEvent>()
    val eventChannel = _eventChannel.receiveAsFlow()

    fun onAction(action: UserDetailsAction) {
        when (action) {
            is UserDetailsAction.OnUserPreferencesChange -> viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true) }
                saveUserPreferences(action.preferences)
                    .showSnackbarOnError()
                    .onSuccess {
                        _eventChannel.send(UserDetailsEvent.Back)
                    }
                _uiState.update { it.copy(isLoading = false) }
            }

            UserDetailsAction.OnUserDelete -> viewModelScope.launch {
                deleteUserData()
                    .showSnackbarOnSuccess(Res.string.feature_user_delete_user_data_message_success)
                    .onSuccess {
                        viewModelScope.launch {
                            _eventChannel.send(UserDetailsEvent.UserDeleted)
                        }
                    }
                    .showSnackbarOnError { error ->
                        if (error == UserError.General.REAUTHENTICATION_REQUIRED) {
                            SnackbarAction(
                                name = UiText.StringResourceId(Res.string.feature_user_delete_user_data_action_reauthenticate),
                                action = {
                                    viewModelScope.launch {
                                        _eventChannel.send(UserDetailsEvent.LoginRequired)
                                    }
                                },
                            )
                        } else {
                            null
                        }
                    }
            }

            else -> Unit
        }
    }

    private fun observeCurrentUser() {
        watchCurrentUser()
            .onEach { user -> _uiState.update { it.copy(currentUser = user) } }
            .launchIn(viewModelScope)
    }

    private fun observeUserPreferences() {
        watchUserPreferences()
            .onEach { preferences ->
                _uiState.update { it.copy(userPreferences = preferences) }
            }
            .launchIn(viewModelScope)
    }
}
