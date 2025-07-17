package hu.piware.bricklog.feature.set.domain.usecase

import co.touchlab.kermit.Logger
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.domain.data
import hu.piware.bricklog.feature.core.domain.onError
import hu.piware.bricklog.feature.set.domain.model.DateFilter
import hu.piware.bricklog.feature.set.domain.model.Set
import hu.piware.bricklog.feature.set.presentation.dashboard.utils.newItemsFilter
import org.koin.core.annotation.Single

@Single
class SyncSets(
    private val getSetDetails: GetSetDetails,
    private val updateSets: UpdateSets,
    private val sendNewSetNotification: SendNewSetNotification,
) {
    private val logger = Logger.withTag("SyncSets")

    private var inProgress: Boolean = false

    suspend operator fun invoke(): EmptyResult<DataError> {
        if (inProgress) {
            logger.w { "Sync already in progress" }
            return Result.Error(DataError.Local.BUSY)
        } else {
            inProgress = true
        }

        logger.i { "Retrieving last info complete set" }
        val lastInfoCompleteSet = getSetDetails(
            newItemsFilter.copy(
                limit = 1
            )
        ).onError {
            logger.w { "Retrieving last info complete set failed" }
            inProgress = false
            return it
        }.data().firstOrNull()

        logger.i { "Updating sets" }
        updateSets()
            .onError {
                logger.w { "Updating sets failed" }
                inProgress = false
                return it
            }

        logger.i { "Retrieving new info complete sets" }
        val newSets = getSetDetails(
            newItemsFilter.copy(
                appearanceDate = lastInfoCompleteSet?.set.createAppearanceDateFilter()
            )
        ).onError {
            logger.w { "Retrieving new info complete sets failed" }
            inProgress = false
            return it
        }.data()

        logger.i { "Sending notification if necessary" }
        sendNewSetNotification(newSets)

        logger.i { "Sync executed successfully" }
        inProgress = false
        return Result.Success(Unit)
    }
}

private fun Set?.createAppearanceDateFilter() = DateFilter.Custom(
    startDate = this
        ?.infoCompleteDate
        ?.toEpochMilliseconds()
        ?.plus(1)
)
