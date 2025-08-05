@file:OptIn(DelicateCoroutinesApi::class)

package hu.piware.bricklog.util

import co.touchlab.kermit.Logger
import com.mmk.kmpauth.google.GoogleAuthCredentials
import com.mmk.kmpauth.google.GoogleAuthProvider
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.PayloadData
import hu.piware.bricklog.BuildKonfig
import hu.piware.bricklog.di.initKoin
import hu.piware.bricklog.feature.core.NOTIFICATION_EVENT_NEW_SETS
import hu.piware.bricklog.feature.core.NotificationController
import hu.piware.bricklog.feature.core.NotificationEvent
import hu.piware.bricklog.feature.core.domain.AppEvent
import hu.piware.bricklog.feature.core.presentation.AppEventController
import hu.piware.bricklog.feature.onboarding.domain.background_task.SyncDataPeriodicBackgroundTaskScheduler
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.dsl.KoinAppDeclaration

object AppInitializer : KoinComponent {

    private val logger = Logger.withTag("AppInitializer")

    fun initialize(config: KoinAppDeclaration? = null) {
        GoogleAuthProvider.create(credentials = GoogleAuthCredentials(serverId = BuildKonfig.GOOGLE_AUTH_WEB_CLIENT_ID))

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

        scheduleSyncDataBackgroundTask()

        MainScope().launch {
            AppEventController.sendEvent(AppEvent.Initialize)
        }
    }

    fun shouldInitializeFirebase(): Boolean {
        return BuildKonfig.DEV_LEVEL < DevLevels.MOCK
    }

    private fun scheduleSyncDataBackgroundTask() {
        val scheduler = getKoin().get<SyncDataPeriodicBackgroundTaskScheduler>()
        scheduler.schedule()
    }
}

private fun PayloadData.toNotificationEvent(): NotificationEvent {
    if (this["type"] == NOTIFICATION_EVENT_NEW_SETS) {
        val minAppearanceDate = (this["minAppearanceDate"] as? String)?.toLong()

        if (minAppearanceDate != null) {
            return NotificationEvent.NewSets(
                startDate = minAppearanceDate,
            )
        }
    }

    return NotificationEvent.Empty
}
