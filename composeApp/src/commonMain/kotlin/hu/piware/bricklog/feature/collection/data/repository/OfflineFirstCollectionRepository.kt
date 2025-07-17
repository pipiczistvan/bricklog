@file:OptIn(ExperimentalCoroutinesApi::class)

package hu.piware.bricklog.feature.collection.data.repository

import hu.piware.bricklog.feature.collection.domain.datasource.LocalCollectionDataSource
import hu.piware.bricklog.feature.collection.domain.datasource.RemoteCollectionDataSource
import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.collection.domain.repository.CollectionRepository
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.domain.SyncedRepository
import hu.piware.bricklog.feature.core.domain.data
import hu.piware.bricklog.feature.core.domain.onError
import hu.piware.bricklog.feature.set.domain.model.SetId
import hu.piware.bricklog.feature.user.domain.manager.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.core.annotation.Single

@Single
class OfflineFirstCollectionRepository(
    private val localDataSource: LocalCollectionDataSource,
    private val remoteDataSource: RemoteCollectionDataSource,
    private val sessionManager: SessionManager,
) : CollectionRepository, SyncedRepository {

    override fun startSync(scope: CoroutineScope) {
        sessionManager.currentUser
            .filterNotNull()
            .flatMapLatest { user ->
                remoteDataSource.watchCollections(user.uid)
            }
            .onEach { remoteCollections ->
                val localCollections = localDataSource.getCollections().data()
                val collectionsToDelete = localCollections.filter { localCollection ->
                    remoteCollections.none { remoteCollection ->
                        remoteCollection.id == localCollection.id
                    }
                }.map { it.id }

                localDataSource.deleteCollections(collectionsToDelete)
                localDataSource.upsertCollections(remoteCollections)
            }
            .launchIn(scope)

        sessionManager.currentUser
            .filterNotNull()
            .flatMapLatest { user ->
                remoteDataSource.watchSetCollections(user.uid)
            }
            .onEach { remoteSetCollections ->
                val localSetCollections = localDataSource.getSetCollections().data()
                val setCollectionsToDelete = localSetCollections
                    .map { setCollection ->
                        setCollection.key to setCollection.value.filter { localCollection ->
                            remoteSetCollections[setCollection.key]?.none { remoteCollection ->
                                remoteCollection.id == localCollection.id
                            } ?: true
                        }
                    }

                setCollectionsToDelete.forEach { (setId, collections) ->
                    localDataSource.removeSetFromCollections(setId, collections.map { it.id })
                }

                remoteSetCollections.forEach { (setId, collections) ->
                    localDataSource.addSetToCollections(setId, collections.map { it.id })
                }
            }
            .launchIn(scope)
    }

    override suspend fun clearLocal(): EmptyResult<DataError> {
        return localDataSource.deleteAllCollections()
    }

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
        localDataSource.deleteCollection(id)
            .onError { return it }

        val user = sessionManager.currentUser.value
        if (user != null) {
            remoteDataSource.deleteCollection(user.uid, id)
                .onError { return it }
        }

        return Result.Success(Unit)
    }

    override suspend fun saveCollection(collection: Collection): EmptyResult<DataError> {
        localDataSource.upsertCollection(collection)
            .onError { return it }

        val user = sessionManager.currentUser.value
        if (user != null) {
            remoteDataSource.upsertCollection(user.uid, collection)
                .onError { return it }
        }

        return Result.Success(Unit)
    }

    override suspend fun addSetToCollection(
        setId: SetId,
        collectionId: CollectionId,
    ): EmptyResult<DataError> {
        localDataSource.addSetToCollection(setId, collectionId)
            .onError { return it }

        val user = sessionManager.currentUser.value
        if (user != null) {
            remoteDataSource.addSetToCollection(user.uid, setId, collectionId)
                .onError { return it }
        }

        return Result.Success(Unit)
    }

    override suspend fun removeSetFromCollection(
        setId: SetId,
        collectionId: CollectionId,
    ): EmptyResult<DataError> {
        localDataSource.removeSetFromCollection(setId, collectionId)
            .onError { return it }

        val user = sessionManager.currentUser.value
        if (user != null) {
            remoteDataSource.removeSetFromCollection(user.uid, setId, collectionId)
                .onError { return it }
        }

        return Result.Success(Unit)
    }

    override suspend fun getCollection(id: CollectionId): Result<Collection, DataError> {
        return localDataSource.getCollection(id)
    }

    override fun watchCollection(id: CollectionId): Flow<Collection> {
        return localDataSource.watchCollection(id)
    }
}
