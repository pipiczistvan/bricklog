package hu.piware.bricklog.feature.set.data.database

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import androidx.room.RoomRawQuery
import androidx.sqlite.SQLiteException
import co.touchlab.kermit.Logger
import hu.piware.bricklog.feature.core.data.database.BricklogDatabase
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.set.domain.datasource.LocalSetDataSource
import hu.piware.bricklog.feature.set.domain.model.Set
import hu.piware.bricklog.feature.set.domain.model.SetDetails
import hu.piware.bricklog.feature.set.domain.model.SetQueryOptions
import hu.piware.bricklog.feature.set.domain.model.SetTheme
import hu.piware.bricklog.feature.set.domain.model.SetThemeGroup
import hu.piware.bricklog.feature.user.domain.model.UserId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import org.koin.core.annotation.Single

@Single
class RoomSetDataSource(
    database: BricklogDatabase,
) : LocalSetDataSource {

    private val logger = Logger.withTag("RoomSetDataSource")

    private val setDao = database.setDao
    private val setDetailsDao = database.setDetailsDao
    private val themeGroupDao = database.themeGroupDao

    override suspend fun getSetCount(): Result<Int, DataError.Local> {
        return try {
            logger.d { "Getting set count" }
            val count = setDao.getSetCount()
            Result.Success(count)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun getLastUpdatedSet(): Result<Set?, DataError> {
        return try {
            logger.d { "Getting last updated set" }
            val set = setDao.getLastUpdatedSet()?.toDomainModel()
            Result.Success(set)
        } catch (e: Exception) {
            logger.e(e) { "Error getting last updated set" }
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override fun watchThemes(): Flow<List<String>> {
        return setDao.watchThemes()
    }

    override fun watchThemeGroups(): Flow<List<SetThemeGroup>> {
        return themeGroupDao.watchThemeGroups()
            .map { rows ->
                rows.groupBy { it.themeGroup }
                    .map { (group, items) ->
                        SetThemeGroup(
                            name = group,
                            themes = items.map { SetTheme(it.theme, it.setCount) }
                        )
                    }
            }
    }

    override fun watchPackagingTypes(): Flow<List<String>> {
        return setDao.watchPackagingTypes()
    }

    override fun watchSetDetails(
        userId: UserId,
        queryOptions: SetQueryOptions,
    ): Flow<List<SetDetails>> {
        val query = RoomRawQuery(buildGetSetDetailsSql(queryOptions))

        return setDetailsDao.watchSetDetails(query)
            .map { entities ->
                entities.map { it.toDomainModel(userId) }
            }
    }

    override fun watchSetDetailsPaged(
        userId: UserId,
        queryOptions: SetQueryOptions,
    ): Flow<PagingData<SetDetails>> {
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
            pagingData.map { it.toDomainModel(userId) }
        }
    }

    override suspend fun upsertSets(sets: List<Set>): EmptyResult<DataError.Local> {
        return try {
            logger.d { "Upserting sets" }
            setDao.upsertSets(sets.map { it.toEntity() })
            Result.Success(Unit)
        } catch (e: SQLiteException) {
            logger.e(e) { "Error upserting sets" }
            Result.Error(DataError.Local.DISK_FULL)
        } catch (e: Exception) {
            logger.e(e) { "Error upserting sets" }
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun deleteSetsUpdatedAfter(date: Instant): EmptyResult<DataError.Local> {
        return try {
            logger.d { "Deleting sets updated after $date" }
            setDao.deleteSetsUpdatedAfter(date)
            Result.Success(Unit)
        } catch (e: Exception) {
            logger.e(e) { "Error deleting sets updated after $date" }
            Result.Error(DataError.Local.UNKNOWN)
        }
    }
}
