@file:OptIn(DelicateCoroutinesApi::class)

package hu.piware.bricklog.util

import co.touchlab.kermit.Logger
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.PayloadData
import hu.piware.bricklog.di.initKoin
import hu.piware.bricklog.feature.core.NOTIFICATION_EVENT_NEW_SETS
import hu.piware.bricklog.feature.core.NotificationController
import hu.piware.bricklog.feature.core.NotificationEvent
import hu.piware.bricklog.feature.set.domain.background_task.SyncSetsPeriodicBackgroundTaskScheduler
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.dsl.KoinAppDeclaration

object AppInitializer : KoinComponent {

    private val logger = Logger.withTag("AppInitializer")

    fun initialize(config: KoinAppDeclaration? = null) {
        initKoin(config)

        NotifierManager.addListener(object : NotifierManager.Listener {
            override fun onNewToken(token: String) {
                logger.d("FirebaseOnNewToken: $token")
            }

            override fun onNotificationClicked(data: PayloadData) {
                super.onNotificationClicked(data)

                GlobalScope.launch {
                    NotificationController.sendEvent(data.toNotificationEvent())
                    logger.d { "Notification event sent" }
                }
            }
        })

        scheduleSyncSetsBackgroundTask()
    }

    private fun scheduleSyncSetsBackgroundTask() {
        val scheduler = getKoin().get<SyncSetsPeriodicBackgroundTaskScheduler>()
        scheduler.schedule()
    }
}

private fun PayloadData.toNotificationEvent(): NotificationEvent {
    if (this["type"] == NOTIFICATION_EVENT_NEW_SETS) {
        val minAppearanceDate = (this["minAppearanceDate"] as? String)?.toLong()

        if (minAppearanceDate != null) {
            return NotificationEvent.NewSets(
                startDate = minAppearanceDate
            )
        }
    }

    return NotificationEvent.Empty
}
