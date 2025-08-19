package hu.piware.bricklog.feature.collection.data.database

import androidx.room.Embedded
import androidx.room.Relation

data class CollectionWithShares(
    @Embedded val collection: CollectionEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "collectionId",
    )
    val shares: List<CollectionShareEntity>,
)
