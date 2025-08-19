package hu.piware.bricklog.feature.set.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.RoomRawQuery
import androidx.room.Transaction
import hu.piware.bricklog.feature.currency.domain.model.CurrencyRegion
import kotlinx.coroutines.flow.Flow

@Dao
interface SetDetailsDao {

    @Transaction
    @RawQuery(observedEntities = [SetDetailsWithCollections::class])
    fun watchSetDetails(query: RoomRawQuery): Flow<List<SetDetailsWithCollections>>

    @Transaction
    @RawQuery(observedEntities = [SetDetailsWithCollections::class])
    fun pagingSource(query: RoomRawQuery): PagingSource<Int, SetDetailsWithCollections>

    @Query("SELECT CASE :region WHEN 'US' THEN MIN(USPrice) WHEN 'EU' THEN MAX(DEPrice) END FROM sets")
    fun watchSetPriceMax(region: CurrencyRegion): Flow<Double?>
}
