package hu.piware.bricklog.feature.set.domain.usecase

import co.touchlab.kermit.Logger
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.FlowForValue
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.domain.awaitInProgressRange
import hu.piware.bricklog.feature.core.domain.data
import hu.piware.bricklog.feature.core.domain.flowForResult
import hu.piware.bricklog.feature.core.domain.flowForValue
import hu.piware.bricklog.feature.core.domain.onError
import hu.piware.bricklog.feature.set.domain.model.DataType
import hu.piware.bricklog.feature.set.domain.model.ExportBatch
import hu.piware.bricklog.feature.set.domain.model.UpdateInfo
import hu.piware.bricklog.feature.set.domain.model.UpdateSetsProgress
import hu.piware.bricklog.feature.set.domain.model.UpdateSetsStep
import hu.piware.bricklog.feature.set.domain.repository.DataServiceRepository
import hu.piware.bricklog.feature.set.domain.repository.SetRepository
import hu.piware.bricklog.feature.set.domain.repository.UpdateInfoRepository
import kotlinx.datetime.Clock
import org.koin.core.annotation.Single

@Single
class UpdateSetsWithProgress(
    private val dataServiceRepository: DataServiceRepository,
    private val setRepository: SetRepository,
    private val updateInfoRepository: UpdateInfoRepository,
) {
    private val logger = Logger.withTag("UpdateSetsWithProgress")

    operator fun invoke(force: Boolean = false) = flowForResult {
        val batches = awaitInProgressRange(0f..0.1f) { prepareBatches(force) }
            .onError { return@flowForResult it }
            .data()

        awaitInProgressRange(0.1f..0.9f) { updateSets(batches) }
            .onError { return@flowForResult it }

        awaitInProgressRange(0.9f..1f) { saveUpdateInfo() }
    }

    private fun prepareBatches(force: Boolean) = flowForResult {
        emitProgress(UpdateSetsProgress(0f, UpdateSetsStep.PREPARE_BATCHES))
        logger.i { "Getting batch export info" }
        val batchExportInfo = dataServiceRepository.getBatchExportInfo()
            .onError { return@flowForResult it }
            .data()
        emitProgress(UpdateSetsProgress(0.5f, UpdateSetsStep.PREPARE_BATCHES))

        logger.i { "Getting last updated set" }
        val lastUpdatedSet = setRepository.getLastUpdatedSet()
            .onError { return@flowForResult it }
            .data()
        emitProgress(UpdateSetsProgress(0.75f, UpdateSetsStep.PREPARE_BATCHES))

        val batches = batchExportInfo.batches
            .filter { force || lastUpdatedSet == null || it.validTo > lastUpdatedSet.lastUpdated }
            .sortedBy { it.validTo }

        if (batches.isEmpty()) {
            logger.i { "No batches to update" }
        }

        emitProgress(UpdateSetsProgress(1f, UpdateSetsStep.PREPARE_BATCHES))
        Result.Success(batches)
    }

    private fun updateSets(batches: List<ExportBatch>) = flowForResult {
        val totalRows = batches.sumOf { it.rowCount }
        val weighedBatches = batches.associateWith { it -> it.rowCount.toFloat() / totalRows }
        var currentStart = 0f
        val batchesWithProgressRanges = weighedBatches.map { (batch, weight) ->
            val currentEnd = currentStart + weight
            val progressRange = currentStart..currentEnd
            currentStart = currentEnd
            batch to progressRange
        }

        batchesWithProgressRanges.map { (batch, range) ->
            awaitInProgressRange(range) { setRepository.updateSetsWithProgress(batch) }
                .onError { return@flowForResult it }
        }

        Result.Success(Unit)
    }

    private fun saveUpdateInfo(): FlowForValue<EmptyResult<DataError>, UpdateSetsProgress> =
        flowForValue {
            emitProgress(UpdateSetsProgress(0f, UpdateSetsStep.SAVE_UPDATE_INFO))
            logger.i { "Storing update info date" }
            val saveResult = updateInfoRepository.saveUpdateInfo(
                UpdateInfo(
                    dataType = DataType.SET_DATA,
                    lastUpdated = Clock.System.now(),
                ),
            )
            emitProgress(UpdateSetsProgress(1f, UpdateSetsStep.SAVE_UPDATE_INFO))
            saveResult
        }
}
