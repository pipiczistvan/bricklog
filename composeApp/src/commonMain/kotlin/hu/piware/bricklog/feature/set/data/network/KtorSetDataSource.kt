package hu.piware.bricklog.feature.set.data.network

import co.touchlab.kermit.Logger
import hu.piware.bricklog.di.DownloadHttpClient
import hu.piware.bricklog.feature.core.data.network.safeCall
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.set.domain.datasource.RemoteSetDataSource
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import korlibs.io.compression.deflate.GZIP
import korlibs.io.compression.uncompress
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Single
import kotlin.time.measureTimedValue

@Single
class KtorSetDataSource(
    @param:DownloadHttpClient private val httpClient: HttpClient,
) : RemoteSetDataSource {
    private val logger = Logger.withTag("KtorRemoteSetDataSource")

    override suspend fun downloadSetsCsv(url: String): Result<ByteArray, DataError.Remote> {
        return try {
            logger.d { "Downloading sets zip" }
            val (downloadResult, downloadTimeTaken) = measureTimedValue {
                safeCall<ByteArray> {
                    withContext(Dispatchers.IO) {
                        httpClient.get(url)
                    }
                }
            }
            if (downloadResult is Result.Error) {
                Logger.w { "Downloading sets zip failed" }
                return downloadResult
            }
            logger.d { "Downloading sets zip took $downloadTimeTaken" }

            withContext(Dispatchers.Default) {
                logger.d { "Uncompressing zip file" }
                val zipBytes = (downloadResult as Result.Success).data
                val (csvBytes, uncompressTimeTaken) = measureTimedValue { zipBytes.uncompress(method = GZIP) }
                logger.d { "Uncompressing zip file took $uncompressTimeTaken" }

                Result.Success(csvBytes)
            }
        } catch (e: Exception) {
            logger.e("Failed to download and parse sets", e)
            Result.Error(DataError.Remote.UNKNOWN)
        }
    }
}
