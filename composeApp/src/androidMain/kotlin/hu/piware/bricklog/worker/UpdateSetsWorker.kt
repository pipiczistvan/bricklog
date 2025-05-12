package hu.piware.bricklog.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import co.touchlab.kermit.Logger
import com.mmk.kmpnotifier.notification.NotifierManager
import hu.piware.bricklog.feature.core.domain.data
import hu.piware.bricklog.feature.core.domain.onError
import hu.piware.bricklog.feature.set.domain.model.Set
import hu.piware.bricklog.feature.set.domain.model.UpdateSetsResult
import hu.piware.bricklog.feature.set.domain.usecase.UpdateSets
import hu.piware.bricklog.feature.settings.domain.usecase.WatchNotificationPreferences
import hu.piware.bricklog.feature.settings.domain.util.newSetsEnabled
import kotlinx.coroutines.flow.lastOrNull
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UpdateSetsWorker(
    appContext: Context,
    params: WorkerParameters,
) : CoroutineWorker(appContext, params), KoinComponent {

    private val logger = Logger.withTag("UpdateSetsWorker")

    private val updateSets: UpdateSets by inject()
    private val watchNotificationPreferences: WatchNotificationPreferences by inject()

    override suspend fun doWork(): Result {
        logger.i { "Updating sets" }
        val updateResult = updateSets()
            .onError {
                logger.w { "Work failed" }
                return Result.failure()
            }
            .data()

        sendNotificationIfNecessary(updateResult)

        logger.i { "Work executed successfully" }
        return Result.success()
    }

    private suspend fun sendNotificationIfNecessary(updateResult: UpdateSetsResult) {
        val notificationPreferences = watchNotificationPreferences().lastOrNull()
        if (updateResult.newSets.isNotEmpty() && notificationPreferences.newSetsEnabled()) {
            logger.i { "Sending notifications" }
            NotifierManager.getLocalNotifier().notify {
                title = "New sets"
                body = updateResult.newSets.buildNotificationMessage()
            }
        }
    }
}

private fun List<Set>.buildNotificationMessage(): String {
    val latestSets = take(2)
    val latestSetNameList = latestSets.map { it.name }.joinToString()
    val messagePostfix = if (latestSets.size < size) " and more." else "."
    return "$size new sets! ${latestSetNameList}${messagePostfix}"
}
