package hu.piware.bricklog.feature.set.domain.datasource

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.Result

interface RemoteSetDataSource {
    suspend fun downloadSetsCsv(url: String): Result<ByteArray, DataError.Remote>
}
