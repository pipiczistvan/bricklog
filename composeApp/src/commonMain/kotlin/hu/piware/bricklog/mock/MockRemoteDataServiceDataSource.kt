package hu.piware.bricklog.mock

import co.touchlab.kermit.Logger
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.set.domain.datasource.RemoteDataServiceDataSource
import hu.piware.bricklog.feature.set.domain.model.BatchExportInfo
import hu.piware.bricklog.feature.set.domain.model.Collectible
import hu.piware.bricklog.feature.set.domain.model.ExportBatch
import hu.piware.bricklog.feature.set.domain.model.FileUploadResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class MockRemoteDataServiceDataSource : RemoteDataServiceDataSource {

    private val logger = Logger.withTag("MockRemoteDataServiceDataSource")

    override suspend fun getBatchExportInfo(): Result<BatchExportInfo, DataError> {
        logger.w("Using mock implementation")

        return Result.Success(
            BatchExportInfo(
                batches = listOf(
                    ExportBatch(
                        validFrom = Instant.DISTANT_PAST,
                        validTo = Clock.System.now(),
                        rowCount = 1000,
                        fileUploads = listOf(
                            FileUploadResult(
                                serviceId = "dropbox",
                                url = "https://www.dropbox.com/scl/fi/iapqpnz0ckubj26doyyxo/" +
                                        "bricklog-data-export-mock.csv.gz?" +
                                        "rlkey=kvfvy1dcxxny75y75knth4yse&dl=1",
                                fileId = "bricklog-data-export-mock.csv.gz",
                                priority = 1,
                            ),
                        ),
                    ),
                ),
            ),
        )
    }

    override fun watchCollectibles(): Flow<List<Collectible>> {
        return emptyFlow()
    }
}
