package hu.piware.bricklog.di

import hu.piware.bricklog.BuildKonfig
import hu.piware.bricklog.mock.mockModule
import hu.piware.bricklog.util.DevLevels
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
    if (BuildKonfig.DEV_LEVEL >= DevLevels.MOCK) {
        modules(
            mockModule
        )
    }
}
