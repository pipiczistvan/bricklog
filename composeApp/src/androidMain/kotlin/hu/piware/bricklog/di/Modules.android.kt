package hu.piware.bricklog.di

import dev.icerock.moko.permissions.PermissionsController
import hu.piware.bricklog.feature.core.data.database.DatabaseFactory
import hu.piware.bricklog.feature.core.data.datastore.DatastoreFactory
import hu.piware.bricklog.feature.core.presentation.LocaleManager
import hu.piware.bricklog.feature.set.domain.background_task.SyncSetsPeriodicBackgroundTaskScheduler
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val platformModule = module {
    single { DatabaseFactory(androidContext()) }
    single { OkHttp.create() }
    single { DatastoreFactory(androidContext()) }
    single { PermissionsController(androidContext()) }
    single { LocaleManager(androidContext()) }
    single { SyncSetsPeriodicBackgroundTaskScheduler(androidContext()) }
}