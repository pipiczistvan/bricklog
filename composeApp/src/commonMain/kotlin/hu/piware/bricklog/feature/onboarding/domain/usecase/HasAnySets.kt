package hu.piware.bricklog.feature.onboarding.domain.usecase

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.domain.data
import hu.piware.bricklog.feature.core.domain.onError
import hu.piware.bricklog.feature.set.domain.repository.SetRepository
import org.koin.core.annotation.Single

@Single
class HasAnySets(
    private val setRepository: SetRepository,
) {
    suspend operator fun invoke(): Result<Boolean, DataError> {
        val count = setRepository.getSetCount()
            .onError { return@invoke it }
            .data()

        return Result.Success(count > 0)
    }
}
