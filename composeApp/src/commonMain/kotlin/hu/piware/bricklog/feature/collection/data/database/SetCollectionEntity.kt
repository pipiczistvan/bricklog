package hu.piware.bricklog.feature.collection.data.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.set.domain.model.SetId
import hu.piware.bricklog.feature.user.domain.model.UserId

@Entity(
    tableName = "set_collections",
    primaryKeys = [
        "setId",
        "collectionId",
        "userId",
    ],
    foreignKeys = [
        ForeignKey(
            entity = CollectionEntity::class,
            parentColumns = ["id", "userId"],
            childColumns = ["collectionId", "userId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index("collectionId", "userId"),
    ],
)
data class SetCollectionEntity(
    val setId: SetId,
    val collectionId: CollectionId,
    val userId: UserId,
)
