package hu.piware.bricklog.feature.set.domain.usecase

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.domain.data
import hu.piware.bricklog.feature.core.domain.onError
import hu.piware.bricklog.feature.set.domain.model.SetDetails
import hu.piware.bricklog.feature.set.domain.model.SetFilter
import hu.piware.bricklog.feature.set.domain.repository.CmfCodeRepository
import hu.piware.bricklog.util.asResultOrNull
import org.koin.core.annotation.Single

@Single
class FindSetDetailsByCmfCode(
    private val cmfCodeRepository: CmfCodeRepository,
    private val getSetDetailsByPreferences: GetSetDetailsByPreferences,
) {
    suspend operator fun invoke(code: String): Result<SetDetails?, DataError> {
        val cmfCode = cmfCodeRepository.watchCmfCode(code)
            .asResultOrNull()
            .onError { return it }
            .data()

        if (cmfCode == null) {
            return Result.Success(null)
        }

        val setDetails = getSetDetailsByPreferences(SetFilter(setIds = listOf(cmfCode.setId)))
            .onError { return it }
            .data()
            .firstOrNull()

        return Result.Success(setDetails)
    }
}
