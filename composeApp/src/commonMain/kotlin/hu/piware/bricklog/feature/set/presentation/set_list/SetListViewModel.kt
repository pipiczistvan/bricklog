@file:OptIn(ExperimentalCoroutinesApi::class)

package hu.piware.bricklog.feature.set.presentation.set_list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import androidx.paging.cachedIn
import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.collection.domain.usecase.ToggleCollectionSet
import hu.piware.bricklog.feature.collection.domain.usecase.WatchCollectionDetailsById
import hu.piware.bricklog.feature.collection.domain.usecase.WatchFavouriteCollectionDetails
import hu.piware.bricklog.feature.core.presentation.asStateFlowIn
import hu.piware.bricklog.feature.core.presentation.navigation.CustomNavType
import hu.piware.bricklog.feature.core.presentation.showSnackbarOnError
import hu.piware.bricklog.feature.currency.domain.usecase.WatchCurrencyPreferenceDetails
import hu.piware.bricklog.feature.set.domain.model.SetFilter
import hu.piware.bricklog.feature.set.domain.model.SetId
import hu.piware.bricklog.feature.set.domain.model.SetListDisplayMode
import hu.piware.bricklog.feature.set.domain.usecase.WatchSetDetailsPaged
import hu.piware.bricklog.feature.set.domain.usecase.WatchSetFilterDomain
import hu.piware.bricklog.feature.set.presentation.SetRoute
import hu.piware.bricklog.feature.set.presentation.set_list.components.SetListTitle
import hu.piware.bricklog.feature.settings.domain.model.SetFilterPreferences
import hu.piware.bricklog.feature.settings.domain.usecase.SaveSetFilterPreferences
import hu.piware.bricklog.feature.settings.domain.usecase.SaveSetListDisplayMode
import hu.piware.bricklog.feature.settings.domain.usecase.WatchSetFilterPreferences
import hu.piware.bricklog.feature.settings.domain.usecase.WatchSetListDisplayMode
import hu.piware.bricklog.feature.user.domain.model.isAuthenticated
import hu.piware.bricklog.feature.user.domain.usecase.WatchCurrentUser
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
    private val toggleCollectionSet: ToggleCollectionSet,
    private val watchSetListDisplayMode: WatchSetListDisplayMode,
    private val saveSetListDisplayMode: SaveSetListDisplayMode,
    private val saveSetFilterPreferences: SaveSetFilterPreferences,
    private val watchSetFilterPreferences: WatchSetFilterPreferences,
    private val watchSetFilterDomain: WatchSetFilterDomain,
    private val watchCollectionDetailsById: WatchCollectionDetailsById,
    private val watchCurrencyPreferenceDetails: WatchCurrencyPreferenceDetails,
    private val watchCurrentUser: WatchCurrentUser,
    private val watchFavouriteCollectionDetails: WatchFavouriteCollectionDetails,
) : ViewModel() {

    private val arguments = savedStateHandle.toRoute<SetRoute.SetListScreen>(
        typeMap = mapOf(typeOf<SetListArguments>() to CustomNavType.SetListArgumentsType),
    ).arguments

    private val _uiState = MutableStateFlow(SetListState())

    val uiState = _uiState
        .asStateFlowIn(viewModelScope) {
            handleArguments(arguments)

            observeSetListDisplayMode()
            observeFilterPreferences()
            observeFilterDomain()
            observeCurrencyPreferenceDetails()
        }

    private val filterOverrides = _uiState
        .map { it.filterOverrides }
        .distinctUntilChanged()

    val pagingData = filterOverrides
        .flatMapLatest { watchSetDetailsPaged(it) }
        .flowOn(Dispatchers.Default)
        .cachedIn(viewModelScope)

    fun onAction(action: SetListAction) {
        when (action) {
            is SetListAction.OnCollectionToggle -> toggleCollection(
                action.setId,
                action.collectionId,
            )

            is SetListAction.OnFilterChange -> saveFilter(action.filterPreferences)
            is SetListAction.OnDisplayModeChange -> saveDisplayMode(action.mode)
            else -> Unit
        }
    }

    private fun toggleCollection(setId: SetId, collectionId: CollectionId) {
        viewModelScope.launch {
            toggleCollectionSet(setId, collectionId)
                .showSnackbarOnError()
        }
    }

    private fun saveDisplayMode(mode: SetListDisplayMode) {
        viewModelScope.launch {
            saveSetListDisplayMode(mode)
        }
    }

    private fun handleArguments(arguments: SetListArguments) {
        when (arguments) {
            is SetListArguments.Collection -> {
                _uiState.update {
                    it.copy(
                        filterOverrides = SetFilter(
                            collectionIds = listOf(arguments.collectionId),
                        ),
                    )
                }
                observeCollectionById(arguments.collectionId)
            }

            is SetListArguments.Filtered -> {
                _uiState.update {
                    it.copy(
                        title = SetListTitle.SimpleText(arguments.title),
                        filterOverrides = arguments.filterOverrides,
                        showFilterBar = arguments.showFilterBar,
                    )
                }
                observeFavouriteCollection()
            }
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

    private fun observeCollectionById(id: CollectionId) {
        watchCurrentUser()
            .flatMapLatest { user ->
                watchCollectionDetailsById(id)
                    .onEach { collection ->
                        if (collection == null) return@onEach

                        _uiState.update {
                            it.copy(
                                baseCollection = collection,
                                title = SetListTitle.CollectionName(
                                    collection = collection,
                                    showRole = user.isAuthenticated,
                                ),
                            )
                        }
                    }
            }
            .launchIn(viewModelScope)
    }

    private fun observeFavouriteCollection() {
        watchFavouriteCollectionDetails()
            .onEach { collection ->
                if (collection == null) return@onEach

                _uiState.update {
                    it.copy(
                        baseCollection = collection,
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun observeCurrencyPreferenceDetails() {
        watchCurrencyPreferenceDetails()
            .onEach { details -> _uiState.update { it.copy(currencyPreferenceDetails = details) } }
            .launchIn(viewModelScope)
    }
}
