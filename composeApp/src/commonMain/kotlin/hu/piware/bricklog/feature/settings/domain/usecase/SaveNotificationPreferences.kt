package hu.piware.bricklog.feature.settings.domain.usecase

import co.touchlab.kermit.Logger
import com.mmk.kmpnotifier.notification.NotifierManager
import hu.piware.bricklog.feature.settings.domain.model.NotificationPreferences
import hu.piware.bricklog.feature.settings.domain.repository.SettingsRepository
import hu.piware.bricklog.feature.settings.domain.usecase.NotificationTopics.NEW_SETS

class SaveNotificationPreferences(
    private val settingsRepository: SettingsRepository,
) {
    private val logger = Logger.withTag("SaveNotificationPreferences")

    suspend operator fun invoke(preferences: NotificationPreferences) {
        settingsRepository.saveNotificationPreferences(preferences)

        if (preferences.general && preferences.newSets) {
            NotifierManager.getPushNotifier().subscribeToTopic(NEW_SETS)
            logger.i { "Subscribed to $NEW_SETS" }
        } else {
            NotifierManager.getPushNotifier().unSubscribeFromTopic(NEW_SETS)
            logger.i { "Unsubscribed from $NEW_SETS" }
        }
    }
}

object NotificationTopics {
    const val NEW_SETS = "new_sets"
}
