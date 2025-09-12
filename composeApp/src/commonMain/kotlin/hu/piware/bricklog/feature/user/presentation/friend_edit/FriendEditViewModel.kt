package hu.piware.bricklog.feature.user.presentation.friend_edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import hu.piware.bricklog.feature.core.domain.data
import hu.piware.bricklog.feature.core.domain.onError
import hu.piware.bricklog.feature.core.domain.onSuccess
import hu.piware.bricklog.feature.core.presentation.asStateFlowIn
import hu.piware.bricklog.feature.core.presentation.navigation.CustomNavType
import hu.piware.bricklog.feature.core.presentation.showSnackbarOnError
import hu.piware.bricklog.feature.user.domain.model.Friend
import hu.piware.bricklog.feature.user.domain.usecase.DeleteFriends
import hu.piware.bricklog.feature.user.domain.usecase.GetFriend
import hu.piware.bricklog.feature.user.domain.usecase.SaveFriends
import hu.piware.bricklog.feature.user.presentation.UserRoute
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.reflect.typeOf

class FriendEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val getFriend: GetFriend,
    private val saveFriends: SaveFriends,
    private val deleteFriends: DeleteFriends,
) : ViewModel() {

    private val arguments = savedStateHandle.toRoute<UserRoute.FriendEditScreen>(
        typeMap = mapOf(typeOf<FriendEditArguments>() to CustomNavType.FriendEditArgumentsType),
    ).arguments

    private val _uiState = MutableStateFlow(FriendEditState())
    val uiState = _uiState.asStateFlowIn(viewModelScope) {
        _uiState.update {
            it.copy(
                friendIdentifierArg = arguments.userId ?: "",
                friendNameArg = arguments.userName ?: "",
                isNew = arguments.isNew,
            )
        }
    }

    private val _eventChannel = Channel<FriendEditEvent>()
    val eventChannel = _eventChannel.receiveAsFlow()

    fun onAction(action: FriendEditAction) {
        when (action) {
            is FriendEditAction.OnFriendChange -> saveFriend(action.friend)
            is FriendEditAction.OnFriendDelete -> deleteFriend()
            else -> Unit
        }
    }

    private fun saveFriend(friend: Friend) {
        viewModelScope.launch {
            saveFriends(friend)
                .showSnackbarOnError()
                .onSuccess {
                    _eventChannel.send(FriendEditEvent.Back)
                }
        }
    }

    private fun deleteFriend() {
        viewModelScope.launch {
            val friend = getFriend(_uiState.value.friendIdentifierArg)
                .showSnackbarOnError()
                .onError { return@launch }
                .data() ?: return@launch

            deleteFriends(friend)
                .showSnackbarOnError()
                .onSuccess {
                    _eventChannel.send(FriendEditEvent.Back)
                }
        }
    }
}
