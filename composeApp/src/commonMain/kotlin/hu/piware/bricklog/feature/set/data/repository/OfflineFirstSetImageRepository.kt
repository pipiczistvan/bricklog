package hu.piware.bricklog.feature.set.data.repository

import co.touchlab.kermit.Logger
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.domain.data
import hu.piware.bricklog.feature.core.domain.onError
import hu.piware.bricklog.feature.set.domain.datasource.LocalSetImageDataSource
import hu.piware.bricklog.feature.set.domain.datasource.RemoteSetImageDataSource
import hu.piware.bricklog.feature.set.domain.model.Image
import hu.piware.bricklog.feature.set.domain.repository.SetImageRepository
import org.koin.core.annotation.Single

@Single
class OfflineFirstSetImageRepository(
    private val remoteDataSource: RemoteSetImageDataSource,
    private val localDataSource: LocalSetImageDataSource,
) : SetImageRepository {

    private val logger = Logger.withTag("OfflineFirstSetImageRepository")

    override suspend fun getAdditionalImages(
        setId: Int,
        forceUpdate: Boolean,
    ): Result<List<Image>, DataError> {
        if (forceUpdate) {
            logger.d { "Force update enabled. Deleting additional images <setId=$setId>." }
            localDataSource.deleteImages(setId)
                .onError { return it }
        }

        logger.d { "Getting local additional images <setId=$setId>." }
        val localImages = localDataSource.getImages(setId)
            .onError { return it }
            .data()

        return if (localImages.isEmpty()) {
            logger.d { "Local additional images not found. Getting remote additional images <setId=$setId>." }
            val remoteImages = remoteDataSource.getAdditionalImages(setId)
                .onError { return it }
                .data()

            logger.d { "Storing remote additional images <setId=$setId>." }
            localDataSource.updateImages(setId, remoteImages)
                .onError { return it }

            Result.Success(remoteImages)
        } else {
            Result.Success(localImages)
        }
    }
}
