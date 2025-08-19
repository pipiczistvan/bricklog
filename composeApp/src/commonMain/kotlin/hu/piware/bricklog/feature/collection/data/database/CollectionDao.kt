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

    @Query("SELECT * FROM collections WHERE id = :collectionId")
    fun watchCollection(collectionId: CollectionId): Flow<CollectionWithShares?>

    @Query(
        """
        SELECT collections.* FROM collections 
        LEFT JOIN collection_shares ON collections.id = collection_shares.collectionId AND collection_shares.withUserId = :userId
        WHERE (collections.owner = :userId OR collection_shares.withUserId = :userId)
        AND (:type IS NULL OR collections.type = :type)
        AND (:setId IS NULL OR collections.id IN (
            SELECT collectionId FROM collection_sets 
            WHERE setId = :setId 
        ))
""",
    )
    fun watchUserAndSharedCollections(
        userId: UserId,
        type: CollectionType? = null,
        setId: SetId? = null,
    ): Flow<List<CollectionWithShares>>

    @Upsert
    suspend fun upsertCollections(collections: List<CollectionEntity>)

    @Query("DELETE FROM collection_shares WHERE collectionId IN (:collectionIds)")
    suspend fun deleteCollectionShares(collectionIds: List<CollectionId>)

    @Upsert
    suspend fun upsertCollectionShares(shares: List<CollectionShareEntity>)

    @Query("DELETE FROM collections WHERE owner = :userId")
    suspend fun deleteUserCollections(userId: UserId)

    @Query("DELETE FROM collections WHERE id IN (:collectionIds)")
    suspend fun deleteCollections(collectionIds: List<CollectionId>)
}
