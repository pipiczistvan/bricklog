package hu.piware.bricklog.feature.set.domain.repository

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.set.domain.model.Instruction

interface SetInstructionRepository {

    suspend fun getInstructions(
        setId: Int,
        forceUpdate: Boolean,
    ): Result<List<Instruction>, DataError>
}
