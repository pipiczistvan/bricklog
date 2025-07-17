package hu.piware.bricklog.feature.collection.domain.datasource

import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.set.domain.model.SetId
import kotlinx.coroutines.flow.Flow

interface LocalCollectionDataSource {
    fun watchCollections(): Flow<List<Collection>>
    fun watchCollectionsBySets(): Flow<Map<SetId, List<Collection>>>
    fun watchCollectionsBySet(setId: SetId): Flow<List<Collection>>
    suspend fun getSetCollections(): Result<Map<SetId, List<Collection>>, DataError.Local>
    suspend fun deleteCollectionById(id: CollectionId): EmptyResult<DataError.Local>
    suspend fun upsertCollection(collection: Collection): EmptyResult<DataError.Local>
    suspend fun addSetToCollection(
        setId: SetId,
        collectionId: CollectionId,
    ): EmptyResult<DataError.Local>

    suspend fun addSetToCollections(
        setId: SetId,
        collectionIds: List<CollectionId>,
    ): EmptyResult<DataError.Local>

    suspend fun removeSetFromCollection(
        setId: SetId,
        collectionId: CollectionId,
    ): EmptyResult<DataError.Local>

    suspend fun removeSetFromCollections(
        setId: SetId,
        collectionIds: List<CollectionId>,
    ): EmptyResult<DataError.Local>

    suspend fun deleteSetCollections(setCollections: Map<SetId, List<CollectionId>>): EmptyResult<DataError.Local>
    suspend fun upsertSetCollections(setCollections: Map<SetId, List<CollectionId>>): EmptyResult<DataError.Local>

    suspend fun getCollection(id: CollectionId): Result<Collection, DataError.Local>
    suspend fun getCollections(): Result<List<Collection>, DataError.Local>
    suspend fun deleteCollection(id: CollectionId): EmptyResult<DataError.Local>
    suspend fun deleteCollections(ids: List<CollectionId>): EmptyResult<DataError.Local>
    suspend fun deleteAllCollections(): EmptyResult<DataError.Local>
    suspend fun upsertCollections(collections: List<Collection>): EmptyResult<DataError.Local>
    fun watchCollection(id: CollectionId): Flow<Collection>
}
