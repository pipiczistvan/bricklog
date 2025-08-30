package hu.piware.bricklog.feature.user.domain.usecase

import hu.piware.bricklog.feature.user.domain.manager.SessionManager
import hu.piware.bricklog.feature.user.domain.manager.userBoundFlow
import hu.piware.bricklog.feature.user.domain.model.Friend
import hu.piware.bricklog.feature.user.domain.model.UserId
import hu.piware.bricklog.feature.user.domain.repository.FriendRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class WatchFriend(
    private val friendRepository: FriendRepository,
    private val sessionManager: SessionManager,
) {
    operator fun invoke(
        friendId: UserId,
        userId: UserId? = null,
    ): Flow<Friend?> {
        return sessionManager.userBoundFlow(userId) { userId ->
            friendRepository.watchFriend(userId, friendId)
        }
    }
}
