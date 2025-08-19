package hu.piware.bricklog.feature.set.data.database

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import hu.piware.bricklog.feature.collection.data.database.CollectionEntity
import hu.piware.bricklog.feature.collection.data.database.CollectionSetEntity
import hu.piware.bricklog.feature.collection.data.database.CollectionShareEntity

data class SetDetailsWithCollections(
    @Embedded val detailsView: SetDetailsView,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = CollectionSetEntity::class,
            parentColumn = "setId",
            entityColumn = "collectionId",
        ),
    )
    val collections: List<CollectionEntity>,
    @Relation(
        parentColumn = "id",
        entityColumn = "collectionId",
        associateBy = Junction(
            value = CollectionSetEntity::class,
            parentColumn = "setId",
            entityColumn = "collectionId",
        ),
    )
    val collectionShares: List<CollectionShareEntity>,
)
