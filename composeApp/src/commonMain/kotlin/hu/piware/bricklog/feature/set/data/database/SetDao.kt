package hu.piware.bricklog.feature.set.data.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

@Dao
interface SetDao {

    @Upsert
    suspend fun upsertAll(sets: List<SetEntity>)

    @Query("SELECT COUNT(*) FROM sets")
    suspend fun getSetCount(): Int

    @Query("SELECT DISTINCT theme FROM sets WHERE theme IS NOT NULL ORDER BY theme ASC")
    fun watchThemes(): Flow<List<String>>

    @Query("SELECT DISTINCT packagingType FROM sets WHERE packagingType IS NOT NULL ORDER BY packagingType ASC")
    fun watchPackagingTypes(): Flow<List<String>>

    @Query("DELETE FROM sets WHERE lastUpdated > :date")
    suspend fun deleteSetsUpdatedAfter(date: Instant)

    @Query("SELECT * FROM sets ORDER BY lastUpdated DESC LIMIT 1")
    suspend fun getLastUpdatedSet(): SetEntity?
}
