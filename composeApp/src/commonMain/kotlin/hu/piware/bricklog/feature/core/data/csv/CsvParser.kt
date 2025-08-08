@file:OptIn(ExperimentalSerializationApi::class)

package hu.piware.bricklog.feature.core.data.csv

import app.softwork.serialization.csv.CSVFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer

abstract class CsvParser<R, D>(
    private val serializer: KSerializer<R>,
    private val transform: (R) -> D,
) {
    private val csvFormat = CSVFormat.Default {
        separator = ';'
        includeHeader = false
    }

    suspend fun parse(
        data: ByteArray,
        onParsed: suspend (List<D>) -> Unit,
    ) {
        val csv = data.decodeToString()
        val lines = csv.lineSequence().drop(1)

        coroutineScope {
            launch(Dispatchers.Default) {
                onParsed(lines.toList().parseLines())
            }
        }
    }

    suspend fun parseInChunksAsync(
        data: ByteArray,
        chunkSize: Int = CHUNK_SIZE,
        onChunkParsed: suspend (List<D>) -> Unit,
    ) {
        val csv = data.decodeToString()
        val lines = csv.lineSequence().drop(1)
        val chunks = lines.chunked(chunkSize)

        coroutineScope {
            for (chunk in chunks) {
                launch(Dispatchers.Default) {
                    onChunkParsed(chunk.parseLines())
                }
            }
        }
    }

    private fun List<String>.parseLines() =
        csvFormat.decodeFromString(ListSerializer(serializer), joinToString("\n"))
            .map { transform(it) }

    companion object {
        private const val CHUNK_SIZE = 3000
    }
}
