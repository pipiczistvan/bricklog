package hu.piware.bricklog.mock

import co.touchlab.kermit.Logger
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.set.domain.datasource.RemoteSetImageDataSource
import hu.piware.bricklog.feature.set.domain.model.Image

class MockRemoteSetImageDataSource : RemoteSetImageDataSource {

    private val logger = Logger.withTag("MockRemoteSetImageDataSource")

    override suspend fun getAdditionalImages(setId: Int): Result<List<Image>, DataError.Remote> {
        logger.w("Using mock implementation")
        return Result.Success(emptyList())
    }
}
