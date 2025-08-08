package hu.piware.bricklog.feature.core.data.network

import co.touchlab.kermit.Logger
import hu.piware.bricklog.di.DownloadHttpClient
import hu.piware.bricklog.feature.core.domain.datasource.RemoteFileDataSource
import hu.piware.bricklog.feature.core.domain.flowForResult
import hu.piware.bricklog.feature.core.domain.usecase.DownloadFileProgress
import hu.piware.bricklog.feature.core.domain.usecase.DownloadFileStep.DOWNLOAD
import io.ktor.client.HttpClient
import io.ktor.client.plugins.onDownload
import io.ktor.client.request.prepareGet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Single
import kotlin.time.measureTimedValue

@Single
class KtorFileDataSource(
    @param:DownloadHttpClient private val httpClient: HttpClient,
) : RemoteFileDataSource {

    private val logger = Logger.withTag("KtorFileDataSource")

    override fun downloadWithProgress(url: String) = flowForResult {
        logger.d { "Downloading file" }
        emitProgress(DownloadFileProgress(0f, DOWNLOAD))
        val (downloadResult, downloadTimeTaken) = measureTimedValue {
            safeCall<ByteArray> {
                withContext(Dispatchers.IO) {
                    var skippedFirstEmission = false
                    var lastEmittedProgress = 0f
                    httpClient.prepareGet(url) {
                        onDownload { bytesSentTotal, contentLength ->
                            if (contentLength != null && contentLength > 0L) {
                                if (!skippedFirstEmission) {
                                    skippedFirstEmission = true
                                    return@onDownload
                                }

                                val progress = bytesSentTotal.toFloat() / contentLength
                                val progressDiff = progress - lastEmittedProgress

                                if (progressDiff > 0.1f) {
                                    lastEmittedProgress = progress

                                    emitProgress(
                                        DownloadFileProgress(
                                            progress,
                                            DOWNLOAD,
                                        ),
                                    )
                                }
                            }
                        }
                    }.execute()
                }
            }
        }
        logger.d { "Downloading file took $downloadTimeTaken" }
        downloadResult
    }
}
