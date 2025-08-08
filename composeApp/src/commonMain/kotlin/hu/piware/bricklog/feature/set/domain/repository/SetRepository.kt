package hu.piware.bricklog.feature.set.domain.repository

import androidx.paging.PagingData
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.set.domain.model.Set
import hu.piware.bricklog.feature.set.domain.model.SetDetails
import hu.piware.bricklog.feature.set.domain.model.SetQueryOptions
import hu.piware.bricklog.feature.set.domain.model.SetThemeGroup
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

interface SetRepository {

    suspend fun getSetCount(): Result<Int, DataError>

    suspend fun getLastUpdatedSet(): Result<Set?, DataError>

    fun watchThemes(): Flow<List<String>>

    fun watchThemeGroups(): Flow<List<SetThemeGroup>>

    fun watchPackagingTypes(): Flow<List<String>>

    fun watchSetDetails(queryOptions: SetQueryOptions): Flow<List<SetDetails>>

    fun watchSetDetailsPaged(queryOptions: SetQueryOptions): Flow<PagingData<SetDetails>>

    suspend fun updateSetsChunked(
        sets: List<Set>,
        chunkSize: Int,
        onChunkInserted: suspend (insertCount: Int) -> Unit,
    ): EmptyResult<DataError.Local>

    suspend fun deleteSetsUpdatedAfter(date: Instant): EmptyResult<DataError.Local>
}
