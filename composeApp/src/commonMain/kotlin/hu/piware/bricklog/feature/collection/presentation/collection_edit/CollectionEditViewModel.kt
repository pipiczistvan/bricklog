package hu.piware.bricklog.feature.collection.presentation.collection_edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import hu.piware.bricklog.feature.collection.CollectionRoute
import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.collection.domain.model.CollectionType
import hu.piware.bricklog.feature.collection.domain.usecase.DeleteCollections
import hu.piware.bricklog.feature.collection.domain.usecase.GetCollection
import hu.piware.bricklog.feature.collection.domain.usecase.SaveCollections
import hu.piware.bricklog.feature.collection.domain.usecase.ValidateCollectionName
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
    private val validateCollectionName: ValidateCollectionName,
    private val saveCollections: SaveCollections,
    private val getCollection: GetCollection,
    private val deleteCollections: DeleteCollections,
) : ViewModel() {

    private val collectionId =
        savedStateHandle.toRoute<CollectionRoute.CollectionEditScreen>().collectionId
    private val _uiState = MutableStateFlow(CollectionEditState())
    private val _eventChannel = Channel<CollectionEditEvent>()

    val uiState = _uiState.asStateFlowIn(viewModelScope) {
        if (collectionId != null) {
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

        validateCollectionName(uiState.value.name)
            .onError { error ->
                _uiState.update { it.copy(nameError = error.error.toUiText()) }
                return
            }

        viewModelScope.launch {
            saveCollections(
                Collection(
                    id = uiState.value.collection?.id ?: "",
                    name = uiState.value.name,
                    icon = uiState.value.icon,
                    type = uiState.value.collection?.type ?: CollectionType.USER_DEFINED,
                ),
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
                            collection = collection,
                            name = collection.name,
                            icon = collection.icon,
                        )
                    }
                }
        }
    }

    private fun deleteCollection() {
        viewModelScope.launch {
            deleteCollections(uiState.value.collection?.id ?: "")
                .showSnackbarOnError()
                .onSuccess {
                    _eventChannel.send(CollectionEditEvent.Back)
                }
        }
    }
}
