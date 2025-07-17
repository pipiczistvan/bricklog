@file:OptIn(ExperimentalCoroutinesApi::class)

package hu.piware.bricklog.feature.collection.data.repository

import co.touchlab.kermit.Logger
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

    private val logger = Logger.withTag("OfflineFirstCollectionRepository")

    override fun startSync(scope: CoroutineScope) {
        sessionManager.currentUser
            .filterNotNull()
            .flatMapLatest { user ->
                remoteDataSource.watchCollections(user.uid)
            }
            .onEach { remoteCollections ->
                logger.d { "Syncing ${remoteCollections.size} collections" }
                val localCollections = localDataSource.getCollections().data()
                val collectionsToDelete = localCollections.filter { localCollection ->
                    remoteCollections.none { remoteCollection ->
                        remoteCollection.id == localCollection.id
                    }
                }.map { it.id }
                val collectionsToUpsert = remoteCollections.filter { remoteCollection ->
                    localCollections.none { localCollection ->
                        localCollection == remoteCollection
                    }
                }

                if (collectionsToDelete.isNotEmpty()) {
                    localDataSource.deleteCollections(collectionsToDelete)
                }
                if (collectionsToUpsert.isNotEmpty()) {
                    localDataSource.upsertCollections(collectionsToUpsert)
                }
            }
            .launchIn(scope)

        sessionManager.currentUser
            .filterNotNull()
            .flatMapLatest { user ->
                remoteDataSource.watchSetCollections(user.uid)
            }
            .onEach { remoteSetCollections ->
                logger.d { "Syncing ${remoteSetCollections.size} set collections" }
                val localSetCollections = localDataSource.getSetCollections().data()
                val setCollectionsToDelete = localSetCollections
                    .map { setCollection ->
                        setCollection.key to setCollection.value.filter { localCollection ->
                            remoteSetCollections[setCollection.key]?.none { remoteCollection ->
                                remoteCollection == localCollection.id
                            } ?: true
                        }.map { it.id }
                    }
                    .toMap()
                    .filter { it.value.isNotEmpty() }
                val setCollectionsToUpsert = remoteSetCollections
                    .map { remoteSetCollection ->
                        remoteSetCollection.key to remoteSetCollection.value.filter { remoteCollection ->
                            localSetCollections[remoteSetCollection.key]?.none { localCollection ->
                                localCollection.id == remoteCollection
                            } ?: true
                        }
                    }
                    .toMap()
                    .filter { it.value.isNotEmpty() }

                if (setCollectionsToDelete.isNotEmpty()) {
                    localDataSource.deleteSetCollections(setCollectionsToDelete)
                }
                if (setCollectionsToUpsert.isNotEmpty()) {
                    localDataSource.upsertSetCollections(setCollectionsToUpsert)
                }
            }
            .launchIn(scope)
    }

    override suspend fun clearLocal(): EmptyResult<DataError> {
        return localDataSource.deleteAllCollections()
    }

    override suspend fun clearRemote(): EmptyResult<DataError> {
        val user = sessionManager.currentUser.value

        if (user == null) {
            return Result.Error(DataError.Remote.UNKNOWN)
        }

        return remoteDataSource.deleteAllCollections(user.uid)
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
