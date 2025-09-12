package hu.piware.bricklog.feature.user.presentation.friend_edit

data class FriendEditState(
    val isLoading: Boolean = false,
    val friendIdentifierArg: String = "",
    val friendNameArg: String = "",
    val isNew: Boolean = false,
)
