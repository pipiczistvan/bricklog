package hu.piware.bricklog.feature.user.domain.usecase

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.user.domain.manager.SessionManager
import hu.piware.bricklog.feature.user.domain.model.UserPreferences
import hu.piware.bricklog.feature.user.domain.repository.UserPreferencesRepository
import org.koin.core.annotation.Single

@Single
class SaveUserPreferences(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val sessionManager: SessionManager,
) {
    suspend operator fun invoke(
        preferences: UserPreferences,
        userId: String = sessionManager.currentUserId,
    ): EmptyResult<DataError> {
        return userPreferencesRepository.saveUserPreferences(userId, preferences)
    }
}
