package hu.piware.bricklog.feature.set.presentation.set_image

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import hu.piware.bricklog.feature.core.domain.data
import hu.piware.bricklog.feature.core.domain.onError
import hu.piware.bricklog.feature.core.presentation.asStateFlowIn
import hu.piware.bricklog.feature.core.presentation.navigation.CustomNavType
import hu.piware.bricklog.feature.core.presentation.showSnackbarOnError
import hu.piware.bricklog.feature.set.domain.usecase.GetAdditionalImages
import hu.piware.bricklog.feature.set.domain.usecase.WatchSetUI
import hu.piware.bricklog.feature.set.presentation.SetRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.reflect.typeOf

class SetImageViewModel(
    savedStateHandle: SavedStateHandle,
    private val watchSetUI: WatchSetUI,
    private val getAdditionalImages: GetAdditionalImages,
) : ViewModel() {

    private val _arguments = savedStateHandle.toRoute<SetRoute.SetImage>(
        typeMap = mapOf(typeOf<SetImageArguments>() to CustomNavType.SetImageArgumentsType)
    ).arguments

    private var _uiState =
        MutableStateFlow(SetImageState(_arguments.setId, _arguments.sharedElementPrefix))
    val uiState = _uiState
        .asStateFlowIn(viewModelScope) {
            loadSet(_arguments.setId)
            loadAdditionalImages(_arguments.setId)
        }

    private fun loadSet(id: Int) {
        viewModelScope.launch {
            val setUI = watchSetUI(id)
                .first()

            _uiState.update {
                it.copy(
                    setId = setUI.set.setID,
                    images = listOf(setUI.set.image) + it.images
                )
            }
        }
    }

    private fun loadAdditionalImages(id: Int) {
        viewModelScope.launch {
            val additionalImages = getAdditionalImages(id)
                .showSnackbarOnError()
                .onError { return@launch }
                .data()

            _uiState.update {
                it.copy(
                    images = it.images + additionalImages
                )
            }
        }
    }
}
