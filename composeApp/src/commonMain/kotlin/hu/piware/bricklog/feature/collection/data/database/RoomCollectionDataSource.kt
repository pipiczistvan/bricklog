package hu.piware.bricklog.feature.collection.data.database

import androidx.sqlite.SQLiteException
import co.touchlab.kermit.Logger
import hu.piware.bricklog.feature.collection.domain.datasource.LocalCollectionDataSource
import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.collection.domain.model.CollectionType
import hu.piware.bricklog.feature.core.data.database.BricklogDatabase
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.set.domain.model.SetId
import hu.piware.bricklog.feature.user.domain.model.UserId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
class RoomCollectionDataSource(
    database: BricklogDatabase,
) : LocalCollectionDataSource {

    private val logger = Logger.withTag("RoomLocalCollectionDataSource")

    private val collectionDao = database.collectionDao
    private val setCollectionDao = database.setCollectionDao

    override fun watchUserCollections(userId: UserId): Flow<List<Collection>> {
        return collectionDao.watchUserCollections(userId)
            .map { entity -> entity.map { it.toDomainModel() } }
    }

    override suspend fun getUserCollectionsByType(
        userId: UserId,
        type: CollectionType,
    ): Result<List<Collection>, DataError.Local> {
        return try {
            logger.d { "Getting collections by type $type" }
            val collections =
                collectionDao.getUserCollectionsByType(userId, type).map { it.toDomainModel() }
            Result.Success(collections)
        } catch (e: Exception) {
            logger.e(e) { "Error getting collections by type $type" }
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override fun watchUserCollectionsBySets(userId: UserId): Flow<Map<SetId, List<Collection>>> {
        return collectionDao.watchUserCollectionsWithSetIds(userId)
            .map { list ->
                list
                    .groupBy { it.setId }
                    .mapValues { collectionWithSetId ->
                        collectionWithSetId.value
                            .map { it.collection.toDomainModel() }
                    }
            }
    }

    override fun watchUserCollectionsBySet(userId: UserId, setId: SetId): Flow<List<Collection>> {
        return collectionDao.watchUserCollectionsBySet(userId, setId)
            .map { entity -> entity.map { it.toDomainModel() } }
    }

    override suspend fun getUserSetCollections(userId: UserId): Result<Map<SetId, List<Collection>>, DataError.Local> {
        return collectionDao.getUserCollectionsWithSetIds(userId)
            .groupBy { it.setId }
            .mapValues { collectionWithSetId ->
                collectionWithSetId.value
                    .map { it.collection.toDomainModel() }
            }
            .let { Result.Success(it) }
    }

    override suspend fun deleteUserCollectionById(
        userId: UserId,
        collectionId: CollectionId,
    ): EmptyResult<DataError.Local> {
        return try {
            logger.d { "Deleting collection with id $collectionId" }
            collectionDao.deleteUserCollectionById(userId, collectionId)
            Result.Success(Unit)
        } catch (e: Exception) {
            logger.e(e) { "Error deleting collection with id $collectionId" }
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun upsertUserCollection(
        userId: UserId,
        collection: Collection,
    ): EmptyResult<DataError.Local> {
        return try {
            logger.d { "Upserting collection $collection" }
            collectionDao.upsertCollection(collection.toEntity(userId))
            Result.Success(Unit)
        } catch (e: SQLiteException) {
            logger.e(e) { "Error upserting collection $collection" }
            Result.Error(DataError.Local.DISK_FULL)
        } catch (e: Exception) {
            logger.e(e) { "Error upserting collection $collection" }
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun addSetToUserCollection(
        setId: SetId,
        userId: UserId,
        collectionId: CollectionId,
    ): EmptyResult<DataError.Local> {
        return try {
            logger.d { "Adding set $setId to collection $collectionId" }
            setCollectionDao.upsertSetCollection(
                SetCollectionEntity(
                    setId = setId,
                    userId = userId,
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

    override suspend fun addSetToUserCollections(
        setId: SetId,
        userId: UserId,
        collectionIds: List<CollectionId>,
    ): EmptyResult<DataError.Local> {
        return try {
            logger.d { "Adding set $setId to collections $collectionIds" }
            setCollectionDao.upsertSetCollections(
                collectionIds.map { collectionId ->
                    SetCollectionEntity(
                        setId = setId,
                        userId = userId,
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

    override suspend fun removeSetFromUserCollection(
        setId: SetId,
        userId: UserId,
        collectionId: CollectionId,
    ): EmptyResult<DataError.Local> {
        return try {
            logger.d { "Removing set $setId from collection $collectionId" }
            setCollectionDao.deleteSetCollection(
                SetCollectionEntity(
                    setId = setId,
                    userId = userId,
                    collectionId = collectionId
                )
            )
            Result.Success(Unit)
        } catch (e: Exception) {
            logger.e(e) { "Error removing set $setId from collection $collectionId" }
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun removeSetFromUserCollections(
        setId: SetId,
        userId: UserId,
        collectionIds: List<CollectionId>,
    ): EmptyResult<DataError.Local> {
        return try {
            logger.d { "Removing set $setId from collections $collectionIds" }
            setCollectionDao.deleteSetCollections(
                collectionIds.map { collectionId ->
                    SetCollectionEntity(
                        setId = setId,
                        userId = userId,
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

    override suspend fun deleteUserSetCollections(
        userId: UserId,
        setCollections: Map<SetId, List<CollectionId>>,
    ): EmptyResult<DataError.Local> {
        return try {
            logger.d { "Deleting set collections" }

            val entities = setCollections.flatMap { (setId, collections) ->
                collections.map { collectionId ->
                    SetCollectionEntity(
                        setId = setId,
                        userId = userId,
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

    override suspend fun upsertUserSetCollections(
        userId: UserId,
        setCollections: Map<SetId, List<CollectionId>>,
    ): EmptyResult<DataError.Local> {
        return try {
            logger.d { "Upserting set collections" }

            val entities = setCollections.flatMap { (setId, collections) ->
                collections.map { collectionId ->
                    SetCollectionEntity(
                        setId = setId,
                        userId = userId,
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

    override suspend fun getUserCollection(
        userId: UserId,
        collectionId: CollectionId,
    ): Result<Collection, DataError.Local> {
        return try {
            val collection =
                collectionDao.getUserCollectionById(userId, collectionId).toDomainModel()
            Result.Success(collection)
        } catch (e: Exception) {
            logger.e(e) { "Error getting collection with id $collectionId" }
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun getUserCollections(userId: UserId): Result<List<Collection>, DataError.Local> {
        return try {
            val collections = collectionDao.getUserCollections(userId).map { it.toDomainModel() }
            Result.Success(collections)
        } catch (e: Exception) {
            logger.e(e) { "Error getting collections" }
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun deleteUserCollection(
        userId: UserId,
        collectionId: CollectionId,
    ): EmptyResult<DataError.Local> {
        return try {
            logger.d { "Deleting collection with id $collectionId" }
            collectionDao.deleteUserCollectionById(userId, collectionId)
            return Result.Success(Unit)
        } catch (e: Exception) {
            logger.e(e) { "Error deleting collection with id $collectionId" }
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun deleteUserCollections(
        userId: UserId,
        collectionIds: List<CollectionId>,
    ): EmptyResult<DataError.Local> {
        return try {
            logger.d { "Deleting ${collectionIds.size} collections" }
            collectionDao.deleteUserCollectionsById(userId, collectionIds)
            Result.Success(Unit)
        } catch (e: Exception) {
            logger.e(e) { "Error deleting collections" }
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun deleteAllUserCollections(userId: UserId): EmptyResult<DataError.Local> {
        return try {
            logger.d { "Deleting all collections" }
            collectionDao.deleteAllUserCollections(userId)
            Result.Success(Unit)
        } catch (e: Exception) {
            logger.e(e) { "Error deleting all collections" }
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun upsertUserCollections(
        userId: UserId,
        collections: List<Collection>,
    ): EmptyResult<DataError.Local> {
        return try {
            logger.d { "Upserting ${collections.size} collections" }
            collectionDao.upsertCollections(collections.map { it.toEntity(userId) })
            Result.Success(Unit)
        } catch (e: SQLiteException) {
            logger.e(e) { "Error upserting collections" }
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override fun watchUserCollection(userId: UserId, collectionId: CollectionId): Flow<Collection> {
        return collectionDao.watchUserCollection(userId, collectionId).map { it.toDomainModel() }
    }
}
