package hu.piware.bricklog.feature.collection.presentation.collection_edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import hu.piware.bricklog.feature.collection.CollectionRoute
import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.collection.domain.usecase.DeleteCollection
import hu.piware.bricklog.feature.collection.domain.usecase.GetCollection
import hu.piware.bricklog.feature.collection.domain.usecase.SaveCollection
import hu.piware.bricklog.feature.collection.domain.usecase.ValidateName
import hu.piware.bricklog.feature.core.domain.onError
import hu.piware.bricklog.feature.core.domain.onSuccess
import hu.piware.bricklog.feature.core.presentation.asStateFlowIn
import hu.piware.bricklog.feature.core.presentation.showSnackbarOnError
import hu.piware.bricklog.feature.core.presentation.toUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CollectionEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val validateName: ValidateName,
    private val saveCollection: SaveCollection,
    private val getCollection: GetCollection,
    private val deleteCollection: DeleteCollection,
) : ViewModel() {

    private val collectionId =
        savedStateHandle.toRoute<CollectionRoute.CollectionEditScreen>().collectionId
    private val _uiState = MutableStateFlow(
        CollectionEditState(
            collectionId = collectionId
        )
    )
    private val _eventChannel = Channel<CollectionEditEvent>()

    val uiState = _uiState.asStateFlowIn(viewModelScope) {
        if (collectionId != 0) {
            loadCollection(collectionId)
        }
    }
    val eventChannel = _eventChannel.receiveAsFlow()

    fun onAction(action: CollectionEditAction) {
        when (action) {
            is CollectionEditAction.OnNameChanged -> {
                _uiState.update { it.copy(name = action.name) }
            }

            is CollectionEditAction.OnIconChanged -> {
                _uiState.update { it.copy(icon = action.icon) }
            }

            CollectionEditAction.OnDeleteClick -> deleteCollection()

            CollectionEditAction.OnSubmit -> submitData()
            else -> Unit
        }
    }

    private fun submitData() {
        _uiState.update { it.copy(nameError = null) }

        validateName(uiState.value.name)
            .onError { error ->
                _uiState.update { it.copy(nameError = error.error.toUiText()) }
                return
            }

        viewModelScope.launch {
            saveCollection(
                Collection(
                    id = uiState.value.collectionId,
                    name = uiState.value.name,
                    icon = uiState.value.icon
                )
            )
                .showSnackbarOnError()
                .onSuccess {
                    _eventChannel.send(CollectionEditEvent.Back)
                }
        }
    }

    private fun loadCollection(id: CollectionId) {
        viewModelScope.launch {
            getCollection(id)
                .showSnackbarOnError()
                .onError {
                    _eventChannel.send(CollectionEditEvent.Back)
                }
                .onSuccess { collection ->
                    _uiState.update {
                        it.copy(
                            name = collection.name,
                            icon = collection.icon,
                        )
                    }
                }
        }
    }

    private fun deleteCollection() {
        viewModelScope.launch {
            deleteCollection(uiState.value.collectionId)
                .showSnackbarOnError()
                .onSuccess {
                    _eventChannel.send(CollectionEditEvent.Back)
                }
        }
    }
}
