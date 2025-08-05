package hu.piware.bricklog.feature.currency.domain.usecase

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
import hu.piware.bricklog.feature.currency.domain.model.UpdateRatesProgress
import hu.piware.bricklog.feature.currency.domain.model.UpdateRatesStep
import hu.piware.bricklog.feature.currency.domain.repository.CurrencyRepository
import hu.piware.bricklog.feature.currency.domain.util.CURRENCY_CODE_EUR
import hu.piware.bricklog.feature.set.domain.model.DataType
import hu.piware.bricklog.feature.set.domain.model.ExportInfo
import hu.piware.bricklog.feature.set.domain.model.UpdateInfo
import hu.piware.bricklog.feature.set.domain.repository.DataServiceRepository
import hu.piware.bricklog.feature.set.domain.repository.UpdateInfoRepository
import hu.piware.bricklog.util.asResultOrNull
import kotlinx.datetime.Clock
import org.koin.core.annotation.Single

@Single
class UpdateEurRatesWithProgress(
    private val currencyRepository: CurrencyRepository,
    private val updateInfoRepository: UpdateInfoRepository,
    private val dataServiceRepository: DataServiceRepository,
) {
    private val logger = Logger.withTag("UpdateCurrencyRatesWithProgress")

    operator fun invoke(force: Boolean = false) = flowForResult {
        val exportInfo = awaitInProgressRange(0f..0.1f) { prepareExportInfo(force) }
            .onError { return@flowForResult it }
            .data()

        awaitInProgressRange(0.1f..0.9f) { updateRates(exportInfo) }
            .onError { return@flowForResult it }

        awaitInProgressRange(0.9f..1f) { saveUpdateInfo() }
    }

    private fun prepareExportInfo(force: Boolean) = flowForResult {
        emitProgress(UpdateRatesProgress(0f, UpdateRatesStep.PREPARE_EXPORT_INFO))
        logger.i { "Getting eur rate export info" }
        val exportInfo = dataServiceRepository.getEurRateExportInfo()
            .onError { return@flowForResult it }
            .data()

        val updateInfo = updateInfoRepository.watchUpdateInfo(DataType.CURRENCY_RATES)
            .asResultOrNull()
            .onError { return@flowForResult it }
            .data()

        emitProgress(UpdateRatesProgress(1f, UpdateRatesStep.PREPARE_EXPORT_INFO))

        if (updateInfo == null || exportInfo.lastUpdated > updateInfo.lastUpdated || force) {
            Result.Success(exportInfo)
        } else {
            Result.Success(null)
        }
    }

    private fun updateRates(exportInfo: ExportInfo?) = flowForResult {
        if (exportInfo != null) {
            awaitInProgressRange(0f..1f) {
                currencyRepository.updateCurrencyRatesWithProgress(
                    CURRENCY_CODE_EUR,
                    exportInfo.fileUploads,
                )
            }
        } else {
            logger.i { "No eur rate export info, skipping update" }
        }

        Result.Success(Unit)
    }

    private fun saveUpdateInfo(): FlowForValue<EmptyResult<DataError>, UpdateRatesProgress> =
        flowForValue {
            emitProgress(UpdateRatesProgress(0f, UpdateRatesStep.SAVE_UPDATE_INFO))
            logger.i { "Storing update info date" }
            val saveResult = updateInfoRepository.saveUpdateInfo(
                UpdateInfo(
                    dataType = DataType.CURRENCY_RATES,
                    lastUpdated = Clock.System.now(),
                ),
            )
            emitProgress(UpdateRatesProgress(1f, UpdateRatesStep.SAVE_UPDATE_INFO))
            saveResult
        }
}
