package hu.piware.bricklog.feature.settings.domain.usecase

import hu.piware.bricklog.feature.settings.domain.model.NotificationPreferences
import hu.piware.bricklog.feature.settings.domain.repository.SettingsRepository
import org.koin.core.annotation.Single

@Single
class SaveNotificationPreferences(
    private val settingsRepository: SettingsRepository,
) {
    suspend operator fun invoke(preferences: NotificationPreferences) {
        settingsRepository.saveNotificationPreferences(preferences)
    }
}
