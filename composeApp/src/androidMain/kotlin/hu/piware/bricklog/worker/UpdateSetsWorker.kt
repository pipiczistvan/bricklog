package hu.piware.bricklog.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mmk.kmpnotifier.notification.NotifierManager
import hu.piware.bricklog.feature.core.domain.data
import hu.piware.bricklog.feature.core.domain.onError
import hu.piware.bricklog.feature.set.domain.model.Set
import hu.piware.bricklog.feature.set.domain.model.UpdateSetsResult
import hu.piware.bricklog.feature.set.domain.usecase.UpdateSets
import hu.piware.bricklog.feature.settings.domain.usecase.WatchNotificationPreferences
import hu.piware.bricklog.feature.settings.domain.util.newSetsEnabled
import kotlinx.coroutines.flow.last
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UpdateSetsWorker(
    appContext: Context,
    params: WorkerParameters,
) : CoroutineWorker(appContext, params), KoinComponent {

    private val updateSets: UpdateSets by inject()
    private val watchNotificationPreferences: WatchNotificationPreferences by inject()

    override suspend fun doWork(): Result {
        val updateResult = updateSets()
            .onError { return Result.failure() }
            .data()

        sendNotificationIfNecessary(updateResult)

        return Result.success()
    }

    private suspend fun sendNotificationIfNecessary(updateResult: UpdateSetsResult) {
        val notificationPreferences = watchNotificationPreferences().last()
        if (updateResult.newSets.isNotEmpty() && notificationPreferences.newSetsEnabled()) {
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
