@file:OptIn(ExperimentalForeignApi::class)

package hu.piware.bricklog.feature.set.domain.background_task

import co.touchlab.kermit.Logger
import hu.piware.bricklog.feature.core.domain.onError
import hu.piware.bricklog.feature.set.domain.usecase.SyncSets
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

actual class SyncSetsPeriodicBackgroundTaskScheduler : KoinComponent {

    private val logger = Logger.withTag("SyncSetsPeriodicBackgroundTaskScheduler")

    private val syncSets: SyncSets by inject()

    init {
        BGTaskScheduler.sharedScheduler.registerForTaskWithIdentifier(
            identifier = TASK_SYNC_SETS_IDENTIFIER,
            usingQueue = null,
            launchHandler = { task ->
                handleUpdateSets(task as BGAppRefreshTask)
            }
        )
    }

    actual fun schedule() {
        val request = BGAppRefreshTaskRequest(identifier = TASK_SYNC_SETS_IDENTIFIER)
        request.earliestBeginDate =
            NSDate.dateWithTimeIntervalSinceNow(TASK_SYNC_SETS_REFRESH_INTERVAL_MS / 1000.0)

        try {
            BGTaskScheduler.sharedScheduler.submitTaskRequest(
                taskRequest = request,
                error = null
            )
            logger.i { "Update sets background task scheduled" }
        } catch (e: Exception) {
            logger.e { "Could not schedule update sets background task" }
        }
    }


    private fun handleUpdateSets(task: BGAppRefreshTask) {
        schedule()

        val operation = SyncSetsPeriodicBackgroundTask(syncSets)

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

    class SyncSetsPeriodicBackgroundTask(
        private val syncSets: SyncSets,
    ) : NSOperation() {

        override fun main() {
            runBlocking {
                syncSets()
                    .onError {
                        cancel()
                    }
            }
        }
    }
}
