package hu.piware.bricklog.feature.set.presentation.set_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.collection.domain.usecase.ToggleSetCollection
import hu.piware.bricklog.feature.collection.domain.usecase.WatchCollections
import hu.piware.bricklog.feature.core.domain.data
import hu.piware.bricklog.feature.core.domain.onError
import hu.piware.bricklog.feature.core.presentation.asStateFlowIn
import hu.piware.bricklog.feature.core.presentation.navigation.CustomNavType
import hu.piware.bricklog.feature.core.presentation.showSnackbarOnError
import hu.piware.bricklog.feature.set.domain.model.SetId
import hu.piware.bricklog.feature.set.domain.usecase.GetInstructions
import hu.piware.bricklog.feature.set.domain.usecase.WatchSetUI
import hu.piware.bricklog.feature.set.presentation.SetRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.reflect.typeOf

class SetDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val watchSetUI: WatchSetUI,
    private val getInstructions: GetInstructions,
    private val toggleSetCollection: ToggleSetCollection,
    private val watchCollections: WatchCollections,
) : ViewModel() {

    private val _arguments = savedStateHandle.toRoute<SetRoute.SetDetails>(
        typeMap = mapOf(typeOf<SetDetailArguments>() to CustomNavType.SetDetailArgumentsType)
    ).arguments

    private var _uiState = MutableStateFlow(
        SetDetailState(sharedElementPrefix = _arguments.sharedElementPrefix)
    )

    val uiState = _uiState
        .asStateFlowIn(viewModelScope) {
            observeSet()
            observeAvailableCollections()
            loadInstructions(_arguments.setId)
        }

    fun onAction(action: SetDetailAction) {
        when (action) {
            is SetDetailAction.OnToggleCollection -> toggleCollection(
                action.setId,
                action.collectionId
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
                    instructions = instructions
                )
            }
        }
    }

    private fun observeSet() {
        watchSetUI(_arguments.setId)
            .onEach { set ->
                _uiState.update { it.copy(setUI = set) }
            }.launchIn(viewModelScope)
    }

    private fun observeAvailableCollections() {
        watchCollections()
            .onEach { collections ->
                _uiState.update { it.copy(availableCollections = collections) }
            }.launchIn(viewModelScope)
    }

    private fun toggleCollection(setId: SetId, collectionId: CollectionId) {
        viewModelScope.launch {
            toggleSetCollection(setId, collectionId)
                .showSnackbarOnError()
        }
    }
}
