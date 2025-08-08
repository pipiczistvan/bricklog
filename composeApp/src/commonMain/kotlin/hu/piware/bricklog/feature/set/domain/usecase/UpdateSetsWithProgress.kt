package hu.piware.bricklog.feature.set.domain.usecase

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.domain.data
import hu.piware.bricklog.feature.core.domain.map
import hu.piware.bricklog.feature.core.domain.onError
import hu.piware.bricklog.feature.core.domain.usecase.DownloadFileByPriority
import hu.piware.bricklog.feature.core.domain.usecase.UpdateDataWithProgressUseCase
import hu.piware.bricklog.feature.set.data.csv.SetCsvParser
import hu.piware.bricklog.feature.set.data.csv.SetRow
import hu.piware.bricklog.feature.set.domain.model.DataType
import hu.piware.bricklog.feature.set.domain.model.ExportBatch
import hu.piware.bricklog.feature.set.domain.model.Set
import hu.piware.bricklog.feature.set.domain.repository.DataServiceRepository
import hu.piware.bricklog.feature.set.domain.repository.SetRepository
import hu.piware.bricklog.feature.set.domain.repository.UpdateInfoRepository
import kotlinx.datetime.Instant
import org.koin.core.annotation.Single

@Single
class UpdateSetsWithProgress(
    updateInfoRepository: UpdateInfoRepository,
    downloadFileByPriority: DownloadFileByPriority,
    csvParser: SetCsvParser,
    private val dataServiceRepository: DataServiceRepository,
    private val setRepository: SetRepository,
) : UpdateDataWithProgressUseCase<SetRow, Set>(
    updateInfoRepository = updateInfoRepository,
    downloadFileByPriority = downloadFileByPriority,
    csvParser = csvParser,
) {
    override val dataType = DataType.SET_DATA

    override suspend fun getExportBatches(): Result<List<ExportBatch>, DataError> {
        return dataServiceRepository.getBatchExportInfo()
            .map { it.batches }
    }

    override suspend fun getBatchFilterMinimumDate(): Result<Instant?, DataError> {
        val lastUpdatedSet = setRepository.getLastUpdatedSet()
            .onError { return it }
            .data()

        return Result.Success(lastUpdatedSet?.lastUpdated)
    }

    override suspend fun saveItems(
        items: List<Set>,
        updateProgress: suspend (Int) -> Unit,
    ): EmptyResult<DataError.Local> {
        return setRepository.updateSetsChunked(items, SET_CHUNK_SIZE, updateProgress)
    }

    companion object Companion {
        private const val SET_CHUNK_SIZE = 3000
    }
}
