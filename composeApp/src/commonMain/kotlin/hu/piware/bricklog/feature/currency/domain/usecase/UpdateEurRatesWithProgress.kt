package hu.piware.bricklog.feature.currency.domain.usecase

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.domain.data
import hu.piware.bricklog.feature.core.domain.map
import hu.piware.bricklog.feature.core.domain.onError
import hu.piware.bricklog.feature.core.domain.usecase.DownloadFileByPriority
import hu.piware.bricklog.feature.core.domain.usecase.UpdateDataWithProgressUseCase
import hu.piware.bricklog.feature.currency.data.csv.CurrencyRateCsvParser
import hu.piware.bricklog.feature.currency.data.csv.CurrencyRateRow
import hu.piware.bricklog.feature.currency.domain.model.CurrencyRate
import hu.piware.bricklog.feature.currency.domain.repository.CurrencyRepository
import hu.piware.bricklog.feature.currency.domain.util.CURRENCY_CODE_EUR
import hu.piware.bricklog.feature.set.domain.model.DataType
import hu.piware.bricklog.feature.set.domain.model.ExportBatch
import hu.piware.bricklog.feature.set.domain.model.ExportInfo
import hu.piware.bricklog.feature.set.domain.repository.DataServiceRepository
import hu.piware.bricklog.feature.set.domain.repository.UpdateInfoRepository
import hu.piware.bricklog.util.asResultOrNull
import kotlinx.datetime.Instant
import org.koin.core.annotation.Single

@Single
class UpdateEurRatesWithProgress(
    private val updateInfoRepository: UpdateInfoRepository,
    downloadFileByPriority: DownloadFileByPriority,
    csvParser: CurrencyRateCsvParser,
    private val currencyRepository: CurrencyRepository,
    private val dataServiceRepository: DataServiceRepository,
) : UpdateDataWithProgressUseCase<CurrencyRateRow, CurrencyRate>(
    updateInfoRepository = updateInfoRepository,
    downloadFileByPriority = downloadFileByPriority,
    csvParser = csvParser,
) {
    override val dataType = DataType.EUR_RATES

    override suspend fun getExportBatches(): Result<List<ExportBatch>, DataError> {
        return dataServiceRepository.getEurRateExportInfo()
            .map { listOf(it.toExportBatch()) }
    }

    override suspend fun getBatchFilterMinimumDate(): Result<Instant?, DataError> {
        val updateInfo = updateInfoRepository.watchUpdateInfo(DataType.EUR_RATES)
            .asResultOrNull()
            .onError { it }
            .data()

        return Result.Success(updateInfo?.lastUpdated)
    }

    override suspend fun saveItems(
        items: List<CurrencyRate>,
        updateProgress: suspend (Int) -> Unit,
    ): EmptyResult<DataError.Local> {
        return currencyRepository.updateRates(CURRENCY_CODE_EUR, items)
    }
}

private fun ExportInfo.toExportBatch(): ExportBatch {
    return ExportBatch(
        validFrom = lastUpdated,
        validTo = lastUpdated,
        rowCount = 0,
        fileUploads = fileUploads,
    )
}
