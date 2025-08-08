package hu.piware.bricklog.feature.set.data.csv

import hu.piware.bricklog.feature.core.data.csv.CsvParser
import hu.piware.bricklog.feature.set.domain.model.Set
import org.koin.core.annotation.Single

@Single
class SetCsvParser : CsvParser<SetRow, Set>(
    serializer = SetRow.serializer(),
    transform = SetRow::toDomainModel,
)
