package hu.piware.bricklog.feature.set.data.repository

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.domain.data
import hu.piware.bricklog.feature.core.domain.onError
import hu.piware.bricklog.feature.set.domain.datasource.LocalSetImageDataSource
import hu.piware.bricklog.feature.set.domain.datasource.RemoteSetImageDataSource
import hu.piware.bricklog.feature.set.domain.model.Image
import hu.piware.bricklog.feature.set.domain.repository.SetImageRepository

class OfflineFirstSetImageRepository(
    private val remoteDataSource: RemoteSetImageDataSource,
    private val localDataSource: LocalSetImageDataSource,
) : SetImageRepository {

    override suspend fun getAdditionalImages(setId: Int): Result<List<Image>, DataError> {
        val localImages = localDataSource.getImages(setId)
            .onError { return it }
            .data()

        return if (localImages.isEmpty()) {
            val remoteImages = remoteDataSource.getAdditionalImages(setId)
                .onError { return it }
                .data()

            localDataSource.updateImages(setId, remoteImages)
                .onError { return it }

            Result.Success(remoteImages)
        } else {
            Result.Success(localImages)
        }
    }
}
