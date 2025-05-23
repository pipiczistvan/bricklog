package hu.piware.bricklog.feature.settings.domain.usecase

import hu.piware.bricklog.feature.settings.domain.model.NotificationPreferences
import hu.piware.bricklog.feature.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class WatchNotificationPreferences(
    private val settingsRepository: SettingsRepository,
) {
    operator fun invoke(): Flow<NotificationPreferences> {
        return settingsRepository.notificationPreferences
    }
}
