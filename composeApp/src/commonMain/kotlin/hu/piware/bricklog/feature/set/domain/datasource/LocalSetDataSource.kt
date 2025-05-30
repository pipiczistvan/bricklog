package hu.piware.bricklog.feature.set.domain.datasource

import androidx.paging.PagingData
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.set.domain.model.Set
import hu.piware.bricklog.feature.set.domain.model.SetDetails
import hu.piware.bricklog.feature.set.domain.model.SetQueryOptions
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

interface LocalSetDataSource {
    fun watchSetDetails(queryOptions: SetQueryOptions): Flow<List<SetDetails>>
    suspend fun getSetDetails(queryOptions: SetQueryOptions): Result<List<SetDetails>, DataError.Local>
    fun watchSetDetailsPaged(queryOptions: SetQueryOptions): Flow<PagingData<SetDetails>>
    fun watchSetDetailsById(id: Int): Flow<SetDetails>
    suspend fun updateSets(sets: List<Set>): EmptyResult<DataError.Local>
    suspend fun getSetCount(): Result<Int, DataError.Local>
    fun watchThemes(): Flow<List<String>>
    fun watchPackagingTypes(): Flow<List<String>>
    suspend fun deleteSetsUpdatedAfter(date: Instant): EmptyResult<DataError.Local>
}
