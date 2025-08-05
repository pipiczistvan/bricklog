package hu.piware.bricklog.feature.set.data.repository

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.set.domain.datasource.RemoteDataServiceDataSource
import hu.piware.bricklog.feature.set.domain.model.BatchExportInfo
import hu.piware.bricklog.feature.set.domain.model.Collectible
import hu.piware.bricklog.feature.set.domain.model.ExportInfo
import hu.piware.bricklog.feature.set.domain.repository.DataServiceRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class RemoteDataServiceRepository(
    private val remoteDataSource: RemoteDataServiceDataSource,
) : DataServiceRepository {

    override suspend fun getBatchExportInfo(): Result<BatchExportInfo, DataError> {
        return remoteDataSource.getBatchExportInfo()
    }

    override fun watchCollectibles(): Flow<List<Collectible>> {
        return remoteDataSource.watchCollectibles()
    }

    override suspend fun getEurRateExportInfo(): Result<ExportInfo, DataError> {
        return remoteDataSource.getEurRateExportInfo()
    }
}
