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
    private val collectionWithSetIdDao = database.collectionWithSetIdDao
    private val setCollectionDao = database.setCollectionDao

    override fun watchCollection(userId: UserId, collectionId: CollectionId): Flow<Collection> {
        return collectionDao.watchCollection(userId, collectionId).map { it.toDomainModel() }
    }

    override fun watchCollections(
        userId: UserId,
        type: CollectionType?,
        setId: SetId?,
    ): Flow<List<Collection>> {
        return collectionDao.watchCollections(userId, type, setId)
            .map { entity -> entity.map { it.toDomainModel() } }
    }

    override fun watchCollectionsBySets(userId: UserId): Flow<Map<SetId, List<Collection>>> {
        return collectionWithSetIdDao.watchCollectionsWithSetIds(userId)
            .map { list ->
                list
                    .groupBy { it.setId }
                    .mapValues { collectionWithSetId ->
                        collectionWithSetId.value
                            .map { it.collection.toDomainModel() }
                    }
            }
    }

    override suspend fun upsertCollections(
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

    override suspend fun upsertCollectionsBySets(
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

    override suspend fun addSetToCollections(
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

    override suspend fun deleteCollections(
        userId: UserId,
        collectionIds: List<CollectionId>,
    ): EmptyResult<DataError.Local> {
        return try {
            logger.d { "Deleting ${collectionIds.size} collections" }
            collectionDao.deleteCollections(userId, collectionIds)
            Result.Success(Unit)
        } catch (e: Exception) {
            logger.e(e) { "Error deleting collections" }
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun deleteCollectionsBySets(
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

    override suspend fun removeSetFromCollections(
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
}
