package hu.piware.bricklog.feature.user.presentation.friend_edit

import hu.piware.bricklog.feature.user.domain.model.UserId
import kotlinx.serialization.Serializable

@Serializable
data class FriendEditArguments(
    val friendId: UserId?,
)
