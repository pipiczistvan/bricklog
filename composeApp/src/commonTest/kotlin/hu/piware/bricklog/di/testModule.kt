package hu.piware.bricklog.di

import hu.piware.bricklog.feature.set.domain.usecase.HasAnySets
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val testModule = module(true) {
    // UseCases
    singleOf(::HasAnySets)
}
