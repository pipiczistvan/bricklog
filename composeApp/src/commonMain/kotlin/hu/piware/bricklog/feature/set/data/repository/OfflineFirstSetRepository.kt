@file:OptIn(ExperimentalCoroutinesApi::class)

package hu.piware.bricklog.feature.set.data.repository

import androidx.paging.PagingData
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.currency.domain.model.CurrencyRegion
import hu.piware.bricklog.feature.set.domain.datasource.LocalSetDataSource
import hu.piware.bricklog.feature.set.domain.model.PriceFilterOption
import hu.piware.bricklog.feature.set.domain.model.Set
import hu.piware.bricklog.feature.set.domain.model.SetDetails
import hu.piware.bricklog.feature.set.domain.model.SetQueryOptions
import hu.piware.bricklog.feature.set.domain.model.SetThemeGroup
import hu.piware.bricklog.feature.set.domain.repository.SetRepository
import hu.piware.bricklog.feature.user.domain.manager.SessionManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import org.koin.core.annotation.Single
import kotlin.math.ln
import kotlin.math.pow

@Single
class OfflineFirstSetRepository(
    private val localDataSource: LocalSetDataSource,
    private val sessionManager: SessionManager,
) : SetRepository {

    override suspend fun getSetCount(): Result<Int, DataError> {
        return localDataSource.getSetCount()
    }

    override suspend fun getLastUpdatedSet(): Result<Set?, DataError> {
        return localDataSource.getLastUpdatedSet()
    }

    override fun watchThemes(): Flow<List<String>> {
        return localDataSource.watchThemes()
    }

    override fun watchThemeGroups(): Flow<List<SetThemeGroup>> {
        return localDataSource.watchThemeGroups()
    }

    override fun watchPackagingTypes(): Flow<List<String>> {
        return localDataSource.watchPackagingTypes()
    }

    override fun watchSetDetails(queryOptions: SetQueryOptions): Flow<List<SetDetails>> {
        return sessionManager.userId.flatMapLatest { userId ->
            localDataSource.watchSetDetails(userId, queryOptions)
        }
    }

    override fun watchSetDetailsPaged(queryOptions: SetQueryOptions): Flow<PagingData<SetDetails>> {
        return sessionManager.userId.flatMapLatest { userId ->
            localDataSource.watchSetDetailsPaged(userId, queryOptions)
        }
    }

    override fun watchPriceRanges(region: CurrencyRegion): Flow<Map<PriceFilterOption, Pair<Double?, Double?>>> {
        return localDataSource.watchSetPriceMax(region)
            .map { maxPrice -> maxPrice ?: 0.0 }
            .map { maxPrice ->
                val ranges = getLogRanges(maxPrice, 4)

                mapOf(
                    PriceFilterOption.BUDGET to Pair(null, ranges[0].endInclusive),
                    PriceFilterOption.AFFORDABLE to Pair(ranges[1].start, ranges[1].endInclusive),
                    PriceFilterOption.EXPENSIVE to Pair(ranges[2].start, ranges[2].endInclusive),
                    PriceFilterOption.PREMIUM to Pair(ranges[3].start, null),
                )
            }
    }

    override suspend fun updateSetsChunked(
        sets: List<Set>,
        chunkSize: Int,
        onChunkInserted: suspend (Int) -> Unit,
    ): EmptyResult<DataError.Local> {
        return localDataSource.upsertSetsChunked(sets, chunkSize, onChunkInserted)
    }

    override suspend fun deleteSetsUpdatedAfter(date: Instant): EmptyResult<DataError.Local> {
        return localDataSource.deleteSetsUpdatedAfter(date)
    }

    private fun getLogRanges(
        maxValue: Double,
        segments: Int,
    ): List<ClosedFloatingPointRange<Double>> {
        require(maxValue > 0) { "Max value must be positive" }

        val logMax = ln(maxValue) / ln(10.0)
        val step = (logMax) / segments

        val boundaries = (0..segments).map { i ->
            10.0.pow(logMax - step * i)
        }.reversed()

        return boundaries.zipWithNext { a, b -> a..b }
    }
}
