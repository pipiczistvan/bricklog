package hu.piware.bricklog.feature.collection.domain.datasource

import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.set.domain.model.SetId
import hu.piware.bricklog.feature.user.domain.model.UserId
import kotlinx.coroutines.flow.Flow

interface RemoteCollectionDataSource {

    fun watchCollections(userId: UserId): Flow<List<Collection>>

    fun watchCollectionsBySets(userId: UserId): Flow<Map<SetId, List<CollectionId>>>

    suspend fun upsertCollections(
        userId: UserId,
        collections: List<Collection>,
    ): EmptyResult<DataError.Remote>

    suspend fun addSetToCollections(
        userId: UserId,
        setId: SetId,
        collectionIds: List<CollectionId>,
    ): EmptyResult<DataError.Remote>

    suspend fun deleteCollections(
        userId: UserId,
        collectionIds: List<CollectionId>,
    ): EmptyResult<DataError.Remote>

    suspend fun removeSetFromCollections(
        userId: UserId,
        setId: SetId,
        collectionIds: List<CollectionId>,
    ): EmptyResult<DataError.Remote>
}
