package hu.piware.bricklog.feature.collection.data.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.collection.domain.model.CollectionType
import hu.piware.bricklog.feature.set.domain.model.SetId
import hu.piware.bricklog.feature.user.domain.model.UserId
import kotlinx.coroutines.flow.Flow

@Dao
interface CollectionDao {

    @Query("SELECT * FROM collections WHERE userId = :userId")
    fun watchUserCollections(userId: UserId): Flow<List<CollectionEntity>>

    @Query("SELECT * FROM collections WHERE userId = :userId AND type = :type")
    suspend fun getUserCollectionsByType(
        userId: UserId,
        type: CollectionType,
    ): List<CollectionEntity>

    @Query(
        "SELECT collections.*, set_collections.setId FROM collections " +
                "JOIN set_collections ON collections.id = set_collections.collectionId AND collections.userId = set_collections.userId " +
                "WHERE collections.userId = :userId"
    )
    fun watchUserCollectionsWithSetIds(userId: UserId): Flow<List<CollectionWithSetId>>

    @Query(
        "SELECT collections.*, set_collections.setId FROM collections " +
                "JOIN set_collections ON collections.id = set_collections.collectionId AND collections.userId = set_collections.userId " +
                "WHERE collections.userId = :userId"
    )
    suspend fun getUserCollectionsWithSetIds(userId: UserId): List<CollectionWithSetId>

    @Query(
        "SELECT collections.* FROM collections " +
                "JOIN set_collections ON collections.id = set_collections.collectionId AND collections.userId = set_collections.userId " +
                "WHERE collections.userId = :userId AND set_collections.setId = :setId"
    )
    fun watchUserCollectionsBySet(userId: UserId, setId: SetId): Flow<List<CollectionEntity>>

    @Query("DELETE FROM collections WHERE userId = :userId AND id = :collectionId")
    suspend fun deleteUserCollectionById(userId: UserId, collectionId: CollectionId)

    @Query("DELETE FROM collections WHERE userId = :userId AND id IN (:collectionIds)")
    suspend fun deleteUserCollectionsById(userId: UserId, collectionIds: List<CollectionId>)

    @Query("DELETE FROM collections WHERE userId = :userId")
    suspend fun deleteAllUserCollections(userId: UserId)

    @Upsert
    suspend fun upsertCollection(collection: CollectionEntity)

    @Upsert
    suspend fun upsertCollections(collections: List<CollectionEntity>)

    @Query("SELECT * FROM collections WHERE userId = :userId AND id = :collectionId")
    suspend fun getUserCollectionById(userId: UserId, collectionId: CollectionId): CollectionEntity

    @Query("SELECT * FROM collections WHERE userId = :userId")
    suspend fun getUserCollections(userId: UserId): List<CollectionEntity>

    @Query("SELECT * FROM collections WHERE userId = :userId AND id = :collectionId")
    fun watchUserCollection(userId: UserId, collectionId: CollectionId): Flow<CollectionEntity>
}
