package hu.piware.bricklog.feature.settings.presentation.appearance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.piware.bricklog.feature.core.presentation.asStateFlowIn
import hu.piware.bricklog.feature.core.presentation.showSnackbarOnError
import hu.piware.bricklog.feature.settings.domain.usecase.SaveThemeOption
import hu.piware.bricklog.feature.settings.domain.usecase.WatchThemeOption
import hu.piware.bricklog.feature.user.domain.usecase.SaveUserPreferences
import hu.piware.bricklog.feature.user.domain.usecase.WatchCurrentUser
import hu.piware.bricklog.feature.user.domain.usecase.WatchUserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class AppearanceViewModel(
    private val watchThemeOption: WatchThemeOption,
    private val saveThemeOption: SaveThemeOption,
    private val watchUserPreferences: WatchUserPreferences,
    private val saveUserPreferences: SaveUserPreferences,
    private val watchCurrentUser: WatchCurrentUser,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AppearanceState())

    val uiState = _uiState
        .asStateFlowIn(viewModelScope) {
            observeTheme()
            observeUserPreferences()
            observeCurrentUser()
        }

    fun onAction(action: AppearanceAction) {
        when (action) {
            is AppearanceAction.OnThemeOptionChange -> viewModelScope.launch {
                saveThemeOption(action.option)
            }

            is AppearanceAction.OnUserPreferencesChange -> viewModelScope.launch {
                if (action.showLoading) {
                    _uiState.update { it.copy(isLoading = true) }
                }
                saveUserPreferences(action.preferences)
                    .showSnackbarOnError()
                _uiState.update { it.copy(isLoading = false) }
            }

            else -> Unit
        }
    }

    private fun observeTheme() {
        watchThemeOption()
            .onEach { theme ->
                _uiState.update { it.copy(themeOption = theme) }
            }
            .launchIn(viewModelScope)
    }

    private fun observeUserPreferences() {
        watchUserPreferences()
            .onEach { preferences ->
                _uiState.update { it.copy(userPreferences = preferences) }
            }
            .launchIn(viewModelScope)
    }

    private fun observeCurrentUser() {
        watchCurrentUser()
            .onEach { user ->
                _uiState.update { it.copy(currentUser = user) }
            }
            .launchIn(viewModelScope)
    }
}
