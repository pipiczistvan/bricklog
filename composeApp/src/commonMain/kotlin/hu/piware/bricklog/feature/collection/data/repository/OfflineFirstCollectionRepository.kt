@file:OptIn(ExperimentalCoroutinesApi::class)

package hu.piware.bricklog.feature.collection.data.repository

import co.touchlab.kermit.Logger
import hu.piware.bricklog.feature.collection.domain.datasource.LocalCollectionDataSource
import hu.piware.bricklog.feature.collection.domain.datasource.RemoteCollectionDataSource
import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.collection.domain.model.CollectionType
import hu.piware.bricklog.feature.collection.domain.repository.CollectionRepository
import hu.piware.bricklog.feature.core.domain.AccountSyncedRepository
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.asEmptyDataResult
import hu.piware.bricklog.feature.set.domain.model.SetId
import hu.piware.bricklog.feature.user.domain.manager.SessionManager
import hu.piware.bricklog.feature.user.domain.manager.filterAuthenticated
import hu.piware.bricklog.feature.user.domain.manager.isAuthenticated
import hu.piware.bricklog.feature.user.domain.model.UserId
import hu.piware.bricklog.util.asResultOrNull
import hu.piware.bricklog.util.firstOrDefault
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import org.koin.core.annotation.Single

@Single
class OfflineFirstCollectionRepository(
    private val localDataSource: LocalCollectionDataSource,
    private val remoteDataSource: RemoteCollectionDataSource,
    private val sessionManager: SessionManager,
) : CollectionRepository, AccountSyncedRepository {

    private val logger = Logger.withTag("OfflineFirstCollectionRepository")

    private var syncJob: Job? = null

    override fun startSync(scope: CoroutineScope) {
        syncJob?.cancel()
        syncJob = sessionManager.userId
            .filterAuthenticated()
            .syncRemoteCollections()
            .syncRemoteCollectionSets()
            .launchIn(scope)
    }

    override suspend fun clearLocalData(userId: UserId): EmptyResult<DataError> {
        return localDataSource.deleteUserCollections(userId)
    }

    override fun watchCollection(collectionId: CollectionId): Flow<Collection?> {
        return localDataSource.watchCollection(collectionId)
    }

    override fun watchCollections(
        userId: UserId,
        type: CollectionType?,
        setId: SetId?,
    ): Flow<List<Collection>> {
        return localDataSource.watchUserAndSharedCollections(userId, type, setId)
    }

    override suspend fun saveCollections(
        userId: UserId,
        collections: List<Collection>,
    ): EmptyResult<DataError> {
        return if (userId.isAuthenticated) {
            remoteDataSource.upsertCollections(collections)
        } else {
            localDataSource.upsertCollections(collections)
        }
    }

    override suspend fun addSetToCollections(
        userId: UserId,
        setId: SetId,
        collectionIds: List<CollectionId>,
    ): EmptyResult<DataError> {
        return if (userId.isAuthenticated) {
            remoteDataSource.addSetToCollections(setId, collectionIds)
        } else {
            localDataSource.addSetToCollections(setId, collectionIds)
        }
    }

    override suspend fun deleteCollections(
        userId: UserId,
        collectionIds: List<CollectionId>,
    ): EmptyResult<DataError> {
        return if (userId.isAuthenticated) {
            remoteDataSource.deleteCollections(collectionIds)
        } else {
            localDataSource.deleteCollections(collectionIds)
        }
    }

    override suspend fun removeSetFromCollections(
        userId: UserId,
        setId: SetId,
        collectionIds: List<CollectionId>,
    ): EmptyResult<DataError> {
        return if (userId.isAuthenticated) {
            remoteDataSource.removeSetFromCollections(setId, collectionIds)
        } else {
            localDataSource.removeSetFromCollections(setId, collectionIds)
        }
    }

    override suspend fun forceSyncRemoteCollections(userId: UserId): EmptyResult<DataError> {
        return flowOf(userId)
            .syncRemoteCollections()
            .asResultOrNull()
            .asEmptyDataResult()
    }

    private fun Flow<UserId>.syncRemoteCollections() =
        flatMapLatest { userId ->
            remoteDataSource.watchUserAndSharedCollections(userId).map { Pair(userId, it) }
        }.onEach { (userId, remoteCollections) ->
            logger.d { "Syncing ${remoteCollections.size} collections for user $userId" }
            val localCollections = localDataSource.watchUserAndSharedCollections(userId)
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
                localDataSource.deleteCollections(collectionsToDelete)
            }
            if (collectionsToUpsert.isNotEmpty()) {
                localDataSource.upsertCollections(collectionsToUpsert)
            }
            logger.d { "Deleted ${collectionsToDelete.size} and upserted ${collectionsToUpsert.size} collections for user $userId" }
        }.map { it.first }

    private fun Flow<UserId>.syncRemoteCollectionSets() =
        flatMapLatest { userId ->
            remoteDataSource.watchUserAndSharedCollectionsBySets(userId).map { Pair(userId, it) }
        }.onEach { (userId, remoteSetCollections) ->
            logger.d { "Syncing ${remoteSetCollections.size} set collections for user $userId" }
            val localSetCollections = localDataSource.watchUserAndSharedCollectionsBySets(userId)
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
                localDataSource.deleteCollectionsBySets(setCollectionsToDelete)
            }
            if (setCollectionsToUpsert.isNotEmpty()) {
                localDataSource.upsertCollectionsBySets(setCollectionsToUpsert)
            }
            logger.d {
                "Deleted ${setCollectionsToDelete.size} and upserted ${setCollectionsToUpsert.size} set collections for user $userId"
            }
        }.map { it.first }
}
