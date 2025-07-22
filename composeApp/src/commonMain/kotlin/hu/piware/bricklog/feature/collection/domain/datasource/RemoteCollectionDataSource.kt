package hu.piware.bricklog.feature.collection.domain.datasource

import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.set.domain.model.SetId
import hu.piware.bricklog.feature.user.domain.model.UserId
import kotlinx.coroutines.flow.Flow

interface RemoteCollectionDataSource {

    fun watchUserCollections(userId: UserId): Flow<List<Collection>>

    fun watchUserSetCollections(userId: UserId): Flow<Map<SetId, List<CollectionId>>>

    suspend fun deleteUserCollection(
        userId: UserId,
        collectionId: CollectionId,
    ): EmptyResult<DataError.Remote>

    suspend fun upsertUserCollection(
        userId: UserId,
        collection: Collection,
    ): EmptyResult<DataError.Remote>

    suspend fun addSetToUserCollection(
        userId: UserId,
        setId: SetId,
        collectionId: CollectionId,
    ): EmptyResult<DataError.Remote>

    suspend fun removeSetFromUserCollection(
        userId: UserId,
        setId: SetId,
        collectionId: CollectionId,
    ): EmptyResult<DataError.Remote>
}
