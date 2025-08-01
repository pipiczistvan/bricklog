package hu.piware.bricklog.feature.collection.data.database

import androidx.room.Entity
import androidx.room.Index
import hu.piware.bricklog.feature.collection.domain.model.CollectionIcon
import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.collection.domain.model.CollectionType
import hu.piware.bricklog.feature.user.domain.model.UserId

@Entity(
    tableName = "collections",
    primaryKeys = [
        "id",
        "userId",
    ],
    indices = [
        Index("userId"),
    ],
)
data class CollectionEntity(
    val id: CollectionId,
    val userId: UserId,
    val name: String,
    val icon: CollectionIcon,
    val type: CollectionType,
)
