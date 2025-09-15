package hu.piware.bricklog.feature.set.domain.usecase

import co.touchlab.kermit.Logger
import hu.piware.bricklog.feature.core.NotificationEvent
import hu.piware.bricklog.feature.core.NotificationEventController
import hu.piware.bricklog.feature.set.domain.model.SetDetails
import hu.piware.bricklog.feature.settings.domain.model.NotificationPreferences
import hu.piware.bricklog.feature.settings.domain.repository.SettingsRepository
import hu.piware.bricklog.util.firstOrDefault
import org.koin.core.annotation.Single

@Single
class SendNewSetNotification(
    private val settingsRepository: SettingsRepository,
) {
    private val logger = Logger.withTag("SendNewSetNotification")

    suspend operator fun invoke(newSets: List<SetDetails>) {
        logger.i { "Reading notification preferences" }
        val notificationPreferences = settingsRepository.watchNotificationPreferences()
            .firstOrDefault { NotificationPreferences() }

        logger.i { "Read notification preferences" }
        if (newSets.isNotEmpty() && notificationPreferences.newSets) {
            logger.i { "Sending notifications" }

            val firstAppearanceDate = newSets.minOf { it.set.infoCompleteDate!! }
            NotificationEventController.sendEvent(
                NotificationEvent.NewSets(
                    message = newSets.buildNotificationMessage(),
                    firstAppearanceDateMs = firstAppearanceDate.toEpochMilliseconds(),
                ),
            )
        }
    }
}

private fun List<SetDetails>.buildNotificationMessage(): String {
    val latestSets = take(2)
    val latestSetNameList = latestSets.map { it.set.name }.joinToString()
    val messagePostfix = if (latestSets.size < size) " and more." else "."
    return "$size new items! ${latestSetNameList}$messagePostfix"
}
