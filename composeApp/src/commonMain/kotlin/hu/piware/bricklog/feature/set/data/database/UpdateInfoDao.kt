package hu.piware.bricklog.feature.set.data.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import hu.piware.bricklog.feature.set.domain.model.DataType
import kotlinx.coroutines.flow.Flow

@Dao
interface UpdateInfoDao {

    @Upsert
    suspend fun upsert(updateInfo: UpdateInfoEntity)

    @Query("SELECT * FROM update_info WHERE dataType = :type")
    suspend fun getByType(type: DataType): UpdateInfoEntity?

    @Query("SELECT * FROM update_info WHERE dataType = :type")
    fun watchByType(type: DataType): Flow<UpdateInfoEntity?>
}
