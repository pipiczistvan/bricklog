package hu.piware.bricklog.feature.core.domain.usecase

import co.touchlab.kermit.Logger
import hu.piware.bricklog.feature.core.data.csv.CsvParser
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.FlowProgressCollector
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.domain.usecase.UpdateDataStep.PARSE_ITEMS
import hu.piware.bricklog.feature.set.domain.repository.UpdateInfoRepository

abstract class UpdateCsvDataWithProgressUseCase<R, D>(
    updateInfoRepository: UpdateInfoRepository,
    downloadFileByPriority: DownloadFileByPriority,
    private val csvParser: CsvParser<R, D>,
) : UpdateDataWithProgressUseCase<D>(
    updateInfoRepository,
    downloadFileByPriority,
) {
    private val logger = Logger.withTag("UpdateCsvDataWithProgressUseCase")

    override suspend fun FlowProgressCollector<UpdateDataProgress>.parseItems(
        rawData: ByteArray,
        linesCount: Int,
    ): Result<List<D>, DataError> {
        return try {
            val parsedItems = mutableListOf<D>()
            csvParser.parseInChunksAsync(rawData) { items ->
                parsedItems.addAll(items)
                if (linesCount > 0) {
                    val progress = parsedItems.size.toFloat() / linesCount
                    emitProgress(
                        UpdateDataProgress(
                            progress,
                            PARSE_ITEMS,
                        ),
                    )
                }
            }
            Result.Success(parsedItems)
        } catch (e: Exception) {
            logger.e("Failed to parse items", e)
            Result.Error(DataError.Local.UNKNOWN)
        }
    }
}
