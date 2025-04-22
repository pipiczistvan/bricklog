package hu.piware.bricklog.feature.set.domain.datasource

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.set.domain.model.Instruction

interface RemoteSetInstructionDataSource {
    suspend fun getInstructions(setId: Int): Result<List<Instruction>, DataError.Remote>
}
