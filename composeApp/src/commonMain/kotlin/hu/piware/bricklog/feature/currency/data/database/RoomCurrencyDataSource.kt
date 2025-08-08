package hu.piware.bricklog.feature.currency.data.database

import androidx.sqlite.SQLiteException
import co.touchlab.kermit.Logger
import hu.piware.bricklog.feature.core.data.database.BricklogDatabase
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.currency.domain.datasource.LocalCurrencyDataSource
import hu.piware.bricklog.feature.currency.domain.model.CurrencyRate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
class RoomCurrencyDataSource(
    database: BricklogDatabase,
) : LocalCurrencyDataSource {

    private val logger = Logger.withTag("RoomCurrencyDataSource")

    private val currencyRateDao = database.currencyRateDao

    override suspend fun getCurrencyRateCount(baseCurrencyCode: String): Result<Int, DataError.Local> {
        return try {
            logger.d { "Getting currency rate count" }
            val count = currencyRateDao.getCurrencyRateCount(baseCurrencyCode)
            Result.Success(count)
        } catch (e: Exception) {
            logger.e(e) { "Error getting currency rate count" }
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override fun watchCurrencyRates(baseCurrencyCode: String): Flow<List<CurrencyRate>> {
        return currencyRateDao.watchCurrencyRates(baseCurrencyCode)
            .map { entities -> entities.map { it.toDomainModel() } }
    }

    override suspend fun upsertRates(
        baseCurrencyCode: String,
        rates: List<CurrencyRate>,
    ): EmptyResult<DataError.Local> {
        return try {
            logger.d { "Upserting currency rates" }
            currencyRateDao.upsertRates(rates.map { it.toEntity(baseCurrencyCode) })
            Result.Success(Unit)
        } catch (e: SQLiteException) {
            logger.e(e) { "Error upserting currency rates" }
            Result.Error(DataError.Local.DISK_FULL)
        } catch (e: Exception) {
            logger.e(e) { "Error upserting currency rates" }
            Result.Error(DataError.Local.UNKNOWN)
        }
    }
}
