@file:OptIn(ExperimentalSerializationApi::class)

package hu.piware.bricklog.feature.set.data.csv

import app.softwork.serialization.csv.CSVFormat
import hu.piware.bricklog.feature.set.domain.model.Set
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString

class SetListCsvParser {

    private val csvFormat = CSVFormat.Default {
        separator = ';'
        includeHeader = false
    }

    suspend fun parseInChunksAsync(
        data: ByteArray,
        chunkSize: Int,
        onChunkParsed: suspend (List<Set>) -> Unit,
    ) {
        val setsCsv = data.decodeToString()
        val csvLines = setsCsv.lineSequence().drop(1) // Use Sequence for lazy processing
        val csvChunks = csvLines.chunked(chunkSize) // Still lazy due to Sequence

        coroutineScope {
            for (chunk in csvChunks) {
                launch {
                    val csv = chunk.joinToString("\n")
                    val setRows = csvFormat.decodeFromString<List<SetRow>>(csv)
                    onChunkParsed(setRows.map { it.toDomainModel() })
                }
            }
        }
    }
}
