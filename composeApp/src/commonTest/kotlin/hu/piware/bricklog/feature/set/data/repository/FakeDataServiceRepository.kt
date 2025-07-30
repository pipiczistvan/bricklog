package hu.piware.bricklog.feature.set.data.repository

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.set.domain.model.BatchExportInfo
import hu.piware.bricklog.feature.set.domain.model.Collectible
import hu.piware.bricklog.feature.set.domain.model.ExportBatch
import hu.piware.bricklog.feature.set.domain.repository.DataServiceRepository
import hu.piware.bricklog.mock.PreviewData
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

class FakeDataServiceRepository : DataServiceRepository {

    override suspend fun getBatchExportInfo(): Result<BatchExportInfo, DataError> {
        return Result.Success(
            BatchExportInfo(
                batches = listOf(
                    ExportBatch(
                        validFrom = Instant.DISTANT_PAST,
                        validTo = Instant.DISTANT_FUTURE,
                        rowCount = PreviewData.sets.size,
                        fileUploads = listOf()
                    )
                )
            )
        )
    }

    override fun watchCollectibles(): Flow<List<Collectible>> {
        TODO("Not yet implemented")
    }
}
