package hu.piware.bricklog.feature.collection.presentation.collection_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.piware.bricklog.feature.collection.domain.usecase.SaveCollectionOrder
import hu.piware.bricklog.feature.collection.domain.usecase.WatchCollectionSetDetails
import hu.piware.bricklog.feature.core.presentation.asStateFlowIn
import hu.piware.bricklog.feature.core.presentation.showSnackbarOnError
import hu.piware.bricklog.feature.user.domain.usecase.WatchCurrentUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class CollectionListViewModel(
    private val watchCollectionSetDetails: WatchCollectionSetDetails,
    private val watchCurrentUser: WatchCurrentUser,
    private val saveCollectionOrder: SaveCollectionOrder,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CollectionListState())
    val uiState = _uiState.asStateFlowIn(viewModelScope) {
        observeCollections()
        observeCurrentUser()
    }

    fun onAction(action: CollectionListAction) {
        when (action) {
            is CollectionListAction.OnCollectionOrderChange -> {
                viewModelScope.launch {
                    _uiState.update {
                        it.copy(
                            collections = action.collections,
                        )
                    }

                    saveCollectionOrder(action.collections.map { it.collection.collection.id })
                        .showSnackbarOnError()
                }
            }

            else -> Unit
        }
    }

    private fun observeCollections() {
        watchCollectionSetDetails(
            setLimit = 4,
        ).onEach { collectionSetDetails ->
            _uiState.value = _uiState.value.copy(
                collections = collectionSetDetails,
            )
        }.launchIn(viewModelScope)
    }

    private fun observeCurrentUser() {
        watchCurrentUser().onEach { user ->
            _uiState.value = _uiState.value.copy(
                currentUser = user,
            )
        }.launchIn(viewModelScope)
    }
}
