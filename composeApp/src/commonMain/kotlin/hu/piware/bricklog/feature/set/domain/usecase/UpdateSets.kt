package hu.piware.bricklog.feature.set.domain.usecase

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.collectForValue
import org.koin.core.annotation.Single

@Single
class UpdateSets(
    private val updateSetsWithProgress: UpdateSetsWithProgress,
) {
    suspend operator fun invoke(force: Boolean = false): EmptyResult<DataError> {
        return updateSetsWithProgress().collectForValue { }
    }
}
