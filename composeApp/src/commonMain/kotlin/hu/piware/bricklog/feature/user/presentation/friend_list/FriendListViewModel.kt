package hu.piware.bricklog.feature.user.presentation.friend_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.piware.bricklog.feature.core.presentation.asStateFlowIn
import hu.piware.bricklog.feature.core.presentation.showSnackbarOnError
import hu.piware.bricklog.feature.user.domain.model.Friend
import hu.piware.bricklog.feature.user.domain.usecase.DeleteFriends
import hu.piware.bricklog.feature.user.domain.usecase.SaveFriends
import hu.piware.bricklog.feature.user.domain.usecase.WatchFriends
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class FriendListViewModel(
    private val watchFriends: WatchFriends,
    private val saveFriends: SaveFriends,
    private val deleteFriends: DeleteFriends,
) : ViewModel() {

    private val _uiState = MutableStateFlow(FriendListState())
    val uiState = _uiState.asStateFlowIn(viewModelScope) {
        observeFriends()
    }

    fun onAction(action: FriendListAction) {
        when (action) {
            is FriendListAction.OnFriendChange -> onSaveFriend(action.friend)
            is FriendListAction.OnFriendDelete -> onDeleteFriend(action.friend)
            else -> Unit
        }
    }

    private fun observeFriends() {
        watchFriends()
            .onEach { friends ->
                _uiState.update { it.copy(friends = friends) }
            }
            .launchIn(viewModelScope)
    }

    private fun onSaveFriend(friend: Friend) {
        viewModelScope.launch {
            saveFriends(friend)
                .showSnackbarOnError()
        }
    }

    private fun onDeleteFriend(friend: Friend) {
        viewModelScope.launch {
            deleteFriends(friend)
                .showSnackbarOnError()
        }
    }
}
