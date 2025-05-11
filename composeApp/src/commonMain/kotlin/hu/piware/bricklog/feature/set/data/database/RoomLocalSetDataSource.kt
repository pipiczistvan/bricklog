package hu.piware.bricklog.feature.set.data.database

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import androidx.room.RoomRawQuery
import androidx.sqlite.SQLiteException
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.set.domain.datasource.LocalSetDataSource
import hu.piware.bricklog.feature.set.domain.model.Set
import hu.piware.bricklog.feature.set.domain.model.SetQueryOptions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull

class RoomLocalSetDataSource(
    private val setDao: SetDao,
) : LocalSetDataSource {

    override fun watchSets(queryOptions: SetQueryOptions): Flow<List<Set>> {
        val query = RoomRawQuery(buildGetSetSql(queryOptions))

        return setDao.watchSets(query)
            .map { setEntities ->
                setEntities.map { it.toDomainModel() }
            }
    }

    override fun watchSet(id: Int): Flow<Set> {
        return setDao.watchSet(id)
            .mapNotNull { it?.toDomainModel() }
    }

    override suspend fun getSets(queryOptions: SetQueryOptions): Result<List<Set>, DataError.Local> {
        val query = RoomRawQuery(buildGetSetSql(queryOptions))

        return try {
            val sets = setDao.getSets(query).map { it.toDomainModel() }
            return Result.Success(sets)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun updateSets(sets: List<Set>): EmptyResult<DataError.Local> {
        return try {
            setDao.upsertAll(sets.map { it.toEntity() })
            Result.Success(Unit)
        } catch (e: SQLiteException) {
            Result.Error(DataError.Local.DISK_FULL)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override fun watchSetsPaged(queryOptions: SetQueryOptions): Flow<PagingData<Set>> {
        return Pager(
            PagingConfig(
                pageSize = 20,
                prefetchDistance = 20,
                enablePlaceholders = false
            )
        ) {
            val query = RoomRawQuery(buildGetSetSql(queryOptions))
            setDao.pagingSource(query)
        }.flow.map { pagingData ->
            pagingData.map { it.toDomainModel() }
        }
    }

    override suspend fun getSetCount(): Result<Int, DataError.Local> {
        return try {
            val count = setDao.getSetCount()
            Result.Success(count)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override fun watchThemes(): Flow<List<String>> {
        return setDao.watchThemes()
    }

    override fun watchPackagingTypes(): Flow<List<String>> {
        return setDao.watchPackagingTypes()
    }
}
