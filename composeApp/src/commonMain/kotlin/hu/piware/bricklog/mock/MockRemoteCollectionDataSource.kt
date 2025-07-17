package hu.piware.bricklog.mock

import hu.piware.bricklog.feature.collection.domain.datasource.RemoteCollectionDataSource
import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.set.domain.model.SetId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MockRemoteCollectionDataSource : RemoteCollectionDataSource {

    private var collections = MutableStateFlow(listOf<Collection>())
    private var setCollections = MutableStateFlow(mapOf<SetId, List<Collection>>())

    override fun watchCollections(userId: String): Flow<List<Collection>> {
        return collections.asStateFlow()
    }

    override fun watchSetCollections(userId: String): Flow<Map<SetId, List<Collection>>> {
        return setCollections.asStateFlow()
    }

    override suspend fun deleteCollection(
        userId: String,
        id: CollectionId,
    ): EmptyResult<DataError.Remote> {
        collections.update { it.filter { it.id != id } }
        return Result.Success(Unit)
    }

    override suspend fun upsertCollection(
        userId: String,
        collection: Collection,
    ): EmptyResult<DataError.Remote> {
        collections.update { currentCollections ->
            currentCollections.filter { it.id != collection.id } + collection
        }
        return Result.Success(Unit)
    }

    override suspend fun addSetToCollection(
        userId: String,
        setId: SetId,
        collectionId: CollectionId,
    ): EmptyResult<DataError.Remote> {
        val collection = collections.value.find { it.id == collectionId }

        if (collection == null) {
            return Result.Error(DataError.Remote.UNKNOWN)
        }

        setCollections.update { currentMap ->
            val currentCollections = currentMap[setId] ?: emptyList()

            // Check if the collection is already present
            val updatedCollections = if (currentCollections.any { it.id == collectionId }) {
                currentCollections // No change
            } else {
                currentCollections + collection
            }

            currentMap + (setId to updatedCollections)
        }

        return Result.Success(Unit)
    }

    override suspend fun removeSetFromCollection(
        userId: String,
        setId: SetId,
        collectionId: CollectionId,
    ): EmptyResult<DataError.Remote> {
        val collection = collections.value.find { it.id == collectionId }

        if (collection == null) {
            return Result.Error(DataError.Remote.UNKNOWN)
        }

        setCollections.update { currentMap ->
            val currentCollections = currentMap[setId] ?: emptyList()

            // Check if the collection is already present
            val updatedCollections = if (currentCollections.none { it.id == collectionId }) {
                currentCollections // No change
            } else {
                currentCollections - collection
            }

            currentMap + (setId to updatedCollections)
        }

        return Result.Success(Unit)
    }
}
