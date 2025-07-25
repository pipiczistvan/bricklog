@file:OptIn(ExperimentalCoroutinesApi::class)

package hu.piware.bricklog.feature.collection.data.repository

import co.touchlab.kermit.Logger
import hu.piware.bricklog.feature.collection.domain.datasource.LocalCollectionDataSource
import hu.piware.bricklog.feature.collection.domain.datasource.RemoteCollectionDataSource
import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.collection.domain.model.CollectionType
import hu.piware.bricklog.feature.collection.domain.repository.CollectionRepository
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.domain.SyncedRepository
import hu.piware.bricklog.feature.core.domain.data
import hu.piware.bricklog.feature.core.domain.onError
import hu.piware.bricklog.feature.set.domain.model.SetId
import hu.piware.bricklog.feature.user.domain.manager.SessionManager
import hu.piware.bricklog.feature.user.domain.manager.filterAuthenticated
import hu.piware.bricklog.feature.user.domain.model.UserId
import hu.piware.bricklog.util.firstOrDefault
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import org.koin.core.annotation.Single

@Single
class OfflineFirstCollectionRepository(
    private val localDataSource: LocalCollectionDataSource,
    private val remoteDataSource: RemoteCollectionDataSource,
    private val sessionManager: SessionManager,
) : CollectionRepository, SyncedRepository {

    private val logger = Logger.withTag("OfflineFirstCollectionRepository")

    private var firestoreSyncJob: Job? = null

    override fun startSync(scope: CoroutineScope) {
        firestoreSyncJob?.cancel()
        firestoreSyncJob = sessionManager.userId
            .filterAuthenticated()
            .flatMapLatest { userId ->
                remoteDataSource.watchCollections(userId).map { Pair(userId, it) }
            }
            .syncRemoteCollections()
            .flatMapLatest { (userId, _) ->
                remoteDataSource.watchCollectionsBySets(userId).map { Pair(userId, it) }
            }
            .syncRemoteSetCollections()
            .launchIn(scope)
    }

    override suspend fun clearLocal(): EmptyResult<DataError> {
        return localDataSource.deleteCollections(sessionManager.currentUserId)
    }

    override fun watchCollection(collectionId: CollectionId): Flow<Collection> {
        return sessionManager.userId.flatMapLatest { userId ->
            localDataSource.watchCollection(userId, collectionId)
        }
    }

    override fun watchCollections(type: CollectionType?, setId: SetId?): Flow<List<Collection>> {
        return sessionManager.userId.flatMapLatest { userId ->
            localDataSource.watchCollections(userId, type, setId)
        }
    }

    override fun watchCollectionsBySets(): Flow<Map<SetId, List<Collection>>> {
        return sessionManager.userId.flatMapLatest { userId ->
            localDataSource.watchCollectionsBySets(userId)
        }
    }

    override suspend fun saveCollections(collections: List<Collection>): Result<List<CollectionId>, DataError> {
        return saveCollections(sessionManager.currentUserId, collections)
    }

    override suspend fun addSetToCollections(
        setId: SetId,
        collectionIds: List<CollectionId>,
    ): EmptyResult<DataError> {
        localDataSource.addSetToCollections(setId, sessionManager.currentUserId, collectionIds)
            .onError { return it }

        if (sessionManager.isAuthenticated) {
            remoteDataSource.addSetToCollections(sessionManager.currentUserId, setId, collectionIds)
                .onError { return it }
        }

        return Result.Success(Unit)
    }

    override suspend fun deleteCollections(collectionIds: List<CollectionId>): EmptyResult<DataError> {
        localDataSource.deleteCollections(sessionManager.currentUserId, collectionIds)
            .onError { return it }

        if (sessionManager.isAuthenticated) {
            remoteDataSource.deleteCollections(sessionManager.currentUserId, collectionIds)
                .onError { return it }
        }

        return Result.Success(Unit)
    }

    override suspend fun removeSetFromCollections(
        setId: SetId,
        collectionIds: List<CollectionId>,
    ): EmptyResult<DataError> {
        localDataSource.removeSetFromCollections(setId, sessionManager.currentUserId, collectionIds)
            .onError { return it }

        if (sessionManager.isAuthenticated) {
            remoteDataSource.removeSetFromCollections(
                sessionManager.currentUserId,
                setId,
                collectionIds
            )
                .onError { return it }
        }

        return Result.Success(Unit)
    }

    private suspend fun saveCollections(
        userId: UserId,
        collections: List<Collection>,
    ): Result<List<CollectionId>, DataError> {
        val savedCollections = localDataSource.upsertCollections(userId, collections)
            .onError { return it }
            .data()

        if (sessionManager.isAuthenticated) {
            remoteDataSource.upsertCollections(userId, savedCollections)
                .onError { return it }
        }

        return Result.Success(savedCollections.map { it.id })
    }

    private fun Flow<Pair<UserId, Map<SetId, List<CollectionId>>>>.syncRemoteSetCollections() =
        onEach { (userId, remoteSetCollections) ->
            logger.d { "Syncing ${remoteSetCollections.size} set collections" }
            val localSetCollections = localDataSource.watchCollectionsBySets(userId)
                .firstOrDefault { emptyMap() }
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
                localDataSource.deleteCollectionsBySets(userId, setCollectionsToDelete)
            }
            if (setCollectionsToUpsert.isNotEmpty()) {
                localDataSource.upsertCollectionsBySets(userId, setCollectionsToUpsert)
            }
        }

    private fun Flow<Pair<UserId, List<Collection>>>.syncRemoteCollections() =
        onEach { (userId, remoteCollections) ->
            logger.d { "Syncing ${remoteCollections.size} collections" }
            val localCollections = localDataSource.watchCollections(userId)
                .firstOrDefault { emptyList() }
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
                localDataSource.deleteCollections(userId, collectionsToDelete)
            }
            if (collectionsToUpsert.isNotEmpty()) {
                localDataSource.upsertCollections(userId, collectionsToUpsert)
            }
        }
}
