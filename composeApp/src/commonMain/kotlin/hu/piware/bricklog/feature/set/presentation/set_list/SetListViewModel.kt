@file:OptIn(ExperimentalCoroutinesApi::class)

package hu.piware.bricklog.feature.set.presentation.set_list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import androidx.paging.cachedIn
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.feature_collection_toggle_message_removed
import bricklog.composeapp.generated.resources.feature_collection_toggle_message_undo
import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.collection.domain.usecase.ToggleCollectionSet
import hu.piware.bricklog.feature.collection.domain.usecase.WatchCollectionDetails
import hu.piware.bricklog.feature.collection.domain.usecase.WatchCollectionDetailsById
import hu.piware.bricklog.feature.core.domain.onSuccess
import hu.piware.bricklog.feature.core.presentation.SnackbarAction
import hu.piware.bricklog.feature.core.presentation.SnackbarEvent
import hu.piware.bricklog.feature.core.presentation.SnackbarEventController
import hu.piware.bricklog.feature.core.presentation.UiText
import hu.piware.bricklog.feature.core.presentation.asStateFlowIn
import hu.piware.bricklog.feature.core.presentation.navigation.CustomNavType
import hu.piware.bricklog.feature.core.presentation.showSnackbarOnError
import hu.piware.bricklog.feature.currency.domain.usecase.WatchCurrencyPreferenceDetails
import hu.piware.bricklog.feature.set.domain.model.SetDetails
import hu.piware.bricklog.feature.set.domain.model.SetFilter
import hu.piware.bricklog.feature.set.domain.model.SetListDisplayMode
import hu.piware.bricklog.feature.set.domain.model.setID
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
    private val watchCollectionDetails: WatchCollectionDetails,
) : ViewModel() {

    private val arguments = savedStateHandle.toRoute<SetRoute.SetListScreen>(
        typeMap = mapOf(typeOf<SetListArguments>() to CustomNavType.SetListArgumentsType),
    ).arguments

    private val _uiState = MutableStateFlow(SetListState())

    val uiState = _uiState
        .asStateFlowIn(viewModelScope) {
            handleArguments(arguments)

            observeCurrentUser()
            observeSetListDisplayMode()
            observeFilterPreferences()
            observeFilterDomain()
            observeCurrencyPreferenceDetails()
            observeCollectionDetails()
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
                action.setDetails,
                action.collectionId,
            )

            is SetListAction.OnFilterChange -> saveFilter(action.filterPreferences)
            is SetListAction.OnDisplayModeChange -> saveDisplayMode(action.mode)
            else -> Unit
        }
    }

    private fun toggleCollection(setDetails: SetDetails, collectionId: CollectionId) {
        viewModelScope.launch {
            toggleCollectionSet(setDetails.setID, collectionId)
                .showSnackbarOnError()
                .onSuccess { added ->
                    val baseCollection = _uiState.value.baseCollection
                    val isCollectionSetList =
                        baseCollection?.collection?.id == collectionId
                    val setWasRemoved = !added

                    if (isCollectionSetList && setWasRemoved) {
                        SnackbarEventController.sendEvent(
                            SnackbarEvent(
                                message = UiText.StringResourceId(
                                    Res.string.feature_collection_toggle_message_removed,
                                    setDetails.set.name ?: "",
                                    baseCollection.collection.name,
                                ),
                                action = SnackbarAction(
                                    name = UiText.StringResourceId(Res.string.feature_collection_toggle_message_undo),
                                    action = { toggleCollection(setDetails, collectionId) },
                                ),
                            ),
                        )
                    }
                }
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
                        title = SetListTitle.SetSearch(arguments.title),
                        filterOverrides = arguments.filterOverrides,
                        showFilterBar = arguments.showFilterBar,
                    )
                }
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

    private fun observeCurrentUser() {
        watchCurrentUser()
            .onEach { user -> _uiState.update { it.copy(user = user) } }
            .launchIn(viewModelScope)
    }

    private fun observeCollectionById(id: CollectionId) {
        uiState.map { it.user }
            .flatMapLatest { user ->
                watchCollectionDetailsById(id)
                    .onEach { collection ->
                        if (collection == null) return@onEach

                        _uiState.update {
                            it.copy(
                                baseCollection = collection,
                                title = SetListTitle.CollectionSearch(
                                    collection = collection,
                                    showRole = user.isAuthenticated,
                                ),
                            )
                        }
                    }
            }
            .launchIn(viewModelScope)
    }

    private fun observeCurrencyPreferenceDetails() {
        watchCurrencyPreferenceDetails()
            .onEach { details -> _uiState.update { it.copy(currencyPreferenceDetails = details) } }
            .launchIn(viewModelScope)
    }

    private fun observeCollectionDetails() {
        watchCollectionDetails()
            .onEach { details -> _uiState.update { it.copy(availableCollections = details) } }
            .launchIn(viewModelScope)
    }
}
