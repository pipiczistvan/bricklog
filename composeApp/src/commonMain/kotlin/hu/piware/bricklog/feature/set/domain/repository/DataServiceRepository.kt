package hu.piware.bricklog.feature.set.domain.repository

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.set.domain.model.BatchExportInfo
import hu.piware.bricklog.feature.set.domain.model.Collectible

interface DataServiceRepository {
    suspend fun getBatchExportInfo(): Result<BatchExportInfo, DataError>
    suspend fun getCollectibles(): Result<List<Collectible>, DataError>
}
