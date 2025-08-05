package hu.piware.bricklog.feature.currency.data.csv

import hu.piware.bricklog.feature.currency.domain.model.CurrencyRate

fun CurrencyRateRow.toDomainModel(): CurrencyRate {
    return CurrencyRate(
        currencyCode = currencyCode,
        rate = rate,
    )
}
