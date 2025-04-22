package hu.piware.bricklog.feature.set.data.repository

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.domain.data
import hu.piware.bricklog.feature.core.domain.onError
import hu.piware.bricklog.feature.set.domain.datasource.LocalSetInstructionDataSource
import hu.piware.bricklog.feature.set.domain.datasource.RemoteSetInstructionDataSource
import hu.piware.bricklog.feature.set.domain.model.Instruction
import hu.piware.bricklog.feature.set.domain.repository.SetInstructionRepository

class OfflineFirstSetInstructionRepository(
    private val remoteDataSource: RemoteSetInstructionDataSource,
    private val localDataSource: LocalSetInstructionDataSource,
) : SetInstructionRepository {

    override suspend fun getInstructions(setId: Int): Result<List<Instruction>, DataError> {
        val localInstructions = localDataSource.getInstructions(setId)
            .onError { return it }
            .data()

        return if (localInstructions.isEmpty()) {
            val remoteInstructions = remoteDataSource.getInstructions(setId)
                .onError { return it }
                .data()

            localDataSource.updateInstructions(setId, remoteInstructions)
                .onError { return it }

            Result.Success(remoteInstructions)
        } else {
            Result.Success(localInstructions)
        }
    }
}
