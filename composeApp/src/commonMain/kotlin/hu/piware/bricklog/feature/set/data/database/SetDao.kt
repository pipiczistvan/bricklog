package hu.piware.bricklog.feature.set.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.RoomRawQuery
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

@Dao
interface SetDao {

    @Upsert
    suspend fun upsertAll(sets: List<SetEntity>)

    @RawQuery(observedEntities = [SetEntity::class])
    fun watchSets(query: RoomRawQuery): Flow<List<SetEntity>>

    @Query("SELECT * FROM sets WHERE id = :id")
    fun watchSet(id: Int): Flow<SetEntity?>

    @RawQuery(observedEntities = [SetEntity::class])
    suspend fun getSets(query: RoomRawQuery): List<SetEntity>

    @RawQuery(observedEntities = [SetEntity::class])
    fun pagingSource(query: RoomRawQuery): PagingSource<Int, SetEntity>

    @Query("SELECT COUNT(*) FROM sets")
    suspend fun getSetCount(): Int

    @Query("SELECT DISTINCT theme FROM sets WHERE theme IS NOT NULL")
    fun watchThemes(): Flow<List<String>>

    @Query("SELECT DISTINCT packagingType FROM sets WHERE packagingType IS NOT NULL")
    fun watchPackagingTypes(): Flow<List<String>>

    @Query("DELETE FROM sets WHERE lastUpdated > :date")
    suspend fun deleteSetsUpdatedAfter(date: Instant)
}
