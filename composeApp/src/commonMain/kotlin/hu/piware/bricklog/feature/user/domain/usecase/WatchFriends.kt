package hu.piware.bricklog.feature.user.domain.usecase

import hu.piware.bricklog.feature.user.domain.manager.SessionManager
import hu.piware.bricklog.feature.user.domain.manager.userBoundFlow
import hu.piware.bricklog.feature.user.domain.model.Friend
import hu.piware.bricklog.feature.user.domain.model.UserId
import hu.piware.bricklog.feature.user.domain.repository.FriendRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class WatchFriends(
    private val friendRepository: FriendRepository,
    private val sessionManager: SessionManager,
) {
    operator fun invoke(userId: UserId? = null): Flow<List<Friend>> {
        return sessionManager.userBoundFlow(userId) { userId ->
            friendRepository.watchFriends(userId)
        }
    }
}
