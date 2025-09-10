package hu.piware.bricklog.feature.set.data.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import hu.piware.bricklog.feature.set.domain.model.SetId
import kotlinx.coroutines.flow.Flow

@Dao
interface CmfCodeDao {

    @Query("SELECT * FROM cmf_codes WHERE code = :code")
    fun watchCmfCode(code: String): Flow<CmfCodeEntity?>

    @Query("SELECT DISTINCT seriesSetId FROM cmf_codes")
    fun watchCmfSeriesSetIds(): Flow<List<SetId>>

    @Upsert
    suspend fun upsertCmfCodes(cmfCodes: List<CmfCodeEntity>)
}
