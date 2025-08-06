package hu.piware.bricklog.di

import hu.piware.bricklog.feature.core.data.database.DatabaseFactory
import hu.piware.bricklog.feature.core.data.datastore.DatastoreFactory
import hu.piware.bricklog.feature.core.presentation.LocaleManager
import hu.piware.bricklog.feature.onboarding.domain.background_task.SyncDataPeriodicBackgroundTaskScheduler
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
actual class PlatformModule {

    @Single
    fun databaseFactory() = DatabaseFactory()

    @Single
    fun httpClient() = OkHttp.create()

    @Single
    fun datastoreFactory() = DatastoreFactory()

//    @Single
//    fun permissionsController(): PermissionsControllerProtocol = PermissionsController()

    @Single
    fun localeManager() = LocaleManager()

    @Single
    fun syncDataPeriodicBackgroundTaskScheduler() = SyncDataPeriodicBackgroundTaskScheduler()
}
