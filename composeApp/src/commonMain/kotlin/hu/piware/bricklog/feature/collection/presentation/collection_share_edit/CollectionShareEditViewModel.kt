package hu.piware.bricklog.feature.collection.presentation.collection_share_edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import hu.piware.bricklog.feature.collection.CollectionRoute
import hu.piware.bricklog.feature.collection.domain.model.UserCollectionShare
import hu.piware.bricklog.feature.collection.domain.usecase.GetCollection
import hu.piware.bricklog.feature.collection.domain.usecase.ShareCollection
import hu.piware.bricklog.feature.collection.domain.usecase.UnshareCollection
import hu.piware.bricklog.feature.core.domain.onSuccess
import hu.piware.bricklog.feature.core.presentation.asStateFlowIn
import hu.piware.bricklog.feature.core.presentation.navigation.CustomNavType
import hu.piware.bricklog.feature.core.presentation.showSnackbarOnError
import hu.piware.bricklog.feature.user.domain.usecase.WatchFriends
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.reflect.typeOf

class CollectionShareEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val getCollection: GetCollection,
    private val shareCollection: ShareCollection,
    private val unshareCollection: UnshareCollection,
    private val watchFriends: WatchFriends,
) : ViewModel() {

    private val arguments = savedStateHandle.toRoute<CollectionRoute.CollectionShareEditScreen>(
        typeMap = mapOf(typeOf<CollectionShareEditArguments>() to CustomNavType.CollectionShareEditArgumentsType),
    ).arguments

    private val _uiState = MutableStateFlow(CollectionShareEditState())
    val uiState = _uiState.asStateFlowIn(viewModelScope) {
        if (arguments.userId != null) {
            _uiState.update { it.copy(shareUserIdArgument = arguments.userId) }
        }
        loadUserCollectionShare()
        observeFriends()
    }

    private val _eventChannel = Channel<CollectionShareEditEvent>()
    val eventChannel = _eventChannel.receiveAsFlow()

    fun onAction(action: CollectionShareEditAction) {
        when (action) {
            is CollectionShareEditAction.OnShareChange -> onShareChange(action.share)

            is CollectionShareEditAction.OnShareDelete -> onShareDelete(action.share)

            else -> Unit
        }
    }

    private suspend fun loadUserCollectionShare() {
        getCollection(arguments.collectionId)
            .showSnackbarOnError()
            .onSuccess { collection ->
                _uiState.update {
                    it.copy(
                        collection = collection,
                        share = arguments.userId?.let {
                            UserCollectionShare(
                                userId = arguments.userId,
                                permissions = collection.shares[arguments.userId]!!,
                            )
                        },
                    )
                }
            }
    }

    private fun observeFriends() {
        watchFriends()
            .onEach { friends ->
                _uiState.update { it.copy(friends = friends) }
            }
            .launchIn(viewModelScope)
    }

    private fun onShareChange(share: UserCollectionShare) {
        val collection = _uiState.value.collection ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            shareCollection(
                collectionId = collection.id,
                share = share,
            )
                .showSnackbarOnError()
                .onSuccess {
                    _eventChannel.send(CollectionShareEditEvent.Back)
                }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    private fun onShareDelete(share: UserCollectionShare) {
        val collection = _uiState.value.collection ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            unshareCollection(
                collectionId = collection.id,
                share = share,
            )
                .showSnackbarOnError()
                .onSuccess {
                    _eventChannel.send(CollectionShareEditEvent.Back)
                }
            _uiState.update { it.copy(isLoading = false) }
        }
    }
}
