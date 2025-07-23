package hu.piware.bricklog.feature.set.domain.datasource

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.set.domain.model.Instruction

interface LocalSetInstructionDataSource {

    suspend fun getInstructions(setId: Int): Result<List<Instruction>, DataError.Local>

    suspend fun upsertInstructions(
        setId: Int,
        instructions: List<Instruction>,
    ): EmptyResult<DataError.Local>

    suspend fun deleteInstructions(setId: Int): EmptyResult<DataError.Local>
}
