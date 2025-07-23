package hu.piware.bricklog.feature.set.data.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import hu.piware.bricklog.feature.set.domain.model.DataType
import kotlinx.coroutines.flow.Flow

@Dao
interface UpdateInfoDao {

    @Query("SELECT * FROM update_info WHERE dataType = :type AND (:setId IS NULL OR setId = :setId)")
    fun watchUpsertInfo(type: DataType, setId: Int?): Flow<UpdateInfoEntity?>

    @Upsert
    suspend fun upsertUpdateInfo(updateInfo: UpdateInfoEntity)
}
