package hu.piware.bricklog.feature.currency.domain.datasource

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.currency.domain.model.CurrencyRate
import kotlinx.coroutines.flow.Flow

interface LocalCurrencyDataSource {
    suspend fun upsertRates(
        baseCurrencyCode: String,
        rates: List<CurrencyRate>,
    ): EmptyResult<DataError.Local>

    suspend fun getCurrencyRateCount(baseCurrencyCode: String): Result<Int, DataError.Local>

    fun watchCurrencyRates(baseCurrencyCode: String): Flow<List<CurrencyRate>>
}
