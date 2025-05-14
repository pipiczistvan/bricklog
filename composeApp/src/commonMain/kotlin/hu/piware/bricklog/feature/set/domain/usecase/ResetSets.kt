package hu.piware.bricklog.feature.set.domain.usecase

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.domain.onError
import hu.piware.bricklog.feature.set.domain.model.DataType
import hu.piware.bricklog.feature.set.domain.model.UpdateInfo
import hu.piware.bricklog.feature.set.domain.repository.SetRepository
import hu.piware.bricklog.feature.set.domain.repository.UpdateInfoRepository
import kotlinx.datetime.Instant

class ResetSets(
    private val setRepository: SetRepository,
    private val updateInfoRepository: UpdateInfoRepository,
) {
    suspend operator fun invoke(resetDate: Instant): EmptyResult<DataError> {
        setRepository.deleteSetsUpdatedAfter(resetDate)
            .onError { return it }

        updateInfoRepository.storeUpdateInfo(
            updateInfo = UpdateInfo(
                dataType = DataType.BRICKSET_SETS,
                lastUpdated = resetDate
            )
        ).onError { return it }

        return Result.Success(Unit)
    }
}
