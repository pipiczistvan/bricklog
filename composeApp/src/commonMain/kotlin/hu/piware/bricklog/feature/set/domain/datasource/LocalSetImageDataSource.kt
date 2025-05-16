package hu.piware.bricklog.feature.set.domain.datasource

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.set.domain.model.Image

interface LocalSetImageDataSource {

    suspend fun updateImages(setId: Int, images: List<Image>): EmptyResult<DataError.Local>

    suspend fun getImages(setId: Int): Result<List<Image>, DataError.Local>

    suspend fun deleteImages(setId: Int): EmptyResult<DataError.Local>
}
