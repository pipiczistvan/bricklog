@file:OptIn(ExperimentalSerializationApi::class)

package hu.piware.bricklog.feature.currency.data.csv

import app.softwork.serialization.csv.CSVFormat
import hu.piware.bricklog.feature.currency.domain.model.CurrencyRate
import korlibs.io.async.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import org.koin.core.annotation.Single

@Single
class CurrencyRateListCsvParser {

    private val csvFormat = CSVFormat.Default {
        separator = ';'
        includeHeader = false
    }

    suspend fun parse(
        data: ByteArray,
        onParsed: suspend (List<CurrencyRate>) -> Unit,
    ) {
        val csv = data.decodeToString()
        val lines = csv.lineSequence().drop(1)

        coroutineScope {
            launch(Dispatchers.Default) {
                val rows =
                    csvFormat.decodeFromString<List<CurrencyRateRow>>(lines.joinToString("\n"))
                onParsed(rows.map { it.toDomainModel() })
            }
        }
    }
}
