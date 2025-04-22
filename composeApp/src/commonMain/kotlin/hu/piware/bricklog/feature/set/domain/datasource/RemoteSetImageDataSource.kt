package hu.piware.bricklog.feature.set.domain.datasource

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.set.domain.model.Image

interface RemoteSetImageDataSource {
    suspend fun getAdditionalImages(setId: Int): Result<List<Image>, DataError.Remote>
}
