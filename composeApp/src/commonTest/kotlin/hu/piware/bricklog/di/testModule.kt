package hu.piware.bricklog.di

import hu.piware.bricklog.feature.currency.domain.usecase.UpdateEurRatesWithProgress
import hu.piware.bricklog.feature.onboarding.domain.usecase.UpdateData
import hu.piware.bricklog.feature.onboarding.domain.usecase.UpdateDataWithProgress
import hu.piware.bricklog.feature.set.domain.usecase.HasAnySets
import hu.piware.bricklog.feature.set.domain.usecase.UpdateSetsWithProgress
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val testModule = module(true) {
    // UseCases
    singleOf(::HasAnySets)
    singleOf(::UpdateData)
    singleOf(::UpdateDataWithProgress)
    singleOf(::UpdateEurRatesWithProgress)
    singleOf(::UpdateSetsWithProgress)
}
