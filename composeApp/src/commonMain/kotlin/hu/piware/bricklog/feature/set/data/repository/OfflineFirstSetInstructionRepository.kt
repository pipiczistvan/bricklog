package hu.piware.bricklog.feature.set.data.repository

import co.touchlab.kermit.Logger
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.domain.data
import hu.piware.bricklog.feature.core.domain.onError
import hu.piware.bricklog.feature.set.domain.datasource.LocalSetInstructionDataSource
import hu.piware.bricklog.feature.set.domain.datasource.RemoteSetInstructionDataSource
import hu.piware.bricklog.feature.set.domain.model.Instruction
import hu.piware.bricklog.feature.set.domain.repository.SetInstructionRepository
import org.koin.core.annotation.Single

@Single
class OfflineFirstSetInstructionRepository(
    private val remoteDataSource: RemoteSetInstructionDataSource,
    private val localDataSource: LocalSetInstructionDataSource,
) : SetInstructionRepository {

    private val logger = Logger.withTag("OfflineFirstSetInstructionRepository")

    override suspend fun getInstructions(
        setId: Int,
        forceUpdate: Boolean,
    ): Result<List<Instruction>, DataError> {
        if (forceUpdate) {
            logger.d { "Force update enabled. Deleting instructions <setId=$setId>." }
            localDataSource.deleteInstructions(setId)
                .onError { return it }
        }

        logger.d { "Getting local instructions <setId=$setId>." }
        val localInstructions = localDataSource.getInstructions(setId)
            .onError { return it }
            .data()

        return if (localInstructions.isEmpty()) {
            logger.d { "Local instructions not found. Getting remote instructions <setId=$setId>." }
            val remoteInstructions = remoteDataSource.getInstructions(setId)
                .onError { return it }
                .data()

            logger.d { "Storing remote instructions <setId=$setId>." }
            localDataSource.upsertInstructions(setId, remoteInstructions)
                .onError { return it }

            Result.Success(remoteInstructions)
        } else {
            Result.Success(localInstructions)
        }
    }
}
