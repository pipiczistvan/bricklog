package hu.piware.bricklog.feature.set.data.repository

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.set.domain.datasource.RemoteDataServiceDataSource
import hu.piware.bricklog.feature.set.domain.model.ExportInfo
import hu.piware.bricklog.feature.set.domain.repository.DataServiceRepository
import org.koin.core.annotation.Single

@Single
class RemoteDataServiceRepository(
    private val remoteDataSource: RemoteDataServiceDataSource,
) : DataServiceRepository {

    override suspend fun getExportInfo(): Result<ExportInfo, DataError> {
        return remoteDataSource.getExportInfo()
    }
}
