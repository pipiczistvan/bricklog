package hu.piware.bricklog.feature.set.domain.usecase

import co.touchlab.kermit.Logger
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.domain.data
import hu.piware.bricklog.feature.core.domain.onError
import hu.piware.bricklog.feature.set.domain.model.DataType
import hu.piware.bricklog.feature.set.domain.model.Instruction
import hu.piware.bricklog.feature.set.domain.model.UpdateInfo
import hu.piware.bricklog.feature.set.domain.repository.SetInstructionRepository
import hu.piware.bricklog.feature.set.domain.repository.UpdateInfoRepository
import hu.piware.bricklog.util.asResultOrNull
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.koin.core.annotation.Single
import kotlin.time.Duration.Companion.days

@Single
class GetInstructions(
    private val setInstructionRepository: SetInstructionRepository,
    private val updateInfoRepository: UpdateInfoRepository,
) {
    private val logger = Logger.withTag("GetInstructions")

    suspend operator fun invoke(setId: Int): Result<List<Instruction>, DataError> {
        logger.d { "Getting update info for instructions." }
        val updateInfo = updateInfoRepository.watchUpdateInfo(DataType.INSTRUCTIONS, setId)
            .asResultOrNull()
            .onError { return it }
            .data()

        val isUpdateNeeded = updateInfo?.lastUpdated?.isObsolete() ?: true

        if (isUpdateNeeded) {
            logger.d { "Stored set instructions are obsolete." }
        }

        logger.d { "Getting instructions." }
        val instructions = setInstructionRepository.getInstructions(setId, isUpdateNeeded)
            .onError { return it }
            .data()

        if (isUpdateNeeded) {
            logger.d { "Storing update info." }
            updateInfoRepository.saveUpdateInfo(
                UpdateInfo(
                    dataType = DataType.INSTRUCTIONS,
                    setId = setId,
                    lastUpdated = Clock.System.now(),
                ),
            ).onError { return it }
        }

        return Result.Success(instructions)
    }
}

private fun Instant.isObsolete() = this < Clock.System.now() - 7.days
