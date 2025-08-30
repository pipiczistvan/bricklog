package hu.piware.bricklog.feature.collection.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import hu.piware.bricklog.feature.collection.domain.model.CollectionId

@Dao
interface CollectionShareDao {

    @Upsert
    suspend fun upsertCollectionShares(shares: List<CollectionShareEntity>)

    @Query("DELETE FROM collection_shares WHERE collectionId IN (:collectionIds)")
    suspend fun deleteCollectionShares(collectionIds: List<CollectionId>)

    @Delete
    suspend fun deleteCollectionShare(share: CollectionShareEntity)
}
