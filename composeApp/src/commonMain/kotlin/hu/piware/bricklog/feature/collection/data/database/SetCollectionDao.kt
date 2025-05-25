package hu.piware.bricklog.feature.collection.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Upsert

@Dao
interface SetCollectionDao {

    @Delete
    suspend fun deleteSetCollection(setCollection: SetCollectionEntity)

    @Upsert
    suspend fun upsertSetCollection(setCollection: SetCollectionEntity)
}
