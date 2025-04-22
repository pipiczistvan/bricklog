@file:OptIn(ExperimentalResourceApi::class)

package hu.piware.bricklog.mock

import bricklog.composeapp.generated.resources.Res
import co.touchlab.kermit.Logger
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.set.domain.datasource.RemoteSetDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.ExperimentalResourceApi

class MockRemoteSetDataSource : RemoteSetDataSource {

    private val logger = Logger.withTag("MockRemoteSetDataSource")

    override suspend fun downloadSetsCsv(url: String): Result<ByteArray, DataError.Remote> {
        logger.w("Using mock implementation")

        return withContext(Dispatchers.IO) {
            try {
                val csvBytes = Res.readBytes("files/mock-data-export.csv")
                Result.Success(csvBytes)
            } catch (e: Exception) {
                logger.e("Failed to read mock csv file", e)
                Result.Error(DataError.Remote.UNKNOWN)
            }
        }
    }
}
