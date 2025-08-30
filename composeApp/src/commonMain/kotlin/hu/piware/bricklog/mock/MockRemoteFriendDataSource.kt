package hu.piware.bricklog.mock

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.user.domain.datasource.RemoteFriendDataSource
import hu.piware.bricklog.feature.user.domain.model.Friend
import hu.piware.bricklog.feature.user.domain.model.UserId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class MockRemoteFriendDataSource : RemoteFriendDataSource {

    private val firestore = MockFirestore

    override fun watchFriends(userId: UserId): Flow<List<Friend>> {
        return firestore.userFriends.map { it[userId] ?: emptyList() }
    }

    override suspend fun upsertFriends(
        userId: UserId,
        friends: List<Friend>,
    ): EmptyResult<DataError.Remote> {
        firestore.userFriends.update { currentMap ->
            val updatedMap = currentMap.toMutableMap()
            updatedMap[userId] = friends
            updatedMap
        }

        return Result.Success(Unit)
    }

    override suspend fun deleteFriends(
        userId: UserId,
        friendIds: List<UserId>,
    ): EmptyResult<DataError.Remote> {
        firestore.userFriends.update { currentMap ->
            val updatedMap = currentMap.toMutableMap()
            updatedMap[userId] = updatedMap[userId]?.filterNot { it.id in friendIds } ?: emptyList()
            updatedMap
        }

        return Result.Success(Unit)
    }

    override suspend fun deleteUserFriends(userId: UserId): EmptyResult<DataError.Remote> {
        firestore.userFriends.update { currentMap ->
            currentMap.filterNot { it.key == userId }
        }

        return Result.Success(Unit)
    }
}
