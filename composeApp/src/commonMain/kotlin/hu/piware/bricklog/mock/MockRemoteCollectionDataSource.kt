@file:OptIn(ExperimentalUuidApi::class)

package hu.piware.bricklog.mock

import hu.piware.bricklog.feature.collection.domain.datasource.RemoteCollectionDataSource
import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.collection.domain.model.isNew
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.set.domain.model.SetId
import hu.piware.bricklog.feature.user.domain.model.UserId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class MockRemoteCollectionDataSource : RemoteCollectionDataSource {

    private val firestore = MockFirestore

    override fun watchUserAndSharedCollections(userId: UserId): Flow<List<Collection>> {
        return firestore.collections.map { collections ->
            collections.filter {
                it.owner == userId || it.shares.containsKey(userId)
            }
        }
    }

    override fun watchUserAndSharedCollectionsBySets(userId: UserId): Flow<Map<SetId, List<CollectionId>>> {
        return firestore.userSetCollections.map { it[userId] ?: emptyMap() }
    }

    override suspend fun upsertCollections(
        collections: List<Collection>,
    ): EmptyResult<DataError.Remote> {
        val preparedCollections = collections.map { collection ->
            collection.copy(
                id = if (collection.isNew) Uuid.random().toString() else collection.id,
            )
        }

        firestore.collections.update { currentCollections ->
            val updateCollectionIds = preparedCollections.mapTo(mutableSetOf()) { it.id }
            currentCollections.filterNot { it.id in updateCollectionIds } + preparedCollections
        }

        return Result.Success(Unit)
    }

    override suspend fun addSetToCollections(
        setId: SetId,
        collectionIds: List<CollectionId>,
    ): EmptyResult<DataError.Remote> {
        // Update userSetCollections mapping
        firestore.userSetCollections.update { currentMap ->
            val updatedMap = currentMap.toMutableMap()

            // For each collection, find all users who have access to it
            val collectionToUsers = mutableMapOf<CollectionId, MutableSet<UserId>>()

            firestore.collections.value.forEach { collection ->
                if (collection.id in collectionIds) {
                    val users = mutableSetOf<UserId>()
                    users.add(collection.owner)
                    users.addAll(collection.shares.keys)
                    collectionToUsers[collection.id] = users
                }
            }

            // Update the mapping for each user
            collectionToUsers.forEach { (collectionId, users) ->
                users.forEach { userId ->
                    val userMap = updatedMap[userId]?.toMutableMap() ?: mutableMapOf()
                    val existingCollections = userMap[setId]?.toMutableList() ?: mutableListOf()

                    if (collectionId !in existingCollections) {
                        existingCollections.add(collectionId)
                        userMap[setId] = existingCollections
                        updatedMap[userId] = userMap
                    }
                }
            }

            updatedMap
        }

        return Result.Success(Unit)
    }

    override suspend fun deleteCollections(
        collectionIds: List<CollectionId>,
    ): EmptyResult<DataError.Remote> {
        val collectionIdSet = collectionIds.toSet()

        // 1. Remove collections from the main collections list
        firestore.collections.update { currentCollections ->
            currentCollections.filterNot { it.id in collectionIdSet }
        }

        // 2. Remove from userSetCollections mapping
        firestore.userSetCollections.update { currentMap ->
            val updatedMap = currentMap.toMutableMap()

            updatedMap.forEach { (userId, userMap) ->
                val updatedUserMap = userMap.mapValues { (setId, collectionIds) ->
                    collectionIds.filterNot { it in collectionIdSet }
                }.filterValues { it.isNotEmpty() }

                if (updatedUserMap.isEmpty()) {
                    updatedMap.remove(userId)
                } else {
                    updatedMap[userId] = updatedUserMap
                }
            }

            updatedMap
        }

        return Result.Success(Unit)
    }

    override suspend fun removeSetFromCollections(
        setId: SetId,
        collectionIds: List<CollectionId>,
    ): EmptyResult<DataError.Remote> {
        // Update userSetCollections mapping to remove the setId from the specified collections
        firestore.userSetCollections.update { currentMap ->
            val updatedMap = currentMap.toMutableMap()

            updatedMap.forEach { (userId, userMap) ->
                val updatedUserMap = userMap.toMutableMap()
                val existingCollections = updatedUserMap[setId]?.toMutableList()

                if (existingCollections != null) {
                    // Remove the specified collection IDs from this set's collections
                    existingCollections.removeAll(collectionIds.toSet())

                    if (existingCollections.isEmpty()) {
                        // If no collections remain for this set, remove the set entirely
                        updatedUserMap.remove(setId)
                    } else {
                        // Update with the remaining collections
                        updatedUserMap[setId] = existingCollections
                    }

                    if (updatedUserMap.isEmpty()) {
                        // If no sets remain for this user, remove the user entirely
                        updatedMap.remove(userId)
                    } else {
                        updatedMap[userId] = updatedUserMap
                    }
                }
            }

            updatedMap
        }

        return Result.Success(Unit)
    }
}
