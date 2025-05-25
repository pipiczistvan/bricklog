package hu.piware.bricklog.feature.collection.domain.util

import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.collection.domain.model.CollectionIcon
import hu.piware.bricklog.feature.collection.domain.model.CollectionId

val COLLECTION_ID_FAVOURITE_SETS: CollectionId = 1

val defaultCollections = listOf(
    Collection(
        id = COLLECTION_ID_FAVOURITE_SETS,
        name = "Favourite Sets",
        icon = CollectionIcon.STAR
    )
)
