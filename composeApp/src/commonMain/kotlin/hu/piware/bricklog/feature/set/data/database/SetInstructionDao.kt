package hu.piware.bricklog.feature.set.data.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface SetInstructionDao {

    @Query("SELECT * FROM set_instructions WHERE setId = :setId")
    suspend fun getInstructions(setId: Int): List<SetInstructionEntity>

    @Upsert
    suspend fun upsertInstructions(images: List<SetInstructionEntity>)

    @Query("DELETE FROM set_instructions WHERE setId = :setId")
    suspend fun deleteInstructions(setId: Int)
}
