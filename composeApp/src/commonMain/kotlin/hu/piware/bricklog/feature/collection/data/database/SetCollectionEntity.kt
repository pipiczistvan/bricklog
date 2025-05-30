package hu.piware.bricklog.feature.collection.data.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.set.data.database.SetEntity
import hu.piware.bricklog.feature.set.domain.model.SetId

@Entity(
    tableName = "set_collections",
    primaryKeys = [
        "setId",
        "collectionId"
    ],
    foreignKeys = [
        ForeignKey(
            entity = SetEntity::class,
            parentColumns = ["id"],
            childColumns = ["setId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CollectionEntity::class,
            parentColumns = ["id"],
            childColumns = ["collectionId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("collectionId")
    ]
)
data class SetCollectionEntity(
    val setId: SetId,
    val collectionId: CollectionId,
)
