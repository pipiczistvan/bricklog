package hu.piware.bricklog.feature.currency.data.repository

import co.touchlab.kermit.Logger
import hu.piware.bricklog.di.CurrencyRateCsvParser
import hu.piware.bricklog.feature.core.data.csv.CsvParser
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.domain.await
import hu.piware.bricklog.feature.core.domain.awaitInProgressRange
import hu.piware.bricklog.feature.core.domain.data
import hu.piware.bricklog.feature.core.domain.flowForResult
import hu.piware.bricklog.feature.core.domain.onError
import hu.piware.bricklog.feature.currency.data.csv.CurrencyRateRow
import hu.piware.bricklog.feature.currency.domain.datasource.LocalCurrencyDataSource
import hu.piware.bricklog.feature.currency.domain.datasource.RemoteCurrencyDataSource
import hu.piware.bricklog.feature.currency.domain.model.CurrencyRate
import hu.piware.bricklog.feature.currency.domain.model.UpdateRatesProgress
import hu.piware.bricklog.feature.currency.domain.model.UpdateRatesStep
import hu.piware.bricklog.feature.currency.domain.repository.CurrencyRepository
import hu.piware.bricklog.feature.set.domain.model.FileUploadResult
import korlibs.io.compression.deflate.GZIP
import korlibs.io.compression.uncompress
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Single
import kotlin.time.measureTimedValue

@Single
class OfflineFirstCurrencyRepository(
    private val remoteDataSource: RemoteCurrencyDataSource,
    private val localDataSource: LocalCurrencyDataSource,
    @param:CurrencyRateCsvParser
    private val csvParser: CsvParser<CurrencyRateRow, CurrencyRate>,
) : CurrencyRepository {

    private val logger = Logger.withTag("OfflineFirstCurrencyRepository")

    override fun updateCurrencyRatesWithProgress(
        baseCurrencyCode: String,
        fileUploads: List<FileUploadResult>,
    ) =
        flowForResult {
            val downloadedFile = awaitInProgressRange(0f..0.25f) {
                downloadFile(fileUploads)
            }.onError { return@flowForResult it }.data()

            val csvFile = awaitInProgressRange(0.25f..0.5f) {
                uncompressFile(downloadedFile)
            }.onError { return@flowForResult it }.data()

            val parsedRates = awaitInProgressRange(0.5f..0.75f) {
                parseRates(csvFile)
            }.onError { return@flowForResult it }.data()

            awaitInProgressRange(0.75f..1f) { storeRates(baseCurrencyCode, parsedRates) }
        }

    override suspend fun getCurrencyRateCount(baseCurrencyCode: String): Result<Int, DataError> {
        return localDataSource.getCurrencyRateCount(baseCurrencyCode)
    }

    override fun watchCurrencyRates(baseCurrencyCode: String): Flow<List<CurrencyRate>> {
        return localDataSource.watchCurrencyRates(baseCurrencyCode)
    }

    private fun downloadFile(fileUploads: List<FileUploadResult>) = flowForResult {
        val orderedFileUploads = fileUploads.sortedBy { it.priority }

        for (fileUpload in orderedFileUploads) {
            val downloadResult = await {
                remoteDataSource.downloadCompressedCsv(fileUpload.url)
            }
            if (downloadResult is Result.Success) {
                return@flowForResult downloadResult
            }
        }

        Result.Error(DataError.Remote.UNKNOWN)
    }

    private fun uncompressFile(gzippedFile: ByteArray) = flowForResult {
        withContext(Dispatchers.Default) {
            logger.d { "Uncompressing gzip file" }
            emitProgress(UpdateRatesProgress(0f, UpdateRatesStep.UNCOMPRESS_FILE))
            val (uncompressedBytes, uncompressTimeTaken) = measureTimedValue {
                gzippedFile.uncompress(method = GZIP)
            }
            logger.d { "Uncompressing gzip file took $uncompressTimeTaken" }
            emitProgress(UpdateRatesProgress(1f, UpdateRatesStep.UNCOMPRESS_FILE))
            Result.Success(uncompressedBytes)
        }
    }

    private fun parseRates(csv: ByteArray) = flowForResult {
        logger.i { "Parsing currency rates" }
        emitProgress(UpdateRatesProgress(0f, UpdateRatesStep.PARSE_RATES))
        val (parseResult, parseTimeTaken) = measureTimedValue {
            try {
                val parsedRates = mutableListOf<CurrencyRate>()
                csvParser.parse(csv) { rates ->
                    parsedRates.addAll(rates)
                    emitProgress(UpdateRatesProgress(1f, UpdateRatesStep.PARSE_RATES))
                }
                Result.Success(parsedRates)
            } catch (e: Exception) {
                logger.e("Failed to parse currency rates", e)
                Result.Error(DataError.Local.UNKNOWN)
            }
        }
        logger.i { "Parsing rates took $parseTimeTaken" }
        parseResult
    }

    private fun storeRates(baseCurrencyCode: String, rates: List<CurrencyRate>) = flowForResult {
        logger.i { "Storing ${rates.size} currency rates" }
        emitProgress(UpdateRatesProgress(0f, UpdateRatesStep.STORE_RATES))
        val (storeResult, storeTimeTaken) = measureTimedValue {
            val result = localDataSource.upsertRates(baseCurrencyCode, rates)
            emitProgress(UpdateRatesProgress(1f, UpdateRatesStep.STORE_RATES))
            result
        }
        logger.i { "Storing currency rates took $storeTimeTaken" }
        storeResult
    }
}
