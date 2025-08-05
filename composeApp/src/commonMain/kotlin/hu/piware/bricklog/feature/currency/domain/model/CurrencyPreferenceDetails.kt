package hu.piware.bricklog.feature.currency.domain.model

data class CurrencyPreferenceDetails(
    val usdEurRate: Double?,
    val preferredRegion: CurrencyRegion,
    val preferredCurrencyCode: String,
    val preferredCurrencyEurRate: Double?,
)
