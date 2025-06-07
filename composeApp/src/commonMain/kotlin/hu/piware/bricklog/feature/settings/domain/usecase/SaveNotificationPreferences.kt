package hu.piware.bricklog.feature.settings.domain.usecase

import co.touchlab.kermit.Logger
import hu.piware.bricklog.feature.settings.domain.model.NotificationPreferences
import hu.piware.bricklog.feature.settings.domain.repository.SettingsRepository
import org.koin.core.annotation.Single

@Single
class SaveNotificationPreferences(
    private val settingsRepository: SettingsRepository,
) {
    private val logger = Logger.withTag("SaveNotificationPreferences")

    suspend operator fun invoke(preferences: NotificationPreferences) {
        logger.i { "Saving notification preferences: $preferences" }
        settingsRepository.saveNotificationPreferences(preferences)
    }
}
