package hu.piware.bricklog.feature.collection.domain.repository

import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.collection.domain.model.CollectionType
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.set.domain.model.SetId
import kotlinx.coroutines.flow.Flow

interface CollectionRepository {
    fun watchCollectionsBySet(setId: SetId): Flow<List<Collection>>
    fun watchCollectionsBySets(): Flow<Map<SetId, List<Collection>>>
    fun watchCollections(): Flow<List<Collection>>
    suspend fun deleteCollectionById(collectionId: CollectionId): EmptyResult<DataError>
    suspend fun saveCollection(collection: Collection): Result<Collection, DataError>
    suspend fun addSetToCollection(setId: SetId, collectionId: CollectionId): EmptyResult<DataError>
    suspend fun removeSetFromCollection(
        setId: SetId,
        collectionId: CollectionId,
    ): EmptyResult<DataError>

    suspend fun getCollection(collectionId: CollectionId): Result<Collection, DataError>
    fun watchCollection(collectionId: CollectionId): Flow<Collection>
    suspend fun getUserCollectionsByType(type: CollectionType): Result<List<Collection>, DataError>
}
