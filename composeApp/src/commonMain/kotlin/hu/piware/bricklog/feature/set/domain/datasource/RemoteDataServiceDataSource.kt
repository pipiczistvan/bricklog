package hu.piware.bricklog.feature.set.domain.datasource

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.set.domain.model.BatchExportInfo
import hu.piware.bricklog.feature.set.domain.model.Collectible
import hu.piware.bricklog.feature.set.domain.model.ExportInfo
import kotlinx.coroutines.flow.Flow

interface RemoteDataServiceDataSource {
    suspend fun getBatchExportInfo(): Result<BatchExportInfo, DataError.Remote>
    fun watchCollectibles(): Flow<List<Collectible>>
    suspend fun getEurRateExportInfo(): Result<ExportInfo, DataError.Remote>
}
