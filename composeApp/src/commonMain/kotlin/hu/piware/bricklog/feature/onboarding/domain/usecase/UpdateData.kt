package hu.piware.bricklog.feature.onboarding.domain.usecase

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.collectForValue
import org.koin.core.annotation.Single

@Single
class UpdateData(
    private val updateDataWithProgress: UpdateDataWithProgress,
) {
    suspend operator fun invoke(force: Boolean = false): EmptyResult<DataError> {
        return updateDataWithProgress(force).collectForValue { }
    }
}
