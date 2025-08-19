@file:OptIn(ExperimentalUuidApi::class)

package hu.piware.bricklog.feature.collection.data.database

import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.collection.domain.model.CollectionShare
import hu.piware.bricklog.feature.collection.domain.model.isNew
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

fun CollectionWithShares.toDomainModel(): Collection {
    return Collection(
        id = collection.id,
        owner = collection.owner,
        name = collection.name,
        icon = collection.icon,
        type = collection.type,
        shares = shares.associate { it.withUserId to CollectionShare(it.canWrite) },
    )
}

fun CollectionEntity.toDomainModel(shares: List<CollectionShareEntity>): Collection {
    return Collection(
        id = id,
        owner = owner,
        name = name,
        icon = icon,
        type = type,
        shares = shares.associate { it.withUserId to CollectionShare(it.canWrite) },
    )
}

fun Collection.toEntity(): CollectionWithShares {
    val id = if (isNew) Uuid.random().toString() else id

    return CollectionWithShares(
        collection = CollectionEntity(
            id = id,
            owner = owner,
            name = name,
            icon = icon,
            type = type,
        ),
        shares = shares.map {
            CollectionShareEntity(
                collectionId = id,
                withUserId = it.key,
                canWrite = it.value.canWrite,
            )
        },
    )
}
