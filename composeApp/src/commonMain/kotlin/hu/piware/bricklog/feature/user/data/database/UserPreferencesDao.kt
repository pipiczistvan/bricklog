package hu.piware.bricklog.feature.user.data.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import hu.piware.bricklog.feature.user.domain.model.UserId
import kotlinx.coroutines.flow.Flow

@Dao
interface UserPreferencesDao {

    @Query("SELECT * FROM user_preferences WHERE userId = :userId")
    fun watchUserPreferences(userId: UserId): Flow<UserPreferencesEntity?>

    @Upsert
    suspend fun upsertUserPreferences(userPreferences: UserPreferencesEntity)

    @Query("DELETE FROM user_preferences WHERE userId = :userId")
    suspend fun deleteUserPreferences(userId: UserId)
}
