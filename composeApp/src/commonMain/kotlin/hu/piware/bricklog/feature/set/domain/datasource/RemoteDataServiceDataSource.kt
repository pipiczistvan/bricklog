package hu.piware.bricklog.feature.set.domain.datasource

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.set.domain.model.ExportInfo

interface RemoteDataServiceDataSource {
    suspend fun getExportInfo(): Result<ExportInfo, DataError>
}
