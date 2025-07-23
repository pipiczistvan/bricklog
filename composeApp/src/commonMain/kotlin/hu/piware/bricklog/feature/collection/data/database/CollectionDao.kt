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

    @Query("SELECT * FROM collections WHERE userId = :userId AND id = :collectionId")
    fun watchCollection(userId: UserId, collectionId: CollectionId): Flow<CollectionEntity>

    @Query(
        """
        SELECT * FROM collections 
        WHERE userId = :userId 
        AND (:type IS NULL OR type = :type)
        AND (:setId IS NULL OR id IN (SELECT collectionId FROM set_collections WHERE setId = :setId))
"""
    )
    fun watchCollections(
        userId: UserId,
        type: CollectionType? = null,
        setId: SetId? = null,
    ): Flow<List<CollectionEntity>>

    @Upsert
    suspend fun upsertCollections(collections: List<CollectionEntity>)

    @Query("DELETE FROM collections WHERE userId = :userId")
    suspend fun deleteCollections(userId: UserId)

    @Query("DELETE FROM collections WHERE userId = :userId AND id IN (:collectionIds)")
    suspend fun deleteCollections(userId: UserId, collectionIds: List<CollectionId>)
}
