@file:OptIn(ExperimentalCoroutinesApi::class)

package hu.piware.bricklog.feature.set.presentation.set_list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import androidx.paging.cachedIn
import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.collection.domain.usecase.ToggleSetCollection
import hu.piware.bricklog.feature.collection.domain.usecase.WatchCollection
import hu.piware.bricklog.feature.collection.domain.util.COLLECTION_ID_FAVOURITE_SETS
import hu.piware.bricklog.feature.core.presentation.asStateFlowIn
import hu.piware.bricklog.feature.core.presentation.navigation.CustomNavType
import hu.piware.bricklog.feature.core.presentation.showSnackbarOnError
import hu.piware.bricklog.feature.set.domain.model.SetListDisplayMode
import hu.piware.bricklog.feature.set.domain.usecase.WatchSetDetailsPaged
import hu.piware.bricklog.feature.set.domain.usecase.WatchSetFilterDomain
import hu.piware.bricklog.feature.set.presentation.SetRoute
import hu.piware.bricklog.feature.settings.domain.model.SetFilterPreferences
import hu.piware.bricklog.feature.settings.domain.usecase.SaveSetFilterPreferences
import hu.piware.bricklog.feature.settings.domain.usecase.SaveSetListDisplayMode
import hu.piware.bricklog.feature.settings.domain.usecase.WatchSetFilterPreferences
import hu.piware.bricklog.feature.settings.domain.usecase.WatchSetListDisplayMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.reflect.typeOf

class SetListViewModel(
    savedStateHandle: SavedStateHandle,
    watchSetDetailsPaged: WatchSetDetailsPaged,
    private val toggleSetCollection: ToggleSetCollection,
    private val watchSetListDisplayMode: WatchSetListDisplayMode,
    private val saveSetListDisplayMode: SaveSetListDisplayMode,
    private val saveSetFilterPreferences: SaveSetFilterPreferences,
    private val watchSetFilterPreferences: WatchSetFilterPreferences,
    private val watchSetFilterDomain: WatchSetFilterDomain,
    private val watchCollection: WatchCollection,
) : ViewModel() {

    private val _arguments = savedStateHandle.toRoute<SetRoute.SetListScreen>(
        typeMap = mapOf(typeOf<SetListArguments>() to CustomNavType.SetListArgumentsType)
    ).arguments

    private val _uiState = MutableStateFlow(
        SetListState(
            title = _arguments.title,
            filterOverrides = _arguments.filterOverrides,
            showFilterBar = _arguments.showFilterBar
        )
    )

    val uiState = _uiState
        .asStateFlowIn(viewModelScope) {
            observeSetListDisplayMode()
            observeFilterPreferences()
            observeFilterDomain()

            if (_arguments.filterOverrides?.collectionIds?.count() == 1) {
                observeCollection(_arguments.filterOverrides.collectionIds.first())
            }
        }

    private val _filterOverrides = _uiState
        .map { it.filterOverrides }
        .distinctUntilChanged()

    val pagingData = _filterOverrides
        .flatMapLatest { filterOverrides ->
            watchSetDetailsPaged(
                filterOverrides,
                _arguments.searchQuery
            )
        }
        .flowOn(Dispatchers.Default)
        .cachedIn(viewModelScope)

    fun onAction(action: SetListAction) {
        when (action) {
            is SetListAction.OnFavouriteClick -> toggleFavourite(action.setId)
            is SetListAction.OnFilterChange -> saveFilter(action.filterPreferences)
            is SetListAction.OnDisplayModeChange -> saveDisplayMode(action.mode)
            else -> Unit
        }
    }

    private fun toggleFavourite(setId: Int) {
        viewModelScope.launch {
            toggleSetCollection(setId, COLLECTION_ID_FAVOURITE_SETS)
                .showSnackbarOnError()
        }
    }

    private fun saveDisplayMode(mode: SetListDisplayMode) {
        viewModelScope.launch {
            saveSetListDisplayMode(mode)
        }
    }

    private fun observeSetListDisplayMode() {
        watchSetListDisplayMode()
            .onEach { mode -> _uiState.update { it.copy(displayMode = mode) } }
            .launchIn(viewModelScope)
    }

    private fun saveFilter(filterPreferences: SetFilterPreferences) {
        viewModelScope.launch {
            saveSetFilterPreferences(filterPreferences)
        }
    }

    private fun observeFilterPreferences() {
        watchSetFilterPreferences()
            .onEach { filter -> _uiState.update { it.copy(filterPreferences = filter) } }
            .launchIn(viewModelScope)
    }

    private fun observeFilterDomain() {
        watchSetFilterDomain()
            .onEach { domain -> _uiState.update { it.copy(filterDomain = domain) } }
            .launchIn(viewModelScope)
    }

    private fun observeCollection(id: CollectionId) {
        watchCollection(id)
            .onEach { collection -> _uiState.update { it.copy(title = collection.name) } }
            .launchIn(viewModelScope)
    }
}
