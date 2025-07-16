package hu.piware.bricklog.feature.collection.data.firebase

import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.collection.domain.model.CollectionId

fun CollectionDocument.toDomainModel(id: CollectionId): Collection {
    return Collection(
        id = id,
        name = name,
        icon = icon
    )
}

fun Collection.toDocument(): CollectionDocument {
    return CollectionDocument(
        name = name,
        icon = icon
    )
}
