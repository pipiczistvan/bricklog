package hu.piware.bricklog.di

import android.content.Context
import dev.icerock.moko.permissions.PermissionsController
import hu.piware.bricklog.feature.core.data.database.DatabaseFactory
import hu.piware.bricklog.feature.core.data.datastore.DatastoreFactory
import hu.piware.bricklog.feature.core.presentation.LocaleManager
import hu.piware.bricklog.feature.set.domain.background_task.SyncSetsPeriodicBackgroundTaskScheduler
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
actual class PlatformModule {

    @Single
    fun databaseFactory(context: Context) = DatabaseFactory(context)

    @Single
    fun httpClient() = OkHttp.create()

    @Single
    fun datastoreFactory(context: Context) = DatastoreFactory(context)

    @Single
    fun permissionsController(context: Context) = PermissionsController(context)

    @Single
    fun localeManager(context: Context) = LocaleManager(context)

    @Single
    fun syncSetsPeriodicBackgroundTaskScheduler(context: Context) =
        SyncSetsPeriodicBackgroundTaskScheduler(context)
}
