package hu.piware.bricklog.feature.set.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.RawQuery
import androidx.room.RoomRawQuery
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface SetDetailsDao {

    @Transaction
    @RawQuery(observedEntities = [SetDetailsView::class])
    fun watchSetDetails(query: RoomRawQuery): Flow<List<SetDetailsView>>

    @Transaction
    @RawQuery(observedEntities = [SetDetailsView::class])
    fun pagingSource(query: RoomRawQuery): PagingSource<Int, SetDetailsView>
}
