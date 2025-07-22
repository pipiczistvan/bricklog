package hu.piware.bricklog.feature.collection.domain.util

import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.collection.domain.model.CollectionIcon
import hu.piware.bricklog.feature.collection.domain.model.CollectionType

val defaultCollections = listOf(
    Collection(
        name = "Favourite Sets",
        icon = CollectionIcon.FAVOURITE,
        type = CollectionType.FAVOURITE
    )
)
