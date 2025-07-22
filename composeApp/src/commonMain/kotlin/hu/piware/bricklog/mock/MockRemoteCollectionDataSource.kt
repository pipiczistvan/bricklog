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

    override fun watchUserCollections(userId: UserId): Flow<List<Collection>> {
        return firestore.userCollections.map { it[userId] ?: emptyList() }
    }

    override fun watchUserSetCollections(userId: UserId): Flow<Map<SetId, List<CollectionId>>> {
        return firestore.setCollections.map { it[userId] ?: emptyMap() }
    }

    override suspend fun deleteUserCollection(
        userId: UserId,
        collectionId: CollectionId,
    ): EmptyResult<DataError.Remote> {
        firestore.userCollections.update { currentMap ->
            val updatedUserCollections = currentMap[userId]
                ?.filterNot { it.id == collectionId }
                ?: return@update currentMap // No change if user or collection not found

            currentMap + (userId to updatedUserCollections)
        }
        return Result.Success(Unit)
    }

    override suspend fun upsertUserCollection(
        userId: UserId,
        collection: Collection,
    ): EmptyResult<DataError.Remote> {
        firestore.userCollections.update { currentMap ->
            val currentCollections = currentMap[userId].orEmpty()
            val updatedCollections = currentCollections
                .filterNot { it.id == collection.id } + collection

            currentMap + (userId to updatedCollections)
        }
        return Result.Success(Unit)
    }

    override suspend fun addSetToUserCollection(
        userId: UserId,
        setId: SetId,
        collectionId: CollectionId,
    ): EmptyResult<DataError.Remote> {
        firestore.setCollections.update { currentMap ->
            val userMap = currentMap[userId].orEmpty()
            val collectionIds = userMap[setId].orEmpty()

            // Avoid duplicates
            if (collectionId in collectionIds) return@update currentMap

            val updatedCollectionIds = collectionIds + collectionId
            val updatedUserMap = userMap + (setId to updatedCollectionIds)

            currentMap + (userId to updatedUserMap)
        }

        return Result.Success(Unit)
    }

    override suspend fun removeSetFromUserCollection(
        userId: UserId,
        setId: SetId,
        collectionId: CollectionId,
    ): EmptyResult<DataError.Remote> {
        firestore.setCollections.update { currentMap ->
            val userMap = currentMap[userId] ?: return@update currentMap
            val collectionIds = userMap[setId] ?: return@update currentMap

            val updatedCollectionIds = collectionIds - collectionId
            val updatedUserMap = if (updatedCollectionIds.isEmpty()) {
                userMap - setId
            } else {
                userMap + (setId to updatedCollectionIds)
            }

            val finalUserMap = if (updatedUserMap.isEmpty()) {
                currentMap - userId
            } else {
                currentMap + (userId to updatedUserMap)
            }

            finalUserMap
        }

        return Result.Success(Unit)
    }
}
