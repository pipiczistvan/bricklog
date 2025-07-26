package hu.piware.bricklog.feature.user.domain.usecase

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.user.domain.manager.SessionManager
import hu.piware.bricklog.feature.user.domain.model.UserPreferences
import hu.piware.bricklog.feature.user.domain.repository.UserPreferencesRepository
import hu.piware.bricklog.util.asResultOrDefault
import org.koin.core.annotation.Single

@Single
class GetUserPreferences(
    private val userPreferencesRepository: UserPreferencesRepository,
) {
    suspend operator fun invoke(): Result<UserPreferences, DataError> {
        return userPreferencesRepository.watchUserPreferences()
            .asResultOrDefault { SessionManager.GUEST_PREFERENCES }
    }
}
