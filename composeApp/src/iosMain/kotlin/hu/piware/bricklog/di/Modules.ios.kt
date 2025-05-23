package hu.piware.bricklog.di

import dev.icerock.moko.permissions.ios.PermissionsController
import dev.icerock.moko.permissions.ios.PermissionsControllerProtocol
import hu.piware.bricklog.feature.core.data.database.DatabaseFactory
import hu.piware.bricklog.feature.core.data.datastore.DatastoreFactory
import hu.piware.bricklog.feature.core.presentation.LocaleManager
import io.ktor.client.engine.darwin.Darwin
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformModule = module {
    single { DatabaseFactory() }
    single { Darwin.create() }
    single { DatastoreFactory() }
    single { PermissionsController() }.bind<PermissionsControllerProtocol>()
    single { LocaleManager() }
}
