package hu.piware.bricklog.feature.onboarding.domain.usecase

import hu.piware.bricklog.di.testModule
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.set.domain.repository.SetRepository
import hu.piware.bricklog.feature.set.domain.usecase.HasAnySets
import kotlinx.coroutines.test.runTest
import org.kodein.mock.Mock
import org.kodein.mock.generated.injectMocks
import org.kodein.mock.tests.TestsWithMocks
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

class HasAnySetsTest : TestsWithMocks(), KoinTest {

    override fun setUpMocks() = mocker.injectMocks(this)

    private val hasAnySets: HasAnySets by inject()

    @Mock
    lateinit var setRepository: SetRepository

    @BeforeTest
    fun setup() {
        startKoin {
            modules(
                testModule + module {
                    single { setRepository }.bind<SetRepository>()
                }
            )
        }
    }

    @Test
    fun `HasAnySets returns true when sets exist`() = runTest {
        everySuspending { setRepository.getSetCount() }.returns(Result.Success(1))

        val result = hasAnySets()
        assertEquals(result, Result.Success(true))
    }

    @Test
    fun `HasAnySets returns false when no sets exist`() = runTest {
        everySuspending { setRepository.getSetCount() }.returns(Result.Success(0))

        val result = hasAnySets()
        assertEquals(result, Result.Success(false))
    }

    @AfterTest
    fun tearDown() {
        stopKoin()
    }
}
