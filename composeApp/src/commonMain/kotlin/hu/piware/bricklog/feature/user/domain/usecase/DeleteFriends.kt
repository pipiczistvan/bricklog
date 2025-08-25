package hu.piware.bricklog.feature.user.domain.usecase

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.user.domain.manager.SessionManager
import hu.piware.bricklog.feature.user.domain.model.Friend
import hu.piware.bricklog.feature.user.domain.model.UserId
import hu.piware.bricklog.feature.user.domain.model.isNew
import hu.piware.bricklog.feature.user.domain.repository.FriendRepository
import org.koin.core.annotation.Single

@Single
class DeleteFriends(
    private val friendRepository: FriendRepository,
    private val sessionManager: SessionManager,
) {
    suspend operator fun invoke(
        vararg friends: Friend,
        userId: UserId = sessionManager.currentUserId,
    ): EmptyResult<DataError> {
        if (friends.any { it.isNew }) {
            return Result.Error(DataError.Local.UNKNOWN)
        }

        val friendIds = friends.map { it.id }

        return friendRepository.deleteFriends(userId, friendIds)
    }
}
