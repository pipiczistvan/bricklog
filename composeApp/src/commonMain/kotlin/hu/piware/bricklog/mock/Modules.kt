package hu.piware.bricklog.mock

import hu.piware.bricklog.feature.collection.domain.datasource.RemoteCollectionDataSource
import hu.piware.bricklog.feature.set.domain.datasource.RemoteDataServiceDataSource
import hu.piware.bricklog.feature.set.domain.datasource.RemoteSetImageDataSource
import hu.piware.bricklog.feature.set.domain.datasource.RemoteSetInstructionDataSource
import hu.piware.bricklog.feature.settings.domain.datasource.RemoteSettingsDataSource
import hu.piware.bricklog.feature.user.domain.datasource.RemoteUserDataSource
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val mockModule = module {
    singleOf(::MockRemoteDataServiceDataSource).bind<RemoteDataServiceDataSource>()
    singleOf(::MockRemoteSetImageDataSource).bind<RemoteSetImageDataSource>()
    singleOf(::MockRemoteSetInstructionDataSource).bind<RemoteSetInstructionDataSource>()
    singleOf(::MockRemoteUserDataSource).bind<RemoteUserDataSource>()
    singleOf(::MockRemoteCollectionDataSource).bind<RemoteCollectionDataSource>()
    singleOf(::MockRemoteSettingsDataSource).bind<RemoteSettingsDataSource>()
}
