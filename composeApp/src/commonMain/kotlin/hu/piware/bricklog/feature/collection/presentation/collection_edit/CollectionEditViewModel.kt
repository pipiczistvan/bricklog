package hu.piware.bricklog.feature.collection.presentation.collection_edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import hu.piware.bricklog.feature.collection.CollectionRoute
import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.collection.domain.usecase.DeleteCollections
import hu.piware.bricklog.feature.collection.domain.usecase.SaveCollections
import hu.piware.bricklog.feature.collection.domain.usecase.WatchCollection
import hu.piware.bricklog.feature.core.domain.onSuccess
import hu.piware.bricklog.feature.core.presentation.asStateFlowIn
import hu.piware.bricklog.feature.core.presentation.showSnackbarOnError
import hu.piware.bricklog.feature.user.domain.usecase.WatchCurrentUser
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CollectionEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val saveCollections: SaveCollections,
    private val watchCollection: WatchCollection,
    private val deleteCollections: DeleteCollections,
    private val watchCurrentUser: WatchCurrentUser,
) : ViewModel() {

    private val collectionId =
        savedStateHandle.toRoute<CollectionRoute.CollectionEditScreen>().collectionId

    private val _uiState = MutableStateFlow(CollectionEditState())
    val uiState = _uiState.asStateFlowIn(viewModelScope) {
        observeCurrentUser()
        if (collectionId != null) {
            observeCollection(collectionId)
        }
    }

    private val _eventChannel = Channel<CollectionEditEvent>()
    val eventChannel = _eventChannel.receiveAsFlow()

    fun onAction(action: CollectionEditAction) {
        when (action) {
            is CollectionEditAction.OnCollectionChange -> onCollectionChange(action.collection)
            is CollectionEditAction.OnCollectionDelete -> onCollectionDelete(action.collection)
            else -> Unit
        }
    }

    private fun observeCollection(id: CollectionId) {
        watchCollection(id)
            .onEach { collection -> _uiState.update { it.copy(collection = collection) } }
            .launchIn(viewModelScope)
    }

    private fun observeCurrentUser() {
        watchCurrentUser()
            .onEach { user -> _uiState.update { it.copy(currentUser = user) } }
            .launchIn(viewModelScope)
    }

    private fun onCollectionChange(collection: Collection) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            saveCollections(collection)
                .showSnackbarOnError()
                .onSuccess {
                    _eventChannel.send(CollectionEditEvent.Back)
                }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    private fun onCollectionDelete(collection: Collection) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            deleteCollections(collection)
                .showSnackbarOnError()
                .onSuccess {
                    _eventChannel.send(CollectionEditEvent.Deleted)
                }
            _uiState.update { it.copy(isLoading = false) }
        }
    }
}
