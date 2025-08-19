package hu.piware.bricklog.feature.user.domain.usecase

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.user.domain.manager.SessionManager
import hu.piware.bricklog.feature.user.domain.model.UserId
import hu.piware.bricklog.feature.user.domain.model.UserPreferences
import hu.piware.bricklog.util.asResultOrDefault
import org.koin.core.annotation.Single

@Single
class GetUserPreferences(
    private val watchUserPreferences: WatchUserPreferences,
    private val sessionManager: SessionManager,
) {
    suspend operator fun invoke(userId: UserId = sessionManager.currentUserId): Result<UserPreferences, DataError> {
        return watchUserPreferences(userId)
            .asResultOrDefault { SessionManager.GUEST_PREFERENCES }
    }
}
