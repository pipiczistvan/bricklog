package hu.piware.bricklog.feature.settings.domain.usecase

import hu.piware.bricklog.feature.settings.domain.model.UserPreferences
import hu.piware.bricklog.feature.settings.domain.repository.SettingsRepository
import org.koin.core.annotation.Single

@Single
class SaveUserPreferences(
    private val settingsRepository: SettingsRepository,
) {
    suspend operator fun invoke(preferences: UserPreferences) =
        settingsRepository.saveUserPreferences(preferences)
}
