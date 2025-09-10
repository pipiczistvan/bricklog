package hu.piware.bricklog.mock

import co.touchlab.kermit.Logger
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.set.domain.datasource.RemoteDataServiceDataSource
import hu.piware.bricklog.feature.set.domain.model.BatchExportInfo
import hu.piware.bricklog.feature.set.domain.model.ExportBatch
import hu.piware.bricklog.feature.set.domain.model.ExportInfo
import hu.piware.bricklog.feature.set.domain.model.FileUploadResult
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class MockRemoteDataServiceDataSource : RemoteDataServiceDataSource {

    private val logger = Logger.withTag("MockRemoteDataServiceDataSource")

    override suspend fun getBatchExportInfo(): Result<BatchExportInfo, DataError.Remote> {
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

    override suspend fun getEurRateExportInfo(): Result<ExportInfo, DataError.Remote> {
        logger.w("Using mock implementation")

        return Result.Success(
            ExportInfo(
                id = 1,
                fileUploads =
                    listOf(
                        FileUploadResult(
                            serviceId = "dropbox",
                            url = "https://www.dropbox.com/scl/fi/ujf1tdky5u4everoypmdx/" +
                                    "eur-rates-export.csv.gz?" +
                                    "rlkey=9zlzd29snypncal4qlnpcimz5&dl=1",
                            fileId = "eur-rates-export.csv.gz",
                            priority = 1,
                        ),
                    ),
                lastUpdated = Clock.System.now(),
            ),
        )
    }

    override suspend fun getCmfCodesExportInfo(): Result<ExportInfo, DataError.Remote> {
        logger.w("Using mock implementation")

        return Result.Success(
            ExportInfo(
                id = 1,
                fileUploads =
                    listOf(
                        FileUploadResult(
                            serviceId = "dropbox",
                            url = "https://www.dropbox.com/scl/fi/iloa1quh6djcihs8tq5la/" +
                                    "cmf-codes-export.json.gz?" +
                                    "rlkey=apj2jjtrg378vye83mewp1ylb&st=shu6hv2r&dl=1",
                            fileId = "cmf-codes-export.json.gz",
                            priority = 1,
                        ),
                    ),
                lastUpdated = Clock.System.now(),
            ),
        )
    }
}
