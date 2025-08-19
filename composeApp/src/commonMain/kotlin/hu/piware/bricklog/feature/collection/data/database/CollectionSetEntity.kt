package hu.piware.bricklog.feature.collection.data.database

import androidx.room.Entity
import androidx.room.ForeignKey
import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.set.domain.model.SetId

@Entity(
    tableName = "collection_sets",
    primaryKeys = [
        "collectionId",
        "setId",
    ],
    foreignKeys = [
        ForeignKey(
            entity = CollectionEntity::class,
            parentColumns = ["id"],
            childColumns = ["collectionId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
)
data class CollectionSetEntity(
    val setId: SetId,
    val collectionId: CollectionId,
)
