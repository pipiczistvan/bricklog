package hu.piware.bricklog.feature.set.data.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface SetImageDao {

    @Query("SELECT * FROM set_images WHERE setId = :setId")
    suspend fun getImages(setId: Int): List<SetImageEntity>

    @Upsert
    suspend fun upsertImages(images: List<SetImageEntity>)

    @Query("DELETE FROM set_images WHERE setId = :setId")
    suspend fun deleteImages(setId: Int)
}
