package hu.piware.bricklog.mock

import hu.piware.bricklog.feature.set.domain.datasource.RemoteDataServiceDataSource
import hu.piware.bricklog.feature.set.domain.datasource.RemoteSetDataSource
import hu.piware.bricklog.feature.set.domain.datasource.RemoteSetImageDataSource
import hu.piware.bricklog.feature.set.domain.datasource.RemoteSetInstructionDataSource
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val mockModule = module {
    singleOf(::MockRemoteDataServiceDataSource).bind<RemoteDataServiceDataSource>()
    singleOf(::MockRemoteSetDataSource).bind<RemoteSetDataSource>()
    singleOf(::MockRemoteSetImageDataSource).bind<RemoteSetImageDataSource>()
    singleOf(::MockRemoteSetInstructionDataSource).bind<RemoteSetInstructionDataSource>()
}
