package hu.piware.bricklog.feature.onboarding.domain.background_task

expect class SyncDataPeriodicBackgroundTaskScheduler {
    fun schedule()
}

const val TASK_SYNC_DATA_IDENTIFIER = "hu.piware.bricklog.sync_data"
const val TASK_SYNC_DATA_REFRESH_INTERVAL_MS = 4 * 60 * 60 * 1000L
