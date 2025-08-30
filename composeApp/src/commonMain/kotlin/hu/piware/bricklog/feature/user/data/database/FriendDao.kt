package hu.piware.bricklog.feature.user.data.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface FriendDao {

    @Query("SELECT * FROM friends WHERE userId = :userId")
    fun watchFriends(userId: String): Flow<List<FriendEntity>>

    @Query("SELECT * FROM friends WHERE userId = :userId AND friendId = :friendId")
    fun watchFriend(userId: String, friendId: String): Flow<FriendEntity?>

    @Upsert
    suspend fun upsertFriends(friends: List<FriendEntity>)

    @Query("DELETE FROM friends WHERE userId = :userId")
    suspend fun deleteUserFriends(userId: String)

    @Query("DELETE FROM friends WHERE userId = :userId AND friendId IN (:friendIds)")
    suspend fun deleteFriends(userId: String, friendIds: List<String>)
}
