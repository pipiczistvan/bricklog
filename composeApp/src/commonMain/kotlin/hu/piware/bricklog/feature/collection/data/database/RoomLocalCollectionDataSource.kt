package hu.piware.bricklog.feature.collection.data.database

import androidx.sqlite.SQLiteException
import co.touchlab.kermit.Logger
import hu.piware.bricklog.feature.collection.domain.datasource.LocalCollectionDataSource
import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.core.data.database.BricklogDatabase
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.set.domain.model.SetId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
class RoomLocalCollectionDataSource(
    database: BricklogDatabase,
) : LocalCollectionDataSource {

    private val logger = Logger.withTag("RoomLocalCollectionDataSource")

    private val collectionDao = database.collectionDao
    private val setCollectionDao = database.setCollectionDao

    override fun watchCollections(): Flow<List<Collection>> {
        return collectionDao.watchCollections().map { entity -> entity.map { it.toDomainModel() } }
    }

    override fun watchCollectionsBySets(): Flow<Map<SetId, List<Collection>>> {
        return collectionDao.watchCollectionsWithSetIds()
            .map { list ->
                list
                    .groupBy { it.setId }
                    .mapValues { collectionWithSetId ->
                        collectionWithSetId.value
                            .map { it.collection.toDomainModel() }
                    }
            }
    }

    override fun watchCollectionsBySet(setId: SetId): Flow<List<Collection>> {
        return collectionDao.watchCollectionsBySet(setId)
            .map { entity -> entity.map { it.toDomainModel() } }
    }

    override suspend fun getSetCollections(): Result<Map<SetId, List<Collection>>, DataError.Local> {
        return collectionDao.getCollectionsWithSetIds()
            .groupBy { it.setId }
            .mapValues { collectionWithSetId ->
                collectionWithSetId.value
                    .map { it.collection.toDomainModel() }
            }
            .let { Result.Success(it) }
    }

    override suspend fun deleteCollectionById(id: CollectionId): EmptyResult<DataError.Local> {
        return try {
            logger.d { "Deleting collection with id $id" }
            collectionDao.deleteCollectionById(id)
            Result.Success(Unit)
        } catch (e: Exception) {
            logger.e(e) { "Error deleting collection with id $id" }
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun upsertCollection(collection: Collection): EmptyResult<DataError.Local> {
        return try {
            logger.d { "Upserting collection $collection" }
            collectionDao.upsertCollection(collection.toEntity())
            Result.Success(Unit)
        } catch (e: SQLiteException) {
            logger.e(e) { "Error upserting collection $collection" }
            Result.Error(DataError.Local.DISK_FULL)
        } catch (e: Exception) {
            logger.e(e) { "Error upserting collection $collection" }
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun addSetToCollection(
        setId: SetId,
        collectionId: CollectionId,
    ): EmptyResult<DataError.Local> {
        return try {
            logger.d { "Adding set $setId to collection $collectionId" }
            setCollectionDao.upsertSetCollection(
                SetCollectionEntity(
                    setId = setId,
                    collectionId = collectionId
                )
            )
            Result.Success(Unit)
        } catch (e: SQLiteException) {
            logger.e(e) { "Error adding set $setId to collection $collectionId" }
            Result.Error(DataError.Local.DISK_FULL)
        } catch (e: Exception) {
            logger.e(e) { "Error adding set $setId to collection $collectionId" }
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun addSetToCollections(
        setId: SetId,
        collectionIds: List<CollectionId>,
    ): EmptyResult<DataError.Local> {
        return try {
            logger.d { "Adding set $setId to collections $collectionIds" }
            setCollectionDao.upsertSetCollections(
                collectionIds.map { collectionId ->
                    SetCollectionEntity(
                        setId = setId,
                        collectionId = collectionId
                    )
                }
            )
            Result.Success(Unit)
        } catch (e: SQLiteException) {
            logger.e(e) { "Error adding set $setId to collections $collectionIds" }
            Result.Error(DataError.Local.DISK_FULL)
        } catch (e: Exception) {
            logger.e(e) { "Error adding set $setId to collections $collectionIds" }
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun removeSetFromCollection(
        setId: SetId,
        collectionId: CollectionId,
    ): EmptyResult<DataError.Local> {
        return try {
            logger.d { "Removing set $setId from collection $collectionId" }
            setCollectionDao.deleteSetCollection(
                SetCollectionEntity(
                    setId = setId,
                    collectionId = collectionId
                )
            )
            Result.Success(Unit)
        } catch (e: Exception) {
            logger.e(e) { "Error removing set $setId from collection $collectionId" }
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun removeSetFromCollections(
        setId: SetId,
        collectionIds: List<CollectionId>,
    ): EmptyResult<DataError.Local> {
        return try {
            logger.d { "Removing set $setId from collections $collectionIds" }
            setCollectionDao.deleteSetCollections(
                collectionIds.map { collectionId ->
                    SetCollectionEntity(
                        setId = setId,
                        collectionId = collectionId
                    )
                }
            )
            Result.Success(Unit)
        } catch (e: Exception) {
            logger.e(e) { "Error removing set $setId from collections $collectionIds" }
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun deleteSetCollections(setCollections: Map<SetId, List<CollectionId>>): EmptyResult<DataError.Local> {
        return try {
            logger.d { "Deleting set collections" }

            val entities = setCollections.flatMap { (setId, collections) ->
                collections.map { collectionId ->
                    SetCollectionEntity(
                        setId = setId,
                        collectionId = collectionId
                    )
                }
            }

            setCollectionDao.deleteSetCollections(entities)

            Result.Success(Unit)
        } catch (e: Exception) {
            logger.e(e) { "Error deleting set collections" }
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun upsertSetCollections(setCollections: Map<SetId, List<CollectionId>>): EmptyResult<DataError.Local> {
        return try {
            logger.d { "Upserting set collections" }

            val entities = setCollections.flatMap { (setId, collections) ->
                collections.map { collectionId ->
                    SetCollectionEntity(
                        setId = setId,
                        collectionId = collectionId
                    )
                }
            }

            setCollectionDao.upsertSetCollections(entities)

            Result.Success(Unit)
        } catch (e: Exception) {
            logger.e(e) { "Error upserting set collections" }
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun getCollection(id: CollectionId): Result<Collection, DataError.Local> {
        return try {
            val collection = collectionDao.getCollectionById(id).toDomainModel()
            Result.Success(collection)
        } catch (e: Exception) {
            logger.e(e) { "Error getting collection with id $id" }
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun getCollections(): Result<List<Collection>, DataError.Local> {
        return try {
            val collections = collectionDao.getCollections().map { it.toDomainModel() }
            Result.Success(collections)
        } catch (e: Exception) {
            logger.e(e) { "Error getting collections" }
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun deleteCollection(id: CollectionId): EmptyResult<DataError.Local> {
        return try {
            logger.d { "Deleting collection with id $id" }
            collectionDao.deleteCollectionById(id)
            return Result.Success(Unit)
        } catch (e: Exception) {
            logger.e(e) { "Error deleting collection with id $id" }
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun deleteCollections(ids: List<CollectionId>): EmptyResult<DataError.Local> {
        return try {
            logger.d { "Deleting ${ids.size} collections" }
            collectionDao.deleteCollectionsById(ids)
            Result.Success(Unit)
        } catch (e: Exception) {
            logger.e(e) { "Error deleting collections" }
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun deleteAllCollections(): EmptyResult<DataError.Local> {
        return try {
            logger.d { "Deleting all collections" }
            collectionDao.deleteAllCollections()
            Result.Success(Unit)
        } catch (e: Exception) {
            logger.e(e) { "Error deleting all collections" }
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun upsertCollections(collections: List<Collection>): EmptyResult<DataError.Local> {
        return try {
            logger.d { "Upserting ${collections.size} collections" }
            collectionDao.upsertCollections(collections.map { it.toEntity() })
            Result.Success(Unit)
        } catch (e: SQLiteException) {
            logger.e(e) { "Error upserting collections" }
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override fun watchCollection(id: CollectionId): Flow<Collection> {
        return collectionDao.watchCollection(id).map { it.toDomainModel() }
    }
}
