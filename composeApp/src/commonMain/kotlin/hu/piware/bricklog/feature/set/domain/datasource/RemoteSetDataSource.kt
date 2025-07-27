package hu.piware.bricklog.feature.set.domain.datasource

import hu.piware.bricklog.feature.core.domain.FlowForResult
import hu.piware.bricklog.feature.set.domain.model.UpdateSetsProgress

interface RemoteSetDataSource {
    fun downloadCompressedCsv(url: String): FlowForResult<ByteArray, UpdateSetsProgress>
}
