package hu.piware.bricklog.feature.user.presentation.friend_list

import hu.piware.bricklog.feature.user.domain.model.UserId

sealed interface FriendListAction {
    data object OnBackClick : FriendListAction
    data class OnFriendEditClick(val friendId: UserId?) : FriendListAction
}
