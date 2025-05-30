package hu.piware.bricklog.feature.set.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.RoomRawQuery
import androidx.room.Transaction
import hu.piware.bricklog.feature.set.domain.model.SetId
import kotlinx.coroutines.flow.Flow

@Dao
interface SetDetailsDao {

    @Transaction
    @Query("SELECT * FROM set_details_view WHERE id = :id")
    fun watchSetDetails(id: SetId): Flow<SetDetailsView?>

    @Transaction
    @RawQuery(observedEntities = [SetDetailsView::class])
    fun watchSetDetails(query: RoomRawQuery): Flow<List<SetDetailsView>>

    @Transaction
    @RawQuery(observedEntities = [SetDetailsView::class])
    suspend fun getSetDetails(query: RoomRawQuery): List<SetDetailsView>

    @Transaction
    @RawQuery(observedEntities = [SetDetailsView::class])
    fun pagingSource(query: RoomRawQuery): PagingSource<Int, SetDetailsView>
}
