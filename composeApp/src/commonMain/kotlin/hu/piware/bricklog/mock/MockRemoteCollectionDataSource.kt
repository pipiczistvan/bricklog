package hu.piware.bricklog.mock

import hu.piware.bricklog.feature.collection.domain.datasource.RemoteCollectionDataSource
import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.set.domain.model.SetId
import hu.piware.bricklog.feature.user.domain.model.UserId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class MockRemoteCollectionDataSource : RemoteCollectionDataSource {

    private val firestore = MockFirestore

    override fun watchCollections(userId: UserId): Flow<List<Collection>> {
        return firestore.userCollections.map { it[userId] ?: emptyList() }
    }

    override fun watchCollectionsBySets(userId: UserId): Flow<Map<SetId, List<CollectionId>>> {
        return firestore.setCollections.map { it[userId] ?: emptyMap() }
    }

    override suspend fun upsertCollections(
        userId: UserId,
        collections: List<Collection>,
    ): EmptyResult<DataError.Remote> {
        firestore.userCollections.update { currentMap ->
            val currentCollections = currentMap[userId].orEmpty()

            // Remove existing collections with the same ids
            val collectionIds = collections.map { it.id }.toSet()
            val filtered = currentCollections.filterNot { it.id in collectionIds }

            // Add or replace with the new ones
            val updatedCollections = filtered + collections

            currentMap + (userId to updatedCollections)
        }

        return Result.Success(Unit)
    }

    override suspend fun addSetToCollections(
        userId: UserId,
        setId: SetId,
        collectionIds: List<CollectionId>,
    ): EmptyResult<DataError.Remote> {
        firestore.setCollections.update { currentMap ->
            val userMap = currentMap[userId].orEmpty()
            val existingIds = userMap[setId].orEmpty()

            // Combine and deduplicate
            val updatedIds = (existingIds + collectionIds).distinct()

            val updatedUserMap = userMap + (setId to updatedIds)
            currentMap + (userId to updatedUserMap)
        }

        return Result.Success(Unit)
    }

    override suspend fun deleteCollections(
        userId: UserId,
        collectionIds: List<CollectionId>,
    ): EmptyResult<DataError.Remote> {
        val collectionIdSet = collectionIds.toSet()

        // 1. Remove from userCollections
        firestore.userCollections.update { currentMap ->
            val currentCollections = currentMap[userId].orEmpty()
            val updatedCollections = currentCollections.filterNot { it.id in collectionIdSet }

            if (updatedCollections.isEmpty()) {
                currentMap - userId
            } else {
                currentMap + (userId to updatedCollections)
            }
        }

        // 2. Remove from setCollections
        firestore.setCollections.update { currentMap ->
            val userMap = currentMap[userId] ?: return@update currentMap

            val updatedUserMap = userMap.mapValues { (_, ids) ->
                ids.filterNot { it in collectionIdSet }
            }.filterValues { it.isNotEmpty() }

            if (updatedUserMap.isEmpty()) {
                currentMap - userId
            } else {
                currentMap + (userId to updatedUserMap)
            }
        }

        return Result.Success(Unit)
    }

    override suspend fun removeSetFromCollections(
        userId: UserId,
        setId: SetId,
        collectionIds: List<CollectionId>,
    ): EmptyResult<DataError.Remote> {
        firestore.setCollections.update { currentMap ->
            val userMap = currentMap[userId] ?: return@update currentMap
            val currentList = userMap[setId] ?: return@update currentMap

            val updatedList = currentList - collectionIds.toSet()
            val updatedUserMap = if (updatedList.isEmpty()) {
                userMap - setId
            } else {
                userMap + (setId to updatedList)
            }

            if (updatedUserMap.isEmpty()) {
                currentMap - userId
            } else {
                currentMap + (userId to updatedUserMap)
            }
        }

        return Result.Success(Unit)
    }
}
