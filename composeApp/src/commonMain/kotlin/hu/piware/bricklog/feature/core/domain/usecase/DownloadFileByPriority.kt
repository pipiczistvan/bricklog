package hu.piware.bricklog.feature.core.domain.usecase

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.domain.UpdateProgress
import hu.piware.bricklog.feature.core.domain.await
import hu.piware.bricklog.feature.core.domain.flowForResult
import hu.piware.bricklog.feature.core.domain.repository.FileRepository
import hu.piware.bricklog.feature.set.domain.model.FileUploadResult
import org.koin.core.annotation.Single

@Single
class DownloadFileByPriority(
    private val fileRepository: FileRepository,
) {
    operator fun invoke(fileUploads: List<FileUploadResult>) = flowForResult {
        val orderedFileUploads = fileUploads.sortedBy { it.priority }

        for (fileUpload in orderedFileUploads) {
            val downloadResult = await {
                fileRepository.downloadWithProgress(fileUpload.url)
            }
            if (downloadResult is Result.Success) {
                return@flowForResult downloadResult
            }
        }

        Result.Error(DataError.Remote.UNKNOWN)
    }
}

typealias DownloadFileProgress = UpdateProgress<DownloadFileStep>

enum class DownloadFileStep {
    DOWNLOAD,
}
