package hu.piware.bricklog.feature.core.domain.usecase

import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.feature_core_update_data_step_download_file
import bricklog.composeapp.generated.resources.feature_core_update_data_step_parse_items
import bricklog.composeapp.generated.resources.feature_core_update_data_step_prepare_export_info
import bricklog.composeapp.generated.resources.feature_core_update_data_step_save_update_info
import bricklog.composeapp.generated.resources.feature_core_update_data_step_store_items
import bricklog.composeapp.generated.resources.feature_core_update_data_step_uncompress_file
import co.touchlab.kermit.Logger
import hu.piware.bricklog.feature.core.data.csv.CsvParser
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.domain.UpdateProgress
import hu.piware.bricklog.feature.core.domain.awaitInProgressRange
import hu.piware.bricklog.feature.core.domain.data
import hu.piware.bricklog.feature.core.domain.flowForResult
import hu.piware.bricklog.feature.core.domain.flowForValue
import hu.piware.bricklog.feature.core.domain.mapProgress
import hu.piware.bricklog.feature.core.domain.onError
import hu.piware.bricklog.feature.core.domain.usecase.UpdateDataStep.DOWNLOAD_FILE
import hu.piware.bricklog.feature.core.domain.usecase.UpdateDataStep.PARSE_ITEMS
import hu.piware.bricklog.feature.core.domain.usecase.UpdateDataStep.PREPARE_EXPORT_INFO
import hu.piware.bricklog.feature.core.domain.usecase.UpdateDataStep.SAVE_UPDATE_INFO
import hu.piware.bricklog.feature.core.domain.usecase.UpdateDataStep.STORE_ITEMS
import hu.piware.bricklog.feature.core.domain.usecase.UpdateDataStep.UNCOMPRESS_FILE
import hu.piware.bricklog.feature.core.presentation.UiText
import hu.piware.bricklog.feature.set.domain.model.DataType
import hu.piware.bricklog.feature.set.domain.model.ExportBatch
import hu.piware.bricklog.feature.set.domain.model.UpdateInfo
import hu.piware.bricklog.feature.set.domain.repository.UpdateInfoRepository
import korlibs.io.compression.deflate.GZIP
import korlibs.io.compression.uncompress
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.measureTimedValue

abstract class UpdateDataWithProgressUseCase<R, D>(
    private val updateInfoRepository: UpdateInfoRepository,
    private val downloadFileByPriority: DownloadFileByPriority,
    private val csvParser: CsvParser<R, D>,
) {
    private val logger = Logger.withTag("UpdateDataWithProgressUseCase")

    abstract val dataType: DataType

    operator fun invoke(force: Boolean = false) = flowForResult {
        val batches = awaitInProgressRange(0f..0.1f) { prepareBatches(force) }
            .onError { return@flowForResult it }
            .data()

        awaitInProgressRange(0.1f..0.9f) { processBatches(batches) }
            .onError { return@flowForResult it }

        awaitInProgressRange(0.9f..1f) { saveUpdateInfo() }
    }

    private fun prepareBatches(force: Boolean) = flowForResult {
        emitProgress(
            UpdateDataProgress(
                0f,
                PREPARE_EXPORT_INFO,
            ),
        )
        logger.i { "Getting export info" }
        val exportBatches = getExportBatches()
            .onError { return@flowForResult it }
            .data()
        emitProgress(
            UpdateDataProgress(
                0.5f,
                PREPARE_EXPORT_INFO,
            ),
        )

        logger.i { "Getting minimum date" }
        val minimumDate = getBatchFilterMinimumDate()
            .onError { return@flowForResult it }
            .data()
        emitProgress(
            UpdateDataProgress(
                0.75f,
                PREPARE_EXPORT_INFO,
            ),
        )

        val processedBatches = exportBatches
            .filter { force || minimumDate == null || it.validTo > minimumDate }
            .sortedBy { it.validTo }

        if (processedBatches.isEmpty()) {
            logger.i { "No batches to update" }
        }

        emitProgress(
            UpdateDataProgress(
                1f,
                PREPARE_EXPORT_INFO,
            ),
        )
        Result.Success(processedBatches)
    }

    protected abstract suspend fun getExportBatches(): Result<List<ExportBatch>, DataError>

    protected abstract suspend fun getBatchFilterMinimumDate(): Result<Instant?, DataError>

    private fun processBatches(batches: List<ExportBatch>) = flowForResult {
        val totalRows = batches.sumOf { it.rowCount }
        val weighedBatches = batches
            .associateWith { it -> if (totalRows == 0) 1f else it.rowCount.toFloat() / totalRows }
        var currentStart = 0f
        val batchesWithProgressRanges = weighedBatches.map { (batch, weight) ->
            val currentEnd = currentStart + weight
            val progressRange = currentStart..currentEnd
            currentStart = currentEnd
            batch to progressRange
        }

        batchesWithProgressRanges.map { (batch, range) ->
            awaitInProgressRange(range) { updateData(batch) }
                .onError { return@flowForResult it }
        }

        Result.Success(Unit)
    }

    private fun updateData(batch: ExportBatch) = flowForResult {
        val downloadedFile = awaitInProgressRange(0f..0.25f) {
            downloadFileByPriority(batch.fileUploads)
                .mapProgress {
                    UpdateDataProgress(
                        stepProgress = it.stepProgress,
                        step = it.step.toUpdateFromExportedDataStep(),
                    )
                }
        }.onError { return@flowForResult it }.data()

        val csvFile = awaitInProgressRange(0.25f..0.5f) {
            uncompressFile(downloadedFile)
        }.onError { return@flowForResult it }.data()

        val parsedItems = awaitInProgressRange(0.5f..0.75f) {
            parseItems(csvFile, batch.rowCount)
        }.onError { return@flowForResult it }.data()

        awaitInProgressRange(0.75f..1f) { storeItems(parsedItems) }
    }

    private fun uncompressFile(gzippedFile: ByteArray) = flowForResult {
        withContext(Dispatchers.Default) {
            logger.d { "Uncompressing gzip file" }
            emitProgress(
                UpdateDataProgress(
                    0f,
                    UNCOMPRESS_FILE,
                ),
            )
            val (uncompressedBytes, uncompressTimeTaken) = measureTimedValue {
                gzippedFile.uncompress(method = GZIP)
            }
            logger.d { "Uncompressing gzip file took $uncompressTimeTaken" }
            emitProgress(
                UpdateDataProgress(
                    1f,
                    UNCOMPRESS_FILE,
                ),
            )
            Result.Success(uncompressedBytes)
        }
    }

    private fun parseItems(csv: ByteArray, linesCount: Int) = flowForResult {
        logger.i { "Parsing items" }
        emitProgress(
            UpdateDataProgress(
                0f,
                PARSE_ITEMS,
            ),
        )
        val (parseResult, parseTimeTaken) = measureTimedValue {
            try {
                val parsedItems = mutableListOf<D>()
                csvParser.parseInChunksAsync(csv) { items ->
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
        logger.i { "Parsing items took $parseTimeTaken" }
        parseResult
    }

    private fun storeItems(items: List<D>) = flowForResult {
        logger.i { "Storing ${items.size} items" }
        emitProgress(
            UpdateDataProgress(
                0f,
                STORE_ITEMS,
            ),
        )
        val (storeResult, storeTimeTaken) = measureTimedValue {
            saveItems(items) { insertCount ->
                if (items.isNotEmpty()) {
                    val progress = insertCount.toFloat() / items.size
                    emitProgress(
                        UpdateDataProgress(
                            progress,
                            STORE_ITEMS,
                        ),
                    )
                }
            }
        }
        logger.i { "Storing items took $storeTimeTaken" }
        storeResult
    }

    protected abstract suspend fun saveItems(
        items: List<D>,
        updateProgress: suspend (insertCount: Int) -> Unit,
    ): EmptyResult<DataError.Local>

    private fun saveUpdateInfo() = flowForValue {
        emitProgress(
            UpdateDataProgress(
                0f,
                SAVE_UPDATE_INFO,
            ),
        )
        logger.i { "Storing update info date" }
        val saveResult = updateInfoRepository.saveUpdateInfo(
            UpdateInfo(
                dataType = dataType,
                lastUpdated = Clock.System.now(),
            ),
        )
        emitProgress(
            UpdateDataProgress(
                1f,
                SAVE_UPDATE_INFO,
            ),
        )
        saveResult
    }
}

typealias UpdateDataProgress = UpdateProgress<UpdateDataStep>

enum class UpdateDataStep {
    PREPARE_EXPORT_INFO,
    DOWNLOAD_FILE,
    UNCOMPRESS_FILE,
    PARSE_ITEMS,
    STORE_ITEMS,
    SAVE_UPDATE_INFO,
}

fun UpdateDataStep.toUiText(): UiText {
    val stringRes = when (this) {
        PREPARE_EXPORT_INFO -> Res.string.feature_core_update_data_step_prepare_export_info
        DOWNLOAD_FILE -> Res.string.feature_core_update_data_step_download_file
        UNCOMPRESS_FILE -> Res.string.feature_core_update_data_step_uncompress_file
        PARSE_ITEMS -> Res.string.feature_core_update_data_step_parse_items
        STORE_ITEMS -> Res.string.feature_core_update_data_step_store_items
        SAVE_UPDATE_INFO -> Res.string.feature_core_update_data_step_save_update_info
    }

    return UiText.StringResourceId(stringRes)
}

private fun DownloadFileStep.toUpdateFromExportedDataStep(): UpdateDataStep {
    return when (this) {
        DownloadFileStep.DOWNLOAD -> DOWNLOAD_FILE
    }
}
