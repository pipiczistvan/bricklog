package hu.piware.bricklog.feature.user.domain.usecase

import hu.piware.bricklog.feature.user.domain.manager.SessionManager
import org.koin.core.annotation.Single

@Single
class WatchCurrentUser(
    private val sessionManager: SessionManager,
) {
    operator fun invoke() = sessionManager.currentUser
}
