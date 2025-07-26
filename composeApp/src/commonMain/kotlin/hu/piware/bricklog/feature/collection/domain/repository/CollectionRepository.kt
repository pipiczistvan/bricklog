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

    fun watchCollection(collectionId: CollectionId): Flow<Collection>

    fun watchCollections(type: CollectionType? = null, setId: SetId? = null): Flow<List<Collection>>

    fun watchCollectionsBySets(): Flow<Map<SetId, List<Collection>>>

    suspend fun saveCollections(collections: List<Collection>): Result<List<CollectionId>, DataError>

    suspend fun addSetToCollections(
        setId: SetId,
        collectionIds: List<CollectionId>,
    ): EmptyResult<DataError>

    suspend fun deleteCollections(collectionIds: List<CollectionId>): EmptyResult<DataError>

    suspend fun removeSetFromCollections(
        setId: SetId,
        collectionIds: List<CollectionId>,
    ): EmptyResult<DataError>
}
