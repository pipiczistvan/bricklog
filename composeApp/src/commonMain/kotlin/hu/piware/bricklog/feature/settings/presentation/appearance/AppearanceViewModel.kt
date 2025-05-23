package hu.piware.bricklog.feature.settings.presentation.appearance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.piware.bricklog.feature.core.presentation.asStateFlowIn
import hu.piware.bricklog.feature.settings.domain.model.ThemeOption
import hu.piware.bricklog.feature.settings.domain.usecase.SaveThemeOption
import hu.piware.bricklog.feature.settings.domain.usecase.WatchThemeOption
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
) : ViewModel() {

    private val _uiState = MutableStateFlow(AppearanceState())

    val uiState = _uiState
        .asStateFlowIn(viewModelScope) {
            observeTheme()
        }

    fun onAction(action: AppearanceAction) {
        when (action) {
            is AppearanceAction.OnThemeOptionChange -> saveTheme(action.option)
            else -> Unit
        }
    }

    private fun saveTheme(theme: ThemeOption) {
        viewModelScope.launch {
            saveThemeOption(theme)
        }
    }

    private fun observeTheme() {
        watchThemeOption()
            .onEach { theme ->
                _uiState.update { it.copy(themeOption = theme) }
            }
            .launchIn(viewModelScope)
    }
}
