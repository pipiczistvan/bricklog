package hu.piware.bricklog.feature.collection.data.firebase

import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.collection.domain.model.SharePermissions

fun CollectionDocument.toDomainModel(id: CollectionId): Collection {
    return Collection(
        id = id,
        owner = owner,
        name = name,
        icon = icon,
        type = type,
        shares = shares.mapValues { it.value.toDomainModel() },
    )
}

fun CollectionShareDocument.toDomainModel(): SharePermissions {
    return SharePermissions(
        canWrite = canWrite,
    )
}

fun Collection.toDocument(): CollectionDocument {
    return CollectionDocument(
        owner = owner,
        name = name,
        icon = icon,
        type = type,
        shares = shares.mapValues { it.value.toDocument() },
        sharedWith = shares.keys.toList(),
    )
}

fun SharePermissions.toDocument(): CollectionShareDocument {
    return CollectionShareDocument(
        canWrite = canWrite,
    )
}
