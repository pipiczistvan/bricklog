package hu.piware.bricklog.feature.set.data.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface SetPreferenceDao {

    @Query("SELECT * FROM set_preferences WHERE isFavourite = true")
    fun watchFavouriteSets(): Flow<List<SetPreferenceEntity>>

    @Query("SELECT * FROM set_preferences WHERE setId = :setId")
    fun watchSetPreferenceById(setId: Int): Flow<SetPreferenceEntity?>

    @Upsert
    suspend fun upsertSetPreference(setPreference: SetPreferenceEntity)
}
