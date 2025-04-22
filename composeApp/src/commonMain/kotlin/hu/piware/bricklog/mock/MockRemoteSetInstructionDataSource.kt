package hu.piware.bricklog.mock

import co.touchlab.kermit.Logger
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.set.domain.datasource.RemoteSetInstructionDataSource
import hu.piware.bricklog.feature.set.domain.model.Instruction

class MockRemoteSetInstructionDataSource : RemoteSetInstructionDataSource {

    private val logger = Logger.withTag("MockRemoteSetInstructionDataSource")

    override suspend fun getInstructions(setId: Int): Result<List<Instruction>, DataError.Remote> {
        logger.w("Using mock implementation")
        return Result.Success(emptyList())
    }
}
