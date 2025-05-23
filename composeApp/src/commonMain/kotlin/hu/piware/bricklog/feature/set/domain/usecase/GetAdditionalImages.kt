package hu.piware.bricklog.feature.set.domain.usecase

import co.touchlab.kermit.Logger
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.domain.data
import hu.piware.bricklog.feature.core.domain.onError
import hu.piware.bricklog.feature.set.domain.model.DataType
import hu.piware.bricklog.feature.set.domain.model.Image
import hu.piware.bricklog.feature.set.domain.model.UpdateInfo
import hu.piware.bricklog.feature.set.domain.repository.SetImageRepository
import hu.piware.bricklog.feature.set.domain.repository.UpdateInfoRepository
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.koin.core.annotation.Single
import kotlin.time.Duration.Companion.days

@Single
class GetAdditionalImages(
    private val setImageRepository: SetImageRepository,
    private val updateInfoRepository: UpdateInfoRepository,
) {
    private val logger = Logger.withTag("GetAdditionalImages")

    suspend operator fun invoke(setId: Int): Result<List<Image>, DataError> {
        logger.d { "Getting update info." }
        val updateInfo = updateInfoRepository.getUpdateInfo(DataType.ADDITIONAL_IMAGES, setId)
            .onError { return it }
            .data()

        val isUpdateNeeded = updateInfo?.lastUpdated?.isObsolete() ?: true

        if (isUpdateNeeded) {
            logger.d { "Stored set additional images are obsolete." }
        }

        logger.d { "Getting additional images." }
        val additionalImages = setImageRepository.getAdditionalImages(setId, isUpdateNeeded)
            .onError { return it }
            .data()

        if (isUpdateNeeded) {
            logger.d { "Storing update info." }
            updateInfoRepository.storeUpdateInfo(
                UpdateInfo(
                    dataType = DataType.ADDITIONAL_IMAGES,
                    setId = setId,
                    lastUpdated = Clock.System.now()
                )
            ).onError { return it }
        }

        return Result.Success(additionalImages)
    }
}

private fun Instant.isObsolete() = this < Clock.System.now() - 7.days
