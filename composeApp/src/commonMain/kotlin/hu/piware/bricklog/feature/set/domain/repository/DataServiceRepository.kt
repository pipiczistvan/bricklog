package hu.piware.bricklog.feature.set.domain.repository

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.set.domain.model.BatchExportInfo
import hu.piware.bricklog.feature.set.domain.model.ExportInfo

interface DataServiceRepository {
    suspend fun getExportInfo(): Result<ExportInfo, DataError>
    suspend fun getBatchExportInfo(): Result<BatchExportInfo, DataError>
}
