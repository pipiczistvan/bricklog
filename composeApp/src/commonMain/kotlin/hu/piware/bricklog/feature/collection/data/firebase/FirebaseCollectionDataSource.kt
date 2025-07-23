package hu.piware.bricklog.feature.collection.data.firebase

import co.touchlab.kermit.Logger
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.FieldValue
import dev.gitlive.firebase.firestore.FirebaseFirestoreException
import dev.gitlive.firebase.firestore.firestore
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
import kotlinx.coroutines.flow.mapNotNull
import org.koin.core.annotation.Single

@Single
class FirebaseCollectionDataSource : RemoteCollectionDataSource {

    private val logger = Logger.withTag("FirebaseCollectionDataSource")

    private val firestore = Firebase.firestore

    override fun watchCollections(userId: UserId): Flow<List<Collection>> {
        return firestore.collection("user-data/$userId/collections").snapshots
            .mapNotNull { snapshot ->
                try {
                    snapshot.documents.map { document ->
                        document.data<CollectionDocument>().toDomainModel(document.id)
                    }
                } catch (e: FirebaseFirestoreException) {
                    logger.w(e) { "An error occurred while fetching collections" }
                    null
                }
            }
    }

    override fun watchCollectionsBySets(userId: UserId): Flow<Map<SetId, List<CollectionId>>> {
        return firestore.collection("user-data/$userId/set-collections").snapshots
            .mapNotNull { snapshot ->
                try {
                    snapshot.documents.flatMap { document ->
                        val setIds = document.get<List<SetId>?>("setIds")
                        setIds?.map { setId ->
                            setId to document.id
                        } ?: emptyList()
                    }.groupBy({ it.first }, { it.second })
                } catch (e: FirebaseFirestoreException) {
                    logger.w(e) { "An error occurred while fetching collections" }
                    null
                }
            }
    }

    override suspend fun upsertCollections(
        userId: UserId,
        collections: List<Collection>,
    ): EmptyResult<DataError.Remote> {
        return try {
            with(firestore) {
                batch().apply {
                    for (collection in collections) {
                        if (collection.isNew) {
                            collection("user-data/$userId/collections").add(collection.toDocument())
                            logger.d { "Collection created successfully" }
                        } else {
                            collection("user-data/$userId/collections")
                                .document(collection.id)
                                .set(
                                    data = collection.toDocument(),
                                    merge = true
                                )
                            logger.d { "Collection updated successfully" }
                        }
                    }
                    commit()
                }
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            logger.e(e) { "An error occurred while saving collection" }
            Result.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun addSetToCollections(
        userId: UserId,
        setId: SetId,
        collectionIds: List<CollectionId>,
    ): EmptyResult<DataError.Remote> {
        return try {
            with(firestore) {
                batch().apply {
                    for (collectionId in collectionIds) {
                        collection("user-data/$userId/set-collections")
                            .document(collectionId)
                            .set(
                                data = mapOf(
                                    "setIds" to FieldValue.arrayUnion(setId)
                                ),
                                merge = true
                            )
                    }
                    commit()
                }
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            logger.e(e) { "An error occurred while adding set to collections" }
            Result.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun deleteCollections(
        userId: UserId,
        collectionIds: List<CollectionId>,
    ): EmptyResult<DataError.Remote> {
        return try {
            with(firestore) {
                batch().apply {
                    for (id in collectionIds) {
                        delete(collection("user-data/$userId/set-collections").document(id))
                        delete(collection("user-data/$userId/collections").document(id))
                    }
                    commit()
                }
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            logger.e(e) { "An error occurred while deleting collection" }
            Result.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun removeSetFromCollections(
        userId: UserId,
        setId: SetId,
        collectionIds: List<CollectionId>,
    ): EmptyResult<DataError.Remote> {
        return try {
            with(firestore) {
                batch().apply {
                    for (id in collectionIds) {
                        collection("user-data/$userId/set-collections")
                            .document(id)
                            .set(
                                data = mapOf(
                                    "setIds" to FieldValue.arrayRemove(setId)
                                ),
                                merge = true
                            )
                    }
                    commit()
                }
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            logger.e(e) { "An error occurred while removing set from collections" }
            Result.Error(DataError.Remote.UNKNOWN)
        }
    }
}
