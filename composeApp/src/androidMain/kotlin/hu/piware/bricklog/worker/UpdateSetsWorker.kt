package hu.piware.bricklog.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import co.touchlab.kermit.Logger
import hu.piware.bricklog.feature.core.domain.data
import hu.piware.bricklog.feature.core.domain.onError
import hu.piware.bricklog.feature.set.domain.model.DateFilter
import hu.piware.bricklog.feature.set.domain.model.Set
import hu.piware.bricklog.feature.set.domain.usecase.GetSets
import hu.piware.bricklog.feature.set.domain.usecase.SendNewSetNotification
import hu.piware.bricklog.feature.set.domain.usecase.UpdateSets
import hu.piware.bricklog.feature.set.presentation.dashboard.utils.newSetsNotificationFilter
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UpdateSetsWorker(
    appContext: Context,
    params: WorkerParameters,
) : CoroutineWorker(appContext, params), KoinComponent {

    private val logger = Logger.withTag("UpdateSetsWorker")

    private val getSets: GetSets by inject()
    private val updateSets: UpdateSets by inject()
    private val sendNewSetNotification: SendNewSetNotification by inject()

    override suspend fun doWork(): Result {
        logger.i { "Retrieving last info complete set" }
        val lastInfoCompleteSet = getSets(
            newSetsNotificationFilter.copy(
                limit = 1
            )
        ).onError {
            logger.w { "Work failed at retrieving last info complete set" }
            return Result.failure()
        }.data().firstOrNull()

        logger.i { "Updating sets" }
        updateSets()
            .onError {
                logger.w { "Work failed at updating sets" }
                return Result.failure()
            }

        logger.i { "Retrieving new info complete sets" }
        val newSets = getSets(
            newSetsNotificationFilter.copy(
                appearanceDate = lastInfoCompleteSet.createAppearanceDateFilter()
            )
        ).onError {
            logger.w { "Work failed at retrieving new info complete sets" }
            return Result.failure()
        }.data()

        logger.i { "Sending notification if necessary" }
        sendNewSetNotification(newSets)

        logger.i { "Work executed successfully" }
        return Result.success()
    }
}

private fun Set?.createAppearanceDateFilter() = DateFilter.Custom(
    startDate = this
        ?.infoCompleteDate
        ?.toEpochMilliseconds()
        ?.plus(1)
)
