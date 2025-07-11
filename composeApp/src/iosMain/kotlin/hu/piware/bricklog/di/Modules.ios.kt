package hu.piware.bricklog.di

import dev.icerock.moko.permissions.ios.PermissionsController
import dev.icerock.moko.permissions.ios.PermissionsControllerProtocol
import hu.piware.bricklog.feature.core.data.database.DatabaseFactory
import hu.piware.bricklog.feature.core.data.datastore.DatastoreFactory
import hu.piware.bricklog.feature.core.presentation.LocaleManager
import hu.piware.bricklog.feature.set.domain.background_task.SyncSetsPeriodicBackgroundTaskScheduler
import io.ktor.client.engine.darwin.Darwin
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
actual class PlatformModule {

    @Single
    fun databaseFactory() = DatabaseFactory()

    @Single
    fun httpClient() = Darwin.create()

    @Single
    fun datastoreFactory() = DatastoreFactory()

    @Single
    fun permissionsController(): PermissionsControllerProtocol = PermissionsController()

    @Single
    fun localeManager() = LocaleManager()

    @Single
    fun syncSetsPeriodicBackgroundTaskScheduler() = SyncSetsPeriodicBackgroundTaskScheduler()
}
