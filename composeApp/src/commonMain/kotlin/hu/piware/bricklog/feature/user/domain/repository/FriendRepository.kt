package hu.piware.bricklog.feature.user.domain.repository

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.user.domain.model.Friend
import hu.piware.bricklog.feature.user.domain.model.UserId
import kotlinx.coroutines.flow.Flow

interface FriendRepository {

    fun watchFriends(userId: UserId): Flow<List<Friend>>

    suspend fun saveFriends(userId: UserId, friends: List<Friend>): EmptyResult<DataError>

    suspend fun deleteFriends(userId: UserId, friendIds: List<UserId>): EmptyResult<DataError>
}
