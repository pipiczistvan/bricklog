package hu.piware.bricklog.feature.set.domain.usecase

import co.touchlab.kermit.Logger
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.domain.data
import hu.piware.bricklog.feature.core.domain.onError
import hu.piware.bricklog.feature.set.domain.model.DataType
import hu.piware.bricklog.feature.set.domain.model.DateFilter
import hu.piware.bricklog.feature.set.domain.model.FileUploadResult
import hu.piware.bricklog.feature.set.domain.model.Set
import hu.piware.bricklog.feature.set.domain.model.SetQueryOptions
import hu.piware.bricklog.feature.set.domain.model.SetSortOption
import hu.piware.bricklog.feature.set.domain.model.UpdateInfo
import hu.piware.bricklog.feature.set.domain.model.UpdateSetsResult
import hu.piware.bricklog.feature.set.domain.repository.DataServiceRepository
import hu.piware.bricklog.feature.set.domain.repository.SetRepository
import hu.piware.bricklog.feature.set.domain.repository.UpdateInfoRepository
import kotlinx.datetime.Clock

class UpdateSets(
    private val dataServiceRepository: DataServiceRepository,
    private val setRepository: SetRepository,
    private val updateInfoRepository: UpdateInfoRepository,
) {
    private val logger = Logger.withTag("UpdateSets")

    suspend operator fun invoke(force: Boolean = false): Result<UpdateSetsResult, DataError> {
        logger.i { "Getting export info" }
        val exportInfo = dataServiceRepository.getExportInfo()
            .onError { return@invoke it }
            .data()

        logger.i { "Getting update info" }
        val updateInfo = updateInfoRepository.getUpdateInfo(DataType.BRICKSET_SETS)
            .onError { return@invoke it }
            .data()

        val isUpdateNecessary =
            force || updateInfo == null || updateInfo.lastUpdated < exportInfo.lastUpdated

        val updateResult: UpdateSetsResult = if (isUpdateNecessary) {
            logger.i { "Set update is necessary" }
            updateSetsWithResult(exportInfo.fileUploads)
                .onError { return@invoke it }
                .data()
        } else {
            logger.i { "Set update is not necessary" }
            UpdateSetsResult(emptyList())
        }

        logger.i { "Storing update info" }
        updateInfoRepository.storeUpdateInfo(
            updateInfo = UpdateInfo(
                dataType = DataType.BRICKSET_SETS,
                lastUpdated = Clock.System.now()
            )
        )
            .onError { return@invoke it }

        return Result.Success(updateResult)
    }

    private suspend fun updateSetsWithResult(fileUploads: List<FileUploadResult>): Result<UpdateSetsResult, DataError> {
        val lastSetBeforeUpdate = setRepository.getSets(baseQueryOptions.copy(limit = 1))
            .onError { return it }
            .data()

        setRepository.updateSets(fileUploads)
            .onError { return it }

        val newSetsAfterUpdate = setRepository.getSets(
            baseQueryOptions.copy(appearanceDate = lastSetBeforeUpdate.appearanceDateFilter)
        )
            .onError { return it }
            .data()

        return Result.Success(
            UpdateSetsResult(
                newSets = newSetsAfterUpdate
            )
        )
    }

    private val List<Set>.appearanceDateFilter: DateFilter
        get() = firstOrNull()?.infoCompleteDate?.let { lastSetAppearanceDate ->
            DateFilter.Custom(
                startDate = lastSetAppearanceDate.toEpochMilliseconds() + 1
            )
        } ?: DateFilter.AnyTime

    companion object {
        private val baseQueryOptions = SetQueryOptions(
            sortOption = SetSortOption.APPEARANCE_DATE_DESCENDING,
            showIncomplete = false
        )
    }
}
