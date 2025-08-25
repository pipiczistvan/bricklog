package hu.piware.bricklog.feature.user.domain.datasource

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.user.domain.model.Friend
import hu.piware.bricklog.feature.user.domain.model.UserId
import kotlinx.coroutines.flow.Flow

interface LocalFriendDataSource {

    fun watchFriends(userId: UserId): Flow<List<Friend>>

    suspend fun upsertFriends(userId: UserId, friends: List<Friend>): EmptyResult<DataError.Local>

    suspend fun deleteUserFriends(userId: UserId): EmptyResult<DataError.Local>

    suspend fun deleteFriends(userId: UserId, friendIds: List<UserId>): EmptyResult<DataError.Local>
}
