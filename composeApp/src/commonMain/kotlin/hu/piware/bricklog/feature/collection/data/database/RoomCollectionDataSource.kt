package hu.piware.bricklog.feature.collection.data.database

import androidx.room.immediateTransaction
import androidx.room.useWriterConnection
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
    val database: BricklogDatabase,
) : LocalCollectionDataSource {

    private val logger = Logger.withTag("RoomLocalCollectionDataSource")

    private val collectionDao = database.collectionDao
    private val collectionWithSetIdDao = database.collectionWithSetIdDao
    private val collectionSetDao = database.collectionSetDao

    override fun watchCollection(collectionId: CollectionId): Flow<Collection?> {
        return collectionDao.watchCollection(collectionId).map { it?.toDomainModel() }
    }

    override fun watchUserAndSharedCollections(
        userId: UserId,
        type: CollectionType?,
        setId: SetId?,
    ): Flow<List<Collection>> {
        return collectionDao.watchUserAndSharedCollections(userId, type, setId)
            .map { entity -> entity.map { it.toDomainModel() } }
    }

    override fun watchUserAndSharedCollectionsBySets(userId: UserId): Flow<Map<SetId, List<Collection>>> {
        return collectionWithSetIdDao.watchUserAndSharedCollectionsWithSetIds(userId)
            .map { list ->
                list
                    .groupBy { it.setId }
                    .mapValues { collectionWithSetId ->
                        collectionWithSetId.value
                            .map { it.collectionWithShares.toDomainModel() }
                    }
            }
    }

    override suspend fun upsertCollections(
        collections: List<Collection>,
    ): EmptyResult<DataError.Local> {
        return try {
            logger.d { "Upserting ${collections.size} collections" }
            val entities = collections.map { it.toEntity() }
            logger.d { "Upserting ${entities.map { it.toString() }} collections" }
            database.useWriterConnection { transactor ->
                transactor.immediateTransaction {
                    collectionDao.upsertCollections(entities.map { it.collection })
                    collectionDao.deleteCollectionShares(entities.map { it.collection.id })
                    collectionDao.upsertCollectionShares(entities.flatMap { it.shares })
                }
            }
            Result.Success(Unit)
        } catch (e: SQLiteException) {
            logger.e(e) { "Error upserting collections" }
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun upsertCollectionsBySets(
        setCollections: Map<SetId, List<CollectionId>>,
    ): EmptyResult<DataError.Local> {
        return try {
            logger.d { "Upserting set collections" }

            val entities = setCollections.flatMap { (setId, collections) ->
                collections.map { collectionId ->
                    CollectionSetEntity(
                        setId = setId,
                        collectionId = collectionId,
                    )
                }
            }

            logger.d { "Upserting ${entities.map { it.toString() }} set collections" }

            collectionSetDao.upsertCollectionSets(entities)

            Result.Success(Unit)
        } catch (e: Exception) {
            logger.e(e) { "Error upserting set collections" }
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun addSetToCollections(
        setId: SetId,
        collectionIds: List<CollectionId>,
    ): EmptyResult<DataError.Local> {
        return try {
            logger.d { "Adding set $setId to collections $collectionIds" }
            collectionSetDao.upsertCollectionSets(
                collectionIds.map { collectionId ->
                    CollectionSetEntity(
                        setId = setId,
                        collectionId = collectionId,
                    )
                },
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

    override suspend fun deleteUserCollections(userId: UserId): EmptyResult<DataError.Local> {
        return try {
            logger.d { "Deleting all collections for user $userId" }
            collectionDao.deleteUserCollections(userId)
            Result.Success(Unit)
        } catch (e: Exception) {
            logger.e(e) { "Error deleting collections for user $userId" }
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun deleteCollections(
        collectionIds: List<CollectionId>,
    ): EmptyResult<DataError.Local> {
        return try {
            logger.d { "Deleting ${collectionIds.size} collections" }
            collectionDao.deleteCollections(collectionIds)
            Result.Success(Unit)
        } catch (e: Exception) {
            logger.e(e) { "Error deleting collections" }
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun deleteCollectionsBySets(
        setCollections: Map<SetId, List<CollectionId>>,
    ): EmptyResult<DataError.Local> {
        return try {
            logger.d { "Deleting set collections" }

            val entities = setCollections.flatMap { (setId, collections) ->
                collections.map { collectionId ->
                    CollectionSetEntity(
                        setId = setId,
                        collectionId = collectionId,
                    )
                }
            }

            collectionSetDao.deleteCollectionSets(entities)

            Result.Success(Unit)
        } catch (e: Exception) {
            logger.e(e) { "Error deleting set collections" }
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun removeSetFromCollections(
        setId: SetId,
        collectionIds: List<CollectionId>,
    ): EmptyResult<DataError.Local> {
        return try {
            logger.d { "Removing set $setId from collections $collectionIds" }
            collectionSetDao.deleteCollectionSets(
                collectionIds.map { collectionId ->
                    CollectionSetEntity(
                        setId = setId,
                        collectionId = collectionId,
                    )
                },
            )
            Result.Success(Unit)
        } catch (e: Exception) {
            logger.e(e) { "Error removing set $setId from collections $collectionIds" }
            Result.Error(DataError.Local.UNKNOWN)
        }
    }
}
