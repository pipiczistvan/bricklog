package hu.piware.bricklog.feature.set.presentation.theme_multi_select

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import hu.piware.bricklog.feature.core.presentation.asStateFlowIn
import hu.piware.bricklog.feature.core.presentation.navigation.CustomNavType
import hu.piware.bricklog.feature.set.domain.usecase.WatchThemes
import hu.piware.bricklog.feature.set.presentation.SetRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlin.reflect.typeOf

class ThemeMultiSelectViewModel(
    private val watchThemes: WatchThemes,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _arguments = savedStateHandle.toRoute<SetRoute.ThemeMultiSelect>(
        typeMap = mapOf(typeOf<ThemeMultiSelectArguments>() to CustomNavType.ThemeMultiSelectArgumentsType)
    ).arguments

    private val _uiState = MutableStateFlow(
        ThemeMultiSelectState(
            selectedThemes = _arguments.themes
        )
    )

    val uiState = _uiState
        .asStateFlowIn(viewModelScope) {
            observeAvailableThemes()
        }

    private val _query = _uiState
        .map { it.searchQuery }
        .distinctUntilChanged()

    fun onAction(action: ThemeMultiSelectAction) {
        when (action) {
            is ThemeMultiSelectAction.OnQueryChange -> _uiState.update { it.copy(searchQuery = action.query) }
            is ThemeMultiSelectAction.OnThemeSelectionChange -> _uiState.update {
                it.copy(
                    selectedThemes = action.themes
                )
            }

            else -> Unit
        }
    }

    private fun observeAvailableThemes() {
        combine(_query, watchThemes()) { query, themes ->
            themes.filter {
                it.contains(query, ignoreCase = true)
            }
        }
            .onEach { themes ->
                _uiState.update { it.copy(availableThemes = themes) }
            }
            .launchIn(viewModelScope)
    }
}
