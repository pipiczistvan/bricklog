package hu.piware.bricklog.di

import hu.piware.bricklog.BuildKonfig
import hu.piware.bricklog.mock.mockModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration?) {
    startKoin {
        config?.invoke(this)
        modules(
            sharedModule,
            platformModule
        )
        if (BuildKonfig.MOCK) {
            modules(
                mockModule
            )
        }
    }
}
