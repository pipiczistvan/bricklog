package hu.piware.bricklog.feature.user.presentation.friend_edit

import hu.piware.bricklog.feature.user.domain.model.Friend

sealed interface FriendEditAction {
    data object OnBackClick : FriendEditAction
    data class OnFriendChange(val friend: Friend) : FriendEditAction
    data object OnFriendDelete : FriendEditAction
}
