package hu.piware.bricklog.feature.set.domain.repository

import androidx.paging.PagingData
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.set.domain.model.FileUploadResult
import hu.piware.bricklog.feature.set.domain.model.Set
import hu.piware.bricklog.feature.set.domain.model.SetQueryOptions
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

interface SetRepository {
    fun watchSets(queryOptions: SetQueryOptions): Flow<List<Set>>
    fun watchSet(id: Int): Flow<Set>
    suspend fun getSets(queryOptions: SetQueryOptions): Result<List<Set>, DataError.Local>
    suspend fun updateSets(fileUploads: List<FileUploadResult>): EmptyResult<DataError>
    fun watchSetsPaged(queryOptions: SetQueryOptions): Flow<PagingData<Set>>
    suspend fun getSetCount(): Result<Int, DataError>
    fun watchThemes(): Flow<List<String>>
    fun watchPackagingTypes(): Flow<List<String>>
    suspend fun deleteSetsUpdatedAfter(date: Instant): EmptyResult<DataError.Local>
}
