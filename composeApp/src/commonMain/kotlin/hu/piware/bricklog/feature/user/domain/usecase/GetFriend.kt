package hu.piware.bricklog.feature.user.domain.usecase

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.user.domain.manager.SessionManager
import hu.piware.bricklog.feature.user.domain.model.Friend
import hu.piware.bricklog.feature.user.domain.model.UserId
import hu.piware.bricklog.util.asResultOrNull
import org.koin.core.annotation.Single

@Single
class GetFriend(
    private val watchFriend: WatchFriend,
    private val sessionManager: SessionManager,
) {
    suspend operator fun invoke(
        friendId: UserId,
        userId: UserId = sessionManager.currentUserId,
    ): Result<Friend?, DataError> {
        return watchFriend(friendId, userId)
            .asResultOrNull()
    }
}
