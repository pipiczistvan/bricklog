package hu.piware.bricklog.feature.set.domain.usecase

import co.touchlab.kermit.Logger
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.domain.data
import hu.piware.bricklog.feature.core.domain.onError
import hu.piware.bricklog.feature.set.domain.repository.DataServiceRepository
import hu.piware.bricklog.feature.set.domain.repository.SetRepository
import org.koin.core.annotation.Single

@Single
class UpdateSets(
    private val dataServiceRepository: DataServiceRepository,
    private val setRepository: SetRepository,
) {
    private val logger = Logger.withTag("UpdateSets")

    suspend operator fun invoke(force: Boolean = false): EmptyResult<DataError> {
        logger.i { "Getting batch export info" }
        val batchExportInfo = dataServiceRepository.getBatchExportInfo()
            .onError { return it }
            .data()

        logger.i { "Getting last updated set" }
        val lastUpdatedSet = setRepository.getLastUpdatedSet()
            .onError { return it }
            .data()

        val updateBatches = batchExportInfo.batches
            .filter { force || lastUpdatedSet == null || it.validTo > lastUpdatedSet.lastUpdated }
            .sortedBy { it.validTo }

        logger.i { "Updating with ${updateBatches.size} update batches" }
        for (batch in updateBatches) {
            setRepository.updateSets(batch.fileUploads)
                .onError { return it }
        }

        logger.i { "Updating sets finished" }
        return Result.Success(Unit)
    }
}
