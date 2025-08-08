package hu.piware.bricklog.feature.currency.domain.repository

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.currency.domain.model.CurrencyRate
import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {

    suspend fun getCurrencyRateCount(baseCurrencyCode: String): Result<Int, DataError>

    fun watchCurrencyRates(baseCurrencyCode: String): Flow<List<CurrencyRate>>

    suspend fun updateRates(
        baseCurrencyCode: String,
        rates: List<CurrencyRate>,
    ): EmptyResult<DataError.Local>
}
