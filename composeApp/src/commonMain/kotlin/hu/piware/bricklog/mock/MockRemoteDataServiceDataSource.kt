package hu.piware.bricklog.mock

import co.touchlab.kermit.Logger
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.set.domain.datasource.RemoteDataServiceDataSource
import hu.piware.bricklog.feature.set.domain.model.BatchExportInfo
import hu.piware.bricklog.feature.set.domain.model.Collectible
import hu.piware.bricklog.feature.set.domain.model.ExportBatch
import hu.piware.bricklog.feature.set.domain.model.ExportInfo
import hu.piware.bricklog.feature.set.domain.model.FileUploadResult
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class MockRemoteDataServiceDataSource : RemoteDataServiceDataSource {

    private val logger = Logger.withTag("MockRemoteDataServiceDataSource")

    override suspend fun getExportInfo(): Result<ExportInfo, DataError> {
        logger.w("Using mock implementation")

        return Result.Success(
            ExportInfo(
                id = 1,
                fileUploads = listOf(
                    FileUploadResult(
                        serviceId = "1",
                        url = "",
                        fileId = "",
                        priority = 1
                    )
                ),
                lastUpdated = Clock.System.now()
            )
        )
    }

    override suspend fun getBatchExportInfo(): Result<BatchExportInfo, DataError> {
        logger.w("Using mock implementation")

        return Result.Success(
            BatchExportInfo(
                batches = listOf(
                    ExportBatch(
                        validFrom = Instant.DISTANT_PAST,
                        validTo = Clock.System.now(),
                        rowCount = 0,
                        fileUploads = listOf(
                            FileUploadResult(
                                serviceId = "mockService",
                                url = "",
                                fileId = "",
                                priority = 1
                            )
                        )
                    )
                )
            )
        )
    }

    override suspend fun getCollectibles(): Result<List<Collectible>, DataError> {
        return Result.Success(emptyList())
    }
}
