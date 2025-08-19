package hu.piware.bricklog.feature.collection.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Upsert

@Dao
interface CollectionSetDao {

    @Delete
    suspend fun deleteCollectionSets(collectionSets: List<CollectionSetEntity>)

    @Upsert
    suspend fun upsertCollectionSets(collectionSets: List<CollectionSetEntity>)
}
