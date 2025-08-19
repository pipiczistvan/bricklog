package hu.piware.bricklog.feature.collection.data.database

import androidx.room.Entity
import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.user.domain.model.UserId

@Entity(
    tableName = "collection_shares",
    primaryKeys = [
        "collectionId",
        "withUserId",
    ],
)
data class CollectionShareEntity(
    val collectionId: CollectionId,
    val withUserId: UserId,
    val canWrite: Boolean,
)
