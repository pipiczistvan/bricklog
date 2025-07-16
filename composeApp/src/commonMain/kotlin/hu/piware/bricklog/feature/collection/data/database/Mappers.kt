@file:OptIn(ExperimentalUuidApi::class)

package hu.piware.bricklog.feature.collection.data.database

import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.collection.domain.model.isNew
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

fun CollectionEntity.toDomainModel(): Collection {
    return Collection(
        id = id.toString(),
        name = name,
        icon = icon
    )
}

fun Collection.toEntity(): CollectionEntity {
    return CollectionEntity(
        id = if (isNew) Uuid.random().toString() else id,
        name = name,
        icon = icon
    )
}
