package hu.piware.bricklog.feature.currency.domain.model

data class CurrencyRate(
    val currencyCode: String,
    val rate: Double,
)
