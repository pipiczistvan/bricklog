package hu.piware.bricklog.feature.currency.domain.repository

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.FlowForResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.currency.domain.model.CurrencyRate
import hu.piware.bricklog.feature.currency.domain.model.UpdateRatesProgress
import hu.piware.bricklog.feature.set.domain.model.FileUploadResult
import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {

    fun updateCurrencyRatesWithProgress(
        baseCurrencyCode: String,
        fileUploads: List<FileUploadResult>,
    ): FlowForResult<Unit, UpdateRatesProgress>

    suspend fun getCurrencyRateCount(baseCurrencyCode: String): Result<Int, DataError>

    fun watchCurrencyRates(baseCurrencyCode: String): Flow<List<CurrencyRate>>
}
