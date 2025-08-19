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

    fun watchCollection(collectionId: CollectionId): Flow<Collection?>

    fun watchUserAndSharedCollections(
        userId: UserId,
        type: CollectionType? = null,
        setId: SetId? = null,
    ): Flow<List<Collection>>

    fun watchUserAndSharedCollectionsBySets(userId: UserId): Flow<Map<SetId, List<Collection>>>

    suspend fun upsertCollections(
        collections: List<Collection>,
    ): EmptyResult<DataError.Local>

    suspend fun upsertCollectionsBySets(
        setCollections: Map<SetId, List<CollectionId>>,
    ): EmptyResult<DataError.Local>

    suspend fun addSetToCollections(
        setId: SetId,
        collectionIds: List<CollectionId>,
    ): EmptyResult<DataError.Local>

    suspend fun deleteUserCollections(userId: UserId): EmptyResult<DataError.Local>

    suspend fun deleteCollections(
        collectionIds: List<CollectionId>,
    ): EmptyResult<DataError.Local>

    suspend fun deleteCollectionsBySets(
        setCollections: Map<SetId, List<CollectionId>>,
    ): EmptyResult<DataError.Local>

    suspend fun removeSetFromCollections(
        setId: SetId,
        collectionIds: List<CollectionId>,
    ): EmptyResult<DataError.Local>
}
