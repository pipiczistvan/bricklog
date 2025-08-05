package hu.piware.bricklog.feature.currency.data.network

import co.touchlab.kermit.Logger
import hu.piware.bricklog.di.DownloadHttpClient
import hu.piware.bricklog.feature.core.data.network.safeCall
import hu.piware.bricklog.feature.core.domain.flowForResult
import hu.piware.bricklog.feature.currency.domain.datasource.RemoteCurrencyDataSource
import hu.piware.bricklog.feature.currency.domain.model.UpdateRatesProgress
import hu.piware.bricklog.feature.currency.domain.model.UpdateRatesStep
import io.ktor.client.HttpClient
import io.ktor.client.plugins.onDownload
import io.ktor.client.request.prepareGet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Single
import kotlin.time.measureTimedValue

@Single
class KtorCurrencyDataSource(
    @param:DownloadHttpClient private val httpClient: HttpClient,
) : RemoteCurrencyDataSource {

    private val logger = Logger.withTag("KtorCurrencyDataSource")

    override fun downloadCompressedCsv(url: String) = flowForResult {
        logger.d { "Downloading currency rates zip" }
        emitProgress(UpdateRatesProgress(0f, UpdateRatesStep.DOWNLOAD_FILE))
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
                                        UpdateRatesProgress(
                                            progress,
                                            UpdateRatesStep.DOWNLOAD_FILE,
                                        ),
                                    )
                                }
                            }
                        }
                    }.execute()
                }
            }
        }
        logger.d { "Downloading currency rates zip took $downloadTimeTaken" }
        downloadResult
    }
}
