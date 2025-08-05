package hu.piware.bricklog.feature.currency.data.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyRateDao {

    @Upsert
    suspend fun upsertRates(rates: List<CurrencyRateEntity>)

    @Query("SELECT COUNT(*) FROM currency_rates WHERE baseCurrencyCode = :baseCurrencyCode")
    suspend fun getCurrencyRateCount(baseCurrencyCode: String): Int

    @Query("SELECT * FROM currency_rates WHERE baseCurrencyCode = :baseCurrencyCode")
    fun watchCurrencyRates(baseCurrencyCode: String): Flow<List<CurrencyRateEntity>>
}
