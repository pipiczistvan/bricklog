package hu.piware.bricklog.feature.user.presentation.friend_list

import hu.piware.bricklog.feature.user.domain.model.Friend

sealed interface FriendListAction {
    data object OnBackClick : FriendListAction
    data class OnFriendEditClick(val friend: Friend?) : FriendListAction
    data object OnUserScannerClick : FriendListAction
}
