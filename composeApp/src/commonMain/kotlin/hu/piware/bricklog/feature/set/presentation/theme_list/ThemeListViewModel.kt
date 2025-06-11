package hu.piware.bricklog.feature.set.presentation.theme_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.piware.bricklog.feature.core.presentation.asStateFlowIn
import hu.piware.bricklog.feature.set.domain.usecase.WatchThemeGroups
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ThemeListViewModel(
    private val watchThemeGroups: WatchThemeGroups,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ThemeListState())

    val uiState = _uiState
        .asStateFlowIn(viewModelScope) {
            observeThemeGroups()
        }

    fun onAction(action: ThemeListAction) {}

    private fun observeThemeGroups() {
        watchThemeGroups()
            .onEach { themeGroups -> _uiState.update { it.copy(themeGroups = themeGroups) } }
            .launchIn(viewModelScope)
    }
}
