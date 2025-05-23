package hu.piware.bricklog.di

import hu.piware.bricklog.BuildKonfig
import hu.piware.bricklog.mock.mockModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.ksp.generated.module

fun initKoin(config: KoinAppDeclaration?) {
    startKoin {
        config?.invoke(this)
        modules(
            platformModule,
            viewModelModule,
            AppModule().module
        )
        if (BuildKonfig.MOCK) {
            modules(
                mockModule
            )
        }
    }
}
