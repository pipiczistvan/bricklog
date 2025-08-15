package hu.piware.bricklog.feature.currency.domain.model

import hu.piware.bricklog.feature.set.domain.model.PriceFilterOption

data class CurrencyPreferenceDetails(
    val usdEurRate: Double?,
    val preferredRegion: CurrencyRegion,
    val preferredCurrencyCode: String,
    val preferredCurrencyEurRate: Double?,
    val priceRanges: Map<PriceFilterOption, Pair<Double?, Double?>>,
)
