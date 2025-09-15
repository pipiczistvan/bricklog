package hu.piware.bricklog

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
import hu.piware.bricklog.util.AppInitializer
import hu.piware.bricklog.util.AppLifecycleObserver
import org.koin.android.ext.koin.androidContext

class BricklogApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        ProcessLifecycleOwner.get().lifecycle.addObserver(AppLifecycleObserver())

        NotifierManager.initialize(
            configuration = NotificationPlatformConfiguration.Android(
                notificationIconResId = R.drawable.notification_icon,
                showPushNotification = true,
            )
        )

        AppInitializer.initialize {
            androidContext(this@BricklogApplication)
        }
    }
}
