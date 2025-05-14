package hu.piware.bricklog

import android.app.Application
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
import hu.piware.bricklog.util.AppInitializer
import hu.piware.bricklog.worker.UpdateSetsWorker
import org.koin.android.ext.koin.androidContext
import java.util.concurrent.TimeUnit

class BricklogApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        NotifierManager.initialize(
            configuration = NotificationPlatformConfiguration.Android(
                notificationIconResId = R.drawable.ic_launcher_foreground,
                showPushNotification = true,
            )
        )
        AppInitializer.initialize {
            androidContext(this@BricklogApplication)
        }
        scheduleWorker()
    }

    private fun scheduleWorker() {
        val workRequest = PeriodicWorkRequestBuilder<UpdateSetsWorker>(
            repeatInterval = 4,
            repeatIntervalTimeUnit = TimeUnit.HOURS
        ).setConstraints(
            Constraints(
                requiredNetworkType = NetworkType.UNMETERED,
                requiresCharging = false,
                requiresDeviceIdle = false,
                requiresBatteryNotLow = false,
                requiresStorageNotLow = true
            )
        ).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            uniqueWorkName = "UpdateSetsWorker",
            existingPeriodicWorkPolicy = ExistingPeriodicWorkPolicy.UPDATE,
            request = workRequest
        )
    }
}
