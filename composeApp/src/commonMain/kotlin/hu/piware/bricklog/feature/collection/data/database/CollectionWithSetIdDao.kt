package hu.piware.bricklog.feature.collection.data.database

import androidx.room.Dao
import androidx.room.Query
import hu.piware.bricklog.feature.user.domain.model.UserId
import kotlinx.coroutines.flow.Flow

@Dao
interface CollectionWithSetIdDao {

    @Query(
        """
            SELECT collections.*, collection_sets.setId FROM collections 
            LEFT JOIN collection_shares ON collections.id = collection_shares.collectionId AND collection_shares.withUserId = :userId
            JOIN collection_sets ON collections.id = collection_sets.collectionId
            WHERE collections.owner = :userId OR collection_shares.withUserId = :userId
            """,
    )
    fun watchUserAndSharedCollectionsWithSetIds(userId: UserId): Flow<List<CollectionWithSetId>>
}
