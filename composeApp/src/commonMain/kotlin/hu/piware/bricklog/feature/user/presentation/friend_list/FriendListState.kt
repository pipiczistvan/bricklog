package hu.piware.bricklog.feature.user.presentation.friend_list

import hu.piware.bricklog.feature.user.domain.model.Friend

data class FriendListState(
    val friends: List<Friend> = emptyList(),
)
