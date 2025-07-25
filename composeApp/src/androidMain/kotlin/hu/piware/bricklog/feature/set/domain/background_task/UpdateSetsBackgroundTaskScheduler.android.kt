package hu.piware.bricklog.feature.set.domain.background_task

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import co.touchlab.kermit.Logger
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.onError
import hu.piware.bricklog.feature.set.domain.usecase.SyncSets
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.concurrent.TimeUnit

actual class SyncSetsPeriodicBackgroundTaskScheduler(
    private val context: Context,
) : KoinComponent {

    private val logger = Logger.withTag("SyncSetsPeriodicBackgroundTaskScheduler")

    actual fun schedule() {
        val workRequest = PeriodicWorkRequestBuilder<SyncSetsPeriodicBackgroundTask>(
            repeatInterval = TASK_SYNC_SETS_REFRESH_INTERVAL_MS,
            repeatIntervalTimeUnit = TimeUnit.MILLISECONDS
        )
            .setInitialDelay(TASK_SYNC_SETS_REFRESH_INTERVAL_MS, TimeUnit.MILLISECONDS)
            .setConstraints(
                Constraints(
                    requiredNetworkType = NetworkType.UNMETERED,
                    requiresCharging = false,
                    requiresDeviceIdle = false,
                    requiresBatteryNotLow = true,
                    requiresStorageNotLow = true
                )
            ).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            uniqueWorkName = TASK_SYNC_SETS_IDENTIFIER,
            existingPeriodicWorkPolicy = ExistingPeriodicWorkPolicy.UPDATE,
            request = workRequest
        )
    }

    class SyncSetsPeriodicBackgroundTask(
        appContext: Context,
        params: WorkerParameters,
    ) : CoroutineWorker(appContext, params), KoinComponent {

        private val logger = Logger.withTag("SyncSetsPeriodicBackgroundTask")

        private val syncSets: SyncSets by inject()

        override suspend fun doWork(): Result {
            syncSets()
                .onError {
                    if (it.error != DataError.Local.BUSY) {
                        logger.w { "Error while syncing sets" }
                        return Result.failure()
                    }
                }

            return Result.success()
        }
    }
}
