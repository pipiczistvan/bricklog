package hu.piware.bricklog.feature.set.domain.usecase

import co.touchlab.kermit.Logger
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.domain.onError
import hu.piware.bricklog.feature.set.domain.repository.SetRepository
import kotlinx.datetime.Instant
import org.koin.core.annotation.Single

@Single
class ResetSets(
    private val setRepository: SetRepository,
) {
    private val logger = Logger.withTag("ResetSets")

    suspend operator fun invoke(resetDate: Instant): EmptyResult<DataError> {
        setRepository.deleteSetsUpdatedAfter(resetDate)
            .onError { return it }

        logger.i { "Sets reset to $resetDate." }
        return Result.Success(Unit)
    }
}
