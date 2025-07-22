@file:OptIn(ExperimentalCoroutinesApi::class)

package hu.piware.bricklog.feature.collection.data.repository

import co.touchlab.kermit.Logger
import hu.piware.bricklog.feature.collection.domain.datasource.LocalCollectionDataSource
import hu.piware.bricklog.feature.collection.domain.datasource.RemoteCollectionDataSource
import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.collection.domain.model.CollectionType
import hu.piware.bricklog.feature.collection.domain.repository.CollectionRepository
import hu.piware.bricklog.feature.collection.domain.util.defaultCollections
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.domain.SyncedRepository
import hu.piware.bricklog.feature.core.domain.data
import hu.piware.bricklog.feature.core.domain.onError
import hu.piware.bricklog.feature.set.domain.model.SetId
import hu.piware.bricklog.feature.user.domain.manager.SessionManager
import hu.piware.bricklog.feature.user.domain.manager.filterAuthenticated
import hu.piware.bricklog.feature.user.domain.manager.filterGuest
import hu.piware.bricklog.feature.user.domain.model.UserId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

    override fun startSync(scope: CoroutineScope) {
        sessionManager.userId
            .filterAuthenticated()
            .flatMapLatest { userId ->
                remoteDataSource.watchUserCollections(userId).map { Pair(userId, it) }
            }
            .syncRemoteCollections()
            .createDefaultCollections()
            .flatMapLatest { (userId, _) ->
                remoteDataSource.watchUserSetCollections(userId).map { Pair(userId, it) }
            }
            .syncRemoteSetCollections()
            .launchIn(scope)

        sessionManager.userId
            .filterGuest()
            .flatMapLatest { userId ->
                localDataSource.watchUserCollections(userId).map { Pair(userId, it) }
            }
            .createDefaultCollections()
            .launchIn(scope)
    }

    override suspend fun clearLocal(): EmptyResult<DataError> {
        return localDataSource.deleteAllUserCollections(sessionManager.currentUserId)
    }

    override fun watchCollections(): Flow<List<Collection>> {
        return sessionManager.userId.flatMapLatest { userId ->
            localDataSource.watchUserCollections(userId)
        }
    }

    override fun watchCollectionsBySets(): Flow<Map<SetId, List<Collection>>> {
        return sessionManager.userId.flatMapLatest { userId ->
            localDataSource.watchUserCollectionsBySets(userId)
        }
    }

    override fun watchCollectionsBySet(setId: SetId): Flow<List<Collection>> {
        return sessionManager.userId.flatMapLatest { userId ->
            localDataSource.watchUserCollectionsBySet(userId, setId)
        }
    }

    override suspend fun deleteCollectionById(collectionId: CollectionId): EmptyResult<DataError> {
        localDataSource.deleteUserCollection(sessionManager.currentUserId, collectionId)
            .onError { return it }

        if (sessionManager.isAuthenticated) {
            remoteDataSource.deleteUserCollection(sessionManager.currentUserId, collectionId)
                .onError { return it }
        }

        return Result.Success(Unit)
    }

    override suspend fun saveCollection(collection: Collection): Result<Collection, DataError> {
        return saveCollection(sessionManager.currentUserId, collection)
    }

    private suspend fun saveCollection(
        userId: UserId,
        collection: Collection,
    ): Result<Collection, DataError> {
        localDataSource.upsertUserCollection(userId, collection)
            .onError { return it }

        if (sessionManager.isAuthenticated) {
            remoteDataSource.upsertUserCollection(userId, collection)
                .onError { return it }
        }

        return Result.Success(collection)
    }

    override suspend fun addSetToCollection(
        setId: SetId,
        collectionId: CollectionId,
    ): EmptyResult<DataError> {
        localDataSource.addSetToUserCollection(setId, sessionManager.currentUserId, collectionId)
            .onError { return it }

        if (sessionManager.isAuthenticated) {
            remoteDataSource.addSetToUserCollection(
                sessionManager.currentUserId,
                setId,
                collectionId
            )
                .onError { return it }
        }

        return Result.Success(Unit)
    }

    override suspend fun removeSetFromCollection(
        setId: SetId,
        collectionId: CollectionId,
    ): EmptyResult<DataError> {
        localDataSource.removeSetFromUserCollection(
            setId,
            sessionManager.currentUserId,
            collectionId
        )
            .onError { return it }

        if (sessionManager.isAuthenticated) {
            remoteDataSource.removeSetFromUserCollection(
                sessionManager.currentUserId,
                setId,
                collectionId
            )
                .onError { return it }
        }

        return Result.Success(Unit)
    }

    override suspend fun getCollection(collectionId: CollectionId): Result<Collection, DataError> {
        return localDataSource.getUserCollection(sessionManager.currentUserId, collectionId)
    }

    override fun watchCollection(collectionId: CollectionId): Flow<Collection> {
        return sessionManager.userId.flatMapLatest { userId ->
            localDataSource.watchUserCollection(userId, collectionId)
        }
    }

    override suspend fun getUserCollectionsByType(type: CollectionType): Result<List<Collection>, DataError> {
        return localDataSource.getUserCollectionsByType(sessionManager.currentUserId, type)
    }

    private fun Flow<Pair<UserId, Map<SetId, List<CollectionId>>>>.syncRemoteSetCollections() =
        onEach { (userId, remoteSetCollections) ->
            logger.d { "Syncing ${remoteSetCollections.size} set collections" }
            val localSetCollections = localDataSource.getUserSetCollections(userId).data()
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
                localDataSource.deleteUserSetCollections(userId, setCollectionsToDelete)
            }
            if (setCollectionsToUpsert.isNotEmpty()) {
                localDataSource.upsertUserSetCollections(userId, setCollectionsToUpsert)
            }
        }

    private fun Flow<Pair<UserId, List<Collection>>>.syncRemoteCollections() =
        onEach { (userId, remoteCollections) ->
            logger.d { "Syncing ${remoteCollections.size} collections" }
            val localCollections = localDataSource.getUserCollections(userId).data()
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
                localDataSource.deleteUserCollections(userId, collectionsToDelete)
            }
            if (collectionsToUpsert.isNotEmpty()) {
                localDataSource.upsertUserCollections(userId, collectionsToUpsert)
            }
        }

    private fun Flow<Pair<UserId, List<Collection>>>.createDefaultCollections() =
        onEach { (userId, collections) ->
            defaultCollections.forEach { defaultCollection ->
                if (collections.none { it.type == defaultCollection.type }) {
                    saveCollection(userId, defaultCollection)
                }
            }
        }
}
