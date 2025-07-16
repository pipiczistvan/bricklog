package hu.piware.bricklog.feature.collection.domain.datasource

import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.set.domain.model.SetId
import kotlinx.coroutines.flow.Flow

interface RemoteCollectionDataSource {

    fun watchCollections(userId: String): Flow<List<Collection>>

    fun watchSetCollections(userId: String): Flow<Map<SetId, List<Collection>>>

    suspend fun deleteCollection(userId: String, id: CollectionId): EmptyResult<DataError.Remote>

    suspend fun upsertCollection(
        userId: String,
        collection: Collection,
    ): EmptyResult<DataError.Remote>

    suspend fun addSetToCollection(
        userId: String,
        setId: SetId,
        collectionId: CollectionId,
    ): EmptyResult<DataError.Remote>

    suspend fun removeSetFromCollection(
        userId: String,
        setId: SetId,
        collectionId: CollectionId,
    ): EmptyResult<DataError.Remote>
}
