package hu.piware.bricklog.feature.collection.data.database

import hu.piware.bricklog.feature.collection.domain.model.Collection

fun CollectionEntity.toDomainModel(): Collection {
    return Collection(
        id = id,
        name = name,
        icon = icon
    )
}

fun Collection.toEntity(): CollectionEntity {
    return CollectionEntity(
        id = id,
        name = name,
        icon = icon
    )
}
