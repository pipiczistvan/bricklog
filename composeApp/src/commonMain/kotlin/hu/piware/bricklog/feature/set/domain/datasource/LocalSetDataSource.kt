package hu.piware.bricklog.feature.set.domain.datasource

import androidx.paging.PagingData
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.set.domain.model.Set
import hu.piware.bricklog.feature.set.domain.model.SetFilter
import kotlinx.coroutines.flow.Flow

interface LocalSetDataSource {

    fun watchSets(queries: List<String>, filter: SetFilter): Flow<List<Set>>

    fun watchSet(id: Int): Flow<Set>

    suspend fun updateSets(sets: List<Set>): EmptyResult<DataError.Local>

    fun watchSetsPaged(queries: List<String>, filter: SetFilter): Flow<PagingData<Set>>

    suspend fun getSetCount(): Result<Int, DataError.Local>

    fun watchThemes(): Flow<List<String>>
}
