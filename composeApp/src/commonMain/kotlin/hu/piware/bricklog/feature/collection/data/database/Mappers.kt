@file:OptIn(ExperimentalUuidApi::class)

package hu.piware.bricklog.feature.collection.data.database

import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.collection.domain.model.isNew
import hu.piware.bricklog.feature.user.domain.model.UserId
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

fun CollectionEntity.toDomainModel(): Collection {
    return Collection(
        id = id,
        name = name,
        icon = icon,
        type = type,
    )
}

fun Collection.toEntity(userId: UserId): CollectionEntity {
    return CollectionEntity(
        id = if (isNew) Uuid.random().toString() else id,
        userId = userId,
        name = name,
        icon = icon,
        type = type,
    )
}
