package hu.piware.bricklog.feature.user.data.database

import androidx.room.Entity
import hu.piware.bricklog.feature.user.domain.model.UserId

@Entity(
    tableName = "friends",
    primaryKeys = [
        "userId",
        "friendId",
    ],
)
data class FriendEntity(
    val userId: UserId,
    val friendId: UserId,
    val friendName: String,
)
