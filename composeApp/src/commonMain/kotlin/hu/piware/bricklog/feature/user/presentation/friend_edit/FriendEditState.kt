package hu.piware.bricklog.feature.user.presentation.friend_edit

import hu.piware.bricklog.feature.user.domain.model.Friend

data class FriendEditState(
    val isLoading: Boolean = false,
    val friend: Friend? = null,
    val friendIdentifierArg: String = "",
)

val FriendEditState.isNew: Boolean
    get() = friend == null
