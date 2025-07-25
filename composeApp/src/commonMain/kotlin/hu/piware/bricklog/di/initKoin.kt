package hu.piware.bricklog.di

import hu.piware.bricklog.mock.mockModule
import hu.piware.bricklog.util.BuildConfig
import hu.piware.bricklog.util.isMockFlavor
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.ksp.generated.module

fun initKoin(config: KoinAppDeclaration?) = startKoin {
    config?.invoke(this)
    modules(
        viewModelModule,
        useCaseModule,
        AppModule().module
    )
    if (BuildConfig.isMockFlavor) {
        modules(
            mockModule
        )
    }
}
