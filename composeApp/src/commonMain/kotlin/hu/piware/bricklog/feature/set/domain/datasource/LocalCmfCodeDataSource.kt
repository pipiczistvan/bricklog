package hu.piware.bricklog.feature.set.domain.datasource

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.set.domain.model.CmfCode
import hu.piware.bricklog.feature.set.domain.model.SetId
import kotlinx.coroutines.flow.Flow

interface LocalCmfCodeDataSource {

    fun watchCmfCode(code: String): Flow<CmfCode?>

    fun watchCmfSeriesSetIds(): Flow<List<SetId>>

    suspend fun upsertCmfCodes(codes: List<CmfCode>): EmptyResult<DataError.Local>
}
