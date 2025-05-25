package hu.piware.bricklog.feature.collection.data.database

import androidx.sqlite.SQLiteException
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
    private val database: BricklogDatabase,
) : LocalCollectionDataSource {

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

    override suspend fun deleteCollectionById(id: CollectionId): EmptyResult<DataError.Local> {
        return try {
            collectionDao.deleteCollectionById(id)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun upsertCollection(collection: Collection): EmptyResult<DataError.Local> {
        return try {
            collectionDao.upsertCollection(collection.toEntity())
            Result.Success(Unit)
        } catch (e: SQLiteException) {
            Result.Error(DataError.Local.DISK_FULL)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun addSetToCollection(
        setId: SetId,
        collectionId: CollectionId,
    ): EmptyResult<DataError.Local> {
        return try {
            setCollectionDao.upsertSetCollection(
                SetCollectionEntity(
                    setId = setId,
                    collectionId = collectionId
                )
            )
            Result.Success(Unit)
        } catch (e: SQLiteException) {
            Result.Error(DataError.Local.DISK_FULL)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun removeSetFromCollection(
        setId: SetId,
        collectionId: CollectionId,
    ): EmptyResult<DataError.Local> {
        return try {
            setCollectionDao.deleteSetCollection(
                SetCollectionEntity(
                    setId = setId,
                    collectionId = collectionId
                )
            )
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun getCollection(id: CollectionId): Result<Collection, DataError.Local> {
        return try {
            val collection = collectionDao.getCollectionById(id).toDomainModel()
            Result.Success(collection)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun deleteCollection(id: CollectionId): EmptyResult<DataError.Local> {
        return try {
            collectionDao.deleteCollectionById(id)
            return Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }
}
