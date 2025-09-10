package hu.piware.bricklog.feature.set.domain.repository

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.set.domain.model.CmfCode
import hu.piware.bricklog.feature.set.domain.model.SetId
import kotlinx.coroutines.flow.Flow

interface CmfCodeRepository {

    fun watchCmfCode(code: String): Flow<CmfCode?>

    fun watchCmfSeriesSetIds(): Flow<List<SetId>>

    suspend fun updateCmfCodes(codes: List<CmfCode>): EmptyResult<DataError>
}
