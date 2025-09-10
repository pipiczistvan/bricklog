package hu.piware.bricklog.feature.set.domain.usecase

import co.touchlab.kermit.Logger
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.FlowProgressCollector
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.domain.data
import hu.piware.bricklog.feature.core.domain.map
import hu.piware.bricklog.feature.core.domain.onError
import hu.piware.bricklog.feature.core.domain.usecase.DownloadFileByPriority
import hu.piware.bricklog.feature.core.domain.usecase.UpdateDataProgress
import hu.piware.bricklog.feature.core.domain.usecase.UpdateDataWithProgressUseCase
import hu.piware.bricklog.feature.set.data.network.CmfSeriesDto
import hu.piware.bricklog.feature.set.data.network.toDomainModels
import hu.piware.bricklog.feature.set.domain.model.CmfCode
import hu.piware.bricklog.feature.set.domain.model.DataType
import hu.piware.bricklog.feature.set.domain.model.ExportBatch
import hu.piware.bricklog.feature.set.domain.model.toExportBatch
import hu.piware.bricklog.feature.set.domain.repository.CmfCodeRepository
import hu.piware.bricklog.feature.set.domain.repository.DataServiceRepository
import hu.piware.bricklog.feature.set.domain.repository.UpdateInfoRepository
import hu.piware.bricklog.util.asResultOrNull
import kotlinx.datetime.Instant
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Single

@Single
class UpdateCmfCodesWithProgress(
    private val updateInfoRepository: UpdateInfoRepository,
    private val dataServiceRepository: DataServiceRepository,
    downloadFileByPriority: DownloadFileByPriority,
    private val cmfCodeRepository: CmfCodeRepository,
) : UpdateDataWithProgressUseCase<CmfCode>(
    updateInfoRepository,
    downloadFileByPriority,
) {
    private val logger = Logger.withTag("UpdateCmfCodesWithProgress")

    override val dataType = DataType.CMF_CODES

    private val json = Json {
        ignoreUnknownKeys = true
    }

    override suspend fun getExportBatches(): Result<List<ExportBatch>, DataError> {
        return dataServiceRepository.getCmfCodesExportInfo()
            .map { listOf(it.toExportBatch()) }
    }

    override suspend fun getBatchFilterMinimumDate(): Result<Instant?, DataError> {
        val updateInfo = updateInfoRepository.watchUpdateInfo(dataType)
            .asResultOrNull()
            .onError { it }
            .data()

        return Result.Success(updateInfo?.lastUpdated)
    }

    override suspend fun FlowProgressCollector<UpdateDataProgress>.parseItems(
        rawData: ByteArray,
        linesCount: Int,
    ): Result<List<CmfCode>, DataError> {
        return try {
            val jsonString = rawData.decodeToString()
            val seriesList = json.decodeFromString<List<CmfSeriesDto>>(jsonString)
            val cmfCodes = seriesList
                .flatMap { series ->
                    series.codes.flatMap {
                        it.toDomainModels(series.setId)
                    }
                }
            Result.Success(cmfCodes)
        } catch (e: Exception) {
            logger.w(e) { "Error parsing CMF codes" }
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun saveItems(
        items: List<CmfCode>,
        updateProgress: suspend (Int) -> Unit,
    ): EmptyResult<DataError> {
        return cmfCodeRepository.updateCmfCodes(items)
    }
}
