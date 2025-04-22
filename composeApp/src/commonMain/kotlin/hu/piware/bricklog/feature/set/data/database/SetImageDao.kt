package hu.piware.bricklog.feature.set.data.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface SetImageDao {

    @Upsert
    suspend fun upsertAll(images: List<SetImageEntity>)

    @Query("DELETE FROM set_images WHERE setId = :setId")
    suspend fun deleteImages(setId: Int)

    @Query("SELECT * FROM set_images WHERE setId = :setId")
    suspend fun getImages(setId: Int): List<SetImageEntity>
}
