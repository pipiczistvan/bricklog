package hu.piware.bricklog.feature.set.domain.model

data class SetPriceDetails(
    val price: Double,
    val currencyCode: String,
    val regionPrice: Double,
)
