package hu.piware.bricklog.feature.collection.domain.datasource

import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.collection.domain.model.CollectionType
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.set.domain.model.SetId
import hu.piware.bricklog.feature.user.domain.model.UserId
import kotlinx.coroutines.flow.Flow

interface LocalCollectionDataSource {
    fun watchUserCollections(userId: UserId): Flow<List<Collection>>
    suspend fun getUserCollectionsByType(
        userId: UserId,
        type: CollectionType,
    ): Result<List<Collection>, DataError.Local>

    fun watchUserCollectionsBySets(userId: UserId): Flow<Map<SetId, List<Collection>>>
    fun watchUserCollectionsBySet(userId: UserId, setId: SetId): Flow<List<Collection>>
    suspend fun getUserSetCollections(userId: UserId): Result<Map<SetId, List<Collection>>, DataError.Local>
    suspend fun deleteUserCollectionById(
        userId: UserId,
        collectionId: CollectionId,
    ): EmptyResult<DataError.Local>

    suspend fun upsertUserCollection(
        userId: UserId,
        collection: Collection,
    ): EmptyResult<DataError.Local>

    suspend fun addSetToUserCollection(
        setId: SetId,
        userId: UserId,
        collectionId: CollectionId,
    ): EmptyResult<DataError.Local>

    suspend fun addSetToUserCollections(
        setId: SetId,
        userId: UserId,
        collectionIds: List<CollectionId>,
    ): EmptyResult<DataError.Local>

    suspend fun removeSetFromUserCollection(
        setId: SetId,
        userId: UserId,
        collectionId: CollectionId,
    ): EmptyResult<DataError.Local>

    suspend fun removeSetFromUserCollections(
        setId: SetId,
        userId: UserId,
        collectionIds: List<CollectionId>,
    ): EmptyResult<DataError.Local>

    suspend fun deleteUserSetCollections(
        userId: UserId,
        setCollections: Map<SetId, List<CollectionId>>,
    ): EmptyResult<DataError.Local>

    suspend fun upsertUserSetCollections(
        userId: UserId,
        setCollections: Map<SetId, List<CollectionId>>,
    ): EmptyResult<DataError.Local>

    suspend fun getUserCollection(
        userId: UserId,
        collectionId: CollectionId,
    ): Result<Collection, DataError.Local>

    suspend fun getUserCollections(userId: UserId): Result<List<Collection>, DataError.Local>
    suspend fun deleteUserCollection(
        userId: UserId,
        collectionId: CollectionId,
    ): EmptyResult<DataError.Local>

    suspend fun deleteUserCollections(
        userId: UserId,
        collectionIds: List<CollectionId>,
    ): EmptyResult<DataError.Local>

    suspend fun deleteAllUserCollections(userId: UserId): EmptyResult<DataError.Local>
    suspend fun upsertUserCollections(
        userId: UserId,
        collections: List<Collection>,
    ): EmptyResult<DataError.Local>

    fun watchUserCollection(userId: UserId, collectionId: CollectionId): Flow<Collection>
}
