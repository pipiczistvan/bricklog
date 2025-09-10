package hu.piware.bricklog.feature.set.domain.datasource

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.set.domain.model.BatchExportInfo
import hu.piware.bricklog.feature.set.domain.model.ExportInfo

interface RemoteDataServiceDataSource {
    suspend fun getBatchExportInfo(): Result<BatchExportInfo, DataError.Remote>

    suspend fun getEurRateExportInfo(): Result<ExportInfo, DataError.Remote>

    suspend fun getCmfCodesExportInfo(): Result<ExportInfo, DataError.Remote>
}
