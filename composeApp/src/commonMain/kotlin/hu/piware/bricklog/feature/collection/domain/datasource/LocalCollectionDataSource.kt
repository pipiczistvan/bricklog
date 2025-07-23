package hu.piware.bricklog.feature.collection.domain.datasource

import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.collection.domain.model.CollectionType
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.set.domain.model.SetId
import hu.piware.bricklog.feature.user.domain.model.UserId
import kotlinx.coroutines.flow.Flow

interface LocalCollectionDataSource {

    fun watchCollection(userId: UserId, collectionId: CollectionId): Flow<Collection>

    fun watchCollections(
        userId: UserId,
        type: CollectionType? = null,
        setId: SetId? = null,
    ): Flow<List<Collection>>

    fun watchCollectionsBySets(userId: UserId): Flow<Map<SetId, List<Collection>>>

    suspend fun upsertCollections(
        userId: UserId,
        collections: List<Collection>,
    ): EmptyResult<DataError.Local>

    suspend fun upsertCollectionsBySets(
        userId: UserId,
        setCollections: Map<SetId, List<CollectionId>>,
    ): EmptyResult<DataError.Local>

    suspend fun addSetToCollections(
        setId: SetId,
        userId: UserId,
        collectionIds: List<CollectionId>,
    ): EmptyResult<DataError.Local>

    suspend fun deleteCollections(
        userId: UserId,
        collectionIds: List<CollectionId> = emptyList(),
    ): EmptyResult<DataError.Local>

    suspend fun deleteCollectionsBySets(
        userId: UserId,
        setCollections: Map<SetId, List<CollectionId>>,
    ): EmptyResult<DataError.Local>

    suspend fun removeSetFromCollections(
        setId: SetId,
        userId: UserId,
        collectionIds: List<CollectionId>,
    ): EmptyResult<DataError.Local>
}
