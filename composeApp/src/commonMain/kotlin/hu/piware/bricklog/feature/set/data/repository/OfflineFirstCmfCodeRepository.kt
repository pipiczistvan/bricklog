package hu.piware.bricklog.feature.set.data.repository

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.set.domain.datasource.LocalCmfCodeDataSource
import hu.piware.bricklog.feature.set.domain.model.CmfCode
import hu.piware.bricklog.feature.set.domain.model.SetId
import hu.piware.bricklog.feature.set.domain.repository.CmfCodeRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class OfflineFirstCmfCodeRepository(
    private val localDataSource: LocalCmfCodeDataSource,
) : CmfCodeRepository {

    override fun watchCmfCode(code: String): Flow<CmfCode?> {
        return localDataSource.watchCmfCode(code)
    }

    override fun watchCmfSeriesSetIds(): Flow<List<SetId>> {
        return localDataSource.watchCmfSeriesSetIds()
    }

    override suspend fun updateCmfCodes(codes: List<CmfCode>): EmptyResult<DataError> {
        return localDataSource.upsertCmfCodes(codes)
    }
}
