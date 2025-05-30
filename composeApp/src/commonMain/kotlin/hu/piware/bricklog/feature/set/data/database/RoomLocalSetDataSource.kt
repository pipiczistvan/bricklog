package hu.piware.bricklog.feature.set.data.database

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import androidx.room.RoomRawQuery
import androidx.sqlite.SQLiteException
import hu.piware.bricklog.feature.core.data.database.BricklogDatabase
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.set.domain.datasource.LocalSetDataSource
import hu.piware.bricklog.feature.set.domain.model.Set
import hu.piware.bricklog.feature.set.domain.model.SetDetails
import hu.piware.bricklog.feature.set.domain.model.SetId
import hu.piware.bricklog.feature.set.domain.model.SetQueryOptions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.datetime.Instant
import org.koin.core.annotation.Single

@Single
class RoomLocalSetDataSource(
    database: BricklogDatabase,
) : LocalSetDataSource {

    private val setDao = database.setDao
    private val setDetailsDao = database.setDetailsDao

    override fun watchSetDetails(queryOptions: SetQueryOptions): Flow<List<SetDetails>> {
        val query = RoomRawQuery(buildGetSetDetailsSql(queryOptions))

        return setDetailsDao.watchSetDetails(query)
            .map { entities ->
                entities.map { it.toDomainModel() }
            }
    }

    override suspend fun getSetDetails(queryOptions: SetQueryOptions): Result<List<SetDetails>, DataError.Local> {
        val query = RoomRawQuery(buildGetSetDetailsSql(queryOptions))

        return try {
            val sets = setDetailsDao.getSetDetails(query).map { it.toDomainModel() }
            return Result.Success(sets)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override fun watchSetDetailsPaged(queryOptions: SetQueryOptions): Flow<PagingData<SetDetails>> {
        return Pager(
            PagingConfig(
                pageSize = 10,
                prefetchDistance = 5,
                enablePlaceholders = true
            )
        ) {
            val query = RoomRawQuery(buildGetSetDetailsSql(queryOptions))
            setDetailsDao.pagingSource(query)
        }.flow.map { pagingData ->
            pagingData.map { it.toDomainModel() }
        }
    }

    override fun watchSetDetailsById(id: SetId): Flow<SetDetails> {
        return setDetailsDao.watchSetDetails(id)
            .mapNotNull { it?.toDomainModel() }
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

    override suspend fun deleteSetsUpdatedAfter(date: Instant): EmptyResult<DataError.Local> {
        return try {
            setDao.deleteSetsUpdatedAfter(date)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }
}
