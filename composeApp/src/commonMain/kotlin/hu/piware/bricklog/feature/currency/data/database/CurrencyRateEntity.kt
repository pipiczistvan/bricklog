package hu.piware.bricklog.feature.currency.data.database

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "currency_rates",
    primaryKeys = ["baseCurrencyCode", "targetCurrencyCode"],
    indices = [
        Index("baseCurrencyCode"),
    ],
)
data class CurrencyRateEntity(
    val baseCurrencyCode: String,
    val targetCurrencyCode: String,
    val rate: Double,
)
