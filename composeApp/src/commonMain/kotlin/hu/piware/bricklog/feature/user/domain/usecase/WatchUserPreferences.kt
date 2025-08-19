package hu.piware.bricklog.feature.user.domain.usecase

import hu.piware.bricklog.feature.user.domain.manager.SessionManager
import hu.piware.bricklog.feature.user.domain.manager.userBoundFlow
import hu.piware.bricklog.feature.user.domain.model.UserId
import hu.piware.bricklog.feature.user.domain.model.UserPreferences
import hu.piware.bricklog.feature.user.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class WatchUserPreferences(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val sessionManager: SessionManager,
) {
    operator fun invoke(userId: UserId? = null): Flow<UserPreferences> {
        return sessionManager.userBoundFlow(userId) {
            userPreferencesRepository.watchUserPreferences(it)
        }
    }
}
