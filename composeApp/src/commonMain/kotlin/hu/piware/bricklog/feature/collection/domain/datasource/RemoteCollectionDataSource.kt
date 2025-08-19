package hu.piware.bricklog.feature.collection.domain.datasource

import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.set.domain.model.SetId
import hu.piware.bricklog.feature.user.domain.model.UserId
import kotlinx.coroutines.flow.Flow

interface RemoteCollectionDataSource {

    fun watchUserAndSharedCollections(userId: UserId): Flow<List<Collection>>

    fun watchUserAndSharedCollectionsBySets(userId: UserId): Flow<Map<SetId, List<CollectionId>>>

    suspend fun upsertCollections(
        collections: List<Collection>,
    ): EmptyResult<DataError.Remote>

    suspend fun addSetToCollections(
        setId: SetId,
        collectionIds: List<CollectionId>,
    ): EmptyResult<DataError.Remote>

    suspend fun deleteCollections(
        collectionIds: List<CollectionId>,
    ): EmptyResult<DataError.Remote>

    suspend fun removeSetFromCollections(
        setId: SetId,
        collectionIds: List<CollectionId>,
    ): EmptyResult<DataError.Remote>
}
