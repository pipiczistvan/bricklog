package hu.piware.bricklog.feature.set.presentation.set_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.collection.domain.usecase.ToggleCollectionSet
import hu.piware.bricklog.feature.collection.domain.usecase.WatchCollectionDetails
import hu.piware.bricklog.feature.collection.domain.usecase.WatchFavouriteCollectionDetails
import hu.piware.bricklog.feature.core.domain.data
import hu.piware.bricklog.feature.core.domain.onError
import hu.piware.bricklog.feature.core.presentation.asStateFlowIn
import hu.piware.bricklog.feature.core.presentation.navigation.CustomNavType
import hu.piware.bricklog.feature.core.presentation.showSnackbarOnError
import hu.piware.bricklog.feature.currency.domain.usecase.WatchCurrencyPreferenceDetails
import hu.piware.bricklog.feature.set.domain.model.SetId
import hu.piware.bricklog.feature.set.domain.usecase.GetInstructions
import hu.piware.bricklog.feature.set.domain.usecase.WatchSetDetailsById
import hu.piware.bricklog.feature.set.domain.util.combineSetWithCurrencyPreference
import hu.piware.bricklog.feature.set.presentation.SetRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.reflect.typeOf

class SetDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val watchSetDetailsById: WatchSetDetailsById,
    private val getInstructions: GetInstructions,
    private val toggleCollectionSet: ToggleCollectionSet,
    private val watchCollectionDetails: WatchCollectionDetails,
    private val watchCurrencyPreferenceDetails: WatchCurrencyPreferenceDetails,
    private val watchFavouriteCollectionDetails: WatchFavouriteCollectionDetails,
) : ViewModel() {

    private val arguments = savedStateHandle.toRoute<SetRoute.SetDetails>(
        typeMap = mapOf(typeOf<SetDetailArguments>() to CustomNavType.SetDetailArgumentsType),
    ).arguments

    private var _uiState = MutableStateFlow(
        SetDetailState(sharedElementPrefix = arguments.sharedElementPrefix),
    )

    val uiState = _uiState
        .asStateFlowIn(viewModelScope) {
            observeSet()
            observeAvailableCollections()
            observePreferredCurrencyPrice()
            observeFavouriteCollection()
            loadInstructions(arguments.setId)
        }

    fun onAction(action: SetDetailAction) {
        when (action) {
            is SetDetailAction.OnCollectionToggle -> toggleCollection(
                action.setId,
                action.collectionId,
            )

            else -> Unit
        }
    }

    private fun loadInstructions(id: Int) {
        viewModelScope.launch {
            val instructions = getInstructions(id)
                .showSnackbarOnError()
                .onError { return@launch }
                .data()

            _uiState.update {
                it.copy(
                    instructions = instructions,
                )
            }
        }
    }

    private fun observeSet() {
        watchSetDetailsById(arguments.setId)
            .onEach { setDetails ->
                _uiState.update { it.copy(setDetails = setDetails) }
            }.launchIn(viewModelScope)
    }

    private fun observeAvailableCollections() {
        watchCollectionDetails()
            .onEach { collections ->
                _uiState.update { it.copy(availableCollections = collections) }
            }.launchIn(viewModelScope)
    }

    private fun observePreferredCurrencyPrice() {
        val currencyDetailsFlow = watchCurrencyPreferenceDetails()
        val setDetailsFlow = uiState.map { it.setDetails }

        combine(setDetailsFlow, currencyDetailsFlow) { setDetails, currencyDetails ->
            combineSetWithCurrencyPreference(setDetails, currencyDetails)
        }.onEach { setPriceDetails ->
            _uiState.update { it.copy(setPriceDetails = setPriceDetails) }
        }.launchIn(viewModelScope)
    }

    private fun observeFavouriteCollection() {
        watchFavouriteCollectionDetails()
            .onEach { baseCollection ->
                _uiState.update { it.copy(baseCollection = baseCollection) }
            }.launchIn(viewModelScope)
    }

    private fun toggleCollection(setId: SetId, collectionId: CollectionId) {
        viewModelScope.launch {
            toggleCollectionSet(setId, collectionId)
                .showSnackbarOnError()
        }
    }
}
