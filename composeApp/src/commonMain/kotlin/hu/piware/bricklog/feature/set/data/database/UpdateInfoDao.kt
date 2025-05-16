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

    @Query("SELECT * FROM update_info WHERE dataType = :type AND (:setId IS NULL OR setId = :setId)")
    suspend fun getByType(type: DataType, setId: Int?): UpdateInfoEntity?

    @Query("SELECT * FROM update_info WHERE dataType = :type AND (:setId IS NULL OR setId = :setId)")
    fun watchByType(type: DataType, setId: Int?): Flow<UpdateInfoEntity?>
}
