package hu.piware.bricklog.feature.set.domain.repository

import androidx.paging.PagingData
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.set.domain.model.FileUploadResult
import hu.piware.bricklog.feature.set.domain.model.Set
import hu.piware.bricklog.feature.set.domain.model.SetFilter
import kotlinx.coroutines.flow.Flow

interface SetRepository {
    fun watchSets(queries: List<String>, filter: SetFilter): Flow<List<Set>>
    fun watchSet(id: Int): Flow<Set>
    suspend fun updateSets(fileUploads: List<FileUploadResult>): EmptyResult<DataError>
    fun watchSetsPaged(queries: List<String>, filter: SetFilter): Flow<PagingData<Set>>
    suspend fun getSetCount(): Result<Int, DataError>
    fun watchThemes(): Flow<List<String>>
}
