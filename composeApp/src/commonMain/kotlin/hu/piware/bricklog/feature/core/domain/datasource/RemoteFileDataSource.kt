package hu.piware.bricklog.feature.core.domain.datasource

import hu.piware.bricklog.feature.core.domain.FlowForResult
import hu.piware.bricklog.feature.core.domain.usecase.DownloadFileProgress

interface RemoteFileDataSource {
    fun downloadWithProgress(url: String): FlowForResult<ByteArray, DownloadFileProgress>
}
