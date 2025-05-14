package hu.piware.bricklog.feature.set.domain.usecase

import co.touchlab.kermit.Logger
import com.mmk.kmpnotifier.notification.NotifierManager
import hu.piware.bricklog.feature.core.NOTIFICATION_EVENT_NEW_SETS
import hu.piware.bricklog.feature.set.domain.model.Set
import hu.piware.bricklog.feature.settings.domain.repository.SettingsRepository
import hu.piware.bricklog.feature.settings.domain.util.newSetsEnabled
import kotlinx.coroutines.flow.firstOrNull

class SendNewSetNotification(
    private val settingsRepository: SettingsRepository,
) {
    private val logger = Logger.withTag("SendNewSetNotification")

    suspend operator fun invoke(newSets: List<Set>) {
        logger.i { "Reading notification preferences" }
        val notificationPreferences = settingsRepository.notificationPreferences.firstOrNull()
        logger.i { "Read notification preferences" }
        if (newSets.isNotEmpty() && notificationPreferences.newSetsEnabled()) {
            logger.i { "Sending notifications" }
            NotifierManager.getLocalNotifier().notify {
                title = "New items"
                body = newSets.buildNotificationMessage()
                payloadData = mapOf(
                    "type" to NOTIFICATION_EVENT_NEW_SETS,
                    "minAppearanceDate" to newSets.minOf { it.infoCompleteDate!! }
                        .toEpochMilliseconds().toString()
                )
            }
        }
    }
}

private fun List<Set>.buildNotificationMessage(): String {
    val latestSets = take(2)
    val latestSetNameList = latestSets.map { it.name }.joinToString()
    val messagePostfix = if (latestSets.size < size) " and more." else "."
    return "$size new items! ${latestSetNameList}${messagePostfix}"
}
