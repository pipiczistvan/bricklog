package hu.piware.bricklog.feature.set.domain.background_task

expect class SyncSetsPeriodicBackgroundTaskScheduler {
    fun schedule()
}

const val TASK_SYNC_SETS_IDENTIFIER = "hu.piware.bricklog.sync_sets"
const val TASK_SYNC_SETS_REFRESH_INTERVAL_MS = 4 * 60 * 60 * 1000L
