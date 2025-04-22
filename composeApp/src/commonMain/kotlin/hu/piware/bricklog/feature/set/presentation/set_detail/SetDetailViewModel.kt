package hu.piware.bricklog.feature.set.presentation.set_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import hu.piware.bricklog.feature.core.domain.data
import hu.piware.bricklog.feature.core.domain.onError
import hu.piware.bricklog.feature.core.presentation.asStateFlowIn
import hu.piware.bricklog.feature.core.presentation.navigation.CustomNavType
import hu.piware.bricklog.feature.core.presentation.showSnackbarOnError
import hu.piware.bricklog.feature.set.domain.usecase.GetInstructions
import hu.piware.bricklog.feature.set.domain.usecase.ToggleFavouriteSet
import hu.piware.bricklog.feature.set.domain.usecase.WatchSetUI
import hu.piware.bricklog.feature.set.presentation.SetRoute
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.reflect.typeOf

class SetDetailViewModel(
    private val watchSetUI: WatchSetUI,
    private val getInstructions: GetInstructions,
    private val toggleFavouriteSet: ToggleFavouriteSet,
    savedStateHandle: SavedStateHandle,
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
            loadInstructions(_arguments.setId)
        }

    fun onAction(action: SetDetailAction) {
        when (action) {
            is SetDetailAction.OnFavouriteClick -> toggleFavourite(action.setId)
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

    private fun toggleFavourite(setId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            toggleFavouriteSet(setId).showSnackbarOnError()
        }
    }
}
