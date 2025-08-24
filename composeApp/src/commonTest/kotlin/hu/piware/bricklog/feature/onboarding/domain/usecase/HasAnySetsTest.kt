package hu.piware.bricklog.feature.onboarding.domain.usecase

import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import hu.piware.bricklog.di.testModule
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.set.domain.repository.SetRepository
import hu.piware.bricklog.feature.set.domain.usecase.HasAnySets
import kotlinx.coroutines.test.runTest
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class HasAnySetsTest : KoinTest {

    private val hasAnySets: HasAnySets by inject()

    private val setRepository = mock<SetRepository>()

    private val overrideModule = module {
        single { setRepository }.bind<SetRepository>()
    }

    @BeforeTest
    fun setup() {
        startKoin {
            modules(testModule + overrideModule)
        }
    }

    @Test
    fun `HasAnySets returns true when sets exist`() = runTest {
        everySuspend { setRepository.getSetCount() }.returns(Result.Success(1))

        val result = hasAnySets()
        assertEquals(result, Result.Success(true))
    }

    @Test
    fun `HasAnySets returns false when no sets exist`() = runTest {
        everySuspend { setRepository.getSetCount() }.returns(Result.Success(0))

        val result = hasAnySets()
        assertEquals(result, Result.Success(false))
    }

    @AfterTest
    fun tearDown() {
        stopKoin()
    }
}
