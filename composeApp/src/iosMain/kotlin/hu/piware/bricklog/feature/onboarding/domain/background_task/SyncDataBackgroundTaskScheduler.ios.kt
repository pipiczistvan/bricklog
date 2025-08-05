@file:OptIn(ExperimentalForeignApi::class)

package hu.piware.bricklog.feature.onboarding.domain.background_task

import co.touchlab.kermit.Logger
import hu.piware.bricklog.feature.core.domain.onError
import hu.piware.bricklog.feature.onboarding.domain.usecase.SyncData
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import platform.BackgroundTasks.BGAppRefreshTask
import platform.BackgroundTasks.BGAppRefreshTaskRequest
import platform.BackgroundTasks.BGTaskScheduler
import platform.Foundation.NSDate
import platform.Foundation.NSOperation
import platform.Foundation.NSOperationQueue
import platform.Foundation.dateWithTimeIntervalSinceNow

actual class SyncDataPeriodicBackgroundTaskScheduler : KoinComponent {

    private val logger = Logger.withTag("SyncDataPeriodicBackgroundTaskScheduler")

    private val syncData: SyncData by inject()

    init {
        BGTaskScheduler.sharedScheduler.registerForTaskWithIdentifier(
            identifier = TASK_SYNC_DATA_IDENTIFIER,
            usingQueue = null,
            launchHandler = { task ->
                handleUpdateData(task as BGAppRefreshTask)
            }
        )
    }

    actual fun schedule() {
        val request = BGAppRefreshTaskRequest(identifier = TASK_SYNC_DATA_IDENTIFIER)
        request.earliestBeginDate =
            NSDate.dateWithTimeIntervalSinceNow(TASK_SYNC_DATA_REFRESH_INTERVAL_MS / 1000.0)

        try {
            BGTaskScheduler.sharedScheduler.submitTaskRequest(
                taskRequest = request,
                error = null
            )
            logger.i { "Update data background task scheduled" }
        } catch (e: Exception) {
            logger.e { "Could not schedule update data background task" }
        }
    }


    private fun handleUpdateData(task: BGAppRefreshTask) {
        schedule()

        val operation = SyncDataPeriodicBackgroundTask(syncData)

        val queue = NSOperationQueue()
        queue.maxConcurrentOperationCount = 1

        queue.addOperations(
            ops = listOf(operation),
            waitUntilFinished = false
        )

        operation.setCompletionBlock {
            task.setTaskCompletedWithSuccess(success = !operation.cancelled)
        }
    }

    class SyncDataPeriodicBackgroundTask(
        private val syncData: SyncData,
    ) : NSOperation() {

        override fun main() {
            runBlocking {
                syncData()
                    .onError {
                        cancel()
                    }
            }
        }
    }
}
