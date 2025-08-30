package hu.piware.bricklog.feature.user.presentation.friend_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.piware.bricklog.feature.core.presentation.asStateFlowIn
import hu.piware.bricklog.feature.user.domain.usecase.WatchFriends
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class FriendListViewModel(
    private val watchFriends: WatchFriends,
) : ViewModel() {

    private val _uiState = MutableStateFlow(FriendListState())
    val uiState = _uiState.asStateFlowIn(viewModelScope) {
        observeFriends()
    }

    private fun observeFriends() {
        watchFriends()
            .onEach { friends ->
                _uiState.update { it.copy(friends = friends) }
            }
            .launchIn(viewModelScope)
    }
}
