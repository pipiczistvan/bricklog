package hu.piware.bricklog.feature.user.data.database

import co.touchlab.kermit.Logger
import hu.piware.bricklog.feature.core.data.database.BricklogDatabase
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.user.domain.datasource.LocalFriendDataSource
import hu.piware.bricklog.feature.user.domain.model.Friend
import hu.piware.bricklog.feature.user.domain.model.UserId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
class RoomFriendDataSource(
    database: BricklogDatabase,
) : LocalFriendDataSource {

    private val logger = Logger.withTag("RoomFriendDataSource")

    val friendDao = database.friendDao

    override fun watchFriends(userId: UserId): Flow<List<Friend>> {
        return friendDao.watchFriends(userId)
            .map { friends -> friends.map { it.toDomainModel() } }
    }

    override suspend fun upsertFriends(
        userId: UserId,
        friends: List<Friend>,
    ): EmptyResult<DataError.Local> {
        return try {
            logger.d { "Upserting friends $friends for user $userId" }
            friendDao.upsertFriends(friends.map { it.toEntity(userId) })
            Result.Success(Unit)
        } catch (e: Exception) {
            logger.e(e) { "Error upserting friends $friends for user $userId" }
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun deleteUserFriends(userId: UserId): EmptyResult<DataError.Local> {
        return try {
            logger.d { "Deleting all friends for user $userId" }
            friendDao.deleteUserFriends(userId)
            Result.Success(Unit)
        } catch (e: Exception) {
            logger.e(e) { "Error deleting friends for user $userId" }
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun deleteFriends(
        userId: UserId,
        friendIds: List<UserId>,
    ): EmptyResult<DataError.Local> {
        return try {
            logger.d { "Deleting friends $friendIds for user $userId" }
            friendDao.deleteFriends(userId, friendIds)
            Result.Success(Unit)
        } catch (e: Exception) {
            logger.e(e) { "Error deleting friends $friendIds for user $userId" }
            Result.Error(DataError.Local.UNKNOWN)
        }
    }
}
