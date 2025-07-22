package hu.piware.bricklog.feature.user.domain.usecase

import hu.piware.bricklog.feature.user.domain.model.UserPreferences
import hu.piware.bricklog.feature.user.domain.repository.UserPreferencesRepository
import org.koin.core.annotation.Single

@Single
class SaveUserPreferences(
    private val userPreferencesRepository: UserPreferencesRepository,
) {
    suspend operator fun invoke(preferences: UserPreferences) =
        userPreferencesRepository.saveUserPreferences(preferences)
}
