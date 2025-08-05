package hu.piware.bricklog.feature.currency.data.database

import hu.piware.bricklog.feature.currency.domain.model.CurrencyRate

fun CurrencyRate.toEntity(baseCurrencyCode: String): CurrencyRateEntity {
    return CurrencyRateEntity(
        baseCurrencyCode = baseCurrencyCode,
        targetCurrencyCode = currencyCode,
        rate = rate,
    )
}

fun CurrencyRateEntity.toDomainModel(): CurrencyRate {
    return CurrencyRate(
        currencyCode = targetCurrencyCode,
        rate = rate,
    )
}
