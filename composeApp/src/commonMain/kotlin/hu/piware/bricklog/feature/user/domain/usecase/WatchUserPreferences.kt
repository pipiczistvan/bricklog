package hu.piware.bricklog.feature.user.domain.usecase

import hu.piware.bricklog.feature.user.domain.repository.UserPreferencesRepository
import org.koin.core.annotation.Single

@Single
class WatchUserPreferences(
    private val userPreferencesRepository: UserPreferencesRepository,
) {
    operator fun invoke() = userPreferencesRepository.watchUserPreferences()
}
