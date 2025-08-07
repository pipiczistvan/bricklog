package hu.piware.bricklog.feature.onboarding.domain.usecase

import hu.piware.bricklog.di.testModule
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.currency.domain.repository.CurrencyRepository
import hu.piware.bricklog.feature.set.data.repository.FakeCurrencyRepository
import hu.piware.bricklog.feature.set.data.repository.FakeDataServiceRepository
import hu.piware.bricklog.feature.set.data.repository.FakeSetRepository
import hu.piware.bricklog.feature.set.data.repository.FakeUpdateInfoRepository
import hu.piware.bricklog.feature.set.domain.repository.DataServiceRepository
import hu.piware.bricklog.feature.set.domain.repository.SetRepository
import hu.piware.bricklog.feature.set.domain.repository.UpdateInfoRepository
import hu.piware.bricklog.feature.set.domain.usecase.HasAnySets
import kotlinx.coroutines.test.runTest
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class HasAnySetsTest : KoinTest {

    private val overrideModule = module {
        singleOf(::FakeSetRepository).bind<SetRepository>()
        singleOf(::FakeUpdateInfoRepository).bind<UpdateInfoRepository>()
        singleOf(::FakeDataServiceRepository).bind<DataServiceRepository>()
        singleOf(::FakeCurrencyRepository).bind<CurrencyRepository>()
    }

    private val hasAnySets: HasAnySets by inject()
    private val updateData: UpdateData by inject()

    @BeforeTest
    fun setup() {
        startKoin {
            modules(testModule + overrideModule)
        }
    }

    @Test
    fun `HasAnySets returns true when sets exist`() = runTest {
        updateData()

        val result = hasAnySets()
        assertEquals(result, Result.Success(true))
    }

    @Test
    fun `HasAnySets returns false when no sets exist`() = runTest {
        val result = hasAnySets()
        assertEquals(result, Result.Success(false))
    }

    @AfterTest
    fun tearDown() {
        stopKoin()
    }
}
