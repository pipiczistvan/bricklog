package hu.piware.bricklog.feature.currency.data.csv

import hu.piware.bricklog.feature.core.data.csv.CsvParser
import hu.piware.bricklog.feature.currency.domain.model.CurrencyRate
import org.koin.core.annotation.Single

@Single
class CurrencyRateCsvParser : CsvParser<CurrencyRateRow, CurrencyRate>(
    serializer = CurrencyRateRow.serializer(),
    transform = CurrencyRateRow::toDomainModel,
)
