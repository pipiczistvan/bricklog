package hu.piware.bricklog.feature.user.data.database

import androidx.room.Entity
import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.user.domain.model.UserId

@Entity(
    tableName = "user_preferences",
    primaryKeys = [
        "userId"
    ]
)
data class UserPreferencesEntity(
    val userId: UserId,
    val showGreetings: Boolean,
    val displayName: String?,
    val collectionOrder: List<CollectionId>,
)
