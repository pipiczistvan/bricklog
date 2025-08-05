package hu.piware.bricklog.feature.currency.data.csv

import kotlinx.serialization.Serializable

@Serializable
data class CurrencyRateRow(
    val currencyCode: String,
    val rate: Double,
)
