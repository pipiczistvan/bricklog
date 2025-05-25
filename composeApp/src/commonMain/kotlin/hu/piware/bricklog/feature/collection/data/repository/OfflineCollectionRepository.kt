package hu.piware.bricklog.feature.collection.data.repository

import hu.piware.bricklog.feature.collection.domain.datasource.LocalCollectionDataSource
import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.collection.domain.repository.CollectionRepository
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.set.domain.model.SetId
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class OfflineCollectionRepository(
    private val localDataSource: LocalCollectionDataSource,
) : CollectionRepository {

    override fun watchCollections(): Flow<List<Collection>> {
        return localDataSource.watchCollections()
    }

    override fun watchCollectionsBySets(): Flow<Map<SetId, List<Collection>>> {
        return localDataSource.watchCollectionsBySets()
    }

    override fun watchCollectionsBySet(setId: SetId): Flow<List<Collection>> {
        return localDataSource.watchCollectionsBySet(setId)
    }

    override suspend fun deleteCollectionById(id: CollectionId): EmptyResult<DataError> {
        return localDataSource.deleteCollectionById(id)
    }

    override suspend fun saveCollection(collection: Collection): EmptyResult<DataError> {
        return localDataSource.upsertCollection(collection)
    }

    override suspend fun addSetToCollection(
        setId: SetId,
        collectionId: CollectionId,
    ): EmptyResult<DataError> {
        return localDataSource.addSetToCollection(setId, collectionId)
    }

    override suspend fun removeSetFromCollection(
        setId: SetId,
        collectionId: CollectionId,
    ): EmptyResult<DataError> {
        return localDataSource.removeSetFromCollection(setId, collectionId)
    }

    override suspend fun getCollection(id: CollectionId): Result<Collection, DataError> {
        return localDataSource.getCollection(id)
    }

    override suspend fun deleteCollection(id: CollectionId): EmptyResult<DataError> {
        return localDataSource.deleteCollection(id)
    }
}
