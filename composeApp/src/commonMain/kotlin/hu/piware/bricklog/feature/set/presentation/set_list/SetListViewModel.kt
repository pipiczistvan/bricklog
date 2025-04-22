@file:OptIn(ExperimentalCoroutinesApi::class)

package hu.piware.bricklog.feature.set.presentation.set_list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import androidx.paging.cachedIn
import androidx.paging.map
import hu.piware.bricklog.feature.core.presentation.asStateFlowIn
import hu.piware.bricklog.feature.core.presentation.navigation.CustomNavType
import hu.piware.bricklog.feature.core.presentation.showSnackbarOnError
import hu.piware.bricklog.feature.set.domain.model.SetListDisplayMode
import hu.piware.bricklog.feature.set.domain.model.SetUI
import hu.piware.bricklog.feature.set.domain.model.calculateStatus
import hu.piware.bricklog.feature.set.domain.usecase.ToggleFavouriteSet
import hu.piware.bricklog.feature.set.domain.usecase.WatchFavouriteSetIds
import hu.piware.bricklog.feature.set.domain.usecase.WatchSetsPaged
import hu.piware.bricklog.feature.set.presentation.SetRoute
import hu.piware.bricklog.feature.settings.domain.usecase.SaveSetListDisplayMode
import hu.piware.bricklog.feature.settings.domain.usecase.WatchSetListDisplayMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.reflect.typeOf

class SetListViewModel(
    watchSetsPaged: WatchSetsPaged,
    watchFavouriteSetIds: WatchFavouriteSetIds,
    savedStateHandle: SavedStateHandle,
    private val toggleFavouriteSet: ToggleFavouriteSet,
    private val watchSetListDisplayMode: WatchSetListDisplayMode,
    private val saveSetListDisplayMode: SaveSetListDisplayMode,
) : ViewModel() {

    private val _arguments = savedStateHandle.toRoute<SetRoute.SetListScreen>(
        typeMap = mapOf(typeOf<SetListArguments>() to CustomNavType.SetListArgumentsType)
    ).arguments

    private val _uiState = MutableStateFlow(
        SetListState(
            title = _arguments.title,
            filter = _arguments.filter,
            themeMultiSelectEnabled = _arguments.themeMultiSelectEnabled
        )
    )

    val uiState = _uiState
        .asStateFlowIn(viewModelScope) {
            observeSetListDisplayMode()
        }

    private val _filter = _uiState
        .mapNotNull { it.filter }
        .distinctUntilChanged()

    private val _pagingData = _filter
        .flatMapLatest { filter -> watchSetsPaged(filter, _arguments.searchQuery) }
        .cachedIn(viewModelScope)

    private val _favouriteSetIds = watchFavouriteSetIds()

    val setUiPagingData = combine(_pagingData, _favouriteSetIds) { pagingData, favouriteSetIds ->
        pagingData.map { set ->
            SetUI(
                set = set,
                isFavourite = favouriteSetIds.contains(set.setID),
                status = set.calculateStatus()
            )
        }
    }

    fun onAction(action: SetListAction) {
        when (action) {
            is SetListAction.OnFavouriteClick -> toggleFavourite(action.setId)
            is SetListAction.OnFilterChange -> _uiState.update { it.copy(filter = action.filter) }
            is SetListAction.OnDisplayModeChange -> saveDisplayMode(action.mode)
            else -> Unit
        }
    }

    private fun toggleFavourite(setId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            toggleFavouriteSet(setId).showSnackbarOnError()
        }
    }

    private fun saveDisplayMode(mode: SetListDisplayMode) {
        viewModelScope.launch(Dispatchers.IO) {
            saveSetListDisplayMode(mode)
        }
    }

    private fun observeSetListDisplayMode() {
        watchSetListDisplayMode()
            .onEach { mode ->
                _uiState.update { it.copy(displayMode = mode) }
            }
            .launchIn(viewModelScope)
    }
}
