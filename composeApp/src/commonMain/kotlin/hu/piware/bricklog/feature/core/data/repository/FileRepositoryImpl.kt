package hu.piware.bricklog.feature.core.data.repository

import hu.piware.bricklog.feature.core.domain.FlowForResult
import hu.piware.bricklog.feature.core.domain.datasource.RemoteFileDataSource
import hu.piware.bricklog.feature.core.domain.repository.FileRepository
import hu.piware.bricklog.feature.core.domain.usecase.DownloadFileProgress
import org.koin.core.annotation.Single

@Single
class FileRepositoryImpl(
    private val remoteDataSource: RemoteFileDataSource,
) : FileRepository {

    override fun downloadWithProgress(url: String): FlowForResult<ByteArray, DownloadFileProgress> {
        return remoteDataSource.downloadWithProgress(url)
    }
}
