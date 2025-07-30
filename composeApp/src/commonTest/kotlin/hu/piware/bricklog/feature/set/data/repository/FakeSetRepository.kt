package hu.piware.bricklog.feature.set.data.repository

import androidx.paging.PagingData
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.FlowForResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.domain.flowForResult
import hu.piware.bricklog.feature.set.domain.model.ExportBatch
import hu.piware.bricklog.feature.set.domain.model.Set
import hu.piware.bricklog.feature.set.domain.model.SetDetails
import hu.piware.bricklog.feature.set.domain.model.SetQueryOptions
import hu.piware.bricklog.feature.set.domain.model.SetThemeGroup
import hu.piware.bricklog.feature.set.domain.model.UpdateSetsProgress
import hu.piware.bricklog.feature.set.domain.repository.SetRepository
import hu.piware.bricklog.mock.PreviewData
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

class FakeSetRepository : SetRepository {

    private var setDetails = listOf<SetDetails>()

    override suspend fun getSetCount(): Result<Int, DataError> {
        return Result.Success(setDetails.size)
    }

    override suspend fun getLastUpdatedSet(): Result<Set?, DataError> {
        return Result.Success(setDetails.maxByOrNull { it.set.lastUpdated }?.set)
    }

    override fun watchThemes(): Flow<List<String>> {
        TODO("Not yet implemented")
    }

    override fun watchThemeGroups(): Flow<List<SetThemeGroup>> {
        TODO("Not yet implemented")
    }

    override fun watchPackagingTypes(): Flow<List<String>> {
        TODO("Not yet implemented")
    }

    override fun watchSetDetails(queryOptions: SetQueryOptions): Flow<List<SetDetails>> {
        TODO("Not yet implemented")
    }

    override fun watchSetDetailsPaged(queryOptions: SetQueryOptions): Flow<PagingData<SetDetails>> {
        TODO("Not yet implemented")
    }

    override fun updateSetsWithProgress(exportBatch: ExportBatch): FlowForResult<Unit, UpdateSetsProgress> {
        return flowForResult {
            setDetails = PreviewData.sets
            Result.Success(Unit)
        }
    }

    override suspend fun deleteSetsUpdatedAfter(date: Instant): EmptyResult<DataError.Local> {
        TODO("Not yet implemented")
    }
}
