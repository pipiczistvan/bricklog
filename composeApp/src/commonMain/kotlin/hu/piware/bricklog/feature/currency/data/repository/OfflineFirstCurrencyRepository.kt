package hu.piware.bricklog.feature.currency.data.repository

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.currency.domain.datasource.LocalCurrencyDataSource
import hu.piware.bricklog.feature.currency.domain.model.CurrencyRate
import hu.piware.bricklog.feature.currency.domain.repository.CurrencyRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class OfflineFirstCurrencyRepository(
    private val localDataSource: LocalCurrencyDataSource,
) : CurrencyRepository {

    override suspend fun getCurrencyRateCount(baseCurrencyCode: String): Result<Int, DataError> {
        return localDataSource.getCurrencyRateCount(baseCurrencyCode)
    }

    override fun watchCurrencyRates(baseCurrencyCode: String): Flow<List<CurrencyRate>> {
        return localDataSource.watchCurrencyRates(baseCurrencyCode)
    }

    override suspend fun updateRates(
        baseCurrencyCode: String,
        rates: List<CurrencyRate>,
    ): EmptyResult<DataError.Local> {
        return localDataSource.upsertRates(baseCurrencyCode, rates)
    }
}
