package hu.piware.bricklog.feature.collection.data.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.set.domain.model.SetId
import kotlinx.coroutines.flow.Flow

@Dao
interface CollectionDao {

    @Query("SELECT * FROM collections")
    fun watchCollections(): Flow<List<CollectionEntity>>

    @Query(
        "SELECT collections.*, set_collections.setId FROM collections " +
                "LEFT JOIN set_collections ON collections.id = set_collections.collectionId"
    )
    fun watchCollectionsWithSetIds(): Flow<List<CollectionWithSetId>>

    @Query(
        "SELECT collections.*, set_collections.setId FROM collections " +
                "LEFT JOIN set_collections ON collections.id = set_collections.collectionId"
    )
    suspend fun getCollectionsWithSetIds(): List<CollectionWithSetId>

    @Query(
        "SELECT collections.* FROM collections " +
                "LEFT JOIN set_collections ON collections.id = set_collections.collectionId " +
                "WHERE set_collections.setId = :setId"
    )
    fun watchCollectionsBySet(setId: SetId): Flow<List<CollectionEntity>>

    @Query("DELETE FROM collections WHERE id = :id")
    suspend fun deleteCollectionById(id: CollectionId)

    @Query("DELETE FROM collections WHERE id IN (:ids)")
    suspend fun deleteCollectionsById(ids: List<CollectionId>)

    @Query("DELETE FROM collections")
    suspend fun deleteAllCollections()

    @Upsert
    suspend fun upsertCollection(collection: CollectionEntity)

    @Upsert
    suspend fun upsertCollections(collections: List<CollectionEntity>)

    @Query("SELECT * FROM collections WHERE id = :id")
    suspend fun getCollectionById(id: CollectionId): CollectionEntity

    @Query("SELECT * FROM collections")
    suspend fun getCollections(): List<CollectionEntity>

    @Query("SELECT * FROM collections WHERE id = :id")
    fun watchCollection(id: CollectionId): Flow<CollectionEntity>
}
