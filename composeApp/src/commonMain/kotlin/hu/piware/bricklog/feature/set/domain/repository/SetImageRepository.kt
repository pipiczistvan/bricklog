package hu.piware.bricklog.feature.set.domain.repository

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.set.domain.model.Image

interface SetImageRepository {
    suspend fun getAdditionalImages(
        setId: Int,
        forceUpdate: Boolean,
    ): Result<List<Image>, DataError>
}
