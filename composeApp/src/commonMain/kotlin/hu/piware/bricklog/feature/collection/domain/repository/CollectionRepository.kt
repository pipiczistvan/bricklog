package hu.piware.bricklog.feature.collection.domain.repository

import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.collection.domain.model.CollectionType
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.set.domain.model.SetId
import hu.piware.bricklog.feature.user.domain.model.UserId
import kotlinx.coroutines.flow.Flow

interface CollectionRepository {

    fun watchCollection(collectionId: CollectionId): Flow<Collection?>

    fun watchCollections(
        userId: UserId,
        type: CollectionType?,
        setId: SetId?,
    ): Flow<List<Collection>>

    suspend fun saveCollections(
        userId: UserId,
        collections: List<Collection>,
    ): EmptyResult<DataError>

    suspend fun addSetToCollections(
        userId: UserId,
        setId: SetId,
        collectionIds: List<CollectionId>,
    ): EmptyResult<DataError>

    suspend fun deleteCollections(
        userId: UserId,
        collectionIds: List<CollectionId>,
    ): EmptyResult<DataError>

    suspend fun removeSetFromCollections(
        userId: UserId,
        setId: SetId,
        collectionIds: List<CollectionId>,
    ): EmptyResult<DataError>

    suspend fun forceSyncRemoteCollections(userId: UserId): EmptyResult<DataError>
}
