package hu.piware.bricklog.feature.set.data.database

import androidx.sqlite.SQLiteException
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.set.domain.datasource.LocalSetInstructionDataSource
import hu.piware.bricklog.feature.set.domain.model.Instruction

class RoomLocalSetInstructionDataSource(
    private val dao: SetInstructionDao,
) : LocalSetInstructionDataSource {

    override suspend fun updateInstructions(
        setId: Int,
        instructions: List<Instruction>,
    ): EmptyResult<DataError.Local> {
        return try {
            dao.upsertAll(instructions.map { it.toEntity(setId) })
            Result.Success(Unit)
        } catch (e: SQLiteException) {
            Result.Error(DataError.Local.DISK_FULL)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun getInstructions(setId: Int): Result<List<Instruction>, DataError.Local> {
        return try {
            val images = dao.getInstructions(setId).map { it.toDomainModel() }
            Result.Success(images)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun deleteInstructions(setId: Int): EmptyResult<DataError.Local> {
        return try {
            dao.deleteInstructions(setId)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }
}
