package hu.piware.bricklog.feature.set.domain.repository

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.set.domain.model.BatchExportInfo
import hu.piware.bricklog.feature.set.domain.model.ExportInfo

interface DataServiceRepository {

    suspend fun getBatchExportInfo(): Result<BatchExportInfo, DataError>

    suspend fun getEurRateExportInfo(): Result<ExportInfo, DataError>

    suspend fun getCmfCodesExportInfo(): Result<ExportInfo, DataError>
}
