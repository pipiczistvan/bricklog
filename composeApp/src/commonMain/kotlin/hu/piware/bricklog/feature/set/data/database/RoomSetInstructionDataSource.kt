package hu.piware.bricklog.feature.set.data.database

import androidx.sqlite.SQLiteException
import hu.piware.bricklog.feature.core.data.database.BricklogDatabase
import hu.piware.bricklog.feature.core.data.network.logger
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.set.domain.datasource.LocalSetInstructionDataSource
import hu.piware.bricklog.feature.set.domain.model.Instruction
import org.koin.core.annotation.Single

@Single
class RoomSetInstructionDataSource(
    database: BricklogDatabase,
) : LocalSetInstructionDataSource {

    private val dao = database.setInstructionsDao

    override suspend fun getInstructions(setId: Int): Result<List<Instruction>, DataError.Local> {
        return try {
            logger.d { "Getting instructions" }
            val images = dao.getInstructions(setId).map { it.toDomainModel() }
            Result.Success(images)
        } catch (e: Exception) {
            logger.e(e) { "Error getting instructions" }
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun upsertInstructions(
        setId: Int,
        instructions: List<Instruction>,
    ): EmptyResult<DataError.Local> {
        return try {
            logger.d { "Storing instructions" }
            dao.upsertInstructions(instructions.map { it.toEntity(setId) })
            Result.Success(Unit)
        } catch (e: SQLiteException) {
            logger.e(e) { "Error storing instructions" }
            Result.Error(DataError.Local.DISK_FULL)
        } catch (e: Exception) {
            logger.e(e) { "Error storing instructions" }
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun deleteInstructions(setId: Int): EmptyResult<DataError.Local> {
        return try {
            logger.d { "Deleting instructions" }
            dao.deleteInstructions(setId)
            Result.Success(Unit)
        } catch (e: Exception) {
            logger.e(e) { "Error deleting instructions" }
            Result.Error(DataError.Local.UNKNOWN)
        }
    }
}
