package hu.piware.bricklog.feature.collection.data.database

import androidx.room.Dao
import androidx.room.Query
import hu.piware.bricklog.feature.user.domain.model.UserId
import kotlinx.coroutines.flow.Flow

@Dao
interface CollectionWithSetIdDao {

    @Query(
        """
            SELECT collections.*, set_collections.setId FROM collections 
            JOIN set_collections ON collections.id = set_collections.collectionId AND collections.userId = set_collections.userId 
            WHERE collections.userId = :userId
            """,
    )
    fun watchCollectionsWithSetIds(userId: UserId): Flow<List<CollectionWithSetId>>
}
