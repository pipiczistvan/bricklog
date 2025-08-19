package hu.piware.bricklog.feature.collection.domain.util

import hu.piware.bricklog.feature.collection.domain.model.CollectionIcon
import hu.piware.bricklog.feature.collection.domain.model.CollectionType

enum class DefaultCollections(
    val collectionName: String,
    val icon: CollectionIcon,
    val type: CollectionType,
) {
    FAVOURITE(
        collectionName = "Favourite Sets",
        icon = CollectionIcon.FAVOURITE,
        type = CollectionType.FAVOURITE,
    ),
}
