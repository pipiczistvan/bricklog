package hu.piware.bricklog.feature.set.domain.datasource

import androidx.paging.PagingData
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.set.domain.model.Set
import hu.piware.bricklog.feature.set.domain.model.SetDetails
import hu.piware.bricklog.feature.set.domain.model.SetQueryOptions
import hu.piware.bricklog.feature.set.domain.model.SetThemeGroup
import hu.piware.bricklog.feature.user.domain.model.UserId
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

interface LocalSetDataSource {

    suspend fun getSetCount(): Result<Int, DataError.Local>

    suspend fun getLastUpdatedSet(): Result<Set?, DataError>

    fun watchThemes(): Flow<List<String>>

    fun watchThemeGroups(): Flow<List<SetThemeGroup>>

    fun watchPackagingTypes(): Flow<List<String>>

    fun watchSetDetails(userId: UserId, queryOptions: SetQueryOptions): Flow<List<SetDetails>>

    fun watchSetDetailsPaged(
        userId: UserId,
        queryOptions: SetQueryOptions,
    ): Flow<PagingData<SetDetails>>

    suspend fun upsertSets(sets: List<Set>): EmptyResult<DataError.Local>

    suspend fun upsertSetsChunked(
        sets: List<Set>,
        chunkSize: Int,
        onChunkInserted: suspend (insertCount: Int) -> Unit,
    ): EmptyResult<DataError.Local>

    suspend fun deleteSetsUpdatedAfter(date: Instant): EmptyResult<DataError.Local>
}
