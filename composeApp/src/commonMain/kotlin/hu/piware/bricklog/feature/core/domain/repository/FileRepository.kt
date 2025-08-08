package hu.piware.bricklog.feature.core.domain.repository

import hu.piware.bricklog.feature.core.domain.FlowForResult
import hu.piware.bricklog.feature.core.domain.usecase.DownloadFileProgress

interface FileRepository {
    fun downloadWithProgress(url: String): FlowForResult<ByteArray, DownloadFileProgress>
}
